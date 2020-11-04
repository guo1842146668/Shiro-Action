package im.zhaojun.tool;

import im.zhaojun.common.util.DateUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collections;


public class ReadFile {
    public static List<String> getListFiles(Object obj) {
        List<String> list = new ArrayList<>();
        String path = obj.toString();
        File file = new File(path);
        File[] files = file.listFiles();
        for (File img:files) {
            list.add(img.getName());
        }
        return list;
    }




    public static boolean checkOverlap(List<String> list){
        Collections.sort(list);//排序ASC

        boolean flag = false;//是否重叠标识
        for(int i=0; i<list.size(); i++){
            if(i>0){
                //跳过第一个时间段不做判断
                String[] itime = list.get(i).split("-");
                for(int j=0; j<list.size(); j++){
                    //如果当前遍历的i开始时间小于j中某个时间段的结束时间那么则有重叠，反之没有重叠
                    //这里比较时需要排除i本身以及i之后的时间段，因为已经排序了所以只比较自己之前(不包括自己)的时间段
                    if(j==i || j>i){
                        continue;
                    }

                    String[] jtime = list.get(j).split("-");
                    //此处DateUtils.compare为日期比较(返回负数date1小、返回0两数相等、返回正整数date1大)
                    int compare = compareDate(
                            (DateUtils.getDate()+" "+itime[0]+":00"),
                            (DateUtils.getDate()+" "+jtime[1]+":00"),
                            "yyyy-MM-dd HH:mm:ss");
                    if(compare<0){
                        flag = true;
                        break;//只要存在一个重叠则可退出内循环
                    }
                }
            }

            //当标识已经认为重叠了则可退出外循环
            if(flag){
                break;
            }
        }

        return flag;
    }


    public static Integer compareDate(String DATE1, String DATE2, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
        }

        return null;
    }

    public static String specialStr(String str){
        Integer index=str.indexOf("%");
        Integer index1=str.indexOf("_");
        Integer index2=str.indexOf("\\");
        StringBuffer stringBuffer = new StringBuffer(str);
        if(index!=-1) {
            stringBuffer.insert(index, "\\");
        }
        if(index1!=-1) {
            stringBuffer.insert(index1, "\\");
        }
        if(index2!=-1) {
            stringBuffer.insert(index2, "\\");
        }
        return stringBuffer.toString();

    }

    public static String specialStrKeyword(String str){
        if(str==null||str==""){
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(str);
        int length=str.length();
        for (int i = 0; i <length; i++) {
            char chari=stringBuffer.charAt(i);
            if(i==0){
                if(chari=='%'||chari=='_'||chari=='\\'){
                    stringBuffer.insert(i, "\\");
                    i++;
                    length++;
                }
            }else{
                if(chari=='%'||chari=='_'||chari=='\\'){
                    stringBuffer.insert(i, "%\\");
                    i+=2;
                    length+=2;
                }else{
                    stringBuffer.insert(i, "%");
                    i++;
                    length++;
                }
            }
        }
        return stringBuffer.toString();

    }

}
