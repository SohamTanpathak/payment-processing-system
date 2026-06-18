package com.sohamtanpathak.fintech.payment_processing_system.common.entity;


import jakarta.persistence.Embeddable;

@Embeddable  // with hEmbeddable we can create class that can be used in different tables for reuse purpose
public class Money {

    private int amountUnits;

    private String currency;

    private Money(int amountUnits, String currency){
        this.amountUnits = amountUnits;
        this.currency = currency;
    }

    public static Money of(int amountUnits, String currency){
        return new Money(amountUnits, currency);
    }

    public static Money inr(int amountUnits){
        return new Money(amountUnits, "INR");
    }

    public Money add(Money other){
        if(!this.currency.equals(other.currency)){
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }

        return new Money(this.amountUnits + other.amountUnits, this.currency);
    }

    public Money subtract(Money other){
        if(!this.currency.equals(other.currency)){
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }

        return new Money(this.amountUnits - other.amountUnits, this.currency);
    }
}
