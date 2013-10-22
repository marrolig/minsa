package ni.gob.minsa.malaria.interfaz.encuestas;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

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
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.servicios.encuestas.CriaderoServices;
import ni.gob.minsa.malaria.servicios.encuestas.CriaderosEspecieServices;
import ni.gob.minsa.malaria.servicios.encuestas.IntervencionServices;
import ni.gob.minsa.malaria.servicios.encuestas.PesquisaServices;
import ni.gob.minsa.malaria.servicios.encuestas.PosInspeccionServices;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
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
	
	private BigDecimal frmInp_SemanaEpidemiologica;
	private BigDecimal frmInp_AxoEpidemiologico;
	
	/*
	 * Identificacion del Criadero
	 */
	private Criadero oCriaderoActual = null;
	private String frmInp_CodigoCriadero;
	
	
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
	private String frmInp_ClasificacionCriaderoOtro;
	
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
	
	private Long frmSom_EspecieAnopheles;
	private String frmInp_EspecieAnophelesOtra;
	
	private String frmInp_Observaciones;


	/*
	 * Variables de manejo de registro de Pesquisas
	 */
	
	
	private Date frmCal_Pesq_FechaInspeccion;
	private BigDecimal frmInp_Pesq_PuntosMuestreados;
	private BigDecimal frmInp_Pesq_TotalCucharonadas;
	private BigDecimal frmInp_Pesq_CucharonadasPositivas;
	private BigDecimal frmInp_Pesq_NumLarvJovEstaIyII;
	private BigDecimal frmInp_Pesq_NumLarvAduEstaIIIyIV;
	private BigDecimal frmInp_Pesq_NumPupas;
	private BigDecimal frmInp_Pesq_DensLarvJovEstaIyII;
	private BigDecimal frmInp_Pesq_DensLarvAduEstaIIIyIV;
	private BigDecimal frmInp_Pesq_DensPupas;
	private String frmInp_Pesq_Observaciones;
	
	private List<CriaderosPesquisa> frmDt_ListaPesquisas = null;
	
	private CriaderosPesquisa oSelPesquisa = null;
	
	/*
	 * Variables de manejo de registro de intervenciones
	 */
	private Date frmCal_Intv_FechaIntervencion;
	private BigDecimal frmInp_Intv_Limpieza;
	private BigDecimal frmInp_Intv_Eva;
	private BigDecimal frmInp_Intv_Drenaje;
	private BigDecimal frmInp_Intv_Relleno;
	private BigDecimal frmInp_Intv_Bsphaericus;
	private BigDecimal frmInp_Intv_Bti;
	private BigDecimal frmInp_Intv_SiembraPeces;
	private BigDecimal frmInp_Intv_ConsumoBsphaericus;
	private BigDecimal frmInp_Intv_ConsumoBti;
	private String frmInp_Intv_Observaciones;
	
	private List<CriaderosIntervencion> frmDt_ListaIntervencion = null;
	private List<CriaderosIntervencion> frmDt_ListaIntervencionEliminar = null;
	
	private CriaderosIntervencion oSelIntervencion = null;
	
	/*
	 * Variables de manejo de registro de PosInspeccion
	 */
	private Date frmCal_PosIns_FechaInspeccion;
	private BigDecimal frmInp_PosIns_PuntosMuestreados;
	private BigDecimal frmInp_PosIns_TotalCucharonadas;
	private BigDecimal frmInp_PosIns_CucharonadasPositivas;
	private BigDecimal frmInp_PosIns_NumLarvJovEstaIyII;
	private BigDecimal frmInp_PosIns_NumLarvAduEstaIIIyIV;
	private BigDecimal frmInp_PosIns_NumPupas;
	private BigDecimal frmInp_PosIns_DensLarvJovEstaIyII;
	private BigDecimal frmInp_PosIns_DensLarvAduEstaIIIyIV;
	private BigDecimal frmInp_PosIns_DensPupas;
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
	private LazyDataModel<Criadero> lazyCriaderos = null;
	
	
	
	/*
	 * Variables de manejos de identificadores componentUI 
	 * para apertura de modales e identificacion de operaciones
	 * sobre componentes especificos
	 */
	private String componenteIdCatOtros;
	private String frmInp_OtroValorCatalogo;
	
	public EncuestaEntomologicaBean() {
		
		this.itemsAbundanciaFauna = srvCatAbndFau.ListarActivos();
		this.itemsAbundanciaVegetacion = srvCatAbndVgt.ListarActivos();
		this.itemsClasesCriaderos = srvCatClasesCriadero.ListarActivos();
		this.itemsEspeciesAnopheles = srvCatEspAnopheles.ListarActivos();
		this.itemsExposicionSols = srvCatExpSol.ListarActivos();
		this.itemsMovimientoAguas = srvCatMovAgua.ListarActivos();
		this.itemsTiposCriaderos = srvCatTipoCriadero.ListarActivos();
		this.itemsTurbidezAguas = srvCatTurbAgua.ListarActivos();
		
		this.itemsSilais = srvEntidadAdtva.EntidadesAdtvasActivas();
		this.itemsCriaderoEspecies = srvCriaderoEspecie.obtenerListaCriaderosEspecies();
		
		
	}
	
	public void limpiarFormulario(){
		/*
		 * Semanana Epidemiologica manejado 
		 * Datos registrados en Entidad CriaderoPesquisa
		 * {@link CriaderosPesquisa}
		 */
		frmInp_SemanaEpidemiologica = null;
		frmInp_AxoEpidemiologico = null;
		
		/*
		 * Identificacion del Criadero
		 */
		frmInp_CodigoCriadero = null;
		
		
		/* 
		 * Datos geograficos del criadero
		 */
		frmInp_Latitud = null;
		frmInp_Longitud = null;
		
		/*
		 * Datos generales del criadero
		 */
		frmSom_SilaisUbicaCriadero = null;
		frmSom_MunicipioUbicaCriadero = null;
		frmInp_ComunidadUbicaCriadero = null;
		frmInp_DireccionCriadero = null;
		
		frmInp_NombreCriadero = null;
		frmSom_TipoCriadero = null;
		
		frmInp_DistMaxCasaProxima = null;
		frmInp_AreaActualCriadero = null;
		
		frmSom_ClasificacionCriadero = null;
		frmInp_ClasificacionCriaderoOtro = null;
		
		/*
		 * Caracteristicas del Criadero
		 */
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
		frmInp_EspecieAnophelesOtra = null;
		
		frmInp_Observaciones = null;


		/*
		 * Variables de manejo de registro de Pesquisas
		 */
		
		
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
		
		frmDt_ListaPesquisas = null;
		
		/*
		 * Variables de manejo de registro de intervenciones
		 */
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
		
		frmDt_ListaIntervencion = null;
		frmDt_ListaIntervencionEliminar = null;
		
		/*
		 * Variables de manejo de registro de PosInspeccion
		 */
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
	 
		frmDt_ListaPosInspeccion = null;
		
		itemsMunicipio = null;
		lazyCriaderos = null;
		
	}
	
	@SuppressWarnings("unchecked")
	public void actualizarMunicipios(ActionEvent evt){
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
	
	public List<Comunidad> completarComunidad(String query){
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades = srvComunidad.ComunidadesPorMunicipioNombre(frmSom_MunicipioUbicaCriadero, query, 10);
		return oComunidades;		
	}
		
	private BigDecimal calcularDensidad(BigDecimal pNumLarvas, BigDecimal pNumCucharonadas){
		
		if( pNumLarvas == null || pNumCucharonadas == null )
			return null;
				
		return ( pNumLarvas.divide(pNumCucharonadas.multiply( new BigDecimal("0.0055"))) );
	}
	
	/**
	 * Metodos de manejo de pesquisas de criaderos
	 * 
	 */
	public void agregarPesquisa(ActionEvent evt){
		CriaderosPesquisa oPesquisa = new CriaderosPesquisa();

		if( frmInp_SemanaEpidemiologica == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Semana epidemiologica no identificada","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}		
		
		if( frmInp_AxoEpidemiologico == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"A�o epidemiologico no identificado","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}
		
		if( frmCal_Pesq_FechaInspeccion == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Fecha Inspeccion","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if( frmInp_Pesq_PuntosMuestreados == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Puntos Muestreados","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}
		
		if( frmInp_Pesq_PuntosMuestreados == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Puntos Muestreados","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}		
		
		if( frmInp_Pesq_TotalCucharonadas == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Total Cucharonadas Colectadas","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}		
		
		if( frmInp_Pesq_CucharonadasPositivas == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Cucharonadas Positivas","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if( frmInp_Pesq_NumLarvJovEstaIyII == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Larvas Jovenes","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}		
		
		if( frmInp_Pesq_NumLarvAduEstaIIIyIV == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Larvas Adultas","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}			
		
		if( frmInp_Pesq_NumPupas == null ){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Completar Pupas","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}			
		
		oPesquisa.setA�oEpidemiologico(frmInp_AxoEpidemiologico);
		oPesquisa.setSemanaEpidemiologica(frmInp_SemanaEpidemiologica);
		oPesquisa.setFechaInspeccion(frmCal_Pesq_FechaInspeccion);
		oPesquisa.setPuntosMuestreados(frmInp_Pesq_PuntosMuestreados);
		oPesquisa.setCuchColectadas(frmInp_Pesq_TotalCucharonadas);
		oPesquisa.setCuchPositivas(frmInp_Pesq_CucharonadasPositivas);
		oPesquisa.setLarvasJovenes(frmInp_Pesq_NumLarvJovEstaIyII);
		oPesquisa.setLarvasAdultas(frmInp_Pesq_NumLarvAduEstaIIIyIV);
		oPesquisa.setPupas(frmInp_Pesq_NumPupas);
		oPesquisa.setObservacion(frmInp_Pesq_Observaciones);
		
		if( frmDt_ListaPesquisas == null ) frmDt_ListaPesquisas = new ArrayList<CriaderosPesquisa>();
		frmDt_ListaPesquisas.add(oPesquisa);
		
	}
	
	public void openModalConfirmaEliminarPesquisa(ActionEvent evt){
		RequestContext.getCurrentInstance().execute("dlgConfirmaEliminarPesquisa.show()");
	}
	
	public void setEliminarPesquisa(CriaderosPesquisa pPesquisa){
		oSelPesquisa = pPesquisa;
	}
	
	public void eliminarPesquisa(ActionEvent evt){
		InfoResultado oResultado = null;
		FacesMessage msg = null;
		try{
			
			frmDt_ListaIntervencionEliminar = frmDt_ListaIntervencion;
			
			for( CriaderosIntervencion oIntervencion : frmDt_ListaIntervencionEliminar ){
				oResultado = null;
				oResultado = srvPosInspeccion.eliminarPosInspeccionPorIntervencion(oIntervencion);
				if( !oResultado.isOk() || oResultado.isExcepcion()){
					msg = Mensajes.enviarMensaje(oResultado);
					if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
					return;
				}
			}
			
			oResultado = null;
			oResultado = srvIntervencion.eliminarIntervencionPorPesquisa(oSelPesquisa);
			if( !oResultado.isOk() || oResultado.isExcepcion()){
				msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}			
			
			oResultado = null;
			oResultado = srvPesquisa.eliminarPesquisa(oSelPesquisa.getCriaderoPesquisaId());
			if( !oResultado.isOk() || oResultado.isExcepcion()){
				msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			
			frmDt_ListaIntervencion = new ArrayList<CriaderosIntervencion>();
			frmDt_ListaPosInspeccion = new ArrayList<CriaderosPosInspeccion>();			
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
	
	public void editarPesquisa(CriaderosPesquisa oPesquisa){
		
		frmCal_Pesq_FechaInspeccion = oPesquisa.getFechaInspeccion();
		frmInp_Pesq_PuntosMuestreados = oPesquisa.getPuntosMuestreados();
		frmInp_Pesq_TotalCucharonadas = oPesquisa.getCuchColectadas();
		frmInp_Pesq_CucharonadasPositivas = oPesquisa.getCuchPositivas();
		frmInp_Pesq_NumLarvJovEstaIyII = oPesquisa.getLarvasJovenes();
		frmInp_Pesq_NumLarvAduEstaIIIyIV = oPesquisa.getLarvasAdultas();
		frmInp_Pesq_NumPupas = oPesquisa.getPupas();	
		frmInp_Pesq_Observaciones = oPesquisa.getObservacion();
		
		frmInp_Pesq_DensLarvJovEstaIyII = 
			calcularDensidad( frmInp_Pesq_NumLarvJovEstaIyII, frmInp_Pesq_TotalCucharonadas);
		
		frmInp_Pesq_DensLarvAduEstaIIIyIV = 
			calcularDensidad(frmInp_Pesq_NumLarvAduEstaIIIyIV, frmInp_Pesq_TotalCucharonadas);
		
		frmInp_Pesq_DensPupas = 
			calcularDensidad(frmInp_Pesq_NumPupas, frmInp_Pesq_TotalCucharonadas);		
		
		frmDt_ListaPesquisas.remove(oPesquisa);
		oPesquisa = null;
	}
	
	public void calcularPesqDensLarvJovenes(ActionEvent evt){
		frmInp_Pesq_DensLarvJovEstaIyII = 
			calcularDensidad( frmInp_Pesq_NumLarvJovEstaIyII, frmInp_Pesq_TotalCucharonadas);
	}

	public void calcularPesqDensLarvAdultas(ActionEvent evt){
		frmInp_Pesq_DensLarvAduEstaIIIyIV = 
			calcularDensidad( frmInp_Pesq_NumLarvAduEstaIIIyIV, frmInp_Pesq_TotalCucharonadas);
	}	

	public void calcularPesqDensPupas(ActionEvent evt){
		frmInp_Pesq_DensPupas = 
			calcularDensidad( frmInp_Pesq_NumPupas, frmInp_Pesq_TotalCucharonadas);
	}		
	
	public void setSeleccionPesquisa(CriaderosPesquisa pPesquisa){
		oSelPesquisa = pPesquisa;
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
		
		RequestContext.getCurrentInstance().execute("dlgAgregarIntervencion.show();");
	}
	
	public void agregarIntervencion(ActionEvent evt){
		CriaderosIntervencion oIntervencion = new CriaderosIntervencion();

		
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
		
		oIntervencion.setFechaIntervencion(frmCal_Intv_FechaIntervencion);
		oIntervencion.setLimpieza(frmInp_Intv_Limpieza);
		oIntervencion.setEva(frmInp_Intv_Eva);
		oIntervencion.setDrenaje(frmInp_Intv_Drenaje);
		oIntervencion.setRelleno(frmInp_Intv_Relleno);
		oIntervencion.setBsphaericus(frmInp_Intv_Bsphaericus);
		oIntervencion.setBti(frmInp_Intv_Bti);
		oIntervencion.setConsumoBsphaericus(frmInp_Intv_ConsumoBsphaericus);
		oIntervencion.setConsumoBti(frmInp_Intv_ConsumoBti);
		oIntervencion.setObservaciones(frmInp_Intv_Observaciones);
		oIntervencion.setCriaderosPesquisa(oSelPesquisa);
		
		InfoResultado oResultado = null;
		FacesMessage msg = null;
		try{
			
			oResultado = srvIntervencion.guardarIntervencion(oIntervencion);
			if( !oResultado.isOk() || oResultado.isExcepcion() ){
				msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance();
				return;
			}
			oIntervencion = (CriaderosIntervencion) oResultado.getObjeto();
		}catch(Exception e){
			msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error Agregando Intervencion",e.getMessage());
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}
		
		if( frmDt_ListaIntervencion == null ) frmDt_ListaIntervencion = new ArrayList<CriaderosIntervencion>();
		frmDt_ListaIntervencion.add(oIntervencion);
		
	}
	
	public void openModalEliminarIntervencion(ActionEvent evt){
		RequestContext.getCurrentInstance().execute("dlgConfirmaEliminarIntervencion();");
	}
	
	public void setEliminarIntervencion(CriaderosIntervencion pIntervencion){
		oSelIntervencion = pIntervencion;
	}
	
	public void eliminarIntervencion(ActionEvent evt){		
		InfoResultado oResultado = null;
		FacesMessage msg = null;
		try{
			
			oResultado = null;
			oResultado = srvPosInspeccion.eliminarPosInspeccionPorIntervencion(oSelIntervencion);
			if( !oResultado.isOk() || oResultado.isExcepcion()){
				msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}			
			
			oResultado = null;
			oResultado = srvIntervencion.eliminarIntervencion(oSelIntervencion.getCriaderoIntervencionId());
			if( !oResultado.isOk() || oResultado.isExcepcion()){
				msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			
			frmDt_ListaPosInspeccion = new ArrayList<CriaderosPosInspeccion>();
			frmDt_ListaIntervencion.remove(oSelIntervencion);
			oSelIntervencion = null;
			
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Registros Eliminados","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
			
		}catch(Exception e){
			msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error Eliminando Intervencion", e.getMessage());
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;					
		}		
		
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
		
		frmDt_ListaPesquisas.remove(oIntervencion);
		oIntervencion = null;
	}
	
	public void setSeleccionIntervencion(CriaderosIntervencion pIntervencion){
		oSelIntervencion = pIntervencion;
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
		
		RequestContext.getCurrentInstance().execute("dlgAgregarPosInspeccion.show();");
	}
	
	public void agregarPosInspeccion(ActionEvent evt){
		CriaderosPosInspeccion oPosInspeccion = new CriaderosPosInspeccion();

		
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
		
		oPosInspeccion.setFechaInspeccion(frmCal_PosIns_FechaInspeccion);
		oPosInspeccion.setPuntosMuestreados(frmInp_PosIns_PuntosMuestreados);
		oPosInspeccion.setCuchColectadas(frmInp_PosIns_TotalCucharonadas);
		oPosInspeccion.setCuchPositivas(frmInp_PosIns_CucharonadasPositivas);
		oPosInspeccion.setLarvasJovenes(frmInp_PosIns_NumLarvJovEstaIyII);
		oPosInspeccion.setLarvasAdultas(frmInp_PosIns_NumLarvAduEstaIIIyIV);
		oPosInspeccion.setPupas(frmInp_PosIns_NumPupas);
		oPosInspeccion.setObservacion(frmInp_PosIns_Observaciones);
		
		InfoResultado oResultado = null;
		FacesMessage msg = null;
		try{
			
			oResultado = srvPosInspeccion.guardarPosInspeccion(oPosInspeccion);
			if( !oResultado.isOk() || oResultado.getObjeto() != null ){
				msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			oPosInspeccion = (CriaderosPosInspeccion) oResultado.getObjeto();
			
		}catch(Exception e){
			msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error Agregando PosInspeccion",e.getMessage());
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if( frmDt_ListaPosInspeccion == null ) frmDt_ListaPosInspeccion = new ArrayList<CriaderosPosInspeccion>();
		frmDt_ListaPosInspeccion.add(oPosInspeccion);
		
	}
	
	public void openModalConfirmaEliminarPosInspeccion(ActionEvent evt){
		RequestContext.getCurrentInstance().execute("dlgConfirmaEliminarPosInspeccion.show();");
	}
	
	public void setEliminarPosInspeccion(CriaderosPosInspeccion pPosInspeccion){
		oSelPosInspeccion = pPosInspeccion;
	}
	
	public void eliminarPosInspeccion(ActionEvent evt){
		InfoResultado oResultado = new InfoResultado();
		FacesMessage msg = null;
		try{
			
			oResultado = srvPosInspeccion.eliminarPosInspeccion(oSelPosInspeccion.getCriaderoPosInspeccionId());
			if( !oResultado.isOk() || oResultado.isExcepcion() ){
				msg = Mensajes.enviarMensaje(oResultado);
				if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;				
			}
			frmDt_ListaPosInspeccion.remove(oSelPosInspeccion);
			oSelPosInspeccion = null;
			
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Registros Eliminados","");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
			
		}catch(Exception e){
			msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error Eliminando PosInspeccion",e.getMessage());
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;			
		}
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
		
		frmDt_ListaPosInspeccion.remove(oPosInspeccion);
		oPosInspeccion = null;
	}
	
	public void calcularPosInsDensLarvJovenes(ActionEvent evt){
		frmInp_PosIns_DensLarvJovEstaIyII = 
			calcularDensidad( frmInp_PosIns_NumLarvJovEstaIyII, frmInp_Pesq_TotalCucharonadas);
	}

	public void calcularPosInsDensLarvAdultas(ActionEvent evt){
		frmInp_PosIns_DensLarvAduEstaIIIyIV = 
			calcularDensidad( frmInp_PosIns_NumLarvAduEstaIIIyIV, frmInp_Pesq_TotalCucharonadas);
	}	

	public void calcularPosInsDensPupas(ActionEvent evt){
		frmInp_PosIns_DensPupas = 
			calcularDensidad( frmInp_PosIns_NumPupas, frmInp_Pesq_TotalCucharonadas);
	}	
	
	
	/*
	 * Metodos para Agregar Clases de Criaderos y Especies anopheles
	 * ML_CRIADEROSESPECIES
	 * ML_CLASFCRIADEROS
	 * 
	 */
	
	public void openModalAgregarCatOtros(ActionEvent evt){
		this.componenteIdCatOtros = evt.getComponent().getClientId();
		RequestContext.getCurrentInstance().execute("dlgOpenModalOtros.show();");
	}
	
	public void agregaCatalogoOtros(ActionEvent evt){
		InfoResultado oResultado = new InfoResultado();
		String codCatalogo = null;
		String dependencia = null;
		AddCatalogo oCat = null;
		
		if(this.componenteIdCatOtros.equals("cmbAddOtroClassCriaderos")){
			dependencia = "ML_CLASFCRIADEROS";
		}else if( this.componenteIdCatOtros.equals("cmbAddOtroEspecieAnopheles")){
			dependencia = "ML_CRIADEROSESPECIES";
		}
			
		codCatalogo = 
			frmInp_OtroValorCatalogo
				.replace("�", "a").replace("�", "e").replace("�","i").replace("�","o").replace("�","u")
				.replace("�", "a").replace("�", "e").replace("�","i").replace("�","o").replace("�","u")
				.replace("�","n")
				.toUpperCase().trim();
		
		oResultado = srvCriadero.agregarCatOtros(frmInp_OtroValorCatalogo, codCatalogo, dependencia);
		if( oResultado.isOk() && oResultado.getObjeto() != null ){
			if(this.componenteIdCatOtros.equals("cmbAddOtroClassCriaderos")){
				oCat = (AddCatalogo) oResultado.getObjeto();
				itemsClasesCriaderos = srvCatClasesCriadero.ListarActivos();
				frmSom_ClasificacionCriadero = oCat.getCatalogoId();
			}else if( this.componenteIdCatOtros.equals("cmbAddOtroEspecieAnopheles")){
				oCat = (AddCatalogo) oResultado.getObjeto();
				itemsEspeciesAnopheles = srvCatEspAnopheles.ListarActivos();
				frmSom_EspecieAnopheles = oCat.getCatalogoId();
			}			
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
		}else{
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
		}
			
	}

	public void guardarEncuesta(ActionEvent evt){
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
		
		if( frmInp_SemanaEpidemiologica == null || frmInp_SemanaEpidemiologica.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Semana Epidemiologica");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;
		}
		
		if( frmInp_AxoEpidemiologico == null || frmInp_AxoEpidemiologico.equals("0")){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar A�o Epidemiologica");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}

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
		
		if( frmSom_EspecieAnopheles == null || frmSom_EspecieAnopheles.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Turbidez del agua");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmInp_Observaciones == null || frmInp_Observaciones.isEmpty() ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Observaciones");
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
		
		if( frmDt_ListaIntervencion == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Es necesario agregar una Intervencion");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		
		if( frmDt_ListaIntervencion.isEmpty() ){
			oResultado.setOk(false);
			oResultado.setMensaje("Es necesario agregar una Intervencion");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmDt_ListaPosInspeccion == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Es necesario agregar una PosInspeccion");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmDt_ListaPosInspeccion.isEmpty() ){
			oResultado.setOk(false);
			oResultado.setMensaje("Es necesario agregar una PosInspeccion");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}	
		
		return oResultado;
	}
	
	public InfoResultado guardar(){
		InfoResultado oResultado = null;
		long auxIdCriadero = 0;
		
		try{
		
			if( oCriaderoActual == null ){
				oCriaderoActual = new Criadero();
				oCriaderoActual.setUsuarioRegistro(Utilidades.obtenerInfoSesion().getUsername());
				oCriaderoActual.setFechaRegistro(new Date());
			}else{
				auxIdCriadero = oCriaderoActual.getCriaderoId();
			}
			
			oCriaderoActual.setCodigo(frmInp_CodigoCriadero);
			oCriaderoActual.setAreaActual(frmInp_AreaActualCriadero);
			oCriaderoActual.setAreaMax(frmInp_DistMaxCasaProxima);
			
			oResultado = srvCatClasesCriadero.Encontrar(frmSom_ClasificacionCriadero);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				ClasesCriaderos oClaseCriadero = (ClasesCriaderos) oResultado.getObjeto();
				oCriaderoActual.setClaseCriadero(oClaseCriadero);
			}else{
				oCriaderoActual.setClaseCriadero(null);
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
				oCriaderoActual.setExposicionSol(null);
			}		
	
			oResultado = null;
			AbundanciaFauna oAbundanciaFauna = null;
	
			oResultado = srvCatAbndFau.Encontrar(frmSom_FaunaAnfibios);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbundanciaFauna = (AbundanciaFauna) oResultado.getObjeto();
				oCriaderoActual.setFaunaAnfibios(oAbundanciaFauna);
			}else{
				oCriaderoActual.setFaunaAnfibios(null);
			}		
			
			oResultado = srvCatAbndFau.Encontrar(frmSom_FaunaInsectos);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbundanciaFauna = (AbundanciaFauna) oResultado.getObjeto();
				oCriaderoActual.setFaunaInsecto(oAbundanciaFauna);
			}else{
				oCriaderoActual.setFaunaInsecto(null);
			}			
			
			oResultado = srvCatAbndFau.Encontrar(frmSom_FaunaPeces);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbundanciaFauna = (AbundanciaFauna) oResultado.getObjeto();
				oCriaderoActual.setFaunaPeces(oAbundanciaFauna);
			}else{
				oCriaderoActual.setFaunaPeces(null);
			}			
			
			oCriaderoActual.setFechaDatos(frmCal_FechaNotificacion);
			oCriaderoActual.setLatitud(frmInp_Latitud);
			oCriaderoActual.setLongitud(frmInp_Longitud);
			
			oResultado = null;
			oResultado = srvCatMovAgua.Encontrar(frmSom_MovimientoAgua);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				MovimientoAgua oMovAgua = (MovimientoAgua) oResultado.getObjeto();
				oCriaderoActual.setMovimientoAgua(oMovAgua);
			}else{
				oCriaderoActual.setMovimientoAgua(null);
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
				oCriaderoActual.setTipoCriadero(null);
			}			
	
			oResultado = null;
			oResultado = srvCatTurbAgua.Encontrar(frmSom_TurbidezAgua);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				TurbidezAgua oTurbAgua = (TurbidezAgua) oResultado.getObjeto();
				oCriaderoActual.setTurbidezAgua(oTurbAgua);
			}else{
				oCriaderoActual.setTurbidezAgua(null);
			}		
			
			oResultado = null;
			AbundanciaVegetacion oAbunVeg = null;
			oResultado = srvCatTurbAgua.Encontrar(frmSom_VegEmergente);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbunVeg = (AbundanciaVegetacion) oResultado.getObjeto();
				oCriaderoActual.setVegEmergente(oAbunVeg);
			}else{
				oCriaderoActual.setVegEmergente(null);
			}		
	
			oResultado = srvCatTurbAgua.Encontrar(frmSom_VegFlotante);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbunVeg = (AbundanciaVegetacion) oResultado.getObjeto();
				oCriaderoActual.setVegFlotante(oAbunVeg);
			}else{
				oCriaderoActual.setVegFlotante(null);
			}		
	
			oResultado = srvCatTurbAgua.Encontrar(frmSom_VegSumergida);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbunVeg = (AbundanciaVegetacion) oResultado.getObjeto();
				oCriaderoActual.setVegSumergida(oAbunVeg);
			}else{
				oCriaderoActual.setVegSumergida(null);
			}			
	
			oResultado = srvCatTurbAgua.Encontrar(frmSom_VegVertical);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				oAbunVeg = (AbundanciaVegetacion) oResultado.getObjeto();
				oCriaderoActual.setVegVertical(oAbunVeg);
			}else{
				oCriaderoActual.setVegVertical(null);
			}		
		
			oResultado = null;
			oResultado = srvCriadero.guardarCriadero(oCriaderoActual);
			if( !oResultado.isOk() || oResultado.isExcepcion()){
				return oResultado;
			}
			oCriaderoActual = (Criadero) oResultado.getObjeto();
			
			if( frmDt_ListaPesquisas != null ){
				for( CriaderosPesquisa oPesquisa : frmDt_ListaPesquisas ){
					oResultado = null;
					oPesquisa.setCriadero(oCriaderoActual);
					oResultado = srvPesquisa.guardarPesquisa(oPesquisa);
					if( !oResultado.isOk() || oResultado.isExcepcion() ){
						return oResultado;
					}
					oPesquisa = (CriaderosPesquisa) oResultado.getObjeto();
				}
			}	

			
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

	
	
	
	/*
	 * Seccion metodos getter and setters propiedades expuestas a la capa vista 
	 */
		
	
	public BigDecimal getFrmInp_SemanaEpidemiologica() {
		return frmInp_SemanaEpidemiologica;
	}

	public void setFrmInp_SemanaEpidemiologica(
			BigDecimal frmInp_SemanaEpidemiologica) {
		this.frmInp_SemanaEpidemiologica = frmInp_SemanaEpidemiologica;
	}

	public BigDecimal getFrmInp_AxoEpidemiologico() {
		return frmInp_AxoEpidemiologico;
	}

	public void setFrmInp_AxoEpidemiologico(BigDecimal frmInp_AxoEpidemiologico) {
		this.frmInp_AxoEpidemiologico = frmInp_AxoEpidemiologico;
	}

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

	public String getFrmInp_ClasificacionCriaderoOtro() {
		return frmInp_ClasificacionCriaderoOtro;
	}

	public void setFrmInp_ClasificacionCriaderoOtro(
			String frmInp_ClasificacionCriaderoOtro) {
		this.frmInp_ClasificacionCriaderoOtro = frmInp_ClasificacionCriaderoOtro;
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

	public Long getFrmSom_EspecieAnopheles() {
		return frmSom_EspecieAnopheles;
	}

	public void setFrmSom_EspecieAnopheles(Long frmSom_EspecieAnopheles) {
		this.frmSom_EspecieAnopheles = frmSom_EspecieAnopheles;
	}

	public String getFrmInp_EspecieAnophelesOtra() {
		return frmInp_EspecieAnophelesOtra;
	}

	public void setFrmInp_EspecieAnophelesOtra(String frmInp_EspecieAnophelesOtra) {
		this.frmInp_EspecieAnophelesOtra = frmInp_EspecieAnophelesOtra;
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

	public BigDecimal getFrmInp_Pesq_PuntosMuestreados() {
		return frmInp_Pesq_PuntosMuestreados;
	}

	public void setFrmInp_Pesq_PuntosMuestreados(
			BigDecimal frmInp_Pesq_PuntosMuestreados) {
		this.frmInp_Pesq_PuntosMuestreados = frmInp_Pesq_PuntosMuestreados;
	}

	public BigDecimal getFrmInp_Pesq_TotalCucharonadas() {
		return frmInp_Pesq_TotalCucharonadas;
	}

	public void setFrmInp_Pesq_TotalCucharonadas(
			BigDecimal frmInp_Pesq_TotalCucharonadas) {
		this.frmInp_Pesq_TotalCucharonadas = frmInp_Pesq_TotalCucharonadas;
	}

	public BigDecimal getFrmInp_Pesq_CucharonadasPositivas() {
		return frmInp_Pesq_CucharonadasPositivas;
	}

	public void setFrmInp_Pesq_CucharonadasPositivas(
			BigDecimal frmInp_Pesq_CucharonadasPositivas) {
		this.frmInp_Pesq_CucharonadasPositivas = frmInp_Pesq_CucharonadasPositivas;
	}

	public BigDecimal getFrmInp_Pesq_NumLarvJovEstaIyII() {
		return frmInp_Pesq_NumLarvJovEstaIyII;
	}

	public void setFrmInp_Pesq_NumLarvJovEstaIyII(
			BigDecimal frmInp_Pesq_NumLarvJovEstaIyII) {
		this.frmInp_Pesq_NumLarvJovEstaIyII = frmInp_Pesq_NumLarvJovEstaIyII;
	}

	public BigDecimal getFrmInp_Pesq_NumLarvAduEstaIIIyIV() {
		return frmInp_Pesq_NumLarvAduEstaIIIyIV;
	}

	public void setFrmInp_Pesq_NumLarvAduEstaIIIyIV(
			BigDecimal frmInp_Pesq_NumLarvAduEstaIIIyIV) {
		this.frmInp_Pesq_NumLarvAduEstaIIIyIV = frmInp_Pesq_NumLarvAduEstaIIIyIV;
	}

	public BigDecimal getFrmInp_Pesq_NumPupas() {
		return frmInp_Pesq_NumPupas;
	}

	public void setFrmInp_Pesq_NumPupas(BigDecimal frmInp_Pesq_NumPupas) {
		this.frmInp_Pesq_NumPupas = frmInp_Pesq_NumPupas;
	}

	public BigDecimal getFrmInp_Pesq_DensLarvJovEstaIyII() {
		return frmInp_Pesq_DensLarvJovEstaIyII;
	}

	public void setFrmInp_Pesq_DensLarvJovEstaIyII(
			BigDecimal frmInp_Pesq_DensLarvJovEstaIyII) {
		this.frmInp_Pesq_DensLarvJovEstaIyII = frmInp_Pesq_DensLarvJovEstaIyII;
	}

	public BigDecimal getFrmInp_Pesq_DensLarvAduEstaIIIyIV() {
		return frmInp_Pesq_DensLarvAduEstaIIIyIV;
	}

	public void setFrmInp_Pesq_DensLarvAduEstaIIIyIV(
			BigDecimal frmInp_Pesq_DensLarvAduEstaIIIyIV) {
		this.frmInp_Pesq_DensLarvAduEstaIIIyIV = frmInp_Pesq_DensLarvAduEstaIIIyIV;
	}

	public BigDecimal getFrmInp_Pesq_DensPupas() {
		return frmInp_Pesq_DensPupas;
	}

	public void setFrmInp_Pesq_DensPupas(BigDecimal frmInp_Pesq_DensPupas) {
		this.frmInp_Pesq_DensPupas = frmInp_Pesq_DensPupas;
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

	public BigDecimal getFrmInp_Intv_Limpieza() {
		return frmInp_Intv_Limpieza;
	}

	public void setFrmInp_Intv_Limpieza(BigDecimal frmInp_Intv_Limpieza) {
		this.frmInp_Intv_Limpieza = frmInp_Intv_Limpieza;
	}

	public BigDecimal getFrmInp_Intv_Eva() {
		return frmInp_Intv_Eva;
	}

	public void setFrmInp_Intv_Eva(BigDecimal frmInp_Intv_Eva) {
		this.frmInp_Intv_Eva = frmInp_Intv_Eva;
	}

	public BigDecimal getFrmInp_Intv_Drenaje() {
		return frmInp_Intv_Drenaje;
	}

	public void setFrmInp_Intv_Drenaje(BigDecimal frmInp_Intv_Drenaje) {
		this.frmInp_Intv_Drenaje = frmInp_Intv_Drenaje;
	}

	public BigDecimal getFrmInp_Intv_Relleno() {
		return frmInp_Intv_Relleno;
	}

	public void setFrmInp_Intv_Relleno(BigDecimal frmInp_Intv_Relleno) {
		this.frmInp_Intv_Relleno = frmInp_Intv_Relleno;
	}

	public BigDecimal getFrmInp_Intv_Bsphaericus() {
		return frmInp_Intv_Bsphaericus;
	}

	public void setFrmInp_Intv_Bsphaericus(BigDecimal frmInp_Intv_Bsphaericus) {
		this.frmInp_Intv_Bsphaericus = frmInp_Intv_Bsphaericus;
	}

	public BigDecimal getFrmInp_Intv_Bti() {
		return frmInp_Intv_Bti;
	}

	public void setFrmInp_Intv_Bti(BigDecimal frmInp_Intv_Bti) {
		this.frmInp_Intv_Bti = frmInp_Intv_Bti;
	}

	public BigDecimal getFrmInp_Intv_SiembraPeces() {
		return frmInp_Intv_SiembraPeces;
	}

	public void setFrmInp_Intv_SiembraPeces(BigDecimal frmInp_Intv_SiembraPeces) {
		this.frmInp_Intv_SiembraPeces = frmInp_Intv_SiembraPeces;
	}

	public BigDecimal getFrmInp_Intv_ConsumoBsphaericus() {
		return frmInp_Intv_ConsumoBsphaericus;
	}

	public void setFrmInp_Intv_ConsumoBsphaericus(
			BigDecimal frmInp_Intv_ConsumoBsphaericus) {
		this.frmInp_Intv_ConsumoBsphaericus = frmInp_Intv_ConsumoBsphaericus;
	}

	public BigDecimal getFrmInp_Intv_ConsumoBti() {
		return frmInp_Intv_ConsumoBti;
	}

	public void setFrmInp_Intv_ConsumoBti(BigDecimal frmInp_Intv_ConsumoBti) {
		this.frmInp_Intv_ConsumoBti = frmInp_Intv_ConsumoBti;
	}

	public String getFrmInp_Intv_Observaciones() {
		return frmInp_Intv_Observaciones;
	}

	public void setFrmInp_Intv_Observaciones(String frmInp_Intv_Observaciones) {
		this.frmInp_Intv_Observaciones = frmInp_Intv_Observaciones;
	}

	public List<CriaderosIntervencion> getFrmDt_ListaIntervencion() {
		return frmDt_ListaIntervencion;
	}

	public void setFrmDt_ListaIntervencion(
			List<CriaderosIntervencion> frmDt_ListaIntervencion) {
		this.frmDt_ListaIntervencion = frmDt_ListaIntervencion;
	}

	public Date getFrmCal_PosIns_FechaInspeccion() {
		return frmCal_PosIns_FechaInspeccion;
	}

	public void setFrmCal_PosIns_FechaInspeccion(Date frmCal_PosIns_FechaInspeccion) {
		this.frmCal_PosIns_FechaInspeccion = frmCal_PosIns_FechaInspeccion;
	}

	public BigDecimal getFrmInp_PosIns_PuntosMuestreados() {
		return frmInp_PosIns_PuntosMuestreados;
	}

	public void setFrmInp_PosIns_PuntosMuestreados(
			BigDecimal frmInp_PosIns_PuntosMuestreados) {
		this.frmInp_PosIns_PuntosMuestreados = frmInp_PosIns_PuntosMuestreados;
	}

	public BigDecimal getFrmInp_PosIns_TotalCucharonadas() {
		return frmInp_PosIns_TotalCucharonadas;
	}

	public void setFrmInp_PosIns_TotalCucharonadas(
			BigDecimal frmInp_PosIns_TotalCucharonadas) {
		this.frmInp_PosIns_TotalCucharonadas = frmInp_PosIns_TotalCucharonadas;
	}

	public BigDecimal getFrmInp_PosIns_CucharonadasPositivas() {
		return frmInp_PosIns_CucharonadasPositivas;
	}

	public void setFrmInp_PosIns_CucharonadasPositivas(
			BigDecimal frmInp_PosIns_CucharonadasPositivas) {
		this.frmInp_PosIns_CucharonadasPositivas = frmInp_PosIns_CucharonadasPositivas;
	}

	public BigDecimal getFrmInp_PosIns_NumLarvJovEstaIyII() {
		return frmInp_PosIns_NumLarvJovEstaIyII;
	}

	public void setFrmInp_PosIns_NumLarvJovEstaIyII(
			BigDecimal frmInp_PosIns_NumLarvJovEstaIyII) {
		this.frmInp_PosIns_NumLarvJovEstaIyII = frmInp_PosIns_NumLarvJovEstaIyII;
	}

	public BigDecimal getFrmInp_PosIns_NumLarvAduEstaIIIyIV() {
		return frmInp_PosIns_NumLarvAduEstaIIIyIV;
	}

	public void setFrmInp_PosIns_NumLarvAduEstaIIIyIV(
			BigDecimal frmInp_PosIns_NumLarvAduEstaIIIyIV) {
		this.frmInp_PosIns_NumLarvAduEstaIIIyIV = frmInp_PosIns_NumLarvAduEstaIIIyIV;
	}

	public BigDecimal getFrmInp_PosIns_NumPupas() {
		return frmInp_PosIns_NumPupas;
	}

	public void setFrmInp_PosIns_NumPupas(BigDecimal frmInp_PosIns_NumPupas) {
		this.frmInp_PosIns_NumPupas = frmInp_PosIns_NumPupas;
	}

	public BigDecimal getFrmInp_PosIns_DensLarvJovEstaIyII() {
		return frmInp_PosIns_DensLarvJovEstaIyII;
	}

	public void setFrmInp_PosIns_DensLarvJovEstaIyII(
			BigDecimal frmInp_PosIns_DensLarvJovEstaIyII) {
		this.frmInp_PosIns_DensLarvJovEstaIyII = frmInp_PosIns_DensLarvJovEstaIyII;
	}

	public BigDecimal getFrmInp_PosIns_DensLarvAduEstaIIIyIV() {
		return frmInp_PosIns_DensLarvAduEstaIIIyIV;
	}

	public void setFrmInp_PosIns_DensLarvAduEstaIIIyIV(
			BigDecimal frmInp_PosIns_DensLarvAduEstaIIIyIV) {
		this.frmInp_PosIns_DensLarvAduEstaIIIyIV = frmInp_PosIns_DensLarvAduEstaIIIyIV;
	}

	public BigDecimal getFrmInp_PosIns_DensPupas() {
		return frmInp_PosIns_DensPupas;
	}

	public void setFrmInp_PosIns_DensPupas(BigDecimal frmInp_PosIns_DensPupas) {
		this.frmInp_PosIns_DensPupas = frmInp_PosIns_DensPupas;
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

	public LazyDataModel<Criadero> getLazyCriaderos() {
		return lazyCriaderos;
	}

	public void setLazyCriaderos(LazyDataModel<Criadero> lazyCriaderos) {
		this.lazyCriaderos = lazyCriaderos;
	}

	public String getFrmInp_OtroValorCatalogo() {
		return frmInp_OtroValorCatalogo;
	}

	public void setFrmInp_OtroValorCatalogo(String frmInp_OtroValorCatalogo) {
		this.frmInp_OtroValorCatalogo = frmInp_OtroValorCatalogo;
	}

	
	

	
	
	
}