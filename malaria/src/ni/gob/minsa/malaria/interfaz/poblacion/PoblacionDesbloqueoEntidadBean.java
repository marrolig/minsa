// -----------------------------------------------
// PoblacionDesbloqueoEntidadBean.java
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
import ni.gob.minsa.malaria.datos.poblacion.ControlEntidadPoblacionDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.poblacion.ControlEntidadPoblacion;
import ni.gob.minsa.malaria.servicios.poblacion.ControlEntidadPoblacionService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;


/**
 * Servicio para la capa de presentaci�n de la p�gina
 * <b>/poblacion/poblacionDesbloqueoEntidad.xhtml</b>
 *
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 16/05/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class PoblacionDesbloqueoEntidadBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private InfoSesion infoSesion;

	private List<EntidadAdtva> entidades;
	// entidad seleccionada
	private Long entidadSelectedId;
	private EntidadAdtva entidadSelected;

	private boolean existenEntidadesConfirmadas;
	
	// mapeo para poblar el combo de a�os
	private Map<Integer,Integer> a�os = new HashMap<Integer,Integer>();
	// a�o seleccionado
	private Integer a�oSelected;
	private Integer a�oActual;
	
	// lista de objetos de entidades para poblar la grilla
	private List<ControlEntidadPoblacion> controlEntidadesPoblacion;

	// objetos seleccionados desde la grilla
	private ControlEntidadPoblacion[] controlEntidadesPoblacionSelected;

	private static ControlEntidadPoblacionService controlEntidadPoblacionService = new ControlEntidadPoblacionDA();
	
	// ---------------------------------------- Constructor
	
	public PoblacionDesbloqueoEntidadBean() {
		
		this.entidades=new ArrayList<EntidadAdtva>();
		this.setControlEntidadesPoblacion(new ArrayList<ControlEntidadPoblacion>());

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
			this.setEntidadSelectedId(this.entidades.get(0).getEntidadAdtvaId());
			this.setEntidadSelected(this.entidades.get(0));
			obtenerEntidadesSituacion();
		}

	}
	
	// -------------------------------------------- M�todos

	/**
	 *  Obtiene la lista de las entidades administrativas
	 *  con poblacion de comunidades confirmada
	 */
	public void obtenerEntidadesSituacion() {
	
		this.controlEntidadesPoblacion.clear();
		this.existenEntidadesConfirmadas=false;
		
		this.controlEntidadesPoblacion=controlEntidadPoblacionService.EntidadesConPoblacionConfirmada(this.a�oSelected);
		if(this.controlEntidadesPoblacion!=null && this.controlEntidadesPoblacion.size()>0) {
			this.existenEntidadesConfirmadas=true;
		} 
	} 

	/**
	 * Elimina la confirmaci�n de los datos de poblaci�n asociados 
	 * a la entidad administrativa
	 */
	public void desbloquear(ActionEvent pEvento){

		if (!(this.controlEntidadesPoblacionSelected!=null && this.controlEntidadesPoblacionSelected.length>0)) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "No existen entidades administrativas seleccionadas", ""));
			return;
		}

		InfoResultado oResultado=new InfoResultado();
		for(ControlEntidadPoblacion oControlEntidadPoblacion:this.controlEntidadesPoblacionSelected){
			oResultado=controlEntidadPoblacionService.Eliminar(oControlEntidadPoblacion.getControlEntidadPoblacionId());
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
			obtenerEntidadesSituacion();
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "Los datos de poblaci�n para las entidades seleccionadas han sido desbloqueados", ""));
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

	public void setExistenEntidadesConfirmadas(boolean existenEntidadesConfirmadas) {
		this.existenEntidadesConfirmadas = existenEntidadesConfirmadas;
	}

	public boolean isExistenEntidadesConfirmadas() {
		return existenEntidadesConfirmadas;
	}

	public void setA�oActual(Integer a�oActual) {
		this.a�oActual = a�oActual;
	}

	public Integer getA�oActual() {
		return a�oActual;
	}

	public void setControlEntidadesPoblacion(
			List<ControlEntidadPoblacion> controlEntidadesPoblacion) {
		this.controlEntidadesPoblacion = controlEntidadesPoblacion;
	}

	public List<ControlEntidadPoblacion> getControlEntidadesPoblacion() {
		return controlEntidadesPoblacion;
	}

	public void setControlEntidadesPoblacionSelected(
			ControlEntidadPoblacion[] controlEntidadesPoblacionSelected) {
		this.controlEntidadesPoblacionSelected = controlEntidadesPoblacionSelected;
	}

	public ControlEntidadPoblacion[] getControlEntidadesPoblacionSelected() {
		return controlEntidadesPoblacionSelected;
	}

	public void setEntidadSelectedId(Long entidadSelectedId) {
		this.entidadSelectedId = entidadSelectedId;
	}

	public Long getEntidadSelectedId() {
		return entidadSelectedId;
	}

	public void setEntidadSelected(EntidadAdtva entidadSelected) {
		this.entidadSelected = entidadSelected;
	}

	public EntidadAdtva getEntidadSelected() {
		return entidadSelected;
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
	public class EntidadSituacion implements Serializable {
		
		private static final long serialVersionUID = 1L;

		private long controlEntidadMetaId;
		private EntidadAdtva entidad;
		private int situacion;
		private boolean selected;
		
		public EntidadSituacion(long pControlEntidadMetaId, EntidadAdtva pEntidad, int pSituacion, boolean pSelected) {
			this.controlEntidadMetaId=pControlEntidadMetaId;
			this.entidad=pEntidad;
			this.setSelected(pSelected);
			this.setSituacion(pSituacion);
		}

		/**
		 * @param entidad Objeto EntidadAdtva
		 */
		public void setEntidad(EntidadAdtva entidad) {
			this.entidad = entidad;
		}

		/**
		 * @return entidad Objeto EntidadAdtva
		 */
		public EntidadAdtva getEntidad() {
			return this.entidad;
		}

		/**
		 * @param controlEntidadMetaId controlEntidadMetaId 
		 */
		public void setControlEntidadMetaId(long controlEntidadMetaId) {
			this.controlEntidadMetaId = controlEntidadMetaId;
		}

		/**
		 * @return controlEntidadMetaId
		 */
		public long getControlEntidadMetaId() {
			return controlEntidadMetaId;
		}

		/**
		 * @param situacion situacion 
		 */
		public void setSituacion(int situacion) {
			this.situacion = situacion;
		}

		/**
		 * @return situacion
		 */
		public int getSituacion() {
			return situacion;
		}

		/**
		 * @param selected selected 
		 */
		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		/**
		 * @return selected
		 */
		public boolean isSelected() {
			return selected;
		}
		
		
	}

	public Map<Integer, Integer> getA�os() {
		return a�os;
	}

	public void setA�os(Map<Integer, Integer> a�os) {
		this.a�os = a�os;
	}

	public Integer getA�oSelected() {
		return a�oSelected;
	}

	public void setA�oSelected(Integer a�oSelected) {
		this.a�oSelected = a�oSelected;
	}
	
}
