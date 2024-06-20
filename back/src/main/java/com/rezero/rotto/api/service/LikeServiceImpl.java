package com.rezero.rotto.api.service;

import com.rezero.rotto.entity.Farm;
import com.rezero.rotto.entity.InterestFarm;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.FarmRepository;
import com.rezero.rotto.repository.InterestFarmRepository;
import com.rezero.rotto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {

    private final InterestFarmRepository interestFarmRepository;
    private final FarmRepository farmRepository;
    private final UserRepository userRepository;


    // 관심 농장 등록
    public ResponseEntity<?> registerInterestFarm(int userCode, int farmCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }
        // 요청 받은 농장이 존재하는지 검사
        Farm farm = farmRepository.findByFarmCode(farmCode);
        if (farm == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("농장을 찾을 수 없습니다.");
        }
        // 요청 받은 농장이 이미 관심 농장으로 등록되어 있는지 검사
        InterestFarm interestFarm = interestFarmRepository.findByFarmCodeAndUserCode(farmCode, userCode);
        if (interestFarm != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 관심 농장으로 등록되어 있습니다.");
        }

        // 관심 농장 등록. interestFarmCode = 자동 생성
        InterestFarm newInterestFarm = new InterestFarm();
        newInterestFarm.setFarmCode(farmCode);
        newInterestFarm.setUserCode(userCode);
        interestFarmRepository.save(newInterestFarm);

        return ResponseEntity.status(HttpStatus.OK).body("관심 농장 등록 성공!");
    }


    // 관심 농장 등록 해제
    public ResponseEntity<?> cancelInterestFarm(int userCode, int farmCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }
        // 요청 받은 농장이 존재하는지 검사
        Farm farm = farmRepository.findByFarmCode(farmCode);
        if (farm == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("농장을 찾을 수 없습니다.");
        }
        // 요청 받은 농장이 관심 농장인지 검사
        InterestFarm interestFarm = interestFarmRepository.findByFarmCodeAndUserCode(farmCode, userCode);
        if (interestFarm == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("관심 농장으로 등록되어 있지 않습니다.");
        }

        // 관심 농장 등록 해제
        interestFarmRepository.delete(interestFarm);

        return ResponseEntity.status(HttpStatus.OK).body("관심 등록 해제 성공!");
    }

}
