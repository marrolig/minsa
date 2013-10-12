// -----------------------------------------------
// ParametroEpidemiologicoBean.java
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
import ni.gob.minsa.malaria.datos.vigilancia.ParametroEpidemiologicoDA;
import ni.gob.minsa.malaria.datos.vigilancia.ParametroEventoDA;
import ni.gob.minsa.malaria.modelo.vigilancia.EventoSalud;
import ni.gob.minsa.malaria.modelo.vigilancia.ParametroEpidemiologico;
import ni.gob.minsa.malaria.modelo.vigilancia.ParametroEvento;
import ni.gob.minsa.malaria.servicios.vigilancia.EventoSaludService;
import ni.gob.minsa.malaria.servicios.vigilancia.ParametroEpidemiologicoService;
import ni.gob.minsa.malaria.servicios.vigilancia.ParametroEventoService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;


/**
 * Servicio para la capa de presentación de la página
 * asociada al mantenimiento de los parámetros epidemiológicos
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.1, &nbsp; 13/06/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class ParametroEpidemiologicoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private InfoSesion infoSesion;
	
	protected ParametroEpidemiologico parametroEpidemiologico;
	
	// objetos para poblar la grilla de Parametros Epidemiologicos
	private List<ParametroEpidemiologico> parametrosEpidemiologicos;
	
	// propiedad seleccionada en la grilla de Parametros Epidemiologicos
	private ParametroEpidemiologico parametroEpidemiologicoSelected;
	
	// información del parametro epidemiologico
	private long parametroEpidemiologicoId;
	private String concepto;
	private String codigo;
	private boolean pasivo;
	private String descripcion;
	private String etiqueta;
	
	private String fechaRegistro;
	// usuario que realiza o efectuó registro
	private String usuarioRegistro;
	private String usuarioNombre;
	
	private List<ParametroEvento> eventosAsociados;
	private List<EventoSalud> eventos;
	private long parametroEventoSelectedId;
	private EventoSalud eventoSelected;
	
	private Format formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm");

	private static ParametroEpidemiologicoService parametroEpidemiologicoService = new ParametroEpidemiologicoDA();
	private static ParametroEventoService parametroEventoService = new ParametroEventoDA();
	private static EventoSaludService eventoSaludService = new EventoSaludDA();
	
	// --------------------------------------- Constructor
	
	public ParametroEpidemiologicoBean(){

		this.infoSesion=Utilidades.obtenerInfoSesion();

		iniciarPropiedades();
	}

	// ------------------------------------------- Métodos
	
	protected void iniciarPropiedades() {

		Boolean iPasivo=null;
		this.parametrosEpidemiologicos=parametroEpidemiologicoService.Listar(iPasivo);
		iniciarDetalle();

	}
	
	protected void iniciarDetalle() {
		
		this.parametroEpidemiologicoId=0;
		this.parametroEpidemiologicoSelected=null;
		this.concepto="";
		this.descripcion="";
		this.etiqueta="";
		this.codigo="";
		this.pasivo=false;
		this.usuarioRegistro=this.infoSesion.getUsername();
		this.usuarioNombre=this.infoSesion.getNombre();
		this.eventosAsociados=new ArrayList<ParametroEvento>();
		
		Date iFecha=new Date();
		this.fechaRegistro=formatter.format(iFecha);

	}
	
	public void onParametroEpidemiologicoSelected(SelectEvent pEvento) {
	
		this.parametroEpidemiologico=this.parametroEpidemiologicoSelected;
		this.parametroEpidemiologicoId=this.parametroEpidemiologicoSelected.getParametroEpidemiologicoId();
		
		this.concepto=this.parametroEpidemiologicoSelected.getConcepto();
		this.codigo=this.parametroEpidemiologicoSelected.getCodigo();
		this.descripcion=this.parametroEpidemiologicoSelected.getDescripcion();
		this.etiqueta=this.parametroEpidemiologicoSelected.getEtiqueta();
		this.pasivo=this.parametroEpidemiologicoSelected.getPasivo().equals(new BigDecimal(1))?true:false;
		
		Date iFecha=this.parametroEpidemiologicoSelected.getFechaRegistro();
		this.fechaRegistro=formatter.format(iFecha);
		
		this.usuarioRegistro=this.parametroEpidemiologicoSelected.getUsuarioRegistro();
		this.usuarioNombre= Seguridad.NombreUsuario(this.usuarioRegistro);
		
		// obtiene la lista de eventos de salud asociados al parámetro epidemiológico
		this.eventosAsociados=parametroEventoService.EventosPorParametro(this.parametroEpidemiologicoId);
		
		FacesContext context = FacesContext.getCurrentInstance( ); 
		UIComponent componentDetalle = null; 
		UIViewRoot root = context.getViewRoot( ); 
		componentDetalle = (UIComponent) root.findComponent("frmParametro:panDetalle");
		for (UIComponent uic:componentDetalle.getChildren()) {
			if (uic instanceof EditableValueHolder) {   
				EditableValueHolder evh=(EditableValueHolder)uic;   
				evh.resetValue();   
			}
		}
	}
	
	public void onParametroEpidemiologicoUnSelected(UnselectEvent pEvento) {
		
		iniciarDetalle();
	}
	
	/**
	 * Actualiza o inserta un parámetro epidemiologico
	 */
	public void guardar(ActionEvent pEvento){

		if (this.parametroEpidemiologicoId==0) {
			if (parametroEpidemiologicoService.EncontrarPorCodigo(this.codigo)!=null) {
				// si no es nulo implica que existe
				FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN,"El código asignado al parámetro epidemiológico ya existe","Utilice otro código."));
				return;
			}
		}
		
		// crea una nueva instancia para manejar el resultado
		InfoResultado oResultado=new InfoResultado();
		ParametroEpidemiologico oParametroEpidemiologico=new ParametroEpidemiologico();

		oParametroEpidemiologico.setConcepto(this.concepto.trim());
		oParametroEpidemiologico.setPasivo(this.pasivo?new BigDecimal(1):new BigDecimal(0));
		oParametroEpidemiologico.setDescripcion(this.descripcion.trim().isEmpty()?null:this.descripcion.trim());
		oParametroEpidemiologico.setEtiqueta(this.etiqueta.trim());
		
		if(this.parametroEpidemiologicoId!=0){
			oParametroEpidemiologico.setParametroEpidemiologicoId(this.parametroEpidemiologicoId);
			oResultado=parametroEpidemiologicoService.Guardar(oParametroEpidemiologico);
		} else{
			oParametroEpidemiologico.setCodigo(this.codigo.trim());
			oParametroEpidemiologico.setUsuarioRegistro(this.usuarioRegistro);
			oParametroEpidemiologico.setFechaRegistro(Calendar.getInstance().getTime());
			oResultado=parametroEpidemiologicoService.Agregar(oParametroEpidemiologico);
		}

		if (oResultado.isOk()){
			this.parametroEpidemiologicoSelected=null;
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

		this.parametroEpidemiologicoSelected=null;
		iniciarDetalle();
		
	}
	
	/**
	 * Elimina el parametro Epidemiologico
	 */
	public void eliminar(ActionEvent pEvento){

		if (this.parametroEpidemiologicoId==0) return;
		
		InfoResultado oResultado = new InfoResultado();
		oResultado=parametroEpidemiologicoService.Eliminar(this.parametroEpidemiologicoId);
		
		if (oResultado.isOk()){
			this.parametroEpidemiologicoSelected=null;
			iniciarPropiedades();
			oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	/**
	 * Eliminar la asociación entre el parámetro epidemiológico y el evento de salud seleccionado.
	 * <p>
	 * @param pEvento Evento asociado al botón
	 */
	public void eliminarParametroEvento(ActionEvent pEvento) {
		
		InfoResultado oResultado = new InfoResultado();
		oResultado=parametroEventoService.Eliminar(this.parametroEventoSelectedId);
		
		this.eventosAsociados=parametroEventoService.EventosPorParametro(this.parametroEpidemiologicoId);
		
		FacesMessage msg = null;
		
		if (oResultado.isOk()){
			oResultado.setMensaje("Se ha eliminado la asociación entre el evento de salud y el parámetro epidemiológico");
		}
		
		msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	/**
	 * Agrega una asociación entre un parámetro epidemiológico y el evento de salud seleccionado.
	 *
	 * @param pEvento ActionEvent
	 */
	public void agregarParametroEvento(ActionEvent pEvento) {
		
		InfoResultado oResultado = new InfoResultado();
		
		ParametroEvento oParametroEvento = new ParametroEvento();
		oParametroEvento.setParametroEpidemiologico(this.parametroEpidemiologicoSelected);
		oParametroEvento.setEventoSalud(this.eventoSelected);
		oParametroEvento.setUsuarioRegistro(this.infoSesion.getUsername());
		oParametroEvento.setFechaRegistro(Calendar.getInstance().getTime());
		
		FacesMessage msg = null;
		
		oResultado=parametroEventoService.Agregar(oParametroEvento);

		this.eventoSelected=null;
		this.eventosAsociados=parametroEventoService.EventosPorParametro(this.parametroEpidemiologicoId);
		
		if (oResultado.isOk()){
			oResultado.setMensaje(Mensajes.REGISTRO_AGREGADO);
		}
		
		msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
	
	}

	/**
	 * Obtiene los eventos de salud activos partir de los cuales se seleccionarán
	 * los eventos de salud a ser asociados a un parámetro epidemiológico.  
	 * 
	 */
	public void obtenerEventos() {
		
		this.eventos=eventoSaludService.Listar(Boolean.FALSE);
		this.eventoSelected=null;
	}

	// Propiedades -------------------------------------------------------------
	
	public ParametroEpidemiologico getParametroEpidemiologico() {
		return parametroEpidemiologico;
	}

	public void setParametroEpidemiologico(ParametroEpidemiologico parametroEpidemiologico) {
		this.parametroEpidemiologico = parametroEpidemiologico;
	}

	public List<ParametroEpidemiologico> getParametrosEpidemiologicos() {
		return parametrosEpidemiologicos;
	}

	public void setParametrosEpidemiologicos(List<ParametroEpidemiologico> parametrosEpidemiologicos) {
		this.parametrosEpidemiologicos = parametrosEpidemiologicos;
	}

	public ParametroEpidemiologico getParametroEpidemiologicoSelected() {
		return parametroEpidemiologicoSelected;
	}

	public void setParametroEpidemiologicoSelected(ParametroEpidemiologico parametroEpidemiologicoSelected) {
		this.parametroEpidemiologicoSelected = parametroEpidemiologicoSelected;
	}

	public long getParametroEpidemiologicoId() {
		return parametroEpidemiologicoId;
	}

	public void setParametroEpidemiologicoId(long parametroEpidemiologicoId) {
		this.parametroEpidemiologicoId = parametroEpidemiologicoId;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
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

	public void setEventosAsociados(List<ParametroEvento> eventosAsociados) {
		this.eventosAsociados = eventosAsociados;
	}

	public List<ParametroEvento> getEventosAsociados() {
		return eventosAsociados;
	}

	public void setEventos(List<EventoSalud> eventos) {
		this.eventos = eventos;
	}

	public List<EventoSalud> getEventos() {
		return eventos;
	}

	public void setParametroEventoSelectedId(long parametroEventoSelectedId) {
		this.parametroEventoSelectedId = parametroEventoSelectedId;
	}

	public long getParametroEventoSelectedId() {
		return parametroEventoSelectedId;
	}

	public EventoSalud getEventoSelected() {
		return eventoSelected;
	}

	public void setEventoSelected(EventoSalud eventoSelected) {
		this.eventoSelected = eventoSelected;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getEtiqueta() {
		return etiqueta;
	}


}
