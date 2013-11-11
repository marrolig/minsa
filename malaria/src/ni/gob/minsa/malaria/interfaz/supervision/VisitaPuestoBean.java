package ni.gob.minsa.malaria.interfaz.supervision;

import java.io.Serializable;
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

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.datos.estructura.UnidadDA;
import ni.gob.minsa.malaria.datos.poblacion.DivisionPoliticaDA;
import ni.gob.minsa.malaria.datos.supervision.VisitaPuestoDA;
import ni.gob.minsa.malaria.datos.vigilancia.PuestoNotificacionDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.supervision.VisitaPuesto;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoHematico;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoNotificacion;
import ni.gob.minsa.malaria.modelo.vigilancia.noEntidad.ColVolPuesto;
import ni.gob.minsa.malaria.reglas.Operacion;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;
import ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService;
import ni.gob.minsa.malaria.servicios.supervision.VisitaPuestoService;
import ni.gob.minsa.malaria.servicios.vigilancia.PuestoNotificacionService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

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
	private int numColVolPuestos;
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
	
	private static EntidadAdtvaService entidadService = new EntidadAdtvaDA();
	private static PuestoNotificacionService puestoNotificacionService = new PuestoNotificacionDA();
	private static UnidadService unidadService = new UnidadDA();
	private static DivisionPoliticaService divisionPoliticaService = new DivisionPoliticaDA();
	private static VisitaPuestoService visitaPuestoService = new VisitaPuestoDA();
	/**************************************************
	 * Constructor
	 **************************************************/
	
	public VisitaPuestoBean(){
		init();
	}
	
	/**************************************************
	 * Eventos
	 **************************************************/
	
	public void agregar(){
		this.capaActiva=2;
		iniciarCapa2();
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

			List<Unidad> oUnidades =ni.gob.minsa.malaria.reglas.Operacion.unidadesAutorizadasPorEntidad(this.infoSesion.getUsuarioId(), this.entidadSelectedId, 0,false
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
					this.clave=oPuestoNotificacion.getClave();
				} else {
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
							this.entidadSelectedId=this.unidadSelected.getEntidadAdtva().getEntidadAdtvaId();
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
							this.entidadSelectedId=this.unidadSelected.getEntidadAdtva().getEntidadAdtvaId();

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
	
	/**************************************************
	 * Métodos privados
	**************************************************/
	
	private void iniciarCapa2(){
		
		// Objetos vinculados a las unidades de salud.
		obtenerUnidades();
		
		// Objetos vinculados a los colvoles que son puestos de
		// notificación
		this.colVolPuestos=null;
		this.numColVolPuestos=0;
		this.colVolPuestoSelected=null;
		this.puestoNotificacionId=0;
		this.filtroColVol="";
		this.nombreColVol="";
	}
		
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
	
	
	
	
}
