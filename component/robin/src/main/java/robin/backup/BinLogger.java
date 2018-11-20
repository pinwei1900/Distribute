/*
 * Copyright (c) 2018年11月20日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/20
 * @Version 1.0.0
 */
public class BinLogger {

    private File binfile;

    public BinLogger(String path) throws IOException {
        binfile = new File(path);
        if (!binfile.exists()) {
            binfile.createNewFile();
        }
    }

    public OutputStream out() throws FileNotFoundException {
        return new FileOutputStream(binfile ,true);
    }

    public InputStream in() throws FileNotFoundException {
        return new FileInputStream(binfile);
    }
}
