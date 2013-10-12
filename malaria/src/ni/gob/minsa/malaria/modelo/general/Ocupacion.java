package ni.gob.minsa.malaria.modelo.general;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.eclipse.persistence.annotations.Cache;

import java.util.Date;


/**
 * Clase de persistencia para la tabla OCUPACIONES.
 * 
 */
@Entity
@Table(name="OCUPACIONES", schema="GENERAL")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="ocupacion.listarActivos",
			query="select to from Ocupacion to " +
					"where to.pasivo=0 and " +
					"	   (:pOcupacion IS NULL or to.codigo=:pOcupacion) " +
					"order by to.nombre"),
	@NamedQuery(
			name="ocupacion.listarPorNombre",
			query="select to from Ocupacion to " +
						"where to.pasivo=0 and " +
							"UPPER(to.nombre) LIKE :pNombre " +
						"order by to.nombre")
					
})
public class Ocupacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OCUPACIONES_OCUPACIONID_GENERATOR", sequenceName="OCUPACIONES_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OCUPACIONES_OCUPACIONID_GENERATOR")
	@Column(name="OCUPACION_ID", updatable=false)
	private long ocupacionId;

	
	@NotNull(message="El nombre de la ocupación es requerido")
	@Column(name="NOMBRE", updatable=true, length=100)
	private String nombre;

	private String pasivo;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO", updatable=false)
	private Date fechaRegistro;

	@Column(name="USUARIO_REGISTRO")
	private String usuarioRegistro;

	@Column(name="CODIGO",insertable=true,nullable=false,unique=true)
	private String codigo;
	
    public Ocupacion() {
    }
	
	public long getOcupacionId() {
		return ocupacionId;
	}

	public void setOcupacionId(long ocupacionId) {
		this.ocupacionId = ocupacionId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPasivo() {
		return pasivo;
	}

	public void setPasivo(String pasivo) {
		this.pasivo = pasivo;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	// esta asignación es importante
	@Override
	public String toString() {
		return nombre;
	}

}