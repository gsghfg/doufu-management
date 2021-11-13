package com.zf.product.doufu.test.pdf.t;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GenerateFold{
    
    /**
     * 判断路径是否存在 存在则用 不存在则创建
     * @param foldName  保存路径
     * @return          需要保存路径
     */
    public  String getFold(String foldName){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        
        String todayStr = format.format(Calendar.getInstance().getTime());
        
        String foldPath = foldName + File.separator + todayStr; 
        
        File file = new File(foldPath);
        
        if(!file.exists() && !file.isDirectory()){
            System.out.println("不存在");
            file.mkdirs();
        }else{
            System.out.println("存在");
        }
        return  foldPath;
    }
}