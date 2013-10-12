// -----------------------------------------------
// FactorRiesgoEventoDA.java
// -----------------------------------------------
package ni.gob.minsa.malaria.datos.vigilancia;

import java.util.*;

import javax.faces.bean.NoneScoped;
import javax.persistence.*;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.vigilancia.FactorRiesgoEvento;
import ni.gob.minsa.malaria.servicios.vigilancia.FactorRiesgoEventoService;
import ni.gob.minsa.malaria.soporte.Mensajes;



/**
 * Acceso de Datos para la entidad FactorRiesgoEvento.
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
public class FactorRiesgoEventoDA implements FactorRiesgoEventoService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public FactorRiesgoEventoDA() {  // clase no instanciable
    }

    @SuppressWarnings("unchecked")
    @Override
	public List<FactorRiesgoEvento> EventosPorFactorRiesgo(long pFactorRiesgoId){
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("eventosPorFactorRiesgo");
            query.setParameter("pFactorRiesgoId", pFactorRiesgoId);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return query.getResultList();
        }finally{
            em.close();
        }
    }

    @Override
    public InfoResultado Eliminar(long pFactorRiesgoEventoId) {
        
    	InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		FactorRiesgoEvento oFactorRiesgoEvento = (FactorRiesgoEvento)oEM.find(FactorRiesgoEvento.class, pFactorRiesgoEventoId);
    		if (oFactorRiesgoEvento!=null) {
    			oEM.remove(oFactorRiesgoEvento);
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
    		oResultado.setFuenteError("FactorRiesgoEventoDA.eliminar");
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
    public InfoResultado Agregar(FactorRiesgoEvento pFactorRiesgoEvento) {
        
    	InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	
    	try{
    		oEM.persist(pFactorRiesgoEvento);
    		oEM.getTransaction().commit();
    		oResultado.setObjeto(pFactorRiesgoEvento);
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);
   			
    		return oResultado;
   			
    	} catch (EntityExistsException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("FactorRiesgoEventoDA.agregar");
    		oResultado.setMensaje(Mensajes.REGISTRO_DUPLICADO);
    		oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
    		oResultado.setOk(false);
    		return oResultado;
    		
    	} catch (PersistenceException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("FactorRiesgoEventoDA.agregar");
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
    public InfoResultado Encontrar(long pFactorRiesgoEventoId) {
        
    	InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		FactorRiesgoEvento oFactorRiesgoEvento = (FactorRiesgoEvento)oEM.find(FactorRiesgoEvento.class, pFactorRiesgoEventoId);
    		if (oFactorRiesgoEvento!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oFactorRiesgoEvento);
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
