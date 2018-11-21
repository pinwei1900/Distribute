/*
 * Copyright (c) 2018年11月21日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/21
 * @Version 1.0.0
 */
public class FileStreamTest {

    private static File file = new File("G:\\Google Drive\\workspace\\binlog\\filestream");

    @BeforeClass
    public static void begin() throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    private InputStream getIn() throws FileNotFoundException {
        return new FileInputStream(file);
    }

    private OutputStream getOut() throws FileNotFoundException {
        return new FileOutputStream(file, true);
    }

    @Test
    public void fileMax() throws FileNotFoundException, InterruptedException {
        while (true) {
            getOut();
            Thread.sleep(1000);
        }
    }

    @Test
    public void inTest() throws InterruptedException {
        new Thread(() -> {

            InputStream in = null;
            try {
                in = getIn();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    int b = in.read();
                    System.out.println("1:" + b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {

            InputStream in = null;
            try {
                in = getIn();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    int b = in.read();
                    System.out.println("2:" + b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(1000000);
    }

    @Test
    public void outTest() throws InterruptedException {

        new Thread(() -> {
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getOut()));
                for (int i = 0; i < 10; i++) {
                    writer.write(i);
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getOut()));
                for (int i = 10; i < 20; i++) {
                    writer.write(i);
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(10000);
    }

}
