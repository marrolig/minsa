package ni.gob.minsa.malaria.modelo.encuesta;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;


/**
 * The persistent class for the CRIADEROS_POS_INSPECCIONES database table.
 * 
 */
@Entity
@Table(name="CRIADEROS_POS_INSPECCIONES")
public class CriaderosPosInspeccion implements Serializable {
	private static final long serialVersionUID = 1L;
	private long criaderoPosInspeccionId;
	private Short cuchColectadas;
	private Short cuchPositivas;
	private Date fechaInspeccion;
	private Date fechaRegistro;
	private Short larvasAdultas;
	private Short larvasJovenes;
	private String observacion;
	private Short puntosMuestreados;
	private Short pupas;
	private String usuarioRegistro;
	private CriaderosIntervencion criaderosIntervencione;

    public CriaderosPosInspeccion() {
    }


	@Id
	@SequenceGenerator(name="CRIADEROS_POS_INSPECCIONES_CRIADEROPOSINSPECCIONID_GENERATOR",sequenceName="SIVE.SEQ_CRIADEROPOSTINSPID", allocationSize=1 )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CRIADEROS_POS_INSPECCIONES_CRIADEROPOSINSPECCIONID_GENERATOR")
	@Column(name="CRIADERO_POS_INSPECCION_ID")
	public long getCriaderoPosInspeccionId() {
		return this.criaderoPosInspeccionId;
	}

	public void setCriaderoPosInspeccionId(long criaderoPosInspeccionId) {
		this.criaderoPosInspeccionId = criaderoPosInspeccionId;
	}


	@Column(name="CUCH_COLECTADAS")
	public Short getCuchColectadas() {
		return this.cuchColectadas;
	}

	public void setCuchColectadas(Short cuchColectadas) {
		this.cuchColectadas = cuchColectadas;
	}


	@Column(name="CUCH_POSITIVAS")
	public Short getCuchPositivas() {
		return this.cuchPositivas;
	}

	public void setCuchPositivas(Short cuchPositivas) {
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO")
	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}


	@Column(name="LARVAS_ADULTAS")
	public Short getLarvasAdultas() {
		return this.larvasAdultas;
	}

	public void setLarvasAdultas(Short larvasAdultas) {
		this.larvasAdultas = larvasAdultas;
	}


	@Column(name="LARVAS_JOVENES")
	public Short getLarvasJovenes() {
		return this.larvasJovenes;
	}

	public void setLarvasJovenes(Short larvasJovenes) {
		this.larvasJovenes = larvasJovenes;
	}


	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	@Column(name="PUNTOS_MUESTREADOS")
	public Short getPuntosMuestreados() {
		return this.puntosMuestreados;
	}

	public void setPuntosMuestreados(Short puntosMuestreados) {
		this.puntosMuestreados = puntosMuestreados;
	}


	public Short getPupas() {
		return this.pupas;
	}

	public void setPupas(Short pupas) {
		this.pupas = pupas;
	}


	@Column(name="USUARIO_REGISTRO")
	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}


	//bi-directional many-to-one association to CriaderosIntervencion
	@NotNull(message="Criadero intervencion requerido")
    @ManyToOne(targetEntity=CriaderosIntervencion.class,fetch=FetchType.LAZY)
	@JoinColumn(name="INTERVENCION",nullable=false,updatable=true)
	public CriaderosIntervencion getCriaderosIntervencione() {
		return this.criaderosIntervencione;
	}

	public void setCriaderosIntervencione(CriaderosIntervencion criaderosIntervencione) {
		this.criaderosIntervencione = criaderosIntervencione;
	}
	
}