// -----------------------------------------------
// TipoIdentificacion.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.sis;

import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para los tipos de identificación
 * establecido bajo el código TPOID
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="TPOID")
@Cache(alwaysRefresh=true,disableHits=true)
public class TipoIdentificacion extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public TipoIdentificacion() {
    }

}