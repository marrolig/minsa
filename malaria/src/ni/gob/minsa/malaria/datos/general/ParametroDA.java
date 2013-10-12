/**
 * ParametroDA.java
 */
package ni.gob.minsa.malaria.datos.general;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.general.Parametro;
import ni.gob.minsa.malaria.servicios.general.ParametroService;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * Acceso de Datos para la entidad {@link Parametro}.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 18/06/2012
 * @since jdk1.6.0_21
 */
public class ParametroDA implements ParametroService {

    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public ParametroDA() {   // clase no instanciable
    	
    }
    
	@Override
	public InfoResultado Encontrar(long pParametroId) {
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		Parametro oParametro = (Parametro)oEM.find(Parametro.class, pParametroId);
    		if (oParametro!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oParametro);
    			return oResultado;
    		}
    		else {
    			oResultado.setMensaje(Mensajes.ENCONTRAR_REGISTRO_NO_EXISTE);
    			oResultado.setOk(false);
    			oResultado.setFilasAfectadas(0);
    			return oResultado;
    		}
    	}
    	catch (Exception iExcepcion) {
    		oResultado.setExcepcion(true);
    		oResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO + iExcepcion.getMessage());
    		oResultado.setFuenteError(iExcepcion.toString().split(":",1).toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
    		oResultado.setFilasAfectadas(0);
    		return oResultado;
    	}
    	finally{
    		oEM.close();
    	}
	}

	@Override
	public InfoResultado Encontrar(String pCodigo) {
        InfoResultado oResultado=new InfoResultado();
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("parametro.encontrarPorCodigo");
            query.setParameter("pCodigo", pCodigo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Parametro oParametro = ((Parametro)query.getSingleResult());
			oResultado.setFilasAfectadas(1);
			oResultado.setOk(true);
			oResultado.setObjeto((Object)oParametro);
			return oResultado;
        }
        catch (NoResultException iExcepcion) {
        	return null;
        }
        catch (Exception iExcepcion) {
        		oResultado.setExcepcion(true);
        		oResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO + iExcepcion.getMessage());
        		oResultado.setFuenteError(iExcepcion.toString().split(":",1).toString());
        		oResultado.setOk(false);
        		oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
        		oResultado.setFilasAfectadas(0);
        		return oResultado;
        }
        finally{
        	em.close();
        }
    }

}
