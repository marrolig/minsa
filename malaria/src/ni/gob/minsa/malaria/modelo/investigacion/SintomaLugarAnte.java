package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Pais;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the SINTOMAS_LUGARES_ANTES database table.
 * 
 */
@Entity
@Table(name="SINTOMAS_LUGARES_ANTES")
public class SintomaLugarAnte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SINTOMAS_LUGARES_ANTES_SINTOMALUGARANTESID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SINTOMAS_LUGARES_ANTES_SINTOMALUGARANTESID_GENERATOR")
	@Column(name="SINTOMA_LUGAR_ANTES_ID", unique=true, nullable=false, precision=10)
	private long sintomaLugarAntesId;

	@ManyToOne
	@JoinColumn(name="COMUNIDAD", referencedColumnName="CODIGO")
	private Comunidad comunidad;

	@Column(nullable=false, precision=4)
	private BigDecimal estadia;

	@Column(name="FECHA_REGISTRO", nullable=false)
	private Object fechaRegistro;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_ULTIMA", nullable=false)
	private Date fechaUltima;

    @ManyToOne
	@JoinColumn(name="MUNICIPIO", referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipio;

	@ManyToOne
	@JoinColumn(name="PAIS", referencedColumnName="CODIGO_ALFADOS")
	private Pais pais;

	@Column(name="PERSONAS_SINTOMAS", nullable=false, precision=1)
	private BigDecimal personasSintomas;

	@Column(name="USUARIO_REGISTRO", nullable=false, length=100)
	private String usuarioRegistro;

	//bi-directional many-to-one association to InvestigacionesSintoma
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INVESTIGACION_SINTOMA", nullable=false)
	private InvestigacionSintoma investigacionSintoma;

    public SintomaLugarAnte() {
    }

	public long getSintomaLugarAntesId() {
		return this.sintomaLugarAntesId;
	}

	public void setSintomaLugarAntesId(long sintomaLugarAntesId) {
		this.sintomaLugarAntesId = sintomaLugarAntesId;
	}

	public Comunidad getComunidad() {
		return this.comunidad;
	}

	public void setComunidad(Comunidad comunidad) {
		this.comunidad = comunidad;
	}

	public BigDecimal getEstadia() {
		return this.estadia;
	}

	public void setEstadia(BigDecimal estadia) {
		this.estadia = estadia;
	}

	public Object getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Object fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaUltima() {
		return this.fechaUltima;
	}

	public void setFechaUltima(Date fechaUltima) {
		this.fechaUltima = fechaUltima;
	}

	public DivisionPolitica getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(DivisionPolitica municipio) {
		this.municipio = municipio;
	}

	public Pais getPais() {
		return this.pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public BigDecimal getPersonasSintomas() {
		return this.personasSintomas;
	}

	public void setPersonasSintomas(BigDecimal personasSintomas) {
		this.personasSintomas = personasSintomas;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public InvestigacionSintoma getInvestigacionesSintoma() {
		return this.investigacionSintoma;
	}

	public void setInvestigacionesSintoma(InvestigacionSintoma investigacionSintoma) {
		this.investigacionSintoma = investigacionSintoma;
	}
	
}