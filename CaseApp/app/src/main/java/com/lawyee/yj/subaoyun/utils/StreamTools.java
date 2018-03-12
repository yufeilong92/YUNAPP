package com.lawyee.yj.subaoyun.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yfl on 2016/11/7 15:29.
 *
 * @purpose:
 */

public class StreamTools {
    //        [1]把字节流转换成字符流
    public static String ReadStream(InputStream inputStream) {
        //        [2]定义个内存输出流
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //        [3]顶一个常量来判断
            int len = -1;
            //        [4]字节流
            byte[] bytes = new byte[1024];
            //        [5]判断其流是否为空
            while ((len = inputStream.read(bytes)) != -1) {
                //        [6]不为空则内存写入
                /**
                 * void write(byte[] b, int off, int len)
                 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte 数组输出流。
                 */
                baos.write(bytes, 0, len);
            }
            inputStream.close();
            //        [7]定义个心得输出流
            /**
             * toByteArray()
             创建一个新分配的 byte 数组。
             */
            String s = new String(baos.toByteArray());
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
