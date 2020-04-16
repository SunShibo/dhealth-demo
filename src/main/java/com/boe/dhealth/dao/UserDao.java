package com.boe.dhealth.dao;


import com.boe.dhealth.domain.AssessmentResultBO;
import com.boe.dhealth.domain.AssessmentResultBO2;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserDao {

    AssessmentResultBO getAssess(String ut);

    void addAssess(AssessmentResultBO assessmentResultBO);

    void updateAssess(AssessmentResultBO assessmentResultBO);

    //查询列表
    List<AssessmentResultBO2> getList(String code);
    //查询详情
    AssessmentResultBO getInfo(Integer id);

}
