package ni.gob.minsa.malaria.modelo.supervision;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoNotificacion;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the VISITA_PUESTOS database table.
 * 
 */
@NamedQueries(
		@NamedQuery(name="VisitaPuesto.listarPorEntidadAñoEpiYMunicipio",
				query="select tvp from VisitaPuesto tvp " 
						+ "where tvp.entidadAdtva.entidadAdtvaId=:pEntidadAdtvaId and " 
						+ "tvp.añoEpidemiologico=:pAñoEpi and (tvp.municipio.divisionPoliticaId=:pMunicipioId or 0=:pMunicipioId) " 
						+ "order by tvp.añoEpidemiologico desc")
)
@Entity
@Table(name="VISITA_PUESTOS")
public class VisitaPuesto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="VISITA_PUESTOS_ID_GENERATOR", sequenceName="VISTASPUESTOS_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VISITA_PUESTOS_ID_GENERATOR")
	@Column(name="VISITA_PUESTO_ID", unique=true, nullable=false, precision=10)
	private long visitaPuestoId;

	@Column(name="AÑO_EPIDEMIOLOGICO", nullable=false, precision=4)
	private BigDecimal añoEpidemiologico;

	@Column(name="ATENCION_PACIENTES", nullable=false, precision=1)
	private BigDecimal atencionPacientes;

	@Column(nullable=false, length=5)
	private String clave;

	@Column(nullable=false, precision=1)
	private BigDecimal divulgacion;

	@ManyToOne(targetEntity=EntidadAdtva.class)
	@JoinColumn(name="ENTIDAD_ADTVA",nullable=false,referencedColumnName="CODIGO")
	private EntidadAdtva entidadAdtva;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_ENTRADA", nullable=false)
	private Date fechaEntrada;

	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO",nullable=true)
	private Date fechaRegistro;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_SALIDA", nullable=false)
	private Date fechaSalida;

    @Temporal( TemporalType.DATE)
	@Column(name="HORARIO_FIN", nullable=false)
	private Date horarioFin;

	@Temporal( TemporalType.DATE)
	@Column(name="HORARIO_INICIO", nullable=false)
	private Date horarioInicio;

    @ManyToOne(targetEntity=DivisionPolitica.class)
	@JoinColumn(name="MUNICIPIO",nullable=false,referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipio;

    @Temporal( TemporalType.DATE)
	@Column(name="PROXIMA_VISITA")
	private Date proximaVisita;

    @ManyToOne(targetEntity=PuestoNotificacion.class)
	@JoinColumn(name="PUESTO_NOTIFICACION",nullable=false,referencedColumnName="PUESTO_NOTIFICACION_ID")
	private PuestoNotificacion puestoNotificacion;

	@Column(nullable=false, precision=1)
	private BigDecimal reconocido;

	@Column(name="SEMANA_EPIDEMIOLOGICA", nullable=false, precision=2)
	private BigDecimal semanaEpidemiologica;

	@Column(nullable=false, precision=1)
	private BigDecimal stock;

	@Column(name="TOMA_MUESTRAS", nullable=false, precision=1)
	private BigDecimal tomaMuestras;

	@ManyToOne(targetEntity=Unidad.class)
	@JoinColumn(name="UNIDAD",nullable=false,referencedColumnName="CODIGO")
	private Unidad unidad;

	@Column(name="USUARIO_REGISTRO", nullable=false, length=100)
	private String usuarioRegistro;

	@Column(name="VISIBLE_CARNET", nullable=false, precision=1)
	private BigDecimal visibleCarnet;

    public VisitaPuesto() {
    }

	public long getVisitaPuestoId() {
		return this.visitaPuestoId;
	}

	public void setVisitaPuestoId(long visitaPuestoId) {
		this.visitaPuestoId = visitaPuestoId;
	}

	public BigDecimal getAñoEpidemiologico() {
		return this.añoEpidemiologico;
	}

	public void setAñoEpidemiologico(BigDecimal añoEpidemiologico) {
		this.añoEpidemiologico = añoEpidemiologico;
	}

	public BigDecimal getAtencionPacientes() {
		return this.atencionPacientes;
	}

	public void setAtencionPacientes(BigDecimal atencionPacientes) {
		this.atencionPacientes = atencionPacientes;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public BigDecimal getDivulgacion() {
		return this.divulgacion;
	}

	public void setDivulgacion(BigDecimal divulgacion) {
		this.divulgacion = divulgacion;
	}

	public EntidadAdtva getEntidadAdtva() {
		return this.entidadAdtva;
	}

	public void setEntidadAdtva(EntidadAdtva entidadAdtva) {
		this.entidadAdtva = entidadAdtva;
	}

	public Date getFechaEntrada() {
		return this.fechaEntrada;
	}

	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaSalida() {
		return this.fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public Date getHorarioFin() {
		return this.horarioFin;
	}

	public void setHorarioFin(Date horarioFin) {
		this.horarioFin = horarioFin;
	}

	public Date getHorarioInicio() {
		return this.horarioInicio;
	}

	public void setHorarioInicio(Date horarioInicio) {
		this.horarioInicio = horarioInicio;
	}

	public DivisionPolitica getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(DivisionPolitica municipio) {
		this.municipio = municipio;
	}

	public Date getProximaVisita() {
		return this.proximaVisita;
	}

	public void setProximaVisita(Date proximaVisita) {
		this.proximaVisita = proximaVisita;
	}

	public PuestoNotificacion getPuestoNotificacion() {
		return this.puestoNotificacion;
	}

	public void setPuestoNotificacion(PuestoNotificacion puestoNotificacion) {
		this.puestoNotificacion = puestoNotificacion;
	}

	public BigDecimal getReconocido() {
		return this.reconocido;
	}

	public void setReconocido(BigDecimal reconocido) {
		this.reconocido = reconocido;
	}

	public BigDecimal getSemanaEpidemiologica() {
		return this.semanaEpidemiologica;
	}

	public void setSemanaEpidemiologica(BigDecimal semanaEpidemiologica) {
		this.semanaEpidemiologica = semanaEpidemiologica;
	}

	public BigDecimal getStock() {
		return this.stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public BigDecimal getTomaMuestras() {
		return this.tomaMuestras;
	}

	public void setTomaMuestras(BigDecimal tomaMuestras) {
		this.tomaMuestras = tomaMuestras;
	}

	public Unidad getUnidad() {
		return this.unidad;
	}

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public BigDecimal getVisibleCarnet() {
		return this.visibleCarnet;
	}

	public void setVisibleCarnet(BigDecimal visibleCarnet) {
		this.visibleCarnet = visibleCarnet;
	}

}