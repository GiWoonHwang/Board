package com.dustin.boardserver.controller;

import com.dustin.boardserver.aop.LoginCheck;
import com.dustin.boardserver.dto.CategoryDTO;
import com.dustin.boardserver.dto.SortStatus;
import com.dustin.boardserver.service.impl.CategoryServiceImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// @RestController: 이 클래스가 스프링 MVC의 REST 컨트롤러임을 나타냅니다.
// 이 클래스의 메서드들은 JSON 또는 XML 형식의 응답을 반환할 수 있습니다.
@RestController

// @RequestMapping: 이 클래스의 모든 요청이 "/categories" 경로로 매핑됨을 나타냅니다.
// 즉, 이 클래스의 모든 엔드포인트는 "/categories"로 시작합니다.
@RequestMapping("/categories")

// @Log4j2: 이 클래스에서 Log4j2를 사용하여 로그를 기록할 수 있게 합니다.
@Log4j2
public class CategoryController {

    // CategoryServiceImpl 인스턴스를 주입받습니다.
    // 이 서비스는 카테고리와 관련된 비즈니스 로직을 처리합니다.
    private CategoryServiceImpl categoryService;

    // 생성자 주입을 통해 CategoryServiceImpl 인스턴스를 주입받습니다.
    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    // POST 요청을 처리하며, 새로운 카테고리를 등록합니다.
    // @PostMapping: 이 메서드는 HTTP POST 요청을 처리합니다.
    // "/categories" 경로로 POST 요청이 들어오면 이 메서드가 호출됩니다.
    // @ResponseStatus(HttpStatus.CREATED): 이 메서드가 성공적으로 실행되면, 응답 상태로 201 Created를 반환합니다.
    // @LoginCheck(type = LoginCheck.UserType.ADMIN): 이 메서드는 관리자 계정으로 로그인한 사용자만 접근할 수 있습니다.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public void registerCategory(String accountId, @RequestBody CategoryDTO categoryDTO) {
        // categoryService를 통해 카테고리를 등록합니다.
        categoryService.register(accountId, categoryDTO);
    }

    // PATCH 요청을 처리하며, 기존 카테고리를 업데이트합니다.
    // @PatchMapping: 이 메서드는 HTTP PATCH 요청을 처리합니다.
    // "/categories/{categoryId}" 경로로 PATCH 요청이 들어오면 이 메서드가 호출됩니다.
    // @LoginCheck(type = LoginCheck.UserType.ADMIN): 이 메서드는 관리자 계정으로 로그인한 사용자만 접근할 수 있습니다.
    @PatchMapping("{categoryId}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public void updateCategories(String accountId,
                                 @PathVariable(name = "categoryId") int categoryId,
                                 @RequestBody CategoryRequest categoryRequest) {
        // 요청 데이터를 바탕으로 CategoryDTO 객체를 생성합니다.
        CategoryDTO categoryDTO = new CategoryDTO(categoryId, categoryRequest.getName(), SortStatus.NEWEST, 10, 1);
        // categoryService를 통해 카테고리를 업데이트합니다.
        categoryService.update(categoryDTO);
    }

    // DELETE 요청을 처리하며, 기존 카테고리를 삭제합니다.
    // @DeleteMapping: 이 메서드는 HTTP DELETE 요청을 처리합니다.
    // "/categories/{categoryId}" 경로로 DELETE 요청이 들어오면 이 메서드가 호출됩니다.
    // @LoginCheck(type = LoginCheck.UserType.ADMIN): 이 메서드는 관리자 계정으로 로그인한 사용자만 접근할 수 있습니다.
    @DeleteMapping("{categoryId}")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public void updateCategories(String accountId,
                                 @PathVariable(name = "categoryId") int categoryId) {
        // categoryService를 통해 카테고리를 삭제합니다.
        categoryService.delete(categoryId);
    }

    // -------------- request 객체 --------------

    // @Setter와 @Getter 어노테이션은 Lombok 라이브러리를 사용하여
    // CategoryRequest 클래스에 대해 getter와 setter 메서드를 자동으로 생성합니다.
    @Setter
    @Getter
    private static class CategoryRequest {
        // 카테고리 ID를 나타내는 필드입니다.
        private int id;
        // 카테고리 이름을 나타내는 필드입니다.
        private String name;
    }

}
