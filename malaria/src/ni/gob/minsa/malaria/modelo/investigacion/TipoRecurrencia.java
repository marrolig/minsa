package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.Cache;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para tipo de 
 * recurrencia bajo el código TREC
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="TrecM")
@Cache(alwaysRefresh=true,disableHits=true)
public class TipoRecurrencia extends Catalogo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public TipoRecurrencia() {
	}
   
}
