package com.company.primenumbers.repo;

import com.company.primenumbers.domain.PrimeNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface PrimeNumberRepo extends JpaRepository<PrimeNumber, Long> {

    @Query("select max(pn.number) from PrimeNumber pn")
    @Nullable
    Long maxPrimeNumber();

    @Query("select max(pn.number) from PrimeNumber pn where ?1 >= pn.number")
    @Nullable
    Long maxPrimeBelow(Long number);
}
