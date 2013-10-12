package ni.gob.minsa.malaria.modelo.general;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla MARCADORES en el esquema SIVE.
 * 
 */
@Entity
@Table(name="MARCADORES", schema="SIVE")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="marcador.listar",
			query="select ttu from Marcador ttu " +
					"order by ttu.nombre"),
	@NamedQuery(
			name="marcador.encontrarPorCodigo",
			query="select ttu from Marcador ttu " +
			        "where ttu.codigo=:pCodigo")
})
public class Marcador implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MARCADORES_MARCADORID_GENERATOR", sequenceName="MARCADORES_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MARCADORES_MARCADORID_GENERATOR")
	@Column(name="MARCADOR_ID", updatable=false)
	private long marcadorId;

	private String nombre;

	private String url;

	@Column(name="CODIGO",insertable=true,nullable=false,unique=true)
	private String codigo;
	
    public Marcador() {
    }
	
	// esta asignación es importante
	@Override
	public String toString() {
		return nombre;
	}

	public void setMarcadorId(long marcadorId) {
		this.marcadorId = marcadorId;
	}

	public long getMarcadorId() {
		return marcadorId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}