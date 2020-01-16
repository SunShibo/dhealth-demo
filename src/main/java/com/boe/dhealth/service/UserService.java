package com.boe.dhealth.service;

import com.boe.dhealth.dao.UserDao;
import com.boe.dhealth.domain.AssessmentResultBO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class UserService {
    @Resource
    private UserDao userDao;

    public AssessmentResultBO getAssess(String ut){
        return userDao.getAssess(ut);
    }

    public void addAssess(AssessmentResultBO assessmentResultBO){
        userDao.addAssess(assessmentResultBO);
    }

    public void updateAssess(AssessmentResultBO assessmentResultBO){
        userDao.updateAssess(assessmentResultBO);
    }


}
