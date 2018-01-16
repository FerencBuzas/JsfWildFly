package ulygroup.util;

import org.jboss.logging.Logger;
import ulygroup.model.User;

import javax.annotation.PostConstruct;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ApplicationScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@ApplicationScoped
public class FacesUtil implements Serializable {
    
    private static final long serialVersionUID = -178234L;

    private static final Logger LOGGER = Logger.getLogger(FacesUtil.class);

    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("postConstruct()");
    }

    public static String getRootErrorMessage(Exception e, String defaultMsg) {

        // Default to general error message that registration failed.
        String errorMessage = defaultMsg + " See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {

            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }

        // This is the root cause message
        return errorMessage;
    }

    public static String refreshPage() {
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
        LOGGER.debug("request princi=" + request.getUserPrincipal().getName()
                + " remU=" + request.getRemoteUser()
                + " m=" + request.getMethod()
                + " auth=" + request.getAuthType() 
                + "\n  contP=" + request.getContextPath()
                + " sltPath=" + request.getServletPath() 
                + " pathInfo=" + request.getPathInfo()
                + " rURI=" + request.getRequestURI()
                + " qS=" + request.getQueryString()
                + "\n  req.isInRole  Admin=" + request.isUserInRole("Admin")
                + "  User=" + request.isUserInRole("User")
        );
    }
}
