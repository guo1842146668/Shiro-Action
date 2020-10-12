package im.zhaojun.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@Controller
@RequestMapping("/operation")
public class OperationController {

    @GetMapping("index")
    public String index(){
        return  "Operation/operation-list";
    }
}
