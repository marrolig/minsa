// -----------------------------------------------
// DivisionPolitica.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.estructura.Unidad;

import org.eclipse.persistence.annotations.Cache;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;


/**
 * Clase de la capa de persistencia la tabla DIVISIONPOLITICA en el esquema GENERAL.
 * 
 */
@Entity
@Table(name="DIVISIONPOLITICA", schema="GENERAL")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="departamentosActivos",
			query="select tdp from DivisionPolitica tdp " +
					"where tdp.pasivo='0' and " +
					    "tdp.departamento IS NULL or " +
					    "(tdp.divisionPoliticaId=:pDivisionPoliticaId and tdp.departamento IS NULL) " +
					"order by tdp.nombre"),
	@NamedQuery(
		name="municipiosActivos",
		query="select tdp from DivisionPolitica tdp " +
					"where tdp.pasivo='0' and " +
						"((:pDepartamentoId IS NULL and tdp.departamento IS NOT NULL) or " +
						"(:pDepartamentoId IS NOT NULL and tdp.departamento.divisionPoliticaId=:pDepartamentoId) or " +
						"(tdp.divisionPoliticaId=:pMunicipioId and tdp.departamento IS NOT NULL)) " +
					"order by tdp.nombre"),
	@NamedQuery(
		name="departamentosTodos",
		query="select tdp from DivisionPolitica tdp " +
				"where tdp.departamento IS NOT NULL " +
				"order by tdp.nombre"),
	@NamedQuery(
		name="municipiosTodos",
		query="select tdp from DivisionPolitica tdp " +
				"where (:pDepartamentoId IS NULL and tdp.departamento IS NOT NULL) or " +
				    "(:pDepartamentoId IS NOT NULL and tdp.departamento.divisionPoliticaId=:pDepartamentoId) " +
					"order by tdp.nombre"),
	@NamedQuery(
		name="divisionPorCodigoNacional",
		query="select tdp from DivisionPolitica tdp " +
					"where tdp.codigoNacional=:pCodigoNacional"),
	@NamedQuery(
		name="divisionPorCodigoISO",
		query="select tdp from DivisionPolitica tdp " +
					"where tdp.codigoIso=:pCodigoIso")
					
})
public class DivisionPolitica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DIVISIONPOLITICA_DIVISIONPOLITICAID_GENERATOR", sequenceName="GENERAL.DIVISIONPOLITICA_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DIVISIONPOLITICA_DIVISIONPOLITICAID_GENERATOR")
	@Column(name="DIVISIONPOLITICA_ID", updatable=false)
	private long divisionPoliticaId;

	private BigDecimal administracion;

	@Column(name="CODIGO_ISO")
	private String codigoIso;

	@Column(name="CODIGO_NACIONAL")
	private String codigoNacional;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO", updatable=false)
	private Date fechaRegistro;

	private BigDecimal latitud;

	private BigDecimal longitud;

	private String nombre;

	private String pasivo;

	@Column(name="USUARIO_REGISTRO",updatable=false)
	private String usuarioRegistro;

	//bi-directional many-to-one association to Divisionpolitica
	@ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.LAZY)
	@JoinColumn(name="DEPENDENCIA")
	private DivisionPolitica departamento;

	//bi-directional many-to-one association to Divisionpolitica
	@OneToMany(mappedBy="departamento",fetch=FetchType.LAZY)
	private Set<DivisionPolitica> municipios;

	//bi-directional many-to-one association to Unidade
	@OneToMany(mappedBy="municipio",fetch=FetchType.LAZY)
	private Set<Unidad> unidades;

    public DivisionPolitica() {
    }

	public long getDivisionPoliticaId() {
		return this.divisionPoliticaId;
	}

	public void setDivisionpoliticaId(long divisionPoliticaId) {
		this.divisionPoliticaId = divisionPoliticaId;
	}

	public BigDecimal getAdministracion() {
		return this.administracion;
	}

	public void setAdministracion(BigDecimal administracion) {
		this.administracion = administracion;
	}

	public String getCodigoIso() {
		return this.codigoIso;
	}

	public void setCodigoIso(String codigoIso) {
		this.codigoIso = codigoIso;
	}

	public String getCodigoNacional() {
		return this.codigoNacional;
	}

	public void setCodigoNacional(String codigoNacional) {
		this.codigoNacional = codigoNacional;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public BigDecimal getLatitud() {
		return this.latitud;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public BigDecimal getLongitud() {
		return this.longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPasivo() {
		return this.pasivo;
	}

	public void setPasivo(String pasivo) {
		this.pasivo = pasivo;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public DivisionPolitica getDepartamento() {
		return this.departamento;
	}

	public void setDepartamento(DivisionPolitica departamento) {
		this.departamento = departamento;
	}
	
	public Set<DivisionPolitica> getMunicipios() {
		return this.municipios;
	}

	public void setMunicipios(Set<DivisionPolitica> municipios) {
		this.municipios = municipios;
	}
	
	public Set<Unidad> getUnidades() {
		return this.unidades;
	}

	public void setUnidades(Set<Unidad> unidades) {
		this.unidades = unidades;
	}
	
}