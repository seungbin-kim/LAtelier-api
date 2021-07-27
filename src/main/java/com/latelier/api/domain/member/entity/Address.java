package com.latelier.api.domain.member.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private Address(final String address,
                    final String zipCode) {

        this.address = address;
        this.zipCode = zipCode;
    }


    @Column(length = 100)
    private String address;

    @Column(length = 10)
    private String zipCode;

}
