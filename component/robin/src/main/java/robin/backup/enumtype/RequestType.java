/*
 * Copyright (c) 2018年11月22日 by XuanWu Wireless Technology Co.Ltd.
 *             All rights reserved
 */
package robin.backup.enumtype;

/**
 * @Description
 * @Author <a href="mailto:haosonglin@wxchina.com">songlin.Hao</a>
 * @Date 2018/11/22
 * @Version 1.0.0
 */
public enum RequestType {

    /** 心跳和注册是一样的，同步表示的起始同步信息。 */
    HEART_BEAT(1), SYSC(2);

    public int type;

    RequestType(int type) {
        this.type = type;
    }

    public static RequestType getByType(int type) {
        for (RequestType request : RequestType.values()) {
            if (request.type == type) {
                return request;
            }
        }
        return null;
    }
}
