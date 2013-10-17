package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the INVESTIGACIONES_LUGARES database table.
 * 
 */
@Entity
@Table(name="INVESTIGACIONES_LUGARES")
public class InvestigacionLugar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INVESTIGACIONES_LUGARES_INVSMALARIALUGARID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INVESTIGACIONES_LUGARES_INVSMALARIALUGARID_GENERATOR")
	@Column(name="INVSMALARIA_LUGAR_ID", unique=true, nullable=false, precision=10)
	private long invsmalariaLugarId;

	@ManyToOne
	@JoinColumn(name="COMUNIDAD", referencedColumnName="CODIGO")
	private Comunidad comunidad;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO", updatable=false, nullable=false)
	private Date fechaRegistro;

	@Column(name="INFECCION_RESIDENCIA", nullable=false, precision=1)
	private BigDecimal infeccionResidencia;

	@Column(name="INVESTIGACION_MALARIA", nullable=false, precision=10)
	private BigDecimal investigacionMalaria;

	@ManyToOne
	@JoinColumn(name="MUNICIPIO", referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipio;

	@Column(length=2)
	private String pais;

	@Column(name="USUARIO_REGISTRO", nullable=false, length=100)
	private String usuarioRegistro;

    public InvestigacionLugar() {
    }

	public long getInvsmalariaLugarId() {
		return this.invsmalariaLugarId;
	}

	public void setInvsmalariaLugarId(long invsmalariaLugarId) {
		this.invsmalariaLugarId = invsmalariaLugarId;
	}

	public Comunidad getComunidad() {
		return this.comunidad;
	}

	public void setComunidad(Comunidad comunidad) {
		this.comunidad = comunidad;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public BigDecimal getInfeccionResidencia() {
		return this.infeccionResidencia;
	}

	public void setInfeccionResidencia(BigDecimal infeccionResidencia) {
		this.infeccionResidencia = infeccionResidencia;
	}

	public BigDecimal getInvestigacionMalaria() {
		return this.investigacionMalaria;
	}

	public void setInvestigacionMalaria(BigDecimal investigacionMalaria) {
		this.investigacionMalaria = investigacionMalaria;
	}

	public DivisionPolitica getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(DivisionPolitica municipio) {
		this.municipio = municipio;
	}

	public String getPais() {
		return this.pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

}