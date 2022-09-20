package com.example.xlb.utils;

import com.example.xlb.config.SetConfig;

/**
 * Created by user on 06/06/2020.
 */

public class RoomInfoUtils {
     static String iccidInfo="",PF_STATUS="";
     static String dataMF="",dataMG="",dataW1="",dataW1S="",dataW2="",dataW2S="",dataQYS="",dataFK="",dataJD="",dataSDPFL="",dataHFL="",dataPFL="";
     static int dataPFL_L,dataPFL_H,dataHFL_L,dataHFL_H;
     static String LWAR="",HWAR="";


    public static final String AnalysisData(String iccidInfo,String type) {

        // 01 02 03 04
        String res = "";
        try {
            if (iccidInfo.length() < 14)
                return "";

                        dataMF=iccidInfo.substring(0,2);
                        dataMF=ByteUtils.hexToDec(dataMF)+"CM/S";
                        iccidInfo=iccidInfo.substring(2);

                        dataMG=iccidInfo.substring(0,2);
                        dataMG=ByteUtils.hexToDec(dataMG)+"CM";
                        iccidInfo=iccidInfo.substring(2);

                        dataW1=iccidInfo.substring(0,2);
                        dataW1=ByteUtils.hexString2binaryString(dataW1);
                        if(dataW1.substring(7).equals("1"))
                        {
                            dataW1S="风速低";
                        }
                        else if(dataW1.substring(6,7).equals("1"))
                        {
                           dataW1S="风速高";
                        }
                        else if(dataW1.substring(5,6).equals("1"))
                        {
                            dataW1S="柜门过高";
                        }
                        else
                        {
                            dataW1S="";
                        }
                        iccidInfo=iccidInfo.substring(2);

                        dataQYS=iccidInfo.substring(0,2);
                        dataQYS=ByteUtils.hexToDec(dataQYS);
                        iccidInfo=iccidInfo.substring(2);

                        dataFK=iccidInfo.substring(0,2);
                        dataFK=ByteUtils.hexToDec(dataFK);
                        iccidInfo=iccidInfo.substring(2);

                        dataJD=iccidInfo.substring(0,2);
                        dataJD=ByteUtils.hexToDec(dataJD)+ "度";;
                        iccidInfo=iccidInfo.substring(2);

                        dataW2=iccidInfo.substring(0,2);
                        dataW2=ByteUtils.hexString2binaryString(dataW2);
                        if(dataW2.substring(7).equals("1"))
                        {
                            dataW2S="回流少";
                        }
                        else if(dataW2.substring(6,7).equals("1"))
                        {
                            dataW2S="回流高";
                        }
                        else
                        {
                            dataW2S="";
                        }
                        iccidInfo=iccidInfo.substring(2);


                        dataSDPFL=iccidInfo.substring(0,2);
                        dataSDPFL=ByteUtils.hexToDec(dataSDPFL) + "00CMH";;
                        iccidInfo=iccidInfo.substring(2);

                        java.text.DecimalFormat myformat=new java.text.DecimalFormat("0.0");

                        dataPFL_H=Integer.parseInt(ByteUtils.hexToDec(iccidInfo.substring(0,2)));
                        iccidInfo=iccidInfo.substring(2);


                        dataPFL_L=Integer.parseInt(ByteUtils.hexToDec(iccidInfo.substring(0,2)));
                        iccidInfo=iccidInfo.substring(2);

                        dataPFL= myformat.format((dataPFL_H * 256 + dataPFL_L)) + "CMH";


                        dataHFL_H=Integer.parseInt(ByteUtils.hexToDec(iccidInfo.substring(0,2)));
                        iccidInfo=iccidInfo.substring(2);


                        dataHFL_L=Integer.parseInt(ByteUtils.hexToDec(iccidInfo.substring(0,2)));
                        dataHFL= myformat.format((dataHFL_H * 256 + dataHFL_L)) + "CMH";
                        //res = dataMF+"!"+dataMG+"!"+dataW1S+"!"+dataQYS+"!"+dataFK+"!"+dataJD+"!"+dataW2S+"!"+dataSDPFL+"!"+dataPFL+"!"+dataHFL;
                        res = dataMF+"!"+dataMG+"!"+dataW1S+"!"+dataJD+"!"+dataW2S+"!"+dataSDPFL+"!"+dataPFL+"!"+dataHFL;

        }
        catch (Exception e)
        {

        }
        return res;
    }
}
