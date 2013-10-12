// -----------------------------------------------
// TipoVivienda.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.general;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para Tipo de Vivienda
 * establecido bajo el código TVIVIENDA
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="TVIVIENDA")
@Cache(alwaysRefresh=true,disableHits=true)
public class TipoVivienda extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public TipoVivienda() {
    }

}