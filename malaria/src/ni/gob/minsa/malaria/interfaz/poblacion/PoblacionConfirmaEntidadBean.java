// -----------------------------------------------
// PoblacionConfirmaEntidadBean.java
// -----------------------------------------------
package ni.gob.minsa.malaria.interfaz.poblacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.datos.poblacion.ControlEntidadPoblacionDA;
import ni.gob.minsa.malaria.datos.poblacion.PoblacionComunidadDA;
import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.modelo.poblacion.ControlEntidadPoblacion;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.UnidadSituacion;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.servicios.poblacion.ControlEntidadPoblacionService;
import ni.gob.minsa.malaria.servicios.poblacion.PoblacionComunidadService;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;


/**
 * Servicio para la capa de presentaci�n de la p�gina
 * <b>/poblacion/poblacionConfirmaEntidad.xhtml</b>
 *
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 15/06/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class PoblacionConfirmaEntidadBean implements Serializable {
	private static final long serialVersionUID = 1L;

	protected InfoSesion infoSesion;
	
	// lista de objetos para poblar el combo de entidades
	private List<EntidadAdtva> entidades;
	// entidad seleccionada
	private Long entidadSelectedId;
	private EntidadAdtva entidadSelected;

	private boolean existePoblacion;
	private boolean poblacionConfirmada;
	private boolean poblacionUnidadesConfirmadas;
	
	// mapeo para poblar el combo de a�os
	private Map<Integer,Integer> a�os = new HashMap<Integer,Integer>();
	// a�o seleccionado
	private Integer a�oSelected;
	private Integer a�oActual;

	// lista de objetos de unidades con su situaci�n para poblar la grilla
	private List<UnidadSituacion> unidadesSituacion;

	private static ControlEntidadPoblacionService controlEntidadPoblacionService = new ControlEntidadPoblacionDA();
	private static PoblacionComunidadService poblacionComunidadService= new PoblacionComunidadDA();
	private static EntidadAdtvaService entidadAdtvaService = new EntidadAdtvaDA();
	
	// ---------------------------------------- Constructor
	
	public PoblacionConfirmaEntidadBean() {
		
		this.entidades=new ArrayList<EntidadAdtva>();
		this.unidadesSituacion=new ArrayList<UnidadSituacion>();

		this.infoSesion=Utilidades.obtenerInfoSesion();
		
		// obtiene los datos para el combo de a�os
		this.a�os.putAll(Utilidades.ObtenerA�os(Integer.valueOf(Utilidades.A�oActual().intValue()-1),true));
		this.a�oSelected=Utilidades.A�oActual();
		this.a�oActual=this.a�oSelected;

		// solo se obtienen las entidades autorizadas de forma directa
		// para los cuales si tiene derecho de confirmar las poblaciones
		this.entidades=ni.gob.minsa.malaria.reglas.Operacion.entidadesAutorizadas(this.infoSesion.getUsuarioId(), true);
		// selecciona la primera entidad
		if (this.entidades!=null && this.entidades.size()>0) {
			this.entidadSelectedId=this.entidades.get(0).getEntidadAdtvaId();
			this.entidadSelected=this.entidades.get(0);
		}
		
		obtenerUnidadesSituacion();

	}
	

	/**
	 * Obtiene el listado de unidades de salud con sectores asociados y la situaci�n
	 * del registro de poblaci�n de las comunidades que pertenecen a dicho sectores
	 * 
	 */
	public void obtenerUnidadesSituacion() {
	
		this.poblacionUnidadesConfirmadas=false;
		this.existePoblacion=false;
		this.poblacionConfirmada=false;

		// si existe al menos una unidad sin poblacion confirmada, la 
		// entidad administrativa no puede ser confirmada, pueden
		// existir unidades sin poblaci�n y la entidad puede confirmar
		this.unidadesSituacion.clear();
		
		if (this.entidadSelected==null) return;

		this.unidadesSituacion=poblacionComunidadService.SituacionUnidadesPorEntidad(this.entidadSelected.getCodigo(), this.a�oSelected);
		
		// si existe poblaci�n, se verificar� primero si la entidad ya ha sido confirmada
		
		this.poblacionConfirmada=controlEntidadPoblacionService.ControlPorEntidad(this.a�oSelected, this.entidadSelectedId)==null?false:true;

		boolean iPendientesConfirmacion=false;
		for (UnidadSituacion oUnidadSituacion:this.unidadesSituacion) {
			if (oUnidadSituacion.getPoblacion()!=null && !oUnidadSituacion.getPoblacion().equals(new BigDecimal(0))) {
				this.existePoblacion=true;
				if (oUnidadSituacion.getSituacion().equals(new BigDecimal(2))) iPendientesConfirmacion=true;
				
				// si ya se encontr� al menos una unidad que tenga registrada poblaci�n
				// y dichos datos no han sido confirmados, se aborta el ciclo
				if (existePoblacion && iPendientesConfirmacion) break;
			}
		}
		if (existePoblacion) {
			this.poblacionUnidadesConfirmadas=!iPendientesConfirmacion;
		}
	} 

	// ------------------------------------------- Acciones
	
	public void cambiarEntidad() {

		InfoResultado oResultado=entidadAdtvaService.Encontrar(this.entidadSelectedId);
		if (oResultado.isOk()) {
			EntidadAdtva oEntidadAdtva=(EntidadAdtva)oResultado.getObjeto();
			this.entidadSelected=oEntidadAdtva;
		} else {
			this.entidadSelected=null;
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}

		obtenerUnidadesSituacion();

	}

	public void cambiarA�o() {

		if ((this.entidadSelectedId!=null && this.entidadSelectedId!=0)) {
			obtenerUnidadesSituacion();
		}
		
	}

	/**
	 * Guarda la confirmaci�n de la poblaci�n de las comunidades para la
	 * entidad administrativa, i.e. guarda el control de bloqueo o protecci�n
	 * para el a�o espec�fico
	 */
	public void confirmar(ActionEvent pEvento){

		InfoResultado oResultado=new InfoResultado();
		ControlEntidadPoblacion oControlEntidadPoblacion=new ControlEntidadPoblacion();
		oControlEntidadPoblacion.setA�o(this.a�oSelected);
		oControlEntidadPoblacion.setEntidadAdtva(this.entidadSelected);
		oControlEntidadPoblacion.setUsuarioRegistro(this.infoSesion.getUsername());
		oControlEntidadPoblacion.setEntidadAdtva(this.entidadSelected);
		oControlEntidadPoblacion.setFechaRegistro(Calendar.getInstance().getTime());

		oResultado=controlEntidadPoblacionService.Agregar(oControlEntidadPoblacion);
			
		if (!oResultado.isOk()) {
			FacesMessage msg=Mensajes.enviarMensaje(oResultado);
			if (msg!=null){
				FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			}
		} else {
			this.poblacionConfirmada=true;
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "Los datos de poblaci�n para la entidad administrativa y el a�o, han sido confirmados", ""));
		}
	}

	// ---------------------------------------- Propiedades	

	public List<EntidadAdtva> getEntidades() {
		return entidades;
	}

	public void setEntidades(List<EntidadAdtva> entidades) {
		this.entidades = entidades;
	}

	public Long getEntidadSelectedId() {
		return entidadSelectedId;
	}

	public void setEntidadSelectedId(Long entidadSelectedId) {
		this.entidadSelectedId = entidadSelectedId;
	}

	public EntidadAdtva getEntidadSelected() {
		return entidadSelected;
	}

	public void setEntidadSelected(EntidadAdtva entidadSelected) {
		this.entidadSelected = entidadSelected;
	}

	public void setA�os(Map<Integer,Integer> a�os) {
		this.a�os = a�os;
	}

	public Map<Integer,Integer> getA�os() {
		return a�os;
	}

	public void setA�oSelected(Integer a�oSelected) {
		this.a�oSelected = a�oSelected;
	}

	public Integer getA�oSelected() {
		return a�oSelected;
	}

	public void setExistePoblacion(boolean existePoblacion) {
		this.existePoblacion = existePoblacion;
	}

	public boolean isExistePoblacion() {
		return existePoblacion;
	}

	public void setPoblacionConfirmada(boolean poblacionConfirmada) {
		this.poblacionConfirmada = poblacionConfirmada;
	}

	public boolean isPoblacionConfirmada() {
		return poblacionConfirmada;
	}
	
	public void setUnidadesSituacion(List<UnidadSituacion> unidadesSituacion) {
		this.unidadesSituacion = unidadesSituacion;
	}

	public List<UnidadSituacion> getUnidadesSituacion() {
		return unidadesSituacion;
	}

	public void setPoblacionUnidadesConfirmadas(boolean poblacionUnidadesConfirmadas) {
		this.poblacionUnidadesConfirmadas = poblacionUnidadesConfirmadas;
	}

	public boolean isPoblacionUnidadesConfirmadas() {
		return poblacionUnidadesConfirmadas;
	}

	public Integer getA�oActual() {
		return a�oActual;
	}

	public void setA�oActual(Integer a�oActual) {
		this.a�oActual = a�oActual;
	}

}
