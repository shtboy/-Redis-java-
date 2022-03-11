package com.morefun.common.thread;

/**
 * @author Song
 * @date 2022/3/11 11:22
 * @Version 1.0
 */
public class UserThreadLocal {
    private static ThreadLocal<String> threadLocal = new ThreadLocal();

    /**
     * 返回用户标识信息
     *
     * @return
     */
    public static String getUser() {
        return threadLocal.get();
    }

    /**
     * 放置用户信息
     *
     * @return
     */
    public static void setUser(String identity) {
        threadLocal.set(identity);
    }

    public UserThreadLocal() {
    }
}
