package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.dto.FarmDto;
import com.rezero.rotto.dto.dto.FarmListDto;
import com.rezero.rotto.dto.dto.MyPageFarmListDto;
import com.rezero.rotto.dto.response.FarmDetailResponse;
import com.rezero.rotto.dto.response.FarmListResponse;
import com.rezero.rotto.dto.response.FarmTop10ListResponse;
import com.rezero.rotto.dto.response.MyPageFarmListResponse;
import com.rezero.rotto.entity.*;
import com.rezero.rotto.repository.*;
import com.rezero.rotto.utils.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.rezero.rotto.utils.Const.VALID_BEAN_TYPES;

@Service
@Transactional
@RequiredArgsConstructor
public class FarmServiceImpl implements FarmService {

    private final FarmRepository farmRepository;
    private final UserRepository userRepository;
    private final InterestFarmRepository interestFarmRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final FarmTop10Repository farmTop10Repository;
    private final Pagination pagination;


    // 농장 목록 조회
    public ResponseEntity<?> getFarmList(int userCode, Integer page, Boolean isLiked, Integer subsStatus, Integer minPrice, Integer maxPrice, String beanType, String sort, String keyword) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 파라미터 유효성 검사
        if (!isValidInput(isLiked, subsStatus, minPrice, maxPrice, beanType)) {
            return ResponseEntity.badRequest().body("잘못된 입력값입니다.");
        }

        // Specification 를 통해 필터링 및 정렬
        Specification<Farm> spec = buildSpecification(userCode, isLiked, subsStatus, minPrice, maxPrice, beanType, sort, keyword);

        // 농장 목록 불러오기
        List<Farm> farms = farmRepository.findAll(spec);
        // 인덱스 선언
        int startIdx = 0;
        int endIdx = 0;
        // 총 페이지 수 선언
        int totalPages = 1;

        // 페이지네이션
        List<Integer> indexes = pagination.pagination(page, 10, farms.size());
        startIdx = indexes.get(0);
        endIdx = indexes.get(1);
        totalPages = indexes.get(2);

        // 페이지네이션
        List<Farm> pageFarms = farms.subList(startIdx, endIdx);

        // 마이페이지일 때와 마이페이지가 아닐 때를 분리하여 Dto 생성
        List<? extends FarmDto> farmDtos = convertToDtoList(pageFarms, userCode,  isLiked != null && isLiked && subsStatus == null && minPrice == null && maxPrice == null && beanType == null && sort == null && keyword == null);

        // 마이페이지일 때와 마이페이지가 아닐 때를 분리하여 Dto 생성하고 반환
        return ResponseEntity.status(HttpStatus.OK).body(buildResponse(farmDtos, totalPages));
    }


    // 농장 상세 조회(원두 상세 조회, 관심 농장 여부 추가 필요)
    public ResponseEntity<?> getFarmDetail(int userCode, int farmCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }

        // 농장이 존재하는지 검사
        Farm farm =  farmRepository.findByFarmCode(farmCode);
        if (farm == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("농장을 찾을 수 없습니다.");
        }

        // 관심 농장 여부 검사
        boolean isLiked = false;
        InterestFarm interestFarm = interestFarmRepository.findByFarmCodeAndUserCode(farmCode, userCode);
        if (interestFarm != null) {
            isLiked = true;
        }

        // 종료된 청약 리스트(최신순 정렬) 불러오기
        LocalDateTime now = LocalDateTime.now();
        List<Subscription> latestEndedSubscriptions = subscriptionRepository.findLatestEndedSubscription(farmCode, now);
        Subscription latestEndedSubscription = null;
        // 최근 종료 청약이 있으면 첫번째 요소 가져옴
        if (!latestEndedSubscriptions.isEmpty()) {
            latestEndedSubscription = latestEndedSubscriptions.get(0);
        }

        BigDecimal returnRate = null;
        if (latestEndedSubscription != null) {
            // 수익률
            returnRate = latestEndedSubscription.getReturnRate();
        }

        // 좋아요 수 가져오기
        Long likeCount = interestFarmRepository.countByFarmCode(farmCode);
        // 펀딩 진행 여부
        Boolean isFunding = isFunding(farmCode);

        // 펀딩 종료까지 남은 기간 계산
        // endedTime >= 현재 시간 인 것. endedTime - 현재 시간 (일수로만)
        Integer deadline = null;
        List<Subscription> impendingOngoingSubscriptions = subscriptionRepository.findImpedingOngoingSubscription(farmCode, now);
        Subscription impendingOngoingSubscription = null;
        if (!impendingOngoingSubscriptions.isEmpty()) {
            impendingOngoingSubscription = impendingOngoingSubscriptions.get(0);
        }

        if (impendingOngoingSubscription != null) {
            long daysBetween = ChronoUnit.DAYS.between(now.toLocalDate(), impendingOngoingSubscription.getEndedTime().toLocalDate());
            deadline = (int) daysBetween;
        }

        // 리스폰스 생성
        FarmDetailResponse response = FarmDetailResponse.builder()
                .farmCode(farmCode)
                .farmName(farm.getFarmName())
                .farmCeoName(farm.getFarmCeoName())
                .farmLogoPath(farm.getFarmLogoPath())
                .farmAddress(farm.getFarmAddress())
                .farmScale(farm.getFarmScale())
                .farmIntroduce(farm.getFarmIntroduce())
                .farmStartedDate(farm.getFarmStartedTime())
                .awardHistory(farm.getAwardHistory())
                .beanName(farm.getFarmBeanName())
                .beanGrade(farm.getFarmBeanGrade())
                .returnRate(returnRate)
                .isLiked(isLiked)
                .isFunding(isFunding)
                .likeCount(likeCount)
                .deadline(deadline)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 수익률 Top 10 농장 목록 조회
    public ResponseEntity<?> getRateTop10FarmList(int userCode) {
        // 해당 유저가 존재하는지 검사
        User user = userRepository.findByUserCode(userCode);
        if (user == null || user.getIsDelete()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
        }
        // FarmTop10 리스트 가져오기
        List<FarmTop10> farmTop10List = farmTop10Repository.findAll();
        // farm 빈 리스트로 생성
        List<FarmListDto> farms = new ArrayList<>();

        // farmTop10List 순회하며 farmCode 이용하여 정보 불러오고 Dto 에 넣기
        for (FarmTop10 farmTop10 : farmTop10List) {
            Farm farm = farmRepository.findByFarmCode(farmTop10.getFarmCode());
            // 관심 농장 여부 검사
            boolean isLiked = false;
            InterestFarm interestFarm = interestFarmRepository.findByFarmCodeAndUserCode(farm.getFarmCode(), userCode);
            if (interestFarm != null) {
                isLiked = true;
            }
            // 종료된 청약 리스트(최신순 정렬) 불러오기
            int farmCode = farm.getFarmCode();
            LocalDateTime now = LocalDateTime.now();
            List<Subscription> latestEndedSubscriptions = subscriptionRepository.findLatestEndedSubscription(farmCode, now);
            Subscription latestEndedSubscription = null;
            // 최근 종료 청약이 있으면 첫번째 요소 가져옴
            if (!latestEndedSubscriptions.isEmpty()) {
                latestEndedSubscription = latestEndedSubscriptions.get(0);
            }
            // 최근 종료 청약의 수익률 구하기
            BigDecimal returnRate;
            if (latestEndedSubscription == null) {
                returnRate = null;
            } else {
                returnRate = latestEndedSubscription.getReturnRate();
            }

            // 좋아요 수 계산
            Long likeCount = interestFarmRepository.countByFarmCode(farmCode);
            // 펀딩 진행여부 계산
            Boolean isFunding = isFunding(farmCode);

            // Dto 생성
            FarmListDto farmListDto = FarmListDto.builder()
                    .farmCode(farmCode)
                    .farmName(farm.getFarmName())
                    .farmLogoPath(farm.getFarmLogoPath())
                    .beanName(farm.getFarmBeanName())
                    .returnRate(returnRate)
                    .isLiked(isLiked)
                    .isFunding(isFunding)
                    .likeCount(likeCount)
                    .build();
            // farms 에 하나씩 담기
            farms.add(farmListDto);
        }
        // 리스폰스 생성
        FarmTop10ListResponse response = FarmTop10ListResponse.builder()
                .farms(farms)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 입력값 오류 검사
    private boolean isValidInput(Boolean isLiked, Integer subsStatus, Integer minPrice, Integer maxPrice, String beanType) {
        // isLiked 가 null 이 아니면서 true 도 아니다.
        if (isLiked != null && !isLiked) {
            return false;
        }
        // subsStatus 가 null 이 아니면서 0 미만이거나 2 초과다.
        if (subsStatus != null && (subsStatus < 0 || subsStatus > 2)) {
            return false;
        }
        // minPrice 와 maxPrice 가 모두 null 이 아니면서 minPrice 가 maxPrice 보다 크다.
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            return false;
        }

        // beanType 이 null 이 아니면서 허용 리스트에 포함되지 않는 값이면 false 처리
        return beanType == null || VALID_BEAN_TYPES.contains(beanType);
    }


    // RequestParam 에 따른 필터링 및 정렬 처리
    private Specification<Farm> buildSpecification(int userCode, Boolean isLiked, Integer subsStatus, Integer minPrice, Integer maxPrice, String beanType, String sort, String keyword) {
        // Specification<Farm> 생성
        Specification<Farm> spec = Specification.where(null);
        // keyword 값에 따른 검색어 필터링
        if (keyword != null) spec = spec.and(FarmSpecification.nameContains(keyword));
        // 관심 농장 필터링
        if (isLiked != null && isLiked) spec = spec.and(FarmSpecification.hasInterest(userCode));
        // 청약 상태에 따른 필터링
        if (subsStatus != null) spec = spec.and(FarmSpecification.filterBySubscriptionStatus(subsStatus));
        // 최소 가격, 최대 가격에 따른 필터링
        if (minPrice != null || maxPrice != null)
            spec = spec.and(FarmSpecification.priceBetween(minPrice, maxPrice));
        // 원두 종류에 따른 필터링
        if (beanType != null) spec = spec.and(FarmSpecification.filterByBeanType(beanType));
        // 정렬
        spec = spec.and(FarmSpecification.applySorting(sort));
        return spec;
    }


    // 마이페이지일 때와 마이페이지가 아닐 때를 분리하여 Dto 생성
    private List<? extends FarmDto> convertToDtoList(List<Farm> farms, int userCode, Boolean isMyPage) {
        // 빈 리스트 생성
        List<FarmDto> farmDtos = new ArrayList<>();
        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // 농장들을 순회
        for (Farm farm : farms) {
            // 해당 농장이 클라이언트의 관심 농장인지 판별
            Boolean farmIsLiked = interestFarmRepository.findByFarmCodeAndUserCode(farm.getFarmCode(), userCode) != null;
            // 해당 농장이 펀딩 진행중인지 판별
            Boolean isFunding = isFunding(farm.getFarmCode());

            // dto 생성
            FarmDto farmDto;
            int farmCode = farm.getFarmCode();
            // 종료된 청약 리스트(최신순 정렬) 불러오기
            List<Subscription> latestEndedSubscriptions = subscriptionRepository.findLatestEndedSubscription(farmCode, now);
            Subscription latestEndedSubscription = null;
            // 최근 종료 청약이 있으면 첫번째 요소 가져옴
            if (!latestEndedSubscriptions.isEmpty()) {
                latestEndedSubscription = latestEndedSubscriptions.get(0);
            }

            // 최근 수익률 가져오기
            BigDecimal returnRate;
            if (latestEndedSubscription == null) {
                returnRate = null;
            } else {
                returnRate = latestEndedSubscription.getReturnRate();
            }

            // 좋아요 수 가져오기
            Long likeCount = interestFarmRepository.countByFarmCode(farmCode);

            // 마이페이지에 따른 Dto 분리 및 추가
            if (isMyPage) {
                farmDto = new MyPageFarmListDto(farmCode, farm.getFarmName(), farm.getFarmLogoPath(), farm.getFarmBeanName(), farmIsLiked, returnRate, isFunding, likeCount);
            } else {
                farmDto = new FarmListDto(farmCode, farm.getFarmName(), farm.getFarmLogoPath(), farm.getFarmBeanName(), farmIsLiked, returnRate, isFunding, likeCount);
            }
            farmDtos.add(farmDto);
        }
        return farmDtos;
    }


    // 마이페이지일 때와 마이페이지가 아닐 때를 분리하여 리스폰스 생성
    private Object buildResponse(List<? extends FarmDto> farmDtos, int totalPages) {
        // 빈 리스트가 들어오면 빈 리스폰스 반환
        if (farmDtos.isEmpty()) {
            return MyPageFarmListResponse.builder().farms(Collections.emptyList()).build();
        }
        // instanceof : 객체가 특정 클래스나 인터페이스의 인스턴스인지 여부를 확인하는 연산자
        // 이 블록은 farmDtos 의 첫 번째 요소가 MyPageFarmListDto 타입인 경우에만 실행
        if (farmDtos.get(0) instanceof MyPageFarmListDto) {
            // Dto 생성하여 리스폰스에 넣고 반환
            List<MyPageFarmListDto> myPageFarmListDtos = farmDtos.stream()
                    .map(farmDto -> (MyPageFarmListDto) farmDto)
                    .collect(Collectors.toList());
            return MyPageFarmListResponse.builder()
                    .farms(myPageFarmListDtos)
                    .totalPages(totalPages)
                    .build();
        // 이 블록은 farmDtos 의 첫 번째 요소가 FarmListDto 타입인 경우에만 실행
        } else {
            // Dto 생성하여 리스폰스에 넣고 반환
            List<FarmListDto> farmListDtos = farmDtos.stream()
                    .map(farmDto -> (FarmListDto) farmDto)
                    .collect(Collectors.toList());
            return FarmListResponse.builder()
                    .farms(farmListDtos)
                    .totalPages(totalPages)
                    .build();
        }
    }


    // 펀딩 진행 여부 검사
    private boolean isFunding(int farmCode) {
        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        // 농장과 연관된 청약 리스트 불러오기
        List<Subscription> subscriptions = subscriptionRepository.findByFarmCode(farmCode);

        // startedTime < 현재 시간 < endedTime = 청약 진행중
        for (Subscription subscription : subscriptions) {
            if (subscription.getStartedTime().isBefore(now) && subscription.getEndedTime().isAfter(now)) {
                return true;
            }
        }

        return false;
    }
}
