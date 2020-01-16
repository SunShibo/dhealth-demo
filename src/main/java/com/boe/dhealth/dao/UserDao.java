package com.boe.dhealth.dao;


import com.boe.dhealth.domain.AssessmentResultBO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserDao {

    AssessmentResultBO getAssess(String ut);

    void addAssess(AssessmentResultBO assessmentResultBO);

    void updateAssess(AssessmentResultBO assessmentResultBO);

}
