package ni.gob.minsa.malaria.modelo.investigacion;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para clasificación 
 * del caso bajo el código CLFC
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="TCCecM")
@Cache(alwaysRefresh=true,disableHits=true)
public class ClasificacionCaso extends Catalogo {

	private static final long serialVersionUID = 1L;

	public ClasificacionCaso() {
	}
   
}
