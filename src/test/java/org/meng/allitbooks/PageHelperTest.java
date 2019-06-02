package org.meng.allitbooks;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PageHelperTest {

    @Test
    public void testDownloadPdf() {
        PageHelper.downloadFile("http://file.allitebooks.com/20180228/Practical%20Python%20AI%20Projects.pdf",
                null, "");
    }

    @Test
    public void testDownloadPdf2() throws IOException {
        Connection.Response resp = null;
        try {
            resp = Jsoup.connect("http://file.allitebooks.com/20190524/Progressive%20Web%20Apps%20with%20Angular.pdf")
//                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .followRedirects(false)
                    .timeout(10 * 10000)
                    .maxBodySize(0)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Thread.currentThread().interrupt();
        System.out.println(resp.cookies());
        FileOutputStream fos = new FileOutputStream(new File("some.pdf"));
        fos.write(resp.bodyAsBytes());
        fos.close();
        System.out.println(Thread.currentThread().isInterrupted());
    }

}