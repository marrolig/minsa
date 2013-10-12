// -----------------------------------------------
// FactorRiesgoBean.java
// -----------------------------------------------
package ni.gob.minsa.malaria.interfaz.vigilancia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import ni.gob.minsa.aplicacion.Seguridad;
import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.datos.vigilancia.EventoSaludDA;
import ni.gob.minsa.malaria.datos.vigilancia.FactorRiesgoDA;
import ni.gob.minsa.malaria.datos.vigilancia.FactorRiesgoEventoDA;
import ni.gob.minsa.malaria.modelo.vigilancia.EventoSalud;
import ni.gob.minsa.malaria.modelo.vigilancia.FactorRiesgo;
import ni.gob.minsa.malaria.modelo.vigilancia.FactorRiesgoEvento;
import ni.gob.minsa.malaria.servicios.vigilancia.EventoSaludService;
import ni.gob.minsa.malaria.servicios.vigilancia.FactorRiesgoEventoService;
import ni.gob.minsa.malaria.servicios.vigilancia.FactorRiesgoService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;


/**
 * Servicio para la capa de presentación de la página
 * asociada al mantenimiento de los factores de riesgo
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.1, &nbsp; 24/05/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class FactorRiesgoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private InfoSesion infoSesion;
	
	protected FactorRiesgo factorRiesgo;
	
	// objetos para poblar la grilla de factores de riesgo
	private List<FactorRiesgo> factoresRiesgos;
	
	// propiedad seleccionada en la grilla de factores de riesgo
	private FactorRiesgo factorRiesgoSelected;
	
	// información del factor de riesgo
	private long factorRiesgoId;
	private String nombre;
	private String codigo;
	private boolean pasivo;
	
	private String fechaRegistro;
	// usuario que realiza o efectuó registro
	private String usuarioRegistro;
	private String usuarioNombre;
	
	private List<FactorRiesgoEvento> eventosAsociados;
	private List<EventoSalud> eventos;
	private long factorRiesgoEventoSelectedId;
	private EventoSalud eventoSelected;
	
	private Format formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm");

	private static FactorRiesgoService factorRiesgoService = new FactorRiesgoDA();
	private static FactorRiesgoEventoService factorRiesgoEventoService = new FactorRiesgoEventoDA();
	private static EventoSaludService eventoSaludService = new EventoSaludDA();
	
	// --------------------------------------- Constructor
	
	public FactorRiesgoBean(){

		this.infoSesion=Utilidades.obtenerInfoSesion();

		iniciarPropiedades();
	}

	// ------------------------------------------- Métodos
	
	protected void iniciarPropiedades() {

		Boolean iPasivo=null;
		this.factoresRiesgos=factorRiesgoService.Listar(iPasivo);
		iniciarDetalle();

	}
	
	protected void iniciarDetalle() {
		
		this.factorRiesgoId=0;
		this.factorRiesgoSelected=null;
		this.nombre="";
		this.codigo="";
		this.pasivo=false;
		this.usuarioRegistro=this.infoSesion.getUsername();
		this.usuarioNombre=this.infoSesion.getNombre();
		this.eventosAsociados=new ArrayList<FactorRiesgoEvento>();
		
		Date iFecha=new Date();
		this.fechaRegistro=formatter.format(iFecha);

	}
	
	public void onFactorRiesgoSelected(SelectEvent pEvento) {
	
		this.factorRiesgo=this.factorRiesgoSelected;
		this.factorRiesgoId=this.factorRiesgoSelected.getFactorRiesgoId();
		
		this.nombre=this.factorRiesgoSelected.getNombre();
		this.codigo=this.factorRiesgoSelected.getCodigo();
		this.pasivo=this.factorRiesgoSelected.getPasivo().equals(new BigDecimal(1))?true:false;
		
		Date iFecha=this.factorRiesgoSelected.getFechaRegistro();
		this.fechaRegistro=formatter.format(iFecha);
		
		this.usuarioRegistro=this.factorRiesgoSelected.getUsuarioRegistro();
		this.usuarioNombre= Seguridad.NombreUsuario(this.usuarioRegistro);
		
		// obtiene la lista de eventos de salud asociados al factor
		this.eventosAsociados=factorRiesgoEventoService.EventosPorFactorRiesgo(this.factorRiesgoId);
		
		FacesContext context = FacesContext.getCurrentInstance( ); 
		UIComponent componentDetalle = null; 
		UIViewRoot root = context.getViewRoot( ); 
		componentDetalle = (UIComponent) root.findComponent("frmFactorRiesgo:panDetalle");
		for (UIComponent uic:componentDetalle.getChildren()) {
			if (uic instanceof EditableValueHolder) {   
				EditableValueHolder evh=(EditableValueHolder)uic;   
				evh.resetValue();   
			}
		}
	}
	
	public void onFactorRiesgoUnSelected(UnselectEvent pEvento) {
		
		iniciarDetalle();
	}
	
	/**
	 * Actualiza o inserta un factor de riesgo
	 */
	public void guardar(ActionEvent pEvento){

		if (this.factorRiesgoId==0) {
			if (factorRiesgoService.EncontrarPorCodigo(this.codigo)!=null) {
				// si no es nulo implica que existe
				FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN,"El código asignado al factor de riesgo ya existe","Utilice otro código."));
				return;
			}
		}
		
		// crea una nueva instancia para manejar el resultado
		InfoResultado oResultado=new InfoResultado();
		FactorRiesgo oFactorRiesgo=new FactorRiesgo();

		oFactorRiesgo.setNombre(this.nombre.trim());
		oFactorRiesgo.setPasivo(this.pasivo?new BigDecimal(1):new BigDecimal(0));
		
		if(this.factorRiesgoId!=0){
			oFactorRiesgo.setFactorRiesgoId(this.factorRiesgoId);
			oResultado=factorRiesgoService.Guardar(oFactorRiesgo);
		} else{
			oFactorRiesgo.setCodigo(this.codigo.trim());
			oFactorRiesgo.setUsuarioRegistro(this.usuarioRegistro);
			oFactorRiesgo.setFechaRegistro(Calendar.getInstance().getTime());
			oResultado=factorRiesgoService.Agregar(oFactorRiesgo);
		}

		if (oResultado.isOk()){
			this.factorRiesgoSelected=null;
			iniciarPropiedades();
			oResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	/**
	 * Se ejecuta cuando el usuario pulsa en el botón agregar
	 * y consiste en la inicialización de las diferentes propiedades
	 * para colocar el estado del formulario en modo de agregar
	 */
	public void agregar(ActionEvent pEvento) {

		this.factorRiesgoSelected=null;
		iniciarDetalle();
		
	}
	
	/**
	 * Elimina el factor de riesgo
	 */
	public void eliminar(ActionEvent pEvento){

		if (this.factorRiesgoId==0) return;
		
		InfoResultado oResultado = new InfoResultado();
		oResultado=factorRiesgoService.Eliminar(this.factorRiesgoId);
		
		if (oResultado.isOk()){
			this.factorRiesgoSelected=null;
			iniciarPropiedades();
			oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	/**
	 * Eliminar la asociación entre el factor de riesgo y el evento de salud seleccionado.
	 * <p>
	 * @param pEvento Evento asociado al botón
	 */
	public void eliminarFactorRiesgoEvento(ActionEvent pEvento) {
		
		InfoResultado oResultado = new InfoResultado();
		oResultado=factorRiesgoEventoService.Eliminar(this.factorRiesgoEventoSelectedId);
		
		this.eventosAsociados=factorRiesgoEventoService.EventosPorFactorRiesgo(this.factorRiesgoId);
		
		FacesMessage msg = null;
		
		if (oResultado.isOk()){
			oResultado.setMensaje("Se ha eliminado la asociación entre el evento de salud y el factor de riesgo");
		}
		
		msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	/**
	 * Agrega una asociación entre un factor de riesgo y el evento de salud seleccionado.
	 *
	 * @param pEvento ActionEvent
	 */
	public void agregarFactorRiesgoEvento(ActionEvent pEvento) {
		
		InfoResultado oResultado = new InfoResultado();
		
		FactorRiesgoEvento oFactorRiesgoEvento = new FactorRiesgoEvento();
		oFactorRiesgoEvento.setFactorRiesgo(this.factorRiesgoSelected);
		oFactorRiesgoEvento.setEventoSalud(this.eventoSelected);
		oFactorRiesgoEvento.setUsuarioRegistro(this.infoSesion.getUsername());
		oFactorRiesgoEvento.setFechaRegistro(Calendar.getInstance().getTime());
		
		FacesMessage msg = null;
		
		oResultado=factorRiesgoEventoService.Agregar(oFactorRiesgoEvento);

		this.eventoSelected=null;
		this.eventosAsociados=factorRiesgoEventoService.EventosPorFactorRiesgo(this.factorRiesgoId);
		
		if (oResultado.isOk()){
			oResultado.setMensaje(Mensajes.REGISTRO_AGREGADO);
		}
		
		msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
	
	}

	/**
	 * Obtiene los eventos de salud activos partir de los cuales se seleccionarán
	 * los eventos de salud a ser asociados a un factor de riesgo.  
	 * 
	 */
	public void obtenerEventos() {
		
		this.eventos=eventoSaludService.Listar(Boolean.FALSE);
		this.eventoSelected=null;
	}

	// Propiedades -------------------------------------------------------------
	
	public FactorRiesgo getFactorRiesgo() {
		return factorRiesgo;
	}

	public void setFactorRiesgo(FactorRiesgo factorRiesgo) {
		this.factorRiesgo = factorRiesgo;
	}

	public List<FactorRiesgo> getFactoresRiesgos() {
		return factoresRiesgos;
	}

	public void setFactoresRiesgos(List<FactorRiesgo> factoresRiesgos) {
		this.factoresRiesgos = factoresRiesgos;
	}

	public FactorRiesgo getFactorRiesgoSelected() {
		return factorRiesgoSelected;
	}

	public void setFactorRiesgoSelected(FactorRiesgo factorRiesgoSelected) {
		this.factorRiesgoSelected = factorRiesgoSelected;
	}

	public long getFactorRiesgoId() {
		return factorRiesgoId;
	}

	public void setFactorRiesgoId(long factorRiesgoId) {
		this.factorRiesgoId = factorRiesgoId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public boolean isPasivo() {
		return pasivo;
	}

	public void setPasivo(boolean pasivo) {
		this.pasivo = pasivo;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getUsuarioNombre() {
		return usuarioNombre;
	}

	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	public void setEventosAsociados(List<FactorRiesgoEvento> eventosAsociados) {
		this.eventosAsociados = eventosAsociados;
	}

	public List<FactorRiesgoEvento> getEventosAsociados() {
		return eventosAsociados;
	}

	public void setEventos(List<EventoSalud> eventos) {
		this.eventos = eventos;
	}

	public List<EventoSalud> getEventos() {
		return eventos;
	}

	public void setFactorRiesgoEventoSelectedId(long factorRiesgoEventoSelectedId) {
		this.factorRiesgoEventoSelectedId = factorRiesgoEventoSelectedId;
	}

	public long getFactorRiesgoEventoSelectedId() {
		return factorRiesgoEventoSelectedId;
	}

	public EventoSalud getEventoSelected() {
		return eventoSelected;
	}

	public void setEventoSelected(EventoSalud eventoSelected) {
		this.eventoSelected = eventoSelected;
	}


}
