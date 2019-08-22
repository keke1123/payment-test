package gh.shin;

import java.util.Arrays;
import java.util.List;

import static gh.shin.Constants.Location.BUSAN;
import static gh.shin.Constants.Location.CHUNGBUK;
import static gh.shin.Constants.Location.CHUNGNAM;
import static gh.shin.Constants.Location.DAEGU;
import static gh.shin.Constants.Location.DAEJEON;
import static gh.shin.Constants.Location.GANGWON;
import static gh.shin.Constants.Location.GWANJU;
import static gh.shin.Constants.Location.GYEONGGI;
import static gh.shin.Constants.Location.INCHEON;
import static gh.shin.Constants.Location.JEJU;
import static gh.shin.Constants.Location.JEONBUK;
import static gh.shin.Constants.Location.JEONNAM;
import static gh.shin.Constants.Location.KYUNGBUK;
import static gh.shin.Constants.Location.KYUNGNAM;
import static gh.shin.Constants.Location.SEJONG;
import static gh.shin.Constants.Location.SEOUL;
import static gh.shin.Constants.Location.WULSAN;

public class Constants {
    public static final List<String> LOCATIONS = Arrays.asList(
        SEOUL, BUSAN, DAEGU, INCHEON, GWANJU, DAEJEON, WULSAN,
        SEJONG, GYEONGGI, GANGWON, CHUNGBUK, CHUNGNAM,
        JEONBUK, JEONNAM, KYUNGBUK, KYUNGNAM, JEJU
    );

    public interface Location {
        String SEOUL = "서울";
        String BUSAN = "부산";
        String DAEGU = "대구";
        String INCHEON = "인천";
        String GWANJU = "광주";
        String DAEJEON = "대전";
        String WULSAN = "울산";
        String SEJONG = "세종";
        String GYEONGGI = "경기";
        String GANGWON = "강원";
        String CHUNGBUK = "충북";
        String CHUNGNAM = "충남";
        String JEONBUK = "전북";
        String JEONNAM = "전남";
        String KYUNGBUK = "경북";
        String KYUNGNAM = "경남";
        String JEJU = "제주";
    }

    public interface PaymentMethod {
        String CARD = "카드";
        String CASH = "송금";
    }

    public interface Category {
        String FOODS = "식품";
        String BEAUTY = "뷰티";
        String SPORTS = "스포츠";
        String BOOKS = "도서";
        String FASHION = "패션";
    }

    public interface Group {
        String GROUP_1 = "GROUP_1";
        String GROUP_2 = "GROUP_2";
        String GROUP_3 = "GROUP_3";
        String GROUP_4 = "GROUP_4";
    }
}
