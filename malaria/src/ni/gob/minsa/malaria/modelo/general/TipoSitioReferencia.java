// -----------------------------------------------
// TipoSitioReferencia.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.general;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para Tipo de Sitio de Referencia
 * establecido bajo el código TSitioRef
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="TSITIOREF")
@Cache(alwaysRefresh=true,disableHits=true)
public class TipoSitioReferencia extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public TipoSitioReferencia() {
    }

}