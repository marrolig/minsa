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
@NamedQueries({
	@NamedQuery(name="InvestigacionSintoma.encontrarPorInvestigacionMalaria",
		query="select ti from InvestigacionSintoma ti " +
				"where ti.investigacionMalaria.investigacionMalariaId=:pInvestigacionMalariaId")
})
@Entity
@Table(name="INVESTIGACIONES_SINTOMAS")
public class InvestigacionSintoma implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INV_SINTOMAS_ID_GENERATOR", sequenceName="SIVE.INV_SINTOMAS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INV_SINTOMAS_ID_GENERATOR")
	@Column(name="INVESTIGACION_SINTOMA_ID", unique=true, nullable=false, precision=10)
	private long investigacionSintomaId;

	@ManyToOne
	@JoinColumn(name="ESTADO_FEBRIL", referencedColumnName="CODIGO",nullable=false)
	private EstadoFebril estadoFebril;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_INICIO_SINTOMAS")
	private Date fechaInicioSintomas;

	@Column(name="INICIO_RESIDENCIA", nullable=false, precision=1)
	private BigDecimal inicioResidencia;

	 @OneToOne( optional = false,targetEntity=InvestigacionMalaria.class)
	 @JoinColumns( {  
           @JoinColumn(name = "INVESTIGACION_MALARIA", referencedColumnName = "INVESTIGACION_MALARIA_ID",nullable=false),  
           @JoinColumn(name = "SINTOMATICO", referencedColumnName = "SINTOMATICO",nullable=false) 
           }) 
	private InvestigacionMalaria investigacionMalaria;

	@Column(name="PERSONAS_SINTOMAS", nullable=false, precision=1)
	private BigDecimal personasSintomas;

	@Column(name="SINTOMATICO",nullable=false, precision=1,insertable=false, updatable=false )
	private BigDecimal sintomatico;
	
	//bi-directional many-to-one association to SintomasLugaresAnte
	@OneToOne(mappedBy="investigacionSintoma",targetEntity=SintomaLugarInicio.class,optional=true,fetch=FetchType.LAZY,cascade=CascadeType.ALL, orphanRemoval = true)
	private SintomaLugarInicio sintomaLugarInicio;
	
	//bi-directional many-to-one association to SintomasLugaresAnte
	@OneToMany(mappedBy="investigacionSintoma",targetEntity=SintomaLugarAnte.class,fetch=FetchType.LAZY,cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<SintomaLugarAnte> sintomaLugarAntes;

	//bi-directional many-to-one association to SintomasLugaresOtro
	@OneToMany(mappedBy="investigacionSintoma",targetEntity=SintomaLugarOtro.class,fetch=FetchType.LAZY,cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<SintomaLugarOtro> sintomaLugarOtros;

    public InvestigacionSintoma() {
    }

	public long getInvestigacionSintomaId() {
		return this.investigacionSintomaId;
	}

	public void setInvestigacionSintomaId(long investigacionSintomaId) {
		this.investigacionSintomaId = investigacionSintomaId;
	}

	public EstadoFebril getEstadoFebril() {
		return this.estadoFebril;
	}

	public void setEstadoFebril(EstadoFebril estadoFebril) {
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

	public InvestigacionMalaria getInvestigacionMalaria() {
		return this.investigacionMalaria;
	}

	public void setInvestigacionMalaria(InvestigacionMalaria investigacionMalaria) {
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

	public void setSintomaLugarInicio(SintomaLugarInicio sintomaLugarInicio) {
		this.sintomaLugarInicio = sintomaLugarInicio;
	}

	public SintomaLugarInicio getSintomaLugarInicio() {
		return sintomaLugarInicio;
	}
	
}