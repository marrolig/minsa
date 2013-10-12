// -----------------------------------------------
// MuestreoDiagnostico.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.vigilancia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;

import org.eclipse.persistence.annotations.Cache;

/**
 * Diagnósticos de las muestras de gota gruesa<p>
 * Clase de persistencia asociada a la tabla MUESTREOS_DIAGNOSTICOS 
 * en el esquema SIVE.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 01/11/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="MUESTREOS_DIAGNOSTICOS",schema="SIVE",
			uniqueConstraints=@UniqueConstraint(columnNames={"MUESTREO_HEMATICO"}))
@Cache(alwaysRefresh=true,disableHits=true)
public class MuestreoDiagnostico extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MSTRS_DIAGS_ID_GENERATOR", sequenceName="SIVE.MSTRS_DIAGS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MSTRS_DIAGS_ID_GENERATOR")
	@Column(name="MUESTREO_DIAGNOSTICO_ID",updatable=false,nullable=false)
	private long muestreoDiagnosticoId;

	@NotNull(message="La asociación con el muestreo hemático es requerida")
	@OneToOne(targetEntity=MuestreoHematico.class)
	@JoinColumn(name="MUESTREO_HEMATICO",nullable=false,updatable=false)
	private MuestreoHematico muestreoHematico;
	
	@NotNull(message="La fecha de recepción de la muestra de gota gruesa es requerida")
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_RECEPCION",nullable=false)
	private Date fechaRecepcion;

	@NotNull(message="La fecha de emisión del diagnóstico es requerida")
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_DIAGNOSTICO",nullable=false)
	private Date fechaDiagnostico;
	
	@NotNull(message="Debe indicarse el resultado: Positivo o Negativo")
	@DecimalMin(value="0",message="Resultado inválido. Debe ser Positivo o Negativo")
	@DecimalMax(value="1",message="Resultado inválido. Debe ser Positivo o Negativo")
	@Digits(integer=1,fraction=0,message="Resultado inválido. Debe ser Positivo o Negativo")
	@Column(nullable=true,precision=1,scale=0)
	private BigDecimal resultado;

	@NotNull(message="Debe indicarse si el resultado es positivo o no para el P.Vivax")
	@DecimalMin(value="0",message="Valor no válido. Debe indicarse si el resultado es positivo o no para el P.Vivax")
	@DecimalMax(value="1",message="Valor no válido. Debe indicarse si el resultado es positivo o no para el P.Vivax")
	@Digits(integer=1,fraction=0,message="Valor no válido. Debe indicarse si el resultado es positivo o no para el P.Vivax")
	@Column(name="POSITIVO_PVIVAX",nullable=true,precision=1,scale=0)
	private BigDecimal positivoPVivax;

	@NotNull(message="Debe indicarse si el resultado es positivo o no para el P.Falciparum")
	@DecimalMin(value="0",message="Valor no válido. Debe indicarse si el resultado es positivo o no para el P.Falciparum")
	@DecimalMax(value="1",message="Valor no válido. Debe indicarse si el resultado es positivo o no para el P.Falciparum")
	@Digits(integer=1,fraction=0,message="Valor no válido. Debe indicarse si el resultado es positivo o no para el P.Falciparum")
	@Column(name="POSITIVO_PFALCIPARUM",nullable=true,precision=1,scale=0)
	private BigDecimal positivoPFalciparum;
	
	@ManyToOne
	@JoinColumn(name="DENSIDAD_PVIVAX",referencedColumnName="CODIGO")
	private DensidadCruces densidadPVivax;
	
	@ManyToOne
	@JoinColumn(name="DENSIDAD_PFALCIPARUM",referencedColumnName="CODIGO")
	private DensidadCruces densidadPFalciparum;
	
	@ManyToOne
	@JoinColumn(name="ESTADIO_PFALCIPARUM",referencedColumnName="CODIGO")
	private EstadioPFalciparum estadioPFalciparum;
	
	@Column(name="ESTADIOS_ASEXUALES",nullable=true,updatable=true)
	private BigDecimal estadiosAsexuales;

	@Column(name="GAMETOCITOS",nullable=true,updatable=true)
	private BigDecimal gametocitos;

	@Column(name="LEUCOCITOS",nullable=true,updatable=true)
	private BigDecimal leucocitos;

	@ManyToOne
	@JoinColumn(name="MOTIVO_FALTA_DIAGNOSTICO",referencedColumnName="CODIGO")
	private MotivoFaltaDiagnostico motivoFaltaDiagnostico;

    @NotNull(message="La entidad administrativa a la cual pertenece la unidad de salud que emitió el diagnóstico es requerida")
    @ManyToOne(targetEntity=EntidadAdtva.class)
	@JoinColumn(name="ENTIDAD_ADTVA_LAB",nullable=false,updatable=true,referencedColumnName="CODIGO")
	private EntidadAdtva entidadAdtvaLaboratorio;

    @NotNull(message="El municipio en el cual se encuentra ubicada la unidad de salud que emitió el diagnóstico es requerido")
    @ManyToOne(targetEntity=DivisionPolitica.class)
	@JoinColumn(name="MUNICIPIO_LAB",nullable=false,updatable=true,referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipioLaboratorio;
    
    @NotNull(message="La unidad de salud responsable del diagnóstico es requerida")
    @ManyToOne(targetEntity=Unidad.class)
	@JoinColumn(name="UNIDAD_LAB",nullable=false,updatable=true,referencedColumnName="CODIGO")
	private Unidad unidadLaboratorio;

    @NotNull(message="El nombre y apellidos del laboratorista son requeridos")
    @Size(max=100,min=1,message="El nombre y apellidos del laboratorista son requeridos")
    @Column(name="LABORATORISTA",nullable=false,updatable=true)
    private String laboratorista;
    
    public MuestreoDiagnostico() {
    }

	/**
	 * @return Identificador único del resultado de la técnica de la gota gruesa asociada a un muestreo hemático
	 */
	public long getMuestreoDiagnosticoId() {
		return muestreoDiagnosticoId;
	}

	/**
	 * Establece el identificador del resultado de la técnica de la gota gruesa
	 * 
	 * @param muestreoDiagnosticoId Identificador del resultado de la técnica de la gota gruesa
	 */
	public void setMuestreoDiagnosticoId(long muestreoDiagnosticoId) {
		this.muestreoDiagnosticoId = muestreoDiagnosticoId;
	}

	/**
	 * Obtiene el objeto {@link MuestreoHematico} al cual corresponde el resultado de la
	 * técnica de la gota gruesa
	 * 
	 * @return {@link MuestreoHematico}
	 */
	public MuestreoHematico getMuestreoHematico() {
		return muestreoHematico;
	}

	/**
	 * Estable el objeto {@link MuestreoHematico} al cual corresponde el resultado de la
	 * técnica de la gota gruesa
	 * 
	 * @param muestreoHematico {@link MuestreoHematico}
	 */
	public void setMuestreoHematico(MuestreoHematico muestreoHematico) {
		this.muestreoHematico = muestreoHematico;
	}

	/**
	 * @return the fechaRecepcion
	 */
	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}

	/**
	 * @param fechaRecepcion the fechaRecepcion to set
	 */
	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

	/**
	 * @return the fechaDiagnostico
	 */
	public Date getFechaDiagnostico() {
		return fechaDiagnostico;
	}

	/**
	 * @param fechaDiagnostico the fechaDiagnostico to set
	 */
	public void setFechaDiagnostico(Date fechaDiagnostico) {
		this.fechaDiagnostico = fechaDiagnostico;
	}

	/**
	 * @return the resultado
	 */
	public BigDecimal getResultado() {
		return resultado;
	}

	/**
	 * @param resultado the resultado to set
	 */
	public void setResultado(BigDecimal resultado) {
		this.resultado = resultado;
	}

	/**
	 * @return the positivoPVivax
	 */
	public BigDecimal getPositivoPVivax() {
		return positivoPVivax;
	}

	/**
	 * @param positivoPVivax the positivoPVivax to set
	 */
	public void setPositivoPVivax(BigDecimal positivoPVivax) {
		this.positivoPVivax = positivoPVivax;
	}

	/**
	 * @return the positivoPFalciparum
	 */
	public BigDecimal getPositivoPFalciparum() {
		return positivoPFalciparum;
	}

	/**
	 * @param positivoPFalciparum the positivoPFalciparum to set
	 */
	public void setPositivoPFalciparum(BigDecimal positivoPFalciparum) {
		this.positivoPFalciparum = positivoPFalciparum;
	}

	/**
	 * @return the densidadPVivax
	 */
	public DensidadCruces getDensidadPVivax() {
		return densidadPVivax;
	}

	/**
	 * @param densidadPVivax the densidadPVivax to set
	 */
	public void setDensidadPVivax(DensidadCruces densidadPVivax) {
		this.densidadPVivax = densidadPVivax;
	}

	/**
	 * @return the densidadPFalciparum
	 */
	public DensidadCruces getDensidadPFalciparum() {
		return densidadPFalciparum;
	}

	/**
	 * @param densidadPFalciparum the densidadPFalciparum to set
	 */
	public void setDensidadPFalciparum(DensidadCruces densidadPFalciparum) {
		this.densidadPFalciparum = densidadPFalciparum;
	}

	/**
	 * @return the estadioPFalciparum
	 */
	public EstadioPFalciparum getEstadioPFalciparum() {
		return estadioPFalciparum;
	}

	/**
	 * @param estadioPFalciparum the estadioPFalciparum to set
	 */
	public void setEstadioPFalciparum(EstadioPFalciparum estadioPFalciparum) {
		this.estadioPFalciparum = estadioPFalciparum;
	}

	/**
	 * @return the estadiosAsexuales
	 */
	public BigDecimal getEstadiosAsexuales() {
		return estadiosAsexuales;
	}

	/**
	 * @param estadiosAsexuales the estadiosAsexuales to set
	 */
	public void setEstadiosAsexuales(BigDecimal estadiosAsexuales) {
		this.estadiosAsexuales = estadiosAsexuales;
	}

	/**
	 * @return the gametocitos
	 */
	public BigDecimal getGametocitos() {
		return gametocitos;
	}

	/**
	 * @param gametocitos the gametocitos to set
	 */
	public void setGametocitos(BigDecimal gametocitos) {
		this.gametocitos = gametocitos;
	}

	/**
	 * @return the leucocitos
	 */
	public BigDecimal getLeucocitos() {
		return leucocitos;
	}

	/**
	 * @param leucocitos the leucocitos to set
	 */
	public void setLeucocitos(BigDecimal leucocitos) {
		this.leucocitos = leucocitos;
	}

	/**
	 * @return the entidadAdtvaLaboratorio
	 */
	public EntidadAdtva getEntidadAdtvaLaboratorio() {
		return entidadAdtvaLaboratorio;
	}

	/**
	 * @param entidadAdtvaLaboratorio the entidadAdtvaLaboratorio to set
	 */
	public void setEntidadAdtvaLaboratorio(EntidadAdtva entidadAdtvaLaboratorio) {
		this.entidadAdtvaLaboratorio = entidadAdtvaLaboratorio;
	}

	/**
	 * @return the municipioLaboratorio
	 */
	public DivisionPolitica getMunicipioLaboratorio() {
		return municipioLaboratorio;
	}

	/**
	 * @param municipioLaboratorio the municipioLaboratorio to set
	 */
	public void setMunicipioLaboratorio(DivisionPolitica municipioLaboratorio) {
		this.municipioLaboratorio = municipioLaboratorio;
	}

	/**
	 * @return the unidadLaboratorio
	 */
	public Unidad getUnidadLaboratorio() {
		return unidadLaboratorio;
	}

	/**
	 * @param unidadLaboratorio the unidadLaboratorio to set
	 */
	public void setUnidadLaboratorio(Unidad unidadLaboratorio) {
		this.unidadLaboratorio = unidadLaboratorio;
	}

	/**
	 * @return the laboratorista
	 */
	public String getLaboratorista() {
		return laboratorista;
	}

	/**
	 * @param laboratorista the laboratorista to set
	 */
	public void setLaboratorista(String laboratorista) {
		this.laboratorista = laboratorista;
	}
	
	/**
	 * Si no existen resultados asociados sobre la muestra de gota gruesa, obtiene el motivo
	 * por el cual no se dispone de un diagnóstico.
	 * @return {@link MotivoFaltaDiagnostico}
	 */
	public MotivoFaltaDiagnostico getMotivoFaltaDiagnostico() {
		return motivoFaltaDiagnostico;
	}

	/**
	 * Establece el motivo por el cual no se dispone de un diagnóstico del caso
	 * sospechoso de malaria
	 * 
	 * @param motivoFaltaDiagnostico {@link MotivoFaltaDiagnostico}
	 */
	public void setMotivoFaltaDiagnostico(
			MotivoFaltaDiagnostico motivoFaltaDiagnostico) {
		this.motivoFaltaDiagnostico = motivoFaltaDiagnostico;
	}

}