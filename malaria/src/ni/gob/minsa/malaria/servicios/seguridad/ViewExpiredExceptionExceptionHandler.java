package ni.gob.minsa.malaria.servicios.seguridad;

import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 * Servicio de control de la excepción de expiración de la vista  
 * (ViewExpiredException)
 * <p>
 * Posteado por: <b>Ed Burns</b><br>
 * Fecha: <b>Septiembre 3, 2009</b><br>
 * Enlace: <b>{@linkplain http://weblogs.java.net/blog/edburns/archive/2009/09/03/dealing-gracefully-viewexpiredexception-jsf2}</b> 
 */
public class ViewExpiredExceptionExceptionHandler extends ExceptionHandlerWrapper {
 
    private ExceptionHandler wrapped;
 
    public ViewExpiredExceptionExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }
 
    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }
 
    @Override
    public void handle() throws FacesException {
        for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();
            if (t instanceof ViewExpiredException) {
                ViewExpiredException vee = (ViewExpiredException) t;
                FacesContext fc = FacesContext.getCurrentInstance();
                Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
                NavigationHandler nav =
                        fc.getApplication().getNavigationHandler();
                try {
                    // Se coloca el identificador del ViewId de la página que
                	// que generó la excepción en el request scope en caso de
                	// ser requerido a futuro
                    requestMap.put("currentViewId", vee.getViewId());
 
                    nav.handleNavigation(fc, null, "viewExpired");
                    fc.renderResponse();
 
                } finally {
                    i.remove();
                }
            } 
        }
        // En este punto, la cola de excepciones no contendrá ninguna
        // evento de excepción ViewExpiredEvents, por lo que se deja 
        // a que lo gestione el padre
        getWrapped().handle();
 
    }
}

