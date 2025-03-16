package com.statemachinedemo.business;

import lombok.Getter;

import java.io.Serializable;

/**
 * @date 2025-03-11 15:16:45
 */
@Getter
public enum ShelveState implements Serializable {
    NEW,
    EXECUTING,
    SUCCESS,
    FAILED,
    ;
}
