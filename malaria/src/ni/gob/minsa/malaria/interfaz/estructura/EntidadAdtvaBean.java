// -----------------------------------------------
// EntidadAdtvaBean.java
// -----------------------------------------------
package ni.gob.minsa.malaria.interfaz.estructura;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ni.gob.minsa.aplicacion.Seguridad;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;


/**
 * Servicio para la capa de presentación de la página
 * <b>/catalogos/entidadAdtva.xhtml</b>
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 15/11/2010
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class EntidadAdtvaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private InfoSesion infoSesion;
	
	protected EntidadAdtva entidadAdtva;

	// objetos para poblar la grilla de entidades administrativas
	private List<EntidadAdtva> entidades;
	
	// entidad seleccionada en la grilla de entidades administrativas
	private EntidadAdtva entidadSelected;

	// información de la entidad administrativa
	private long entidadAdtva_id;
	private String nombre;
	private Long codigo;
	private String municipio;
	private String telefono;
	private String fax;
	private String email;
	private BigDecimal latitud;
	private BigDecimal longitud;
	
	private boolean pasivo;
	private String usuarioRegistro;
	private String usuarioNombre;
	private String fechaRegistro;
	
	private static EntidadAdtvaService entidadAdtvaService = new EntidadAdtvaDA();

	private Format formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm");
	
	// ---------------------------------------- Constructor
	
	public EntidadAdtvaBean(){

		this.infoSesion=Utilidades.obtenerInfoSesion();
		iniciarPropiedades();
		this.entidades=entidadAdtvaService.EntidadesAdtvasActivas();
	}
	
	// -------------------------------------------- Métodos
	
	/**
	 * Inicializa las propiedades a un estado en el cual no se encuentra
	 * ninguna vacuna seleccionada.
	 */
	protected void iniciarPropiedades() {
		
		this.entidadAdtva_id=0;
		this.nombre="";
		this.codigo=null;
		this.setMunicipio("");
		this.setTelefono("");
		this.setFax("");
		this.setEmail("");
		this.setLatitud(null);
		this.setLongitud(null);
		this.pasivo=false;

		this.entidadSelected=null;
		this.usuarioRegistro=this.infoSesion.getUsername();
		this.usuarioNombre=this.infoSesion.getNombre();
		
		Date iFecha=new Date();
		this.fechaRegistro=formatter.format(iFecha);
		
	}

	public void onEntidadSelected(SelectEvent iEvento) {

		this.entidadAdtva=this.entidadSelected;
		this.entidadAdtva_id=this.entidadSelected.getEntidadAdtvaId();
		this.nombre=this.entidadSelected.getNombre();
		this.codigo=this.entidadSelected.getCodigo();
		this.setLatitud(this.entidadSelected.getLatitud());
		this.setLongitud(this.entidadSelected.getLongitud());
		this.setTelefono(this.entidadSelected.getTelefono());
		this.setFax(this.entidadSelected.getFax());
		this.setEmail(this.entidadSelected.getEmail());
		this.setMunicipio(this.entidadSelected.getMunicipio().getNombre());
		this.pasivo=Utilidades.CadenaABooleano(this.entidadSelected.getPasivo());
		
		this.usuarioRegistro=this.entidadSelected.getUsuarioRegistro();
		this.usuarioNombre=Seguridad.NombreUsuario(this.usuarioRegistro);
		
		Date iFecha=this.entidadSelected.getFechaRegistro();

		this.fechaRegistro=formatter.format(iFecha);

	}

	public void onEntidadUnSelected(UnselectEvent iEvento) {
		
		iniciarPropiedades();
		
	}

	// ---------------------------------------- Propiedades
	
	/**
	 * Establece el identificador de fila del objeto {@link EntidadAdtva}.
	 * Este valor debe ser 0, si se trata de un nuevo objeto.
	 * 
	 * @param entidadAdtva_id Entero largo con el identificado del objeto {@link EntidadAdtva}
	 */
	public void setEntidadAdtva_id(long entidadAdtva_id) {
		this.entidadAdtva_id = entidadAdtva_id;
	}
	/**
	 * Obtiene el identificador de fila del objeto {@link EntidadAdtva}.
	 * Este valor será 0, si se trata de un nuevo objeto
	 * 
	 * @return Entero largo con el identificador del objeto {@link EntidadAdtva}
	 */
	public long getEntidadAdtva_id() {
		return entidadAdtva_id;
	}
	/**
	 * Establece el nombre de la entidad administrativa
	 * 
	 * @param nombre Cadena de caracteres con el nombre de la entidad administrativa
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * Obtiene el nombre de la entidad administrativa
	 * 
	 * @return Cadena de caracteres con el nombre de la entidad administrativa
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * Establece el valor booleando que indica si la entidad administrativa
	 * se encuentra habilitada (activo) o no.
	 * 
	 * @param pasivo Verdadero si será inhabilitada (pasivo) o Falso, si se habilitará (activo).
	 */
	public void setPasivo(boolean pasivo) {
		this.pasivo = pasivo;
	}
	/**
	 * Obtiene el valor booleano que indica si la entidad administrativa
	 * se encuentra habilitada (activo) o no.
	 * 
	 * @return Verdadero si está inhabilitado (pasivo) o Falso, habilitado (activo).
	 */
	public boolean isPasivo() {
		return pasivo;
	}
	/**
	 * Establece el nombre del usuario que registró un objeto
	 * existente o el usuario que será utilizado como responsable
	 * de su inserción en la base de datos
	 * 
	 * @param usuarioRegistro Cadena de caracteres con el nombre del usuario
	 */
	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	/**
	 * Obtiene el usuario que registró un objeto existente
	 * o el usuario que será utilizado como responsable de su
	 * inserción en la base de datos.
	 *  
	 * @return Cadena de caracteres con el nombre del usuario
	 */
	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}
	/**
	 * Establece la lista de objetos {@link EntidadAdtva} a ser utilizados
	 * en la grilla de entidades administrativas
	 * 
	 * @param vacunas Lista de objetos {@link EntidadAdtva}
	 */
	public void setEntidades(List<EntidadAdtva> entidades) {
		this.entidades = entidades;
	}
	/**
	 * Obtiene la lista de objetos {@link EntidadAdtva} que es utilizada
	 * en la grilla de entidades
	 * 
	 * @return Lista de objetos {@link EntidadAdtva}
	 */
	public List<EntidadAdtva> getEntidades() {
		return entidades;
	}
	/**
	 * Establece el objeto {@link EntidadAdtva} que el usuario selecciona en la
	 * grilla de entidades administrativas.  
	 * 
	 * @param entidadSelected Objeto {@link EntidadAdtva} seleccionado
	 */
	public void setEntidadSelected(EntidadAdtva entidadSelected) {
			this.entidadSelected = entidadSelected;
	}

	/**
	 * Obtiene el objeto {@link EntidadAdtva} que ha sido seleccionado en la grilla
	 * de entidades administrativas
	 * 
	 * @return Objeto {@link EntidadAdtva} seleccionado
	 */
	public EntidadAdtva getEntidadSelected() {
		return entidadSelected;
	}
	/**
	 * Establece la fecha de registro del objeto {@link EntidadAdtva}. Si se trata de
	 * un nuevo objeto, será por omisión la fecha actual del sistema
	 * 
	 * @param fechaRegistro Cadena de caracteres con la fecha de Registro
	 */
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	/**
	 * Obtiene la fecha de registro del objeto {@link EntidadAdtva} en la base de datos,
	 * en caso de no existir, será por omisión la fecha actual del sistema
	 *  
	 * @return Cadena de caracteres con la fecha de registro
	 */
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	/**
	 * Establece el código institucional para la entidad administrativa.
	 * Si el código es cero, se considerará nulo en la base de datos.
	 * 
	 * @param codigo Objeto de entero largo que representa el código institucional
	 */
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	/**
	 * Obtiene el código institucional asignado a la entidad administrativa.
	 * 
	 * @return
	 */
	public Long getCodigo() {
		return codigo;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getMunicipio() {
		return municipio;
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

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public BigDecimal getLongitud() {
		return longitud;
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
