// -----------------------------------------------
// ComunidadReferenciaBean.java
// -----------------------------------------------

package ni.gob.minsa.malaria.interfaz.poblacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.datos.estructura.UnidadDA;
import ni.gob.minsa.malaria.datos.general.CatalogoElementoDA;
import ni.gob.minsa.malaria.datos.general.MarcadorDA;
import ni.gob.minsa.malaria.datos.general.ParametroDA;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadDA;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadReferenciaDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.general.Marcador;
import ni.gob.minsa.malaria.modelo.general.Parametro;
import ni.gob.minsa.malaria.modelo.general.TipoSitioReferencia;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.ComunidadReferencia;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.servicios.general.MarcadorService;
import ni.gob.minsa.malaria.servicios.general.ParametroService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadReferenciaService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.soporte.NivelZoom;
import ni.gob.minsa.malaria.soporte.Utilidades;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * Servicio para la capa de presentación de la página 
 * poblacion/comunidadReferencia.xhtml
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 08/05/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class ComunidadReferenciaBean implements Serializable {

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

	private List<ComunidadReferencia> comunidadesReferencias;
	private ComunidadReferencia comunidadReferenciaSelected;
	private long comunidadReferenciaId;
	
	// comunidad seleccionada de la lista de sugerencias del control de autocompletar
	private Comunidad comunidadSelected;
	
	private String nombre;
	private List<TipoSitioReferencia> tiposSitiosReferencias;
	private long tipoSitioReferenciaSelectedId;
	private String observaciones;
	private BigDecimal longitud;
	private BigDecimal latitud;
	
	// comunidad seleccionada de la grilla de comunidades
	private Comunidad comunidadListaSelected;
	// lista de comunidades presentado en la grilla de la ventana modal
	private LazyDataModel<Comunidad> comunidades;
	// literal utilizada para filtrar las comunidades en la grilla
	private String filtroComunidad;
	private int numRegistros;
	
	// propiedades vinculadas al mapa
	
	// el centro del mapa será considerado como el centro del polígono
	// establecido por los diferentes puntos de longitud y latitud de los sitios
	// de referencia.  Si no existe ningún punto de referencia establecido
	// se colocará el mapa tomando como centro la longitud y latitud de la comunidad,
	// si ésta no se encuentra establecida, se tomará la longitud y latitud del
	// municipio donde se encuentra ubicada la comunidad.
	private String centroMapa;
	private MapModel mapaModelo;  
    private Marker marcadorMapa;
    private String urlMarcadorSitio;
    private int zoom;
	private NivelZoom nivelZoom;
    
    private String coordenadasPais;
	
	private static ComunidadReferenciaService comunidadReferenciaService=new ComunidadReferenciaDA();
	private static ComunidadService comunidadService = new ComunidadDA();
	private static UnidadService unidadService = new UnidadDA();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TipoSitioReferencia,Integer> tipoSitioReferenciaService=new CatalogoElementoDA(TipoSitioReferencia.class,"TipoSitioReferencia");
	private static MarcadorService marcadorService = new MarcadorDA();
	private static ParametroService parametroService = new ParametroDA();
	
	public ComunidadReferenciaBean() {
		
		this.nivelZoom=new NivelZoom();
		
		this.infoSesion=Utilidades.obtenerInfoSesion();
		this.entidades= new ArrayList<EntidadAdtva>();
		this.unidades= new ArrayList<Unidad>();
		this.comunidadesReferencias= new ArrayList<ComunidadReferencia>();

		this.entidadSelectedId=0;
		this.unidadSelectedId=0;

		this.urlMarcadorSitio="";
		InfoResultado oResultadoParametro=parametroService.Encontrar("MAPA_SITIO_REFERENCIA");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
			Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
			InfoResultado oResultadoMarcador=marcadorService.Encontrar(oParametro.getValor());
			if (oResultadoMarcador!=null && oResultadoMarcador.isOk()) {
				this.urlMarcadorSitio=((Marcador)oResultadoMarcador.getObjeto()).getUrl();
			}
		}

		this.coordenadasPais="0,0";
		oResultadoParametro=parametroService.Encontrar("COORDENADAS_PAIS");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
			Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
			this.coordenadasPais=oParametro.getValor();
		}

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

					numRegistros=comunidadService.ContarComunidadesPorUnidad(unidadSelectedId, filtroComunidad.trim(),null);
					comunidadesList=comunidadService.ComunidadesPorUnidad(unidadSelectedId, filtroComunidad.trim(),null,first, pageSize,numRegistros);

					if (!(comunidadesList!=null && comunidadesList.size()>0)) {
						FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "No se encontraron comunidades con los parámetros de búsqueda seleccionados","Verifique la literal de búsqueda, el municipio y/o sector");
						if (msg!=null)
							FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					
				} else {
				
					numRegistros=comunidadService.ContarComunidadesPorUnidad(unidadSelectedId,null,null);
					comunidadesList = comunidadService.ComunidadesPorUnidad(unidadSelectedId, null, null, first, pageSize,numRegistros);
				}
				return comunidadesList;
			}

		};
		
		this.comunidades.setRowCount(numRegistros);

		iniciarDetalle();

	}
	
	public void iniciarDetalle() {
		
		this.comunidadReferenciaSelected=null;
		this.comunidadReferenciaId=0;
		this.comunidadSelected=null;
		this.nombre=null;
		this.observaciones=null;
		this.longitud=null;
		this.latitud=null;
		this.tipoSitioReferenciaSelectedId=0;
		this.setTiposSitiosReferencias(tipoSitioReferenciaService.ListarActivos());
		this.filtroComunidad="";
		
	}
	
	public List<Comunidad> completarComunidad(String query) {
		
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades=comunidadService.ComunidadesPorUnidadYNombre(this.unidadSelectedId,query);
		
		return oComunidades;

	}
	
	public void buscarComunidad() {
		
		if (this.filtroComunidad.trim().length()>0 && this.filtroComunidad.trim().length()<3) { 

			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "La búsqueda solo es permitida para un número superior a 3 caracteres","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		iniciarComunidades();
		
	}
	
	public void cambiarUnidad() {
		
		InfoResultado oResultado=unidadService.Encontrar(this.unidadSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}

		Unidad oUnidad=(Unidad)oResultado.getObjeto();
		this.unidadSelected=oUnidad;
		
		obtenerSitiosReferencias();
		iniciarDetalle();
		
	}
	
	public void iniciarComunidades() {
		
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmComunidadReferencia:grdComunidades");
		this.comunidades.load(0, 50, null, null, null);
		this.comunidades.setRowCount(numRegistros);
		dataTable.loadLazyData();
		if (dataTable.getRowCount() <= dataTable.getPage()) {
			dataTable.setFirst(0);
		} else {
			dataTable.setFirst(dataTable.getPage());
		}
		this.comunidadListaSelected=null;
		
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
	 * Evento que se ejecuta cuando el usuario selecciona un sitio de referencia
	 * de la grilla de sitios de referencia existentes.  Traslada todos los
	 * valores del objeto seleccionado a los controles del panel de detalle 
	 */
	public void onComunidadReferenciaSelected(SelectEvent iEvento) { 
		
		this.comunidadReferenciaId=this.comunidadReferenciaSelected.getComunidadReferenciaId();
		this.nombre=this.comunidadReferenciaSelected.getNombre();
		this.tipoSitioReferenciaSelectedId=this.comunidadReferenciaSelected.getTipoSitio().getCatalogoId();
		
		this.tiposSitiosReferencias=tipoSitioReferenciaService.ListarActivos(this.comunidadReferenciaSelected.getTipoSitio().getCodigo());
		
		this.observaciones=this.comunidadReferenciaSelected.getObservaciones();
		this.longitud=this.comunidadReferenciaSelected.getLongitud();
		this.latitud=this.comunidadReferenciaSelected.getLatitud();
		
		this.comunidadSelected=this.comunidadReferenciaSelected.getComunidad();
		
		FacesContext context = FacesContext.getCurrentInstance( ); 
		UIComponent componentDetalle = null; 
		UIViewRoot root = context.getViewRoot( ); 
		componentDetalle = (UIComponent) root.findComponent("frmComunidadReferencia:panDetalle");
		for (UIComponent uic:componentDetalle.getChildren()) {
			if (uic instanceof EditableValueHolder) {   
				EditableValueHolder evh=(EditableValueHolder)uic;   
				evh.resetValue();   
			}
		}
		
	}
	
	public void onComunidadReferenciaUnSelected(UnselectEvent iEvento) {
		
		iniciarDetalle();
		
	}

	
	/**
	 * Obtiene las unidades de salud con autorización explícita
	 * asociadas a una entidad administrativa (ímplicita) 
	 */
	public void obtenerUnidades() {

		this.unidadSelected=null;
		this.unidadSelectedId=0;
		
		this.unidades=ni.gob.minsa.malaria.reglas.Operacion.unidadesAutorizadasPorEntidad(this.infoSesion.getUsuarioId(), this.entidadSelectedId, 0,true,null);
		if ((this.unidades!=null) && (this.unidades.size()>0)) {
			this.unidadSelectedId=this.unidades.get(0).getUnidadId();
			this.unidadSelected=this.unidades.get(0);
			
			obtenerSitiosReferencias();
		}
		
		iniciarDetalle();
	}
	
	public void obtenerSitiosReferencias() {
		
		this.comunidadesReferencias.clear();
		this.comunidadesReferencias=comunidadReferenciaService.SitiosReferenciaPorUnidad(this.unidadSelectedId);
	}

	/**
	 * Evento que se ejecuta cuando el usuario hace clic sobre el botón
	 * aceptar de la ventana modal, dando por seleccionada una comunidad 
	 */
	public void seleccionarComunidad(ActionEvent pEvento) {
		
		this.comunidadSelected=this.comunidadListaSelected;

	}
	
	public void generarMapa(ActionEvent pEvento) {
		
		this.mapaModelo = new DefaultMapModel();  
        BigDecimal iSumaLatitudes=BigDecimal.ZERO;
        BigDecimal iSumaLongitudes=BigDecimal.ZERO;
        int iNumSitios=0;
        this.centroMapa="0,0";
        this.zoom=nivelZoom.PAIS;
        
		for (ComunidadReferencia oComunidadReferencia:this.comunidadesReferencias) {
			if (oComunidadReferencia.getLatitud()!=null && oComunidadReferencia.getLongitud()!=null) {
				iSumaLatitudes=iSumaLatitudes.add(oComunidadReferencia.getLatitud());
				iSumaLongitudes=iSumaLongitudes.add(oComunidadReferencia.getLongitud());
				iNumSitios++;
				
				LatLng iCoordenada = new LatLng(oComunidadReferencia.getLatitud().doubleValue(),oComunidadReferencia.getLongitud().doubleValue());
				Marker oMarker = new Marker(iCoordenada, oComunidadReferencia.getNombre());
				if (!this.urlMarcadorSitio.isEmpty()) {
					oMarker.setIcon(this.urlMarcadorSitio);
				}
				this.mapaModelo.addOverlay(oMarker);
			}
		}
		
		if (iNumSitios!=0) {
			this.centroMapa=iSumaLatitudes.divide(new BigDecimal(iNumSitios),6,RoundingMode.HALF_UP).toPlainString() + "," + iSumaLongitudes.divide(new BigDecimal(iNumSitios),6,RoundingMode.HALF_UP).toPlainString();
			this.zoom=nivelZoom.MUNICIPIO;
		}
		
		if (this.unidadSelected.getLatitud()!=null && this.unidadSelected.getLongitud()!=null) {

			if (iNumSitios==0) {
				this.centroMapa=this.unidadSelected.getLatitud().toPlainString() + "," + this.unidadSelected.getLongitud().toPlainString();
			}
			
			LatLng iCoordenada = new LatLng(this.unidadSelected.getLatitud().doubleValue(),this.unidadSelected.getLongitud().doubleValue());
			Marker oMarker = new Marker(iCoordenada, this.unidadSelected.getNombre());
			if (this.unidadSelected.getTipoUnidad().getMarcador()!=null) {
				oMarker.setIcon(this.unidadSelected.getTipoUnidad().getMarcador().getUrl());
			}
			this.mapaModelo.addOverlay(oMarker);

			this.zoom=nivelZoom.UNIDAD;
			return;
		}
		
		if (this.unidadSelected.getMunicipio().getLatitud()!=null && this.unidadSelected.getMunicipio().getLongitud()!=null) {
			this.centroMapa=this.unidadSelected.getMunicipio().getLatitud().toPlainString() + "," + this.unidadSelected.getMunicipio().getLongitud().toPlainString();
			this.zoom=nivelZoom.MUNICIPIO;
		} else {
			if (this.unidadSelected.getMunicipio().getDepartamento().getLatitud()!=null && this.unidadSelected.getMunicipio().getDepartamento().getLongitud()!=null) {
				this.centroMapa=this.unidadSelected.getMunicipio().getDepartamento().getLatitud().toPlainString() + "," + this.unidadSelected.getMunicipio().getDepartamento().getLongitud().toPlainString();
				this.zoom=nivelZoom.DEPARTAMENTO;
			} else {
				this.centroMapa=this.coordenadasPais;
			}
			
		}
		
	}
	
	/**
	 * Se ejecuta cuando el usuario pulsa en el botón agregar
	 * y consiste en la inicialización de las diferentes propiedades
	 * para colocar el estado del formulario en modo de agregar
	 */
	public void agregar(ActionEvent pEvento) {
		
		this.comunidadReferenciaSelected=null;
		iniciarDetalle();
		
	}
	
	/**
	 * Se ejecuta cuando el usuario pulsa en el botón guardar.
	 * Este proceso incluye guardar un nuevo registro o actualizar
	 * un registro existente
	 */
	public void guardar(ActionEvent pEvento) {
		
		// verifica si se ha seleccionado la comunidad, en caso de ser registro nuevo
		if (this.comunidadReferenciaId==0) {
			if (this.comunidadSelected==null) {
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La declaración de la comunidad a la cual pertenece el sitio de referencia es requerida","Seleccione la comunidad de la lista");
				if (msg!=null)
					FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
		}

		if (this.tipoSitioReferenciaSelectedId==0) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Seleccione un tipo para el sitio de referencia","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		InfoResultado oResultado = new InfoResultado();
		ComunidadReferencia oComunidadReferencia = new ComunidadReferencia();
		oComunidadReferencia.setNombre(this.nombre);
		oComunidadReferencia.setLatitud(this.latitud);
		oComunidadReferencia.setLongitud(this.longitud);
		if (this.observaciones==null || this.observaciones.isEmpty()) {
			oComunidadReferencia.setObservaciones(null);
		} else {
			oComunidadReferencia.setObservaciones(this.observaciones);
		}

		oComunidadReferencia.setComunidad(this.comunidadSelected);
		oComunidadReferencia.setTipoSitio((TipoSitioReferencia)tipoSitioReferenciaService.Encontrar(this.tipoSitioReferenciaSelectedId).getObjeto());
		
		if (this.comunidadReferenciaId!=0) {
			oComunidadReferencia.setComunidadReferenciaId(this.comunidadReferenciaId);
			oResultado=comunidadReferenciaService.Guardar(oComunidadReferencia);
		} else {
			oResultado=comunidadReferenciaService.Agregar(oComunidadReferencia);
		}
		
		if (oResultado.isOk()){
			obtenerSitiosReferencias();
			iniciarDetalle();
			oResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
		
	}
	
	public void eliminar(ActionEvent pEvento){

		if (this.comunidadReferenciaId==0) return;
		
		InfoResultado oResultado = new InfoResultado();
		oResultado=comunidadReferenciaService.Eliminar(this.comunidadReferenciaId);
		
		if (oResultado.isOk()){
			iniciarDetalle();
			obtenerSitiosReferencias();
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

	public List<ComunidadReferencia> getComunidadesReferencias() {
		return comunidadesReferencias;
	}

	public void setComunidadesReferencias(
			List<ComunidadReferencia> comunidadesReferencias) {
		this.comunidadesReferencias = comunidadesReferencias;
	}

	public ComunidadReferencia getComunidadReferenciaSelected() {
		return comunidadReferenciaSelected;
	}

	public void setComunidadReferenciaSelected(
			ComunidadReferencia comunidadReferenciaSelected) {
		this.comunidadReferenciaSelected = comunidadReferenciaSelected;
	}

	public long getComunidadReferenciaId() {
		return comunidadReferenciaId;
	}

	public void setComunidadReferenciaId(long comunidadReferenciaId) {
		this.comunidadReferenciaId = comunidadReferenciaId;
	}

	public void setTiposSitiosReferencias(List<TipoSitioReferencia> tiposSitiosReferencias) {
		this.tiposSitiosReferencias = tiposSitiosReferencias;
	}

	public List<TipoSitioReferencia> getTiposSitiosReferencias() {
		return tiposSitiosReferencias;
	}

	public void setComunidadSelected(Comunidad comunidadSelected) {
		this.comunidadSelected = comunidadSelected;
	}

	public Comunidad getComunidadSelected() {
		return comunidadSelected;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setTipoSitioReferenciaSelectedId(
			long tipoSitioReferenciaSelectedId) {
		this.tipoSitioReferenciaSelectedId = tipoSitioReferenciaSelectedId;
	}

	public long getTipoSitioReferenciaSelectedId() {
		return tipoSitioReferenciaSelectedId;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setComunidadListaSelected(Comunidad comunidadListaSelected) {
		this.comunidadListaSelected = comunidadListaSelected;
	}

	public Comunidad getComunidadListaSelected() {
		return comunidadListaSelected;
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

	public void setCentroMapa(String centroMapa) {
		this.centroMapa = centroMapa;
	}

	public String getCentroMapa() {
		return centroMapa;
	}

	public void setMapaModelo(MapModel mapaModelo) {
		this.mapaModelo = mapaModelo;
	}

	public MapModel getMapaModelo() {
		return mapaModelo;
	}

	public void setMarcadorMapa(Marker marcadorMapa) {
		this.marcadorMapa = marcadorMapa;
	}

	public Marker getMarcadorMapa() {
		return marcadorMapa;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public int getZoom() {
		return zoom;
	}

	public void setCoordenadasPais(String coordenadasPais) {
		this.coordenadasPais = coordenadasPais;
	}

	public String getCoordenadasPais() {
		return coordenadasPais;
	}
}
