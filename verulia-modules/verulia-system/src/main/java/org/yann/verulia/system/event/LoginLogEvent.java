package org.yann.verulia.system.event;

import org.springframework.context.ApplicationEvent;

/**
 * 登录日志事件
 *
 * @author Yann
 */
public class LoginLogEvent extends ApplicationEvent {
    
    private final String username;
    private final boolean success;
    private final String message;

    public LoginLogEvent(Object source, String username, boolean success, String message) {
        super(source);
        this.username = username;
        this.success = success;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
