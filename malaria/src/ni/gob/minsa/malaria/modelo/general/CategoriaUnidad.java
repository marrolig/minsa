package ni.gob.minsa.malaria.modelo.general;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;

import java.math.BigDecimal;


/**
 * The persistent class for the CATEGORIAS_UNIDADES database table.
 * 
 */
@Entity
@Table(name="CATEGORIAS_UNIDADES",schema="GENERAL")
@Cache(alwaysRefresh=true,disableHits=true)
public class CategoriaUnidad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CATEGORIAS_UNIDADES_CATEGORIAUNIDADID_GENERATOR", sequenceName="CATEGORIAS_UNIDADES_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CATEGORIAS_UNIDADES_CATEGORIAUNIDADID_GENERATOR")
	@Column(name="CATEGORIA_UNIDAD_ID", updatable=false)
	private long categoriaUnidadId;

	private BigDecimal codigo;

	private String nombre;

	private String pasivo;

	@Column(name="URL_IMAGEN")
	private String urlImagen;

    public CategoriaUnidad() {
    }

	public long getCategoriaUnidadId() {
		return this.categoriaUnidadId;
	}

	public void setCategoriaUnidadId(long categoriaUnidadId) {
		this.categoriaUnidadId = categoriaUnidadId;
	}

	public BigDecimal getCodigo() {
		return this.codigo;
	}

	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
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

	public String getUrlImagen() {
		return this.urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

}