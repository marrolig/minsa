package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the INVESTIGACIONES_MEDICAMENTOS database table.
 * 
 */
@NamedQueries({
	@NamedQuery(name="InvestigacionMedicamento.listar",
			query="select tm from InvestigacionMedicamento tm " +
				  "where tm.investigacionMalaria.investigacionMalariaId = :pInvestigacionMalaria")
})
@Entity
@Table(name="INVESTIGACIONES_MEDICAMENTOS")
public class InvestigacionMedicamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INV_MEDICAMENTOS_ID_GENERATOR", sequenceName="SIVE.INV_MEDICAMENTOS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INV_MEDICAMENTOS_ID_GENERATOR")
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
	

    @Override
    public boolean equals(final Object pObject) {
    	
    	if(pObject==null){
    		return  false;
        }
        if (!(pObject instanceof InvestigacionMedicamento)){
        	return false;
        }
        InvestigacionMedicamento oInvMedicamento = (InvestigacionMedicamento) pObject;
        if((oInvMedicamento.getMedicamento()==null || oInvMedicamento.getInvestigacionesMalaria()==null) ||
        		(this.getMedicamento()==null || this.getInvestigacionesMalaria()==null)){
        	return false;
        }
        if ((this.getMedicamento().getCodigo().equals(oInvMedicamento.getMedicamento().getCodigo())) &&
        		(this.getInvestigacionesMalaria().getInvestigacionMalariaId() == oInvMedicamento.getInvestigacionesMalaria().getInvestigacionMalariaId())){
        	return true;
         }
        
        return false;
    }
}