package splityourbills;
//signal r
import javax.sound.sampled.*;
import java.io.*;

import com.sun.istack.internal.Nullable;
import org.apache.commons.codec.binary.Base64;
import okhttp3.*;
public class Listen
{
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public boolean flag = true;
    public void stop()
    {
        this.flag = false;
    }
    private final OkHttpClient client = new OkHttpClient();
    public String run() throws Exception
    {
        flag = true;
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat format =  new AudioFormat(sampleRate,
                sampleSizeInBits, channels, signed, bigEndian);
        DataLine.Info info = new DataLine.Info(
                TargetDataLine.class, format);
        TargetDataLine line = (TargetDataLine)
                AudioSystem.getLine(info);
        line.open(format);
        line.start();
        int bufferSize = (int)format.getSampleRate() *
                format.getFrameSize();
        byte buffer[] = new byte[bufferSize];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        long t= System.currentTimeMillis();
        //System.out.println("Listening...");
        while (flag) {
            int count = line.read(buffer, 0, buffer.length);
            if (count > 0) {
                out.write(buffer, 0, count);
            }
        }
        //System.out.println("RECORDED");
        String encodedAudio = Base64.encodeBase64URLSafeString(out.toByteArray());
        out.close();
        String text  = post(encodedAudio);
        if(!text.contains("transcript"))
        {
            System.out.println("Nope, error.");
        }
        System.out.print(text);
        //System.out.println("WHAT");
        return text;
    }
    public String post(String audio) throws Exception
    {
        System.out.println(audio);
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://speech.googleapis.com/v1/speech:recognize").newBuilder();
        urlBuilder.addQueryParameter("key", "AIzaSyCuxWT5UWe26fjGJ53yVwaceGhfsuK9KKE");
        String url = urlBuilder.build().toString();
        //System.out.println("POSTING");
        //String audioo = audio.toString().trim();
        String json  = "{"
                + "\"config\": {"
                + "\"encoding\":\"LINEAR16\","
                + "\"sampleRateHertz\": 16000,"
                + "\"languageCode\": \"en-US\","
                + "\"enableWordTimeOffsets\": false"
                + "},"
                + "\"audio\": {"
                + "\"content\": \""+ audio
                + "\"}"
                + "}";
        //Accept: application/json
        //Content-Type: application/json
        RequestBody body = RequestBody.create(JSON, json);

        //System.out.println(body);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String respond = response.body().string();
        return respond;
    }
}
