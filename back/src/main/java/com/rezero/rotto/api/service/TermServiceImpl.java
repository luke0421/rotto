package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.response.TermResponse;
import com.rezero.rotto.entity.Term;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.TermsRepository;
import com.rezero.rotto.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TermServiceImpl implements TermService{

    private final UserRepository userRepository;
    private final TermsRepository termsRepository;


    public ResponseEntity<?> getTerms(int userCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        List<Term> termList = termsRepository.findAll();
        if (termList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("데이터가 없습니다.");
        }

        Term term = termList.get(0);
        TermResponse termResponse = TermResponse.builder()
                .title(term.getTitle())
                .content(term.getContent())
                .updateTime(term.getUpdateTime())
                .createTime(term.getCreateTime())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(termResponse);
    }
}
