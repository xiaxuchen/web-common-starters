package com.originit.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Slf4j
public class FileUDUtil {



    public static Logger logger = LoggerFactory.getLogger(FileUDUtil.class);



    /**
     * 处理下载文件时的文件名中文乱码问题，兼容浏览器
     * @param filename 源文件名
     * @param agent 浏览器的user-agent
     */
    private static String resolveDownloadFileName(String filename, String agent){
        try {
            if (agent.contains("MSIE"))
            {
                // IE浏览器
                filename = URLEncoder.encode(filename, "utf-8");
                filename = filename.replace("+", " ");
            }
            else if (agent.contains("Firefox"))
            {
                // 火狐浏览器
                filename = new String(filename.getBytes(), "ISO8859-1");
            }
            else if (agent.contains("Chrome"))
            {
                // google浏览器
                filename = URLEncoder.encode(filename, "utf-8");
            }
            else
            {
                // 其它浏览器
                filename = URLEncoder.encode(filename, "utf-8");
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.buildErrorMessage(e));
            throw new IllegalStateException("文件名编码异常");
        }
        return filename;
    }

    /**
     * 将path的文件转换为ResponseEntity
     * @param code 文件所在文件夹路径
     * @param filename 文件名
     * @param agent 浏览器的user-agent
     */
    public static void downloadFile(String code, String filename, String agent, HttpServletResponse resp) {
        String realPath = new String(Base64.getDecoder().decode(code), StandardCharsets.UTF_8);
        if (filename == null) {
            filename = realPath.substring(realPath.lastIndexOf(File.separator) + 1);
        }
        log.info("【初始化文件上传工具类】current download file name is {}", filename);
        downloadFileWithPath("",realPath,filename,agent,resp);
    }

    /**
     * 获取对应code的文件
     * @param code 上传文件生成的code
     */
    public static File getFile (String code) {
        if (code == null) {
            return null;
        }
        String realPath = new String(Base64.getDecoder().decode(code), StandardCharsets.UTF_8);
        // 获取目录下的资源
        return getFileByPath(realPath);
    }

    public static void downloadFileWithPath (String relativePath,String realPath, String filename, String agent, HttpServletResponse resp) {
        try {
            File file;
            // 获取目录下的资源,如果指定了相对的路径就使用相对路径
            if (relativePath == null || relativePath.trim().isEmpty()){
                file = getFileByPath(realPath);
            } else {
                file = new File(FileUDUtil.class.getClassLoader().getResource(relativePath).getPath(), realPath);
            }
            if (filename == null) {
                filename = realPath.substring(realPath.lastIndexOf(File.separator) + 1);
            }
            resp.reset();
//          // 让浏览器显示下载文件对话框
            resp.setContentType(MediaType.parseMediaType(Files.probeContentType(Paths.get(file.getAbsolutePath()))).getType());
            resp.setCharacterEncoding("utf-8");
            resp.setContentLength((int) file.length());
            resp.setHeader("Content-Disposition", "attachment;filename="+resolveDownloadFileName(filename,agent));
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os = resp.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                int i = 0;
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("文件下载异常");
        }
    }

    /**
     * 获取默认的头像
     * @return 默认头像
     */
    public static File getDefaultHeadImg () {
        return new File(FileUDUtil.class.getClassLoader().getResource("images").getPath(), "/default_logo.jpg");
    }


    /**
     * 通过文件名获取hash
     * @param filename 文件名
     * @return 路径
     */
    private static String getSavingPath(String path, String filename) {
        //得到hashCode
        int hashcode = filename.hashCode();
        //得到名为1到16的下及文件夹
        int dir1 = hashcode & 0xf;
        //得到名为1到16的下下及文件夹
        int dir2 = (hashcode & 0xf0) >> 4;
        path = FilenameUtils.separatorsToSystem(path);
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        //得到文件路径
        String fileSavePath = path + dir1 + File.separator + dir2 + File.separator +
                UUID.randomUUID().toString().replace("-","") +
                filename.substring(filename.lastIndexOf("."));
        return fileSavePath;
    }
    /***
     * 上传文件
     * @param inputStream 文件的输入流
     * @param pathPrefix 路径前缀
     * @param filename 文件名
     * @return 文件的编码
     */
    public static String saveFile(InputStream inputStream,String pathPrefix, String filename) {
        try {
            String path = getSavingPath(pathPrefix, filename);
            mkdirIfNotExist(path);
            File file = getFileByPath(path);
            // TODO 修改
            FileCopyUtils.copy(inputStream,new FileOutputStream(file));
            log.info("save file at the position path in {}",file.getAbsolutePath());
            // 返回base64编码
            return  Base64.getEncoder().encodeToString(path.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("文件上传异常");
        }
    }

    /**
     * 创建文件夹
     * @param path 路径
     */
    private static void mkdirIfNotExist(String path) {
        File file = getFileByPath(path.substring(0,path.lastIndexOf(File.separator)));
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 获取文件的系统url
     * @param code 编码code
     * @return
     */
    public static String getSystemURL (String code) {
        if (code == null) {
            return null;
        }
        try {
            final String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String url = "http://" + hostAddress + "/union" + "/resource/file/" + code;
            logger.warn("the ip is {}",url);
            return url;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.error("the ip is null,error {}",ExceptionUtil.buildErrorMessage(e));
        }
        return null;
    }

    /**
     * 统一的获取文件
     * @param path 文件路径
     * @return 文件
     */
    private static File getFileByPath(String path){
        return new File(path);
//        return  new File(FileUDUtil.class.getClassLoader().getResource("").getPath(), path);
    }

}
