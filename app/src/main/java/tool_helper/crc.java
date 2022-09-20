package tool_helper;

import com.example.xlb.utils.ByteUtils;

public  class crc {
//

   public static int modbus_crc(byte[] data)
    {
        int crcr,i,j;
        byte[] buf = new byte[data.length];
        for (i = 0; i < data.length; i++) {
            buf[i] = data[i];  //数据的复制
        }
        crcr=0xffff;
        for(i=0; i<buf.length; i++)//nEnd 是需要校验的字节数
        {
            crcr^=(int) buf[i];
            for(j=1;j<=8;j++)
            {
                if((crcr&1)!=0)
                crcr=(crcr>>1)^0xa001;
                else crcr=(crcr>>1);
            }
        }
        return(crcr);

    }



    public static byte[] makefcs(byte[] data)
    {
        int i;
        int crc=0x0000FFFF;
        byte[] buf = new byte[data.length];// 存储需要产生校验码的数据
        byte[] bup=new byte[2];
        for (i = 0; i < data.length; i++) {
            buf[i] = data[i];  //数据的复制
        }
        int len = buf.length;
        for (int pos = 0; pos < len; pos++) {

            crc ^= ((int) buf[pos]& 0x000000ff);
            for (i = 1; i <=8; i++) {
                if ((crc & 0x00000001)!=0) {
                    crc >>= 1;   //右移运算符
                    crc ^= 0x0000a001;
                } else
                    crc >>= 1;
            }
        }
        // 24   0801       //
        String c = Integer.toHexString(crc);
        if (c.length() == 4) {
            c = c.substring(2, 4) + c.substring(0, 2);
        } else if (c.length() == 3) {
            c = "0" + c;
            c = c.substring(2, 4) + c.substring(0, 2);
        } else if (c.length() == 2) {
            c =  c + "00";
        }
        else if (c.length() == 1) {
            c =  "0"+c+ "00";
        }
        bup= ByteUtils.toBytes(c);
        return bup;
    }

}
