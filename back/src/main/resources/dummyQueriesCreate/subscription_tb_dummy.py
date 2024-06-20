# -*- coding: utf-8 -*-
import random
from faker import Faker
from datetime import datetime, timezone, timedelta
fake = Faker()

def create_dummy_data(farm_num):
    sql_queries = []
    
    for num in range(1, farm_num + 1):
        # 몇개의 청약을 넣을 것인가?
        # 진행예정, 진행중 : 0~1, 진행예정 + 진행중 <= 1, 종료 1~3
        subs = {}
        # 0~1 사이 랜덤
        subs['proceed'] = random.randint(0,1)
        subs['progress'] = random.randint(0,1)
        subs['end'] = random.randint(1,3)
        
        # 진행예정과 진행중 모두 1이면
        if subs['proceed'] and subs['progress']:
            # 0으로 만들 것 랜덤 선택
            drop_choice = random.choice(['proceed', 'progress'])
            # 0으로 만듦
            subs[drop_choice] = 0
        
        # 농장 코드
        farm_code = num
        
        # 청약 만들기
        # 고정 값
        # 조각 가격 = 10000원 고정 
        confirm_price = 10000
        # 인당 구매 개수 제한 = 50개 고정
        limit_num = 50
        # 농장주에게 나눠줄 수익 비율 = 10% 고정(임시)
        bean_grade = farm_bean_grade_ls[num-1]
        partner_farm_rate = 12 - bean_grade * 2
        # 총 발행 토큰 수 = 농장 규모 m^2 만큼
        farm_scale = farm_scales[f'{num}']
        total_token_count = farm_scale

        # 매출액(원두 총 판매액) = 농장 규모 * 원두등급 가중치(1:10, 2:7, 3:5) * 10000
        bean_grade_dict = {1:10, 2:7, 3:5}
        total_sales = farm_scale * bean_grade_dict[bean_grade] * 10000

        # 진행 예정 청약
        for i in range(subs['proceed']):
            # 시작 시간 = 1달 후 ~ 1년 후 사이
            started_time = fake.date_time_between(start_date='+1m', end_date='+1y').replace(hour=9, minute=0, second=0).strftime('%Y-%m-%d %H:%M:%S')
            # 종료 시간 = 시작 시간 + 2주 뒤
            ended_time = (datetime.strptime(started_time, '%Y-%m-%d %H:%M:%S') + timedelta(weeks=2)).replace(hour=8, minute=59, second=59).strftime('%Y-%m-%d %H:%M:%S')
            # 수익률은 -10.00~25.00% 소수점 두자리까지
            return_rate = round(random.uniform(-10, 25), 2)
            
            # sql문 생성
            sql = f"INSERT INTO subscription_tb (farm_code, confirm_price, started_time, ended_time, limit_num, return_rate, total_token_count, partner_farm_rate, total_sales) VALUES ('{farm_code}', '{confirm_price}', '{started_time}', '{ended_time}', '{limit_num}', '{return_rate}', '{total_token_count}', '{partner_farm_rate}', {total_sales});"

            sql_queries.append(sql)


        # 진행중 청약
        for i in range(subs['progress']):
            # 시작 시간 = 1주 전 ~ 현재 사이
            started_time = fake.date_time_between(start_date='-1w', end_date='now').replace(hour=9, minute=0, second=0).strftime('%Y-%m-%d %H:%M:%S')
            # 종료 시간 = 시작 시간 + 2주 뒤
            ended_time = (datetime.strptime(started_time, '%Y-%m-%d %H:%M:%S') + timedelta(weeks=2)).replace(hour=8, minute=59, second=59).strftime('%Y-%m-%d %H:%M:%S')
            # 수익률은 0~30.00% 소수점 두자리까지
            return_rate = round(random.uniform(0, 30), 2)

            # sql문 생성
            sql = f"INSERT INTO subscription_tb (farm_code, confirm_price, started_time, ended_time, limit_num, return_rate, total_token_count, partner_farm_rate, total_sales) VALUES ('{farm_code}', '{confirm_price}', '{started_time}', '{ended_time}', '{limit_num}', '{return_rate}', '{total_token_count}', '{partner_farm_rate}', {total_sales});"

            sql_queries.append(sql)


        # 진행종료 청약
        for i in range(subs['end']):
            # 시작 시간 = 2년 전 ~ 2주 전
            started_time = fake.date_time_between(start_date='-2y', end_date='-2w').replace(hour=9, minute=0, second=0).strftime('%Y-%m-%d %H:%M:%S')
            # 종료 시간 = 시작 시간 + 2주 뒤
            ended_time = (datetime.strptime(started_time, '%Y-%m-%d %H:%M:%S') + timedelta(weeks=2)).replace(hour=8, minute=59, second=59).strftime('%Y-%m-%d %H:%M:%S')
            # 수익률은 0~30.00% 소수점 두자리까지
            return_rate = round(random.uniform(0, 30), 2)

            # sql문 생성
            sql = f"INSERT INTO subscription_tb (farm_code, confirm_price, started_time, ended_time, limit_num, return_rate, total_token_count, partner_farm_rate, total_sales) VALUES ('{farm_code}', '{confirm_price}', '{started_time}', '{ended_time}', '{limit_num}', '{return_rate}', '{total_token_count}', '{partner_farm_rate}', {total_sales});"

            sql_queries.append(sql)
        
        
    return sql_queries

# 내 DB에 저장된 farm_scale 칼럼값(162개)
farm_scale_ls = [
    14400, 11700, 12600, 8800, 8200, 14200, 10100, 14500, 10500, 4800, 
    12200, 9200, 12100, 11900, 7200, 6200, 9800, 6100, 14900, 12700, 
    8200, 11500, 4200, 9000, 6700, 13900, 6300, 8100, 10100, 14700, 
    14800, 11500, 7800, 12100, 13800, 11500, 11800, 14200, 13100, 13800, 
    8500, 6600, 6000, 11200, 11400, 13600, 7900, 4500, 7200, 9500, 
    9100, 7700, 10900, 8500, 10800, 12300, 15000, 9300, 8400, 14100, 
    14200, 7400, 6900, 5100, 13600, 8800, 8500, 5000, 5700, 12800, 
    10500, 10900, 13700, 4500, 5300, 7000, 8300, 6400, 9400, 13300, 
    11100, 6800, 5900, 14400, 4700, 14100, 4400, 7900, 11100, 14800, 
    4000, 5200, 14700, 14500, 4900, 5300, 11600, 6800, 10000, 12800, 
    9600, 7300, 5000, 11900, 8600, 14300, 11100, 8100, 9200, 12400, 
    12500, 6400, 13300, 5300, 13900, 4200, 6600, 10500, 11300, 5100, 
    10200, 4200, 11000, 6000, 14500, 14700, 6400, 13900, 13900, 8200, 
    13300, 7700, 5500, 14600, 14500, 13000, 7400, 14200, 9600, 8700, 
    13400, 4200, 13700, 8400, 13400, 5100, 7000, 13500, 9700, 12200, 
    8400, 9800, 9200, 5900, 8600, 13800, 13500, 12300, 10100, 11900, 
    7300, 5000
]

# 내 DB에 저장된 farm_bean_grade 칼럼값(162개)
farm_bean_grade_ls = [
    1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1,
    2, 2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 3, 3, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 3, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 2, 2, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1,
    1, 3, 2, 1, 1, 2, 1, 2, 1, 3, 2, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1,
    1, 1, 2, 2, 1, 2, 1, 1, 1, 3, 1, 1
]

# SQL 쿼리 생성
farm_num = int(input("농장 개수를 입력하세요: "))
# farm_code : farm_scale 형태로 딕셔너리로 만듦
farm_scales = {f'{i}':farm_scale_ls[i-1] for i in range(1, farm_num + 1)}
queries = (create_dummy_data(farm_num))

with open('subs_dummy_data_queries.txt', 'w', encoding='utf-8') as f:
    for query in queries:
        f.write(query + '\n')
print('생성 완료')
