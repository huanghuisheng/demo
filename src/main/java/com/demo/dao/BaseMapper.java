package com.demo.dao;

import java.math.BigDecimal;



public interface BaseMapper<T> {
    int insert(T record);
}
