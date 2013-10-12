// -----------------------------------------------
// ControlEntidadPoblacionDA.java
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
import ni.gob.minsa.malaria.modelo.poblacion.ControlEntidadPoblacion;
import ni.gob.minsa.malaria.servicios.poblacion.ControlEntidadPoblacionService;
import ni.gob.minsa.malaria.soporte.Mensajes;


/**
 * Acceso de Datos para la entidad {@link ControlEntidadPoblacion}.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 09/05/2012
 * @since jdk1.6.0_21
 */
public class ControlEntidadPoblacionDA implements ControlEntidadPoblacionService {

	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public ControlEntidadPoblacionDA() {  // clase no instanciable
    }

	/* (non-Javadoc)
	 * @see servicios.Actividad.ControlEntidadPoblacionService#Encontrar(long)
	 */
	@Override
	public InfoResultado Encontrar(long pControlEntidadPoblacionId) {
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		ControlEntidadPoblacion oControlEntidadPoblacion = (ControlEntidadPoblacion)oEM.find(ni.gob.minsa.malaria.modelo.poblacion.ControlEntidadPoblacion.class, pControlEntidadPoblacionId);
    		if (oControlEntidadPoblacion!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oControlEntidadPoblacion);
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

	/* (non-Javadoc)
	 * @see servicios.Actividad.ControlEntidadPoblacionService#Agregar(modelo.Actividad.ControlEntidadPoblacion)
	 */
	@Override
	public InfoResultado Agregar(ControlEntidadPoblacion pControlEntidadPoblacion) {
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
		@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
            oEM.persist(pControlEntidadPoblacion);
            oEM.getTransaction().commit();
            oResultado.setObjeto(pControlEntidadPoblacion);
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

	/* (non-Javadoc)
	 * @see servicios.Actividad.ControlEntidadPoblacionService#Eliminar(long)
	 */
	@Override
	public InfoResultado Eliminar(long pControlEntidadPoblacionId) {
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		ControlEntidadPoblacion oControlEntidadPoblacion = (ControlEntidadPoblacion)oEM.find(ni.gob.minsa.malaria.modelo.poblacion.ControlEntidadPoblacion.class, pControlEntidadPoblacionId);
    		if (oControlEntidadPoblacion!=null) {
    			oEM.remove(oControlEntidadPoblacion);
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
	 * @see ni.gob.minsa.malaria.servicios.poblacion.ControlEntidadPoblacionService#ControlPorEntidad(java.lang.Integer, long)
	 */
	@Override
	public ControlEntidadPoblacion ControlPorEntidad(Integer pAño, long pEntidad) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("controlPoblacionPorEntidad");
            query.setParameter("pAño", pAño);
            query.setParameter("pEntidadAdtva", pEntidad);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            //retorna el resultado del query
            return((ControlEntidadPoblacion)query.getSingleResult());
        }catch (NoResultException iExcepcion) {
        	return null;
        }finally{
            em.close();
        }
     }

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.ControlEntidadPoblacionService#EntidadesConPoblacionConfirmada(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ControlEntidadPoblacion> EntidadesConPoblacionConfirmada(
			Integer pAño) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("entidadesPoblacionConfirmada");
            query.setParameter("pAño", pAño);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }
	}

}
