package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.estructura.Unidad;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the INVESTIGACIONES_TRANSFUSIONES database table.
 * 
 */
@Entity
@Table(name="INVESTIGACIONES_TRANSFUSIONES")
public class InvestigacionTransfusion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INVESTIGACIONES_TRANSFUSIONES_INVESTIGACIONTRANSFUSIONID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INVESTIGACIONES_TRANSFUSIONES_INVESTIGACIONTRANSFUSIONID_GENERATOR")
	@Column(name="INVESTIGACION_TRANSFUSION_ID", unique=true, nullable=false, precision=10)
	private long investigacionTransfusionId;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_TRANSFUSION")
	private Date fechaTransfusion;

	@Column(name="INVESTIGACION_MALARIA", nullable=false, precision=10)
	private BigDecimal investigacionMalaria;

	@Column(length=4)
	private String municipio;

	@Column(length=2)
	private String pais;

	@Column(nullable=false, precision=1)
	private BigDecimal transfusion;

	@ManyToOne
	@JoinColumn(name="UNIDAD", referencedColumnName="CODIGO")
	private Unidad unidad;

    public InvestigacionTransfusion() {
    }

	public long getInvestigacionTransfusionId() {
		return this.investigacionTransfusionId;
	}

	public void setInvestigacionTransfusionId(long investigacionTransfusionId) {
		this.investigacionTransfusionId = investigacionTransfusionId;
	}

	public Date getFechaTransfusion() {
		return this.fechaTransfusion;
	}

	public void setFechaTransfusion(Date fechaTransfusion) {
		this.fechaTransfusion = fechaTransfusion;
	}

	public BigDecimal getInvestigacionMalaria() {
		return this.investigacionMalaria;
	}

	public void setInvestigacionMalaria(BigDecimal investigacionMalaria) {
		this.investigacionMalaria = investigacionMalaria;
	}

	public String getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getPais() {
		return this.pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public BigDecimal getTransfusion() {
		return this.transfusion;
	}

	public void setTransfusion(BigDecimal transfusion) {
		this.transfusion = transfusion;
	}

	public Unidad getUnidad() {
		return this.unidad;
	}

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

}