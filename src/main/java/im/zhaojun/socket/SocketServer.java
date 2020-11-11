package im.zhaojun.socket;

import im.zhaojun.system.controller.equipmentRunnable;
import im.zhaojun.system.mapper.EquipmentMapper;
import im.zhaojun.system.service.AlertService;
import im.zhaojun.system.service.EquipmentService;
import im.zhaojun.system.service.ScheduledService;
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
	@Resource
	private ScheduledService scheduledService;
	@Resource
	private EquipmentMapper equipmentMapper;

	@Override
	public void run(ApplicationArguments args){
		equipmentService.updateElectricStatus();
		new Thread(new StartServerSocket9000(equipmentService,alertService,scheduledService)).start();
		new Thread(new equipmentRunnable(equipmentMapper)).start();
	}

}
