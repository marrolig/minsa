// -----------------------------------------------
// ViviendaBean.java
// -----------------------------------------------

package ni.gob.minsa.malaria.interfaz.poblacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
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
import org.primefaces.context.RequestContext;
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
import ni.gob.minsa.malaria.datos.poblacion.ManzanaDA;
import ni.gob.minsa.malaria.datos.poblacion.ViviendaDA;
import ni.gob.minsa.malaria.datos.poblacion.ViviendaEncuestaDA;
import ni.gob.minsa.malaria.datos.poblacion.ViviendaParametroDA;
import ni.gob.minsa.malaria.datos.poblacion.ViviendaRiesgoDA;
import ni.gob.minsa.malaria.datos.vigilancia.FactorRiesgoDA;
import ni.gob.minsa.malaria.datos.vigilancia.ParametroEpidemiologicoDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.general.CondicionOcupacion;
import ni.gob.minsa.malaria.modelo.general.Marcador;
import ni.gob.minsa.malaria.modelo.general.NivelRiesgo;
import ni.gob.minsa.malaria.modelo.general.Parametro;
import ni.gob.minsa.malaria.modelo.general.TipoVivienda;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.Manzana;
import ni.gob.minsa.malaria.modelo.poblacion.Vivienda;
import ni.gob.minsa.malaria.modelo.poblacion.ViviendaEncuesta;
import ni.gob.minsa.malaria.modelo.poblacion.ViviendaManzana;
import ni.gob.minsa.malaria.modelo.poblacion.ViviendaParametroEpidemiologico;
import ni.gob.minsa.malaria.modelo.poblacion.ViviendaRiesgo;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.ViviendaUltimaEncuesta;
import ni.gob.minsa.malaria.modelo.vigilancia.FactorRiesgo;
import ni.gob.minsa.malaria.modelo.vigilancia.ParametroEpidemiologico;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.servicios.general.MarcadorService;
import ni.gob.minsa.malaria.servicios.general.ParametroService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.servicios.poblacion.ManzanaService;
import ni.gob.minsa.malaria.servicios.poblacion.ViviendaEncuestaService;
import ni.gob.minsa.malaria.servicios.poblacion.ViviendaParametroService;
import ni.gob.minsa.malaria.servicios.poblacion.ViviendaRiesgoService;
import ni.gob.minsa.malaria.servicios.poblacion.ViviendaService;
import ni.gob.minsa.malaria.servicios.vigilancia.FactorRiesgoService;
import ni.gob.minsa.malaria.servicios.vigilancia.ParametroEpidemiologicoService;
import ni.gob.minsa.malaria.soporte.NivelZoom;
import ni.gob.minsa.malaria.soporte.TipoArea;
import ni.gob.minsa.malaria.soporte.Utilidades;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * Servicio para la capa de presentación de la página 
 * poblacion/vivienda.xhtml
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 28/05/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class ViviendaBean implements Serializable {

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

	// objetos vinculados a la grilla de viviendas (capa 1)
	private LazyDataModel<ViviendaUltimaEncuesta> viviendasUltimaEncuesta;
	private ViviendaUltimaEncuesta viviendaUltimaEncuestaSelected;
	private long viviendaUltimaEncuestaId;
	
	// objetos vinculados al combo de tipo de vivienda (capa 2)
	private List<TipoVivienda> tiposViviendas;
	private TipoVivienda tipoViviendaSelected;
	private long tipoViviendaSelectedId;
	
	// objetos vinculados al combo de manzana (capa 2)
	private List<Manzana> manzanas;
	private Manzana manzanaSelected;
	private long manzanaSelectedId;
	
	// objetos del panel de detalle de vivienda (capa 2)
	private Vivienda vivienda;
	private long viviendaId;
	private String codigoComunidad;
	private String consecutivo;
	private String direccion;
	
	private String observacionesVivienda;
	private BigDecimal longitud;
	private BigDecimal latitud;
	private boolean pasivo;
	
	// objetos vinculados a la grilla de encuestas (capa 2)
	private ViviendaEncuesta viviendaEncuestaSelected;
	
	// objetos vinculados a la ventana modal para edición de encuestas de viviendas
	private String fechaEncuesta;
	private String jefeFamilia;
	private BigDecimal habitantes;
	private BigDecimal hogares;
	private List<CondicionOcupacion> condicionesOcupacion;
	private CondicionOcupacion condicionOcupacionSelected;
	private long condicionOcupacionSelectedId;
	private String observacionesEncuesta;
	
	// objetos vinculados a la grilla de factores de riesgos
	private ViviendaRiesgo viviendaRiesgoSelected;
	
	// objetos vinculados a la ventana modal para edición de factores de riesgos
	private List<FactorRiesgo> factoresRiesgos;
	private FactorRiesgo factorRiesgoSelected;
	private long factorRiesgoSelectedId;
	
	private List<NivelRiesgo> nivelesRiesgos;
	private NivelRiesgo nivelRiesgoSelected;
	private long nivelRiesgoSelectedId;
	
	private String fechaTomaRiesgo;
	private String observacionesRiesgo;
	
	// objetos vinculados a la ventana modal para edición de parámetros epidemiológicos
	private ViviendaParametroEpidemiologico parametroEpidemiologicoViviendaSelected;
	
	private List<ParametroEpidemiologico> parametrosEpidemiologicos;
	private ParametroEpidemiologico parametroEpidemiologicoSelected;
	private long parametroEpidemiologicoSelectedId;
	private Map<String, Object> valoresParametro;
	private String valorParametroSelected;
	private BigDecimal valorParametro;
	private String fechaTomaParametro;
	private String observacionesParametro;
	
	// comunidad seleccionada de la grilla de comunidades
	private Comunidad comunidadSelected;
	// lista de comunidades presentado en la grilla de la ventana modal
	private LazyDataModel<Comunidad> comunidades;
	// literal utilizada para filtrar las comunidades en la grilla, por el nombre
	private String filtroComunidad;
	// literal utilizada para filtrar las viviendas en la grilla, por el jefe de familia
	private String filtroVivienda;
	
	private int numRegistrosComunidades;
	private int numRegistrosViviendas;
	
	// variable para gestión de las capas
	private int capaActiva;
	
	// variable para control de cambios
	private boolean cambiosPendientes; 
	
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
    private String urlMarcadorCasaConRiesgo;
    private String urlMarcadorCasaSinRiesgo;
    private int zoom;
	private NivelZoom nivelZoom;
    
    private String coordenadasPais;
	
	private static ManzanaService manzanaService=new ManzanaDA();
	private static ViviendaService viviendaService = new ViviendaDA();
	private static ComunidadService comunidadService = new ComunidadDA();
	private static ViviendaEncuestaService viviendaEncuestaService = new ViviendaEncuestaDA();
	private static ViviendaRiesgoService viviendaRiesgoService = new ViviendaRiesgoDA();
	private static ViviendaParametroService viviendaParametroService = new ViviendaParametroDA();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TipoVivienda,Integer> tipoViviendaService=new CatalogoElementoDA(TipoVivienda.class,"TipoVivienda");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<NivelRiesgo,Integer> nivelRiesgoService=new CatalogoElementoDA(NivelRiesgo.class,"NivelRiesgo");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<CondicionOcupacion,Integer> condicionOcupacionService=new CatalogoElementoDA(CondicionOcupacion.class,"CondicionOcupacion");
	private static FactorRiesgoService factorRiesgoService = new FactorRiesgoDA();
	private static ParametroEpidemiologicoService parametroEpidemiologicoService = new ParametroEpidemiologicoDA();
	private static UnidadService unidadService = new UnidadDA();
	
	private static MarcadorService marcadorService = new MarcadorDA();
	private static ParametroService parametroService = new ParametroDA();
	
	public ViviendaBean() {
		
		this.nivelZoom=new NivelZoom();
		
		this.capaActiva=1;
		
		this.infoSesion=Utilidades.obtenerInfoSesion();
		
		this.entidades= new ArrayList<EntidadAdtva>();
		this.unidades= new ArrayList<Unidad>();

		this.entidadSelectedId=0;
		this.unidadSelectedId=0;
		
		this.filtroComunidad="";
		this.filtroVivienda="";

		this.urlMarcadorCasaConRiesgo="";
		this.urlMarcadorCasaSinRiesgo="";
		InfoResultado oResultadoParametro=parametroService.Encontrar("CASA_CON_RIESGO");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
			Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
			InfoResultado oResultadoMarcador=marcadorService.Encontrar(oParametro.getValor());
			if (oResultadoMarcador!=null && oResultadoMarcador.isOk()) {
				this.urlMarcadorCasaConRiesgo=((Marcador)oResultadoMarcador.getObjeto()).getUrl();
			}
		}
		
		oResultadoParametro=parametroService.Encontrar("CASA_SIN_RIESGO");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
			Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
			InfoResultado oResultadoMarcador=marcadorService.Encontrar(oParametro.getValor());
			if (oResultadoMarcador!=null && oResultadoMarcador.isOk()) {
				this.urlMarcadorCasaSinRiesgo=((Marcador)oResultadoMarcador.getObjeto()).getUrl();
			}
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
			
				if (unidadSelectedId==0) {
					return null;
				}

				List<Comunidad> comunidadesList=null;
				numRegistrosComunidades=0;
			
				// verifica que exista un filtro para la búsqueda de las comunidades
				if ((filtroComunidad!=null) && !filtroComunidad.trim().isEmpty() && filtroComunidad.trim().length()>=3) {

					numRegistrosComunidades=comunidadService.ContarComunidadesPorUnidad(unidadSelectedId, filtroComunidad.trim(),null);
					comunidadesList=comunidadService.ComunidadesPorUnidad(unidadSelectedId, filtroComunidad.trim(), null, first, pageSize,numRegistrosComunidades);

					if (!(comunidadesList!=null && comunidadesList.size()>0)) {
						FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "No se encontraron comunidades con los parámetros de búsqueda seleccionados","Verifique la literal de búsqueda, la entidad administrativa y la unidad de salud");
						if (msg!=null)
							FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					
				} else {
				
					numRegistrosComunidades=comunidadService.ContarComunidadesPorUnidad(unidadSelectedId,null,null);
					comunidadesList = comunidadService.ComunidadesPorUnidad(unidadSelectedId, null, null, first, pageSize,numRegistrosComunidades);
				}
				this.setRowCount(numRegistrosComunidades);
				return comunidadesList;
			}

		};
		
		this.comunidades.setRowCount(numRegistrosComunidades);

		this.viviendasUltimaEncuesta = new LazyDataModel<ViviendaUltimaEncuesta>() {

			private static final long serialVersionUID = 1L;

			@Override
		    public List<ViviendaUltimaEncuesta> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
			
				List<ViviendaUltimaEncuesta> viviendasList=null;
				numRegistrosViviendas=0;
			
				if (comunidadSelected!=null) {

					// verifica que exista un filtro para la búsqueda de las viviendas
					if ((filtroVivienda!=null) && !filtroVivienda.trim().isEmpty() && filtroVivienda.trim().length()>=3) {

						numRegistrosViviendas=viviendaService.ContarViviendasPorComunidad(comunidadSelected.getCodigo(), filtroVivienda.trim(),null);
						viviendasList=viviendaService.ViviendasPorComunidad(comunidadSelected.getCodigo(), filtroVivienda.trim(), null, first, pageSize,numRegistrosViviendas);

						if (!(viviendasList!=null && viviendasList.size()>0)) {
							FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "No se encontraron viviendas con los parámetros de búsqueda seleccionados","Verifique la literal de búsqueda.");
							if (msg!=null)
								FacesContext.getCurrentInstance().addMessage(null, msg);
						}
					
					} else {
				
						numRegistrosViviendas=viviendaService.ContarViviendasPorComunidad(comunidadSelected.getCodigo(),null,null);
						viviendasList = viviendaService.ViviendasPorComunidad(comunidadSelected.getCodigo(), null, null, first, pageSize,numRegistrosViviendas);
					}
				} 
				return viviendasList;
			}

		};
		
		this.viviendasUltimaEncuesta.setRowCount(numRegistrosViviendas);

		iniciarCapa1();

	}

	/**
	 * Inicializa las variables vinculadas a la gestión de la grilla de viviendas,i.e.
	 * filtro de búsqueda, selección.  Se ejecuta al deseleccionar una comunidad,
	 * al cargar por primera vez la página, al cambiar la entidad administrativa o
	 * al cambiar la unidad de salud.  En estos últimos dos casos, como se reconstruye
	 * la lista de comunidades y la comunidad seleccionada se vuelve null (deselección
	 * de comunidad)
	 */
	public void iniciarCapa1() {
		
		this.viviendaUltimaEncuestaSelected=null;
		
		this.viviendaEncuestaSelected=null;
		this.numRegistrosViviendas=0;
		
		this.vivienda=new Vivienda();
		this.viviendaId=0;
		
		filtroVivienda="";

		
	}

	public void regresarCapa1() {
		
		this.capaActiva=1;
		obtenerViviendasUltimaEncuesta();
		
	}
	
	public void iniciarCapa2() {

		this.cambiosPendientes=false;
		this.vivienda=new Vivienda();
		this.consecutivo=null;
		this.viviendaId=0;
		this.pasivo=false;
		this.manzanaSelected=null;
		this.manzanaSelectedId=0;
		this.tipoViviendaSelected=null;
		this.tipoViviendaSelectedId=0;
		this.tiposViviendas=tipoViviendaService.ListarActivos();
		
		// solo mostrará las manzanas cuando la comunidad sea urbana
		if (this.comunidadSelected.getTipoArea()!=null && this.comunidadSelected.getTipoArea().equals(TipoArea.URBANO.getCodigo())) {
			this.manzanas=manzanaService.ManzanasPorComunidad(this.comunidadSelected.getCodigo(), true, null);
		} else {
			this.manzanas.clear();
		}
		
		this.direccion=null;
		this.observacionesVivienda=null;
		this.longitud=null;
		this.latitud=null;
		this.pasivo=false;
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
	
	public void buscarVivienda() {
		
		if (this.filtroVivienda!=null && this.filtroVivienda.trim().length()>0 && this.filtroVivienda.trim().length()<3) { 

			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "La búsqueda solo es permitida para un número superior a 3 caracteres","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		iniciarViviendas();
		
	}
	
	/**
	 * El cambio de unidad se desencadena desde el combo de unidades.
	 * Al cambiarse la unidad de salud, se debe reconstruir la lista de comunidad y por ende,
	 * la lista de viviendas.
	 */
	public void cambiarUnidad() {
		
		InfoResultado oResultado=unidadService.Encontrar(this.unidadSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}

		Unidad oUnidad=(Unidad)oResultado.getObjeto();
		this.unidadSelected=oUnidad;

		this.filtroComunidad="";
		this.filtroVivienda="";
		
		iniciarComunidades();
		iniciarCapa1();
		
	}
	
	public void iniciarComunidades() {
		
/*		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmVivienda:grdComunidades");
		this.comunidades.load(0, 50, null, null, null);
		this.comunidades.setRowCount(numRegistrosComunidades);
		dataTable.loadLazyData();
		if (dataTable.getRowCount() <= dataTable.getPage()) {
			dataTable.setFirst(0);
		} else {
			dataTable.setFirst(dataTable.getPage());
		}
*/		this.comunidadSelected=null;
		this.codigoComunidad=null;

	}
	
	public void iniciarViviendas() {
		
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmVivienda:grdViviendas");
		this.viviendasUltimaEncuesta.load(0, 50, null, null, null);
		this.viviendasUltimaEncuesta.setRowCount(numRegistrosViviendas);
		dataTable.loadLazyData();
		if (dataTable.getRowCount() <= dataTable.getPage()) {
			dataTable.setFirst(0);
		} else {
			dataTable.setFirst(dataTable.getPage());
		}

		this.viviendaUltimaEncuestaSelected=null;
		this.viviendaUltimaEncuestaId=0;
		this.vivienda=new Vivienda();
		this.viviendaId=0;
		
	}
	
	/**
	 * Quita el filtro de la busqueda de comunidades, obtiene todas las comunidades
	 * vinculadas a la unidad de salud seleccionada 
	 */
	public void quitarFiltroComunidad() {
		
		this.filtroComunidad="";
		iniciarComunidades();
		iniciarCapa1();
		
	}

	/**
	 * Quita el filtro de la busqueda de viviendas y obtiene todas las
	 * viviendas vinculadas a la comunidad seleccionada
	 *  
	 */
	public void quitarFiltroVivienda() {
		
		this.filtroVivienda="";
		iniciarViviendas();
		
	}

	public void generarMapa(ActionEvent pEvento) {
		
		this.mapaModelo = new DefaultMapModel();  
        BigDecimal iSumaLatitudes=BigDecimal.ZERO;
        BigDecimal iSumaLongitudes=BigDecimal.ZERO;
        int iNumPuntos=0;
        this.centroMapa="0,0";
        this.zoom=nivelZoom.PAIS;
        
        // genera la lista total de viviendas activas vinculadas a la comunidad
        
        List<ViviendaUltimaEncuesta> oViviendasUltimaEncuesta=viviendaService.ViviendasPorComunidad(this.comunidadSelected.getCodigo(), false);
		for (ViviendaUltimaEncuesta oViviendaUltimaEncuesta:oViviendasUltimaEncuesta) {
			if (oViviendaUltimaEncuesta.getLatitud()!=null && oViviendaUltimaEncuesta.getLongitud()!=null) {
				iSumaLatitudes=iSumaLatitudes.add(oViviendaUltimaEncuesta.getLatitud());
				iSumaLongitudes=iSumaLongitudes.add(oViviendaUltimaEncuesta.getLongitud());
				iNumPuntos++;
				
				LatLng iCoordenada = new LatLng(oViviendaUltimaEncuesta.getLatitud().doubleValue(),oViviendaUltimaEncuesta.getLongitud().doubleValue());
				String iTituloMarcador=null;
				if (oViviendaUltimaEncuesta.getJefeFamilia()!=null && !oViviendaUltimaEncuesta.getJefeFamilia().isEmpty()) {
					iTituloMarcador=oViviendaUltimaEncuesta.getJefeFamilia();
				} else {
					if (oViviendaUltimaEncuesta.getCondicionOcupacion()!=null && !oViviendaUltimaEncuesta.getCondicionOcupacion().isEmpty()) {
						iTituloMarcador=oViviendaUltimaEncuesta.getCondicionOcupacion();
					} else {
						iTituloMarcador="No encuestada";
					}
				}
				Marker oMarker = new Marker(iCoordenada, iTituloMarcador);
				if (oViviendaUltimaEncuesta.getExistenFactoresRiesgo().equals(BigDecimal.ONE)) {
					if (!this.urlMarcadorCasaConRiesgo.isEmpty()) {
						oMarker.setIcon(this.urlMarcadorCasaConRiesgo);
					}
				} else {
					if (!this.urlMarcadorCasaSinRiesgo.isEmpty()) {
						oMarker.setIcon(this.urlMarcadorCasaSinRiesgo);
					}
				}
				this.mapaModelo.addOverlay(oMarker);
			}
		}
		
		if (iNumPuntos!=0) {
			this.centroMapa=iSumaLatitudes.divide(new BigDecimal(iNumPuntos),6,RoundingMode.HALF_UP).toPlainString() + "," + iSumaLongitudes.divide(new BigDecimal(iNumPuntos),6,RoundingMode.HALF_UP).toPlainString();
			this.zoom=nivelZoom.MUNICIPIO;
		}

		if (this.comunidadSelected.getLatitud()!=null && this.comunidadSelected.getLongitud()!=null) {

			if (iNumPuntos==0) {
				this.centroMapa=this.comunidadSelected.getLatitud().toPlainString() + "," + this.comunidadSelected.getLongitud().toPlainString();
			}
			
			this.zoom=nivelZoom.COMUNIDAD;
			return;
		}

		if (this.unidadSelected.getLatitud()!=null && this.unidadSelected.getLongitud()!=null) {

			if (iNumPuntos==0) {
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

	// -----------------------------------------------------------------------------
	// Encuestas
	// -----------------------------------------------------------------------------
	
	public void agregarEncuesta() {
		this.viviendaEncuestaSelected=null;
		editarEncuesta();
	}
	
	/**
	 * Este método permita la edición de una encuesta existente o agregar
	 * una nueva encuesta
	 */
	public void editarEncuesta() {
		
		boolean iAgregar=false;
		if (this.viviendaEncuestaSelected==null) iAgregar=true; 
		
		if (iAgregar) {
			this.condicionesOcupacion=condicionOcupacionService.ListarActivos();
			iniciarEncuesta();
		} else {
			this.jefeFamilia=this.viviendaEncuestaSelected.getJefeFamilia();
			this.habitantes=this.viviendaEncuestaSelected.getHabitantes();
			this.hogares=this.viviendaEncuestaSelected.getHogares();
			this.observacionesEncuesta=this.viviendaEncuestaSelected.getObservaciones();
			this.fechaEncuesta=new SimpleDateFormat("dd/MM/yyyy").format(this.viviendaEncuestaSelected.getFechaEncuesta());
			this.condicionesOcupacion=condicionOcupacionService.ListarActivos(this.viviendaEncuestaSelected.getCondicionOcupacion().getCodigo());
			this.condicionOcupacionSelected=this.viviendaEncuestaSelected.getCondicionOcupacion();
			this.condicionOcupacionSelectedId=this.condicionOcupacionSelected.getCatalogoId();
		}
		
	}
	
	public void iniciarEncuesta() {
		
		this.fechaEncuesta=null;
		this.jefeFamilia="";
		this.habitantes=null;
		this.hogares=null;
		this.condicionOcupacionSelected=null;
		this.condicionOcupacionSelectedId=0;
		this.observacionesEncuesta="";
		
	}

	/**
	 * Deselecciona la encuesta en la ventana modal de edición de encuestas
	 */
	public void cerrarEncuesta(ActionEvent pEvento) {
		this.viviendaEncuestaSelected=null;
	}
	
	public void guardarEncuesta(ActionEvent pEvento) {

		if ((this.habitantes==null || this.habitantes.equals(BigDecimal.ZERO)) && (this.hogares!=null && this.hogares.equals(BigDecimal.ZERO))) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Si el número de habitantes es cero, el número de hogares no debe ser declarado","");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorEncuesta(false);
			return;
		}
		
		if ((this.habitantes==null || this.habitantes.equals(BigDecimal.ZERO)) && (this.jefeFamilia!=null && !this.jefeFamilia.trim().isEmpty())) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Si el número de habitantes es cero, el jefe de familia no debe ser declarado o debe declararse un número de habitantes","");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorEncuesta(false);
			return;
		}

		if (this.condicionOcupacionSelectedId==0) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La condición de ocupación es obligatoria","Seleccione una condición de ocupación de la lista");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorEncuesta(false);
			return;
		}
		
		if (Utilidades.textoAFecha(this.fechaEncuesta, "dd/MM/yyyy")==null) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La fecha de la encuesta no posee el formato correcto o no es válida","Revise la fecha de la encuesta");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorEncuesta(false);
			return;
		}

		if (this.viviendaEncuestaSelected==null) {
			// verifica que únicamente exista una encuesta por fecha y que la fecha
			// sea superior a la fecha máxima declarada para el conjunto de encuestas
			boolean iError=false;
			for (ViviendaEncuesta oEncuesta:this.vivienda.getEncuestas()) {
				if (this.fechaEncuesta.equals(new SimpleDateFormat("dd/MM/yyyy").format(oEncuesta.getFechaEncuesta()))) {
					FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Unicamente se permite una encuesta de vivienda por fecha","Revise la fecha de la encuesta");
					if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
					controlErrorEncuesta(false);
					iError=true;
					break;
				}
			}
			if (iError) return;
		}
		
		ViviendaEncuesta oViviendaEncuesta = new ViviendaEncuesta();
		CondicionOcupacion oOcupacion=(CondicionOcupacion)condicionOcupacionService.Encontrar(this.condicionOcupacionSelectedId).getObjeto();
		
		oViviendaEncuesta.setHabitantes(this.habitantes);
		oViviendaEncuesta.setHogares(this.hogares);
		
		if (this.observacionesEncuesta!=null && !this.observacionesEncuesta.trim().isEmpty()) {
			oViviendaEncuesta.setObservaciones(this.observacionesEncuesta);
		} else {
			oViviendaEncuesta.setObservaciones(null);
		}
		
		if (this.jefeFamilia!=null && !this.jefeFamilia.trim().isEmpty()) {
			oViviendaEncuesta.setJefeFamilia(this.jefeFamilia);
		} else {
			oViviendaEncuesta.setJefeFamilia(null);
		}
		
		oViviendaEncuesta.setVivienda(this.vivienda);
		oViviendaEncuesta.setCondicionOcupacion(oOcupacion);
		
		// si viviendaEncuestaSelected es nulo, implica que no se ha seleccionado
		// ninguna encuesta de la grilla y se ha solicitado agregar una nueva
		// encuesta asociada a la vivienda
		
		InfoResultado oResultado = new InfoResultado();
		
		if (this.viviendaEncuestaSelected==null) {
			// la fecha de la encuesta no puede ser modificada
			
			oViviendaEncuesta.setFechaEncuesta(Utilidades.textoAFecha(this.fechaEncuesta, "dd/MM/yyyy"));
			oViviendaEncuesta.setVivienda(this.vivienda);
			oViviendaEncuesta.setFechaRegistro(Calendar.getInstance().getTime());
			oViviendaEncuesta.setUsuarioRegistro(this.infoSesion.getUsername());
			
			oViviendaEncuesta.setSistema(Utilidades.CODIGO_SISTEMA);
			oResultado=viviendaEncuestaService.Agregar(oViviendaEncuesta);
		} else {
			oViviendaEncuesta.setViviendaEncuestaId(this.viviendaEncuestaSelected.getViviendaEncuestaId());
			oResultado=viviendaEncuestaService.Guardar(oViviendaEncuesta);
		}
		
		if (oResultado.isOk()) {
			controlErrorEncuesta(true);
			this.viviendaEncuestaSelected=null;
			this.vivienda.setEncuestas(viviendaEncuestaService.EncuestasPorVivienda(this.vivienda.getCodigo()));
		} else {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorEncuesta(false);
		}
		
	}

	public void eliminarEncuesta(ActionEvent pEvento){
	
		if (this.viviendaEncuestaSelected==null) return;

		InfoResultado oResultado = new InfoResultado();
		oResultado=viviendaEncuestaService.Eliminar(this.viviendaEncuestaSelected.getViviendaEncuestaId());
		this.viviendaEncuestaSelected=null;
		
		if (oResultado.isOk()){
			oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
			this.vivienda.setEncuestas(viviendaEncuestaService.EncuestasPorVivienda(this.vivienda.getCodigo()));
			controlErrorEncuesta(true);
		} else {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorEncuesta(false);
		}
		
	}
	
	/**
	 * Agregar un parámetro de retorno y actualiza los controles
	 * respectivos en caso de exitir o no un error en el proceso
	 * @param pProcesoOK  <code>true</code> si el proceso se ha llevado a cabo sin errores, 
	 *                    <code>false</code> si durante el proceso se ha producido un error.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void controlErrorEncuesta(boolean pProcesoOK) {
	
		RequestContext context = RequestContext.getCurrentInstance();
		
		if (!pProcesoOK) {
	        context.addCallbackParam("encuestaOK", false);
			String aComponentes[] = {"frmVivienda:grwMensaje"};
	        Collection iComponentes = Arrays.asList(aComponentes);
	        context.update(iComponentes);
		} else {
			context.addCallbackParam("encuestaOK", true);
			String aComponentes[] = {"frmVivienda:grdEncuestas","frmVivienda:grwMensaje"};
	        Collection iComponentes = Arrays.asList(aComponentes);
	        context.update(iComponentes);
		}

	}


	// -----------------------------------------------------------------------------
	// Factores de Riesgo
	// -----------------------------------------------------------------------------

	public void agregarRiesgo() {
		this.viviendaRiesgoSelected=null;
		editarRiesgo();
	}

	public void editarRiesgo() {
		
		boolean iAgregar=false;
		if (this.viviendaRiesgoSelected==null) iAgregar=true; 
		
		if (iAgregar) {
			this.factoresRiesgos=factorRiesgoService.Listar(false);
			this.nivelesRiesgos=nivelRiesgoService.ListarActivos();
			iniciarRiesgo();
		} else {
			this.fechaTomaRiesgo=new SimpleDateFormat("dd/MM/yyyy").format(this.viviendaRiesgoSelected.getFechaToma());
			this.observacionesRiesgo=this.viviendaRiesgoSelected.getObservaciones();
			this.nivelRiesgoSelected=this.viviendaRiesgoSelected.getNivelRiesgo();
			this.nivelRiesgoSelectedId=this.viviendaRiesgoSelected.getNivelRiesgo().getCatalogoId();
			this.factorRiesgoSelected=this.viviendaRiesgoSelected.getFactorRiesgo();
			this.factorRiesgoSelectedId=this.viviendaRiesgoSelected.getFactorRiesgo().getFactorRiesgoId();
			this.factoresRiesgos=factorRiesgoService.Listar(this.factorRiesgoSelected.getCodigo());
			this.nivelesRiesgos=nivelRiesgoService.ListarActivos(this.nivelRiesgoSelected.getCodigo());
		}
		
	}

	public void iniciarRiesgo() {
		
		this.fechaTomaRiesgo=null;
		this.factorRiesgoSelected=null;
		this.factorRiesgoSelectedId=0;
		this.nivelRiesgoSelected=null;
		this.nivelRiesgoSelectedId=0;
		this.observacionesRiesgo="";
		
	}

	/**
	 * Deselecciona el riesgo en la ventana modal de edición de factores de riesgos
	 */
	public void cerrarRiesgo(ActionEvent pEvento) {
		this.viviendaRiesgoSelected=null;
	}
	
	public void guardarRiesgo(ActionEvent pEvento) {

		if (this.factorRiesgoSelectedId==0) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Debe especificarse un factor de riesgo","Seleccione un factor de riesgo de la lista");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorRiesgo(false);
			return;
		}

		if (this.nivelRiesgoSelectedId==0) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Debe especificarse un nivel de riesgo para el factor","Seleccione un nivel de riesgo de la lista");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorRiesgo(false);
			return;
		}
		
		if (Utilidades.textoAFecha(this.fechaTomaRiesgo, "dd/MM/yyyy")==null) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La fecha en la cual se tomaron los datos sobre el factor de riesgo es requerida","La fecha no puede ser mayor a la fecha actual ni inferior a 365 días");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorRiesgo(false);
			return;
		}

		if (this.viviendaRiesgoSelected==null) {
			// verifica que únicamente exista un factor de riesgo por fecha y que la fecha
			// sea superior a la fecha máxima declarada para el conjunto de encuestas
			boolean iError=false;
			for (ViviendaRiesgo oViviendaRiesgo:this.vivienda.getRiesgos()) {
				if (this.factorRiesgoSelectedId==oViviendaRiesgo.getFactorRiesgo().getFactorRiesgoId()) {
					FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Unicamente se permite declarar una vez el factor de riesgo","Revise la lista de factores de riesgo asociados a la vivienda");
					if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
					controlErrorRiesgo(false);
					iError=true;
					break;
				}
			}
			if (iError) return;
		}
		
		ViviendaRiesgo oViviendaRiesgo = new ViviendaRiesgo();
		NivelRiesgo oNivelRiesgo=(NivelRiesgo)nivelRiesgoService.Encontrar(this.nivelRiesgoSelectedId).getObjeto();
		
		if (this.observacionesRiesgo!=null && !this.observacionesRiesgo.trim().isEmpty()) {
			oViviendaRiesgo.setObservaciones(this.observacionesRiesgo);
		} else {
			oViviendaRiesgo.setObservaciones(null);
		}
		
		oViviendaRiesgo.setVivienda(this.vivienda);
		oViviendaRiesgo.setFechaToma(Utilidades.textoAFecha(this.fechaTomaRiesgo, "dd/MM/yyyy"));
		oViviendaRiesgo.setNivelRiesgo(oNivelRiesgo);
		
		// si viviendaRiesgoSelected es nulo, implica que no se ha seleccionado
		// ningun factor de riesgo de la grilla y se ha solicitado agregar un
		// nuevo factor asociado a la vivienda
		
		InfoResultado oResultado = new InfoResultado();
		
		if (this.viviendaRiesgoSelected==null) {
			oViviendaRiesgo.setFechaRegistro(Calendar.getInstance().getTime());
			oViviendaRiesgo.setUsuarioRegistro(this.infoSesion.getUsername());
			
			oViviendaRiesgo.setFactorRiesgo((FactorRiesgo)factorRiesgoService.Encontrar(this.factorRiesgoSelectedId).getObjeto());

			oViviendaRiesgo.setSistema(Utilidades.CODIGO_SISTEMA);
			oResultado=viviendaRiesgoService.Agregar(oViviendaRiesgo);

		} else {
			oViviendaRiesgo.setViviendaRiesgoId(this.viviendaRiesgoSelected.getViviendaRiesgoId());
			oResultado=viviendaRiesgoService.Guardar(oViviendaRiesgo);
		}
		
		if (oResultado.isOk()) {
			controlErrorRiesgo(true);
			this.viviendaRiesgoSelected=null;
			this.vivienda.setRiesgos(viviendaRiesgoService.RiesgosPorVivienda(this.vivienda.getCodigo()));
		} else {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorRiesgo(false);
		}
		
	}

	public void eliminarRiesgo(ActionEvent pEvento){
	
		if (this.viviendaRiesgoSelected==null) return;

		InfoResultado oResultado = new InfoResultado();
		oResultado=viviendaRiesgoService.Eliminar(this.viviendaRiesgoSelected.getViviendaRiesgoId());
		this.viviendaRiesgoSelected=null;
		
		if (oResultado.isOk()){
			oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
			this.vivienda.setRiesgos(viviendaRiesgoService.RiesgosPorVivienda(this.vivienda.getCodigo()));
			controlErrorRiesgo(true);
		} else {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorRiesgo(false);
		}
		
	}

	/**
	 * Agregar un parámetro de retorno y actualiza los controles
	 * respectivos en caso de exitir o no un error en el proceso
	 * @param pProcesoOK  <code>true</code> si el proceso se ha llevado a cabo sin errores, 
	 *                    <code>false</code> si durante el proceso se ha producido un error.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void controlErrorRiesgo(boolean pProcesoOK) {
	
		RequestContext context = RequestContext.getCurrentInstance();
		
		if (!pProcesoOK) {
	        context.addCallbackParam("riesgoOK", false);
			String aComponentes[] = {"frmVivienda:grwMensaje"};
	        Collection iComponentes = Arrays.asList(aComponentes);
	        context.update(iComponentes);
		} else {
			context.addCallbackParam("riesgoOK", true);
			String aComponentes[] = {"frmVivienda:grdRiesgos","frmVivienda:grwMensaje"};
	        Collection iComponentes = Arrays.asList(aComponentes);
	        context.update(iComponentes);
		}

	}


	
	
	
	
	
	
	
	
	
	
	// -----------------------------------------------------------------------------
	// Parametros Epidemiológicos
	// -----------------------------------------------------------------------------

	public void agregarParametro() {
		this.parametroEpidemiologicoViviendaSelected=null;
		editarParametro();
	}

	public void editarParametro() {
		
		boolean iAgregar=false;
		this.valoresParametro=new LinkedHashMap<String,Object>();
		
		if (this.parametroEpidemiologicoViviendaSelected==null) iAgregar=true; 
		
		if (iAgregar) {
			this.parametrosEpidemiologicos=parametroEpidemiologicoService.Listar(false);
			// como no se ha seleccionado ningún parámetro epidemiológico, no se puede inicializar la lista de valores
			iniciarParametro();
		} else {
			this.fechaTomaParametro=new SimpleDateFormat("dd/MM/yyyy").format(this.parametroEpidemiologicoViviendaSelected.getFechaToma());
			this.observacionesParametro=this.parametroEpidemiologicoViviendaSelected.getObservaciones();
			this.parametroEpidemiologicoSelected=this.parametroEpidemiologicoViviendaSelected.getParametro();
			this.parametroEpidemiologicoSelectedId=this.parametroEpidemiologicoViviendaSelected.getParametro().getParametroEpidemiologicoId();
			this.parametrosEpidemiologicos=parametroEpidemiologicoService.Listar(this.parametroEpidemiologicoSelected.getCodigo());
			
			String[] oValores=this.parametroEpidemiologicoSelected.getEtiqueta().split("\\|");
			for (int i=0;i<oValores.length;i++) {
				this.valoresParametro.put(oValores[i], oValores[i]);  // label, value
			}
			
			this.valorParametroSelected=this.parametroEpidemiologicoViviendaSelected.getValor();
			
			// debe de agregar el valor del parámetro, en caso de que ya no exista en la lista
			if (!this.valoresParametro.containsValue(this.valorParametroSelected)) {
				this.valoresParametro.put(this.valorParametroSelected,this.valorParametroSelected);
			}
			
		}
		
	}

	public void cambiarParametro() {
		
		this.parametroEpidemiologicoSelected=(ParametroEpidemiologico)parametroEpidemiologicoService.Encontrar(this.parametroEpidemiologicoSelectedId).getObjeto(); 
		String[] oValores=this.parametroEpidemiologicoSelected.getEtiqueta().split("\\|");
		for (int i=0;i<oValores.length;i++) {
			this.valoresParametro.put(oValores[i], oValores[i]);  // label, value
		}
		this.valorParametroSelected="";
		
	}
	
	public void iniciarParametro() {
		
		this.fechaTomaParametro=null;
		this.parametroEpidemiologicoSelected=null;
		this.parametroEpidemiologicoSelectedId=0;
		this.valoresParametro=new LinkedHashMap<String,Object>();
		this.valorParametroSelected=null;
		this.observacionesParametro="";
		
	}

	/**
	 * Deselecciona el parámetro en la ventana modal de edición de parámetros epidemiológicos
	 */
	public void cerrarParametro(ActionEvent pEvento) {
		this.parametroEpidemiologicoViviendaSelected=null;
	}
	
	public void guardarParametro(ActionEvent pEvento) {

		if (this.parametroEpidemiologicoSelectedId==0) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Debe especificarse un parámetro epidemiológico","Seleccione un parámetro epidemiológico de la lista");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorParametro(false);
			return;
		}

		if (this.valorParametroSelected==null || this.valorParametroSelected.equals("")) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Debe especificarse un valor para el parámetro epidemiológico","Seleccione un valor de la lista");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorParametro(false);
			return;
		}
		
		if (Utilidades.textoAFecha(this.fechaTomaParametro, "dd/MM/yyyy")==null) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La fecha en la cual se tomaron los datos sobre el parámetro epidemiológico es requerida","La fecha no puede ser mayor a la fecha actual ni inferior a 365 días");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorParametro(false);
			return;
		}

		if (this.parametroEpidemiologicoViviendaSelected==null) {
			// verifica que el parametro epidemiologico sea único, i.e. no puede declararse dos veces un mismo parámetro epidemiológico
			boolean iError=false;
			for (ViviendaParametroEpidemiologico oViviendaParametroEpidemiologico:this.vivienda.getParametros()) {
				if (this.parametroEpidemiologicoSelectedId==oViviendaParametroEpidemiologico.getParametro().getParametroEpidemiologicoId()) {
					FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Unicamente se permite declarar una vez el parámetro epidemiológico","Revise la lista de parámetros epidemiológicos asociados a la vivienda");
					if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
					controlErrorParametro(false);
					iError=true;
					break;
				}
			}
			if (iError) return;
		}
		
		ViviendaParametroEpidemiologico oViviendaParametroEpidemiologico = new ViviendaParametroEpidemiologico();
		
		if (this.observacionesParametro!=null && !this.observacionesParametro.trim().isEmpty()) {
			oViviendaParametroEpidemiologico.setObservaciones(this.observacionesParametro);
		} else {
			oViviendaParametroEpidemiologico.setObservaciones(null);
		}
		
		oViviendaParametroEpidemiologico.setVivienda(this.vivienda);
		oViviendaParametroEpidemiologico.setFechaToma(Utilidades.textoAFecha(this.fechaTomaParametro, "dd/MM/yyyy"));
		oViviendaParametroEpidemiologico.setValor(this.valorParametroSelected);
		
		// si parametroEpidemiologicoViviendaSelected es nulo, implica que no se ha seleccionado
		// ningun parámetro epidemiológico de la grilla y se ha solicitado agregar un
		// nuevo factor asociado a la vivienda
		
		InfoResultado oResultado = new InfoResultado();
		
		if (this.parametroEpidemiologicoViviendaSelected==null) {
			oViviendaParametroEpidemiologico.setFechaRegistro(Calendar.getInstance().getTime());
			oViviendaParametroEpidemiologico.setUsuarioRegistro(this.infoSesion.getUsername());
			
			oViviendaParametroEpidemiologico.setParametro((ParametroEpidemiologico)parametroEpidemiologicoService.Encontrar(this.parametroEpidemiologicoSelectedId).getObjeto());

			oViviendaParametroEpidemiologico.setSistema(Utilidades.CODIGO_SISTEMA);
			oResultado=viviendaParametroService.Agregar(oViviendaParametroEpidemiologico);

		} else {
			oViviendaParametroEpidemiologico.setViviendaParametroEpidemiologicoId(this.parametroEpidemiologicoViviendaSelected.getViviendaParametroEpidemiologicoId());
			oResultado=viviendaParametroService.Guardar(oViviendaParametroEpidemiologico);
		}
		
		if (oResultado.isOk()) {
			controlErrorParametro(true);
			this.parametroEpidemiologicoViviendaSelected=null;
			this.vivienda.setParametros(viviendaParametroService.ParametrosPorVivienda(this.vivienda.getCodigo()));
		} else {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorParametro(false);
		}
		
	}

	public void eliminarParametro(ActionEvent pEvento){
	
		if (this.parametroEpidemiologicoViviendaSelected==null) return;

		InfoResultado oResultado = new InfoResultado();
		oResultado=viviendaParametroService.Eliminar(this.parametroEpidemiologicoViviendaSelected.getViviendaParametroEpidemiologicoId());
		this.parametroEpidemiologicoViviendaSelected=null;
		
		if (oResultado.isOk()){
			oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
			this.vivienda.setParametros(viviendaParametroService.ParametrosPorVivienda(this.vivienda.getCodigo()));
			controlErrorParametro(true);
		} else {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			controlErrorParametro(false);
		}
		
	}

	/**
	 * Agregar un parámetro de retorno y actualiza los controles
	 * respectivos en caso de exitir o no un error en el proceso
	 * @param pProcesoOK  <code>true</code> si el proceso se ha llevado a cabo sin errores, 
	 *                    <code>false</code> si durante el proceso se ha producido un error.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void controlErrorParametro(boolean pProcesoOK) {
	
		RequestContext context = RequestContext.getCurrentInstance();
		
		if (!pProcesoOK) {
	        context.addCallbackParam("parametroOK", false);
			String aComponentes[] = {"frmVivienda:grwMensaje"};
	        Collection iComponentes = Arrays.asList(aComponentes);
	        context.update(iComponentes);
		} else {
			context.addCallbackParam("parametroOK", true);
			String aComponentes[] = {"frmVivienda:grdParametros","frmVivienda:grwMensaje"};
	        Collection iComponentes = Arrays.asList(aComponentes);
	        context.update(iComponentes);
		}

	}
	
	// -----------------------------------------------------------------------------
	// Gestión Capa 1
	// -----------------------------------------------------------------------------

	/**
	 * Evento que se ejecuta cuando el usuario selecciona una comunidad
	 * de la grilla de comunidades vinculadas a la unidad de salud 
	 */
	public void onComunidadSelected(SelectEvent iEvento) { 
		
		obtenerViviendasUltimaEncuesta();
		this.codigoComunidad=this.comunidadSelected.getCodigo();
	}
	
	/**
	 * Evento que se ejecuta cuando el usuario deselecciona una comunidad
	 * de la grilla de comunidades vinculadas a la unidad de salud 
	 */
	public void onComunidadUnSelected(UnselectEvent iEvento) { 
		
		this.comunidadSelected=null;
		this.codigoComunidad=null;
		iniciarCapa1();
	}

	/**
	 * Se obtiene el listado de las viviendas con información de la
	 * última encuesta, ya sea cuando se seleccionar una comunidad
	 * o cuando se retorna de la capa 2 después de efectuar una inserción
	 * o modificación a los datos de vivienda
	 */
	public void obtenerViviendasUltimaEncuesta() {
		
		iniciarCapa1();
		iniciarViviendas();
	}
	
	/**
	 * Evento que se ejecuta cuando el usuario selecciona una vivienda
	 * de la grilla de viviendas existentes.  Traslada todos los
	 * valores del objeto seleccionado a los controles del panel de detalle
	 * de la capa 2 
	 */
	public void onViviendaUltimaEncuestaSelected(SelectEvent iEvento) { 
		
		this.capaActiva=2;
		this.cambiosPendientes=false;
		
		this.viviendaId=this.viviendaUltimaEncuestaSelected.getViviendaId();

		InfoResultado oResultado=new InfoResultado();
		oResultado = viviendaService.Encontrar(this.viviendaId);
		if (!oResultado.isOk()) {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		this.vivienda=(Vivienda)oResultado.getObjeto();
		this.tiposViviendas=tipoViviendaService.ListarActivos(this.vivienda.getTipoVivienda().getCodigo());
		
		this.consecutivo=this.vivienda.getCodigo().substring(9, 14);
		this.tipoViviendaSelected=this.vivienda.getTipoVivienda();
		this.tipoViviendaSelectedId=this.tipoViviendaSelected.getCatalogoId();

		this.manzanaSelected=this.vivienda.getViviendaManzana()!=null?this.vivienda.getViviendaManzana().getManzana():null;
		this.manzanaSelectedId=this.manzanaSelected==null?0:this.manzanaSelected.getManzanaId();

		if (this.comunidadSelected.getTipoArea().equals(TipoArea.URBANO.getCodigo())) {
			this.manzanas=manzanaService.ManzanasPorComunidad(this.comunidadSelected.getCodigo(), true, this.manzanaSelected==null?null:this.manzanaSelected.getCodigo());
		} else {
			this.manzanas.clear();
			if (this.manzanaSelectedId!=0) {
				// esto únicamente pasaría en el caso de que el tipo de area
				// de la comunidad haya sido modificado y la vivienda ya posea
				// una manzana asociada
				this.manzanas.add(this.manzanaSelected);
			}
		}
		
		this.direccion=this.vivienda.getDireccion();
		this.observacionesVivienda=this.vivienda.getObservaciones();
		this.longitud=this.vivienda.getLongitud();
		this.latitud=this.vivienda.getLatitud();
		this.pasivo=this.vivienda.getPasivo().equals(new BigDecimal(0))?false:true;

		// obliga a tener una lista ordenada y fresca de encuestas vinculadas a la vivienda
		// en lugar de tenerlo del contexto de persistencia
		this.vivienda.setEncuestas(viviendaEncuestaService.EncuestasPorVivienda(this.vivienda.getCodigo()));
		
		FacesContext context = FacesContext.getCurrentInstance( ); 
		UIComponent componentDetalle = null; 
		UIViewRoot root = context.getViewRoot( ); 
		componentDetalle = (UIComponent) root.findComponent("frmVivienda:panDetalle");
		for (UIComponent uic:componentDetalle.getChildren()) {
			if (uic instanceof EditableValueHolder) {   
				EditableValueHolder evh=(EditableValueHolder)uic;   
				evh.resetValue();   
			}
		}
		
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
		iniciarCapa1();
		
	}

	/**
	 * Evento que se ejecuta al momento de seleccionar una
	 * encuesta de la grilla de encuestas vinculadas a una vivienda 
	 */
	public void onEncuestaSelected(SelectEvent iEvento) {

		editarEncuesta();

	}

	/**
	 * Evento que se ejecuta al momento de seleccionar un
	 * factor de riesgo de la grilla de riesgos vinculados a una vivienda 
	 */
	public void onRiesgoSelected(SelectEvent iEvento) {

		editarRiesgo();

	}

	/**
	 * Evento que se ejecuta al momento de seleccionar un
	 * parametro epidemiológico de la grilla de parametros vinculados a una vivienda 
	 */
	public void onParametroSelected(SelectEvent iEvento) {

		editarParametro();

	}

	public void activarPrimerCapa(ActionEvent pEvento){

		// antes de retornar verifica si se han realizado cambios y
		// si estos no han sido aplicados a la base de datos
		
		if (this.viviendaId==0) {
			if (this.direccion!=null && !this.direccion.trim().isEmpty()) this.cambiosPendientes=true;
			if (!this.cambiosPendientes && this.latitud!=null) this.cambiosPendientes=true;
			if (!this.cambiosPendientes && this.longitud!=null) this.cambiosPendientes=true;
			if (!this.cambiosPendientes && this.observacionesVivienda!=null && !this.observacionesVivienda.trim().isEmpty()) this.cambiosPendientes=true;
			if (!this.cambiosPendientes && this.pasivo) this.cambiosPendientes=true;
		} else {
			if (this.direccion!=null && 
					!this.direccion.trim().equals(this.vivienda.getDireccion()==null?"":this.vivienda.getDireccion())) this.cambiosPendientes=true;
			if (!this.cambiosPendientes && 
					this.latitud!=null && 
					!this.latitud.equals(this.vivienda.getLatitud())) this.cambiosPendientes=true;
			if (!this.cambiosPendientes && 
					this.longitud!=null && 
					!this.longitud.equals(this.vivienda.getLongitud())) this.cambiosPendientes=true;
			if (!this.cambiosPendientes && 
					this.observacionesVivienda!=null && 
					!this.observacionesVivienda.trim().equals(this.vivienda.getObservaciones()==null?"":this.vivienda.getObservaciones())) this.cambiosPendientes=true;
			if (!this.cambiosPendientes && 
					this.pasivo!=(this.vivienda.getPasivo().equals(BigDecimal.ONE)?true:false)) this.cambiosPendientes=true;
		}
		
		if (this.cambiosPendientes) {
			controlCambios();
			return;
		}
		
		if (this.viviendaId==0) {
			if (this.consecutivo!=null && !this.consecutivo.isEmpty()) this.cambiosPendientes=true;
			if (this.tipoViviendaSelectedId!=0) this.cambiosPendientes=true;
			if (this.manzanaSelectedId!=0) this.cambiosPendientes=true;
			if (this.cambiosPendientes) {
				controlCambios();
				return;
			}
		} else {
			if (this.tipoViviendaSelectedId!=this.vivienda.getTipoVivienda().getCatalogoId()) this.cambiosPendientes=true;
			if (!this.cambiosPendientes && this.vivienda.getViviendaManzana()==null && this.manzanaSelectedId!=0) this.cambiosPendientes=true;
			if (!this.cambiosPendientes && this.vivienda.getViviendaManzana()!=null && this.vivienda.getViviendaManzana().getManzana().getManzanaId()!=this.manzanaSelectedId) this.cambiosPendientes=true;
			if (this.cambiosPendientes) {
				controlCambios();
				return;
			}
		}
		
		controlCambios();
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void controlCambios() {

		RequestContext context = RequestContext.getCurrentInstance();
		
		if (this.cambiosPendientes) {
	        context.addCallbackParam("cambiosPendientes", true);  
		} else {
			context.addCallbackParam("cambiosPendientes", false);
			String aComponentes[] = {"frmVivienda:capa1", "frmVivienda:capa2"};
	        Collection iComponentes = Arrays.asList(aComponentes);
	        context.update(iComponentes);
			regresarCapa1();
		}
	}

	
	/**
	 * Se ejecuta cuando el usuario pulsa en el botón agregar
	 * y consiste en la inicialización de las diferentes propiedades
	 * para colocar el estado del formulario en modo de agregar
	 */
	public void agregar(ActionEvent pEvento) {
		
		this.capaActiva=2;
		this.viviendaUltimaEncuestaSelected=null;
		iniciarCapa2();
		
	}
	
	/**
	 * Se ejecuta cuando el usuario pulsa en el botón guardar.
	 * Este proceso incluye guardar un nuevo registro o actualizar
	 * un registro existente
	 */
	public void guardar(ActionEvent pEvento) {

		String iCodigo=this.codigoComunidad + String.format("%05d", Integer.valueOf(this.consecutivo));
		
		InfoResultado oResultado = new InfoResultado();
		// valida si el código asignado a la vivienda ya existe
		if (this.vivienda.getViviendaId()==0 && viviendaService.Encontrar(iCodigo)!=null) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "El código utilizado para la vivienda ya ha sido asignado","Utilice otro número consecutivo de vivienda");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		// obtiene el objeto manzana que se ha vinculado a la vivienda
		// y que se encuentra identificado por manzanaSelectedId
		oResultado = new InfoResultado();

		this.tipoViviendaSelected=null;
		if (this.tipoViviendaSelectedId!=0) {
			oResultado=tipoViviendaService.Encontrar(this.tipoViviendaSelectedId);
			if (!oResultado.isOk()) {
				FacesMessage msg = Mensajes.enviarMensaje(oResultado);
				if (msg!=null)
					FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			this.tipoViviendaSelected=(TipoVivienda)oResultado.getObjeto();
		}
		
		if (this.tipoViviendaSelected==null) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "El tipo de vivienda es un dato requerido","Elija un elemento de la lista");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		this.manzanaSelected=null;
		if (this.manzanaSelectedId!=0) {
			oResultado=new InfoResultado();
			oResultado=manzanaService.Encontrar(this.manzanaSelectedId);
			if (!oResultado.isOk()) {
				FacesMessage msg = Mensajes.enviarMensaje(oResultado);
				if (msg!=null)
					FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			this.manzanaSelected=(Manzana)oResultado.getObjeto();
		}
		
		Vivienda oVivienda = new Vivienda();
		oVivienda.setPasivo(this.pasivo?(new BigDecimal(1)):(new BigDecimal(0)));
		if (this.direccion==null || this.direccion.trim().isEmpty()) {
			oVivienda.setDireccion(null);
		} else {
			oVivienda.setDireccion(this.direccion.trim());
		}
		oVivienda.setLatitud(this.latitud);
		oVivienda.setLongitud(this.longitud);
		if (this.observacionesVivienda==null || this.observacionesVivienda.trim().isEmpty()) {
			oVivienda.setObservaciones(null);
		} else {
			oVivienda.setObservaciones(this.observacionesVivienda.trim());
		}

		oVivienda.setTipoVivienda(this.tipoViviendaSelected);
		if (this.vivienda.getEncuestas().isEmpty()) {
			oVivienda.setEncuestas(null);
		} else {
			oVivienda.setEncuestas(this.vivienda.getEncuestas());
		}

		if (this.vivienda.getRiesgos().isEmpty()) {
			oVivienda.setRiesgos(null);
		} else {
			oVivienda.setRiesgos(this.vivienda.getRiesgos());
		}
		
		if (this.vivienda.getParametros().isEmpty()) {
			oVivienda.setParametros(null);
		} else {
			oVivienda.setParametros(this.vivienda.getParametros());
		}
		
		if (this.manzanaSelected!=null) {
			ViviendaManzana oViviendaManzana = new ViviendaManzana();
			oViviendaManzana.setManzana(this.manzanaSelected);
			oViviendaManzana.setVivienda(oVivienda);
			oVivienda.setViviendaManzana(oViviendaManzana);
		} else {
			oVivienda.setViviendaManzana(null);
		}
		
		oVivienda.setComunidad(this.comunidadSelected);
		oVivienda.setCodigo(iCodigo);
		
		if (this.viviendaId!=0) {
			oVivienda.setViviendaId(this.viviendaId);
			oResultado=viviendaService.Guardar(oVivienda);
		} else {
			oVivienda.setUsuarioRegistro(this.infoSesion.getUsername());
			oVivienda.setFechaRegistro(Calendar.getInstance().getTime());
			oResultado=viviendaService.Agregar(oVivienda);
		}
		
		if (oResultado.isOk()){
			this.vivienda=oVivienda;
			this.viviendaId=oVivienda.getViviendaId();
			oResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
		
	}

	
	public void eliminar(ActionEvent pEvento){

		if (this.viviendaId==0) return;
		
		InfoResultado oResultado = new InfoResultado();
		oResultado=viviendaService.Eliminar(this.viviendaId);
		
		if (oResultado.isOk()){
			iniciarCapa2();
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

	public long getManzanaSelectedId() {
		return manzanaSelectedId;
	}

	public void setManzanaSelectedId(long manzanaSelectedId) {
		this.manzanaSelectedId = manzanaSelectedId;
	}

	public void setComunidadSelected(Comunidad comunidadSelected) {
		this.comunidadSelected = comunidadSelected;
	}

	public Comunidad getComunidadSelected() {
		return comunidadSelected;
	}

	public void setObservacionesVivienda(String observacionesVivienda) {
		this.observacionesVivienda = observacionesVivienda;
	}

	public String getObservacionesVivienda() {
		return observacionesVivienda;
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

	public int getNumRegistrosComunidades() {
		return numRegistrosComunidades;
	}

	public void setNumRegistrosComunidades(int numRegistrosComunidades) {
		this.numRegistrosComunidades = numRegistrosComunidades;
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

	public BigDecimal getHabitantes() {
		return habitantes;
	}

	public void setHabitantes(BigDecimal habitantes) {
		this.habitantes = habitantes;
	}

	public BigDecimal getHogares() {
		return hogares;
	}

	public void setHogares(BigDecimal hogares) {
		this.hogares = hogares;
	}

	public boolean isPasivo() {
		return pasivo;
	}

	public void setPasivo(boolean pasivo) {
		this.pasivo = pasivo;
	}

	public LazyDataModel<ViviendaUltimaEncuesta> getViviendasUltimaEncuesta() {
		return viviendasUltimaEncuesta;
	}

	public void setViviendasUltimaEncuesta(
			LazyDataModel<ViviendaUltimaEncuesta> viviendasUltimaEncuesta) {
		this.viviendasUltimaEncuesta = viviendasUltimaEncuesta;
	}

	public ViviendaUltimaEncuesta getViviendaUltimaEncuestaSelected() {
		return viviendaUltimaEncuestaSelected;
	}

	public void setViviendaUltimaEncuestaSelected(
			ViviendaUltimaEncuesta viviendaUltimaEncuestaSelected) {
		this.viviendaUltimaEncuestaSelected = viviendaUltimaEncuestaSelected;
	}

	public long getViviendaUltimaEncuestaId() {
		return viviendaUltimaEncuestaId;
	}

	public void setViviendaUltimaEncuestaId(long viviendaUltimaEncuestaId) {
		this.viviendaUltimaEncuestaId = viviendaUltimaEncuestaId;
	}

	public List<TipoVivienda> getTiposViviendas() {
		return tiposViviendas;
	}

	public void setTiposViviendas(List<TipoVivienda> tiposViviendas) {
		this.tiposViviendas = tiposViviendas;
	}

	public TipoVivienda getTipoViviendaSelected() {
		return tipoViviendaSelected;
	}

	public void setTipoViviendaSelected(TipoVivienda tipoViviendaSelected) {
		this.tipoViviendaSelected = tipoViviendaSelected;
	}

	public long getTipoViviendaSelectedId() {
		return tipoViviendaSelectedId;
	}

	public void setTipoViviendaSelectedId(long tipoViviendaSelectedId) {
		this.tipoViviendaSelectedId = tipoViviendaSelectedId;
	}

	public Vivienda getVivienda() {
		return vivienda;
	}

	public void setVivienda(Vivienda vivienda) {
		this.vivienda = vivienda;
	}

	public long getViviendaId() {
		return viviendaId;
	}

	public void setViviendaId(long viviendaId) {
		this.viviendaId = viviendaId;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public ViviendaEncuesta getViviendaEncuestaSelected() {
		return viviendaEncuestaSelected;
	}

	public void setViviendaEncuestaSelected(
			ViviendaEncuesta viviendaEncuestaSelected) {
		this.viviendaEncuestaSelected = viviendaEncuestaSelected;
	}

	public String getFechaEncuesta() {
		return fechaEncuesta;
	}

	public void setFechaEncuesta(String fechaEncuesta) {
		this.fechaEncuesta = fechaEncuesta;
	}

	public String getJefeFamilia() {
		return jefeFamilia;
	}

	public void setJefeFamilia(String jefeFamilia) {
		this.jefeFamilia = jefeFamilia;
	}

	public CondicionOcupacion getCondicionOcupacionSelected() {
		return condicionOcupacionSelected;
	}

	public void setCondicionOcupacionSelected(CondicionOcupacion condicionOcupacionSelected) {
		this.condicionOcupacionSelected = condicionOcupacionSelected;
	}

	public String getObservacionesEncuesta() {
		return observacionesEncuesta;
	}

	public void setObservacionesEncuesta(String observacionesEncuesta) {
		this.observacionesEncuesta = observacionesEncuesta;
	}

	public ViviendaRiesgo getViviendaRiesgoSelected() {
		return viviendaRiesgoSelected;
	}

	public void setViviendaRiesgoSelected(ViviendaRiesgo viviendaRiesgoSelected) {
		this.viviendaRiesgoSelected = viviendaRiesgoSelected;
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

	public long getFactorRiesgoSelectedId() {
		return factorRiesgoSelectedId;
	}

	public void setFactorRiesgoSelectedId(long factorRiesgoSelectedId) {
		this.factorRiesgoSelectedId = factorRiesgoSelectedId;
	}

	public List<NivelRiesgo> getNivelesRiesgos() {
		return nivelesRiesgos;
	}

	public void setNivelesRiesgos(List<NivelRiesgo> nivelesRiesgos) {
		this.nivelesRiesgos = nivelesRiesgos;
	}

	public NivelRiesgo getNivelRiesgoSelected() {
		return nivelRiesgoSelected;
	}

	public void setNivelRiesgoSelected(NivelRiesgo nivelRiesgoSelected) {
		this.nivelRiesgoSelected = nivelRiesgoSelected;
	}

	public long getNivelRiesgoSelectedId() {
		return nivelRiesgoSelectedId;
	}

	public void setNivelRiesgoSelectedId(long nivelRiesgoSelectedId) {
		this.nivelRiesgoSelectedId = nivelRiesgoSelectedId;
	}

	public String getFechaTomaRiesgo() {
		return fechaTomaRiesgo;
	}

	public void setFechaTomaRiesgo(String fechaTomaRiesgo) {
		this.fechaTomaRiesgo = fechaTomaRiesgo;
	}

	public String getObservacionesRiesgo() {
		return observacionesRiesgo;
	}

	public void setObservacionesRiesgo(String observacionesRiesgo) {
		this.observacionesRiesgo = observacionesRiesgo;
	}

	public ViviendaParametroEpidemiologico getParametroEpidemiologicoViviendaSelected() {
		return parametroEpidemiologicoViviendaSelected;
	}

	public void setParametroEpidemiologicoViviendaSelected(
			ViviendaParametroEpidemiologico parametroEpidemiologicoViviendaSelected) {
		this.parametroEpidemiologicoViviendaSelected = parametroEpidemiologicoViviendaSelected;
	}

	public long getParametroEpidemiologicoSelectedId() {
		return parametroEpidemiologicoSelectedId;
	}

	public void setParametroEpidemiologicoSelectedId(
			long parametroEpidemiologicoSelectedId) {
		this.parametroEpidemiologicoSelectedId = parametroEpidemiologicoSelectedId;
	}

	public BigDecimal getValorParametro() {
		return valorParametro;
	}

	public void setValorParametro(BigDecimal valorParametro) {
		this.valorParametro = valorParametro;
	}

	public int getCapaActiva() {
		return capaActiva;
	}

	public void setCapaActiva(int capaActiva) {
		this.capaActiva = capaActiva;
	}

	public void setCondicionesOcupacion(List<CondicionOcupacion> condicionesOcupacion) {
		this.condicionesOcupacion = condicionesOcupacion;
	}

	public List<CondicionOcupacion> getCondicionesOcupacion() {
		return condicionesOcupacion;
	}

	public void setCondicionOcupacionSelectedId(long condicionOcupacionSelectedId) {
		this.condicionOcupacionSelectedId = condicionOcupacionSelectedId;
	}

	public long getCondicionOcupacionSelectedId() {
		return condicionOcupacionSelectedId;
	}

	public void setFiltroVivienda(String filtroVivienda) {
		this.filtroVivienda = filtroVivienda;
	}

	public String getFiltroVivienda() {
		return filtroVivienda;
	}

	public void setNumRegistrosViviendas(int numRegistrosViviendas) {
		this.numRegistrosViviendas = numRegistrosViviendas;
	}

	public int getNumRegistrosViviendas() {
		return numRegistrosViviendas;
	}

	public void setFechaTomaParametro(String fechaTomaParametro) {
		this.fechaTomaParametro = fechaTomaParametro;
	}

	public String getFechaTomaParametro() {
		return fechaTomaParametro;
	}

	public void setObservacionesParametro(String observacionesParametro) {
		this.observacionesParametro = observacionesParametro;
	}

	public String getObservacionesParametro() {
		return observacionesParametro;
	}

	public void setParametrosEpidemiologicos(
			List<ParametroEpidemiologico> parametrosEpidemiologicos) {
		this.parametrosEpidemiologicos = parametrosEpidemiologicos;
	}

	public List<ParametroEpidemiologico> getParametrosEpidemiologicos() {
		return parametrosEpidemiologicos;
	}

	public void setParametroEpidemiologicoSelected(ParametroEpidemiologico parametroEpidemiologicoSelected) {
		this.parametroEpidemiologicoSelected = parametroEpidemiologicoSelected;
	}

	public ParametroEpidemiologico getParametroEpidemiologicoSelected() {
		return parametroEpidemiologicoSelected;
	}

	public Map<String, Object> getValoresParametro() {
		return valoresParametro;
	}

	public void setValoresParametro(Map<String, Object> valoresParametro) {
		this.valoresParametro = valoresParametro;
	}

	public String getValorParametroSelected() {
		return valorParametroSelected;
	}

	public void setValorParametroSelected(String valorParametroSelected) {
		this.valorParametroSelected = valorParametroSelected;
	}

	public String getCentroMapa() {
		return centroMapa;
	}

	public void setCentroMapa(String centroMapa) {
		this.centroMapa = centroMapa;
	}

	public MapModel getMapaModelo() {
		return mapaModelo;
	}

	public void setMapaModelo(MapModel mapaModelo) {
		this.mapaModelo = mapaModelo;
	}

	public Marker getMarcadorMapa() {
		return marcadorMapa;
	}

	public void setMarcadorMapa(Marker marcadorMapa) {
		this.marcadorMapa = marcadorMapa;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public String getCoordenadasPais() {
		return coordenadasPais;
	}

	public void setCoordenadasPais(String coordenadasPais) {
		this.coordenadasPais = coordenadasPais;
	}

	public void setUrlMarcadorCasaConRiesgo(String urlMarcadorCasaConRiesgo) {
		this.urlMarcadorCasaConRiesgo = urlMarcadorCasaConRiesgo;
	}

	public String getUrlMarcadorCasaConRiesgo() {
		return urlMarcadorCasaConRiesgo;
	}

	public void setUrlMarcadorCasaSinRiesgo(String urlMarcadorCasaSinRiesgo) {
		this.urlMarcadorCasaSinRiesgo = urlMarcadorCasaSinRiesgo;
	}

	public String getUrlMarcadorCasaSinRiesgo() {
		return urlMarcadorCasaSinRiesgo;
	}
}
