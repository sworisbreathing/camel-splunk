package org.apache.camel.component.splunk.consumer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.camel.util.FileUtil;
import org.apache.camel.util.IOHelper;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileReadTimeMarker implements ReadTimeMarker {
    private final Logger LOG = LoggerFactory.getLogger(FileReadTimeMarker.class);

    private File fileStore;

    public FileReadTimeMarker(String fileName) {
        fileStore = new File(fileName);
    }

    @Override
    public String readLatestMark() {
        FileInputStream fis = null;
        String answer = null;
        try {
            // create store if missing
            if (fileStore.exists()) {
                // read the new marker
                fis = new FileInputStream(fileStore);
                StringBuffer fileContent = new StringBuffer("");
                byte[] buffer = new byte[1024];
                while (fis.read(buffer) != -1) {
                    fileContent.append(new String(buffer));
                }
                answer = fileContent.toString();
            }
            return answer;
        } catch (IOException e) {
            throw ObjectHelper.wrapRuntimeCamelException(e);
        } finally {
            IOHelper.close(fis, "Reading new marker", LOG);
        }
    }

    @Override
    public void writeLatestMark(String marker) {
        LOG.debug("Writing {} to filemarker filestore: {}", marker, fileStore);
        FileOutputStream fos = null;
        try {
            // create store if missing
            if (!fileStore.exists()) {
                FileUtil.createNewFile(fileStore);
            }
            // write the new marker
            fos = new FileOutputStream(fileStore, false);
            fos.write(marker.getBytes());
        } catch (IOException e) {
            throw ObjectHelper.wrapRuntimeCamelException(e);
        } finally {
            IOHelper.close(fos, "Writing new marker", LOG);
        }
    }

}
