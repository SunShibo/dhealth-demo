package com.boe.dhealth.service;

import com.boe.dhealth.domain.*;
import com.boe.dhealth.service.util.*;
import com.boe.dhealth.service.util.JsonUtils;
import net.sf.json.JSONObject;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

@Service
public class CalculateService {

    public void calculate(String sourceFilePath) throws Exception {
        CalculateService cs = new CalculateService();
        // 取点
        File sourceFile = new File(sourceFilePath);
        InputStream inputStream = new FileInputStream(sourceFile);
        MultipartFile file = new MockMultipartFile(sourceFile.getName(), inputStream);
        cs.getPoint(file,null);
    }


    public void getPoint(MultipartFile file,Map<String,Object> map) throws Exception {
        CalculateService cs = new CalculateService();
        // 开始处理轮廓
        String binaryImagePath = "/wwww/img/binaryImage.jpg";//二值图
        String resultPath = "/www/img/resultImage.jpg";//处理原图然后输出
        File sourceFile = multipartFileToFile(file);

        List<String> coordinateList = new ArrayList<String>(); // 取边缘点用

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

        int right_wristx = Integer.parseInt(listJd.get(16));//r_wrist(右手腕)x
        int right_wristy = Integer.parseInt(listJd.get(17));//r_wrist(右手腕)y
        int l_wristx = Integer.parseInt(listJd.get(28));//l_wrist(左手腕)x
        int l_wristy = Integer.parseInt(listJd.get(29));//l_wrist(左手腕)y
        int x_head = Integer.parseInt(listJd.get(0));//head(头部)x
        int y_head = Integer.parseInt(listJd.get(1));//head(头部)y
        int neck_y = Integer.parseInt(listJd.get(5));//neck(脖子)y
        int right_elbowx = Integer.parseInt(listJd.get(12));//r_elbow(右手肘)x
        int right_elbowy = Integer.parseInt(listJd.get(13));//r_elbow(右手肘)y
        int neck_x = Integer.parseInt(listJd.get(4));//neck(脖子)x
        int right_shoulderx = Integer.parseInt(listJd.get(8));//右肩x
        int right_shouldery = Integer.parseInt(listJd.get(9));//右肩y
        int l_elbowx = Integer.parseInt(listJd.get(24));//l_elbow(左手肘)x
        int l_elbowy = Integer.parseInt(listJd.get(25));//l_elbow(左手肘)y
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
        BufferedImage image = ImageIO.read(new File(SystemConfigUtil.getPath() + filePath));
        BufferedImage copyImage  = ImageIO.read(new File(SystemConfigUtil.getPath() + filePath));// 在复制一个

        //调用人体分割接口
        String base64 = BodySeg.body_seg(image);
        JSONObject resultJson = JsonUtils.getJsonObject4JavaPOJO(base64);

        //生成二值图然后输出二值图
//        BufferedImage sourceImage = ImageIO.read(sourceFile);
        BufferedImage binaryImage = BodySeg.convert(resultJson.getString("labelmap"), copyImage.getWidth(), copyImage.getHeight());
        List<String> rgb = BodySeg.setRGB(binaryImage, copyImage, resultPath);
        map.put("line",rgb);
        String headPointEdge = cs.getHeadPointEdge(copyImage, x_head, y_head);
        coordinateList.add(headPointEdge); // 添加头部边缘点
        map.put("head_top",new PointReturnBO(headPointEdge)); //头顶部

        List<String> neckPointsEdge = cs.getNeckPointsEdge(copyImage, neck_x, neck_y, left_eary);
        map.put("left_neck",new PointReturnBO(neckPointsEdge.get(1))); //左颈部
        map.put("right_neck",new PointReturnBO(neckPointsEdge.get(0))); //右颈部
        coordinateList.addAll(neckPointsEdge); // 添加脖子边缘点,最高点区耳朵Y轴
        //肩部关节点

        //获取肘关节边缘点
        List<String> elbowPointsEdge = cs.getElbowPointsEdge(copyImage, l_elbowx, l_elbowy, right_elbowx, right_elbowy);
        map.put("left_elbow_below",new PointReturnBO(elbowPointsEdge.get(0))); //左肘左
        map.put("left_elbow_top",new PointReturnBO(elbowPointsEdge.get(1))); //左肘右
        map.put("right_elbow_top",new PointReturnBO(elbowPointsEdge.get(2))); //右肘右
        map.put("right_elbow_below",new PointReturnBO(elbowPointsEdge.get(3))); //右肘左
        coordinateList.addAll(elbowPointsEdge);
        //手腕
        List<String> wristPointsEdge = cs.getWristPointsEdge(copyImage, l_wristx, l_wristy, right_wristx, right_wristy);
        coordinateList.addAll(wristPointsEdge) ;
        map.put("left_wrist_below",new PointReturnBO(wristPointsEdge.get(0))); //左手左
        map.put("left_wrist_top",new PointReturnBO(wristPointsEdge.get(1))); //左手腕右
        map.put("right_wrist_top",new PointReturnBO(wristPointsEdge.get(2))); //右手腕右
        map.put("right_wrist_below",new PointReturnBO(wristPointsEdge.get(3))); //右手腕左

        //腋下
        List<String> armpitPointsEdge = cs.getArmpitPointsEdge(copyImage, l_elbowx, l_elbowy, right_elbowx, right_elbowy);
        coordinateList.addAll(armpitPointsEdge) ;
        map.put("left_oxter",new PointReturnBO(armpitPointsEdge.get(0))); //左腋下
        map.put("right_oxter",new PointReturnBO(armpitPointsEdge.get(1))); //右腋下

        //腹部
        List<String> abdomenPointsEdge = cs.getAbdomenPointsEdge(copyImage, l_hipx, l_hipy, r_hipx, l_shouldery);
        coordinateList.addAll(abdomenPointsEdge);
        map.put("right_abdomen",new PointReturnBO(abdomenPointsEdge.get(0))); //右腹部
        map.put("left_abdomen",new PointReturnBO(armpitPointsEdge.get(1))); //左腹部

        //裆部
        List<String> crotchPointsEdge = cs.getCrotchPointsEdge(copyImage, r_kneex, l_kneey, l_kneex);
        coordinateList.addAll(crotchPointsEdge) ;
        map.put("crotch",new PointReturnBO(crotchPointsEdge.get(0))); //裆部

        List<String> kneePointsEdge = cs.getKneePointsEdge(copyImage, l_kneex, l_kneey, r_kneex, r_kneey);
        //膝盖
        coordinateList.addAll(kneePointsEdge) ;
        map.put("left_outer_knee",new PointReturnBO(kneePointsEdge.get(0))); //左膝左
        map.put("left_within_knee",new PointReturnBO(kneePointsEdge.get(1))); //左膝右
        map.put("right_outer_knee",new PointReturnBO(kneePointsEdge.get(2))); //右膝右
        map.put("right_within_knee",new PointReturnBO(kneePointsEdge.get(3))); //右膝左

        //脚部
        List<String> anklePointsEdge = cs.getAnklePointsEdge(copyImage, l_anklex, l_ankley, r_anklex, r_ankley);
        coordinateList.addAll(anklePointsEdge) ;
        map.put("left_outer_foot",new PointReturnBO(anklePointsEdge.get(0))); //左脚左
        map.put("left_within_foot_",new PointReturnBO(anklePointsEdge.get(1))); //左脚右
        map.put("right_outer_foot",new PointReturnBO(anklePointsEdge.get(2))); //右脚右
        map.put("right_within_foot",new PointReturnBO(anklePointsEdge.get(3))); //右脚左

        map.put("left_tiptoe",new PointReturnBO("0","0")); //左脚尖
        map.put("right_tiptoe",new PointReturnBO("0","0")); //右脚尖

        map.put("left_shoulder",new PointReturnBO("0","0")); //左肩
        map.put("right_shoulder",new PointReturnBO("0","0")); //右肩


    /*   String out = this.bodyjdImg(filePath, jdjsonBO, jsonRootBean, coordinateList , image);
         int left = MathUtil.getInt(personInfoBO.getLocation().getLeft());//人物矩形左上角X
        int top = MathUtil.getInt(personInfoBO.getLocation().getTop());//人物矩形左上角Y
        int width = MathUtil.getInt(personInfoBO.getLocation().getWidth());//人物矩形宽
        int height = MathUtil.getInt(personInfoBO.getLocation().getHeight());//人物矩形高

        // 测试输出
        File resultFile = new File(resultPath);
        ImageIO.write(copyImage, "jpg", resultFile);
        File binaryFile = new File(binaryImagePath);
        ImageIO.write(binaryImage, "jpg", binaryFile);*/

    }

//    public List<String> getFootPointsEdge(BufferedImage sourceImage , int l_anklex , int l_ankley , int r_anklex ,int r_ankley ){
//        int return_l_x = 0;
//        int return_l_y = 0;
//        int return_r_x = 0;
//        int return_r_y = 0;
//
//        //获取左脚位置
//        for (int y = l_ankley ; y < sourceImage.getHeight() ; y ++) {
//            for (int x = l_anklex ; x > 0 ; x --) {
//                int rgb = sourceImage.getRGB(x, y);
//                if (rgb == Color.green.getRGB()) {
//                    return_l_x = x;
//                    return_l_y = y;
//                    break;
//                }
//            }
//
//            for (int x = head_x ; x < sourceImage.getWidth() ; x++) {
//                int rgb = sourceImage.getRGB(x, y);
//                if (rgb == Color.green.getRGB()) {
//                    top_x = x;
//                    top_y = y;
//                    break;
//                }
//            }
//        }
//        System.out.println("top_x:" + top_x + " top_y: " + top_y);
//        return (top_x - 1) + ":" + (top_y + 12); //-1 和 12都是修正坐标位置
//    }

    /**
     * 获取腋下边缘点
     * @param sourceImage
     * @param l_elbowx
     * @param l_elbowy
     * @param right_elbowx
     * @param right_elbowy
     * @return
     */
    public List<String> getArmpitPointsEdge(BufferedImage sourceImage , int l_elbowx , int l_elbowy , int right_elbowx ,int right_elbowy ){

        int return_left_x = 0 ;
        int return_left_y = 0 ;
        int return_right_x = 0 ;
        int return_right_y = 0 ;
        boolean finish_left = false ;
        boolean finish_right = false ;
        //左腋下点
        for (int y = l_elbowy ; y > 0 ; y --) {
            for (int x = l_elbowx; x > right_elbowx + (l_elbowx - right_elbowx) / 2; x--) {
                finish_left = false ;
                int rgb = sourceImage.getRGB(x, y);
                if (rgb == Color.green.getRGB()) {
                    return_left_y = y;
                    return_left_x = x ;
                    finish_left = true;
                    break;
                }
            }
            if (finish_left == false) {
                break;
            }
        }
        //右腋下点
        for (int y = right_elbowy ; y > 0 ; y --) {
            for (int x = right_elbowx; x < l_elbowx - (l_elbowx - right_elbowx) / 2; x++) {
                finish_right = false ;
                int rgb = sourceImage.getRGB(x, y);
                if (rgb == Color.green.getRGB()) {
                    return_right_y = y;
                    return_right_x = x ;
                    finish_right = true;
                    break;
                }
            }
            if (finish_right == false) {
                break;
            }
        }
        List<String> result = new ArrayList<>();
        result.add(return_left_x + ":" + return_left_y) ;
        result.add(return_right_x + ":" + return_right_y) ;
        return  result;

    }

    /**
     * 取裆部边缘线点
     * @param sourceImage
     * @param knee_r_x
     * @param knee_l_y
     * @param knee_l_x
     * @return
     */
    public List<String> getCrotchPointsEdge(BufferedImage sourceImage , int knee_r_x , int knee_l_y , int knee_l_x ){
        int crotch_y = 0 ;
        int crotch_x = 0 ;
        boolean finish = false ;
        for (int y = knee_l_y ; y > 0 ; y --) {
            for (int x = knee_r_x; x <= knee_l_x; x++) {
                finish = false ;
                int rgb = sourceImage.getRGB(x, y);
                if (rgb == Color.green.getRGB()) {
                    crotch_y = y;
                    crotch_x = x ;
                    finish = true;
                    break;
                }
            }
            if (finish == false) {
                break;
            }
        }
        List<String> result = new ArrayList<>();
        result.add(crotch_x + ":" + crotch_y) ;
        return  result;
    }
    /**
     * 计算腹部边缘线坐标点
     * @param sourceImage
     * @param l_hipx
     * @param l_hipy
     * @param r_hipx
     * @param l_shouldery
     * @return
     */
    public List<String> getAbdomenPointsEdge(BufferedImage sourceImage , int l_hipx , int l_hipy , int r_hipx , int l_shouldery ){
        int abdomen_y = l_shouldery + (l_hipy - l_shouldery) / 3 * 2 ;// 肩部到臀部的三分之二出为纵坐标高度。
        int x_center = l_hipx - (l_hipx - r_hipx )/2 ; // 算出腹部x轴中心点。
        int abdomen_x_l = 0;
        int abdomen_x_r = 0;

        //往左扫腹部右点
        for (int x = x_center ; x > 0 ; x --) {
            int rgb = sourceImage.getRGB(x, abdomen_y);
            if (rgb == Color.green.getRGB()) {
                abdomen_x_r = x;
                break;
            }
        }
        //往右扫腹部左点
        for (int x = x_center ; x < sourceImage.getWidth() ; x++) {
            int rgb = sourceImage.getRGB(x, abdomen_y);
            if (rgb == Color.green.getRGB()) {
                abdomen_x_l = x;
                break;
            }
        }
        List<String> result = new ArrayList<>();
        result.add(abdomen_x_r + ":" + abdomen_y) ;
        result.add(abdomen_x_l + ":" + abdomen_y) ;
        return  result;

    }
    /**
     * 获取手腕的边缘点
     * @param sourceImage
     * @param wrist_l_x
     * @param wrist_l_y
     * @param wrist_r_x
     * @param wrist_r_y
     * @return
     */
    public List<String> getWristPointsEdge(BufferedImage sourceImage , int wrist_l_x ,int wrist_l_y ,int wrist_r_x , int wrist_r_y ) {
        List<String> result = new ArrayList<>();
        // 返回值 左手腕左边x点
        int return_leftWrist_leftX = 0 ;
        int return_leftWrist_rightX = 0 ;
        int return_rightWrist_rightX = 0 ;
        int return_rightWrist_leftX = 0 ;

        // 左手腕左侧边缘点
        for (int x = wrist_l_x ; x < sourceImage.getWidth() ; x ++) {
            int rgb = sourceImage.getRGB(x, wrist_l_y);
            if (rgb == Color.green.getRGB()) {
                return_leftWrist_leftX = x;
                break;
            }
        }
        // 左手腕  取中心点往三倍内的长度为界限，如果没有去对称点
        for (int x = wrist_l_x ; x >  wrist_l_x - (return_leftWrist_leftX - wrist_l_x) * 3 ; x --) {
            int rgb = sourceImage.getRGB(x, wrist_l_y);
            if (rgb == Color.green.getRGB()) {
                return_leftWrist_rightX = x;
                break;
            }
        }
        if (return_leftWrist_rightX == 0) {
            return_leftWrist_rightX = wrist_l_x + ( wrist_l_x - return_leftWrist_leftX) ;
        }

        // 右腿右侧膝盖点
        for (int x = wrist_r_x ; x > 0 ; x --) {
            int rgb = sourceImage.getRGB(x, wrist_r_y);
            if (rgb == Color.green.getRGB()) {
                return_rightWrist_rightX = x;
                break;
            }
        }

        // 右腿左侧膝盖点  取中心点往三倍内的长度为界限，如果没有去对称点
        for (int x = wrist_r_x ; x <  wrist_r_x + (wrist_r_x - return_rightWrist_rightX) * 3 ; x ++) {
            int rgb = sourceImage.getRGB(x, wrist_r_y);
            if (rgb == Color.green.getRGB()) {
                return_rightWrist_leftX = x;
                break;
            }
        }
        if (return_rightWrist_leftX == 0) {
            return_rightWrist_leftX = wrist_r_x + ( wrist_r_x - return_rightWrist_rightX) ;
        }
        result.add(return_leftWrist_leftX + ":" + wrist_l_y) ;
        result.add(return_leftWrist_rightX + ":" + wrist_l_y) ;
        result.add(return_rightWrist_rightX + ":" + wrist_r_y) ;
        result.add(return_rightWrist_leftX + ":" + wrist_r_y) ;
        return  result;
    }
    /**
     * 获取脚踝边缘点
     * @param sourceImage
     * @param ankle_l_x
     * @param ankle_l_y
     * @param ankle_r_x
     * @param ankle_r_y
     * @return
     */
    public List<String> getAnklePointsEdge(BufferedImage sourceImage , int ankle_l_x ,int ankle_l_y ,int ankle_r_x , int ankle_r_y ) {
        List<String> result = new ArrayList<>();
        // 返回值 左脚踝左边x点
        int return_leftAnkle_leftX = 0 ;
        int return_leftAnkle_rightX = 0 ;
        int return_rightAnkle_rightX = 0 ;
        int return_rightAnkle_leftX = 0 ;

        // 脚踝
        for (int x = ankle_l_x ; x < sourceImage.getWidth() ; x ++) {
            int rgb = sourceImage.getRGB(x, ankle_l_y);
            if (rgb == Color.green.getRGB()) {
                return_leftAnkle_leftX = x;
                break;
            }
        }
        // 脚踝  取中心点往三倍内的长度为界限，如果没有去对称点
        for (int x = ankle_l_x ; x >  ankle_l_x - (return_leftAnkle_leftX - ankle_l_x) * 3 ; x --) {
            int rgb = sourceImage.getRGB(x, ankle_l_y);
            if (rgb == Color.green.getRGB()) {
                return_leftAnkle_rightX = x;
                break;
            }
        }
        if (return_leftAnkle_rightX == 0) {
            return_leftAnkle_rightX = ankle_l_x + ( ankle_l_x - return_leftAnkle_leftX) ;
        }

        // 右脚踝点
        for (int x = ankle_r_x ; x > 0 ; x --) {
            int rgb = sourceImage.getRGB(x, ankle_r_y);
            if (rgb == Color.green.getRGB()) {
                return_rightAnkle_rightX = x;
                break;
            }
        }

        // 右脚踝点  取中心点往三倍内的长度为界限，如果没有去对称点
        for (int x = ankle_r_x ; x <  ankle_r_x + (ankle_r_x - return_rightAnkle_rightX) * 3 ; x ++) {
            int rgb = sourceImage.getRGB(x, ankle_r_y);
            if (rgb == Color.green.getRGB()) {
                return_rightAnkle_leftX = x;
                break;
            }
        }
        if (return_rightAnkle_leftX == 0) {
            return_rightAnkle_leftX = ankle_r_x + ( ankle_r_x - return_rightAnkle_rightX) ;
        }
        result.add(return_leftAnkle_leftX + ":" + ankle_l_y) ;
        result.add(return_leftAnkle_rightX + ":" + ankle_l_y) ;
        result.add(return_rightAnkle_rightX + ":" + ankle_r_y) ;
        result.add(return_rightAnkle_leftX + ":" + ankle_r_y) ;
        return  result;
    }
    /**
     * 获取膝盖点
     * @param sourceImage
     * @param knee_l_x
     * @param knee_l_y
     * @param knee_r_x
     * @param knee_r_y
     * @return
     */
    public List<String> getKneePointsEdge(BufferedImage sourceImage , int knee_l_x ,int knee_l_y ,int knee_r_x , int knee_r_y ) {
        List<String> result = new ArrayList<>();
        // 返回值 左膝盖左边x点
        int return_leftKnee_leftX = 0 ;
        int return_leftKnee_rightX = 0 ;
        int return_rightKnee_rightX = 0 ;
        int return_rightKnee_leftX = 0 ;

        // 左腿左侧膝盖点
        for (int x = knee_l_x ; x < sourceImage.getWidth() ; x ++) {
            int rgb = sourceImage.getRGB(x, knee_l_y);
            if (rgb == Color.green.getRGB()) {
                return_leftKnee_leftX = x;
                break;
            }
        }
        // 左腿右侧膝盖点  取中心点往三倍内的长度为界限，如果没有去对称点
        for (int x = knee_l_x ; x >  knee_l_x - (return_leftKnee_leftX - knee_l_x) * 3 ; x --) {
            int rgb = sourceImage.getRGB(x, knee_l_y);
            if (rgb == Color.green.getRGB()) {
                return_leftKnee_rightX = x;
                break;
            }
        }
        if (return_leftKnee_rightX == 0) {
            return_leftKnee_rightX = knee_l_x + ( knee_l_x - return_leftKnee_leftX) ;
        }

        // 右腿右侧膝盖点
        for (int x = knee_r_x ; x > 0 ; x --) {
            int rgb = sourceImage.getRGB(x, knee_r_y);
            if (rgb == Color.green.getRGB()) {
                return_rightKnee_rightX = x;
                break;
            }
        }

        // 右腿左侧膝盖点  取中心点往三倍内的长度为界限，如果没有去对称点
        for (int x = knee_r_x ; x <  knee_r_x + (knee_r_x - return_rightKnee_rightX) * 3 ; x ++) {
            int rgb = sourceImage.getRGB(x, knee_r_y);
            if (rgb == Color.green.getRGB()) {
                return_rightKnee_leftX = x;
                break;
            }
        }
        if (return_rightKnee_leftX == 0) {
            return_rightKnee_leftX = knee_r_x + ( knee_r_x - return_rightKnee_rightX) ;
        }
        result.add(return_leftKnee_leftX + ":" + knee_l_y) ;
        result.add(return_leftKnee_rightX + ":" + knee_l_y) ;
        result.add(return_rightKnee_rightX + ":" + knee_r_y) ;
        result.add(return_rightKnee_leftX + ":" + knee_r_y) ;
        return  result;
    }
    /**
     * 获取肘关节边缘点
     * @param sourceImage
     * @param elbow_l_x
     * @param elbow_l_y
     * @param elbow_r_x
     * @param elbow_r_y
     * @return
     */
    public List<String> getElbowPointsEdge(BufferedImage sourceImage , int elbow_l_x ,int elbow_l_y ,int elbow_r_x , int elbow_r_y ) {

        List<String> result = new ArrayList<>();
        // 返回值 左臂左边x点
        int return_leftElbow_leftX = 0 ;
        int return_leftElbow_rightX = 0 ;
        int return_rightElbow_rightX = 0 ;
        int return_rightElbow_leftX = 0 ;

        // 左臂左侧肘关节点
        for (int x = elbow_l_x ; x < sourceImage.getWidth() ; x ++) {
            int rgb = sourceImage.getRGB(x, elbow_l_y);
            if (rgb == Color.green.getRGB()) {
                return_leftElbow_leftX = x;
                break;
            }
        }
        // 左臂右侧肘关节点  取中心点往三倍内的长度为界限，如果没有去对称点
        for (int x = elbow_l_x ; x >  elbow_l_x - (return_leftElbow_leftX - elbow_l_x) * 3 ; x --) {
            int rgb = sourceImage.getRGB(x, elbow_l_y);
            if (rgb == Color.green.getRGB()) {
                return_leftElbow_rightX = x;
                break;
            }
        }
        if (return_leftElbow_rightX == 0) {
            return_leftElbow_rightX = elbow_l_x + ( elbow_l_x - return_leftElbow_leftX) ;
        }

        // 右臂右侧肘关节点
        for (int x = elbow_r_x ; x > 0 ; x --) {
            int rgb = sourceImage.getRGB(x, elbow_r_y);
            if (rgb == Color.green.getRGB()) {
                return_rightElbow_rightX = x;
                break;
            }
        }

        // 右臂左侧肘关节点  取中心点往三倍内的长度为界限，如果没有去对称点
        for (int x = elbow_r_x ; x <  elbow_r_x + (elbow_r_x - return_rightElbow_rightX) * 3 ; x ++) {
            int rgb = sourceImage.getRGB(x, elbow_r_y);
            if (rgb == Color.green.getRGB()) {
                return_rightElbow_leftX = x;
                break;
            }
        }
        if (return_rightElbow_leftX == 0) {
            return_rightElbow_leftX = elbow_r_x + ( elbow_r_x - return_rightElbow_rightX) ;
        }
        result.add(return_leftElbow_leftX + ":" + elbow_l_y) ;
        result.add(return_leftElbow_rightX + ":" + elbow_l_y) ;
        result.add(return_rightElbow_rightX + ":" + elbow_r_y) ;
        result.add(return_rightElbow_leftX + ":" + elbow_r_y) ;
        return  result;
    }
    /**
     * 获取脖子边缘线点
     * @param sourceImage
     * @param neck_x
     * @param neck_y
     * @return
     */
    public List<String> getNeckPointsEdge(BufferedImage sourceImage , int neck_x , int neck_y , int top_y) {

        int neck_x_l = 0;
        int neck_x_r = 0;
        int neck_y_reasult = 0;
        int distance = 100000; //因为要比小，所以默认写个很大的数值
        List<String> list = new ArrayList<String>() ;

        for (int y = neck_y; y > top_y ; y--) {
            //从脖子中心点往左上扫
            int x_left = 0;
            int x_right = 0;
            for (int x = neck_x; x > 0; x--) {
                int rgb = sourceImage.getRGB(x, y);
                if (rgb == Color.green.getRGB()) {
                    x_left = x;
                    break;
                }
            }
            //从脖子中心点往右上扫
            for (int x = neck_x; x < sourceImage.getWidth(); x++) {
                int rgb = sourceImage.getRGB(x, y);
                if (rgb == Color.green.getRGB()) {
                    x_right = x;
                    break;
                }
            }
            // 比较大小，y轴取风坐标两点距离的最小值
            if (x_right - x_left < distance) {
                neck_x_l = x_left;
                neck_x_r = x_right;
                neck_y_reasult = y;
                distance = x_right - x_left;
            }
        }
        list.add(neck_x_l + ":" + neck_y_reasult) ;
        list.add(neck_x_r + ":" + neck_y_reasult) ;
        return list ;
    }
    /**
     * 获取头顶边缘线的点
     * @param sourceImage
     * @param head_x
     * @param head_y
     * @return
     */
    public String getHeadPointEdge(BufferedImage sourceImage , int head_x , int head_y) {
        int top_x = 0;
        int top_y = 0;

        for (int y = head_y ; y > 0 ; y --) {
            //从头部中心点往左上扫
            for (int x = head_x ; x > 0 ; x --) {
                int rgb = sourceImage.getRGB(x, y);
                if (rgb == Color.green.getRGB()) {
                    top_x = x;
                    top_y = y;
                    break;
                }
            }

            for (int x = head_x ; x < sourceImage.getWidth() ; x++) {
                int rgb = sourceImage.getRGB(x, y);
                if (rgb == Color.green.getRGB()) {
                    top_x = x;
                    top_y = y;
                    break;
                }
            }
        }
        System.out.println("top_x:" + top_x + " top_y: " + top_y);
        return (top_x - 1) + ":" + (top_y + 12); //-1 和 12都是修正坐标位置
    }


    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getName());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    public static void inputStreamToFile(InputStream ins, File file) {

        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private Boolean section(BigDecimal bigDecimal) {
        if (bigDecimal.compareTo(new BigDecimal(0)) > -1 && bigDecimal.compareTo(new BigDecimal(60)) == -1) {
            return false;//low
        } else {
            return true;//high
        }
    }

    /**
     * 使用百度的脸部点和京东的身体点
     *
     * @param filePath
     * @param jdjsonBO
     * @return
     * @throws Exception
     */
    public String bodyjdImg(String filePath, JdjsonBO jdjsonBO, JsonRootBean jsonRootBean , List<String> coordinateList , BufferedImage image) throws Exception {
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
        g.drawString(".", nosex - 9, nosey + 12);//鼻子
        g.drawString(".", left_earx - 9, left_eary + 12);//左耳
        g.drawString(".", left_eyex - 9, left_eyey + 12);//左眼
        g.drawString(".", right_eyex - 9, right_eyey + 12);//右眼
        g.drawString(".", right_earx - 9, right_eary + 12);//右耳
        g.drawString(".", neck_x - 9, neck_y + 12);//颈部
        g.drawString(".", head_x - 9, head_y + 12);//head(头部)
        g.drawString(".", right_shoulderx - 9, right_shouldery + 12);//右肩
        g.drawString(".", right_elbowx - 9, right_elbowy + 12);//右肘
        g.drawString(".", right_wristx - 9, right_wristy + 12);//右手腕
        g.drawString(".", l_shoulderx - 9, l_shouldery + 12);//l_shoulder（左肩膀）
        g.drawString(".", l_elbowx - 9, l_elbowy + 12);//l_elbow(左手肘)
        g.drawString(".", l_wristx - 9, l_wristy + 12);//l_wrist(左手腕)
        g.drawString(".", r_hipx - 9, r_hipy + 12);//r_hip（右臀部）
        g.drawString(".", r_kneex - 9, r_kneey + 12);//r_knee(右膝盖)
        g.drawString(".", r_anklex - 9, r_ankley + 12);//r_ankle(右脚踝)
        g.drawString(".", l_hipx - 9, l_hipy + 12);//l_hip（左臀部）
        g.drawString(".", l_kneex - 9, l_kneey + 12);//l_knee(左膝盖)
        g.drawString(".", l_anklex - 9, l_ankley + 12);//l_ankle(左脚踝)

        //设置边缘点
        for (String s : coordinateList) {
            g.setColor(Color.green);
            g.drawString(".", Integer.parseInt(s.split(":")[0]), Integer.parseInt(s.split(":")[1]));//
        }
        Font axesFont1 = new Font("微软雅黑", 16, 20);
        g.setColor(Color.RED);
        g.setFont(axesFont1);
        //双肩度数
        g.drawString(bigshoulder + "°", head_x + 1, l_shouldery - 10);
        //髋部度数
        g.drawString(bighip + "°", head_x + 1, l_hipy - 10);
        //膝盖度数
        g.drawString(bigknee + "°", head_x + 1, l_kneey - 10);
        String output = "/www/img" + new Date().getTime() + "_" + new Random().nextInt(1000) + "解析" + "." + "jpg";
        FileOutputStream out = new FileOutputStream(output);//输出图片的地址
        ImageIO.write(image, "jpeg", out);
        out.close();
        return output;
    }

    public static void main(String[] args) throws Exception {
        String sourceFilePath = "/Users/sunshibo/Desktop/body/333.jpg";

        // 取点
        File sourceFile = new File(sourceFilePath);
        InputStream inputStream = new FileInputStream(sourceFile);
        MultipartFile file = new MockMultipartFile(sourceFile.getName(), inputStream);
        CalculateService calculate = new CalculateService();
        calculate.getPoint(file,null);


    }
}
