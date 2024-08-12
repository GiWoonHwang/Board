package com.dustin.boardserver.controller;

import com.dustin.boardserver.aop.LoginCheck;
import com.dustin.boardserver.dto.UserDTO;
import com.dustin.boardserver.dto.request.UserDeleteId;
import com.dustin.boardserver.dto.request.UserLoginRequest;
import com.dustin.boardserver.dto.request.UserUpdatePasswordRequest;
import com.dustin.boardserver.dto.response.LoginResponse;
import com.dustin.boardserver.dto.response.UserInfoResponse;
import com.dustin.boardserver.service.impl.UserServiceImpl;
import com.dustin.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController: 이 클래스가 스프링 MVC의 REST 컨트롤러임을 나타냅니다.
// 이 클래스의 메서드들은 JSON 또는 XML 형식의 응답을 반환할 수 있습니다.
@RestController

// @RequestMapping: 이 클래스의 모든 요청이 "/users" 경로로 매핑됨을 나타냅니다.
// 즉, 이 클래스의 모든 엔드포인트는 "/users"로 시작합니다.
@RequestMapping("/users")

// @Log4j2: 이 클래스에서 Log4j2를 사용하여 로그를 기록할 수 있게 합니다.
@Log4j2
public class UserController {

    // UserServiceImpl 인스턴스를 주입받습니다.
    // 이 서비스는 사용자와 관련된 비즈니스 로직을 처리합니다.
    private final UserServiceImpl userService;

    // 실패 응답을 나타내는 상수로, BAD_REQUEST 상태를 가지는 ResponseEntity 객체입니다.
    private static final ResponseEntity<LoginResponse> FAIL_RESPONSE = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);

    // 로그인 응답을 담는 정적 변수입니다.
    private static LoginResponse loginResponse;

    // @Autowired: 생성자 주입을 통해 UserServiceImpl 인스턴스를 주입받습니다.
    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    // POST 요청을 처리하며, 새로운 사용자를 등록(회원가입)합니다.
    // @PostMapping: 이 메서드는 HTTP POST 요청을 처리합니다.
    // "/users/sign-up" 경로로 POST 요청이 들어오면 이 메서드가 호출됩니다.
    // @ResponseStatus(HttpStatus.CREATED): 이 메서드가 성공적으로 실행되면, 응답 상태로 201 Created를 반환합니다.
    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody UserDTO userDTO) {
        // 회원가입 시 필수 데이터가 모두 입력되었는지 확인합니다.
        if (UserDTO.hasNullDataBeforeSignup(userDTO)) {
            throw new NullPointerException("회원가입시 필수 데이터를 모두 입력해야 합니다.");
        }
        // userService를 통해 사용자를 등록합니다.
        userService.register(userDTO);
    }

    // POST 요청을 처리하며, 사용자의 로그인 요청을 처리합니다.
    // @PostMapping: 이 메서드는 HTTP POST 요청을 처리합니다.
    // "/users/sign-in" 경로로 POST 요청이 들어오면 이 메서드가 호출됩니다.
    public HttpStatus login(@RequestBody UserLoginRequest loginRequest,
                            HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String userId = loginRequest.getUserId();
        String password = loginRequest.getPassword();
        UserDTO userInfo = userService.login(userId, password);

        // 여기서 발생할 수 있는 문제:
        // 만약 userInfo가 null인 경우, userInfo.getId().toString()에서 NullPointerException이 발생할 수 있습니다.
        // 이 경우 바로 NOT_FOUND를 반환하는 것이 좋습니다.
        if (userInfo == null) {
            return HttpStatus.NOT_FOUND;
        }

        // 사용자가 존재하는 경우 로그인 성공 처리
        String id = userInfo.getId().toString();
        loginResponse = LoginResponse.success(userInfo);

        // 사용자가 관리자일 경우와 일반 사용자일 경우 세션에 ID를 저장합니다.
        if (userInfo.isAdmin())
            SessionUtil.setLoginAdminId(session, id);
        else
            SessionUtil.setLoginMemberId(session, id);

        // 성공적인 로그인 응답을 설정합니다.
        responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);

        // 정상적으로 로그인이 처리된 경우, OK 상태를 반환합니다.
        return HttpStatus.OK;
    }

    // GET 요청을 처리하며, 현재 로그인한 사용자의 정보를 반환합니다.
    // @GetMapping: 이 메서드는 HTTP GET 요청을 처리합니다.
    // "/users/my-info" 경로로 GET 요청이 들어오면 이 메서드가 호출됩니다.
    public UserInfoResponse memberInfo(HttpSession session) {
        // 세션에서 로그인된 사용자 ID를 가져옵니다.
        String id = SessionUtil.getLoginMemberId(session);
        if (id == null) id = SessionUtil.getLoginAdminId(session);

        // userService를 통해 사용자 정보를 가져옵니다.
        UserDTO memberInfo = userService.getUserInfo(id);

        // 사용자 정보를 담은 UserInfoResponse 객체를 반환합니다.
        return new UserInfoResponse(memberInfo);
    }

    // PUT 요청을 처리하며, 사용자를 로그아웃 처리합니다.
    // @PutMapping: 이 메서드는 HTTP PUT 요청을 처리합니다.
    // "/users/logout" 경로로 PUT 요청이 들어오면 이 메서드가 호출됩니다.
    public void logout(String accountId, HttpSession session) {
        // 세션을 초기화하여 로그아웃 처리합니다.
        SessionUtil.clear(session);
    }

    // PATCH 요청을 처리하며, 사용자의 비밀번호를 업데이트합니다.
    // @PatchMapping: 이 메서드는 HTTP PATCH 요청을 처리합니다.
    // "/users/password" 경로로 PATCH 요청이 들어오면 이 메서드가 호출됩니다.
    // @LoginCheck(type = LoginCheck.UserType.USER): 이 메서드는 로그인된 일반 사용자만 접근할 수 있습니다.
    @PatchMapping("password")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<LoginResponse> updateUserPassword(String accountId, @RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
                                                            HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String Id = accountId;
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        try {
            // userService를 통해 비밀번호를 업데이트합니다.
            userService.updatePassword(Id, beforePassword, afterPassword);
            ResponseEntity.ok(new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK));
        } catch (IllegalArgumentException e) {
            // 비밀번호 업데이트에 실패한 경우, 오류를 로그로 기록하고, 실패 응답을 반환합니다.
            log.error("updatePassword 실패", e);
            responseEntity = FAIL_RESPONSE;
        }
        return responseEntity;
    }

    // DELETE 요청을 처리하며, 사용자의 계정을 삭제합니다.
    // @DeleteMapping: 이 메서드는 HTTP DELETE 요청을 처리합니다.
    // "/users" 경로로 DELETE 요청이 들어오면 이 메서드가 호출됩니다.
    public ResponseEntity<LoginResponse> deleteId(@RequestBody UserDeleteId userDeleteId,
                                                  HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String Id = SessionUtil.getLoginMemberId(session);

        try {
            // userService를 통해 계정을 삭제합니다.
            userService.deleteId(Id, userDeleteId.getPassword());
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            // 계정 삭제에 실패한 경우, 실패 로그를 기록하고, 실패 응답을 반환합니다.
            log.info("deleteID 실패");
            responseEntity = FAIL_RESPONSE;
        }
        return responseEntity;
    }
}