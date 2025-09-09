package com.papahasuo.new_copilot_instructions.service;

import com.papahasuo.new_copilot_instructions.exception.UserNotFoundException;
import com.papahasuo.new_copilot_instructions.exception.UserValidationException;
import com.papahasuo.new_copilot_instructions.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class UserService {
    
    private final List<User> users = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);
    
    public UserService() {
        log.info("UserServiceを初期化します");
        // 初期データ
        users.add(new User(counter.getAndIncrement(), "田中太郎", "tanaka@example.com", 25));
        users.add(new User(counter.getAndIncrement(), "佐藤花子", "sato@example.com", 30));
        log.info("初期データを作成しました。ユーザー数: {}", users.size());
    }
    
    public List<User> getAllUsers() {
        log.debug("全ユーザーを取得します。現在のユーザー数: {}", users.size());
        return new ArrayList<>(users);
    }
    
    public User saveUser(User user) {
        log.info("ユーザーの保存を開始します: {}", user.getName());
        
        // バリデーション
        validateUser(user);
        
        // メールアドレスの重複チェック
        if (isEmailExists(user.getEmail())) {
            log.warn("メールアドレスが既に存在します: {}", user.getEmail());
            throw new UserValidationException("このメールアドレスは既に登録されています: " + user.getEmail());
        }
        
        user.setId(counter.getAndIncrement());
        users.add(user);
        log.info("ユーザーの保存が完了しました: {} (ID: {})", user.getName(), user.getId());
        return user;
    }
    
    public User findById(Long id) {
        log.debug("ユーザーを検索します: ID={}", id);
        if (id == null) {
            log.warn("IDがnullです");
            return null;
        }
        
        User user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
        
        if (user != null) {
            log.debug("ユーザーが見つかりました: {} (ID: {})", user.getName(), id);
        } else {
            log.debug("ユーザーが見つかりません: ID={}", id);
        }
        
        return user;
    }
    
    public User updateUser(Long id, User updatedUser) {
        log.info("ユーザーの更新を開始します: ID={}", id);
        
        User existingUser = findById(id);
        if (existingUser == null) {
            log.warn("更新対象のユーザーが見つかりません: ID={}", id);
            throw new UserNotFoundException(id);
        }
        
        // バリデーション
        validateUser(updatedUser);
        
        // メールアドレスの重複チェック（自分以外）
        if (!existingUser.getEmail().equals(updatedUser.getEmail()) && isEmailExists(updatedUser.getEmail())) {
            log.warn("メールアドレスが既に存在します: {}", updatedUser.getEmail());
            throw new UserValidationException("このメールアドレスは既に登録されています: " + updatedUser.getEmail());
        }
        
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setAge(updatedUser.getAge());
        
        log.info("ユーザーの更新が完了しました: {} (ID: {})", existingUser.getName(), id);
        return existingUser;
    }
    
    public boolean deleteUser(Long id) {
        log.info("ユーザーの削除を開始します: ID={}", id);
        
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            log.info("ユーザーの削除が完了しました: ID={}", id);
        } else {
            log.warn("削除対象のユーザーが見つかりません: ID={}", id);
        }
        
        return removed;
    }
    
    private void validateUser(User user) {
        if (user == null) {
            throw new UserValidationException("ユーザー情報が必要です");
        }
        
        if (!hasText(user.getName())) {
            throw new UserValidationException("名前は必須です");
        }
        
        if (!hasText(user.getEmail())) {
            throw new UserValidationException("メールアドレスは必須です");
        }
        
        if (!isValidEmail(user.getEmail())) {
            throw new UserValidationException("有効なメールアドレスを入力してください");
        }
        
        if (user.getAge() < 0 || user.getAge() > 150) {
            throw new UserValidationException("年齢は0歳から150歳の間で入力してください");
        }
        
        log.debug("ユーザーのバリデーションが完了しました: {}", user.getName());
    }
    
    private boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    private boolean isEmailExists(String email) {
        return users.stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }
}
