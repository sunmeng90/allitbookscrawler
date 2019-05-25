package org.meng.allitbooks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Optional;

public class PageHelper {
    private static final Logger logger = LoggerFactory.getLogger(PageHelper.class);

    public static Optional<Document> getPageContent(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .userAgent("mozilla/5.0 (windows nt 10.0; wow64) applewebkit/537.36 (khtml, like gecko) chrome/65.0.3325.146 safari/537.36")
                    .timeout(120000)
                    .get();
        } catch (IOException e) {
            logger.error("Failed to download page content for url {}.", url, e);
        }
        return Optional.ofNullable(document);
    }

    public static void downloadFile(String url, String name, final String path) {
        if (name == null && url != null) {
            name = URLDecoder.decode(url.substring(url.lastIndexOf("/") + 1));
        }
        try {
            InputStream is = new URL(url).openStream();
            FileOutputStream fos = new FileOutputStream(path + name);
            byte[] buffer = new byte[4096];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            logger.error("Can not download and save file {} from url {}. Reason: ", name, url, e);
        }
    }
}