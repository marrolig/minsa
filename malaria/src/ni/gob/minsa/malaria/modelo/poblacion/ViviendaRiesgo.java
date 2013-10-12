// -----------------------------------------------
// ViviendaRiesgo.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.general.NivelRiesgo;
import ni.gob.minsa.malaria.modelo.vigilancia.FactorRiesgo;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla VIVIENDAS_RIESGOS 
 * en el esquema SIVE.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 24/05/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="VIVIENDAS_RIESGOS",schema="SIVE",
			uniqueConstraints=@UniqueConstraint(columnNames={"VIVIENDA","FACTOR_RIESGO"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="riesgosPorVivienda",
			query="select vr from ViviendaRiesgo vr " +
				    "where vr.vivienda.codigo=:pVivienda " +
				    "order by vr.fechaToma DESC")
})
public class ViviendaRiesgo extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="VIVRIESGOS_VIVRIESGOID_GENERATOR", sequenceName="SIVE.VIVRIESGOS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VIVRIESGOS_VIVRIESGOID_GENERATOR")
	@Column(name="VIVIENDA_RIESGO_ID",updatable=false,nullable=false)
	private long viviendaRiesgoId;

    @ManyToOne(targetEntity=Vivienda.class)
	@JoinColumn(name="VIVIENDA",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private Vivienda vivienda;

    @NotNull(message="El sistema al cual se asocia el registro del factor de riesgo, es requerido.")
	private String sistema;

    @ManyToOne(targetEntity=NivelRiesgo.class)
	@JoinColumn(name="NIVEL_RIESGO", nullable=false, referencedColumnName="CODIGO")
	private NivelRiesgo nivelRiesgo;

    @ManyToOne(targetEntity=FactorRiesgo.class)
	@JoinColumn(name="FACTOR_RIESGO", nullable=false, referencedColumnName="CODIGO")
	private FactorRiesgo factorRiesgo;

    @Size(min=1,max=400,message="Solo se admiten 400 caracteres para las observaciones")
    @Column(nullable=true,length=400)
    private String observaciones;

	@Temporal(TemporalType.DATE)
	@NotNull(message="La fecha en la cual se recolectaron los datos es requerida")
	@Column(name="FECHA_TOMA", nullable=false)
	private Date fechaToma;
	
	@SuppressWarnings("unused")
	@Transient
	private boolean protegido;
	
    public ViviendaRiesgo() {
    }

	public void setViviendaRiesgoId(long viviendaRiesgoId) {
		this.viviendaRiesgoId = viviendaRiesgoId;
	}

	public long getViviendaRiesgoId() {
		return viviendaRiesgoId;
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

	public void setNivelRiesgo(NivelRiesgo nivelRiesgo) {
		this.nivelRiesgo = nivelRiesgo;
	}

	public NivelRiesgo getNivelRiesgo() {
		return nivelRiesgo;
	}

	public void setFactorRiesgo(FactorRiesgo factorRiesgo) {
		this.factorRiesgo = factorRiesgo;
	}

	public FactorRiesgo getFactorRiesgo() {
		return factorRiesgo;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setFechaToma(Date fechaToma) {
		this.fechaToma = fechaToma;
	}

	public Date getFechaToma() {
		return fechaToma;
	}

	public boolean isProtegido() {
		return !this.sistema.equals(Utilidades.CODIGO_SISTEMA);
	}

}