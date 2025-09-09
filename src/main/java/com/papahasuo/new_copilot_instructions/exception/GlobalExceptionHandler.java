package com.papahasuo.new_copilot_instructions.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * グローバル例外ハンドラー
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException e, Model model) {
        log.warn("ユーザーが見つかりません: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        model.addAttribute("title", "エラー - ユーザー管理システム");
        return "redirect:/users";
    }
    
    @ExceptionHandler(UserValidationException.class)
    public String handleUserValidationException(UserValidationException e, Model model) {
        log.warn("ユーザーバリデーションエラー: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "users/form";
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        log.warn("不正な引数: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "users/form";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, Model model) {
        log.error("予期しないエラーが発生しました", e);
        model.addAttribute("error", "システムエラーが発生しました。しばらく待ってから再度お試しください。");
        model.addAttribute("title", "システムエラー - ユーザー管理システム");
        return "error";
    }
}
