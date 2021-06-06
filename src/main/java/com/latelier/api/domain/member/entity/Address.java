package com.latelier.api.domain.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Builder
    private Address(final String address, final String zip) {
        this.address = address;
        this.zip = zip;
    }


    @Column(length = 100)
    private String address;

    @Column(length = 10)
    private String zip;

}
