// -----------------------------------------------
// SistemaCatalogo.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.general;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;
import ni.gob.minsa.malaria.modelo.portal.Sistema;

/**
 * Clase de persistencia para la tabla SISTEMASCATALOGOS
 * <p>
 * Asignación de los sistemas a los cuales responde un elemento
 * del catálogo.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 19/05/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="SISTEMASCATALOGOS",schema="GENERAL")
@AdditionalCriteria(value="this.sistema.codigo='malaria'")
public class SistemaCatalogo extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SISTCATS_ID_GENERATOR", sequenceName="GENERAL.SISTCAT_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SISTCATS_ID_GENERATOR")
	@Column(name="SISTEMACATALOGO_ID",updatable=false)
	private long sistemaCatalogoId;

    @ManyToOne(targetEntity=Sistema.class)
	@JoinColumn(name="SISTEMA",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private Sistema sistema;

    @ManyToOne(targetEntity=Catalogo.class)
	@JoinColumn(name="CATALOGO",referencedColumnName="CODIGO",nullable=false,updatable=false)
	private Catalogo catalogo;

    public SistemaCatalogo() {
    }

	public void setSistemaCatalogoId(long sistemaCatalogoId) {
		this.sistemaCatalogoId = sistemaCatalogoId;
	}

	public long getSistemaCatalogoId() {
		return sistemaCatalogoId;
	}

	public void setCatalogo(Catalogo catalogo) {
		this.catalogo = catalogo;
	}

	public Catalogo getCatalogo() {
		return catalogo;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public Sistema getSistema() {
		return sistema;
	}

}