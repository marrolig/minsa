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
import ni.gob.minsa.malaria.modelo.vigilancia.ParametroEpidemiologico;
import ni.gob.minsa.malaria.servicios.vigilancia.ParametroEpidemiologicoService;
import ni.gob.minsa.malaria.soporte.Mensajes;

public class ParametroEpidemiologicoDA implements ParametroEpidemiologicoService {

	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

	@Override
	public InfoResultado Encontrar(long pParametroEpidemiologicoId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		ParametroEpidemiologico oParametroEpidemiologico = (ParametroEpidemiologico)oEM.find(ParametroEpidemiologico.class, pParametroEpidemiologicoId);
    		if (oParametroEpidemiologico!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oParametroEpidemiologico);
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
	public InfoResultado Guardar(ParametroEpidemiologico pParametroEpidemiologico) {

		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
        	ParametroEpidemiologico oDetachedParametroEpidemiologico = (ParametroEpidemiologico)oEM.find(ParametroEpidemiologico.class, pParametroEpidemiologico.getParametroEpidemiologicoId());
        	ParametroEpidemiologico oParametroEpidemiologico=oEM.merge(oDetachedParametroEpidemiologico);
        	oParametroEpidemiologico.setPasivo(pParametroEpidemiologico.getPasivo());
        	oParametroEpidemiologico.setConcepto(pParametroEpidemiologico.getConcepto());
        	oParametroEpidemiologico.setDescripcion(pParametroEpidemiologico.getDescripcion().isEmpty()?null:pParametroEpidemiologico.getDescripcion());
        	oParametroEpidemiologico.setEtiqueta(pParametroEpidemiologico.getEtiqueta());
        	
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
	public InfoResultado Agregar(ParametroEpidemiologico pParametroEpidemiologico) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
		@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
            oEM.persist(pParametroEpidemiologico);
            oEM.getTransaction().commit();
            oResultado.setObjeto(pParametroEpidemiologico);
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
	public InfoResultado Eliminar(long pParametroEpidemiologicoId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		ParametroEpidemiologico oParametroEpidemiologico = (ParametroEpidemiologico)oEM.find(ParametroEpidemiologico.class, pParametroEpidemiologicoId);
    		if (oParametroEpidemiologico!=null) {
    			oEM.remove(oParametroEpidemiologico);
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
	 * @see ni.gob.minsa.malaria.servicios.general.ParametroEpidemiologicoService#Listar(java.lang.Boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ParametroEpidemiologico> Listar(Boolean pPasivo) {
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
	    try{
	    	Query query = em.createNamedQuery("parametroEpidemiologico.listar");
	    	query.setParameter("pPasivo", pPasivo==null?null:pPasivo?1:0);
	        return(query.getResultList());
	    }finally{
	    	em.close();
	    }		
	}

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.general.ParametroEpidemiologicoService#Listar(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ParametroEpidemiologico> Listar(String pParametroEpidemiologico) {
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
	    try{
	    	Query query = em.createNamedQuery("parametroEpidemiologico.listarCombo");
	    	query.setParameter("pParametroEpidemiologico",pParametroEpidemiologico);
	        return(query.getResultList());
	    }finally{
	    	em.close();
	    }		
	}

	@Override
	public ParametroEpidemiologico EncontrarPorCodigo(String pCodigo) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("parametroEpidemiologico.encontrarPorCodigo");
            query.setParameter("pCodigo", pCodigo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return (ParametroEpidemiologico)query.getSingleResult();
        }catch (NoResultException iExcepcion) {
        	return null;
        }
        finally{
            em.close();
        }
    }
}
