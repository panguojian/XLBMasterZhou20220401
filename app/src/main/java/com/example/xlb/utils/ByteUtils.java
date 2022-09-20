package com.example.xlb.utils;


import java.math.BigInteger;

/**
 * Created by user on 06/06/2020.
 */

public class ByteUtils {
    private static String hexStr =  "0123456789ABCDEF";
    private static String[] binaryArray =
            {"0000","0001","0010","0011",
                    "0100","0101","0110","0111",
                    "1000","1001","1010","1011",
                    "1100","1101","1110","1111"};

    /**
     * 将整数转换为byte数组并指定长度
     * @param a 整数
     * @param length 指定长度
     * @return
     */
    public static byte[] intToBytes(int a, int length) {
        byte[] bs = new byte[length];
        for (int i = bs.length - 1; i >= 0; i--) {
            bs[i] = (byte) (a % 255);
            a = a / 255;
        }
        return bs;
    }

    /**
     * 将byte数组转换为整数
     * @param bs
     * @return
     */
    public static int bytesToInt(byte[] bs) {
        int a = 0;
        for (int i = bs.length - 1; i >= 0; i--) {
            a += bs[i] * Math.pow(255, bs.length - i - 1);
        }
        return a;
    }

    /**
     * 字节数组拼接
     * @param bt1 bt2 bt3 b4
     * @return bt5
     */

    public static byte[] byteMerger(byte[] bt1, byte[] bt2, byte[] bt3, byte[] bt4){
        byte[] bt5 = new byte[bt1.length+bt2.length+bt3.length+bt4.length];
        System.arraycopy(bt1, 0, bt5, 0, bt1.length);
        System.arraycopy(bt2, 0, bt5, bt1.length, bt2.length);
        System.arraycopy(bt3, 0, bt5, bt1.length+bt2.length, bt3.length);
        System.arraycopy(bt4, 0, bt5, bt1.length+bt2.length+bt3.length, bt4.length);
        return bt5;
    }

    /**
     * 字节数组拼接
     * @param bt1 bt2 bt3
     * @return bt4
     */

    public static byte[] byteMerger(byte[] bt1, byte[] bt2, byte[] bt3){
        byte[] bt4 = new byte[bt1.length+bt2.length+bt3.length];
        System.arraycopy(bt1, 0, bt4, 0, bt1.length);
        System.arraycopy(bt2, 0, bt4, bt1.length, bt2.length);
        System.arraycopy(bt3, 0, bt4, bt1.length+bt2.length, bt3.length);
        return bt4;
    }

    /**
     * 字节数组拼接
     * @param bt1 bt2
     * @return bt3
     */

    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    /**
     * 在字节数组中截取指定长度数组
     * @param
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }


    /**
     * 字节转字符串
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 字节数组转string
     *
     * @param b
     */
    public static String printHexString(byte[] b) {
        String hexStr = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexStr += hex;
        }
        return hexStr;
    }


    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
           // str="00";
        }
        if(str.length()==1)
        {
            str="0"+str;
        }
        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    /**
     * 字节数据转二进制字符串
     * @param by
     * @return
     */
    public static String getBit(byte by){
        StringBuffer sb = new StringBuffer();
        sb.append((by>>7)&0x1)
                .append((by>>6)&0x1)
                .append((by>>5)&0x1)
                .append((by>>4)&0x1)
                .append((by>>3)&0x1)
                .append((by>>2)&0x1)
                .append((by>>1)&0x1)
                .append((by>>0)&0x1);
        return sb.toString();
    }


    /**
     * 16进制转2进制
     * @param hexString
     * @return
     */
    public static String hexString2binaryString(String hexString){
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++)
        {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(hexString
                    .substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    /**
     * 二进制转16进制
     * @param bString
     * @return
     */
    public static String binaryString2hexString(String bString){
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4)
        {
            iTmp = 0;
            for (int j = 0; j < 4; j++)
            {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }
    /**
     * 温控小数转换（按协议）
     * @param n
     * @return
     */
    public static String num(String n){
        String back = "";
        if("00".equals(n)){
            back = "0";
        }else if("01".equals(n)||"02".equals(n)){
            back = "1";
        }else if("03".equals(n)){
            back = "2";
        }else if("04".equals(n)||"05".equals(n)){
            back = "3";
        }else if("06".equals(n)||"07".equals(n)){
            back = "4";
        }else if("08".equals(n)){
            back = "5";
        }else if("09".equals(n) || "0a".equals(n)){
            back = "6";
        }else if("0b".equals(n)){
            back = "7";
        }else if("0c".equals(n)||"0d".equals(n)){
            back = "8";
        }else if("0e".equals(n)||"0f".equals(n)){
            back = "9";
        }
        return back;
    }

    /**
     * 16进制求和
     * @param hexdata
     * @return
     */
    public static String makeChecksum(String hexdata) {
        if (hexdata == null || hexdata.equals("")) {
            return "00";
        }
        hexdata = hexdata.replaceAll(" ", "");
        int total = 0;
        int len = hexdata.length();
        if (len % 2 != 0) {
            return "00";
        }
        int num = 0;
        while (num < len) {
            String s = hexdata.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        return hexInt(total);
    }

    public static String hexInt(int total) {
        int a = total / 256;
        int b = total % 256;
        if (a > 255) {
            return hexInt(a) + format(b);
        }
        //return format(a) + format(b);
        return format(b);
    }

    public static String format(int hex) {
        String hexa = Integer.toHexString(hex);
        int len = hexa.length();
        if (len < 2) {
            hexa = "0" + hexa;
        }
        return hexa;
    }


    /**
     * 十进制数据转换为十六进制字符串数
     *
     * @param dec
     * @return
     */
    public static String decToHex(String dec) {
        BigInteger data = new BigInteger(dec,10);
        return data.toString(16);
    }
    /**
     * 十六进制数据转换为十进制字符串数
     *
     * @param hex
     * @return
     */
    public static String hexToDec(String hex) {
        BigInteger data = new BigInteger(hex,16);
        return data.toString(10);
    }

    public static BigInteger hexToInt(String hex) {
        BigInteger data = new BigInteger(hex,16);
        return data;
    }

}
