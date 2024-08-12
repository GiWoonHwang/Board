package com.dustin.boardserver.service.impl;

// 필요한 패키지와 클래스들을 임포트합니다.
import com.dustin.boardserver.dto.PostDTO;
import com.dustin.boardserver.dto.request.PostSearchRequest;
import com.dustin.boardserver.exception.BoardServerException;
import com.dustin.boardserver.mapper.PostSearchMapper;
import com.dustin.boardserver.service.PostSearchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

// 이 클래스는 PostSearchService 인터페이스의 구현체로, 게시글 검색과 관련된 비즈니스 로직을 처리합니다.
@Service
@Log4j2 // 로그를 사용하기 위해 Lombok의 @Log4j2 어노테이션을 사용합니다.
public class PostSearchServiceImpl implements PostSearchService {

    // PostSearchMapper를 주입받아 사용할 수 있도록 필드를 선언합니다.
    @Autowired
    private PostSearchMapper productSearchMapper;

    // 게시글 검색 메서드입니다. 비동기로 실행되며, 캐싱이 적용됩니다.
    @Async // 비동기로 메서드를 실행하기 위해 @Async 어노테이션을 사용합니다.
    @Cacheable(value = "getProducts", key = "'getProducts' + #postSearchRequest.getName() + #postSearchRequest.getCategoryId()")
    // 캐싱을 적용하여 동일한 검색 요청에 대해 캐시에 저장된 데이터를 반환하도록 합니다.
    @Override
    public List<PostDTO> getProducts(PostSearchRequest postSearchRequest) {
        List<PostDTO> postDTOList = null; // 검색 결과를 담을 리스트를 선언합니다.
        try {
            postDTOList = productSearchMapper.selectPosts(postSearchRequest); // 검색 요청을 매퍼를 통해 실행합니다.
        } catch (RuntimeException e) { // 예외가 발생하면
            log.error("selectPosts 실패"); // 로그를 남기고
            throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
        }
        return postDTOList; // 검색 결과를 반환합니다.
    }
}
