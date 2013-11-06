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
	private Float carga;

	@Column(nullable=false, precision=4)
	private Short cerradas;

	@Column(precision=2)
	private Short ciclo;

	@ManyToOne
	@JoinColumn(name="COMUNIDAD",referencedColumnName="CODIGO",nullable=false)
	private Comunidad comunidad;

	@Column(nullable=false, precision=4)
	private Short construccion;

	@Column(nullable=false, precision=4)
	private Short control;

	@Column(name="DESALOJO_ADECUADO", nullable=false, precision=4)
	private Short desalojoAdecuado;

	@Column(name="DESALOJO_INADECUADO", nullable=false, precision=4)
	private Short desalojoInadecuado;

	@Column(nullable=false, precision=4)
	private Short enfermos;

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
	private Float formulacion;

	@Column(name="HAB_NO_ROCIADAS", nullable=false, precision=4)
	private Short habNoRociadas;

	@Column(name="HAB_ROCIADAS", nullable=false, precision=4)
	private Short habRociadas;

	@ManyToOne
	@JoinColumn(name="INSECTICIDA",referencedColumnName="CODIGO",nullable=false)
	private InsecticidasMalaria insecticida;

	@ManyToOne
	@JoinColumn(name="MUNICIPIO",referencedColumnName="CODIGO_NACIONAL",nullable=false)
	private DivisionPolitica municipio;

	@Column(nullable=false, precision=4)
	private Short otros;

	@Column(nullable=false, precision=4)
	private Short renuentes;

	@Column(length=50)
	private String rociador;

	@ManyToOne
	@JoinColumn(name="SECTOR",referencedColumnName="CODIGO",nullable=false)
	private Sector sector;

	@Column(name="TOTAL_CARGAS", nullable=false, precision=6, scale=1)
	private Float totalCargas;

	@Column(name="VIVIENDAS_PROGRAMADAS", nullable=false, precision=4)
	private Short viviendasProgramadas;

	@Column(name="VIVIENDAS_ROCIADAS", nullable=false, precision=4)
	private Short viviendasRociadas;
	
    @Temporal( TemporalType.DATE)
	@Column(nullable=false)
	private Date fechaRegistro;
	
    @Column(name="USUARIO_REGISTRO",length=60)
	private String usuarioRegistro;
	
	@Column(name="HABITANTES_PROTEGIDOS",nullable=false,precision=4)
	private Short habitantesProtegidos;
	
	
	@Column(name="TOTAL_UTILIZADAS", nullable=false, precision=6, scale=1)
	private Float totalUtilizadas; 

    public RociadosMalaria() {
    }

	public long getRociadoId() {
		return rociadoId;
	}

	public void setRociadoId(long rociadoId) {
		this.rociadoId = rociadoId;
	}

	public String getBoquilla() {
		return boquilla;
	}

	public void setBoquilla(String boquilla) {
		this.boquilla = boquilla;
	}

	public Float getCarga() {
		return carga;
	}

	public void setCarga(Float carga) {
		this.carga = carga;
	}

	public Short getCerradas() {
		return cerradas;
	}

	public void setCerradas(Short cerradas) {
		this.cerradas = cerradas;
	}

	public Short getCiclo() {
		return ciclo;
	}

	public void setCiclo(Short ciclo) {
		this.ciclo = ciclo;
	}

	public Comunidad getComunidad() {
		return comunidad;
	}

	public void setComunidad(Comunidad comunidad) {
		this.comunidad = comunidad;
	}

	public Short getConstruccion() {
		return construccion;
	}

	public void setConstruccion(Short construccion) {
		this.construccion = construccion;
	}

	public Short getControl() {
		return control;
	}

	public void setControl(Short control) {
		this.control = control;
	}

	public Short getDesalojoAdecuado() {
		return desalojoAdecuado;
	}

	public void setDesalojoAdecuado(Short desalojoAdecuado) {
		this.desalojoAdecuado = desalojoAdecuado;
	}

	public Short getDesalojoInadecuado() {
		return desalojoInadecuado;
	}

	public void setDesalojoInadecuado(Short desalojoInadecuado) {
		this.desalojoInadecuado = desalojoInadecuado;
	}

	public Short getEnfermos() {
		return enfermos;
	}

	public void setEnfermos(Short enfermos) {
		this.enfermos = enfermos;
	}

	public EntidadAdtva getSilais() {
		return silais;
	}

	public void setSilais(EntidadAdtva silais) {
		this.silais = silais;
	}

	public EquiposMalaria getEquipo() {
		return equipo;
	}

	public void setEquipo(EquiposMalaria equipo) {
		this.equipo = equipo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Float getFormulacion() {
		return formulacion;
	}

	public void setFormulacion(Float formulacion) {
		this.formulacion = formulacion;
	}

	public Short getHabNoRociadas() {
		return habNoRociadas;
	}

	public void setHabNoRociadas(Short habNoRociadas) {
		this.habNoRociadas = habNoRociadas;
	}

	public Short getHabRociadas() {
		return habRociadas;
	}

	public void setHabRociadas(Short habRociadas) {
		this.habRociadas = habRociadas;
	}

	public InsecticidasMalaria getInsecticida() {
		return insecticida;
	}

	public void setInsecticida(InsecticidasMalaria insecticida) {
		this.insecticida = insecticida;
	}

	public DivisionPolitica getMunicipio() {
		return municipio;
	}

	public void setMunicipio(DivisionPolitica municipio) {
		this.municipio = municipio;
	}

	public Short getOtros() {
		return otros;
	}

	public void setOtros(Short otros) {
		this.otros = otros;
	}

	public Short getRenuentes() {
		return renuentes;
	}

	public void setRenuentes(Short renuentes) {
		this.renuentes = renuentes;
	}

	public String getRociador() {
		return rociador;
	}

	public void setRociador(String rociador) {
		this.rociador = rociador;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public Float getTotalCargas() {
		return totalCargas;
	}

	public void setTotalCargas(Float totalCargas) {
		this.totalCargas = totalCargas;
	}

	public Short getViviendasProgramadas() {
		return viviendasProgramadas;
	}

	public void setViviendasProgramadas(Short viviendasProgramadas) {
		this.viviendasProgramadas = viviendasProgramadas;
	}

	public Short getViviendasRociadas() {
		return viviendasRociadas;
	}

	public void setViviendasRociadas(Short viviendasRociadas) {
		this.viviendasRociadas = viviendasRociadas;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public Short getHabitantesProtegidos() {
		return habitantesProtegidos;
	}

	public void setHabitantesProtegidos(Short habitantesProtegidos) {
		this.habitantesProtegidos = habitantesProtegidos;
	}

	public Float getTotalUtilizadas() {
		return totalUtilizadas;
	}

	public void setTotalUtilizadas(Float totalUtilizadas) {
		this.totalUtilizadas = totalUtilizadas;
	}

    
	
}