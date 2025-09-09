package com.papahasuo.new_copilot_instructions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.papahasuo.new_copilot_instructions.model.User;
import com.papahasuo.new_copilot_instructions.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRestController.class)
@DisplayName("UserRestController WebMvcテスト")
class UserRestControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private UserService userService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("全ユーザー取得APIが正常に動作すること")
    void 全ユーザー取得APIが正常に動作すること() throws Exception {
        List<User> users = Arrays.asList(
            new User(1L, "田中太郎", "tanaka@example.com", 25),
            new User(2L, "佐藤花子", "sato@example.com", 30)
        );
        
        when(userService.getAllUsers()).thenReturn(users);
        
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("田中太郎"))
                .andExpect(jsonPath("$[1].name").value("佐藤花子"));
    }
    
    @Test
    @DisplayName("指定IDでユーザー取得APIが正常に動作すること")
    void 指定IDでユーザー取得APIが正常に動作すること() throws Exception {
        User user = new User(1L, "田中太郎", "tanaka@example.com", 25);
        
        when(userService.findById(1L)).thenReturn(user);
        
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("田中太郎"))
                .andExpect(jsonPath("$.email").value("tanaka@example.com"))
                .andExpect(jsonPath("$.age").value(25));
    }
    
    @Test
    @DisplayName("存在しないIDでユーザー取得API時に404が返されること")
    void 存在しないIDでユーザー取得API時に404が返されること() throws Exception {
        when(userService.findById(999L)).thenReturn(null);
        
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("ユーザー作成APIが正常に動作すること")
    void ユーザー作成APIが正常に動作すること() throws Exception {
        User newUser = new User("山田次郎", "yamada@example.com", 28);
        User savedUser = new User(3L, "山田次郎", "yamada@example.com", 28);
        
        when(userService.saveUser(any(User.class))).thenReturn(savedUser);
        
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("山田次郎"))
                .andExpect(jsonPath("$.email").value("yamada@example.com"))
                .andExpect(jsonPath("$.age").value(28));
    }
    
    @Test
    @DisplayName("ユーザー更新APIが正常に動作すること")
    void ユーザー更新APIが正常に動作すること() throws Exception {
        User updatedUser = new User("田中太郎（更新済み）", "tanaka-updated@example.com", 26);
        User resultUser = new User(1L, "田中太郎（更新済み）", "tanaka-updated@example.com", 26);
        
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(resultUser);
        
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("田中太郎（更新済み）"))
                .andExpect(jsonPath("$.email").value("tanaka-updated@example.com"))
                .andExpect(jsonPath("$.age").value(26));
    }
    
    @Test
    @DisplayName("ユーザー削除APIが正常に動作すること")
    void ユーザー削除APIが正常に動作すること() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);
        
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("存在しないIDでユーザー削除API時に404が返されること")
    void 存在しないIDでユーザー削除API時に404が返されること() throws Exception {
        when(userService.deleteUser(999L)).thenReturn(false);
        
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());
    }
}
