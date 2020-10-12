package im.zhaojun.system.controller;

import com.github.pagehelper.PageInfo;
import im.zhaojun.common.util.PageResultBean;
import im.zhaojun.system.model.Version;
import im.zhaojun.system.service.VersionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/version")
public class VersionController {
    @Resource
    private VersionService versionService;

    @GetMapping("/index")
    public String index(){
        return "version/version-list";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageResultBean<Version> getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "limit", defaultValue = "10") int limit){
        List<Version> all = versionService.getAll(page, limit);
        PageInfo<Version> pageInfo = new PageInfo<>(all);
        return new PageResultBean<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @GetMapping("/see")
    public String see(Integer version, Model model){
        model.addAttribute("version",versionService.getOne(version));
        return "version/version-see";
    }


}
