package ni.gob.minsa.malaria.interfaz.encuestas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.encuestas.CriaderoDA;
import ni.gob.minsa.malaria.datos.encuestas.CriaderoEspecieDA;
import ni.gob.minsa.malaria.datos.encuestas.IntervencionDA;
import ni.gob.minsa.malaria.datos.encuestas.PesquisaDA;
import ni.gob.minsa.malaria.datos.encuestas.PosInspeccionDA;
import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.datos.general.CatalogoElementoDA;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadDA;
import ni.gob.minsa.malaria.modelo.encuesta.AbundanciaFauna;
import ni.gob.minsa.malaria.modelo.encuesta.AbundanciaVegetacion;
import ni.gob.minsa.malaria.modelo.encuesta.AddCatalogo;
import ni.gob.minsa.malaria.modelo.encuesta.AddSistemaCatalogo;
import ni.gob.minsa.malaria.modelo.encuesta.ClasesCriaderos;
import ni.gob.minsa.malaria.modelo.encuesta.Criadero;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosEspecie;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosIntervencion;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosPesquisa;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosPosInspeccion;
import ni.gob.minsa.malaria.modelo.encuesta.EspeciesAnopheles;
import ni.gob.minsa.malaria.modelo.encuesta.ExposicionSol;
import ni.gob.minsa.malaria.modelo.encuesta.MovimientoAgua;
import ni.gob.minsa.malaria.modelo.encuesta.TiposCriaderos;
import ni.gob.minsa.malaria.modelo.encuesta.TurbidezAgua;
import ni.gob.minsa.malaria.modelo.encuesta.noEntidad.CriaderosUltimaNotificacion;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Sector;
import ni.gob.minsa.malaria.servicios.encuestas.CriaderoServices;
import ni.gob.minsa.malaria.servicios.encuestas.CriaderosEspecieServices;
import ni.gob.minsa.malaria.servicios.encuestas.IntervencionServices;
import ni.gob.minsa.malaria.servicios.encuestas.PesquisaServices;
import ni.gob.minsa.malaria.servicios.encuestas.PosInspeccionServices;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.soporte.CalendarioEPI;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

@ManagedBean
@ViewScoped
public class EncuestaEntomologicaBean implements Serializable {
	
	private static final long serialVersionUID = -4578628355488302498L;

	/*
	 * Declaracion de Servicios
	 */
	private static CriaderoServices  srvCriadero = new CriaderoDA();
	private static IntervencionServices srvIntervencion = new IntervencionDA();
	private static PesquisaServices srvPesquisa = new PesquisaDA();
	private static PosInspeccionServices srvPosInspeccion = new PosInspeccionDA();
	private static CriaderosEspecieServices srvCriaderoEspecie = new CriaderoEspecieDA();
	private static EntidadAdtvaService srvEntidadAdtva = new EntidadAdtvaDA();
	private static ComunidadService srvComunidad = new ComunidadDA();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<AbundanciaFauna,Integer> srvCatAbndFau = new CatalogoElementoDA(AbundanciaFauna.class,"AbundanciaFauna");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<AbundanciaVegetacion,Integer> srvCatAbndVgt = new CatalogoElementoDA(AbundanciaVegetacion.class,"AbundanciaVegetacion");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<ClasesCriaderos,Integer> srvCatClasesCriadero = new CatalogoElementoDA(ClasesCriaderos.class,"ClasesCriaderos");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<EspeciesAnopheles,Integer> srvCatEspAnopheles = new CatalogoElementoDA(EspeciesAnopheles.class,"EspeciesAnopheles");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<ExposicionSol,Integer> srvCatExpSol = new CatalogoElementoDA(ExposicionSol.class,"ExposicionSol");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<MovimientoAgua,Integer> srvCatMovAgua = new CatalogoElementoDA(MovimientoAgua.class,"MovimientoAgua");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TiposCriaderos,Integer> srvCatTipoCriadero = new CatalogoElementoDA(TiposCriaderos.class,"TiposCriaderos");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TurbidezAgua,Integer> srvCatTurbAgua = new CatalogoElementoDA(TurbidezAgua.class,"TurbidezAgua");
	
	
	/*
	 * Propiedades de la vista para el manejo de los datos del criadero
	 * 
	 */
	
	/*
	 * Semanana Epidemiologica manejado 
	 * Datos registrados en Entidad CriaderoPesquisa
	 * {@link CriaderosPesquisa}
	 */
	

	/*
	 * Identificacion del Criadero
	 */
	private Criadero oCriaderoActual = null;
	private String frmInp_CodigoCriadero;
	private short criaderoSelected = 0;
	
	/* 
	 * Datos geograficos del criadero
	 */
	private BigDecimal frmInp_Latitud;
	private BigDecimal frmInp_Longitud;
	
	/*
	 * Datos generales del criadero
	 */
	private Long frmSom_SilaisUbicaCriadero;
	private String frmSom_MunicipioUbicaCriadero;
	private Comunidad frmInp_ComunidadUbicaCriadero;
	private String frmInp_DireccionCriadero;
	
	private String frmInp_NombreCriadero;
	private Long frmSom_TipoCriadero;
	
	private BigDecimal frmInp_DistMaxCasaProxima;
	private BigDecimal frmInp_AreaActualCriadero;
	
	private Long frmSom_ClasificacionCriadero;

	
	/*
	 * Caracteristicas del Criadero
	 */
	private Long frmSom_VegVertical;
	private Long frmSom_VegEmergente;
	private Long frmSom_VegFlotante;
	private Long frmSom_VegSumergida;
	
	private Long frmSom_FaunaInsectos;
	private Long frmSom_FaunaPeces;
	private Long frmSom_FaunaAnfibios;
	
	private Long frmSom_TurbidezAgua;
	private Long frmSom_MovimientoAgua;
	private Long frmSom_ExposicionSol;
	
	private BigDecimal frmInp_Ph;
	private BigDecimal frmInp_Temperatura;
	private BigDecimal frmInp_Cloro;
	
	private Date frmCal_FechaTomaDatos;
	
	private List<Long> frmSom_EspecieAnopheles;
	
	private String frmInp_Observaciones;

	/*
	 * Variables de manejo de registro de Pesquisas
	 */
	
	private short pesquisaSelected = 0;
	
	private Integer frmInp_Pesq_SemanaEpidemiologica;
	private Integer frmInp_Pesq_AxoEpidemiologico;	
	private Date frmCal_Pesq_FechaInspeccion;
	private Date frmCal_Pesq_FechaNotificacion;
	private String frmInp_Pesq_Inspector;
	private Short frmInp_Pesq_PuntosMuestreados;
	private Short frmInp_Pesq_TotalCucharonadas;
	private Short frmInp_Pesq_CucharonadasPositivas;
	private Short frmInp_Pesq_NumLarvJovEstaIyII;
	private Short frmInp_Pesq_NumLarvAduEstaIIIyIV;
	private Short frmInp_Pesq_NumPupas;
	private Double frmInp_Pesq_DensLarvJovEstaIyII;
	private Double frmInp_Pesq_DensLarvAduEstaIIIyIV;
	private Double frmInp_Pesq_DensPupas;
	private String frmInp_Pesq_Observaciones;
	
	private List<CriaderosPesquisa> frmDt_ListaPesquisas = null;
	
	private CriaderosPesquisa oSelPesquisa = null;
	
	/*
	 * Variables de manejo de registro de intervenciones
	 */
	
	private short intervencionSelected = 0;
	
	private Date frmCal_Intv_FechaIntervencion;
	private Long frmInp_Intv_Limpieza;
	private Long frmInp_Intv_Eva;
	private Long frmInp_Intv_Drenaje;
	private Long frmInp_Intv_Relleno;
	private Long frmInp_Intv_Bsphaericus;
	private Long frmInp_Intv_Bti;
	private Long frmInp_Intv_SiembraPeces;
	private Long frmInp_Intv_ConsumoBsphaericus;
	private Long frmInp_Intv_ConsumoBti;
	private String frmInp_Intv_Observaciones;
	
	private List<CriaderosIntervencion> frmDt_ListaIntervencion = null;
	private List<CriaderosIntervencion> frmDt_ListaIntervencionEliminar = null;
	
	private CriaderosIntervencion oSelIntervencion = null;
	
	/*
	 * Variables de manejo de registro de PosInspeccion
	 */
	
	private short posInspeccionSelected = 0;
	
	private Date frmCal_PosIns_FechaInspeccion;
	private Short frmInp_PosIns_PuntosMuestreados;
	private Short frmInp_PosIns_TotalCucharonadas;
	private Short frmInp_PosIns_CucharonadasPositivas;
	private Short frmInp_PosIns_NumLarvJovEstaIyII;
	private Short frmInp_PosIns_NumLarvAduEstaIIIyIV;
	private Short frmInp_PosIns_NumPupas;
	private Double frmInp_PosIns_DensLarvJovEstaIyII;
	private Double frmInp_PosIns_DensLarvAduEstaIIIyIV;
	private Double frmInp_PosIns_DensPupas;
	private String frmInp_PosIns_Observaciones;
 
	private List<CriaderosPosInspeccion> frmDt_ListaPosInspeccion = null;
	private CriaderosPosInspeccion oSelPosInspeccion = null;
	
	/*
	 * Variables de manejo de datos de Inspeccion e Inspector
	 * Datos registrados en Entidad CriaderoPesquisa
	 * {@link CriaderosPesquisa
	 * 
	 */
	private Date frmCal_FechaNotificacion;
	private String frmInp_Inspector;
	
	
	
	/*
	 * Variables de poblado de Elementos de controles de formularios
	 */
	
	private List<EntidadAdtva> itemsSilais = null;
	private List<DivisionPolitica> itemsMunicipioBusqueda = null;
	private List<DivisionPolitica> itemsMunicipio = null;
	private List<AbundanciaFauna> itemsAbundanciaFauna = null;
	private List<AbundanciaVegetacion> itemsAbundanciaVegetacion = null;
	private List<ClasesCriaderos> itemsClasesCriaderos = null;
	private List<CriaderosEspecie> itemsCriaderoEspecies = null;
	private List<EspeciesAnopheles> itemsEspeciesAnopheles = null;
	private List<ExposicionSol> itemsExposicionSols = null;
	private List<MovimientoAgua> itemsMovimientoAguas = null;
	private List<TiposCriaderos> itemsTiposCriaderos = null;
	private List<TurbidezAgua> itemsTurbidezAguas = null;
		
	/*
	 * Variables para el menejo de resultados de busquedas
	 */
	private long frmSom_SilaisBusqueda = 0;
	private String frmSom_MunicipioBusqueda = "0";
	private Comunidad frmInp_ComunidadBusqueda = null;
	private LazyDataModel<CriaderosUltimaNotificacion> lazyCriaderos = null;
	
	
	/*
	 * Variables de identificacion del criadero en encuesta
	 */
	private String frmInp_CodCriaderoEnc;
	private String frmInp_DatosCriaderoEnc;
	private String frmInp_UbicacionEnc;
	private BigDecimal frmInp_DistMaxCasaEnc;
	private BigDecimal frmInp_AreaEnc;
	
	
	/*
	 * Variables de manejos de identificadores componentUI 
	 * para apertura de modales e identificacion de operaciones
	 * sobre componentes especificos
	 */
	private String componenteIdCatOtros;
	private String frmInp_OtroValorCatalogo;
	
	private short panelBusqueda = 0;
	private short panelEncuesta = 0;
	private short panelCriadero = 0;
	private short cmbRegresar = 0;
	private short cmbGuardar = 0;
	private short cmbNuevo = 0;
	
	private boolean isCriaderoActive = false;
	private boolean isEncuestaActive = false;
	private boolean isEditPesquisa = false;
	
	public EncuestaEntomologicaBean() {
		
		this.itemsAbundanciaFauna = srvCatAbndFau.ListarActivos();
		this.itemsAbundanciaVegetacion = srvCatAbndVgt.ListarActivos();
		this.itemsClasesCriaderos = srvCatClasesCriadero.ListarActivos();
		this.itemsEspeciesAnopheles = srvCatEspAnopheles.ListarActivos();
		this.itemsExposicionSols = srvCatExpSol.ListarActivos();
		this.itemsMovimientoAguas = srvCatMovAgua.ListarActivos();
		this.itemsTiposCriaderos = srvCatTipoCriadero.ListarActivos();
		this.itemsTurbidezAguas = srvCatTurbAgua.ListarActivos();
		
		this.itemsSilais = ni.gob.minsa.malaria.reglas.Operacion.entidadesAutorizadas(Utilidades.obtenerInfoSesion().getUsuarioId(),false);
		this.itemsCriaderoEspecies = srvCriaderoEspecie.obtenerListaCriaderosEspecies();
		
		this.lazyCriaderos = new LazyDataModel<CriaderosUltimaNotificacion>() {
			
			@Override
			public List<CriaderosUltimaNotificacion> load(int pPage, int pRows, String pSortField, SortOrder pSortOrder,
					Map<String, String> pArgs) {
				InfoResultado oResultado = new InfoResultado();
				List<CriaderosUltimaNotificacion> resultado = null;
				
				if( frmInp_ComunidadBusqueda != null )
					oResultado = srvCriadero.obtenerCriaderosDto(pPage, pRows, pSortField, pSortOrder, frmInp_ComunidadBusqueda);
				else if( !frmSom_MunicipioBusqueda.equals("0") )
					oResultado = srvCriadero.obtenerCriaderosDto(pPage, pRows, pSortField, pSortOrder, frmSom_MunicipioBusqueda);
				else if( frmSom_SilaisBusqueda > 0 )
					oResultado = srvCriadero.obtenerCriaderosDto(pPage, pRows, pSortField, pSortOrder, frmSom_SilaisBusqueda);
				else oResultado.setOk(false);
				
				if( oResultado.isOk() && oResultado.getObjeto() != null ){
					this.setRowCount(oResultado.getFilasAfectadas());
					resultado = (List<CriaderosUltimaNotificacion>) oResultado.getObjeto();
				}else{
					this.setRowCount(0);
				}

				
				return resultado;
			}
		};
		
		
	}
	
	private void limpiarDatosPanelCriadero(){
		frmInp_CodigoCriadero = null;
		frmInp_Latitud = null;
		frmInp_Longitud = null;
		frmSom_SilaisUbicaCriadero = null;
		frmSom_MunicipioUbicaCriadero = null;
		frmInp_ComunidadUbicaCriadero = null;
		frmInp_DireccionCriadero = null;
		frmInp_NombreCriadero = null;
		frmSom_TipoCriadero = null;
		frmInp_DistMaxCasaProxima = null;
		frmInp_AreaActualCriadero = null;
		frmSom_ClasificacionCriadero = null;
		frmSom_VegVertical = null;
		frmSom_VegEmergente = null;
		frmSom_VegFlotante = null;
		frmSom_VegSumergida = null;
		frmSom_FaunaInsectos = null;
		frmSom_FaunaPeces = null;
		frmSom_FaunaAnfibios = null;
		frmSom_TurbidezAgua = null;
		frmSom_MovimientoAgua = null;
		frmSom_ExposicionSol = null;
		frmInp_Ph = null;
		frmInp_Temperatura = null;
		frmInp_Cloro = null;
		frmCal_FechaTomaDatos = null;
		frmSom_EspecieAnopheles = null;
		frmInp_Observaciones = null;
		frmInp_OtroValorCatalogo = null;
		criaderoSelected = 0;
	}
		
	private void limpiarDatosModalPesquisa(){
		frmInp_Pesq_SemanaEpidemiologica = null;
		frmInp_Pesq_AxoEpidemiologico = null;
		frmCal_Pesq_FechaInspeccion = null;
		frmInp_Pesq_PuntosMuestreados = null;
		frmInp_Pesq_TotalCucharonadas = null;
		frmInp_Pesq_CucharonadasPositivas = null;
		frmInp_Pesq_NumLarvJovEstaIyII = null;
		frmInp_Pesq_NumLarvAduEstaIIIyIV = null;
		frmInp_Pesq_NumPupas = null;
		frmInp_Pesq_DensLarvJovEstaIyII = null;
		frmInp_Pesq_DensLarvAduEstaIIIyIV = null;
		frmInp_Pesq_DensPupas = null;
		frmInp_Pesq_Observaciones = null;	
		frmCal_Pesq_FechaNotificacion = null;
		frmInp_Pesq_Inspector = null;
		pesquisaSelected = 0;
	}
	
	private void limpiarDatosModalIntervencion(){
		frmCal_Intv_FechaIntervencion = null;
		frmInp_Intv_Limpieza = null;
		frmInp_Intv_Eva = null;
		frmInp_Intv_Drenaje = null;
		frmInp_Intv_Relleno = null;
		frmInp_Intv_Bsphaericus = null;
		frmInp_Intv_Bti = null;
		frmInp_Intv_SiembraPeces = null;
		frmInp_Intv_ConsumoBsphaericus = null;
		frmInp_Intv_ConsumoBti = null;
		frmInp_Intv_Observaciones = null;
		intervencionSelected = 0;
	}		
	
	private void limpiarDatosModalPosInspeccion(){
		frmCal_PosIns_FechaInspeccion = null;
		frmInp_PosIns_PuntosMuestreados = null;
		frmInp_PosIns_TotalCucharonadas = null;
		frmInp_PosIns_CucharonadasPositivas = null;
		frmInp_PosIns_NumLarvJovEstaIyII = null;
		frmInp_PosIns_NumLarvAduEstaIIIyIV = null;
		frmInp_PosIns_NumPupas = null;
		frmInp_PosIns_DensLarvJovEstaIyII = null;
		frmInp_PosIns_DensLarvAduEstaIIIyIV = null;
		frmInp_PosIns_DensPupas = null;
		frmInp_PosIns_Observaciones = null;
		posInspeccionSelected = 0;
	}
	
	private void limpiarDatosPanelEncuesta(){
		frmInp_CodCriaderoEnc = null;
		frmInp_DatosCriaderoEnc = null;
		frmInp_UbicacionEnc = null;
		frmInp_DistMaxCasaEnc = null;
		frmInp_AreaEnc = null;	
		frmDt_ListaPesquisas = null;
		
		criaderoSelected = 0;
		pesquisaSelected = 0;
		intervencionSelected = 0;
		posInspeccionSelected = 0;
		
	}
	
	private void limpiarDatosPanelBusqueda(){
		itemsMunicipio = null;
		lazyCriaderos = null;
		frmSom_SilaisBusqueda = 0;
		frmSom_MunicipioBusqueda = "0";
		frmInp_ComunidadBusqueda = null;
	}
	
	public void limpiarFormulario(){
		
		oCriaderoActual = null;
		oSelPesquisa = null;
		oSelIntervencion = null;
		oSelPosInspeccion = null;
		
		// limpieza de panel formulario criaderos
		limpiarDatosPanelCriadero();
		
		// limpieda de modal dato captura pesquisa
		limpiarDatosModalPesquisa();
		
		// limpiesa de modal datos captura intervencion
		limpiarDatosModalIntervencion();

		// limpiesa de modal datos captura posinspeccion
		limpiarDatosModalPosInspeccion();
		
		// limpiesa datos panel encuesta
		limpiarDatosPanelEncuesta();
		
		// limpieza datos panel busqueda
		//limpiarDatosPanelBusqueda();
	}

	@SuppressWarnings("unchecked")
	public void actualizarMunicipiosBusqueda(){
		InfoResultado oResultado = new InfoResultado();
		try{
			
			oResultado = srvCriadero.obtenerMunicipiosPorSilais(frmSom_SilaisBusqueda);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				itemsMunicipioBusqueda = (List<DivisionPolitica>) oResultado.getObjeto();
			}else{
				itemsMunicipioBusqueda = null;
			}
			
		}catch(Exception e){
			System.out.println("Error Obteniendo Listado Municipios Por Silais");
			e.printStackTrace();
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void actualizarMunicipiosUbicacion(){
		InfoResultado oResultado = new InfoResultado();
		try{
			
			oResultado = srvCriadero.obtenerMunicipiosPorSilais(frmSom_SilaisUbicaCriadero);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				itemsMunicipio = (List<DivisionPolitica>) oResultado.getObjeto();
			}else{
				itemsMunicipio = null;
			}
			
		}catch(Exception e){
			System.out.println("Error Obteniendo Listado Municipios Por Silais");
			e.printStackTrace();
		}
		
	}

	public List<Comunidad> completarComunidadBusqueda(String query){
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades = srvComunidad.ComunidadesPorMunicipioNombre(frmSom_MunicipioBusqueda, query, 10);
		return oComunidades;		
	}	
	
	public List<Comunidad> completarComunidad(String query){
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades = srvComunidad.ComunidadesPorMunicipioNombre(frmSom_MunicipioUbicaCriadero, query, 10);
		return oComunidades;		
	}
		
	private Double calcularDensidad(Short pNumLarvas, Short pNumCucharonadas){
		if( pNumLarvas == null || pNumCucharonadas == null )
			return null;
				
		return ( pNumLarvas / ( 0.0055 * pNumCucharonadas) );
	}
	

	/**
	 * Metodos para el manejo de criaderos
	 */
	
	public void setSeleccionCriadero(CriaderosUltimaNotificacion pCri){
		InfoResultado oResultado = null;
		
		oResultado = srvCriadero.obtenerCriaderoPorId(pCri.getCriaderoId());
		if( oResultado.isOk() && oResultado.getObjeto() != null )
			this.oCriaderoActual = (Criadero) oResultado.getObjeto();
		else
			this.oCriaderoActual = null;
		actualizarDatosPanelEncuesta();
	}
	
	public void agregarCriadero(ActionEvent evt){
		InfoResultado oResultado = null;
		
		oResultado = validarFormularioCriadero();
		if( !oResultado.isOk() || oResultado.isExcepcion() ){
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		oResultado = agregarCriadero();
		if( !oResultado.isOk() || oResultado.isExcepcion() ){
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		actualizarDatosPanelEncuesta();
		criaderoSelected = 1;
		RequestContext.getCurrentInstance().execute("dlgCriadero.hide();");
	}
	
	private InfoResultado validarFormularioCriadero(){
		InfoResultado oResultado = new InfoResultado();
		
		oResultado.setOk(true);

		if( frmInp_CodigoCriadero == null || frmInp_CodigoCriadero.isEmpty()){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Codigo Criadero");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		

		if( frmInp_Latitud == null || frmInp_Latitud.equals("0")){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Coordenada Latitud");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		

		if( frmInp_Longitud == null || frmInp_Longitud.equals("0")){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Coordenada Longitud");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmSom_SilaisUbicaCriadero == null || frmSom_SilaisUbicaCriadero.equals("0")){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Silais de Ubicacion");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}	
		
		if( frmSom_MunicipioUbicaCriadero == null || frmSom_MunicipioUbicaCriadero.equals("0")){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Municipio de Ubicacion");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}	

		if( frmInp_ComunidadUbicaCriadero == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Comunidad de Ubicacion");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		

		if( frmInp_DireccionCriadero == null || frmInp_DireccionCriadero.isEmpty() ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Direccion de Ubicacion");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		

		if( frmInp_NombreCriadero == null || frmInp_NombreCriadero.isEmpty() ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Nombre del Criadero");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		

		if( frmSom_TipoCriadero == null || frmSom_TipoCriadero.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Tipo de Criadero");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}				

		if( frmInp_DistMaxCasaProxima == null || frmInp_DistMaxCasaProxima.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar distancia a casa mas proxima de Criadero");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}					

		if( frmInp_AreaActualCriadero == null || frmInp_AreaActualCriadero.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Area actual del Criadero");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmSom_ClasificacionCriadero == null || frmSom_ClasificacionCriadero.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Clasificacion del Criadero");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}				
		
		if( frmSom_ClasificacionCriadero == null || frmSom_ClasificacionCriadero == 999999 ){
			oResultado.setOk(false);
			oResultado.setMensaje("Opcion Clasificacion criadero otro no permitido");
			oResultado.setMensajeDetalle("Agregar otra clasificacion a traves de ventana emergente solicitada por el sistema");
			oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
			return oResultado;			
		}
		
		if( frmSom_VegVertical == null || frmSom_VegVertical.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Tipo y Abundancia relativa de vegetacion vertical");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmSom_VegEmergente == null || frmSom_VegEmergente.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Tipo y Abundancia relativa de vegetacion emergente");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmSom_VegFlotante == null || frmSom_VegFlotante.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Tipo y Abundancia relativa de vegetacion flotante");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}				
		
		if( frmSom_VegSumergida == null || frmSom_VegSumergida.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Tipo y Abundancia relativa de vegetacion sumergida");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}						
		
		if( frmSom_FaunaInsectos == null || frmSom_FaunaInsectos.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Fauna depredadora y Abundancia relativa a insectos");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmSom_FaunaPeces == null || frmSom_FaunaPeces.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Fauna depredadora y Abundancia relativa a peces");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmSom_FaunaAnfibios == null || frmSom_FaunaAnfibios.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Fauna depredadora y Abundancia relativa de anfibios");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmSom_TurbidezAgua == null || frmSom_TurbidezAgua.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Turbidez del agua");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmSom_MovimientoAgua == null || frmSom_MovimientoAgua.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Movimiento del agua");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmSom_ExposicionSol == null || frmSom_ExposicionSol.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Exposicion al Sol");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}

		if( frmInp_Ph == null || frmInp_Ph.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Ph del agua");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		

		if( frmInp_Temperatura == null || frmInp_Temperatura.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Temperatura del agua");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		

		if( frmInp_Cloro == null || frmInp_Temperatura.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Cloro del agua");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}

		if( frmCal_FechaTomaDatos == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Fecha de toma de datos");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		
		if( frmSom_EspecieAnopheles == null || frmSom_EspecieAnopheles.isEmpty() ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Especies Anopheles");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		String str;
		for(int i=0 ; i < frmSom_EspecieAnopheles.size(); i++){
			str = String.valueOf(frmSom_EspecieAnopheles.get(i));
			if( str.equals("999999")){
				oResultado.setOk(false);
				oResultado.setMensaje("Opcion Especie Anopheles Otro no permitida");
				oResultado.setMensajeDetalle("Agregar otra especie a traves de ventana emergente solicitada por el sistema");
				oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
				return oResultado;
			}
		}
		
		if( frmInp_Observaciones == null || frmInp_Observaciones.isEmpty() ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Observaciones");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
				
		return oResultado;
	}
	
	private InfoResultado agregarCriadero(){
		InfoResultado oResultado = new InfoResultado();
		
		try{
		
			if( oCriaderoActual == null ) oCriaderoActual = new Criadero();
			
			oCriaderoActual.setCodigo(frmInp_CodigoCriadero);
			oCriaderoActual.setAreaActual(frmInp_AreaActualCriadero);
			oCriaderoActual.setAreaMax(frmInp_DistMaxCasaProxima);
			
			oResultado = srvCatClasesCriadero.Encontrar(frmSom_ClasificacionCriadero);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				ClasesCriaderos oClaseCriadero = (ClasesCriaderos) oResultado.getObjeto();
				oCriaderoActual.setClaseCriadero(oClaseCriadero);
			}else{
				return oResultado;
			}
			
			oCriaderoActual.setCloro(frmInp_Cloro);
			oCriaderoActual.setComunidad(frmInp_ComunidadUbicaCriadero);
			oCriaderoActual.setDireccion(frmInp_DireccionCriadero);
			oCriaderoActual.setDistanciaCasa(frmInp_DistMaxCasaProxima);
	
			oResultado = null;
			oResultado = srvCatExpSol.Encontrar(frmSom_ExposicionSol);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				ExposicionSol oExposicionSol = (ExposicionSol) oResultado.getObjeto();
				oCriaderoActual.setExposicionSol(oExposicionSol);
			}else{
				return oResultado;
			}		
	
			oResultado = null;
			AbundanciaFauna oAbundanciaFauna = null;
	
			oResultado = srvCatAbndFau.Encontrar(frmSom_FaunaAnfibios);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbundanciaFauna = (AbundanciaFauna) oResultado.getObjeto();
				oCriaderoActual.setFaunaAnfibios(oAbundanciaFauna);
			}else{
				return oResultado;
			}		
			
			oResultado = srvCatAbndFau.Encontrar(frmSom_FaunaInsectos);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbundanciaFauna = (AbundanciaFauna) oResultado.getObjeto();
				oCriaderoActual.setFaunaInsecto(oAbundanciaFauna);
			}else{
				return oResultado;
			}			
			
			oResultado = srvCatAbndFau.Encontrar(frmSom_FaunaPeces);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbundanciaFauna = (AbundanciaFauna) oResultado.getObjeto();
				oCriaderoActual.setFaunaPeces(oAbundanciaFauna);
			}else{
				return oResultado;
			}			
			
			oCriaderoActual.setFechaDatos(frmCal_FechaTomaDatos);
			oCriaderoActual.setLatitud(frmInp_Latitud);
			oCriaderoActual.setLongitud(frmInp_Longitud);
			
			oResultado = null;
			oResultado = srvCatMovAgua.Encontrar(frmSom_MovimientoAgua);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				MovimientoAgua oMovAgua = (MovimientoAgua) oResultado.getObjeto();
				oCriaderoActual.setMovimientoAgua(oMovAgua);
			}else{
				return oResultado;
			}		
			
			oCriaderoActual.setNombre(frmInp_NombreCriadero);
			oCriaderoActual.setObservaciones(frmInp_Observaciones);
			oCriaderoActual.setPh(frmInp_Ph);
			oCriaderoActual.setTemperatura(frmInp_Temperatura);
	
			oResultado = null;
			oResultado = srvCatTipoCriadero.Encontrar(frmSom_TipoCriadero);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				TiposCriaderos oTipCriadero = (TiposCriaderos) oResultado.getObjeto();
				oCriaderoActual.setTipoCriadero(oTipCriadero);
			}else{
				return oResultado;
			}			
	
			oResultado = null;
			oResultado = srvCatTurbAgua.Encontrar(frmSom_TurbidezAgua);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				TurbidezAgua oTurbAgua = (TurbidezAgua) oResultado.getObjeto();
				oCriaderoActual.setTurbidezAgua(oTurbAgua);
			}else{
				return oResultado;
			}		
			
			oResultado = null;
			AbundanciaVegetacion oAbunVeg = null;
			oResultado = srvCatAbndVgt.Encontrar(frmSom_VegEmergente);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbunVeg = (AbundanciaVegetacion) oResultado.getObjeto();
				oCriaderoActual.setVegEmergente(oAbunVeg);
			}else{
				return oResultado;
			}		
	
			oResultado = srvCatAbndVgt.Encontrar(frmSom_VegFlotante);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbunVeg = (AbundanciaVegetacion) oResultado.getObjeto();
				oCriaderoActual.setVegFlotante(oAbunVeg);
			}else{
				return oResultado;
			}		
	
			oResultado = srvCatAbndVgt.Encontrar(frmSom_VegSumergida);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbunVeg = (AbundanciaVegetacion) oResultado.getObjeto();
				oCriaderoActual.setVegSumergida(oAbunVeg);
			}else{
				return oResultado;
			}			
	
			oResultado = srvCatAbndVgt.Encontrar(frmSom_VegVertical);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbunVeg = (AbundanciaVegetacion) oResultado.getObjeto();
				oCriaderoActual.setVegVertical(oAbunVeg);
			}else{
				return oResultado;
			}				
			
			oResultado.setOk(true);
			oResultado.setMensaje(Mensajes.REGISTRO_AGREGADO);
			oResultado.setMensajeDetalle("Es necesario guardar los datos desde el formulario principal");
			oResultado.setGravedad(InfoResultado.SEVERITY_INFO);
			
		}catch(Exception e){
			oResultado.setOk(false);
			oResultado.setExcepcion(true);
			oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
			oResultado.setMensaje("Error Agregando Criadero");
			oResultado.setMensajeDetalle(e.getMessage());
		}
		
		return oResultado;
			
	}
	
	private void actualizarDatosPanelEncuesta(){
		InfoResultado oResultado = new InfoResultado();
		
		if( oCriaderoActual == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Criadero no valido","Seleccionar o agregar un criadero valido");
			if( msg != null) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}				

		frmInp_ComunidadUbicaCriadero = oCriaderoActual.getComunidad();
		frmInp_UbicacionEnc = oCriaderoActual.getDireccion() + ", (Bo/Com): " + oCriaderoActual.getComunidad().getNombre();
		
		Sector oSector = frmInp_ComunidadUbicaCriadero.getSector() != null ? (Sector) frmInp_ComunidadUbicaCriadero.getSector() : null;
		if( oSector != null ){
			if( oSector.getMunicipio() != null ){
				frmSom_MunicipioUbicaCriadero = oSector.getMunicipio() != null ? oSector.getMunicipio().getCodigoNacional() : "0";
				frmInp_UbicacionEnc += ". " +oSector.getMunicipio().getNombre();
			}
			
			if( oSector.getUnidad() != null ){
				frmSom_SilaisUbicaCriadero = oSector.getUnidad().getEntidadAdtva() != null ? oSector.getUnidad().getEntidadAdtva().getCodigo() : 0;
				frmInp_UbicacionEnc += oSector.getUnidad().getEntidadAdtva() != null ? ", " +oSector.getUnidad().getEntidadAdtva().getNombre() : "";
			}
			
		}
		
		frmInp_CodCriaderoEnc = oCriaderoActual.getCodigo();
		frmInp_DatosCriaderoEnc = oCriaderoActual.getNombre();
		frmInp_DistMaxCasaEnc = oCriaderoActual.getDistanciaCasa();
		frmInp_AreaEnc = oCriaderoActual.getAreaActual();
		
		if( oCriaderoActual.getCriaderoId() > 0 ){
			oResultado = srvPesquisa.obtenerPesquisasPorCriadero(oCriaderoActual);
			if( !oResultado.isOk() || oResultado.isExcepcion() ){
				FacesMessage msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			
			frmDt_ListaPesquisas = (List<CriaderosPesquisa>) oResultado.getObjeto();
		}

	}
	
	public void openModalPanelCriadero(ActionEvent evt){
		if( oCriaderoActual != null ){
			actualizarDatosPanelCriadero();
		}
		RequestContext.getCurrentInstance().execute("dlgCriadero.show();");
	}	
	
	@SuppressWarnings("unchecked")
	private void actualizarDatosPanelCriadero(){
		InfoResultado oResultado = null;
		
		frmInp_CodigoCriadero = oCriaderoActual.getCodigo();
		frmInp_Latitud = oCriaderoActual.getLatitud();
		frmInp_Longitud = oCriaderoActual.getLongitud();
		
		frmInp_ComunidadUbicaCriadero = oCriaderoActual.getComunidad();
		
		Sector oSector = frmInp_ComunidadUbicaCriadero.getSector() != null ? (Sector) frmInp_ComunidadUbicaCriadero.getSector() : null;
		if( oSector != null ){
			if( oSector.getMunicipio() != null ){
				frmSom_MunicipioUbicaCriadero = oSector.getMunicipio() != null ? oSector.getMunicipio().getCodigoNacional() : "0";
			}
			
			if( oSector.getUnidad() != null ){
				frmSom_SilaisUbicaCriadero = oSector.getUnidad().getEntidadAdtva() != null ? oSector.getUnidad().getEntidadAdtva().getCodigo() : 0;
				oResultado = null;
				oResultado = srvCriadero.obtenerMunicipiosPorSilais(frmSom_SilaisUbicaCriadero);
				if( oResultado.isOk() && oResultado.getObjeto() != null ){
					itemsMunicipio = (List<DivisionPolitica>) oResultado.getObjeto();
				}
			}
		}	
		
		frmInp_DireccionCriadero = oCriaderoActual.getDireccion();
		frmInp_NombreCriadero = oCriaderoActual.getNombre();
		frmSom_TipoCriadero = oCriaderoActual.getTipoCriadero() != null ? oCriaderoActual.getTipoCriadero().getCatalogoId() : 0;
		frmInp_DistMaxCasaProxima = oCriaderoActual.getDistanciaCasa();
		frmInp_AreaActualCriadero = oCriaderoActual.getAreaActual();
		frmSom_ClasificacionCriadero = oCriaderoActual.getClaseCriadero() != null ? oCriaderoActual.getClaseCriadero().getCatalogoId() : 0;
		frmSom_VegVertical = oCriaderoActual.getVegVertical() != null ? oCriaderoActual.getVegVertical().getCatalogoId() : 0;
		frmSom_VegEmergente = oCriaderoActual.getVegEmergente() != null ? oCriaderoActual.getVegEmergente().getCatalogoId() : 0;
		frmSom_VegFlotante = oCriaderoActual.getVegFlotante() != null ? oCriaderoActual.getVegFlotante().getCatalogoId() : 0;
		frmSom_VegSumergida = oCriaderoActual.getVegSumergida() != null ? oCriaderoActual.getVegSumergida().getCatalogoId() : 0;
		frmSom_FaunaInsectos = oCriaderoActual.getFaunaInsecto() != null ? oCriaderoActual.getFaunaInsecto().getCatalogoId() : 0;
		frmSom_FaunaPeces = oCriaderoActual.getFaunaPeces() != null ? oCriaderoActual.getFaunaPeces().getCatalogoId() : 0;
		frmSom_FaunaAnfibios = oCriaderoActual.getFaunaAnfibios() != null ? oCriaderoActual.getFaunaAnfibios().getCatalogoId() : 0;
		frmSom_TurbidezAgua = oCriaderoActual.getTurbidezAgua() != null ? oCriaderoActual.getTurbidezAgua().getCatalogoId() : 0;
		frmSom_MovimientoAgua = oCriaderoActual.getMovimientoAgua() != null ? oCriaderoActual.getMovimientoAgua().getCatalogoId() : 0;
		frmSom_ExposicionSol = oCriaderoActual.getExposicionSol() != null ? oCriaderoActual.getExposicionSol().getCatalogoId() : 0;
		frmInp_Ph = oCriaderoActual.getPh();
		frmInp_Temperatura = oCriaderoActual.getTemperatura();
		frmInp_Cloro = oCriaderoActual.getCloro();
		frmCal_FechaTomaDatos = oCriaderoActual.getFechaDatos();
		
		oResultado = srvCriaderoEspecie.obtenerEspeciesPorCriadero(oCriaderoActual);
		if( oResultado.isOk() && oResultado.getObjeto() != null ){
			List<CriaderosEspecie> listaCriaderosEspecie = (List<CriaderosEspecie>) oResultado.getObjeto();
			if( frmSom_EspecieAnopheles == null) frmSom_EspecieAnopheles = new ArrayList<Long>();
			for( CriaderosEspecie oEspCri : listaCriaderosEspecie ){
				frmSom_EspecieAnopheles.add(oEspCri.getEspecieAnophele().getCatalogoId());
			}
		}
		
		frmInp_Observaciones = oCriaderoActual.getObservaciones();
		
	}

	
	
	/**
	 * Metodos de manejo de pesquisas de criaderos
	 * 
	 */
	
	public void openModalAgregarPesquisa(ActionEvent evt){
		if( oCriaderoActual == null){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Criadero no valido","Completar un criadero para continuar");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		oSelPesquisa = null;
		limpiarDatosModalPesquisa();
		RequestContext.getCurrentInstance().execute("dlgPesquisa.show();");
	}
	
	public void agregarPesquisa(ActionEvent evt){
		InfoResultado oResultado = null;
		
		oResultado = validarPequisa();
		if( !oResultado.isOk() || oResultado.isExcepcion() ){
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		agregarPesquisa();
		RequestContext.getCurrentInstance().execute("dlgPesquisa.hide();");
		
	}

	private InfoResultado validarPequisa(){
		InfoResultado oResultado = new InfoResultado();
		
		oResultado.setOk(true);
		
		if( frmInp_Pesq_SemanaEpidemiologica == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Semana epidemiologica no identificada");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;
		}		
		
		if( frmInp_Pesq_AxoEpidemiologico == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Año epidemiologico no identificado");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmCal_Pesq_FechaInspeccion == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Fecha Inspeccion");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;
		}
		
		if( frmInp_Pesq_PuntosMuestreados == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Puntos Muestreados");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;
		}
		
		if( frmInp_Pesq_TotalCucharonadas == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Total Cucharonadas Colectadas");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmInp_Pesq_CucharonadasPositivas == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Cucharonadas PositivasCompletar Total Cucharonadas Colectadas");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;	
		}
		
		if( frmInp_Pesq_NumLarvJovEstaIyII == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Larvas Jovenes");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;	
		}		
		
		if( frmInp_Pesq_NumLarvAduEstaIIIyIV == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Larvas Adultas");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;	
		}			
		
		if( frmInp_Pesq_NumPupas == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Pupas");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;				
		}
		
		if( frmCal_Pesq_FechaNotificacion == null){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Fecha de Notificacion");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmInp_Pesq_Inspector == null){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Nombre del Inspector");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		return oResultado;
	}
	
	
	private void agregarPesquisa(){
		if( oSelPesquisa == null ) oSelPesquisa = new CriaderosPesquisa();
		
		oSelPesquisa.setAñoEpidemiologico(frmInp_Pesq_AxoEpidemiologico);
		oSelPesquisa.setSemanaEpidemiologica(frmInp_Pesq_SemanaEpidemiologica);
		oSelPesquisa.setFechaInspeccion(frmCal_Pesq_FechaInspeccion);
		oSelPesquisa.setPuntosMuestreados(frmInp_Pesq_PuntosMuestreados);
		oSelPesquisa.setCuchColectadas(frmInp_Pesq_TotalCucharonadas);
		oSelPesquisa.setCuchPositivas(frmInp_Pesq_CucharonadasPositivas);
		oSelPesquisa.setLarvasJovenes(frmInp_Pesq_NumLarvJovEstaIyII);
		oSelPesquisa.setLarvasAdultas(frmInp_Pesq_NumLarvAduEstaIIIyIV);
		oSelPesquisa.setPupas(frmInp_Pesq_NumPupas);
		oSelPesquisa.setObservacion(frmInp_Pesq_Observaciones);
		oSelPesquisa.setFechaNotificacion(frmCal_Pesq_FechaNotificacion);
		oSelPesquisa.setInspector(frmInp_Pesq_Inspector);
		
		if( frmDt_ListaPesquisas == null ) frmDt_ListaPesquisas = new ArrayList<CriaderosPesquisa>();
		if( isEditPesquisa == false ) frmDt_ListaPesquisas.add(oSelPesquisa);
		else isEditPesquisa = false;
		
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Agregado","");
		if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
		
		oSelIntervencion = null;
		oSelPosInspeccion = null;
		
	}
	
	public void setEliminarPesquisa(CriaderosPesquisa pPesquisa){
		oSelPesquisa = pPesquisa;
		actualizarTrasSelecccionPesquisa();
	}
	
	public void eliminarPesquisa(ActionEvent evt){
		InfoResultado oResultado = null;
		FacesMessage msg = null;
		try{

			oResultado = null;
			oResultado = srvPosInspeccion.eliminarPosInspeccionPorIntervencion(oSelIntervencion);
			if( oResultado.isExcepcion()){
				msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}	
			
			oResultado = null;
			oResultado = srvIntervencion.eliminarIntervencionPorPesquisa(oSelPesquisa);
			if( oResultado.isExcepcion()){
				msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}			
			
			oResultado = null;
			oResultado = srvPesquisa.eliminarPesquisa(oSelPesquisa.getCriaderoPesquisaId());
			if( oResultado.isExcepcion()){
				msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			
			frmDt_ListaPesquisas.remove(oSelPesquisa);
			oSelPesquisa = null;
			
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Registros Eliminados","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
			
		}catch(Exception e){
			msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error Eliminando Pesquisa",e.getMessage());
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;					
		}
		
	}
	
	public void setEditarPesquisa(CriaderosPesquisa oPesquisa){
		
		oSelPesquisa = oPesquisa;
		isEditPesquisa = true;
		
		frmCal_Pesq_FechaInspeccion = oPesquisa.getFechaInspeccion();
		frmInp_Pesq_SemanaEpidemiologica = oPesquisa.getSemanaEpidemiologica();
		frmInp_Pesq_AxoEpidemiologico = oPesquisa.getAñoEpidemiologico();
		frmInp_Pesq_PuntosMuestreados = oPesquisa.getPuntosMuestreados();
		frmInp_Pesq_TotalCucharonadas = oPesquisa.getCuchColectadas();
		frmInp_Pesq_CucharonadasPositivas = oPesquisa.getCuchPositivas();
		frmInp_Pesq_NumLarvJovEstaIyII = oPesquisa.getLarvasJovenes();
		frmInp_Pesq_NumLarvAduEstaIIIyIV = oPesquisa.getLarvasAdultas();
		frmInp_Pesq_NumPupas = oPesquisa.getPupas();	
		frmInp_Pesq_Observaciones = oPesquisa.getObservacion();
		frmCal_Pesq_FechaNotificacion = oPesquisa.getFechaNotificacion();
		frmInp_Pesq_Inspector = oPesquisa.getInspector();
		
		frmInp_Pesq_DensLarvJovEstaIyII = 
			calcularDensidad( frmInp_Pesq_NumLarvJovEstaIyII, frmInp_Pesq_TotalCucharonadas);
		
		frmInp_Pesq_DensLarvAduEstaIIIyIV = 
			calcularDensidad(frmInp_Pesq_NumLarvAduEstaIIIyIV, frmInp_Pesq_TotalCucharonadas);
		
		frmInp_Pesq_DensPupas = 
			calcularDensidad(frmInp_Pesq_NumPupas, frmInp_Pesq_TotalCucharonadas);		
		
	}
	
	public void calcularPesqDensLarvJovenes(){
		frmInp_Pesq_DensLarvJovEstaIyII = 
			calcularDensidad( frmInp_Pesq_NumLarvJovEstaIyII, frmInp_Pesq_TotalCucharonadas);
	}

	public void calcularPesqDensLarvAdultas(){
		frmInp_Pesq_DensLarvAduEstaIIIyIV = 
			calcularDensidad( frmInp_Pesq_NumLarvAduEstaIIIyIV, frmInp_Pesq_TotalCucharonadas);
	}	

	public void calcularPesqDensPupas(){
		frmInp_Pesq_DensPupas = 
			calcularDensidad( frmInp_Pesq_NumPupas, frmInp_Pesq_TotalCucharonadas);
	}		
	
	public void actualizarSemanaAxoEpi(){
		frmInp_Pesq_SemanaEpidemiologica = CalendarioEPI.semana(frmCal_Pesq_FechaNotificacion);
		frmInp_Pesq_AxoEpidemiologico = CalendarioEPI.año(frmCal_Pesq_FechaNotificacion);
	}
		
	public void actualizarTrasSeleccionPesquisa(SelectEvent evt){
		actualizarTrasSelecccionPesquisa();
	}
	
	private void actualizarTrasSelecccionPesquisa(){
		if( oSelPesquisa == null ) return;

		oSelIntervencion = null;
		oSelPosInspeccion = null;
		
		InfoResultado oResultado = srvIntervencion.obtenerIntervencionPorPesquisa(oSelPesquisa);
		if( oResultado.isOk() && oResultado.getObjeto() != null){
			oSelIntervencion = (CriaderosIntervencion) oResultado.getObjeto();
		}
		
		oResultado = null;
		oResultado = srvPosInspeccion.obtenerPosInspeccionPorPorIntervencion(oSelIntervencion);
		if( oResultado.isOk() && oResultado.getObjeto() != null ){
			oSelPosInspeccion = (CriaderosPosInspeccion) oResultado.getObjeto();
		}
	}
	
	public CriaderosPesquisa getoSelPesquisa() {
		return oSelPesquisa;
	}

	public void setoSelPesquisa(CriaderosPesquisa oSelPesquisa) {
		this.oSelPesquisa = oSelPesquisa;
	}

	/**
	 * Metodos de manejo de intervenciones
	 * 
	 */
	public void openModalIntervencion(ActionEvent evt){
		if( oSelPesquisa == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar una Pesquisa", "");
			if( msg != null) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if( oSelPesquisa.getCriaderoPesquisaId() == 0 ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "No es posible agregar intervenciones", "Debe guardar la pesquisa seleccionada");
			if( msg != null) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}
		
		limpiarDatosModalIntervencion();
		if( oSelIntervencion != null ) editarIntervencion(oSelIntervencion);

		RequestContext.getCurrentInstance().execute("dlgIntervencion.show();");
	}
	
	public void editarIntervencion(CriaderosIntervencion oIntervencion){
		
		frmCal_Intv_FechaIntervencion = oIntervencion.getFechaIntervencion();
		frmInp_Intv_Limpieza = oIntervencion.getLimpieza();
		frmInp_Intv_Eva = oIntervencion.getEva();
		frmInp_Intv_Drenaje = oIntervencion.getDrenaje();
		frmInp_Intv_Relleno = oIntervencion.getRelleno();
		frmInp_Intv_Bsphaericus = oIntervencion.getBsphaericus();
		frmInp_Intv_Bti = oIntervencion.getBti();
		frmInp_Intv_ConsumoBsphaericus = oIntervencion.getConsumoBsphaericus();
		frmInp_Intv_ConsumoBti = oIntervencion.getConsumoBti();
		frmInp_Intv_Observaciones = oIntervencion.getObservaciones();
		frmInp_Intv_SiembraPeces = oIntervencion.getSiembraPeces();
		
	}	
	
	public void agregarIntervencion(){
		long auxId = 0;
		
		if( frmCal_Intv_FechaIntervencion == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Fecha Intervencion","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if( frmInp_Intv_Limpieza == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Limpieza","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}
		
		if( frmInp_Intv_Eva == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Eliminacion Vegetacion Actuatica","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}		
		
		if( frmInp_Intv_Drenaje == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Drenaje o Zanjeo","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}		
		
		if( frmInp_Intv_Relleno == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Relleno o Aterramiento","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if( frmInp_Intv_Bsphaericus == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Tratamiento Bsphaericus","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}		
		
		if( frmInp_Intv_Bti == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Bti","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}			
		
		if( frmInp_Intv_SiembraPeces == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Siembra de Peces","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}			

		if( frmInp_Intv_ConsumoBsphaericus == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Consumo Bsphaericus","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}			
		
		if( frmInp_Intv_ConsumoBti == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Consumo Bti","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}					
		
		if( oSelIntervencion == null) oSelIntervencion = new CriaderosIntervencion();
		auxId = oSelIntervencion.getCriaderoIntervencionId();
		
		oSelIntervencion.setFechaIntervencion(frmCal_Intv_FechaIntervencion);
		oSelIntervencion.setLimpieza(frmInp_Intv_Limpieza);
		oSelIntervencion.setEva(frmInp_Intv_Eva);
		oSelIntervencion.setDrenaje(frmInp_Intv_Drenaje);
		oSelIntervencion.setRelleno(frmInp_Intv_Relleno);
		oSelIntervencion.setBsphaericus(frmInp_Intv_Bsphaericus);
		oSelIntervencion.setBti(frmInp_Intv_Bti);
		oSelIntervencion.setConsumoBsphaericus(frmInp_Intv_ConsumoBsphaericus);
		oSelIntervencion.setConsumoBti(frmInp_Intv_ConsumoBti);
		oSelIntervencion.setObservaciones(frmInp_Intv_Observaciones);
		oSelIntervencion.setCriaderosPesquisa(oSelPesquisa);
		oSelIntervencion.setSiembraPeces(frmInp_Intv_SiembraPeces);
		
		InfoResultado oResultado = null;
		FacesMessage msg = null;
		try{
			oResultado = srvIntervencion.guardarIntervencion(oSelIntervencion);
			if( !oResultado.isOk() || oResultado.isExcepcion() ){
				if( auxId == 0) oSelIntervencion = null;
				msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance();
				return;
			}
			oSelIntervencion = (CriaderosIntervencion) oResultado.getObjeto();
		}catch(Exception e){
			msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error Agregando Intervencion",e.getMessage());
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}
		
		msg = Mensajes.enviarMensaje(oResultado);
		if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
		
		RequestContext.getCurrentInstance().execute("dlgIntervencion.hide();");
	}		

	/**
	 * Metodos de manejo de Pos Inspecciones
	 * 
	 */
	public void openModalPosInspeccion(ActionEvent evt){
		
		if( oSelIntervencion == null){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar una Intervencion", "");
			if( msg != null) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if( oSelIntervencion.getCriaderoIntervencionId() == 0){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "No es posible Agregar PosInspecciones", "Debe guardar la intervencion seleccionada");
			if( msg != null) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if( oSelPosInspeccion != null) editarPosInspeccion(oSelPosInspeccion);
		RequestContext.getCurrentInstance().execute("dlgPosInspeccion.show();");
	}

	public void editarPosInspeccion(CriaderosPosInspeccion oPosInspeccion){
		
		frmCal_PosIns_FechaInspeccion = oPosInspeccion.getFechaInspeccion();
		frmInp_PosIns_PuntosMuestreados = oPosInspeccion.getPuntosMuestreados();
		frmInp_PosIns_TotalCucharonadas = oPosInspeccion.getCuchColectadas();
		frmInp_PosIns_CucharonadasPositivas = oPosInspeccion.getCuchPositivas();
		frmInp_PosIns_NumLarvJovEstaIyII = oPosInspeccion.getLarvasJovenes();
		frmInp_PosIns_NumLarvAduEstaIIIyIV = oPosInspeccion.getLarvasAdultas();
		frmInp_PosIns_NumPupas = oPosInspeccion.getPupas();	
		frmInp_PosIns_Observaciones = oPosInspeccion.getObservacion();
	
		frmInp_PosIns_DensLarvJovEstaIyII = 
			calcularDensidad( frmInp_PosIns_NumLarvJovEstaIyII, frmInp_PosIns_TotalCucharonadas);
		
		frmInp_PosIns_DensLarvAduEstaIIIyIV = 
			calcularDensidad(frmInp_PosIns_NumLarvAduEstaIIIyIV, frmInp_PosIns_TotalCucharonadas);
		
		frmInp_PosIns_DensPupas = 
			calcularDensidad(frmInp_PosIns_NumPupas, frmInp_PosIns_TotalCucharonadas);

	}
	
	
	public void agregarPosInspeccion(){
		long auxId = 0;
				
		if( frmCal_PosIns_FechaInspeccion == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Fecha Inspeccion","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if( frmInp_PosIns_PuntosMuestreados == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Puntos Muestreados","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}
		
		if( frmInp_PosIns_PuntosMuestreados == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Puntos Muestreados","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}		
		
		if( frmInp_PosIns_TotalCucharonadas == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Total Cucharonadas Colectadas","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}		
		
		if( frmInp_PosIns_CucharonadasPositivas == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Cucharonadas Positivas","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if( frmInp_PosIns_NumLarvJovEstaIyII == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Larvas Jovenes","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}		
		
		if( frmInp_PosIns_NumLarvAduEstaIIIyIV == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Larvas Adultas","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}			
		
		if( frmInp_PosIns_NumPupas == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Pupas","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}			
		
		if( oSelPosInspeccion == null ) oSelPosInspeccion = new CriaderosPosInspeccion();
		auxId = oSelPosInspeccion.getCriaderoPosInspeccionId();
		
		oSelPosInspeccion.setFechaInspeccion(frmCal_PosIns_FechaInspeccion);
		oSelPosInspeccion.setPuntosMuestreados(frmInp_PosIns_PuntosMuestreados);
		oSelPosInspeccion.setCuchColectadas(frmInp_PosIns_TotalCucharonadas);
		oSelPosInspeccion.setCuchPositivas(frmInp_PosIns_CucharonadasPositivas);
		oSelPosInspeccion.setLarvasJovenes(frmInp_PosIns_NumLarvJovEstaIyII);
		oSelPosInspeccion.setLarvasAdultas(frmInp_PosIns_NumLarvAduEstaIIIyIV);
		oSelPosInspeccion.setPupas(frmInp_PosIns_NumPupas);
		oSelPosInspeccion.setObservacion(frmInp_PosIns_Observaciones);
		oSelPosInspeccion.setUsuarioRegistro(Utilidades.obtenerInfoSesion().getUsername());
		oSelPosInspeccion.setFechaRegistro(new Date());
		oSelPosInspeccion.setCriaderosIntervencion(oSelIntervencion);
		
		
		
		InfoResultado oResultado = null;
		FacesMessage msg = null;
		try{
			
			oResultado = srvPosInspeccion.guardarPosInspeccion(oSelPosInspeccion);
			if( !oResultado.isOk() || oResultado.isExcepcion() ){
				if( auxId == 0 ) oSelPosInspeccion = null;
				msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			oSelPosInspeccion = (CriaderosPosInspeccion) oResultado.getObjeto();
			
		}catch(Exception e){
			msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error Agregando PosInspeccion",e.getMessage());
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
				
		msg = Mensajes.enviarMensaje(oResultado);
		if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
		
		RequestContext.getCurrentInstance().execute("dlgPosInspeccion.hide();");
	}
	
	public void calcularPosInsDensLarvJovenes(){
		frmInp_PosIns_DensLarvJovEstaIyII = 
			calcularDensidad( frmInp_PosIns_NumLarvJovEstaIyII, frmInp_PosIns_TotalCucharonadas);
	}

	public void calcularPosInsDensLarvAdultas(){
		frmInp_PosIns_DensLarvAduEstaIIIyIV = 
			calcularDensidad( frmInp_PosIns_NumLarvAduEstaIIIyIV, frmInp_PosIns_TotalCucharonadas);
	}	

	public void calcularPosInsDensPupas(){
		frmInp_PosIns_DensPupas = 
			calcularDensidad( frmInp_PosIns_NumPupas, frmInp_PosIns_TotalCucharonadas);
	}	
	
	/*
	 * Metodos para Agregar Clases de Criaderos y Especies anopheles
	 * ML_CRIADEROSESPECIES
	 * ML_CLASFCRIADEROS
	 * 
	 */
	public void openModalAgregarCatOtros(AjaxBehaviorEvent evt){
		this.componenteIdCatOtros = evt.getComponent().getClientId();
		String str = null;
		
		if( this.componenteIdCatOtros.equals("frmEncEnto:frmSom_ClasificacionCriadero") ){
			if( frmSom_ClasificacionCriadero == 999999)
				RequestContext.getCurrentInstance().execute("dlgOpenModalOtros.show();");
		}else if(this.componenteIdCatOtros.equals("frmEncEnto:frmSom_EspecieAnopheles") ){
			if( frmSom_EspecieAnopheles != null ){
				for(int i=0; i<frmSom_EspecieAnopheles.size(); i++){
					str = String.valueOf(frmSom_EspecieAnopheles.get(i));
					if( str.equals("999999") ){
						RequestContext.getCurrentInstance().execute("dlgOpenModalOtros.show();");
						break;						
					}
				}
			}
		}
			
	}
	
	public void agregaCatalogoOtros(ActionEvent evt){
		InfoResultado oResultado = new InfoResultado();
		String codCatalogo = null;
		String dependencia = null;
		AddCatalogo oCat = null;
		
		if(this.componenteIdCatOtros.equals("frmEncEnto:frmSom_ClasificacionCriadero")){
			dependencia = "ML_CLASFCRIADEROS";
		}else if( this.componenteIdCatOtros.equals("frmEncEnto:frmSom_EspecieAnopheles")){
			dependencia = "ML_ESPEANOPHELES";
		}
			
		codCatalogo = 
			frmInp_OtroValorCatalogo
				.replace("á", "a").replace("é", "e").replace("í","i").replace("ó","o").replace("ú","u")
				.replace("ä", "a").replace("ë", "e").replace("ï","i").replace("ö","o").replace("ü","u")
				.replace("ñ","n")
				.toUpperCase().trim();
		
		oResultado = srvCriadero.agregarCatOtros(frmInp_OtroValorCatalogo, codCatalogo, dependencia);
		if( oResultado.isOk() && oResultado.getObjeto() != null ){
			AddCatalogo oCatalogo = (AddCatalogo) oResultado.getObjeto();
			
			oResultado = null;
			oResultado = srvCriadero.agregarSistemaCatalogo(oCatalogo.getCodigo());
			if( oResultado.isOk() && oResultado.getObjeto() != null){
				AddSistemaCatalogo oSisCat = (AddSistemaCatalogo) oResultado.getObjeto();
			}
			
			if(this.componenteIdCatOtros.equals("frmEncEnto:frmSom_ClasificacionCriadero")){
				itemsClasesCriaderos = srvCatClasesCriadero.ListarActivos();
				frmSom_ClasificacionCriadero = oCatalogo.getCatalogoId();
			}else if( this.componenteIdCatOtros.equals("frmEncEnto:frmSom_EspecieAnopheles")){
				itemsEspeciesAnopheles = srvCatEspAnopheles.ListarActivos();
				if( frmSom_EspecieAnopheles == null ) frmSom_EspecieAnopheles = new ArrayList<Long>();
				frmSom_EspecieAnopheles.remove(999999);
				frmSom_EspecieAnopheles.add(oCatalogo.getCatalogoId());
			}			
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
		}else{
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
		}
			
	}
	
	public void guardar(ActionEvent evt){
		guardarEncuesta();
	}

	private void guardarEncuesta(){
		InfoResultado oResultado = null;
		
		oResultado = validarFormulario();
		if( !oResultado.isOk() ){
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		oResultado = null;
		oResultado = guardar();
		if( !oResultado.isOk() || oResultado.isExcepcion() ){
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
		
	}	
	
	private InfoResultado validarFormulario(){
		InfoResultado oResultado = new InfoResultado();
		
		oResultado.setOk(true);

		if( oCriaderoActual == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar criadero");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmDt_ListaPesquisas == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Es necesario agregar una Pesquisa");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmDt_ListaPesquisas.isEmpty() ){
			oResultado.setOk(false);
			oResultado.setMensaje("Es necesario agregar una Pesquisa");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
			
		return oResultado;
	}
	
	public InfoResultado guardar(){
		InfoResultado oResultado = null;
		long auxIdCriadero = 0;
		long auxIdPesquisa = 0;
		
		try{
			
			if( oCriaderoActual == null ){
				oCriaderoActual = new Criadero();
				oCriaderoActual.setUsuarioRegistro(Utilidades.obtenerInfoSesion().getUsername());
				oCriaderoActual.setFechaRegistro(new Date());
			}else if( oCriaderoActual.getCriaderoId() == 0 ){
				oCriaderoActual.setUsuarioRegistro(Utilidades.obtenerInfoSesion().getUsername());
				oCriaderoActual.setFechaRegistro(new Date());
			}
			
			auxIdCriadero = oCriaderoActual.getCriaderoId();
			
			
			oResultado = srvCriadero.guardarCriadero(oCriaderoActual);
			if( !oResultado.isOk() || oResultado.isExcepcion()){
				if( auxIdCriadero == 0 ) oCriaderoActual.setCriaderoId(0); 
				return oResultado;
			}
			oCriaderoActual = (Criadero) oResultado.getObjeto();			

			oResultado = null;
			CriaderosEspecie oEspecie = null;
			EspeciesAnopheles oEspAnopheles = null;
			List<CriaderosEspecie> listaEspeciesCriadero = null;
			List<CriaderosEspecie> listaEspeciesCriaderoEliminar = new ArrayList<CriaderosEspecie>(0);
			
			oResultado = srvCriaderoEspecie.obtenerEspeciesPorCriadero(oCriaderoActual);
			if( oResultado.isOk() && oResultado.getObjeto() != null){
				listaEspeciesCriadero = (List<CriaderosEspecie>) oResultado.getObjeto();
			}
							
			if( listaEspeciesCriadero == null ) listaEspeciesCriadero = new ArrayList<CriaderosEspecie>(); 
			
			boolean existe = false;
			String str;
			long oLong;
			if( listaEspeciesCriadero != null && frmSom_EspecieAnopheles != null) {
				for(int i=0; i<frmSom_EspecieAnopheles.size();i++){
					oLong = Long.valueOf(String.valueOf(frmSom_EspecieAnopheles.get(i)));
					oEspecie = new CriaderosEspecie();
					oResultado = null;
					for(CriaderosEspecie oEspCri: listaEspeciesCriadero){
						if( oEspCri.getEspecieAnophele().getCatalogoId() == oLong ){
							existe = true;
							break;
						}
					}
					if( !existe ){
						oResultado = srvCatEspAnopheles.Encontrar(oLong);
						if( oResultado.isOk() && oResultado.getObjeto() != null ){
							oEspecie.setCriadero(oCriaderoActual);
							oEspecie.setFechaRegistro(new Date());
							oEspecie.setUsuarioRegistro(Utilidades.obtenerInfoSesion().getUsername());
							oEspAnopheles = (EspeciesAnopheles) oResultado.getObjeto();
							oEspecie.setEspecieAnophele(oEspAnopheles);
							listaEspeciesCriadero.add(oEspecie);
						}
					}
				}
				
				for(CriaderosEspecie oEspCri1 : listaEspeciesCriadero){
					for(int i=0; i < frmSom_EspecieAnopheles.size() ;i++){
						oLong = Long.valueOf(String.valueOf(frmSom_EspecieAnopheles.get(i)));
						if( oEspCri1.getEspecieAnophele().getCatalogoId() == oLong ){
							existe = true;
							break;
						}
					}
					if( !existe ){
						listaEspeciesCriaderoEliminar.add(oEspCri1);
					}
				}
				
				oResultado = null;
				for(CriaderosEspecie oEspCriS : listaEspeciesCriadero){
					oResultado = srvCriaderoEspecie.guardarEspecie(oEspCriS);
					if( !oResultado.isOk() || oResultado.isExcepcion() ){
						return oResultado;
					}
					oEspCriS = (CriaderosEspecie) oResultado.getObjeto();
				}

				oResultado = null;
				for(CriaderosEspecie oEspCriD : listaEspeciesCriaderoEliminar){
					oResultado = srvCriaderoEspecie.eliminarEspecie(oEspCriD.getCriaderoEspecieId());
					if( !oResultado.isOk() || oResultado.isExcepcion() ){
						return oResultado;
					}
				}	
				listaEspeciesCriaderoEliminar = null;
				
			}
			
			if( frmDt_ListaPesquisas != null ){
				for( CriaderosPesquisa oPesquisa : frmDt_ListaPesquisas ){
					auxIdPesquisa = oPesquisa.getCriaderoPesquisaId();
					oResultado = null;
					oPesquisa.setCriadero(oCriaderoActual);
					oPesquisa.setUsuarioRegistro(Utilidades.obtenerInfoSesion().getUsername());
					oPesquisa.setFechaRegistro(new Date());
					oPesquisa.setRevisado(false);
					oResultado = srvPesquisa.guardarPesquisa(oPesquisa);
					if( !oResultado.isOk() || oResultado.isExcepcion() ){
						if( auxIdPesquisa == 0 ) oPesquisa.setCriaderoPesquisaId(0);
						return oResultado;
					}
					oPesquisa = (CriaderosPesquisa) oResultado.getObjeto();
				}
			}	
		
			oResultado = new InfoResultado();
			oResultado.setOk(true);
			if( auxIdCriadero == 0 ) oResultado.setMensaje(Mensajes.REGISTRO_AGREGADO);
			else oResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
			oResultado.setGravedad(InfoResultado.SEVERITY_INFO);
			
		}catch(Exception e){
			if( auxIdCriadero == 0) oCriaderoActual = null;
			oResultado = new InfoResultado();
			oResultado.setOk(false);
			oResultado.setExcepcion(true);
			oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
			oResultado.setMensaje("Error Metodo Guardar");
			oResultado.setMensajeDetalle(e.getMessage());
			return oResultado;
		}
		
		return oResultado;
	}

	public void actualizarVisibilidadPaneles(ActionEvent evt){

		if( evt.getComponent().getClientId().equals("frmEncEnto:cmbNuevo")
				|| evt.getComponent().getId().equals("cmbSeleccionarCriadero") ){
			panelBusqueda = 1;
			panelEncuesta = 1;
			cmbRegresar = 1;
			cmbGuardar = 1;
			cmbNuevo = 1;
			limpiarFormulario();
		}else if( evt.getComponent().getClientId().equals("frmEncEnto:cmbRegresar")){
			panelEncuesta = 0;
			panelBusqueda = 0;
			cmbNuevo = 0;
			cmbGuardar = 0;
			cmbRegresar = 0;
			limpiarFormulario();
		}

	}
	

	
	
	
	/*
	 * Seccion metodos getter and setters propiedades expuestas a la capa vista 
	 */
		

	

	public String getFrmInp_CodigoCriadero() {
		return frmInp_CodigoCriadero;
	}

	public void setFrmInp_CodigoCriadero(String frmInp_CodigoCriadero) {
		this.frmInp_CodigoCriadero = frmInp_CodigoCriadero;
	}

	public BigDecimal getFrmInp_Latitud() {
		return frmInp_Latitud;
	}

	public void setFrmInp_Latitud(BigDecimal frmInp_Latitud) {
		this.frmInp_Latitud = frmInp_Latitud;
	}

	public BigDecimal getFrmInp_Longitud() {
		return frmInp_Longitud;
	}

	public void setFrmInp_Longitud(BigDecimal frmInp_Longitud) {
		this.frmInp_Longitud = frmInp_Longitud;
	}

	public Long getFrmSom_SilaisUbicaCriadero() {
		return frmSom_SilaisUbicaCriadero;
	}

	public void setFrmSom_SilaisUbicaCriadero(Long frmSom_SilaisUbicaCriadero) {
		this.frmSom_SilaisUbicaCriadero = frmSom_SilaisUbicaCriadero;
	}

	public String getFrmSom_MunicipioUbicaCriadero() {
		return frmSom_MunicipioUbicaCriadero;
	}

	public void setFrmSom_MunicipioUbicaCriadero(
			String frmSom_MunicipioUbicaCriadero) {
		this.frmSom_MunicipioUbicaCriadero = frmSom_MunicipioUbicaCriadero;
	}

	public Comunidad getFrmInp_ComunidadUbicaCriadero() {
		return frmInp_ComunidadUbicaCriadero;
	}

	public void setFrmInp_ComunidadUbicaCriadero(
			Comunidad frmInp_ComunidadUbicaCriadero) {
		this.frmInp_ComunidadUbicaCriadero = frmInp_ComunidadUbicaCriadero;
	}

	public String getFrmInp_DireccionCriadero() {
		return frmInp_DireccionCriadero;
	}

	public void setFrmInp_DireccionCriadero(String frmInp_DireccionCriadero) {
		this.frmInp_DireccionCriadero = frmInp_DireccionCriadero;
	}

	public String getFrmInp_NombreCriadero() {
		return frmInp_NombreCriadero;
	}

	public void setFrmInp_NombreCriadero(String frmInp_NombreCriadero) {
		this.frmInp_NombreCriadero = frmInp_NombreCriadero;
	}

	public Long getFrmSom_TipoCriadero() {
		return frmSom_TipoCriadero;
	}

	public void setFrmSom_TipoCriadero(Long frmSom_TipoCriadero) {
		this.frmSom_TipoCriadero = frmSom_TipoCriadero;
	}

	public BigDecimal getFrmInp_DistMaxCasaProxima() {
		return frmInp_DistMaxCasaProxima;
	}

	public void setFrmInp_DistMaxCasaProxima(BigDecimal frmInp_DistMaxCasaProxima) {
		this.frmInp_DistMaxCasaProxima = frmInp_DistMaxCasaProxima;
	}

	public BigDecimal getFrmInp_AreaActualCriadero() {
		return frmInp_AreaActualCriadero;
	}

	public void setFrmInp_AreaActualCriadero(BigDecimal frmInp_AreaActualCriadero) {
		this.frmInp_AreaActualCriadero = frmInp_AreaActualCriadero;
	}

	public Long getFrmSom_ClasificacionCriadero() {
		return frmSom_ClasificacionCriadero;
	}

	public void setFrmSom_ClasificacionCriadero(Long frmSom_ClasificacionCriadero) {
		this.frmSom_ClasificacionCriadero = frmSom_ClasificacionCriadero;
	}

	public Long getFrmSom_VegVertical() {
		return frmSom_VegVertical;
	}

	public void setFrmSom_VegVertical(Long frmSom_VegVertical) {
		this.frmSom_VegVertical = frmSom_VegVertical;
	}

	public Long getFrmSom_VegEmergente() {
		return frmSom_VegEmergente;
	}

	public void setFrmSom_VegEmergente(Long frmSom_VegEmergente) {
		this.frmSom_VegEmergente = frmSom_VegEmergente;
	}

	public Long getFrmSom_VegFlotante() {
		return frmSom_VegFlotante;
	}

	public void setFrmSom_VegFlotante(Long frmSom_VegFlotante) {
		this.frmSom_VegFlotante = frmSom_VegFlotante;
	}

	public Long getFrmSom_VegSumergida() {
		return frmSom_VegSumergida;
	}

	public void setFrmSom_VegSumergida(Long frmSom_VegSumergida) {
		this.frmSom_VegSumergida = frmSom_VegSumergida;
	}

	public Long getFrmSom_FaunaInsectos() {
		return frmSom_FaunaInsectos;
	}

	public void setFrmSom_FaunaInsectos(Long frmSom_FaunaInsectos) {
		this.frmSom_FaunaInsectos = frmSom_FaunaInsectos;
	}

	public Long getFrmSom_FaunaPeces() {
		return frmSom_FaunaPeces;
	}

	public void setFrmSom_FaunaPeces(Long frmSom_FaunaPeces) {
		this.frmSom_FaunaPeces = frmSom_FaunaPeces;
	}

	public Long getFrmSom_FaunaAnfibios() {
		return frmSom_FaunaAnfibios;
	}

	public void setFrmSom_FaunaAnfibios(Long frmSom_FaunaAnfibios) {
		this.frmSom_FaunaAnfibios = frmSom_FaunaAnfibios;
	}

	public Long getFrmSom_TurbidezAgua() {
		return frmSom_TurbidezAgua;
	}

	public void setFrmSom_TurbidezAgua(Long frmSom_TurbidezAgua) {
		this.frmSom_TurbidezAgua = frmSom_TurbidezAgua;
	}

	public Long getFrmSom_MovimientoAgua() {
		return frmSom_MovimientoAgua;
	}

	public void setFrmSom_MovimientoAgua(Long frmSom_MovimientoAgua) {
		this.frmSom_MovimientoAgua = frmSom_MovimientoAgua;
	}

	public Long getFrmSom_ExposicionSol() {
		return frmSom_ExposicionSol;
	}

	public void setFrmSom_ExposicionSol(Long frmSom_ExposicionSol) {
		this.frmSom_ExposicionSol = frmSom_ExposicionSol;
	}

	public BigDecimal getFrmInp_Ph() {
		return frmInp_Ph;
	}

	public void setFrmInp_Ph(BigDecimal frmInp_Ph) {
		this.frmInp_Ph = frmInp_Ph;
	}

	public BigDecimal getFrmInp_Temperatura() {
		return frmInp_Temperatura;
	}

	public void setFrmInp_Temperatura(BigDecimal frmInp_Temperatura) {
		this.frmInp_Temperatura = frmInp_Temperatura;
	}

	public BigDecimal getFrmInp_Cloro() {
		return frmInp_Cloro;
	}

	public void setFrmInp_Cloro(BigDecimal frmInp_Cloro) {
		this.frmInp_Cloro = frmInp_Cloro;
	}

	public Date getFrmCal_FechaTomaDatos() {
		return frmCal_FechaTomaDatos;
	}

	public void setFrmCal_FechaTomaDatos(Date frmCal_FechaTomaDatos) {
		this.frmCal_FechaTomaDatos = frmCal_FechaTomaDatos;
	}

	public List<Long> getFrmSom_EspecieAnopheles() {
		return frmSom_EspecieAnopheles;
	}

	public void setFrmSom_EspecieAnopheles(List<Long> frmSom_EspecieAnopheles) {
		this.frmSom_EspecieAnopheles = frmSom_EspecieAnopheles;
	}
	
	public String getFrmInp_Observaciones() {
		return frmInp_Observaciones;
	}

	public void setFrmInp_Observaciones(String frmInp_Observaciones) {
		this.frmInp_Observaciones = frmInp_Observaciones;
	}

	public Date getFrmCal_Pesq_FechaInspeccion() {
		return frmCal_Pesq_FechaInspeccion;
	}

	public void setFrmCal_Pesq_FechaInspeccion(Date frmCal_Pesq_FechaInspeccion) {
		this.frmCal_Pesq_FechaInspeccion = frmCal_Pesq_FechaInspeccion;
	}
	
	public String getFrmInp_Pesq_Observaciones() {
		return frmInp_Pesq_Observaciones;
	}

	public void setFrmInp_Pesq_Observaciones(String frmInp_Pesq_Observaciones) {
		this.frmInp_Pesq_Observaciones = frmInp_Pesq_Observaciones;
	}

	public List<CriaderosPesquisa> getFrmDt_ListaPesquisas() {
		return frmDt_ListaPesquisas;
	}

	public void setFrmDt_ListaPesquisas(List<CriaderosPesquisa> frmDt_ListaPesquisas) {
		this.frmDt_ListaPesquisas = frmDt_ListaPesquisas;
	}

	public Date getFrmCal_Intv_FechaIntervencion() {
		return frmCal_Intv_FechaIntervencion;
	}

	public void setFrmCal_Intv_FechaIntervencion(Date frmCal_Intv_FechaIntervencion) {
		this.frmCal_Intv_FechaIntervencion = frmCal_Intv_FechaIntervencion;
	}

	public String getFrmInp_PosIns_Observaciones() {
		return frmInp_PosIns_Observaciones;
	}

	public void setFrmInp_PosIns_Observaciones(String frmInp_PosIns_Observaciones) {
		this.frmInp_PosIns_Observaciones = frmInp_PosIns_Observaciones;
	}

	public List<CriaderosPosInspeccion> getFrmDt_ListaPosInspeccion() {
		return frmDt_ListaPosInspeccion;
	}

	public void setFrmDt_ListaPosInspeccion(
			List<CriaderosPosInspeccion> frmDt_ListaPosInspeccion) {
		this.frmDt_ListaPosInspeccion = frmDt_ListaPosInspeccion;
	}

	public Date getFrmCal_FechaNotificacion() {
		return frmCal_FechaNotificacion;
	}

	public void setFrmCal_FechaNotificacion(Date frmCal_FechaNotificacion) {
		this.frmCal_FechaNotificacion = frmCal_FechaNotificacion;
	}

	public String getFrmInp_Inspector() {
		return frmInp_Inspector;
	}

	public void setFrmInp_Inspector(String frmInp_Inspector) {
		this.frmInp_Inspector = frmInp_Inspector;
	}

	public List<EntidadAdtva> getItemsSilais() {
		return itemsSilais;
	}

	public void setItemsSilais(List<EntidadAdtva> itemsSilais) {
		this.itemsSilais = itemsSilais;
	}

	public List<DivisionPolitica> getItemsMunicipio() {
		return itemsMunicipio;
	}

	public void setItemsMunicipio(List<DivisionPolitica> itemsMunicipio) {
		this.itemsMunicipio = itemsMunicipio;
	}

	public List<AbundanciaFauna> getItemsAbundanciaFauna() {
		return itemsAbundanciaFauna;
	}

	public void setItemsAbundanciaFauna(List<AbundanciaFauna> itemsAbundanciaFauna) {
		this.itemsAbundanciaFauna = itemsAbundanciaFauna;
	}

	public List<AbundanciaVegetacion> getItemsAbundanciaVegetacion() {
		return itemsAbundanciaVegetacion;
	}

	public void setItemsAbundanciaVegetacion(
			List<AbundanciaVegetacion> itemsAbundanciaVegetacion) {
		this.itemsAbundanciaVegetacion = itemsAbundanciaVegetacion;
	}

	public List<ClasesCriaderos> getItemsClasesCriaderos() {
		return itemsClasesCriaderos;
	}

	public void setItemsClasesCriaderos(List<ClasesCriaderos> itemsClasesCriaderos) {
		this.itemsClasesCriaderos = itemsClasesCriaderos;
	}

	public List<CriaderosEspecie> getItemsCriaderoEspecies() {
		return itemsCriaderoEspecies;
	}

	public void setItemsCriaderoEspecies(
			List<CriaderosEspecie> itemsCriaderoEspecies) {
		this.itemsCriaderoEspecies = itemsCriaderoEspecies;
	}

	public List<EspeciesAnopheles> getItemsEspeciesAnopheles() {
		return itemsEspeciesAnopheles;
	}

	public void setItemsEspeciesAnopheles(
			List<EspeciesAnopheles> itemsEspeciesAnopheles) {
		this.itemsEspeciesAnopheles = itemsEspeciesAnopheles;
	}

	public List<ExposicionSol> getItemsExposicionSols() {
		return itemsExposicionSols;
	}

	public void setItemsExposicionSols(List<ExposicionSol> itemsExposicionSols) {
		this.itemsExposicionSols = itemsExposicionSols;
	}

	public List<MovimientoAgua> getItemsMovimientoAguas() {
		return itemsMovimientoAguas;
	}

	public void setItemsMovimientoAguas(List<MovimientoAgua> itemsMovimientoAguas) {
		this.itemsMovimientoAguas = itemsMovimientoAguas;
	}

	public List<TiposCriaderos> getItemsTiposCriaderos() {
		return itemsTiposCriaderos;
	}

	public void setItemsTiposCriaderos(List<TiposCriaderos> itemsTiposCriaderos) {
		this.itemsTiposCriaderos = itemsTiposCriaderos;
	}

	public List<TurbidezAgua> getItemsTurbidezAguas() {
		return itemsTurbidezAguas;
	}

	public void setItemsTurbidezAguas(List<TurbidezAgua> itemsTurbidezAguas) {
		this.itemsTurbidezAguas = itemsTurbidezAguas;
	}

	public LazyDataModel<CriaderosUltimaNotificacion> getLazyCriaderos() {
		return lazyCriaderos;
	}

	public void setLazyCriaderos(LazyDataModel<CriaderosUltimaNotificacion> lazyCriaderos) {
		this.lazyCriaderos = lazyCriaderos;
	}

	public String getFrmInp_OtroValorCatalogo() {
		return frmInp_OtroValorCatalogo;
	}

	public void setFrmInp_OtroValorCatalogo(String frmInp_OtroValorCatalogo) {
		this.frmInp_OtroValorCatalogo = frmInp_OtroValorCatalogo;
	}

	public long getFrmSom_SilaisBusqueda() {
		return frmSom_SilaisBusqueda;
	}

	public void setFrmSom_SilaisBusqueda(long frmSom_SilaisBusqueda) {
		this.frmSom_SilaisBusqueda = frmSom_SilaisBusqueda;
	}

	public String getFrmSom_MunicipioBusqueda() {
		return frmSom_MunicipioBusqueda;
	}

	public void setFrmSom_MunicipioBusqueda(String frmSom_MunicipioBusqueda) {
		this.frmSom_MunicipioBusqueda = frmSom_MunicipioBusqueda;
	}

	public Comunidad getFrmInp_ComunidadBusqueda() {
		return frmInp_ComunidadBusqueda;
	}

	public void setFrmInp_ComunidadBusqueda(Comunidad frmInp_ComunidadBusqueda) {
		this.frmInp_ComunidadBusqueda = frmInp_ComunidadBusqueda;
	}

	public short getPanelBusqueda() {
		return panelBusqueda;
	}

	public void setPanelBusqueda(short panelBusqueda) {
		this.panelBusqueda = panelBusqueda;
	}

	public short getPanelEncuesta() {
		return panelEncuesta;
	}

	public void setPanelEncuesta(short panelEncuesta) {
		this.panelEncuesta = panelEncuesta;
	}

	public short getCmbRegresar() {
		return cmbRegresar;
	}

	public void setCmbRegresar(short cmbRegresar) {
		this.cmbRegresar = cmbRegresar;
	}

	public String getFrmInp_CodCriaderoEnc() {
		return frmInp_CodCriaderoEnc;
	}

	public String getFrmInp_DatosCriaderoEnc() {
		return frmInp_DatosCriaderoEnc;
	}

	public void setFrmInp_DatosCriaderoEnc(String frmInp_DatosCriaderoEnc) {
		this.frmInp_DatosCriaderoEnc = frmInp_DatosCriaderoEnc;
	}

	public String getFrmInp_UbicacionEnc() {
		return frmInp_UbicacionEnc;
	}

	public void setFrmInp_UbicacionEnc(String frmInp_UbicacionEnc) {
		this.frmInp_UbicacionEnc = frmInp_UbicacionEnc;
	}

	public BigDecimal getFrmInp_DistMaxCasaEnc() {
		return frmInp_DistMaxCasaEnc;
	}

	public void setFrmInp_DistMaxCasaEnc(BigDecimal frmInp_DistMaxCasaEnc) {
		this.frmInp_DistMaxCasaEnc = frmInp_DistMaxCasaEnc;
	}

	public BigDecimal getFrmInp_AreaEnc() {
		return frmInp_AreaEnc;
	}

	public void setFrmInp_AreaEnc(BigDecimal frmInp_AreaEnc) {
		this.frmInp_AreaEnc = frmInp_AreaEnc;
	}

	public short getPanelCriadero() {
		return panelCriadero;
	}

	public void setPanelCriadero(short panelCriadero) {
		this.panelCriadero = panelCriadero;
	}

	public short getCmbGuardar() {
		return cmbGuardar;
	}

	public void setCmbGuardar(short cmbGuardar) {
		this.cmbGuardar = cmbGuardar;
	}

	public short getCmbNuevo() {
		return cmbNuevo;
	}

	public void setCmbNuevo(short cmbNuevo) {
		this.cmbNuevo = cmbNuevo;
	}

	public List<DivisionPolitica> getItemsMunicipioBusqueda() {
		return itemsMunicipioBusqueda;
	}

	public Date getFrmCal_Pesq_FechaNotificacion() {
		return frmCal_Pesq_FechaNotificacion;
	}

	public void setFrmCal_Pesq_FechaNotificacion(Date frmCal_Pesq_FechaNotificacion) {
		this.frmCal_Pesq_FechaNotificacion = frmCal_Pesq_FechaNotificacion;
	}

	public String getFrmInp_Pesq_Inspector() {
		return frmInp_Pesq_Inspector;
	}

	public void setFrmInp_Pesq_Inspector(String frmInp_Pesq_Inspector) {
		this.frmInp_Pesq_Inspector = frmInp_Pesq_Inspector;
	}
	
	public Integer getFrmInp_Pesq_SemanaEpidemiologica() {
		return frmInp_Pesq_SemanaEpidemiologica;
	}

	public void setFrmInp_Pesq_SemanaEpidemiologica(
			Integer frmInp_Pesq_SemanaEpidemiologica) {
		this.frmInp_Pesq_SemanaEpidemiologica = frmInp_Pesq_SemanaEpidemiologica;
	}

	public Integer getFrmInp_Pesq_AxoEpidemiologico() {
		return frmInp_Pesq_AxoEpidemiologico;
	}

	public void setFrmInp_Pesq_AxoEpidemiologico(Integer frmInp_Pesq_AxoEpidemiologico) {
		this.frmInp_Pesq_AxoEpidemiologico = frmInp_Pesq_AxoEpidemiologico;
	}

	public Short getFrmInp_Pesq_PuntosMuestreados() {
		return frmInp_Pesq_PuntosMuestreados;
	}

	public void setFrmInp_Pesq_PuntosMuestreados(Short frmInp_Pesq_PuntosMuestreados) {
		this.frmInp_Pesq_PuntosMuestreados = frmInp_Pesq_PuntosMuestreados;
	}

	public Short getFrmInp_Pesq_TotalCucharonadas() {
		return frmInp_Pesq_TotalCucharonadas;
	}

	public void setFrmInp_Pesq_TotalCucharonadas(Short frmInp_Pesq_TotalCucharonadas) {
		this.frmInp_Pesq_TotalCucharonadas = frmInp_Pesq_TotalCucharonadas;
	}

	public Short getFrmInp_Pesq_CucharonadasPositivas() {
		return frmInp_Pesq_CucharonadasPositivas;
	}

	public void setFrmInp_Pesq_CucharonadasPositivas(
			Short frmInp_Pesq_CucharonadasPositivas) {
		this.frmInp_Pesq_CucharonadasPositivas = frmInp_Pesq_CucharonadasPositivas;
	}

	public Short getFrmInp_Pesq_NumLarvJovEstaIyII() {
		return frmInp_Pesq_NumLarvJovEstaIyII;
	}

	public void setFrmInp_Pesq_NumLarvJovEstaIyII(
			Short frmInp_Pesq_NumLarvJovEstaIyII) {
		this.frmInp_Pesq_NumLarvJovEstaIyII = frmInp_Pesq_NumLarvJovEstaIyII;
	}

	public Short getFrmInp_Pesq_NumLarvAduEstaIIIyIV() {
		return frmInp_Pesq_NumLarvAduEstaIIIyIV;
	}

	public void setFrmInp_Pesq_NumLarvAduEstaIIIyIV(
			Short frmInp_Pesq_NumLarvAduEstaIIIyIV) {
		this.frmInp_Pesq_NumLarvAduEstaIIIyIV = frmInp_Pesq_NumLarvAduEstaIIIyIV;
	}

	public Short getFrmInp_Pesq_NumPupas() {
		return frmInp_Pesq_NumPupas;
	}

	public void setFrmInp_Pesq_NumPupas(Short frmInp_Pesq_NumPupas) {
		this.frmInp_Pesq_NumPupas = frmInp_Pesq_NumPupas;
	}

	public Double getFrmInp_Pesq_DensLarvJovEstaIyII() {
		return frmInp_Pesq_DensLarvJovEstaIyII;
	}

	public void setFrmInp_Pesq_DensLarvJovEstaIyII(
			Double frmInp_Pesq_DensLarvJovEstaIyII) {
		this.frmInp_Pesq_DensLarvJovEstaIyII = frmInp_Pesq_DensLarvJovEstaIyII;
	}

	public Double getFrmInp_Pesq_DensLarvAduEstaIIIyIV() {
		return frmInp_Pesq_DensLarvAduEstaIIIyIV;
	}

	public void setFrmInp_Pesq_DensLarvAduEstaIIIyIV(
			Double frmInp_Pesq_DensLarvAduEstaIIIyIV) {
		this.frmInp_Pesq_DensLarvAduEstaIIIyIV = frmInp_Pesq_DensLarvAduEstaIIIyIV;
	}

	public Double getFrmInp_Pesq_DensPupas() {
		return frmInp_Pesq_DensPupas;
	}

	public void setFrmInp_Pesq_DensPupas(Double frmInp_Pesq_DensPupas) {
		this.frmInp_Pesq_DensPupas = frmInp_Pesq_DensPupas;
	}

	public Long getFrmInp_Intv_Limpieza() {
		return frmInp_Intv_Limpieza;
	}

	public void setFrmInp_Intv_Limpieza(Long frmInp_Intv_Limpieza) {
		this.frmInp_Intv_Limpieza = frmInp_Intv_Limpieza;
	}

	public Long getFrmInp_Intv_Eva() {
		return frmInp_Intv_Eva;
	}

	public void setFrmInp_Intv_Eva(Long frmInp_Intv_Eva) {
		this.frmInp_Intv_Eva = frmInp_Intv_Eva;
	}

	public Long getFrmInp_Intv_Drenaje() {
		return frmInp_Intv_Drenaje;
	}

	public void setFrmInp_Intv_Drenaje(Long frmInp_Intv_Drenaje) {
		this.frmInp_Intv_Drenaje = frmInp_Intv_Drenaje;
	}

	public Long getFrmInp_Intv_Relleno() {
		return frmInp_Intv_Relleno;
	}

	public void setFrmInp_Intv_Relleno(Long frmInp_Intv_Relleno) {
		this.frmInp_Intv_Relleno = frmInp_Intv_Relleno;
	}

	public Long getFrmInp_Intv_Bsphaericus() {
		return frmInp_Intv_Bsphaericus;
	}

	public void setFrmInp_Intv_Bsphaericus(Long frmInp_Intv_Bsphaericus) {
		this.frmInp_Intv_Bsphaericus = frmInp_Intv_Bsphaericus;
	}

	public Long getFrmInp_Intv_Bti() {
		return frmInp_Intv_Bti;
	}

	public void setFrmInp_Intv_Bti(Long frmInp_Intv_Bti) {
		this.frmInp_Intv_Bti = frmInp_Intv_Bti;
	}

	public Long getFrmInp_Intv_SiembraPeces() {
		return frmInp_Intv_SiembraPeces;
	}

	public void setFrmInp_Intv_SiembraPeces(Long frmInp_Intv_SiembraPeces) {
		this.frmInp_Intv_SiembraPeces = frmInp_Intv_SiembraPeces;
	}

	public Long getFrmInp_Intv_ConsumoBsphaericus() {
		return frmInp_Intv_ConsumoBsphaericus;
	}

	public void setFrmInp_Intv_ConsumoBsphaericus(
			Long frmInp_Intv_ConsumoBsphaericus) {
		this.frmInp_Intv_ConsumoBsphaericus = frmInp_Intv_ConsumoBsphaericus;
	}

	public Long getFrmInp_Intv_ConsumoBti() {
		return frmInp_Intv_ConsumoBti;
	}

	public void setFrmInp_Intv_ConsumoBti(Long frmInp_Intv_ConsumoBti) {
		this.frmInp_Intv_ConsumoBti = frmInp_Intv_ConsumoBti;
	}

	public String getFrmInp_Intv_Observaciones() {
		return frmInp_Intv_Observaciones;
	}

	public void setFrmInp_Intv_Observaciones(String frmInp_Intv_Observaciones) {
		this.frmInp_Intv_Observaciones = frmInp_Intv_Observaciones;
	}

	public Date getFrmCal_PosIns_FechaInspeccion() {
		return frmCal_PosIns_FechaInspeccion;
	}

	public void setFrmCal_PosIns_FechaInspeccion(Date frmCal_PosIns_FechaInspeccion) {
		this.frmCal_PosIns_FechaInspeccion = frmCal_PosIns_FechaInspeccion;
	}

	public Short getFrmInp_PosIns_PuntosMuestreados() {
		return frmInp_PosIns_PuntosMuestreados;
	}

	public void setFrmInp_PosIns_PuntosMuestreados(
			Short frmInp_PosIns_PuntosMuestreados) {
		this.frmInp_PosIns_PuntosMuestreados = frmInp_PosIns_PuntosMuestreados;
	}

	public Short getFrmInp_PosIns_TotalCucharonadas() {
		return frmInp_PosIns_TotalCucharonadas;
	}

	public void setFrmInp_PosIns_TotalCucharonadas(
			Short frmInp_PosIns_TotalCucharonadas) {
		this.frmInp_PosIns_TotalCucharonadas = frmInp_PosIns_TotalCucharonadas;
	}

	public Short getFrmInp_PosIns_CucharonadasPositivas() {
		return frmInp_PosIns_CucharonadasPositivas;
	}

	public void setFrmInp_PosIns_CucharonadasPositivas(
			Short frmInp_PosIns_CucharonadasPositivas) {
		this.frmInp_PosIns_CucharonadasPositivas = frmInp_PosIns_CucharonadasPositivas;
	}

	public Short getFrmInp_PosIns_NumLarvJovEstaIyII() {
		return frmInp_PosIns_NumLarvJovEstaIyII;
	}

	public void setFrmInp_PosIns_NumLarvJovEstaIyII(
			Short frmInp_PosIns_NumLarvJovEstaIyII) {
		this.frmInp_PosIns_NumLarvJovEstaIyII = frmInp_PosIns_NumLarvJovEstaIyII;
	}

	public Short getFrmInp_PosIns_NumLarvAduEstaIIIyIV() {
		return frmInp_PosIns_NumLarvAduEstaIIIyIV;
	}

	public void setFrmInp_PosIns_NumLarvAduEstaIIIyIV(
			Short frmInp_PosIns_NumLarvAduEstaIIIyIV) {
		this.frmInp_PosIns_NumLarvAduEstaIIIyIV = frmInp_PosIns_NumLarvAduEstaIIIyIV;
	}

	public Short getFrmInp_PosIns_NumPupas() {
		return frmInp_PosIns_NumPupas;
	}

	public void setFrmInp_PosIns_NumPupas(Short frmInp_PosIns_NumPupas) {
		this.frmInp_PosIns_NumPupas = frmInp_PosIns_NumPupas;
	}

	public Double getFrmInp_PosIns_DensLarvJovEstaIyII() {
		return frmInp_PosIns_DensLarvJovEstaIyII;
	}

	public void setFrmInp_PosIns_DensLarvJovEstaIyII(
			Double frmInp_PosIns_DensLarvJovEstaIyII) {
		this.frmInp_PosIns_DensLarvJovEstaIyII = frmInp_PosIns_DensLarvJovEstaIyII;
	}

	public Double getFrmInp_PosIns_DensLarvAduEstaIIIyIV() {
		return frmInp_PosIns_DensLarvAduEstaIIIyIV;
	}

	public void setFrmInp_PosIns_DensLarvAduEstaIIIyIV(
			Double frmInp_PosIns_DensLarvAduEstaIIIyIV) {
		this.frmInp_PosIns_DensLarvAduEstaIIIyIV = frmInp_PosIns_DensLarvAduEstaIIIyIV;
	}

	public Double getFrmInp_PosIns_DensPupas() {
		return frmInp_PosIns_DensPupas;
	}

	public void setFrmInp_PosIns_DensPupas(Double frmInp_PosIns_DensPupas) {
		this.frmInp_PosIns_DensPupas = frmInp_PosIns_DensPupas;
	}

	public void setFrmInp_CodCriaderoEnc(String frmInp_CodCriaderoEnc) {
		this.frmInp_CodCriaderoEnc = frmInp_CodCriaderoEnc;
	}

	
	public List<CriaderosIntervencion> getFrmDt_ListaIntervencion() {
		return frmDt_ListaIntervencion;
	}

	public Criadero getoCriaderoActual() {
		return oCriaderoActual;
	}

	public void setoCriaderoActual(Criadero oCriaderoActual) {
		this.oCriaderoActual = oCriaderoActual;
	}

	public CriaderosIntervencion getoSelIntervencion() {
		return oSelIntervencion;
	}

	public void setoSelIntervencion(CriaderosIntervencion oSelIntervencion) {
		this.oSelIntervencion = oSelIntervencion;
	}

	public CriaderosPosInspeccion getoSelPosInspeccion() {
		return oSelPosInspeccion;
	}

	public void setoSelPosInspeccion(CriaderosPosInspeccion oSelPosInspeccion) {
		this.oSelPosInspeccion = oSelPosInspeccion;
	}
	
	
	
	
}
