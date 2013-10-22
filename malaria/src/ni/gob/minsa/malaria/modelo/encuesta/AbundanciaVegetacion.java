package ni.gob.minsa.malaria.modelo.encuesta;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="ML_ABUNVEGETACION")
@Cache(alwaysRefresh=true,disableHits=true)
public class AbundanciaVegetacion extends Catalogo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -788223133763089215L;

	public AbundanciaVegetacion(){
		
	}

}
