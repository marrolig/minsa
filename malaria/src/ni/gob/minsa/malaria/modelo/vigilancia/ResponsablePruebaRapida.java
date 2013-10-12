// -----------------------------------------------
// ResposablePruebaRapida.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.vigilancia;

import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para los valores de los resultados
 * para la prueba r�pida de malaria y que
 * se encuentra establecido bajo el c�digo Rsp_PRMS
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="Rsp_PRMS")
@Cache(alwaysRefresh=true,disableHits=true)
public class ResponsablePruebaRapida extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public ResponsablePruebaRapida() {
    }

}