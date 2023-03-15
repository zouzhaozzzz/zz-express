package com.zouzhao.opt.manage.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 姚超
 * @DATE: 2023-3-13
 */
public class RandomUtils {

    // Utility 工具

    // CTRL + SHIFT + U 批量转换大小写
    // static 修饰的内容","是不依赖于这个类的任何实例的","当这个类一旦在运行期间加载进来","它就已经存在","有且只有一份","大家共享的
    // static修饰的方法只能访问static修饰的变量
    private final static String data = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; // 字典数据
    private static final Random ran = new Random(); // 对于引用类型","只要它自己的地址不发生改变","也可以修饰为final的

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // private static final String[] genders = new String[]{"male, female"};
    private static final String[] genders = {"male", "female"}; // 利用了编译器的优化机制(语法糖)进行简化","和上面这一行等效

    private static final String[] firstNames = {"王", "李", "张", "刘", "陈", "杨", "黄", "赵", "吴", "周", "徐", "孙", "马", "朱", "胡", "郭", "何", "高", "林", "罗", "郑", "梁", "谢", "宋", "唐", "许", "韩", "冯", "邓", "曹", "彭", "曾", "肖", "田", "董", "袁", "潘", "于", "蒋", "蔡", "余", "杜", "叶", "程", "苏", "魏", "吕", "丁", "任", "沈", "姚", "卢", "姜", "崔", "钟", "谭", "陆", "汪", "范", "金", "石", "廖", "贾", "夏", "韦", "傅", "方", "白", "邹", "孟", "熊", "秦", "邱", "江", "尹", "薛", "闫", "段", "雷", "侯"};
    private static final String[] lastNames = {"加", "鸿", "稷", "蔚", "鸿", "香", "昕", "中", "轩", "梓", "鑫", "玥", "怡", "濡", "玲", "卓", "逸", "骏", "康", "冰", "锟", "颜", "腾", "月", "韦", "曼", "勇", "雪", "舒", "礼", "彦", "丽", "柏", "正", "升", "优", "霞", "尧", "吉", "璇", "雯", "侠", "裕", "彤", "裕", "鑫", "冬", "柔", "娅", "振", "妮", "升", "泽", "凯", "沛", "鹤", "柏", "年", "梓", "函", "礼", "邦", "喆", "彬", "潍", "澄", "澄", "优", "凡", "礼",
            "韦", "坤", "昕", "璟", "海", "函", "惠", "东", "彩", "荣", "彦", "震", "呈", "钰", "腾", "谛", "欣", "雪", "初", "暄", "紫", "德", "浩", "祜", "怡", "铭", "斌", "稷", "桂", "帝", "弦", "香", "峰", "花", "洲", "柏", "运", "子", "淑", "翱", "鹏", "弘", "鹏", "雅", "东", "芃", "格", "岚", "蓓", "祥", "沛", "橘", "年", "璟", "平", "可", "璟", "辞", "洲", "佑", "凡", "锦", "敏", "柏", "云", "美", "加", "楠", "菡", "运", "荣", "金", "璇", "初", "航", "芳", "诗", "欣", "金", "娅", "泽", "爵", "怡", "琪",
            "杉", "金", "星", "鑫", "礼", "克", "芸", "桂", "佑", "东", "日", "弦", "恒", "璟", "锟", "秀", "初", "采", "玥", "欢", "中", "欣", "晓", "泽", "钰", "凡", "克", "璟", "韵", "薇", "翱邦", "允", "灵", "雪", "蓓", "曦", "柏", "鸿", "祥", "香", "子", "心", "惠", "佑", "枫"};

    //组织ids
    private static final String[] orgIds = {"1629689987338137601",
            "1629690009769275393",
            "1629690024159932418",
            "1629690037497819137",
            "1629690050902814722",
            "1635260635812515841",
            "1635260687830274049",
            "1635260812870864898",
            "1635261013484425217",
            "1635261065195999233",
            "1635261233400172546",
            "1635261270406516737",
            "1635261444562407425",
            "1635261640402849794",
            "1635261687446163458",
            "1635261938085187586",
            "1635261987196293121",
            "1635262139919290369",
            "1635262229958414338",
            "1635262308580642818",
            "1635262349558992898",
            "1635262393053925377",
            "1635262450247454722",
            "1635262512906162178",
            "1635262557210595330",
            "1635262605784829953",
            "1635262639729332226",
            "1635262679130624002",
            "1635262784151801858",
            "1635262825910292481",
            "1635262867765252098",
            "1635262903651717122",
            "1635262942725853186",
            "1635262979509899266",
            "1635263022761562114",
            "1635263601424519170",
            "1635263634244947969",
            "1635263684413018114",
            "1635263719829721090",
            "1635263759876935681",
            "1635263813803102209",
            "1635263842089488386",
            "1635263897668210690"};

    private static final String[] provinces = {
            "北京市,市辖区,东城区", "北京市,市辖区,西城区", "北京市,市辖区,海淀区", "北京市,市辖区,朝阳区",
            "天津市,市辖区,和平区", "天津市,市辖区,河东区", "天津市,市辖区,河西区", "天津市,市辖区,南开区",
            "河北省,石家庄,长安区", "河北省,石家庄,桥西区", "河北省,石家庄,新华区", "河北省,石家庄,并陉矿区",
            "山西省,太原市,小店区", "山西省,太原市,迎泽区", "山西省,太原市,杏花岭区", "山西省,太原市,尖草坪区",
            "内蒙古自治区,呼和浩特市,回民区", "内蒙古自治区,呼和浩特市,新城区", "内蒙古自治区,呼和浩特市,玉泉区", "内蒙古自治区,呼和浩特市,赛罕区",
            "辽宁省,沈阳市,和平区", "辽宁省,沈阳市,沈河区", "辽宁省,沈阳市,大东区", "辽宁省,沈阳市,皇姑区",
            "吉林省,长春市,南关区", "吉林省,长春市,宽城区", "吉林省,长春市,朝阳区", "吉林省,长春市,二道区",
            "黑龙江省,哈尔滨市,道里区", "黑龙江省,哈尔滨市,南岗区", "黑龙江省,哈尔滨市,道外区", "黑龙江省,哈尔滨市,平房区",
            "上海市,市辖区,黄浦区", "上海市,市辖区,徐汇区", "上海市,市辖区,长宁区", "上海市,市辖区,静安区",
            "江苏省,南京市,玄武区", "江苏省,南京市,秦淮区", "江苏省,南京市,建邺区", "江苏省,南京市,鼓楼区",
            "浙江省,杭州市,上城区", "浙江省,杭州市,下城区", "浙江省,杭州市,江干区", "浙江省,杭州市,拱墅区",
            "安徽省,合肥市,蜀山区", "安徽省,合肥市,瑶海区", "安徽省,合肥市,庐阳区", "安徽省,合肥市,包河区",
            "福建省,福州市,鼓楼区", "福建省,福州市,台江区", "福建省,福州市,仓山区", "福建省,福州市,马尾区",
            "江西省,南昌市,东湖区", "江西省,南昌市,西湖区", "江西省,南昌市,青云谱区", "江西省,南昌市,青山湖区",
            "山东省,济南市,历下区", "山东省,济南市,市中区", "山东省,济南市,槐荫区", "山东省,济南市,天桥区",
            "河南省,郑州市,中原区", "河南省,郑州市,二七区", "河南省,郑州市,管城回族区", "河南省,郑州市,金水区",
            "湖北省,武汉市,江岸区", "湖北省,武汉市,江汉区", "湖北省,武汉市,硚口区", "湖北省,武汉市,汉阳区",
            "湖南省,长沙市,芙蓉区", "湖南省,长沙市,天心区", "湖南省,长沙市,岳麓区", "湖南省,长沙市,开福区",
            "广东省,广州市,越秀区", "广东省,广州市,荔湾区", "广东省,广州市,海珠区", "广东省,广州市,天河区",
            "广西壮族自治州,南宁市,兴宁区", "广西壮族自治州,南宁市,青秀区", "广西壮族自治州,南宁市,江南区", "广西壮族自治州,南宁市,西乡塘区",
            "海南省,海口市,秀英区", "海南省,海口市,龙华区", "海南省,海口市,琼山区", "海南省,海口市,美兰区",
            "重庆市,市辖区,万州区", "重庆市,市辖区,黔江区", "重庆市,市辖区,涪陵区", "重庆市,市辖区,渝中区",
            "四川省,成都市,锦江区", "四川省,成都市,青羊区", "四川省,成都市,金牛区", "四川省,成都市,武侯区",
            "贵州省,贵阳市,观山湖区", "贵州省,贵阳市,南明区", "贵州省,贵阳市,云岩区", "贵州省,贵阳市,花溪区",
            "云南省,昆明市,五华区", "云南省,昆明市,盘龙区", "云南省,昆明市,官渡区", "云南省,昆明市,西山区",
            "西藏自治区,拉萨市,城关区", "西藏自治区,拉萨市,堆龙德庆区", "西藏自治区,拉萨市,达孜区",
            "陕西省,西安市,新城区", "陕西省,西安市,碑林区", "陕西省,西安市,莲湖区", "陕西省,西安市,雁塔区",
            "甘肃省,甘州市,城关区", "甘肃省,甘州市,七里河区", "甘肃省,甘州市,西固区", "甘肃省,甘州市,安宁区",
            "青海省,西宁市,城中区", "青海省,西宁市,城东区", "青海省,西宁市,城西区", "青海省,西宁市,城北区",
            "宁夏回族自治区,银川市,兴庆区", "宁夏回族自治区,银川市,金凤区", "宁夏回族自治区,银川市,西夏区",
            "新疆维吾尔自治区,乌鲁木齐市,水磨沟区", "新疆维吾尔自治区,乌鲁木齐市,天山区", "新疆维吾尔自治区,乌鲁木齐市,沙依巴克区", "新疆维吾尔自治区,乌鲁木齐市,新市区",
            "台湾省,台北市,中正区", "台湾省,台北市,大同区", "台湾省,台北市,中山区", "台湾省,台北市,松山区",
    };

    //随机城市
    public static String[] randomProvince() {
        String province = provinces[randomNumber(0, provinces.length)];
        return province.split(",");
    }

    //随机double 0-1
    public static double randomDouble() {
        return ran.nextDouble();
    }

    //随机重量(传入参数为整数位数)
    //整数范围包左不包右
    public static double randomLessWeight(int max) {
        // return ran.nextBoolean() ? "male" : "female";
        double index = ran.nextDouble(); // 0-1
        //9-1
        int flag = randomNumber(0, 10);
        if (flag <= 8) {
            return index;
        } else {
            int i = randomNumber(1, max);
            double var5 = Math.pow(10, i);
            return index * var5;
        }
    }


    //随机体积(传入参数为整数位数)
    //整数范围包左不包右
    public static double randomLessVolume(int max) {
        // return ran.nextBoolean() ? "male" : "female";
        double index = ran.nextDouble(); // 0-1
        //9-1
        int flag = randomNumber(0, 10);
        if (flag <= 8) {
            return index;
        } else {
            int i = randomNumber(1, max);
            double var5 = Math.pow(10, i);
            return index * var5;
        }
    }

    //随机组织
    public static String randomOrgId() {
        return orgIds[randomNumber(0, orgIds.length)];
    }


    //随机姓名
    public static String randomName() {
        int i = randomNumber(0, firstNames.length);
        int j = randomNumber(1, 3);
        String firstname = firstNames[i];
        String lastname = "";
        for (int index = 0; index < j; index++) {
            lastname = lastname + lastNames[randomNumber(0, lastNames.length)];
        }
        return firstname + lastname;
    }


    public static void longSleep(int min, int max) {
        int time = randomNumber(min, max);
        try {
            // Thread.sleep(time);
            TimeUnit.MILLISECONDS.sleep(time); // 代码易读","语义更清晰
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void longSleep(int sec) {
        try {
            // Thread.sleep(time);
            TimeUnit.MILLISECONDS.sleep(sec); // 代码易读","语义更清晰
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>生成步骤</p>
     * <ul>
     *     <li>对字典字符串拆分","得到一个字符数组</li>
     *     <li>产生某一个合适范围的索引值</li>
     *     <li>把字符数组变换成一个完整的字符串</li>
     * </ul>
     *
     * @param len 表示要生成的字符串长度
     * @return 返回一个随机内容的字符串
     */
    public static String randomString(int len) {
        // String[] split = data.split("");
        char[] chars = data.toCharArray(); // 'A'
        // 循环从split数组中随机获取一个字符","然后组装成一个完整字符串
        // 创建一个临时存放零散字符的数组
        char[] temp = new char[len]; // 用来构造一个完整的字符串的
        for (int j = 0; j < len; j++) {
            char c = chars[ran.nextInt(data.length())]; // [0, data.length())
            temp[j] = c;
        }
        return new String(temp); // 不使用字符串的拼串是因为会产生大量的内存零时垃圾
    }

    public static String randomString() {
        return randomString(5); // 重用上面的逻辑
    }

    /**
     * 产生从from到to之间的某一个数字","包左","不包右
     *
     * @param min 最小值
     * @param max 最大值
     */
    public static int randomNumber(int min, int max) { // 3 - 10
        if (max < min) {
            System.out.printf("%d 不能 比 %d 小\r\n", max, min);
        }
        return ran.nextInt(max - min) + min;
    }

    /**
     * 产生从 1 到to之间的某一个数字","包左","不包右
     *
     * @param max 最大值
     */
    public static int randomNumber(int max) {
        return randomNumber(1, max);
    }

    public static String randomGender() {
        // return ran.nextBoolean() ? "male" : "female";
        int index = ran.nextInt(2); // 0 1
        return genders[index];
        // return genders[ran.nextInt(2)];
    }

    //更大概率为否
    public static int randomMoreFalseFlag() {
        int i = randomNumber(0, 10);
        //0-8,9  9比1
        if (i <= 8) return 0;
        else return 1;
    }

    //随机金额(金额范围)
    public static double randomFee(int min, int max) {
        int i = randomNumber(min, max);
        int flag = randomNumber(0, 5);
        //4-1
        if (flag <= 3) {
            double decimal = ran.nextDouble();// 0-1
            return i + decimal;
        } else return i;
    }


    public static Date randomTime2022() {
        String dateStr = "2022-";
        //3-3-3-4-4-5-5-5-6-6-6-9
        int i = randomNumber(0, 59);
        String month;
        if (i < 9) {
            month = randomNumber(1, 4) + "-" + randomNumber(1, 27) + " " + randomNumber(0, 24) + ":" + randomNumber(0, 59) + ":" + randomNumber(0, 59);
        } else if (i < 16) {
            month = randomNumber(4, 6) + "-" + randomNumber(1, 27) + " " + randomNumber(0, 24) + ":" + randomNumber(0, 59) + ":" + randomNumber(0, 59);
        } else if (i < 31) {
            month = randomNumber(6, 9) + "-" + randomNumber(1, 27) + " " + randomNumber(0, 24) + ":" + randomNumber(0, 59) + ":" + randomNumber(0, 59);
        } else if (i < 49) {
            month = randomNumber(9, 12) + "-" + randomNumber(1, 27) + " " + randomNumber(0, 24) + ":" + randomNumber(0, 59) + ":" + randomNumber(0, 59);
        } else {
            month = 12 + "-" + randomNumber(1, 27) + " " + randomNumber(0, 24) + ":" + randomNumber(0, 59) + ":" + randomNumber(0, 59);
        }

        try {
            return sdf.parse(dateStr + month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date randomAddTime(Date date) {
        date.setTime(date.getTime() + (long) randomNumber(0, 168) * 60 * 60 * 1000);
        return date;
    }


}

