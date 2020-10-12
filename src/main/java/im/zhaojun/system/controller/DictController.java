package im.zhaojun.system.controller;

import im.zhaojun.common.util.ResultBean;
import im.zhaojun.system.service.DictService;
import im.zhaojun.tool.DictEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-09-01
 */
@Controller
@RequestMapping("business/dict")
public class DictController {
    @Resource
    private DictService dictService;


    @GetMapping("/getGuide")
    public ResultBean getGuide(){
        return ResultBean.success(dictService.getGuide(DictEnum.Guide));
    }

    @GetMapping("/getAgreement")
    public ResultBean getAgreement(){
        return ResultBean.success(dictService.getGuide(DictEnum.Agreement));
    }

    @GetMapping("/getCustomer")
    public ResultBean getCustomer(){
        return ResultBean.success(dictService.getGuide(DictEnum.Customer));
    }

    @GetMapping("/getType")
    public ResultBean getType(){
        return ResultBean.success(dictService.getGuide(DictEnum.Type));
    }

    @GetMapping("/getCustomerType")
    public ResultBean getCustomerType(){
        return ResultBean.success(dictService.getGuide(DictEnum.CustomerType));
    }
}
