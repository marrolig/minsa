// -----------------------------------------------
// MuestreoHematicoBean.java
// -----------------------------------------------

package ni.gob.minsa.malaria.interfaz.vigilancia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.ejbPersona.dto.Persona;
import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.datos.estructura.UnidadDA;
import ni.gob.minsa.malaria.datos.general.CatalogoElementoDA;
import ni.gob.minsa.malaria.datos.general.MarcadorDA;
import ni.gob.minsa.malaria.datos.general.ParametroDA;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadDA;
import ni.gob.minsa.malaria.datos.poblacion.DivisionPoliticaDA;
import ni.gob.minsa.malaria.datos.poblacion.ManzanaDA;
import ni.gob.minsa.malaria.datos.poblacion.ViviendaDA;
import ni.gob.minsa.malaria.datos.sis.SisPersonaDA;
import ni.gob.minsa.malaria.datos.vigilancia.ColVolDA;
import ni.gob.minsa.malaria.datos.vigilancia.MuestreoHematicoDA;
import ni.gob.minsa.malaria.datos.vigilancia.PuestoNotificacionDA;
import ni.gob.minsa.malaria.interfaz.sis.PersonaBean;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Manzana;
import ni.gob.minsa.malaria.modelo.poblacion.Vivienda;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.ViviendaUltimaEncuesta;
import ni.gob.minsa.malaria.modelo.sis.Etnia;
import ni.gob.minsa.malaria.modelo.sis.Sexo;
import ni.gob.minsa.malaria.modelo.vigilancia.DensidadCruces;
import ni.gob.minsa.malaria.modelo.vigilancia.EstadioPFalciparum;
import ni.gob.minsa.malaria.modelo.vigilancia.MotivoFaltaDiagnostico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoDiagnostico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoHematico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoPruebaRapida;
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
import ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService;
import ni.gob.minsa.malaria.servicios.poblacion.ManzanaService;
import ni.gob.minsa.malaria.servicios.poblacion.ViviendaService;
import ni.gob.minsa.malaria.servicios.sis.SisPersonaService;
import ni.gob.minsa.malaria.servicios.vigilancia.ColVolService;
import ni.gob.minsa.malaria.servicios.vigilancia.MuestreoHematicoService;
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
	private boolean autorizado;
	
	// -------------------------------------------------------------
	// Estado
	// 
	// El atributo estado indica la situación en que se encuentra
	// una ficha de muestreo hemático y por tanto es la variable que 
	// gestionará el bloqueo y y peticiones desde el interfaz al bean.
	//
	// Valores:
	// 0 : Ficha nueva
	// 1 : Ficha existente sin investigación epidemiológica asociada.  
	// 2 : Ficha existente con investigación epidemiológica abierta sin confirmación.  Pueden modificarse todos los datos incluyendo el diagnóstico de gota gruesa, 	siempre y cuando no exista declaración de confirmación por SILAIS o confirmación por CNDR.
	//     existe M10, CasoCerrado=False y ConfirmacionCNDR y ConfirmacionSILAIS igual a IMDIAG|P
	// 3 : Ficha existente con investigación epidemiológica abierta con confirmación de diagnóstico.  Pueden modificarse los datos pero se excluye los resultados de gota gruesa.
	//     existe M10, CasoCerrado=False y ConfirmacionCNDR o ConfirmacionSILAIS diferente a IMDIAG|P
	// 4 : Ficha existente con investigación epidemiológica cerrada.  No se pueden modificar los datos.
    //     existe M10 y CasoCerrado=True

	// -------------------------------------------------------------
	private int estado;
	
	// si el puesto se encuentra activo, se pueden agregar láminas
	private boolean puestoActivo;
	private long muestreoHematicoId;
	private MuestreoHematico muestreoHematicoSelected;
	private MuestreoHematico muestreoHematicoSelectedTmp;
	private LazyDataModel<MuestreoHematico> muestreosHematicos;
	private boolean peticionLista;
	private String filtroPaciente;
	private int numRegistros;
	
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
	private PuestoNotificacion puestoNotificacionSelected;
	private String filtroColVol;
	private String nombreColVol;
	
	// atributos vinculados a la identificación de la ficha E-2
	private String clave;
	private BigDecimal numeroLamina;
	
	// año epidemiológico utilizado para el filtro de muestreos hemáticos
	// al momento de abrir la ventana modal para seleccionar una ficha E-2
	// existente
    private List<SelectItem> aniosEpidemiologicos;
    private Integer anioEpiSelected=0;
    
    // la situación corresponde al estado de la ficha E2 que ha sido seleccionado
	// para llenar la grilla en la ventana modal
	private Integer situacion;
	
	private List<TipoBusqueda> tiposBusquedas;
	
	// atributos vinculados a la persona a la cual se le realiza el muestreo
	// y que son almacenados en la ficha de muestreo y por tanto son inmutables
	// independientemente si posteriormente, por otro vía, se realizan modificaciones
	// a los datos de la persona.  Para que esas modificaciones sean efectivas
	// deben ser realizadas desde la misma ficha.

	private long personaId;
	
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

	private String tmpPersonaReferente;
	private String tmpTelefonoReferente;
	
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
	
	private Boolean positivoPVivax;
	private Boolean positivoPFalciparum;
	
	private List<DensidadCruces> densidadCruces;
	
	private long densidadPVivaxSelectedId;
	private long densidadPFalciparumSelectedId;
	
	private long estadioPFalciparumSelectedId;
	private List<EstadioPFalciparum> estadiosPFalciparum;
	
	private long entidadLaboratorioSelectedId;
	private List<EntidadAdtva> entidadesLaboratorios;
	
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
	private static DivisionPoliticaService divisionService = new DivisionPoliticaDA();
	private static UnidadService unidadService = new UnidadDA();
	private static ComunidadService comunidadService = new ComunidadDA();
	private static MarcadorService marcadorService = new MarcadorDA();
	private static ParametroService parametroService = new ParametroDA();

	private static CatalogoElementoService<ResponsablePruebaRapida,Integer> responsablePruebaRapidaService=new CatalogoElementoDA(ResponsablePruebaRapida.class,"ResponsablePruebaRapida");
	private static CatalogoElementoService<ResultadoPruebaRapida,Integer> resultadoPruebaRapidaService=new CatalogoElementoDA(ResultadoPruebaRapida.class,"ResultadoPruebaRapida");

	private static CatalogoElementoService<DensidadCruces,Integer> densidadCrucesService=new CatalogoElementoDA(DensidadCruces.class,"DensidadCruces");
	private static CatalogoElementoService<EstadioPFalciparum,Integer> estadioPFalciparumService=new CatalogoElementoDA(EstadioPFalciparum.class,"EstadioPFalciparum");
	private static CatalogoElementoService<MotivoFaltaDiagnostico,Integer> motivoFaltaDiagnosticoService=new CatalogoElementoDA(MotivoFaltaDiagnostico.class,"MotivoFaltaDiagnostico");
	private static CatalogoElementoService<Sexo,Integer> sexoService=new CatalogoElementoDA(Sexo.class,"Sexo");
	private static CatalogoElementoService<Etnia,Integer> etniaService=new CatalogoElementoDA(Etnia.class,"Etnia");
	
	private static MuestreoHematicoService muestreoHematicoService = new MuestreoHematicoDA();
	private static PuestoNotificacionService puestoNotificacionService = new PuestoNotificacionDA();
	private static ManzanaService manzanaService = new ManzanaDA();
	private static ViviendaService viviendaService = new ViviendaDA();
	
	private static EntidadAdtvaService entidadAdtvaService = new EntidadAdtvaDA();
	private static SisPersonaService sisPersonaService = new SisPersonaDA();
	
	public MuestreoHematicoBean() {
		
		this.capaActiva=1;
		this.estado=0;
		
		this.situacion=1;
		this.anioEpiSelected=Integer.valueOf(CalendarioEPI.año(Calendar.getInstance().getTime()));
			
		this.peticionLista=false;
		this.muestreoHematicoSelected=new MuestreoHematico();
		
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
		this.entidades=ni.gob.minsa.malaria.reglas.Operacion.entidadesAutorizadas(this.infoSesion.getUsuarioId(),null);
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
		iniciarCapaPRM();
		iniciarCapaPGG();

		this.muestreosHematicos = new LazyDataModel<MuestreoHematico>() {

			private static final long serialVersionUID = 1L;

			@Override
		    public List<MuestreoHematico> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
			
				// si la unidad de salud no ha sido seleccionada y no se ha realizado 
				// la petición de consulta no puede efectuarse la carga.  La petición de consulta
				// se utiliza para evitar la carga por cada cambio de unidad
				if (unidadSelectedId==0) {
					return null;
				}
				
				List<MuestreoHematico> muestreosList=null;
				numRegistros=0;
			
				if (colVolPuestoSelected==null) {
					numRegistros=muestreoHematicoService.ContarPorUnidad(unidadSelectedId, anioEpiSelected, situacion);
					muestreosList=muestreoHematicoService.ListarPorUnidad(unidadSelectedId, anioEpiSelected, situacion, first, pageSize,numRegistros);

					if (!(muestreosList!=null && muestreosList.size()>0)) {
						FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "No se encontraron fichas de muestreo hemático asociadas a la unidad de salud","");
						if (msg!=null)
							FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					this.setRowCount(numRegistros);
					return muestreosList;
				} else {
					numRegistros=muestreoHematicoService.ContarPorPuesto(colVolPuestoSelected.getPuestoNotificacionId(), anioEpiSelected, situacion);
					muestreosList=muestreoHematicoService.ListarPorPuesto(colVolPuestoSelected.getPuestoNotificacionId(), anioEpiSelected, situacion, first, pageSize,numRegistros);

					if (!(muestreosList!=null && muestreosList.size()>0)) {
						FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "No se encontraron fichas de muestreo hemático asociadas a la clave especificada","");
						if (msg!=null)
							FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					this.setRowCount(numRegistros);
					return muestreosList;
				}
			}

		};
		
		this.muestreosHematicos.setRowCount(numRegistros);

	}
	
	/**
	 * Selecciona un registro de muestreo hemático de la grilla
	 */
	public void aceptarMuestreo() {
		
		// Establece nuevamente el objeto muestreo seleccionado para desacoplar los dos objetos
		this.muestreoHematicoSelected=(MuestreoHematico)(muestreoHematicoService.Encontrar(this.muestreoHematicoSelectedTmp.getMuestreoHematicoId())).getObjeto();
		this.estado=this.situacion;
		this.numeroLamina=this.muestreoHematicoSelected.getNumeroLamina();
		this.muestreoHematicoSelectedTmp=null;
		cargarMuestreo();
		this.puestoNotificacionId=this.muestreoHematicoSelected.getPuestoNotificacion().getPuestoNotificacionId();
		this.puestoNotificacionSelected=this.muestreoHematicoSelected.getPuestoNotificacion();
		this.colVolPuestoSelected=null;
		if (this.muestreoHematicoSelected.getPuestoNotificacion().getColVol()!=null) {
			this.colVolPuestoSelected=new ColVolPuesto();
			this.colVolPuestoSelected.setClave(clave);
			this.colVolPuestoSelected.setNombreColVol(this.muestreoHematicoSelected.getPuestoNotificacion().getColVol().getSisPersona().getNombreCompleto());
			this.colVolPuestoSelected.setPuestoNotificacionId(this.muestreoHematicoSelected.getPuestoNotificacion().getPuestoNotificacionId());
		}
		iniciarVarColVol();
		this.clave=this.muestreoHematicoSelected.getClave();
		
	}
	
	/**
	 * Genera los años epidemiológicos en los cuales existen muestreos hemáticos independientemente de
	 * la situación de dicho muestreo, tomando siempre como año epidemiológico actual el año epidemiológico
	 * calculado en base a la fecha actual y verificando la existencia de muestreos 2 años atrás.
	 */
	public void generarAnios() {
		
        this.aniosEpidemiologicos = new LinkedList<SelectItem>();
        Integer anioActual = Integer.valueOf(CalendarioEPI.año(Calendar.getInstance().getTime()));
        this.anioEpiSelected=anioActual;
        List<Integer> oAños = null;
        
        if (this.colVolPuestoSelected==null) {
        	oAños = muestreoHematicoService.ListarAñosConMuestreoUnidad(this.unidadSelectedId,Integer.valueOf(anioActual.intValue()-2));
        } else {
        	oAños = muestreoHematicoService.ListarAñosConMuestreoPuesto(this.puestoNotificacionId,Integer.valueOf(anioActual.intValue()-2));
        }
        
        // incluye el año epidemiológico actual por omisión
        this.aniosEpidemiologicos.add(new SelectItem(anioActual,anioActual.toString()));
        
        if ((oAños!=null) && (!oAños.isEmpty())){
        	for(Integer año:oAños){
        		if (!año.equals(anioActual)) {
        			this.aniosEpidemiologicos.add(new SelectItem(año,año.toString()));
        		}
        	} 
        }
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
		this.muestreoHematicoSelected=new MuestreoHematico();

	}
	
	public void iniciarCapaPaciente() {
		
		iniciarPersonas();
	}
		
	public void iniciarCapaReferente() {
		
//		this.personaReferente=null;
//		this.telefonoReferente=null;
		
	}
	
	public void iniciarCapaPRM() {
		
		this.responsablesPruebasRapidas=responsablePruebaRapidaService.ListarActivos();
		this.resultadosPruebasRapidas=resultadoPruebaRapidaService.ListarActivos();
		iniciarPRM();
		
	}
	
	public void iniciarCapaPGG() {
		
		this.densidadCruces=densidadCrucesService.ListarActivos();
		this.densidadPVivaxSelectedId=0;
		this.densidadPFalciparumSelectedId=0;
		this.estadioPFalciparumSelectedId=0;
		this.estadiosPFalciparum=estadioPFalciparumService.ListarActivos();
		
		this.entidadesLaboratorios=entidadService.EntidadesAdtvasActivas();
		this.entidadLaboratorioSelectedId=this.entidadSelectedId;
		
	}
	
	public void iniciarPersonas() {

		this.personaId=0;
		this.personaSelected=null;
		
		// inicializa las variables del componente de personas
		FacesContext context = FacesContext.getCurrentInstance();
		PersonaBean personaBean = (PersonaBean)context.getApplication().evaluateExpressionGet(context, "#{personaBean}", PersonaBean.class);
		personaBean.setPersonaSelected(null);
		personaBean.setTextoBusqueda("");
		personaBean.setPersonaListaSelected(null);
		personaBean.setModo(0);
		personaBean.limpiaDetallePersona();

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
				this.manzanaCodigo=this.viviendaSelected.getViviendaManzana().getManzana().getCodigo().substring(9, 12);
			} else {
				this.manzanaCodigo=null;
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
	
	public void quitarInicioSintomas() {
		
		this.muestreoHematicoSelected.setInicioSintomas(null);
	}
	
	public void cambiarTratamiento() {
		
		if (this.muestreoHematicoSelected.getInicioTratamiento()==null) {
			iniTratamiento();
		}
	}
	
	public void quitarTratamiento() {
		
		this.muestreoHematicoSelected.setInicioTratamiento(null);
		iniTratamiento();
	}
	
	public void iniTratamiento() {

		this.muestreoHematicoSelected.setFinTratamiento(null);
		this.muestreoHematicoSelected.setCloroquina(null);
		this.muestreoHematicoSelected.setPrimaquina15mg(null);
		this.muestreoHematicoSelected.setPrimaquina5mg(null);
		this.muestreoHematicoSelected.setTratamientoEnBoca(null);
		this.muestreoHematicoSelected.setTratamientoRemanente(null);
		
	}
	
	public void quitarFinTratamiento() {
		
		this.muestreoHematicoSelected.setFinTratamiento(null);
		
	}
	
	public void editarReferente(ActionEvent pEvento) {
		
		this.tmpPersonaReferente=this.muestreoHematicoSelected.getPersonaReferente();
		this.tmpTelefonoReferente=this.muestreoHematicoSelected.getTelefonoReferente();
		
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
			this.muestreoHematicoSelected.setPersonaReferente(this.tmpPersonaReferente);
			this.muestreoHematicoSelected.setTelefonoReferente(this.tmpTelefonoReferente);
		}
	}

	public void eliminarReferente() {

		this.muestreoHematicoSelected.setPersonaReferente(null);
		this.muestreoHematicoSelected.setTelefonoReferente(null);
		this.tmpPersonaReferente=null;
		this.tmpTelefonoReferente=null;
		
	}

	public void aceptarPRM() {

		RequestContext rContext = RequestContext.getCurrentInstance();

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
		
		this.muestreoPruebaRapidaSelected.setResultado(this.resultadoPruebaRapidaSelected);
		this.muestreoPruebaRapidaSelected.setRealizado(this.responsablePruebaRapidaSelected);
		InfoResultado oResultado=VigilanciaValidacion.validarPRM(this.muestreoPruebaRapidaSelected);
		
		if (!oResultado.isOk()) {
			rContext.addCallbackParam("prmValida", false);
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, oResultado.getMensaje(),"");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			rContext.addCallbackParam("prmValida", true);
			
			// si muestreoPrubaRapidaSelected es nula, implica que se trata de un nuevo
			// valor para la prueba rápida
			if (this.muestreoHematicoSelected.getPruebaRapida()==null) {
				this.muestreoHematicoSelected.setPruebaRapida(new MuestreoPruebaRapida());
				this.muestreoPruebaRapidaSelected.setUsuarioRegistro(this.infoSesion.getUsername());
				this.muestreoPruebaRapidaSelected.setFechaRegistro(Calendar.getInstance().getTime());
			}
			this.muestreoHematicoSelected.getPruebaRapida().setFecha(this.muestreoPruebaRapidaSelected.getFecha());
			this.muestreoHematicoSelected.getPruebaRapida().setRealizado(this.responsablePruebaRapidaSelected);
			this.muestreoHematicoSelected.getPruebaRapida().setResultado(this.resultadoPruebaRapidaSelected);
		}
	}

	public void cambiarResultado() {
	
		// si el resultado es nulo, es evaluado cuando se efectúa
		// una llamada interna y no desde el interfaz
		
		if (this.muestreoDiagnosticoSelected.getResultado()==null) {
			this.muestreoDiagnosticoSelected.setPositivoPFalciparum(null);
			this.muestreoDiagnosticoSelected.setPositivoPVivax(null);
			this.positivoPVivax=null;
			this.positivoPFalciparum=null;
		}
		
		if (this.muestreoDiagnosticoSelected.getResultado()!=null && this.muestreoDiagnosticoSelected.getResultado().equals(Utilidades.NEGATIVO)) {
			this.muestreoDiagnosticoSelected.setPositivoPFalciparum(BigDecimal.ZERO);
			this.muestreoDiagnosticoSelected.setPositivoPVivax(BigDecimal.ZERO);
			this.positivoPVivax=Boolean.FALSE;
			this.positivoPFalciparum=Boolean.FALSE;
		}
		
		// incluye si es nulo y negativo
		if (this.muestreoDiagnosticoSelected.getResultado()==null || this.muestreoDiagnosticoSelected.getResultado().equals(Utilidades.NEGATIVO)) {
			this.muestreoDiagnosticoSelected.setDensidadPFalciparum(null);
			this.muestreoDiagnosticoSelected.setDensidadPVivax(null);
			this.densidadPFalciparumSelectedId=0;
			this.densidadPVivaxSelectedId=0;
			this.muestreoDiagnosticoSelected.setEstadioPFalciparum(null);
			this.estadioPFalciparumSelectedId=0;
			this.muestreoDiagnosticoSelected.setGametocitos(null);
			this.muestreoDiagnosticoSelected.setLeucocitos(null);
			this.muestreoDiagnosticoSelected.setEstadiosAsexuales(null);
		}
	}
	
	public void controlarRecepcion() {
		
		if (this.muestreoDiagnosticoSelected.getFechaRecepcion()==null) {
			this.muestreoDiagnosticoSelected.setFechaDiagnostico(null);
			controlarDiagnostico();

			this.muestreoDiagnosticoSelected.setEntidadAdtvaLaboratorio(null);
			this.entidadLaboratorioSelectedId=0;
			this.muestreoDiagnosticoSelected.setUnidadLaboratorio(null);
			this.muestreoDiagnosticoSelected.setLaboratorista(null);
		}
	}
	
	public void controlarDiagnostico() {
		
		if (this.muestreoDiagnosticoSelected.getFechaDiagnostico()==null) {
			this.muestreoDiagnosticoSelected.setResultado(null);
			cambiarResultado();
		} else {
			this.muestreoDiagnosticoSelected.setMotivoFaltaDiagnostico(null);
			this.motivoFaltaDiagnosticoSelectedId=0;
		}
	}
	
	public void controlarVivax() {
		
		if (!this.positivoPVivax) {
			this.muestreoDiagnosticoSelected.setPositivoPVivax(BigDecimal.ZERO);
			this.muestreoDiagnosticoSelected.setDensidadPVivax(null);
			this.densidadPVivaxSelectedId=0;
		}
	}

	public void controlarFalciparum() {
		
		if (!this.positivoPFalciparum) {
			this.muestreoDiagnosticoSelected.setPositivoPFalciparum(BigDecimal.ZERO);
			this.muestreoDiagnosticoSelected.setDensidadPFalciparum(null);
			this.densidadPFalciparumSelectedId=0;
			this.muestreoDiagnosticoSelected.setEstadioPFalciparum(null);
			this.estadioPFalciparumSelectedId=0;
		}
	}

	public void aceptarPGG() {

		RequestContext rContext = RequestContext.getCurrentInstance();

		if (this.muestreoDiagnosticoSelected.getResultado()==null || this.muestreoDiagnosticoSelected.getResultado().equals(Utilidades.NEGATIVO)) {
			this.muestreoDiagnosticoSelected.setPositivoPVivax(null);
			this.muestreoDiagnosticoSelected.setPositivoPFalciparum(null);
		} else {
			this.muestreoDiagnosticoSelected.setPositivoPVivax(this.positivoPVivax?Utilidades.POSITIVO:Utilidades.NEGATIVO);
			this.muestreoDiagnosticoSelected.setPositivoPFalciparum(this.positivoPFalciparum?Utilidades.POSITIVO:Utilidades.NEGATIVO);
		}
		
		if (this.densidadPVivaxSelectedId==0) {
			this.muestreoDiagnosticoSelected.setDensidadPVivax(null);
		} else {
			InfoResultado oResultado=densidadCrucesService.Encontrar(this.densidadPVivaxSelectedId);
			if (oResultado.isOk()) {
				this.muestreoDiagnosticoSelected.setDensidadPVivax((DensidadCruces) oResultado.getObjeto());
			} else {
				rContext.addCallbackParam("pggValida", false);
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "La densidad en cruces que ha seleccionado no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return; 
			}
		}
		
		if (this.densidadPFalciparumSelectedId==0) {
			this.muestreoDiagnosticoSelected.setDensidadPFalciparum(null);
		} else {
			InfoResultado oResultado=densidadCrucesService.Encontrar(this.densidadPFalciparumSelectedId);
			if (oResultado.isOk()) {
				this.muestreoDiagnosticoSelected.setDensidadPFalciparum((DensidadCruces) oResultado.getObjeto());
			} else {
				rContext.addCallbackParam("pggValida", false);
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "La densidad en cruces que ha seleccionado no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return; 
			}
		}
		
		if (this.estadioPFalciparumSelectedId==0) {
			this.muestreoDiagnosticoSelected.setEstadioPFalciparum(null);
		} else {
			InfoResultado oResultado=estadioPFalciparumService.Encontrar(this.estadioPFalciparumSelectedId);
			if (oResultado.isOk()) {
				this.muestreoDiagnosticoSelected.setEstadioPFalciparum((EstadioPFalciparum) oResultado.getObjeto());
			} else {
				rContext.addCallbackParam("pggValida", false);
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "El estadío seleccionado para el P.falciparum no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return; 
			}
		}
		
		if (this.entidadLaboratorioSelectedId==0) {
			this.muestreoDiagnosticoSelected.setEntidadAdtvaLaboratorio(null);
		} else {
			InfoResultado oResultado=entidadAdtvaService.Encontrar(this.entidadLaboratorioSelectedId);
			if (oResultado.isOk()) {
				this.muestreoDiagnosticoSelected.setEntidadAdtvaLaboratorio((EntidadAdtva) oResultado.getObjeto());
			} else {
				rContext.addCallbackParam("pggValida", false);
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "La entidad administrativa seleccionada no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return; 
			}			
		}
		this.muestreoDiagnosticoSelected.setMunicipioLaboratorio(this.muestreoDiagnosticoSelected.getUnidadLaboratorio().getMunicipio());
		
		if (this.motivoFaltaDiagnosticoSelectedId==0) {
			this.muestreoDiagnosticoSelected.setMotivoFaltaDiagnostico(null);
		} else {
			InfoResultado oResultado=motivoFaltaDiagnosticoService.Encontrar(this.motivoFaltaDiagnosticoSelectedId);
			if (oResultado.isOk()) {
				this.muestreoDiagnosticoSelected.setMotivoFaltaDiagnostico((MotivoFaltaDiagnostico) oResultado.getObjeto());
			} else {
				rContext.addCallbackParam("pggValida", false);
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "El motivo declarado por la ausencia de diagnóstico no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
		}
		
		InfoResultado oResultado=VigilanciaValidacion.validarPGG(this.muestreoDiagnosticoSelected);
		
		if (!oResultado.isOk()) {
			rContext.addCallbackParam("pggValida", false);
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, oResultado.getMensaje(),"");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			rContext.addCallbackParam("pggValida", true);
			
			// si muestreoDiagnosticoSelectedId es cero, implica que se trata de un nuevo
			// valor para la prueba de gota gruesa
			if (this.muestreoDiagnosticoSelected.getMuestreoDiagnosticoId()==0) {
				this.muestreoDiagnosticoSelected.setUsuarioRegistro(this.infoSesion.getUsername());
				this.muestreoDiagnosticoSelected.setFechaRegistro(Calendar.getInstance().getTime());
			}
			this.muestreoDiagnosticoSelected.setMunicipioLaboratorio(this.muestreoDiagnosticoSelected.getUnidadLaboratorio().getMunicipio());

			if (this.muestreoHematicoSelected.getDiagnostico()==null) {
				this.muestreoHematicoSelected.setDiagnostico(new MuestreoDiagnostico());
			}
			this.muestreoHematicoSelected.getDiagnostico().setDensidadPFalciparum(this.muestreoDiagnosticoSelected.getDensidadPFalciparum());
			this.muestreoHematicoSelected.getDiagnostico().setDensidadPVivax(this.muestreoDiagnosticoSelected.getDensidadPVivax());
			this.muestreoHematicoSelected.getDiagnostico().setEntidadAdtvaLaboratorio(this.muestreoDiagnosticoSelected.getEntidadAdtvaLaboratorio());
			this.muestreoHematicoSelected.getDiagnostico().setEstadioPFalciparum(this.muestreoDiagnosticoSelected.getEstadioPFalciparum());
			this.muestreoHematicoSelected.getDiagnostico().setEstadiosAsexuales(this.muestreoDiagnosticoSelected.getEstadiosAsexuales());
			this.muestreoHematicoSelected.getDiagnostico().setFechaDiagnostico(this.muestreoDiagnosticoSelected.getFechaDiagnostico());
			this.muestreoHematicoSelected.getDiagnostico().setFechaRecepcion(this.muestreoDiagnosticoSelected.getFechaRecepcion());
			this.muestreoHematicoSelected.getDiagnostico().setGametocitos(this.muestreoDiagnosticoSelected.getGametocitos());
			this.muestreoHematicoSelected.getDiagnostico().setLaboratorista(this.muestreoDiagnosticoSelected.getLaboratorista());
			this.muestreoHematicoSelected.getDiagnostico().setLeucocitos(this.muestreoDiagnosticoSelected.getLeucocitos());
			this.muestreoHematicoSelected.getDiagnostico().setMotivoFaltaDiagnostico(this.muestreoDiagnosticoSelected.getMotivoFaltaDiagnostico());
			this.muestreoHematicoSelected.getDiagnostico().setMunicipioLaboratorio(this.muestreoDiagnosticoSelected.getMunicipioLaboratorio());
			this.muestreoHematicoSelected.getDiagnostico().setPositivoPFalciparum(this.muestreoDiagnosticoSelected.getPositivoPFalciparum());
			this.muestreoHematicoSelected.getDiagnostico().setPositivoPVivax(this.muestreoDiagnosticoSelected.getPositivoPVivax());
			this.muestreoHematicoSelected.getDiagnostico().setResultado(this.muestreoDiagnosticoSelected.getResultado());
			this.muestreoHematicoSelected.getDiagnostico().setUnidadLaboratorio(this.muestreoDiagnosticoSelected.getUnidadLaboratorio());

		}
	}
	
	/**
	 * Se ejecuta cuando el usuario directamente modifica la clave del puesto de
	 * notificación.  El sistema debe verificar si la clave corresponde a un colaborador
	 * voluntario o a una unidad de salud, en cuyo caso debe también verificar si ambos
	 * están asociados a una unidad de salud autorizada para el usuario.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void cambiarClave() {

		if (this.clave==null || this.clave.trim().isEmpty()) return;
		
		RequestContext rContext = RequestContext.getCurrentInstance();
		Collection iComponentes = Arrays.asList("grwMensaje","txtClave");

		this.colVolPuestoSelected=null;
		this.unidadSelected=null;
		this.unidadSelectedId=0;
		this.nombreColVol=null;
		this.puestoNotificacionId=0;
		this.clave=this.clave.trim();
		
		if (this.entidadSelectedId==0) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "Debe especificar una entidad administrativa","Si la lista desplegable está vacía significa que no está autorizado a ninguna unidad de salud o entidad administrativa");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.clave=null;
			rContext.update(iComponentes);
			return; 
		}
		
		if (this.clave.isEmpty()) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "Debe especificar una clave válida","Verifique si la clave se encuentra definida y si ésta se encuentra activa o ha dejado de serlo en un período de 12 meses antes de la fecha actual");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.clave=null;
			rContext.update(iComponentes);
			return; 
		}

		// verifica si la clave pertenece a un puesto de notificación activo
		PuestoNotificacion oPuestoNotificacion = puestoNotificacionService.EncontrarPorClave(this.clave,this.entidadSelectedId);
		if (oPuestoNotificacion==null) {
			// si la clave no se encuentra activa es posible que tenga muestreos asociados
			// pero no puede registrar nuevos muestras
			
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "La clave " + this.clave + " especificada no corresponde a ningún puesto de notificación activo","Verifique que la clave indicada sea correcta o solicite la asignación de la clave a la unidad o colaborador voluntario.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.clave=null;
			rContext.update(iComponentes);
			return; 
		}
		
		// verifica si la unidad de salud asociada al puesto de notificación se encuentra autorizada al usuario
		// las unidades autorizadas forman parte de la lista del combo
		
		if (oPuestoNotificacion.getUnidad()!=null) {
			// implica que la unidad de salud es el puesto de notificación
			boolean enLista=false;
			for (Unidad oUnidad:this.unidades) {
				if (oUnidad.getUnidadId()==oPuestoNotificacion.getUnidad().getUnidadId()) {
					enLista=true;
					break;
				}
			}

			if (!enLista) {
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "La clave no corresponde a una unidad de salud autorizada al usuario","Notifique al administrador del sistema para obtener dicha autorización");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				this.clave=null;
				rContext.update(iComponentes);
				return; 
			}
			
			this.autorizado=Operacion.esUnidadAutorizada(this.infoSesion.getUsuarioId(), oPuestoNotificacion.getUnidad().getUnidadId());
			this.unidadSelected=oPuestoNotificacion.getUnidad();
			this.unidadSelectedId=this.unidadSelected.getUnidadId();
			this.puestoNotificacionId=oPuestoNotificacion.getPuestoNotificacionId();
			this.puestoNotificacionSelected=oPuestoNotificacion;
			this.entidadSelectedId=this.unidadSelected.getEntidadAdtva().getEntidadAdtvaId();
			
			if (this.numeroLamina!=null) {
				comprobarLamina();
			} else {
				rContext.update(iComponentes);
			}
			return;
			
		} else {
			// implica que el puesto de notificación corresponde a un colaborador voluntario

			boolean enLista=false;
			for (Unidad oUnidad:this.unidades) {
				if (oUnidad.getUnidadId()==oPuestoNotificacion.getColVol().getUnidad().getUnidadId()) {
					enLista=true;
					break;
				}
			}

			if (!enLista) {
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "La clave no corresponde a una unidad de salud autorizada al usuario","Notifique al administrador del sistema para obtener dicha autorización");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				this.clave=null;
				rContext.update(iComponentes);
				return; 
			}

			this.autorizado=Operacion.esUnidadAutorizada(this.infoSesion.getUsuarioId(), oPuestoNotificacion.getColVol().getUnidad().getUnidadId());
			this.unidadSelected=oPuestoNotificacion.getColVol().getUnidad();
			this.unidadSelectedId=this.unidadSelected.getUnidadId();
			this.nombreColVol=oPuestoNotificacion.getColVol().getSisPersona().getNombreCompleto();
			this.puestoNotificacionId=oPuestoNotificacion.getPuestoNotificacionId();
			this.puestoNotificacionSelected=oPuestoNotificacion;
			this.entidadSelectedId=this.unidadSelected.getEntidadAdtva().getEntidadAdtvaId();

			ColVolPuesto oColVolPuesto = new ColVolPuesto();
			oColVolPuesto.setClave(oPuestoNotificacion.getClave());
			oColVolPuesto.setNombreColVol(oPuestoNotificacion.getColVol().getSisPersona().getNombreCompleto());
			oColVolPuesto.setPuestoNotificacionId(oPuestoNotificacion.getPuestoNotificacionId());
			oColVolPuesto.setFechaApertura(oPuestoNotificacion.getFechaApertura());
			oColVolPuesto.setFechaCierre(oPuestoNotificacion.getFechaCierre());
			this.colVolPuestoSelected=oColVolPuesto;

			if (this.numeroLamina!=null) {
				comprobarLamina();
			} else {
				rContext.update(iComponentes);
			}
			return;

		}
		
		
	}

	public void cambiarUnidad() {
		
		cancelarLamina();
		
		this.clave=null;
		this.colVolPuestoSelected=null;
		this.nombreColVol=null;
		this.puestoNotificacionId=0;
		this.puestoNotificacionSelected=null;
		this.colVolPuestoSelected=null;

		if (this.unidadSelectedId==0) {
			this.autorizado=false;
			return;
		}
		
		InfoResultado oResultado=unidadService.Encontrar(this.unidadSelectedId);
		if (!oResultado.isOk()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(oResultado));
			return;
		}

		Unidad oUnidad=(Unidad)oResultado.getObjeto();
		this.unidadSelected=oUnidad;
		
		// si el resultado oPuestoUnidad es nulo, implica que la unidad no es puesto
		// de notificación y solamente coordina a los colaboradores voluntarios para
		// la recepción de los muestreos hemáticos
		PuestoNotificacion oPuestoUnidad = puestoNotificacionService.EncontrarPorUnidad(oUnidad.getUnidadId(), 2);
		if (oPuestoUnidad!=null && oPuestoUnidad.getClave()!=null) {
			this.clave = oPuestoUnidad.getClave();
			this.puestoNotificacionId=oPuestoUnidad.getPuestoNotificacionId();
			this.puestoNotificacionSelected=oPuestoUnidad;
		} 
		
		this.autorizado=Operacion.esUnidadAutorizada(this.infoSesion.getUsuarioId(), this.unidadSelectedId);
		
	}

	public void quitarColVol() {
	
		this.colVolPuestoSelected=null;
		iniciarVarColVol();
		
	}
	
	/*
	 * Inicializa el objeto MuestreoHematico vinculado al número de
	 * lámina especificado para la clave (puesto de notificación) y deja
	 * el interfaz preparado para que el usuario ingrese otro número de
	 * lámina para la misma clave.
	 */
	public void cancelarLamina() {
		
		this.numeroLamina=null;
		this.muestreoHematicoId=0;
		this.muestreoHematicoSelected=new MuestreoHematico();
		iniciarPRM();
		iniciarPGG();
		iniciarCapaReferente();
		this.estado=0;
		this.viviendaCodigo=null;
		this.viviendaSelected=null;
		this.manzanaCodigo=null;
		this.manzanaSelected=null;
		iniciarPersonas();
		
	}
	
	public void iniciarPRM() {

		this.muestreoPruebaRapidaSelected=new MuestreoPruebaRapida();
		this.muestreoHematicoSelected.setPruebaRapida(null);
		this.responsablePruebaRapidaSelectedId=0;
		this.responsablePruebaRapidaSelected=null;
		this.resultadoPruebaRapidaSelected=null;
		this.resultadoPruebaRapidaSelectedId=0;

	}

	public void iniciarPGG() {
		
		this.muestreoDiagnosticoSelected=new MuestreoDiagnostico();
		this.muestreoHematicoSelected.setDiagnostico(null);
		this.densidadPFalciparumSelectedId=0;
		this.densidadPVivaxSelectedId=0;
		this.motivoFaltaDiagnosticoSelectedId=0;
		this.estadioPFalciparumSelectedId=0;
		this.positivoPVivax=null;
		this.positivoPFalciparum=null;
		this.entidadLaboratorioSelectedId=this.entidadSelectedId;

	}
	
	/*
	 *  Obtiene las muestras asociados específicamente a un puesto de notificación o bien,
	 *  todas aquellas muestras asociadas a todos los puestos de notificación vinculados
	 *  a una unidad de salud.
	 */
	public void obtenerMuestras() {
		
		if (this.unidadSelectedId==0) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "Debe seleccionar una unidad de salud o un colaborador voluntario","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		generarAnios();

//		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmMuestreo:grdMuestreos");
//		this.muestreosHematicos.load(0, 50, null, null, null);
//		this.muestreosHematicos.setRowCount(numRegistros);
//		dataTable.loadLazyData();
//		if (dataTable.getRowCount() <= dataTable.getPage()) {
//			dataTable.setFirst(0);
//		} else {
//			dataTable.setFirst(dataTable.getPage());
//		}
		
	}
	
	public void cargarMuestreo() {

		// TODO aqui se debe establecer el estado del muestreo según situación
		
		this.muestreoHematicoId=this.muestreoHematicoSelected.getMuestreoHematicoId();
		
		// se debe ensamblar la DTO de persona
		InfoResultado oResPersonaSel=Operacion.ensamblarPersona(this.muestreoHematicoSelected.getSisPersona());
		if (!oResPersonaSel.isOk()) {
			FacesMessage msg = Mensajes.enviarMensaje(oResPersonaSel);
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		this.puestoNotificacionSelected=this.muestreoHematicoSelected.getPuestoNotificacion();
		this.puestoNotificacionId=this.puestoNotificacionSelected.getPuestoNotificacionId();
		
		if (this.puestoNotificacionSelected.getColVol()!=null) {
			this.nombreColVol=this.puestoNotificacionSelected.getColVol().getSisPersona().getNombreCompleto();
		} else {
			this.nombreColVol=null;
			this.colVolPuestoSelected=null;
		}

		this.manzanaCodigo=null;
		this.viviendaCodigo=null;
				
		this.personaSelected=(Persona)oResPersonaSel.getObjeto();
		this.manzanaSelected=this.muestreoHematicoSelected.getManzana();
		if (this.manzanaSelected!=null) {
			this.manzanaCodigo=this.muestreoHematicoSelected.getManzana().getCodigo().substring(9, 12);
		}
		this.viviendaSelected=this.muestreoHematicoSelected.getVivienda();
		if (this.viviendaSelected!=null) {
			this.viviendaCodigo=this.muestreoHematicoSelected.getVivienda().getCodigo().substring(9, 14);;
		}
				
		this.muestreoPruebaRapidaSelected=new MuestreoPruebaRapida();
		if (this.muestreoHematicoSelected.getPruebaRapida()!=null) {
			this.muestreoPruebaRapidaSelected.setMuestreoPruebaRapidaId(this.muestreoHematicoSelected.getPruebaRapida().getMuestreoPruebaRapidaId());
			this.muestreoPruebaRapidaSelected.setFecha(this.muestreoHematicoSelected.getPruebaRapida().getFecha());
			this.muestreoPruebaRapidaSelected.setRealizado(this.muestreoHematicoSelected.getPruebaRapida().getRealizado());
			this.muestreoPruebaRapidaSelected.setResultado(this.muestreoHematicoSelected.getPruebaRapida().getResultado());
			this.responsablePruebaRapidaSelectedId=this.muestreoHematicoSelected.getPruebaRapida().getRealizado().getCatalogoId();
			this.resultadoPruebaRapidaSelectedId=this.muestreoHematicoSelected.getPruebaRapida().getResultado().getCatalogoId();
		}

		this.tmpPersonaReferente=this.muestreoHematicoSelected.getPersonaReferente();
		this.tmpTelefonoReferente=this.muestreoHematicoSelected.getTelefonoReferente();
				
		this.muestreoDiagnosticoSelected=new MuestreoDiagnostico();
		if (this.muestreoHematicoSelected.getDiagnostico()!=null) {
			this.muestreoDiagnosticoSelected.setMuestreoDiagnosticoId(this.muestreoHematicoSelected.getDiagnostico().getMuestreoDiagnosticoId());
			this.muestreoDiagnosticoSelected.setFechaRecepcion(this.muestreoHematicoSelected.getDiagnostico().getFechaRecepcion());
			this.muestreoDiagnosticoSelected.setFechaDiagnostico(this.muestreoHematicoSelected.getDiagnostico().getFechaDiagnostico());
			this.muestreoDiagnosticoSelected.setResultado(this.muestreoHematicoSelected.getDiagnostico().getResultado());
			this.muestreoDiagnosticoSelected.setDensidadPVivax(this.muestreoHematicoSelected.getDiagnostico().getDensidadPVivax());
			this.muestreoDiagnosticoSelected.setDensidadPFalciparum(this.muestreoHematicoSelected.getDiagnostico().getDensidadPFalciparum());
			this.muestreoDiagnosticoSelected.setEntidadAdtvaLaboratorio(this.muestreoHematicoSelected.getDiagnostico().getEntidadAdtvaLaboratorio());
			this.muestreoDiagnosticoSelected.setEstadioPFalciparum(this.muestreoHematicoSelected.getDiagnostico().getEstadioPFalciparum());
			this.muestreoDiagnosticoSelected.setEstadiosAsexuales(this.muestreoHematicoSelected.getDiagnostico().getEstadiosAsexuales());
			this.muestreoDiagnosticoSelected.setGametocitos(this.muestreoHematicoSelected.getDiagnostico().getGametocitos());
			this.muestreoDiagnosticoSelected.setLaboratorista(this.muestreoHematicoSelected.getDiagnostico().getLaboratorista());
			this.muestreoDiagnosticoSelected.setLeucocitos(this.muestreoHematicoSelected.getDiagnostico().getLeucocitos());
			this.muestreoDiagnosticoSelected.setMotivoFaltaDiagnostico(this.muestreoHematicoSelected.getDiagnostico().getMotivoFaltaDiagnostico());
			this.muestreoDiagnosticoSelected.setMunicipioLaboratorio(this.muestreoHematicoSelected.getDiagnostico().getMunicipioLaboratorio());
			this.muestreoDiagnosticoSelected.setUnidadLaboratorio(this.muestreoHematicoSelected.getDiagnostico().getUnidadLaboratorio());
			this.muestreoDiagnosticoSelected.setPositivoPFalciparum(this.muestreoHematicoSelected.getDiagnostico().getPositivoPFalciparum());
			this.muestreoDiagnosticoSelected.setPositivoPVivax(this.muestreoHematicoSelected.getDiagnostico().getPositivoPVivax());

			if (this.muestreoDiagnosticoSelected.getPositivoPFalciparum()!=null) {
				this.positivoPFalciparum=this.muestreoDiagnosticoSelected.getPositivoPFalciparum().equals(BigDecimal.ONE)?Boolean.TRUE:Boolean.FALSE;
			} else {
				this.positivoPFalciparum=null;
			}
			
			if (this.muestreoDiagnosticoSelected.getPositivoPVivax()!=null) {
				this.positivoPVivax=this.muestreoDiagnosticoSelected.getPositivoPVivax().equals(BigDecimal.ONE)?Boolean.TRUE:Boolean.FALSE;
			
			} else {
				this.positivoPVivax=null;
			}
			
			this.densidadPFalciparumSelectedId=0;
			if (this.muestreoDiagnosticoSelected.getDensidadPFalciparum()!=null) {
				this.densidadPFalciparumSelectedId=this.muestreoDiagnosticoSelected.getDensidadPFalciparum().getCatalogoId();
			}
			this.densidadPVivaxSelectedId=0;
			if (this.muestreoDiagnosticoSelected.getDensidadPVivax()!=null) {
				this.densidadPVivaxSelectedId=this.muestreoDiagnosticoSelected.getDensidadPVivax().getCatalogoId();
			}
			this.motivoFaltaDiagnosticoSelectedId=0;
			if (this.muestreoDiagnosticoSelected.getMotivoFaltaDiagnostico()!=null) {
				this.motivoFaltaDiagnosticoSelectedId=this.muestreoDiagnosticoSelected.getMotivoFaltaDiagnostico().getCatalogoId();
			}
					
		}
				
		// inicializa las variables del componente de personas
		FacesContext context = FacesContext.getCurrentInstance();
		PersonaBean personaBean = (PersonaBean)context.getApplication().evaluateExpressionGet(context, "#{personaBean}", PersonaBean.class);
		personaBean.setPersonaSelected(this.personaSelected);
		personaBean.iniciarPropiedades();

	}
	
	public void onMuestreoSelected(SelectEvent iEvento) {
		
	}

	/*
	 * Calcula la semana y año epidemiológico en base a la fecha de
	 * toma de muestra de la gota gruesa
	 */
	public void calcularSemana() {

		// valida si la fecha de toma de muestra es igual o mayor que la fecha de apetura y
		// menor o igual que la fecha de cierre, en caso de estar definida.
		
		if (this.puestoNotificacionSelected.getFechaApertura().after(this.muestreoHematicoSelected.getFechaToma())) {
			SimpleDateFormat oFormatoFecha = new SimpleDateFormat("dd/MM/yyyy");
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "La fecha de toma de la muestra debe ser superior o igual que la fecha de apertura del puesto de notificación","Fecha de Apertura del Puesto de Notificación: " + oFormatoFecha.format(this.puestoNotificacionSelected.getFechaApertura()));
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			this.muestreoHematicoSelected.setFechaToma(null);
			this.muestreoHematicoSelected.setSemanaEpidemiologica(null);
			this.muestreoHematicoSelected.setAñoEpidemiologico(null);
			return;
		}
		
		if (this.puestoNotificacionSelected.getFechaCierre()!=null && this.puestoNotificacionSelected.getFechaCierre().before(this.muestreoHematicoSelected.getFechaToma())) {
			SimpleDateFormat oFormatoFecha = new SimpleDateFormat("dd/MM/yyyy");
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "La fecha de toma de la muestra debe ser inferior o igual que la fecha de cierre del puesto de notificación","Fecha de Cierre del Puesto de Notificación: " + oFormatoFecha.format(this.puestoNotificacionSelected.getFechaCierre()));
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			this.muestreoHematicoSelected.setFechaToma(null);
			this.muestreoHematicoSelected.setSemanaEpidemiologica(null);
			this.muestreoHematicoSelected.setAñoEpidemiologico(null);
			return;
		}
		
		this.muestreoHematicoSelected.setSemanaEpidemiologica(Integer.valueOf(CalendarioEPI.semana(this.muestreoHematicoSelected.getFechaToma())));
		this.muestreoHematicoSelected.setAñoEpidemiologico(Integer.valueOf(CalendarioEPI.año(this.muestreoHematicoSelected.getFechaToma())));
	}
	
	
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

		cancelarLamina();
		
		this.clave=null;
		this.colVolPuestoSelected=null;
		this.nombreColVol=null;
		this.puestoNotificacionId=0;
		this.colVolPuestoSelected=null;
		
		this.unidadSelected=null;
		this.unidadSelectedId=0;

		// se filtran únicamente las unidades de salud a las cuales tiene autorización el
		// usuario y que hayan sido declaradas como puestos de notificación activas y aquellas
		// unidades, que no siendo puesto de notificación, tienen declarada la característica
		// funcional respectiva

		this.unidades=ni.gob.minsa.malaria.reglas.Operacion.unidadesAutorizadasPorEntidad(this.infoSesion.getUsuarioId(), this.entidadSelectedId, 0,false,Utilidades.ES_PUESTO_NOTIFICACION +", " + Utilidades.DECLARA_MUESTREO_HEMATICO);

		if ((this.unidades!=null) && (this.unidades.size()>0)) {
			this.unidadSelectedId=this.unidades.get(0).getUnidadId();
			this.unidadSelected=this.unidades.get(0);
			
			// obtiene clave de la unidad de salud, en caso que sea un puesto de notificación
			// ya que podría ser únicamente una unidad que registra las fichas de muestreo hemático
			// notificadas por los puestos de notificación
			PuestoNotificacion oPuestoNotificacion = puestoNotificacionService.EncontrarPorUnidad(this.unidadSelectedId, 1);
			if (oPuestoNotificacion!=null) {
				this.clave=oPuestoNotificacion.getClave();
				this.puestoNotificacionId=oPuestoNotificacion.getPuestoNotificacionId();
				this.puestoNotificacionSelected=oPuestoNotificacion;
			} else {
				this.clave=null;
				this.puestoNotificacionId=0;
				this.puestoNotificacionSelected=null;
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
	 * Obtiene los colvoles asociados a una unidad de salud, la cual puede o no ser
	 * un puesto de notificación
	 */
	public void listarColVoles(ActionEvent pEvento) {
		
		obtenerColVoles();
	}
	
	public void obtenerColVoles() {
		
		this.colVolPuestos = new ArrayList<ColVolPuesto>();

		if (this.unidadSelectedId==0) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "Debe seleccionar primero la unidad de salud que coordina al Colaborador Voluntario","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		this.colVolPuestos=puestoNotificacionService.ListarColVolPorUnidad(this.unidadSelectedId,this.filtroColVol,1);
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
	
	/**
	 * Una vez seleccionado el colVol de la grilla, el usuario debe confirmar dicha selección
	 * pulsando en el botón Aceptar.  En este caso, el ColVol es seleccionado a partir de
	 * la selección de la unidad de salud, por lo cual no es necesario actualizar dicha información.
	 * 
	 */
	public void aceptarColVol(ActionEvent pEvento) {

		iniciarVarColVol();
	}

	public void iniciarVarColVol() {

		if (this.colVolPuestoSelected!=null) {
			this.puestoNotificacionId=this.colVolPuestoSelected.getPuestoNotificacionId();
			this.setNombreColVol(this.colVolPuestoSelected.getNombreColVol());
			this.clave=this.colVolPuestoSelected.getClave();
			this.puestoNotificacionSelected=(PuestoNotificacion)(puestoNotificacionService.Encontrar(this.puestoNotificacionId)).getObjeto();

		} else {
			this.puestoNotificacionId=0;
			this.nombreColVol=null;
			this.clave=null;
			this.puestoNotificacionSelected=null;
		}

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
	 * Verifica si la lámina ya ha sido utilizada por la clave dentro de una
	 * misma entidad administrativa y envía un mensaje al usuario solo de advertencia
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void comprobarLamina() {
		
		RequestContext rContext = RequestContext.getCurrentInstance();
		Collection iComponentes = Arrays.asList("grwMensaje","txtLamina");
		
		if (this.puestoNotificacionId==0) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Seleccione primero un puesto de notificación",""));
			this.numeroLamina=null;
		    rContext.update(iComponentes);
			return;
		}
		
		if (this.clave==null || this.clave.trim().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Debe especificar la clave responsable de la toma de muestra",""));
			this.numeroLamina=null;
		    rContext.update(iComponentes);
			return;			
		}
		
		if (this.clave!=null && !this.clave.trim().isEmpty() && this.numeroLamina!=null && this.numeroLamina.compareTo(BigDecimal.ZERO)==1) {
			MuestreoHematico oMuestreoHematico = muestreoHematicoService.EncontrarPorLamina(this.entidadSelectedId, this.clave, this.numeroLamina);
			
			// si el resultado es nulo, implica que no existe una pareja de valores
			// de clave y lámina declarados, el número de lámina se declara por numeración
			// consecutiva del block de formatos E-2 proporcionados al puesto de notificación.
			
			if (oMuestreoHematico!=null) {

				// se verifica si el puesto de notificación declarado en el muestreo hemático
				// se corresponde con el puesto de notificación seleccionado, caso contrario
				// implica que el número de lámina ya ha sido utilizada por la misma clave
				// pero diferente puesto de notificación
				
				if (this.puestoNotificacionId!=oMuestreoHematico.getPuestoNotificacion().getPuestoNotificacionId()) {
					FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "El número de lámina " + this.numeroLamina.toString() + " ya ha sido utilizada por esta clave pero diferente puesto de notificación",""));
					this.numeroLamina=null;
				    rContext.update(iComponentes);
					return;
				}
				
				//TODO Debe actualizar la variable estado de acuerdo según exista o no la M10.

				this.estado=1;
				this.muestreoHematicoSelected=oMuestreoHematico;
				cargarMuestreo();

				//actualiza el formulario completo con los datos cargados
				rContext.update("@form");
				
			} else {
				if (!this.autorizado) {
					FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "No tiene autorización para registrar muestreos hemáticos para esta unidad de salud.",""));
					this.numeroLamina=null;
				}
			    rContext.update(iComponentes);
				return;
			}
		
		}
	}
	
	/**
	 * Se ejecuta cuando el usuario pulsa en el botón guardar.
	 * Este proceso incluye guardar un nuevo registro o actualizar
	 * un registro existente. <br>
	 */
	public void guardar(ActionEvent pEvento) {
		
		if (!this.autorizado) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "No tiene permisos para realizar esta operación",Mensajes.NOTIFICACION_ADMINISTRADOR));
			return;
		}
		
		if (this.puestoNotificacionId==0) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Debe especificar un puesto de notificación válido","Verifique la clave. Si la clave es válida es posible que usted no tenga autorización a registrar muestreos hemáticos para dicha clave."));
			return;
		}

		if (this.numeroLamina==null || this.numeroLamina.compareTo(BigDecimal.ZERO)<1) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Debe especificar un número válido para la lámina",""));
			return;
		}
		
		if (this.muestreoHematicoSelected.getFechaToma()==null) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Debe especificar una fecha de toma de muestra",""));
			return;
		}
			
		if (this.personaSelected==null) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La declaración de la persona a la cual se realiza el muestreo hemático es requerida",""));
			return;
		}
		
		// obtiene los objetos vinculados a persona que no son accesibles directamente
		// desde el DTO de persona
		
		Comunidad oComunidadResidencia = null;
		InfoResultado oResComunidad = comunidadService.Encontrar(this.personaSelected.getComuResiCodigo());
		if (oResComunidad.isOk()) {
			oComunidadResidencia=(Comunidad)oResComunidad.getObjeto();
		} else {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La comunidad es requerida en los datos del paciente","Favor verificar en los datos del paciente"));
			return;
		}

		DivisionPolitica oMunicipioResidencia = null;
		InfoResultado oResMunicipio = divisionService.EncontrarPorCodigoNacional(this.personaSelected.getMuniResiCodigoNac());
		if (oResMunicipio.isOk()) {
			oMunicipioResidencia=(DivisionPolitica)oResMunicipio.getObjeto();
		} else {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "El municipio de residencia es requerido en los datos del paciente","Favor verificar en los datos del paciente"));
			return;
		}

		// Para una ficha de muestreo hemático existente no puede modificarse la persona asociada

		InfoResultado oResultado = new InfoResultado();

		if (this.muestreoHematicoSelected.getMuestreoHematicoId()==0) {
			this.muestreoHematicoSelected.setClave(this.clave);
			this.muestreoHematicoSelected.setNumeroLamina(this.numeroLamina);
			this.muestreoHematicoSelected.setUnidadNotificacion(this.unidadSelected);
			this.muestreoHematicoSelected.setMunicipioNotificacion(this.unidadSelected.getMunicipio());
			this.muestreoHematicoSelected.setEntidadNotificacion(this.unidadSelected.getEntidadAdtva());
			this.muestreoHematicoSelected.setPuestoNotificacion((PuestoNotificacion)puestoNotificacionService.Encontrar(this.puestoNotificacionId).getObjeto());
		}
		
		this.muestreoHematicoSelected.setComunidadResidencia(oComunidadResidencia);
		this.muestreoHematicoSelected.setMunicipioResidencia(oMunicipioResidencia);
		this.muestreoHematicoSelected.setDireccionResidencia(this.personaSelected.getDireccionResi());
		this.muestreoHematicoSelected.setManzana(this.manzanaSelected);
		this.muestreoHematicoSelected.setVivienda(this.viviendaSelected);
		this.muestreoHematicoSelected.setFechaNacimiento(this.personaSelected.getFechaNacimiento());
		this.muestreoHematicoSelected.setSexo((Sexo)sexoService.Encontrar(this.personaSelected.getSexoId()).getObjeto());
		this.muestreoHematicoSelected.setEtnia((Etnia)etniaService.Encontrar(this.personaSelected.getEtniaId()).getObjeto());
		
		// los resultados del muestreo por gota gruesa ya han sido validados en la modal
		// y los valores se han establecido en muestreoDiagnosticoSelected
		this.muestreoHematicoSelected.setDiagnostico(null);
		if (this.muestreoDiagnosticoSelected!=null) {
			this.muestreoDiagnosticoSelected.setMuestreoHematico(this.muestreoHematicoSelected);
			this.muestreoHematicoSelected.setDiagnostico(this.muestreoDiagnosticoSelected);
		}
		
		// los resultados de la prueba rápida ya han sido validados en la modal
		// y los valores se han establecido en muestreoPruebaRapidaSelected
		this.muestreoHematicoSelected.setPruebaRapida(null);
		if (this.muestreoPruebaRapidaSelected!=null && this.muestreoPruebaRapidaSelected.getResultado()!=null) {
			this.muestreoPruebaRapidaSelected.setMuestreoHematico(this.muestreoHematicoSelected);
			this.muestreoHematicoSelected.setPruebaRapida(this.muestreoPruebaRapidaSelected);
		}

		if ((this.muestreoDiagnosticoSelected!=null) && (this.muestreoDiagnosticoSelected.getMuestreoDiagnosticoId()==0)) {
			this.muestreoDiagnosticoSelected.setUsuarioRegistro(this.infoSesion.getUsername());
			this.muestreoDiagnosticoSelected.setFechaRegistro(Calendar.getInstance().getTime());
		}

		if ((this.muestreoPruebaRapidaSelected!=null && (this.muestreoPruebaRapidaSelected.getMuestreoPruebaRapidaId()==0))) {
			this.muestreoPruebaRapidaSelected.setUsuarioRegistro(this.infoSesion.getUsername());
			this.muestreoPruebaRapidaSelected.setFechaRegistro(Calendar.getInstance().getTime());
		}

		// si el identificador del muestreo hemático es 0, implica es una nueva
		// ficha de muestreo hemático, i.e. estado=0

		if (this.muestreoHematicoId==0) {
			this.muestreoHematicoSelected.setFechaRegistro(Calendar.getInstance().getTime());
			this.muestreoHematicoSelected.setUsuarioRegistro(this.infoSesion.getUsername());
			oResultado=muestreoHematicoService.Agregar(this.muestreoHematicoSelected, this.personaSelected);
		} else {
			oResultado=muestreoHematicoService.Guardar(this.muestreoHematicoSelected, this.personaSelected);
		}
		
		if (!oResultado.isOk()) {
			FacesMessage msg = Mensajes.enviarMensaje(oResultado);
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		cancelarLamina();
		this.capaActiva=1;
		
		oResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void eliminar(ActionEvent pEvento){

		if (!this.autorizado) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "No tiene permisos para realizar esta operación",Mensajes.NOTIFICACION_ADMINISTRADOR));
			return;
		}

		if (this.muestreoHematicoId==0) {
			FacesContext.getCurrentInstance().addMessage(null, Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "Debe especificar un muestreo hemático.",""));
			return;
		}
		
		InfoResultado oResultado = new InfoResultado();
		
		if (oResultado.isOk()){
			cancelarLamina();
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

		this.personaId=this.personaSelected.getPersonaId();
		this.muestreoHematicoSelected.setSexo((Sexo)sexoService.Encontrar(this.personaSelected.getSexoId()).getObjeto());
		this.muestreoHematicoSelected.setEtnia((Etnia)etniaService.Encontrar(this.personaSelected.getEtniaId()).getObjeto());
		this.muestreoHematicoSelected.setDireccionResidencia(this.personaSelected.getDireccionResi());
		this.muestreoHematicoSelected.setFechaNacimiento(this.personaSelected.getFechaNacimiento());
		
		InfoResultado oResComunidad = comunidadService.Encontrar(this.personaSelected.getComuResiCodigo());
		this.muestreoHematicoSelected.setComunidadResidencia((Comunidad)oResComunidad.getObjeto());

		InfoResultado oResMunicipio = divisionService.EncontrarPorCodigoNacional(this.personaSelected.getMuniResiCodigoNac());
		this.muestreoHematicoSelected.setMunicipioResidencia((DivisionPolitica)oResMunicipio.getObjeto());

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

	public long getEstadioPFalciparumSelectedId() {
		return estadioPFalciparumSelectedId;
	}

	public void setEstadioPFalciparumSelectedId(long estadioPFalciparumSelectedId) {
		this.estadioPFalciparumSelectedId = estadioPFalciparumSelectedId;
	}

	public List<EstadioPFalciparum> getEstadiosPFalciparum() {
		return estadiosPFalciparum;
	}

	public void setEstadiosPFalciparum(List<EstadioPFalciparum> estadiosPFalciparum) {
		this.estadiosPFalciparum = estadiosPFalciparum;
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

	/**
	 * @return the filtroColVol
	 */
	public String getFiltroColVol() {
		return filtroColVol;
	}

	/**
	 * @param filtroColVol the filtroColVol to set
	 */
	public void setFiltroColVol(String filtroColVol) {
		this.filtroColVol = filtroColVol;
	}

	public void setMuestreoHematicoId(long muestreoHematicoId) {
		this.muestreoHematicoId = muestreoHematicoId;
	}

	public long getMuestreoHematicoId() {
		return muestreoHematicoId;
	}

	/**
	 * @return the muestreoHematicoSelected
	 */
	public MuestreoHematico getMuestreoHematicoSelected() {
		return muestreoHematicoSelected;
	}

	/**
	 * @param muestreoHematicoSelected the muestreoHematicoSelected to set
	 */
	public void setMuestreoHematicoSelected(
			MuestreoHematico muestreoHematicoSelected) {
		this.muestreoHematicoSelected = muestreoHematicoSelected;
	}

	/**
	 * @return the muestreosHematicos
	 */
	public LazyDataModel<MuestreoHematico> getMuestreosHematicos() {
		return muestreosHematicos;
	}

	/**
	 * @param muestreosHematicos the muestreosHematicos to set
	 */
	public void setMuestreosHematicos(
			LazyDataModel<MuestreoHematico> muestreosHematicos) {
		this.muestreosHematicos = muestreosHematicos;
	}

	/**
	 * @return the peticionLista
	 */
	public boolean isPeticionLista() {
		return peticionLista;
	}

	/**
	 * @param peticionLista the peticionLista to set
	 */
	public void setPeticionLista(boolean peticionLista) {
		this.peticionLista = peticionLista;
	}

	/**
	 * @return the numRegistros
	 */
	public int getNumRegistros() {
		return numRegistros;
	}

	/**
	 * @param numRegistros the numRegistros to set
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}

	/**
	 * @return the filtroPaciente
	 */
	public String getFiltroPaciente() {
		return filtroPaciente;
	}

	/**
	 * @param filtroPaciente the filtroPaciente to set
	 */
	public void setFiltroPaciente(String filtroPaciente) {
		this.filtroPaciente = filtroPaciente;
	}

	/**
	 * @return the puestoActivo
	 */
	public boolean isPuestoActivo() {
		return puestoActivo;
	}

	/**
	 * @param puestoActivo the puestoActivo to set
	 */
	public void setPuestoActivo(boolean puestoActivo) {
		this.puestoActivo = puestoActivo;
	}

	/**
	 * @return the situacion
	 */
	public Integer getSituacion() {
		return situacion;
	}

	/**
	 * @param situacion the situacion to set
	 */
	public void setSituacion(Integer situacion) {
		this.situacion = situacion;
	}

	public void setMuestreoHematicoSelectedTmp(
			MuestreoHematico muestreoHematicoSelectedTmp) {
		this.muestreoHematicoSelectedTmp = muestreoHematicoSelectedTmp;
	}

	public MuestreoHematico getMuestreoHematicoSelectedTmp() {
		return muestreoHematicoSelectedTmp;
	}

	public void setAnioEpiSelected(Integer anioEpiSelected) {
		this.anioEpiSelected = anioEpiSelected;
	}

	public Integer getAnioEpiSelected() {
		return anioEpiSelected;
	}

	/**
	 * @return the aniosEpidemiologicos
	 */
	public List<SelectItem> getAniosEpidemiologicos() {
		return aniosEpidemiologicos;
	}

	/**
	 * @param aniosEpidemiologicos the aniosEpidemiologicos to set
	 */
	public void setAniosEpidemiologicos(List<SelectItem> aniosEpidemiologicos) {
		this.aniosEpidemiologicos = aniosEpidemiologicos;
	}

	public void setAutorizado(boolean autorizado) {
		this.autorizado = autorizado;
	}

	public boolean isAutorizado() {
		return autorizado;
	}

	/**
	 * @return the puestoNotificacionSelected
	 */
	public PuestoNotificacion getPuestoNotificacionSelected() {
		return puestoNotificacionSelected;
	}

	/**
	 * @param puestoNotificacionSelected the puestoNotificacionSelected to set
	 */
	public void setPuestoNotificacionSelected(
			PuestoNotificacion puestoNotificacionSelected) {
		this.puestoNotificacionSelected = puestoNotificacionSelected;
	}

}