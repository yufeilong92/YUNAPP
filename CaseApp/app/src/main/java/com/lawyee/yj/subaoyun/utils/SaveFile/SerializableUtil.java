package com.lawyee.yj.subaoyun.utils.SaveFile;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

/**
 * @Author : YFL  is Creating a porject in YFPHILPS
 * @Email : yufeilong92@163.com
 * @Time :2016/12/28 11:08
 * @Purpose :
 */
public class SerializableUtil {
    /**
     *
     * @param list 要保存list数据
     * @param <E>
     * @return
     * @throws IOException
     */
    public static <E> String list2String(List<E> list) throws IOException {
        //实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        //writeObject 方法负责写入特定类的对象的状态，以便相应的readObject可以还原它
        oos.writeObject(list);
        //最后，用Base64.encode将字节文件转换成Base64编码，并以String形式保存
        String listString = new String(Base64.encode(baos.toByteArray(),Base64.DEFAULT));
        //关闭oos
        oos.close();
        return listString;
    }
    /**
     *
     * @param obj 要保存的任意类型的
     * @return
     * @throws IOException
     */
    public static  String obj2Str(Object obj)throws IOException
    {
        if(obj == null) {
            return "";
        }
        //实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        //writeObject 方法负责写入特定类的对象的状态，以便相应的readObject可以还原它
        oos.writeObject(obj);
        //最后，用Base64.encode将字节文件转换成Base64编码，并以String形式保存
        String listString = new String(Base64.encode(baos.toByteArray(),Base64.DEFAULT));
        //关闭oos
        oos.close();
        return listString;
    }


    /**
     *
     * @param str 将保存的任意数据还原成Object
     * @return
     * @throws StreamCorruptedException
     * @throws IOException

     */ //将序列化的数据还原成Object
    public static Object str2Obj(String str) throws StreamCorruptedException,IOException{
        byte[] mByte = Base64.decode(str.getBytes(),Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(mByte);
        ObjectInputStream ois = new ObjectInputStream(bais);
        try {
            return ois.readObject();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param str 将保存的序列化的list还原成list
     * @param <E>
     * @return
     * @throws StreamCorruptedException
     * @throws IOException
     */
    public static <E> List<E> string2List(String str) throws StreamCorruptedException,IOException{
        byte[] mByte = Base64.decode(str.getBytes(),Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(mByte);
        ObjectInputStream ois = new ObjectInputStream(bais);
        List<E> stringList = null;
        try {
            stringList = (List<E>) ois.readObject();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return stringList;
    }
}