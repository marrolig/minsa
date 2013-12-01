// -----------------------------------------------
// ViviendaEncuesta.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.general.CondicionOcupacion;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla VIVIENDAS_ENCUESTAS 
 * en el esquema GENERAL.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 24/05/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="VIVIENDAS_ENCUESTAS",schema="SIVE",
			uniqueConstraints=@UniqueConstraint(columnNames={"VIVIENDA","FECHA_ENCUESTA"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="encuestasPorVivienda",
			query="select ve from ViviendaEncuesta ve " +
				    "where ve.vivienda.codigo=:pVivienda " +
				    "order by ve.fechaEncuesta DESC")
})
public class ViviendaEncuesta extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Comparator<ViviendaEncuesta> ORDEN_FECHA =
		new Comparator<ViviendaEncuesta>() {
			public int compare(ViviendaEncuesta ve1, ViviendaEncuesta ve2) {
				return ve1.fechaEncuesta.compareTo(ve2.fechaEncuesta);
			}
	};

	@Id
	@SequenceGenerator(name="VIVENCUESTAS_VIVIENDA_ENCUESTA_ID_GENERATOR", sequenceName="SIVE.VIVENCUESTAS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VIVENCUESTAS_VIVIENDA_ENCUESTA_ID_GENERATOR")
	@Column(name="VIVIENDA_ENCUESTA_ID",updatable=false,nullable=false)
	private long viviendaEncuestaId;

    @ManyToOne(targetEntity=Vivienda.class)
	@JoinColumn(name="VIVIENDA",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private Vivienda vivienda;

    @NotNull(message="El sistema al cual se asocia la encuesta de la vivieda, es requerido.")
	private String sistema;

    @ManyToOne(targetEntity=CondicionOcupacion.class,fetch=FetchType.LAZY)
	@JoinColumn(name="CONDICION_OCUPACION", nullable=false, referencedColumnName="CODIGO")
	private CondicionOcupacion condicionOcupacion;
    
    @Size(min=1,max=400,message="Solo se admiten 400 caracteres para las observaciones")
    @Column(nullable=true,length=400)
    private String observaciones;

    @Size(min=1,max=100,message="Solo se admiten 100 caracteres para el nombre del jefe de familia")
    @Column(name="JEFE_FAMILIA",nullable=true,length=100)
    private String jefeFamilia;

	@DecimalMin(value="0",message="El número de habitantes debe ser igual o mayor que cero")
	@DecimalMax(value="999",message="El número de habitantes no puede ser mayor que 999")
	@NotNull(message="El número de habitantes no puede ser nulo")
	@Column(nullable=false,precision=3,scale=0)
	private BigDecimal habitantes;

	@DecimalMin(value="1",message="El número de hogares debe ser mayor que cero")
	@DecimalMax(value="9",message="El número de hogares no puede ser mayor que 9")
	@Column(nullable=true,precision=1,scale=0)
	private BigDecimal hogares;
	
	@Temporal(TemporalType.DATE)
	@NotNull(message="Debe declararse una fecha de encuesta para la vivienda")
	@Column(name="FECHA_ENCUESTA", nullable=false,updatable=false)
	private Date fechaEncuesta;
	
	@SuppressWarnings("unused")
	@Transient
	private boolean protegido;

    public ViviendaEncuesta() {
    }

	public void setViviendaEncuestaId(long viviendaEncuestaId) {
		this.viviendaEncuestaId = viviendaEncuestaId;
	}

	public long getViviendaEncuestaId() {
		return viviendaEncuestaId;
	}

	public void setVivienda(Vivienda vivienda) {
		this.vivienda = vivienda;
	}

	public Vivienda getVivienda() {
		return vivienda;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getSistema() {
		return sistema;
	}

	public void setCondicionOcupacion(CondicionOcupacion condicionOcupacion) {
		this.condicionOcupacion = condicionOcupacion;
	}

	public CondicionOcupacion getCondicionOcupacion() {
		return condicionOcupacion;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setJefeFamilia(String jefeFamilia) {
		this.jefeFamilia = jefeFamilia;
	}

	public String getJefeFamilia() {
		return jefeFamilia;
	}

	public void setHabitantes(BigDecimal habitantes) {
		this.habitantes = habitantes;
	}

	public BigDecimal getHabitantes() {
		return habitantes;
	}

	public void setHogares(BigDecimal hogares) {
		this.hogares = hogares;
	}

	public BigDecimal getHogares() {
		return hogares;
	}

	public void setFechaEncuesta(Date fechaEncuesta) {
		this.fechaEncuesta = fechaEncuesta;
	}

	public Date getFechaEncuesta() {
		return fechaEncuesta;
	}
	
	public boolean isProtegido() {
		return !this.sistema.equals(Utilidades.CODIGO_SISTEMA);
	}

}