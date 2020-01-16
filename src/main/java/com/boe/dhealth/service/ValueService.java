package com.boe.dhealth.service;

import com.boe.dhealth.dao.ValueDao;
import com.boe.dhealth.domain.ValueBO;
import com.boe.dhealth.service.util.OSSClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ValueService {
    @Autowired
    private ValueDao valueDao;

    public ValueBO getValue(){
        return valueDao.getValue();
    }


    public static void main(String[] args){
        OSSClientUtil  oss=new OSSClientUtil();
        System.out.println(oss.hashCode());
    }
}
