package com.xingyao.smart.servlet;

import com.xingyao.smart.annotation.Action;
import com.xingyao.smart.annotation.Controller;
import com.xingyao.smart.annotation.Service;
import com.xingyao.smart.bean.*;
import com.xingyao.smart.config.ConfigConstant;
import com.xingyao.smart.enums.RequestMethod;
import com.xingyao.smart.helper.BeanHelper;
import com.xingyao.smart.helper.ClassHelper;
import com.xingyao.smart.helper.ConfigHelper;
import com.xingyao.smart.helper.IOCHelper;
import com.xingyao.smart.util.ArrayUtils;
import com.xingyao.smart.util.JSONUtils;
import com.xingyao.smart.util.ReflectionUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimeType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author ranger
 * @Date 2020/6/19 16:18
 **/

@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private Map<Request, Handler> handlerMapping;

    private Configuration cfg;


    @Override
    public void init() throws ServletException {
        // 完成bean的注入
        IOCHelper.populate();
        this.resolveHandler();

        // 注册静态资源servlet
        registerDefaultServlet();

        String viewPath = ConfigHelper.getConfig(ConfigConstant.VIEW_PATH, "views") + "/";
        cfg = new Configuration(Configuration.VERSION_2_3_26);
//        cfg.setServletContextForTemplateLoading(getServletContext(), "WEB-INF/");
        cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), viewPath);

    }

    private void registerDefaultServlet() {
        ServletContext servletContext = this.getServletContext();
        ServletRegistration servletRegistration = servletContext.getServletRegistration("default");
        servletRegistration.addMapping(ConfigHelper.getConfig(ConfigConstant.ASSET_PATH, "/static") + "/*");

    }

    private void resolveHandler() {
        logger.info("start resolving request handler");
        Set<Class<?>> controllerClass = ClassHelper.getClassWithAnnotation(Controller.class);
        if (CollectionUtils.isNotEmpty(controllerClass)) {
            for (Class controller : controllerClass) {
                Method[] methods = controller.getDeclaredMethods();
                if (ArrayUtils.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Action.class)) {
                            Action action = method.getAnnotation(Action.class);
                            String path = action.value();
                            RequestMethod requestMethod = action.method();
                            if (StringUtils.isBlank(path)) continue;
                            if (!path.startsWith("/")) {
                                path = "/" + path;
                            }

                            if (!path.endsWith("/")) {
                                path = path + "/";
                            }

                            Request request = new Request(requestMethod.name(), path);
                            Handler handler = new Handler(controller, method);
                            logger.info("add path [{}] to [{}.{}]", path, controller.getName(), method.getName());
                            if (this.handlerMapping == null) {
                                handlerMapping = new HashMap<>();
                            }
                            if (handlerMapping.containsKey(request)) {
                                logger.warn("duplicate path to url [{}]", path);
                            }
                            handlerMapping.put(request, handler);

                        }
                    }
                }
            }
        }

    }

    private Handler getHandler(String method, String path) {
        Request request = new Request(method, path);
        return this.handlerMapping.get(request);
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestMethod = req.getMethod();
        String path = req.getPathInfo();

        if (!path.endsWith("/")) {
            path = path + "/";
        }

        try {
            Handler handler = this.getHandler(requestMethod, path);
            if (handler != null) {
                logger.debug("hand request from [{}]", req.getRequestURL());
                Class controller = handler.getControllerClass();
                Object instance = BeanHelper.getBean(controller);
                // resolve request params
                Map<String, Object> paramMap = new HashMap<>();
                Enumeration<String> params = req.getHeaderNames();
                while (params.hasMoreElements()) {
                    String paramName = params.nextElement();
                    String paramValue = req.getParameter(paramName);
                    paramMap.put(paramName, paramValue);
                }

                Param param = new Param(paramMap);
                Method controllerMethod = handler.getActionMethod();
                // 判断方法是否含有Param类型的参数
                Object result = ReflectionUtil.invokeMethod(instance, controllerMethod);


                // 判断返回值是View类型，还是Data类型
                if (result instanceof View) {
                    // 返回模板解析后的
                    View view = (View) result;
                    String viewPath = view.getPath();

                    if (StringUtils.isNotBlank(viewPath)) {
                        Map<String, Object> data = new HashMap<String, Object>();
                        // 得到模板
                        try {
                            // 将模板和数据结合，并返回浏览器
                            Template template = cfg.getTemplate(viewPath);
                            template.process(data, resp.getWriter());
                        } catch (Exception e) {
                            logger.error("渲染模板失败", e);
                            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }


                    }

                } else if (result instanceof Data) {
                    Data data = (Data) result;
                    Object model = data.getModel();
                    if (model != null) {
                        resp.setContentType("application/json");
                        resp.setCharacterEncoding("UTF-8");
                        String json = JSONUtils.toJSON(model);
                        Writer writer = resp.getWriter();
                        writer.append(json);
                        writer.flush();
                        writer.close();

                    }
                }
            }
        }catch (Exception e){
            logger.error("处理请求异常", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }
}
