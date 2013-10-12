// -----------------------------------------------
// MotivoFaltaDiagnostico.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.vigilancia;

import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para los valores de los motivos
 * de falta de diagnóstico para el muestreo hemático y que
 * se encuentra establecido bajo el código MHm_ND
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="MHm_ND")
@Cache(alwaysRefresh=true,disableHits=true)
public class MotivoFaltaDiagnostico extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public MotivoFaltaDiagnostico() {
    }

}