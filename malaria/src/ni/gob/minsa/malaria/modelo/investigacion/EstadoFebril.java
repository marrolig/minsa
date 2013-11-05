package ni.gob.minsa.malaria.modelo.investigacion;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.eclipse.persistence.annotations.Cache;

import ni.gob.minsa.malaria.modelo.general.Catalogo;


/**
 * Clase de persistencia para la tabla CATALOGOS
 * con el criterio de filtro para clasificaci�n 
 * cl�nica bajo el c�digo ESTF
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="EFebr")
@Cache(alwaysRefresh=true,disableHits=true)
public class EstadoFebril extends Catalogo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public EstadoFebril(){
		
	}
}
