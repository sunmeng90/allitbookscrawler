package org.meng.allitbooks;

import org.junit.Test;

public class PageHelperTest {

    @Test
    public void testDownloadPdf() {
        PageHelper.downloadFile("http://file.allitebooks.com/20180228/Practical%20Python%20AI%20Projects.pdf",
                null, "");
    }


}