package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the INVESTIGACIONES_SINTOMAS database table.
 * 
 */
@Entity
@Table(name="INVESTIGACIONES_SINTOMAS")
public class InvestigacionSintoma implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INVESTIGACIONES_SINTOMAS_INVESTIGACIONSINTOMAID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INVESTIGACIONES_SINTOMAS_INVESTIGACIONSINTOMAID_GENERATOR")
	@Column(name="INVESTIGACION_SINTOMA_ID", unique=true, nullable=false, precision=10)
	private long investigacionSintomaId;

	@Column(name="ESTADO_FEBRIL", nullable=false, length=50)
	private String estadoFebril;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_INICIO_SINTOMAS")
	private Date fechaInicioSintomas;

	@Column(name="INICIO_RESIDENCIA", nullable=false, precision=1)
	private BigDecimal inicioResidencia;

	@Column(name="INVESTIGACION_MALARIA", nullable=false, precision=10)
	private BigDecimal investigacionMalaria;

	@Column(name="PERSONAS_SINTOMAS", nullable=false, precision=1)
	private BigDecimal personasSintomas;

	@Column(nullable=false, precision=1)
	private BigDecimal sintomatico;

	//bi-directional many-to-one association to SintomasLugaresAnte
	@OneToMany(mappedBy="investigacionSintoma")
	private Set<SintomaLugarAnte> sintomaLugarAntes;

	//bi-directional many-to-one association to SintomasLugaresOtro
	@OneToMany(mappedBy="investigacionSintoma")
	private Set<SintomaLugarOtro> sintomaLugarOtros;

    public InvestigacionSintoma() {
    }

	public long getInvestigacionSintomaId() {
		return this.investigacionSintomaId;
	}

	public void setInvestigacionSintomaId(long investigacionSintomaId) {
		this.investigacionSintomaId = investigacionSintomaId;
	}

	public String getEstadoFebril() {
		return this.estadoFebril;
	}

	public void setEstadoFebril(String estadoFebril) {
		this.estadoFebril = estadoFebril;
	}

	public Date getFechaInicioSintomas() {
		return this.fechaInicioSintomas;
	}

	public void setFechaInicioSintomas(Date fechaInicioSintomas) {
		this.fechaInicioSintomas = fechaInicioSintomas;
	}

	public BigDecimal getInicioResidencia() {
		return this.inicioResidencia;
	}

	public void setInicioResidencia(BigDecimal inicioResidencia) {
		this.inicioResidencia = inicioResidencia;
	}

	public BigDecimal getInvestigacionMalaria() {
		return this.investigacionMalaria;
	}

	public void setInvestigacionMalaria(BigDecimal investigacionMalaria) {
		this.investigacionMalaria = investigacionMalaria;
	}

	public BigDecimal getPersonasSintomas() {
		return this.personasSintomas;
	}

	public void setPersonasSintomas(BigDecimal personasSintomas) {
		this.personasSintomas = personasSintomas;
	}

	public BigDecimal getSintomatico() {
		return this.sintomatico;
	}

	public void setSintomatico(BigDecimal sintomatico) {
		this.sintomatico = sintomatico;
	}

	public Set<SintomaLugarAnte> getSintomasLugaresAntes() {
		return this.sintomaLugarAntes;
	}

	public void setSintomasLugaresAntes(Set<SintomaLugarAnte> sintomaLugarAntes) {
		this.sintomaLugarAntes = sintomaLugarAntes;
	}
	
	public Set<SintomaLugarOtro> getSintomasLugaresOtros() {
		return this.sintomaLugarOtros;
	}

	public void setSintomasLugaresOtros(Set<SintomaLugarOtro> sintomaLugarOtros) {
		this.sintomaLugarOtros = sintomaLugarOtros;
	}
	
}