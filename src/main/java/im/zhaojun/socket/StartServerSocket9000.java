package im.zhaojun.socket;

import im.zhaojun.system.service.AlertService;
import im.zhaojun.system.service.EquipmentService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class StartServerSocket9000 implements Runnable {

    private ServerSocket server;

    private EquipmentService equipmentService;

    private AlertService alertService;

    public StartServerSocket9000(EquipmentService equipmentService,AlertService alertService) {
        this.equipmentService=equipmentService;
        this.alertService=alertService;
    }

    @Override
    public void run() {
        try {
            // 服务端在9801端口监听客户端请求的TCP连接
            server = new ServerSocket(9000);
            System.out.println("启动成功，端口为：" + 9000);
            Socket socket = null;
            boolean f = true;
            while (f) {
                // 等待客户端的连接，如果没有获取连接
                socket = server.accept();
                String address = socket.getInetAddress().getHostAddress();//客户端ip
                if (HairUtil.map.get(address) == null) {
                    new Thread(new ServerThread9000(socket,equipmentService,alertService)).start();
                }
            }
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
