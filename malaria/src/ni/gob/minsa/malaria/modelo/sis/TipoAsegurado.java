// -----------------------------------------------
// TipoAsegurado.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.sis;

import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para los tipos de asegurado
 * establecido bajo el código TPOAS
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="TPOAS")
@Cache(alwaysRefresh=true,disableHits=true)
public class TipoAsegurado extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public TipoAsegurado() {
    }

}