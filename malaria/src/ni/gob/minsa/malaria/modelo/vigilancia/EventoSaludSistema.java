package ni.gob.minsa.malaria.modelo.vigilancia;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.portal.Sistema;

import org.eclipse.persistence.annotations.AdditionalCriteria;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name="EVENTOS_SALUD_SISTEMAS", schema="SIVE", uniqueConstraints=
	@UniqueConstraint(columnNames={"EVENTO_SALUD","SISTEMA"}))
@AdditionalCriteria(value="this.sistema.codigo='malaria'")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="sistemasPorEventoSalud",
			query="select trm from EventoSaludSistema trm where trm.eventoSalud.eventoSaludId=:pEventoSaludId order by trm.sistema.nombre")
})
/**
 * Clase para la capa de persistencia para la tabla EVENTOS_SALUD_SISTEMAS.
 * 
 */
public class EventoSaludSistema extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="EVSALUD_SISTEMA_ID_GENERATOR", sequenceName="SIVE.EVSALUD_SIST_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EVSALUD_SISTEMA_ID_GENERATOR")
	@Column(name="EVENTO_SALUD_SISTEMA_ID", insertable=true, updatable=false, unique=true, nullable=false, precision=10)
	private long eventoSaludSistemaId; 

	@ManyToOne
	@JoinColumn(name="EVENTO_SALUD", referencedColumnName="CODIGO", nullable=false)
	private EventoSalud eventoSalud;

	@ManyToOne
	@JoinColumn(name="SISTEMA", referencedColumnName="CODIGO", nullable=false)
	private Sistema sistema;

    public EventoSaludSistema() {
    }
	
	public Sistema getSistema() {
		return this.sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public void setEventoSaludSistemaId(long eventoSaludSistemaId) {
		this.eventoSaludSistemaId = eventoSaludSistemaId;
	}

	public long getEventoSaludSistemaId() {
		return eventoSaludSistemaId;
	}

	public void setEventoSalud(EventoSalud eventoSalud) {
		this.eventoSalud = eventoSalud;
	}

	public EventoSalud getEventoSalud() {
		return eventoSalud;
	}
	
}