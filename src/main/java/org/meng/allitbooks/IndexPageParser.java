package org.meng.allitbooks;

import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IndexPageParser implements PageParser<List<String>> {
    private static final Logger logger = LoggerFactory.getLogger(IndexPageParser.class);

    @Override
    public List<String> parsePage(String url) {
        logger.info("Parse url {}", url);
        if (url == null || url.trim().length() == 0) {
            return Collections.EMPTY_LIST;
        }
        return PageHelper.getPageContent(url)
                .map(doc -> doc.select("main#main-content h2.entry-title a"))
                .orElseGet(Elements::new)
                .stream()
                .filter(Objects::nonNull)
                .map(e -> e.attr("href"))
                .collect(Collectors.toList());
    }
}
