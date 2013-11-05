package ni.gob.minsa.malaria.modelo.rociado;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CHECKLIST_MALARIA database table.
 * 
 */
@Entity
@Table(name="CHECKLIST_MALARIA")
public class ChecklistMalaria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CHECKLISTMALARIAID_GENERATOR", sequenceName="SEQ_CHKLISTMALARIAID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CHECKLISTMALARIAID_GENERATOR")
	@Column(name="CHECKLIST_MALARIA_ID", unique=true, nullable=false, precision=10)
	private long checklistMalariaId;

	@ManyToOne
	@JoinColumn(name="ELEMENTO_LISTA", referencedColumnName="CODIGO", nullable=false)
	private ItemsCheckListMalaria elementoLista;

	@Column(name="FECHA_REGISTRO", nullable=false)
	private Object fechaRegistro;

	@Column(name="USUARIO_REGISTRO", nullable=false, length=50)
	private String usuarioRegistro;

	//bi-directional many-to-one association to RociadosMalaria
    @ManyToOne
	@JoinColumn(name="ROCIADO_MALARIA", nullable=false)
	private RociadosMalaria rociadosMalaria;

    public ChecklistMalaria() {
    }

	public long getChecklistMalariaId() {
		return this.checklistMalariaId;
	}

	public void setChecklistMalariaId(long checklistMalariaId) {
		this.checklistMalariaId = checklistMalariaId;
	}

	public ItemsCheckListMalaria getElementoLista() {
		return this.elementoLista;
	}

	public void setElementoLista(ItemsCheckListMalaria elementoLista) {
		this.elementoLista = elementoLista;
	}

	public Object getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Object fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public RociadosMalaria getRociadosMalaria() {
		return this.rociadosMalaria;
	}

	public void setRociadosMalaria(RociadosMalaria rociadosMalaria) {
		this.rociadosMalaria = rociadosMalaria;
	}
	
}