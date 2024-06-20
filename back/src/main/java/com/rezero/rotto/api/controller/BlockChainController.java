package com.rezero.rotto.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rezero.rotto.api.service.BlockChainService;
import com.rezero.rotto.dto.request.CreateTokenRequest;
import com.rezero.rotto.dto.request.PayTokensRequest;
import com.rezero.rotto.dto.request.RefundsTokenRequest;
import com.rezero.rotto.dto.request.WhiteListRequest;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.UserRepository;
import com.rezero.rotto.utils.JwtTokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/blocks")
@AllArgsConstructor
@Tag(name = "블록체인 컨트롤러", description = "스마트 컨트랙트에게 토큰(ROTTO) 관련 요청을 하기 위한 API")
public class BlockChainController {
	@Autowired
	private BlockChainService blockChainService;
	private UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Operation(summary = "ROTTO 생성", description = "입력받은 청약 code에 대한 ROTTO를 입력받은 수 만큼 생성한다.")
	@PostMapping("/create")
	public ResponseEntity<?> createToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
		@RequestBody CreateTokenRequest request) throws Exception {
			int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
			User user = userRepository.findByUserCode(userCode);
			if(user.getAdmin())
				return blockChainService.createToken(request);

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("요청 권한이 없습니다.");
	}

	@Operation(summary = "ROTTO 지급", description = "")
	@PostMapping("/getToken")
	public ResponseEntity<?> getTokens(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
		@RequestBody PayTokensRequest request) {
		int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
		User user = userRepository.findByUserCode(userCode);
		if(user.getAdmin())
			return blockChainService.distributeToken(request);

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("요청 권한이 없습니다.");
	}

	@Operation(summary = "ROTTO 환급")
	@PostMapping("/burnToken")
	public ResponseEntity<?> RefundsToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
		@RequestBody RefundsTokenRequest request){
		int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
		User user = userRepository.findByUserCode(userCode);
		if(user.getAdmin())
			return blockChainService.RefundsToken(request);

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("요청 권한이 없습니다.");
	}

	@Operation(summary = "whitelist 추가", description = "입력받은 지갑 주소를 whitelist에 추가한다.")
	@PostMapping("/insert")
	public ResponseEntity<?> InsertWhiteList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody WhiteListRequest wallet){
		int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
		User user = userRepository.findByUserCode(userCode);
		if(user.getAdmin()) {
			String address = wallet.getWallet();
			return blockChainService.InsertWhiteList(address);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("요청 권한이 없습니다.");
	}

	@Operation(summary = "whitelist 제거", description = "입력받은 지갑 주소를 whitelist에 제거한다.")
	@PutMapping("/remove")
	public ResponseEntity<?> RemoveWhiteList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody WhiteListRequest wallet){
		int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
		User user = userRepository.findByUserCode(userCode);
		if(user.getAdmin()) {
			String address = wallet.getWallet();
			return blockChainService.RemoveWhiteList(address);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("요청 권한이 없습니다.");
	}

	@Operation(summary = "whiteList 체크", description = "입력받은 지갑 주소가 whiteList에 있는지 체크한다.")
	@GetMapping("/checkList")
	public ResponseEntity<?> checkWhiteList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody WhiteListRequest wallet){
		int userCode = Integer.parseInt(jwtTokenProvider.getPayload(authorizationHeader.substring(7)));
		User user = userRepository.findByUserCode(userCode);
		if(user.getAdmin()) {
			String address = wallet.getWallet();
			return blockChainService.checkWhiteList(address);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("요청 권한이 없습니다.");
	}
}
