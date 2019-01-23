package com.wxscistor.util;

import java.util.ArrayList;

/**
 *@Description:
 */
public class Test {

    @org.junit.Test
    public void tst1(){
        ArrayList arr = new ArrayList();

        arr.add(1);
        arr.add(2);
        System.out.println(arr);
    }

    /**
     * @param md5L16
     * @return
     * @Date:2014-3-18
     * @Author:lulei
     * @Description: 将16位的md5转化为long值
     */
    public static long parseMd5L16ToLong(String md5L16){
        if (md5L16 == null) {
            throw new NumberFormatException("null");
        }
        md5L16 = md5L16.toLowerCase();
        byte[] bA = md5L16.getBytes();
        long re = 0L;
        for (int i = 0; i < bA.length; i++) {
            //加下一位的字符时，先将前面字符计算的结果左移4位
            re <<= 4;
            //0-9数组
            byte b = (byte) (bA[i] - 48);
            //A-F字母
            if (b > 9) {
                b = (byte) (b - 39);
            }
            //非16进制的字符
            if (b > 15 || b < 0) {
                throw new NumberFormatException("For input string '" + md5L16);
            }
            re += b;
        }
        return re;
    }

    /**
     * @param str16
     * @return
     * @Date:2014-3-18
     * @Author:lulei
     * @Description: 将16进制的字符串转化为long值
     */
    public static long parseString16ToLong(String str16){
        if (str16 == null) {
            throw new NumberFormatException("null");
        }
        //先转化为小写
        str16 = str16.toLowerCase();
        //如果字符串以0x开头，去掉0x
        str16 = str16.startsWith("0x") ? str16.substring(2) : str16;
        if (str16.length() > 16) {
            throw new NumberFormatException("For input string '" + str16 + "' is to long");
        }
        return parseMd5L16ToLong(str16);
    }

    public static void main(String[] args) {
        System.out.println(parseString16ToLong("0x1fffff"));
        System.out.println(Long.valueOf("7fffffffffff", 16));

        String l1 = "3231323332663239376135376135613734333839346130653461383031666333";
        System.out.println(Long.MAX_VALUE);
    }

}
