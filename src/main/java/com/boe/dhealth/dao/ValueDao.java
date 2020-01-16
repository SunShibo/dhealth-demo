package com.boe.dhealth.dao;


import com.boe.dhealth.domain.ValueBO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ValueDao {
    ValueBO getValue();
}
