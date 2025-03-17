package com.dima.entity;

import java.io.Serializable;

public interface BaseEntity <T extends Serializable> {

    T get();

    void setId(T id);
}
