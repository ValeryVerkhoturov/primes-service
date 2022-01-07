package com.company.primenumbers.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PrimeNumber {

    @Id
    private Long number;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PrimeNumber that = (PrimeNumber) o;
        return number != null && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
