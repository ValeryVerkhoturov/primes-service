package com.company.primenumbers.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BaseResponse(
        @JsonProperty("requested-x") Long requestedX,
        @JsonProperty("prime-number") Long primeNumber) {
}
