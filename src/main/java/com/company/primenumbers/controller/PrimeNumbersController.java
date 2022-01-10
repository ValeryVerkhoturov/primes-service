package com.company.primenumbers.controller;

import com.company.primenumbers.domain.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrimeNumbersController {

    PrimeUtils primeUtils;

    Jobs jobs;

    @GetMapping(value = "/{number}")
    public BaseResponse baseResponse(@PathVariable Long number) throws ExecutionException, InterruptedException {
        PrimeNumber primeNumber = primeUtils.maxEqualOrBelowPrime(number).get().orElse(null);

        if (primeNumber != null)
            return new BaseResponse(number, primeNumber.getNumber());
        return new BaseResponse(number, null);
    }

    @PostMapping(value = "/{number}")
    public DelayedResponse delayedResponse(@PathVariable Long number) {
        return new DelayedResponse(jobs.addJob(number));
    }

    @GetMapping(value = "/result/{job-id}")
    public StatusResponse statusResponse(@PathVariable("job-id") UUID uuid) throws ExecutionException, InterruptedException {
        if (!jobs.containsJob(uuid))
            return new StatusResponse(Status.notExists);

        PrimeNumber primeNumber = jobs.getResult(uuid);
        if (primeNumber == null)
            return new StatusResponse(Status.pending);
        else
            return new StatusResponse(Status.ready, primeNumber.getNumber());
    }
}
