package com.papahasuo.new_copilot_instructions;

import com.papahasuo.new_copilot_instructions.controller.HomeController;
import com.papahasuo.new_copilot_instructions.controller.UserController;
import com.papahasuo.new_copilot_instructions.controller.UserRestController;
import com.papahasuo.new_copilot_instructions.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("アプリケーション統合テスト")
class NewCopilotInstructionsApplicationTests {

	@Autowired
	private HomeController homeController;
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private UserRestController userRestController;
	
	@Autowired
	private UserService userService;

	@Test
	@DisplayName("コンテキストが正常にロードされること")
	void contextLoads() {
		// Spring Bootアプリケーションが正常に起動することを確認
	}
	
	@Test
	@DisplayName("全てのコントローラーが正常にインジェクションされること")
	void 全てのコントローラーが正常にインジェクションされること() {
		assertNotNull(homeController);
		assertNotNull(userController);
		assertNotNull(userRestController);
	}
	
	@Test
	@DisplayName("サービスが正常にインジェクションされること")
	void サービスが正常にインジェクションされること() {
		assertNotNull(userService);
	}
}
