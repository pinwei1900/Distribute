/*
 * Copyright (c) 2018年11月16日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */

import com.google.common.hash.Hashing;
import java.util.ArrayList;
import robin.utils.ConsistentHash;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/16
 * @Version 1.0.0
 */
public class HashTest {
    public static void main(String[] args) {
        ArrayList<String> nodeList = new ArrayList<>();
        nodeList.add("www.google.com.hk");
        nodeList.add("www.apple.com.cn");
        nodeList.add("twitter.com");
        nodeList.add("weibo.com");
        ConsistentHash<String> consistentHash = new ConsistentHash<>(Hashing.md5(), 100, nodeList);
        System.out.println(consistentHash.get(12));
    }
}
