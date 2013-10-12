// -----------------------------------------------
// UnidadSaludBean.java
// -----------------------------------------------

package ni.gob.minsa.malaria.interfaz.estructura;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.datos.estructura.UnidadDA;
import ni.gob.minsa.malaria.datos.general.TipoUnidadDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.general.TipoUnidad;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;
import ni.gob.minsa.malaria.servicios.general.TipoUnidadService;
import ni.gob.minsa.malaria.soporte.GrupoEconomico;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;




/**
 * Servicio para la capa de presentación de la página 
 * catalogos/unidadSalud.xhtml
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 16/11/2010
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class UnidadSaludBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private InfoSesion infoSesion;

	protected Unidad unidad;
	
	// objetos para poblar el combo de entidades administrativas
	private List<EntidadAdtva> entidades;
	
	// entidad administrativa seleccionada en el combo de entidades
	private Long entidadSelectedId;

	// objetos para poblar el combo de tipos de unidades
	private List<TipoUnidad> tiposUnidades;
	
	// tipo de unidad seleccionada en el combo de tipos de unidades
	private Long tipoUnidadSelectedId;
	
	// objetos para poblar la grilla de unidades
	private List<Unidad> unidades;
	
	// unidad seleccionada en la grilla de unidades
	private Unidad unidadSelected;
	
	// información de la unidad
	private long unidad_id;
	private String nombre;
	private String municipio;
	private String grupoEconomico;
	private String direccion;
	private String regimen;
	private String categoria;
	private String razonSocial;
	private BigDecimal longitud;
	private BigDecimal latitud;
	private boolean conectividad;
	private boolean declaraSector;
	private String telefono;
	private String fax;
	private String email;
	
	// usuario que realiza o efectuó registro
	private String usuarioRegistro;
	private String usuarioNombre;
	
	private static UnidadService unidadService = new UnidadDA();
	private static TipoUnidadService tipoUnidadService = new TipoUnidadDA();
	private static EntidadAdtvaService entidadAdtvaService= new EntidadAdtvaDA();
	
	// --------------------------------------- Constructor
	
	public UnidadSaludBean(){

		this.setInfoSesion(Utilidades.obtenerInfoSesion());
		iniciarPropiedades();
	}

	// ------------------------------------------- Métodos
	
	/**
	 * Establece la lista de unidades en la propiedad <b>Unidades</b> que
	 * dependen de una entidad administrativa y que pertenecen a un tipo
	 * de unidad o perfil determinado
	 * 
	 * @param pEntidadId    Identificador de la Entidad Administrativa
	 * @param pTipoUnidadId Identificador del Tipo de Unidad o Perfil
	 * 
	 * @return Lista de unidades de salud
	 */
	protected List<Unidad> UnidadesPorEntidadYTipo(long pEntidadId,long pTipoUnidadId) {
		return unidadService.UnidadesActivasPorEntidadYTipo(pEntidadId,pTipoUnidadId);
	}
	/**
	 * Inicializa las propiedades a un estado en el cual no se encuentra
	 * ninguna unidad seleccionada.
	 */
	protected void iniciarPropiedades() {
		
		iniciarDetalle();
		
		// obtiene las entidades administrativas activas
		this.entidades=entidadAdtvaService.EntidadesAdtvasActivas();
		// obtiene los tipos de unidades activas
		this.tiposUnidades=tipoUnidadService.TiposUnidadesActivas();
		// selecciona la primera entidad administrativa
		if (this.entidades!=null && this.entidades.size()>0) {
			this.entidadSelectedId=this.entidades.get(0).getEntidadAdtvaId();
		}
		// selecciona el primer tipo de unidad
		if (this.tiposUnidades!=null && this.tiposUnidades.size()>0) { 
			this.tipoUnidadSelectedId=this.tiposUnidades.get(0).getTipounidadId();
		}
		// llena la grilla
		if (this.tiposUnidades!=null && this.tiposUnidades.size()>0) {
			this.unidades=UnidadesPorEntidadYTipo(this.entidadSelectedId,this.tipoUnidadSelectedId);
		}
	}
	
	protected void iniciarDetalle() {
		
		this.unidad_id=0;
		this.nombre="";
		this.grupoEconomico="";
		this.usuarioRegistro=this.infoSesion.getUsername();
		this.usuarioNombre=this.infoSesion.getNombre();
		this.direccion="";
		this.regimen="";
		this.categoria="";
		this.unidad=null;
		this.municipio="";
		this.razonSocial="";
		this.telefono="";
		this.fax="";
		this.email="";
		this.conectividad=false;
		this.declaraSector=false;
		this.longitud=null;
		this.latitud=null;
		
	}
	/**
	 * Obtiene las unidades que dependen de una Entidad administrativa
	 * y un tipo de unidad especificado en los combos respectivos, y que
	 * se ejecuta al efectuar un cambio en cualquiera de los dos combos
	 */
	public void obtenerUnidades() {

		this.unidades=UnidadesPorEntidadYTipo(this.entidadSelectedId,this.tipoUnidadSelectedId);
		 // inicializa las variables vinculadas al detalle de la unidad, control de la unidad y usuarios autorizados
		 iniciarDetalle();
		 
	}
	
	public void onUnidadSelected(SelectEvent iEvento) {

		this.unidad=this.unidadSelected;
		this.unidad_id=this.unidadSelected.getUnidadId();
		
		this.nombre=this.unidadSelected.getNombre();
		this.municipio=this.unidadSelected.getMunicipio().getNombre();
		this.direccion=this.unidadSelected.getDireccion();
		this.regimen=this.unidadSelected.getRegimen().getNombre();
		this.grupoEconomico=GrupoEconomico.encontrarPorCodigo(this.unidadSelected.getGrupoEconomico()).getNombre();
		this.categoria=this.unidadSelected.getCategoriaUnidad().getNombre();
		this.telefono=this.unidadSelected.getTelefono();
		this.fax=this.unidadSelected.getFax();
		this.email=this.unidadSelected.getEmail();
		this.razonSocial=this.unidadSelected.getRazonSocial();
		this.conectividad=Utilidades.CadenaABooleano(this.unidadSelected.getConectividad());
		this.declaraSector=Utilidades.CadenaABooleano(this.unidadSelected.getDeclaraSector());
		this.latitud=this.unidadSelected.getLatitud();
		this.longitud=this.unidadSelected.getLongitud();

	}
	
	public void onUnidadUnSelected(UnselectEvent iEvento) {
		
		iniciarDetalle();
		
	}

	// --------------------------------------- Propiedades
	
	/**
	 *  Establece el nombre de la unidad
	 *  
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 *  Obtiene el nombre de la unidad
	 *  
	 * @return Cadena con el nombre de la unidad
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre del grupo economico en el
	 * cual está catalogado el establecimiento
	 * 
	 * @param grupoEconomico Cadena con el nombre del grupo economico
	 */
	public void setGrupoEconomico(String grupoEconomico) {
		this.grupoEconomico = grupoEconomico;
	}

	/**
	 * Obtiene el nombre del grupo economico
	 * del establecimiento o unidad de salud
	 * 
	 * @return Cadena con el nombre del grupo economico
	 */
	public String getGrupoEconomico() {
		return grupoEconomico;
	}

	/**
	 * Establece el nombre del municipio al cual pertenece
	 * geográficamente la unidad
	 * 
	 * @param municipio Nombre del municipio
	 */
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	/**
	 * Obtiene el nombre del municipio al cual pertenece
	 * geográficamente la unidad
	 * 
	 * @return Cadena con el nombre del municipio
	 */
	public String getMunicipio() {
		return municipio;
	}

	public void setUnidad_id(long unidad_id) {
		this.unidad_id = unidad_id;
	}

	public long getUnidad_id() {
		return unidad_id;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	
	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setEntidades(List<EntidadAdtva> entidades) {
		this.entidades = entidades;
	}

	public List<EntidadAdtva> getEntidades() {
		return entidades;
	}

	public void setUnidades(List<Unidad> unidades) {
		this.unidades = unidades;
	}

	public List<Unidad> getUnidades() {
		return unidades;
	}

	public void setUnidadSelected(Unidad unidadSelected) {
		
		this.unidadSelected = unidadSelected;
	}

	public Unidad getUnidadSelected() {
		return unidadSelected;
	}
	
	public void setEntidadSelectedId(Long entidadSelectedId) {
		this.entidadSelectedId = entidadSelectedId;
	}

	public Long getEntidadSelectedId() {
		return entidadSelectedId;
	}

	public void setTiposUnidades(List<TipoUnidad> tiposUnidades) {
		this.tiposUnidades = tiposUnidades;
	}

	public List<TipoUnidad> getTiposUnidades() {
		return tiposUnidades;
	}

	public void setTipoUnidadSelectedId(Long tipoUnidadSelectedId) {
		this.tipoUnidadSelectedId = tipoUnidadSelectedId;
	}

	public Long getTipoUnidadSelectedId() {
		return tipoUnidadSelectedId;
	}

	public void setRegimen(String regimen) {
		this.regimen = regimen;
	}

	public String getRegimen() {
		return regimen;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRazonSocial() {
		return razonSocial;
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

	public void setConectividad(boolean conectividad) {
		this.conectividad = conectividad;
	}

	public boolean isConectividad() {
		return conectividad;
	}

	public void setDeclaraSector(boolean declaraSector) {
		this.declaraSector = declaraSector;
	}

	public boolean isDeclaraSector() {
		return declaraSector;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return fax;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	/**
	 * @param usuarioNombre usuarioNombre 
	 */
	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	/**
	 * @return usuarioNombre
	 */
	public String getUsuarioNombre() {
		return usuarioNombre;
	}

	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	public InfoSesion getInfoSesion() {
		return infoSesion;
	}
}
