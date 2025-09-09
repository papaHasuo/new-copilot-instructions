package com.papahasuo.new_copilot_instructions.controller;

import com.papahasuo.new_copilot_instructions.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.papahasuo.new_copilot_instructions.model.User;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("UserController WebMvcテスト")
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private UserService userService;
    
    @Test
    @DisplayName("ユーザー一覧ページが正常に表示されること")
    void ユーザー一覧ページが正常に表示されること() throws Exception {
        List<User> users = Arrays.asList(
            new User(1L, "田中太郎", "tanaka@example.com", 25),
            new User(2L, "佐藤花子", "sato@example.com", 30)
        );
        
        when(userService.getAllUsers()).thenReturn(users);
        
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/list"))
                .andExpect(model().attributeExists("users"));
    }
    
    @Test
    @DisplayName("新規ユーザー登録フォームが正常に表示されること")
    void 新規ユーザー登録フォームが正常に表示されること() throws Exception {
        mockMvc.perform(get("/users/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/form"))
                .andExpect(model().attributeExists("user"));
    }
    
    @Test
    @DisplayName("ユーザー詳細ページが正常に表示されること")
    void ユーザー詳細ページが正常に表示されること() throws Exception {
        User user = new User(1L, "田中太郎", "tanaka@example.com", 25);
        
        when(userService.findById(1L)).thenReturn(user);
        
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/detail"))
                .andExpect(model().attributeExists("user"));
    }
    
    @Test
    @DisplayName("存在しないユーザーの詳細ページアクセス時にリダイレクトされること")
    void 存在しないユーザーの詳細ページアクセス時にリダイレクトされること() throws Exception {
        when(userService.findById(999L)).thenReturn(null);
        
        mockMvc.perform(get("/users/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }
    
    @Test
    @DisplayName("ユーザー登録が正常に完了すること")
    void ユーザー登録が正常に完了すること() throws Exception {
        User newUser = new User("山田次郎", "yamada@example.com", 28);
        User savedUser = new User(3L, "山田次郎", "yamada@example.com", 28);
        
        when(userService.saveUser(newUser)).thenReturn(savedUser);
        
        mockMvc.perform(post("/users")
                .param("name", "山田次郎")
                .param("email", "yamada@example.com")
                .param("age", "28"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }
}
