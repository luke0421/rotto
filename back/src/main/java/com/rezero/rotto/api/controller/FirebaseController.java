package com.rezero.rotto.api.controller;

import com.rezero.rotto.api.service.FirebaseService;
import com.rezero.rotto.dto.request.SendMessageToAllUsersRequest;
import com.rezero.rotto.dto.request.SendMessageToRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/firebase")
@Tag(name = "Firebase 컨트롤러", description = "푸시 알림 관리를 위한 API")
public class FirebaseController {

    private final FirebaseService firebaseService;


    @Operation(summary = "특정 유저에게 푸시 알림 전송", description = "해당 요청이 들어올 때 알림 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 기기에 대한 요청"),
            @ApiResponse(responseCode = "500", description = "서버 에러로 전송 실패")
    })
    @PostMapping("/user")
    public ResponseEntity<?> sendMessageTo(@RequestBody SendMessageToRequest request) {
        return firebaseService.sendMessageTo(request);
    }


    @Operation(summary = "모든 유저에게 푸시 알림 전송", description = "해당 요청이 들어올 때 알림 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @PostMapping("/all")
    public ResponseEntity<?> sendMessageToAllUsers(@RequestBody SendMessageToAllUsersRequest request) {
        return firebaseService.sendMessageToAllUsers(request);
    }

}
