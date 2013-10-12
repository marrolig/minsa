// -----------------------------------------------
// ManzanaBean.java
// -----------------------------------------------

package ni.gob.minsa.malaria.interfaz.poblacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadDA;
import ni.gob.minsa.malaria.datos.poblacion.ManzanaDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.Manzana;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.servicios.poblacion.ManzanaService;
import ni.gob.minsa.malaria.soporte.TipoArea;
import ni.gob.minsa.malaria.soporte.Utilidades;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * Servicio para la capa de presentación de la página 
 * poblacion/manzana.xhtml
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 22/05/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class ManzanaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	protected InfoSesion infoSesion;
	
	// objetos para poblar el combo de entidades administrativas
	private List<EntidadAdtva> entidades;
	
	// identificador de la entidad seleccionada
	private long entidadSelectedId;
	
	// objetos para poblar el combo de unidades de salud, unidad seleccionada e identificador
	private List<Unidad> unidades;
	private Unidad unidadSelected;
	private long unidadSelectedId;

	private List<Manzana> manzanas;
	private Manzana manzanaSelected;
	private long manzanaId;
	
	private String codigoComunidad;
	private String consecutivo;
	private String observaciones;
	private BigDecimal viviendas;
	private BigDecimal poblacion;
	private boolean pasivo;
	
	// comunidad seleccionada de la grilla de comunidades
	private Comunidad comunidadSelected;
	// lista de comunidades presentado en la grilla de la ventana modal
	private LazyDataModel<Comunidad> comunidades;
	// literal utilizada para filtrar las comunidades en la grilla
	private String filtroComunidad;
	private int numRegistros;
	
	private static ManzanaService manzanaService=new ManzanaDA();
	private static ComunidadService comunidadService = new ComunidadDA();
	
	public ManzanaBean() {
		
		this.infoSesion=Utilidades.obtenerInfoSesion();
		
		this.entidades= new ArrayList<EntidadAdtva>();
		this.unidades= new ArrayList<Unidad>();
		this.manzanas= new ArrayList<Manzana>();

		this.entidadSelectedId=0;
		this.unidadSelectedId=0;
		
		this.filtroComunidad="";

		// obtiene los datos para el combo de entidades autorizadas
		// únicamente se podrán seleccionar aquellas entidades administrativas
		// asociadas a las unidades de salud con autorización explícita
		this.entidades=ni.gob.minsa.malaria.reglas.Operacion.entidadesAutorizadas(this.infoSesion.getUsuarioId(),false);
		if ((this.entidades!=null) && (this.entidades.size()>0)) {
			this.entidadSelectedId=this.entidades.get(0).getEntidadAdtvaId();
		}

		// si se ha encontrado una entidad administrativa, se seleccionará por omisión
		// la primera unidad de salud asociada a dicha unidad de salud
		if (this.entidadSelectedId!=0) {
			obtenerUnidades();
		}
		
		this.comunidades = new LazyDataModel<Comunidad>() {

			private static final long serialVersionUID = 1L;

			@Override
		    public List<Comunidad> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
			
				List<Comunidad> comunidadesList=null;
			
				// verifica que exista un filtro para la búsqueda de las comunidades
				if ((filtroComunidad!=null) && !filtroComunidad.trim().isEmpty() && filtroComunidad.trim().length()>=3) {

					numRegistros=comunidadService.ContarComunidadesPorUnidad(unidadSelectedId, filtroComunidad.trim(),TipoArea.URBANO.getCodigo());
					comunidadesList=comunidadService.ComunidadesPorUnidad(unidadSelectedId, filtroComunidad.trim(), TipoArea.URBANO.getCodigo(), first, pageSize,numRegistros);

					if (!(comunidadesList!=null && comunidadesList.size()>0)) {
						FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "No se encontraron comunidades con los parámetros de búsqueda seleccionados","Verifique la literal de búsqueda, el municipio y/o sector");
						if (msg!=null)
							FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					
				} else {
				
					numRegistros=comunidadService.ContarComunidadesPorUnidad(unidadSelectedId,null,TipoArea.URBANO.getCodigo());
					comunidadesList = comunidadService.ComunidadesPorUnidad(unidadSelectedId, null, TipoArea.URBANO.getCodigo(), first, pageSize,numRegistros);
				}
				return comunidadesList;
			}

		};
		
		this.comunidades.setRowCount(numRegistros);

		iniciarDetalle();

	}
	
	public void iniciarDetalle() {
		
		this.manzanaSelected=null;
		this.manzanaId=0;
		this.consecutivo=null;
		this.pasivo=false;
		this.observaciones=null;
		this.poblacion=null;
		this.viviendas=null;
		this.filtroComunidad="";
		
	}
	
	public List<Comunidad> completarComunidad(String query) {
		
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades=comunidadService.ComunidadesPorUnidadYNombre(this.unidadSelectedId,query);
		
		return oComunidades;

	}
	
	public void buscarComunidad() {
		
		if (this.filtroComunidad!=null && this.filtroComunidad.trim().length()>0 && this.filtroComunidad.trim().length()<3) { 

			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "La búsqueda solo es permitida para un número superior a 3 caracteres","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		iniciarComunidades();
		
	}
	
	public void cambiarUnidad() {
		
		this.filtroComunidad="";
		iniciarComunidades();
		iniciarDetalle();
		
	}
	
	public void iniciarComunidades() {
		
		this.manzanas.clear();
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmManzana:grdComunidades");
		this.comunidades.load(0, 50, null, null, null);
		this.comunidades.setRowCount(numRegistros);
		dataTable.loadLazyData();
		if (dataTable.getRowCount() <= dataTable.getPage()) {
			dataTable.setFirst(0);
		} else {
			dataTable.setFirst(dataTable.getPage());
		}
		this.comunidadSelected=null;
		this.codigoComunidad=null;
		
	}
	
	/**
	 * Quita el filtro de la busqueda de comunidades, obtiene todas las comunidades
	 * vinculadas a la unidad de salud seleccionada 
	 */
	public void quitarFiltro() {
		
		this.filtroComunidad="";
		iniciarComunidades();
		
	}

	/**
	 * Evento que se ejecuta cuando el usuario selecciona una comunidad
	 * de la grilla de comunidades vinculadas a la unidad de salud 
	 */
	public void onComunidadSelected(SelectEvent iEvento) { 
		
		obtenerManzanas();
		iniciarDetalle();
		this.codigoComunidad=this.comunidadSelected.getCodigo();
	}
	
	/**
	 * Evento que se ejecuta cuando el usuario deselecciona una comunidad
	 * de la grilla de comunidades vinculadas a la unidad de salud 
	 */
	public void onComunidadUnSelected(UnselectEvent iEvento) { 
		
		this.comunidadSelected=null;
		this.codigoComunidad=null;
		this.manzanas.clear();
		iniciarDetalle();
	}
	
	/**
	 * Evento que se ejecuta cuando el usuario selecciona una manzana
	 * de la grilla de manzanas existentes.  Traslada todos los
	 * valores del objeto seleccionado a los controles del panel de detalle 
	 */
	public void onManzanaSelected(SelectEvent iEvento) { 
		
		this.manzanaId=this.manzanaSelected.getManzanaId();
		this.pasivo=this.manzanaSelected.getPasivo().equals(new BigDecimal(0))?false:true;
		this.observaciones=this.manzanaSelected.getObservaciones();
		this.poblacion=this.manzanaSelected.getPoblacion();
		this.viviendas=this.manzanaSelected.getViviendas();
		this.consecutivo=this.manzanaSelected.getCodigo().substring(9, 12);
		
		FacesContext context = FacesContext.getCurrentInstance( ); 
		UIComponent componentDetalle = null; 
		UIViewRoot root = context.getViewRoot( ); 
		componentDetalle = (UIComponent) root.findComponent("frmManzana:panDetalle");
		for (UIComponent uic:componentDetalle.getChildren()) {
			if (uic instanceof EditableValueHolder) {   
				EditableValueHolder evh=(EditableValueHolder)uic;   
				evh.resetValue();   
			}
		}
		
	}
	
	public void onManzanaUnSelected(UnselectEvent iEvento) {
		
		iniciarDetalle();
		
	}

	
	/**
	 * Obtiene las unidades de salud con autorización explícita
	 * asociadas a una entidad administrativa (ímplicita) 
	 */
	public void obtenerUnidades() {

		this.filtroComunidad="";
		this.unidadSelected=null;
		this.unidadSelectedId=0;
		
		this.unidades=ni.gob.minsa.malaria.reglas.Operacion.unidadesAutorizadasPorEntidad(this.infoSesion.getUsuarioId(), this.entidadSelectedId, 0,true,null);
		if ((this.unidades!=null) && (this.unidades.size()>0)) {
			this.unidadSelectedId=this.unidades.get(0).getUnidadId();
			this.unidadSelected=this.unidades.get(0);
		}
	}
	
	/**
	 * Se ejecuta al momento de efectuarse un cambio de una entidad administrativa
	 * en el interfaz del usuario
	 */
	public void cambiarEntidad() {
		
		obtenerUnidades();
		iniciarComunidades();
		iniciarDetalle();
		
	}
	
	public void obtenerManzanas() {
		
		this.manzanas.clear();
		this.manzanas=manzanaService.ManzanasPorComunidad(this.comunidadSelected.getCodigo(), false,null);
	}

	/**
	 * Se ejecuta cuando el usuario pulsa en el botón agregar
	 * y consiste en la inicialización de las diferentes propiedades
	 * para colocar el estado del formulario en modo de agregar
	 */
	public void agregar(ActionEvent pEvento) {
		
		this.manzanaSelected=null;
		iniciarDetalle();
		
	}
	
	/**
	 * Se ejecuta cuando el usuario pulsa en el botón guardar.
	 * Este proceso incluye guardar un nuevo registro o actualizar
	 * un registro existente
	 */
	public void guardar(ActionEvent pEvento) {
		
		InfoResultado oResultado = new InfoResultado();
		Manzana oManzana = new Manzana();
		oManzana.setPasivo(this.pasivo?(new BigDecimal(1)):(new BigDecimal(0)));
		oManzana.setPoblacion(this.poblacion);
		oManzana.setViviendas(this.viviendas);
		if (this.observaciones==null || this.observaciones.isEmpty()) {
			oManzana.setObservaciones(null);
		} else {
			oManzana.setObservaciones(this.observaciones);
		}

		oManzana.setComunidad(this.comunidadSelected);
		
		oManzana.setCodigo(this.codigoComunidad + String.format("%03d", Integer.valueOf(this.consecutivo)));
		
		if (this.manzanaId!=0) {
			oManzana.setManzanaId(this.manzanaId);
			oResultado=manzanaService.Guardar(oManzana);
		} else {
			oManzana.setUsuarioRegistro(this.infoSesion.getUsername());
			oManzana.setFechaRegistro(Calendar.getInstance().getTime());
			oResultado=manzanaService.Agregar(oManzana);
		}
		
		if (oResultado.isOk()){
			obtenerManzanas();
			iniciarDetalle();
			oResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
		
	}
	
	public void eliminar(ActionEvent pEvento){

		if (this.manzanaId==0) return;
		
		InfoResultado oResultado = new InfoResultado();
		oResultado=manzanaService.Eliminar(this.manzanaId);
		
		if (oResultado.isOk()){
			iniciarDetalle();
			obtenerManzanas();
			oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);

	}
	
	public List<EntidadAdtva> getEntidades() {
		return entidades;
	}

	public void setEntidades(List<EntidadAdtva> entidades) {
		this.entidades = entidades;
	}

	public long getEntidadSelectedId() {
		return entidadSelectedId;
	}

	public void setEntidadSelectedId(long entidadSelectedId) {
		this.entidadSelectedId = entidadSelectedId;
	}

	public List<Unidad> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<Unidad> unidades) {
		this.unidades = unidades;
	}

	public Unidad getUnidadSelected() {
		return unidadSelected;
	}

	public void setUnidadSelected(Unidad unidadSelected) {
		this.unidadSelected = unidadSelected;
	}

	public long getUnidadSelectedId() {
		return unidadSelectedId;
	}

	public void setUnidadSelectedId(long unidadSelectedId) {
		this.unidadSelectedId = unidadSelectedId;
	}

	public List<Manzana> getManzanas() {
		return manzanas;
	}

	public void setManzanas(
			List<Manzana> manzanas) {
		this.manzanas = manzanas;
	}

	public Manzana getManzanaSelected() {
		return manzanaSelected;
	}

	public void setManzanaSelected(
			Manzana manzanaSelected) {
		this.manzanaSelected = manzanaSelected;
	}

	public long getManzanaId() {
		return manzanaId;
	}

	public void setManzanaId(long manzanaId) {
		this.manzanaId = manzanaId;
	}

	public void setComunidadSelected(Comunidad comunidadSelected) {
		this.comunidadSelected = comunidadSelected;
	}

	public Comunidad getComunidadSelected() {
		return comunidadSelected;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setFiltroComunidad(String filtroComunidad) {
		this.filtroComunidad = filtroComunidad;
	}

	public String getFiltroComunidad() {
		return filtroComunidad;
	}

	public LazyDataModel<Comunidad> getComunidades() {
		return comunidades;
	}

	public void setComunidades(LazyDataModel<Comunidad> comunidades) {
		this.comunidades = comunidades;
	}

	public int getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}

	public String getCodigoComunidad() {
		return codigoComunidad;
	}

	public void setCodigoComunidad(String codigoComunidad) {
		this.codigoComunidad = codigoComunidad;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public BigDecimal getViviendas() {
		return viviendas;
	}

	public void setViviendas(BigDecimal viviendas) {
		this.viviendas = viviendas;
	}

	public BigDecimal getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(BigDecimal poblacion) {
		this.poblacion = poblacion;
	}

	public boolean isPasivo() {
		return pasivo;
	}

	public void setPasivo(boolean pasivo) {
		this.pasivo = pasivo;
	}
}
