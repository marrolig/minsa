// -----------------------------------------------
// Escolaridad.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.sis;

import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para la escolaridad
 * establecido bajo el código ESCDA
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="ESCDA")
@Cache(alwaysRefresh=true,disableHits=true)
public class Escolaridad extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public Escolaridad() {
    }

}