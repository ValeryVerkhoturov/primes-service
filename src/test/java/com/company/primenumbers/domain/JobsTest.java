package com.company.primenumbers.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class JobsTest {

    @Autowired
    private Jobs jobs;

//    @Test
//    public void addJob() throws ExecutionException, InterruptedException {
//        long expected = 3L;
//
//        UUID uuid = jobs.addJob(3L);
//        TimeUnit.MILLISECONDS.sleep(200);
//        Long actual = Objects.requireNonNull(jobs.getResult(uuid)).getNumber();
//        Assertions.assertEquals(expected, actual);
//    }
}
