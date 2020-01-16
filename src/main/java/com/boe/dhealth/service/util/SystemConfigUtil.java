package com.boe.dhealth.service.util;

import java.io.File;

/**
 * 获取文件路径
 */
public class SystemConfigUtil {

    //FIXME  这里在不同环境创建路径可能没有权限导致失败
    private static  String path;
    static{
        File file;
        //判断是不是linux 系统
        if(System.getProperty("os.name").toLowerCase().contains("linux")){
             file = new File("/usr/dhealthImg/");
        }else{
            file = new File("C:\\dhealthImg\\");
        }
        //不存在创建路径
        if(!file .exists()){
            file .mkdir();
        }
        path=file.getPath()+File.separator;
    }

    /**
     * 返回路径
     * @return
     */
    public static String getPath(){
        return  path;
    }
}
