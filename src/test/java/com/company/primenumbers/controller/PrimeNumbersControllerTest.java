package com.company.primenumbers.controller;

import com.company.primenumbers.domain.Status;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PrimeNumbersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void returnBaseRequest() throws Exception {
        long requestedNumber = 12L;
        long expectedResultPrime = 11L;
        mockMvc.perform(MockMvcRequestBuilders.get("/" + requestedNumber))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(MockMvcResultMatchers.jsonPath("$.requested-x").value(requestedNumber),
                        MockMvcResultMatchers.jsonPath("$.prime-number").value(expectedResultPrime));
    }

    @Test
    public void returnDelayedResponse() throws Exception {
        String UUIDPattern = "\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b";
        mockMvc.perform(MockMvcRequestBuilders.post("/15"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.job-id", MatchesPattern.matchesPattern(UUIDPattern)));
    }

    @Test
    public void returnPendingResponse() throws Exception {
        long requestedNumber = 500L;
        long expectedResultPrime = 499L;
        MvcResult postResoult = mockMvc.perform(MockMvcRequestBuilders.post("/" + requestedNumber))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String uuid = JsonPath.read(postResoult.getResponse().getContentAsString(), "$.job-id");

        mockMvc.perform(MockMvcRequestBuilders.get("/result/" + uuid))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(MockMvcResultMatchers.jsonPath("$.status").value(Status.pending.name()),
                        MockMvcResultMatchers.jsonPath("$.prime-number").doesNotExist());

        TimeUnit.SECONDS.sleep(20);

        mockMvc.perform(MockMvcRequestBuilders.get("/result/" + uuid))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(MockMvcResultMatchers.jsonPath("$.status").value(Status.ready.name()),
                        MockMvcResultMatchers.jsonPath("$.prime-number").value(expectedResultPrime));
    }

    @Test
    public void notExistingStatusResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/result/" + UUID.randomUUID()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpectAll(MockMvcResultMatchers.jsonPath("$.status").value(Status.notExists.name()),
                        MockMvcResultMatchers.jsonPath("$.prime-number").doesNotExist());
    }
}
