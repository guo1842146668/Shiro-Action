package im.zhaojun.system.controller;

import cn.hutool.json.JSONUtil;
import im.zhaojun.system.mapper.EquipmentMapper;
import im.zhaojun.system.model.Equipment;
import lombok.SneakyThrows;

import java.util.List;

public class equipmentRunnable implements Runnable {

    private EquipmentMapper equipmentMapper;


    public equipmentRunnable(EquipmentMapper equipmentMapper) {
        try {
            this.equipmentMapper = equipmentMapper;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SneakyThrows
    @Override
    public void run() {
        while (true){
            List<Equipment> maps = equipmentMapper.selectEquipmentAll();
            WebSocketServer.sendInfo(JSONUtil.toJsonStr(JSONUtil.parse(maps)),"30");
            Thread.sleep(2000);
        }
    }
}
