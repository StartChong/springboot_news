package cn.icloudit.itfree.utils;

import cn.icloudit.itfree.entity.TempletJson;
import com.github.pagehelper.PageInfo;

import java.util.List;

public class Layui {
    /**
     * 分页方法使用
     * @param pageInfo
     * @return
     */
    public static TempletJson loadJsonPage(PageInfo pageInfo){
        TempletJson templetJson = new TempletJson();
        if(pageInfo!=null){

        }
        try{
            List list= pageInfo.getList();
            int i=(int)pageInfo.getTotal();
            templetJson.setCount(i);
            templetJson.setCode(0);
            templetJson.setMsg("");
            templetJson.setData(list);
        }catch (Exception e){
            templetJson.setCode(1);
        }
        return templetJson;
    }
}
