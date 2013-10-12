// -----------------------------------------------
// ViviendaParametroEpidemiologico.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.vigilancia.ParametroEpidemiologico;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla VIVIENDAS_PRMSEPIS 
 * en el esquema SIVE.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 24/05/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="VIVIENDAS_PRMSEPIS",schema="SIVE",
			uniqueConstraints=@UniqueConstraint(columnNames={"VIVIENDA","PRM_EPI"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="parametrosPorVivienda",
			query="select vp from ViviendaParametroEpidemiologico vp " +
				    "where vp.vivienda.codigo=:pVivienda " +
				    "order by vp.fechaToma DESC")
})
public class ViviendaParametroEpidemiologico extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="VIVS_PRMEPI_ID_GENERATOR", sequenceName="SIVE.VIVS_PRMSEPIS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VIVS_PRMEPI_ID_GENERATOR")
	@Column(name="VIVIENDA_PRMEPI_ID",updatable=false,nullable=false)
	private long viviendaParametroEpidemiologicoId;

    @NotNull(message="El parámetro epidemiológico debe asociarse a una vivienda")
    @ManyToOne(targetEntity=Vivienda.class)
	@JoinColumn(name="VIVIENDA",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private Vivienda vivienda;

    @NotNull(message="El parámetro epidemiológico es requerido")
    @ManyToOne(targetEntity=ParametroEpidemiologico.class)
	@JoinColumn(name="PRM_EPI",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private ParametroEpidemiologico parametro;

    @NotNull(message="El sistema al cual se asocia el registro del parámetro epidemiológico, es requerido.")
	private String sistema;

    @NotNull(message="El valor del parámetro epidemiológico es requerido.")
    @Size(min=1,max=30,message="El valor asociado al parámetro tiene una longitud de caracteres mayor al permitido.")
	@Column(nullable=false,length=30)
	private String valor;

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
	
    public ViviendaParametroEpidemiologico() {
    }

	public void setViviendaParametroEpidemiologicoId(
			long viviendaParametroEpidemiologicoId) {
		this.viviendaParametroEpidemiologicoId = viviendaParametroEpidemiologicoId;
	}

	public long getViviendaParametroEpidemiologicoId() {
		return viviendaParametroEpidemiologicoId;
	}

	public void setVivienda(Vivienda vivienda) {
		this.vivienda = vivienda;
	}

	public Vivienda getVivienda() {
		return vivienda;
	}

	public void setParametro(ParametroEpidemiologico parametro) {
		this.parametro = parametro;
	}

	public ParametroEpidemiologico getParametro() {
		return parametro;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getSistema() {
		return sistema;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
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