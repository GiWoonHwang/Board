package com.dustin.boardserver.controller;

import com.dustin.boardserver.dto.PostDTO;
import com.dustin.boardserver.dto.request.PostSearchRequest;
import com.dustin.boardserver.service.impl.PostSearchServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// @RestController: 이 클래스가 스프링 MVC의 REST 컨트롤러임을 나타냅니다.
// 이 클래스의 메서드들은 JSON 또는 XML 형식의 응답을 반환할 수 있습니다.
@RestController

// @RequestMapping: 이 클래스의 모든 요청이 "/search" 경로로 매핑됨을 나타냅니다.
// 즉, 이 클래스의 모든 엔드포인트는 "/search"로 시작합니다.
@RequestMapping("/search")

// @Log4j2: 이 클래스에서 Log4j2를 사용하여 로그를 기록할 수 있게 합니다.
@Log4j2

// @RequiredArgsConstructor: Lombok 어노테이션으로,
// 이 클래스의 final 필드나 @NonNull이 붙은 필드들에 대해 생성자를 자동으로 생성합니다.
@RequiredArgsConstructor
public class PostSearchController {

    // PostSearchServiceImpl 인스턴스를 주입받습니다.
    // 이 서비스는 게시물 검색과 관련된 비즈니스 로직을 처리합니다.
    private final PostSearchServiceImpl postSearchService;

    // @PostMapping: 이 메서드는 HTTP POST 요청을 처리합니다.
    // "/search" 경로로 POST 요청이 들어오면 이 메서드가 호출됩니다.
    // 검색 요청을 처리하고, 그 결과를 PostSearchResponse 객체로 반환합니다.
    @PostMapping
    public PostSearchResponse search(@RequestBody PostSearchRequest postSearchRequest) {
        // postSearchService를 통해 검색 요청에 해당하는 게시물 리스트를 가져옵니다.
        List<PostDTO> postDTOList = postSearchService.getProducts(postSearchRequest);
        // 검색 결과를 담은 PostSearchResponse 객체를 생성하여 반환합니다.
        return new PostSearchResponse(postDTOList);
    }

    // -------------- response 객체 --------------

    // @Getter와 @AllArgsConstructor 어노테이션은 Lombok 라이브러리를 사용하여
    // PostSearchResponse 클래스에 대해 getter 메서드와 모든 필드를 받는 생성자를 자동으로 생성합니다.
    @Getter
    @AllArgsConstructor
    private static class PostSearchResponse {
        // 검색 결과로 반환되는 게시물 목록입니다.
        private List<PostDTO> postDTOList;
    }
}