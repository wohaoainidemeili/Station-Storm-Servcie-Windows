package Storm.ServerSocketOperation.Protocol;

/**
 * modbus protocol need the CRC16 caculate
 * Created by Yuan on 2016/4/19.
 */
public class CRC16 {
    /**
     * caculate the 2 check code left in crcbuf
     * @param crcbuf the send command information which has the 2 check code unfinished
     * @return the full command for querying the sensor data by socket
     */
    public static byte[] CRCCaculate(byte[] crcbuf){
        int crc=0xffff;
        int len=crcbuf.length-2;
        for (int n=0;n<len;n++){
            crc= crc^crcbuf[n];//the byte and the int type XOR. only the low 8 code operate the XOR operation
            for (int i=0;i<8;i++) {
                int _out = crc & 1;
                crc = crc >> 1;
                if (_out == 1) {
                    crc = crc ^ 0xa001;
                }
            }
        }
        //change the low 8 code and the high 8 code
        crcbuf[len+1]=(byte)(crc>>8);
        crcbuf[len]=(byte)crc;
        return crcbuf;
    }

    /**
     * checking the recieved data is correct
     * @param recieveBuffer the recieved data organized by bytes
     * @return true if the data is correct,false if the data is incorrect
     */
    public static boolean CRCcheck(byte[] recieveBuffer){
        boolean isCorrect=true;

        int CRC=0xffff;
        int temp=0xA001;
        for (int i=0;i<recieveBuffer.length;i++){
            CRC^=recieveBuffer[i];
            for (int j=0;j<8;j++){
                int lastOne=CRC&1;
                CRC>>=1;
                CRC&=0x7fff;
                if (lastOne==1){
                    CRC^=temp;
                }
            }
        }
        if (CRC==0) isCorrect=true;
        else isCorrect=false;
        return  isCorrect;
    }
}
