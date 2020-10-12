package im.zhaojun.socket;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.sql.visitor.functions.Char;
import im.zhaojun.system.model.Alert;
import im.zhaojun.system.model.Equipment;
import im.zhaojun.system.service.AlertService;
import im.zhaojun.system.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.*;


/**
 * 该类为多线程类，用于服务端
 */

public class ServerThread9000 implements Runnable {
    @Resource
    private  EquipmentService equipmentService;
    @Resource
    private AlertService alertService;

    private static Logger logger = LoggerFactory.getLogger(ServerThread9000.class);

    private SocketSpace socketSpace = new SocketSpace();
    private String time = DateUtil.now();
    private String address;//ip地址
    private String ICCID;//设备id
    private String data;//接收的数据
    private String[] dataArrays;//接受收的数据
    private boolean flag = true;

    public static Map<String, String> ICCID_IP = new HashMap<>();

    public static List<String> lists = new LinkedList<>();

    public ServerThread9000(Socket client,EquipmentService equipmentService,AlertService alertService) {
        try {
            this.socketSpace.setSocket(client);
            this.socketSpace.setIs(client.getInputStream());
            this.socketSpace.setOut(new PrintStream(client.getOutputStream()));
            this.equipmentService=equipmentService;
            this.alertService=alertService;
            address = client.getInetAddress().getHostAddress();//客户端ip
            this.mapPut();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static Integer num=1;
    @Override
    public void run() {
        byte[] by = new byte[1024];
        try {
            while (flag) {
                int blen = socketSpace.getIs().read(by);// 接收从客户端发送过来的数据
                System.out.println("接收字节数为 ：" + blen);
                if (blen <= 0) {//客戶端主動中短
                    Equipment equipment = new Equipment();
                    equipment.setEquipmentNO(HexadecimalToDecimal(ICCID));
                    equipment.setElectricStatus(-1);
                    equipmentService.updateByEquipmentNo(equipment);
                    System.out.println("客戶端主動中短");
                    HairUtil.socketClose(address);
                    flag = false;
                } else {
                    time = DateUtil.now();//接收到消息的時間
                    if(num == 1){
                        Timer();
                        num ++;
                    }
                    /*this.Timer();*/
                    data = new String(by);
                    data = HairUtil.bytesToHex(by, 0, blen);
                    System.out.println(address + "于" + time + "接收的数据为:" + data);
                    dataArrays = data.split(" ");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i <= 9; i++) {
                        sb.append(dataArrays[i] + " ");
                    }
                    ICCID = sb.toString();
                    //存入ip地址
                    ICCID_IP.put(ICCID, address);
                    System.out.println("ip地址map = "+ ICCID_IP);
                    Map<String, Object> map = equipmentService.selectByEquipmentNO(HexadecimalToDecimal(ICCID));
                    if(map == null){
                        Equipment equipment = new Equipment();
                        equipment.setEquipmentName("新设备");
                        equipment.setEquipmentNO(HexadecimalToDecimal(ICCID));
                        equipmentService.saveEquipment(equipment);
                    }
                    if (dataArrays[10].equals("02")) {
                        //readTheParsing(dataArrays);
                        //解析
                        Map<String, Object> map1 = readTheParsing(dataArrays);
                        Equipment equipment = new Equipment();
                        equipment.setEquipmentNO(HexadecimalToDecimal(ICCID));
                        equipment.setElectricCurrentIA(Double.parseDouble(map1.get("currentA").toString()));
                        equipment.setElectricCurrentIB(Double.parseDouble(map1.get("currentB").toString()));
                        equipment.setElectricCurrentIC(Double.parseDouble(map1.get("currentC").toString()));
                        equipment.setRunningState(Integer.parseInt(map1.get("runningState").toString()));
                        equipment.setOperationMode(Integer.parseInt(map1.get("OperationMode").toString()));
                        equipment.setFaultStatus(Integer.parseInt(map1.get("FaultStatus").toString()));
                        equipment.setElectricStatus(Integer.parseInt(map1.get("electricStatus").toString()));
                        if(map1.get("FaultStatus").toString().equals("1")){
                            if(map.get("FaultStatus").toString().equals("0")){
                                Alert alert = new Alert();
                                alert.setAlertTime(new Date());
                                alert.setEquipmentNO(HexadecimalToDecimal(ICCID));
                                alertService.saveAlert(alert);
                            }
                        }
                        equipmentService.updateByEquipmentNo(equipment);
                    }else if(dataArrays[10].equals("01")){
                        //握手
                        shakeHands(dataArrays);
                        /*Thread.sleep(2000);
                        //读取
                        read(ICCID);*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Equipment equipment = new Equipment();
            equipment.setEquipmentNO(HexadecimalToDecimal(ICCID));
            equipment.setElectricStatus(-1);
            equipmentService.updateByEquipmentNo(equipment);
            System.out.println("执行出错");
            HairUtil.socketClose(address);
            flag = false;
        }
        System.out.println(address + "的线程已走完!");
    }

    private void mapPut() {
        if (HairUtil.map.get(address) == null) {
            HairUtil.map.put(address, socketSpace);
            System.out.println("当前连接数：" + HairUtil.map.size());
            System.out.println(address);
            System.out.println(socketSpace);
        }
    }

    private static String HexadecimalToDecimal(String ID){
        StringBuilder hexBuilder = new StringBuilder();
        String[] s = ID.split(" ");
        for (int i = 0; i < s.length; i++) {
            if(Integer.parseInt(s[i],16)<10){
                hexBuilder.append("0"+Integer.parseInt(s[i],16));
            }else{
                hexBuilder.append(Integer.parseInt(s[i],16));
            }
        }
        return hexBuilder.toString();
    }


    public static String Division(String equipmentNO){
        StringBuilder hexBuilder = new StringBuilder();
        for (int i = 0; i < equipmentNO.length(); i++) {

        }
        int m=equipmentNO.length()/2;
        if(m*2<equipmentNO.length()){
            m++;
        }
        int j=0;
        for(int i=0;i<equipmentNO.length();i++){
            if(i%2==1){//每隔bai两个
                hexBuilder.append(""+equipmentNO.charAt(i));
            }else{
                hexBuilder.append(" "+equipmentNO.charAt(i));
                j++;
            }
        }
        return DecimalToHexadecimal(StrUtil.trimStart(hexBuilder.toString()));
    }


    private static String DecimalToHexadecimal(String equipmentNO){
        StringBuilder hexBuilder = new StringBuilder();
        String[] s = equipmentNO.split(" ");
        for (int i = 0; i < s.length; i++) {
            if(Integer.parseInt(s[i])<16){
                hexBuilder.append(" 0"+Integer.toHexString(Integer.parseInt(s[i])));
            }else{
                hexBuilder.append(" "+Integer.toHexString(Integer.parseInt(s[i])));
            }

        }
        return StrUtil.trimStart(hexBuilder.toString().toUpperCase());
    }

    /*private void Timer() {
        if (flag) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (flag) {
                        long timeLength = DateUtil.between(DateUtil.parse(time), DateUtil.parse(DateUtil.now()), DateUnit.MINUTE);
                        if (timeLength <= 30) {
                            Equipment equipment = new Equipment();
                            equipment.setEquipmentNO(ICCID);
                            equipment.setElectricStatus(1);
                            equipmentService.updateByEquipmentNo(equipment);
                            System.out.println(address + "没断开");
                        } else {
                            System.out.println(address + "监测到60s内没收到心跳,视为断开");
                            Equipment equipment = new Equipment();
                            equipment.setEquipmentNO(ICCID);
                            equipment.setElectricStatus(-1);
                            equipmentService.updateByEquipmentNo(equipment);
                            HairUtil.socketClose(address);
                            flag = false;
                        }
                    }
                }
            }, 60000);
        }
    }*/

    /**
     * 定时器，监听心跳包
     */
    private void Timer() {
        if (flag) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
//                    if(lists.size() == 0){
                        for (String key: ICCID_IP.keySet()) {
                            //lists.add(key);
                            read(key);
                        }
//                    }
                  /*  read(lists.get(0));
                    lists.remove(0);*/
                    //System.out.println("发送读取命令");
                    //没过三秒给设备发送一次读取命令
                }
            }, 3000,3000);
        }
    }

    /**
     * 握手下发
     */
    /*public  void shakeHands(String[] dataArrays) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 9; i++) {
            sb.append(dataArrays[i] + " ");
        }
        String iccid = sb.toString();
        //功能吗
        sb.append("01 ");
        //反控位
        sb.append("00 ");
        //小时
        String hour=new BigInteger(DateUtil.hour(new Date(), true) + "", 10).toString(16);
        sb.append(hour.length()==1?"0"+hour:hour);
        sb.append(" ");
        //分
        String minute=new BigInteger(DateUtil.minute(new Date()) + "", 10).toString(16);
        sb.append(minute.length()==1?"0"+minute:minute);
        sb.append(" ");
        //秒
        String second=new BigInteger(DateUtil.second(new Date()) + "", 10).toString(16);
        sb.append(second.length()==1?"0"+second:second);
        sb.append(" ");
        sb.append("01 01");
        HairUtil.send(CRC16.getCrc(sb.toString()), ICCID_IP.get(iccid.substring(0,iccid.length()-1)));
    }
*/
    public static void shakeHands(String[] dataArrays) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 9; i++) {
            sb.append(dataArrays[i] + " ");
        }
        String iccid = sb.toString();
        ServerThread9000.ICCID_IP.put(sb.toString().substring(0, sb.toString().length() - 1), ICCID_IP.get(iccid));
        //功能吗
        sb.append("01 ");
        //反控位
        sb.append("00 ");
        //小时
        String hour = new BigInteger(DateUtil.hour(new Date(), true) + "", 10).toString(16);
        sb.append(hour.length() == 1 ? "0" + hour : hour);
        sb.append(" ");
        //分
        String minute = new BigInteger(DateUtil.minute(new Date()) + "", 10).toString(16);
        sb.append(minute.length() == 1 ? "0" + minute : minute);
        sb.append(" ");
        //秒
        String second = new BigInteger(DateUtil.second(new Date()) + "", 10).toString(16);
        sb.append(second.length() == 1 ? "0" + second : second);
        sb.append(" ");
        sb.append("01 01");
        HairUtil.send(CRC16.getCrc(sb.toString()), ICCID_IP.get(iccid));
    }

    /**
     * 读取命令
     */
/*
    public  void read(String iccid) {
        StringBuilder sb = new StringBuilder();
        sb.append(iccid);
        //功能码
        sb.append(" 02");
        //反控位
        sb.append(" 00 ");
        //小时
        String hour=new BigInteger(DateUtil.hour(new Date(), true) + "", 10).toString(16);
        sb.append(hour.length()==1?"0"+hour:hour);
        sb.append(" ");
        //分
        String minute=new BigInteger(DateUtil.minute(new Date()) + "", 10).toString(16);
        sb.append(minute.length()==1?"0"+minute:minute);
        sb.append(" ");
        //秒
        String second=new BigInteger(DateUtil.second(new Date()) + "", 10).toString(16);
        sb.append(second.length()==1?"0"+second:second);
        sb.append(" ");
        sb.append("01 01");
        HairUtil.send(CRC16.getCrc(sb.toString()), ICCID_IP.get(iccid.substring(0,iccid.length()-1));
    }
*/

    public static void read(String iccid) {
        StringBuilder sb = new StringBuilder();
        sb.append(iccid);
        //功能码
        sb.append("02");
        //反控位
        sb.append(" 00 ");
        //小时
        String hour = new BigInteger(DateUtil.hour(new Date(), true) + "", 10).toString(16);
        sb.append(hour.length() == 1 ? "0" + hour : hour);
        sb.append(" ");
        //分
        String minute = new BigInteger(DateUtil.minute(new Date()) + "", 10).toString(16);
        sb.append(minute.length() == 1 ? "0" + minute : minute);
        sb.append(" ");
        //秒
        String second = new BigInteger(DateUtil.second(new Date()) + "", 10).toString(16);
        sb.append(second.length() == 1 ? "0" + second : second);
        sb.append(" ");
        sb.append("01 01");
        HairUtil.send(CRC16.getCrc(sb.toString()), ICCID_IP.get(iccid));
    }


    /**
     * 反控
     * @param type 1开启，0关闭
     */
    public static void accused(String iccid, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append(iccid);
        //功能码
        sb.append(" 03");
        //反控位
        if (type.equals("1")) {
            sb.append(" 01 ");
        } else {
            sb.append(" 00 ");
        }
        //小时
        String hour = new BigInteger(DateUtil.hour(new Date(), true) + "", 10).toString(16);
        sb.append(hour.length() == 1 ? "0" + hour : hour);
        sb.append(" ");
        //分
        String minute = new BigInteger(DateUtil.minute(new Date()) + "", 10).toString(16);
        sb.append(minute.length() == 1 ? "0" + minute : minute);
        sb.append(" ");
        //秒
        String second = new BigInteger(DateUtil.second(new Date()) + "", 10).toString(16);
        sb.append(second.length() == 1 ? "0" + second : second);
        sb.append(" ");
        sb.append("01 01");
        HairUtil.send(CRC16.getCrc(sb.toString()), ICCID_IP.get(iccid+" "));
    }

    /**
     * 设置启停时间
     *
     * @param iccid   ICCID
     * @param hour1   时
     * @param minute1 分
     * @param hour2   时
     * @param minute2 分
     */
    public static void setTheStartAndStopTime(String iccid, String hour1, String minute1, String hour2, String minute2,String time) {
        StringBuilder sb = new StringBuilder();
        sb.append(iccid);
        //功能码
        sb.append(" 04");
        //反控位
        sb.append(" 00 ");
        //小时
        String hour = new BigInteger(DateUtil.hour(new Date(), true) + "", 10).toString(16);
        sb.append(hour.length() == 1 ? "0" + hour : hour);
        sb.append(" ");
        //分
        String minute = new BigInteger(DateUtil.minute(new Date()) + "", 10).toString(16);
        sb.append(minute.length() == 1 ? "0" + minute : minute);
        sb.append(" ");
        //秒
        String second = new BigInteger(DateUtil.second(new Date()) + "", 10).toString(16);
        sb.append(second.length() == 1 ? "0" + second : second);
        sb.append(" 01 01 ");
        time=new BigInteger(time, 10).toString(16);
        sb.append(time.length() == 1 ? "0" + time : time);
        sb.append(" ");
        hour=new BigInteger(hour1, 10).toString(16);
        sb.append(hour.length() == 1 ? "0" + hour : hour);
        sb.append(" ");
        hour=new BigInteger(minute1, 10).toString(16);
        sb.append(hour.length() == 1 ? "0" + hour : hour);
        sb.append(" ");
        hour=new BigInteger(hour2, 10).toString(16);
        sb.append(hour.length() == 1 ? "0" + hour : hour);
        sb.append(" ");
        hour=new BigInteger(minute2, 10).toString(16);
        sb.append(hour.length() == 1 ? "0" + hour : hour);
        System.out.println("时控 = "+sb.toString());
        System.out.println("时控 = "+iccid);
        HairUtil.send(CRC16.getCrc(sb.toString()), ICCID_IP.get(iccid+" "));
    }


    /**
     * 读取解析
     */
    public static Map<String,Object> readTheParsing(String[] dataArrays){
        Map<String,Object> map = new HashMap<>();
        //电流A
        String currentA=new BigInteger(dataArrays[11]+dataArrays[12],16).toString(10);
        map.put("currentA",Double.parseDouble(currentA)/10);
        //电流B
        String currentB=new BigInteger(dataArrays[13]+dataArrays[14],16).toString(10);
        map.put("currentB",Double.parseDouble(currentB)/10);
        //电流C
        String currentC=new BigInteger(dataArrays[15]+dataArrays[16],16).toString(10);
        map.put("currentC",Double.parseDouble(currentC)/10);
        String status=fillZero(new BigInteger(dataArrays[17],16).toString(2));
        //状态
        if(status.substring(3,4).equals("0")){
            //1为运行，0为停止
            map.put("runningState",0);
        }else{
            map.put("runningState",1);
        }
        if(status.substring(2,3).equals("0")){
            //0为手动，1为自动
            map.put("OperationMode",0);
        }else{
            map.put("OperationMode",1);
        }
        if(status.substring(1,2).equals("0")){
            //1为故障，0为正常
            map.put("FaultStatus",0);
        }else{
            map.put("FaultStatus",1);
        }
        map.put("electricStatus",1);
        return map;
    }

    /**
     * 每两位切一次
     * @param str
     * @return
     */
    public static String cutting(String str) {
        String regex = "(.{2})";
        str = str.replaceAll(regex, "$1 ");
        str = str.substring(0, str.length() - 1);
        System.out.println(str);
        return str;
    }

    /**
     * 补0
     * @return
     */
    public static String fillZero(String str){
        StringBuilder sb=new StringBuilder();
        for(int i=str.length();i<8;i++){
            sb.append("0");
        }
        sb.append(str);
        return sb.toString();
    }

    public static void main(String[] args) {
        shakeHands("59 56 04 45 0A 13 46 61 24 3B 01 90 83".split(" "));
        //read("59 56 04 45 0A 13 46 61 24 3B ");
        //accused("595604450A134661243B","1");
        //setTheStartAndStopTime("595604450A134661243B", "10", "10", "11", "20");
        //readTheParsing("59 56 04 45 0A 13 46 61 24 3B 02 00 00 00 00 00 00 0E 4E 2B".split(" "));
    }


}