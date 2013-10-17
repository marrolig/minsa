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
public class EspeciesAnopheles extends Catalogo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EspeciesAnopheles() {
		// TODO Auto-generated constructor stub
	}

}
