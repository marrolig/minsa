// -----------------------------------------------
// HeaderBean.java
// -----------------------------------------------

package ni.gob.minsa.malaria.interfaz.seguridad;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import ni.gob.minsa.aplicacion.Seguridad;
import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.MenuModel;

/**
 * Servicio para la capa de presentación del encabezado del
 * formato de maquetación utilizado, específicamente
 * en la sección header.xhtml
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 30/01/2011
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class HeaderBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fecha;
	
	// username
	private String usuario;
	
	// nombre completo del usuario
	private String usuarioNombre;
	
	// identificador del usuario
	private Long usuarioId;
	
	// atributos para el cambio de clave
	private String claveActual;
	private String claveNueva;
	private String claveRepite;

	private Map<String, String> mapSistema = new HashMap<String,String>();
	private Map.Entry<String, String> sistemaSelected;
	
	// URL del portal
	private String urlLogin;
	
	// menú autorizado al usuario
	private MenuModel menu;
	
	@SuppressWarnings("unchecked")
	public HeaderBean(){
		
		InfoSesion oInfoSesion=Utilidades.obtenerInfoSesion();
		this.usuario=oInfoSesion.getUsername();
		this.usuarioNombre=oInfoSesion.getNombre();
		this.usuarioId=oInfoSesion.getUsuarioId();
		
		FacesContext oFacesContext = FacesContext.getCurrentInstance();
		this.setMenu((MenuModel)oFacesContext.getExternalContext().getSessionMap().get("menuActual"));

		claveActual=null;
		claveNueva=null;
		claveRepite=null;

		this.mapSistema=(Map<String,String>)oFacesContext.getExternalContext().getSessionMap().get("sistemasAutorizados");
		
		Date iFecha=new Date();
		Format formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm");
		fecha=formatter.format(iFecha);
		
	}
	
	public void iniciarPropiedadesCambio() {
		this.claveActual="";
		this.claveNueva="";
		this.claveRepite="";
	}

	public void cancelarCambio(ActionEvent pEvento) {
		
		iniciarPropiedadesCambio();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onSistemaSelected(SelectEvent iEvento) {

		RequestContext context = RequestContext.getCurrentInstance();

		InfoResultado oInfoResultado = Seguridad.solicitarAplicacion(this.usuario, this.sistemaSelected.getKey());
		if (!oInfoResultado.isOk()) {
			FacesMessage msg = Mensajes.enviarMensaje(oInfoResultado);
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
	        context.addCallbackParam("peticionOK", false);
			String aComponentes[] = {"frmHeader:grwMensaje"};
	        Collection iComponentes = Arrays.asList(aComponentes);
	        context.update(iComponentes);
		} else {
			context.addCallbackParam("peticionOK", true);
			context.addCallbackParam("urlApp", (String)oInfoResultado.getObjeto());
		}

	}
	
	public void cambiarClave() {
		
		RequestContext oContext = RequestContext.getCurrentInstance();
		
		this.claveActual.trim();
		this.claveNueva.trim();
		this.claveRepite.trim();

		if (!this.claveNueva.equals(this.claveRepite) || this.claveNueva.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La nueva clave no coincide con la solicitud de repetir la misma o no se ha proporcionado una de las dos.",""));
			oContext.addCallbackParam("logOK", false);
			iniciarPropiedadesCambio();
			return;
		}

		InfoResultado oResultadoClave=Seguridad.guardarClave(this.usuario, this.claveActual, this.claveNueva);
		if (!oResultadoClave.isOk()) {
			iniciarPropiedadesCambio();
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_FATAL,oResultadoClave.getMensaje(),"La clave NO ha sido modificada.  Intentelo nuevamente."));
			oContext.addCallbackParam("logOK", false);
			iniciarPropiedadesCambio();
			return;
		}
		oContext.addCallbackParam("logOK", true);
		iniciarPropiedadesCambio();
	}

	public String getFecha() {
		return(fecha);
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClaveActual() {
		return claveActual;
	}

	public void setClaveActual(String claveActual) {
		this.claveActual = claveActual;
	}

	public String getClaveNueva() {
		return claveNueva;
	}

	public void setClaveNueva(String claveNueva) {
		this.claveNueva = claveNueva;
	}

	public String getClaveRepite() {
		return claveRepite;
	}

	public void setClaveRepite(String claveRepite) {
		this.claveRepite = claveRepite;
	}
	
	@SuppressWarnings("rawtypes")
	public void borrarSesiones(ActionEvent pEvento) {
		
		FacesContext oFacesContext = FacesContext.getCurrentInstance();
		Map oSessionMap= oFacesContext.getExternalContext().getSessionMap();
		for (Iterator iter = oSessionMap.keySet().iterator(); iter.hasNext();) {
			Object key = (Object) iter.next();
			oSessionMap.remove(key);
		}
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
		this.urlLogin = urlLogin;
	}

	/**
	 * @return urlLogin
	 */
	public String getUrlLogin() {
		return urlLogin;
	}

	/**
	 * @param menu menu 
	 */
	public void setMenu(MenuModel menu) {
		this.menu = menu;
	}

	/**
	 * @return menu
	 */
	public MenuModel getMenu() {
		return menu;
	}

	/**
	 * @param usuarioNombre usuarioNombre 
	 */
	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	/**
	 * @return usuarioNombre
	 */
	public String getUsuarioNombre() {
		return usuarioNombre;
	}
	
	public List<Map.Entry<String, String>> getSistemas() {
	    Set<Map.Entry<String, String>> sistemaSet = 
	                     mapSistema.entrySet();
	    return new ArrayList<Map.Entry<String, String>>(sistemaSet);
	}

	public void setSistemaSelected(Map.Entry<String, String> sistemaSelected) {
		this.sistemaSelected = sistemaSelected;
	}

	public Map.Entry<String, String> getSistemaSelected() {
		return sistemaSelected;
	}

}
