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
public enum ResponseType {

    /** 心跳检测，同步 */
    HEART_BEAT(1), SYNC(2);

    public int type;

    ResponseType(int type) {
        this.type = type;
    }

    public static ResponseType getByType(int type) {
        for (ResponseType request : ResponseType.values()) {
            if (request.type == type) {
                return request;
            }
        }
        return null;
    }
}
