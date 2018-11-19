/*
 * Copyright (c) 2018年11月19日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */

import static robin.utils.HttpUtils.sendGet;
import static robin.utils.HttpUtils.sendPost;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/19
 * @Version 1.0.0
 */
public class HttpTest {


    @Test
    public void get() throws IOException {
        Map<String, String> param = new HashMap();
        for (int i = 0; i < 5; i++) {
            param.put("key" + i, "value" + i);
        }
        sendGet("http://47.107.122.21:8110/getUsers", param);
    }

    @Test
    public void post() throws IOException {
        Map<String, Object> param = new HashMap();
        for (int i = 0; i < 5; i++) {
            param.put("key" + i, "value" + i);
        }
        param.put("file1", new File("G:\\Google Drive\\workspace\\leetcode.git\\trunk\\blog\\3，安装Java.md"));
        param.put("file2", new File("G:\\Google Drive\\workspace\\leetcode.git\\trunk\\blog\\2，建立管理员账户.md"));

        sendPost("http://47.107.122.21:8110/getUsers", param);
    }
}
