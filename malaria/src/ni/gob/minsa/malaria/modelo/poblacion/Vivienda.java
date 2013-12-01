// -----------------------------------------------
// Vivienda.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.general.TipoVivienda;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla VIVIENDAS 
 * en el esquema GENERAL.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 22/05/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="VIVIENDAS",schema="GENERAL",
			uniqueConstraints=@UniqueConstraint(columnNames={"CODIGO"}))
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="viviendasActivasPorComunidad",
			query="select v from Vivienda v " +
				    "where v.comunidad.codigo=:pComunidad and " +
				    "      v.pasivo=0 " +
				    "order by v.codigo"),
	@NamedQuery(
			name="viviendasTodasPorComunidad",
			query="select v from Vivienda v " +
					"where v.comunidad.codigo=:pComunidad " +
					"order by v.codigo"),
	@NamedQuery(
			name="viviendaPorCodigo",
			query="select v from Vivienda v " +
					"where v.codigo=:pVivienda")
})
public class Vivienda extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="VIVIENDAS_VIVIENDAID_GENERATOR", sequenceName="GENERAL.VIVIENDAS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VIVIENDAS_VIVIENDAID_GENERATOR")
	@Column(name="VIVIENDA_ID",updatable=false,nullable=false)
	private long viviendaId;

	@DecimalMin(value="0")
	@DecimalMax(value="1")
	@Digits(integer=1,fraction=0,message="Pasivo únicamente puede tener valor 0 (habilitado) ó 1 (inhabilitado)")
	@Column(nullable=false,precision=1,scale=0)
	private BigDecimal pasivo;

    @ManyToOne(targetEntity=Comunidad.class,fetch=FetchType.LAZY)
	@JoinColumn(name="COMUNIDAD",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private Comunidad comunidad;

    @Size(min=14,max=14,message="El código de la vivienda no es válido, debe contener 14 caracteres")
    @Column(nullable=false,length=14,unique=true,updatable=false)
    private String codigo;
    
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TIPO_VIVIENDA", nullable=false, referencedColumnName="CODIGO")
	private TipoVivienda tipoVivienda;
    
    @Size(min=1,max=200,message="Solo se admiten 200 caracteres para las observaciones")
    @Column(nullable=true,length=200)
    private String observaciones;

    @Size(min=1,max=200,message="Solo se admiten 200 caracteres para la dirección de la vivienda")
    @Column(nullable=true,length=200)
    private String direccion;

	@Digits(integer=4,fraction=6)
	@Column(nullable=true,precision=10,scale=6)
	private BigDecimal longitud;

	@Digits(integer=4,fraction=6)
	@Column(nullable=true,precision=10,scale=6)
	private BigDecimal latitud;
	
	@OneToMany(mappedBy="vivienda",targetEntity=ni.gob.minsa.malaria.modelo.poblacion.ViviendaRiesgo.class,fetch=FetchType.LAZY)
	private List<ViviendaRiesgo> riesgos;

	@OneToMany(mappedBy="vivienda",targetEntity=ni.gob.minsa.malaria.modelo.poblacion.ViviendaEncuesta.class,fetch=FetchType.LAZY)
	private List<ViviendaEncuesta> encuestas;

	@OneToMany(mappedBy="vivienda",targetEntity=ni.gob.minsa.malaria.modelo.poblacion.ViviendaParametroEpidemiologico.class,fetch=FetchType.LAZY)
	private List<ViviendaParametroEpidemiologico> parametros;

	@OneToOne(mappedBy="vivienda",targetEntity=ni.gob.minsa.malaria.modelo.poblacion.ViviendaManzana.class,fetch=FetchType.LAZY,optional=true,cascade=CascadeType.PERSIST)
	private ViviendaManzana viviendaManzana;
	
    public Vivienda() {
    }

	public void setViviendaId(long viviendaId) {
		this.viviendaId = viviendaId;
	}

	public long getViviendaId() {
		return viviendaId;
	}

	public void setPasivo(BigDecimal pasivo) {
		this.pasivo = pasivo;
	}

	public BigDecimal getPasivo() {
		return pasivo;
	}

	public void setComunidad(Comunidad comunidad) {
		this.comunidad = comunidad;
	}

	public Comunidad getComunidad() {
		return comunidad;
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

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setTipoVivienda(TipoVivienda tipoVivienda) {
		this.tipoVivienda = tipoVivienda;
	}

	public TipoVivienda getTipoVivienda() {
		return tipoVivienda;
	}

	public void setRiesgos(List<ViviendaRiesgo> riesgos) {
		this.riesgos = riesgos;
	}

	public List<ViviendaRiesgo> getRiesgos() {
		if (riesgos==null) riesgos=new ArrayList<ViviendaRiesgo>();
		return riesgos;
	}

	public void setEncuestas(List<ViviendaEncuesta> encuestas) {
		this.encuestas = encuestas;
	}

	public List<ViviendaEncuesta> getEncuestas() {
		if (encuestas==null) encuestas=new ArrayList<ViviendaEncuesta>();
		return encuestas;
	}

	public void setViviendaManzana(ViviendaManzana viviendaManzana) {
		this.viviendaManzana = viviendaManzana;
	}

	public ViviendaManzana getViviendaManzana() {
		return viviendaManzana;
	}

	public void setParametros(List<ViviendaParametroEpidemiologico> parametros) {
		this.parametros = parametros;
	}

	public List<ViviendaParametroEpidemiologico> getParametros() {
		return parametros;
	}

}