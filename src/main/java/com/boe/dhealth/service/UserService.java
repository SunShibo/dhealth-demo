package com.boe.dhealth.service;

import com.boe.dhealth.dao.UserDao;
import com.boe.dhealth.domain.AssessmentResultBO;
import com.boe.dhealth.domain.AssessmentResultBO2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService {
    @Resource
    private UserDao userDao;

    public AssessmentResultBO getAssess(String ut) {
        return userDao.getAssess(ut);
    }

    public void addAssess(AssessmentResultBO assessmentResultBO) {
        userDao.addAssess(assessmentResultBO);
    }

    public void updateAssess(AssessmentResultBO assessmentResultBO) {
        userDao.updateAssess(assessmentResultBO);
    }

    //查询列表
    public List<AssessmentResultBO2> getList(String code, String userKey) {
        List<AssessmentResultBO2> list;
        if (!StringUtils.isEmpty(userKey)) {
            list = userDao.getList(userKey);
        } else {
            list = userDao.getList(code.substring(3));
        }

        for (AssessmentResultBO2 as : list) {
            //时间转换
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //String dateString = formatter.format(new Date(as.getCreateTime()));
            as.setCreateTimeStr(as.getCreateTime());
            as.setHighRiskCount(0);
            //查询高位风险项
            AssessmentResultBO assessmentResultBO = userDao.getInfo(as.getId());
            if (assessmentResultBO == null || assessmentResultBO.getAnalysis() == null || !"yes".equals(assessmentResultBO.getStatus())) {
                continue;
            }
            int highRiskCount = 0;
            //驼背---
            if (assessmentResultBO.getHunchback() != null) {
                if (assessmentResultBO.getHunchback() > 50) {
                    highRiskCount++;
                }
            }
            //脊柱侧弯-右---
            if(assessmentResultBO.getScoliosisR()!=null) {
                if (Double.parseDouble(assessmentResultBO.getScoliosisR()) > 50) {
                    highRiskCount++;
                }
            }
            //盆骨侧倾-左---
            if(assessmentResultBO.getPelvicTiltL()!=null) {
                if (Double.parseDouble(assessmentResultBO.getPelvicTiltL()) > 50) {
                    highRiskCount++;
                }
            }
            //X型腿-左腿---
            if(assessmentResultBO.getxLeftlegR()!=null) {
                if (Double.parseDouble(assessmentResultBO.getxLeftlegR()) > 50) {
                    highRiskCount++;
                }
            }
            //X型腿-右腿---
            if(assessmentResultBO.getxRightlegR()!=null) {
                if (Double.parseDouble(assessmentResultBO.getxRightlegR()) > 50) {
                    highRiskCount++;
                }
            }
            //头部侧倾-左---
            if(assessmentResultBO.getHeadHeelL()!=null) {
                if (Double.parseDouble(assessmentResultBO.getHeadHeelL()) > 50) {
                    highRiskCount++;
                }
            }
            //头部侧倾-右---
            if(assessmentResultBO.getHeadHeelR()!=null) {
                if (Double.parseDouble(assessmentResultBO.getHeadHeelR()) > 50) {
                    highRiskCount++;
                }
            }
            //高低肩-左倾---
            if(assessmentResultBO.getHighLowShoulderL()!=null) {
                if (Double.parseDouble(assessmentResultBO.getHighLowShoulderL()) > 50) {
                    highRiskCount++;
                }
            }
            //高低肩-右倾---
            if(assessmentResultBO.getHighLowShoulderR()!=null) {
                if (Double.parseDouble(assessmentResultBO.getHighLowShoulderR()) > 50) {
                    highRiskCount++;
                }
            }
            //盆骨侧倾-右
            if(assessmentResultBO.getPelvicTiltR()!=null) {
                if (Double.parseDouble(assessmentResultBO.getPelvicTiltR()) > 50) {
                    highRiskCount++;
                }
            }
            //脊柱侧弯-左----
            if(assessmentResultBO.getScoliosisL()!=null) {
                if (Double.parseDouble(assessmentResultBO.getScoliosisL()) > 50) {
                    highRiskCount++;
                }
            }
            //O型腿-左腿---
            if(assessmentResultBO.getoLeftlegR()!=null) {
                if (Double.parseDouble(assessmentResultBO.getoLeftlegR()) > 50) {
                    highRiskCount++;
                }
            }
            //O型腿-右腿---
            if(assessmentResultBO.getoRightlegR()!=null) {
                if (Double.parseDouble(assessmentResultBO.getoRightlegR()) > 50) {
                    highRiskCount++;
                }
            }
            //头部前倾---
            if(assessmentResultBO.getHeadForerake()!=null) {
                if (assessmentResultBO.getHeadForerake() > 50) {
                    highRiskCount++;
                }
            }
            //盆骨前倾
            if(assessmentResultBO.getPelvisForerake()!=null) {
                if (assessmentResultBO.getPelvisForerake() > 50) {
                    highRiskCount++;
                }
            }
            //膝过伸---
            if(assessmentResultBO.getKnee1()!=null) {
                if (assessmentResultBO.getKnee1() > 50) {
                    highRiskCount++;
                }
            }
            as.setHighRiskCount(highRiskCount);
        }
        return list;
    }

    //查询详情
    public AssessmentResultBO getInfo(Integer id) {
        return userDao.getInfo(id);
    }

    public static void main(String[] args) {
        System.err.println(new Date());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateString = formatter.format(new Date());
        System.err.println(dateString);
    }
}
