// -----------------------------------------------
// 
// -----------------------------------------------
package ni.gob.minsa.malaria.interfaz.seguridad;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import ni.gob.minsa.aplicacion.Seguridad;
import ni.gob.minsa.ciportal.dto.InfoSesion;

/**
 * Control de autenticación del usuario y permisos a las opciones de menú
 * según los roles o perfiles asociados.  Utilizado en el preRenderView
 * del main-template.xhtml como listener
 * <p>
 * @author Marlon Arróliga Téllez
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 29/01/2011
 * @since jdk1.6.0_21
 */
@ManagedBean
@RequestScoped
public class LoginController implements Serializable {

	private static final long serialVersionUID = 1L;

	public InfoSesion getUsuarioActual() {

    	FacesContext oFacesContext = FacesContext.getCurrentInstance();
        return (InfoSesion)oFacesContext.getExternalContext().getSessionMap().get("usuarioActual");
    }

    public boolean isUsuarioAutenticado() {
		FacesContext oFacesContext = FacesContext.getCurrentInstance();
    	return oFacesContext.getExternalContext().getSessionMap().containsKey("usuarioActual");
    }

    public boolean isUsuarioAutorizado(long pUsuarioId, String pViewId){
    	
    	InfoSesion oInfoSesion=this.getUsuarioActual();
    	
		return Seguridad.esUsuarioAutorizado(pUsuarioId, pViewId,oInfoSesion.getSistemaSesion());

    }

    public void redireccionarALoginSiNoAutenticado(ComponentSystemEvent cse) {
    	if (!isUsuarioAutenticado()) {
    		FacesContext oFacesContext = FacesContext.getCurrentInstance();
    		oFacesContext.getApplication().getNavigationHandler(). handleNavigation(oFacesContext, null,"/redirect.xhtml");
    	}
    }

    public void redireccionarALoginSiNoAutorizado(ComponentSystemEvent cse) {
    	FacesContext oFacesContext = FacesContext.getCurrentInstance();
    	String viewId = oFacesContext.getViewRoot().getViewId();
    	
    	if (!isUsuarioAutenticado()) {
    		oFacesContext.getApplication().getNavigationHandler().handleNavigation(oFacesContext, null,"/redirect.xhtml");
    	} else {
    		if (!viewId.equals("/inicio.xhtml")) {
    			if (!getUsuarioActual().isAdministrador()) {
    				if(!isUsuarioAutorizado(getUsuarioActual().getUsuarioId(), viewId)){
    					oFacesContext.getApplication().getNavigationHandler(). handleNavigation(oFacesContext, null,"/redirect.xhtml");
    				}
    			}
    		}
    	}
    }

    //public String logOut() {
    //	String result = "/login.xhtml?faces-redirect=true";
    //	setUsuarioActual(null);
    //	getFacesContext().getExternalContext().invalidateSession();
    //	return result;
    //}

}
