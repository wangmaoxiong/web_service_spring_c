package com.lct.www.controller;

import com.lct.www.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2019/2/19 0019.
 * 用户控制器
 */
@Controller
public class UserController {

    //生成的代理类，也是接口和实现类的结构，已经交由了Spring容器管理，所以直接取值即可
    @Resource
    private UserService userService;

    /**
     * 根据用户id获取用户信息
     * 浏览器访问地址为：http://localhost/web_service_spring_c/getUserById.action
     *
     * @return 直接将内容打印到页面
     */
    @RequestMapping("getUserById.action")
    public void getUserById(HttpServletResponse response) {
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.write(userService.getUserById(1).toString());
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
