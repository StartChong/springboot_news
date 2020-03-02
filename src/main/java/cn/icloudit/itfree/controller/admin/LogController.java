package cn.icloudit.itfree.controller.admin;


import cn.icloudit.itfree.entity.Log;

import cn.icloudit.itfree.service.ILogService;
import cn.icloudit.itfree.utils.AjaxResult;
import cn.icloudit.itfree.utils.Layui;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/log")
public class LogController {

    @Autowired
    private ILogService logService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping("/queryByPager")
    @ResponseBody
    public Object queryByPager(@RequestParam(value = "limit",defaultValue = "2") int limit, @RequestParam(value = "page",defaultValue = "1") int page, Log log, Date start,Date end){
        PageHelper.startPage(page,limit);
        List<Log> list = logService.queryByPager(log,start,end);
        PageInfo<Log> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<>();
        return Layui.loadJsonPage(pageInfo);
    }

    @RequestMapping("/add")
    @ResponseBody
    public void add(String msg){
        try {
            Log log = new Log();
            log.setContent(msg);
            log.setCreateTime(new Date());
            logService.save(log);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Integer[] ids){
        String msgs = "";
        int okCount = 0;
        for (Integer id:ids) {
            try {
                logService.delete(id);
                okCount++;
            }catch (Exception e){
                msgs += "编号为：'"+ id +"'存在删除风险,删除失败！\n";
            }
        }
        if (okCount == 0){
            return AjaxResult.error("删除失败！");
        } else if (okCount < ids.length){
            return AjaxResult.success("部分信息删除成功！\n" + msgs);
        } else {
            return AjaxResult.success("删除成功！");
        }
    }
}
