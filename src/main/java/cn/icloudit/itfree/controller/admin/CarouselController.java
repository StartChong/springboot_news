package cn.icloudit.itfree.controller.admin;


import cn.icloudit.itfree.entity.Carousel;
import cn.icloudit.itfree.service.ICarouselService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("carousel")
public class CarouselController {

    @Autowired
    private ICarouselService carouselService;
    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @RequestMapping("/queryAll")
    @ResponseBody
    public List queryall(){
        List<Carousel> list = carouselService.queryAll();
        return list;
    }

    @RequestMapping("/queryByPager")
    @ResponseBody
    public Object queryByPager(@RequestParam(value = "limit",defaultValue = "2") int limit, @RequestParam(value = "page",defaultValue = "1") int page, Carousel carousel){
        PageHelper.startPage(page,limit);
        List<Carousel> list = carouselService.queryByPager(carousel);
        PageInfo<Carousel> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<>();
        return Layui.loadJsonPage(pageInfo);
    }

    @RequestMapping("/upload_photo")
    @ResponseBody
    public Object upload_photo(MultipartFile file,HttpServletRequest request){
        return UploadImgUtils.uploadImg(file,request, UPLOAD_DIR,"carousel_photo");
    }


    @RequestMapping("/add")
    @ResponseBody
    public Object add(Carousel carousel){
        try {
            carouselService.save(carousel);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("添加失败！");
        }
        return AjaxResult.success("添加成功！");
    }

    @RequestMapping("/toUpdate")
    public Object toUpdate(Integer id, Model model){
        Carousel carousel = carouselService.queryById(id);
        model.addAttribute("carousel",carousel);
        return "admin/carousel/carousel-update";
    }

    @RequestMapping("/update")
    @ResponseBody
    public Object update(Carousel carousel){
        try {
            carouselService.update(carousel);
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
        Carousel carousel = new Carousel();
        for (Integer id:ids) {
            try {
                carousel.setId(id);
                carousel.setStatus(2);
                carouselService.update(carousel);
                okCount++;
            }catch (Exception e){
                msgs += "'"+ id +"'存在删除风险,删除失败！\n";
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
