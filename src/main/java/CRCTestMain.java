import Storm.ServerSocketOperation.Protocol.CRC16;

/**
 * use this to test CRC 16
 * Created by Administrator on 2016/5/18.
 */
public class CRCTestMain {
    public static void main(String[] args) {
        byte[] check = {1, 3, 32, 0, 10, 0, 0, 2, 115, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -10, 0, -21, 0, 0, 0, -69, 0, -103, 0, 0, 3, -14, 0, 0, 53, -110};
        boolean iso = CRC16.CRCcheck(check);
        System.out.print((-10)&0xff);
        int x = 0;
    }

}
