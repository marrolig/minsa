// -----------------------------------------------
// TipoUnidadBean.java
// -----------------------------------------------
package ni.gob.minsa.malaria.interfaz.estructura;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ni.gob.minsa.aplicacion.Seguridad;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.datos.general.TipoUnidadDA;
import ni.gob.minsa.malaria.modelo.general.TipoUnidad;
import ni.gob.minsa.malaria.servicios.general.TipoUnidadService;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;


/**
 * Servicio para la capa de presentación de la página
 * <b>catalogos/tipoUnidad.xhtml</b>
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 16/11/2010
 * @since jdk1.6.0_21
 */
@ManagedBean
@ViewScoped
public class TipoUnidadBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private InfoSesion infoSesion;
	protected TipoUnidad tipoUnidad;
	
	private long tipoUnidad_id;
	private String nombre;
	private Long codigo;
	private Long orden;
	private boolean pasivo;
	private String usuarioRegistro;
	private String usuarioNombre;
	
	private String fechaRegistro;
	
	private List<TipoUnidad> tipoUnidades;
	
	private TipoUnidad tipoUnidadSelected;
	
	private static TipoUnidadService tipoUnidadService = new TipoUnidadDA();

	private Format formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm");
	
	// ---------------------------------------- Constructor
	
	public TipoUnidadBean(){

		this.infoSesion=Utilidades.obtenerInfoSesion();
		iniciarPropiedades();
		this.tipoUnidades=tipoUnidadService.TiposUnidadesActivas();
	}
	
	// -------------------------------------------- Métodos
	
	/**
	 * Inicializa las propiedades a un estado en el cual no se encuentra
	 * ningún tipo de unidad seleccionada.
	 */
	protected void iniciarPropiedades() {
		
		this.tipoUnidad_id=0;
		this.nombre="";
		this.codigo=null;
		this.pasivo=false;
		this.orden=null;
		this.tipoUnidadSelected=null;
		this.usuarioRegistro=this.infoSesion.getUsername();
		this.usuarioNombre=this.infoSesion.getNombre();
		
		Date iFecha=new Date();
		this.fechaRegistro=formatter.format(iFecha);
		
	}

	public void onTipoUnidadSelected(SelectEvent iEvento) {

		this.tipoUnidad=this.tipoUnidadSelected;
		this.tipoUnidad_id=this.tipoUnidadSelected.getTipounidadId();
		this.nombre=this.tipoUnidadSelected.getNombre();
		this.codigo=this.tipoUnidadSelected.getCodigo();
		this.pasivo=Utilidades.CadenaABooleano(this.tipoUnidadSelected.getPasivo());
		this.orden=this.tipoUnidadSelected.getOrden();
		this.usuarioRegistro=this.tipoUnidadSelected.getUsuarioRegistro();
		this.usuarioNombre=Seguridad.NombreUsuario(this.usuarioRegistro);
		
		Date iFecha=this.tipoUnidadSelected.getFechaRegistro();

		this.fechaRegistro=formatter.format(iFecha);

	}

	public void onTipoUnidadUnSelected(UnselectEvent iEvento) {
		
		iniciarPropiedades();
	}

	// ---------------------------------------- Propiedades
	
	/**
	 * Establece el identificador de fila del objeto Tipo Unidad.
	 * Este valor debe ser 0, si se trata de un nuevo objeto.
	 * 
	 * @param ambito_id Entero largo con el identificado del objeto Tipo Unidad
	 */
	public void setTipoUnidad_id(long tipoUnidad_id) {
		this.tipoUnidad_id = tipoUnidad_id;
	}
	/**
	 * Obtiene el identificador de fila del objeto Tipo Unidad.
	 * Este valor será 0, si se trata de un nuevo objeto
	 * 
	 * @return Entero largo con el identificador del objeto Tipo Unidad
	 */
	public long getTipoUnidad_id() {
		return tipoUnidad_id;
	}
	/**
	 * Establece el nombre del Tipo de Unidad
	 * 
	 * @param nombre Cadena de caracteres con el nombre del tipo de Unidad
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * Obtiene el nombre del tipo de unidad
	 * 
	 * @return Cadena de caracteres con el nombre del tipo de unidad
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * Establece el valor booleando que indica si el tipo de unidad
	 * se encuentra habilitado (activo) o no.
	 * 
	 * @param pasivo Verdadero si será inhabilitado (pasivo) o Falso, si se habilitará (activo).
	 */
	public void setPasivo(boolean pasivo) {
		this.pasivo = pasivo;
	}
	/**
	 * Obtiene el valor booleano que indica si el tipo de unidad
	 * se encuentra habilitado (activo) o no.
	 * 
	 * @return Verdadero si está inhabilitado (pasivo) o Falso, habilitado (activo).
	 */
	public boolean isPasivo() {
		return pasivo;
	}
	/**
	 * Establece el número de orden que será utilizado al presentar
	 * la información organizada por tipo de unidades
	 * 
	 * @param orden Número entero con el número de orden.
	 */
	public void setOrden(Long orden) {
		this.orden = orden;
	}
	/**
	 * Obtiene el número de orden que es utilizado al presentar
	 * la información organizada por tipo de unidades.
	 * 
	 * @return Número entero con el número de orden
	 */
	public Long getOrden() {
		return orden;
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
	 * Establece la lista de objetos {@link TipoUnidad} a ser utilizados
	 * en la grilla de tipos de unidades
	 * 
	 * @param tipoUnidades Lista de objetos {@link TipoUnidad}
	 */
	public void setTipoUnidades(List<TipoUnidad> tipoUnidades) {
		this.tipoUnidades = tipoUnidades;
	}
	/**
	 * Obtiene la lista de objetos {@link TipoUnidad} que es utilizada
	 * en la grilla de tipos de unidades
	 * 
	 * @return Lista de objetos {@link TipoUnidad}
	 */
	public List<TipoUnidad> getTipoUnidades() {
		return tipoUnidades;
	}

	/**
	 * Establece el objeto {@link TipoUnidad} que el usuario selecciona en la
	 * grilla de tipos de unidades.  
	 * 
	 * @param tipoUnidadSelected Objeto {@link TipoUnidad} seleccionado
	 */
	public void setTipoUnidadSelected(TipoUnidad tipoUnidadSelected) {
			this.tipoUnidadSelected = tipoUnidadSelected;
	}
	/**
	 * Obtiene el objeto tipo unidad que ha sido seleccionado en la grilla
	 * de tipos de unidades
	 * 
	 * @return Objeto TipoUnidad seleccionado
	 */
	public TipoUnidad getTipoUnidadSelected() {
		return tipoUnidadSelected;
	}
	/**
	 * Establece la fecha de registro del objeto tipo de unidad. Si se trata de
	 * un nuevo objeto, será por omisión la fecha actual del sistema
	 * 
	 * @param fechaRegistro Cadena de caracteres con la fecha de Registro
	 */
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	/**
	 * Obtiene la fecha de registro del objeto tipo de unidad en la base de datos,
	 * en caso de no existir, será por omisión la fecha actual del sistema
	 *  
	 * @return Cadena de caracteres con la fecha de registro
	 */
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	/**
	 * Establece el código institucional del tipo de unidad
	 * 
	 * @param codigo Objeto de entero largo con el código institucional
	 */
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	/**
	 * Obtiene el código institucional del tipo de unidad
	 * 
	 * @return Objeto de entero largo con el código institucional
	 */
	public Long getCodigo() {
		return codigo;
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
