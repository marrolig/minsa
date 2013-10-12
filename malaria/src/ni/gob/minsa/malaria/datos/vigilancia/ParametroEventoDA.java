// -----------------------------------------------
// ParametroEventoDA.java
// -----------------------------------------------
package ni.gob.minsa.malaria.datos.vigilancia;

import java.util.*;

import javax.faces.bean.NoneScoped;
import javax.persistence.*;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.vigilancia.ParametroEvento;
import ni.gob.minsa.malaria.servicios.vigilancia.ParametroEventoService;
import ni.gob.minsa.malaria.soporte.Mensajes;



/**
 * Acceso de Datos para la entidad ParametroEvento.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 13/06/2011
 * @since jdk1.6.0_21
 */
@NoneScoped
public class ParametroEventoDA implements ParametroEventoService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public ParametroEventoDA() {  // clase no instanciable
    }

    @SuppressWarnings("unchecked")
    @Override
	public List<ParametroEvento> EventosPorParametro(long pParametroEpidemiologicoId){
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("eventosPorParametro");
            query.setParameter("pParametroEpidemiologicoId", pParametroEpidemiologicoId);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return query.getResultList();
        }finally{
            em.close();
        }
    }

    @Override
    public InfoResultado Eliminar(long pParametroEventoId) {
        
    	InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		ParametroEvento oParametroEvento = (ParametroEvento)oEM.find(ParametroEvento.class, pParametroEventoId);
    		if (oParametroEvento!=null) {
    			oEM.remove(oParametroEvento);
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
    		oResultado.setFuenteError("ParametroEventoDA.eliminar");
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
    		
    	} finally{
    		oEM.close();
    	}
    }
    
    @Override
    public InfoResultado Agregar(ParametroEvento pParametroEvento) {
        
    	InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	
    	try{
    		oEM.persist(pParametroEvento);
    		oEM.getTransaction().commit();
    		oResultado.setObjeto(pParametroEvento);
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);
   			
    		return oResultado;
   			
    	} catch (EntityExistsException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("ParametroEventoDA.agregar");
    		oResultado.setMensaje(Mensajes.REGISTRO_DUPLICADO);
    		oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
    		oResultado.setOk(false);
    		return oResultado;
    		
    	} catch (PersistenceException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("ParametroEventoDA.agregar");
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

    @Override
    public InfoResultado Encontrar(long pParametroEventoId) {
        
    	InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		ParametroEvento oParametroEvento = (ParametroEvento)oEM.find(ParametroEvento.class, pParametroEventoId);
    		if (oParametroEvento!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oParametroEvento);
    			return oResultado;
    		}
    		else {
    			oResultado.setMensaje(Mensajes.ENCONTRAR_REGISTRO_NO_EXISTE);
    			oResultado.setOk(false);
    			oResultado.setFilasAfectadas(0);
    			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
    			return oResultado;    		}
    	}
    	catch (Exception iExcepcion) {
    		oResultado.setExcepcion(true);
    		oResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO + iExcepcion.getMessage());
    		oResultado.setFuenteError(iExcepcion.toString().split(":",1).toString());
    		oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
    		oResultado.setOk(false);
    		oResultado.setFilasAfectadas(0);
    		return oResultado;
    	}
    	finally{
    		oEM.close();
    	}
    }

}
