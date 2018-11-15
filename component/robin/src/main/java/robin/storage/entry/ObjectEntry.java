/*
 * Copyright (c) 2018年11月15日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.storage.entry;

import lombok.Data;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/15
 * @Version 1.0.0
 */
@Data
public class ObjectEntry {
    byte[] content;

    public ObjectEntry(byte[] content) {
        this.content = content;
    }

}
