package im.zhaojun.socket;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class HairUtil {
	
	public static Map<String, SocketSpace> map = new HashMap<String, SocketSpace>();

	/**
	 * 通过IP地址发送消息
	 * @param data			数据
	 * @param address		IP地址
	 * @return
	 */
	public static boolean send(String data,String address) throws Exception{
		try{
			boolean f = false;
			SocketSpace socketSpace = map.get(address);
			System.out.println("设备"+address+"发送的数据:"+data);
			if(socketSpace != null){
				byte[] by = toByteArray(data);
				PrintStream out = socketSpace.getOut();
				out.write(by);
				f = true;
			}else{
				throw new NullPointerException();
				//System.out.println("无法发送，ip为"+address+"的连接对象为null");
			}
			return f;
		}catch (Exception e){
			throw new Exception();
		}
	}

	/**
	 * 通过IP地址关闭流
	 * @param address	IP地址
	 */
	public static void socketClose(String address) {
  		SocketSpace socketSpace = HairUtil.map.get(address);
  		if(socketSpace != null) {
  			
  			try {
  				if(socketSpace.getOut() != null) {
  					socketSpace.getOut().close();//关闭输出流
  				}
			} catch (Exception e) {}
  			try {
  				if(socketSpace.getIs() != null) {
  					socketSpace.getIs().close();//关闭输入流
  				}
  			}catch (Exception e) {}
  			try {
  				if(socketSpace.getSocket() != null) {
  					socketSpace.getSocket().close();//关闭socket
  				}
			} catch (Exception e) {}
  			HairUtil.map.remove(address);//删除map的socket
  			System.out.println("链接已断开");
  		}
  	}

	
    /**
     * 将 byte 数组转化为十六进制字符串
     *
     * @param bytes byte[] 数组
     * @param begin 起始位置
     * @param end 结束位置
     * @return byte 数组的十六进制字符串表示
     */
    public static String bytesToHex(byte[] bytes, int begin, int end) {
        StringBuilder hexBuilder = new StringBuilder(2 * (end - begin));
        for (int i = begin; i < end; i++) {
            hexBuilder.append(Character.forDigit((bytes[i] & 0xF0) >> 4, 16)); // 转化高四位
            hexBuilder.append(Character.forDigit((bytes[i] & 0x0F), 16)); // 转化低四位
            hexBuilder.append(' '); // 加一个空格将每个字节分隔开
        }
        return hexBuilder.toString().toUpperCase();
    }
    
  //把16进制的字符串装换成16进制形式的byte数组 
    public static byte[] toByteArray(String string) {
    	String[] s = string.split(" ");
        byte[] by = new byte[s.length];
        for (int i = 0; i < s.length; i++) {
        	by[i] = (byte) Integer.parseInt(s[i],16);
        }
        return by;
    }
    
}
