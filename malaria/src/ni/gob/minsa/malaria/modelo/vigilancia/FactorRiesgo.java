// -----------------------------------------------
// FactorRiesgo.java
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

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla FACTORES_RIESGOS 
 * en el esquema SIVE.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 24/05/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="FACTORES_RIESGOS",schema="SIVE",
			uniqueConstraints=@UniqueConstraint(columnNames={"CODIGO"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="factorRiesgo.listar",
			query="select fr from FactorRiesgo fr " +
					"where (:pPasivo is null or fr.pasivo=:pPasivo) and " +
					"exists (select fre from fr.factoresRiesgosEventos fre " +
					"			where fre.eventoSalud.sistemas IS NOT EMPTY) " +
					"order by fr.nombre"),
	@NamedQuery(
			name="factorRiesgo.listarCombo",
			query="select fr from FactorRiesgo fr " +
					"where fr.codigo=:pFactorRiesgo or " +
					"(fr.pasivo=0 and " +
					"exists (select fre from fr.factoresRiesgosEventos fre " +
					"           where fre.eventoSalud.sistemas IS NOT EMPTY)) " +
					"order by fr.nombre"),
	@NamedQuery(
			name="factorRiesgo.encontrarPorCodigo",
			query="select fr from FactorRiesgo fr " +
					"where fr.codigo=:pCodigo")
})
public class FactorRiesgo extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FACTORES_RIESGOS_FACTORRIESGOID_GENERATOR", sequenceName="SIVE.FACTORES_RIESGOS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FACTORES_RIESGOS_FACTORRIESGOID_GENERATOR")
	@Column(name="FACTOR_RIESGO_ID",nullable=false,updatable=false)
	private long factorRiesgoId;

	@DecimalMin(value="0")
	@DecimalMax(value="1")
	@Digits(integer=1,fraction=0,message="Pasivo únicamente puede tener valor 0 (habilitado) ó 1 (inhabilitado)")
	@Column(nullable=false,precision=1,scale=0)
	private BigDecimal pasivo;

    @Size(min=1,max=10,message="El código del factor de riesgo debe contener de 1 a 10 caracteres")
    @NotNull(message="El código del factor de riesgo es requerido")
    @Column(nullable=false,length=10,unique=true,updatable=false)
    private String codigo;
    
    @Size(min=1,max=100,message="Solo se admiten 100 caracteres para el nombre del factor de riesgo")
    @NotNull(message="El nombre del factor de riesgo es requerido")
    @Column(nullable=false,length=100)
    private String nombre;
    
	@OneToMany(mappedBy="factorRiesgo",
			   targetEntity=ni.gob.minsa.malaria.modelo.vigilancia.FactorRiesgoEvento.class,
			   fetch=FetchType.LAZY)
	private Set<FactorRiesgoEvento> factoresRiesgosEventos;

    public FactorRiesgo() {
    }

	public void setFactorRiesgoId(long factorRiesgoId) {
		this.factorRiesgoId = factorRiesgoId;
	}

	public long getFactorRiesgoId() {
		return factorRiesgoId;
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

	public void setFactoresRiesgosEventos(Set<FactorRiesgoEvento> factoresRiesgosEventos) {
		this.factoresRiesgosEventos = factoresRiesgosEventos;
	}

	public Set<FactorRiesgoEvento> getFactoresRiesgosEventos() {
		return factoresRiesgosEventos;
	}

}