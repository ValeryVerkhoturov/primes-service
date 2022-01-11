package com.company.primenumbers.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Jobs {

    ConcurrentHashMap<UUID, Future<Optional<PrimeNumber>>> jobs = new ConcurrentHashMap<>();

    PrimeUtils primeUtils;

    public UUID addJob(Long requested) {
        UUID uuid = UUID.randomUUID();
        jobs.put(uuid, primeUtils.maxEqualOrBelowPrime(requested));
        return uuid;
    }

    public boolean containsJob(UUID uuid) {
        return jobs.containsKey(uuid);
    }

    @Nullable
    public PrimeNumber getResult(UUID uuid) throws ExecutionException, InterruptedException {
        Future<Optional<PrimeNumber>> future = jobs.get(uuid);
        if (future != null && future.isDone())
            return future.get().orElse(null);

        return null;
    }
}
