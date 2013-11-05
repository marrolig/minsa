package ni.gob.minsa.malaria.interfaz.investigacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.datos.estructura.UnidadDA;
import ni.gob.minsa.malaria.datos.general.CatalogoElementoDA;
import ni.gob.minsa.malaria.datos.investigacion.InvestigacionDA;
import ni.gob.minsa.malaria.datos.investigacion.InvestigacionHospitalarioDA;
import ni.gob.minsa.malaria.datos.investigacion.InvestigacionLugarDA;
import ni.gob.minsa.malaria.datos.investigacion.InvestigacionMedicamentoDA;
import ni.gob.minsa.malaria.datos.investigacion.InvestigacionSintomaDA;
import ni.gob.minsa.malaria.datos.investigacion.InvestigacionTransfusionDA;
import ni.gob.minsa.malaria.datos.investigacion.SintomaLugarAnteDA;
import ni.gob.minsa.malaria.datos.investigacion.SintomaLugarInicioDA;
import ni.gob.minsa.malaria.datos.investigacion.SintomaLugarOtroDA;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadDA;
import ni.gob.minsa.malaria.datos.poblacion.DivisionPoliticaDA;
import ni.gob.minsa.malaria.datos.poblacion.PaisDA;
import ni.gob.minsa.malaria.datos.vigilancia.MuestreoHematicoDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.investigacion.ClasificacionCaso;
import ni.gob.minsa.malaria.modelo.investigacion.ClasificacionClinica;
import ni.gob.minsa.malaria.modelo.investigacion.ConfirmacionDiagnostico;
import ni.gob.minsa.malaria.modelo.investigacion.EstadoFebril;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionHospitalario;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionLugar;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMalaria;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMedicamento;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionSintoma;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionTransfusion;
import ni.gob.minsa.malaria.modelo.investigacion.Medicamento;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarAnte;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarInicio;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarOtro;
import ni.gob.minsa.malaria.modelo.investigacion.TipoComplicacion;
import ni.gob.minsa.malaria.modelo.investigacion.TipoRecurrencia;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Pais;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoHematico;
import ni.gob.minsa.malaria.reglas.InvestigacionValidacion;
import ni.gob.minsa.malaria.reglas.Operacion;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionHospitalarioService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionLugarService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionMedicamentoService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionSintomaService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionTransfusionService;
import ni.gob.minsa.malaria.servicios.investigacion.SintomaLugarAnteService;
import ni.gob.minsa.malaria.servicios.investigacion.SintomaLugarInicioService;
import ni.gob.minsa.malaria.servicios.investigacion.SintomaLugarOtroService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService;
import ni.gob.minsa.malaria.servicios.poblacion.PaisService;
import ni.gob.minsa.malaria.servicios.vigilancia.MuestreoHematicoService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * Servicio para la capa de presentación de la página 
 * investigacion/investigacionMalaria.xhtml
 *
 * <p>
 * @author Félix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 20/10/2013
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class InvestigacionMalariaBean implements Serializable{
private static final long serialVersionUID = 1L;
	
	protected InfoSesion infoSesion;
	private int capaActiva;
	
	// -------------------------------------------------------------
	// Modo
	// 
	// El atributo modo indica si se mostraran los muestreos sin o con  
	// M10 sin cerrar y por tanto es la variable que gestionará el bloqueo 
	// y peticiones desde el interfaz al bean.
	//
	// Valores:
	// 0 : Se podrán buscar muestreos sin investigación o con una 
	//      investigación sin cerrar. En este modo se podrá agregar 
	//      o editar una investigación según corresponda.
	//     
	// 1 : Se podrán buscar muestreos con investigaciones cerradas
	//	   para consultar su información exclusivamente.	
	// -------------------------------------------------------------
	private int modo=0;
	
	// -------------------------------------------------------------
	// Entidad Administrativa
	//
	// Objetos vinculados a las entidades administrativas 
	// indirectas a las cuales el usuario tiene autorización según 
	// el listado de unidades de salud autorizadas
	// -------------------------------------------------------------
	private List<EntidadAdtva> entidades;
	private long entidadSelectedId;

	// -------------------------------------------------------------
	// Unidad de Salud
	//
	// Objetos vinculados a las unidades de salud para las cuales
	// el usuario tiene autorización explícita.
	// -------------------------------------------------------------
	private List<Unidad> unidades;
	private Unidad unidadSelected;
	private long unidadSelectedId;
	
	// atributos vinculados al muestreo hemático al cual se efectuará una 
	// investigación.
	private List<SelectItem> aniosEpidemiologicos;
	private int anioEpiSelected=0;
	private LazyDataModel<MuestreoHematico> muestreosHematicos;
	private MuestreoHematico muestreoHematicoSelected;
	private long muestreoHematicoSelectedId;
	private int numMuestreos;
	
	//atributos vinculados a investigacion de malaria M10.
	private InvestigacionMalaria investigacionMalariaSelected;
	private long investigacionMalariaSelectedId;
	private String numeroCaso;
	private BigDecimal longitud;
	private BigDecimal latitud;
	private String pacienteEmbarazada="";
	
	//atributos vinculados a investigacion de medicamentos.
	private List<InvestigacionMedicamento> investigacionesMedicamentos;
	private List<ConfirmacionDiagnostico> diagnosticosEntidad;
	private long diagnosticoEntidadSelectedId;
	private List<ConfirmacionDiagnostico> diagnosticosCndr;
	private long diagnosticoCndrSelectedId;
	
	//atributos vinculados a investigacion de sintomas.
	private BigDecimal esSintomatico=BigDecimal.valueOf(Long.parseLong("0"));
	private Date fechaInicioSintoma;
	private BigDecimal sintomaInicio=BigDecimal.valueOf(Long.parseLong("1"));
	private List<EstadoFebril> estadosFebriles;
	private long estadoFebrilSelectedId;
	private InvestigacionSintoma investigacionSintomaSelected;
	private long investigacionSintomaSelectedId;
	private List<SintomaLugarAnte> sintomasLugaresAntes;
	private SintomaLugarAnte sintomaLugarAnteSelected;
	private long sintomaLugarAnteSelectedId;
	private List<SintomaLugarOtro> sintomasLugaresOtros;
	private SintomaLugarOtro sintomaLugarOtroSelected;
	private long sintomaLugarOtroSelectedId;
	private List<Pais> paises;
	
	private Pais paisLugInicioSelected;
	private long paisLugInicioSelectedId;
	private List<DivisionPolitica> deptsLugsInicios;
	private DivisionPolitica deptLugInicioSelected;
	private long deptLugInicioSelectedId;
	private List<DivisionPolitica> munisLugsInicios;
	private DivisionPolitica muniLugInicioSelected;
	private long muniLugInicioSelectedId;
	private Comunidad comuLugInicioSelected;
	private BigDecimal estadiaLugar;
	private BigDecimal personaConSintomaLugar=BigDecimal.valueOf(Long.parseLong("0"));
	private BigDecimal viajeZonaRiesgo=BigDecimal.valueOf(Long.parseLong("0"));
	private BigDecimal usaMosquitero;
	
	private Date fechaUltimaLugAnte;
	private BigDecimal estadiaLugAnte;
	private Pais paisLugAnteSelected;
	private long paisLugAnteSelectedId;
	private List<DivisionPolitica> deptsSintomasLugsAntes;
	private DivisionPolitica deptLugAnteSelected;
	private long deptLugAnteSelectedId;
	private List<DivisionPolitica> munisSintomasLugsAntes;
	private DivisionPolitica muniLugAnteSelected;
	private long muniLugAnteSelectedId;
	private Comunidad comuLugAnteSelected;
	private BigDecimal personaSintomaLugAnte=BigDecimal.valueOf(Long.parseLong("0"));
	
	private Date fechaUltimaLugOtro;
	private BigDecimal estadiaLugOtro;
	private Pais paisLugOtroSelected;
	private long paisLugOtroSelectedId;
	private List<DivisionPolitica> deptsSintomasLugsOtros;
	private DivisionPolitica deptLugOtroSelected;
	private long deptLugOtroSelectedId;
	private List<DivisionPolitica> munisSintomasLugsOtros;
	private DivisionPolitica muniLugOtroSelected;
	private long muniLugOtroSelectedId;
	private Comunidad comuLugOtroSelected;
	private BigDecimal autoMedicacionLugOtro=BigDecimal.valueOf(Long.parseLong("0"));
	private BigDecimal diagPositivoLugOtro=BigDecimal.valueOf(Long.parseLong("0"));
	private BigDecimal tratamientoCompletoLugOtro=BigDecimal.valueOf(Long.parseLong("0"));
	
	//atributos vinculados a antecedentes de transfusión.
	BigDecimal antTransfusion=BigDecimal.valueOf(0);
	private Date fechaTransfusion;
	private Pais paisTransfusionSelected;
	private long paisTransfusionSelectedId;
	private List<Unidad> unidadesTransfusion;
	private Unidad unidadTransfusionSelected;
	private long unidadTransfusionSelectedId;
	
	//atributos vinculados a manejo clínico -Investigación Hospitalaria	-
	private BigDecimal manejoClinico=BigDecimal.valueOf(Long.parseLong("0"));
	private Date inicioTratamientoMClinico;
	private Date finTratamientoMClinico;
	private List<Medicamento> medicamentosAntiMalaricos;
	private String medicamentosEnLista;
	private Medicamento medicamentoSelected;
	private long medicamentoSelectedId;
	private List<EntidadAdtva> entidadesMClinico;
	private EntidadAdtva entidadMClinicoSelected;
	private long entidadMClinicoSelectedId;
	private List<Unidad> unidadesMClinico;
	private Unidad unidadMClinicoSelected;
	private long unidadMClinicoSelectedId;
	private String numeroExpedienteMClinico;
	private Date fechaIngresoMClinico;
	private BigDecimal diasEstanciaMClinico;
	private BigDecimal convivientesTratadosMClinico;
	private BigDecimal colateralesTratadosMClinico;
	private BigDecimal tratamientoCompletoMClinico=BigDecimal.valueOf(Long.parseLong("0"));
	private BigDecimal tratamientoSupervisadoMClinico=BigDecimal.valueOf(Long.parseLong("0"));
	private BigDecimal controlParasitarioMClinico=BigDecimal.valueOf(Long.parseLong("0"));
	private BigDecimal numDiasPosterioresMClinico;
	private BigDecimal resultControlPositivoMClinico;
	private BigDecimal condicionFinalMClinico=BigDecimal.valueOf(Long.parseLong("1"));
	private Date fechaDefuncionMClinico;
	private BigDecimal automedicacionMClinico=BigDecimal.valueOf(Long.parseLong("0"));
	private String medicamentosAutomedicacionMClinico;
	
	//Atributos vinculados al resultado de la investigacion
		
	//Investigación Lugares
	private BigDecimal infeccionEnResidenciaRInv=BigDecimal.valueOf(Long.parseLong("1"));
	private Pais paisRInvSelected;
	private long paisRInvSelectedId;
	private List<DivisionPolitica> deptsSintomasRInv;
	private DivisionPolitica deptRInvSelected;
	private long deptRInvSelectedId;
	private List<DivisionPolitica> munisSintomasRInv;
	private DivisionPolitica muniRInvSelected;
	private long muniRInvSelectedId;
	private Comunidad comuRInvSelected;
	
	private Date fechaInfeccionRInv;
	private List<ClasificacionClinica> clasificsClinicasRInv;
	private ClasificacionClinica clasifClinicaRInvSelected;
	private long clasifClinicaRInvSelectedId;
	private List<TipoRecurrencia> tiposInfeccionesRInv;
	private TipoRecurrencia tipoInfeccionRInvSelected;
	private long tipoInfeccionRInvSelectedId;
	private List<TipoComplicacion> tiposComplicacionesRInv;
	private TipoComplicacion tipoComplicacionRInvSelected;
	private long tipoComplicacionRInvSelectedId;
	private List<ClasificacionCaso> clasificacionesCasosRInv;
	private ClasificacionCaso clasificacionCasoRInvSelected;
	private long clasificacionCasoRInvSelectedId;

	private String observaciones;
	private String responsableInvest;
	private String epidemiologo;
	private BigDecimal nivelAutorizacion=BigDecimal.valueOf(Long.parseLong("0"));
	private BigDecimal casoCerrado=BigDecimal.valueOf(Long.parseLong("0"));
	private Date fechaCierre;
	
	// ----------------------------------------------------------
	// atributos vinculados a los servicios y capa DAO
	// ----------------------------------------------------------
	private static EntidadAdtvaService entidadService = new EntidadAdtvaDA();
	private static UnidadService unidadService = new UnidadDA();
	private static DivisionPoliticaService divisionPoliticaService = new DivisionPoliticaDA();
	private static ComunidadService comunidadService = new ComunidadDA();
	private static PaisService paisService = new PaisDA();
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<ClasificacionCaso,Integer> clasificacionCasoService=new CatalogoElementoDA(ClasificacionCaso.class,"ClasificacionCaso");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<EstadoFebril, Integer> estadoFebrilService=new CatalogoElementoDA(EstadoFebril.class,"EstadoFebril");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<ClasificacionClinica, Integer> clasificacionClinicaService=new CatalogoElementoDA(ClasificacionClinica.class,"ClasificacionClinica");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<ConfirmacionDiagnostico, Integer> confirmacionDiagnosticoService=new CatalogoElementoDA(ConfirmacionDiagnostico.class,"ConfirmacionDiagnostico");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<Medicamento, Integer> medicamentoService=new CatalogoElementoDA(Medicamento.class,"Medicamento");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TipoComplicacion, Integer> tipoComplicacionService=new CatalogoElementoDA(TipoComplicacion.class,"TipoComplicacion");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TipoRecurrencia, Integer> tipoRecurrenciaService=new CatalogoElementoDA(TipoRecurrencia.class,"TipoRecurrencia");
	
	
	private static MuestreoHematicoService muestreoHematicoService = new MuestreoHematicoDA();
	private static InvestigacionService investigacionService = new InvestigacionDA();
	private static InvestigacionMedicamentoService investigacionMedicamentoService = new InvestigacionMedicamentoDA();
	private static InvestigacionSintomaService investigacionSintomaService = new InvestigacionSintomaDA();
	private static SintomaLugarAnteService sintomaLugarAnteService  = new SintomaLugarAnteDA();
	private static SintomaLugarOtroService sintomaLugarOtroService = new SintomaLugarOtroDA();
	private static SintomaLugarInicioService sintomaLugarInicioService = new SintomaLugarInicioDA();
	private static InvestigacionTransfusionService investigacionTransfusion = new InvestigacionTransfusionDA();
	private static InvestigacionHospitalarioService investigacionHospitalario = new InvestigacionHospitalarioDA();
	private static InvestigacionLugarService investigacionLugarService = new InvestigacionLugarDA();
	
	
	/**************************************************
	 * Constructor
	 **************************************************/
	public InvestigacionMalariaBean(){
		init();
	}
	
	/**************************************************
	 * Eventos
	 **************************************************/
	
	
	public void onModoSelected(){
		this.aniosEpidemiologicos = new LinkedList<SelectItem>();
		Integer anioActual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
		this.anioEpiSelected = anioActual;
		
		if (this.modo == 0) {
			for (int i = 0; i < 3; i++) {
				this.aniosEpidemiologicos.add(new SelectItem(anioActual,anioActual.toString()));
				anioActual -= 1;
			}
		}else{
			if(!(this.entidadSelectedId>0&& this.unidadSelectedId >0)){
				return;
			}
			List<Integer> oAños = muestreoHematicoService.ListarAñosConPositivosPorUnidad(this.entidadSelectedId,this.unidadSelectedId,(this.modo==0 ? true : false));
			if(oAños!=null){
				for(Integer año:oAños){
					this.aniosEpidemiologicos.add(new SelectItem(año,año.toString()));
				}
			}
		}
	}
	
	/**
	 * Evento que se ejecuta cuando el usuario selecciona un muestreo hemático
	 * de la grilla.  Traslada la M10 asociada al objeto seleccionado 
	 * a los controles del panel de detalle.
	 */
	public void onMuestreoHematicoSelected(SelectEvent iEvento) { 
		this.muestreoHematicoSelectedId = this.muestreoHematicoSelected.getMuestreoHematicoId();
		
		InfoResultado oInfoResultado=investigacionService.EncontrarPorMuestreoHematico(this.muestreoHematicoSelectedId);
		if (oInfoResultado.isExcepcion()) {
			FacesMessage msg = Mensajes.enviarMensaje(oInfoResultado);
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		this.capaActiva=2;
		this.investigacionMalariaSelected=(InvestigacionMalaria)oInfoResultado.getObjeto();
		this.investigacionMalariaSelectedId=this.investigacionMalariaSelected!=null ? this.investigacionMalariaSelected.getInvestigacionMalariaId() : 0;
		iniciarCapa2();
		//Si no se encuentra una Investigación para la E2 seleccionada, quiere decir que estamos agregando una nueva M10; 
		//de no ser nulo quiere decir que estamos editando una M10. 
		if (this.investigacionMalariaSelected != null) {
			
			this.numeroCaso=this.investigacionMalariaSelected.getNumeroCaso();
			this.longitud = this.investigacionMalariaSelected.getLongitudVivienda();
			this.latitud = this.investigacionMalariaSelected.getLatitudVivienda();
			this.esSintomatico = this.investigacionMalariaSelected.getSintomatico();
			
			if (this.investigacionMalariaSelected.getConfirmacionEntidad() != null) {
				this.diagnosticosEntidad = confirmacionDiagnosticoService.ListarActivos(this.investigacionMalariaSelected.getConfirmacionEntidad().getCodigo());
				this.diagnosticoEntidadSelectedId = this.investigacionMalariaSelected.getConfirmacionEntidad().getCatalogoId();
			} else{
				this.diagnosticosEntidad = confirmacionDiagnosticoService.ListarActivos();
			}
			if (this.investigacionMalariaSelected.getConfirmacionCndr() != null) {
				this.diagnosticosCndr = confirmacionDiagnosticoService.ListarActivos(this.investigacionMalariaSelected.getConfirmacionCndr().getCodigo());
				this.diagnosticoCndrSelectedId = this.investigacionMalariaSelected.getConfirmacionCndr().getCatalogoId();
			} else{
				this.diagnosticosCndr = confirmacionDiagnosticoService.ListarActivos();
			}
			
			// Valores asociados a investigación de sintomas
			if (this.esSintomatico.intValue() == 1) {
				 oInfoResultado = investigacionSintomaService.EncontrarPorInvestigacionMalaria(this.investigacionMalariaSelected.getInvestigacionMalariaId());
				if (oInfoResultado.isOk()) {
					this.investigacionSintomaSelected = (InvestigacionSintoma) oInfoResultado.getObjeto();
					this.investigacionSintomaSelectedId = this.investigacionSintomaSelected.getInvestigacionSintomaId();
					this.fechaInicioSintoma = this.investigacionSintomaSelected.getFechaInicioSintomas();
					if (this.investigacionSintomaSelected.getEstadoFebril() != null) {
						this.estadosFebriles = estadoFebrilService.ListarActivos(this.investigacionSintomaSelected.getEstadoFebril().getCodigo());
						this.estadoFebrilSelectedId = this.investigacionSintomaSelected.getEstadoFebril().getCatalogoId();
					}
					this.personaConSintomaLugar = this.investigacionSintomaSelected.getPersonasSintomas();
					obtenerSintomasLugaresAntes();
					obtenerSintomasLugaresOtros();
				}
			}
			if(this.estadosFebriles==null){
				this.estadosFebriles = estadoFebrilService.ListarActivos();
			}
			
			//Si es sintomático y los sintomas inician en un lugar diferente al lugar de resisencia,
			//se obtienen los valores asociados a al luger de inicio
			if (this.esSintomatico.intValue() == 1 && this.investigacionSintomaSelected != null) {
				if (this.investigacionSintomaSelected.getInicioResidencia().intValue() == 0) {
					this.sintomaInicio = this.investigacionSintomaSelected.getInicioResidencia();
					 oInfoResultado = sintomaLugarInicioService.EncontrarPorInvestigacionSintoma(
							this.investigacionSintomaSelected.getInvestigacionSintomaId());
					if (oInfoResultado.isOk()) {
						SintomaLugarInicio oLugInicio = (SintomaLugarInicio) oInfoResultado.getObjeto();
						this.sintomaInicio = oLugInicio.getInicioResidencia();
						if(oLugInicio.getPais()!=null){
							this.paisLugInicioSelected = oLugInicio.getPais();
							this.paisLugInicioSelectedId = oLugInicio.getPais().getPaisId();
						}else{
							InfoResultado oResultado = paisService.Encontrar(Utilidades.PAIS_CODIGO);
							Pais oPais = (Pais) oResultado.getObjeto();
							this.paisLugInicioSelected = oPais;
							this.paisLugInicioSelectedId = oPais.getPaisId();
							this.deptsLugsInicios = divisionPoliticaService.DepartamentosActivos();
							if(oLugInicio.getMunicipio()!=null){
								this.deptLugInicioSelected = oLugInicio.getMunicipio().getDepartamento();
								this.deptLugInicioSelectedId = oLugInicio.getMunicipio().getDepartamento().getDivisionPoliticaId();
								this.munisLugsInicios = divisionPoliticaService.MunicipiosActivos(this.deptLugInicioSelectedId);
								this.muniLugInicioSelected = oLugInicio.getMunicipio();
								this.muniLugInicioSelectedId = oLugInicio.getMunicipio().getDivisionPoliticaId();
								this.comuLugInicioSelected = oLugInicio.getComunidad();
							}
						}
					}
				}
			}
			
			this.viajeZonaRiesgo = this.investigacionMalariaSelected.getViajesZonaRiesgo();
			if(this.viajeZonaRiesgo.intValue()==1){
				this.usaMosquitero = this.investigacionMalariaSelected.getUsoMosquitero();
			}
			//se obtienen los valores asociados a antecedentes transfusionales
			this.antTransfusion = this.investigacionMalariaSelected.getTransfusion();
			if(this.antTransfusion.intValue()==1){
				 oInfoResultado = investigacionTransfusion.EncontrarPorInvestigacionMalaria(
						this.investigacionMalariaSelected.getInvestigacionMalariaId());
				if(oInfoResultado.isOk()){
					InvestigacionTransfusion oTransfusion = (InvestigacionTransfusion) oInfoResultado.getObjeto();
					if(oTransfusion.getPais()!=null){
						this.paisTransfusionSelected = oTransfusion.getPais();
						this.paisTransfusionSelectedId = oTransfusion.getPais().getPaisId();
					}else{
						InfoResultado oResultado = paisService.Encontrar(Utilidades.PAIS_CODIGO);
						Pais oPais = (Pais) oResultado.getObjeto();
						this.paisTransfusionSelected = oPais;
						this.paisTransfusionSelectedId = oPais.getPaisId();
						this.unidadesTransfusion = unidadService.UnidadesActivasPorPropiedad(Utilidades.ES_UNIDAD_TRANSFUSIONAL);
						if(oTransfusion.getUnidad()!=null){
							this.unidadTransfusionSelected = oTransfusion.getUnidad();
							this.unidadTransfusionSelectedId = oTransfusion.getUnidad().getUnidadId();
						}
					}
				}
			}
			
			this.manejoClinico=this.investigacionMalariaSelected.getManejoClinico();
			this.inicioTratamientoMClinico=this.investigacionMalariaSelected.getInicioTratamiento();
			this.finTratamientoMClinico= this.investigacionMalariaSelected.getFinTratamiento();
			
			// Valores asociados a investigación hospitalaria y ambulatoria
			if(this.manejoClinico.intValue()==1){
				 oInfoResultado = investigacionHospitalario.EncontrarPorInvestigacionMalaria(
						this.investigacionMalariaSelected.getInvestigacionMalariaId());
				if(oInfoResultado.isOk()){
					InvestigacionHospitalario oHospitalario = (InvestigacionHospitalario) oInfoResultado.getObjeto();
					this.numeroExpedienteMClinico = oHospitalario.getExpediente();
					this.fechaIngresoMClinico = oHospitalario.getFechaIngreso();
					this.diasEstanciaMClinico = oHospitalario.getDiasEstancia();
					this.unidadesMClinico = unidadService.UnidadesActivasPorEntidadYCategoria(this.entidadMClinicoSelectedId,
							Utilidades.ES_UNIDAD_HOSPITALIZACION);
					if(oHospitalario.getUnidad()!=null){
						this.unidadMClinicoSelected = oHospitalario.getUnidad();
						this.unidadMClinicoSelectedId = oHospitalario.getUnidad().getUnidadId();
					}
				}
			}
			
			this.convivientesTratadosMClinico = this.investigacionMalariaSelected.getConvivientesTratados();
			this.colateralesTratadosMClinico = this.investigacionMalariaSelected.getColateralesTratados();
			this.tratamientoSupervisadoMClinico = this.investigacionMalariaSelected.getTratamientoSupervisado();
			this.tratamientoCompletoMClinico = this.investigacionMalariaSelected.getTratamientoCompleto();
			this.controlParasitarioMClinico = this.investigacionMalariaSelected.getControlParasitario();
			if(this.controlParasitarioMClinico.intValue()==1){
				this.numDiasPosterioresMClinico=this.investigacionMalariaSelected.getDiasPosterioresControl();
				this.resultControlPositivoMClinico=this.investigacionMalariaSelected.getResultadoControlPositivo();
			}
			
			//Resultado de investigación
			this.condicionFinalMClinico=this.investigacionMalariaSelected.getCondicionFinalVivo();
			if(this.condicionFinalMClinico.intValue()==0){
				this.fechaDefuncionMClinico=this.investigacionMalariaSelected.getFechaDefuncion();
			}
			this.automedicacionMClinico=this.investigacionMalariaSelected.getAutomedicacion();
			if(this.automedicacionMClinico.intValue()==1){
				this.medicamentosAutomedicacionMClinico=this.investigacionMalariaSelected.getMedicamentosAutomedicacion();
			}
			this.fechaInfeccionRInv=this.investigacionMalariaSelected.getFechaInfeccion();
			this.infeccionEnResidenciaRInv=this.investigacionMalariaSelected.getInfeccionResidencia();
			if(this.infeccionEnResidenciaRInv.intValue()==0 && this.investigacionSintomaSelected!=null){
				 oInfoResultado= investigacionLugarService.EncontrarPorInvestigacionMalaria(
						this.investigacionSintomaSelected.getInvestigacionSintomaId());
				if(oInfoResultado.isOk()){
					InvestigacionLugar oLugar = (InvestigacionLugar) oInfoResultado.getObjeto();
					this.infeccionEnResidenciaRInv = oLugar.getInfeccionResidencia();
					if(oLugar.getPais()!=null){
						this.paisRInvSelected = oLugar.getPais();
						this.paisRInvSelectedId = oLugar.getPais().getPaisId();
					}else{
						InfoResultado oResultado = paisService.Encontrar(Utilidades.PAIS_CODIGO);
						Pais oPais = (Pais) oResultado.getObjeto();
						this.paisRInvSelected = oPais;
						this.paisRInvSelectedId = oPais.getPaisId();
						this.deptsSintomasRInv = divisionPoliticaService.DepartamentosActivos();
						if(oLugar.getMunicipio()!=null){
							this.deptRInvSelected = oLugar.getMunicipio().getDepartamento();
							this.deptRInvSelectedId = oLugar.getMunicipio().getDepartamento().getDivisionPoliticaId();
							this.munisSintomasRInv= divisionPoliticaService.MunicipiosActivos(this.deptLugInicioSelectedId);
							this.muniRInvSelected = oLugar.getMunicipio();
							this.muniRInvSelectedId = oLugar.getMunicipio().getDivisionPoliticaId();
							this.comuRInvSelected = oLugar.getComunidad();
						}
					}		
				}
			}
			if (this.investigacionMalariaSelected.getClasificacionClinica() != null) {
				this.clasificsClinicasRInv = clasificacionClinicaService.ListarActivos(this.investigacionMalariaSelected.getClasificacionClinica().getCodigo());
				this.clasifClinicaRInvSelected=this.investigacionMalariaSelected.getClasificacionClinica();
				this.clasifClinicaRInvSelectedId=this.investigacionMalariaSelected.getClasificacionClinica().getCatalogoId();
			}else{
				this.clasificsClinicasRInv = clasificacionClinicaService.ListarActivos();
			}
			if (this.investigacionMalariaSelected.getTipoRecurrencia() != null) {
				this.tiposInfeccionesRInv = tipoRecurrenciaService.ListarActivos(this.investigacionMalariaSelected.getTipoRecurrencia().getCodigo());
				this.tipoInfeccionRInvSelected=this.investigacionMalariaSelected.getTipoRecurrencia();
				this.tipoInfeccionRInvSelectedId=this.investigacionMalariaSelected.getTipoRecurrencia().getCatalogoId();
			}else{
				this.tiposInfeccionesRInv = tipoRecurrenciaService.ListarActivos();
			}
			if (this.investigacionMalariaSelected.getTipoComplicacion() != null) {
				this.tiposComplicacionesRInv = tipoComplicacionService.ListarActivos(this.investigacionMalariaSelected.getTipoComplicacion().getCodigo());
				this.tipoComplicacionRInvSelected=this.investigacionMalariaSelected.getTipoComplicacion();
				this.tipoComplicacionRInvSelectedId=this.investigacionMalariaSelected.getTipoComplicacion().getCatalogoId();
			}else{
				this.tiposComplicacionesRInv = tipoComplicacionService.ListarActivos();
			}
			if (this.investigacionMalariaSelected.getClasificacionCaso() != null) {
				this.clasificacionesCasosRInv = clasificacionCasoService.ListarActivos(this.investigacionMalariaSelected.getClasificacionCaso().getCodigo());
				this.clasificacionCasoRInvSelected =this.investigacionMalariaSelected.getClasificacionCaso();
				this.clasificacionCasoRInvSelectedId =this.investigacionMalariaSelected.getClasificacionCaso().getCatalogoId();
			}else{
				this.clasificacionesCasosRInv = clasificacionCasoService.ListarActivos();
			}
			this.observaciones=this.investigacionMalariaSelected.getObservaciones();
			this.nivelAutorizacion=this.investigacionMalariaSelected.getNivelAutorizacion();
			this.responsableInvest=this.investigacionMalariaSelected.getResponsable();
			this.epidemiologo=this.investigacionMalariaSelected.getEpidemiologo();
			
			this.casoCerrado=this.investigacionMalariaSelected.getCasoCerrado();
			this.fechaCierre=this.investigacionMalariaSelected.getFechaCierreCaso();
		}else{
			if(this.muestreoHematicoSelected.getVivienda()!=null){
				this.longitud = this.muestreoHematicoSelected.getVivienda().getLatitud();
				this.latitud = this.muestreoHematicoSelected.getVivienda().getLatitud();
				this.fechaInicioSintoma=this.muestreoHematicoSelected.getInicioSintomas();
			}
		}
	}
	
	public void onSintomaticoSelected(){
		if(this.esSintomatico.intValue()==0){
			this.sintomaInicio=BigDecimal.valueOf(1);
			this.fechaInicioSintoma=null;
			this.sintomasLugaresAntes=null;
			this.sintomasLugaresOtros=null;
			this.paisLugInicioSelected=null;
			this.paisLugInicioSelectedId=0;
			this.deptsLugsInicios=null;
			this.deptLugInicioSelected=null;
			this.deptLugInicioSelectedId=0;
			this.munisLugsInicios=null;
			this.muniLugInicioSelected=null;
			this.muniLugInicioSelectedId=0;
		}
	}
	
	public void onSintomaInicioSelected(){
		this.paisLugInicioSelected=null;
		this.paisLugInicioSelectedId=0;
		this.deptsLugsInicios=null;
		this.deptLugInicioSelected=null;
		this.deptLugInicioSelectedId=0;
		this.munisLugsInicios=null;
		this.muniLugInicioSelected=null;
		this.muniLugInicioSelectedId=0;
		
		if(this.sintomaInicio.intValue()==0){
			InfoResultado oResultado=paisService.Encontrar(Utilidades.PAIS_CODIGO);
			if(oResultado.isOk()){
				if (!oResultado.isOk()) {
					return;
				}
				Pais oPais=(Pais)oResultado.getObjeto();
				this.paisLugInicioSelected=oPais;
				this.paisLugInicioSelectedId=oPais.getPaisId();
				this.deptsLugsInicios = divisionPoliticaService.DepartamentosActivos();
			}
		}
	}
	
	public void onViajeZonaRiesgoSelected(){
		this.usaMosquitero=null;
	}
	
	public void onAntTransfusionSelected(){
		this.fechaTransfusion=null;
		this.paisTransfusionSelected=null;
		this.paisTransfusionSelectedId=0;
		this.unidadTransfusionSelected=null;
		this.unidadTransfusionSelectedId=0;
		if(this.antTransfusion.intValue()==1){
			InfoResultado oResultado=paisService.Encontrar(Utilidades.PAIS_CODIGO);
			if(oResultado.isOk()){
				if (!oResultado.isOk()) {
					return;
				}
				Pais oPais=(Pais)oResultado.getObjeto();
				this.paisTransfusionSelected=oPais;
				this.paisTransfusionSelectedId=oPais.getPaisId();
			}
		}
	}
	
	public void onManejoClinicoSelected(){
		if(this.manejoClinico.intValue()==0){
			this.entidadMClinicoSelected=null;
			this.entidadMClinicoSelectedId=0;
			this.unidadesMClinico=null;
			this.unidadMClinicoSelected=null;
			this.unidadMClinicoSelectedId=0;
			this.numeroExpedienteMClinico="";
			this.fechaIngresoMClinico=null;
			this.diasEstanciaMClinico=null;
		}
	}
	
	public void onResultControlPositivoMClinicoSelected(){
		if(this.controlParasitarioMClinico.intValue()==0){
			this.resultControlPositivoMClinico=null;
			this.numDiasPosterioresMClinico=null;
		}
	}
	public void onCondicionFinalMClinico(){
		if(this.condicionFinalMClinico.intValue()==1){
			this.fechaDefuncionMClinico=null;
		}
	}
	
	public void onAutomedicacionMClinico(){
		if(this.automedicacionMClinico.intValue()==0){
			this.medicamentosAutomedicacionMClinico=null;
		}
	}
	
	public void onCasoCerradoSelected(){
		if(this.casoCerrado.intValue()==0){
			this.fechaCierre=null;
		}
	}
	
	public void onInfeccionRInvSelected(){
		this.paisRInvSelected=null;
		this.paisRInvSelectedId=0;
		this.deptsSintomasRInv=null;
		this.deptRInvSelected=null;
		this.deptRInvSelectedId=0;
		this.munisSintomasRInv=null;
		this.muniRInvSelected=null;
		this.muniRInvSelectedId=0;
		this.comuRInvSelected=null;
		
		if(this.infeccionEnResidenciaRInv.intValue()==0){
			InfoResultado oResultado=paisService.Encontrar(Utilidades.PAIS_CODIGO);
			if(oResultado.isOk()){
				if (!oResultado.isOk()) {
					return;
				}
				Pais oPais=(Pais)oResultado.getObjeto();
				this.paisRInvSelected=oPais;
				this.paisRInvSelectedId=oPais.getPaisId();
				this.deptsSintomasRInv= divisionPoliticaService.DepartamentosActivos();
			}
		}
	}
	
	public List<Comunidad> completarComuLugInicio(String query) {
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades=comunidadService.ComunidadesPorMunicipioYNombre(this.muniLugInicioSelectedId,query);
		return oComunidades;
	}
	
	public List<Comunidad> completarComuLugAnte(String query){
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades=comunidadService.ComunidadesPorMunicipioYNombre(this.muniLugAnteSelectedId,query);
		return oComunidades;
	}
	
	public List<Comunidad> completarComuLugOtro(String query){
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades=comunidadService.ComunidadesPorMunicipioYNombre(this.muniLugOtroSelectedId,query);
		return oComunidades;
	}
	
	public List<Comunidad> completarComuRInv(String query){
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades=comunidadService.ComunidadesPorMunicipioYNombre(this.muniRInvSelectedId,query);
		return oComunidades;
	}
	
	/**
	 * Obtiene las unidades de salud con autorización explícita
	 * asociadas a una entidad administrativa (ímplicita) 
	 */
	public void obtenerUnidades() {

		this.unidadSelected=null;
		this.unidadSelectedId=0;
		
		// se filtran únicamente las unidades de salud a las cuales tiene autorización el
		// usuario y que hayan sido declaradas como puestos de notificación activas y aquellas
		// unidades, que no siendo puesto de notificación, tienen declarada la característica
		// funcional respectiva

		this.unidades=Operacion.unidadesAutorizadasPorEntidad(this.infoSesion.getUsuarioId(),
				this.entidadSelectedId, 0,true,Utilidades.ES_PUESTO_NOTIFICACION +", " + Utilidades.DECLARA_MUESTREO_HEMATICO);

		if ((this.unidades!=null) && (this.unidades.size()>0)) {
			this.unidadSelectedId=this.unidades.get(0).getUnidadId();
			this.unidadSelected=this.unidades.get(0);	
		}
		onModoSelected();
	}
	
	public void obtenerUnidadesHospitalizacion(){
		this.unidadMClinicoSelected=null;
		this.unidadMClinicoSelectedId=0;
		
		// se filtran únicamente las unidades de salud cuya categoría corresponda a hospitalización
		this.unidadesMClinico = unidadService.UnidadesActivasPorEntidadYCategoria(this.entidadMClinicoSelectedId,
				Utilidades.ES_UNIDAD_HOSPITALIZACION);
	}
	
	
	public void cambiarUnidad() {
		
		if (this.unidadSelectedId==0) {
			return;
		}
		
		InfoResultado oResultado=unidadService.Encontrar(this.unidadSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}

		Unidad oUnidad=(Unidad)oResultado.getObjeto();
		this.unidadSelected=oUnidad;
		this.unidadSelectedId=oUnidad.getUnidadId();
		onModoSelected();
	}
	
	public void onAceptarUnidadHosp(){
		if(this.unidadMClinicoSelectedId==0){
			return;
		}
		InfoResultado oResultado=unidadService.Encontrar(this.unidadMClinicoSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}

		Unidad oUnidad=(Unidad)oResultado.getObjeto();
		this.unidadMClinicoSelected=oUnidad;
		this.unidadMClinicoSelectedId=oUnidad.getUnidadId();
	}
	
	public void cambiarUnidadTransfusion(){
		if(this.unidadTransfusionSelectedId==0){
			return;
		}
		
		InfoResultado oResultado=unidadService.Encontrar(this.unidadTransfusionSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}

		Unidad oUnidad=(Unidad)oResultado.getObjeto();
		this.unidadTransfusionSelected=oUnidad;
		this.unidadTransfusionSelectedId=oUnidad.getUnidadId();
	}
	
	public void cambiarPaisLugInicio(){
		this.deptsLugsInicios=null;
		this.deptLugInicioSelected=null;
		this.deptLugInicioSelectedId=0;
		this.munisLugsInicios=null;
		this.muniLugInicioSelected=null;
		this.muniLugInicioSelectedId=0;
		this.comuLugInicioSelected=null;
		
		InfoResultado oResultado=paisService.Encontrar(this.paisLugInicioSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		Pais oPais=(Pais)oResultado.getObjeto();
		this.paisLugInicioSelected=oPais;
		this.paisLugInicioSelectedId=oPais.getPaisId();
		
		if(paisLugInicioSelected.getCodigoAlfados().trim().equals("") || 
				paisLugInicioSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false){
			return;
		}
		this.deptsLugsInicios = divisionPoliticaService.DepartamentosActivos();
	}
	
	public void cambiarPaisLugAnte(){
		this.deptsSintomasLugsAntes=null;
		this.deptLugAnteSelected=null;
		this.deptLugAnteSelectedId=0;
		this.munisSintomasLugsAntes=null;
		this.muniLugAnteSelected=null;
		this.muniLugAnteSelectedId=0;
		this.comuLugAnteSelected=null;
		
		InfoResultado oResultado=paisService.Encontrar(this.paisLugAnteSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		Pais oPais=(Pais)oResultado.getObjeto();
		this.paisLugAnteSelected=oPais;
		this.paisLugAnteSelectedId=oPais.getPaisId();
		
		if(paisLugAnteSelected.getCodigoAlfados().trim().equals("") || 
				paisLugAnteSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false){
			return;
		}
		this.deptsSintomasLugsAntes = divisionPoliticaService.DepartamentosActivos();
	}
	
	public void cambiarPaisLugOtro(){
		this.deptsSintomasLugsOtros=null;
		this.deptLugOtroSelected=null;
		this.deptLugOtroSelectedId=0;
		this.munisSintomasLugsOtros=null;
		this.muniLugOtroSelected=null;
		this.muniLugOtroSelectedId=0;
		this.comuLugOtroSelected=null;
		
		InfoResultado oResultado=paisService.Encontrar(this.paisLugOtroSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		Pais oPais=(Pais)oResultado.getObjeto();
		this.paisLugOtroSelected=oPais;
		this.paisLugOtroSelectedId=oPais.getPaisId();
		
		if(paisLugOtroSelected.getCodigoAlfados().trim().equals("") || 
				paisLugOtroSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false){
			return;
		}
		this.deptsSintomasLugsOtros = divisionPoliticaService.DepartamentosActivos();
	}
	
	public void cambiarPaisTransfusion(){
		this.unidadTransfusionSelected=null;
		this.unidadTransfusionSelectedId=0;
		
		InfoResultado oResultado=paisService.Encontrar(this.paisTransfusionSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		Pais oPais=(Pais)oResultado.getObjeto();
		this.paisTransfusionSelected=oPais;
		this.paisTransfusionSelectedId=oPais.getPaisId();
		
		if(paisTransfusionSelected.getCodigoAlfados().trim().equals("") || 
				paisTransfusionSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false){
			return;
		}
		
	}
	
	public void cambiarPaisRInv(){
		this.deptsSintomasRInv=null;
		this.deptRInvSelected=null;
		this.deptRInvSelectedId=0;
		this.munisSintomasRInv=null;
		this.muniRInvSelected=null;
		this.muniRInvSelectedId=0;
		this.comuRInvSelected=null;
		
		InfoResultado oResultado=paisService.Encontrar(this.paisLugInicioSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		Pais oPais=(Pais)oResultado.getObjeto();
		this.paisRInvSelected=oPais;
		this.paisRInvSelectedId=oPais.getPaisId();
		
		if(this.paisRInvSelected==null ||( paisRInvSelected.getCodigoAlfados().trim().equals("") || 
				paisRInvSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false)){
			return;
		}
		this.deptsSintomasRInv = divisionPoliticaService.DepartamentosActivos();
	}
	
	public void cambiarDeptLugInicio(){
		this.munisLugsInicios=null;
		this.muniLugInicioSelected=null;
		this.muniLugInicioSelectedId=0;
		this.comuLugInicioSelected=null;
		
		if(this.paisLugInicioSelected==null ||( paisLugInicioSelected.getCodigoAlfados().trim().equals("") || 
				paisLugInicioSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false)
				|| this.deptLugInicioSelectedId ==0){
			return;
		}
		InfoResultado oResultado=divisionPoliticaService.Encontrar(this.deptLugInicioSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		DivisionPolitica oDept =(DivisionPolitica) oResultado.getObjeto();
		this.deptLugInicioSelected=oDept;
		this.deptLugInicioSelectedId=oDept.getDivisionPoliticaId();
		
		this.munisLugsInicios = divisionPoliticaService.MunicipiosActivos(this.deptLugInicioSelectedId);
	}
	
	public void cambiarDeptLugAnte(){
		this.munisSintomasLugsAntes=null;
		this.muniLugAnteSelected=null;
		this.muniLugAnteSelectedId=0;
		this.comuLugAnteSelected=null;
		
		if(this.paisLugAnteSelected==null ||( paisLugAnteSelected.getCodigoAlfados().trim().equals("") || 
				paisLugAnteSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false)
				|| this.deptLugAnteSelectedId ==0){
			return;
		}
		InfoResultado oResultado=divisionPoliticaService.Encontrar(this.deptLugAnteSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		DivisionPolitica oDept =(DivisionPolitica) oResultado.getObjeto();
		this.deptLugAnteSelected=oDept;
		this.deptLugAnteSelectedId=oDept.getDivisionPoliticaId();
		
		this.munisSintomasLugsAntes = divisionPoliticaService.MunicipiosActivos(this.deptLugAnteSelectedId);
	}
	
	public void cambiarDeptLugOtro(){
		this.munisSintomasLugsOtros=null;
		this.muniLugOtroSelected=null;
		this.muniLugOtroSelectedId=0;
		this.comuLugOtroSelected=null;
		
		if(this.paisLugOtroSelected==null ||( paisLugOtroSelected.getCodigoAlfados().trim().equals("") || 
				paisLugOtroSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false)
				|| this.deptLugOtroSelectedId ==0){
			return;
		}
		InfoResultado oResultado=divisionPoliticaService.Encontrar(this.deptLugOtroSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		DivisionPolitica oDept =(DivisionPolitica) oResultado.getObjeto();
		this.deptLugOtroSelected=oDept;
		this.deptLugOtroSelectedId=oDept.getDivisionPoliticaId();
		
		this.munisSintomasLugsOtros = divisionPoliticaService.MunicipiosActivos(this.deptLugOtroSelectedId);
	}

	public void cambiarDeptRInv(){
		this.munisSintomasRInv=null;
		this.muniRInvSelected=null;
		this.muniRInvSelectedId=0;
		this.comuRInvSelected=null;
		
		if(this.paisRInvSelected==null ||( paisRInvSelected.getCodigoAlfados().trim().equals("") || 
				paisRInvSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false)
				|| this.paisRInvSelectedId ==0){
			return;
		}
		InfoResultado oResultado=divisionPoliticaService.Encontrar(this.deptRInvSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		DivisionPolitica oDept =(DivisionPolitica) oResultado.getObjeto();
		this.deptRInvSelected=oDept;
		this.deptRInvSelectedId=oDept.getDivisionPoliticaId();
		
		this.munisSintomasRInv = divisionPoliticaService.MunicipiosActivos(this.deptRInvSelectedId);
	}
	
	public void cambiarMuniLugAnte(){
		this.comuLugAnteSelected=null;
		if(this.paisLugAnteSelected==null ||( paisLugAnteSelected.getCodigoAlfados().trim().equals("") || 
				paisLugAnteSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false)
				|| this.paisLugAnteSelectedId ==0){
			return;
		}
		InfoResultado oResultado=divisionPoliticaService.Encontrar(this.muniLugAnteSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		DivisionPolitica oMuni = (DivisionPolitica)oResultado.getObjeto();
		this.muniLugAnteSelected=oMuni;
		this.muniLugAnteSelectedId=oMuni.getDivisionPoliticaId();
	}
	
	public void cambiarMuniLugOtro(){
		this.comuLugOtroSelected=null;
		if(this.paisLugOtroSelected==null ||( paisLugOtroSelected.getCodigoAlfados().trim().equals("") || 
				paisLugOtroSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false)
				|| this.paisLugOtroSelectedId ==0){
			return;
		}
		InfoResultado oResultado=divisionPoliticaService.Encontrar(this.muniLugOtroSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		DivisionPolitica oMuni = (DivisionPolitica)oResultado.getObjeto();
		this.muniLugOtroSelected=oMuni;
		this.muniLugOtroSelectedId=oMuni.getDivisionPoliticaId();
	}
	
	public void cambiarMuniLugInicio(){
		this.comuLugAnteSelected=null;
		if(this.paisLugInicioSelected==null ||( paisLugInicioSelected.getCodigoAlfados().trim().equals("") || 
				paisLugInicioSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false)
				|| this.muniLugInicioSelectedId ==0){
			return;
		}
		InfoResultado oResultado=divisionPoliticaService.Encontrar(this.muniLugInicioSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		DivisionPolitica oMuni = (DivisionPolitica)oResultado.getObjeto();
		this.muniLugInicioSelected=oMuni;
		this.muniLugInicioSelectedId=oMuni.getDivisionPoliticaId();
	}
	
	public void cambiarMuniRInv(){
		this.comuLugAnteSelected=null;
		
	
		if(this.paisRInvSelected==null ||( paisRInvSelected.getCodigoAlfados().trim().equals("") || 
				paisRInvSelected.getCodigoAlfados().trim().equalsIgnoreCase(Utilidades.PAIS_CODIGO) == false)
				|| this.muniRInvSelectedId ==0){
			return;
		}
		InfoResultado oResultado=divisionPoliticaService.Encontrar(this.muniRInvSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}
		DivisionPolitica oMuni = (DivisionPolitica)oResultado.getObjeto();
		this.muniRInvSelected=oMuni;
		this.muniRInvSelectedId=oMuni.getDivisionPoliticaId();
	}
	
	public void agregarMedicamentos(){
		this.medicamentoSelected=null;
		this.medicamentoSelectedId=0;
	}
	
	public void agregarUnidadHosp(){
		this.entidadMClinicoSelected=null;
		this.entidadMClinicoSelectedId=0;
		this.unidadesMClinico=null;
	}
	
	public void agregarSintomaLugarAnte(){
		this.fechaUltimaLugAnte=null;
		this.estadiaLugAnte=null;
		this.personaSintomaLugAnte=BigDecimal.valueOf(0);
		this.paisLugAnteSelected=null;
		this.paisLugAnteSelectedId=0;
		this.deptsSintomasLugsAntes=null;
		this.deptLugAnteSelected=null;
		this.deptLugAnteSelectedId=0;
		this.munisSintomasLugsAntes=null;
		this.muniLugAnteSelected=null;
		this.muniLugAnteSelectedId=0;
		this.comuLugAnteSelected=null;
		
		InfoResultado oResultado=paisService.Encontrar(Utilidades.PAIS_CODIGO);
		if (!oResultado.isOk()) {
			return;
		}
		
		Pais oPais = (Pais) oResultado.getObjeto();
		this.paisLugAnteSelected = oPais;
		this.paisLugAnteSelectedId = oPais.getPaisId();
		this.deptsSintomasLugsAntes = divisionPoliticaService.DepartamentosActivos();
	}
	
	public void agregarSintomaLugarOtro(){
		this.fechaUltimaLugOtro=null;
		this.estadiaLugOtro=null;
		this.autoMedicacionLugOtro=BigDecimal.valueOf(0);
		this.diagPositivoLugOtro=BigDecimal.valueOf(0);
		this.tratamientoCompletoLugOtro=BigDecimal.valueOf(0);
		this.paisLugOtroSelected=null;
		this.paisLugOtroSelectedId=0;
		this.deptsSintomasLugsOtros=null;
		this.deptLugOtroSelected=null;
		this.deptLugOtroSelectedId=0;
		this.munisSintomasLugsOtros=null;
		this.muniLugOtroSelected=null;
		this.muniLugOtroSelectedId=0;
		this.comuLugOtroSelected=null;
		
		InfoResultado oResultado=paisService.Encontrar(Utilidades.PAIS_CODIGO);
		if (!oResultado.isOk()) {
			return;
		}
		
		Pais oPais = (Pais) oResultado.getObjeto();
		this.paisLugOtroSelected = oPais;
		this.paisLugOtroSelectedId = oPais.getPaisId();
		this.deptsSintomasLugsOtros = divisionPoliticaService.DepartamentosActivos();
	}
	
	public void onAceptarMedicamentoAntiMalarico(){
		InfoResultado oResultado = new InfoResultado();
		
		if(this.investigacionesMedicamentos==null){
			this.investigacionesMedicamentos = new ArrayList<InvestigacionMedicamento>();
		}
		
		for(InvestigacionMedicamento oInvestigacion : this.investigacionesMedicamentos){
			if(oInvestigacion.getMedicamento().getCatalogoId()==this.medicamentoSelected.getCatalogoId()){
				oResultado.setMensaje(Mensajes.REGISTRO_DUPLICADO);
				oResultado.setOk(false);
			}
		}
		if (!oResultado.isOk()) {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null){
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}	
		}else{
			InvestigacionMedicamento oInvestigacion = new InvestigacionMedicamento();
			oInvestigacion.setMedicamento(this.medicamentoSelected);
			oInvestigacion.setUsuarioRegistro(this.infoSesion.getUsername());
			oInvestigacion.setFechaRegistro(Calendar.getInstance().getTime());
			this.investigacionesMedicamentos.add(oInvestigacion);
			this.medicamentosEnLista="";
			for(int i=0;i <= this.investigacionesMedicamentos.size();i++){
				if(i <= this.investigacionesMedicamentos.size()){
					if(i==0){
						this.medicamentosEnLista+=this.investigacionesMedicamentos.get(i).getMedicamento().getValor();
					}else{
						this.medicamentosEnLista+=  ", " + this.investigacionesMedicamentos.get(i).getMedicamento().getValor();
					}
				}else{
					this.medicamentosEnLista+=this.investigacionesMedicamentos.get(i).getMedicamento().getValor();
				}
			}
		}	
		this.medicamentoSelected=null;
		this.medicamentoSelectedId=0;
	}
	
	public void guardarSintomaLugarAnte(){
		RequestContext oContext = RequestContext.getCurrentInstance(); 
		InfoResultado oResultado = new InfoResultado();
		SintomaLugarAnte oSintomaLugar = new SintomaLugarAnte();
		if (this.paisLugAnteSelected != null) {
			if (!this.paisLugAnteSelected.getCodigoAlfados().equalsIgnoreCase(Utilidades.PAIS_CODIGO)) {
				oSintomaLugar.setPais(this.paisLugAnteSelected);
			}
		}
		oSintomaLugar.setMunicipio(this.muniLugAnteSelected);
		oSintomaLugar.setComunidad(this.comuLugAnteSelected);
		oSintomaLugar.setFechaUltima(this.fechaUltimaLugAnte);
		oSintomaLugar.setEstadia(this.estadiaLugAnte);
		oSintomaLugar.setPersonasSintomas(this.personaSintomaLugAnte);
		oSintomaLugar.setUsuarioRegistro(this.infoSesion.getUsername());
		oSintomaLugar.setFechaRegistro(Calendar.getInstance().getTime());
		//Si se está agregando una M1O a la E2, agregaremos el objeto a la lista de objetos de "Sintimas Lugares Antes", y se persistirá 
		//cada elemento en la lista hasta que se guarde la M10
		if (this.investigacionMalariaSelected == null|| this.investigacionMalariaSelected.getInvestigacionMalariaId() == 0) {
			oResultado = InvestigacionValidacion.validarSintomaLugarAnte(oSintomaLugar);
			if (oResultado.isOk()) {
				if (this.sintomasLugaresAntes == null) {
					this.sintomasLugaresAntes = new ArrayList<SintomaLugarAnte>();
				}
				// Identificador temporal, el cual será reemplazado al momento
				// de persistirlo
				oSintomaLugar.setSintomaLugarAntesId(this.sintomasLugaresAntes.size() + 1);
				this.sintomasLugaresAntes.add(oSintomaLugar);
			}
		} else {
			oResultado=sintomaLugarAnteService.Agregar(oSintomaLugar);
			obtenerSintomasLugaresAntes();
		}
		if (!oResultado.isOk()) {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			oContext.addCallbackParam("agregarLugAnteOK", false); 
			return;
		}else{
			oContext.addCallbackParam("agregarLugAnteOK", true); 
		}	
	}
	
	public void guardarSintomaLugarOtro(){
		RequestContext oContext = RequestContext.getCurrentInstance(); 
		InfoResultado oResultado = new InfoResultado();
		SintomaLugarOtro oSintomaLugar = new SintomaLugarOtro();
		if (this.paisLugOtroSelected != null) {
			if (!this.paisLugOtroSelected.getCodigoAlfados().equalsIgnoreCase(Utilidades.PAIS_CODIGO)) {
				oSintomaLugar.setPais(this.paisLugOtroSelected);
			}
		}
		oSintomaLugar.setMunicipio(this.muniLugOtroSelected);
		oSintomaLugar.setComunidad(this.comuLugOtroSelected);
		oSintomaLugar.setEstadia(estadiaLugOtro);
		oSintomaLugar.setAutomedicacion(this.autoMedicacionLugOtro);
		oSintomaLugar.setDiagnosticoPositivo(this.diagPositivoLugOtro);
		oSintomaLugar.setTratamientoCompleto(this.tratamientoCompletoLugOtro);
		if(this.fechaUltimaLugOtro!=null){
			oSintomaLugar.setAñoInicio(BigDecimal.valueOf(Integer.parseInt(new SimpleDateFormat("yyyy").format(this.fechaUltimaLugOtro))));
			oSintomaLugar.setMesInicio(BigDecimal.valueOf(Integer.parseInt(new SimpleDateFormat("MM").format(this.fechaUltimaLugOtro))));
		}
		oSintomaLugar.setUsuarioRegistro(this.infoSesion.getUsername());
		oSintomaLugar.setFechaRegistro(Calendar.getInstance().getTime());
		
		//Si se está agregando una M1O a la E2, agregaremos el objeto a la lista de objetos de "Sintimas Lugares Otros", y se persistirá 
		//cada elemento en la lista hasta que se guarde la M10
		if (this.investigacionMalariaSelected == null|| this.investigacionMalariaSelected.getInvestigacionMalariaId() == 0) {
			oResultado = InvestigacionValidacion.validarSintomaLugarOtro(oSintomaLugar);
			if (oResultado.isOk()) {
				if (this.sintomasLugaresOtros == null) {
					this.sintomasLugaresOtros = new ArrayList<SintomaLugarOtro>();
				}
				// Identificador temporal, el cual será reemplazado al momento
				// de persistirlo
				oSintomaLugar.setSintomaLugarOtroId(this.sintomasLugaresOtros.size() + 1);
				this.sintomasLugaresOtros.add(oSintomaLugar);
			}
		} else {
			oResultado=sintomaLugarOtroService.Agregar(oSintomaLugar);
			obtenerSintomasLugaresOtros();
		}
		if (!oResultado.isOk()) {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			oContext.addCallbackParam("agregarLugOtroOK", false); 
			return;
		}else{
			oContext.addCallbackParam("agregarLugOtroOK", true); 
		}	
	}
	
	/*
	 * Se ejecuta cuando el usuario pulsa en el botón guardar. Este proceso
	 * incluye guardar un nuevo registro o actualizar un registro existente.
	 * <br>
	 */
	public void guardar() {
		
		InfoResultado oResultado = new InfoResultado();
		InvestigacionMalaria oInvestigacion = new InvestigacionMalaria();
		InvestigacionSintoma oInvestigacionSintoma=null;
		SintomaLugarInicio oSintomaLugarInicio=null;
		InvestigacionLugar oInvestigacionLugar=null;
		InvestigacionTransfusion oInvestigacionTransfusion=null;
		InvestigacionHospitalario oInvestigacionHospitalario=null;
		
		oInvestigacion.setMuestreoHematico(this.muestreoHematicoSelected);
		oInvestigacion.setNumeroCaso(this.numeroCaso);
		oInvestigacion.setLatitudVivienda(this.latitud);
		oInvestigacion.setLongitudVivienda(this.longitud);
		oInvestigacion.setConfirmacionEntidad((ConfirmacionDiagnostico) confirmacionDiagnosticoService.Encontrar(this.diagnosticoEntidadSelectedId).getObjeto());
		oInvestigacion.setConfirmacionCndr((ConfirmacionDiagnostico) confirmacionDiagnosticoService.Encontrar(this.diagnosticoCndrSelectedId).getObjeto());
		oInvestigacion.setSintomatico(this.esSintomatico);
		
		//Si es sintomático, se guardan los valores asociados a investigación de sintomas
		if(this.esSintomatico.intValue()==1){
			oInvestigacionSintoma = new InvestigacionSintoma();
			oInvestigacionSintoma.setFechaInicioSintomas(this.fechaInicioSintoma);
			oInvestigacionSintoma.setSintomatico(this.esSintomatico);
			oInvestigacionSintoma.setEstadoFebril((EstadoFebril) estadoFebrilService.Encontrar(this.estadoFebrilSelectedId).getObjeto());
			oInvestigacionSintoma.setPersonasSintomas(this.personaConSintomaLugar);
			oInvestigacionSintoma.setInicioResidencia(this.sintomaInicio);
			
			//Si es sintomático y los sintomas inician en un lugar diferente al lugar de resisencia,
			//Se almacenan los valores asociados a Sintomas Lugares Inicio
			if(this.sintomaInicio.intValue()==0){
				oSintomaLugarInicio = new SintomaLugarInicio();
				oSintomaLugarInicio.setInicioResidencia(this.sintomaInicio);
				if (this.paisLugInicioSelected != null) {
					if (!this.paisLugInicioSelected.getCodigoAlfados().equalsIgnoreCase(Utilidades.PAIS_CODIGO)) {
						oSintomaLugarInicio.setPais(this.paisLugInicioSelected);
					}
				}
				oSintomaLugarInicio.setMunicipio(this.muniLugInicioSelected);
				oSintomaLugarInicio.setComunidad(this.comuLugInicioSelected);
				oSintomaLugarInicio.setEstadia(this.estadiaLugar);
			}
		}
		oInvestigacion.setViajesZonaRiesgo(this.viajeZonaRiesgo);
		if(this.viajeZonaRiesgo.intValue()==1){
			oInvestigacion.setUsoMosquitero(this.usaMosquitero);
		}
		
		oInvestigacion.setTransfusion(this.antTransfusion);
		//Si existen antecedentes transfusionales, se guardarán los valores asociados.
		if(this.antTransfusion.intValue()==1){
			oInvestigacionTransfusion = new InvestigacionTransfusion();
			oInvestigacionTransfusion.setTransfusion(this.antTransfusion);
			if (this.paisTransfusionSelected != null) {
				if (!this.paisTransfusionSelected.getCodigoAlfados().equalsIgnoreCase(Utilidades.PAIS_CODIGO)) {
					oInvestigacionTransfusion.setPais(this.paisTransfusionSelected);
				}
			}
			oInvestigacionTransfusion.setUnidad(this.unidadTransfusionSelected);
			if(this.unidadTransfusionSelected!=null){
				oInvestigacionTransfusion.setMunicipio(this.unidadTransfusionSelected.getMunicipio());
			}
		}
		
		oInvestigacion.setManejoClinico(this.manejoClinico);
		oInvestigacion.setInicioTratamiento(this.inicioTratamientoMClinico);
		oInvestigacion.setFinTratamiento(this.finTratamientoMClinico);
		//Si el manejo clínico es hospitalario, se guardarán los valores asociados a investigación hospitalaria
		if(this.manejoClinico.intValue()==1){
			oInvestigacionHospitalario = new InvestigacionHospitalario();
			oInvestigacionHospitalario.setManejoClinico(this.manejoClinico);
			oInvestigacionHospitalario.setUnidad(this.unidadMClinicoSelected);
			oInvestigacionHospitalario.setExpediente(this.numeroExpedienteMClinico);
			oInvestigacionHospitalario.setFechaIngreso(this.fechaIngresoMClinico);
			oInvestigacionHospitalario.setDiasEstancia(this.diasEstanciaMClinico);
			if(this.unidadMClinicoSelected!=null){
				oInvestigacionHospitalario.setMunicipio(this.unidadMClinicoSelected.getMunicipio());
			}
		}
		oInvestigacion.setConvivientesTratados(this.convivientesTratadosMClinico);
		oInvestigacion.setColateralesTratados(this.colateralesTratadosMClinico);
		oInvestigacion.setTratamientoSupervisado(this.tratamientoSupervisadoMClinico);
		oInvestigacion.setTratamientoCompleto(this.tratamientoCompletoMClinico);
		oInvestigacion.setControlParasitario(this.controlParasitarioMClinico);
		if(this.controlParasitarioMClinico.intValue()==1){
			oInvestigacion.setDiasPosterioresControl(this.numDiasPosterioresMClinico);
			oInvestigacion.setResultadoControlPositivo(this.resultControlPositivoMClinico);
		}
		oInvestigacion.setCondicionFinalVivo(this.condicionFinalMClinico);
		if(this.condicionFinalMClinico.intValue()==0){
			oInvestigacion.setFechaDefuncion(this.fechaDefuncionMClinico);
		}
		oInvestigacion.setAutomedicacion(this.automedicacionMClinico);
		if(this.automedicacionMClinico.intValue()==1){
			oInvestigacion.setMedicamentosAutomedicacion(this.medicamentosAutomedicacionMClinico);
		}
		oInvestigacion.setFechaInfeccion(this.fechaInfeccionRInv);
		oInvestigacion.setInfeccionResidencia(this.infeccionEnResidenciaRInv);
		if(this.infeccionEnResidenciaRInv.intValue()==0){
			oInvestigacionLugar = new InvestigacionLugar();
			oInvestigacionLugar.setInfeccionResidencia(this.infeccionEnResidenciaRInv);
			if (this.paisRInvSelected != null) {
				if (!this.paisRInvSelected.getCodigoAlfados().equalsIgnoreCase(Utilidades.PAIS_CODIGO)) {
					oInvestigacionLugar.setPais(this.paisRInvSelected);
				}
			}
			oInvestigacionLugar.setMunicipio(this.muniRInvSelected);
			oInvestigacionLugar.setComunidad(this.comuRInvSelected);
			
		}
		oInvestigacion.setClasificacionClinica((ClasificacionClinica) clasificacionClinicaService.Encontrar(this.clasifClinicaRInvSelectedId).getObjeto());
		oInvestigacion.setTipoRecurrencia((TipoRecurrencia) tipoRecurrenciaService.Encontrar(this.tipoInfeccionRInvSelectedId).getObjeto());
		oInvestigacion.setTipoComplicacion((TipoComplicacion) tipoComplicacionService.Encontrar(this.tipoComplicacionRInvSelectedId).getObjeto());
		oInvestigacion.setClasificacionCaso((ClasificacionCaso) clasificacionCasoService.Encontrar(this.clasificacionCasoRInvSelectedId).getObjeto());
		
		oInvestigacion.setObservaciones(this.observaciones);
		oInvestigacion.setNivelAutorizacion(this.nivelAutorizacion);
		oInvestigacion.setResponsable(this.responsableInvest);
		oInvestigacion.setEpidemiologo(this.epidemiologo);
		
		oInvestigacion.setCasoCerrado(this.casoCerrado);
		if(this.casoCerrado.intValue()==1){
			oInvestigacion.setFechaCierreCaso(this.fechaCierre);
		}
		
		if (this.investigacionMalariaSelected!=null) {
			oInvestigacion.setInvestigacionMalariaId(this.investigacionMalariaSelected.getInvestigacionMalariaId());
			oResultado=investigacionService.Guardar(
					oInvestigacion,
					oInvestigacionSintoma,
					oSintomaLugarInicio,
					this.sintomasLugaresAntes,
					this.sintomasLugaresOtros,
					this.investigacionesMedicamentos,
					oInvestigacionLugar,
					oInvestigacionTransfusion,
					oInvestigacionHospitalario
			);
		} else {
			if(oSintomaLugarInicio!=null){
				oSintomaLugarInicio.setUsuarioRegistro(this.infoSesion.getUsername());
				oSintomaLugarInicio.setFechaRegistro(Calendar.getInstance().getTime());
			}
			if(oInvestigacionLugar!=null){
				oInvestigacionLugar.setUsuarioRegistro(this.infoSesion.getUsername());
				oInvestigacionLugar.setFechaRegistro(Calendar.getInstance().getTime());
			}
			if(oInvestigacionHospitalario!=null){
				oInvestigacionHospitalario.setUsuarioRegistro(this.infoSesion.getUsername());
				oInvestigacionHospitalario.setFechaRegistro(Calendar.getInstance().getTime());
			}
			oResultado=investigacionService.Agregar(
					oInvestigacion,
					oInvestigacionSintoma,
					oSintomaLugarInicio,
					this.sintomasLugaresAntes,
					this.sintomasLugaresOtros,
					this.investigacionesMedicamentos,
					oInvestigacionLugar,
					oInvestigacionTransfusion,
					oInvestigacionHospitalario
			);
		}
		
		if (oResultado.isOk()){
			oResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
			onMuestreoHematicoSelected(null);
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null){
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
			
	}
	
	public void eliminar() {
		if (this.investigacionMalariaSelected == null || this.investigacionMalariaSelected.getInvestigacionMalariaId()==0) {
			return;
		}

		InfoResultado oResultado = new InfoResultado();
		oResultado = investigacionService.Eliminar( this.investigacionMalariaSelected.getInvestigacionMalariaId());

		if (oResultado.isOk()) {
			this.capaActiva=1;
			iniciarCapa1();
			iniciarCapa2();
			oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
		}

		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg != null) {
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	
	}
	
	public void eliminarSintomaLugarAnte(){
		if (this.sintomaLugarAnteSelectedId==0){
			return;
		}
		InfoResultado oResultado = new InfoResultado();
		//Si aún no se ha persistido la M10 quiere decir que los elementos en la lista tampoco, por lo que únicamente
		//se removera de la lista
		if(this.investigacionMalariaSelected==null || this.investigacionMalariaSelected.getInvestigacionMalariaId()==0){
			
			for(int i=0;i<=this.sintomasLugaresAntes.size();i++){
				if(this.sintomasLugaresAntes.get(i).getSintomaLugarAntesId() == this.sintomaLugarAnteSelectedId){
					this.sintomasLugaresAntes.remove(i);
				}
			}
			oResultado.setOk(true);
			oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
		}else{
			oResultado=sintomaLugarAnteService.Eliminar(this.sintomaLugarAnteSelectedId);
			if (oResultado.isOk()){
				oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
				obtenerSintomasLugaresAntes();
			}
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null){
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void eliminarSintomaLugarOtro(){
		if (this.sintomaLugarOtroSelectedId==0){
			return;
		}
		InfoResultado oResultado = new InfoResultado();
		//Si aún no se ha persistido la M10 quiere decir que los elementos en la lista tampoco, por lo que únicamente
		//se removera de la lista
		if(this.investigacionMalariaSelected==null || this.investigacionMalariaSelected.getInvestigacionMalariaId()==0){
			
			for(int i=0;i<=this.sintomasLugaresOtros.size();i++){
				if(this.sintomasLugaresOtros.get(i).getSintomaLugarOtroId() == this.sintomaLugarOtroSelectedId){
					this.sintomasLugaresOtros.remove(i);
				}
			}
			oResultado.setOk(true);
			oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
		}else{
			oResultado=sintomaLugarOtroService.Eliminar(this.sintomaLugarOtroSelectedId);
			if (oResultado.isOk()){
				oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
				obtenerSintomasLugaresOtros();
			}
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null){
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void onEliminarMedicamentoAntiMalarico(){
		this.medicamentosEnLista="";
		this.investigacionesMedicamentos=null;
	}
	
	public void onQuitarUnidadHosp(){
		this.unidadMClinicoSelected=null;
		this.unidadMClinicoSelectedId=0;
	}
	
	public void regresarCapa1() {
		iniciarCapa1();
		iniciarCapa2();
	}
		
	
	/**************************************************
	 * Métodos privados
	 **************************************************/
	
	private void init(){
		this.capaActiva=1;
		this.modo=0;
		this.infoSesion=Utilidades.obtenerInfoSesion();
		this.entidades= new ArrayList<EntidadAdtva>();
		this.unidades= new ArrayList<Unidad>();

		this.entidadSelectedId=0;
		this.unidadSelectedId=0;
		
		//Obtiene los tres últimos años a la fecha actual para llemar el filtro
		// de años epidemiológicos a usar en la grilla de muestreos hemáticos.
		this.aniosEpidemiologicos = new LinkedList<SelectItem>();
	    Integer anioActual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
	    this.anioEpiSelected=anioActual;
	    for(int i=0;i < 3;i++){
	    	this.aniosEpidemiologicos.add(new SelectItem(anioActual, anioActual.toString()));
	    	anioActual-=1;
	    }
	    
		// obtiene los datos para el combo de entidades autorizadas
		// únicamente se podrán seleccionar aquellas entidades administrativas
		// asociadas a las unidades de salud con autorización explícita
		this.entidades=Operacion.entidadesAutorizadas(this.infoSesion.getUsuarioId(),false);
		if ((this.entidades!=null) && (this.entidades.size()>0)) {
			this.entidadSelectedId=this.entidades.get(0).getEntidadAdtvaId();
			obtenerUnidades();
		}
		
		this.paises=paisService.listarPaises();
		iniDataModelMuestreoHematico();
	}
	
	private void iniciarCapa1(){
		this.capaActiva=1;
		this.muestreoHematicoSelected=null;
		this.muestreoHematicoSelectedId=0;
	}
	
	private void iniciarCapa2(){
		this.numeroCaso="";
		this.longitud=null;
		this.latitud=null;
		this.esSintomatico=BigDecimal.valueOf(Long.parseLong("0"));
		this.estadoFebrilSelectedId=0;
		this.viajeZonaRiesgo=BigDecimal.valueOf(Long.parseLong("0"));
		this.usaMosquitero=null;
		this.fechaInicioSintoma=null;
		this.sintomasLugaresAntes=null;
		this.sintomasLugaresOtros=null;
		this.paisLugInicioSelected=null;
		this.paisLugInicioSelectedId=0;
		this.deptsLugsInicios=null;
		this.deptLugInicioSelected=null;
		this.deptLugInicioSelectedId=0;
		this.munisLugsInicios=null;
		this.muniLugInicioSelected=null;
		this.muniLugInicioSelectedId=0;
		
		this.sintomasLugaresAntes=null;
		this.sintomasLugaresOtros=null;
		
		//Atributos vinculados a Investigación de transfuciones
		this.antTransfusion=BigDecimal.valueOf(Long.parseLong("0"));
		this.fechaTransfusion=null;
		this.paisTransfusionSelected=null;
		this.paisTransfusionSelectedId=0;
		this.unidadesTransfusion=null;
		this.unidadTransfusionSelected=null;
		this.unidadTransfusionSelectedId=0;
		
		
		//Atributos vinculados a manejo clínico - Investigación Hospitalaria-
		this.manejoClinico=BigDecimal.valueOf(Long.parseLong("0"));
		this.inicioTratamientoMClinico=null;
		this.finTratamientoMClinico=null;
		this.medicamentoSelected=null;
		this.medicamentoSelectedId=0;
		this.entidadMClinicoSelected=null;
		this.entidadMClinicoSelectedId=0;
		this.unidadesMClinico=null;
		this.unidadMClinicoSelected=null;
		this.unidadMClinicoSelectedId=0;
		this.numeroExpedienteMClinico="";
		this.fechaIngresoMClinico=null;
		this.diasEstanciaMClinico=null;
		this.convivientesTratadosMClinico=null;
		this.colateralesTratadosMClinico=null;
		this.tratamientoCompletoMClinico=BigDecimal.valueOf(Long.parseLong("0"));
		this.tratamientoSupervisadoMClinico=BigDecimal.valueOf(Long.parseLong("0"));
		this.controlParasitarioMClinico=BigDecimal.valueOf(Long.parseLong("0"));
		this.numDiasPosterioresMClinico=null;
		this.resultControlPositivoMClinico=null;
		this.condicionFinalMClinico=BigDecimal.valueOf(Long.parseLong("1"));
		this.fechaDefuncionMClinico=null;
		this.automedicacionMClinico=BigDecimal.valueOf(Long.parseLong("0"));
		this.medicamentosAutomedicacionMClinico=null;
		
		//Atributos vinculados al resultado de la investigacion
		this.infeccionEnResidenciaRInv=BigDecimal.valueOf(Long.parseLong("1"));
		this.paisRInvSelected=null;
		this.paisRInvSelectedId=0;
		this.deptsSintomasRInv=null;
		this.deptRInvSelected=null;
		this.deptRInvSelectedId=0;
		this.munisSintomasRInv=null;
		this.muniRInvSelected=null;
		this.muniRInvSelectedId=0;
		this.comuRInvSelected=null;
		this.fechaInfeccionRInv=null;
		
		this.clasifClinicaRInvSelected=null;
		this.clasifClinicaRInvSelectedId=0;
		this.tipoInfeccionRInvSelected=null;
		this.tipoInfeccionRInvSelectedId=0;
		
		this.tipoComplicacionRInvSelected=null;
		this.tipoComplicacionRInvSelectedId=0;
		this.clasificacionCasoRInvSelected=null;
		this.clasificacionCasoRInvSelectedId=0;

		this.observaciones="";
		this.responsableInvest="";
		this.epidemiologo="";
		this.nivelAutorizacion=BigDecimal.valueOf(Long.parseLong("0"));
		this.casoCerrado=BigDecimal.valueOf(Long.parseLong("0"));
		this.fechaCierre=null;
		
		this.entidadesMClinico = entidadService.EntidadesAdtvasActivas();
		this.unidadesTransfusion = unidadService.UnidadesActivasPorPropiedad(Utilidades.ES_UNIDAD_TRANSFUSIONAL);
		this.tiposInfeccionesRInv = tipoRecurrenciaService.ListarActivos();
		this.estadosFebriles = estadoFebrilService.ListarActivos();
		this.diagnosticosEntidad = confirmacionDiagnosticoService.ListarActivos();
		this.diagnosticosCndr = confirmacionDiagnosticoService.ListarActivos();
		this.medicamentosAntiMalaricos = medicamentoService.ListarActivos();
		this.clasificsClinicasRInv = clasificacionClinicaService.ListarActivos();
		this.tiposInfeccionesRInv = tipoRecurrenciaService.ListarActivos();
		this.tiposComplicacionesRInv = tipoComplicacionService.ListarActivos();
		this.clasificacionesCasosRInv=clasificacionCasoService.ListarActivos();
	}
	
	// inicia las variables primarias de modo tal que
	// ningun muestreo hemático e Investigación de malaria haya sido seleccionada
	public void iniciarCapas() {
		this.capaActiva=1;
	}
	
	private void iniDataModelMuestreoHematico() {
		
		this.muestreosHematicos = new LazyDataModel<MuestreoHematico>(){
			private static final long serialVersionUID = 1L;
			@Override
			public List<MuestreoHematico> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
				if(entidadSelectedId < 1){
					numMuestreos=0;
					return null;
				}
				
				List<MuestreoHematico> oMuestreoHematicoList=null;
				numMuestreos=0;
				
				numMuestreos = muestreoHematicoService.ContarPositivosPorUnidad(entidadSelectedId,unidadSelectedId,
						anioEpiSelected , (modo==0 ? true : false));
				oMuestreoHematicoList = muestreoHematicoService.ListarPositivosPorUnidad(entidadSelectedId,unidadSelectedId,  
						anioEpiSelected, (modo==0 ? true : false),  first, pageSize, numMuestreos);
				this.setRowCount(numMuestreos);
				return oMuestreoHematicoList;
			}
		};		
	}
	
	public void obtenerInvestigacionMedicamentos(){
		this.investigacionesMedicamentos = null;
		if(this.investigacionMalariaSelected==null || this.investigacionMalariaSelected.getInvestigacionMalariaId() < 1){
			return;
		}
		
		this.investigacionesMedicamentos = investigacionMedicamentoService.
			MedicamentosPorInvestigacion(this.investigacionMalariaSelected.getInvestigacionMalariaId());
		if(this.investigacionesMedicamentos!=null){
			for(int i=0;i <= this.investigacionesMedicamentos.size();i++){
				if(i <= this.investigacionesMedicamentos.size()){
					if(i==0){
						this.medicamentosEnLista+=this.investigacionesMedicamentos.get(i).getMedicamento().getValor();
					}else{
						this.medicamentosEnLista+=  ", " + this.investigacionesMedicamentos.get(i).getMedicamento().getValor();
					}
				}else{
					this.medicamentosEnLista+=this.investigacionesMedicamentos.get(i).getMedicamento().getValor();
				}
			}
		}
	}
	
	private void obtenerSintomasLugaresAntes(){
		this.sintomasLugaresAntes=null;
		if(this.investigacionSintomaSelected==null||this.investigacionSintomaSelectedId < 1){
			return;
		}
		this.sintomasLugaresAntes = sintomaLugarAnteService.SintomasLugarAntePorInvestigacionSintomas(this.investigacionSintomaSelectedId);
	}
	

	private void obtenerSintomasLugaresOtros(){
		this.sintomasLugaresOtros=null;
		if(this.investigacionSintomaSelected==null||this.investigacionSintomaSelectedId < 1){
			return;
		}
		this.sintomasLugaresOtros = sintomaLugarOtroService.SintomasLugarOtroPorInvestigacionSintomas(this.investigacionSintomaSelectedId);
	}

	/**************************************************
	 * Métodos de acceso a propiedades
	 **************************************************/
	
	
	public long getEntidadSelectedId() {
		return entidadSelectedId;
	}

	public int getModo() {
		return modo;
	}

	public void setModo(int modo) {
		this.modo = modo;
	}

	public int getCapaActiva() {
		return capaActiva;
	}

	public void setCapaActiva(int capaActiva) {
		this.capaActiva = capaActiva;
	}

	public MuestreoHematico getMuestreoHematicoSelected() {
		return muestreoHematicoSelected;
	}

	public void setMuestreoHematicoSelected(
			MuestreoHematico muestreoHematicoSelected) {
		this.muestreoHematicoSelected = muestreoHematicoSelected;
	}

	public List<SelectItem> getAniosEpidemiologicos() {
		return aniosEpidemiologicos;
	}

	public LazyDataModel<MuestreoHematico> getMuestreosHematicos() {
		return muestreosHematicos;
	}

	public List<InvestigacionMedicamento> getInvestigacionesMedicamentos() {
		return investigacionesMedicamentos;
	}

	public void setEntidadSelectedId(long entidadSelectedId) {
		this.entidadSelectedId = entidadSelectedId;
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

	public int getAnioEpiSelected() {
		return anioEpiSelected;
	}

	public void setAnioEpiSelected(int anioEpiSelected) {
		this.anioEpiSelected = anioEpiSelected;
	}

	public List<EntidadAdtva> getEntidades() {
		return entidades;
	}

	public List<Unidad> getUnidades() {
		return unidades;
	}

	public long getMuestreoHematicoSelectedId() {
		return muestreoHematicoSelectedId;
	}

	public void setMuestreoHematicoSelectedId(long muestreoHematicoSelectedId) {
		this.muestreoHematicoSelectedId = muestreoHematicoSelectedId;
	}

	public int getNumMuestreos() {
		return numMuestreos;
	}

	public void setNumMuestreos(int numMuestreos) {
		this.numMuestreos = numMuestreos;
	}

	public InvestigacionMalaria getInvestigacionMalariaSelected() {
		return investigacionMalariaSelected;
	}

	public void setInvestigacionMalariaSelected(
			InvestigacionMalaria investigacionMalariaSelected) {
		this.investigacionMalariaSelected = investigacionMalariaSelected;
	}
	
	public long getInvestigacionMalariaSelectedId() {
		return investigacionMalariaSelectedId;
	}

	public void setInvestigacionMalariaSelectedId(
			long investigacionMalariaSelectedId) {
		this.investigacionMalariaSelectedId = investigacionMalariaSelectedId;
	}

	public String getNumeroCaso() {
		return numeroCaso;
	}

	public void setNumeroCaso(String numeroCaso) {
		this.numeroCaso = numeroCaso;
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

	public String getPacienteEmbarazada() {
		return pacienteEmbarazada;
	}

	public void setPacienteEmbarazada(String pacienteEmbarazada) {
		this.pacienteEmbarazada = pacienteEmbarazada;
	}

	public long getDiagnosticoEntidadSelectedId() {
		return diagnosticoEntidadSelectedId;
	}

	public void setDiagnosticoEntidadSelectedId(long diagnosticoEntidadSelectedId) {
		this.diagnosticoEntidadSelectedId = diagnosticoEntidadSelectedId;
	}

	public long getDiagnosticoCndrSelectedId() {
		return diagnosticoCndrSelectedId;
	}

	public void setDiagnosticoCndrSelectedId(long diagnosticoCndrSelectedId) {
		this.diagnosticoCndrSelectedId = diagnosticoCndrSelectedId;
	}

	public InvestigacionSintoma getInvestigacionSintomaSelected() {
		return investigacionSintomaSelected;
	}

	public void setInvestigacionSintomaSelected(
			InvestigacionSintoma investigacionSintomaSelected) {
		this.investigacionSintomaSelected = investigacionSintomaSelected;
	}

	public long getInvestigacionSintomaSelectedId() {
		return investigacionSintomaSelectedId;
	}

	public void setInvestigacionSintomaSelectedId(
			long investigacionSintomaSelectedId) {
		this.investigacionSintomaSelectedId = investigacionSintomaSelectedId;
	}

	public List<SintomaLugarAnte> getSintomasLugaresAntes() {
		return sintomasLugaresAntes;
	}

	public void setSintomasLugaresAntes(List<SintomaLugarAnte> sintomasLugaresAntes) {
		this.sintomasLugaresAntes = sintomasLugaresAntes;
	}

	public SintomaLugarAnte getSintomaLugarAnteSelected() {
		return sintomaLugarAnteSelected;
	}

	public void setSintomaLugarAnteSelected(
			SintomaLugarAnte sintomaLugarAnteSelected) {
		this.sintomaLugarAnteSelected = sintomaLugarAnteSelected;
	}

	public long getSintomaLugarAnteSelectedId() {
		return sintomaLugarAnteSelectedId;
	}

	public void setSintomaLugarAnteSelectedId(long sintomaLugarAnteSelectedId) {
		this.sintomaLugarAnteSelectedId = sintomaLugarAnteSelectedId;
	}

	public List<SintomaLugarOtro> getSintomasLugaresOtros() {
		return sintomasLugaresOtros;
	}

	public void setSintomasLugaresOtros(List<SintomaLugarOtro> sintomasLugaresOtros) {
		this.sintomasLugaresOtros = sintomasLugaresOtros;
	}

	public SintomaLugarOtro getSintomaLugarOtroSelected() {
		return sintomaLugarOtroSelected;
	}

	public void setSintomaLugarOtroSelected(
			SintomaLugarOtro sintomaLugarOtroSelected) {
		this.sintomaLugarOtroSelected = sintomaLugarOtroSelected;
	}

	public long getSintomaLugarOtroSelectedId() {
		return sintomaLugarOtroSelectedId;
	}

	public void setSintomaLugarOtroSelectedId(long sintomaLugarOtroSelectedId) {
		this.sintomaLugarOtroSelectedId = sintomaLugarOtroSelectedId;
	}

	public List<ConfirmacionDiagnostico> getDiagnosticosEntidad() {
		return diagnosticosEntidad;
	}

	public List<ConfirmacionDiagnostico> getDiagnosticosCndr() {
		return diagnosticosCndr;
	}

	public void setMuestreosHematicos(
			LazyDataModel<MuestreoHematico> muestreosHematicos) {
		this.muestreosHematicos = muestreosHematicos;
	}
	
	public BigDecimal getEsSintomatico() {
		return esSintomatico;
	}

	public void setEsSintomatico(BigDecimal esSintomatico) {
		this.esSintomatico = esSintomatico;
	}

	public Date getFechaInicioSintoma() {
		return fechaInicioSintoma;
	}

	public void setFechaInicioSintoma(Date fechaInicioSintoma) {
		this.fechaInicioSintoma = fechaInicioSintoma;
	}

	public long getEstadoFebrilSelectedId() {
		return estadoFebrilSelectedId;
	}

	public void setEstadoFebrilSelectedId(long estadoFebrilSelectedId) {
		this.estadoFebrilSelectedId = estadoFebrilSelectedId;
	}

	public List<EstadoFebril> getEstadosFebriles() {
		return estadosFebriles;
	}

	public BigDecimal getSintomaInicio() {
		return sintomaInicio;
	}

	public void setSintomaInicio(BigDecimal sintomaInicio) {
		this.sintomaInicio = sintomaInicio;
	}

	public Pais getPaisLugInicioSelected() {
		return paisLugInicioSelected;
	}

	public void setPaisLugInicioSelected(Pais paisLugInicioSelected) {
		this.paisLugInicioSelected = paisLugInicioSelected;
	}

	public long getPaisLugInicioSelectedId() {
		return paisLugInicioSelectedId;
	}

	public void setPaisLugInicioSelectedId(long paisLugInicioSelectedId) {
		this.paisLugInicioSelectedId = paisLugInicioSelectedId;
	}

	public DivisionPolitica getDeptLugInicioSelected() {
		return deptLugInicioSelected;
	}

	public void setDeptLugInicioSelected(DivisionPolitica deptLugInicioSelected) {
		this.deptLugInicioSelected = deptLugInicioSelected;
	}

	public long getDeptLugInicioSelectedId() {
		return deptLugInicioSelectedId;
	}

	public void setDeptLugInicioSelectedId(long deptLugInicioSelectedId) {
		this.deptLugInicioSelectedId = deptLugInicioSelectedId;
	}

	public DivisionPolitica getMuniLugInicioSelected() {
		return muniLugInicioSelected;
	}

	public void setMuniLugInicioSelected(DivisionPolitica muniLugInicioSelected) {
		this.muniLugInicioSelected = muniLugInicioSelected;
	}

	public long getMuniLugInicioSelectedId() {
		return muniLugInicioSelectedId;
	}

	public void setMuniLugInicioSelectedId(long muniLugInicioSelectedId) {
		this.muniLugInicioSelectedId = muniLugInicioSelectedId;
	}

	public Comunidad getComuLugInicioSelected() {
		return comuLugInicioSelected;
	}

	public void setComuLugInicioSelected(Comunidad comuLugInicioSelected) {
		this.comuLugInicioSelected = comuLugInicioSelected;
	}

	public List<Pais> getPaises() {
		return paises;
	}

	public List<DivisionPolitica> getDeptsLugsInicios() {
		return deptsLugsInicios;
	}

	public List<DivisionPolitica> getMunisLugsInicios() {
		return munisLugsInicios;
	}

	public BigDecimal getEstadiaLugar() {
		return estadiaLugar;
	}

	public void setEstadiaLugar(BigDecimal estadiaLugar) {
		this.estadiaLugar = estadiaLugar;
	}

	public BigDecimal getUsaMosquitero() {
		return usaMosquitero;
	}

	public void setUsaMosquitero(BigDecimal usaMosquitero) {
		this.usaMosquitero = usaMosquitero;
	}

	public BigDecimal getViajeZonaRiesgo() {
		return viajeZonaRiesgo;
	}

	public void setViajeZonaRiesgo(BigDecimal viajeZonaRiesgo) {
		this.viajeZonaRiesgo = viajeZonaRiesgo;
	}

	public Date getFechaUltimaLugAnte() {
		return fechaUltimaLugAnte;
	}

	public void setFechaUltimaLugAnte(Date fechaUltimaLugAnte) {
		this.fechaUltimaLugAnte = fechaUltimaLugAnte;
	}

	public BigDecimal getEstadiaLugAnte() {
		return estadiaLugAnte;
	}

	public void setEstadiaLugAnte(BigDecimal estadiaLugAnte) {
		this.estadiaLugAnte = estadiaLugAnte;
	}

	public Pais getPaisLugAnteSelected() {
		return paisLugAnteSelected;
	}

	public void setPaisLugAnteSelected(Pais paisLugAnteSelected) {
		this.paisLugAnteSelected = paisLugAnteSelected;
	}

	public long getPaisLugAnteSelectedId() {
		return paisLugAnteSelectedId;
	}

	public void setPaisLugAnteSelectedId(long paisLugAnteSelectedId) {
		this.paisLugAnteSelectedId = paisLugAnteSelectedId;
	}

	public DivisionPolitica getDeptLugAnteSelected() {
		return deptLugAnteSelected;
	}

	public void setDeptLugAnteSelected(DivisionPolitica deptLugAnteSelected) {
		this.deptLugAnteSelected = deptLugAnteSelected;
	}

	public long getDeptLugAnteSelectedId() {
		return deptLugAnteSelectedId;
	}

	public void setDeptLugAnteSelectedId(long deptLugAnteSelectedId) {
		this.deptLugAnteSelectedId = deptLugAnteSelectedId;
	}

	public DivisionPolitica getMuniLugAnteSelected() {
		return muniLugAnteSelected;
	}

	public void setMuniLugAnteSelected(DivisionPolitica muniLugAnteSelected) {
		this.muniLugAnteSelected = muniLugAnteSelected;
	}

	public long getMuniLugAnteSelectedId() {
		return muniLugAnteSelectedId;
	}

	public void setMuniLugAnteSelectedId(long muniLugAnteSelectedId) {
		this.muniLugAnteSelectedId = muniLugAnteSelectedId;
	}

	public Comunidad getComuLugAnteSelected() {
		return comuLugAnteSelected;
	}

	public void setComuLugAnteSelected(Comunidad comuLugAnteSelected) {
		this.comuLugAnteSelected = comuLugAnteSelected;
	}

	public BigDecimal getPersonaSintomaLugAnte() {
		return personaSintomaLugAnte;
	}

	public void setPersonaSintomaLugAnte(BigDecimal personaSintomaLugAnte) {
		this.personaSintomaLugAnte = personaSintomaLugAnte;
	}

	public List<DivisionPolitica> getDeptsSintomasLugsAntes() {
		return deptsSintomasLugsAntes;
	}

	public List<DivisionPolitica> getMunisSintomasLugsAntes() {
		return munisSintomasLugsAntes;
	}

	public Date getFechaUltimaLugOtro() {
		return fechaUltimaLugOtro;
	}

	public void setFechaUltimaLugOtro(Date fechaUltimaLugOtro) {
		this.fechaUltimaLugOtro = fechaUltimaLugOtro;
	}
	
	public BigDecimal getEstadiaLugOtro() {
		return estadiaLugOtro;
	}

	public void setEstadiaLugOtro(BigDecimal estadiaLugOtro) {
		this.estadiaLugOtro = estadiaLugOtro;
	}

	public Pais getPaisLugOtroSelected() {
		return paisLugOtroSelected;
	}

	public void setPaisLugOtroSelected(Pais paisLugOtroSelected) {
		this.paisLugOtroSelected = paisLugOtroSelected;
	}

	public long getPaisLugOtroSelectedId() {
		return paisLugOtroSelectedId;
	}

	public void setPaisLugOtroSelectedId(long paisLugOtroSelectedId) {
		this.paisLugOtroSelectedId = paisLugOtroSelectedId;
	}

	public DivisionPolitica getDeptLugOtroSelected() {
		return deptLugOtroSelected;
	}

	public void setDeptLugOtroSelected(DivisionPolitica deptLugOtroSelected) {
		this.deptLugOtroSelected = deptLugOtroSelected;
	}

	public long getDeptLugOtroSelectedId() {
		return deptLugOtroSelectedId;
	}

	public void setDeptLugOtroSelectedId(long deptLugOtroSelectedId) {
		this.deptLugOtroSelectedId = deptLugOtroSelectedId;
	}

	public DivisionPolitica getMuniLugOtroSelected() {
		return muniLugOtroSelected;
	}

	public void setMuniLugOtroSelected(DivisionPolitica muniLugOtroSelected) {
		this.muniLugOtroSelected = muniLugOtroSelected;
	}

	public long getMuniLugOtroSelectedId() {
		return muniLugOtroSelectedId;
	}

	public void setMuniLugOtroSelectedId(long muniLugOtroSelectedId) {
		this.muniLugOtroSelectedId = muniLugOtroSelectedId;
	}

	public Comunidad getComuLugOtroSelected() {
		return comuLugOtroSelected;
	}

	public void setComuLugOtroSelected(Comunidad comuLugOtroSelected) {
		this.comuLugOtroSelected = comuLugOtroSelected;
	}

	public BigDecimal getAutoMedicacionLugOtro() {
		return autoMedicacionLugOtro;
	}

	public void setAutoMedicacionLugOtro(BigDecimal autoMedicacionLugOtro) {
		this.autoMedicacionLugOtro = autoMedicacionLugOtro;
	}

	public BigDecimal getDiagPositivoLugOtro() {
		return diagPositivoLugOtro;
	}

	public void setDiagPositivoLugOtro(BigDecimal diagPositivoLugOtro) {
		this.diagPositivoLugOtro = diagPositivoLugOtro;
	}

	public BigDecimal getTratamientoCompletoLugOtro() {
		return tratamientoCompletoLugOtro;
	}

	public void setTratamientoCompletoLugOtro(BigDecimal tratamientoCompletoLugOtro) {
		this.tratamientoCompletoLugOtro = tratamientoCompletoLugOtro;
	}

	public List<DivisionPolitica> getDeptsSintomasLugsOtros() {
		return deptsSintomasLugsOtros;
	}

	public List<DivisionPolitica> getMunisSintomasLugsOtros() {
		return munisSintomasLugsOtros;
	}

	public BigDecimal getAntTransfusion() {
		return antTransfusion;
	}

	public void setAntTransfusion(BigDecimal antTransfusion) {
		this.antTransfusion = antTransfusion;
	}

	public Date getFechaTransfusion() {
		return fechaTransfusion;
	}

	public void setFechaTransfusion(Date fechaTransfusion) {
		this.fechaTransfusion = fechaTransfusion;
	}

	public Pais getPaisTransfusionSelected() {
		return paisTransfusionSelected;
	}

	public void setPaisTransfusionSelected(Pais paisTransfusionSelected) {
		this.paisTransfusionSelected = paisTransfusionSelected;
	}

	public long getPaisTransfusionSelectedId() {
		return paisTransfusionSelectedId;
	}

	public void setPaisTransfusionSelectedId(long paisTransfusionSelectedId) {
		this.paisTransfusionSelectedId = paisTransfusionSelectedId;
	}

	public Unidad getUnidadTransfusionSelected() {
		return unidadTransfusionSelected;
	}

	public void setUnidadTransfusionSelected(Unidad unidadTransfusionSelected) {
		this.unidadTransfusionSelected = unidadTransfusionSelected;
	}

	public long getUnidadTransfusionSelectedId() {
		return unidadTransfusionSelectedId;
	}

	public void setUnidadTransfusionSelectedId(long unidadTransfusionSelectedId) {
		this.unidadTransfusionSelectedId = unidadTransfusionSelectedId;
	}

	public List<Unidad> getUnidadesTransfusion() {
		return unidadesTransfusion;
	}

	public BigDecimal getManejoClinico() {
		return manejoClinico;
	}

	public void setManejoClinico(BigDecimal manejoClinico) {
		this.manejoClinico = manejoClinico;
	}

	public Date getInicioTratamientoMClinico() {
		return inicioTratamientoMClinico;
	}

	public void setInicioTratamientoMClinico(Date inicioTratamientoMClinico) {
		this.inicioTratamientoMClinico = inicioTratamientoMClinico;
	}

	public Date getFinTratamientoMClinico() {
		return finTratamientoMClinico;
	}

	public void setFinTratamientoMClinico(Date finTratamientoMClinico) {
		this.finTratamientoMClinico = finTratamientoMClinico;
	}

	public Medicamento getMedicamentoSelected() {
		return medicamentoSelected;
	}

	public void setMedicamentoSelected(Medicamento medicamentoSelected) {
		this.medicamentoSelected = medicamentoSelected;
	}

	public long getMedicamentoSelectedId() {
		return medicamentoSelectedId;
	}

	public void setMedicamentoSelectedId(long medicamentoSelectedId) {
		this.medicamentoSelectedId = medicamentoSelectedId;
	}

	public EntidadAdtva getEntidadMClinicoSelected() {
		return entidadMClinicoSelected;
	}

	public void setEntidadMClinicoSelected(EntidadAdtva entidadMClinicoSelected) {
		this.entidadMClinicoSelected = entidadMClinicoSelected;
	}

	public long getEntidadMClinicoSelectedId() {
		return entidadMClinicoSelectedId;
	}

	public void setEntidadMClinicoSelectedId(long entidadMClinicoSelectedId) {
		this.entidadMClinicoSelectedId = entidadMClinicoSelectedId;
	}

	public Unidad getUnidadMClinicoSelected() {
		return unidadMClinicoSelected;
	}

	public void setUnidadMClinicoSelected(Unidad unidadMClinicoSelected) {
		this.unidadMClinicoSelected = unidadMClinicoSelected;
	}

	public long getUnidadMClinicoSelectedId() {
		return unidadMClinicoSelectedId;
	}

	public void setUnidadMClinicoSelectedId(long unidadMClinicoSelectedId) {
		this.unidadMClinicoSelectedId = unidadMClinicoSelectedId;
	}

	public String getNumeroExpedienteMClinico() {
		return numeroExpedienteMClinico;
	}

	public void setNumeroExpedienteMClinico(String numeroExpedienteMClinico) {
		this.numeroExpedienteMClinico = numeroExpedienteMClinico;
	}

	public BigDecimal getDiasEstanciaMClinico() {
		return diasEstanciaMClinico;
	}

	public void setDiasEstanciaMClinico(BigDecimal diasEstanciaMClinico) {
		this.diasEstanciaMClinico = diasEstanciaMClinico;
	}

	public BigDecimal getConvivientesTratadosMClinico() {
		return convivientesTratadosMClinico;
	}

	public void setConvivientesTratadosMClinico(
			BigDecimal convivientesTratadosMClinico) {
		this.convivientesTratadosMClinico = convivientesTratadosMClinico;
	}

	public BigDecimal getColateralesTratadosMClinico() {
		return colateralesTratadosMClinico;
	}

	public void setColateralesTratadosMClinico(BigDecimal colateralesTratadosMClinico) {
		this.colateralesTratadosMClinico = colateralesTratadosMClinico;
	}

	public BigDecimal getTratamientoCompletoMClinico() {
		return tratamientoCompletoMClinico;
	}

	public void setTratamientoCompletoMClinico(
			BigDecimal tratamientoCompletoMClinico) {
		this.tratamientoCompletoMClinico = tratamientoCompletoMClinico;
	}

	public BigDecimal getTratamientoSupervisadoMClinico() {
		return tratamientoSupervisadoMClinico;
	}

	public void setTratamientoSupervisadoMClinico(
			BigDecimal tratamientoSupervisadoMClinico) {
		this.tratamientoSupervisadoMClinico = tratamientoSupervisadoMClinico;
	}

	public BigDecimal getControlParasitarioMClinico() {
		return controlParasitarioMClinico;
	}

	public void setControlParasitarioMClinico(BigDecimal controlParasitarioMClinico) {
		this.controlParasitarioMClinico = controlParasitarioMClinico;
	}

	public BigDecimal getNumDiasPosterioresMClinico() {
		return numDiasPosterioresMClinico;
	}

	public void setNumDiasPosterioresMClinico(BigDecimal numDiasPosterioresMClinico) {
		this.numDiasPosterioresMClinico = numDiasPosterioresMClinico;
	}

	public BigDecimal getResultControlPositivoMClinico() {
		return resultControlPositivoMClinico;
	}

	public void setResultControlPositivoMClinico(
			BigDecimal resultControlPositivoMClinico) {
		this.resultControlPositivoMClinico = resultControlPositivoMClinico;
	}

	public BigDecimal getCondicionFinalMClinico() {
		return condicionFinalMClinico;
	}

	public void setCondicionFinalMClinico(BigDecimal condicionFinalMClinico) {
		this.condicionFinalMClinico = condicionFinalMClinico;
	}

	public Date getFechaDefuncionMClinico() {
		return fechaDefuncionMClinico;
	}

	public void setFechaDefuncionMClinico(Date fechaDefuncionMClinico) {
		this.fechaDefuncionMClinico = fechaDefuncionMClinico;
	}

	public BigDecimal getAutomedicacionMClinico() {
		return automedicacionMClinico;
	}

	public void setAutomedicacionMClinico(BigDecimal automedicacionMClinico) {
		this.automedicacionMClinico = automedicacionMClinico;
	}

	public String getMedicamentosAutomedicacionMClinico() {
		return medicamentosAutomedicacionMClinico;
	}

	public void setMedicamentosAutomedicacionMClinico(String medicamentosAutomedicacion) {
		this.medicamentosAutomedicacionMClinico = medicamentosAutomedicacion;
	}

	public List<Medicamento> getMedicamentosAntiMalaricos() {
		return medicamentosAntiMalaricos;
	}
	
	public String getMedicamentosEnLista() {
		return medicamentosEnLista;
	}

	public void setMedicamentosEnLista(String medicamentosEnLista) {
		this.medicamentosEnLista = medicamentosEnLista;
	}

	public List<EntidadAdtva> getEntidadesMClinico() {
		return entidadesMClinico;
	}

	public List<Unidad> getUnidadesMClinico() {
		return unidadesMClinico;
	}

	public Date getFechaIngresoMClinico() {
		return fechaIngresoMClinico;
	}

	public void setFechaIngresoMClinico(Date fechaIngresoMClinico) {
		this.fechaIngresoMClinico = fechaIngresoMClinico;
	}

	public BigDecimal getInfeccionEnResidenciaRInv() {
		return infeccionEnResidenciaRInv;
	}

	public void setInfeccionEnResidenciaRInv(BigDecimal infeccionEnResidenciaRInv) {
		this.infeccionEnResidenciaRInv = infeccionEnResidenciaRInv;
	}

	public Pais getPaisRInvSelected() {
		return paisRInvSelected;
	}

	public void setPaisRInvSelected(Pais paisRInvSelected) {
		this.paisRInvSelected = paisRInvSelected;
	}

	public long getPaisRInvSelectedId() {
		return paisRInvSelectedId;
	}

	public void setPaisRInvSelectedId(long paisRInvSelectedId) {
		this.paisRInvSelectedId = paisRInvSelectedId;
	}

	public DivisionPolitica getDeptRInvSelected() {
		return deptRInvSelected;
	}

	public void setDeptRInvSelected(DivisionPolitica deptRInvSelected) {
		this.deptRInvSelected = deptRInvSelected;
	}

	public long getDeptRInvSelectedId() {
		return deptRInvSelectedId;
	}

	public void setDeptRInvSelectedId(long deptRInvSelectedId) {
		this.deptRInvSelectedId = deptRInvSelectedId;
	}

	public DivisionPolitica getMuniRInvSelected() {
		return muniRInvSelected;
	}

	public void setMuniRInvSelected(DivisionPolitica muniRInvSelected) {
		this.muniRInvSelected = muniRInvSelected;
	}

	public long getMuniRInvSelectedId() {
		return muniRInvSelectedId;
	}

	public void setMuniRInvSelectedId(long muniRInvSelectedId) {
		this.muniRInvSelectedId = muniRInvSelectedId;
	}

	public Comunidad getComuRInvSelected() {
		return comuRInvSelected;
	}

	public void setComuRInvSelected(Comunidad comuRInvSelected) {
		this.comuRInvSelected = comuRInvSelected;
	}

	public Date getFechaInfeccionRInv() {
		return fechaInfeccionRInv;
	}

	public void setFechaInfeccionRInv(Date fechaInfeccionRInv) {
		this.fechaInfeccionRInv = fechaInfeccionRInv;
	}

	public ClasificacionClinica getClasifClinicaRInvSelected() {
		return clasifClinicaRInvSelected;
	}

	public void setClasifClinicaRInvSelected(
			ClasificacionClinica clasifClinicaRInvSelected) {
		this.clasifClinicaRInvSelected = clasifClinicaRInvSelected;
	}

	public long getClasifClinicaRInvSelectedId() {
		return clasifClinicaRInvSelectedId;
	}

	public void setClasifClinicaRInvSelectedId(long clasifClinicaRInvSelectedId) {
		this.clasifClinicaRInvSelectedId = clasifClinicaRInvSelectedId;
	}

	public TipoRecurrencia getTipoInfeccionRInvSelected() {
		return tipoInfeccionRInvSelected;
	}

	public void setTipoInfeccionRInvSelected(
			TipoRecurrencia tipoInfeccionRInvSelected) {
		this.tipoInfeccionRInvSelected = tipoInfeccionRInvSelected;
	}

	public long getTipoInfeccionRInvSelectedId() {
		return tipoInfeccionRInvSelectedId;
	}

	public void setTipoInfeccionRInvSelectedId(long tipoInfeccionRInvSelectedId) {
		this.tipoInfeccionRInvSelectedId = tipoInfeccionRInvSelectedId;
	}

	public TipoComplicacion getTipoComplicacionRInvSelected() {
		return tipoComplicacionRInvSelected;
	}

	public void setTipoComplicacionRInvSelected(
			TipoComplicacion tipoComplicacionRInvSelected) {
		this.tipoComplicacionRInvSelected = tipoComplicacionRInvSelected;
	}

	public long getTipoComplicacionRInvSelectedId() {
		return tipoComplicacionRInvSelectedId;
	}

	public void setTipoComplicacionRInvSelectedId(
			long tipoComplicacionRInvSelectedId) {
		this.tipoComplicacionRInvSelectedId = tipoComplicacionRInvSelectedId;
	}

	public ClasificacionCaso getClasificacionCasoRInvSelected() {
		return clasificacionCasoRInvSelected;
	}

	public void setClasificacionCasoRInvSelected(
			ClasificacionCaso clasificacionCasoRInvSelected) {
		this.clasificacionCasoRInvSelected = clasificacionCasoRInvSelected;
	}

	public long getClasificacionCasoRInvSelectedId() {
		return clasificacionCasoRInvSelectedId;
	}

	public void setClasificacionCasoRInvSelectedId(
			long clasificacionCasoRInvSelectedId) {
		this.clasificacionCasoRInvSelectedId = clasificacionCasoRInvSelectedId;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getResponsableInvest() {
		return responsableInvest;
	}

	public void setResponsableInvest(String responsableInvest) {
		this.responsableInvest = responsableInvest;
	}

	public String getEpidemiologo() {
		return epidemiologo;
	}

	public void setEpidemiologo(String epidemiologo) {
		this.epidemiologo = epidemiologo;
	}

	public BigDecimal getNivelAutorizacion() {
		return nivelAutorizacion;
	}

	public void setNivelAutorizacion(BigDecimal nivelAutorizacion) {
		this.nivelAutorizacion = nivelAutorizacion;
	}

	public BigDecimal getCasoCerrado() {
		return casoCerrado;
	}

	public void setCasoCerrado(BigDecimal casoCerrado) {
		this.casoCerrado = casoCerrado;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public List<DivisionPolitica> getDeptsSintomasRInv() {
		return deptsSintomasRInv;
	}

	public List<DivisionPolitica> getMunisSintomasRInv() {
		return munisSintomasRInv;
	}

	public List<ClasificacionClinica> getClasificsClinicasRInv() {
		return clasificsClinicasRInv;
	}

	public List<TipoRecurrencia> getTiposInfeccionesRInv() {
		return tiposInfeccionesRInv;
	}

	public List<TipoComplicacion> getTiposComplicacionesRInv() {
		return tiposComplicacionesRInv;
	}

	public List<ClasificacionCaso> getClasificacionesCasosRInv() {
		return clasificacionesCasosRInv;
	}

	public BigDecimal getPersonaConSintomaLugar() {
		return personaConSintomaLugar;
	}

	public void setPersonaConSintomaLugar(BigDecimal personaConSintomaLugar) {
		this.personaConSintomaLugar = personaConSintomaLugar;
	}
	
}
