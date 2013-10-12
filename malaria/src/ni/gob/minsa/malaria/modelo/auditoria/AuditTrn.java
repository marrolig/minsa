package ni.gob.minsa.malaria.modelo.auditoria;

import java.io.Serializable;
import javax.persistence.*;



/**
 * Clase de persistencia para la tabla AUDIT_TRN
 * 
 */
@Entity
@Table(name="AUDIT_TRN",schema="GENERAL")
public class AuditTrn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="AUDIT_TRN_AUDITTRNID_GENERATOR", sequenceName="GENERAL.AUDIT_TRN_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AUDIT_TRN_AUDITTRNID_GENERATOR")
	@Column(name="AUDIT_TRN_ID")
	private long auditTrnId;

	private String columna;

    @Lob()
	@Column(name="VALOR_ANTERIOR")
	private String valorAnterior;

    @Lob()
	@Column(name="VALOR_NUEVO")
	private String valorNuevo;

	// relación bi-direccional muchos a uno con AuditSsn
    @ManyToOne
	@JoinColumn(name="AUDIT_SSN")
	private AuditSsn auditSsn;

    public AuditTrn() {
    }

	public long getAuditTrnId() {
		return this.auditTrnId;
	}

	public void setAuditTrnId(long auditTrnId) {
		this.auditTrnId = auditTrnId;
	}

	public String getColumna() {
		return this.columna;
	}

	public void setColumna(String columna) {
		this.columna = columna;
	}

	public String getValorAnterior() {
		return this.valorAnterior;
	}

	public void setValorAnterior(String valorAnterior) {
		this.valorAnterior = valorAnterior;
	}

	public String getValorNuevo() {
		return this.valorNuevo;
	}

	public void setValorNuevo(String valorNuevo) {
		this.valorNuevo = valorNuevo;
	}

	public AuditSsn getAuditSsn() {
		return this.auditSsn;
	}

	public void setAuditSsn(AuditSsn auditSsn) {
		this.auditSsn = auditSsn;
	}
	
}