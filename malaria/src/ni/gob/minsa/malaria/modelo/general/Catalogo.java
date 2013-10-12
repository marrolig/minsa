// -----------------------------------------------
// Catalogo.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.general;

import java.io.Serializable;
import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.BaseEntidadCatalogo;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 */
@Entity
@Table(name="CATALOGOS", schema="GENERAL")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Cache(alwaysRefresh=true,disableHits=true)
@DiscriminatorColumn(name="DEPENDENCIA",discriminatorType = DiscriminatorType.STRING)
@NamedQueries({
	@NamedQuery(
			name="catalogo.listarColecciones",
			query="select tr from Catalogo tr " +
					"where tr.pasivo='0' and " +
					"tr.nodoPadre IS NULL and " +
					"EXISTS (select s from tr.sistemas s) " +
					"order by tr.orden")
})
public class Catalogo extends BaseEntidadCatalogo implements Serializable {
	private static final long serialVersionUID = 1L;

    public Catalogo() {
    }

}