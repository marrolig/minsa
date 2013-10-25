package ni.gob.minsa.malaria.modelo.encuesta;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
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
	private BigDecimal bsphaericus;
	private BigDecimal bti;
	private BigDecimal consumoBsphaericus;
	private BigDecimal consumoBti;
	private BigDecimal drenaje;
	private BigDecimal eva;
	private Date fechaIntervencion;
	private BigDecimal limpieza;
	private String observaciones;
	private BigDecimal relleno;
	private BigDecimal siembraPeces;
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


	public BigDecimal getBsphaericus() {
		return this.bsphaericus;
	}

	public void setBsphaericus(BigDecimal bsphaericus) {
		this.bsphaericus = bsphaericus;
	}


	public BigDecimal getBti() {
		return this.bti;
	}

	public void setBti(BigDecimal bti) {
		this.bti = bti;
	}


	@Column(name="CONSUMO_BSPHAERICUS")
	public BigDecimal getConsumoBsphaericus() {
		return this.consumoBsphaericus;
	}

	public void setConsumoBsphaericus(BigDecimal consumoBsphaericus) {
		this.consumoBsphaericus = consumoBsphaericus;
	}


	@Column(name="CONSUMO_BTI")
	public BigDecimal getConsumoBti() {
		return this.consumoBti;
	}

	public void setConsumoBti(BigDecimal consumoBti) {
		this.consumoBti = consumoBti;
	}


	public BigDecimal getDrenaje() {
		return this.drenaje;
	}

	public void setDrenaje(BigDecimal drenaje) {
		this.drenaje = drenaje;
	}


	public BigDecimal getEva() {
		return this.eva;
	}

	public void setEva(BigDecimal eva) {
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


	public BigDecimal getLimpieza() {
		return this.limpieza;
	}

	public void setLimpieza(BigDecimal limpieza) {
		this.limpieza = limpieza;
	}


	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public BigDecimal getRelleno() {
		return this.relleno;
	}

	public void setRelleno(BigDecimal relleno) {
		this.relleno = relleno;
	}


	@Column(name="SIEMBRA_PECES")
	public BigDecimal getSiembraPeces() {
		return this.siembraPeces;
	}

	public void setSiembraPeces(BigDecimal siembraPeces) {
		this.siembraPeces = siembraPeces;
	}


	//bi-directional many-to-one association to CriaderosPesquisa
	@NotNull(message="Criadero pesquisa requerido")
    @ManyToOne(targetEntity=CriaderosPesquisa.class,fetch=FetchType.LAZY)
	@JoinColumn(name="PESQUISA",nullable=false,updatable=true)
	public CriaderosPesquisa getCriaderosPesquisa() {
		return this.criaderosPesquisa;
	}

	public void setCriaderosPesquisa(CriaderosPesquisa criaderosPesquisa) {
		this.criaderosPesquisa = criaderosPesquisa;
	}
	
	
}