package com.capitalone.dashboard;

import com.google.common.base.Predicate;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MarkdownTest {

    private Pattern absoluteLinkPattern = Pattern.compile(".*github.com/capitalone/Hygieia.*", Pattern.DOTALL);
    private File root;

    @Before
    public void before(){
        root = getProjectRoot();
    }

    @Test
    public void licenseInRoot(){
        File license = new File(root, "LICENSE");
        assertTrue(license.exists());
        license = null;
    }

    private static final Predicate<File> MARK_DOWN_PREDICATE = new Predicate<File>() {
        @Override
        public boolean apply(@Nullable File file) {
            return file != null && file.getName().endsWith(".md");
        }
    };

    @Test
    public void noAbsoluteLinksInMarkdown() throws Exception {
        for (File file : Files.fileTreeTraverser().preOrderTraversal(root).filter(MARK_DOWN_PREDICATE)) {
            String absolutePath = file.getAbsolutePath();
            if(absoluteLinkPattern.matcher(fromFile(absolutePath)).matches()){
                fail(String.format(Locale.US, "Use relative links in markdown documents: [%s]", absolutePath));
            }
        }
    }

    private File getProjectRoot() {
        return new File(".").getAbsoluteFile().getParentFile().getParentFile();
    }

    CharSequence fromFile(String filename) throws IOException {
        try (FileChannel channel = new FileInputStream(filename).getChannel()) {
            ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
            return Charset.forName("8859_1").newDecoder().decode(buffer);
        }
    }
}
