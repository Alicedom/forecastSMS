package sms;

import java.util.LinkedList;
import java.util.List;

/*
Chua cac rule SMS va cac ham de lay session of day, sms
 */
public class SMSRule {

    public static List<String> getListSMS(Object[][] rules){
        List<String> listSMS = new LinkedList<String>();

        for (Object[] rule : rules) {
            listSMS.add(rule[2].toString());
        }

        return listSMS;
    }
    public static String smsGenerateElement(float data, Object[][] rules){
        String sms="Ngoài ngưỡng "+rules.getClass().getName();

        float leftBound,rightBound;

        for ( Object[] rule: rules) {
            if(rule[0] != null)
                leftBound = Float.valueOf(rule[0].toString());
            else leftBound = Float.MIN_VALUE;

            if (rule[1] !=null)
                rightBound = Float.valueOf(rule[1].toString());
            else rightBound = Float.MAX_VALUE;

            if( leftBound <=data && data < rightBound )
                sms = rule[2].toString();
        }

        return sms;
    }


    public static String getSession(String datetime){
        //yyyy-M-d hh-mm-ss
        return smsGenerateElement(Integer.valueOf(datetime.split(" ")[1].split(":")[0]),session);
    }

    public static String getSession(int hour){
        return smsGenerateElement(hour,session);
    }

    public static Object[][] getSession() {
        return session;
    }

    private static final Object[][] session={
            {0,7,"Sáng sớm"},
            {7,10,"Sáng"},
            {10,13,"Trưa"},
            {13,16,"Chiều"},
            {16,19,"Chiều tối"},
            {19,22,"Tối"},
            {22,24,"Đêm"}
    };

    private static final Object[][] uvSMSRule={
            {null ,34.9 , "Trời tối"},
            {34.9 , 200, "Mây thay đổi"},
            {200, null, "Nắng"}
    };

    private static final Object[][]  temperatureSMSRule={
            {-20,0,"Băng giá"},
            {0,13, "Rét hại"},
            {13,15,"Rét đậm"},
            {15,20,"Rét"},
            {20,35, "Bình thường"},
            {35,37,"Nắng nóng"},
            {37,39,"Nắng nóng gay gắt"},
            {39,null,"Nắng nóng đặc biệt gay gắt"}
    };

    private static final Object[][]  windSpeedConvertLevelRule={
            { null, 0.25,0},
            { 0.25, 1.55,1},
            { 1.55, 3.35,2},
            { 3.35, 5.45,3},
            { 5.45, 7.95,4},
            { 7.95, 10.75,5},
            {10.75,13.85,6},
            {13.86,17.15,7},
            {17.15,20.75,8},
            {20.75,24.45,9},
            {24.45,28.45,10},
            {28.45,32.65,11},
            {32.75,36.95,12},
            {37.05,41.45,13},
            {41.45,46.15,14},
            {46.15,50.95,15},
            {50.95,56.05,16},
            {56.05,61.25,17},
            {61.25,null,18}
    };

    private static final Object[][] getWindSpeedSMSRule={
            {null,5.45,"Không gây nguy hại"},
            {5.45,10.75,"Cây nhỏ có lá bắt đầu lay động. ảnh hưởng đến lúa đang phơi màu. Biển hơi động. Thuyền đánh cá bị chao nghiêng, phải cuốn bớt buồm."},
            {10.75,17.15,"Cây cối rung chuyển. Khó đi ngược gió. Biển động. Nguy hiểm đối với tàu, thuyền."},
            {17.15,24.45,"Gió làm gãy cành cây, tốc mái nhà gây thiệt hại về nhà cửa. Không thể đi ngược gió. Biển động rất mạnh. Rất nguy hiểm đối với tàu, thuyền."},
            {24.45,32.65,"Làm đổ cây cối, nhà cửa, cột điện. Gây thiệt hại rất nặng. Biển động dữ dội. Làm đắm tàu biển."},
            {32.65,null,"Sức phá hoại cực kỳ lớn. Sóng biển cực kỳ mạnh. Đánh đắm tàu biển có trọng tải lớn."}
    };
    private static final Object[][] getHumiditySMSRule={
            {null,30.5,"Rất khô"},
            {30.5,50.5,"Khô hanh"},
            {50.5,89.5,"Bình thường"},
            {89.5,100,"Ẩm ướt"},
            {100,null,"Mưa, sương mù"}

    };
    private static final Object[][] getRainSMSRule={
            {null,null," không mưa"},
            {0.0,0.3," mưa nhỏ lượng không đáng kể"},
            {0.3,3.0," mưa nhỏ"},
            {3.0,8.0," mưa"},
            {8.0,25.0," mưa vừa"},
            {25.0,50.0," mưa to"},
            {50.0,null," mưa rất to"}
    };

    private static final Object[][] getWindDirectSMSRule={
            {0.0,11.5,"Bắc"},
            {11.5,33.5,"Bắc Đông Bắc"},
            {33.5,56.5,"Đông Bắc"},
            {56.5,78.5,"Đông Đông Bắc"},
            {78.5,101.5,"Đông"},
            {101.5,123.5,"Đông Đông Nam"},
            {123.5,146.5,"Đông Nam"},
            {146.5,168.5,"Nam Đông Nam"},
            {168.5,191.5,"Nam"},
            {191.5,213.5,"Nam Tây Nam"},
            {213.5,236.5,"Tây Nam"},
            {236.5,258.5,"Tây Tây Nam"},
            {258.5,281.5,"Tây"},
            {281.5,303.5,"Tây Tây Bắc"},
            {303.5,326.5,"Tây Bắc"},
            {326.5,348.5,"Bắc Tây Bắc"},
            {348.5,360.5,"Bắc"}
    };

    private static final Object[][] getWindDirectELRule={

            {0.0,11.5,"N"},
            {11.5,33.5,"NNE"},
            {33.5,56.5,"NE"},
            {56.5,78.5,"ENE"},
            {78.5,101.5,"E"},
            {101.5,123.5,"ESE"},
            {123.5,146.5,"SE"},
            {146.5,168.5,"SSE"},
            {168.5,191.5,"S"},
            {191.5,213.5,"SSW"},
            {213.5,236.5,"SW"},
            {236.5,258.5,"WSW"},
            {258.5,281.5,"W"},
            {281.5,303.5,"WNW"},
            {303.5,326.5,"NW"},
            {326.5,348.5,"NNW"},
            {348.5,360.5,"N"}
    };

    public static Object[][] getUvSMSRule() {
        return uvSMSRule;
    }

    public static Object[][] getTemperatureSMSRule() {
        return temperatureSMSRule;
    }

    public static Object[][] getWindSpeedConvertLevelRule() {
        return windSpeedConvertLevelRule;
    }

    public static Object[][] getGetWindSpeedSMSRule() {
        return getWindSpeedSMSRule;
    }

    public static Object[][] getGetHumiditySMSRule() {
        return getHumiditySMSRule;
    }

    public static Object[][] getGetRainSMSRule() {
        return getRainSMSRule;
    }

    public static Object[][] getGetWindDirectSMSRule() {
        return getWindDirectSMSRule;
    }

    public static Object[][] getGetWindDirectELRule() {
        return getWindDirectELRule;
    }



}
