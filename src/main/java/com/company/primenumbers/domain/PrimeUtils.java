package com.company.primenumbers.domain;

import com.company.primenumbers.repo.PrimeNumberRepo;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Service
@EnableAsync
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrimeUtils {

    int MIN_PRIME_VALUE = 2;

    PrimeNumberRepo primeNumberRepo;

    MaxRequestedNumber maxRequestedNumber;

    @Async
    public Future<Optional<PrimeNumber>> maxEqualOrBelowPrime(@NonNull Long request) {
        if (MIN_PRIME_VALUE > request)
            return new AsyncResult<>(Optional.empty());

        Long maxRequested = maxRequestedNumber.get();
        if (Objects.nonNull(maxRequested) && maxRequested >= request)
            return new AsyncResult<>(Optional.of(new PrimeNumber(primeNumberRepo.maxPrimeBelow(request))));

        List<PrimeNumber> primesToInsert = primesBetween(maxRequested, request);

        synchronized (this) {
            Long maxPrime = primeNumberRepo.maxPrimeBelow(request);
            if (maxPrime != null)
                primesToInsert = primesToInsert.stream()
                        .filter(primeNumber -> maxPrime < primeNumber.getNumber())
                        .toList();
            primeNumberRepo.saveAll(primesToInsert);
            maxRequestedNumber.update(request);
        }

        return new AsyncResult<>(primesToInsert.stream().max(Comparator.comparing(PrimeNumber::getNumber)));
    }

    /**
     * from < to
     * @param from included
     * @param to included
     * @return prime numbers
     */
    private List<PrimeNumber> primesBetween(@Nullable Long from, @NonNull Long to) {
        if (from == null || from < MIN_PRIME_VALUE)
            from = (long) MIN_PRIME_VALUE;

        List<Long> primes;

        if (Integer.MAX_VALUE < to)
            primes = findPrimesByBruteForce(from, to);
        else
            primes = findPrimesBySieveOfEratosthenes(from.intValue(), to.intValue());

        return primes.stream().map(PrimeNumber::new).toList();
    }

    private List<Long> findPrimesBySieveOfEratosthenes(int from, int to) {
        boolean[] primes = new boolean[to + 1];
        Arrays.fill(primes, true);
        primes[0] = primes[1] = false;
        for (int i = MIN_PRIME_VALUE; i < primes.length; ++i) {
            if (primes[i])
                for (int j = MIN_PRIME_VALUE; i * j < primes.length; ++j) {
                    primes[i * j] = false;
                }
        }
        return IntStream.rangeClosed(from, to).filter(i -> primes[i]).mapToObj(i -> (long) i).toList();
    }

    private List<Long> findPrimesByBruteForce(@NonNull Long from, @NonNull Long to) {
        return LongStream.rangeClosed(from, to)
                .filter(this::isPrimeByBruteForce)
                .boxed()
                .toList();
    }

    private boolean isPrimeByBruteForce(Long number) {
        boolean bool = LongStream.rangeClosed(MIN_PRIME_VALUE, (long) (Math.sqrt(number)))
                .noneMatch(n -> number % n == 0);
        System.out.println(number + " " + bool);
        return bool;
    }
}
