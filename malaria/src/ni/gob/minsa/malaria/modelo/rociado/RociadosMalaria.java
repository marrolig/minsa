package ni.gob.minsa.malaria.modelo.rociado;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Sector;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the ROCIADOS_MALARIA database table.
 * 
 */
@Entity
@Table(name="ROCIADOS_MALARIA")
public class RociadosMalaria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROCIADOID_GENERATOR", sequenceName="SEQ_ROCIADOID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROCIADOID_GENERATOR")
	@Column(name="ROCIADO_ID", unique=true, nullable=false, precision=10)
	private long rociadoId;

	@Column(length=10)
	private String boquilla;

	@Column(nullable=false, precision=3, scale=1)
	private BigDecimal carga;

	@Column(nullable=false, precision=4)
	private BigDecimal cerradas;

	@Column(precision=2)
	private BigDecimal ciclo;

	@ManyToOne
	@JoinColumn(name="COMUNIDAD",referencedColumnName="CODIGO",nullable=false)
	private Comunidad comunidad;

	@Column(nullable=false, precision=4)
	private BigDecimal construccion;

	@Column(nullable=false, precision=4)
	private BigDecimal control;

	@Column(name="DESALOJO_ADECUADO", nullable=false, precision=4)
	private BigDecimal desalojoAdecuado;

	@Column(name="DESALOJO_INADECUADO", nullable=false, precision=4)
	private BigDecimal desalojoInadecuado;

	@Column(nullable=false, precision=4)
	private BigDecimal enfermos;

	@ManyToOne
	@JoinColumn(name="ENTIDAD_ADTVA", nullable=false)
	private EntidadAdtva silais;

	@ManyToOne
	@JoinColumn(name="EQUIPO",referencedColumnName="CODIGO",nullable=false)
	private EquiposMalaria equipo;

    @Temporal( TemporalType.DATE)
	@Column(nullable=false)
	private Date fecha;

	@Column(nullable=false, precision=6, scale=2)
	private BigDecimal formulacion;

	@Column(name="HAB_NO_ROCIADAS", nullable=false, precision=4)
	private BigDecimal habNoRociadas;

	@Column(name="HAB_ROCIADAS", nullable=false, precision=4)
	private BigDecimal habRociadas;

	@ManyToOne
	@JoinColumn(name="INSECTICIDA",referencedColumnName="CODIGO",nullable=false)
	private InsecticidasMalaria insecticida;

	@ManyToOne
	@JoinColumn(name="MUNICIPIO",referencedColumnName="CODIGO_NACIONAL",nullable=false)
	private DivisionPolitica municipio;

	@Column(nullable=false, precision=4)
	private BigDecimal otros;

	@Column(nullable=false, precision=4)
	private BigDecimal renuentes;

	@Column(length=50)
	private String rociador;

	@ManyToOne
	@JoinColumn(name="SECTOR",referencedColumnName="CODIGO",nullable=false)
	private Sector sector;

	@Column(name="TOTAL_CARGAS", nullable=false, precision=6, scale=1)
	private BigDecimal totalCargas;

	@Column(name="VIVIENDAS_PROGRAMADAS", nullable=false, precision=4)
	private BigDecimal viviendasProgramadas;

	@Column(name="VIVIENDAS_ROCIADAS", nullable=false, precision=4)
	private BigDecimal viviendasRociadas;

    public RociadosMalaria() {
    }

	public long getRociadoId() {
		return this.rociadoId;
	}

	public void setRociadoId(long rociadoId) {
		this.rociadoId = rociadoId;
	}

	public String getBoquilla() {
		return this.boquilla;
	}

	public void setBoquilla(String boquilla) {
		this.boquilla = boquilla;
	}

	public BigDecimal getCarga() {
		return this.carga;
	}

	public void setCarga(BigDecimal carga) {
		this.carga = carga;
	}

	public BigDecimal getCerradas() {
		return this.cerradas;
	}

	public void setCerradas(BigDecimal cerradas) {
		this.cerradas = cerradas;
	}

	public BigDecimal getCiclo() {
		return this.ciclo;
	}

	public void setCiclo(BigDecimal ciclo) {
		this.ciclo = ciclo;
	}

	public Comunidad getComunidad() {
		return this.comunidad;
	}

	public void setComunidad(Comunidad comunidad) {
		this.comunidad = comunidad;
	}

	public BigDecimal getConstruccion() {
		return this.construccion;
	}

	public void setConstruccion(BigDecimal construccion) {
		this.construccion = construccion;
	}

	public BigDecimal getControl() {
		return this.control;
	}

	public void setControl(BigDecimal control) {
		this.control = control;
	}

	public BigDecimal getDesalojoAdecuado() {
		return this.desalojoAdecuado;
	}

	public void setDesalojoAdecuado(BigDecimal desalojoAdecuado) {
		this.desalojoAdecuado = desalojoAdecuado;
	}

	public BigDecimal getDesalojoInadecuado() {
		return this.desalojoInadecuado;
	}

	public void setDesalojoInadecuado(BigDecimal desalojoInadecuado) {
		this.desalojoInadecuado = desalojoInadecuado;
	}

	public BigDecimal getEnfermos() {
		return this.enfermos;
	}

	public void setEnfermos(BigDecimal enfermos) {
		this.enfermos = enfermos;
	}

	public EntidadAdtva getSilais() {
		return this.silais;
	}

	public void setSilais(EntidadAdtva silais) {
		this.silais = silais;
	}

	public EquiposMalaria getEquipo() {
		return this.equipo;
	}

	public void setEquipo(EquiposMalaria equipo) {
		this.equipo = equipo;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getFormulacion() {
		return this.formulacion;
	}

	public void setFormulacion(BigDecimal formulacion) {
		this.formulacion = formulacion;
	}

	public BigDecimal getHabNoRociadas() {
		return this.habNoRociadas;
	}

	public void setHabNoRociadas(BigDecimal habNoRociadas) {
		this.habNoRociadas = habNoRociadas;
	}

	public BigDecimal getHabRociadas() {
		return this.habRociadas;
	}

	public void setHabRociadas(BigDecimal habRociadas) {
		this.habRociadas = habRociadas;
	}

	public InsecticidasMalaria getInsecticida() {
		return this.insecticida;
	}

	public void setInsecticida(InsecticidasMalaria insecticida) {
		this.insecticida = insecticida;
	}

	public DivisionPolitica getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(DivisionPolitica municipio) {
		this.municipio = municipio;
	}

	public BigDecimal getOtros() {
		return this.otros;
	}

	public void setOtros(BigDecimal otros) {
		this.otros = otros;
	}

	public BigDecimal getRenuentes() {
		return this.renuentes;
	}

	public void setRenuentes(BigDecimal renuentes) {
		this.renuentes = renuentes;
	}

	public String getRociador() {
		return this.rociador;
	}

	public void setRociador(String rociador) {
		this.rociador = rociador;
	}

	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public BigDecimal getTotalCargas() {
		return this.totalCargas;
	}

	public void setTotalCargas(BigDecimal totalCargas) {
		this.totalCargas = totalCargas;
	}

	public BigDecimal getViviendasProgramadas() {
		return this.viviendasProgramadas;
	}

	public void setViviendasProgramadas(BigDecimal viviendasProgramadas) {
		this.viviendasProgramadas = viviendasProgramadas;
	}

	public BigDecimal getViviendasRociadas() {
		return this.viviendasRociadas;
	}

	public void setViviendasRociadas(BigDecimal viviendasRociadas) {
		this.viviendasRociadas = viviendasRociadas;
	}
	
}