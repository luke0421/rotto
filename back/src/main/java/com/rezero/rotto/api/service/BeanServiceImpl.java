package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.dto.BeanListDto;
import com.rezero.rotto.dto.response.BeanDetailResponse;
import com.rezero.rotto.dto.response.BeanListResponse;
import com.rezero.rotto.entity.Bean;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.BeanRepository;
import com.rezero.rotto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BeanServiceImpl implements BeanService {

    private final UserRepository userRepository;
    private final BeanRepository beanRepository;


    // 원두 목록 조회
    public ResponseEntity<?> getBeanList(int userCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 원두 목록 불러오기
        List<Bean> beanList = beanRepository.findAll();
        // 빈 리스트 생성
        List<BeanListDto> beans = new ArrayList<>();

        // 원두 목록 순회하며 Dto 에 담기
        for (Bean bean : beanList) {
            BeanListDto beanListDto = BeanListDto.builder()
                    .beanCode(bean.getBeanCode())
                    .beanName(bean.getBeanName())
                    .beanImgPath(bean.getBeanImgPath())
                    .build();

            // 리스트에 담는다
            beans.add(beanListDto);
        }

        // 리스폰스 생성
        BeanListResponse response = BeanListResponse.builder()
                .beans(beans)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 원두 상세 조회
    public ResponseEntity<?> getBeanDetail(int userCode, int beanCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 원두 불러오기
        Bean bean = beanRepository.findByBeanCode(beanCode);
        // 해당 원두가 존재하는지 검사
        if (bean == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 원두입니다");
        }
        // 리스폰스 생성
        BeanDetailResponse response = BeanDetailResponse.builder()
                .beanCode(beanCode)
                .beanName(bean.getBeanName())
                .beanDescription(bean.getBeanDescription())
                .beanImgPath(bean.getBeanImgPath())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
