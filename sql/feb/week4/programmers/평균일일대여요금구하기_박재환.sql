-- 소수점 반올림 (ROUND / CEILING)
-- ROUND(숫자, 소수점 자리수) : 소수점 반올림
-- CEILING(숫자) : 소수점 올림
SELECT ROUND(AVG(daily_fee)) AS 'AVERAGE_FEE' FROM CAR_RENTAL_COMPANY_CAR
WHERE car_type = 'SUV'
GROUP BY car_type