package com.hrbustsecond.util;

import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class HttpRequest {

    public static String callHttpPost(StringBuilder url,String hostIP, int port, String actionName, Map<String,String> paramsMap)
            throws UnsupportedEncodingException {
        JSONObject resultJSon = new JSONObject();
        String callbackResult = "";
        System.err.println("请求参数是： IP" + hostIP + "   actionName:" + actionName );//+ "  params:" + paramsMap
        HttpClient httpClient = HttpClientBuilder.create().build();
        //url
        //StringBuilder url = getIPandPort(hostIP,port);
        if ( !"".equals(actionName) && actionName != null) {
            url.append("/").append(actionName);
        }
        System.out.println(url);
        //参数（字典）
        String str ="";
        if(paramsMap!=null &&  paramsMap.size()!=0){
            str = JSONObject.fromObject(paramsMap).toString();
        }
        //System.err.println("请求参数为："+ str);
        HttpPost httpPost = new HttpPost(url.toString());
        StringEntity entity = new StringEntity(str, "utf-8");
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            String entityString = EntityUtils.toString(response.getEntity());
            System.err.println("请求返回码为："+ String.valueOf(statusCode)+"  返回结果为:" +entityString);
            if(statusCode==200 ){
                callbackResult = entityString;
            }else{
                resultJSon.put("retCode",-1);
                resultJSon.put("retMsg","请求异常，请检查请求内容");
                resultJSon.put("resultCode",statusCode);
                callbackResult = resultJSon.toString();
            }
        } catch (Exception e) {
            System.err.println("客户端连接异常："+e.getMessage());
            resultJSon.put("retCode",-2);
            resultJSon.put("retMsg","连接异常，请检查客户端机服务是否正常开启");
            resultJSon.put("resultCode",500);
            callbackResult = resultJSon.toString();
            e.printStackTrace();
        }
        return callbackResult;
    }


    //拼接url
    public static StringBuilder getIPandPort(String hostIP,int port){
        StringBuilder url = new StringBuilder("http://");
        if (hostIP != null && !"".equals(hostIP) ) {
            url.append(hostIP).append(":").append(String.valueOf(port));
        }
        return url;
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
