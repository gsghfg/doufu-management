package com.zf.product.doufu.test.pdf.t;

import com.zf.product.doufu.test.pdf.t.CreatePdf;
import com.zf.product.doufu.test.pdf.t.User;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        System.out.println("begin");
        
        String [] head = {"name","age","height","adress","sex","love"};
        
        List<User> list = new ArrayList<User>();
        User user1 = new User("李逍遥",21,185,"渔村","男","打架");
        User user2 = new User("林月如",18,177,"南武林","女","打架");
        
        list.add(user1);
        list.add(user2);
        
        
        String filePath = new CreatePdf().generatePDFs(head,list);
        System.out.println(filePath);
        System.out.println("end");
    }
}