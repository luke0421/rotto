package com.rezero.rotto.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Pagination {
    
    
    // 일반적인 상황에서의 페이지네이션
    public List<Integer> pagination(Integer page, int showNum, int arraySize) {
        int startIdx = 0;
        int endIdx = 0;
        if (page != null) {
            startIdx = Math.min((page - 1) * showNum, arraySize) / showNum * showNum;
            endIdx = Math.min(startIdx + 10, arraySize);
        } else {
            endIdx = arraySize;
        }
        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) arraySize / showNum);

        List<Integer> indexes = new ArrayList<>();
        indexes.add(startIdx);
        indexes.add(endIdx);
        indexes.add(totalPages);

        return indexes;
    }

}
