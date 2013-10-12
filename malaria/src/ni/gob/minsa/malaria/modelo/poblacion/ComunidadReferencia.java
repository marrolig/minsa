package ni.gob.minsa.malaria.modelo.poblacion;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.general.TipoSitioReferencia;
import ni.gob.minsa.malaria.servicios.auditoria.GestorAuditoria;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia para la tabla COMUNIDADES_REFERENCIAS de la base de datos
 * <p>
 * @author Marlon Arróliga 
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.1, &nbsp; 19/05/2012
 * @since jdk1.6.0_21
 */
@Entity
@EntityListeners(GestorAuditoria.class)
@Table(name="COMUNIDADES_REFERENCIAS",schema="GENERAL")
@Cache(alwaysRefresh=true,disableHits=true)
@NamedQueries({
	@NamedQuery(
			name="sitiosReferenciaPorMunicipio",
			query="select cr from ComunidadReferencia cr " +
					"where cr.comunidad.sector.municipio.divisionPoliticaId=:pMunicipioId " +
					"order by cr.nombre"),
	@NamedQuery(
			name="sitiosReferenciaPorUnidad",
			query="select cr from ComunidadReferencia cr " +
					"where cr.comunidad.sector.unidad.unidadId=:pUnidadId " +
					"order by cr.nombre")
})
public class ComunidadReferencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="COMREFS_ID_GENERATOR", sequenceName="GENERAL.COMREFS_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMREFS_ID_GENERATOR")
	@Column(name="COMUNIDAD_REFERENCIA_ID",updatable=false,unique=true, nullable=false, precision=10)
	private long comunidadReferenciaId;
	
	private String observaciones;

    @Column(nullable=false, length=100)
	private String nombre;

    private BigDecimal latitud;

	private BigDecimal longitud;

    @ManyToOne
	@JoinColumn(name="COMUNIDAD", nullable=false, updatable=false, referencedColumnName="CODIGO")
	private Comunidad comunidad;

    @ManyToOne
	@JoinColumn(name="TIPO_SITIO", nullable=false, referencedColumnName="CODIGO")
	private TipoSitioReferencia tipoSitio;

    public ComunidadReferencia() {
    }

	public void setComunidadReferenciaId(long comunidadReferenciaId) {
		this.comunidadReferenciaId = comunidadReferenciaId;
	}

	public long getComunidadReferenciaId() {
		return comunidadReferenciaId;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
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

	public void setComunidad(Comunidad comunidad) {
		this.comunidad = comunidad;
	}

	public Comunidad getComunidad() {
		return comunidad;
	}

	public void setTipoSitio(TipoSitioReferencia tipoSitio) {
		this.tipoSitio = tipoSitio;
	}

	public TipoSitioReferencia getTipoSitio() {
		return tipoSitio;
	}

}