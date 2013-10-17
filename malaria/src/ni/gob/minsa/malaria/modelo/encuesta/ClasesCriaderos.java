package ni.gob.minsa.malaria.modelo.encuesta;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="MHm_Dsd+")
@Cache(alwaysRefresh=true,disableHits=true)
public class ClasesCriaderos extends Catalogo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3206203002452371856L;

	public ClasesCriaderos(){
		
	}
}
