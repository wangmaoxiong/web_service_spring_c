package com.lct.www.interceptors;

import com.sun.org.apache.xml.internal.utils.DOMHelper;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2019/2/18 0018.
 * 自定义 webService 拦截器——————用于提供请求的账号与密码
 */
public class WsUserInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

    private static final Logger logger = Logger.getGlobal();//日志记录器
    /**
     * webService 需要传递的账号与密码
     */
    private String name;
    private String password;

    /**
     * 使用构造器传入参数，构造器中必须使用 super 设置拦截器发生的时刻/阶段
     * org.apache.cxf.phase.Phase 中提供了大量的常量表示拦截器发生的时刻
     *
     * @param name
     * @param password
     */
    public WsUserInterceptor(String name, String password) {
        super(Phase.PRE_PROTOCOL);//协议前进行拦截
        this.name = name;
        this.password = password;
    }

    /**
     * 默认情况下，客户端给服务端请求的 soap 消息如下：
     * <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><ns2:getStudentByIdResponse xmlns:ns2="http://web_service.lct.com/"><getStudentById><birthday>2019-02-18T16:19:12.102+08:00</birthday><punishStatus>5</punishStatus><sID>11</sID><sName>0a90959c-22ee-497b-b60d-0550b1f2adf7</sName></getStudentById></ns2:getStudentByIdResponse></soap:Body></soap:Envelope>
     * 其中的 <body></body>部分为请求的主体，现在为 Envelope 设置头信息 <head></head>，将账户密码信息放在 <head></head>中，<head>与 body 同级
     * <head></head>中的内容设置为账户与密码元素，如：
     * <head><userInfo><name>admin</name><password>123456</password></userInfo></head>，其中的 userInfo、name、password 等元素名称必须和服务端约定的一致，
     * 因为服务端还需要根据这些名称解析出元素的值
     *
     * @param message ：在客户端请求服务端前，先进入此方法，然后将账户密码信息添加到 soap 消息头中
     * @throws Fault
     */
    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        /**
         * com.sun.org.apache.xml.internal.utils.DOMHelper
         * org.w3c.dom.Document
         * org.w3c.dom.Element
         */
        Document document = DOMHelper.createDocument();//创建 w3c 的文档对象
        Element userInfoEle = document.createElement("userInfo");//创建标签元素
        Element nameEle = document.createElement("name");//创建标签元素
        Element passwordEle = document.createElement("password");//创建标签元素

        nameEle.setTextContent(this.name);//设置标签元素内容
        passwordEle.setTextContent(this.password);//设置标签元素内容
        userInfoEle.appendChild(nameEle);//添加子元素
        userInfoEle.appendChild(passwordEle);//添加子元素

        /**
         * org.apache.cxf.headers.Header
         * 最后将创建好的 soap 头信息添加到 SoapMessage 中
         */
        List<Header> headerList = message.getHeaders();
        /**QName构造器中的值与后面的 userInfoEle 元素的标签名保持一致*/
        headerList.add(new Header(new QName("userInfo"), userInfoEle));
        logger.info("客户端 webServic 自定义出拦截器执行完毕....");
    }
}
