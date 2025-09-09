package com.papahasuo.new_copilot_instructions.exception;

/**
 * ユーザーのバリデーション失敗時にスローされる例外
 */
public class UserValidationException extends RuntimeException {
    
    public UserValidationException(String message) {
        super(message);
    }
    
    public UserValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
