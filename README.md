# payment-test


### 1.기동 방법
1. jar 파일로 실행
    > $ mvn package\
     $ java -jar payment-test-1.0.jar --spring.config.location=file:{application.properties 파일 경로}

2. IDE로 실행
    > Maven project import 후 gh.shin.PaymentApp 클래스 실행\
      \*intellij idea로 실행할 경우, test/rest/\*.http 파일을 통해 REST API 테스트 가능

### 2.제약 사항
     java 실행 시 -Dfile.encoding=UTF-8이 되어있지 않은 경우 기동 시 Exception 발생 및 프로세스 종료
     
### 3.동작 설명
1. 기동 시 application.properties 내의 설정을 통해 SpringBoot 기동
2. H2 db 기동 > data.sql에 정의된 테이블 생성 
3. csv 폴더 내의 accounts.csv, payments.csv 파일을 db에 적재
4. 기동 완료 후 application.properties 내의 server.port로 embedded tomcat 기동

### 4.API 설명
     POST /payment
     GET /statistics?groupId={GROUP_ID}
     
   1. POST /payment
      1. Request : 
         1. Content-Type: application/json
         2. Body Example:
             > POST /payment\
               {\
                 "paymentId":1,\
                 "accountId":1,\
                 "amount":300,\
                 "methodType":"카드",\
                 "itemCategory":"식품",\
                 "region":"서울"\
                }

      2. Response :
         1. Content-Type: application/json
         2. Body Example:
              > 200 OK\
               {\
                 "success":true,\
                 "errorMessage":null\
               }
   2. GET /statistics?groupId={GROUP_ID}
      1. Response :
         1. Content-Type: application/json
         2. Body Example:
              > 200 OK\
               {\
                 "groupId":"GROUP_D",\
                 "count":5\
                 "totalAmount":384300,\
                 "avgAmount":76860\
                 "minAmount":3000,\
                 "maxAmount":225000\
               }
### 5.Constants
  * methodType : 카드, 송금
  * itemCategory : 식품, 뷰티, 스포츠, 도서, 패션
  * region : 서울, 부산, 대구, 인천, 광주, 대전, 울산, 세종, 경기, 강원, 충북, 충남, 전북, 전남, 경북, 경남, 제주
  * groupId
    * GROUP_1 : 10,000원 미만의 카드 결제
    * GROUP_2 : 서울, 경기 지역에서 30대인 사람의 결제 (30~39세)
    * GROUP_3 : 결제자의 거주지역에서 패션 상품 결제
    * GROUP_4 : 1,100,000원 이상의 카드 결제 또는 1,000,000원 이상의 송금 결제