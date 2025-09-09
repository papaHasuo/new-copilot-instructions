package com.papahasuo.new_copilot_instructions.exception;

/**
 * ユーザーが見つからない場合にスローされる例外
 */
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(Long id) {
        super("ユーザーが見つかりません: ID=" + id);
    }
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
