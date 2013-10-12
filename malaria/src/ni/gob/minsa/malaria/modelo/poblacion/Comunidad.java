package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import ni.gob.minsa.malaria.servicios.auditoria.GestorAuditoria;

import org.eclipse.persistence.annotations.Cache;

import java.util.Date;
import java.util.Set;


/**
 * Clase de persistencia para la tabla COMUNIDADES de la base de datos
 * <p>
 * @author Marlon Arróliga 
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.1, &nbsp; 13/03/2012
 * @since jdk1.6.0_21
 */
@Entity
@EntityListeners(GestorAuditoria.class)
@Table(name="COMUNIDADES",schema="GENERAL")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="comunidadesPorMunicipio",
			query="select tc from Comunidad tc " +
					"where tc.sector.municipio.divisionPoliticaId=:pMunicipioId " +
					"order by tc.nombre"),
	@NamedQuery(
			name="comunidadesPorSector",
			query="select tc from Comunidad tc " +
					"where tc.sector.sectorId=:pSectorId " +
					"order by tc.nombre"),
	@NamedQuery(
			name="comunidadesPorMunicipioActivos",
			query="select tc from Comunidad tc " +
					"where tc.sector.municipio.divisionPoliticaId=:pMunicipioId and " +
						  "tc.pasivo='0' " +
					"order by tc.nombre"),
	@NamedQuery(
			name="comunidadesPorSectorActivos",
			query="select tc from Comunidad tc " +
					"where tc.sector.sectorId=:pSectorId and " +
						  "tc.pasivo='0' " +
					"order by tc.nombre"),
	@NamedQuery(
			name="comunidadesPorNombre",
			query="select tc from Comunidad tc " +
					"where tc.sector.municipio.divisionPoliticaId=:pMunicipioId and " +
					"      (:pSectorId=0 or tc.sector.sectorId=:pSectorId) and " +
					"      UPPER(tc.nombre) LIKE :pNombre " +
					"order by tc.nombre"),
	@NamedQuery(
			name="comunidadesActivasPorUnidadYNombre",
			query="select tc from Comunidad tc " +
					"where tc.sector.unidad.unidadId=:pUnidadId and " +
					"      tc.pasivo='0' and (:pTipoArea is null or tc.tipoArea=:pTipoArea) and " +
					"      UPPER(tc.nombre) LIKE :pNombre " +
					"order by tc.nombre"),
	@NamedQuery(
			name="comunidadesActivasPorUnidad",
			query="select tc from Comunidad tc " +
					"where tc.sector.unidad.unidadId=:pUnidadId and " +
					"      tc.pasivo='0' and (:pTipoArea is null or tc.tipoArea=:pTipoArea) " +
					"order by tc.nombre"),
	@NamedQuery(
			name="comunidadPorCodigo",
			query="select tc from Comunidad tc " +
					"where tc.codigo=:pCodigo")
})
public class Comunidad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="COMUNIDADES_COMUNIDADID_GENERATOR", sequenceName="GENERAL.COMUNIDADES_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMUNIDADES_COMUNIDADID_GENERATOR")
	@Column(name="COMUNIDAD_ID",updatable=false,unique=true, nullable=false, precision=10)
	private long comunidadId;

	
	private String caracteristicas;

	@Column(nullable=false, length=9)
	private String codigo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO",updatable=false,nullable=false)
	private Date fechaRegistro;

    @Column(nullable=false, length=100)
	private String nombre;

    @Column(nullable=false, length=1)
	private String pasivo;

    @Column(nullable=true, length=1000)
	private String referencias;
    
	private BigDecimal latitud;

	private BigDecimal longitud;

	// asociación bi-direccional muchos a uno con Sectores
    @ManyToOne
	@JoinColumn(name="SECTOR",referencedColumnName="CODIGO")
	private Sector sector;

	@Column(name="TIPO_AREA", length=1,nullable=false)
	private String tipoArea;

	@Column(name="USUARIO_REGISTRO",updatable=false, length=100)
	private String usuarioRegistro;
	
	@OneToMany(mappedBy="comunidad",targetEntity=ni.gob.minsa.malaria.modelo.poblacion.PoblacionComunidad.class,fetch=FetchType.LAZY)
	private Set<PoblacionComunidad> poblaciones;

    public Comunidad() {
    }

	public long getComunidadId() {
		return this.comunidadId;
	}

	public void setComunidadId(long comunidadId) {
		this.comunidadId = comunidadId;
	}

	public String getCaracteristicas() {
		return this.caracteristicas;
	}

	public void setCaracteristicas(String caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
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

	public String getReferencias() {
		return this.referencias;
	}

	public void setReferencias(String referencias) {
		this.referencias = referencias;
	}

	public Sector getSector() {
		return this.sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public String getTipoArea() {
		return this.tipoArea;
	}

	public void setTipoArea(String tipoArea) {
		this.tipoArea = tipoArea;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setPoblaciones(Set<PoblacionComunidad> poblaciones) {
		this.poblaciones = poblaciones;
	}

	public Set<PoblacionComunidad> getPoblaciones() {
		return poblaciones;
	}

}