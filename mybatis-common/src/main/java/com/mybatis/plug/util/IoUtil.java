package com.mybatis.plug.util;

import com.mybatis.plug.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author coco
 * @date 2020-08-05 20:05
 **/
public class IoUtil {

    private static final Logger LOG = LoggerFactory.getLogger(IoUtil.class);

    public static byte[] readInputStream(InputStream inputStream, String inputStreamName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[16 * 1024];
        try {
            int bytesRead = inputStream.read(buffer);
            while (bytesRead != -1) {
                outputStream.write(buffer, 0, bytesRead);
                bytesRead = inputStream.read(buffer);
            }
        } catch (Exception e) {
            throw new CommonException("couldn't read input stream " + inputStreamName, e);
        }
        return outputStream.toByteArray();
    }


    public static void closeSilently(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ignore) {
            LOG.debug("关闭流失败！reason: {}",ignore.getMessage(),ignore);
            // Exception is silently ignored
        }
    }

    public static void closeSilently(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException ignore) {
            LOG.debug("关闭流失败！reason: {}",ignore.getMessage(),ignore);
            // Exception is silently ignored
        }
    }
}
