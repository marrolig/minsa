// -----------------------------------------------
// EventoSalud.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.vigilancia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.general.ClaseEvento;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla EVENTOS_SALUD 
 * en el esquema SIVE.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 12/06/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="EVENTOS_SALUD",schema="SIVE",
			uniqueConstraints=@UniqueConstraint(columnNames={"CODIGO"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="eventoSalud.listar",
			query="select es from EventoSalud es " +
					"where (:pPasivo is null or es.pasivo=:pPasivo) and " +
					"exists (select evs from es.sistemas evs) " +
					"order by es.nombre"),
	@NamedQuery(
			name="eventoSalud.listarCombo",
			query="select es from EventoSalud es " +
					"where es.codigo=:pEventoSalud or (es.pasivo=0 and " +
					"exists (select evs from es.sistemas evs)) " +
					"order by es.nombre"),
	@NamedQuery(
			name="eventoSalud.encontrarPorCodigo",
			query="select es from EventoSalud es " +
					"where es.codigo=:pCodigo")
})
public class EventoSalud extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="EVENTOSSALUD_EVENTOID_GENERATOR", sequenceName="SIVE.EVENTOS_SALUD_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EVENTOSSALUD_EVENTOID_GENERATOR")
	@Column(name="EVENTO_SALUD_ID",nullable=false,updatable=false)
	private long eventoSaludId;

	@DecimalMin(value="0")
	@DecimalMax(value="1")
	@Digits(integer=1,fraction=0,message="Pasivo únicamente puede tener valor 0 (habilitado) ó 1 (inhabilitado)")
	@Column(nullable=false,precision=1,scale=0)
	private BigDecimal pasivo;

    @Size(min=1,max=10,message="El código del Evento de Salud debe contener de 1 a 10 caracteres")
    @NotNull(message="El código del evento de salud es requerido")
    @Column(nullable=false,length=10,unique=true,updatable=false)
    private String codigo;
    
    @Size(min=1,max=50,message="Solo se admiten 50 caracteres para el nombre del evento de salud")
    @NotNull(message="El nombre del evento de salud es requerido")
    @Column(nullable=false,length=50)
    private String nombre;
    
    @ManyToOne
	@JoinColumn(name="CLASE_EVENTO", nullable=false, referencedColumnName="CODIGO")
	private ClaseEvento claseEvento;
    
	@OneToMany(mappedBy="eventoSalud",
			   targetEntity=ni.gob.minsa.malaria.modelo.vigilancia.EventoSaludSistema.class,
			   fetch=FetchType.LAZY)
	private Set<EventoSaludSistema> sistemas;


    public EventoSalud() {
    }

	public void setEventoSaludId(long eventoSaludId) {
		this.eventoSaludId = eventoSaludId;
	}

	public long getEventoSaludId() {
		return eventoSaludId;
	}

	public void setPasivo(BigDecimal pasivo) {
		this.pasivo = pasivo;
	}

	public BigDecimal getPasivo() {
		return pasivo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setClaseEvento(ClaseEvento claseEvento) {
		this.claseEvento = claseEvento;
	}

	public ClaseEvento getClaseEvento() {
		return claseEvento;
	}

	public void setSistemas(Set<EventoSaludSistema> sistemas) {
		this.sistemas = sistemas;
	}

	public Set<EventoSaludSistema> getSistemas() {
		return sistemas;
	}

}