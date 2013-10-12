package ni.gob.minsa.malaria.datos.seguridad;


import javax.faces.bean.NoneScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ni.gob.minsa.malaria.datos.JPAResourceBean;

/**
 * Acceso de Datos para la entidad {@link PropiedadUnidad}.
 * La persistencia es accedida mediante {@link JPAResourceBean}
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.1, &nbsp; 26/12/2012
 * @since jdk1.6.0_21
 */
@NoneScoped
public class PropiedadUnidadDA implements ni.gob.minsa.malaria.servicios.seguridad.PropiedadUnidadService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();
    
    public PropiedadUnidadDA() {  // clase no instanciable
    }

    @Override
	public boolean TienePropiedad(long pUnidadId, String pPropiedad){
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createQuery("select count(pu) from PropiedadUnidad pu " +
            		               "where pu.unidad.unidadId=:pUnidadId AND " +
            		               "      pu.propiedad=:pPropiedad");
            query.setParameter("pUnidadId", pUnidadId);
            query.setParameter("pPropiedad", pPropiedad);
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
