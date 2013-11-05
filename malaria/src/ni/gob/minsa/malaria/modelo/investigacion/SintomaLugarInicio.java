package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Pais;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the SINTOMAS_LUGARES_INICIO database table.
 * 
 */
@NamedQueries({
	@NamedQuery(name="SintomaLugarInicio.encontrarPorInvestigacionSintoma",
			query="select ts from SintomaLugarInicio ts " 
				+ "where ts.investigacionSintoma.investigacionSintomaId = :pInvestigacionSintomaId")
})
@Entity
@Table(name="SINTOMAS_LUGARES_INICIO")
public class SintomaLugarInicio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INV_LUGINICIO_ID_GENERATOR", sequenceName="SIVE.INV_LUGINICIO_SINTOMAS_SEQ",allocationSize=1 )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INV_LUGINICIO_ID_GENERATOR")
	@Column(name="SINTOMA_LUGAR_INICIO_ID", unique=true, nullable=false, precision=10)
	private long sintomaLugarInicioId;

	@ManyToOne
	@JoinColumn(name="COMUNIDAD", referencedColumnName="CODIGO")
	private Comunidad comunidad;

	@Column(precision=4)
	private BigDecimal estadia;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO", updatable=false, nullable=false)
	private Date fechaRegistro;

	@Column(name="INICIO_RESIDENCIA", nullable=false, precision=1)
	private BigDecimal inicioResidencia;
	
	@OneToOne
	@JoinColumn(name="INVESTIGACION_SINTOMA", referencedColumnName="INVESTIGACION_SINTOMA_ID",nullable=false)
	private InvestigacionSintoma investigacionSintoma;

	@ManyToOne
	@JoinColumn(name="MUNICIPIO", referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipio;

	@ManyToOne
	@JoinColumn(name="PAIS", referencedColumnName="CODIGO_ALFADOS")
	private Pais pais;

	@Column(name="USUARIO_REGISTRO", nullable=false, length=100)
	private String usuarioRegistro;

    public SintomaLugarInicio() {
    }

	public long getSintomaLugarInicioId() {
		return this.sintomaLugarInicioId;
	}

	public void setSintomaLugarInicioId(long sintomaLugarInicioId) {
		this.sintomaLugarInicioId = sintomaLugarInicioId;
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

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public BigDecimal getInicioResidencia() {
		return this.inicioResidencia;
	}

	public void setInicioResidencia(BigDecimal inicioResidencia) {
		this.inicioResidencia = inicioResidencia;
	}

	public InvestigacionSintoma getInvestigacionSintoma() {
		return this.investigacionSintoma;
	}

	public void setInvestigacionSintoma(InvestigacionSintoma investigacionSintoma) {
		this.investigacionSintoma = investigacionSintoma;
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

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

}