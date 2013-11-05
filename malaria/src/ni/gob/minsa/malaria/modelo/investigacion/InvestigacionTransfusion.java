package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Pais;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the INVESTIGACIONES_TRANSFUSIONES database table.
 * 
 */
@NamedQueries({
	@NamedQuery(name="InvestigacionTransfusion.encontrarPorInvestigacionMalaria",
			query="select ti from InvestigacionTransfusion ti " 
					+ "where ti.investigacionMalaria.investigacionMalariaId=:pInvestigacionMalariaId")
})
@Entity
@Table(name="INVESTIGACIONES_TRANSFUSIONES")
public class InvestigacionTransfusion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INV_TRANSFUSIONES_ID_GENERATOR", sequenceName="SIVE.INV_TRANSFUSIONES_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INV_TRANSFUSIONES_ID_GENERATOR")
	@Column(name="INVESTIGACION_TRANSFUSION_ID", unique=true, nullable=false, precision=10)
	private long investigacionTransfusionId;

    @Temporal( TemporalType.DATE)
	@Column(name="FECHA_TRANSFUSION")
	private Date fechaTransfusion;

    @OneToOne
	@JoinColumn(name="INVESTIGACION_MALARIA", referencedColumnName="INVESTIGACION_MALARIA_ID",nullable=false)
	private InvestigacionMalaria investigacionMalaria;

    @ManyToOne
	@JoinColumn(name="MUNICIPIO", referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipio;

    @ManyToOne
	@JoinColumn(name="PAIS", referencedColumnName="CODIGO_ALFADOS")
	private Pais pais;

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

	public InvestigacionMalaria getInvestigacionMalaria() {
		return this.investigacionMalaria;
	}

	public void setInvestigacionMalaria(InvestigacionMalaria investigacionMalaria) {
		this.investigacionMalaria = investigacionMalaria;
	}

	public DivisionPolitica getMunicipio() {
		return municipio;
	}

	public void setMunicipio(DivisionPolitica municipio) {
		this.municipio = municipio;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
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