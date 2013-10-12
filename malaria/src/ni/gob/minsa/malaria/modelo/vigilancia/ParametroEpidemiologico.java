// -----------------------------------------------
// ParametroEpidemiologico.java
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
 * Clase de persistencia asociada a la tabla PRMS_EPIS 
 * en el esquema SIVE.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 24/05/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="PRMS_EPIS",schema="SIVE",
			uniqueConstraints=@UniqueConstraint(columnNames={"CODIGO"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="parametroEpidemiologico.listar",
			query="select pe from ParametroEpidemiologico pe " +
					"where (:pPasivo is null or pe.pasivo=:pPasivo) and " +
					"exists (select pev from pe.parametrosEventos pev " +
					"			where pev.eventoSalud.sistemas IS NOT EMPTY) " +
					"order by pe.concepto"),
	@NamedQuery(
			name="parametroEpidemiologico.listarCombo",
			query="select pe from ParametroEpidemiologico pe " +
					"where pe.codigo=:pParametroEpidemiologico or " +
					"(pe.pasivo=0 and " +
					"exists (select pev from pe.parametrosEventos pev " +
					"           where pev.eventoSalud.sistemas IS NOT EMPTY)) " +
					"order by pe.concepto"),
	@NamedQuery(
			name="parametroEpidemiologico.encontrarPorCodigo",
			query="select pe from ParametroEpidemiologico pe " +
					"where pe.codigo=:pCodigo")
})
public class ParametroEpidemiologico extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PRMS_EPIS_PRM_EPI_ID_GENERATOR", sequenceName="SIVE.PRMS_EPIS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRMS_EPIS_PRM_EPI_ID_GENERATOR")
	@Column(name="PRM_EPI_ID",nullable=false,updatable=false)
	private long parametroEpidemiologicoId;

	@DecimalMin(value="0")
	@DecimalMax(value="1")
	@Digits(integer=1,fraction=0,message="Pasivo únicamente puede tener valor 0 (habilitado) ó 1 (inhabilitado)")
	@Column(nullable=false,precision=1,scale=0)
	private BigDecimal pasivo;

    @Size(min=1,max=10,message="El código del parámetro epidemiológico debe contener de 1 a 10 caracteres")
    @NotNull(message="El código del parámetro epidemiológico es requerido")
    @Column(nullable=false,length=10,unique=true,updatable=false)
    private String codigo;
    
    @Size(min=1,max=100,message="Solo se admiten 100 caracteres para el concepto")
    @NotNull(message="El concepto del parámetro epidemiológico es requerido")
    @Column(nullable=false,length=100)
    private String concepto;

    @Size(min=1,max=400,message="Solo se admiten 400 caracteres para la descripción del parámetro epidemiológico")
    @Column(nullable=true,length=400)
    private String descripcion;

    @Size(min=1,max=400,message="Solo se admiten 400 caracteres para la lista de valores")
    @NotNull(message="La lista de valores a ser utilizada por el parámetro epidemiológico es requerida")
    @Column(nullable=false,length=400)
    private String etiqueta;
    
	@OneToMany(mappedBy="parametroEpidemiologico",
			   targetEntity=ni.gob.minsa.malaria.modelo.vigilancia.ParametroEvento.class,
			   fetch=FetchType.LAZY)
	private Set<ParametroEvento> parametrosEventos;

    public ParametroEpidemiologico() {
    }

	public void setParametroEpidemiologicoId(long parametroEpidemiologicoId) {
		this.parametroEpidemiologicoId = parametroEpidemiologicoId;
	}

	public long getParametroEpidemiologicoId() {
		return parametroEpidemiologicoId;
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

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setParametrosEventos(Set<ParametroEvento> parametrosEventos) {
		this.parametrosEventos = parametrosEventos;
	}

	public Set<ParametroEvento> getParametrosEventos() {
		return parametrosEventos;
	}

}