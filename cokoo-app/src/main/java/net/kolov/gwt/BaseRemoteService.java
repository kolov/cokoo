package net.kolov.gwt;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseRemoteService extends RemoteServiceServlet implements ServletContextAware {

    private ServletContext servletContext;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        doPost(request, response);
        return null; // response handled by GWT RPC over XmlHttpRequest
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }
}
