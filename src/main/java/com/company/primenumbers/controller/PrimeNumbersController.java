package com.company.primenumbers.controller;

import com.company.primenumbers.domain.BaseResponse;
import com.company.primenumbers.domain.PrimeNumber;
import com.company.primenumbers.domain.PrimeUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrimeNumbersController {

    PrimeUtils primeUtils;

    @GetMapping(value = "/{number}")
    public BaseResponse baseResponse(@PathVariable Long number) {
        PrimeNumber primeNumber = primeUtils.maxPrimeEqualsOrBelow(number).orElse(null);

        if (primeNumber != null)
            return new BaseResponse(number, primeNumber.getNumber());
        return new BaseResponse(number, null);
    }


}
