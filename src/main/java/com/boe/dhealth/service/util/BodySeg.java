package com.boe.dhealth.service.util;


//import com.baidu.ai.aip.utils.Base64Util;
//import com.baidu.ai.aip.utils.FileUtil;
//import com.baidu.ai.aip.utils.HttpUtil;

import net.sf.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * 人像分割
 */
public class BodySeg {

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String body_seg(File file) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_seg";
        try {

            byte[] imgData = FileUtil.readFileByFile(file);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = BodyKeyPointUtil.getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println("result" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String body_seg(BufferedImage image ) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_seg";
        try {

            byte[] imgData = FileUtil.readFileByBufferedImage(image);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = BodyKeyPointUtil.getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println("result" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static BufferedImage convert(String labelmapBase64, int realWidth, int realHeight) {
        try {

            byte[] bytes = Base64.getDecoder().decode(labelmapBase64);
            InputStream is = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(is);
            BufferedImage newImage = resize(image, realWidth, realHeight);
            BufferedImage grayImage = new BufferedImage(realWidth, realHeight, BufferedImage.TYPE_BYTE_BINARY);
//            BufferedImage grayImage = new BufferedImage(realWidth, realHeight, BufferedImage.TYPE_BYTE_GRAY);
            for (int i = 0; i < realWidth; i++) {
                for (int j = 0; j < realHeight; j++) {
                    int rgb = newImage.getRGB(i, j);
                    grayImage.setRGB(i, j, rgb * 255);  //将像素存入缓冲区
                }
            }
            return grayImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static  List<String> setRGB(BufferedImage binaryImage, BufferedImage sourceImage, String resultPath) throws IOException {

        int width = binaryImage.getWidth();
        int height = binaryImage.getHeight();
        System.out.println("width:" + width + " height:" + height);
        // 将BufferedImage 转Graphics2D 划线
        Graphics2D g = sourceImage.createGraphics();
        Graphics2D binaryGraphics = binaryImage.createGraphics();
        //画笔颜色
        g.setColor(Color.green);
        binaryGraphics.setColor(Color.green);
        List<String> coordinateList = new ArrayList<String>();
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int rgb = binaryImage.getRGB(i, j);
                int upRgb = binaryImage.getRGB(i - 1, j);
                int downRgb = binaryImage.getRGB(i + 1, j);
                int leftRgb = binaryImage.getRGB(i, j - 1);
                int rightRgb = binaryImage.getRGB(i, j + 1);
                int leftUpRgb = binaryImage.getRGB(i - 1, j - 1);
                int leftDownRgb = binaryImage.getRGB(i + 1, j - 1);
                int rightUpRgb = binaryImage.getRGB(i - 1, j + 1);
                int rightDownRgb = binaryImage.getRGB(i + 1, j + 1);

                if ((i != 0 && j != 0) && ((rgb != upRgb) || (rgb != downRgb) || (rgb != leftRgb) ||
                        (rgb != rightRgb) || (rgb != leftUpRgb) || (rgb != leftDownRgb) || (rgb != rightUpRgb) || (rgb != rightDownRgb))) {
                    coordinateList.add(i + ":" + j);
                }
            }
        }
        // 描边
        for (String s : coordinateList) {
            g.drawString("•", Integer.parseInt(s.split(":")[0]), Integer.parseInt(s.split(":")[1]));
        }
        return coordinateList;

//        File newFile = new File(resultPath);
//        ImageIO.write(sourceImage, "jpg", newFile);
    }


    public static BufferedImage getPicEdge(BufferedImage originalPic) {
        int imageWidth = originalPic.getWidth();
        int imageHeight = originalPic.getHeight();

        BufferedImage newPic = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_3BYTE_BGR);

        float[] elements = {0.0f, -1.0f, 0.0f, -1.0f, 4.0f, -1.0f, 0.0f,
                -1.0f, 0.0f};

        Kernel kernel = new Kernel(3, 3, elements);
        ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        cop.filter(originalPic, newPic);
        return newPic;
    }

    /**
     * 计算每个像素点的距离
     * @param top_x
     * @param down_x
     * @param height
     */
    public int calculate(int top_x , int down_x , int height) {
        int unit_distance = height / (down_x - top_x) ;
        return unit_distance;
    }
//    public static void main(String[] args) throws IOException {
//        String path = "/Users/sunshibo/Desktop/body/123456.png";
//        String writePath = "/Users/sunshibo/Desktop/body/result2.jpeg";
//        File file = new File(path);
//        BufferedImage sourceImage = ImageIO.read(file);
//        BufferedImage picEdge = BodySeg.getPicEdge(sourceImage);
//        File newFile = new File(writePath);
//        ImageIO.write(sourceImage, "jpg", newFile);
//    }

    public static void main(String[] args) throws IOException {
        String sourcePath = "/Users/sunshibo/Desktop/body/body2.jpg";
        String binaryImagePath = "/Users/sunshibo/Desktop/body/binaryImage.jpg";//二值图
        String resultPath = "/Users/sunshibo/Desktop/body/resultImage.jpg";//处理原图然后输出

        File sourceFile = new File(sourcePath);
        String base64 = BodySeg.body_seg(sourceFile);
        JSONObject resultJson = JsonUtils.getJsonObject4JavaPOJO(base64);
        File file = new File(sourcePath);

        //生成二值图然后输出二值图
        BufferedImage sourceImage = ImageIO.read(file);
        BufferedImage binaryImage = BodySeg.convert(resultJson.getString("labelmap"), sourceImage.getWidth(), sourceImage.getHeight());
        File newFile = new File(binaryImagePath);
        ImageIO.write(binaryImage, "jpg", newFile);
        BodySeg.setRGB(binaryImage, sourceImage, resultPath);
    }
}