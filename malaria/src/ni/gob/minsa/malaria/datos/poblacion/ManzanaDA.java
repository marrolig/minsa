package ni.gob.minsa.malaria.datos.poblacion;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.poblacion.Manzana;
import ni.gob.minsa.malaria.servicios.poblacion.ManzanaService;
import ni.gob.minsa.malaria.soporte.Mensajes;

public class ManzanaDA implements ManzanaService {

	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.poblacion.ManzanaService#ManzanasPorComunidad(java.lang.String, boolean, java.lang.String)
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Manzana> ManzanasPorComunidad(String pComunidad, boolean pSoloActivas, String pManzana) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
        	Query query = null;
        	if (pSoloActivas) {
        		query = em.createNamedQuery("manzanasActivasPorComunidad");
        		query.setParameter("pManzana", pManzana);
        	} else {
        		query = em.createNamedQuery("manzanasTodasPorComunidad");
        	}
        	query.setParameter("pComunidad", pComunidad);
        	query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        	return(query.getResultList());
            		             
        }finally{
            em.close();
        }
	}

	@Override
	public InfoResultado Encontrar(long pManzanaId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		Manzana oManzana = (Manzana)oEM.find(Manzana.class, pManzanaId);
    		if (oManzana!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oManzana);
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
	public InfoResultado Guardar(Manzana pManzana) {

		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
        	Manzana oDetachedManzana = (Manzana)oEM.find(Manzana.class, pManzana.getManzanaId());
        	Manzana oManzana=oEM.merge(oDetachedManzana);
        	oManzana.setPasivo(pManzana.getPasivo());
        	oManzana.setObservaciones(pManzana.getObservaciones());
        	oManzana.setViviendas(pManzana.getViviendas());
        	oManzana.setPoblacion(pManzana.getPoblacion());
        	
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
	public InfoResultado Agregar(Manzana pManzana) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
		@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
            oEM.persist(pManzana);
            oEM.getTransaction().commit();
            oResultado.setObjeto(pManzana);
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
	public InfoResultado Eliminar(long pManzanaId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		Manzana oManzana = (Manzana)oEM.find(Manzana.class, pManzanaId);
    		if (oManzana!=null) {
    			oEM.remove(oManzana);
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
	public List<Manzana> ListarPorComunidad(String pComunidad,
			int pPaginaActual, int pTotalPorPagina, int pNumRegistros) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("manzanasActivasPorComunidad");
            query.setParameter("pComunidad", pComunidad);
    		query.setParameter("pManzana", null);
            //retorna el resultado del query
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            if (pNumRegistros<=pPaginaActual) pPaginaActual-=pNumRegistros;
            pPaginaActual=pNumRegistros<=pPaginaActual ? 0: pPaginaActual;
            query.setFirstResult(pPaginaActual);
            query.setMaxResults(pTotalPorPagina);
            return(query.getResultList());
        }finally{
            em.close();
        }
    }

	@Override
	public int ContarPorComunidad(String pComunidad) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        int totalManzanas=0;
        
        Query query = em.createQuery("select count(tm) from Manzana tm " + 
        								    "where tm.comunidad.codigo=:pComunidad and tm.pasivo=0");
        query.setParameter("pComunidad", pComunidad);
        totalManzanas = ((Long)query.getSingleResult()).intValue();

        return totalManzanas;	
    }

}
