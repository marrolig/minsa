// -----------------------------------------------
// PuestoUnidadBean.java
// -----------------------------------------------

package ni.gob.minsa.malaria.interfaz.vigilancia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.datos.estructura.UnidadAccesoDA;
import ni.gob.minsa.malaria.datos.estructura.UnidadDA;
import ni.gob.minsa.malaria.datos.general.CatalogoElementoDA;
import ni.gob.minsa.malaria.datos.general.MarcadorDA;
import ni.gob.minsa.malaria.datos.general.ParametroDA;
import ni.gob.minsa.malaria.datos.general.TipoUnidadDA;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadDA;
import ni.gob.minsa.malaria.datos.poblacion.DivisionPoliticaDA;
import ni.gob.minsa.malaria.datos.vigilancia.PuestoComunidadDA;
import ni.gob.minsa.malaria.datos.vigilancia.PuestoNotificacionDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.estructura.UnidadAcceso;
import ni.gob.minsa.malaria.modelo.general.ClaseAccesibilidad;
import ni.gob.minsa.malaria.modelo.general.Marcador;
import ni.gob.minsa.malaria.modelo.general.Parametro;
import ni.gob.minsa.malaria.modelo.general.TipoTransporte;
import ni.gob.minsa.malaria.modelo.general.TipoUnidad;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoComunidad;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoNotificacion;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.estructura.UnidadAccesoService;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.servicios.general.MarcadorService;
import ni.gob.minsa.malaria.servicios.general.ParametroService;
import ni.gob.minsa.malaria.servicios.general.TipoUnidadService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService;
import ni.gob.minsa.malaria.servicios.vigilancia.PuestoComunidadService;
import ni.gob.minsa.malaria.servicios.vigilancia.PuestoNotificacionService;
import ni.gob.minsa.malaria.soporte.NivelZoom;
import ni.gob.minsa.malaria.soporte.Utilidades;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * Servicio para la capa de presentación de la página 
 * vigilancia/puestoUnidad.xhtml
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 02/10/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class PuestoUnidadBean implements Serializable {

	private static final long serialVersionUID = 1L;

	protected InfoSesion infoSesion;
	private int capaActiva;
	
	private int modo;
	
	// objetos para poblar el combo de entidades administrativas
	private List<EntidadAdtva> entidades;
	
	// identificador de la entidad seleccionada
	private long entidadSelectedId;
	private boolean cambioEntidad;
	
	private Unidad unidadSelected;
	private long unidadSelectedId;

	// lista de objetos para la grilla de colvoles
	private List<PuestoNotificacion> puestosNotificacion;
	private PuestoNotificacion puestoNotificacionSelected;

	// literal utilizada para filtrar a las unidades en la grilla de búsqueda
	private String filtroUnidad;
	private int numRegistros;

	// datos de la unidad que pueden ser modificados por el usuario
	private String telefono;
	private String fax;
	private String email;
	private String direccion;
	private BigDecimal longitud;
	private BigDecimal latitud;
	
	// atributos vinculados al puesto de notificación
	
	private long puestoNotificacionId;
	private String clave;
	private Date fechaApertura;
	private Date fechaCierre;
	private String observaciones;
	private List<PuestoComunidad> puestosComunidades;
	
	// ------------------------------------------------------------------------------
	// atributos vinculados a las comunidades atendidas por el puesto de notificación
	// ------------------------------------------------------------------------------
	
	private long departamentoComunidadSelectedId;
	private long municipioComunidadSelectedId;
	private List<DivisionPolitica> departamentosComunidad;
	private List<DivisionPolitica> municipiosComunidad;
	private Comunidad comunidadPuestoSelected;
	
	// este atributo es inicializado desde el interfaz del usuario al 
	// seleccionar una comunidad vinculada al puesto de notificación para ser eliminada
	
	private long puestoComunidadSelectedId;
	
	// --------------------------------------------------------
	// atributos vinculados al acceso de la unidad
	// --------------------------------------------------------
	
	private String comoLlegar;
	private String puntoReferencia;
	
	private List<TipoTransporte> tiposTransportes;
	private long tipoTransporteSelectedId;
	
	private List<ClaseAccesibilidad> clasesAccesibilidad;
	private long claseAccesibilidadSelectedId;
	
	private BigDecimal distancia;
	private BigDecimal tiempo;
	
	// comunidad seleccionada de la lista de sugerencias del control de autocompletar
	private Comunidad comunidadSelected;

	// ----------------------------------------------------------
	// atributos vinculados a la búsqueda de la unidad
	// que se vinculará al puesto de notificación
	// ----------------------------------------------------------
	
	private long tipoUnidadSelectedId;
	private List<TipoUnidad> tiposUnidades;
	private List<Unidad> unidades;
	
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
    private String urlMarcadorPuesto;
    private String urlMarcadorEntidad;
    private int zoom;
	private NivelZoom nivelZoom;
    private String coordenadasPais;
	
	private static UnidadService unidadService = new UnidadDA();
	private static UnidadAccesoService unidadAccesoService = new UnidadAccesoDA();
	private static ComunidadService comunidadService = new ComunidadDA();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TipoTransporte,Integer> tipoTransporteService=new CatalogoElementoDA(TipoTransporte.class,"TipoTransporte");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<ClaseAccesibilidad,Integer> claseAccesibilidadService=new CatalogoElementoDA(ClaseAccesibilidad.class,"ClaseAccesibilidad");
	private static MarcadorService marcadorService = new MarcadorDA();
	private static ParametroService parametroService = new ParametroDA();
	
	private static PuestoNotificacionService puestoNotificacionService = new PuestoNotificacionDA();
	private static DivisionPoliticaService divisionPoliticaService = new DivisionPoliticaDA();
	private static EntidadAdtvaService entidadAdtvaService=new EntidadAdtvaDA();
	
	public PuestoUnidadBean() {
		
		this.nivelZoom=new NivelZoom();
		this.capaActiva=1;
		
		this.infoSesion=Utilidades.obtenerInfoSesion();
		this.entidades= new ArrayList<EntidadAdtva>();
		this.unidades= new ArrayList<Unidad>();
		this.setFiltroUnidad("");

		this.entidadSelectedId=0;
		this.unidadSelectedId=0;

		this.urlMarcadorPuesto="";
		
		InfoResultado oResultadoParametro=parametroService.Encontrar("MAPA_PUESTO_UNIDAD");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
			Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
			InfoResultado oResultadoMarcador=marcadorService.Encontrar(oParametro.getValor());
			if (oResultadoMarcador!=null && oResultadoMarcador.isOk()) {
				this.urlMarcadorPuesto=((Marcador)oResultadoMarcador.getObjeto()).getUrl();
			}
		}
		
		oResultadoParametro=parametroService.Encontrar("MAPA_ENTIDAD");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
			Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
			InfoResultado oResultadoMarcador=marcadorService.Encontrar(oParametro.getValor());
			if (oResultadoMarcador!=null && oResultadoMarcador.isOk()) {
				this.urlMarcadorEntidad=((Marcador)oResultadoMarcador.getObjeto()).getUrl();
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
		this.cambioEntidad=true;
		this.entidades=ni.gob.minsa.malaria.reglas.Operacion.entidadesAutorizadas(this.infoSesion.getUsuarioId(),false);
		if ((this.entidades!=null) && (this.entidades.size()>0)) {
			this.entidadSelectedId=this.entidades.get(0).getEntidadAdtvaId();
		}
		
		// se llena la grilla con las unidades que forman parte de la red de
		// vigilancia, i.e. que son puestos de notificación, ya sea activos o pasivos.
		
		obtenerUnidades();
		
		iniciarCapa1();

	}
	
	// inicia las variables primarias de modo tal que
	// ningun colVol y persona haya sido seleccionada
	public void iniciarCapas() {

		this.puestoNotificacionSelected=null;
		this.unidadSelected=null;
		this.puestoNotificacionId=0;
		this.unidadSelectedId=0;
		
		this.puestosComunidades=new ArrayList<PuestoComunidad>();
		this.setFiltroUnidad("");
		this.capaActiva=1;

	}
	
	public void iniciarCapa1() {
		
		iniciarCapa2();
		iniciarCapas();
		
	}
	
	public void iniciarCapa2() {
		
		this.capaActiva=2;
		
		this.direccion=null;
		this.telefono=null;
		this.fax=null;
		this.email=null;
		this.longitud=null;
		this.latitud=null;
		this.comoLlegar=null;
		this.puntoReferencia=null;
		this.distancia=null;
		this.tiempo=null;
		this.clave=null;
		this.fechaApertura=null;
		this.fechaCierre=null;
		
		this.tipoTransporteSelectedId=0;
		this.tiposTransportes=tipoTransporteService.ListarActivos();

		this.claseAccesibilidadSelectedId=0;
		this.clasesAccesibilidad=claseAccesibilidadService.ListarActivos();
		
		this.comunidadPuestoSelected=null;
	
	}

	public void regresarCapa1() {
		
		iniciarCapa1();
		
	}
	
	public void aceptarUnidad() {
		
		this.unidadSelectedId=this.unidadSelected.getUnidadId();
		
		UnidadAcceso oUnidadAcceso = unidadAccesoService.EncontrarPorUnidad(this.unidadSelected.getCodigo());
		if (oUnidadAcceso!=null) {
			this.comoLlegar=oUnidadAcceso.getComoLlegar();
			this.puntoReferencia=oUnidadAcceso.getPuntoReferencia();
			this.tipoTransporteSelectedId=oUnidadAcceso.getTipoTransporte()!=null?oUnidadAcceso.getTipoTransporte().getCatalogoId():0;
			this.claseAccesibilidadSelectedId=oUnidadAcceso.getClaseAccesibilidad()!=null?oUnidadAcceso.getClaseAccesibilidad().getCatalogoId():0;
			this.distancia=oUnidadAcceso.getDistancia();
			this.tiempo=oUnidadAcceso.getTiempo();
		} else {
			this.comoLlegar=null;
			this.puntoReferencia=null;
			this.tipoTransporteSelectedId=0;
			this.claseAccesibilidadSelectedId=0;
			this.distancia=null;
			this.tiempo=null;
		}
		
		this.direccion=this.unidadSelected.getDireccion();
		this.latitud=this.unidadSelected.getLatitud();
		this.longitud=this.unidadSelected.getLongitud();
		this.telefono=this.unidadSelected.getTelefono();
		this.fax=this.unidadSelected.getFax();
		this.email=this.unidadSelected.getEmail();
		
		this.capaActiva=2;
	}

	public void cambiarEntidad() {
		
		if (this.entidadSelectedId==0) {
			return;
		}
		
		this.puestosNotificacion=puestoNotificacionService.ListarUnidadesPorEntidad(this.entidadSelectedId, false);

		iniciarCapa1();
		
	}
	
	/**
	 * Evento que se ejecuta cuando el usuario selecciona una unidad de salud
	 * de la grilla.  Traslada todos los valores del objeto seleccionado 
	 * a los controles del panel de detalle 
	 */
	public void onPuestoNotificacionSelected(SelectEvent iEvento) { 
		
		this.capaActiva=2;
		this.puestoNotificacionId=this.puestoNotificacionSelected.getPuestoNotificacionId();
		
		if (this.puestoNotificacionSelected.getUnidad().getUnidadAcceso()!=null) {
			
			if (this.puestoNotificacionSelected.getUnidad().getUnidadAcceso().getTipoTransporte()!=null) {
				this.tipoTransporteSelectedId=this.puestoNotificacionSelected.getUnidad().getUnidadAcceso().getTipoTransporte().getCatalogoId();
				this.tiposTransportes=tipoTransporteService.ListarActivos(this.puestoNotificacionSelected.getUnidad().getUnidadAcceso().getTipoTransporte().getCodigo());
			} else {
				this.tiposTransportes=tipoTransporteService.ListarActivos();
			}
			
			if (this.puestoNotificacionSelected.getUnidad().getUnidadAcceso().getClaseAccesibilidad()!=null) {
				this.claseAccesibilidadSelectedId=this.puestoNotificacionSelected.getUnidad().getUnidadAcceso().getClaseAccesibilidad().getCatalogoId();
				this.clasesAccesibilidad=claseAccesibilidadService.ListarActivos(this.puestoNotificacionSelected.getUnidad().getUnidadAcceso().getClaseAccesibilidad().getCodigo());
			} else {
				this.clasesAccesibilidad=claseAccesibilidadService.ListarActivos();
			}
			
			this.comoLlegar=this.puestoNotificacionSelected.getUnidad().getUnidadAcceso().getComoLlegar();
			this.distancia=this.puestoNotificacionSelected.getUnidad().getUnidadAcceso().getDistancia();
			this.tiempo=this.puestoNotificacionSelected.getUnidad().getUnidadAcceso().getTiempo();
			this.puntoReferencia=this.puestoNotificacionSelected.getUnidad().getUnidadAcceso().getPuntoReferencia();
			
		} else {
			
			this.comoLlegar=null;
			this.distancia=null;
			this.tiempo=null;
			this.puntoReferencia=null;
			this.tiposTransportes=tipoTransporteService.ListarActivos();
			this.clasesAccesibilidad=claseAccesibilidadService.ListarActivos();
		}
		
		this.comunidadPuestoSelected=null;
		this.unidadSelected=this.puestoNotificacionSelected.getUnidad();
		this.unidadSelectedId=this.unidadSelected.getUnidadId();
				
		// propiedades modificables de la unidad
		this.direccion=this.unidadSelected.getDireccion();
		this.latitud=this.unidadSelected.getLatitud();
		this.longitud=this.unidadSelected.getLongitud();
		this.telefono=this.unidadSelected.getTelefono();
		this.fax=this.unidadSelected.getFax();
		this.email=this.unidadSelected.getEmail();
				
		this.clave=this.puestoNotificacionSelected.getClave();
		this.fechaApertura=this.puestoNotificacionSelected.getFechaApertura();
		this.fechaCierre=this.puestoNotificacionSelected.getFechaCierre();
		this.observaciones=this.puestoNotificacionSelected.getObservaciones();
		this.puestosComunidades=this.puestoNotificacionSelected.getComunidadesPuesto();
			
		FacesContext context = FacesContext.getCurrentInstance();
		UIComponent componentDetalle = null; 
		UIViewRoot root = context.getViewRoot( ); 
		componentDetalle = (UIComponent) root.findComponent("frmPuestoUnidad:panDetalle");
		for (UIComponent uic:componentDetalle.getChildren()) {
			if (uic instanceof EditableValueHolder) {   
				EditableValueHolder evh=(EditableValueHolder)uic;   
				evh.resetValue();   
			}
		}
		
	}
	
	public void generarMapa(ActionEvent pEvento) {
		
		this.mapaModelo = new DefaultMapModel();  
        BigDecimal iSumaLatitudes=BigDecimal.ZERO;
        BigDecimal iSumaLongitudes=BigDecimal.ZERO;
        int iNumPuestos=0;
        this.centroMapa="0,0";
        this.zoom=nivelZoom.PAIS;
        
        List<PuestoNotificacion> oPuestos=puestoNotificacionService.ListarUnidadesPorEntidad(this.entidadSelectedId, true);
		for (PuestoNotificacion oPuestoNotificacion:oPuestos) {
			Unidad oUnidad=oPuestoNotificacion.getUnidad();
			if (oUnidad.getLatitud()!=null && oUnidad.getLongitud()!=null) {
				iSumaLatitudes=iSumaLatitudes.add(oUnidad.getLatitud());
				iSumaLongitudes=iSumaLongitudes.add(oUnidad.getLongitud());
				iNumPuestos++;
				
				LatLng iCoordenada = new LatLng(oUnidad.getLatitud().doubleValue(),oUnidad.getLongitud().doubleValue());
				Marker oMarker = new Marker(iCoordenada, oUnidad.getNombre());
				if (!this.urlMarcadorPuesto.isEmpty()) {
					oMarker.setIcon(this.urlMarcadorPuesto);
				} 
				this.mapaModelo.addOverlay(oMarker);
			}
		}
		
		if (iNumPuestos!=0) {
			this.centroMapa=iSumaLatitudes.divide(new BigDecimal(iNumPuestos),6,RoundingMode.HALF_UP).toPlainString() + "," + iSumaLongitudes.divide(new BigDecimal(iNumPuestos),6,RoundingMode.HALF_UP).toPlainString();
			this.zoom=nivelZoom.MUNICIPIO;
		}
		
		InfoResultado oResEntidad=entidadAdtvaService.Encontrar(this.entidadSelectedId);
		if (oResEntidad.isOk()) {
			EntidadAdtva oEntidad = (EntidadAdtva)oResEntidad.getObjeto();
			if (oEntidad.getLatitud()!=null && oEntidad.getLongitud()!=null) {
				if (iNumPuestos==0) {
					this.centroMapa=oEntidad.getLatitud().toPlainString() + "," + oEntidad.getLongitud().toPlainString();
				}
			
				LatLng iCoordenada = new LatLng(oEntidad.getLatitud().doubleValue(),oEntidad.getLongitud().doubleValue());
				Marker oMarker = new Marker(iCoordenada, oEntidad.getNombre());
				if (this.urlMarcadorEntidad!=null) {
					oMarker.setIcon(this.urlMarcadorEntidad);
				}
				
				this.mapaModelo.addOverlay(oMarker);
				this.zoom=nivelZoom.DEPARTAMENTO;
				return;
			}
			
			if (oEntidad.getMunicipio().getDepartamento().getLatitud()!=null && oEntidad.getMunicipio().getDepartamento().getLongitud()!=null) {
				this.centroMapa=oEntidad.getMunicipio().getDepartamento().getLatitud().toPlainString() + "," + oEntidad.getMunicipio().getDepartamento().getLongitud().toPlainString();
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
		
		if (this.tiposUnidades==null) {
			TipoUnidadService tipoUnidadService=new TipoUnidadDA();
			this.tiposUnidades=tipoUnidadService.TiposUnidadesActivas();
		}

		if (this.cambioEntidad) {
			this.tipoUnidadSelectedId=this.tiposUnidades.get(0).getTipounidadId();
			this.unidades=unidadService.UnidadesActivasPorEntidadYTipo(this.entidadSelectedId, this.tipoUnidadSelectedId);
			this.cambioEntidad=false;
		}

		iniciarCapas();
		
	}

	
	/**
	 * Se ejecuta cuando el usuario pulsa en el botón guardar.
	 * Este proceso incluye guardar un nuevo registro o actualizar
	 * un registro existente. <br>
	 */
	public void guardar(ActionEvent pEvento) {
		
		if (this.unidadSelected==null) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La declaración de la unidad de salud que funciona como puesto de notificación es requerida","");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		// si es un nuevo registro de puesto de notificación, debe verificarse que 
		// la unidad no exista como tal. 
		if (this.puestoNotificacionId==0) {
			PuestoNotificacion oPuestoNotificacion=puestoNotificacionService.EncontrarPorUnidad(this.unidadSelectedId,0);
			if (oPuestoNotificacion!=null) {
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, "La unidad de salud ya se encuentra registrada como puesto de notificación","");
				if (msg!=null)
					FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}
			
			if (this.comunidadPuestoSelected==null) {
				FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "Seleccione la comunidad de la lista de comunidades propuesta","");
				if (msg!=null)
					FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			}

		}

		InfoResultado oResultado = new InfoResultado();
		PuestoNotificacion oPuestoNotificacion = new PuestoNotificacion();
		
		Unidad oUnidad = (Unidad)unidadService.Encontrar(this.unidadSelectedId).getObjeto();
		oUnidad.setLatitud(this.latitud);
		oUnidad.setLongitud(this.longitud);
		oUnidad.setDireccion(this.direccion!=null && !this.direccion.trim().isEmpty()?this.direccion:null);
		oUnidad.setTelefono(this.telefono!=null && !this.telefono.trim().isEmpty()?this.telefono:null);
		oUnidad.setFax(this.fax!=null && !this.fax.trim().isEmpty()?this.fax:null);
		oUnidad.setEmail(this.email!=null && !this.email.trim().isEmpty()?this.email:null);
		
		UnidadAcceso oUnidadAcceso=(UnidadAcceso)unidadAccesoService.EncontrarPorUnidad(this.unidadSelected.getCodigo());
		if (oUnidadAcceso==null) {
			// no existen datos de acceso para la unidad de salud
			oUnidadAcceso=new UnidadAcceso();
			oUnidadAcceso.setUnidad(oUnidad);
			oUnidadAcceso.setUsuarioRegistro(this.infoSesion.getUsername());
			oUnidadAcceso.setFechaRegistro(Calendar.getInstance().getTime());
		}
		
		oUnidadAcceso.setComoLlegar(this.comoLlegar!=null && !this.comoLlegar.trim().isEmpty()?this.comoLlegar:null);
		oUnidadAcceso.setDistancia(this.distancia!=null && !this.distancia.equals(new BigDecimal(0))? this.distancia:null);
		oUnidadAcceso.setTiempo(this.tiempo!=null && !this.tiempo.equals(new BigDecimal(0))?this.tiempo:null);
		oUnidadAcceso.setPuntoReferencia(this.puntoReferencia!=null && !this.puntoReferencia.trim().isEmpty()?this.puntoReferencia:null);
			
		if (this.tipoTransporteSelectedId!=0) {
				oUnidadAcceso.setTipoTransporte((TipoTransporte)tipoTransporteService.Encontrar(this.tipoTransporteSelectedId).getObjeto());
		} else {
			oUnidadAcceso.setTipoTransporte(null);
		}
			
		if (this.claseAccesibilidadSelectedId!=0) {
			oUnidadAcceso.setClaseAccesibilidad((ClaseAccesibilidad)claseAccesibilidadService.Encontrar(this.claseAccesibilidadSelectedId).getObjeto());
		} else {
			oUnidadAcceso.setClaseAccesibilidad(null);
		}

		oPuestoNotificacion.setUnidad(oUnidad);
		oPuestoNotificacion.setClave(this.clave!=null && !this.clave.trim().isEmpty()?this.clave.trim():null);
		oPuestoNotificacion.setFechaApertura(this.fechaApertura);
		oPuestoNotificacion.setFechaCierre(this.fechaCierre);
		oPuestoNotificacion.setPuestoNotificacionId(this.puestoNotificacionId);
		oPuestoNotificacion.setObservaciones(this.observaciones!=null && !this.observaciones.trim().isEmpty()?this.observaciones:null);

		if (this.puestoNotificacionId!=0) {
			oPuestoNotificacion.setPuestoNotificacionId(this.puestoNotificacionId);
			oResultado=puestoNotificacionService.Guardar(oPuestoNotificacion,oUnidad,oUnidadAcceso);
		} else {
			// es un nuevo registro y por tanto debe inicializar los valores de fecha Registro y Usuario Registro
			oPuestoNotificacion.setFechaRegistro(Calendar.getInstance().getTime());
			oPuestoNotificacion.setUsuarioRegistro(this.infoSesion.getUsername());
			oResultado=puestoNotificacionService.Agregar(oPuestoNotificacion,oUnidad,oUnidadAcceso);
			if (oResultado.isOk()) {
				
				PuestoComunidad oPuestoComunidad = new PuestoComunidad();
				oPuestoComunidad.setComunidad(this.comunidadPuestoSelected);
				oPuestoComunidad.setFechaRegistro(Calendar.getInstance().getTime());
				oPuestoComunidad.setUsuarioRegistro(this.infoSesion.getUsername());
				oPuestoNotificacion=(PuestoNotificacion)oResultado.getObjeto();
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
			
		}

		if (oResultado.isOk()){
			iniciarCapa1();
			oResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
		} 
		
		FacesMessage msg = Mensajes.enviarMensaje(oResultado);
		if (msg!=null)
			FacesContext.getCurrentInstance().addMessage(null, msg);
		
		this.puestosNotificacion=puestoNotificacionService.ListarUnidadesPorEntidad(this.entidadSelectedId, false);
		
	}
	
	public void eliminar(ActionEvent pEvento){

		if (this.puestoNotificacionId==0) return;
		
		InfoResultado oResultado = new InfoResultado();
		oResultado=puestoNotificacionService.Eliminar(this.puestoNotificacionId);
		
		if (oResultado.isOk()){
			iniciarCapa1();
			oResultado.setMensaje(Mensajes.REGISTRO_ELIMINADO);
			obtenerUnidades();
			this.cambioEntidad=false;
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
	
	public void obtenerUnidades() {
	
		if (this.entidadSelectedId!=0) {
			this.setPuestosNotificacion(puestoNotificacionService.ListarUnidadesPorEntidad(this.entidadSelectedId, false));
			this.cambioEntidad=true;
		}
	}
	
	/**
	 * Obtiene las unidades activas vinculadas a la entidad administrativa seleccionada
	 * y el tipo de unidad seleccionado.  Método desencadenado al efectuar un cambio de valor
	 * en el combo de tipos de unidades en la ventana modal de selección de la unidad de salud
	 */
	public void obtenerUnidadesPortipo() {
		
		if (this.tipoUnidadSelectedId!=0) {
			this.unidades=unidadService.UnidadesActivasPorEntidadYTipo(this.entidadSelectedId, this.tipoUnidadSelectedId);
		}
	}
	
	public void obtenerMunicipiosComunidad() {
		
		if (this.departamentoComunidadSelectedId!=0) {
			this.municipiosComunidad=divisionPoliticaService.MunicipiosActivos(this.departamentoComunidadSelectedId);
		} else {
			this.municipiosComunidad=null;
			this.municipioComunidadSelectedId=0;
		}
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
		
		PuestoNotificacion oPuestoNotificacion = puestoNotificacionService.EncontrarPorUnidad(this.unidadSelectedId,0);
		if (oPuestoNotificacion==null) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_ERROR, "El puesto de notificación al cual se encuentra vinculada la unidad de salud no existe",Mensajes.NOTIFICACION_ADMINISTRADOR);
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

	public void setDepartamentoComunidadSelectedId(
			long departamentoComunidadSelectedId) {
		this.departamentoComunidadSelectedId = departamentoComunidadSelectedId;
	}

	public long getDepartamentoComunidadSelectedId() {
		return departamentoComunidadSelectedId;
	}

	public void setDepartamentosComunidad(List<DivisionPolitica> departamentosComunidad) {
		this.departamentosComunidad = departamentosComunidad;
	}

	public List<DivisionPolitica> getDepartamentosComunidad() {
		return departamentosComunidad;
	}

	public void setMunicipiosComunidad(List<DivisionPolitica> municipiosComunidad) {
		this.municipiosComunidad = municipiosComunidad;
	}

	public List<DivisionPolitica> getMunicipiosComunidad() {
		return municipiosComunidad;
	}

	public void setComunidadPuestoSelected(Comunidad comunidadPuestoSelected) {
		this.comunidadPuestoSelected = comunidadPuestoSelected;
	}

	public Comunidad getComunidadPuestoSelected() {
		return comunidadPuestoSelected;
	}

	public long getMunicipioComunidadSelectedId() {
		return municipioComunidadSelectedId;
	}

	public void setMunicipioComunidadSelectedId(long municipioComunidadSelectedId) {
		this.municipioComunidadSelectedId = municipioComunidadSelectedId;
	}

	public void setPuestoComunidadSelectedId(long puestoComunidadSelectedId) {
		this.puestoComunidadSelectedId = puestoComunidadSelectedId;
	}

	public long getPuestoComunidadSelectedId() {
		return puestoComunidadSelectedId;
	}

	public void setPuestosNotificacion(List<PuestoNotificacion> puestosNotificacion) {
		this.puestosNotificacion = puestosNotificacion;
	}

	public List<PuestoNotificacion> getPuestosNotificacion() {
		return puestosNotificacion;
	}

	public void setFiltroUnidad(String filtroUnidad) {
		this.filtroUnidad = filtroUnidad;
	}

	public String getFiltroUnidad() {
		return filtroUnidad;
	}

	public PuestoNotificacion getPuestoNotificacionSelected() {
		return puestoNotificacionSelected;
	}

	public void setPuestoNotificacionSelected(
			PuestoNotificacion puestoNotificacionSelected) {
		this.puestoNotificacionSelected = puestoNotificacionSelected;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTipoUnidadSelectedId(long tipoUnidadSelectedId) {
		this.tipoUnidadSelectedId = tipoUnidadSelectedId;
	}

	public long getTipoUnidadSelectedId() {
		return tipoUnidadSelectedId;
	}

	public void setTiposUnidades(List<TipoUnidad> tiposUnidades) {
		this.tiposUnidades = tiposUnidades;
	}

	public List<TipoUnidad> getTiposUnidades() {
		return tiposUnidades;
	}

	public void setCambioEntidad(boolean cambioEntidad) {
		this.cambioEntidad = cambioEntidad;
	}

	public boolean isCambioEntidad() {
		return cambioEntidad;
	}
}
