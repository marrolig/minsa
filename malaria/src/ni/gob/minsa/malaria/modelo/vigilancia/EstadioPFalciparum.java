// -----------------------------------------------
// EstadioPFalciparum.java
// -----------------------------------------------

package ni.gob.minsa.malaria.modelo.vigilancia;

import javax.persistence.*;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para los valores de los estad�os
 * del P.falciparum utilizadas para el diagn�stico de la prueba de gota
 * gruesa.  Estos valores se encuentran bajo el c�digo MHm_EtPF
 * 
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="MHm_EtPF")
@Cache(alwaysRefresh=true,disableHits=true)
public class EstadioPFalciparum extends Catalogo {
	private static final long serialVersionUID = 1L;
	
    public EstadioPFalciparum() {
    }

}