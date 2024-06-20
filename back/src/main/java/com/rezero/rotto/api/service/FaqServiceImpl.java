package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.dto.FaqListDto;
import com.rezero.rotto.dto.response.FaqListResponse;
import com.rezero.rotto.entity.Faq;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.FaqReqository;
import com.rezero.rotto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService{

    private final UserRepository userRepository;
    private final FaqReqository faqReqository;

    public ResponseEntity<?> getFaqList(int userCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        List<Faq> faqList = faqReqository.findAll();
        List<FaqListDto> faqListDtos = new ArrayList<>();

        for (int i = 0; i < faqList.size(); i++){
            FaqListDto faqListDto = FaqListDto.builder()
                    .faqCode(faqList.get(i).getFaqCode())
                    .title(faqList.get(i).getTitle())
                    .content(faqList.get(i).getContent())
                    .build();
            faqListDtos.add(faqListDto);
        }

        FaqListResponse faqListResponse = FaqListResponse.builder()
                .faqListDtos(faqListDtos)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(faqListResponse);
    }

}
