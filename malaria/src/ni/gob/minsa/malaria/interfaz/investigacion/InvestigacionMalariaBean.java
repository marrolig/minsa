package ni.gob.minsa.malaria.interfaz.investigacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import ni.gob.minsa.malaria.datos.investigacion.InvestigacionMedicamentoDA;
import ni.gob.minsa.malaria.datos.investigacion.InvestigacionSintomaDA;
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
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMalaria;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMedicamento;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionSintoma;
import ni.gob.minsa.malaria.modelo.investigacion.Medicamento;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarAnte;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarOtro;
import ni.gob.minsa.malaria.modelo.investigacion.TipoComplicacion;
import ni.gob.minsa.malaria.modelo.investigacion.TipoRecurrencia;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Pais;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoHematico;
import ni.gob.minsa.malaria.reglas.Operacion;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionMedicamentoService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionSintomaService;
import ni.gob.minsa.malaria.servicios.investigacion.SintomaLugarAnteService;
import ni.gob.minsa.malaria.servicios.investigacion.SintomaLugarInicioService;
import ni.gob.minsa.malaria.servicios.investigacion.SintomaLugarOtroService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService;
import ni.gob.minsa.malaria.servicios.poblacion.PaisService;
import ni.gob.minsa.malaria.servicios.vigilancia.MuestreoHematicoService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

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
	private int esSintomatico=0;
	private Date fechaInicioSintomas;
	private int sintomaInicio=-2;
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
	private BigDecimal usaMosquitero=BigDecimal.valueOf(-2);
	private BigDecimal viajeZonaRiesgo=BigDecimal.valueOf(0);
	
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
	private static CatalogoElementoService<ClasificacionClinica, Integer> clasificacionClinicaService=new CatalogoElementoDA(ClasificacionClinica.class,"ClasificacionClinica");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<ConfirmacionDiagnostico, Integer> confirmacionDiagnosticoService=new CatalogoElementoDA(ConfirmacionDiagnostico.class,"ConfirmacionDiagnostico");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<Medicamento, Integer> medicamentoService=new CatalogoElementoDA(Medicamento.class,"Medicamento");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TipoComplicacion, Integer> tipoComplicacion=new CatalogoElementoDA(TipoComplicacion.class,"TipoComplicacion");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TipoRecurrencia, Integer> tipoRecurrencia=new CatalogoElementoDA(TipoRecurrencia.class,"TipoRecurrencia");
	
	private static MuestreoHematicoService muestreoHematicoService = new MuestreoHematicoDA();
	private static InvestigacionService investigacionService = new InvestigacionDA();
	private static InvestigacionMedicamentoService investigacionMedicamentoService = new InvestigacionMedicamentoDA();
	private static InvestigacionSintomaService investigacionSintomaService = new InvestigacionSintomaDA();
	private static SintomaLugarAnteService sintomaLugarAnteService  = new SintomaLugarAnteDA();
	private static SintomaLugarOtroService sintomaLugarOtroService = new SintomaLugarOtroDA();
	private static SintomaLugarInicioService sintomaLugarInicioService = new SintomaLugarInicioDA();
	
	/**************************************************
	 * Constructor
	 **************************************************/
	public InvestigacionMalariaBean(){
		init();
	}
	
	/**************************************************
	 * Eventos
	 **************************************************/
	/**
	 * Evento que se ejecuta cuando el usuario selecciona un muestreo hemático
	 * de la grilla.  Traslada la M10 asociada al objeto seleccionado 
	 * a los controles del panel de detalle.
	 */
	public void onMuestreoHematicoSelected(SelectEvent iEvento) { 
		this.muestreoHematicoSelectedId = this.muestreoHematicoSelected.getMuestreoHematicoId();
		
		InfoResultado oResInvestigacionSel=investigacionService.EncontrarPorMuestreoHematico(this.muestreoHematicoSelectedId);
		if (oResInvestigacionSel.isExcepcion()) {
			FacesMessage msg = Mensajes.enviarMensaje(oResInvestigacionSel);
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		this.capaActiva=2;
		this.investigacionMalariaSelected=(InvestigacionMalaria)oResInvestigacionSel.getObjeto();
		//Si no se encuentra una Investigación para la E2 seleccionada, quiere decir que estamos agregando una nueva M10; 
		//de no ser nulo quiere decir que estamos editando una M10. 
		if (this.investigacionMalariaSelected != null) {
			if (this.investigacionMalariaSelected.getConfirmacionEntidad() != null) {
				this.diagnosticosEntidad = confirmacionDiagnosticoService.ListarActivos(this.investigacionMalariaSelected.getConfirmacionEntidad().getCodigo());
				this.diagnosticoEntidadSelectedId = this.investigacionMalariaSelected.getConfirmacionEntidad().getCatalogoId();

			} else {
				this.diagnosticosEntidad = confirmacionDiagnosticoService.ListarActivos();
			}
			
			if (this.investigacionMalariaSelected.getConfirmacionCndr() != null) {
				this.diagnosticosCndr = confirmacionDiagnosticoService.ListarActivos(this.investigacionMalariaSelected.getConfirmacionCndr().getCodigo());
				this.diagnosticoCndrSelectedId = this.investigacionMalariaSelected.getConfirmacionCndr().getCatalogoId();
			} else {
				this.diagnosticosCndr = confirmacionDiagnosticoService.ListarActivos();
			}
			this.longitud = this.investigacionMalariaSelected.getLongitudVivienda();
			this.latitud = this.investigacionMalariaSelected.getLatitudVivienda();
			
		} else {
			this.numeroCaso="";
			if(this.muestreoHematicoSelected.getVivienda()!=null){
				this.longitud = this.muestreoHematicoSelected.getVivienda().getLongitud();
				this.latitud = this.muestreoHematicoSelected.getVivienda().getLatitud();
			}else{
				this.longitud=null;
				this.latitud=null;
			}
			this.diagnosticosEntidad = confirmacionDiagnosticoService.ListarActivos();
			this.diagnosticosCndr = confirmacionDiagnosticoService.ListarActivos();
		}
		
	}
	
	public void onSintomaticoSelected(){
		if(this.esSintomatico==0){
			this.sintomaInicio=-2;
			this.fechaInicioSintomas=null;
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
		
		if(this.sintomaInicio==0){
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
		this.usaMosquitero=BigDecimal.valueOf(-2);
	}
	
	public List<Comunidad> completarComuLugInicio(String query) {
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades=comunidadService.ComunidadesPorMunicipioYNombre(this.muniLugInicioSelectedId,query);
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
	
	public void cambiarMuniLugInicio(){
		this.comuLugInicioSelected=null;
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
	
	public void regresarCapa1() {
		iniciarCapa1();
	}
		
	
	/**************************************************
	 * Métodos privados
	 **************************************************/
	
	private void init(){
		this.capaActiva=1;
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
		
	}
	
	private void iniciarCapa2(){
		this.diagnosticosEntidad = confirmacionDiagnosticoService.ListarActivos();
		this.diagnosticosCndr = confirmacionDiagnosticoService.ListarActivos();
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
				
				numMuestreos = muestreoHematicoService.ContarPositivosPorUnidadActivos(entidadSelectedId,unidadSelectedId, anioEpiSelected , true);
				oMuestreoHematicoList = muestreoHematicoService.ListarPositivosPorUnidad(entidadSelectedId,unidadSelectedId,  anioEpiSelected, true, 
						 first, pageSize, numMuestreos);
				this.setRowCount(numMuestreos);
				return oMuestreoHematicoList;
			}
		};		
	}
	
	private void iniInvestigacionMedicamentos(){
		this.investigacionesMedicamentos = null;
		if(this.investigacionMalariaSelected==null || this.investigacionMalariaSelected.getInvestigacionMalariaId() < 1){
			return;
		}
		
		this.investigacionesMedicamentos = investigacionMedicamentoService.
			MedicamentosPorInvestigacion(this.investigacionMalariaSelected.getInvestigacionMalariaId());
		
	}
	
	private void iniSintomasLugaresAntes(){
		this.sintomasLugaresAntes=null;
		if(this.investigacionSintomaSelected==null||this.investigacionSintomaSelectedId < 1){
			return;
		}
		this.sintomasLugaresAntes = sintomaLugarAnteService.SintomasLugarAntePorInvestigacionSintomas(this.investigacionSintomaSelectedId);
	}
	

	private void iniSintomasLugaresOtros(){
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
	
	public int getEsSintomatico() {
		return esSintomatico;
	}

	public void setEsSintomatico(int esSintomatico) {
		this.esSintomatico = esSintomatico;
	}

	public Date getFechaInicioSintomas() {
		return fechaInicioSintomas;
	}

	public void setFechaInicioSintomas(Date fechaInicioSintomas) {
		this.fechaInicioSintomas = fechaInicioSintomas;
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

	public int getSintomaInicio() {
		return sintomaInicio;
	}

	public void setSintomaInicio(int sintomaInicio) {
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
	
	
	
}
