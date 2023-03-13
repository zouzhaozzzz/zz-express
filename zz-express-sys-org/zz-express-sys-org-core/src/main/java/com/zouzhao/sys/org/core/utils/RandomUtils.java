package com.zouzhao.sys.org.core.utils;

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

    // private static final String[] genders = new String[]{"male, female"};
    private static final String[] genders = {"male", "female"}; // 利用了编译器的优化机制(语法糖)进行简化","和上面这一行等效

    private static final String[] firstNames = {"王", "李", "张", "刘", "陈", "杨", "黄", "赵", "吴", "周", "徐", "孙", "马", "朱", "胡", "郭", "何", "高", "林", "罗", "郑", "梁", "谢", "宋", "唐", "许", "韩", "冯", "邓", "曹", " 彭", "曾", "肖", "田", "董", "袁", "潘", "于", "蒋", "蔡", " 余", "杜", "叶", "程", "苏", "魏", "吕", "丁", "任", "沈", " 姚", "卢", "姜", "崔", "钟", "谭", "陆", "汪", "范", "金", "石", "廖", "贾", "夏", "韦", "傅", "方", "白", "邹", "孟", " 熊", "秦", "邱", "江", "尹", "薛", "闫", "段", "雷", "侯"};
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

    //随机组织
    public static String randomOrgId() {
        return orgIds[randomNumber(0, orgIds.length)];
    }


    //随机姓名
    public static String randomName() {
        int i = randomNumber(0, firstNames.length);
        int j = randomNumber(1, 2);
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
}
