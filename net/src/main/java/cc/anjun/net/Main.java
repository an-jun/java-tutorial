package cc.anjun.net;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        new ReadByGet().start();
//        new DoPost().start();
//        new HttpClientGet().start();
        new HttpClientPost().start();
    }


    static class ReadByGet extends Thread {
        @Override
        public void run() {
            try {
                URL url = new URL("http://www.baidu.com");
//                URLConnection conn = url.openConnection();
//                InputStream is = conn.getInputStream();
                InputStream is = url.openStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                br.close();
                isr.close();
                is.close();
                System.out.println(builder.toString());
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }
    static class DoPost extends  Thread{
        @Override
        public void run() {
            try {
                HttpURLConnection conn = (HttpURLConnection) ( new URL("http://localhost/post.php")).openConnection();
                conn.addRequestProperty("encoding","UTF-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                BufferedWriter bw = new BufferedWriter(osw);

                bw.write("a=3&b=5");
                bw.flush();

                InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line=reader.readLine())!=null){
                    builder.append(line);
                }
                reader.close();
                System.out.println(builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    static class HttpClientGet extends Thread{
        HttpClient client =  HttpClients.createDefault();
        @Override
        public void run() {
            HttpGet get = new HttpGet("http://www.baidu.com");
            try {
             HttpResponse res =  client.execute(get);
                HttpEntity entity =res.getEntity();
                String result = EntityUtils.toString(entity,"UTF-8");
                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    static  class HttpClientPost extends Thread{
        HttpClient client = HttpClients.createDefault();
        @Override
        public void run() {
            HttpPost post = new HttpPost("http://localhost/post.php");
            try {
                List<BasicNameValuePair> parameters = new ArrayList<>();
                parameters.add(new BasicNameValuePair("a","3f"));
                post.setEntity(new UrlEncodedFormEntity(parameters,"UTF-8"));
                HttpResponse response = client.execute(post);
                HttpEntity entity= response.getEntity();
                String result = EntityUtils.toString(entity,"UTF-8");
                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
