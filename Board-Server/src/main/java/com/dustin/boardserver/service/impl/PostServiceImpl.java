package com.dustin.boardserver.service.impl;

// 필요한 패키지와 클래스들을 임포트합니다.
import com.dustin.boardserver.dto.CommentDTO;
import com.dustin.boardserver.dto.PostDTO;
import com.dustin.boardserver.dto.TagDTO;
import com.dustin.boardserver.dto.UserDTO;
import com.dustin.boardserver.exception.BoardServerException;
import com.dustin.boardserver.mapper.CommentMapper;
import com.dustin.boardserver.mapper.PostMapper;
import com.dustin.boardserver.mapper.TagMapper;
import com.dustin.boardserver.mapper.UserProfileMapper;
import com.dustin.boardserver.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

// 이 클래스는 PostService 인터페이스의 구현체로, 게시물과 관련된 비즈니스 로직을 처리합니다.
@Service
@Log4j2 // 로그를 사용하기 위해 Lombok의 @Log4j2 어노테이션을 사용합니다.
public class PostServiceImpl implements PostService {

    // 각 Mapper 객체들을 자동으로 주입받아 사용하기 위한 필드를 선언합니다.
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    // 새로운 게시물을 등록하는 메서드입니다. 캐시를 무효화하여 최신 상태를 유지합니다.
    @CacheEvict(value = "getProducts", allEntries = true) // 모든 캐시된 'getProducts' 엔트리를 무효화합니다.
    @Override
    public void register(String id, PostDTO postDTO) {
        // 사용자 정보를 조회하여 게시물 작성자 정보를 설정합니다.
        UserDTO memberInfo = userProfileMapper.getUserProfile(id);
        postDTO.setUserId(memberInfo.getId());
        postDTO.setCreateTime(new Date()); // 현재 시간을 생성 시간으로 설정합니다.

        // 사용자 정보가 있는 경우 게시물을 등록합니다.
        if (memberInfo != null) {
            try {
                postMapper.register(postDTO);
            } catch (RuntimeException e) { // 예외가 발생하면
                log.error("register 실패"); // 로그를 남기고
                throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
            }
        } else { // 사용자 정보가 없는 경우 오류를 로그에 기록하고 예외를 던집니다.
            log.error("register ERROR! {}", postDTO);
            throw new RuntimeException("register ERROR! 상품 등록 메서드를 확인해주세요\n" + "Params : " + postDTO);
        }
    }

    // 특정 사용자의 게시물을 조회하는 메서드입니다.
    @Override
    public List<PostDTO> getMyProducts(int accountId) {
        List<PostDTO> postDTOList = null;
        try {
            postDTOList = postMapper.selectMyProducts(accountId); // 사용자 ID로 게시물을 조회합니다.
        } catch (RuntimeException e) { // 예외가 발생하면
            log.error("getMyProducts 실패"); // 로그를 남기고
            throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
        }
        return postDTOList; // 조회한 게시물 목록을 반환합니다.
    }

    // 게시물을 업데이트하는 메서드입니다.
    @Override
    public void updateProducts(PostDTO postDTO) {
        // 게시물 정보와 사용자 ID가 유효한지 확인합니다.
        if (postDTO != null && postDTO.getId() != 0 && postDTO.getUserId() != 0) {
            try {
                postMapper.updateProducts(postDTO); // 게시물을 업데이트합니다.
            } catch (RuntimeException e) { // 예외가 발생하면
                log.error("updateProducts 실패"); // 로그를 남기고
                throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
            }
        } else { // 게시물 정보가 유효하지 않은 경우 예외를 던집니다.
            log.error("updateProducts ERROR! {}", postDTO);
            throw new RuntimeException("updateProducts ERROR! 물품 변경 메서드를 확인해주세요\n" + "Params : " + postDTO);
        }
    }

    // 게시물을 삭제하는 메서드입니다.
    @Override
    public void deleteProduct(int userId, int productId) {
        // 사용자 ID와 게시물 ID가 유효한지 확인합니다.
        if (userId != 0 && productId != 0) {
            try {
                postMapper.deleteProduct(productId); // 게시물을 삭제합니다.
            } catch (RuntimeException e) { // 예외가 발생하면
                log.error("deleteProduct 실패"); // 로그를 남기고
                throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
            }
        } else { // ID가 유효하지 않은 경우 예외를 던집니다.
            log.error("deleteProduct ERROR! {}", productId);
            throw new RuntimeException("deleteProduct ERROR! 물품 삭제 메서드를 확인해주세요\n" + "Params : " + productId);
        }
    }

    // 댓글을 등록하는 메서드입니다.
    @Override
    public void registerComment(CommentDTO commentDTO) {
        // 댓글이 특정 게시물에 속하는지 확인합니다.
        if (commentDTO.getPostId() != 0) {
            try {
                commentMapper.register(commentDTO); // 댓글을 등록합니다.
            } catch (RuntimeException e) { // 예외가 발생하면
                log.error("register 실패"); // 로그를 남기고
                throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
            }
        } else { // 댓글 정보가 유효하지 않은 경우 예외를 던집니다.
            log.error("registerComment ERROR! {}", commentDTO);
            throw new RuntimeException("registerComment ERROR! 댓글 추가 메서드를 확인해주세요\n" + "Params : " + commentDTO);
        }
    }

    // 댓글을 업데이트하는 메서드입니다.
    @Override
    public void updateComment(CommentDTO commentDTO) {
        // 댓글 정보가 유효한지 확인합니다.
        if (commentDTO != null) {
            try {
                commentMapper.updateComments(commentDTO); // 댓글을 업데이트합니다.
            } catch (RuntimeException e) { // 예외가 발생하면
                log.error("updateComments 실패"); // 로그를 남기고
                throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
            }
        } else { // 댓글 정보가 유효하지 않은 경우 예외를 던집니다.
            log.error("updateComment ERROR! {}", commentDTO);
            throw new RuntimeException("updateComment ERROR! 댓글 변경 메서드를 확인해주세요\n" + "Params : " + commentDTO);
        }
    }

    // 특정 사용자의 댓글을 삭제하는 메서드입니다.
    @Override
    public void deletePostComment(int userId, int commentId) {
        // 사용자 ID와 댓글 ID가 유효한지 확인합니다.
        if (userId != 0 && commentId != 0) {
            try {
                commentMapper.deletePostComment(commentId); // 댓글을 삭제합니다.
            } catch (RuntimeException e) { // 예외가 발생하면
                log.error("deletePostComment 실패"); // 로그를 남기고
                throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
            }
        } else { // ID가 유효하지 않은 경우 예외를 던집니다.
            log.error("deletePostComment ERROR! {}", commentId);
            throw new RuntimeException("deletePostComment ERROR! 댓글 삭제 메서드를 확인해주세요\n" + "Params : " + commentId);
        }
    }

    // 태그를 등록하는 메서드입니다.
    @Override
    public void registerTag(TagDTO tagDTO) {
        // 태그가 특정 게시물에 속하는지 확인합니다.
        if (tagDTO.getPostId() != 0) {
            try {
                tagMapper.register(tagDTO); // 태그를 등록합니다.
            } catch (RuntimeException e) { // 예외가 발생하면
                log.error("register 실패"); // 로그를 남기고
                throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
            }
        } else { // 태그 정보가 유효하지 않은 경우 예외를 던집니다.
            log.error("registerTag ERROR! {}", tagDTO);
            throw new RuntimeException("registerTag ERROR! 태그 추가 메서드를 확인해주세요\n" + "Params : " + tagDTO);
        }
    }

    // 태그를 업데이트하는 메서드입니다.
    @Override
    public void updateTag(TagDTO tagDTO) {
        // 태그 정보가 유효한지 확인합니다.
        if (tagDTO != null) {
            try {
                tagMapper.updateTags(tagDTO); // 태그를 업데이트합니다.
            } catch (RuntimeException e) { // 예외가 발생하면
                log.error("updateTags 실패"); // 로그를 남기고
                throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
            }
        } else { // 태그 정보가 유효하지 않은 경우 예외를 던집니다.
            log.error("updateTag ERROR! {}", tagDTO);
            throw new RuntimeException("updateTag ERROR! 태그 변경 메서드를 확인해주세요\n" + "Params : " + tagDTO);
        }
    }

    // 특정 사용자의 태그를 삭제하는 메서드입니다.
    @Override
    public void deletePostTag(int userId, int tagId) {
        // 사용자 ID와 태그 ID가 유효한지 확인합니다.
        if (userId != 0 && tagId != 0) {
            try {
                tagMapper.deletePostTag(tagId); // 태그를 삭제합니다.
            } catch (RuntimeException e) { // 예외가 발생하면
                log.error("deletePostTag 실패"); // 로그를 남기고
                throw new BoardServerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // 예외를 던집니다.
            }
        } else { // ID가 유효하지 않은 경우 예외를 던집니다.
            log.error("deletePostTag ERROR! {}", tagId);
            throw new RuntimeException("deletePostTag ERROR! 태그 삭제 메서드를 확인해주세요\n" + "Params : " + tagId);
        }
    }
}
