package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the INVESTIGACIONES_HOSPITALARIOS database table.
 * 
 */
@Entity
@Table(name="INVESTIGACIONES_HOSPITALARIOS")
public class InvestigacionHospitalario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INVESTIGACIONES_HOSPITALARIOS_INVESTIGACIONHOSPITALARIOID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INVESTIGACIONES_HOSPITALARIOS_INVESTIGACIONHOSPITALARIOID_GENERATOR")
	@Column(name="INVESTIGACION_HOSPITALARIO_ID", unique=true, nullable=false, precision=10)
	private long investigacionHospitalarioId;

	@Column(name="DIAS_ESTANCIA", precision=3)
	private BigDecimal diasEstancia;

	@Column(length=30)
	private String expediente;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_INGRESO")
	private Date fechaIngreso;

	@Column(name="FECHA_REGISTRO", nullable=false)
	private Object fechaRegistro;

	@Column(name="INVESTIGACION_MALARIA", nullable=false, precision=10)
	private BigDecimal investigacionMalaria;

	@Column(name="MANEJO_CLINICO", nullable=false, precision=1)
	private BigDecimal manejoClinico;

	@ManyToOne
	@JoinColumn(name="MUNICIPIO", referencedColumnName="CODIGO_NACIONAL",nullable=false)
	private DivisionPolitica municipio;

	@Column(nullable=false, precision=10)
	private BigDecimal unidad;

	@Column(name="USUARIO_REGISTRO", nullable=false, length=100)
	private String usuarioRegistro;

    public InvestigacionHospitalario() {
    }

	public long getInvestigacionHospitalarioId() {
		return this.investigacionHospitalarioId;
	}

	public void setInvestigacionHospitalarioId(long investigacionHospitalarioId) {
		this.investigacionHospitalarioId = investigacionHospitalarioId;
	}

	public BigDecimal getDiasEstancia() {
		return this.diasEstancia;
	}

	public void setDiasEstancia(BigDecimal diasEstancia) {
		this.diasEstancia = diasEstancia;
	}

	public String getExpediente() {
		return this.expediente;
	}

	public void setExpediente(String expediente) {
		this.expediente = expediente;
	}

	public Date getFechaIngreso() {
		return this.fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Object getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Object fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public BigDecimal getInvestigacionMalaria() {
		return this.investigacionMalaria;
	}

	public void setInvestigacionMalaria(BigDecimal investigacionMalaria) {
		this.investigacionMalaria = investigacionMalaria;
	}

	public BigDecimal getManejoClinico() {
		return this.manejoClinico;
	}

	public void setManejoClinico(BigDecimal manejoClinico) {
		this.manejoClinico = manejoClinico;
	}

	public DivisionPolitica getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(DivisionPolitica municipio) {
		this.municipio = municipio;
	}

	public BigDecimal getUnidad() {
		return this.unidad;
	}

	public void setUnidad(BigDecimal unidad) {
		this.unidad = unidad;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

}