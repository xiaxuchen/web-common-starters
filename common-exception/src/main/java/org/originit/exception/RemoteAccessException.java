package org.originit.exception;


import org.originit.exception.enums.ResultCode;

/**
 * @author zhumaer
 * @desc 远程访问异常
 * @since 7/18/2017 3:00 PM
 */
public class RemoteAccessException extends BusinessException {


    private static final long serialVersionUID = -832464574076215195L;

    // 真实的异常
    private Throwable throwable;

    public RemoteAccessException() {
        super();
    }

    public RemoteAccessException(Object data) {
        super.data = data;
    }

    public RemoteAccessException(ResultCode resultCode) {
        super(resultCode);
    }

    public RemoteAccessException(ResultCode resultCode, Object data) {
        super(resultCode, data);
    }

    public RemoteAccessException(String msg) {
        super(msg);
    }

    public RemoteAccessException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

    public RemoteAccessException(String msg,Throwable throwable) {
        super(msg);
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
