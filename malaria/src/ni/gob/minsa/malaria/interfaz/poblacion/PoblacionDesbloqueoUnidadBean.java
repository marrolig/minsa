// -----------------------------------------------
// PoblacionDesbloqueoUnidadBean.java
// -----------------------------------------------
package ni.gob.minsa.malaria.interfaz.poblacion;

import java.io.Serializable;
import java.util.ArrayList;
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
import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.datos.poblacion.ControlEntidadPoblacionDA;
import ni.gob.minsa.malaria.datos.poblacion.ControlUnidadPoblacionDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.poblacion.ControlUnidadPoblacion;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.poblacion.ControlEntidadPoblacionService;
import ni.gob.minsa.malaria.servicios.poblacion.ControlUnidadPoblacionService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;


/**
 * Servicio para la capa de presentaci�n de la p�gina
 * <b>/poblacion/poblacionDesbloqueoUnidad.xhtml</b>
 *
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 16/05/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class PoblacionDesbloqueoUnidadBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private InfoSesion infoSesion;
	
	// lista de objetos para poblar el combo de entidades
	private List<EntidadAdtva> entidades;
	// entidad seleccionada
	private Long entidadSelectedId;
	private EntidadAdtva entidadSelected;

	// lista de objetos de unidad para poblar la grilla
	private List<ControlUnidadPoblacion> controlUnidadesPoblacion;
	
	private boolean existePoblacion;
	private boolean poblacionConfirmada;
	private boolean existenUnidadesConfirmadas;
	
	// mapeo para poblar el combo de a�os
	private Map<Integer,Integer> a�os = new HashMap<Integer,Integer>();
	// a�o seleccionado
	private Integer a�oSelected;
	private Integer a�oActual;
	
	// objetos seleccionados desde la grilla
	private ControlUnidadPoblacion[] controlUnidadesPoblacionSelected;

	private static ControlEntidadPoblacionService controlEntidadPoblacionService = new ControlEntidadPoblacionDA();
	private static ControlUnidadPoblacionService controlUnidadPoblacionService = new ControlUnidadPoblacionDA();
	private static EntidadAdtvaService entidadAdtvaService = new EntidadAdtvaDA();
	
	// ---------------------------------------- Constructor
	
	public PoblacionDesbloqueoUnidadBean() {
		
		this.entidades=new ArrayList<EntidadAdtva>();
		this.controlUnidadesPoblacion=new ArrayList<ControlUnidadPoblacion>();

		this.infoSesion=Utilidades.obtenerInfoSesion();
		
		// obtiene los datos para el combo de a�os
		this.a�os.putAll(Utilidades.ObtenerA�os(Integer.valueOf(Utilidades.A�oActual().intValue()-1),true));
		this.a�oSelected=Utilidades.A�oActual();
		this.setA�oActual(this.a�oSelected);

		// solo se obtienen las entidades autorizadas de forma directa
		// para los cuales si tiene derecho de confirmar las poblaciones
		this.entidades=ni.gob.minsa.malaria.reglas.Operacion.entidadesAutorizadas(this.infoSesion.getUsuarioId(), true);
		// selecciona la primera entidad
		if (this.entidades!=null && this.entidades.size()>0) {
			this.entidadSelectedId=this.entidades.get(0).getEntidadAdtvaId();
			this.entidadSelected=this.entidades.get(0);
			obtenerUnidadesSituacion();
		}

	}
	
	// -------------------------------------------- M�todos

	/**
	 *  Obtiene la lista de unidades con meta poblacional confirmada
	 *  para el a�o y ambito seleccionados y que adem�s se les ha
	 *  declarado que deben registrar meta poblacional.
	 */
	public void obtenerUnidadesSituacion() {
	
		this.controlUnidadesPoblacion.clear();
		this.existenUnidadesConfirmadas=false;
		
		if (this.entidadSelected==null) return;
		
		this.controlUnidadesPoblacion=controlUnidadPoblacionService.UnidadesConPoblacionConfirmada(this.a�oSelected,this.entidadSelected.getCodigo());
		if(this.controlUnidadesPoblacion!=null && this.controlUnidadesPoblacion.size()>0) {
			this.existenUnidadesConfirmadas=true;
		} 
		this.poblacionConfirmada=controlEntidadPoblacionService.ControlPorEntidad(this.a�oSelected, this.entidadSelected.getCodigo())==null?false:true;
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
	 * Elimina la confirmaci�n de los datos de poblaci�n de las comunidades
	 * atendidas por las unidades de salud gestionadas por la entidad administrativa 
	 */
	public void desbloquear(ActionEvent pEvento){

		if (!(this.controlUnidadesPoblacionSelected!=null && this.controlUnidadesPoblacionSelected.length>0)) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "No existen unidades de salud seleccionadas", ""));
			return;
		}
		
		InfoResultado oResultado=new InfoResultado();
		for(ControlUnidadPoblacion oControlUnidadPoblacion:this.controlUnidadesPoblacionSelected){
			oResultado=controlUnidadPoblacionService.Eliminar(oControlUnidadPoblacion.getControlUnidadPoblacionId());
			if (!oResultado.isOk()) {
				break;
			}
		}
			
		if (!oResultado.isOk()) {
			FacesMessage msg=Mensajes.enviarMensaje(oResultado);
			if (msg!=null){
				FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			}
		} else {
			obtenerUnidadesSituacion();
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "Los datos de poblaci�n para las unidades seleccionadas han sido desbloqueados", ""));
		}
	}

	// ---------------------------------------- Propiedades	

	/**
	 * @return entidades
	 */
	public List<EntidadAdtva> getEntidades() {
		return entidades;
	}

	/**
	 * @param entidades entidades 
	 */
	public void setEntidades(List<EntidadAdtva> entidades) {
		this.entidades = entidades;
	}

	/**
	 * @return entidadSelectedId
	 */
	public Long getEntidadSelectedId() {
		return entidadSelectedId;
	}

	/**
	 * @param entidadSelectedId entidadSelectedId 
	 */
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

	/**
	 * @param existenUnidadesConfirmadas existenUnidadesConfirmadas 
	 */
	public void setExistenUnidadesConfirmadas(boolean existenUnidadesConfirmadas) {
		this.existenUnidadesConfirmadas = existenUnidadesConfirmadas;
	}

	/**
	 * @return existenUnidadesConfirmadas
	 */
	public boolean isExistenUnidadesConfirmadas() {
		return existenUnidadesConfirmadas;
	}

	public void setControlUnidadesPoblacion(List<ControlUnidadPoblacion> controlUnidadesPoblacion) {
		this.controlUnidadesPoblacion = controlUnidadesPoblacion;
	}

	public List<ControlUnidadPoblacion> getControlUnidadesPoblacion() {
		return controlUnidadesPoblacion;
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

	public void setA�oActual(Integer a�oActual) {
		this.a�oActual = a�oActual;
	}

	public Integer getA�oActual() {
		return a�oActual;
	}

	public void setControlUnidadesPoblacionSelected(
			ControlUnidadPoblacion[] controlUnidadesPoblacionSelected) {
		this.controlUnidadesPoblacionSelected = controlUnidadesPoblacionSelected;
	}

	public ControlUnidadPoblacion[] getControlUnidadesPoblacionSelected() {
		return controlUnidadesPoblacionSelected;
	}

	/**
	 * 
	 * 
	 *
	 * <p>
	 * @author USER
	 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
	 * @version 1.0, &nbsp; 99/99/2010
	 * @since jdk1.6.0_21
	 */
	public class UnidadSituacion implements Serializable {
		
		private static final long serialVersionUID = 1L;

		private long controlUnidadMetaId;
		private Unidad unidad;
		
		public UnidadSituacion(long pControlUnidadMetaId, Unidad pUnidad) {
			this.controlUnidadMetaId=pControlUnidadMetaId;
			this.unidad=pUnidad;
		}

		/**
		 * @param unidad unidad 
		 */
		public void setUnidad(Unidad unidad) {
			this.unidad = unidad;
		}

		/**
		 * @return unidad
		 */
		public Unidad getUnidad() {
			return unidad;
		}

		/**
		 * @param controlUnidadMetaId controlUnidadMetaId 
		 */
		public void setControlUnidadMetaId(long controlUnidadMetaId) {
			this.controlUnidadMetaId = controlUnidadMetaId;
		}

		/**
		 * @return controlUnidadMetaId
		 */
		public long getControlUnidadMetaId() {
			return controlUnidadMetaId;
		}
		
		
	}
	
}
