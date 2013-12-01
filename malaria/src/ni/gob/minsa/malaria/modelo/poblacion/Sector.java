package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.servicios.auditoria.GestorAuditoria;

import org.eclipse.persistence.annotations.Cache;

import java.util.Comparator;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the SECTORES database table.
 * 
 */
@Entity
@EntityListeners(GestorAuditoria.class)
@Table(name="SECTORES",schema="GENERAL")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="sectoresPorMunicipio",
			query="select ts from Sector ts " +
					"where ts.municipio.divisionPoliticaId=:pMunicipioId " +
					"order by ts.nombre"),
	@NamedQuery(
			name="sectoresPorUnidad",
			query="select ts from Sector ts " +
					"where ts.unidad.unidadId=:pUnidadId " +
					"order by ts.nombre"),
	@NamedQuery(
			name="sectoresPorUnidadActivos",
			query="select ts from Sector ts " +
					"where ts.unidad.unidadId=:pUnidadId and " +
						  "ts.pasivo='0' " +
					"order by ts.nombre"),
	@NamedQuery(
			name="sectoresPorMunicipioActivos",
			query="select ts from Sector ts " +
					"where ts.municipio.divisionPoliticaId=:pMunicipioId and " +
						  "ts.pasivo='0' " +
					"order by ts.nombre"),
	@NamedQuery(
			name="sectorPorCodigo",
			query="select ts from Sector ts " +
					 "where ts.codigo=:pCodigo ")
})
public class Sector implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Comparator<Sector> ORDEN_CODIGO =
		new Comparator<Sector>() {
			public int compare(Sector s1, Sector s2) {
				return s1.codigo.compareTo(s2.codigo);
			}
	};

	@Id
	@SequenceGenerator(name="SECTORES_SECTORID_GENERATOR", sequenceName="GENERAL.SECTORES_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SECTORES_SECTORID_GENERATOR")
	@Column(name="SECTOR_ID",updatable=false,unique=true, nullable=false, precision=10)
	private long sectorId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FECHA_REGISTRO",updatable=false,nullable=false)
	private Date fechaRegistro;

	// asociación bi-direccional muchos a uno con DivisionPolitica
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MUNICIPIO",referencedColumnName="CODIGO_NACIONAL")
	private DivisionPolitica municipio;

    @Column(nullable=false, length=100)
	private String nombre;

    @Column(nullable=false, length=7)
    private String codigo;
    
    @Column(nullable=false, length=1)
	private String pasivo;

    @Column(nullable=true, length=300)
	private String referencias;

    @Column(nullable=false, length=1)
	private String sede;

	// asociación bi-direccional muchos a uno con Unidad
    @ManyToOne(targetEntity=Unidad.class,fetch=FetchType.LAZY)
	@JoinColumn(name="UNIDAD",referencedColumnName="CODIGO")
	private Unidad unidad;

	@Column(name="USUARIO_REGISTRO",updatable=false, length=100)
	private String usuarioRegistro;
	
	@OneToMany(mappedBy="sector",targetEntity=ni.gob.minsa.malaria.modelo.poblacion.Comunidad.class,fetch=FetchType.LAZY)
	private Set<Comunidad> comunidades;

    public Sector() {
    }

	public long getSectorId() {
		return this.sectorId;
	}

	public void setSectorId(long sectorId) {
		this.sectorId = sectorId;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public DivisionPolitica getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(DivisionPolitica municipio) {
		this.municipio = municipio;
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

	public String getSede() {
		return this.sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}

	public Unidad getUnidad() {
		return this.unidad;
	}

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setComunidades(Set<Comunidad> comunidades) {
		this.comunidades = comunidades;
	}

	public Set<Comunidad> getComunidades() {
		return comunidades;
	}

}