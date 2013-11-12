package ni.gob.minsa.malaria.interfaz.supervision;

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
import ni.gob.minsa.malaria.datos.supervision.VisitaPuestoDA;
import ni.gob.minsa.malaria.datos.vigilancia.PuestoNotificacionDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.supervision.VisitaPuesto;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoNotificacion;
import ni.gob.minsa.malaria.modelo.vigilancia.noEntidad.ColVolPuesto;
import ni.gob.minsa.malaria.reglas.Operacion;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;
import ni.gob.minsa.malaria.servicios.supervision.VisitaPuestoService;
import ni.gob.minsa.malaria.servicios.vigilancia.PuestoNotificacionService;
import ni.gob.minsa.malaria.soporte.CalendarioEPI;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * Servicio para la capa de presentación de la página 
 * supervision/visitaPueto.xhtml
 *
 * <p>
 * @author Félix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 09/11/2013
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class VisitaPuestoBean implements Serializable{
	private static final long serialVersionUID = 1L;

	protected InfoSesion infoSesion;
	private int capaActiva;
	
	// -------------------------------------------------------------
	// Estado
	// 
	// El atributo estado indica la situación en que se encuentra
	// una vificha y por tanto es la variable que  gestionará el bloqueo y
	// peticiones desde el interfaz al bean.
	//
	// Valores:
	// 0 : Nueva ficha.  El interfaz se encuentra preparada para agregar
	//     una nueva ficha de visita a un puesto de notificación; el usuario
	//     debe tener permisos explícitos en la un
	// 1 : Ficha existente sin investigación epidemiológica asociada.  
	//     El usuario ha indicado un número de clave
	//     y lámina, y ésta ha sido encontrada en la base de datos.  En
	//     este caso, se visualizan los datos y se protegen aquellos que
	//     no son modificables.
	// -------------------------------------------------------------
	private int modo;
	
	// -------------------------------------------------------------
	// Entidad Administrativa
	//
	// Objetos vinculados a las entidades administrativas 
	// indirectas a las cuales el usuario tiene autorización según 
	// el listado de unidades de salud autorizadas. En caso de ser un usuario
	// del nivel central, tentrá acceso a todas las entidades
	// -------------------------------------------------------------
	private List<EntidadAdtva> entidades;
	private long entidadSelectedId;
	private long autorizadoEnEntidadSelected=0;
	
	
	// -------------------------------------------------------------
	// Unidad de Salud
	//
	// Objetos vinculados a las unidades de salud para las cuales
	// coordinadas por entidad administrativa y en un municipio dado, 
	// dichas unidades debe estar declarada como puesto de notificación
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
	private ColVolPuesto colVolPuestoSelected;
	private long puestoNotificacionId;
	private String filtroColVol;
	private String nombreColVol;
	
	// atributos vinculados a la identificación de la ficha E-2
	private String clave;
	
	//Municipios donde existen unidades de notificación para la entidad 
	// administrativa seleccionada
	private List<DivisionPolitica> municipios;
	private long municipioSelectedId;
	
	private List<SelectItem> aniosEpidemiologicos;
	private int anioEpiSelected=0;
	
	private LazyDataModel<VisitaPuesto> visitasPuestos;
	private VisitaPuesto visitaPuestoSelected;
	private long visitaPuestoSelectedId;
	private int numVisitas;
	
	private Date fechaEntrada;
	private Date fechaSalida;
	private BigDecimal anioEpi;
	private BigDecimal semanaEpi;
	private BigDecimal divulgacionSelected;
	private BigDecimal visibleCarnetSelected;
	private Date horarioInicio;
	private Date horarioFin;
	private BigDecimal tomaMuestraSelected;
	private BigDecimal atencionPacienteSelected;
	private BigDecimal stockSelected;
	private BigDecimal reconocidoSelected;
	private Date proximaVisita;
	
	private static EntidadAdtvaService entidadService = new EntidadAdtvaDA();
	private static UnidadService unidadService = new UnidadDA();
	private static VisitaPuestoService visitaPuestoService = new VisitaPuestoDA();
	private static PuestoNotificacionService puestoNotificacionService = new PuestoNotificacionDA();
	/**************************************************
	 * Constructor
	 **************************************************/
	
	public VisitaPuestoBean(){
		init();
	}
	
	/**************************************************
	 * Eventos
	 **************************************************/
	
	public void onVisitaSelected(){
		if(this.visitaPuestoSelected==null){
			return;
		}
		
		InfoResultado oResultado;
		this.capaActiva=2;
		
		this.visitaPuestoSelectedId = this.visitaPuestoSelected.getVisitaPuestoId();
		List<Unidad> oUnidades =Operacion.unidadesAutorizadasPorEntidad(this.infoSesion.getUsuarioId(), this.entidadSelectedId, 0,false
				,Utilidades.ES_PUESTO_NOTIFICACION +", " + Utilidades.DECLARA_MUESTREO_HEMATICO);

		if ((oUnidades!=null) && (oUnidades.size()>0)) {
			this.unidades=new ArrayList<Unidad>();
			for(Unidad oUnidad:oUnidades){
				if(oUnidad.getMunicipio().getDivisionPoliticaId()==this.municipioSelectedId){
					this.unidades.add(oUnidad);
				}
			}
		}
		// se verifica si la unidad de la visita seleccionada se encuentra en la lista, en caso contrario se agrega
		if ((this.unidades != null) && (this.unidades.size() > 0)) {
			boolean iExisteEntidad = false;
			iExisteEntidad = false;
			for (Unidad oUnidad : this.unidades) {
				if (oUnidad.getUnidadId() == this.visitaPuestoSelected.getUnidad().getUnidadId()) {
					iExisteEntidad = true;
					break;
				}
			}
			if (!iExisteEntidad) {
			   oResultado = unidadService.Encontrar(this.visitaPuestoSelected.getUnidad().getUnidadId());
				if (oResultado.isOk()) {
					this.unidades.add((Unidad) oResultado.getObjeto());
				}
			}
		}
		oResultado = puestoNotificacionService.Encontrar(this.visitaPuestoSelected.getPuestoNotificacion().getPuestoNotificacionId());
		if(oResultado.isOk()){
			PuestoNotificacion oPuestoNotificacion = (PuestoNotificacion)oResultado.getObjeto();
			
			if(oPuestoNotificacion.getUnidad()!=null){
				// implica que la unidad es un puesto de notificación
				this.puestoNotificacionId=oPuestoNotificacion.getPuestoNotificacionId();
			}else{
				this.nombreColVol=oPuestoNotificacion.getColVol().getSisPersona().getNombreCompleto();
				this.puestoNotificacionId=oPuestoNotificacion.getPuestoNotificacionId();
				ColVolPuesto oColVolPuesto = new ColVolPuesto();
				oColVolPuesto.setClave(oPuestoNotificacion.getClave());
				oColVolPuesto.setNombreColVol(oPuestoNotificacion.getColVol().getSisPersona().getNombreCompleto());
				oColVolPuesto.setPuestoNotificacionId(oPuestoNotificacion.getPuestoNotificacionId());
				this.colVolPuestoSelected=oColVolPuesto;
				this.nombreColVol=oColVolPuesto.getNombreColVol();
			}
		}
		this.unidadSelected=this.visitaPuestoSelected.getUnidad();
		this.unidadSelectedId=this.visitaPuestoSelected.getUnidad().getUnidadId();
		this.clave=this.colVolPuestoSelected.getClave();
		this.fechaEntrada=this.visitaPuestoSelected.getFechaEntrada();
		this.fechaSalida=this.visitaPuestoSelected.getFechaSalida();
		this.anioEpi=this.visitaPuestoSelected.getAñoEpidemiologico();
		this.semanaEpi=this.visitaPuestoSelected.getSemanaEpidemiologica();
		this.divulgacionSelected=this.visitaPuestoSelected.getDivulgacion();
		this.visibleCarnetSelected=this.visitaPuestoSelected.getVisibleCarnet();
		this.horarioInicio=this.visitaPuestoSelected.getHorarioInicio();
		this.horarioFin=this.visitaPuestoSelected.getHorarioFin();
		this.tomaMuestraSelected=this.visitaPuestoSelected.getTomaMuestras();
		this.stockSelected=this.visitaPuestoSelected.getStock();
		this.reconocidoSelected=this.visitaPuestoSelected.getReconocido();
		this.atencionPacienteSelected=this.visitaPuestoSelected.getAtencionPacientes();
		this.proximaVisita=this.visitaPuestoSelected.getProximaVisita();
	}
	
    public void obtenerMunicipios(){
		 this.municipios=null;
		 this.municipioSelectedId=0;
		 
		 if(this.entidadSelectedId < 1){
			 return;
		 }
		 if(Utilidades.obtenerInfoSesion().isNivelCentral()){
			 //Se comprueba si el usuario del nivel central tiene permisos para gestionar datos en la entidad seleccionada.
			 if(Operacion.esEntidadAutorizada(Utilidades.obtenerInfoSesion().getUsuarioId(), this.entidadSelectedId)){
				 this.autorizadoEnEntidadSelected = 1;
			 }else{
				 this.autorizadoEnEntidadSelected = 0;
			 }
		 }else{
			 this.autorizadoEnEntidadSelected = 1;
		 }
		
		 /* Se obtienen todos los municipios donde existan puestos de notificación
		 * coordinados por la entidad administrativa seleccionada.
		 * Se considera activo a todo Puesto de Notificación cuya fecha de cierre no ha sido declarada o si la
		 * fecha cierre es mayor que la fecha actual.*/
		 this.municipios = puestoNotificacionService.ListarMunicipiosPorEntidad(this.entidadSelectedId, false);
		 
		 if ((this.municipios!=null) && (this.municipios.size()>0)) {
				this.municipioSelectedId=this.municipios.get(0).getDivisionPoliticaId();
		 }
	 }
	 
	 /**
	 * Obtiene las unidades de salud con autorización explícita
	 * asociadas a una entidad administrativa (ímplicita) 
	 */
	public void obtenerUnidades() {
		
		this.unidades = null;
		this.unidadSelected=null;
		this.unidadSelectedId=0;

		// se filtran únicamente las unidades de salud a las cuales tiene autorización el
		// usuario y que hayan sido declaradas como puestos de notificación activas y aquellas
		// unidades, que no siendo puesto de notificación, tienen declarada la característica
		// funcional respectiva

		List<Unidad> oUnidades =Operacion.unidadesAutorizadasPorEntidad(this.infoSesion.getUsuarioId(), this.entidadSelectedId, 0,false
				,Utilidades.ES_PUESTO_NOTIFICACION +", " + Utilidades.DECLARA_MUESTREO_HEMATICO);

		if ((oUnidades!=null) && (oUnidades.size()>0)) {
			this.unidades=new ArrayList<Unidad>();
			for(Unidad oUnidad:oUnidades){
				if(oUnidad.getMunicipio().getDivisionPoliticaId()==this.municipioSelectedId){
					this.unidades.add(oUnidad);
				}
			}
		}
		if((this.unidades!=null) && (this.unidades.size()>0)){
			this.unidadSelectedId=this.unidades.get(0).getUnidadId();
			this.unidadSelected=this.unidades.get(0);
			
			// obtiene clave de la unidad de salud, en caso que sea un puesto de notificación
			// ya que podría ser únicamente una unidad que registra las fichas de muestreo hemático
			// notificadas por los puestos de notificación
			PuestoNotificacion oPuestoNotificacion = puestoNotificacionService.EncontrarPorUnidad(this.unidadSelectedId, 1);
			if (oPuestoNotificacion!=null) {
				this.puestoNotificacionId=oPuestoNotificacion.getPuestoNotificacionId();
				this.clave=oPuestoNotificacion.getClave();
			} else {
				this.puestoNotificacionId=0;
				this.clave=null;
			}
		}
		
	}
	
	public void cambiarUnidad() {
		this.clave=null;
		this.nombreColVol=null;
		this.puestoNotificacionId=0;
		
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
		
		// si el resultado oPuestoUnidad es nulo, implica que la unidad no es puesto
		// de notificación y solamente coordina a los colaboradores voluntarios para
		// la recepción de los muestreos hemáticos
		PuestoNotificacion oPuestoUnidad = puestoNotificacionService.EncontrarPorUnidad(oUnidad.getUnidadId(), 1);
		if (oPuestoUnidad!=null && oPuestoUnidad.getClave()!=null) {
			this.puestoNotificacionId=oPuestoUnidad.getPuestoNotificacionId();
			this.clave = oPuestoUnidad.getClave();
		} 
	}
	
	/**
	 * Obtiene los colvoles asociados a una unidad de salud, la cual puede o no ser
	 * un puesto de notificación
	 */
	public void listarColVoles() {
		
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
		
		this.colVolPuestos=puestoNotificacionService.ListarColVolPorUnidad(this.unidadSelectedId,this.filtroColVol,true);
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
	public void aceptarColVol() {
		this.puestoNotificacionId=this.colVolPuestoSelected.getPuestoNotificacionId();
		this.nombreColVol= this.colVolPuestoSelected.getNombreColVol();
		this.clave=this.colVolPuestoSelected.getClave();
	}
	
		
	/**
	 * Se ejecuta cuando el usuario directamente modifica la clave del puesto de
	 * notificación.  El sistema debe verificar si la clave corresponde a un colaborador
	 * voluntario o a una unidad de salud, en cuyo caso debe también verificar si ambos
	 * están asociados a una unidad de salud autorizada para el usuario.
	 */
	public void cambiarClave() {

		this.colVolPuestoSelected=null;
		this.unidadSelected=null;
		this.unidadSelectedId=0;
		this.nombreColVol=null;
		this.puestoNotificacionId=0;

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

		if (!this.unidades.isEmpty()) {
			for (Unidad oUnidad:this.unidades) {

				// implica que la unidad es un puesto de notificación

				if (oPuestoNotificacion.getUnidad()!=null) {
					if (oUnidad.getUnidadId()==oPuestoNotificacion.getUnidad().getUnidadId()) {
						this.unidadSelected=oPuestoNotificacion.getUnidad();
						this.unidadSelectedId=this.unidadSelected.getUnidadId();
						this.puestoNotificacionId=oPuestoNotificacion.getPuestoNotificacionId();
						break;
					}
				} else {
					
					// si la clave proporcionada corresponde a un puesto de notificación
					// y no pertenece a una unidad de salud, implica que es un colaborador
					// voluntario, y se debe verificar si dicho colvol esta relacionado
					// a una unidad de salud autorizada

					if (oUnidad.getUnidadId()==oPuestoNotificacion.getColVol().getUnidad().getUnidadId()) {
						this.unidadSelected=oPuestoNotificacion.getColVol().getUnidad();
						this.unidadSelectedId=this.unidadSelected.getUnidadId();
						this.nombreColVol=oPuestoNotificacion.getColVol().getSisPersona().getNombreCompleto();
						this.puestoNotificacionId=oPuestoNotificacion.getPuestoNotificacionId();

						ColVolPuesto oColVolPuesto = new ColVolPuesto();
						oColVolPuesto.setClave(oPuestoNotificacion.getClave());
						oColVolPuesto.setNombreColVol(oPuestoNotificacion.getColVol().getSisPersona().getNombreCompleto());
						oColVolPuesto.setPuestoNotificacionId(oPuestoNotificacion.getPuestoNotificacionId());
						this.colVolPuestoSelected=oColVolPuesto;
						break;
					}
				}
			}
			
			// si al finalizar, unidadSelected es nulo, implica que la unidad no esta autorizada
			if (this.unidadSelected==null) {
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR,
						"La clave no corresponde a una unidad de salud en la el Municipio seleccionado","");
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
				return; 
			}
			
		}
		
	}
	
	// Calcula la semana y año epidemiológico en base a la fecha en la cual se inicia la inspección
	public void calcularSemana() {
		this.anioEpi=null;
		this.semanaEpi=null;
		if(this.fechaEntrada!=null){
			this.anioEpi=BigDecimal.valueOf(Long.valueOf(CalendarioEPI.año(this.fechaEntrada)));
			this.semanaEpi=BigDecimal.valueOf(Long.valueOf(CalendarioEPI.semana(this.fechaEntrada)));
		}
	}
		
	public void agregar() {
		this.capaActiva = 2;
		iniciarCapa2();
	}
	
	public void guardar(){
		InfoResultado oResultado = new InfoResultado();
		VisitaPuesto oVisitaPuesto = new VisitaPuesto();
		
		oVisitaPuesto.setEntidadAdtva((EntidadAdtva) entidadService.Encontrar(this.entidadSelectedId).getObjeto());
		oVisitaPuesto.setUnidad(this.unidadSelected);
		oVisitaPuesto.setMunicipio(this.unidadSelected.getMunicipio());
		oVisitaPuesto.setPuestoNotificacion((PuestoNotificacion)puestoNotificacionService.Encontrar(this.puestoNotificacionId).getObjeto());
		oVisitaPuesto.setClave(this.clave);
		oVisitaPuesto.setFechaEntrada(this.fechaEntrada);
		oVisitaPuesto.setFechaSalida(this.fechaSalida);
		if(this.fechaEntrada!=null){
			oVisitaPuesto.setAñoEpidemiologico(BigDecimal.valueOf(Long.valueOf(CalendarioEPI.año(this.fechaEntrada))));
			oVisitaPuesto.setSemanaEpidemiologica(BigDecimal.valueOf(Long.valueOf(CalendarioEPI.semana(this.fechaEntrada))));
		}
		oVisitaPuesto.setHorarioInicio(this.horarioInicio);
		oVisitaPuesto.setHorarioFin(this.horarioFin);
		oVisitaPuesto.setTomaMuestras(this.tomaMuestraSelected);
		oVisitaPuesto.setStock(this.stockSelected);
		oVisitaPuesto.setReconocido(this.reconocidoSelected);
		oVisitaPuesto.setAtencionPacientes(this.atencionPacienteSelected);
		oVisitaPuesto.setProximaVisita(this.proximaVisita);
		
		if (this.visitaPuestoSelectedId!=0) {
			oVisitaPuesto.setVisitaPuestoId(this.visitaPuestoSelectedId);
			oResultado=visitaPuestoService.Guardar(oVisitaPuesto);
		} else {
			oVisitaPuesto.setUsuarioRegistro(this.infoSesion.getUsername());
			oVisitaPuesto.setFechaRegistro(Calendar.getInstance().getTime());
			oResultado=visitaPuestoService.Agregar(oVisitaPuesto);
		}
		
		if (oResultado.isOk()){
			if(this.visitaPuestoSelectedId!=0){
				oResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
			}else{
				oResultado.setMensaje(Mensajes.REGISTRO_AGREGADO);
			}
			onVisitaSelected();
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null){
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void eliminar() {
		if (this.visitaPuestoSelected == null || this.visitaPuestoSelected.getVisitaPuestoId()==0) {
			return;
		}
		InfoResultado oResultado = new InfoResultado();
		oResultado = visitaPuestoService.Eliminar( this.visitaPuestoSelected.getVisitaPuestoId());

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
	
	public void regresarCapa1() {
		iniciarCapa1();
		iniciarCapa2();
	}
	
	/**************************************************
	 * Métodos privados
	**************************************************/

	private void init(){
		this.capaActiva=1;
		this.infoSesion=Utilidades.obtenerInfoSesion();
		this.entidades= new ArrayList<EntidadAdtva>();
		
		//Obtiene los tres últimos años a la fecha actual para llemar el filtro
		// de años epidemiológicos a usar en la grilla de muestreos hemáticos.
		this.aniosEpidemiologicos = new LinkedList<SelectItem>();
	    Integer anioActual = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
	    this.anioEpiSelected=anioActual;
	    for (int i = 0; i < 3; i++) {
			this.aniosEpidemiologicos.add(new SelectItem(anioActual,anioActual.toString()));
			anioActual -= 1;
		}
	    
		// Si el usuario es del nivel central se obtentrán todas las entidades y únicamente
	    // podrá consultar la información de las fichas de vistas registradas en los puestos de
	    // de notificación de dichas entidades, por el contrario, si el usuario no es el del nivel
	    // central, únicamente se podrán seleccionar aquellas entidades administrativas
	    // asociadas de forma explícita al usuario, lo que le dará en consecuencia permisos para
	    // agregar o guardar registros.
	    if(this.infoSesion.isNivelCentral()){
	    	this.modo=1;
	    	this.entidades=Operacion.entidadesAutorizadas(this.infoSesion.getUsuarioId(),null);
	    }else{
	    	this.modo=0;
	    	this.entidades=Operacion.entidadesAutorizadas(this.infoSesion.getUsuarioId(),true);
	    }
	    	
		//Se optienen los municipios donde existán puesto de notificación para la entidad administrativa
	    //seleccionada.
		if ((this.entidades!=null) && (this.entidades.size()>0)) {
			this.entidadSelectedId=this.entidades.get(0).getEntidadAdtvaId();
			obtenerMunicipios();
		}
	    
	    iniDataModelVistaPuesto();
	}
	
	private void iniciarCapa1(){
		this.capaActiva=1;
		this.visitaPuestoSelected=null;
		this.visitaPuestoSelectedId=0;
	}
	
	private void iniciarCapa2(){
		
		// Objetos vinculados a los colvoles que son puestos de
		// notificación
		this.puestoNotificacionId=0;
		this.colVolPuestos=null;
		this.colVolPuestoSelected=null;
		this.clave="";
		this.filtroColVol="";
		this.nombreColVol="";
		this.fechaEntrada=null;
		this.fechaSalida=null;
		this.anioEpi=null;
		this.semanaEpi=null;
		this.divulgacionSelected=null;
		this.visibleCarnetSelected=null;
		this.horarioInicio=null;
		this.horarioFin=null;
		this.tomaMuestraSelected=null;
		this.stockSelected=null;
		this.reconocidoSelected=null;
		this.atencionPacienteSelected=null;
		this.proximaVisita=null;
		
		// Objetos vinculados a las unidades de salud.
		obtenerUnidades();
	}

	private void iniDataModelVistaPuesto() {
		this.visitasPuestos = new LazyDataModel<VisitaPuesto>(){
			private static final long serialVersionUID = 1L;
			@Override
			public List<VisitaPuesto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
				if(entidadSelectedId < 1){
					numVisitas=0;
					return null;
				}
				
				List<VisitaPuesto> oVisitaList=null;
				numVisitas=0;
				
				numVisitas = visitaPuestoService.ContarPositivosPorUnidad(entidadSelectedId,anioEpiSelected, municipioSelectedId);
				oVisitaList = visitaPuestoService.ListarPorEntidadAñoEpiYMunicipio(entidadSelectedId,anioEpiSelected,
						 municipioSelectedId, first, pageSize, numVisitas);
				this.setRowCount(numVisitas);
				return oVisitaList;
			}
		};		
	}
	
	
	/**************************************************
	 * Métodos de acceso a propiedades
	 **************************************************/
	
	public int getCapaActiva() {
		return capaActiva;
	}

	public void setCapaActiva(int capaActiva) {
		this.capaActiva = capaActiva;
	}

	public int getModo() {
		return modo;
	}

	public void setModo(int modo) {
		this.modo = modo;
	}

	public long getEntidadSelectedId() {
		return entidadSelectedId;
	}

	public void setEntidadSelectedId(long entidadSelectedId) {
		this.entidadSelectedId = entidadSelectedId;
	}

	public long getMunicipioSelectedId() {
		return municipioSelectedId;
	}

	public void setMunicipioSelectedId(long municipioSelectedId) {
		this.municipioSelectedId = municipioSelectedId;
	}

	public int getAnioEpiSelected() {
		return anioEpiSelected;
	}

	public void setAnioEpiSelected(int anioEpiSelected) {
		this.anioEpiSelected = anioEpiSelected;
	}

	public VisitaPuesto getVisitaPuestoSelected() {
		return visitaPuestoSelected;
	}

	public void setVisitaPuestoSelected(VisitaPuesto visitaPuestoSelected) {
		this.visitaPuestoSelected = visitaPuestoSelected;
	}

	public long getVisitaPuestoSelectedId() {
		return visitaPuestoSelectedId;
	}

	public void setVisitaPuestoSelectedId(long visitaPuestoSelectedId) {
		this.visitaPuestoSelectedId = visitaPuestoSelectedId;
	}

	public int getNumVisitas() {
		return numVisitas;
	}

	public void setNumVisitas(int numVisitas) {
		this.numVisitas = numVisitas;
	}

	public List<EntidadAdtva> getEntidades() {
		return entidades;
	}

	public List<DivisionPolitica> getMunicipios() {
		return municipios;
	}

	public List<SelectItem> getAniosEpidemiologicos() {
		return aniosEpidemiologicos;
	}

	public LazyDataModel<VisitaPuesto> getVisitasPuestos() {
		return visitasPuestos;
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

	public ColVolPuesto getColVolPuestoSelected() {
		return colVolPuestoSelected;
	}

	public void setColVolPuestoSelected(ColVolPuesto colVolPuestoSelected) {
		this.colVolPuestoSelected = colVolPuestoSelected;
	}

	public long getPuestoNotificacionId() {
		return puestoNotificacionId;
	}

	public void setPuestoNotificacionId(long puestoNotificacionId) {
		this.puestoNotificacionId = puestoNotificacionId;
	}

	public String getNombreColVol() {
		return nombreColVol;
	}

	public void setNombreColVol(String nombreColVol) {
		this.nombreColVol = nombreColVol;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public long getAutorizadoEnEntidadSelected() {
		return autorizadoEnEntidadSelected;
	}

	public List<Unidad> getUnidades() {
		return unidades;
	}

	public List<ColVolPuesto> getColVolPuestos() {
		return colVolPuestos;
	}

	public String getFiltroColVol() {
		return filtroColVol;
	}

	public Date getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	
	public BigDecimal getAnioEpi() {
		return anioEpi;
	}

	public void setAnioEpi(BigDecimal anioEpi) {
		this.anioEpi = anioEpi;
	}

	public BigDecimal getSemanaEpi() {
		return semanaEpi;
	}

	public void setSemanaEpi(BigDecimal semanaEpi) {
		this.semanaEpi = semanaEpi;
	}

	public BigDecimal getDivulgacionSelected() {
		return divulgacionSelected;
	}

	public void setDivulgacionSelected(BigDecimal divulgacionSelected) {
		this.divulgacionSelected = divulgacionSelected;
	}

	public BigDecimal getVisibleCarnetSelected() {
		return visibleCarnetSelected;
	}

	public void setVisibleCarnetSelected(BigDecimal visibleCarnetSelected) {
		this.visibleCarnetSelected = visibleCarnetSelected;
	}

	public Date getHorarioInicio() {
		return horarioInicio;
	}

	public void setHorarioInicio(Date horarioInicio) {
		this.horarioInicio = horarioInicio;
	}

	public Date getHorarioFin() {
		return horarioFin;
	}

	public void setHorarioFin(Date horarioFin) {
		this.horarioFin = horarioFin;
	}

	public BigDecimal getTomaMuestraSelected() {
		return tomaMuestraSelected;
	}

	public void setTomaMuestraSelected(BigDecimal tomaMuestraSelected) {
		this.tomaMuestraSelected = tomaMuestraSelected;
	}

	public BigDecimal getStockSelected() {
		return stockSelected;
	}

	public void setStockSelected(BigDecimal stockSelected) {
		this.stockSelected = stockSelected;
	}

	public BigDecimal getAtencionPacienteSelected() {
		return atencionPacienteSelected;
	}

	public void setAtencionPacienteSelected(BigDecimal atencionPacienteSelected) {
		this.atencionPacienteSelected = atencionPacienteSelected;
	}

	public BigDecimal getReconocidoSelected() {
		return reconocidoSelected;
	}

	public void setReconocidoSelected(BigDecimal reconocidoSelected) {
		this.reconocidoSelected = reconocidoSelected;
	}

	public void setFiltroColVol(String filtroColVol) {
		this.filtroColVol = filtroColVol;
	}

	public Date getProximaVisita() {
		return proximaVisita;
	}

	public void setProximaVisita(Date proximaVisita) {
		this.proximaVisita = proximaVisita;
	}

	
}
