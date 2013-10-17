package ni.gob.minsa.malaria.modelo.encuesta;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the CRIADEROS_PESQUISAS database table.
 * 
 */
@Entity
@Table(name="CRIADEROS_PESQUISAS")
public class CriaderosPesquisa implements Serializable {
	private static final long serialVersionUID = 1L;
	private long criaderoPesquisaId;
	private BigDecimal añoEpidemiologico;
	private Criadero criadero;
	private BigDecimal cuchColectadas;
	private BigDecimal cuchPositivas;
	private Date fechaInspeccion;
	private Date fechaNotificacion;
	private Date fechaRegistro;
	private Date fechaRevision;
	private String inspector;
	private BigDecimal larvasAdultas;
	private BigDecimal larvasJovenes;
	private String observacion;
	private BigDecimal puntosMuestreados;
	private BigDecimal pupas;
	private BigDecimal revisado;
	private BigDecimal semanaEpidemiologica;
	private String usuarioRegistro;
	private String usuarioRevision;

    public CriaderosPesquisa() {
    }


	@Id
	@SequenceGenerator(name="CRIADEROS_PESQUISAS_CRIADEROPESQUISAID_GENERATOR",sequenceName="SIVE.SEQ_CRIADEROPESQUISAID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CRIADEROS_PESQUISAS_CRIADEROPESQUISAID_GENERATOR")
	@Column(name="CRIADERO_PESQUISA_ID")
	public long getCriaderoPesquisaId() {
		return this.criaderoPesquisaId;
	}

	public void setCriaderoPesquisaId(long criaderoPesquisaId) {
		this.criaderoPesquisaId = criaderoPesquisaId;
	}


	@Column(name="AÑO_EPIDEMIOLOGICO")
	public BigDecimal getAñoEpidemiologico() {
		return this.añoEpidemiologico;
	}

	public void setAñoEpidemiologico(BigDecimal añoEpidemiologico) {
		this.añoEpidemiologico = añoEpidemiologico;
	}


	@NotNull(message="La identificacion del criadero es requerido")
	@ManyToOne(targetEntity=Criadero.class,fetch=FetchType.LAZY)
	@JoinColumn(name="CRIADERO",referencedColumnName="CODIGO",nullable=false)
	public Criadero getCriadero() {
		return this.criadero;
	}

	public void setCriadero(Criadero criadero) {
		this.criadero = criadero;
	}


	@Column(name="CUCH_COLECTADAS")
	public BigDecimal getCuchColectadas() {
		return this.cuchColectadas;
	}

	public void setCuchColectadas(BigDecimal cuchColectadas) {
		this.cuchColectadas = cuchColectadas;
	}


	@Column(name="CUCH_POSITIVAS")
	public BigDecimal getCuchPositivas() {
		return this.cuchPositivas;
	}

	public void setCuchPositivas(BigDecimal cuchPositivas) {
		this.cuchPositivas = cuchPositivas;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_INSPECCION")
	public Date getFechaInspeccion() {
		return this.fechaInspeccion;
	}

	public void setFechaInspeccion(Date fechaInspeccion) {
		this.fechaInspeccion = fechaInspeccion;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_NOTIFICACION")
	public Date getFechaNotificacion() {
		return this.fechaNotificacion;
	}

	public void setFechaNotificacion(Date fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}

	@Temporal( TemporalType.DATE)
	@Column(name="FECHA_REGISTRO")
	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@Temporal( TemporalType.DATE)
	@Column(name="FECHA_REVISION")
	public Date getFechaRevision() {
		return this.fechaRevision;
	}

	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}


	public String getInspector() {
		return this.inspector;
	}

	public void setInspector(String inspector) {
		this.inspector = inspector;
	}


	@Column(name="LARVAS_ADULTAS")
	public BigDecimal getLarvasAdultas() {
		return this.larvasAdultas;
	}

	public void setLarvasAdultas(BigDecimal larvasAdultas) {
		this.larvasAdultas = larvasAdultas;
	}


	@Column(name="LARVAS_JOVENES")
	public BigDecimal getLarvasJovenes() {
		return this.larvasJovenes;
	}

	public void setLarvasJovenes(BigDecimal larvasJovenes) {
		this.larvasJovenes = larvasJovenes;
	}


	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	@Column(name="PUNTOS_MUESTREADOS")
	public BigDecimal getPuntosMuestreados() {
		return this.puntosMuestreados;
	}

	public void setPuntosMuestreados(BigDecimal puntosMuestreados) {
		this.puntosMuestreados = puntosMuestreados;
	}


	public BigDecimal getPupas() {
		return this.pupas;
	}

	public void setPupas(BigDecimal pupas) {
		this.pupas = pupas;
	}


	public BigDecimal getRevisado() {
		return this.revisado;
	}

	public void setRevisado(BigDecimal revisado) {
		this.revisado = revisado;
	}


	@Column(name="SEMANA_EPIDEMIOLOGICA")
	public BigDecimal getSemanaEpidemiologica() {
		return this.semanaEpidemiologica;
	}

	public void setSemanaEpidemiologica(BigDecimal semanaEpidemiologica) {
		this.semanaEpidemiologica = semanaEpidemiologica;
	}


	@Column(name="USUARIO_REGISTRO")
	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}


	@Column(name="USUARIO_REVISION")
	public String getUsuarioRevision() {
		return this.usuarioRevision;
	}

	public void setUsuarioRevision(String usuarioRevision) {
		this.usuarioRevision = usuarioRevision;
	}
	
}