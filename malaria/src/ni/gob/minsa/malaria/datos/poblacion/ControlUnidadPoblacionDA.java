// -----------------------------------------------
// ControlUnidadPoblacionDA.java
// -----------------------------------------------
package ni.gob.minsa.malaria.datos.poblacion;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.poblacion.ControlUnidadPoblacion;
import ni.gob.minsa.malaria.servicios.poblacion.ControlUnidadPoblacionService;
import ni.gob.minsa.malaria.soporte.Mensajes;


/**
 * Acceso de Datos para la entidad {@link ControlUnidadPoblacion}.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 09/05/2012
 * @since jdk1.6.0_21
 */
public class ControlUnidadPoblacionDA implements ControlUnidadPoblacionService {

	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public ControlUnidadPoblacionDA() {  // clase no instanciable
    }

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.poblacion.ControlUnidadPoblacionService#Encontrar(long)
     */
	@Override
	public InfoResultado Encontrar(long pControlUnidadPoblacionId) {
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		ControlUnidadPoblacion oControlUnidadPoblacion = (ControlUnidadPoblacion)oEM.find(ni.gob.minsa.malaria.modelo.poblacion.ControlUnidadPoblacion.class, pControlUnidadPoblacionId);
    		if (oControlUnidadPoblacion!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oControlUnidadPoblacion);
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

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.ControlUnidadPoblacionService#Agregar(ni.gob.minsa.malaria.modelo.poblacion.ControlUnidadPoblacion)
	 */
	@Override
	public InfoResultado Agregar(ControlUnidadPoblacion pControlUnidadPoblacion) {
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
		@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
            oEM.persist(pControlUnidadPoblacion);
            oEM.getTransaction().commit();
            oResultado.setObjeto(pControlUnidadPoblacion);
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);
   			
    		return oResultado;
   			
    	} catch (EntityExistsException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Agregar");
    		oResultado.setMensaje(Mensajes.REGISTRO_DUPLICADO);
    		oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
    		oResultado.setOk(false);
    		return oResultado;
    		
    	} catch (PersistenceException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Agregar");
    		oResultado.setMensaje("Error al agregar el registro. " + Mensajes.REGISTRO_DUPLICADO);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setOk(false);
    		return oResultado;
    		
    	} catch (Exception iExcepcion){
    		oResultado.setExcepcion(true);
    		oResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO + iExcepcion.getMessage());
    		oResultado.setFuenteError(iExcepcion.toString().split(":",1).toString());
    		oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
    		oResultado.setOk(false);
    		oResultado.setFilasAfectadas(0);
    		return oResultado;
    		
    	} finally{
    		oEM.close();
    	}
	}

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.ControlUnidadPoblacionService#Eliminar(long)
	 */
	@Override
	public InfoResultado Eliminar(long pControlUnidadPoblacionId) {
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		ControlUnidadPoblacion oControlUnidadPoblacion = (ControlUnidadPoblacion)oEM.find(ni.gob.minsa.malaria.modelo.poblacion.ControlUnidadPoblacion.class, pControlUnidadPoblacionId);
    		if (oControlUnidadPoblacion!=null) {
    			oEM.remove(oControlUnidadPoblacion);
    			oEM.getTransaction().commit();
    			
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			
    			return oResultado;
    		}
    		else {
    			
    			oResultado.setMensaje(Mensajes.ELIMINAR_REGISTRO_NO_EXISTE);
    			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
    			oResultado.setOk(false);
    			
    			return oResultado;
    		}
    	} catch (PersistenceException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Eliminar");
    		oResultado.setMensaje(Mensajes.ELIMINAR_RESTRICCION);
    		oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
    		oResultado.setOk(false);
    		return oResultado;
    		
    	} catch (Exception iExcepcion) {
    		
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

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.ControlUnidadPoblacionService#ControlPoblacionPorUnidad(java.lang.Integer, long)
	 */
	@Override
	public ControlUnidadPoblacion ControlPoblacionPorUnidad(Integer pAño, long pUnidad) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("controlPoblacionPorUnidad");
            query.setParameter("pAño", pAño);
            query.setParameter("pUnidad", pUnidad);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            //retorna el resultado del query
            return((ControlUnidadPoblacion)query.getSingleResult());
        }catch (NoResultException iExcepcion) {
        	return null;
        }finally{
            em.close();
        }	
    }

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.ControlUnidadPoblacionService#UnidadesConPoblacionConfirmada(java.lang.Integer, long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ControlUnidadPoblacion> UnidadesConPoblacionConfirmada(Integer pAño, long pEntidadAdtva) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("unidadesPoblacionConfirmadaPorEntidad");
            query.setParameter("pAño", pAño);
            query.setParameter("pEntidadAdtva", pEntidadAdtva);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }
    }

}
