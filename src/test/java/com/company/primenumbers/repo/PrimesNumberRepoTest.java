package com.company.primenumbers.repo;

import com.company.primenumbers.domain.PrimeNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.LongStream;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrimesNumberRepoTest {

    @Autowired
    PrimeNumberRepo primeNumberRepo;

    long minNumber = 0;
    long maxNumber = 100;

    @BeforeAll
    public void saveNumbers() {
        List<PrimeNumber> primeNumberList = LongStream.rangeClosed(minNumber, maxNumber)
                .mapToObj(PrimeNumber::new)
                .toList();
        primeNumberRepo.saveAll(primeNumberList);
    }

    @Test
    public void maxPrimeNumber() {
        Long expected = maxNumber;
        Long actual = primeNumberRepo.maxPrimeNumber();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void maxPrimeBelow() {
        Long expected = primeNumberRepo.findAll().stream()
                .mapToLong(PrimeNumber::getNumber)
                .filter(n -> n <= 50)
                .max()
                .orElseThrow();
        Long actual = primeNumberRepo.maxPrimeBelow(expected);

        Assertions.assertEquals(expected, actual);
    }
}
