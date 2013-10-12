// -----------------------------------------------
// MainBean.java
// -----------------------------------------------
package ni.gob.minsa.malaria.interfaz.seguridad;

import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.ciportal.servicios.PortalService;
import ni.gob.minsa.componente.MenuModelo;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;


/**
 * Servicio para la capa de presentación del contenedor
 * de todas las páginas de la aplicación.  Este bean gestiona en diferentes
 * variables de sesión el username del usuario autenticado, así como
 * el identificador de dicho usuario, el menú generado especìficamente
 * para dicho usuario.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 20/09/2010
 * @since jdk1.6.0_21
 */
@ManagedBean
@RequestScoped
public class MainBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long usuarioId;
	private String urlLogin;
	private String sesionId;
	private String redirectToUrlLogin;
	@SuppressWarnings("unused")
	private Map<String,String> sistemasAutorizados;

	public MainBean(){
		
		this.setRedirectToUrlLogin("");

		// obtiene la cookie  
		HttpServletRequest httpServletRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Cookie[] cookies = httpServletRequest.getCookies();
		this.sesionId=null;
		if (cookies != null) {
			for(int i=0; i<cookies.length; i++){
				if (cookies[i].getName().equalsIgnoreCase("BDSESSIONID")){
					this.sesionId = cookies[i].getValue();
				}
			}
		}

		InitialContext ctx;
		
		try {
			
			ctx = new InitialContext();
			PortalService portalService = (PortalService)ctx.lookup("ejb/Portal");

			// obtiene de los parámetros de configuración la
			// dirección URL de la página de autenticación

			this.urlLogin=portalService.obtenerUrlLogin();
			if (this.urlLogin==null) {
				this.redirectToUrlLogin="alert('No se encontró un parámetro de la aplicación.\\nNotifique al administrador del sistema'); top.document.location.replace('http://www.minsa.gob.ni');";
				return;
			}

			if (this.sesionId==null) {
				setRedirectToUrlLogin(this.urlLogin);
				return;
			}

			InfoResultado oInfoResultado = portalService.obtenerInfoSesion(this.sesionId);
			if (oInfoResultado.isOk()) {

				InfoSesion oInfoSesion=(InfoSesion)oInfoResultado.getObjeto();
				if (!oInfoSesion.getSistemaSesion().equals(Utilidades.CODIGO_SISTEMA)) {
					this.redirectToUrlLogin="alert('Se ha presentado un error en la aplicación: El sistema no se corresponde con el autorizado desde el portal de aplicaciones.\\nNotifique al administrador del sistema'); top.document.location.replace('"+this.urlLogin+"');";
		    		return;
		    	}

		    	setUsuarioActual(oInfoSesion);
		    	MenuModel oMenu=new DefaultMenuModel();
		    	oMenu=MenuModelo.obtenerMenuModelo(oInfoSesion.getUsuarioId(), oInfoSesion.getSistemaSesion());
		    	if (oMenu==null) {
					this.redirectToUrlLogin="alert('Se ha presentado un error en la aplicación: No fue posible generar el menú con los parámetros de la sesión.\\nNotifique al administrador del sistema'); top.document.location.replace('"+this.urlLogin+"');";
					return;
		    	}
		    	setMenuActual(oMenu); 
		    	
		    	setSistemasAutorizados(portalService.listarSistemasAutorizados(oInfoSesion.getUsername())); 
		    	
		    	
			} else {
	    		setRedirectToUrlLogin(this.urlLogin);
	    		return;
			}
			
		} catch (NamingException e) {
			e.printStackTrace();
			this.redirectToUrlLogin="alert('Se ha presentado un error en la aplicación: No fue posible acceder al componente de servicios del portal de aplicaciones.\\nNotifique al administrador del sistema'); top.document.location.replace('http://www.minsa.gob.ni');";
			return;
		}	
	
	}
	
	public void setUsuarioActual(InfoSesion pInfoSesion) {
        
		FacesContext oFacesContext = FacesContext.getCurrentInstance();
        oFacesContext.getExternalContext().getSessionMap().remove("usuarioActual");
        if (null!=pInfoSesion) {
        	oFacesContext.getExternalContext().getSessionMap().put("usuarioActual",pInfoSesion);
        	this.usuarioId=pInfoSesion.getUsuarioId();
        }

	}

	public void setMenuActual(MenuModel menuActual) {

		FacesContext oFacesContext = FacesContext.getCurrentInstance();
        oFacesContext.getExternalContext().getSessionMap().remove("menuActual");
        if (null!=menuActual) {
        	oFacesContext.getExternalContext().getSessionMap().put("menuActual",menuActual);
        }
		
	}

	public void setSistemasAutorizados(Map<String,String> sistemasAutorizados) {

		sistemasAutorizados.remove(Utilidades.CODIGO_SISTEMA);
		
		FacesContext oFacesContext = FacesContext.getCurrentInstance();
        oFacesContext.getExternalContext().getSessionMap().remove("sistemasAutorizados");
        if (null!=sistemasAutorizados) {
        	oFacesContext.getExternalContext().getSessionMap().put("sistemasAutorizados",sistemasAutorizados);
        }
		
	}

	public void borrarSesiones(ActionEvent pEvento) {
		
		FacesContext oFacesContext = FacesContext.getCurrentInstance();
		oFacesContext.getExternalContext().getSessionMap().remove("usuarioActual");
		oFacesContext.getExternalContext().getSessionMap().remove("usuarioActualId");
		oFacesContext.getExternalContext().getSessionMap().remove("menuActual");
		oFacesContext.getExternalContext().getSessionMap().remove("urlLogin");
		setRedirectToUrlLogin(this.urlLogin);

	}
	
	public void finalizarSesion(ActionEvent iEvento)
	{
	   borrarSesiones(null);
	}

	/**
	 * @param usuarioId usuarioId 
	 */
	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	/**
	 * @return usuarioId
	 */
	public Long getUsuarioId() {
		return usuarioId;
	}

	/**
	 * @param urlLogin urlLogin 
	 */
	public void setUrlLogin(String urlLogin) {
		FacesContext oFacesContext = FacesContext.getCurrentInstance();
        oFacesContext.getExternalContext().getSessionMap().remove("urlLogin");
        if (null!=urlLogin) {
        	oFacesContext.getExternalContext().getSessionMap().put("urlLogin",urlLogin);
        }
		this.urlLogin = urlLogin;
	}

	/**
	 * @return urlLogin
	 */
	public String getUrlLogin() {
		return urlLogin;
	}

	/**
	 * @param sesionId sesionId 
	 */
	public void setSesionId(String sesionId) {
		this.sesionId = sesionId;
	}

	/**
	 * @return sesionId
	 */
	public String getSesionId() {
		return sesionId;
	}

	/**
	 * @param rediretToUrlLogin rediretToUrlLogin 
	 */
	public void setRedirectToUrlLogin(String urlLogin) {
		if (!urlLogin.equals("")) {
			this.redirectToUrlLogin = "top.document.location.replace('"+urlLogin+"')";
		}
		else {
			this.redirectToUrlLogin="";
		}
	}

	/**
	 * @return rediretToUrlLogin
	 */
	public String getRedirectToUrlLogin() {
		return redirectToUrlLogin;
	}
  
}
