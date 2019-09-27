import org.apache.kafka.common.utils.ByteUtils;
import org.apache.spark.sql.execution.columnar.BYTE;
import org.junit.Test;
import parquet.bytes.BytesUtils;
import scala.Byte;

public class Tmp {
    public static void main(String[] args) {
//        BytesUtils.by
    }

    @Test
    public void tmp() {
        int i = 19;
        byte b = (byte) i;
        long l = 100;
        byte b1 = (byte) l;

        byte[] bytes2 = intToByteArray(10);
        int i2 = byteArrayToInt(bytes2);

        byte[] bytes = {1, 0, 0, 1};

        int i1 = ByteUtils.readUnsignedIntLE(bytes, 0);

        byte[] bytes1 = "789087".getBytes();
        System.out.println(b);
    }

    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }
}
