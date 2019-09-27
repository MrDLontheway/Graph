import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;

interface Callback {
    void action(String line);
}

public class NIOReadline {
    private int buffersize;
    private Charset charset ;
    private Callback callback;
    private FileChannel channel;
    ByteBuffer buffer ;

    public NIOReadline(String file, Callback callback) throws UnsupportedEncodingException, IOException {
        this(file,callback,200,Charset.defaultCharset().displayName());
    }

    public NIOReadline(String file, Callback callback,int bufferSize,String charsetName) throws UnsupportedEncodingException, IOException {
        charset=Charset.forName(charsetName);
        buffersize=bufferSize;
        channel = new FileInputStream(file).getChannel();
        buffer=ByteBuffer.allocate(bufferSize);
        this.callback=callback;
        action();
    }

    private void action() throws UnsupportedEncodingException, IOException {
        while (channel.read(buffer) > 0) {
            buffer.flip();
            byte[] data = new byte[buffersize];
            // 数组最后一个可用数据
            int last = buffer.limit() - 1;
            buffer.get(data, 0, buffer.limit());
            buffer.compact();
            // 找最后一个换行符
            int i = last;
            while (data[i] != (byte) '\n') {
                i = i - 1;
                // 如果没有换行符,直接退出,可能是最后一行
                if (i < 0) {
                    break;
                }
            }

            if (i > 0) {
                // 如果有换行符
                byte[] bytes1 = Arrays.copyOfRange(data, 0, 0 + i + 1);
                System.out.println(bytes1.length);
//                action(new String(data, 0, i + 1, charset));

                while (i < last) {
                    buffer.put(data[i + 1]);
                    i++;
                }
            } else {
                action(new String(data, 0, buffer.limit(), charset));
            }
        }
        channel.close();
    }

    private void action(String str) {
        String[] data = str.split("\n");
        for (String line : data) {
            callback.action(line);
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        NIOReadline r=new NIOReadline("/Users/daile/Desktop/tmpwendang", new Callback() {

            @Override
            public void action(String line) {
//                System.out.println(line);
            }
        });


        System.out.println("over");

        FileInputStream fileInputStream = new FileInputStream(new File("/Users/daile/Desktop/tmpwendang"));
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while ((line = bufferedReader.readLine())!=null){
            System.out.println(line.getBytes().length);
        }

    }
}
