package ni.gob.minsa.malaria.modelo.vigilancia;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;

import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name="FACTORES_RIESGOS_EVENTOS", schema="SIVE", uniqueConstraints=
	@UniqueConstraint(columnNames={"FACTOR_RIESGO","EVENTO_SALUD"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="eventosPorFactorRiesgo",
			query="select trm from FactorRiesgoEvento trm " +
					"where trm.factorRiesgo.factorRiesgoId=:pFactorRiesgoId " +
					"order by trm.eventoSalud.nombre")
})
/**
 * Clase para la capa de persistencia para la tabla FACTORES_RIESGOS_EVENTOS.
 * 
 */
public class FactorRiesgoEvento extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FRIESGO_EVENTO_ID_GENERATOR", sequenceName="SIVE.FRIESGOS_EVTS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FRIESGO_EVENTO_ID_GENERATOR")
	@Column(name="FACTOR_RIESGO_EVENTO_ID", insertable=true, updatable=false, unique=true, nullable=false, precision=10)
	private long factorRiesgoEventoId; 

	@ManyToOne
	@JoinColumn(name="EVENTO_SALUD", referencedColumnName="CODIGO", nullable=false)
	private EventoSalud eventoSalud;

	@ManyToOne(targetEntity=FactorRiesgo.class)
	@JoinColumn(name="FACTOR_RIESGO", referencedColumnName="CODIGO", nullable=false)
	private FactorRiesgo factorRiesgo;

    public FactorRiesgoEvento() {
    }
	
	public void setEventoSalud(EventoSalud eventoSalud) {
		this.eventoSalud = eventoSalud;
	}

	public EventoSalud getEventoSalud() {
		return eventoSalud;
	}

	public void setFactorRiesgoEventoId(long factorRiesgoEventoId) {
		this.factorRiesgoEventoId = factorRiesgoEventoId;
	}

	public long getFactorRiesgoEventoId() {
		return factorRiesgoEventoId;
	}

	public void setFactorRiesgo(FactorRiesgo factorRiesgo) {
		this.factorRiesgo = factorRiesgo;
	}

	public FactorRiesgo getFactorRiesgo() {
		return factorRiesgo;
	}
	
}