// -----------------------------------------------
// ClaseEvento.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.general;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para Tipo de Vivienda
 * establecido bajo el código CEVENTO
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="CEVENTO")
@Cache(alwaysRefresh=true,disableHits=true)
public class ClaseEvento extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public ClaseEvento() {
    }

}