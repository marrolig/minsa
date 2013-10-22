package ni.gob.minsa.malaria.modelo.encuesta;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import ni.gob.minsa.malaria.modelo.general.Catalogo;

import org.eclipse.persistence.annotations.Cache;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="ML_ABUNFAUNA")
@Cache(alwaysRefresh=true,disableHits=true)
public class AbundanciaFauna extends Catalogo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1570313656487008116L;

	public AbundanciaFauna() {
		// TODO Auto-generated constructor stub
	}

}
