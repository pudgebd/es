package pers.pudgebd.java.elasticSearch.bigDataTest;

import pers.pudgebd.java.utils.HttpUtils;

import java.util.Random;

public class EsJThread extends Thread {

    private static String[] firstNames = {"Abigail", "Ada", "Adela", "Adelaide", "Afra", "Athena", "Belle", "Betsy", "Blanche"
            , "Breenda", "Camille", "Cathy", "Charlotte", "Cora", "Dawn", "Debby", "Doris", "Elizabeth", "Emily"
            , "Faithe", "Freda", "Geraldine", "Griselda", "Hannah", "Hermosa", "Ingrid", "Ivy", "Janet", "Jessie"
            , "Jocelyn", "Julie", "Kay", "Kristin", "Leila", "Linda", "Lydia", "Mandy", "Marina", "Mary", "Maxine"
            , "Michaelia", "Natividad", "Norma", "Odelette", "Olivia", "Pamela", "Penny", "Polly", "Rachel", "Renee"
            , "Rosemary", "Sabina", "Samantha", "Sarah", "Sheila", "Simona", "Spring", "Stephanie", "Suzanne", "Tammy"
            , "Tiffany", "Trista", "Una", "Vanessa", "Veromca", "Violet", "Wallis", "Winni", "Yetta", "Zenobia", "Zora"};

    private static String[] lastNames = {"Martin", "Bernard", "Dubois", "Thomas", "Robert", "Richard", "Petit", "Durand"
            , "Leroy", "Moreau", "Simon", "Laurent", "Lefebvre", "Michel", "Garcia", "David", "Bertrand"
            , "Roux", "Vincent", "Fournier", "Schmidt", "Schneider", "Fischer", "Meyer", "Weber", "Schulz"
            , "Wagner", "Becker", "Hoffmann", "Nagy", "Varga", "Papp", "Rossi", "Ferrari", "Greco", "Bruno"
            , "Gallo", "Conti", "Costa", "Giordano", "Mancini", "Rizzo", "Lombardi", "Moretti", "Hansen"
            , "Johansen", "Olsen", "Larsen", "Andersen", "Pedersen", "Nilsen", "Kristiansen", "Jensen", "Karlsen"};

    private static String[] jobs = {"防腐蚀工", "车工", "铣工", "磨工", "铸造工", "金属热处理工", "冷作钣金工", "涂装工", "装配钳工", "工具钳工"
            , "锅炉设备装配工", "电机装配工", "高低压电器装配工", "电线电缆制造工电子仪器仪表装配工", "电工仪器仪表装配工", "机修钳工"
            , "汽车修理工", "摩托车维修工", "精密仪器仪表修理工", "锅炉设备安装工", "变电设备安装工", "变配电室值班电工常用电机检修工"
            , "维修电工", "计算机维修工", "音响调音员", "手工木工", "精细木工", "贵金属首饰手制作工", "装饰美工", "土石方机械操作工"
            , "砌筑工", "混凝土工", "钢筋工", "架子工", "防水工", "装饰装修工", "电气设备安装工", "管工", "汽车驾驶员", "起重装卸机械操作工"
            , "化学检验工", "食品检验工", "评茶员纺织纤维检验工", "军人"};

    private static String[] esServers = {"231", "232", "233", "234", "235"};

    private static String UTF8 = "utf8";

    private static Random random = new Random();

    int begin;
    int end;


    public EsJThread(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void run() {
        //System.out.println(joinStr("start to thread", this.getName()));
        int count = begin;
        String urlStr = "http://192.168.2.225:9200/bd/employee/_bulk";
//        String urlStr = "http://127.0.0.1:9200/bd/employee/_bulk";
        StringBuilder sb = new StringBuilder();

        while (count <= end) {
            try {
                sb.append(joinStr("{\"index\":{\"_id\":", count, "}}\n"));
                sb.append(joinStr("{\"first_name\":\"", firstNames[random.nextInt(71)],
                        "\",\"last_name\":\"", lastNames[random.nextInt(54)], "\",\"age\":",
                        (random.nextInt(30) + 10), ",\"job\":\"", jobs[random.nextInt(44)], "\"}\n"));

                if (count % 150000 == 0) {
                    System.out.println(joinStr(this.getName(), "_", count));
                    HttpUtils.req(urlStr, HttpUtils.POST, sb.toString().getBytes(UTF8));
                    sb = new StringBuilder();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //String respStr = IOUtils.toString(input, UTF8);
            //System.out.println("req http successfully");
            count = count + 1;
        }
    }

    public static String joinStr(Object... objs) {
        if (objs == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : objs) {
            sb.append(obj);
        }
        return sb.toString();
    }

    public static byte[] getUtf8Bytes(String str) throws Exception {
        return str.getBytes(UTF8);
    }

}
