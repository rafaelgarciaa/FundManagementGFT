package com.fondosGFT.fondosGFT.util.enums;

/**
 * Defines the types of financial transactions that can occur within the investment fund system.
 * This enum categorizes operations, such as clients adding funds to an investment
 * or withdrawing from one.
 */
public enum TransactionType {
    /**
     * Represents a transaction where a client invests money into a fund.
     */
    SUSCRIPTION,
    /**
     * Represents a transaction where a client withdraws their investment from a fund.
     */
    CANCELATION
}