package ni.gob.minsa.malaria.modelo.portal;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.seguridad.UsuarioUnidad;

import org.eclipse.persistence.annotations.Cache;


import java.util.Date;
import java.util.Set;


/**
 * Clase de persistencia para la tabla USUARIOS de la base de datos
 * en el esquema PORTAL 
 */
@Entity
@Table(name="USUARIOS",schema="PORTAL")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="usuarioPorNombre",
			query="select tu from Usuario tu " +
					"where tu.pasivo='0' and tu.userName=:pUsuarioNombre " +
						"and (tu.fechaFin is null or tu.fechaFin>CURRENT_DATE)")
})
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USUARIOS_USUARIOID_GENERATOR", sequenceName="PORTAL.USUARIOS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USUARIOS_USUARIOID_GENERATOR")
	@Column(name="USUARIO_ID", updatable=false)
	private long usuarioId;

	@Column(name="USERNAME")
	private String userName;

	@Column(name="CLAVE",length=50,nullable=false)
	private String clave;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_FIN")
	private Date fechaFin;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO")
	private Date fechaRegistro;

	private String nombre;
	
	private String referencia;
	
	private String email;

	private String pasivo;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="ULTIMA_MODIFICACION")
	private Date ultimaModificacion;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="ULTIMA_SESION")
	private Date ultimaSesion;
    	
	//Asociación bi-direccional muchos a uno con UsuarioUnidad
	@OneToMany(mappedBy="usuario", targetEntity=UsuarioUnidad.class, fetch=FetchType.LAZY)
	private Set<UsuarioUnidad> unidades;

	@SuppressWarnings("unused")
	@Transient
    private boolean vigente;

    public Usuario() {
    }

	public long getUsuarioId() {
		return this.usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPasivo() {
		return this.pasivo;
	}

	public void setPasivo(String pasivo) {
		this.pasivo = pasivo;
	}

	public Date getUltimaModificacion() {
		return this.ultimaModificacion;
	}

	public void setUltimaModificacion(Date ultimaModificacion) {
		this.ultimaModificacion = ultimaModificacion;
	}

	public Date getUltimaSesion() {
		return this.ultimaSesion;
	}

	public void setUltimaSesion(Date ultimaSesion) {
		this.ultimaSesion = ultimaSesion;
	}

	/**
	 * @param clave clave 
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}

	/**
	 * @return clave
	 */
	public String getClave() {
		return clave;
	}

	public boolean isVigente() {
		
		if (this.pasivo.equals("1")) return false;
		if (this.fechaFin==null || this.fechaFin.after(new Date())) return true;
		return false;
	}

	public void setUnidades(Set<UsuarioUnidad> unidades) {
		this.unidades = unidades;
	}

	public Set<UsuarioUnidad> getUnidades() {
		return unidades;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

}