// -----------------------------------------------
// Manzana.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion;

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
 * Clase de persistencia asociada a la tabla MANZANAS 
 * en el esquema GENERAL.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 22/05/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="MANZANAS",schema="GENERAL",
			uniqueConstraints=@UniqueConstraint(columnNames={"CODIGO"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="manzanasActivasPorComunidad",
			query="select m from Manzana m " +
				    "where m.comunidad.codigo=:pComunidad and " +
				    "      m.pasivo=0 or " +
				    "      (:pManzana is not null and " +
				    "	   m.codigo=:pManzana) " +
				    "order by m.codigo"),
	@NamedQuery(
			name="manzanasTodasPorComunidad",
			query="select m from Manzana m " +
					"where m.comunidad.codigo=:pComunidad " +
					"order by m.codigo")
})
public class Manzana extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MANZANAS_MANZANAID_GENERATOR", sequenceName="GENERAL.MANZANAS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MANZANAS_MANZANAID_GENERATOR")
	@Column(name="MANZANA_ID",nullable=false,updatable=false)
	private long manzanaId;

	@DecimalMin(value="0")
	@DecimalMax(value="1")
	@Digits(integer=1,fraction=0,message="Pasivo únicamente puede tener valor 0 (habilitado) ó 1 (inhabilitado)")
	@Column(nullable=false,precision=1,scale=0)
	private BigDecimal pasivo;

    @ManyToOne(targetEntity=Comunidad.class)
	@JoinColumn(name="COMUNIDAD",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private Comunidad comunidad;

    @Size(min=12,max=12,message="El código de la manzana no es válido, debe contener 12 caracteres")
    @Column(nullable=false,length=12,unique=true,updatable=false)
    private String codigo;
    
    @Size(min=1,max=300,message="Solo se admiten 300 caracteres para las observaciones")
    @Column(nullable=true,length=300)
    private String observaciones;

	@DecimalMin(value="1",message="La población debe ser mayor que cero")
	@DecimalMax(value="9999",message="La población no puede ser mayor que 9999")
	@Column(nullable=true,precision=4,scale=0)
	private BigDecimal poblacion;

	@DecimalMin(value="1",message="El número de viviendas debe ser mayor que cero")
	@DecimalMax(value="9999",message="El número de viviendas no puede ser mayor que 9999")
	@NotNull(message="El número de viviendas no puede ser nulo")
	@Column(nullable=false,precision=4,scale=0)
	private BigDecimal viviendas;
	
	@OneToMany(mappedBy="manzana",targetEntity=ni.gob.minsa.malaria.modelo.poblacion.ViviendaManzana.class,fetch=FetchType.LAZY)
	private Set<ViviendaManzana> viviendasManzanas;

    public Manzana() {
    }

	public long getManzanaId() {
		return this.manzanaId;
	}

	public void setManzanaId(long manzanaId) {
		this.manzanaId = manzanaId;
	}

	public BigDecimal getPoblacion() {
		return this.poblacion;
	}

	public void setPoblacion(BigDecimal poblacion) {
		this.poblacion = poblacion;
	}

	public BigDecimal getViviendas() {
		return this.viviendas;
	}

	public void setViviendas(BigDecimal viviendas) {
		this.viviendas = viviendas;
	}

	public void setComunidad(Comunidad comunidad) {
		this.comunidad = comunidad;
	}

	public Comunidad getComunidad() {
		return comunidad;
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

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setViviendasManzanas(Set<ViviendaManzana> viviendasManzanas) {
		this.viviendasManzanas = viviendasManzanas;
	}

	public Set<ViviendaManzana> getViviendasManzanas() {
		return viviendasManzanas;
	}

}