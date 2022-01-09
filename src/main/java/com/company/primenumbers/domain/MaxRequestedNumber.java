package com.company.primenumbers.domain;

import com.company.primenumbers.repo.PrimeNumberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class MaxRequestedNumber {

    private final AtomicLong atomicLong;

    @Autowired
    public MaxRequestedNumber(PrimeNumberRepo primeNumberRepo) {
        Long maxPrimeNumber = primeNumberRepo.maxPrimeNumber();
        if (maxPrimeNumber != null)
            atomicLong = new AtomicLong(maxPrimeNumber);
        else
            atomicLong = new AtomicLong();
    }

    public Long get() {
        return atomicLong.get();
    }

    public void update(@NonNull Long value) {
        if (value > get())
            atomicLong.set(value);
    }
}
