package ni.gob.minsa.malaria.datos.vigilancia;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.general.ClaseEvento;
import ni.gob.minsa.malaria.modelo.vigilancia.EventoSalud;
import ni.gob.minsa.malaria.servicios.vigilancia.EventoSaludService;
import ni.gob.minsa.malaria.soporte.Mensajes;

public class EventoSaludDA implements EventoSaludService {

	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

	@Override
	public InfoResultado Encontrar(long pEventoSaludId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		EventoSalud oEventoSalud = (EventoSalud)oEM.find(EventoSalud.class, pEventoSaludId);
    		if (oEventoSalud!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oEventoSalud);
    			return oResultado;
    		}
    		else {
    			oResultado.setMensaje(Mensajes.ENCONTRAR_REGISTRO_NO_EXISTE);
    			oResultado.setOk(false);
    			oResultado.setFilasAfectadas(0);
    			return oResultado;
    		}
    	}
    	catch (ConstraintViolationException iExcepcion) {
    		oResultado.setExcepcion(true);
    		ConstraintViolation<?> oConstraintViolation = iExcepcion.getConstraintViolations().iterator().next();
    		oResultado.setMensaje(oConstraintViolation.getMessage());
    		oResultado.setFuenteError(oConstraintViolation.getPropertyPath().toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setFilasAfectadas(0);
    		return oResultado;
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
	public InfoResultado Guardar(EventoSalud pEventoSalud) {

		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
        	EventoSalud oDetachedEventoSalud = (EventoSalud)oEM.find(EventoSalud.class, pEventoSalud.getEventoSaludId());
        	EventoSalud oEventoSalud=oEM.merge(oDetachedEventoSalud);
        	oEventoSalud.setPasivo(pEventoSalud.getPasivo());
        	oEventoSalud.setNombre(pEventoSalud.getNombre());
        	
        	ClaseEvento oClaseEvento = (ClaseEvento)oEM.find(ClaseEvento.class, pEventoSalud.getClaseEvento().getCatalogoId());
        	oEventoSalud.setClaseEvento(oClaseEvento);
        	
            oEM.getTransaction().commit();
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);
   			
    		return oResultado;
   			
    	} catch (EntityExistsException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Guardar");
    		oResultado.setMensaje(Mensajes.EXCEPCION_REGISTRO_EXISTE);
    		oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
    		oResultado.setOk(false);
    		return oResultado;
    		
    	} catch (ConstraintViolationException iExcepcion) {
    		oResultado.setExcepcion(true);
    		ConstraintViolation<?> oConstraintViolation = iExcepcion.getConstraintViolations().iterator().next();
    		oResultado.setMensaje(oConstraintViolation.getMessage());
    		oResultado.setFuenteError(oConstraintViolation.getPropertyPath().toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setFilasAfectadas(0);
    		return oResultado;
    	} 
    	catch (PersistenceException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Guardar");
    		oResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setOk(false);
    		return oResultado;
    		
    	} catch (Exception iExcepcion){
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
	public InfoResultado Agregar(EventoSalud pEventoSalud) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
		@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
            oEM.persist(pEventoSalud);
            oEM.getTransaction().commit();
            oResultado.setObjeto(pEventoSalud);
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
    		
    	} catch (ConstraintViolationException iExcepcion) {
    		oResultado.setExcepcion(true);
    		ConstraintViolation<?> oConstraintViolation = iExcepcion.getConstraintViolations().iterator().next();
    		oResultado.setMensaje(oConstraintViolation.getMessage());
    		oResultado.setFuenteError(oConstraintViolation.getPropertyPath().toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setFilasAfectadas(0);
    		return oResultado;
    	} 
    	catch (PersistenceException iExPersistencia) {
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

	@Override
	public InfoResultado Eliminar(long pEventoSaludId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		EventoSalud oEventoSalud = (EventoSalud)oEM.find(EventoSalud.class, pEventoSaludId);
    		if (oEventoSalud!=null) {
    			oEM.remove(oEventoSalud);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<EventoSalud> Listar(Boolean pPasivo) {
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
	    try{
	    	Query query = em.createNamedQuery("eventoSalud.listar");
	    	query.setParameter("pPasivo", pPasivo==null?null:pPasivo?1:0);
	        return(query.getResultList());
	    }finally{
	    	em.close();
	    }		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventoSalud> Listar(String pEventoSalud) {
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
	    try{
	    	Query query = em.createNamedQuery("eventoSalud.listarCombo");
	    	query.setParameter("pEventoSalud",pEventoSalud);
	        return(query.getResultList());
	    }finally{
	    	em.close();
	    }		
	}

	@Override
	public EventoSalud EncontrarPorCodigo(String pCodigo) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("eventoSalud.encontrarPorCodigo");
            query.setParameter("pCodigo", pCodigo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return (EventoSalud)query.getSingleResult();
        }catch (NoResultException iExcepcion) {
        	return null;
        }
        finally{
            em.close();
        }			
	}
}
