package com.originit.common.utils;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;

@Slf4j
public class ResourceUtil {

    public static void getFile2Response(String path, String filename, String agent, HttpServletResponse response) {
        try {
            ResponseEntity<byte[]> r = getFile2BytesInternal(path, filename, agent);
            response.setStatus(r.getStatusCode().value());
            r.getHeaders().forEach((s, strings) -> {
                if (strings.size() == 1){
                    response.setHeader(s,strings.get(0));
                } else {
                    for (String string : strings) {
                        response.addHeader(s,string);
                    }
                }
            });
            IoUtil.write(response.getOutputStream(),false,r.getBody());
        } catch (Exception e) {
            log.error("exception:{}\r\nmsg:{}\r\ntrace:{}",e.getClass(),e.getMessage(), cn.hutool.core.exceptions.ExceptionUtil.stacktraceToString(e));
            throw new RuntimeException(e);
        }
    }

    public static ResponseEntity<byte[]> getFile2Bytes(String path, String filename, String agent, HttpServletResponse request) {
        try {
            return getFile2BytesInternal(path,filename,agent);
        } catch (Exception e) {
            log.error("exception:{}\r\nmsg:{}\r\ntrace:{}",e.getClass(),e.getMessage(),cn.hutool.core.exceptions.ExceptionUtil.stacktraceToString(e));
            throw new RuntimeException(e);
        }
    }

    private static ResponseEntity<byte[]> getFile2BytesInternal(String path,String filename,String agent) throws IOException {
        // 获取目录下的资源
        ClassPathResource cpr = new ClassPathResource(path+ File.separatorChar + filename);
        // 获得请求头中的User-Agent
        // 根据不同的客户端进行不同的编码
        String filenameEncoder;
        if (agent != null) {
            if (agent.contains("MSIE")){
                // IE浏览器
                filenameEncoder = URLEncoder.encode(filename, "utf-8");
                filenameEncoder = filenameEncoder.replace("+", " ");
            }
            else if (agent.contains("Firefox"))
            {
                // 火狐浏览器
                BASE64Encoder base64Encoder = new BASE64Encoder();
                filenameEncoder = "=?utf-8?B?" + base64Encoder.encode(filename.getBytes(Charset.forName("utf-8"))) + "?=";
            }else { // 其它浏览器
                filenameEncoder = URLEncoder.encode(filename, "utf-8");
            }
        }
        else { // 其它浏览器
            filenameEncoder = URLEncoder.encode(filename, "utf-8");
        }
        // 模板代码，装配ResponseEntity
        InputStream input = cpr.getInputStream();
        byte[] bytes = IoUtil.readBytes(input);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaTypeFactory.getMediaType(filename).orElse(MediaType.MULTIPART_FORM_DATA));
        // 让浏览器显示下载文件对话框
        headers.add("Content-Disposition", "attchement;filename="+filenameEncoder);
        HttpStatus statusCode = HttpStatus.OK;
        return new ResponseEntity<>(bytes, headers, statusCode);
    }
}
