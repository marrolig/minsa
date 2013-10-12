// -----------------------------------------------
// EstadoCivil.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.sis;

import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para el estado civil de las personas
 * establecido bajo el código ESTCV
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="ESTCV")
@Cache(alwaysRefresh=true,disableHits=true)
public class EstadoCivil extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public EstadoCivil() {
    }

}