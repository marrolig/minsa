package ni.gob.minsa.malaria.modelo.encuesta;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;


/**
 * The persistent class for the CRIADEROS_INTERVENCIONES database table.
 * 
 */
@Entity
@Table(name="CRIADEROS_INTERVENCIONES")
public class CriaderosIntervencion implements Serializable {
	private static final long serialVersionUID = 1L;
	private long criaderoIntervencionId;
	private Long bsphaericus;
	private Long bti;
	private Long consumoBsphaericus;
	private Long consumoBti;
	private Long drenaje;
	private Long eva;
	private Date fechaIntervencion;
	private Long limpieza;
	private String observaciones;
	private Long relleno;
	private Long siembraPeces;
	private CriaderosPesquisa criaderosPesquisa;

    public CriaderosIntervencion() {
    }


	@Id
	@SequenceGenerator(name="CRIADEROS_INTERVENCIONES_CRIADEROINTERVENCIONID_GENERATOR",sequenceName="SIVE.SEQ_CRIADEROINTERVID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CRIADEROS_INTERVENCIONES_CRIADEROINTERVENCIONID_GENERATOR")
	@Column(name="CRIADERO_INTERVENCION_ID")
	public long getCriaderoIntervencionId() {
		return this.criaderoIntervencionId;
	}

	public void setCriaderoIntervencionId(long criaderoIntervencionId) {
		this.criaderoIntervencionId = criaderoIntervencionId;
	}


	public Long getBsphaericus() {
		return this.bsphaericus;
	}

	public void setBsphaericus(Long bsphaericus) {
		this.bsphaericus = bsphaericus;
	}


	public Long getBti() {
		return this.bti;
	}

	public void setBti(Long bti) {
		this.bti = bti;
	}


	@Column(name="CONSUMO_BSPHAERICUS")
	public Long getConsumoBsphaericus() {
		return this.consumoBsphaericus;
	}

	public void setConsumoBsphaericus(Long consumoBsphaericus) {
		this.consumoBsphaericus = consumoBsphaericus;
	}


	@Column(name="CONSUMO_BTI")
	public Long getConsumoBti() {
		return this.consumoBti;
	}

	public void setConsumoBti(Long consumoBti) {
		this.consumoBti = consumoBti;
	}


	public Long getDrenaje() {
		return this.drenaje;
	}

	public void setDrenaje(Long drenaje) {
		this.drenaje = drenaje;
	}


	public Long getEva() {
		return this.eva;
	}

	public void setEva(Long eva) {
		this.eva = eva;
	}


    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_INTERVENCION")
	public Date getFechaIntervencion() {
		return this.fechaIntervencion;
	}

	public void setFechaIntervencion(Date fechaIntervencion) {
		this.fechaIntervencion = fechaIntervencion;
	}


	public Long getLimpieza() {
		return this.limpieza;
	}

	public void setLimpieza(Long limpieza) {
		this.limpieza = limpieza;
	}


	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public Long getRelleno() {
		return this.relleno;
	}

	public void setRelleno(Long relleno) {
		this.relleno = relleno;
	}


	@Column(name="SIEMBRA_PECES")
	public Long getSiembraPeces() {
		return this.siembraPeces;
	}

	public void setSiembraPeces(Long siembraPeces) {
		this.siembraPeces = siembraPeces;
	}


	//bi-directional many-to-one association to CriaderosPesquisa
	@NotNull(message="Criadero pesquisa requerido")
    @ManyToOne
	@JoinColumn(name="PESQUISA",nullable=false,updatable=true)
	public CriaderosPesquisa getCriaderosPesquisa() {
		return this.criaderosPesquisa;
	}

	public void setCriaderosPesquisa(CriaderosPesquisa criaderosPesquisa) {
		this.criaderosPesquisa = criaderosPesquisa;
	}
	
	
}