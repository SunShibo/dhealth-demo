package com.boe.dhealth.service.util;


import com.wxapi.WxApiCall.WxApiCall;
import com.wxapi.model.RequestModel;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.*;

public class BodyKeyPointUtil {
    //FIXME 这里需要替换成正式的Key 现在为测试使用并不稳定随时可能被停止
    //百度AI 开放平台
    private static String clientId = "sbjRmx1aqUBE1FRicpKfh4M1";
    private static String  clientSecret = "7T6E3sajEAqkA38klRVGaXusNYB0QbZC";
    //京东开放平
    private static String appkey = "0e39d25ff7f437889e331ca2039a919a";
    private static String secretKey = "a16006bca35147ea8b68d71dba83904d";
    /**
     * 百度云
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getBodyKeyPoint(String filePath)throws Exception{
            String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_analysis";   // 请求url
            byte[] imgData = FileUtil.readFileByBytes(SystemConfigUtil.getPath()+filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();
            String result = HttpUtil.post(url, accessToken, param);
            return result;
    }

    /**
     * 京东云
     * @param
     * @return
     * @throws Exception
     */
    public static String getBodyKeyPointJd(String filePath){
        // 请求url
        String url = "https://aiapi.jd.com/jdai/pose_estimation";
        int muti_det = 1;//单人姿态预测或多人姿态预测；当值为1时，实现单人姿态预测；当值为2时，实现多人姿态预测
        RequestModel model = new RequestModel();
        model.setGwUrl(url);
        model.setAppkey(appkey);
        model.setSecretKey(secretKey);
        model.setFilePath(filePath);
        Map<String, Object> queryMap = new HashMap();
        queryMap.put("muti_det", muti_det);
        model.setQueryParams(queryMap);
        WxApiCall call = new WxApiCall();
        call.setModel(model);
        String string = call.request();
        System.out.println(string);
        return string;
    }

    /**
     * 百度获取Token
     * @return
     */
    public static String getAuth() {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + clientId
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + clientSecret;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }
}