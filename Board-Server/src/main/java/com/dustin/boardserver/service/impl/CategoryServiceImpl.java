package com.dustin.boardserver.service.impl;

// 필요한 패키지와 클래스들을 임포트합니다.
import com.dustin.boardserver.dto.CategoryDTO;
import com.dustin.boardserver.exception.BoardServerException;
import com.dustin.boardserver.mapper.CategoryMapper;
import com.dustin.boardserver.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

// 이 클래스는 CategoryService 인터페이스의 구현체로, 카테고리와 관련된 비즈니스 로직을 처리합니다.
@Service
@Log4j2 // 로그를 사용하기 위해 Lombok의 @Log4j2 어노테이션을 사용합니다.
public class CategoryServiceImpl implements CategoryService {

    // 카테고리와 관련된 데이터베이스 작업을 수행하기 위한 매퍼 객체를 선언합니다.
    private CategoryMapper categoryMapper;

    // CategoryServiceImpl 클래스의 생성자입니다. CategoryMapper 객체를 주입받아 초기화합니다.
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    // 카테고리를 등록하는 메서드입니다. accountId가 null이 아니면 카테고리를 등록하고, 예외가 발생하면 로그를 남기고 BoardServerException을 던집니다.
    @Override
    public void register(String accountId, CategoryDTO categoryDTO) {
        if (accountId != null) { // accountId가 null이 아닌지 확인합니다.
            try {
                categoryMapper.register(categoryDTO); // 카테고리를 등록합니다.
            } catch (RuntimeException e) { // 예외가 발생하면
                log.error("register 실패"); // 로그를 남기고
                throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
            }
        } else {
            log.error("register ERROR! {}", categoryDTO); // accountId가 null일 경우 에러 로그를 남깁니다.
            throw new RuntimeException("register ERROR! 상품 카테고리 등록 메서드를 확인해주세요\n" + "Params : " + categoryDTO); // 예외를 던집니다.
        }
    }

    // 카테고리를 업데이트하는 메서드입니다. categoryDTO가 null이 아니면 카테고리를 업데이트하고, 예외가 발생하면 로그를 남기고 BoardServerException을 던집니다.
    @Override
    public void update(CategoryDTO categoryDTO) {
        if (categoryDTO != null) { // categoryDTO가 null이 아닌지 확인합니다.
            try {
                categoryMapper.updateCategory(categoryDTO); // 카테고리를 업데이트합니다.
            } catch (RuntimeException e) { // 예외가 발생하면
                log.error("update 실패"); // 로그를 남기고
                throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
            }
        } else {
            log.error("update ERROR! {}", categoryDTO); // categoryDTO가 null일 경우 에러 로그를 남깁니다.
            throw new RuntimeException("update ERROR! 물품 카테고리 변경 메서드를 확인해주세요\n" + "Params : " + categoryDTO); // 예외를 던집니다.
        }
    }

    // 카테고리를 삭제하는 메서드입니다. categoryId가 0이 아니면 카테고리를 삭제하고, 예외가 발생하면 로그를 남기고 BoardServerException을 던집니다.
    @Override
    public void delete(int categoryId) {
        if (categoryId != 0) { // categoryId가 0이 아닌지 확인합니다.
            try {
                categoryMapper.deleteCategory(categoryId); // 카테고리를 삭제합니다.
            } catch (RuntimeException e) { // 예외가 발생하면
                log.error("delete 실패"); // 로그를 남기고
                throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
            }
        } else {
            log.error("deleteCategory ERROR! {}", categoryId); // categoryId가 0일 경우 에러 로그를 남깁니다.
            throw new RuntimeException("deleteCategory ERROR! 물품 카테고리 삭제 메서드를 확인해주세요\n" + "Params : " + categoryId); // 예외를 던집니다.
        }
    }
}
