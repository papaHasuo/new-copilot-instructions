package com.papahasuo.new_copilot_instructions.service;

import com.papahasuo.new_copilot_instructions.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    
    private final List<User> users = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);
    
    public UserService() {
        // 初期データ
        users.add(new User(counter.getAndIncrement(), "田中太郎", "tanaka@example.com", 25));
        users.add(new User(counter.getAndIncrement(), "佐藤花子", "sato@example.com", 30));
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    public User saveUser(User user) {
        user.setId(counter.getAndIncrement());
        users.add(user);
        return user;
    }
    
    public User findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
