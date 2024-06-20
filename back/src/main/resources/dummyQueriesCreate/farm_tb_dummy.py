# -*- coding: utf-8 -*-
import random
from faker import Faker
from datetime import datetime, timezone
fake = Faker()

def create_dummy_data(n):
    sql_queries = []
    for num in range(n):
        # 이름으로 사용할 더미 n개
        farm_name_english = ['에이드리언', '마크', '코트니', '베스', '스테이시', '앤서니', '브래들리', '카일', '그렉', '라우리', '섀넌', '로라', '피터', '채드', '커티스', '캐서린', '모건', '스티븐',
        '토니', '트래비스', '자레드', '에밀리', '신시아', '라토야', '나이틀린', '보니', '매튜', '요셉', '제나', '태너', '로빈', '나타샤', '자스민', '엘리자베스', '루이스', '메이븐', '숀', '레이먼드', '이본', '레이첼', '웨인', '크리스토퍼', '다이안', '토마스', '알렉산더', '캐머론', '리처드', '샤론', '데보라', '로건', '아담', '브라이언', '스코트', '코니', '티모시', '제이클린', '맬러리', '아만다', '메건', '쉘비', '켈리', '제시카', '존', '조나단', '러셀', '저스틴', '던', '케이틀린', '빅터', '카티', '베서니', '멜리사', '후안', '찰린', '제이스', '레오나르드', '패트리시아', '사라', '티나', '줄리', '헤더', '패트릭', '타라', '알리샤', '롤리', '폴', '토드', '다니엘', '마리아', '미셸', '카렌', '베티', '디아나', '린드', '밥', '에이프릴', '제임스', '켈시', '제프리', '조제', '조슈아', '로버트', '제니퍼', '에블린', '니콜', '킴벌리', '케이티', '안젤라', '파멜라', '엘리야', '윌리엄', '케빈', '리사', '브룩', '스티브', '트레버', '에린', '토니아', '린다', '앤드류', '로버타', '제리', '제퍼리', '크리스티나', '타냐', '크로빈', '크레이그', '에밀리아', '데일리', '스테파니', '데이비드', '크레이머', '샬롯', '스미스', '완다', '브렌다', '케릴', '모니카', '베를린', '미숀', '잭', '모랄레스', '빌리', '루', '토빈', '한나', '트로이', '트레이시', '캐리', '에릭', '제이콥', '제이슨', '마이클', '제레미', '에반스', '톰', '션', '수산', '랜디', '필립', '제니스', '마틴']

        # 농장이름 = 이름 + 농장
        farm_name = f"{farm_name_english[num]} 농장"

        # 농장 ceo 이름 = 이름
        farm_ceo_name = farm_name_english[num]

        # 주소 랜덤 생성
        farm_address = fake.address()

        # 농장 규모 4,000~15,000m^2
        farm_scale = random.randint(40, 150) * 100

        # 50년 전부터 1년 전까지 날짜 랜덤 생성(timestamp 형태, 0시 0분 0초는 고정)
        farm_started_time = fake.date_time_between(start_date='-50y', end_date='-1y').replace(hour=0, minute=0, second=0).strftime('%Y-%m-%d %H:%M:%S')

        # 원두 이름 더미 5개
        farm_bean_name = random.choice(["자메이카 블루마운틴", "예멘 모카 아이리시", "예멘 애플 마티니", "하와이 코나", "파나마 게이샤"])

        # 원두 등급 1등급 70%, 2등급 20%, 3등급 10% 비율로 생성
        farm_bean_grade = random.choices([1, 2, 3], weights=[70, 20, 10], k=1)[0]

        # 수상 내역 만들기
        cup_awards = []
        for i in range(random.randint(2,4)):
            award_year = random.randint(1970, 2022)
            award_name = fake.company()
            award_ranking = random.choice(["Grand Prize", "First Prize", "Second Prize", "Third Prize", "Special Prize"])
            award = f"{award_year} {award_name} Cup Coffee Bean Contest {award_ranking}"
            cup_awards.append(award)

        category_awards = []
        for i in range(random.randint(2,4)):
            award_year = random.randint(1970, 2022)
            award_name = fake.company()
            award_ranking = random.choice(["1st Place", "2nd Place", "3rd Place"])
            award = f"{award_year} {award_name} Wins {award_ranking} in Most Outstanding Coffee Bean category"
            category_awards.append(award)
            
        combined_awards = cup_awards + category_awards
        # 줄바꿈당 하나의 수상내역으로 만듦
        award_history = "\n".join(random.sample(combined_awards, k=random.randint(1, len(combined_awards))))
        
        # 계좌번호 1로 시작하는 12자리 문자열
        farm_bank_account_num = '1' + ''.join([str(random.randint(0, 9)) for _ in range(11)])

        # sql 문 생성
        sql = f"INSERT INTO farm_tb (farm_name, farm_ceo_name, farm_address, farm_scale, farm_started_time, award_history, farm_bean_name, farm_bean_grade, farm_bank_account_num) VALUES ('{farm_name}', '{farm_ceo_name}', '{farm_address}', {farm_scale}, '{farm_started_time}', '{award_history}', '{farm_bean_name}', {farm_bean_grade}, '{farm_bank_account_num}');"

        sql_queries.append(sql)
        
    return sql_queries

# SQL 쿼리 생성
n = int(input("더미 데이터 개수를 입력하세요: "))
queries = (create_dummy_data(n))
print(queries)
with open('farm_dummy_data_queries.txt', 'w', encoding='utf-8') as f:
    for query in queries:
        f.write(query + '\n')
print('생성 완료')
