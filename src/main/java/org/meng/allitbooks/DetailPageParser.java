package org.meng.allitbooks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DetailPageParser implements PageParser<String> {
    private static final Logger logger = LoggerFactory.getLogger(DetailPageParser.class);

    @Override
    public String parsePage(String url) {
        logger.info("Parse url {}", url);
        if (url == null || url.trim().length() == 0) {
            return null;
        }
        return PageHelper.getPageContent(url)
                .flatMap(doc -> Optional.ofNullable(doc.selectFirst("main#main-content span.download-links a")))
                .filter(e -> e != null && e.attr("href") != null && e.attr("href").endsWith(".pdf"))
                .map(e -> e.attr("href"))
                .orElse(null);
    }
}
