package cn.icloudit.itfree.controller.admin;


import cn.icloudit.itfree.entity.News;
import cn.icloudit.itfree.service.INewsService;
import cn.icloudit.itfree.utils.AjaxResult;
import cn.icloudit.itfree.utils.Layui;
import cn.icloudit.itfree.utils.UploadImgUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("news")
public class NewsController {

    @Autowired
    private INewsService newsService;
    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @RequestMapping("/queryAll")
    @ResponseBody
    public List queryall(){
        List<News> list = newsService.queryAll();
        return list;
    }

    @RequestMapping("/queryByPager")
    @ResponseBody
    public Object queryByPager(@RequestParam(value = "limit",defaultValue = "2") int limit, @RequestParam(value = "page",defaultValue = "1") int page, News news){
        PageHelper.startPage(page,limit);
        List<News> list = newsService.queryByPager(news);
        PageInfo<News> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<>();
        return Layui.loadJsonPage(pageInfo);
    }

    @RequestMapping("/upload_photo")
    @ResponseBody
    public Object upload_photo(MultipartFile file,HttpServletRequest request){
        return UploadImgUtils.uploadImg(file,request, UPLOAD_DIR,"news_photo");
    }


    @RequestMapping("/add")
    @ResponseBody
    public Object add(News news){
        news.setCreateTime(new Date());
        news.setViewNumber(0);
        news.setCommentNumber(0);
        try {
            newsService.save(news);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("添加失败！");
        }
        return AjaxResult.success("添加成功！");
    }

    @RequestMapping("/toUpdate")
    public Object toUpdate(Integer id, Model model){
        News news = newsService.queryById(id);
        model.addAttribute("news",news);
        return "admin/news/news-update";
    }

    @RequestMapping("/update")
    @ResponseBody
    public Object update(News news){
        try {
            newsService.update(news);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("修改失败！");
        }
        return AjaxResult.success("修改成功！");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Integer[] ids){
        String msgs = "";
        int okCount = 0;
        for (Integer id:ids) {
            try {
                newsService.delete(id);
                okCount++;
            }catch (Exception e){
                News news = newsService.queryById(id);
                msgs += "'"+ news.getTitle() +"'存在删除风险,删除失败！\n";
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
