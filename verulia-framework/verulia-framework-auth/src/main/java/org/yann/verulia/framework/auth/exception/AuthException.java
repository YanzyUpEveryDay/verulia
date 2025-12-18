package org.yann.verulia.framework.auth.exception;


import org.yann.verulia.framework.core.exception.BusinessException;

/**
 *
 * @author Yann
 * @date 2025/12/18 16:50
 */
public class AuthException extends BusinessException {

    public AuthException(String msg) {
        super(msg);
    }
}
