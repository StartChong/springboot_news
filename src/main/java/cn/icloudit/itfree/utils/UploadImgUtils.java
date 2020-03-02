package cn.icloudit.itfree.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/****
 * 图片上传
 */
public class UploadImgUtils {

    /***************
     *
     * @param file 文件
     * @param request HttpServletRequest
     * @param upload_dir 图片保存的文件夹更目录地址
     * @param fileInsetName 图片保存的文件夹根目录名
     * @return json
     */
    public static Map<String, String> uploadImg(MultipartFile file, HttpServletRequest request, String upload_dir, String fileInsetName){
        fileInsetName = fileInsetName.toLowerCase();
        Map<String, String> map = new HashMap<>();
        if (file == null){
            map.put("code","500");
            map.put("msg","请选择要上传的文件!");
            return map;
        }
        if (file.getSize() > 1024*1024*1024){
            map.put("code","500");
            map.put("msg","文件大小不能超过10M!");
            return map;
        }
        // 获取文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1,file.getOriginalFilename().length());
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())){
            map.put("code","500");
            map.put("msg","请选择jpg,jpeg,gif,png格式的图片!");
            return map;
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        String savePath = upload_dir + fileInsetName +"/"+ year +"/"+ month +"/"+ day +"/";
        File savePathFile = new File(savePath);
        if (!savePathFile.exists()){
            // 如不存在该目录，则创建该目录
            savePathFile.mkdirs();
        }
        String filename = UUID.randomUUID().toString().replaceAll("-","") + "." + suffix;
        try {
            // 将文件保存至指定目录
           file.transferTo(new File(savePath + filename));
        } catch (IOException e) {
            map.put("code","500");
            map.put("msg","保存文件异常！");
            e.printStackTrace();
            return map;
        }
        map.put("code","200");
        map.put("msg","图片已成功上传！");
        map.put("filePath","/image/"+ fileInsetName +"/"+ year +"/"+ month +"/"+ day +"/" + filename);
        return map;
    }
}
