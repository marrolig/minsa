package ni.gob.minsa.malaria.datos.seguridad;

import java.math.BigDecimal;

import javax.faces.bean.NoneScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.servicios.seguridad.CategoriaUnidadService;
/**

 * Acceso de Datos para la entidad {@link CategoriaUnidad}.
 * La persistencia es accedida mediante {@link JPAResourceBean}
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 26/12/2012
 * @since jdk1.6.0_21
 */
@NoneScoped
public class CategoriaUnidadDA implements CategoriaUnidadService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();
    
    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.seguridad.CategoriaUnidadService#TieneCategoria(long, java.lang.String)
     */
	@Override
	public boolean TieneCategoria(long pUnidadId, BigDecimal pCategoria) {
		 EntityManager em = jpaResourceBean.getEMF().createEntityManager();
	        try{
	            Query query = em.createQuery("select count(cu) from Unidad cu " +
	            		               "where cu.unidadId=:pUnidadId and " +
	            		               "      cu.categoria.codigo=:pCategoria");
	            query.setParameter("pUnidadId", pUnidadId);
	            query.setParameter("pCategoria", pCategoria);
	            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
	            
	            int totUnidades = ((Long)query.getSingleResult()).intValue();
	            if (totUnidades>=1) return true;

	            return false;
	        }catch (NoResultException iExcepcion) {
	        	return false;
	        }
	        finally{
	            em.close();
	        }			
	}

}
