package ni.gob.minsa.malaria.modelo.investigacion;

import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para grado de 
 * confirmación diagnóstica bajo el código 
 * CDIAG
 */

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="CDMecM")
@Cache(alwaysRefresh=true,disableHits=true)
public class ConfirmacionDiagnostico extends Catalogo {

	private static final long serialVersionUID = 1L;

	public ConfirmacionDiagnostico() {
	}
   
}
