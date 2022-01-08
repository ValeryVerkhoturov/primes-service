package com.company.primenumbers.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Jobs {

    ExecutorService executorService = Executors.newCachedThreadPool();

    ConcurrentHashMap<UUID, Future<PrimeNumber>> jobs = new ConcurrentHashMap<>();

    PrimeUtils primeUtils;

    public UUID addJob(Long requested) {
        UUID uuid = UUID.randomUUID();
        jobs.put(uuid, executorService.submit(() -> primeUtils.maxEqualOrBelowPrime(requested).orElse(null)));
        return uuid;
    }

    @Nullable
    public PrimeNumber getResult(UUID uuid) throws ExecutionException, InterruptedException {
        Future<PrimeNumber> future = jobs.get(uuid);
        if (future != null && future.isDone())
            return future.get();

        return null;
    }
}
