package com.dustin.boardserver.controller;

import com.dustin.boardserver.aop.LoginCheck;
import com.dustin.boardserver.dto.CommentDTO;
import com.dustin.boardserver.dto.PostDTO;
import com.dustin.boardserver.dto.TagDTO;
import com.dustin.boardserver.dto.UserDTO;
import com.dustin.boardserver.dto.response.CommonResponse;
import com.dustin.boardserver.service.impl.PostServiceImpl;
import com.dustin.boardserver.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;




@RestController
@RequestMapping("/posts")
@Log4j2
public class PostController {
    // PostServiceImpl 및 UserServiceImpl 인스턴스를 주입받습니다.
    // 이 서비스들은 게시물과 사용자 관련된 비즈니스 로직을 처리합니다.
    private final PostServiceImpl postService;
    private final UserServiceImpl userService;

    // 생성자 주입을 통해 PostServiceImpl 및 UserServiceImpl 인스턴스를 주입받습니다.
    public PostController(PostServiceImpl postService, UserServiceImpl userService) {
        this.postService = postService;
        this.userService = userService;
    }

    // POST 요청을 처리하며, 새로운 게시물을 등록합니다.
    // @PostMapping: 이 메서드는 HTTP POST 요청을 처리합니다.
    // "/posts" 경로로 POST 요청이 들어오면 이 메서드가 호출됩니다.
    // @ResponseStatus(HttpStatus.CREATED): 이 메서드가 성공적으로 실행되면, 응답 상태로 201 Created를 반환합니다.
    // @LoginCheck(type = LoginCheck.UserType.USER): 이 메서드는 로그인된 일반 사용자만 접근할 수 있습니다.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostDTO>> registerPost(String accountId, @RequestBody PostDTO postDTO) {
        // postService를 통해 게시물을 등록합니다.
        postService.register(accountId, postDTO);
        // 성공 응답을 생성하여 반환합니다.
        CommonResponse<PostDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "registerPost", postDTO);
        return ResponseEntity.ok(commonResponse);
    }

    // GET 요청을 처리하며, 사용자의 게시물 정보를 가져옵니다.
    // @GetMapping: 이 메서드는 HTTP GET 요청을 처리합니다.
    // "/posts/my-posts" 경로로 GET 요청이 들어오면 이 메서드가 호출됩니다.
    // @LoginCheck(type = LoginCheck.UserType.USER): 이 메서드는 로그인된 일반 사용자만 접근할 수 있습니다.
    @GetMapping("my-posts")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<List<PostDTO>>> myPostInfo(String accountId) {
        // userService를 통해 사용자 정보를 가져옵니다.
        UserDTO memberInfo = userService.getUserInfo(accountId);
        // postService를 통해 사용자의 게시물 리스트를 가져옵니다.
        List<PostDTO> postDTOList = postService.getMyProducts(memberInfo.getId());
        // 성공 응답을 생성하여 반환합니다.
        CommonResponse<List<PostDTO>> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "myPostInfo", postDTOList);
        return ResponseEntity.ok(commonResponse);
    }

    // PATCH 요청을 처리하며, 기존 게시물을 업데이트합니다.
    // @PatchMapping: 이 메서드는 HTTP PATCH 요청을 처리합니다.
    // "/posts/{postId}" 경로로 PATCH 요청이 들어오면 이 메서드가 호출됩니다.
    // @LoginCheck(type = LoginCheck.UserType.USER): 이 메서드는 로그인된 일반 사용자만 접근할 수 있습니다.
    @PatchMapping("{postId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostDTO>> updatePosts(String accountId,
                                                               @PathVariable(name = "postId") int postId,
                                                               @RequestBody PostRequest postRequest) {
        // userService를 통해 사용자 정보를 가져옵니다.
        UserDTO memberInfo = userService.getUserInfo(accountId);

        // 요청 데이터를 바탕으로 PostDTO 객체를 생성합니다.
        PostDTO postDTO = PostDTO.builder()
                .id(postId)
                .name(postRequest.getName())
                .contents(postRequest.getContents())
                .views(postRequest.getViews())
                .categoryId(postRequest.getCategoryId())
                .userId(memberInfo.getId())
                .fileId(postRequest.getFileId())
                .updateTime(new Date())
                .build();

        // postService를 통해 게시물을 업데이트합니다.
        postService.updateProducts(postDTO);

        // CommonResponse 타입을 PostDTO로 수정
        CommonResponse<PostDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "updatePosts", postDTO);
        return ResponseEntity.ok(commonResponse);
    }


    // DELETE 요청을 처리하며, 기존 게시물을 삭제합니다.
    // @DeleteMapping: 이 메서드는 HTTP DELETE 요청을 처리합니다.
    // "/posts/{postId}" 경로로 DELETE 요청이 들어오면 이 메서드가 호출됩니다.
    // @LoginCheck(type = LoginCheck.UserType.USER): 이 메서드는 로그인된 일반 사용자만 접근할 수 있습니다.
    @DeleteMapping("{postId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<PostDeleteRequest>> deleteposts(String accountId,
                                                                         @PathVariable(name = "postId") int postId,
                                                                         @RequestBody PostDeleteRequest postDeleteRequest) {
        // userService를 통해 사용자 정보를 가져옵니다.
        UserDTO memberInfo = userService.getUserInfo(accountId);
        // postService를 통해 게시물을 삭제합니다.
        postService.deleteProduct(memberInfo.getId(), postId);
        // 성공 응답을 생성하여 반환합니다.
        CommonResponse<PostDeleteRequest> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "deleteposts", postDeleteRequest);
        return ResponseEntity.ok(commonResponse);
    }

    // -------------- comments (댓글 관련 엔드포인트) --------------

    // POST 요청을 처리하며, 새로운 댓글을 등록합니다.
    // @PostMapping: 이 메서드는 HTTP POST 요청을 처리합니다.
    // "/posts/comments" 경로로 POST 요청이 들어오면 이 메서드가 호출됩니다.
    // @ResponseStatus(HttpStatus.CREATED): 이 메서드가 성공적으로 실행되면, 응답 상태로 201 Created를 반환합니다.
    // @LoginCheck(type = LoginCheck.UserType.USER): 이 메서드는 로그인된 일반 사용자만 접근할 수 있습니다.
    @PostMapping("comments")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> registerPostComment(String accountId, @RequestBody CommentDTO commentDTO) {
        // postService를 통해 댓글을 등록합니다.
        postService.registerComment(commentDTO);
        // 성공 응답을 생성하여 반환합니다.
        CommonResponse<CommentDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "registerPostComment", commentDTO);
        return ResponseEntity.ok(commonResponse);
    }

    // PATCH 요청을 처리하며, 기존 댓글을 업데이트합니다.
    // @PatchMapping: 이 메서드는 HTTP PATCH 요청을 처리합니다.
    // "/posts/comments/{commentId}" 경로로 PATCH 요청이 들어오면 이 메서드가 호출됩니다.
    // @LoginCheck(type = LoginCheck.UserType.USER): 이 메서드는 로그인된 일반 사용자만 접근할 수 있습니다.
    @PatchMapping("comments/{commentId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> updatePostComment(String accountId,
                                                                        @PathVariable(name = "commentId") int commentId,
                                                                        @RequestBody CommentDTO commentDTO) {
        // userService를 통해 사용자 정보를 가져옵니다.
        UserDTO memberInfo = userService.getUserInfo(accountId);
        // memberInfo가 null이 아닌 경우에만 댓글을 업데이트합니다.
        if(memberInfo != null)
            postService.updateComment(commentDTO);
        // 성공 응답을 생성하여 반환합니다.
        CommonResponse<CommentDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "updatePostComment", commentDTO);
        return ResponseEntity.ok(commonResponse);
    }

    // DELETE 요청을 처리하며, 기존 댓글을 삭제합니다.
    // @DeleteMapping: 이 메서드는 HTTP DELETE 요청을 처리합니다.
    // "/posts/comments/{commentId}" 경로로 DELETE 요청이 들어오면 이 메서드가 호출됩니다.
    // @LoginCheck(type = LoginCheck.UserType.USER): 이 메서드는 로그인된 일반 사용자만 접근할 수 있습니다.
    @DeleteMapping("comments/{commentId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<CommentDTO>> deletePostComment(String accountId,
                                                                        @PathVariable(name = "commentId") int commentId,
                                                                        @RequestBody CommentDTO commentDTO) {
        // userService를 통해 사용자 정보를 가져옵니다.
        UserDTO memberInfo = userService.getUserInfo(accountId);
        // memberInfo가 null이 아닌 경우에만 댓글을 삭제합니다.
        if(memberInfo != null)
            postService.deletePostComment(memberInfo.getId(), commentId);
        // 성공 응답을 생성하여 반환합니다.
        CommonResponse<CommentDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "deletePostComment", commentDTO);
        return ResponseEntity.ok(commonResponse);
    }

    // -------------- tags (태그 관련 엔드포인트) --------------

    // POST 요청을 처리하며, 새로운 태그를 등록합니다.
    // @PostMapping: 이 메서드는 HTTP POST 요청을 처리합니다.
    // "/posts/tags" 경로로 POST 요청이 들어오면 이 메서드가 호출됩니다.
    // @ResponseStatus(HttpStatus.CREATED): 이 메서드가 성공적으로 실행되면, 응답 상태로 201 Created를 반환합니다.
    // @LoginCheck(type = LoginCheck.UserType.USER): 이 메서드는 로그인된 일반 사용자만 접근할 수 있습니다.
    @PostMapping("tags")
    @ResponseStatus(HttpStatus.CREATED)
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<TagDTO>> registerPostTag(String accountId, @RequestBody TagDTO tagDTO) {
        // postService를 통해 태그를 등록합니다.
        postService.registerTag(tagDTO);
        // 성공 응답을 생성하여 반환합니다.
        CommonResponse<TagDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "registerPostTag", tagDTO);
        return ResponseEntity.ok(commonResponse);
    }

    // PATCH 요청을 처리하며, 기존 태그를 업데이트합니다.
    // @PatchMapping: 이 메서드는 HTTP PATCH 요청을 처리합니다.
    // "/posts/tags/{tagId}" 경로로 PATCH 요청이 들어오면 이 메서드가 호출됩니다.
    // @LoginCheck(type = LoginCheck.UserType.USER): 이 메서드는 로그인된 일반 사용자만 접근할 수 있습니다.
    @PatchMapping("tags/{tagId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<TagDTO>> updatePostTag(String accountId,
                                                                @PathVariable(name = "tagId") int tagId,
                                                                @RequestBody TagDTO tagDTO) {
        // userService를 통해 사용자 정보를 가져옵니다.
        UserDTO memberInfo = userService.getUserInfo(accountId);
        // memberInfo가 null이 아닌 경우에만 태그를 업데이트합니다.
        if(memberInfo != null)
            postService.updateTag(tagDTO);
        // 성공 응답을 생성하여 반환합니다.
        CommonResponse<TagDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "updatePostTag", tagDTO);
        return ResponseEntity.ok(commonResponse);
    }

    // DELETE 요청을 처리하며, 기존 태그를 삭제합니다.
    // @DeleteMapping: 이 메서드는 HTTP DELETE 요청을 처리합니다.
    // "/posts/tags/{tagId}" 경로로 DELETE 요청이 들어오면 이 메서드가 호출됩니다.
    // @LoginCheck(type = LoginCheck.UserType.USER): 이 메서드는 로그인된 일반 사용자만 접근할 수 있습니다.
    @DeleteMapping("tags/{tagId}")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<CommonResponse<TagDTO>> deletePostTag(String accountId,
                                                                @PathVariable(name = "tagId") int tagId,
                                                                @RequestBody TagDTO tagDTO) {
        // userService를 통해 사용자 정보를 가져옵니다.
        UserDTO memberInfo = userService.getUserInfo(accountId);
        // memberInfo가 null이 아닌 경우에만 태그를 삭제합니다.
        if(memberInfo != null)
            postService.deletePostTag(memberInfo.getId(), tagId);
        // 성공 응답을 생성하여 반환합니다.
        CommonResponse<TagDTO> commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "deletePostTag", tagDTO);
        return ResponseEntity.ok(commonResponse);
    }

    // -------------- response 객체 --------------

    // @Getter와 @AllArgsConstructor 어노테이션은 Lombok 라이브러리를 사용하여
    // PostResponse 클래스에 대해 getter 메서드를 자동으로 생성하고, 모든 필드를 받는 생성자를 자동으로 생성합니다.
    @Getter
    @AllArgsConstructor
    private static class PostResponse {
        private List<PostDTO> postDTO;
    }

    // -------------- request 객체 --------------

    // @Setter와 @Getter 어노테이션은 Lombok 라이브러리를 사용하여
    // PostRequest 클래스에 대해 getter와 setter 메서드를 자동으로 생성합니다.
    @Setter
    @Getter
    private static class PostRequest {
        private String name;
        private String contents;
        private int views;
        private int categoryId;
        private int userId;
        private int fileId;
        private Date updateTime;
    }

    // @Setter와 @Getter 어노테이션은 Lombok 라이브러리를 사용하여
    // PostDeleteRequest 클래스에 대해 getter와 setter 메서드를 자동으로 생성합니다.
    @Setter
    @Getter
    private static class PostDeleteRequest {
        private int id;
        private int accountId;
    }

}
