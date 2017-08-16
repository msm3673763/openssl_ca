package com.ucsmy.ucas.ca.utils;


import com.ucsmy.ucas.ca.Exception.CertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Pattern;

/**
 * 文件相关工具类
 *
 * @author ucs_masiming
 *
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    /**
     * 过滤符合filter的文件
     *
     * @param filter
     */
    public boolean matchFileName(String filter,File file) {
        if ("".equals(filter))
            return true;
        return Pattern.compile(filter).matcher(file.getName()).find();
    }

    /**
     * 将字符串存储到文件中
     * @param filePath
     * @param fileName
     * @param str
     * @return
     */
    public static boolean stringToFile(String filePath, String fileName,
                                       String str) throws IOException {
        File f = new File(filePath);
        f.mkdirs();//确保文件夹存在
        File file = new File(f, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("创建文件失败！", e);
                return false ;
            }
        }

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            bos.write(str.getBytes("UTF-8"));
            bos.flush();
        }
        return true;
    }

    /**
     * 删除文件夹中的所有内容
     *
     * @param sourceDir
     */
    public static void clearDirFiles(String sourceDir) throws CertException {
        try {
            File file = new File(sourceDir);
            if (!file.exists()) {
                return;
            }
            if (!file.isDirectory()) {
                return;
            }
            String[] tempList = file.list();
            File temp;
            for (int i = 0; i < tempList.length; i++) {
                if (sourceDir.endsWith(File.separator)) {
                    temp = new File(sourceDir + tempList[i]);
                } else {
                    temp = new File(sourceDir + File.separator + tempList[i]);
                }
                if (temp.isFile()) {
                    temp.delete();
                }
                if (temp.isDirectory()) {
                    clearDirFiles(sourceDir + "/" + tempList[i]);// 先删除文件夹里面的文件
                    clearDirFiles(sourceDir + "/" + tempList[i]); // 删除完里面所有内容
                    String filePath = sourceDir + "/" + tempList[i];
                    java.io.File myFilePath = new java.io.File(filePath);
                    myFilePath.delete(); // 删除空文件夹
                }
            }
            return;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new CertException(e.getMessage(), e);
        }
    }

    /**
     * 删除文件夹中的所有内容
     *
     * @param sourceFolder
     */
    public static void clearFileOrFolder(String sourceFolder) throws CertException {
        try {
            //删除文件夹里面的所有内容
            clearDirFiles(sourceFolder);
            //删除该文件夹
            File file = new File(sourceFolder);
            if (!file.exists()) {
                return;
            }
            file.delete();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new CertException(e.getMessage(), e);
        }
    }

    /**
     * 读取文件，返回二进制数组
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static byte[] readFile(String filePath) throws IOException, CertException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            logger.error("file too big...");
            throw new CertException("file too big");
        }

        try (FileInputStream in = new FileInputStream(filePath)) {
            byte[] buffer = new byte[(int) fileSize];
            int offset = 0;
            int numRead = 0;
            while (offset < buffer.length
                    && (numRead = in.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }

            // 确保所有数据均被读取
            if (offset != buffer.length) {
                throw new CertException("Could not completely read file " + file.getName());
            }
            return buffer;
        }
    }

    /**
     * 保存二进制流到文件
     *
     * @param filePath
     * @param byteArr
     * @throws Exception
     */
    public static void saveFile(String filePath, byte[] byteArr) throws IOException {
        if (byteArr != null && byteArr.length > 0) {
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                out.write(byteArr);
                out.flush();
            }
        }
    }

    /**
     * 拷贝文件
     *
     * @param srcPath
     * @param descPath
     * @throws Exception
     */
    public static void copyFile(String srcPath, String descPath) throws IOException {
        try (FileInputStream fis = new FileInputStream(srcPath);
             FileOutputStream fos = new FileOutputStream(descPath)) {
            byte[] buff = new byte[1024];
            int readed = -1;
            while((readed = fis.read(buff)) > 0)
                fos.write(buff, 0, readed);
        }
    }

    /**
     * 拷贝文件夹
     *
     * @param srcPath
     * @param destPath
     * @throws Exception
     */
    public static void copyDir(String srcPath, String destPath) throws Exception {
        File src = new File(srcPath);
        if (src.isFile()) {
            copyFile(srcPath, destPath);
        } else if (src.isDirectory()) {
            //确保目标文件夹存在
            File dest = new File(destPath);
            dest.mkdirs();
            //获取下一级目录|文件
            for (File sub : src.listFiles()) {
                copyDir(sub.getAbsolutePath(), new File(dest, sub.getName()).getAbsolutePath());
            }
        }

    }



    /**
     * 获取文件大小
     *
     * @param file
     * @param format
     *            格式，可以为b,k,m。b以byte为单位返回，k以kb为单位返回，m以兆为单位返回
     * @return
     */
    public static double getFileSize(File file, String format) throws IOException {
        long result = 0;
        try (FileInputStream in = new FileInputStream(file)) {
            result = in.available();
        }
        if (format == null || "".equalsIgnoreCase(format.trim()) || "B".equalsIgnoreCase(format.trim())) {
            return result;
        } else if ("K".equalsIgnoreCase(format.trim())) {
            return result / 1024d;
        } else if ("M".equalsIgnoreCase(format.trim())) {
            return result / 1024d / 1024d;
        } else {
            throw new IllegalArgumentException("format invalid value!");
        }
    }
    /**
     * 建路径
     * @param path
     */
    public static void createDir(String path) {
        File file=new File(path);
        try{
            if(!file.exists()){
                file.mkdirs();
            }
        }catch (Exception e) {
            logger.error("创建目录失败", e);
        }
    }

    /**
     * 下载文件
     * @param httpServletResponse
     * @param reportPath	文件路径(含文件名)
     * @param fileName		下载显示的文件名
     * @throws Exception
     */
    public static void downLoadFile(HttpServletResponse httpServletResponse,
                                    String reportPath, String fileName) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(reportPath));
             OutputStream os = httpServletResponse.getOutputStream()) {
            //下载时显示的文件名
            String name = new String(fileName.getBytes("UTF-8"), "GBK"); // 处理中文文件名的问题
            httpServletResponse.setContentType("application/octet-stream");
            httpServletResponse.setHeader("Content-disposition", "attachment; filename=" + name);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                os.write(buff, 0, bytesRead);
            }
            os.flush();
        }
    }

    /**
     * 下载文件流
     * @param httpServletResponse
     * @param fileName
     * @param fileBytes
     * @throws Exception
     */
    public static void downLoadFileBytes(HttpServletResponse httpServletResponse,
                                         String fileName, byte[] fileBytes) throws CertException {
        OutputStream os=null;
        try {
            //下载时显示的文件名
            String fileNameShow = java.net.URLEncoder.encode(fileName, "UTF-8"); // 处理中文文件名的问题
            fileNameShow = new String(fileNameShow.getBytes("UTF-8"), "GBK"); // 处理中文文件名的问题
            httpServletResponse.setContentType("application/octet-stream");
            httpServletResponse.setHeader("Content-disposition", "attachment; filename=" + fileNameShow);
            os=httpServletResponse.getOutputStream();
            os.write(fileBytes);
            os.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new CertException("下载文件失败!" + e.getMessage());
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 判断文件是否存在
     * @param filePath 文件路径
     * @param isFile 是否是文件类型，为true时，只有文件路径指定的file是文件才返回true
     * @return
     */
    public static boolean isFileExist(String filePath, boolean isFile){
        if(filePath == null){
            return false;
        }
        File file = new File(filePath);
        if (file.exists()){
            return isFile && !file.isFile() ? false : true;
        }
        return false;
    }
}
