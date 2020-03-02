package cn.icloudit.itfree.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {

    @RequestMapping("/page/admin/{page}")
    public String toAdminPage(@PathVariable("page") String page){
        if (page.indexOf("-") > 0){
            return "admin/" + page.split("-")[0] + "/" + page;
        }
        return "admin/" + page;
    }

    @RequestMapping("/page/home/{page}")
    public String toHomePage(@PathVariable("page") String page){
        if (page.indexOf("-") > 0){
            return "home/" + page.split("-")[0] + "/" + page;
        }
        return "home/" + page;
    }

    @RequestMapping
    public String index(){
        return "redirect:/home/index/";
    }

}
