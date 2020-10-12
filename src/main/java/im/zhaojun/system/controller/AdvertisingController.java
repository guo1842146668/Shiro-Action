package im.zhaojun.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import im.zhaojun.common.annotation.OperationLog;
import im.zhaojun.common.util.PageResultBean;
import im.zhaojun.common.util.ResultBean;
import im.zhaojun.system.service.DictService;
import im.zhaojun.tool.DictEnum;
import im.zhaojun.tool.ReadFile;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

@CrossOrigin
@Controller
@RequestMapping("/advertising")
public class AdvertisingController {
    @Resource
    private DictService dictService;

    @GetMapping("/index")
    public String index(){
        return  "Advertising/advertising-list";
    }

    @OperationLog("查询广告图片列表")
    @GetMapping("/list")
    @ResponseBody
    public PageResultBean<Map<String, Object>> listAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                       @RequestParam(value = "limit", defaultValue = "10") int limit)
    {
        List<Map<String,Object>> map = new ArrayList<>();
        PageHelper.startPage(page, limit);
        List<Map<String, Object>> guide = dictService.getGuide(DictEnum.CustomerType);
        String resource = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/")).getPath();
        resource = resource.substring(0,resource.lastIndexOf(guide.get(1).get("dictName").toString()));
        resource = resource.substring(5,resource.length());
        String path=resource + guide.get(0).get("dictName");
        List<String> listFiles = ReadFile.getListFiles(path);
        for (int i = 0; i < listFiles.size(); i++) {
            Map<String,Object> map1 = new HashMap<>();
            map1.put("name",listFiles.get(i));
            map.add(map1);
        }
          /*  Map<String,Object> map1 = new HashMap<>();
            map1.put("name","750d05366b67afd5f30ce876ac1a3987.jpg");
            map.add(map1);*/

        PageInfo<Map<String, Object>> userPageInfo = new PageInfo<>(map);
        return new PageResultBean<>(userPageInfo.getTotal(), userPageInfo.getList());
    }


    @OperationLog("显示图片详情")
    @PostMapping("/preview")
    public String preview(Model model,String name) {
        List<Map<String, Object>> guide = dictService.getGuide(DictEnum.CustomerType);
        model.addAttribute("path","http://localhost:8082//"+guide.get(0).get("dictName")+"//"+name);
        return "Advertising/advertising-preview";
    }

    @OperationLog("删除图片")
    @DeleteMapping("/delete")
    @ResponseBody
    public ResultBean delete(String name) {
        List<Map<String, Object>> guide = dictService.getGuide(DictEnum.CustomerType);
        String resource = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/")).getPath();
        resource = resource.substring(0,resource.lastIndexOf(guide.get(1).get("dictName").toString()));
        resource = resource.substring(5,resource.length());
        String path=resource + guide.get(0).get("dictName");
        File file = new File(path+name);
        if(file.exists()){
            file.delete();
            return  ResultBean.success();
        }
        return  ResultBean.error("路径错误");
    }

    @PostMapping(value="/uploadImage")
    @ResponseBody
    public ResultBean uploadImage(@RequestParam(value="file") MultipartFile file){
        List<Map<String, Object>> guide = dictService.getGuide(DictEnum.CustomerType);
        String resource = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/")).getPath();
        resource = resource.substring(0,resource.lastIndexOf(guide.get(1).get("dictName").toString()));
        resource = resource.substring(5,resource.length());
        String path=resource + guide.get(0).get("dictName");
        File dest = new File(path);
        if(dest.exists()) {
            dest.mkdir();
        }
        String originalFilename = file.getOriginalFilename();
        if(originalFilename == null){
            return  ResultBean.error("请上传正确的图片");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
        String targetUploadPath = dest + File.separator + filename;
        try {
            FileUtils.writeByteArrayToFile(new File(targetUploadPath), file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return ResultBean.error("储存错误");
        }
        return  ResultBean.success();
    }
}
