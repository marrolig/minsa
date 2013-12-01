// -----------------------------------------------
// PersonaBean.java
// -----------------------------------------------
package ni.gob.minsa.malaria.interfaz.sis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.ejbPersona.dto.Persona;
import ni.gob.minsa.ejbPersona.servicios.PersonaBTMService;
import ni.gob.minsa.malaria.datos.general.CatalogoElementoDA;
import ni.gob.minsa.malaria.datos.general.OcupacionDA;
import ni.gob.minsa.malaria.datos.general.ParametroDA;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadDA;
import ni.gob.minsa.malaria.datos.poblacion.DivisionPoliticaDA;
import ni.gob.minsa.malaria.datos.poblacion.PaisDA;
import ni.gob.minsa.malaria.modelo.general.Ocupacion;
import ni.gob.minsa.malaria.modelo.general.Parametro;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Pais;
import ni.gob.minsa.malaria.modelo.sis.Escolaridad;
import ni.gob.minsa.malaria.modelo.sis.EstadoCivil;
import ni.gob.minsa.malaria.modelo.sis.Etnia;
import ni.gob.minsa.malaria.modelo.sis.Sexo;
import ni.gob.minsa.malaria.modelo.sis.TipoAsegurado;
import ni.gob.minsa.malaria.modelo.sis.TipoIdentificacion;
import ni.gob.minsa.malaria.reglas.PersonaValidacion;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.servicios.general.OcupacionService;
import ni.gob.minsa.malaria.servicios.general.ParametroService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService;
import ni.gob.minsa.malaria.servicios.poblacion.PaisService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

/**
 * Servicio para la capa de presentación del componente
 * persona.xhtml
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.1, &nbsp; 01/07/2012
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class PersonaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final int EDITAR_PERSONA=1;
	private static final int BUSCAR_PERSONA=0;
	
	private InfoSesion infoSesion;
	
	private long personaId;
	private String textoBusqueda;
	private Persona personaSelected;
	private Persona personaListaSelected;
	private LazyDataModel<Persona> personas;

	private String primerApellido;
	private String segundoApellido;
	private String primerNombre;
	private String segundoNombre;
	private Date fechaNacimiento;
	private String edadEnAnios;
	private String identNumero;
	private String direccion;
	private String telefonoResidencia;
	private String telefonoMovil;
	private String numeroAsegurado;
	
	private long tipoIdentificacionSelectedId;
	private List<TipoIdentificacion> tiposIdentificaciones;
	private List<TipoIdentificacion> tiposIdentificacionesInicial;
	
	private long sexoSelectedId;
	private List<Sexo> sexos;
	private List<Sexo> sexosInicial;
	
	private long etniaSelectedId;
	private List<Etnia> etniasInicial;
	private List<Etnia> etnias;
	
	private long estadoCivilSelectedId;
	private List<EstadoCivil> estadosCivilesInicial;
	private List<EstadoCivil> estadosCiviles;
	
	private long paisNacimientoSelectedId;
	private List<Pais> paisesNacimiento;
	
	private long departamentoNacimientoSelectedId;
	private List<DivisionPolitica> departamentosNacimiento;
	
	private long municipioNacimientoSelectedId;
	private List<DivisionPolitica> municipiosNacimiento;

	private long departamentoResidenciaSelectedId;
	private List<DivisionPolitica> departamentosResidencia;
	
	private long municipioResidenciaSelectedId;
	private List<DivisionPolitica> municipiosResidencia;

	private long comunidadResidenciaSelectedId;
	private List<Comunidad> comunidadesResidencia;
	private Comunidad comunidadResidenciaSelected;

	private long escolaridadSelectedId;
	private List<Escolaridad> escolaridadesInicial;
	private List<Escolaridad> escolaridades;

	private long tipoAseguradoSelectedId;
	private List<TipoAsegurado> tiposAsegurados;
	private List<TipoAsegurado> tiposAseguradosInicial;

	private Ocupacion ocupacionSelected;
	private long ocupacionSelectedId;
	
	private int numRegistros;
	private int modo;
	
	private String codigoAlfa2Pais;
	private Boolean esNacidoNacional;
	private Boolean verificado;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<Etnia,Integer> etniaService=new CatalogoElementoDA(Etnia.class,"Etnia");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<EstadoCivil,Integer> estadoCivilService=new CatalogoElementoDA(EstadoCivil.class,"EstadoCivil");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<Sexo,Integer> sexoService=new CatalogoElementoDA(Sexo.class,"Sexo");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TipoIdentificacion,Integer> tipoIdentificacionService=new CatalogoElementoDA(TipoIdentificacion.class,"TipoIdentificacion");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<TipoAsegurado,Integer> tipoAseguradoService=new CatalogoElementoDA(TipoAsegurado.class,"TipoAsegurado");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<Escolaridad,Integer> escolaridadService=new CatalogoElementoDA(Escolaridad.class,"Escolaridad");
	private static DivisionPoliticaService divisionPoliticaService = new DivisionPoliticaDA();
	private static ComunidadService comunidadService = new ComunidadDA();
	private static PaisService paisService = new PaisDA();
	private static OcupacionService ocupacionService = new OcupacionDA();
	private static ParametroService parametroService = new ParametroDA();

	// --------------------------------------- Constructor
	
	public PersonaBean(){

		this.infoSesion=Utilidades.obtenerInfoSesion();

		this.paisesNacimiento=paisService.listarPaises();
		this.etniasInicial=etniaService.ListarActivos();
		this.escolaridadesInicial=escolaridadService.ListarActivos();
		this.tiposAseguradosInicial=tipoAseguradoService.ListarActivos();
		this.sexosInicial=sexoService.ListarActivos();
		this.tiposIdentificacionesInicial=tipoIdentificacionService.ListarActivos();
		this.estadosCivilesInicial=estadoCivilService.ListarActivos();
		
		InfoResultado oResultadoParametro=parametroService.Encontrar("CODIGOALFA2_PAIS");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
			Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
			this.codigoAlfa2Pais=(String)oParametro.getValor();
		} else {
			this.codigoAlfa2Pais=null;
		}

		iniciarPropiedades();

		this.personas = new LazyDataModel<Persona>() {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
		    public List<Persona> load(int pPaginaActual, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
			
				List<Persona> personaList=null;
				numRegistros=0;

				if (textoBusqueda.trim().length()>0 && textoBusqueda.trim().length()<3) {
					return personaList;
				}
				
				InitialContext ctx;
				
				try {
					ctx = new InitialContext();
					PersonaBTMService personaBTMService = (PersonaBTMService)ctx.lookup("ejb/PersonaBTM");
					InfoResultado oResultadoBusqueda = personaBTMService.buscarPorNombre(pPaginaActual, pageSize, textoBusqueda);
					if (oResultadoBusqueda.isOk() && oResultadoBusqueda.getObjeto()!=null) {
						numRegistros=oResultadoBusqueda.getFilasAfectadas().intValue();
						personaList=(List<Persona>)oResultadoBusqueda.getObjeto();
					} 
					
					if (!(personaList!=null && personaList.size()>0)) {
						FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, "No se encontraron personas con los parámetros de búsqueda seleccionados","Verifique la literal de búsqueda");
						if (msg!=null)
							FacesContext.getCurrentInstance().addMessage(null, msg);
					}
					this.setRowCount(numRegistros);

					return personaList;
					
				} catch (NamingException e) {
					FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_FATAL, "El servicio para el enlace a personas no se encuentra activo",Mensajes.NOTIFICACION_ADMINISTRADOR);
					if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
					System.out.println("-------------------------------------------------------");
					System.out.println("Error: No se ha podido encontrar el servicio ejb/PersonalBTM");
					e.printStackTrace();
					return null;
				}
			}

		};
		
		this.personas.setRowCount(numRegistros);
		
	}

	// ------------------------------------------- Métodos

	public void iniciarPersona(ActionEvent pEvento) {
		iniciarPropiedades();
	}
	
	public void iniciarPropiedades() {

		this.textoBusqueda="";
		this.personaId=0;
		
		// si no existe una persona vinculada al colaborador voluntario entonces
		// se permitirá al usuario realizar la búsqueda de personas, en este
		// caso no importará el valor del atributo searchMode del componente
		
		// FacesContext facesContext = FacesContext.getCurrentInstance();
		// ELContext elContext = facesContext.getELContext();
		// ValueExpression veModoBuscar = facesContext.getApplication().getExpressionFactory().createValueExpression(elContext, "#{cc.attrs.searchMode}", Boolean.class);

		// this.modo=((Boolean)veModoBuscar.getValue(elContext)).booleanValue()?BUSCAR_PERSONA:EDITAR_PERSONA;
		
		if (this.personaSelected==null) {
			this.setModo(BUSCAR_PERSONA);
		} else {
			// this.personaSelected=(Persona)vePersona.getValue(elContext);
			inicializaDetallePersona();
		}
		
		this.personaListaSelected=null;

	}
	
	public void buscarPersonas() {
		
		if (this.textoBusqueda.trim().length()>0 && this.textoBusqueda.trim().length()<3) { 

			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_INFO, Mensajes.RESTRICCION_BUSQUEDA,"");
			if (msg!=null)
				FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		iniciarPersonas();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void aceptarPersona(ActionEvent pEvento) {

		Persona oPersona = new Persona();
		
		oPersona.setPersonaId(this.personaId);
		oPersona.setAseguradoNumero(this.numeroAsegurado);
		oPersona.setVerificados(this.verificado);

		if (this.comunidadResidenciaSelected!=null) {
			oPersona.setComuResiCodigo(this.comunidadResidenciaSelected.getCodigo());
			oPersona.setComuResiNombre(this.comunidadResidenciaSelected.getNombre());
		}
		
		if (this.direccion!=null && !this.direccion.trim().isEmpty()) {
			oPersona.setDireccionResi(this.direccion);
		}
		
		if (this.escolaridadSelectedId!=0) {
			Escolaridad oEscolaridad=(Escolaridad)((InfoResultado)escolaridadService.Encontrar(this.escolaridadSelectedId)).getObjeto();
			oPersona.setEscolaridadCodigo(oEscolaridad.getCodigo());
			oPersona.setEscolaridadValor(oEscolaridad.getValor());
		}
		
		if (this.estadoCivilSelectedId!=0) {
			EstadoCivil oEstadoCivil=(EstadoCivil)((InfoResultado)estadoCivilService.Encontrar(this.estadoCivilSelectedId)).getObjeto();
			oPersona.setEstadoCivilCodigo(oEstadoCivil.getCodigo());
			oPersona.setEstadoCivilValor(oEstadoCivil.getValor());
			
		}
		
		if (this.etniaSelectedId!=0) {
			Etnia oEtnia=(Etnia)((InfoResultado)etniaService.Encontrar(this.etniaSelectedId)).getObjeto();
			oPersona.setEtniaId(this.etniaSelectedId);
			oPersona.setEtniaCodigo(oEtnia.getCodigo());
			oPersona.setEtniaValor(oEtnia.getValor());
		}
		
		oPersona.setFechaNacimiento(this.fechaNacimiento);
		
		if (this.tipoIdentificacionSelectedId!=0) {
			TipoIdentificacion oTipoIdentificacion = (TipoIdentificacion)((InfoResultado)tipoIdentificacionService.Encontrar(this.tipoIdentificacionSelectedId)).getObjeto();
			oPersona.setIdentCodigo(oTipoIdentificacion.getCodigo());
			oPersona.setIdentValor(oTipoIdentificacion.getValor());
		} 
		
		if (this.identNumero!=null && !this.identNumero.trim().isEmpty()) {
			oPersona.setIdentNumero(this.identNumero.trim());
		} 
		
		if (this.municipioNacimientoSelectedId!=0) {
			DivisionPolitica oMunicipioNac = (DivisionPolitica)((InfoResultado)divisionPoliticaService.Encontrar(this.municipioNacimientoSelectedId)).getObjeto();
			oPersona.setMuniNacCodigoNac(oMunicipioNac.getCodigoNacional());
			oPersona.setMuniNacNombre(oMunicipioNac.getNombre());
		} 
		
		if (this.municipioResidenciaSelectedId!=0) {
			DivisionPolitica oMunicipioResi = (DivisionPolitica)((InfoResultado)divisionPoliticaService.Encontrar(this.municipioResidenciaSelectedId)).getObjeto();
			oPersona.setMuniResiCodigoNac(oMunicipioResi.getCodigoNacional());
			oPersona.setMuniResiNombre(oMunicipioResi.getNombre());
		} 

		if (this.ocupacionSelected!=null) {
			oPersona.setOcupacionCodigo(this.ocupacionSelected.getCodigo());
			oPersona.setOcupacionValor(this.ocupacionSelected.getNombre());
			oPersona.setOcupacionId(this.ocupacionSelected.getOcupacionId());
		} else {
			oPersona.setOcupacionCodigo(null);
			oPersona.setOcupacionId(0);
			oPersona.setOcupacionValor(null);
		}

		if (this.paisNacimientoSelectedId!=0) {
			Pais oPaisNac=(Pais)((InfoResultado)paisService.Encontrar(this.paisNacimientoSelectedId)).getObjeto();
			oPersona.setPaisNacCodigoAlfados(oPaisNac.getCodigoAlfados());
			oPersona.setPaisNacNombre(oPaisNac.getNombre());
		}
		
		if (this.primerApellido!=null && !this.primerApellido.trim().isEmpty()) {
			oPersona.setPrimerApellido(this.primerApellido.trim());
		}
		
		if (this.primerNombre!=null && !this.primerNombre.trim().isEmpty()) {
			oPersona.setPrimerNombre(this.primerNombre.trim());
		}
		
		if (this.segundoApellido!=null && !this.segundoApellido.trim().isEmpty()) {
			oPersona.setSegundoApellido(this.segundoApellido.trim());
		}
		
		if (this.segundoNombre!=null && !this.segundoNombre.trim().isEmpty()) {
			oPersona.setSegundoNombre(this.segundoNombre.trim());
		}
		
		if (this.sexoSelectedId!=0) {
			Sexo oSexo = (Sexo)((InfoResultado)sexoService.Encontrar(this.sexoSelectedId)).getObjeto();
			oPersona.setSexoId(this.sexoSelectedId);
			oPersona.setSexoCodigo(oSexo.getCodigo());
			oPersona.setSexoValor(oSexo.getValor());
		}
		
		if (this.sexoSelectedId==0) {
			System.out.println("---------------------------------------------");
			System.out.println("No existe el id para el sexo");
			System.out.println("---------------------------------------------");
		}
				
		if (this.telefonoMovil!=null && !this.telefonoMovil.trim().isEmpty()) {
			oPersona.setTelefonoMovil(this.telefonoMovil.trim());
		}
		
		if (this.telefonoResidencia!=null && !this.telefonoResidencia.trim().isEmpty()) {
			oPersona.setTelefonoResi(this.telefonoResidencia.trim());
		}
		
		if (this.tipoAseguradoSelectedId!=0) {
			TipoAsegurado oTipoAsegurado = (TipoAsegurado)((InfoResultado)tipoAseguradoService.Encontrar(this.tipoAseguradoSelectedId)).getObjeto();
			oPersona.setTipoAsegCodigo(oTipoAsegurado.getCodigo());
			oPersona.setTipoAsegValor(oTipoAsegurado.getValor());
		}

		Integer iEdad = Utilidades.calcularEdadEnAnios(this.fechaNacimiento);
		oPersona.setEdadEnAnios(iEdad!=null?iEdad:null);
		
		RequestContext rContext = RequestContext.getCurrentInstance();
		rContext.addCallbackParam("personaValida", false);
		
		InfoResultado oResValPersona=PersonaValidacion.validar(oPersona);
		if (!oResValPersona.isOk()) {
			FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_WARN, oResValPersona.getMensaje(),"");
			if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ELContext elContext = facesContext.getELContext();
		ValueExpression vePersona = facesContext.getApplication().getExpressionFactory().createValueExpression(elContext, "#{cc.attrs.value}", Persona.class);
		vePersona.setValue(elContext, oPersona);
		
		ValueExpression veControlProperty = facesContext.getApplication().getExpressionFactory().createValueExpression(elContext, "#{cc.attrs.controlProperty}", Integer.class);
		if (veControlProperty.getValue(elContext)!=null) {
			ValueExpression veValueControlProperty = facesContext.getApplication().getExpressionFactory().createValueExpression(elContext, "#{cc.attrs.valueControlProperty}", Integer.class);
			if (veValueControlProperty.getValue(elContext)!=null) {
				veControlProperty.setValue(elContext, (Integer)veValueControlProperty.getValue(elContext));
			} else {
				veControlProperty.setValue(elContext, new Integer(1));
			}
		}

		ValueExpression veUpdate = facesContext.getApplication().getExpressionFactory().createValueExpression(elContext, "#{cc.attrs.update}", String.class);
		if (veUpdate.getValue(elContext)!=null) {
			String aComponentes[] = ((String)veUpdate.getValue(elContext)).split(" ");
			Collection iComponentes = Arrays.asList(aComponentes);
		    rContext.update(iComponentes);
		}
		
		rContext.addCallbackParam("personaValida", true);  

	}

	/**
	 * Evento disparado como resultado de seleccionar a una persona de la lista
	 * de personas generada con una literal de búsqueda
	 */
	public void seleccionarPersona(ActionEvent pEvento) {
		this.personaSelected=this.personaListaSelected;
		inicializaDetallePersona();
	}
	
	public void calcularEdad() {
		
		this.edadEnAnios=null;
		if (this.fechaNacimiento!=null) {
			Integer iEdad=Utilidades.calcularEdadEnAnios(this.fechaNacimiento);
			this.edadEnAnios=iEdad!=null?iEdad.toString():null;
		}
	}
	
	/**
	 * Inicializa detalle de la Persona
	 */
	public void inicializaDetallePersona() {
		
		this.comunidadResidenciaSelected=null;
		this.ocupacionSelected=null;
		
		this.personaId=this.personaSelected.getPersonaId();
		this.primerNombre=this.personaSelected.getPrimerNombre();
		this.segundoNombre=this.personaSelected.getSegundoNombre();
		this.primerApellido=this.personaSelected.getPrimerApellido();
		this.segundoApellido=this.personaSelected.getSegundoApellido();
		this.fechaNacimiento=this.personaSelected.getFechaNacimiento();
		this.edadEnAnios=this.personaSelected.getEdadEnAnios().toString();
		this.tipoIdentificacionSelectedId=this.personaSelected.getIdentId();
		if (this.tipoIdentificacionSelectedId!=0) {
			boolean oEncontrado=false;
			for(TipoIdentificacion s: this.tiposIdentificacionesInicial){
				if(s.getCodigo()==this.personaSelected.getIdentCodigo()) {
					oEncontrado=true;
				}
			}
			if (!oEncontrado) {
				this.tiposIdentificaciones=tipoIdentificacionService.ListarActivos(this.personaSelected.getIdentCodigo());
			} else {
				this.tiposIdentificaciones=this.tiposIdentificacionesInicial;
			}
		} else {
			this.tiposIdentificaciones=this.tiposIdentificacionesInicial;
		}
		
		this.identNumero=this.personaSelected.getIdentNumero();
		this.sexoSelectedId=this.personaSelected.getSexoId();
		if (this.sexoSelectedId!=0) {
			boolean oEncontrado=false;
			for(Sexo s: this.sexosInicial){
				if(s.getCodigo()==this.personaSelected.getSexoCodigo()) {
					oEncontrado=true;
				}
			}
			if (!oEncontrado) {
				this.sexos=sexoService.ListarActivos(this.personaSelected.getSexoCodigo());
			} else {
				this.sexos=this.sexosInicial;
			}
		} else {
			this.sexos=this.sexosInicial;
		}
		
		this.etniaSelectedId=this.personaSelected.getEtniaId();
		if (this.etniaSelectedId!=0) {
			boolean oEncontrado=false;
			for(Etnia s: this.etniasInicial){
				if(s.getCodigo()==this.personaSelected.getEtniaCodigo()) {
					oEncontrado=true;
				}
			}
			if (!oEncontrado) {
				this.etnias=etniaService.ListarActivos(this.personaSelected.getEtniaCodigo());
			} else {
				this.etnias=this.etniasInicial;
			}
		} else {
			this.etnias=this.etniasInicial;
		}
		
		this.estadoCivilSelectedId=this.personaSelected.getEstadoCivilId();
		if (this.estadoCivilSelectedId!=0) {
			boolean oEncontrado=false;
			for(EstadoCivil s: this.estadosCivilesInicial){
				if(s.getCodigo()==this.personaSelected.getEstadoCivilCodigo()) {
					oEncontrado=true;
				}
			}
			if (!oEncontrado) {
				this.estadosCiviles=estadoCivilService.ListarActivos(this.personaSelected.getEstadoCivilCodigo());
			} else {
				this.estadosCiviles=this.estadosCivilesInicial;
			}
		} else {
			this.estadosCiviles=this.estadosCivilesInicial;
		}
		
		this.paisNacimientoSelectedId=this.personaSelected.getPaisNacId();
		if (this.paisNacimientoSelectedId!=0) {
			// si se ha declarado un país de nacimiento extranjero, se debe comparar con la
			// variable CodigoPaisAlfa2, si ésta es nula, no podrá efectuarse ninguna comparación
			// y los departamentos y municipios de nacimientos no podrán ser especificados
			InfoResultado oResPais = paisService.Encontrar(this.paisNacimientoSelectedId);
			this.setEsNacidoNacional(false);
			if (this.codigoAlfa2Pais!=null && 
					((Pais)oResPais.getObjeto()).getCodigoAlfados().equals(this.codigoAlfa2Pais)) {
				
				this.setEsNacidoNacional(true);
				if (this.personaSelected.getMuniNacId()!=0) {
					// si se ha declarado un municipio de nacimiento, obtiene todos los departamentos
					// incluyendo aquel que posiblemente se haya pasivado
					this.departamentosNacimiento=divisionPoliticaService.DepartamentosActivos(this.personaSelected.getMuniNacId());

					// obtiene el objeto Municipio a fin de conocer el id del departamento en el
					// cual nació la persona y establecerlo en el combo de departamentos respectivo
					InfoResultado oResMunicipio=divisionPoliticaService.Encontrar(this.personaSelected.getMuniNacId());
					this.departamentoNacimientoSelectedId=((DivisionPolitica)oResMunicipio.getObjeto()).getDepartamento().getDivisionPoliticaId();
					
					this.municipioNacimientoSelectedId=this.personaSelected.getMuniNacId();			
					this.municipiosNacimiento=divisionPoliticaService.MunicipiosActivos(this.departamentoNacimientoSelectedId, this.municipioNacimientoSelectedId);
				} else {
					this.departamentoNacimientoSelectedId=0;
					this.departamentosNacimiento=null;
					this.municipiosNacimiento=null;
				}
			}
		} else {
			this.departamentosNacimiento=null;
			this.municipiosNacimiento=null;
			this.municipioNacimientoSelectedId=0;
			this.departamentoNacimientoSelectedId=0;
		}

		this.municipioResidenciaSelectedId=this.personaSelected.getMuniResiId();
		if (this.personaSelected.getMuniResiId()!=0) {
			InfoResultado oResMunicipio=divisionPoliticaService.Encontrar(this.personaSelected.getMuniResiId());
			this.departamentoResidenciaSelectedId=((DivisionPolitica)oResMunicipio.getObjeto()).getDepartamento().getDivisionPoliticaId();
			this.departamentosResidencia=divisionPoliticaService.DepartamentosActivos(this.departamentoResidenciaSelectedId);
			this.municipiosResidencia=divisionPoliticaService.MunicipiosActivos(this.departamentoResidenciaSelectedId, this.municipioResidenciaSelectedId);
			this.comunidadResidenciaSelectedId=this.personaSelected.getComuResiId();
			this.comunidadResidenciaSelected=(Comunidad)comunidadService.Encontrar(this.comunidadResidenciaSelectedId).getObjeto();
			
		} else {
			this.departamentoResidenciaSelectedId=0;
			this.departamentosResidencia=divisionPoliticaService.DepartamentosActivos();
			this.comunidadResidenciaSelectedId=0;
			this.comunidadResidenciaSelected=null;
		}

		this.direccion=this.personaSelected.getDireccionResi();
		this.telefonoResidencia=this.personaSelected.getTelefonoResi();
		this.telefonoMovil=this.personaSelected.getTelefonoMovil();

		this.escolaridadSelectedId=this.personaSelected.getEscolaridadId();
		if (this.escolaridadSelectedId!=0) {
			boolean oEncontrado=false;
			for(Escolaridad s: this.escolaridadesInicial){
				if(s.getCodigo()==this.personaSelected.getEscolaridadCodigo()) {
					oEncontrado=true;
				}
			}
			if (!oEncontrado) {
				this.escolaridades=escolaridadService.ListarActivos(this.personaSelected.getEscolaridadCodigo());
			} else {
				this.escolaridades=this.escolaridadesInicial;
			}
		} else {
			this.escolaridades=this.escolaridadesInicial;
		}

		this.ocupacionSelectedId=this.personaSelected.getOcupacionId();
		if (this.ocupacionSelectedId!=0) {
			InfoResultado oResOcupacion = ocupacionService.Encontrar(this.ocupacionSelectedId);
			this.ocupacionSelected=(Ocupacion)oResOcupacion.getObjeto();
		} else {
			this.ocupacionSelected=null;
		}
		
		this.tipoAseguradoSelectedId=this.personaSelected.getTipoAsegId();
		if (this.tipoAseguradoSelectedId!=0) {
			boolean oEncontrado=false;
			for(TipoAsegurado s: this.tiposAseguradosInicial){
				if(s.getCodigo()==this.personaSelected.getTipoAsegCodigo()) {
					oEncontrado=true;
				}
			}
			if (!oEncontrado) {
				this.tiposAsegurados=tipoAseguradoService.ListarActivos(this.personaSelected.getTipoAsegCodigo());
			} else {
				this.tiposAsegurados=this.tiposAseguradosInicial;
			}
		} else {
			this.tiposAsegurados=this.tiposAseguradosInicial;
		}
		
		this.numeroAsegurado=this.personaSelected.getAseguradoNumero();
		this.verificado=this.personaSelected.getVerificados();
		
	}

	/**
	 * Prepara el detalle de la Persona para un nuevo registro
	 */
	public void limpiaDetallePersona() {
		
		this.personaSelected=null;
		
		this.comunidadResidenciaSelected=null;
		this.ocupacionSelected=null;
		
		this.personaId=0;
		this.primerNombre=null;
		this.segundoNombre=null;
		this.primerApellido=null;
		this.segundoApellido=null;
		this.fechaNacimiento=null;
		this.edadEnAnios=null;
		this.tipoIdentificacionSelectedId=0;
		this.tiposIdentificaciones=tipoIdentificacionService.ListarActivos();
		this.identNumero=null;
		this.sexoSelectedId=0;
		this.sexos=sexoService.ListarActivos();
		this.etniaSelectedId=0;
		this.etnias=this.etniasInicial;
		this.estadoCivilSelectedId=0;
		this.estadosCiviles=estadoCivilService.ListarActivos();
		this.paisNacimientoSelectedId=0;
		this.departamentosNacimiento=null;
		this.municipiosNacimiento=null;
		this.municipioNacimientoSelectedId=0;
		this.departamentoNacimientoSelectedId=0;

		this.municipioResidenciaSelectedId=0;
		this.departamentoResidenciaSelectedId=0;
		this.departamentosResidencia=divisionPoliticaService.DepartamentosActivos();
		this.comunidadResidenciaSelectedId=0;
		this.comunidadResidenciaSelected=null;

		this.direccion=null;
		this.telefonoResidencia=null;
		this.telefonoMovil=null;

		this.escolaridadSelectedId=0;
		this.escolaridades=this.escolaridadesInicial;

		this.ocupacionSelectedId=0;
		this.ocupacionSelected=null;
		
		this.tipoAseguradoSelectedId=0;
		this.tiposAsegurados=this.tiposAseguradosInicial;
		
		this.numeroAsegurado=null;
		this.verificado=false;
		
	}

	public void agregarPersona(ActionEvent pEvento) {
		
		this.personaSelected=new Persona();
		limpiaDetallePersona();
		this.textoBusqueda="";

	}

	public void cancelarEditarPersona(ActionEvent pEvento) {
		
		if (this.modo==BUSCAR_PERSONA) {
			this.personaSelected=null;
		}
		this.personaListaSelected=null;
		this.textoBusqueda="";

	}
	
	public void iniciarPersonas() {
		
		this.personaSelected=null;
		
	}
	
	public List<Ocupacion> completarOcupacion(String query) {
		
		List<Ocupacion> oOcupaciones = new ArrayList<Ocupacion>();
		oOcupaciones=ocupacionService.ListarPorNombre(query);
		
		return oOcupaciones;

	}

	public List<Comunidad> completarComunidad(String query) {
		
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades=comunidadService.ComunidadesPorNombre(this.municipioResidenciaSelectedId, 0, query, 0, 10, 200);
		
		return oComunidades;

	}
	
	/**
	 * Se desencadena cual el usuario cambia de municipio, con lo cual
	 * la comunidad de residencia debe de ser inicializada
	 */
	public void iniciarComunidadResidencia() {
		
		this.comunidadResidenciaSelected=null;
		this.comunidadResidenciaSelectedId=0;
	}

	public void iniciarNumeroAsegurado() {
	
		this.numeroAsegurado="";
	}
	
	/**
	 * Obtiene la lista de departamentos, en caso de que el país de nacimiento
	 * corresponda al nacional
	 */
	public void obtenerDepartamentosNacimiento() {
		
		InfoResultado oResPais = paisService.Encontrar(this.paisNacimientoSelectedId);
		this.setEsNacidoNacional(false);
		if (this.codigoAlfa2Pais!=null && 
				((Pais)oResPais.getObjeto()).getCodigoAlfados().equals(this.codigoAlfa2Pais)) {
			
			this.setEsNacidoNacional(true);
			this.departamentosNacimiento=divisionPoliticaService.DepartamentosActivos();
			this.municipioNacimientoSelectedId=0;
			this.municipiosNacimiento=null;
		} else {
			this.departamentosNacimiento=null;
			this.departamentoNacimientoSelectedId=0;
			this.municipiosNacimiento=null;
			this.municipioNacimientoSelectedId=0;
		}
		
	}
	
	public void obtenerMunicipiosNacimiento() {
		
		if (this.departamentoNacimientoSelectedId!=0) {
			this.municipiosNacimiento=divisionPoliticaService.MunicipiosActivos(this.departamentoNacimientoSelectedId);
		} else {
			this.municipiosNacimiento=null;
			this.municipioNacimientoSelectedId=0;
		}
	}

	public void obtenerMunicipiosResidencia() {
		
		if (this.departamentoResidenciaSelectedId!=0) {
			this.municipiosResidencia=divisionPoliticaService.MunicipiosActivos(this.departamentoResidenciaSelectedId);
		} else {
			this.municipiosResidencia=null;
			this.municipioResidenciaSelectedId=0;
		}
	}

	public void setTextoBusqueda(String textoBusqueda) {
		this.textoBusqueda = textoBusqueda;
	}

	public String getTextoBusqueda() {
		return textoBusqueda;
	}

	public void setPersonaSelected(Persona personaSelected) {
		this.personaSelected = personaSelected;
	}

	public Persona getPersonaSelected() {
		return personaSelected;
	}

	public void setPersonas(LazyDataModel<Persona> personas) {
		this.personas = personas;
	}

	public LazyDataModel<Persona> getPersonas() {
		return personas;
	}

	public Persona getPersonaListaSelected() {
		return personaListaSelected;
	}

	public void setPersonaListaSelected(Persona personaListaSelected) {
		this.personaListaSelected = personaListaSelected;
	}

	public void setModo(int modo) {
		this.modo = modo;
	}

	public int getModo() {
		return modo;
	}

	public void setTipoIdentificacionSelectedId(long tipoIdentificacionSelectedId) {
		this.tipoIdentificacionSelectedId = tipoIdentificacionSelectedId;
	}

	public long getTipoIdentificacionSelectedId() {
		return tipoIdentificacionSelectedId;
	}

	public void setTiposIdentificaciones(List<TipoIdentificacion> tiposIdentificaciones) {
		this.tiposIdentificaciones = tiposIdentificaciones;
	}

	public List<TipoIdentificacion> getTiposIdentificaciones() {
		return tiposIdentificaciones;
	}

	public void setSexoSelectedId(long sexoSelectedId) {
		this.sexoSelectedId = sexoSelectedId;
	}

	public long getSexoSelectedId() {
		return sexoSelectedId;
	}

	public void setSexos(List<Sexo> sexos) {
		this.sexos = sexos;
	}

	public List<Sexo> getSexos() {
		return sexos;
	}

	public void setEtniaSelectedId(long etniaSelectedId) {
		this.etniaSelectedId = etniaSelectedId;
	}

	public long getEtniaSelectedId() {
		return etniaSelectedId;
	}

	public void setEtnias(List<Etnia> etnias) {
		this.etnias = etnias;
	}

	public List<Etnia> getEtnias() {
		return etnias;
	}

	public void setEstadoCivilSelectedId(long estadoCivilSelectedId) {
		this.estadoCivilSelectedId = estadoCivilSelectedId;
	}

	public long getEstadoCivilSelectedId() {
		return estadoCivilSelectedId;
	}

	public void setEstadosCiviles(List<EstadoCivil> estadosCiviles) {
		this.estadosCiviles = estadosCiviles;
	}

	public List<EstadoCivil> getEstadosCiviles() {
		return estadosCiviles;
	}

	public void setPaisNacimientoSelectedId(long paisNacimientoSelectedId) {
		this.paisNacimientoSelectedId = paisNacimientoSelectedId;
	}

	public long getPaisNacimientoSelectedId() {
		return paisNacimientoSelectedId;
	}

	public void setPaisesNacimiento(List<Pais> paisesNacimiento) {
		this.paisesNacimiento = paisesNacimiento;
	}

	public List<Pais> getPaisesNacimiento() {
		return paisesNacimiento;
	}

	public void setDepartamentoNacimientoSelectedId(
			long departamentoNacimientoSelectedId) {
		this.departamentoNacimientoSelectedId = departamentoNacimientoSelectedId;
	}

	public long getDepartamentoNacimientoSelectedId() {
		return departamentoNacimientoSelectedId;
	}

	public void setDepartamentosNacimiento(List<DivisionPolitica> departamentosNacimiento) {
		this.departamentosNacimiento = departamentosNacimiento;
	}

	public List<DivisionPolitica> getDepartamentosNacimiento() {
		return departamentosNacimiento;
	}

	public void setMunicipioNacimientoSelectedId(
			long municipioNacimientoSelectedId) {
		this.municipioNacimientoSelectedId = municipioNacimientoSelectedId;
	}

	public long getMunicipioNacimientoSelectedId() {
		return municipioNacimientoSelectedId;
	}

	public void setMunicipiosNacimiento(List<DivisionPolitica> municipiosNacimiento) {
		this.municipiosNacimiento = municipiosNacimiento;
	}

	public List<DivisionPolitica> getMunicipiosNacimiento() {
		return municipiosNacimiento;
	}

	public void setDepartamentoResidenciaSelectedId(
			long departamentoResidenciaSelectedId) {
		this.departamentoResidenciaSelectedId = departamentoResidenciaSelectedId;
	}

	public long getDepartamentoResidenciaSelectedId() {
		return departamentoResidenciaSelectedId;
	}

	public void setDepartamentosResidencia(List<DivisionPolitica> departamentosResidencia) {
		this.departamentosResidencia = departamentosResidencia;
	}

	public List<DivisionPolitica> getDepartamentosResidencia() {
		return departamentosResidencia;
	}

	public void setMunicipioResidenciaSelectedId(
			long municipioResidenciaSelectedId) {
		this.municipioResidenciaSelectedId = municipioResidenciaSelectedId;
	}

	public long getMunicipioResidenciaSelectedId() {
		return municipioResidenciaSelectedId;
	}

	public void setMunicipiosResidencia(List<DivisionPolitica> municipiosResidencia) {
		this.municipiosResidencia = municipiosResidencia;
	}

	public List<DivisionPolitica> getMunicipiosResidencia() {
		return municipiosResidencia;
	}

	public void setComunidadResidenciaSelectedId(
			long comunidadResidenciaSelectedId) {
		this.comunidadResidenciaSelectedId = comunidadResidenciaSelectedId;
	}

	public long getComunidadResidenciaSelectedId() {
		return comunidadResidenciaSelectedId;
	}

	public void setComunidadesResidencia(List<Comunidad> comunidadesResidencia) {
		this.comunidadesResidencia = comunidadesResidencia;
	}

	public List<Comunidad> getComunidadesResidencia() {
		return comunidadesResidencia;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setTelefonoResidencia(String telefonoResidencia) {
		this.telefonoResidencia = telefonoResidencia;
	}

	public String getTelefonoResidencia() {
		return telefonoResidencia;
	}

	public void setTelefonoMovil(String telefonoMovil) {
		this.telefonoMovil = telefonoMovil;
	}

	public String getTelefonoMovil() {
		return telefonoMovil;
	}

	public void setOcupacionSelected(Ocupacion ocupacionSelected) {
		this.ocupacionSelected = ocupacionSelected;
	}

	public Ocupacion getOcupacionSelected() {
		return ocupacionSelected;
	}

	public void setOcupacionSelectedId(long ocupacionSelectedId) {
		this.ocupacionSelectedId = ocupacionSelectedId;
	}

	public long getOcupacionSelectedId() {
		return ocupacionSelectedId;
	}

	public void setEscolaridadSelectedId(long escolaridadSelectedId) {
		this.escolaridadSelectedId = escolaridadSelectedId;
	}

	public long getEscolaridadSelectedId() {
		return escolaridadSelectedId;
	}

	public void setEscolaridades(List<Escolaridad> escolaridades) {
		this.escolaridades = escolaridades;
	}

	public List<Escolaridad> getEscolaridades() {
		return escolaridades;
	}

	public void setTipoAseguradoSelectedId(long tipoAseguradoSelectedId) {
		this.tipoAseguradoSelectedId = tipoAseguradoSelectedId;
	}

	public long getTipoAseguradoSelectedId() {
		return tipoAseguradoSelectedId;
	}

	public void setTiposAsegurados(List<TipoAsegurado> tiposAsegurados) {
		this.tiposAsegurados = tiposAsegurados;
	}

	public List<TipoAsegurado> getTiposAsegurados() {
		return tiposAsegurados;
	}

	public void setNumeroAsegurado(String numeroAsegurado) {
		this.numeroAsegurado = numeroAsegurado;
	}

	public String getNumeroAsegurado() {
		return numeroAsegurado;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setEdadEnAnios(String edadEnAnios) {
		this.edadEnAnios = edadEnAnios;
	}

	public String getEdadEnAnios() {
		return edadEnAnios;
	}

	public void setIdentNumero(String identNumero) {
		this.identNumero = identNumero;
	}

	public String getIdentNumero() {
		return identNumero;
	}

	public void setCodigoAlfa2Pais(String codigoAlfa2Pais) {
		this.codigoAlfa2Pais = codigoAlfa2Pais;
	}

	public String getCodigoAlfa2Pais() {
		return codigoAlfa2Pais;
	}

	public void setEsNacidoNacional(Boolean esNacidoNacional) {
		this.esNacidoNacional = esNacidoNacional;
	}

	public Boolean getEsNacidoNacional() {
		return esNacidoNacional;
	}

	public void setComunidadResidenciaSelected(
			Comunidad comunidadResidenciaSelected) {
		this.comunidadResidenciaSelected = comunidadResidenciaSelected;
	}

	public Comunidad getComunidadResidenciaSelected() {
		return comunidadResidenciaSelected;
	}

	public void setVerificado(Boolean verificado) {
		this.verificado = verificado;
	}

	public Boolean getVerificado() {
		return verificado;
	}

	public void setEtniasInicial(List<Etnia> etniasInicial) {
		this.etniasInicial = etniasInicial;
	}

	public List<Etnia> getEtniasInicial() {
		return etniasInicial;
	}

	public void setEscolaridadesInicial(List<Escolaridad> escolaridadesInicial) {
		this.escolaridadesInicial = escolaridadesInicial;
	}

	public List<Escolaridad> getEscolaridadesInicial() {
		return escolaridadesInicial;
	}

	public void setTiposAseguradosInicial(List<TipoAsegurado> tiposAseguradosInicial) {
		this.tiposAseguradosInicial = tiposAseguradosInicial;
	}

	public List<TipoAsegurado> getTiposAseguradosInicial() {
		return tiposAseguradosInicial;
	}

	public void setSexosInicial(List<Sexo> sexosInicial) {
		this.sexosInicial = sexosInicial;
	}

	public List<Sexo> getSexosInicial() {
		return sexosInicial;
	}

}
