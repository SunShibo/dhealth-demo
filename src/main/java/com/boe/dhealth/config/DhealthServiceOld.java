package com.boe.dhealth.config;


import com.boe.dhealth.domain.*;
import com.boe.dhealth.service.UserService;
import com.boe.dhealth.service.ValueService;
import com.boe.dhealth.service.util.*;
import com.boe.dhealth.service.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;


@Service
public class DhealthServiceOld {

    @Autowired
    private ValueService valueService;
    @Autowired
    private UserService userService;

    /**
     * 正面关键点处理
     *
     * @param file
     * @param code
     * @return
     */
    public Map<String, Object> front(MultipartFile file, String code) throws Exception {
        // 创建压缩图片
        String filePath = ImageUtil.getPCpicture(file, 1);
        //获取京东身体点
        String jdstr = BodyKeyPointUtil.getBodyKeyPointJd(SystemConfigUtil.getPath() + filePath);
        //将json转换为对象
        List<JdjsonBO> jdjsonBOList = JsonUtils.getJSONtoList("[" + jdstr + "]", JdjsonBO.class);
        JdjsonBO jdjsonBO = jdjsonBOList.get(0);
        List<String> listJd = jdjsonBO.getResult().getDet_info().get(0).getNode_info();
        //百度云获取的关键点
        String baidustr = BodyKeyPointUtil.getBodyKeyPoint(filePath);
        //将json转换为对象
        List<JsonRootBean> listBd = JsonUtils.getJSONtoList("[" + baidustr + "]", JsonRootBean.class);
        JsonRootBean jsonRootBean = listBd.get(0);
        PersonInfoBO personInfoBO = jsonRootBean.getPerson_info().get(0);
        BodyPartsBO bodyPartsBO = personInfoBO.getBody_parts();
        int neck_y = Integer.parseInt(listJd.get(5));//neck(脖子)y
        int l_elbowy = Integer.parseInt(listJd.get(25));//l_elbow(左手肘)y
        int l_wristy = Integer.parseInt(listJd.get(29));//l_wrist(左手腕)y
        int neck_x = Integer.parseInt(listJd.get(4));//neck(脖子)x
        int right_shoulderx = Integer.parseInt(listJd.get(8));//右肩x
        int right_shouldery = Integer.parseInt(listJd.get(9));//右肩y
        int right_elbowy = Integer.parseInt(listJd.get(13));//r_elbow(右手肘)y
        int l_shoulderx = Integer.parseInt(listJd.get(20));//l_shoulder（左肩膀）x
        int l_shouldery = Integer.parseInt(listJd.get(21));//l_shoulder（左肩膀）y
        int r_hipx = Integer.parseInt(listJd.get(32));//r_hip（右臀部）x
        int r_hipy = Integer.parseInt(listJd.get(33));//r_hip（右臀部）y
        int r_kneex = Integer.parseInt(listJd.get(36));//r_knee(右膝盖)x
        int r_kneey = Integer.parseInt(listJd.get(37));//r_knee(右膝盖)y
        int r_anklex = Integer.parseInt(listJd.get(40));//r_ankle(右脚踝)x
        int r_ankley = Integer.parseInt(listJd.get(41));//r_ankle(右脚踝)y
        int l_hipx = Integer.parseInt(listJd.get(44));//l_hip（左臀部）x
        int l_hipy = Integer.parseInt(listJd.get(45));//l_hip（左臀部）y
        int l_kneex = Integer.parseInt(listJd.get(48));//l_knee(左膝盖)x
        int l_kneey = Integer.parseInt(listJd.get(49));//l_knee(左膝盖)y
        int l_anklex = Integer.parseInt(listJd.get(52));//l_ankle(左脚踝)x
        int l_ankley = Integer.parseInt(listJd.get(53));//l_ankle(左脚踝)y
        int left_earx = MathUtil.getInt(bodyPartsBO.getLeft_ear().getX());//左耳
        int left_eary = MathUtil.getInt(bodyPartsBO.getLeft_ear().getY());
        int right_earx = MathUtil.getInt(bodyPartsBO.getRight_ear().getX());//右耳
        int right_eary = MathUtil.getInt(bodyPartsBO.getRight_ear().getY());
        //画解析图片
        String out = this.bodyjdImg(filePath, jdjsonBO, jsonRootBean);
        int left = MathUtil.getInt(personInfoBO.getLocation().getLeft());//人物矩形左上角X
        int top = MathUtil.getInt(personInfoBO.getLocation().getTop());//人物矩形左上角Y
        int width = MathUtil.getInt(personInfoBO.getLocation().getWidth());//人物矩形宽
        int height = MathUtil.getInt(personInfoBO.getLocation().getHeight());//人物矩形高
        //图片截取-全身
        String body_image = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, left, top, height, width);
        //图片截取-右肩
        String r_shoulder_image = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, left, neck_y, neck_x - left, neck_x - left);
        //图片截取-左肩
        String l_shoulder_image = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, neck_x, neck_y, left + width - neck_x, left + width - neck_x);
        //图片截取-脊柱
        String spine_image = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, left, l_shouldery, l_hipy - l_shouldery, width);
        //图片截取-头部
        String head_image = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, left, top, (int) personInfoBO.getBody_parts().getRight_shoulder().getY() - (int) personInfoBO.getLocation().getTop(), width);
        //图片截取-胸部
        String chest_image = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, left, neck_y, l_elbowy - neck_y, width);
        //图片截取-腿部
        String leg_image = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, left, r_hipy, Math.abs(r_ankley - r_hipy), width);
        //图片截取-骨盆
        String pelvis_image = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, left, right_elbowy + ((l_wristy - l_elbowy) / 2), r_kneey - r_hipy, width);
        //图片截取-骨盆左
        String l_pelvis_image = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, r_hipx + (l_hipx - r_hipx) / 2 - 20, r_hipy - 40, left + width - (r_hipx + (l_hipx - r_hipx) / 2) + 20, left + width - (r_hipx + (l_hipx - r_hipx) / 2) + 20);
        //图片截取-骨盆右
        String r_pelvis_image = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, left + 10, r_hipy - 40, r_hipx - left + (l_hipx - r_hipx) / 2 + 20, r_hipx - left + (l_hipx - r_hipx) / 2 + 20);

        //TODO  上传至OSS 请更换OSS地址
        OSSClientUtil ossClientUtil = new OSSClientUtil();
        String body_image_oss = ossClientUtil.uploadImg2Oss(body_image);//全身
        String spine_image_oss = ossClientUtil.uploadImg2Oss(spine_image);//脊柱
        String head_image_oss = ossClientUtil.uploadImg2Oss(head_image);//头部
        String chest_image_oss = ossClientUtil.uploadImg2Oss(chest_image);//胸部
        String leg_image_oss = ossClientUtil.uploadImg2Oss(leg_image);//腿部
        String pelvis_image_oss = ossClientUtil.uploadImg2Oss(pelvis_image);//骨盆
        String r_shoulder_image_oss = ossClientUtil.uploadImg2Oss(r_shoulder_image);//右肩切图
        String l_shoulder_image_oss = ossClientUtil.uploadImg2Oss(l_shoulder_image);//左肩切图
        String l_pelvis_image_oss = ossClientUtil.uploadImg2Oss(l_pelvis_image);//左骨盆切图
        String r_pelvis_image_oss = ossClientUtil.uploadImg2Oss(r_pelvis_image);//右骨盆切图
        String out_oss = ossClientUtil.uploadImg2Oss(out);//解析
        String old_oss = ossClientUtil.uploadImg2Oss(file);//原图

        //----------------评估
        //获取各个评估标准的value
        ValueBO valueBO = valueService.getValue();
        //头部侧倾 <0为左倾 >0为右倾
        BigDecimal headHeel = AssessUtil.getHeadHeel_R(left_earx, left_eary, right_earx, right_eary, valueBO.getHead());
        int r = headHeel.compareTo(BigDecimal.ZERO);
        BigDecimal headHeel_L = new BigDecimal(0);
        BigDecimal headHeel_R = new BigDecimal(0);
        if (r == 1) {
            headHeel_R = headHeel;
            headHeel_L = new BigDecimal(0);
        }
        if (r == -1) {
            headHeel_L = headHeel.multiply(new BigDecimal(-1));
            headHeel_R = new BigDecimal(0);
        }
        if (r == 0) {
            headHeel_L = new BigDecimal(0);
            headHeel_R = new BigDecimal(0);
        }
        if (headHeel_L.intValue() > 100) {
            headHeel_L = new BigDecimal(100);
        }
        if (headHeel_R.intValue() > 100) {
            headHeel_R = new BigDecimal(100);
        }
        //高低肩
        BigDecimal highLowShoulder = AssessUtil.getHighLowShoulder(l_shoulderx, l_shouldery, right_shoulderx, right_shouldery, valueBO.getShoulder());
        int p = highLowShoulder.compareTo(BigDecimal.ZERO);
        //if(r==0) //等于 if(r==1) //大于 if(r==-1) //小于
        BigDecimal highLowShoulder_L = new BigDecimal(0);
        BigDecimal highLowShoulder_R = new BigDecimal(0);
        if (p == 1) {
            highLowShoulder_R = highLowShoulder;
            highLowShoulder_L = new BigDecimal(0);
        }
        if (p == -1) {
            highLowShoulder_L = highLowShoulder.multiply(new BigDecimal(-1));
            highLowShoulder_R = new BigDecimal(0);
        }
        if (p == 0) {
            highLowShoulder_L = new BigDecimal(0);
            highLowShoulder_R = new BigDecimal(0);
        }
        if (highLowShoulder_L.intValue() > 100) {
            highLowShoulder_L = new BigDecimal(100);
        }
        if (highLowShoulder_R.intValue() > 100) {
            highLowShoulder_R = new BigDecimal(100);
        }
        //盆骨侧倾
        BigDecimal pelvicTiltR = AssessUtil.getPelvicTilt_R(l_hipx, l_hipy, r_hipx, r_hipy, valueBO.getHip());
        int q = pelvicTiltR.compareTo(BigDecimal.ZERO);
        //if(r==0) //等于 if(r==1) //大于 if(r==-1) //小于
        BigDecimal pelvicTiltR_L = new BigDecimal(0);
        BigDecimal pelvicTiltR_R = new BigDecimal(0);
        if (q == 1) {
            pelvicTiltR_R = pelvicTiltR;
            pelvicTiltR_L = new BigDecimal(0);
        }
        if (q == -1) {
            pelvicTiltR_L = pelvicTiltR.multiply(new BigDecimal(-1));
            pelvicTiltR_R = new BigDecimal(0);
        }
        if (q == 0) {
            pelvicTiltR_L = new BigDecimal(0);
            pelvicTiltR_R = new BigDecimal(0);
        }
        if (pelvicTiltR_L.intValue() > 100) {
            pelvicTiltR_L = new BigDecimal(100);
        }
        if (pelvicTiltR_R.intValue() > 100) {
            pelvicTiltR_R = new BigDecimal(100);
        }
        //脊柱侧弯
        BigDecimal scoliosisR = AssessUtil.getScoliosis(l_shoulderx, l_shouldery, right_shoulderx, right_shouldery, l_hipx, l_hipy, r_hipx, r_hipy, valueBO.getSpine());
        int s = scoliosisR.compareTo(BigDecimal.ZERO);
        //if(r==0) //等于 if(r==1) //大于 if(r==-1) //小于
        BigDecimal scoliosisR_L = new BigDecimal(0);
        BigDecimal scoliosisR_R = new BigDecimal(0);
        if (s == 1) {
            scoliosisR_R = scoliosisR;
            scoliosisR_L = new BigDecimal(0);
        }
        if (s == -1) {
            scoliosisR_L = scoliosisR.multiply(new BigDecimal(-1));
            scoliosisR_R = new BigDecimal(0);
        }
        if (s == 0) {
            scoliosisR_L = new BigDecimal(0);
            scoliosisR_R = new BigDecimal(0);
        }
        if (scoliosisR_L.intValue() > 100) {
            scoliosisR_L = new BigDecimal(100);
        }
        if (scoliosisR_R.intValue() > 100) {
            scoliosisR_R = new BigDecimal(100);
        }
        //左腿
        BigDecimal leftleg_r = AssessUtil.getO_leftleg_R(l_hipx, l_hipy, l_kneex, l_kneey, l_kneex, l_kneey, l_anklex, l_ankley, valueBO.getLeftLeg());
        //右腿
        BigDecimal rightleg_r = AssessUtil.getO_rightleg_R(r_hipx, r_hipy, r_kneex, r_kneey, r_kneex, r_kneey, r_anklex, r_ankley, valueBO.getRightLeg());

        int t_l = leftleg_r.compareTo(BigDecimal.ZERO);
        int t_r = rightleg_r.compareTo(BigDecimal.ZERO);
        //if(r==0) //等于 if(r==1) //大于 if(r==-1) //小于
        BigDecimal x_leftleg_r = new BigDecimal(0);
        BigDecimal o_leftleg_r = new BigDecimal(0);
        BigDecimal x_rightleg_r = new BigDecimal(0);
        BigDecimal o_rightleg_r = new BigDecimal(0);
        //左腿正数为X型腿 负数为O型腿
        if (t_l == -1) {
            o_leftleg_r = leftleg_r.multiply(new BigDecimal(-1));
            x_leftleg_r = new BigDecimal(0);
            //右腿正数为O型腿 负数为X型腿
            if (t_r == 1) {
                o_rightleg_r = rightleg_r;
                x_rightleg_r = new BigDecimal(0);
            }
            if (t_r == -1) {
                o_rightleg_r = new BigDecimal(0);
                x_rightleg_r = rightleg_r.multiply(new BigDecimal(-1));
            }
            if (t_r == 0) {
                o_rightleg_r = new BigDecimal(0);
                x_rightleg_r = new BigDecimal(0);
            }
        }
        //左腿正数为X型腿 负数为O型腿
        if (t_l == 1) {
            o_leftleg_r = new BigDecimal(0);
            x_leftleg_r = leftleg_r;
            //右腿正数为O型腿 负数为X型腿
            if (t_r == 1) {
                o_rightleg_r = rightleg_r;
                x_rightleg_r = new BigDecimal(0);
            }
            if (t_r == -1) {
                o_rightleg_r = new BigDecimal(0);
                x_rightleg_r = rightleg_r.multiply(new BigDecimal(-1));
            }
            if (t_r == 0) {
                o_rightleg_r = new BigDecimal(0);
                x_rightleg_r = new BigDecimal(0);
            }
        }
        if (t_l == 0) {
            o_leftleg_r = new BigDecimal(0);
            x_leftleg_r = new BigDecimal(0);
            //右腿正数为O型腿 负数为X型腿
            if (t_r == 1) {
                o_rightleg_r = rightleg_r;
                x_rightleg_r = new BigDecimal(0);
            }
            if (t_r == -1) {
                o_rightleg_r = new BigDecimal(0);
                x_rightleg_r = rightleg_r.multiply(new BigDecimal(-1));
            }
            if (t_r == 0) {
                o_rightleg_r = new BigDecimal(0);
                x_rightleg_r = new BigDecimal(0);
            }
        }
        if (o_leftleg_r.intValue() > 100) {
            o_leftleg_r = new BigDecimal(100);
        }
        if (x_leftleg_r.intValue() > 100) {
            x_leftleg_r = new BigDecimal(100);
        }
        if (o_rightleg_r.intValue() > 100) {
            o_rightleg_r = new BigDecimal(100);
        }
        if (x_rightleg_r.intValue() > 100) {
            x_rightleg_r = new BigDecimal(100);
        }

        AssessmentResultBO assessmentResultBO = new AssessmentResultBO();
        assessmentResultBO.setUt(code.substring(3));//截取第三位以后的字符串
        assessmentResultBO.setOld(SystemConfigUtil.getPath() + filePath);//原图
        assessmentResultBO.setAnalysis(out);//解析图
        assessmentResultBO.setBodyImage(body_image);
        assessmentResultBO.setHeadImage(head_image);
        assessmentResultBO.setChestImage(chest_image);
        assessmentResultBO.setLegImage(leg_image);
        assessmentResultBO.setPelvisImage(pelvis_image);
        assessmentResultBO.setSpineImage(spine_image);
        assessmentResultBO.setOldOss(out_oss);
        assessmentResultBO.setOutOss(old_oss);
        assessmentResultBO.setBodyImageOss(body_image_oss);
        assessmentResultBO.setHeadImageOss(head_image_oss);
        assessmentResultBO.setChestImageOss(chest_image_oss);
        assessmentResultBO.setLegImageOss(leg_image_oss);
        assessmentResultBO.setPelvisImageOss(pelvis_image_oss);
        assessmentResultBO.setSpineImageOss(spine_image_oss);
        assessmentResultBO.setlShoulderImageOss(l_shoulder_image_oss);
        assessmentResultBO.setrShoulderImageOss(r_shoulder_image_oss);
        assessmentResultBO.setlPelvisImageOss(l_pelvis_image_oss);
        assessmentResultBO.setrPelvisImageOss(r_pelvis_image_oss);
        assessmentResultBO.setHeadHeelL(headHeel_L.toString());
        assessmentResultBO.setHeadHeelR(headHeel_R.toString());
        assessmentResultBO.setHighLowShoulderL(highLowShoulder_L.toString());
        assessmentResultBO.setHighLowShoulderR(highLowShoulder_R.toString());
        assessmentResultBO.setPelvicTiltL(pelvicTiltR_L.toString());
        assessmentResultBO.setPelvicTiltR(pelvicTiltR_R.toString());
        assessmentResultBO.setScoliosisL(scoliosisR_L.toString());
        assessmentResultBO.setScoliosisR(scoliosisR_R.toString());
        assessmentResultBO.setoLeftlegR(o_leftleg_r.toString());//左腿O
        assessmentResultBO.setoRightlegR(o_rightleg_r.toString());//右腿O
        assessmentResultBO.setxLeftlegR(x_leftleg_r.toString());
        assessmentResultBO.setxRightlegR(x_rightleg_r.toString());
        //头部侧倾
        if (section(headHeel_L)) {
            assessmentResultBO.setHeadHeelLStatus("high");
        } else {
            assessmentResultBO.setHeadHeelLStatus("low");
        }
        if (section(headHeel_R)) {
            assessmentResultBO.setHeadHeelRStatus("high");
        } else {
            assessmentResultBO.setHeadHeelRStatus("low");
        }
        //高低肩
        if (section(highLowShoulder_L)) {
            assessmentResultBO.setHighLowShoulderLStatus("high");
        } else {
            assessmentResultBO.setHighLowShoulderLStatus("low");
        }
        if (section(highLowShoulder_R)) {
            assessmentResultBO.setHighLowShoulderRStatus("high");
        } else {
            assessmentResultBO.setHighLowShoulderRStatus("low");
        }
        //盆骨侧倾
        if (section(pelvicTiltR_L)) {
            assessmentResultBO.setPelvicTiltLStatus("high");
        } else {
            assessmentResultBO.setPelvicTiltLStatus("low");
        }
        if (section(pelvicTiltR_R)) {
            assessmentResultBO.setPelvicTiltRStatus("high");
        } else {
            assessmentResultBO.setPelvicTiltRStatus("low");
        }
        //脊柱侧弯
        if (section(scoliosisR_L)) {
            assessmentResultBO.setScoliosisLStatus("high");
        } else {
            assessmentResultBO.setScoliosisLStatus("low");
        }
        if (section(scoliosisR_R)) {
            assessmentResultBO.setScoliosisRStatus("high");
        } else {
            assessmentResultBO.setScoliosisRStatus("low");
        }
        //O型腿
        if (section(o_leftleg_r)) {
            assessmentResultBO.setoLeftlegRStatus("high");
        } else {
            assessmentResultBO.setoLeftlegRStatus("low");
        }
        if (section(o_rightleg_r)) {
            assessmentResultBO.setoRightlegRStatus("high");
        } else {
            assessmentResultBO.setoRightlegRStatus("low");
        }
        //X型腿
        if (section(x_leftleg_r)) {
            assessmentResultBO.setxLeftlegRStatus("high");
        } else {
            assessmentResultBO.setxLeftlegRStatus("low");
        }
        if (section(x_rightleg_r)) {
            assessmentResultBO.setxRightlegRStatus("high");
        } else {
            assessmentResultBO.setxRightlegRStatus("low");
        }
        userService.addAssess(assessmentResultBO);
        Map<String, Object> map = new HashMap();
        map.put("old_oss", old_oss);
        map.put("out_oss", out_oss);
        map.put("body_image_oss", body_image_oss);
        map.put("head_image_oss", head_image_oss);//头部
        map.put("chest_image_oss", chest_image_oss);//胸部
        map.put("leg_image_oss", leg_image_oss);//腿部
        map.put("pelvis_image_oss", pelvis_image_oss);//盆骨
        map.put("spine_image_oss", spine_image_oss);//脊柱

        //------------评估
        map.put("headHeel_L", headHeel_L);//头部侧倾-左
        map.put("headHeel_R", headHeel_R);//头部侧倾-右
        map.put("highLowShoulder_L", highLowShoulder_L);//高低肩
        map.put("highLowShoulder_R", highLowShoulder_R);
        map.put("pelvicTilt_L", pelvicTiltR_L);//盆骨侧倾-左
        map.put("pelvicTilt_R", pelvicTiltR_R);
        map.put("scoliosis_L", scoliosisR_L);//脊柱侧弯-左
        map.put("scoliosis_R", scoliosisR_R);
        map.put("o_leftleg_r", o_leftleg_r);
        map.put("o_rightleg_r", o_rightleg_r);
        map.put("x_leftleg_r", x_leftleg_r);
        map.put("x_rightleg_r", x_rightleg_r);

        return map;
    }

    /**
     * 使用百度的脸部点和京东的身体点
     *
     * @param filePath
     * @param jdjsonBO
     * @return
     * @throws Exception
     */
    public String bodyjdImg(String filePath, JdjsonBO jdjsonBO, JsonRootBean jsonRootBean) throws Exception {
        //京东获取的点
        List<String> listJd = jdjsonBO.getResult().getDet_info().get(0).getNode_info();
        int head_x = Integer.parseInt(listJd.get(0));//head(头部)x
        int head_y = Integer.parseInt(listJd.get(1));//head(头部)y
        int neck_x = Integer.parseInt(listJd.get(4));//neck(脖子)x
        int neck_y = Integer.parseInt(listJd.get(5));//neck(脖子)y
        int right_shoulderx = Integer.parseInt(listJd.get(8));//右肩x
        int right_shouldery = Integer.parseInt(listJd.get(9));//右肩y
        int right_elbowx = Integer.parseInt(listJd.get(12));//r_elbow(右手肘)x
        int right_elbowy = Integer.parseInt(listJd.get(13));//r_elbow(右手肘)y
        int right_wristx = Integer.parseInt(listJd.get(16));//r_wrist(右手腕)x
        int right_wristy = Integer.parseInt(listJd.get(17));//r_wrist(右手腕)y
        int l_shoulderx = Integer.parseInt(listJd.get(20));//l_shoulder（左肩膀）x
        int l_shouldery = Integer.parseInt(listJd.get(21));//l_shoulder（左肩膀）y
        int l_elbowx = Integer.parseInt(listJd.get(24));//l_elbow(左手肘)x
        int l_elbowy = Integer.parseInt(listJd.get(25));//l_elbow(左手肘)y
        int l_wristx = Integer.parseInt(listJd.get(28));//l_wrist(左手腕)x
        int l_wristy = Integer.parseInt(listJd.get(29));//l_wrist(左手腕)y
        int r_hipx = Integer.parseInt(listJd.get(32));//r_hip（右臀部）x
        int r_hipy = Integer.parseInt(listJd.get(33));//r_hip（右臀部）y
        int r_kneex = Integer.parseInt(listJd.get(36));//r_knee(右膝盖)x
        int r_kneey = Integer.parseInt(listJd.get(37));//r_knee(右膝盖)y
        int r_anklex = Integer.parseInt(listJd.get(40));//r_ankle(右脚踝)x
        int r_ankley = Integer.parseInt(listJd.get(41));//r_ankle(右脚踝)y
        int l_hipx = Integer.parseInt(listJd.get(44));//l_hip（左臀部）x
        int l_hipy = Integer.parseInt(listJd.get(45));//l_hip（左臀部）y
        int l_kneex = Integer.parseInt(listJd.get(48));//l_knee(左膝盖)x
        int l_kneey = Integer.parseInt(listJd.get(49));//l_knee(左膝盖)y
        int l_anklex = Integer.parseInt(listJd.get(52));//l_ankle(左脚踝)x
        int l_ankley = Integer.parseInt(listJd.get(53));//l_ankle(左脚踝)y
        //百度获取的点
        BodyPartsBO bodyPartsBO = jsonRootBean.getPerson_info().get(0).getBody_parts();
        int left_earx = MathUtil.getInt(bodyPartsBO.getLeft_ear().getX());//左耳
        int left_eary = MathUtil.getInt(bodyPartsBO.getLeft_ear().getY());
        int left_eyex = MathUtil.getInt(bodyPartsBO.getLeft_eye().getX());//左眼
        int left_eyey = MathUtil.getInt(bodyPartsBO.getLeft_eye().getY());
        int nosex = MathUtil.getInt(bodyPartsBO.getNose().getX());//鼻子
        int nosey = MathUtil.getInt(bodyPartsBO.getNose().getY());
        int right_eyex = MathUtil.getInt(bodyPartsBO.getRight_eye().getX());//右眼
        int right_eyey = MathUtil.getInt(bodyPartsBO.getRight_eye().getY());
        int right_earx = MathUtil.getInt(bodyPartsBO.getRight_ear().getX());//右耳
        int right_eary = MathUtil.getInt(bodyPartsBO.getRight_ear().getY());
        BufferedImage image = ImageIO.read(new File(SystemConfigUtil.getPath() + filePath));
        Graphics2D g = image.createGraphics();
        Font axesFont = new Font("宋体", 16, 16);
        g.setFont(axesFont);
        //画笔颜色
        g.setColor(Color.red);
        //脸部连线
        int[] arrx = new int[]{left_earx, left_eyex, nosex, right_eyex, right_earx};
        int[] arry = new int[]{left_eary, left_eyey, nosey, right_eyey, right_eary};
        g.drawPolyline(arrx, arry, 5);//第一组折线 脸部连线
        int[] arrx1 = new int[]{l_wristx, l_elbowx, l_shoulderx, neck_x, right_shoulderx, right_elbowx, right_wristx};
        int[] arry1 = new int[]{l_wristy, l_elbowy, l_shouldery, neck_y, right_shouldery, right_elbowy, right_wristy};
        g.drawPolyline(arrx1, arry1, 7);//第一组折线 上半身连线
        //头顶到脖子
        int[] arrx2 = new int[]{l_anklex, l_kneex, l_hipx, r_hipx, r_kneex, r_anklex};
        int[] arry2 = new int[]{l_ankley, l_kneey, l_hipy, r_hipy, r_kneey, r_ankley};
        g.drawPolyline(arrx2, arry2, 6);//第二组折线 下半身连线
        g.setColor(new Color(16, 168, 229));
        //左肩延长线上的y值
        double left_shoulder_y = MathUtil.getY(l_shoulderx, l_shouldery, right_shoulderx, right_shouldery, l_shoulderx + 300);
        //左肩延长线
        g.drawLine(l_shoulderx, l_shouldery, l_shoulderx + 300, new Double(left_shoulder_y).intValue());
        //右肩延长线上的点的Y值
        double right_shoulder_y = MathUtil.getY(l_shoulderx, l_shouldery, right_shoulderx, right_shouldery, right_shoulderx - 300);
        //右肩延长线
        g.drawLine(right_shoulderx, right_shouldery, right_shoulderx - 300, new Double(right_shoulder_y).intValue());
        g.drawLine(l_shoulderx, l_shouldery, right_shoulderx, right_shouldery);
        //左右髋部角度
        double shoulder = MathUtil.getAngle(l_shoulderx, l_shouldery, right_shoulderx, right_shouldery);
        BigDecimal bigshoulder = new BigDecimal(shoulder);
        bigshoulder = bigshoulder.setScale(2, BigDecimal.ROUND_HALF_UP);
        g.drawLine(l_hipx, l_hipy, r_hipx, r_hipy);
        //左髋部延长线上的y值
        double left_hip_y = MathUtil.getY(l_hipx, l_hipy, r_hipx, r_hipy, l_hipx + 300);
        g.drawLine(l_hipx, l_hipy, l_hipx + 300, new Double(left_hip_y).intValue());//左髋部延长线
        //右髋部延长线上的y值
        double right_hip_y = MathUtil.getY(l_hipx, l_hipy, r_hipx, r_hipy, r_hipx - 300);
        g.drawLine(r_hipx, r_hipy, r_hipx - 300, new Double(right_hip_y).intValue());//右髋部延长线
        //左右髋部角度
        double hip = MathUtil.getAngle(l_hipx, l_hipy, r_hipx, r_hipy);
        BigDecimal bighip = new BigDecimal(hip);
        bighip = bighip.setScale(2, BigDecimal.ROUND_HALF_UP);
        //左膝盖延长线上的y值
        double left_knee_y = MathUtil.getY(l_kneex, l_kneey, r_kneex, r_kneey, l_kneex + 300);
        g.drawLine(l_kneex, l_kneey, l_kneex + 300, new Double(left_knee_y).intValue());//左膝盖延长线
        //右膝盖延长线上的y值
        double right_knee_y = MathUtil.getY(l_kneex, l_kneey, r_kneex, r_kneey, r_kneex - 300);
        g.drawLine(r_kneex, r_kneey, r_kneex - 300, new Double(right_knee_y).intValue());//右膝盖延长线
        g.drawLine(l_kneex, l_kneey, r_kneex, r_kneey);
        //左右膝盖角度
        double knee = MathUtil.getAngle(l_kneex, l_kneey, r_kneex, r_kneey);
        BigDecimal bigknee = new BigDecimal(knee);
        bigknee = bigknee.setScale(2, BigDecimal.ROUND_HALF_UP);
        //垂直参考线
        g.drawLine(l_shoulderx, l_shouldery - 500, l_shoulderx, l_shouldery + 1000);
        g.drawLine(right_shoulderx, right_shouldery - 500, right_shoulderx, right_shouldery + 1000);
        g.drawLine(head_x, head_y - 500, head_x, head_y + 1000);
        //标记点
        g.setColor(Color.yellow);
        g.setFont(new Font(null, Font.BOLD, 30));
        g.drawString("•", nosex - 9, nosey + 12);//鼻子
        g.drawString("•", left_earx - 9, left_eary + 12);//左耳
        g.drawString("•", left_eyex - 9, left_eyey + 12);//左眼
        g.drawString("•", right_eyex - 9, right_eyey + 12);//右眼
        g.drawString("•", right_earx - 9, right_eary + 12);//右耳
        g.drawString("•", neck_x - 9, neck_y + 12);//颈部
        g.drawString("•", head_x - 9, head_y + 12);//head(头部)
        g.drawString("•", right_shoulderx - 9, right_shouldery + 12);//右肩
        g.drawString("•", right_elbowx - 9, right_elbowy + 12);//右肘
        g.drawString("•", right_wristx - 9, right_wristy + 12);//右手腕
        g.drawString("•", l_shoulderx - 9, l_shouldery + 12);//l_shoulder（左肩膀）
        g.drawString("•", l_elbowx - 9, l_elbowy + 12);//l_elbow(左手肘)
        g.drawString("•", l_wristx - 9, l_wristy + 12);//l_wrist(左手腕)
        g.drawString("•", r_hipx - 9, r_hipy + 12);//r_hip（右臀部）
        g.drawString("•", r_kneex - 9, r_kneey + 12);//r_knee(右膝盖)
        g.drawString("•", r_anklex - 9, r_ankley + 12);//r_ankle(右脚踝)
        g.drawString("•", l_hipx - 9, l_hipy + 12);//l_hip（左臀部）
        g.drawString("•", l_kneex - 9, l_kneey + 12);//l_knee(左膝盖)
        g.drawString("•", l_anklex - 9, l_ankley + 12);//l_ankle(左脚踝)
        Font axesFont1 = new Font("微软雅黑", 16, 20);
        g.setColor(Color.RED);
        g.setFont(axesFont1);
        //双肩度数
        g.drawString(bigshoulder + "°", head_x + 1, l_shouldery - 10);
        //髋部度数
        g.drawString(bighip + "°", head_x + 1, l_hipy - 10);
        //膝盖度数
        g.drawString(bigknee + "°", head_x + 1, l_kneey - 10);
        String output = SystemConfigUtil.getPath() + new Date().getTime() + "_" + new Random().nextInt(1000) + "解析" + "." + "jpg";
        FileOutputStream out = new FileOutputStream(output);//输出图片的地址
        ImageIO.write(image, "jpeg", out);
        out.close();
        return output;
    }

    private Boolean section(BigDecimal bigDecimal) {
        if (bigDecimal.compareTo(new BigDecimal(0)) > -1 && bigDecimal.compareTo(new BigDecimal(60)) == -1) {
            return false;//low
        } else {
            return true;//high
        }
    }


    //*************************************************************************************************
    //*************************************************************************************************

    /**
     * 侧面关键点
     *
     * @param file
     * @return
     */
    public Map<String, Object> side(MultipartFile file) throws Exception {
        // 图片存放本地
        String filePath = ImageUtil.getPCpicture(file, 1);
        String s = BodyKeyPointUtil.getBodyKeyPoint(filePath);
        List<JsonRootBean> listBd = JsonUtils.getJSONtoList("[" + s + "]", JsonRootBean.class);
        LocationBO location = listBd.get(0).getPerson_info().get(0).getLocation();
        BufferedImage image = ImageIO.read(new File(SystemConfigUtil.getPath() + filePath));
        Graphics2D g = image.createGraphics();
        Font 宋体 = new Font("宋体", 16, 100);
        g.setFont(宋体);
        g.setColor(new Color(16, 168, 229));
        //边框
        g.drawLine((int) location.getLeft(), (int) location.getTop(), (int) (location.getLeft() + location.getWidth()), (int) location.getTop());//上线
        g.drawLine((int) location.getLeft(), (int) (location.getTop() + location.getHeight()), (int) (location.getLeft() + location.getWidth()), (int) (location.getTop() + location.getHeight()));//下线
        g.drawLine((int) location.getLeft(), (int) location.getTop(), (int) location.getLeft(), (int) (location.getTop() + location.getHeight()));//左线
        g.drawLine((int) (location.getLeft() + location.getWidth()), (int) location.getTop(), (int) (location.getLeft() + location.getWidth()), (int) (location.getTop() + location.getHeight()));//右线
        //关键点
        BodyPartsBO body_parts = listBd.get(0).getPerson_info().get(0).getBody_parts();
        CoordinateBO top_head = body_parts.getTop_head(); //头顶
        CoordinateBO right_ear = body_parts.getRight_ear(); //右耳
        CoordinateBO right_eye = body_parts.getRight_eye();//右眼
        CoordinateBO nose = body_parts.getNose();//鼻子
        CoordinateBO right_shoulder = body_parts.getRight_shoulder();//右肩
        CoordinateBO right_elbow = body_parts.getRight_elbow();//右手肘
        CoordinateBO right_wrist = body_parts.getRight_wrist(); //右手腕
        CoordinateBO right_hip = body_parts.getRight_hip();//右髋部
        CoordinateBO right_knee = body_parts.getRight_knee(); //右膝
        CoordinateBO right_ankle = body_parts.getRight_ankle(); //右脚踝
        CoordinateBO neck = body_parts.getNeck(); //颈部 //FIXME  新增代码

        g.drawString(".", (int) top_head.getX() + -12, (int) Math.ceil(top_head.getY()) + 10);  //头顶
        g.drawString(".", (int) right_ear.getX() + -12, (int) Math.ceil(right_ear.getY()) + 10);  //右耳
        g.drawString(".", (int) right_eye.getX() + -10, (int) Math.ceil(right_eye.getY()) + 10);  //右眼
        g.drawString(".", (int) nose.getX() + -12, (int) Math.ceil(nose.getY()) + 10);  //鼻子
        g.drawString(".", (int) right_shoulder.getX() + -12, (int) Math.ceil(right_shoulder.getY()) + 10);  //右肩
        g.drawString(".", (int) right_elbow.getX() + -12, (int) Math.ceil(right_elbow.getY()) + 10);  //右手肘
        g.drawString(".", (int) right_wrist.getX() + -12, (int) Math.ceil(right_wrist.getY()) + 10);  //右手腕
        g.drawString(".", (int) right_hip.getX() + -12, (int) Math.ceil(right_hip.getY()) + 10);  //右髋部
        g.drawString(".", (int) right_knee.getX() + -12, (int) Math.ceil(right_knee.getY()) + 10);  //右膝
        g.drawString(".", (int) right_ankle.getX() + -12, (int) Math.ceil(right_ankle.getY()) + 10);  //右脚踝
        g.drawString(".", (int) neck.getX() + -12, (int) Math.ceil(neck.getY()) + 10);  //颈部 //FIXME  新增代码

        double left= right_shoulder.getX()- location.getLeft(); //FIXME  新增代码
        left=right_shoulder.getX()-(left/2);//FIXME  新增代码
        g.drawString(".", (int)left, (int) Math.ceil(right_shoulder.getY()) + 10);  //背部关键点//FIXME  新增代码


        //连接线
        g.drawLine((int) top_head.getX(), (int) Math.ceil(top_head.getY()), (int) right_ear.getX(), (int) Math.ceil(right_ear.getY()));//头顶连右耳
        g.drawLine((int) right_ear.getX(), (int) Math.ceil(right_ear.getY()), (int) right_shoulder.getX(), (int) Math.ceil(right_shoulder.getY()));//右耳连肩部
        g.drawLine((int) right_ear.getX(), (int) Math.ceil(right_ear.getY()), (int) right_eye.getX(), (int) Math.ceil(right_eye.getY()));// 耳朵连眼睛
        g.drawLine((int) right_eye.getX(), (int) Math.ceil(right_eye.getY()), (int) nose.getX(), (int) Math.ceil(nose.getY()));// 眼睛连鼻子
        g.drawLine((int) right_shoulder.getX(), (int) Math.ceil(right_shoulder.getY()), (int) right_elbow.getX(), (int) Math.ceil(right_elbow.getY()));//肩部连肘部
        g.drawLine((int) right_elbow.getX(), (int) Math.ceil(right_elbow.getY()), (int) right_wrist.getX(), (int) Math.ceil(right_wrist.getY()));//肘部连手腕
        g.drawLine((int) right_hip.getX(), (int) Math.ceil(right_hip.getY()), (int) right_knee.getX(), (int) Math.ceil(right_knee.getY()));//髋部连膝部
        g.drawLine((int) right_knee.getX(), (int) Math.ceil(right_knee.getY()), (int) right_ankle.getX(), (int) Math.ceil(right_ankle.getY()));//膝部连脚部

        //FIXME  新增代码
        g.setColor(new Color(209, 61, 229));
        //FIXME  新增代码
        g.drawLine((int) neck.getX(), (int) Math.ceil(neck.getY()), (int) left+12, (int) Math.ceil(right_shoulder.getY()));//背部连颈部



        g.setColor(new Color(34, 229, 30));

        //两条辅助线
        g.drawLine((int) right_ear.getX(), (int) location.getTop(), (int) right_ear.getX(), (int) (location.getTop() + location.getHeight()));//膝部连脚部
        g.drawLine((int) nose.getX(), (int) location.getTop(), (int) nose.getX(), (int) (location.getTop() + location.getHeight()));//膝部连脚部

        g.setColor(new Color(229, 47, 46));
        宋体 = new Font("宋体", Font.PLAIN, 15);
        g.setFont(宋体);
        double k = 90 - MathUtil.getAngle((int) right_ear.getX(), (int) right_ear.getY(), (int) right_shoulder.getX(), (int) right_shoulder.getY());
        g.drawString(String.format("%.2f", k) + "°", (int) right_ear.getX() + -90, (int) (right_ear.getY()) + 35);
        double k1 = 90 - MathUtil.getAngle((int) right_hip.getX(), (int) right_hip.getY(), (int) right_knee.getX(), (int) right_knee.getY());
        g.drawString(String.format("%.2f", k1) + "°", (int) right_hip.getX() + -90, (int) (right_hip.getY()) + 35);  //右手肘

        //FIXME  新增代码
        double k2 = 90 - MathUtil.getAngle((int) neck.getX(), (int) neck.getY(), (int) left+12, (int) right_shoulder.getY());
        //FIXME  新增代码
        g.drawString(String.format("%.2f", k2) + "°", (int) right_shoulder.getX() + -90, (int) (right_shoulder.getY()) + 35);  //右手肘

        //FIXME  新增代码
        double k3 =  right_knee.getX()-5 -  right_ankle.getX() ;//90 - MathUtil.getAngle((int), (int) right_knee.getY(), (int) right_ankle.getX(), (int) right_ankle.getY());
        //FIXME  新增代码
        g.drawString(String.format("%.2f", k3) + "°", (int) right_knee.getX() + -90, (int) (right_knee.getY()) + 35);  //膝


        String output = SystemConfigUtil.getPath() + new Date().getTime() + "_" + new Random().nextInt(1000) + "解析" + "." + "jpg";
        FileOutputStream out = new FileOutputStream(output);//输出图片的地址
        ImageIO.write(image, "jpeg", out);
        out.close();

        //截取头部
        String head = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, (int) location.getLeft(), (int) location.getTop(), (int) right_shoulder.getY(), (int) location.getWidth());
        //盆骨
        String hip = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, (int) location.getLeft() , (int) right_elbow.getY(), (int) (right_knee.getY() - right_elbow.getY()), (int) location.getWidth());

        //截取背部 //FIXME  新增代码
        String back = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, (int) location.getLeft(), (int) right_ear.getY(), (int) right_elbow.getY(), (int) location.getWidth());
       //截取膝部//FIXME  新增代码
        String knee = ImageUtil.cut(SystemConfigUtil.getPath() + filePath, (int) location.getLeft() , (int) right_hip.getY(), (int) (right_ankle.getY() - right_hip.getY()), (int) location.getWidth());


        //上传至OSS
        OSSClientUtil oss = new OSSClientUtil();
        String analysis = oss.uploadImg2Oss(output);//解析图
        String aHead = oss.uploadImg2Oss(head);//头
        String aWaist = oss.uploadImg2Oss(hip);//腰部
        String aBack = oss.uploadImg2Oss(back);//背部  //FIXME  新增代码
        String aken = oss.uploadImg2Oss(knee);//膝部位 //FIXME  新增代码



        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("analysis", analysis);
        resultMap.put("head", aHead);
        resultMap.put("back", aBack);

        ValueBO value = valueService.getValue(); //FIXME  ValueBO实体类中新增 knee 字段手动加一下 生成get set
        double headForerake = AssessUtil.hepOs(k, value.getHeadForward());
        double pelvisForerake = AssessUtil.hepOs(k1, value.getKneeHyperextension());
        resultMap.put("headForerake",headForerake);
        resultMap.put("pelvisForerake",pelvisForerake);


        //驼背
        double hunchback = AssessUtil.hepOs(k2, value.getHunchback()); //FIXME  新增代码
        //膝过申
        double knee1 = AssessUtil.hepOs(k3, value.getKnee());//FIXME  新增代码
        resultMap.put("k2",hunchback); //FIXME  新增代码
        resultMap.put("k3",knee1);//FIXME  新增代码

        return resultMap;
    }


}
