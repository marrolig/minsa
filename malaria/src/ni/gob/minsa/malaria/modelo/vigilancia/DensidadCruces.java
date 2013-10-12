// -----------------------------------------------
// DensidadCruces.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.vigilancia;

import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para los valores de las densidades
 * en cruces utilizadas para el diagnóstico de la prueba de gota
 * gruesa.  Estos valores se encuentran bajo el código MHm_Dsd+
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="MHm_Dsd+")
@Cache(alwaysRefresh=true,disableHits=true)
public class DensidadCruces extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public DensidadCruces() {
    }

}