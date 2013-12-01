// -----------------------------------------------
// ColVolBean.java
// -----------------------------------------------

package ni.gob.minsa.malaria.interfaz.vigilancia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.ejbPersona.dto.Persona;
import ni.gob.minsa.malaria.datos.estructura.UnidadDA;
import ni.gob.minsa.malaria.datos.general.CatalogoElementoDA;
import ni.gob.minsa.malaria.datos.general.MarcadorDA;
import ni.gob.minsa.malaria.datos.general.ParametroDA;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadDA;
import ni.gob.minsa.malaria.datos.vigilancia.ColVolDA;
import ni.gob.minsa.malaria.datos.vigilancia.MuestreoHematicoDA;
import ni.gob.minsa.malaria.datos.vigilancia.PuestoComunidadDA;
import ni.gob.minsa.malaria.datos.vigilancia.PuestoNotificacionDA;
import ni.gob.minsa.malaria.interfaz.sis.PersonaBean;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.general.ClaseAccesibilidad;
import ni.gob.minsa.malaria.modelo.general.Marcador;
import ni.gob.minsa.malaria.modelo.general.Parametro;
import ni.gob.minsa.malaria.modelo.general.TipoTransporte;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.vigilancia.ColVol;
import ni.gob.minsa.malaria.modelo.vigilancia.ColVolAcceso;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoComunidad;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoNotificacion;
import ni.gob.minsa.malaria.reglas.Operacion;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.servicios.general.MarcadorService;
import ni.gob.minsa.malaria.servicios.general.ParametroService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.servicios.vigilancia.ColVolService;
import ni.gob.minsa.malaria.servicios.vigilancia.MuestreoHematicoService;
import ni.gob.minsa.malaria.servicios.vigilancia.PuestoComunidadService;
import ni.gob.minsa.malaria.servicios.vigilancia.PuestoNotificacionService;
import ni.gob.minsa.malaria.soporte.NivelZoom;
import ni.gob.minsa.malaria.soporte.Utilidades;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * Servicio para la capa de presentación de la página 
 * poblacion/colVol.xhtml
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 26/06/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class ColVolBean implements Serializable {

	private static final long serialVersionUID = 1L;

	protected InfoSesion infoSesion;
	private int capaActiva;
	
	private int modo;
	
	// el puesto de notificación tiene muestreos hemáticos vinculados
	private boolean puestoConActividad;
	
	// objetos para poblar el combo de entidades administrativas
	private List<EntidadAdtva> entidades;
	
	// identificador de la entidad seleccionada
	private long entidadSelectedId;
	
	// objetos para poblar el combo de unidades de salud, unidad seleccionada e identificador
	private List<Unidad> unidades;
	private Unidad unidadSelected;
	private long unidadSelectedId;

	private boolean mostrarSoloActivos;
	
	// lista de objetos para la grilla de colvoles
	private LazyDataModel<ColVol> colVoles;
	private ColVol colVolSelected;
	
	// true si el colVol se encuentra activo y la persona no puede ser declarada nuevamente
	private boolean colVolActivo;
	private long colVolId;

	// literal utilizada para filtrar a los colaboradores voluntarios en la grilla
	private String filtroColVol;
	private int numRegistros;

	// identificación de la persona a la cual se asocia el colaborador voluntario
	private long personaId;
	private String edad;
	private Date fechaInicio;
	private Date fechaFin;
	private String observaciones;
	private BigDecimal longitud;
	private BigDecimal latitud;
	
	// atributos vinculados al puesto de notificación
	
	private long puestoNotificacionId;
	private String clave;
	private Date fechaApertura;
	private Date fechaCierre;
	private List<PuestoComunidad> puestosComunidades;
	
	// ------------------------------------------------------------------------------
	// atributos vinculados a las comunidades atendidas por el puesto de notificación
	// ------------------------------------------------------------------------------
	
	private Comunidad comunidadPuestoSelected;
	
	// este atributo es inicializado desde el interfaz del usuario al 
	// seleccionar una comunidad vinculada al puesto de notificación para ser eliminada
	
	private long puestoComunidadSelectedId;
	
	// --------------------------------------------------------
	// atributos vinculados al acceso del colVol
	// --------------------------------------------------------
	
	private String comoLlegar;
	private String puntoReferencia;
	
	private List<TipoTransporte> tiposTransportes;
	private List<TipoTransporte> tiposTransportesActivos;
	private long tipoTransporteSelectedId;
	
	private List<ClaseAccesibilidad> clasesAccesibilidad;
	private List<ClaseAccesibilidad> clasesAccesibilidadActivos;
	private long claseAccesibilidadSelectedId;
	
	private BigDecimal distancia;
	private BigDecimal tiempo;
	
	// ----------------------------------------------------------
	// atributos vinculados a la gestión de personas
	// ----------------------------------------------------------
	
	// persona seleccionada desde la lista de personas resultantes de la busqueda al componente de personas
	private Persona personaSelected;

	// comunidad seleccionada de la lista de sugerencias del control de autocompletar
	private Comunidad comunidadSelected;
	

	// ----------------------------------------------------------
	// propiedades vinculadas al mapa
	// ----------------------------------------------------------
	
	// el centro del mapa será considerado como el centro del polígono
	// establecido por los diferentes puntos de longitud y latitud de los sitios
	// de referencia.  Si no existe ningún punto de referencia establecido
	// se colocará el mapa tomando como centro la longitud y latitud de la comunidad,
	// si ésta no se encuentra establecida, se tomará la longitud y latitud del
	// municipio donde se encuentra ubicada la comunidad.
	private String centroMapa;
	private MapModel mapaModelo;  
    private Marker marcadorMapa;
    private String urlMarcadorColVolM;
    private String urlMarcadorColVolF;
    private int zoom;
	private NivelZoom nivelZoom;
    private String coordenadasPais;
	
	private static ColVolService colVolService = new ColVolDA();
	private static UnidadService unidadService = new UnidadDA();
	private static ComunidadService comunidadService = new ComunidadDA();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TipoTransporte,Integer> tipoTransporteService=new CatalogoElementoDA(TipoTransporte.class,"TipoTransporte");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<ClaseAccesibilidad,Integer> claseAccesibilidadService=new CatalogoElementoDA(ClaseAccesibilidad.class,"ClaseAccesibilidad");
	private static MarcadorService marcadorService = new MarcadorDA();
	private static ParametroService parametroService = new ParametroDA();
	
	private static PuestoNotificacionService puestoNotificacionService = new PuestoNotificacionDA();
	private static MuestreoHematicoService muestreoHematicoService = new MuestreoHematicoDA();
	
	public ColVolBean() {
		
		this.nivelZoom=new NivelZoom();
		this.capaActiva=1;
		this.colVolActivo=false;
		this.puestoConActividad=false;
		this.mostrarSoloActivos=true;
		
		this.infoSesion=Utilidades.obtenerInfoSesion();
		this.entidades= new ArrayList<EntidadAdtva>();
		this.unidades= new ArrayList<Unidad>();
		this.filtroColVol="";

		this.entidadSelectedId=0;
		this.unidadSelectedId=0;

		this.tiposTransportesActivos=tipoTransporteService.ListarActivos();
		this.clasesAccesibilidad=claseAccesibilidadService.ListarActivos();

		this.urlMarcadorColVolM="";
		this.urlMarcadorColVolF="";

		InfoResultado oResultadoParametro=parametroService.Encontrar("MAPA_COLVOL_HOMBRE");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
			Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
			InfoResultado oResultadoMarcador=marcadorService.Encontrar(oParametro.getValor());
			if (oResultadoMarcador!=null && oResultadoMarcador.isOk()) {
				this.urlMarcadorColVolM=((Marcador)oResultadoMarcador.getObjeto()).getUrl();
			}
		}

		oResultadoParametro=parametroService.Encontrar("MAPA_COLVOL_MUJER");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
			Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
			InfoResultado oResultadoMarcador=marcadorService.Encontrar(oParametro.getValor());
			if (oResultadoMarcador!=null && oResultadoMarcador.isOk()) {
				this.urlMarcadorColVolF=((Marcador)oResultadoMarcador.getObjeto()).getUrl();
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
		
		this.colVoles = new LazyDataModel<ColVol>() {

			private static final long serialVersionUID = 1L;

			@Override
		    public List<ColVol> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
			
				if (unidadSelectedId==0) {
					return null;
				}
				
				List<ColVol> colVolList=null;
				numRegistros=0;
			
				numRegistros=colVolService.ContarPorUnidad(unidadSelectedId, filtroColVol.trim(), mostrarSoloActivos);
				colVolList=colVolService.ListarPorUnidad(unidadSelectedId, filtroColVol.trim(),mostrarSoloActivos, first, pageSize,numRegistros);

				if (!(colVolList!=null && colVolList.size()>0)) {
					FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "No se encontraron comunidades con los parámetros de búsqueda seleccionados","Verifique la literal de búsqueda, el municipio y/o sector");
					if (msg!=null)
						FacesContext.getCurrentInstance().addMessage(null, msg);
				}
				this.setRowCount(numRegistros);
				return colVolList;
			}

		};
		
		this.colVoles.setRowCount(numRegistros);

		iniciarCapa1();

	}
	
	public void iniciaPersonas() {

		// inicializa las variables del componente de personas
		FacesContext context = FacesContext.getCurrentInstance();
		PersonaBean personaBean = (PersonaBean)context.getApplication().evaluateExpressionGet(context, "#{personaBean}", PersonaBean.class);
		personaBean.setPersonaSelected(null);
		personaBean.setModo(0);
		personaBean.setTextoBusqueda("");
		personaBean.setPersonaListaSelected(null);

	}

	// inicia las variables primarias de modo tal que
	// ningun colVol y persona haya sido seleccionada
	public void iniciarCapas() {

		this.colVolSelected=null;
		this.personaSelected=null;
		this.puestoNotificacionId=0;
		this.puestosComunidades=new ArrayList<PuestoComunidad>();
		this.colVolId=0;
		this.filtroColVol="";
		this.capaActiva=1;

	}
	
	public void iniciarCapa1() {
		
		iniciarCapa2();
		iniciarCapas();
		iniciaPersonas();
		
	}
	
	public void iniciarCapa2() {
		
		this.capaActiva=2;
		this.edad=null;
		this.fechaInicio=null;
		this.fechaFin=null;
		this.observaciones=null;
		this.longitud=null;
		this.latitud=null;
		this.comoLlegar=null;
		this.puntoReferencia=null;
		this.distancia=null;
		this.tiempo=null;
		this.clave=null;
		this.fechaApertura=null;
		this.fechaCierre=null;
		this.puestoConActividad=false;
		this.colVolActivo=false;
		
		this.tipoTransporteSelectedId=0;
		this.tiposTransportes=this.tiposTransportesActivos;

		this.claseAccesibilidadSelectedId=0;
		this.clasesAccesibilidad=this.clasesAccesibilidadActivos;
		
		this.comunidadPuestoSelected=null;
	
	}

	public void regresarCapa1() {
		
		iniciarCapa1();
		
	}

	public void buscarColVol() {
		
		if (this.filtroColVol.trim().length()>0 && this.filtroColVol.trim().length()<3) { 

			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, Mensajes.RESTRICCION_BUSQUEDA,"");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		iniciarColVoles();
		
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
		
		iniciarCapa1();
		
	}
	
	
	public void iniciarColVoles() {
		
		this.colVolSelected=null;
		this.personaSelected=null;
		
	}
	
	/**
	 * Quita el filtro de la busqueda de colvoles, obtiene todos los colvoles
	 * vinculados a la unidad de salud seleccionada 
	 */
	public void quitarFiltro() {
		
		this.filtroColVol="";
		iniciarColVoles();
		
	}

	/**
	 * Evento que se ejecuta cuando el usuario selecciona un colVol
	 * de la grilla.  Traslada todos los valores del objeto seleccionado 
	 * a los controles del panel de detalle 
	 */
	public void onColVolSelected(SelectEvent iEvento) { 
		
		this.capaActiva=2;
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
				this.tiposTransportes=this.tiposTransportesActivos;
			}
			
			if (this.colVolSelected.getColVolAcceso().getClaseAccesibilidad()!=null) {
				this.claseAccesibilidadSelectedId=this.colVolSelected.getColVolAcceso().getClaseAccesibilidad().getCatalogoId();
				this.clasesAccesibilidad=claseAccesibilidadService.ListarActivos(this.colVolSelected.getColVolAcceso().getClaseAccesibilidad().getCodigo());
			} else {
				this.clasesAccesibilidad=this.clasesAccesibilidadActivos;
			}
			
			this.comoLlegar=this.colVolSelected.getColVolAcceso().getComoLlegar();
			this.distancia=this.colVolSelected.getColVolAcceso().getDistancia();
			this.tiempo=this.colVolSelected.getColVolAcceso().getTiempo();
			this.puntoReferencia=this.colVolSelected.getColVolAcceso().getPuntoReferencia();
			
		} else {
			
			this.comoLlegar=null;
			this.distancia=null;
			this.tiempo=null;
			this.puntoReferencia=null;
			this.tiposTransportes=tipoTransporteService.ListarActivos();
			this.clasesAccesibilidad=claseAccesibilidadService.ListarActivos();
		}
		
		// obtiene los datos vinculados al puesto de notificación

		this.puestoNotificacionId=0;
		this.clave=null;
		this.fechaApertura=null;
		this.fechaCierre=null;
		
		this.puestoConActividad=false;
		this.comunidadPuestoSelected=null;

		if (this.colVolSelected.getPuestoNotificacion()!=null) {
			this.puestoNotificacionId=this.colVolSelected.getPuestoNotificacion().getPuestoNotificacionId();
			this.clave=this.colVolSelected.getPuestoNotificacion().getClave();
			this.fechaApertura=this.colVolSelected.getPuestoNotificacion().getFechaApertura();
			this.fechaCierre=this.colVolSelected.getPuestoNotificacion().getFechaCierre();
			this.puestosComunidades=this.colVolSelected.getPuestoNotificacion().getComunidadesPuesto();

			// verifica si el puesto tiene muestreos hemáticos vinculados
			int iCuentaMuestreos = muestreoHematicoService.ContarPorPuesto(this.puestoNotificacionId, 0, 0);
			if (iCuentaMuestreos>0) {
				this.puestoConActividad=true;
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "El puesto de notificación tiene datos vinculados por lo que algunos datos no pueden ser modificados.","Número de muestreos hemáticos vinculados: "+String.valueOf(iCuentaMuestreos));
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			}

		}
	
		this.observaciones=this.colVolSelected.getObservaciones();
		this.longitud=this.colVolSelected.getLongitud();
		this.latitud=this.colVolSelected.getLatitud();
		this.fechaInicio=this.colVolSelected.getFechaInicio();
		this.fechaFin=this.colVolSelected.getFechaFin();
		
		// si al seleccionar un colVol, éste se encuentra pasivo, verifica 
		// si la persona asociada a éste se encuentra declarada nuevamente como 
		// colVol activo, en cuyo caso el registro no puede ser modificado.
		
		if (this.colVolSelected.getFechaFin()!=null && this.colVolSelected.getFechaFin().before(Calendar.getInstance().getTime())) {
			// el colvol seleccionado tiene fecha fin declarada y la fecha es anterior a la fecha actual
			// por lo que se considera pasivo
			ColVol oColVol = colVolService.EncontrarPorPersona(this.personaId);
			if (oColVol!=null && oColVol.getColVolId()!=this.colVolId) {
				// implica que encontro otro registro de la misma persona como colaborador activo
				this.colVolActivo=true;
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La persona se encuentra registrada como Colaborador Voluntario ACTIVO; el registro actual no puede ser modificado.","La unidad de la cual depende dicho ColVol es " + oColVol.getUnidad().getNombre() + " en " + oColVol.getUnidad().getMunicipio().getNombre());
				if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
		
		// inicializa las variables del componente de personas
		FacesContext context = FacesContext.getCurrentInstance();
		PersonaBean personaBean = (PersonaBean)context.getApplication().evaluateExpressionGet(context, "#{personaBean}", PersonaBean.class);
		personaBean.setPersonaSelected(this.personaSelected);
		personaBean.setModo(1);
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
		}
	}
	
	public void generarMapa(ActionEvent pEvento) {
		
		this.mapaModelo = new DefaultMapModel();  
        BigDecimal iSumaLatitudes=BigDecimal.ZERO;
        BigDecimal iSumaLongitudes=BigDecimal.ZERO;
        int iNumColVoles=0;
        this.centroMapa="0,0";
        this.zoom=nivelZoom.PAIS;
        
        List<ColVol> oColVoles=colVolService.ListarPorUnidad(this.unidadSelectedId);
		for (ColVol oColVol:oColVoles) {
			if (oColVol.getLatitud()!=null && oColVol.getLongitud()!=null) {
				iSumaLatitudes=iSumaLatitudes.add(oColVol.getLatitud());
				iSumaLongitudes=iSumaLongitudes.add(oColVol.getLongitud());
				iNumColVoles++;
				
				LatLng iCoordenada = new LatLng(oColVol.getLatitud().doubleValue(),oColVol.getLongitud().doubleValue());
				Marker oMarker = new Marker(iCoordenada, oColVol.getSisPersona().getNombreCompleto());
				if (oColVol.getSisPersona().getSexo().getCodigo().equals(Utilidades.SEXO_MASCULINO)) {
					if (!this.urlMarcadorColVolM.isEmpty()) {
						oMarker.setIcon(this.urlMarcadorColVolM);
					} 
				}
				if (oColVol.getSisPersona().getSexo().getCodigo().equals(Utilidades.SEXO_FEMENINO)) {
					if (!this.urlMarcadorColVolF.isEmpty()) {
						oMarker.setIcon(this.urlMarcadorColVolF);
					} 
				}
				
				this.mapaModelo.addOverlay(oMarker);
			}
		}
		
		if (iNumColVoles!=0) {
			this.centroMapa=iSumaLatitudes.divide(new BigDecimal(iNumColVoles),6,RoundingMode.HALF_UP).toPlainString() + "," + iSumaLongitudes.divide(new BigDecimal(iNumColVoles),6,RoundingMode.HALF_UP).toPlainString();
			this.zoom=nivelZoom.MUNICIPIO;
		}
		
		if (this.unidadSelected.getLatitud()!=null && this.unidadSelected.getLongitud()!=null) {

			if (iNumColVoles==0) {
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
	
	public void activarPrimerCapa(ActionEvent pEvento) {
		
		iniciarCapa1();
		
	}

	/**
	 * Se ejecuta cuando el usuario pulsa en el botón agregar
	 * y consiste en la inicialización de las diferentes propiedades
	 * para colocar el estado del formulario en modo de agregar
	 */
	public void agregar(ActionEvent pEvento) {
		
		iniciarCapas();
	    iniciaPersonas();
		
	}
	
	/**
	 * Se ejecuta cuando el usuario pulsa en el botón guardar.
	 * Este proceso incluye guardar un nuevo registro o actualizar
	 * un registro existente. <br>
	 */
	public void guardar(ActionEvent pEvento) {
		
		if (this.personaSelected==null) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La declaración de la persona a la cual se asocia el colaborador voluntario es requerida","");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		// La persona solo puede declararse nuevamente como colVol si no se encuentra
		// activa, por lo que si se encuentra activa tampoco puede declararse como puesto de notificación
		ColVolService colVolService = new ColVolDA();
		ColVol oColVolExistente=colVolService.EncontrarPorPersona(this.personaSelected.getPersonaId());
		if (oColVolExistente!=null && oColVolExistente.getColVolId()!=this.colVolId) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La persona se encuentra registrada como Colaborador Voluntario ACTIVO","La unidad de la cual depende dicho ColVol es " + oColVolExistente.getUnidad().getNombre() + " en " + oColVolExistente.getUnidad().getMunicipio().getNombre());
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		InfoResultado oResultado = new InfoResultado();
		ColVol oColVol = new ColVol();
		oColVol.setFechaInicio(this.fechaInicio);
		oColVol.setFechaFin(this.fechaFin); 
		oColVol.setLatitud(this.latitud);
		oColVol.setLongitud(this.longitud);
		oColVol.setObservaciones(this.observaciones!=null && !this.observaciones.trim().isEmpty()?this.observaciones:null);
		oColVol.setUnidad((Unidad)unidadService.Encontrar(this.unidadSelectedId).getObjeto());

		ColVolAcceso oColVolAcceso = new ColVolAcceso();

		if (this.distancia!=null || this.tiempo!=null || 
				(this.puntoReferencia!=null && !this.puntoReferencia.trim().isEmpty()) ||
				(this.comoLlegar!=null && !this.comoLlegar.trim().isEmpty()) ||
				this.tipoTransporteSelectedId!=0 || this.claseAccesibilidadSelectedId!=0) {

			oColVolAcceso.setColVol(oColVol);
			oColVolAcceso.setComoLlegar(this.comoLlegar!=null && !this.comoLlegar.trim().isEmpty()?this.comoLlegar:null);
			oColVolAcceso.setDistancia(this.distancia);
			oColVolAcceso.setTiempo(this.tiempo);
			oColVolAcceso.setPuntoReferencia(this.puntoReferencia!=null && !this.puntoReferencia.trim().isEmpty()?this.puntoReferencia:null);
			
			if (this.tipoTransporteSelectedId!=0) {
				oColVolAcceso.setTipoTransporte((TipoTransporte)tipoTransporteService.Encontrar(this.tipoTransporteSelectedId).getObjeto());
			}
			
			if (this.claseAccesibilidadSelectedId!=0) {
				oColVolAcceso.setClaseAccesibilidad((ClaseAccesibilidad)claseAccesibilidadService.Encontrar(this.claseAccesibilidadSelectedId).getObjeto());
			}

		} else {
			
			oColVolAcceso=null;
		}

		oColVol.setColVolAcceso(oColVolAcceso);

		// Datos del puesto de notificación
		
		PuestoNotificacion oPuestoNotificacion = new PuestoNotificacion();
		oPuestoNotificacion.setClave(this.clave!=null && !this.clave.trim().isEmpty()?this.clave.trim():null);
		oPuestoNotificacion.setFechaApertura(this.fechaApertura);
		oPuestoNotificacion.setFechaCierre(this.fechaCierre);
		oPuestoNotificacion.setPuestoNotificacionId(this.puestoNotificacionId);
		if (this.puestoNotificacionId==0 &&
				this.clave!=null && !this.clave.trim().isEmpty() &&
				this.fechaApertura!=null) {
			oPuestoNotificacion.setFechaRegistro(Calendar.getInstance().getTime());
			oPuestoNotificacion.setUsuarioRegistro(this.infoSesion.getUsername());
		}
		
		oPuestoNotificacion.setColVol(oColVol);
		
		if (this.colVolId!=0) {
				oColVol.setColVolId(this.colVolId);
				if (oColVolAcceso!=null) {
					if (this.colVolSelected.getColVolAcceso()!=null) {
						// hay datos de acceso que registrar y existían datos de acceso previamente
						oColVolAcceso.setColVolAccesoId(this.colVolSelected.getColVolAcceso().getColVolAccesoId());
					} else {
						// hay datos de acceso que registrar y no existían datos de acceso en el registro del colVol
						oColVolAcceso.setFechaRegistro(Calendar.getInstance().getTime());
						oColVolAcceso.setUsuarioRegistro(this.infoSesion.getUsername());
					}
				}
				oColVol.setUsuarioRegistro(this.colVolSelected.getUsuarioRegistro());
				oResultado=colVolService.Guardar(oColVol,this.personaSelected,oPuestoNotificacion);
		} else {
			// es un nuevo registro y por tanto debe inicializar los valores de fecha Registro y Usuario Registro
			oColVol.setFechaRegistro(Calendar.getInstance().getTime());
			oColVol.setUsuarioRegistro(this.infoSesion.getUsername());
			if (oColVolAcceso!=null) {
				oColVolAcceso.setUsuarioRegistro(this.infoSesion.getUsername());
				oColVolAcceso.setFechaRegistro(Calendar.getInstance().getTime());
			}

			oResultado=colVolService.Agregar(oColVol,this.personaSelected,oPuestoNotificacion);
		}

		if (oResultado.isOk()){
			iniciarCapa1();
			oResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
		} 
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
		
	}
	
	public void eliminar(ActionEvent pEvento){

		if (this.colVolId==0) return;
		
		InfoResultado oResultado = new InfoResultado();
		oResultado=colVolService.Eliminar(this.colVolId);
		
		if (oResultado.isOk()){
			iniciarCapa1();
			oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
		}
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	/**
	 * Elimina la comunidad asociada al puesto de notificación.  La eliminación
	 * únicamente es posible si existen mas de una comunidad asociada al puesto
	 * de notificación, ya que al menos debe existir una comunidad vinculada a
	 * dicho puesto. 
	 */
	public void eliminarPuestoComunidad(ActionEvent pEvento) {
		
		if (this.puestoComunidadSelectedId!=0) { 
			PuestoComunidadService puestoComunidadService = new PuestoComunidadDA();
			InfoResultado oResultadoEliminar = puestoComunidadService.Eliminar(this.puestoComunidadSelectedId);
			if (!oResultadoEliminar.isOk()) {
				FacesMessage msg = Mensajes.enviarMensaje(oResultadoEliminar);
				if (msg!=null)
					FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				this.puestosComunidades=puestoComunidadService.ComunidadesPorPuesto(this.puestoNotificacionId);
			}
			
		} else {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN,"No se pudo acceder al identificador de la comunidad para realizar esta operación","Esto puede ser debido a la velocidad de la conexión a internet.  Inténtelo nuevamente.");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public List<Comunidad> completarComunidad(String query) {
		
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades=comunidadService.ComunidadesPorUnidad(this.unidadSelectedId, query, null, 0, 10, 200);
		
		return oComunidades;

	}
	
	/**
	 * Se desencadena cual el usuario cambia de municipio, con lo cual
	 * la comunidad que atenderá el puesto de notificación debe de ser inicializada
	 */
	public void iniciarComunidadPuesto() {
		
		this.comunidadPuestoSelected=null;
	}
	
	/**
	 * Vincula una comunidad al puesto de notificación, con lo cual
	 * se considera que dicha comunidad será atendida por el colaborador
	 * voluntario
	 */
	public void guardarComunidad(ActionEvent pEvento) {

		if (this.comunidadPuestoSelected==null) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "Seleccione la comunidad de la lista de comunidades propuesta","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		PuestoComunidad oPuestoComunidad = new PuestoComunidad();
		oPuestoComunidad.setComunidad(this.comunidadPuestoSelected);
		oPuestoComunidad.setFechaRegistro(Calendar.getInstance().getTime());
		oPuestoComunidad.setUsuarioRegistro(this.infoSesion.getUsername());
		
		PuestoNotificacion oPuestoNotificacion = puestoNotificacionService.EncontrarPorColVol(this.colVolId);
		if (oPuestoNotificacion==null) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "El puesto de notificación al cual se encuentra vinculado el colaborador voluntario no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}

		oPuestoComunidad.setPuestoNotificacion(oPuestoNotificacion);

		PuestoComunidadService puestoComunidadService = new PuestoComunidadDA();
		InfoResultado oResultadoAgregar = puestoComunidadService.Agregar(oPuestoComunidad);
		if (!oResultadoAgregar.isOk()) {
			FacesMessage msg = Mensajes.enviarMensaje(oResultadoAgregar);
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		this.puestosComunidades=puestoComunidadService.ComunidadesPorPuesto(oPuestoNotificacion.getPuestoNotificacionId());
		this.comunidadPuestoSelected=null;
		
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

	public LazyDataModel<ColVol> getColVoles() {
		return colVoles;
	}

	public void setColVoles(LazyDataModel<ColVol> colVoles) {
		this.colVoles = colVoles;
	}

	public ColVol getColVolSelected() {
		return colVolSelected;
	}

	public void setColVolSelected(ColVol colVolSelected) {
		this.colVolSelected = colVolSelected;
	}

	public long getColVolId() {
		return colVolId;
	}

	public void setColVolId(long colVolId) {
		this.colVolId = colVolId;
	}

	public String getFiltroColVol() {
		return filtroColVol;
	}

	public void setFiltroColVol(String filtroColVol) {
		this.filtroColVol = filtroColVol;
	}

	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getComoLlegar() {
		return comoLlegar;
	}

	public void setComoLlegar(String comoLlegar) {
		this.comoLlegar = comoLlegar;
	}

	public String getPuntoReferencia() {
		return puntoReferencia;
	}

	public void setPuntoReferencia(String puntoReferencia) {
		this.puntoReferencia = puntoReferencia;
	}

	public List<TipoTransporte> getTiposTransportes() {
		return tiposTransportes;
	}

	public void setTiposTransportes(List<TipoTransporte> tiposTransportes) {
		this.tiposTransportes = tiposTransportes;
	}

	public long getTipoTransporteSelectedId() {
		return tipoTransporteSelectedId;
	}

	public void setTipoTransporteSelectedId(long tipoTransporteSelectedId) {
		this.tipoTransporteSelectedId = tipoTransporteSelectedId;
	}

	public List<ClaseAccesibilidad> getClasesAccesibilidad() {
		return clasesAccesibilidad;
	}

	public void setClasesAccesibilidad(List<ClaseAccesibilidad> clasesAccesibilidad) {
		this.clasesAccesibilidad = clasesAccesibilidad;
	}

	public long getClaseAccesibilidadSelectedId() {
		return claseAccesibilidadSelectedId;
	}

	public void setClaseAccesibilidadSelectedId(long claseAccesibilidadSelectedId) {
		this.claseAccesibilidadSelectedId = claseAccesibilidadSelectedId;
	}

	public BigDecimal getDistancia() {
		return distancia;
	}

	public void setDistancia(BigDecimal distancia) {
		this.distancia = distancia;
	}

	public BigDecimal getTiempo() {
		return tiempo;
	}

	public void setTiempo(BigDecimal tiempo) {
		this.tiempo = tiempo;
	}

	public String getUrlMarcadorColVolM() {
		return urlMarcadorColVolM;
	}

	public void setUrlMarcadorColVolM(String urlMarcadorColVolM) {
		this.urlMarcadorColVolM = urlMarcadorColVolM;
	}

	public String getUrlMarcadorColVolF() {
		return urlMarcadorColVolF;
	}

	public void setUrlMarcadorColVolF(String urlMarcadorColVolF) {
		this.urlMarcadorColVolF = urlMarcadorColVolF;
	}

	public NivelZoom getNivelZoom() {
		return nivelZoom;
	}

	public void setNivelZoom(NivelZoom nivelZoom) {
		this.nivelZoom = nivelZoom;
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

	public void setModo(int modo) {
		this.modo = modo;
	}

	public int getModo() {
		return modo;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getClave() {
		return clave;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setPersonaId(long personaId) {
		this.personaId = personaId;
	}

	public long getPersonaId() {
		return personaId;
	}

	public void setPuestosComunidades(List<PuestoComunidad> puestosComunidades) {
		this.puestosComunidades = puestosComunidades;
	}

	public List<PuestoComunidad> getPuestosComunidades() {
		return puestosComunidades;
	}

	public void setPuestoNotificacionId(long puestoNotificacionId) {
		this.puestoNotificacionId = puestoNotificacionId;
	}

	public long getPuestoNotificacionId() {
		return puestoNotificacionId;
	}

	public void setComunidadPuestoSelected(Comunidad comunidadPuestoSelected) {
		this.comunidadPuestoSelected = comunidadPuestoSelected;
	}

	public Comunidad getComunidadPuestoSelected() {
		return comunidadPuestoSelected;
	}

	public void setPuestoComunidadSelectedId(long puestoComunidadSelectedId) {
		this.puestoComunidadSelectedId = puestoComunidadSelectedId;
	}

	public long getPuestoComunidadSelectedId() {
		return puestoComunidadSelectedId;
	}

	public void setColVolActivo(boolean colVolActivo) {
		this.colVolActivo = colVolActivo;
	}

	public boolean isColVolActivo() {
		return colVolActivo;
	}

	/**
	 * @return the puestoConActividad
	 */
	public boolean isPuestoConActividad() {
		return puestoConActividad;
	}

	/**
	 * @param puestoConActividad the puestoConActividad to set
	 */
	public void setPuestoConActividad(boolean puestoConActividad) {
		this.puestoConActividad = puestoConActividad;
	}

	public void setTiposTransportesActivos(List<TipoTransporte> tiposTransportesActivos) {
		this.tiposTransportesActivos = tiposTransportesActivos;
	}

	public List<TipoTransporte> getTiposTransportesActivos() {
		return tiposTransportesActivos;
	}

	public void setClasesAccesibilidadActivos(
			List<ClaseAccesibilidad> clasesAccesibilidadActivos) {
		this.clasesAccesibilidadActivos = clasesAccesibilidadActivos;
	}

	public List<ClaseAccesibilidad> getClasesAccesibilidadActivos() {
		return clasesAccesibilidadActivos;
	}

	public void setMostrarSoloActivos(boolean mostrarSoloActivos) {
		this.mostrarSoloActivos = mostrarSoloActivos;
	}

	public boolean isMostrarSoloActivos() {
		return mostrarSoloActivos;
	}
}
