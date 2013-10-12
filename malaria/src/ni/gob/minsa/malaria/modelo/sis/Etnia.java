// -----------------------------------------------
// Etnia.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.sis;

import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para las etnias
 * establecido bajo el código ETNIA
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="ETNIA")
@Cache(alwaysRefresh=true,disableHits=true)
public class Etnia extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public Etnia() {
    }

}