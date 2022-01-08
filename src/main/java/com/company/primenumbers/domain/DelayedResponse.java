package com.company.primenumbers.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record DelayedResponse(@JsonProperty("job-id") UUID jobId) {
}
