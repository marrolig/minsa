package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the INVESTIGACIONES_MEDICAMENTOS database table.
 * 
 */
@Entity
@Table(name="INVESTIGACIONES_MEDICAMENTOS")
public class InvestigacionMedicamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INVESTIGACIONES_MEDICAMENTOS_INVESTIGACIONMEDICAMENTOID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INVESTIGACIONES_MEDICAMENTOS_INVESTIGACIONMEDICAMENTOID_GENERATOR")
	@Column(name="INVESTIGACION_MEDICAMENTO_ID", unique=true, nullable=false, precision=10)
	private long investigacionMedicamentoId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO", updatable=false, nullable=false)
	private Date fechaRegistro;

	@ManyToOne
	@JoinColumn(name="MEDICAMENTO", referencedColumnName="CODIGO",nullable=false)
	private Medicamento medicamento;

	@Column(name="USUARIO_REGISTRO", nullable=false, length=100)
	private String usuarioRegistro;

	//bi-directional many-to-one association to InvestigacionesMalaria
	@ManyToOne
	@JoinColumn(name="INVESTIGACION_MALARIA", nullable=false)
	private InvestigacionMalaria investigacionMalaria;

    public InvestigacionMedicamento() {
    }

	public long getInvestigacionMedicamentoId() {
		return this.investigacionMedicamentoId;
	}

	public void setInvestigacionMedicamentoId(long investigacionMedicamentoId) {
		this.investigacionMedicamentoId = investigacionMedicamentoId;
	}
	
	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Medicamento getMedicamento() {
		return this.medicamento;
	}

	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public InvestigacionMalaria getInvestigacionesMalaria() {
		return this.investigacionMalaria;
	}

	public void setInvestigacionesMalaria(InvestigacionMalaria investigacionMalaria) {
		this.investigacionMalaria = investigacionMalaria;
	}
	
}