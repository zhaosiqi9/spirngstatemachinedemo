package com.statemachinedemo.business;

import lombok.Getter;

import java.io.Serializable;

/**
 * @date 2025-03-11 15:18:49
 */
@Getter
public enum ShelveEvent implements Serializable {
    SHELVE_START,
    SHELVE_SUCCESS,
    SHELVE_FAILURE,
    ;
}
