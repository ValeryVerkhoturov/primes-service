package com.company.primenumbers.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusResponse {
    @JsonProperty(value = "status")
    @NonNull
    Status status;

    @JsonProperty(value = "prime-number")
    @Nullable
    Long prime;
}
