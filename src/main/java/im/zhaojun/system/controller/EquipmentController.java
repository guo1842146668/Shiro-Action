package im.zhaojun.system.controller;

import com.github.pagehelper.PageInfo;
import im.zhaojun.common.annotation.OperationLog;
import im.zhaojun.common.util.PageResultBean;
import im.zhaojun.common.util.ResultBean;
import im.zhaojun.socket.ServerThread9000;
import im.zhaojun.system.model.Equipment;
import im.zhaojun.system.model.Scheduled;
import im.zhaojun.system.model.User;
import im.zhaojun.system.service.DictService;
import im.zhaojun.system.service.EquipmentService;
import im.zhaojun.system.service.ScheduledService;
import im.zhaojun.system.service.UserService;
import im.zhaojun.tool.DictEnum;
import im.zhaojun.tool.ExprotExcel;
import im.zhaojun.tool.ReadFile;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/equipment")
public class EquipmentController {
    @Resource
    private EquipmentService equipmentService;
    @Resource
    private UserService userService;
    @Resource
    private ScheduledService scheduledService;
    @Resource
    private DictService dictService;


    @OperationLog("设备信息列表页面挑转")
    @GetMapping("/index")
    public String index(){
        return  "equipment/equipment-list";
    }

    @OperationLog("查询设备信息列表")
    @GetMapping("/list")
    @ResponseBody
    public PageResultBean<Map<String, Object>> listAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                       Equipment equipment)
    {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user.getType() == null || user.getType() != 1){
            if(user.getDeptId() == 0){
                equipment.setEquipmentID(user.getUserId());
            }else{
                equipment.setUserID(user.getUserId().toString());
            }
        }
        if(equipment != null){
            if (!"".equals(equipment.getEquipmentNO()) && equipment.getEquipmentNO() != null) {
                equipment.setEquipmentNO(ReadFile.specialStr(equipment.getEquipmentNO().trim()));// 排除%等通配符
                equipment.setEquipmentNO(ReadFile.specialStrKeyword(equipment.getEquipmentNO()));
            }
            if (!"".equals(equipment.getEquipmentAddress()) && equipment.getEquipmentAddress() != null) {
                equipment.setEquipmentAddress(ReadFile.specialStr(equipment.getEquipmentAddress().trim()));// 排除%等通配符
                equipment.setEquipmentAddress(ReadFile.specialStrKeyword(equipment.getEquipmentAddress()));
            }
        }

        List<Map<String, Object>> maps = equipmentService.listAll(equipment, page, limit);
        PageInfo<Map<String, Object>> userPageInfo = new PageInfo<>(maps);
        return new PageResultBean<>(userPageInfo.getTotal(), userPageInfo.getList());
    }

    @OperationLog("添加设备界面跳转")
    @GetMapping("add")
    public String add(Model model) {
        model.addAttribute("Equipment", null);
        return "equipment/equipment-add";
    }

    @OperationLog("修改设备界面跳转")
    @GetMapping("/{equipmentID}")
    public String update(@PathVariable("equipmentID") Integer equipmentID, Model model) {
        model.addAttribute("Equipment", equipmentService.selectOne(equipmentID));
        return "equipment/equipment-edit";
    }

    @OperationLog("添加设备")
    @PostMapping("/add")
    @ResponseBody
    public ResultBean add(Equipment equipment){
        if(equipment.getEquipmentNO() == null || equipment.getEquipmentNO().equals("")){
            ResultBean.error("设备编号不能为空！");
        }
        if(equipmentService.selectByEquipmentNO(equipment.getEquipmentNO()) != null){
            ResultBean.error("该设备已经添加过！");
        }
        equipmentService.saveEquipment(equipment);
        return  ResultBean.success();
    }

    @OperationLog("修改设备")
    @PutMapping("/update")
    @ResponseBody
    public ResultBean update(Equipment equipment){
        equipmentService.updateEquipment(equipment);
        return  ResultBean.success();
    }

    @OperationLog("删除设备")
    @DeleteMapping("/{equipmentID}")
    @ResponseBody
    public ResultBean delete(@PathVariable("equipmentID") Integer equipmentID) {
        equipmentService.deleteEquipment(equipmentID);
        return ResultBean.success();
    }

    @OperationLog("开启")
    @GetMapping("/open")
    @ResponseBody
    public String open(String equipmentNO) throws Exception {
        try {
            ServerThread9000.accused(ServerThread9000.Division(equipmentNO),"1");
            ServerThread9000.read(ServerThread9000.Division(equipmentNO));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "error";
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = equipmentService.selectByEquipmentNO(equipmentNO);
        if(map.get("runningState").equals(1)){
            return "success";
        }else{
            return "error";
        }
    }

    @OperationLog("关闭")
    @GetMapping("/down")
    @ResponseBody
    public String down(String equipmentNO) throws Exception {
        try {
            ServerThread9000.accused(ServerThread9000.Division(equipmentNO),"0");
            ServerThread9000.read(ServerThread9000.Division(equipmentNO));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "error";
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = equipmentService.selectByEquipmentNO(equipmentNO);
        if(map.get("runningState").equals(0)){
            return "success";
        }else{
            return "error";
        }
        //return "success";
    }

    @GetMapping("/getListOne")
    @ResponseBody
    public ResultBean getListOne() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        User users = new User();
        users.setUserId(user.getUserId());
        List<Map<String, Object>> selectOne = new ArrayList<>();
        if(user.getType() == null || user.getType() != 1){
            selectOne = userService.getSelectOne(users);
        }else{
            selectOne = userService.selectAdminOne();
        }
        return ResultBean.success(selectOne);
    }

    @GetMapping("/getOne")
    @ResponseBody
    public ResultBean getOne(String equipmentNO) {
        return ResultBean.success(equipmentService.selectByEquipmentNO(equipmentNO));
    }

    @OperationLog("控制")
    @GetMapping("/kongzhi")
    public String kongzhi(Integer equipmentID, Model model) {
        model.addAttribute("Equipment", equipmentService.selectOne(equipmentID));
        return "equipment/equipment-kongzhi";
    }

    @OperationLog("定时")
    @GetMapping("/timing")
    public String timing(String equipmentNO, Model model) {
        List<Scheduled> byUserID = scheduledService.getByCronId(equipmentNO);
        if (byUserID.size() < 5) {
            Scheduled scheduled = new Scheduled();
            scheduled.setCronStatus(-1);
            for (int i = 0; i < 5 - byUserID.size(); i++) {
                scheduled.setCronName(equipmentNO + (i + 1 + byUserID.size()));
                scheduled.setCronStartTime("00:00");
                scheduled.setCronEndTime("00:00");
                scheduled.setEquipmentNO(equipmentNO);
                scheduledService.saveScheduled(scheduled);
            }
        }
        List<Map<String, Object>> map = equipmentService.selectScheduled(equipmentNO);
        model.addAttribute("Scheduled", equipmentService.selectScheduled(equipmentNO));
        return "equipment/equipment-timing";
    }

    @GetMapping("/read")
    @ResponseBody
    public String read(String equipmentNO) throws Exception {
        try {
            ServerThread9000.read(ServerThread9000.Division(equipmentNO));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }


    @GetMapping("/uploadExcel")
    @ResponseBody
    public ResultBean uploadExcel(Equipment equipment, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, ClassNotFoundException {
        //Map<String, Object> headerList = getNameAndComment();
        Map<String, Object> headerList = new LinkedHashMap<>();
        headerList.put("设备ID","equipmentNO");
        headerList.put("设备类型","equipmentTypeName");
        headerList.put("所属用户","name");
        headerList.put("安装地址","equipmentAddress");
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user.getType() == null || user.getType() != 1){
            if(user.getDeptId() == 0){
                equipment.setEquipmentID(user.getUserId());
            }else{
                equipment.setUserID(user.getUserId().toString());
            }

        }
        List<Map<String, Object>> maps = equipmentService.uploadExcel(equipment);
        ExprotExcel.exportAll(maps,headerList,request,response);
        return ResultBean.success();
    }




    @PostMapping("/importExcel")
    @ResponseBody
    public ResultBean importExcel(HttpServletRequest request, MultipartFile file){
        List<Map<String, Object>> guide = dictService.getGuide(DictEnum.Type);
        Map<Integer, Object> errors = new HashMap<Integer, Object>();
        try {
            String originName = file.getOriginalFilename();
            if (!originName.endsWith(".xlsx") && !originName.endsWith(".xls")) {
                return ResultBean.error("文件格式错误");
            }
            InputStream is = file.getInputStream();

            if (originName.endsWith(".xlsx")) {
                XSSFWorkbook xwb = new XSSFWorkbook(is);
                XSSFSheet sheet = xwb.getSheetAt(0);
                int rows = sheet.getLastRowNum();

                if (errors != null && errors.size() > 0) {
                    return ResultBean.error("导入出错");
                }

                for (int rowNum = 2; rowNum <= rows; rowNum++) {
                    Equipment equipment = new Equipment();
                    XSSFRow hssfRow = sheet.getRow(rowNum);
                    if (hssfRow != null) {
                        XSSFCell equipmentNO = hssfRow.getCell(0);
                        XSSFCell equipmentType= hssfRow.getCell(1);
                        XSSFCell userID= hssfRow.getCell(2);
                        XSSFCell equipmentAddress = hssfRow.getCell(3);
                        if(equipmentNO == null&&equipmentType==null&&userID==null&&equipmentAddress==null){
                            continue;
                        }
                        if(equipmentNO == null || equipmentNO.equals("")){
                            return ResultBean.error("第"+(rowNum+1)+"行，设备ID出错");
                        }else{
                            equipment.setEquipmentNO(equipmentNO.toString());
                        }
                        if(equipmentType == null || equipmentType.equals("")){
                            equipment.setEquipmentType(null);

                            //return ResultBean.error("第"+(rowNum+1)+"行，设备类型出错");
                        }else{
                            for (int i = 0; i < guide.size(); i++) {
                                if(guide.get(i).get("dictName").equals(equipmentType.toString())){
                                    equipment.setEquipmentType(Integer.parseInt(guide.get(i).get("dictID").toString()));
                                }
                            }
                            if(equipment.getEquipmentType() == null || equipment.getEquipmentType().equals("null")){
                                return ResultBean.error("第"+(rowNum+1)+"行，设备类型出错");
                            }
                        }
                        if(userID == null || userID.equals("")){
                            equipment.setUserID(null);
                            //return ResultBean.error("第"+(rowNum+1)+"行，所属用户出错");
                        }else{
                           try{
                               User byName = userService.getByName(userID.toString());
                               if(byName == null){
                                   return ResultBean.error("第"+(rowNum+1)+"行，所属用户出错");
                               }else{
                                   equipment.setUserID(byName.getUserId().toString());
                               }
                           }catch (Exception e) {
                               e.printStackTrace();
                               return ResultBean.error("第"+(rowNum+1)+"行，所属用户出错");
                           }
                        }

                        if(equipmentAddress == null || equipmentAddress.equals("")){
                           equipment.setEquipmentAddress(null);
                        }else{
                            equipment.setEquipmentAddress(equipmentAddress.toString());
                        }

                        Map<String, Object> map = equipmentService.selectByEquipmentNO(equipmentNO.toString());
                        if(map != null){
                            equipment.setEquipmentID(Integer.parseInt(map.get("equipmentID").toString()));
                            equipmentService.updateEquipment(equipment);
                        }else{
                            equipmentService.saveEquipment(equipment);
                        }

                    }

                }
            } else if (originName.endsWith(".xls")) {
                HSSFWorkbook xwb = new HSSFWorkbook(is);
                HSSFSheet sheet = xwb.getSheetAt(0);
                int rows = sheet.getLastRowNum();

                if (errors != null && errors.size() > 0) {
                    return ResultBean.error("导入出错");
                }

                for (int rowNum = 2; rowNum <= rows; rowNum++) {
                    Equipment equipment = new Equipment();
                    HSSFRow hssfRow = sheet.getRow(rowNum);
                    if (hssfRow != null) {
                        HSSFCell equipmentNO = hssfRow.getCell(0);
                        HSSFCell equipmentType= hssfRow.getCell(1);
                        HSSFCell userID= hssfRow.getCell(2);
                        HSSFCell equipmentAddress = hssfRow.getCell(3);

                        if(equipmentNO == null || equipmentNO.equals("")){
                            return ResultBean.error("第"+(rowNum+1)+"行，设备ID出错");
                        }else{
                            equipment.setEquipmentNO(equipmentNO.toString());
                        }
                        if(equipmentType == null || equipmentType.equals("")){
                            return ResultBean.error("第"+(rowNum+1)+"行，设备类型出错");
                        }else{
                            for (int i = 0; i < guide.size(); i++) {
                                if(guide.get(i).get("dictName").equals(equipmentType)){
                                    equipment.setEquipmentType(Integer.parseInt(guide.get(i).get("dictID").toString()));
                                }
                            }
                            if(equipment.getEquipmentType() == null || equipment.getEquipmentType().equals("null")){
                                return ResultBean.error("第"+(rowNum+1)+"行，设备类型出错");
                            }
                        }
                        if(userID == null || userID.equals("")){
                            return ResultBean.error("第"+(rowNum+1)+"行，所属用户出错");
                        }else{
                            User byName = userService.getByName(userID.toString());
                            if(byName == null){
                                return ResultBean.error("第"+(rowNum+1)+"行，所属用户出错");
                            }else{
                                equipment.setUserID(byName.getUserId().toString());
                            }
                        }

                        if(equipmentAddress == null || equipmentAddress.equals("")){
                            equipment.setEquipmentAddress(null);
                        }else{
                            equipment.setEquipmentAddress(equipmentAddress.toString());
                        }

                        Map<String, Object> map = equipmentService.selectByEquipmentNO(equipmentNO.toString());
                        if(map != null){
                            equipment.setEquipmentID(Integer.parseInt(map.get("equipmentID").toString()));
                            equipmentService.updateEquipment(equipment);
                        }else{
                            equipmentService.saveEquipment(equipment);
                        }
                    }
                }
            }

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultBean.success();
    }


/*
    @Value("${spring.datasource.driver-class-name}")
    private  String DRIVER;
    @Value("${spring.datasource.url}")
    private  String URL;
    @Value("${spring.datasource.username}")
    private  String USERNAME;
    @Value("${spring.datasource.password}")
    private  String PASSWORD;

    private static final String SQL = "SELECT * FROM ";// 数据库操作


    *//**
     * 获取数据库连接
     *
     * @return
     *//*
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {

        }
        return conn;
    }

    *//**
     * 关闭数据库连接
     * @param conn
     *//*
    public  void closeConnection(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {

            }
        }
    }

    *//**
     * 获取数据库下的所有表名
     *//*
    public  List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[] { "TABLE" });
            while(rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {

        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {

            }
        }
        return tableNames;
    }

    *//**
     * 获取表中所有字段名称
     * @param tableName 表名
     * @return
     *//*
    public  List<String> getColumnNames(String tableName) {
        List<String> columnNames = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
            }
        } catch (SQLException e) {

        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {

                }
            }
        }
        return columnNames;
    }

    *//**
     * 获取表中所有字段类型
     * @param tableName
     * @return
     *//*
    public  List<String> getColumnTypes(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnTypes.add(rsmd.getColumnTypeName(i + 1));
            }
        } catch (SQLException e) {

        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {

                }
            }
        }
        return columnTypes;
    }

    *//**
     * 获取表中字段的所有注释
     * @param tableName
     * @return
     *//*
    public  List<String> getColumnComments(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        List<String> columnComments = new ArrayList<>();//列名注释集合
        ResultSet rs = null;
        try {
            pStemt = conn.prepareStatement(tableSql);
            rs = pStemt.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                columnComments.add(rs.getString("Comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    closeConnection(conn);
                } catch (SQLException e) {

                }
            }
        }
        return columnComments;
    }*/

   /* public Map<String,Object> getNameAndComment() throws ClassNotFoundException {
        Class.forName(DRIVER);
        List<String> name = getColumnNames("equipment");
        List<String> comment = getColumnComments("equipment");
        Map<String,Object> map = new LinkedHashMap<>();
        for (int i = 0; i < name.size(); i++) {
            map.put(comment.get(i),name.get(i));
        }
        return map;
    }*/
}
