package im.zhaojun.socket;

import im.zhaojun.system.service.AlertService;
import im.zhaojun.system.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 启动main自动执行下面的类
 * 
 * @author Administrator
 *
 */
@Component
@Order(value = 1)
public class SocketServer implements ApplicationRunner {
	@Resource
	private EquipmentService equipmentService;
	@Resource
	private AlertService alertService;

	@Override
	public void run(ApplicationArguments args){
		new Thread(new StartServerSocket9000(equipmentService,alertService)).start();
	}

}
