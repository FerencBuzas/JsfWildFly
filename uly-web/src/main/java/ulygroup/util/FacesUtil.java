package ulygroup.util;

import org.jboss.logging.Logger;
import ulygroup.model.User;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.security.Principal;

@Named("facesUtil")
@SessionScoped
public class FacesUtil implements Serializable {
    
    private static final long serialVersionUID = -178234L;

    private static final Logger LOGGER = Logger.getLogger(FacesUtil.class);

    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("postConstruct()");
    }

    public String refreshPage() {
        LOGGER.debug("refreshPage()");

        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        ViewHandler handler = context.getApplication().getViewHandler();
        UIViewRoot root = handler.createView(context, viewId);
        root.setViewId(viewId);
        context.setViewRoot(root);
        return null;
    }

    // ============== session =======================
    
    public HttpSession getSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        return (HttpSession) externalContext.getSession(true);
    }

    public User getSessionUser(String attributeName) {
        return (User)getSession().getAttribute(attributeName);
    }

    public void setSessionUser(String attributeName, User user) {
        getSession().setAttribute(attributeName, user);
    }



    // ====================== HTTP ============================
    public void logRequest(HttpServletRequest request) {
        Principal pr = request.getUserPrincipal();
        LOGGER.debug("request princi=" + pr + "  pr class=" + pr.getClass().getName()
                + "\n  remU=" + request.getRemoteUser()
                + " m=" + request.getMethod()
                + " auth=" + request.getAuthType() 
                + "\n  contP=" + request.getContextPath()
                + " sltPath=" + request.getServletPath() 
                + " pathInfo=" + request.getPathInfo()
                + " rURI=" + request.getRequestURI()
                + " qS=" + request.getQueryString()
                + "\n  req.isUserInRole  Admin=" + request.isUserInRole("Admin")
                + "  User=" + request.isUserInRole("User")
                + "  SuperUser=" + request.isUserInRole("SuperUser")
                + "  Administrator=" + request.isUserInRole("Administrator")
                + "  Manager=" + request.isUserInRole("Manager")
                + "  **=" + request.isUserInRole("**")  // see ..oracle./com/javaee/7/..HttpServletRequest
        );
    }

    public void logRequest() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        logRequest(request);
    }

    public void logResponse(HttpServletResponse response) {
        LOGGER.debug("response " + response);
    }
}
