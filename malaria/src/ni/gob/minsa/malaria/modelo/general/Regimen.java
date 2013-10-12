package ni.gob.minsa.malaria.modelo.general;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;

import java.math.BigDecimal;


/**
 * The persistent class for the REGIMENES database table.
 * 
 */
@Entity
@Table(name="REGIMENES", schema="GENERAL")
@Cache(alwaysRefresh=true,disableHits=true)
public class Regimen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="REGIMENES_REGIMENID_GENERATOR", sequenceName="REGIMENES_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="REGIMENES_REGIMENID_GENERATOR")
	@Column(name="REGIMEN_ID", updatable=false)
	private long regimenId;

	private BigDecimal codigo;

	private String nombre;

	private String pasivo;

    public Regimen() {
    }

	public long getRegimenId() {
		return this.regimenId;
	}

	public void setRegimenId(long regimenId) {
		this.regimenId = regimenId;
	}

	public BigDecimal getCodigo() {
		return this.codigo;
	}

	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
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

}