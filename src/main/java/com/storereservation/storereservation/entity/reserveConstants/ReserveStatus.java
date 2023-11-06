package com.storereservation.storereservation.entity.reserveConstants;

public enum ReserveStatus {
    WAIT,
    APPROVE,
    REJECT;

    public static final ReserveStatus DEFAULT = WAIT;
}
