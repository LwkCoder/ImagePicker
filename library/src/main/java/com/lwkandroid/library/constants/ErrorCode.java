package com.lwkandroid.library.constants;

/**
 * @description: 错误码集合
 * @author: LWK
 * @date: 2021/6/1 15:39
 */
public final class ErrorCode
{
    private ErrorCode()
    {
        throw new UnsupportedOperationException("Can't instantiate this class !");
    }

    /**
     * 未知错误
     */
    public static final int UNKNOWN_ERROR = 10000;
    /**
     * 权限被拒绝
     */
    public static final int PERMISSION_DENIED = 20001;
    /**
     * 摄像头不可用
     */
    public static final int CAMEAR_UNAVAILABLE = 20002;
    /**
     * 无法发起拍照
     */
    public static final int UNABLE_TO_PHOTOGRAPH = 20003;


}