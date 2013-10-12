// -----------------------------------------------
// ViewExpiredExceptionHandlerFactory.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.seguridad;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Servicio de factoría para el contro de la excepción
 * de la expiración de la vista(ViewExpiredException)
 * <p>
 * Posteado por: <b>Ed Burns</b><br>
 * Fecha: <b>Septiembre 3, 2009</b><br>
 * Enlace: <b>{@linkplain http://weblogs.java.net/blog/edburns/archive/2009/09/03/dealing-gracefully-viewexpiredexception-jsf2}</b> 
 */
public class ViewExpiredExceptionExceptionHandlerFactory extends ExceptionHandlerFactory {
 
    private ExceptionHandlerFactory parent;
 
    public ViewExpiredExceptionExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }
 
    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler result = parent.getExceptionHandler();
        result = new ViewExpiredExceptionExceptionHandler(result);
 
        return result;
    }
 
 
}
