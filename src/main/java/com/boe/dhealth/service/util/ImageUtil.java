package com.boe.dhealth.service.util;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

/**
 * 图片裁剪
 */
public class ImageUtil {


    /**
     * 对图片裁剪，并把裁剪完蛋新图片保存 。
     */
    public static String cut(String srcpath, int x, int y, int height, int width) throws IOException {
        String subpath = SystemConfigUtil.getPath() + new Date().getTime() + "_" + new Random().nextInt(1000) + "partition" + ".jpg";
        FileInputStream is = null;
        ImageInputStream iis = null;

        try {
            // 读取图片文件
            is = new FileInputStream(srcpath);
            /*
             * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader
             * 声称能够解码指定格式。 参数：formatName - 包含非正式格式名称 .
             *（例如 "jpeg" 或 "tiff"）等 。
             */
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName("jpg");
            ImageReader reader = it.next();
            // 获取图片流
            iis = ImageIO.createImageInputStream(is);
            /*
             * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索'。
             * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader
             * 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
             */
            reader.setInput(iis, true);
            /*
             * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
             * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件
             * 将从其 ImageReader 实现的 getDefaultReadParam 方法中返回
             * ImageReadParam 的实例。
             */
            ImageReadParam param = reader.getDefaultReadParam();

            /*
             * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
             * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
             */
            Rectangle rect = new Rectangle(x, y, width, height);
            // 提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);
            /*
             * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将
             * 它作为一个完整的 BufferedImage 返回。
             */
            BufferedImage bi = reader.read(0, param);
            // 保存新图片
            ImageIO.write(bi, "jpg", new File(subpath));
        } finally {
            if (is != null)
                is.close();
            if (iis != null)
                iis.close();
        }
        return subpath;
    }


    /**
     * 获取远程网络图片信息
     *
     * @param imageURL
     * @return
     */
    public static BufferedImage getRemoteBufferedImage(String imageURL) {
        URL url = null;
        InputStream is = null;
        BufferedImage bufferedImage = null;
        try {
            url = new URL(imageURL);
            is = url.openStream();
            bufferedImage = ImageIO.read(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("imageURL: " + imageURL + ",无效!");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("imageURL: " + imageURL + ",读取失败!");
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("imageURL: " + imageURL + ",流关闭异常!");
                return null;
            }
        }
        return bufferedImage;
    }


    /**
     * 创建 & 压缩图片
     *
     * @param file
     * @param ratio
     * @return
     * @throws IOException
     */
    public static String getPCpicture(MultipartFile file, double ratio) throws IOException {

        String imgurl = SystemConfigUtil.getPath() + new Date().getTime() + "_" + new Random().nextInt(1000) + ".jpg";
        ImageUtil.RotateImg(file, imgurl);
        Image img = ImageIO.read(new File(imgurl));      // 取文件流构造Image对象
        int widthR = img.getWidth(null);    // 得到源图宽
        int heightR = img.getHeight(null);
        //压缩之后的宽高
        int widthW = 0;
        int heightW = 0;

        if (ratio > 0) {
            widthW = (int) (widthR / ratio);
            heightW = (int) (heightR / ratio);
        }

        BufferedImage image = new BufferedImage(widthW, heightW, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img, 0, 0, widthW, heightW, null); // 绘制缩小后的图
        String result = "";
        String name = file.getOriginalFilename();
        name = name.replace(".", ",");
        String[] str = name.split(",");//获取文件后缀名
        String fileName = new Date().getTime() + "_" + new Random().nextInt(1000) + "." + str[str.length - 1];

        String filePath = SystemConfigUtil.getPath() + fileName;
        ImageIO.write(image, str[str.length - 1], new File(filePath));
        result = fileName;

        return result;
    }


    public static String reduceImg(String imgsrc) {
        String imgdist = SystemConfigUtil.getPath() + new Date().getTime() + "_" + new Random().nextInt(1000) + ".jpg";
        try {
            File srcfile = new File(imgsrc);
            // 检查图片文件是否存在
            if (!srcfile.exists()) {
                System.out.println("文件不存在");
            }
            int[] results = getImgWidthHeight(srcfile);

            int widthdist = results[0];
            int heightdist = results[1];
            // 开始读取文件并进行压缩
            Image src = ImageIO.read(srcfile);

            // 构造一个类型为预定义图像类型之一的 BufferedImage
            BufferedImage tag = new BufferedImage(widthdist, heightdist, BufferedImage.TYPE_INT_RGB);

            // 这边是压缩的模式设置
            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);

            //创建文件输出流
            FileOutputStream out = new FileOutputStream(imgdist);
            //将图片按JPEG压缩，保存到out中
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            //关闭文件输出流
            out.close();
        } catch (Exception ef) {
            ef.printStackTrace();
        }
        return imgdist;
    }

    /**
     * 获取图片宽度和高度
     *
     * @param file 图片路径
     * @return 返回图片的宽度
     */
    public static int[] getImgWidthHeight(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int result[] = {0, 0};
        try {
            // 获得文件输入流
            is = new FileInputStream(file);
            // 从流里将图片写入缓冲图片区
            src = ImageIO.read(is);
            // 得到源图片宽
            result[0] = src.getWidth(null);
            // 得到源图片高
            result[1] = src.getHeight(null);
            is.close();  //关闭输入流
        } catch (Exception ef) {
            ef.printStackTrace();
        }
        return result;
    }

    /**
     * 旋转图片
     *
     * @param filePath
     * @param newFilePath 在读取手机照片的时候会存在旋转现象
     * @return
     */
    public static boolean RotateImg(MultipartFile filePath, String newFilePath) {
        try {
            InputStream ins = filePath.getInputStream();
            File file = new File(filePath.getOriginalFilename());
            FileUtil.inputStreamToFile(ins, file);
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            Directory directory = metadata.getFirstDirectoryOfType(ExifDirectoryBase.class);
            int orientation = 0;
            // Exif信息中有保存方向,把信息复制到缩略图
            // 原图片的方向信息
            if (directory != null && directory.containsTag(ExifDirectoryBase.TAG_ORIENTATION)) {
                orientation = directory.getInt(ExifDirectoryBase.TAG_ORIENTATION);
            }
            int angle = 0;
            if (6 == orientation) {
                //6旋转90
                angle = 90;
            } else if (3 == orientation) {
                //3旋转180
                angle = 180;
            } else if (8 == orientation) {
                //8旋转90
                angle = 270;
            }
            BufferedImage src = ImageIO.read(file);
            BufferedImage des = Rotate(src, angle);
            String filename = file.getName();
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            ImageIO.write(des, ext, new File(newFilePath));
            return true;
        } catch (JpegProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MetadataException e) {
            e.printStackTrace();
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static BufferedImage Rotate(Image src, int angel) {
        int src_width = src.getWidth(null);
        int src_height = src.getHeight(null);
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
                src_width, src_height)), angel);

        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // transform
        g2.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

        g2.drawImage(src, null, null);
        return res;
    }

    public static Rectangle CalcRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }

    /**
     *      * 将图片转换成Base64编码
     *      * @param imgFile 待处理图片
     *      * @return
     *     
     */
    public static String getImgStr(String imgFile) {
   // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
     // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

}
