package ni.gob.minsa.malaria.modelo.vigilancia;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;

import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name="PRMSEPIS_EVENTOS", schema="SIVE", uniqueConstraints=
	@UniqueConstraint(columnNames={"PRM_EPI","EVENTO_SALUD"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="eventosPorParametro",
			query="select trm from ParametroEvento trm where trm.parametroEpidemiologico.parametroEpidemiologicoId=:pParametroEpidemiologicoId order by trm.eventoSalud.nombre")
})
/**
 * Clase para la capa de persistencia para la tabla PRMSEPIS_EVENTOS.
 * 
 */
public class ParametroEvento extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PRMSEPIS_EVENTO_ID_GENERATOR", sequenceName="SIVE.PRMSEPIS_EVTS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRMSEPIS_EVENTO_ID_GENERATOR")
	@Column(name="PRMEPI_EVENTO_ID", insertable=true, updatable=false, unique=true, nullable=false, precision=10)
	private long parametroEventoId; 

	@ManyToOne
	@JoinColumn(name="EVENTO_SALUD", referencedColumnName="CODIGO", nullable=false)
	private EventoSalud eventoSalud;

	@ManyToOne
	@JoinColumn(name="PRM_EPI", referencedColumnName="CODIGO", nullable=false)
	private ParametroEpidemiologico parametroEpidemiologico;

    public ParametroEvento() {
    }
	
	public void setEventoSalud(EventoSalud eventoSalud) {
		this.eventoSalud = eventoSalud;
	}

	public EventoSalud getEventoSalud() {
		return eventoSalud;
	}

	public void setParametroEventoId(long parametroEventoId) {
		this.parametroEventoId = parametroEventoId;
	}

	public long getParametroEventoId() {
		return parametroEventoId;
	}

	public void setParametroEpidemiologico(ParametroEpidemiologico parametroEpidemiologico) {
		this.parametroEpidemiologico = parametroEpidemiologico;
	}

	public ParametroEpidemiologico getParametroEpidemiologico() {
		return parametroEpidemiologico;
	}
	
}