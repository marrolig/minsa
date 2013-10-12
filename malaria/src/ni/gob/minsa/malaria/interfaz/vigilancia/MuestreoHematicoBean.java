// -----------------------------------------------
// MuestreoHematicoBean.java
// -----------------------------------------------

package ni.gob.minsa.malaria.interfaz.vigilancia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.ejbPersona.dto.Persona;
import ni.gob.minsa.ejbPersona.servicios.PersonaBTMService;
import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.datos.estructura.UnidadDA;
import ni.gob.minsa.malaria.datos.general.CatalogoElementoDA;
import ni.gob.minsa.malaria.datos.general.MarcadorDA;
import ni.gob.minsa.malaria.datos.general.ParametroDA;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadDA;
import ni.gob.minsa.malaria.datos.poblacion.DivisionPoliticaDA;
import ni.gob.minsa.malaria.datos.poblacion.ManzanaDA;
import ni.gob.minsa.malaria.datos.poblacion.ViviendaDA;
import ni.gob.minsa.malaria.datos.vigilancia.ColVolDA;
import ni.gob.minsa.malaria.datos.vigilancia.PuestoComunidadDA;
import ni.gob.minsa.malaria.datos.vigilancia.PuestoNotificacionDA;
import ni.gob.minsa.malaria.interfaz.sis.PersonaBean;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.general.ClaseAccesibilidad;
import ni.gob.minsa.malaria.modelo.general.Marcador;
import ni.gob.minsa.malaria.modelo.general.Ocupacion;
import ni.gob.minsa.malaria.modelo.general.Parametro;
import ni.gob.minsa.malaria.modelo.general.TipoTransporte;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Manzana;
import ni.gob.minsa.malaria.modelo.poblacion.Vivienda;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.ViviendaUltimaEncuesta;
import ni.gob.minsa.malaria.modelo.seguridad.UsuarioUnidad;
import ni.gob.minsa.malaria.modelo.sis.Etnia;
import ni.gob.minsa.malaria.modelo.sis.Sexo;
import ni.gob.minsa.malaria.modelo.vigilancia.ColVol;
import ni.gob.minsa.malaria.modelo.vigilancia.ColVolAcceso;
import ni.gob.minsa.malaria.modelo.vigilancia.DensidadCruces;
import ni.gob.minsa.malaria.modelo.vigilancia.EstadioPFalciparum;
import ni.gob.minsa.malaria.modelo.vigilancia.MotivoFaltaDiagnostico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoDiagnostico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoHematico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoPruebaRapida;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoComunidad;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoNotificacion;
import ni.gob.minsa.malaria.modelo.vigilancia.ResponsablePruebaRapida;
import ni.gob.minsa.malaria.modelo.vigilancia.ResultadoPruebaRapida;
import ni.gob.minsa.malaria.modelo.vigilancia.noEntidad.ColVolPuesto;
import ni.gob.minsa.malaria.reglas.Operacion;
import ni.gob.minsa.malaria.reglas.VigilanciaValidacion;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.servicios.general.MarcadorService;
import ni.gob.minsa.malaria.servicios.general.ParametroService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.servicios.poblacion.ManzanaService;
import ni.gob.minsa.malaria.servicios.poblacion.ViviendaService;
import ni.gob.minsa.malaria.servicios.vigilancia.ColVolService;
import ni.gob.minsa.malaria.servicios.vigilancia.PuestoNotificacionService;
import ni.gob.minsa.malaria.soporte.CalendarioEPI;
import ni.gob.minsa.malaria.soporte.TipoBusqueda;
import ni.gob.minsa.malaria.soporte.Utilidades;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * Servicio para la capa de presentación de la página 
 * poblacion/muestreoHematico.xhtml
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 05/12/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class MuestreoHematicoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	protected InfoSesion infoSesion;
	private int capaActiva;
	
	// -------------------------------------------------------------
	// Estado
	// 
	// El atributo estado indica la situación en que se encuentra
	// una ficha de muestreo hemático y por tanto es la variable que 
	// gestionará el bloqueo y y peticiones desde el interfaz al bean.
	//
	// Valores:
	// 0 : Nueva ficha.  El interfaz se encuentra preparado para agregar
	//     una nueva ficha de muestreo hemático.  Todos los paneles
	//     se encuentran vacíos
	// 1 : Ficha existente sin investigación epidemiológica asociada.  
	//     El usuario ha indicado un número de clave
	//     y lámina, y ésta ha sido encontrada en la base de datos.  En
	//     este caso, se visualizan los datos y se protegen aquellos que
	//     no son modificables.
	// 2 : Ficha existente con investigación epidemiológica abierta.
	// 3 : Ficha existente con investigación epidemiológica cerrada.
	// -------------------------------------------------------------
	private int estado;
	
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
	// el usuario tiene autorización explícita, dichas unidades
	// debe estar declarada como puesto de notificación
	// -------------------------------------------------------------
	private List<Unidad> unidades;
	private Unidad unidadSelected;
	private long unidadSelectedId;

	// -------------------------------------------------------------
	// Colaborador Voluntario
	//
	// Objetos vinculados a los colvoles que son puestos de
	// notificación
	// -------------------------------------------------------------
	private List<ColVolPuesto> colVolPuestos;
	private int numColVolPuestos;
	private ColVolPuesto colVolPuestoSelected;
	private long puestoNotificacionId;
	private String filtroColVol;
	private String nombreColVol;
	
	// atributos vinculados a la identificación de la ficha E-2
	private String clave;
	private BigDecimal numeroLamina;
	private Date fechaTomaMuestra;
	
	private TipoBusqueda tipoBusquedaSelected;
	private List<TipoBusqueda> tiposBusquedas;
	
	// atributos calculados en base a la fecha de toma de muestra de la gota gruesa
	private String semanaEpidemiologica;
	private String anioEpidemiologico;
	
	// atributos vinculados a la persona a la cual se le realiza el muestreo
	// y que son almacenados en la ficha de muestreo y por tanto son inmutables
	// independientemente si posteriormente, por otro vía, se realizan modificaciones
	// a los datos de la persona.  Para que esas modificaciones sean efectivas
	// deben ser realizadas desde la misma ficha.

	private long personaId;
	
	private Sexo sexo;
	private Date fechaNacimiento;
	private Etnia etnia;
	private DivisionPolitica municipioResidencia;
	private Comunidad comunidadResidencia;
	private String direccionResidencia;
	private BigDecimal embarazada;
	
	private Manzana manzanaSelected;
	private String manzanaCodigo;  // únicamente el consecutivo de manzana sin el código de la comunidad
	private LazyDataModel<Manzana> manzanas;
	private int numManzanas;
	
	private Vivienda viviendaSelected;
	private ViviendaUltimaEncuesta viviendaUltimaEncuestaSelected;
	private String viviendaCodigo; // únicamente el consecutivo de la vivienda sin el código de la comunidad
	private LazyDataModel<ViviendaUltimaEncuesta> viviendas;
	private int numViviendas;

	private String personaReferente;
	private String telefonoReferente;
	private String tmpPersonaReferente;
	private String tmpTelefonoReferente;
	
	private String empleador;
	
	// atributos vinculados a los síntomas y tratamiento

	private Date inicioSintomas;
	private BigDecimal manejoClinico;   // hospitalario=1, ambulatorio=0
	private Date inicioTratamiento;
	private Date finTratamiento;
	private BigDecimal tratamientoEnBoca;
	private BigDecimal tratamientoRemanente;
	private BigDecimal cloroquina;
	private BigDecimal primaquina5mg;
	private BigDecimal primaquina15mg;
	
	// atributos vinculados a la prueba rápida de malaria
	
	private MuestreoPruebaRapida muestreoPruebaRapidaSelected;
	
	private Date fechaAplicacion;
	
	private long responsablePruebaRapidaSelectedId;
	private List<ResponsablePruebaRapida> responsablesPruebasRapidas;
	private ResponsablePruebaRapida responsablePruebaRapidaSelected;
	
	private long resultadoPruebaRapidaSelectedId;
	private List <ResultadoPruebaRapida> resultadosPruebasRapidas;
	private ResultadoPruebaRapida resultadoPruebaRapidaSelected;
	
	// atributos vinculados al diagnóstico por técnica de gota gruesa
	
	private MuestreoDiagnostico muestreoDiagnosticoSelected;
	
	private Date fechaRecepcion;
	private Date fechaDiagnostico;
	private BigDecimal resultado;
	private Boolean positivoPVivax;
	private Boolean positivoPFalciparum;
	
	private List<DensidadCruces> densidadCruces;
	
	private long densidadPVivaxSelectedId;
	private DensidadCruces densidadPVivaxSelected;
	private long densidadPFalciparumSelectedId;
	private DensidadCruces densidadPFalciparumSelected;
	
	private long estadioPFalciparumSelectedId;
	private EstadioPFalciparum estadioPFalciparumSelected;
	private List<EstadioPFalciparum> estadiosPFalciparum;
	
	private BigDecimal estadiosAsexuales;
	private BigDecimal gametocitos;
	private BigDecimal leucocitos;
	
	private EntidadAdtva entidadLaboratorioSelected;
	private long entidadLaboratorioSelectedId;
	private List<EntidadAdtva> entidadesLaboratorios;
	private Unidad unidadLaboratorioSelected;
	private String laboratorista;
	
	private MotivoFaltaDiagnostico motivoFaltaDiagnosticoSelected;
	private long motivoFaltaDiagnosticoSelectedId;
	private List<MotivoFaltaDiagnostico> motivosFaltaDiagnosticos;
	
		
	// ----------------------------------------------------------
	// atributos vinculados a la gestión de personas
	// ----------------------------------------------------------
	
	// persona seleccionada desde la lista de personas resultantes de la busqueda al componente de personas
	private Persona personaSelected;

	// comunidad seleccionada de la lista de sugerencias del control de autocompletar
	private Comunidad comunidadSelected;
	
	// ----------------------------------------------------------
	// atributos vinculados a los servicios y capa DAO
	// ----------------------------------------------------------
	
	private static ColVolService colVolService = new ColVolDA();
	private static EntidadAdtvaService entidadService = new EntidadAdtvaDA();
	private static UnidadService unidadService = new UnidadDA();
	private static ComunidadService comunidadService = new ComunidadDA();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TipoTransporte,Integer> tipoTransporteService=new CatalogoElementoDA(TipoTransporte.class,"TipoTransporte");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<ClaseAccesibilidad,Integer> claseAccesibilidadService=new CatalogoElementoDA(ClaseAccesibilidad.class,"ClaseAccesibilidad");
	private static MarcadorService marcadorService = new MarcadorDA();
	private static ParametroService parametroService = new ParametroDA();

	private static CatalogoElementoService<ResponsablePruebaRapida,Integer> responsablePruebaRapidaService=new CatalogoElementoDA(ResponsablePruebaRapida.class,"ResponsablePruebaRapida");
	private static CatalogoElementoService<ResultadoPruebaRapida,Integer> resultadoPruebaRapidaService=new CatalogoElementoDA(ResultadoPruebaRapida.class,"ResultadoPruebaRapida");

	private static CatalogoElementoService<DensidadCruces,Integer> densidadCrucesService=new CatalogoElementoDA(DensidadCruces.class,"DensidadCruces");
	private static CatalogoElementoService<EstadioPFalciparum,Integer> estadioPFalciparumService=new CatalogoElementoDA(EstadioPFalciparum.class,"EstadioPFalciparum");
	private static CatalogoElementoService<MotivoFaltaDiagnostico,Integer> motivoFaltaDiagnosticoService=new CatalogoElementoDA(MotivoFaltaDiagnostico.class,"MotivoFaltaDiagnostico");
	
	private static PuestoNotificacionService puestoNotificacionService = new PuestoNotificacionDA();
	private static ManzanaService manzanaService = new ManzanaDA();
	private static ViviendaService viviendaService = new ViviendaDA();
	
	private static EntidadAdtvaService entidadAdtvaService = new EntidadAdtvaDA();
	
	public MuestreoHematicoBean() {
		
		this.capaActiva=1;
		
		this.infoSesion=Utilidades.obtenerInfoSesion();
		this.entidades= new ArrayList<EntidadAdtva>();
		this.unidades= new ArrayList<Unidad>();

		this.entidadSelectedId=0;
		this.unidadSelectedId=0;

		this.tiposBusquedas=new ArrayList<TipoBusqueda>();
		this.tiposBusquedas=TipoBusqueda.VALORES_EXT;

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

		iniDataModelManzanas();
		iniDataModelViviendas();
		
		this.tiposBusquedas=TipoBusqueda.VALORES;
		
		iniciarCapaControl();
		iniciarCapaPaciente();
		iniciarCapaReferente();
		iniciarCapaTratamiento();
		iniciarCapaPRM();
		iniciarCapaPGG();

	}

	public void iniDataModelManzanas() {
		
		this.manzanas = new LazyDataModel<Manzana>() {

			private static final long serialVersionUID = 1L;

			@Override
		    public List<Manzana> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
			
				if (personaSelected==null || personaSelected.getComuResiCodigo()==null || personaSelected.getComuResiCodigo().trim().isEmpty()) {
					return null;
				}
				
				List<Manzana> manzanaList=null;
				numManzanas=0;
			
				numManzanas=manzanaService.ContarPorComunidad(personaSelected.getComuResiCodigo());
				manzanaList=manzanaService.ListarPorComunidad(personaSelected.getComuResiCodigo(), first, pageSize, numManzanas);
				this.setRowCount(numManzanas);

				return manzanaList;
			}

		};
		
		this.manzanas.setRowCount(numManzanas);

	}
	
	public void iniDataModelViviendas() {

		this.viviendas = new LazyDataModel<ViviendaUltimaEncuesta>() {

			private static final long serialVersionUID = 1L;

			@Override
		    public List<ViviendaUltimaEncuesta> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
			
				if (personaSelected==null || personaSelected.getComuResiCodigo()==null || personaSelected.getComuResiCodigo().trim().isEmpty()) {
					return null;
				}
				
				List<ViviendaUltimaEncuesta> viviendaList=null;
				numViviendas=0;
			
				numViviendas=viviendaService.ContarViviendasPorComunidad(personaSelected.getComuResiCodigo(),null,Boolean.FALSE);
				viviendaList=viviendaService.ViviendasPorComunidad(personaSelected.getComuResiCodigo(), null, Boolean.FALSE, first, pageSize, numViviendas);
				this.setRowCount(numViviendas);

				return viviendaList;
			}

		};
		
		this.viviendas.setRowCount(numViviendas);

	}
	
	public void iniciarCapas() {

		this.colVolPuestoSelected=null;
		this.personaSelected=null;
		this.puestoNotificacionId=0;
		this.capaActiva=1;

	}
	
	public void iniciarCapa1() {
		
		iniciarCapas();
		
	}

	/**
	 * Unicamente se inician todas las variables a excepción
	 * de la entidad administrativa, municipio y unidad de salud, ya que
	 * estos se inicializan al entrar a la opción
	 */
	public void iniciarCapaControl() {
		
		this.clave=null;
		this.numeroLamina=null;
		this.fechaTomaMuestra=null;
		this.anioEpidemiologico=null;
		this.semanaEpidemiologica=null;
		this.tipoBusquedaSelected=TipoBusqueda.PASIVA;

	}
	
	public void iniciarCapaPaciente() {
		
		iniciarPersonas();
	}
		
	public void iniciarCapaReferente() {
		
		this.personaReferente=null;
		this.telefonoReferente=null;
		
	}
	
	public void iniciarCapaTratamiento() {
	
		this.inicioSintomas=null;
		this.manejoClinico=Utilidades.CERO;
		this.inicioTratamiento=null;
		this.finTratamiento=null;
		this.tratamientoEnBoca=null;
		this.tratamientoRemanente=null;
		this.cloroquina=null;
		this.primaquina15mg=null;
		this.primaquina5mg=null;
			
	}
	
	public void iniciarCapaPRM() {
		
		this.fechaAplicacion = null;
		this.responsablePruebaRapidaSelectedId=0;
		this.responsablesPruebasRapidas=responsablePruebaRapidaService.ListarActivos();
		this.responsablePruebaRapidaSelected=null;
		this.resultadoPruebaRapidaSelectedId=0;
		this.resultadosPruebasRapidas=resultadoPruebaRapidaService.ListarActivos();
		this.resultadoPruebaRapidaSelected=null;
		
	}
	
	public void iniciarCapaPGG() {
		
		this.fechaRecepcion=null;
		this.fechaDiagnostico=null;
		this.resultado=null;
		this.positivoPVivax=null;
		this.positivoPFalciparum=null;
		this.densidadCruces=densidadCrucesService.ListarActivos();
		this.densidadPVivaxSelectedId=0;
		this.densidadPVivaxSelected=null;
		this.densidadPFalciparumSelectedId=0;
		this.densidadPFalciparumSelected=null;
		this.estadioPFalciparumSelectedId=0;
		this.estadioPFalciparumSelected=null;
		this.estadiosPFalciparum=estadioPFalciparumService.ListarActivos();
		this.estadiosAsexuales=null;
		this.gametocitos=null;
		this.leucocitos=null;
		
		this.entidadLaboratorioSelected=null;
		this.entidadesLaboratorios=entidadService.EntidadesAdtvasActivas();
		this.entidadLaboratorioSelectedId=this.entidadSelectedId;

		this.laboratorista=null;
		
	}
	
	public void iniciarPersonas() {

		// inicializa las variables del componente de personas
		FacesContext context = FacesContext.getCurrentInstance();
		PersonaBean personaBean = (PersonaBean)context.getApplication().evaluateExpressionGet(context, "#{personaBean}", PersonaBean.class);
		personaBean.setPersonaSelected(null);
		personaBean.setTextoBusqueda("");
		personaBean.setPersonaListaSelected(null);

	}

	public List<ColVolPuesto> completarColVol(String query) {

		List<ColVolPuesto> oColVolesPuestos = new ArrayList<ColVolPuesto>();

		if (this.unidadSelectedId==0) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "Debe seleccionar primero la unidad de salud que coordina al Colaborador Voluntario","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return oColVolesPuestos;
		}
		
		oColVolesPuestos=puestoNotificacionService.ListarColVolPorUnidad(this.unidadSelectedId,query,true,0,10,200);
		return oColVolesPuestos;

	}
	
	/**
	 * Ejecutado cuando el usuario selecciona una manzana y pulsa el
	 * botón Aceptar.  Se inicializan las variables correspondientes
	 * para la manzana seleccionada.
	 */
	public void aceptarManzana() {
		
		this.manzanaCodigo=this.manzanaSelected.getCodigo().substring(9, 12);
		
	}

	/**
	 * Ejecutado cuando el usuario selecciona una vivienda y pulsa el
	 * botón Aceptar.  Se inicializan las variables correspondientes
	 * para la vivienda seleccionada.
	 */
	public void aceptarVivienda() {
		
		this.viviendaCodigo=this.viviendaUltimaEncuestaSelected.getCodigo().substring(9, 14);
		InfoResultado oInfoVivienda=viviendaService.Encontrar(this.viviendaUltimaEncuestaSelected.getViviendaId());
		if (oInfoVivienda.isOk()) {
			this.viviendaSelected=(Vivienda)oInfoVivienda.getObjeto();
			if (this.viviendaSelected.getViviendaManzana()!=null) {
				this.manzanaCodigo=this.viviendaSelected.getViviendaManzana().getManzana().getCodigo().substring(9, 12);;
			}
		}
		
	}
	
	public void quitarManzana() {
		
		this.manzanaSelected=null;
		this.manzanaCodigo=null;

	}

	public void quitarVivienda() {
		
		this.viviendaSelected=null;
		this.viviendaCodigo=null;

	}
	
	public void editarReferente(ActionEvent pEvento) {
		
		this.tmpPersonaReferente=this.personaReferente;
		this.tmpTelefonoReferente=this.telefonoReferente;
		
	}

	public void aceptarReferente() {
		
		RequestContext rContext = RequestContext.getCurrentInstance();
		
		String iControl=(this.tmpPersonaReferente!=null && !this.tmpPersonaReferente.trim().isEmpty())?"1":"0"; 
		iControl=iControl+((this.tmpTelefonoReferente!=null && !this.tmpTelefonoReferente.trim().isEmpty())?"1":"0");
		if (iControl.equals("01")) {
			rContext.addCallbackParam("referenteValida", false);
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Si declara un número de teléfono, debe especificar el nombre de la persona referente","");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			rContext.addCallbackParam("referenteValida", true);
			this.personaReferente=this.tmpPersonaReferente;
			this.telefonoReferente=this.tmpTelefonoReferente;
		}
	}

	public void eliminarReferente() {

		this.personaReferente=null;
		this.telefonoReferente=null;
		this.tmpPersonaReferente=null;
		this.tmpTelefonoReferente=null;
		
	}

	public void aceptarPRM() {

		RequestContext rContext = RequestContext.getCurrentInstance();

		MuestreoPruebaRapida oMuestreoPruebaRapida = new MuestreoPruebaRapida();
		oMuestreoPruebaRapida.setFecha(this.fechaAplicacion);
		
		if (this.resultadoPruebaRapidaSelectedId==0) {
			this.resultadoPruebaRapidaSelected=null;
		} else {
			InfoResultado oResultado=resultadoPruebaRapidaService.Encontrar(this.resultadoPruebaRapidaSelectedId);
			if (oResultado.isOk()) {
				this.resultadoPruebaRapidaSelected=(ResultadoPruebaRapida) oResultado.getObjeto();
			} else {
				rContext.addCallbackParam("prmValida", false);
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "El resultado de la prueba rápida seleccionado no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return; 
			}
		}
		
		if (this.responsablePruebaRapidaSelectedId==0) {
			this.responsablePruebaRapidaSelected=null;
		} else {
			InfoResultado oResultado=responsablePruebaRapidaService.Encontrar(this.responsablePruebaRapidaSelectedId);
			if (oResultado.isOk()) {
				this.responsablePruebaRapidaSelected=(ResponsablePruebaRapida) oResultado.getObjeto();
			} else {
				rContext.addCallbackParam("prmValida", false);
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "El responsable de aplicación de la prueba rápida seleccionado no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
		}
		
		oMuestreoPruebaRapida.setResultado(this.resultadoPruebaRapidaSelected);
		oMuestreoPruebaRapida.setRealizado(this.responsablePruebaRapidaSelected);
		InfoResultado oResultado=VigilanciaValidacion.validarPRM(oMuestreoPruebaRapida);
		
		if (!oResultado.isOk()) {
			rContext.addCallbackParam("prmValida", false);
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, oResultado.getMensaje(),"");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			rContext.addCallbackParam("prmValida", true);
			
			// si muestreoPrubaRapidaSelected es nula, implica que se trata de un nuevo
			// valor para la prueba rápida
			if (this.muestreoPruebaRapidaSelected==null) {
				this.muestreoPruebaRapidaSelected=new MuestreoPruebaRapida();
				this.muestreoPruebaRapidaSelected.setUsuarioRegistro(this.infoSesion.getUsername());
				this.muestreoPruebaRapidaSelected.setFechaRegistro(Calendar.getInstance().getTime());
			}
			this.muestreoPruebaRapidaSelected.setFecha(this.fechaAplicacion);
			this.muestreoPruebaRapidaSelected.setRealizado(this.responsablePruebaRapidaSelected);
			this.muestreoPruebaRapidaSelected.setResultado(this.resultadoPruebaRapidaSelected);
		}
	}

	public void eliminarPRM() {

		this.muestreoPruebaRapidaSelected=null;
		this.fechaAplicacion=null;
		this.responsablePruebaRapidaSelectedId=0;
		this.responsablePruebaRapidaSelected=null;
		this.resultadoPruebaRapidaSelected=null;
		this.resultadoPruebaRapidaSelectedId=0;
		
	}
	
	public void cambiarResultado() {
	
		// si el resultado es nulo, es evaluado cuando se efectúa
		// una llamada interna y no desde el interfaz
		
		if (this.resultado==null) {
			this.positivoPVivax=null;
			this.positivoPFalciparum=null;
		}
		
		if (this.resultado!=null && this.resultado.equals(Utilidades.NEGATIVO)) {
			this.positivoPVivax=Boolean.FALSE;
			this.positivoPFalciparum=Boolean.FALSE;
		}
		
		// incluye si es nulo y negativo
		if (this.resultado==null || this.resultado.equals(Utilidades.NEGATIVO)) {
			this.densidadPFalciparumSelected=null;
			this.densidadPVivaxSelected=null;
			this.densidadPFalciparumSelectedId=0;
			this.densidadPVivaxSelectedId=0;
			this.estadioPFalciparumSelected=null;
			this.estadioPFalciparumSelectedId=0;
			this.gametocitos=null;
			this.leucocitos=null;
			this.estadiosAsexuales=null;
		}
	}
	
	public void controlarRecepcion() {
		
		if (this.fechaRecepcion==null) {
			this.fechaDiagnostico=null;
			controlarDiagnostico();

			this.entidadLaboratorioSelected=null;
			this.entidadLaboratorioSelectedId=0;
			this.unidadLaboratorioSelected=null;
			this.laboratorista=null;
		}
	}
	
	public void controlarDiagnostico() {
		
		if (this.fechaDiagnostico==null) {
			this.resultado=null;
			cambiarResultado();
		} else {
			this.motivoFaltaDiagnosticoSelected=null;
			this.motivoFaltaDiagnosticoSelectedId=0;
		}
	}
	
	public void controlarVivax() {
		
		if (!this.positivoPVivax) {
			this.densidadPVivaxSelected=null;
			this.densidadPVivaxSelectedId=0;
		}
	}

	public void controlarFalciparum() {
		
		if (!this.positivoPFalciparum) {
			this.densidadPFalciparumSelected=null;
			this.densidadPFalciparumSelectedId=0;
			this.estadioPFalciparumSelected=null;
			this.estadioPFalciparumSelectedId=0;
		}
	}

	public void aceptarPGG() {

		RequestContext rContext = RequestContext.getCurrentInstance();

		MuestreoDiagnostico oMuestreoDiagnostico = new MuestreoDiagnostico();
		oMuestreoDiagnostico.setFechaRecepcion(this.fechaRecepcion);
		oMuestreoDiagnostico.setFechaDiagnostico(this.fechaDiagnostico);
		oMuestreoDiagnostico.setResultado(this.resultado);
		if (this.resultado==null || this.resultado.equals(Utilidades.CERO)) {
			oMuestreoDiagnostico.setPositivoPVivax(null);
			oMuestreoDiagnostico.setPositivoPFalciparum(null);
		} else {
			oMuestreoDiagnostico.setPositivoPVivax(this.positivoPVivax?Utilidades.POSITIVO:Utilidades.NEGATIVO);
			oMuestreoDiagnostico.setPositivoPFalciparum(this.positivoPFalciparum?Utilidades.POSITIVO:Utilidades.NEGATIVO);
		}
		
		if (this.densidadPVivaxSelectedId==0) {
			this.densidadPVivaxSelected=null;
		} else {
			InfoResultado oResultado=densidadCrucesService.Encontrar(this.densidadPVivaxSelectedId);
			if (oResultado.isOk()) {
				this.densidadPVivaxSelected=(DensidadCruces) oResultado.getObjeto();
			} else {
				rContext.addCallbackParam("pggValida", false);
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "La densidad en cruces que ha seleccionado no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return; 
			}
		}
		oMuestreoDiagnostico.setDensidadPVivax(this.densidadPVivaxSelected);
		
		if (this.densidadPFalciparumSelectedId==0) {
			this.densidadPFalciparumSelected=null;
		} else {
			InfoResultado oResultado=densidadCrucesService.Encontrar(this.densidadPFalciparumSelectedId);
			if (oResultado.isOk()) {
				this.densidadPFalciparumSelected=(DensidadCruces) oResultado.getObjeto();
			} else {
				rContext.addCallbackParam("pggValida", false);
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "La densidad en cruces que ha seleccionado no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return; 
			}
		}
		oMuestreoDiagnostico.setDensidadPFalciparum(this.densidadPFalciparumSelected);
		
		oMuestreoDiagnostico.setEstadiosAsexuales(this.estadiosAsexuales);
		oMuestreoDiagnostico.setGametocitos(this.gametocitos);
		oMuestreoDiagnostico.setLeucocitos(this.leucocitos);
		
		if (this.estadioPFalciparumSelectedId==0) {
			this.estadioPFalciparumSelected=null;
		} else {
			InfoResultado oResultado=estadioPFalciparumService.Encontrar(this.estadioPFalciparumSelectedId);
			if (oResultado.isOk()) {
				this.estadioPFalciparumSelected=(EstadioPFalciparum) oResultado.getObjeto();
			} else {
				rContext.addCallbackParam("pggValida", false);
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "El estadío seleccionado para el P.falciparum no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return; 
			}
		}
		oMuestreoDiagnostico.setEstadioPFalciparum(this.estadioPFalciparumSelected);
		
		if (this.entidadLaboratorioSelectedId==0) {
			this.entidadLaboratorioSelected=null;
		} else {
			InfoResultado oResultado=entidadAdtvaService.Encontrar(this.entidadLaboratorioSelectedId);
			if (oResultado.isOk()) {
				this.entidadLaboratorioSelected=(EntidadAdtva) oResultado.getObjeto();
			} else {
				rContext.addCallbackParam("pggValida", false);
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "La entidad administrativa seleccionada no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return; 
			}			
		}
		oMuestreoDiagnostico.setEntidadAdtvaLaboratorio(this.entidadLaboratorioSelected);
		oMuestreoDiagnostico.setUnidadLaboratorio(this.unidadLaboratorioSelected);
		oMuestreoDiagnostico.setMunicipioLaboratorio(this.unidadLaboratorioSelected.getMunicipio());
		oMuestreoDiagnostico.setLaboratorista(this.laboratorista);
		
		if (this.motivoFaltaDiagnosticoSelectedId==0) {
			this.motivoFaltaDiagnosticoSelected=null;
		} else {
			InfoResultado oResultado=motivoFaltaDiagnosticoService.Encontrar(this.motivoFaltaDiagnosticoSelectedId);
			if (oResultado.isOk()) {
				this.motivoFaltaDiagnosticoSelected=(MotivoFaltaDiagnostico) oResultado.getObjeto();
			} else {
				rContext.addCallbackParam("pggValida", false);
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "El motivo declarado por la ausencia de diagnóstico no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
		}
		oMuestreoDiagnostico.setMotivoFaltaDiagnostico(this.motivoFaltaDiagnosticoSelected);
		
		InfoResultado oResultado=VigilanciaValidacion.validarPGG(oMuestreoDiagnostico);
		
		if (!oResultado.isOk()) {
			rContext.addCallbackParam("pggValida", false);
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, oResultado.getMensaje(),"");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			rContext.addCallbackParam("pggValida", true);
			
			// si muestreoDiagnosticoSelected es nulo, implica que se trata de un nuevo
			// valor para la prueba de gota gruesa
			if (this.muestreoDiagnosticoSelected==null) {
				this.muestreoDiagnosticoSelected=new MuestreoDiagnostico();
				this.muestreoDiagnosticoSelected.setUsuarioRegistro(this.infoSesion.getUsername());
				this.muestreoDiagnosticoSelected.setFechaRegistro(Calendar.getInstance().getTime());
			}
			this.muestreoDiagnosticoSelected.setFechaRecepcion(this.fechaAplicacion);
			this.muestreoDiagnosticoSelected.setFechaDiagnostico(this.fechaDiagnostico);
			this.muestreoDiagnosticoSelected.setDensidadPFalciparum(this.densidadPFalciparumSelected);
			this.muestreoDiagnosticoSelected.setDensidadPVivax(this.densidadPVivaxSelected);
			this.muestreoDiagnosticoSelected.setEntidadAdtvaLaboratorio(this.entidadLaboratorioSelected);
			this.muestreoDiagnosticoSelected.setEstadioPFalciparum(this.estadioPFalciparumSelected);
			this.muestreoDiagnosticoSelected.setEstadiosAsexuales(this.estadiosAsexuales);
			this.muestreoDiagnosticoSelected.setGametocitos(this.gametocitos);
			this.muestreoDiagnosticoSelected.setLaboratorista(this.laboratorista);
			this.muestreoDiagnosticoSelected.setLeucocitos(this.leucocitos);
			this.muestreoDiagnosticoSelected.setMotivoFaltaDiagnostico(this.motivoFaltaDiagnosticoSelected);
			this.muestreoDiagnosticoSelected.setMunicipioLaboratorio(this.unidadLaboratorioSelected.getMunicipio());
			this.muestreoDiagnosticoSelected.setResultado(this.resultado);
			if (this.resultado==null || this.resultado.equals(Utilidades.NEGATIVO)) {
				this.muestreoDiagnosticoSelected.setPositivoPFalciparum(null);
			    this.muestreoDiagnosticoSelected.setPositivoPVivax(null);
			} else {
				this.muestreoDiagnosticoSelected.setPositivoPFalciparum(this.positivoPFalciparum?Utilidades.POSITIVO:Utilidades.NEGATIVO);
				this.muestreoDiagnosticoSelected.setPositivoPVivax(this.positivoPVivax?Utilidades.POSITIVO:Utilidades.NEGATIVO);
			}
			this.muestreoDiagnosticoSelected.setUnidadLaboratorio(this.unidadLaboratorioSelected);
		}
	}
	
	/**
	 * Se ejecuta cuando el usuario directamente modifica la clave del puesto de
	 * notificación.  El sistema debe verificar si la clave corresponde a un colaborador
	 * voluntario o a una unidad de salud, en cuyo caso debe también verificar si ambos
	 * están asociados a una unidad de salud autorizada para el usuario.
	 */
	public void cambiarClave() {
		
		if (this.clave.trim().isEmpty()) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "Debe especificar una clave válida","");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			return; 
		}

		PuestoNotificacion oPuestoNotificacion = puestoNotificacionService.EncontrarPorClave(this.clave);
		if (oPuestoNotificacion==null) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "La clave indicada no corresponde a ningún puesto de notificación","Verifica que las claves asignadas correspondan a puestos de notificación activos");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			return; 
		}
		
		// verifica si la unidad de salud asociada se encuentra autorizada al usuario
		// las unidades autorizadas forman parte de la lista del combo

		this.colVolPuestoSelected=null;
		this.unidadSelected=null;
		this.unidadSelectedId=0;
		
		if (!this.unidades.isEmpty()) {
			for (Unidad oUnidad:this.unidades) {
				if (oUnidad.getUnidadId()==oPuestoNotificacion.getUnidad().getUnidadId()) {
					this.unidadSelected=oPuestoNotificacion.getUnidad();
					this.unidadSelectedId=oPuestoNotificacion.getUnidad().getUnidadId();
					break;
				}
			}
		}
		
		// verifica si dicho puesto pertenece a un colaborador voluntario
		
		if (oPuestoNotificacion.getColVol()!=null) {
			ColVolPuesto oColVolPuesto = new ColVolPuesto();
			oColVolPuesto.setClave(oPuestoNotificacion.getClave());
			oColVolPuesto.setNombreColVol(oPuestoNotificacion.getColVol().getSisPersona().getNombreCompleto());
			oColVolPuesto.setPuestoNotificacionId(oPuestoNotificacion.getPuestoNotificacionId());
			this.colVolPuestoSelected=oColVolPuesto;
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
		
		this.clave=null;
		PuestoNotificacion oPuestoUnidad = puestoNotificacionService.EncontrarPorUnidad(oUnidad.getUnidadId(), 1);
		if (oPuestoUnidad.getClave()!=null) {
			this.clave = oPuestoUnidad.getClave();
		} 
		
		iniciarCapa1();
		
	}
	
	public void calcularSemana() {

		this.semanaEpidemiologica=String.valueOf(CalendarioEPI.semana(this.fechaTomaMuestra));
		this.anioEpidemiologico=String.valueOf(CalendarioEPI.año(this.fechaTomaMuestra));
	}
	
	/**
	 * Se ejecuta cuando el usuario selecciona un colaborador voluntario.  La lista de colaboradores
	 * voluntarios únicamente corresponde a los colvoles activos como puesto de notificación y que 
	 * son coordinados por la unidad de salud seleccionada, que a su vez corresponde a la unidad de 
	 * salud a la cual el colvol ha notificado el caso malárico.  Al seleccionar un colvol (puesto
	 * de notificación) se actualiza la clave
	 *  
	 */
	public void onColVolPuestoSelected() { 

		this.puestoNotificacionId=this.colVolPuestoSelected.getPuestoNotificacionId();
		this.clave=colVolPuestoSelected.getClave();
		
	}
	
	
	
/*		this.capaActiva=2;
		this.colVolId=this.colVolSelected.getColVolId();
		
		InfoResultado oResPersonaSel=Operacion.ensamblarPersona(this.colVolSelected.getSisPersona());
		if (!oResPersonaSel.isOk()) {
			FacesMessage msg = Mensajes.enviarMensaje(oResPersonaSel);
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		this.personaSelected=(Persona)oResPersonaSel.getObjeto();
		
		if (this.colVolSelected.getColVolAcceso()!=null) {
			
			if (this.colVolSelected.getColVolAcceso().getTipoTransporte()!=null) {
				this.tipoTransporteSelectedId=this.colVolSelected.getColVolAcceso().getTipoTransporte().getCatalogoId();
				this.tiposTransportes=tipoTransporteService.ListarActivos(this.colVolSelected.getColVolAcceso().getTipoTransporte().getCodigo());
			} else {
				this.tiposTransportes=tipoTransporteService.ListarActivos();
			}
			
			
		} else {
			
		}
		
		// obtiene los datos vinculados al puesto de notificación
		
		this.puestoNotificacionId=0;
		this.clave=null;
		
		// se verifica si existe un municipio de residencia asociado, ya que
		// podría darse el caso que desde otro módulo se elimine
		
		// TODO eliminar si funciona
		
		if (this.colVolSelected.getSisPersona().getMunicipioResidencia()!=null) {
			this.municipioComunidadSelectedId=this.colVolSelected.getSisPersona().getMunicipioResidencia().getDivisionPoliticaId();
			this.departamentoComunidadSelectedId=this.colVolSelected.getSisPersona().getMunicipioResidencia().getDepartamento().getDivisionPoliticaId();
			this.departamentosComunidad=divisionPoliticaService.DepartamentosActivos(this.departamentoComunidadSelectedId);
			this.municipiosComunidad=divisionPoliticaService.MunicipiosActivos(this.departamentoComunidadSelectedId, this.municipioComunidadSelectedId);
		} else {
			this.municipioComunidadSelectedId=0;
			this.departamentoComunidadSelectedId=0;
			this.departamentosComunidad=divisionPoliticaService.DepartamentosActivos();
		}
		
		this.comunidadPuestoSelected=null;

		if (this.colVolId!=0) {
			PuestoNotificacion oPuestoNotificacion = puestoNotificacionService.EncontrarPorColVol(this.colVolId);
			if (oPuestoNotificacion!=null) {
				this.puestoNotificacionId=oPuestoNotificacion.getPuestoNotificacionId();
				this.clave=oPuestoNotificacion.getClave();
				this.fechaApertura=oPuestoNotificacion.getFechaApertura();
				this.fechaCierre=oPuestoNotificacion.getFechaCierre();
				this.puestosComunidades=oPuestoNotificacion.getComunidadesPuesto();
			}
			
		}
	
		// inicializa las variables del componente de personas
		FacesContext context = FacesContext.getCurrentInstance();
		PersonaBean personaBean = (PersonaBean)context.getApplication().evaluateExpressionGet(context, "#{personaBean}", PersonaBean.class);
		personaBean.setPersonaSelected(this.personaSelected);
		personaBean.iniciarPropiedades();
		
		UIComponent componentDetalle = null; 
		UIViewRoot root = context.getViewRoot( ); 
		componentDetalle = (UIComponent) root.findComponent("frmColVol:panDetalle");
		for (UIComponent uic:componentDetalle.getChildren()) {
			if (uic instanceof EditableValueHolder) {   
				EditableValueHolder evh=(EditableValueHolder)uic;   
				evh.resetValue();   
			}
		}
*/		

	public List<Unidad> completarUnidadLaboratorio(String query) {
		
		List<Unidad> oUnidades = new ArrayList<Unidad>();
		oUnidades=unidadService.UnidadesPorNombre(query, this.entidadLaboratorioSelectedId, 0, "0");
		
		return oUnidades;

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

		this.unidades=ni.gob.minsa.malaria.reglas.Operacion.unidadesAutorizadasPorEntidad(this.infoSesion.getUsuarioId(), this.entidadSelectedId, 0,true,Utilidades.ES_PUESTO_NOTIFICACION +", " + Utilidades.DECLARA_MUESTREO_HEMATICO);

		if ((this.unidades!=null) && (this.unidades.size()>0)) {
			this.unidadSelectedId=this.unidades.get(0).getUnidadId();
			this.unidadSelected=this.unidades.get(0);
			
			// obtiene clave de la unidad de salud, en caso que sea un puesto de notificación
			// ya que podría ser únicamente una unidad que registra las fichas de muestreo hemático
			// notificadas por los puestos de notificación
			PuestoNotificacion oPuestoNotificacion = puestoNotificacionService.EncontrarPorUnidad(this.unidadSelectedId, 1);
			if (oPuestoNotificacion!=null) {
				this.clave=oPuestoNotificacion.getClave();
			} else {
				this.clave=null;
			}

		}
		
	}
	
	public void activarPrimerCapa(ActionEvent pEvento) {
		
		iniciarCapa1();
		
	}

	/**
	 * Quita el filtro de la busqueda de colvoles y 
	 * obtiene todos los colvoles asociados a la unidad
	 */
	public void quitarFiltroColVol() {
		
		this.filtroColVol="";
		obtenerColVoles();
		
	}

	/**
	 * Obtiene los colvoles asociados a una unidad de salud.
	 */
	public void obtenerColVoles() {
		
		this.colVolPuestos = new ArrayList<ColVolPuesto>();

		if (this.unidadSelectedId==0) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "Debe seleccionar primero la unidad de salud que coordina al Colaborador Voluntario","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		this.colVolPuestos=puestoNotificacionService.ListarColVolPorUnidad(this.unidadSelectedId,true);
		return;

	}
	
	/**
	 * Busca los colVoles que contiene la literal especificada en el control de búsqueda
	 * y que están asociados a la unidad de salud seleccionada y son activos
	 */
	public void buscarColVol() {
		
		if (this.filtroColVol.trim().length()>0 && this.filtroColVol.trim().length()<3) { 

			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "La búsqueda solo es permitida para un número superior a 3 caracteres","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		obtenerColVoles();
		
	}
	
	public void aceptarColVol(ActionEvent pEvento) {

		this.puestoNotificacionId=this.colVolPuestoSelected.getPuestoNotificacionId();
		this.setNombreColVol(this.colVolPuestoSelected.getNombreColVol());
		this.clave=this.colVolPuestoSelected.getClave();
		
	}

	/**
	 * Se ejecuta cuando el usuario pulsa en el botón agregar
	 * y consiste en la inicialización de las diferentes propiedades
	 * para colocar el estado del formulario en modo de agregar
	 */
	public void agregar(ActionEvent pEvento) {
		
		iniciarCapas();
	    iniciarPersonas();
		
	}
	
	/**
	 * Se ejecuta cuando el usuario pulsa en el botón guardar.
	 * Este proceso incluye guardar un nuevo registro o actualizar
	 * un registro existente. <br>
	 */
	public void guardar(ActionEvent pEvento) {
		
		if (this.personaSelected==null) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La declaración de la persona a la cual se realiza el muestreo hemático es requerida","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		if (this.clave.trim().isEmpty()) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La declaración de la clave asociada al muestreo hemático es requerida","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		
		// A una ficha de muestreo hemático existente no puede modificarse la persona asociada

		InfoResultado oResultado = new InfoResultado();
		
		MuestreoHematico oMuestreoHematico = new MuestreoHematico();
		oMuestreoHematico.setAñoEpidemiologico(Integer.getInteger(this.anioEpidemiologico));
		oMuestreoHematico.setClave(this.clave);
		
		
		
		if (oResultado.isOk()){
			iniciarCapa1();
			oResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
		} 
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
		
	}
	
	public void eliminar(ActionEvent pEvento){

		if (this.puestoNotificacionId==0) return;
		
		InfoResultado oResultado = new InfoResultado();

		
		if (oResultado.isOk()){
			iniciarCapa1();
			oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public List<Comunidad> completarComunidad(String query) {
		
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades=comunidadService.ComunidadesPorUnidad(this.unidadSelectedId, query, null, 0, 10, 200);
		
		return oComunidades;

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

	public void setComunidadSelected(Comunidad comunidadSelected) {
		this.comunidadSelected = comunidadSelected;
	}

	public Comunidad getComunidadSelected() {
		return comunidadSelected;
	}

	public int getNumColVolPuestos() {
		return numColVolPuestos;
	}

	public void setNumColVolPuestos(int numColVolPuestos) {
		this.numColVolPuestos = numColVolPuestos;
	}

	public List<ColVolPuesto> getColVolPuestos() {
		return colVolPuestos;
	}

	public void setColVolPuestos(List<ColVolPuesto> colVolPuestos) {
		this.colVolPuestos = colVolPuestos;
	}

	public ColVolPuesto getColVolPuestoSelected() {
		return colVolPuestoSelected;
	}

	public void setColVolPuestoSelected(ColVolPuesto colVolPuestoSelected) {
		this.colVolPuestoSelected = colVolPuestoSelected;
	}

	public void setCapaActiva(int capaActiva) {
		this.capaActiva = capaActiva;
	}

	public int getCapaActiva() {
		return capaActiva;
	}

	public void setPersonaSelected(Persona personaSelected) {
		this.personaSelected = personaSelected;
	}

	public Persona getPersonaSelected() {
		return personaSelected;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getEstado() {
		return estado;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getClave() {
		return clave;
	}

	public void setPersonaId(long personaId) {
		this.personaId = personaId;
	}

	public long getPersonaId() {
		return personaId;
	}

	public void setPuestoNotificacionId(long puestoNotificacionId) {
		this.puestoNotificacionId = puestoNotificacionId;
	}

	public long getPuestoNotificacionId() {
		return puestoNotificacionId;
	}

	/**
	 * @return the numeroLamina
	 */
	public BigDecimal getNumeroLamina() {
		return numeroLamina;
	}

	/**
	 * @param numeroLamina the numeroLamina to set
	 */
	public void setNumeroLamina(BigDecimal numeroLamina) {
		this.numeroLamina = numeroLamina;
	}

	/**
	 * @return the fechaTomaMuestra
	 */
	public Date getFechaTomaMuestra() {
		return fechaTomaMuestra;
	}

	/**
	 * @param fechaTomaMuestra the fechaTomaMuestra to set
	 */
	public void setFechaTomaMuestra(Date fechaTomaMuestra) {
		this.fechaTomaMuestra = fechaTomaMuestra;
	}

	/**
	 * @return the semanaEpidemiologica
	 */
	public String getSemanaEpidemiologica() {
		return semanaEpidemiologica;
	}

	/**
	 * @param semanaEpidemiologica the semanaEpidemiologica to set
	 */
	public void setSemanaEpidemiologica(String semanaEpidemiologica) {
		this.semanaEpidemiologica = semanaEpidemiologica;
	}

	/**
	 * @return the anioEpidemiologico
	 */
	public String getAnioEpidemiologico() {
		return anioEpidemiologico;
	}

	/**
	 * @param anioEpidemiologico the anioEpidemiologico to set
	 */
	public void setAnioEpidemiologico(String anioEpidemiologico) {
		this.anioEpidemiologico = anioEpidemiologico;
	}

	/**
	 * @return the sexo
	 */
	public Sexo getSexo() {
		return sexo;
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	/**
	 * @return the fechaNacimiento
	 */
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento the fechaNacimiento to set
	 */
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * @return the etnia
	 */
	public Etnia getEtnia() {
		return etnia;
	}

	/**
	 * @param etnia the etnia to set
	 */
	public void setEtnia(Etnia etnia) {
		this.etnia = etnia;
	}

	/**
	 * @return the municipioResidencia
	 */
	public DivisionPolitica getMunicipioResidencia() {
		return municipioResidencia;
	}

	/**
	 * @param municipioResidencia the municipioResidencia to set
	 */
	public void setMunicipioResidencia(DivisionPolitica municipioResidencia) {
		this.municipioResidencia = municipioResidencia;
	}

	/**
	 * @return the comunidadResidencia
	 */
	public Comunidad getComunidadResidencia() {
		return comunidadResidencia;
	}

	/**
	 * @param comunidadResidencia the comunidadResidencia to set
	 */
	public void setComunidadResidencia(Comunidad comunidadResidencia) {
		this.comunidadResidencia = comunidadResidencia;
	}

	/**
	 * @return the direccionResidencia
	 */
	public String getDireccionResidencia() {
		return direccionResidencia;
	}

	/**
	 * @param direccionResidencia the direccionResidencia to set
	 */
	public void setDireccionResidencia(String direccionResidencia) {
		this.direccionResidencia = direccionResidencia;
	}

	/**
	 * @return the embarazada
	 */
	public BigDecimal getEmbarazada() {
		return embarazada;
	}

	/**
	 * @param embarazada the embarazada to set
	 */
	public void setEmbarazada(BigDecimal embarazada) {
		this.embarazada = embarazada;
	}

	/**
	 * @return the manzana
	 */
	public Manzana getManzanaSelected() {
		return manzanaSelected;
	}

	/**
	 * @param manzana the manzana to set
	 */
	public void setManzanaSelected(Manzana manzanaSelected) {
		this.manzanaSelected = manzanaSelected;
	}

	/**
	 * @return the vivienda
	 */
	public Vivienda getViviendaSelected() {
		return viviendaSelected;
	}

	/**
	 * @param vivienda the vivienda to set
	 */
	public void setViviendaSelected(Vivienda viviendaSelected) {
		this.viviendaSelected = viviendaSelected;
	}

	public String getPersonaReferente() {
		return personaReferente;
	}

	public void setPersonaReferente(String personaReferente) {
		this.personaReferente = personaReferente;
	}

	public String getTelefonoReferente() {
		return telefonoReferente;
	}

	public void setTelefonoReferente(String telefonoReferente) {
		this.telefonoReferente = telefonoReferente;
	}

	public String getTmpPersonaReferente() {
		return tmpPersonaReferente;
	}

	public void setTmpPersonaReferente(String tmpPersonaReferente) {
		this.tmpPersonaReferente = tmpPersonaReferente;
	}

	public String getTmpTelefonoReferente() {
		return tmpTelefonoReferente;
	}

	public void setTmpTelefonoReferente(String tmpTelefonoReferente) {
		this.tmpTelefonoReferente = tmpTelefonoReferente;
	}

	public Date getInicioSintomas() {
		return inicioSintomas;
	}

	public void setInicioSintomas(Date inicioSintomas) {
		this.inicioSintomas = inicioSintomas;
	}

	public BigDecimal getManejoClinico() {
		return manejoClinico;
	}

	public void setManejoClinico(BigDecimal manejoClinico) {
		this.manejoClinico = manejoClinico;
	}

	public Date getInicioTratamiento() {
		return inicioTratamiento;
	}

	public void setInicioTratamiento(Date inicioTratamiento) {
		this.inicioTratamiento = inicioTratamiento;
	}

	public Date getFinTratamiento() {
		return finTratamiento;
	}

	public void setFinTratamiento(Date finTratamiento) {
		this.finTratamiento = finTratamiento;
	}

	public BigDecimal getTratamientoEnBoca() {
		return tratamientoEnBoca;
	}

	public void setTratamientoEnBoca(BigDecimal tratamientoEnBoca) {
		this.tratamientoEnBoca = tratamientoEnBoca;
	}

	public BigDecimal getTratamientoRemanente() {
		return tratamientoRemanente;
	}

	public void setTratamientoRemanente(BigDecimal tratamientoRemanente) {
		this.tratamientoRemanente = tratamientoRemanente;
	}

	public BigDecimal getCloroquina() {
		return cloroquina;
	}

	public void setCloroquina(BigDecimal cloroquina) {
		this.cloroquina = cloroquina;
	}

	public BigDecimal getPrimaquina5mg() {
		return primaquina5mg;
	}

	public void setPrimaquina5mg(BigDecimal primaquina5mg) {
		this.primaquina5mg = primaquina5mg;
	}

	public BigDecimal getPrimaquina15mg() {
		return primaquina15mg;
	}

	public void setPrimaquina15mg(BigDecimal primaquina15mg) {
		this.primaquina15mg = primaquina15mg;
	}

	public Date getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(Date fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public long getResponsablePruebaRapidaSelectedId() {
		return responsablePruebaRapidaSelectedId;
	}

	public void setResponsablePruebaRapidaSelectedId(
			long responsablePruebaRapidaSelectedId) {
		this.responsablePruebaRapidaSelectedId = responsablePruebaRapidaSelectedId;
	}

	public List<ResponsablePruebaRapida> getResponsablesPruebasRapidas() {
		return responsablesPruebasRapidas;
	}

	public void setResponsablesPruebasRapidas(
			List<ResponsablePruebaRapida> responsablesPruebasRapidas) {
		this.responsablesPruebasRapidas = responsablesPruebasRapidas;
	}

	public ResponsablePruebaRapida getResponsablePruebaRapidaSelected() {
		return responsablePruebaRapidaSelected;
	}

	public void setResponsablePruebaRapidaSelected(
			ResponsablePruebaRapida responsablePruebaRapidaSelected) {
		this.responsablePruebaRapidaSelected = responsablePruebaRapidaSelected;
	}

	public long getResultadoPruebaRapidaSelectedId() {
		return resultadoPruebaRapidaSelectedId;
	}

	public void setResultadoPruebaRapidaSelectedId(
			long resultadoPruebaRapidaSelectedId) {
		this.resultadoPruebaRapidaSelectedId = resultadoPruebaRapidaSelectedId;
	}

	public List<ResultadoPruebaRapida> getResultadosPruebasRapidas() {
		return resultadosPruebasRapidas;
	}

	public void setResultadosPruebasRapidas(
			List<ResultadoPruebaRapida> resultadosPruebasRapidas) {
		this.resultadosPruebasRapidas = resultadosPruebasRapidas;
	}

	public ResultadoPruebaRapida getResultadoPruebaRapidaSelected() {
		return resultadoPruebaRapidaSelected;
	}

	public void setResultadoPruebaRapidaSelected(
			ResultadoPruebaRapida resultadoPruebaRapidaSelected) {
		this.resultadoPruebaRapidaSelected = resultadoPruebaRapidaSelected;
	}

	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

	public Date getFechaDiagnostico() {
		return fechaDiagnostico;
	}

	public void setFechaDiagnostico(Date fechaDiagnostico) {
		this.fechaDiagnostico = fechaDiagnostico;
	}

	public BigDecimal getResultado() {
		return resultado;
	}

	public void setResultado(BigDecimal resultado) {
		this.resultado = resultado;
	}

	public Boolean getPositivoPVivax() {
		return positivoPVivax;
	}

	public void setPositivoPVivax(Boolean positivoPVivax) {
		this.positivoPVivax = positivoPVivax;
	}

	public Boolean getPositivoPFalciparum() {
		return positivoPFalciparum;
	}

	public void setPositivoPFalciparum(Boolean positivoPFalciparum) {
		this.positivoPFalciparum = positivoPFalciparum;
	}

	public List<DensidadCruces> getDensidadCruces() {
		return densidadCruces;
	}

	public void setDensidadCruces(List<DensidadCruces> densidadCruces) {
		this.densidadCruces = densidadCruces;
	}

	/**
	 * @return the densidadPVivaxSelectedId
	 */
	public long getDensidadPVivaxSelectedId() {
		return densidadPVivaxSelectedId;
	}

	/**
	 * @param densidadPVivaxSelectedId the densidadPVivaxSelectedId to set
	 */
	public void setDensidadPVivaxSelectedId(long densidadPVivaxSelectedId) {
		this.densidadPVivaxSelectedId = densidadPVivaxSelectedId;
	}

	/**
	 * @return the densidadPVivaxSelected
	 */
	public DensidadCruces getDensidadPVivaxSelected() {
		return densidadPVivaxSelected;
	}

	/**
	 * @param densidadPVivaxSelected the densidadPVivaxSelected to set
	 */
	public void setDensidadPVivaxSelected(DensidadCruces densidadPVivaxSelected) {
		this.densidadPVivaxSelected = densidadPVivaxSelected;
	}

	/**
	 * @return the densidadPFalciparumSelectedId
	 */
	public long getDensidadPFalciparumSelectedId() {
		return densidadPFalciparumSelectedId;
	}

	/**
	 * @param densidadPFalciparumSelectedId the densidadPFalciparumSelectedId to set
	 */
	public void setDensidadPFalciparumSelectedId(long densidadPFalciparumSelectedId) {
		this.densidadPFalciparumSelectedId = densidadPFalciparumSelectedId;
	}

	/**
	 * @return the densidadPFalciparumSelected
	 */
	public DensidadCruces getDensidadPFalciparumSelected() {
		return densidadPFalciparumSelected;
	}

	/**
	 * @param densidadPFalciparumSelected the densidadPFalciparumSelected to set
	 */
	public void setDensidadPFalciparumSelected(
			DensidadCruces densidadPFalciparumSelected) {
		this.densidadPFalciparumSelected = densidadPFalciparumSelected;
	}

	public long getEstadioPFalciparumSelectedId() {
		return estadioPFalciparumSelectedId;
	}

	public void setEstadioPFalciparumSelectedId(long estadioPFalciparumSelectedId) {
		this.estadioPFalciparumSelectedId = estadioPFalciparumSelectedId;
	}

	public EstadioPFalciparum getEstadioPFalciparumSelected() {
		return estadioPFalciparumSelected;
	}

	public void setEstadioPFalciparumSelected(
			EstadioPFalciparum estadioPFalciparumSelected) {
		this.estadioPFalciparumSelected = estadioPFalciparumSelected;
	}

	public List<EstadioPFalciparum> getEstadiosPFalciparum() {
		return estadiosPFalciparum;
	}

	public void setEstadiosPFalciparum(List<EstadioPFalciparum> estadiosPFalciparum) {
		this.estadiosPFalciparum = estadiosPFalciparum;
	}

	public BigDecimal getEstadiosAsexuales() {
		return estadiosAsexuales;
	}

	public void setEstadiosAsexuales(BigDecimal estadiosAsexuales) {
		this.estadiosAsexuales = estadiosAsexuales;
	}

	public BigDecimal getGametocitos() {
		return gametocitos;
	}

	public void setGametocitos(BigDecimal gametocitos) {
		this.gametocitos = gametocitos;
	}

	public BigDecimal getLeucocitos() {
		return leucocitos;
	}

	public void setLeucocitos(BigDecimal leucocitos) {
		this.leucocitos = leucocitos;
	}

	public EntidadAdtva getEntidadLaboratorioSelected() {
		return entidadLaboratorioSelected;
	}

	/**
	 * @param entidadLaboratorioSelected the entidadLaboratorioSelected to set
	 */
	public void setEntidadLaboratorioSelected(
			EntidadAdtva entidadLaboratorioSelected) {
		this.entidadLaboratorioSelected = entidadLaboratorioSelected;
	}

	/**
	 * @return the entidadLaboratorioSelectedId
	 */
	public long getEntidadLaboratorioSelectedId() {
		return entidadLaboratorioSelectedId;
	}

	/**
	 * @param entidadLaboratorioSelectedId the entidadLaboratorioSelectedId to set
	 */
	public void setEntidadLaboratorioSelectedId(long entidadLaboratorioSelectedId) {
		this.entidadLaboratorioSelectedId = entidadLaboratorioSelectedId;
	}

	/**
	 * @return the entidadesLaboratorios
	 */
	public List<EntidadAdtva> getEntidadesLaboratorios() {
		return entidadesLaboratorios;
	}

	/**
	 * @param entidadesLaboratorios the entidadesLaboratorios to set
	 */
	public void setEntidadesLaboratorios(List<EntidadAdtva> entidadesLaboratorios) {
		this.entidadesLaboratorios = entidadesLaboratorios;
	}

	/**
	 * @return the laboratorista
	 */
	public String getLaboratorista() {
		return laboratorista;
	}

	/**
	 * @param laboratorista the laboratorista to set
	 */
	public void setLaboratorista(String laboratorista) {
		this.laboratorista = laboratorista;
	}

	/**
	 * @return the motivoFaltaDiagnosticoSelected
	 */
	public MotivoFaltaDiagnostico getMotivoFaltaDiagnosticoSelected() {
		return motivoFaltaDiagnosticoSelected;
	}

	/**
	 * @param motivoFaltaDiagnosticoSelected the motivoFaltaDiagnosticoSelected to set
	 */
	public void setMotivoFaltaDiagnosticoSelected(
			MotivoFaltaDiagnostico motivoFaltaDiagnosticoSelected) {
		this.motivoFaltaDiagnosticoSelected = motivoFaltaDiagnosticoSelected;
	}

	/**
	 * @return the tipoBusquedaSelected
	 */
	public TipoBusqueda getTipoBusquedaSelected() {
		return tipoBusquedaSelected;
	}

	/**
	 * @param tipoBusquedaSelected the tipoBusquedaSelected to set
	 */
	public void setTipoBusquedaSelected(TipoBusqueda tipoBusquedaSelected) {
		this.tipoBusquedaSelected = tipoBusquedaSelected;
	}

	/**
	 * @return the tiposBusquedas
	 */
	public List<TipoBusqueda> getTiposBusquedas() {
		return tiposBusquedas;
	}

	/**
	 * @param tiposBusquedas the tiposBusquedas to set
	 */
	public void setTiposBusquedas(List<TipoBusqueda> tiposBusquedas) {
		this.tiposBusquedas = tiposBusquedas;
	}

	public void setManzanaCodigo(String manzanaCodigo) {
		this.manzanaCodigo = manzanaCodigo;
	}

	public String getManzanaCodigo() {
		return manzanaCodigo;
	}

	public void setViviendaCodigo(String viviendaCodigo) {
		this.viviendaCodigo = viviendaCodigo;
	}

	public String getViviendaCodigo() {
		return viviendaCodigo;
	}

	public void setEmpleador(String empleador) {
		this.empleador = empleador;
	}

	public String getEmpleador() {
		return empleador;
	}

	public void setUnidadLaboratorioSelected(Unidad unidadLaboratorioSelected) {
		this.unidadLaboratorioSelected = unidadLaboratorioSelected;
	}

	public Unidad getUnidadLaboratorioSelected() {
		return unidadLaboratorioSelected;
	}

	public void setManzanas(LazyDataModel<Manzana> manzanas) {
		this.manzanas = manzanas;
	}

	public LazyDataModel<Manzana> getManzanas() {
		return manzanas;
	}

	public void setViviendas(LazyDataModel<ViviendaUltimaEncuesta> viviendas) {
		this.viviendas = viviendas;
	}

	public LazyDataModel<ViviendaUltimaEncuesta> getViviendas() {
		return viviendas;
	}

	public void setNumViviendas(int numViviendas) {
		this.numViviendas = numViviendas;
	}

	public int getNumViviendas() {
		return numViviendas;
	}

	public void setNumManzanas(int numManzanas) {
		this.numManzanas = numManzanas;
	}

	public int getNumManzanas() {
		return numManzanas;
	}

	public void setViviendaUltimaEncuestaSelected(
			ViviendaUltimaEncuesta viviendaUltimaEncuestaSelected) {
		this.viviendaUltimaEncuestaSelected = viviendaUltimaEncuestaSelected;
	}

	public ViviendaUltimaEncuesta getViviendaUltimaEncuestaSelected() {
		return viviendaUltimaEncuestaSelected;
	}

	public void setMuestreoPruebaRapidaSelected(MuestreoPruebaRapida muestreoPruebaRapidaSelected) {
		this.muestreoPruebaRapidaSelected = muestreoPruebaRapidaSelected;
	}

	public MuestreoPruebaRapida getMuestreoPruebaRapidaSelected() {
		return muestreoPruebaRapidaSelected;
	}

	public void setMotivoFaltaDiagnosticoSelectedId(
			long motivoFaltaDiagnosticoSelectedId) {
		this.motivoFaltaDiagnosticoSelectedId = motivoFaltaDiagnosticoSelectedId;
	}

	public long getMotivoFaltaDiagnosticoSelectedId() {
		return motivoFaltaDiagnosticoSelectedId;
	}

	public void setMotivosFaltaDiagnosticos(List<MotivoFaltaDiagnostico> motivosFaltaDiagnosticos) {
		this.motivosFaltaDiagnosticos = motivosFaltaDiagnosticos;
	}

	public List<MotivoFaltaDiagnostico> getMotivosFaltaDiagnosticos() {
		return motivosFaltaDiagnosticos;
	}

	public void setMuestreoDiagnosticoSelected(
			MuestreoDiagnostico muestreoDiagnosticoSelected) {
		this.muestreoDiagnosticoSelected = muestreoDiagnosticoSelected;
	}

	public MuestreoDiagnostico getMuestreoDiagnosticoSelected() {
		return muestreoDiagnosticoSelected;
	}

	public void setNombreColVol(String nombreColVol) {
		this.nombreColVol = nombreColVol;
	}

	public String getNombreColVol() {
		return nombreColVol;
	}

}