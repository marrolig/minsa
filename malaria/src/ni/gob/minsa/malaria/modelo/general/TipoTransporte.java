// -----------------------------------------------
// TipoTransporte.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.general;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para Tipo de Transporte
 * establecido bajo el código TTRANSP
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="TTRANSP")
@Cache(alwaysRefresh=true,disableHits=true)
public class TipoTransporte extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public TipoTransporte() {
    }

}