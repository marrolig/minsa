// ----------------------------------------------------------
// JPAResouceBean
// ----------------------------------------------------------

package ni.gob.minsa.malaria.datos;

import javax.faces.bean.ApplicationScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

/**
 * Este es un bean de Aplicación que mantiene el JPA EntityManagerFactory.  
 * 
 * Como este recurso es a nivel de aplicación el EntityManagerFactory 
 * es creado una sola vez para la aplicación
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 18/09/2010
 * @since jdk1.6.0_21
 */
@ApplicationScoped
public class JPAResourceBean {
	
	@PersistenceUnit
	private EntityManagerFactory iEMF;

	public EntityManagerFactory getEMF() {
		if (iEMF == null){
	    	iEMF = Persistence.createEntityManagerFactory("malaria");
		}
	    return(iEMF);
	}
}
