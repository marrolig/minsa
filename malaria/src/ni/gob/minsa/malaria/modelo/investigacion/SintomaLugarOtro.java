package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Pais;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the SINTOMAS_LUGARES_OTROS database table.
 * 
 */
@NamedQueries({
	@NamedQuery(name="SintomaLugarOtro.listar",
			query="select ts from SintomaLugarOtro ts " 
					+ "where ts.investigacionSintoma.investigacionSintomaId=:pInvestigacionSintomaId")
})
@Entity
@Table(name="SINTOMAS_LUGARES_OTROS")
public class SintomaLugarOtro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SINTOMAS_LUGARES_OTROS_SINTOMALUGAROTROID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SINTOMAS_LUGARES_OTROS_SINTOMALUGAROTROID_GENERATOR")
	@Column(name="SINTOMA_LUGAR_OTRO_ID", unique=true, nullable=false, precision=10)
	private long sintomaLugarOtroId;

	@Column(name="AÑO_INICIO", nullable=false, precision=4)
	private BigDecimal añoInicio;

	@Column(nullable=false, precision=1)
	private BigDecimal automedicacion;

	@ManyToOne
	@JoinColumn(name="COMUNIDAD", referencedColumnName="CODIGO")
	private Comunidad comunidad;

	@Column(name="DIAGNOSTICO_POSITIVO", nullable=false, precision=1)
	private BigDecimal diagnosticoPositivo;

	@Column(nullable=false, precision=4)
	private BigDecimal estadia;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO", updatable=false, nullable=false)
	private Date fechaRegistro;

	@Column(name="MES_INICIO", nullable=false, precision=2)
	private BigDecimal mesInicio;

	@ManyToOne
	@JoinColumn(name="MUNICIPIO", referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipio;

	@ManyToOne
	@JoinColumn(name="PAIS", referencedColumnName="CODIGO_ALFADOS")
	private Pais pais;

	@Column(name="TRATAMIENTO_COMPLETO", precision=1)
	private BigDecimal tratamientoCompleto;

	@Column(name="USUARIO_REGISTRO", nullable=false, length=100)
	private String usuarioRegistro;

	//bi-directional many-to-one association to InvestigacionesSintoma
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INVESTIGACION_SINTOMA", nullable=false)
	private InvestigacionSintoma investigacionSintoma;

    public SintomaLugarOtro() {
    }

	public long getSintomaLugarOtroId() {
		return this.sintomaLugarOtroId;
	}

	public void setSintomaLugarOtroId(long sintomaLugarOtroId) {
		this.sintomaLugarOtroId = sintomaLugarOtroId;
	}

	public BigDecimal getAñoInicio() {
		return this.añoInicio;
	}

	public void setAñoInicio(BigDecimal añoInicio) {
		this.añoInicio = añoInicio;
	}

	public BigDecimal getAutomedicacion() {
		return this.automedicacion;
	}

	public void setAutomedicacion(BigDecimal automedicacion) {
		this.automedicacion = automedicacion;
	}

	public Comunidad getComunidad() {
		return this.comunidad;
	}

	public void setComunidad(Comunidad comunidad) {
		this.comunidad = comunidad;
	}

	public BigDecimal getDiagnosticoPositivo() {
		return this.diagnosticoPositivo;
	}

	public void setDiagnosticoPositivo(BigDecimal diagnosticoPositivo) {
		this.diagnosticoPositivo = diagnosticoPositivo;
	}

	public BigDecimal getEstadia() {
		return this.estadia;
	}

	public void setEstadia(BigDecimal estadia) {
		this.estadia = estadia;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public BigDecimal getMesInicio() {
		return this.mesInicio;
	}

	public void setMesInicio(BigDecimal mesInicio) {
		this.mesInicio = mesInicio;
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

	public BigDecimal getTratamientoCompleto() {
		return this.tratamientoCompleto;
	}

	public void setTratamientoCompleto(BigDecimal tratamientoCompleto) {
		this.tratamientoCompleto = tratamientoCompleto;
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