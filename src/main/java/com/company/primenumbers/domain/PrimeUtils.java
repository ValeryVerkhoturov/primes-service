package com.company.primenumbers.domain;

import com.company.primenumbers.domain.MaxRequestedNumber;
import com.company.primenumbers.domain.PrimeNumber;
import com.company.primenumbers.repo.PrimeNumberRepo;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.LongStream;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrimeUtils {

    long MIN_PRIME_VALUE = 2;

    PrimeNumberRepo primeNumberRepo;

    MaxRequestedNumber maxRequestedNumber;

    public Optional<PrimeNumber> maxPrimeEqualsOrBelow(@NonNull Long request) {
        if (MIN_PRIME_VALUE > request)
            return Optional.empty();

        Long maxRequested = maxRequestedNumber.get();
        if (Objects.nonNull(maxRequested) && maxRequested >= request)
            return Optional.of(new PrimeNumber(primeNumberRepo.maxPrimeBelow(request)));

        List<PrimeNumber> primesToInsert = primesBetween(maxRequested, request);
        primeNumberRepo.saveAll(primesToInsert);
        maxRequestedNumber.update(request);

        return primesToInsert.stream().max(Comparator.comparing(PrimeNumber::getNumber));
    }

    /**
     * from < to
     * @param from included
     * @param to included
     * @return
     */
    private List<PrimeNumber> primesBetween(@Nullable Long from, @NonNull Long to) {
        if (from == null || from < MIN_PRIME_VALUE)
            from = MIN_PRIME_VALUE;
        Map<Long, Boolean> primes = new HashMap<>();
        for (long i = from; i <= to; ++i) {
            Boolean isPrime = primes.get(i);
            if (isPrime == null || isPrime)
                for (long j = MIN_PRIME_VALUE; j <= to; ++j) {
                    primes.put(i * j, false);
                }
        }
        return LongStream.range(from, to + 1)
                .filter(n -> !primes.containsKey(n))
                .mapToObj(PrimeNumber::new).toList();
    }
}
