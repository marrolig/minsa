package ni.gob.minsa.malaria.datos.estructura;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.estructura.UnidadAcceso;
import ni.gob.minsa.malaria.modelo.general.ClaseAccesibilidad;
import ni.gob.minsa.malaria.modelo.general.TipoTransporte;
import ni.gob.minsa.malaria.servicios.estructura.UnidadAccesoService;
import ni.gob.minsa.malaria.soporte.Mensajes;

public class UnidadAccesoDA implements UnidadAccesoService {

	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

	@Override
	public InfoResultado Encontrar(long pUnidadAccesoId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		UnidadAcceso oUnidadAcceso = (UnidadAcceso)oEM.find(UnidadAcceso.class, pUnidadAccesoId);
    		if (oUnidadAcceso!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oUnidadAcceso);
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
	public InfoResultado Guardar(UnidadAcceso pUnidadAcceso) {

    	// El objeto que contiene los datos de acceso no es nulo, pero
    	// puede ser que no tenga datos asociados y solo contenga el identificador
    	if ((pUnidadAcceso.getUnidadAccesoId()!=0) && (pUnidadAcceso.getClaseAccesibilidad()==null) &&
    		   (pUnidadAcceso.getComoLlegar()==null || pUnidadAcceso.getComoLlegar().trim().isEmpty()) &&
    		   (pUnidadAcceso.getDistancia()==null && pUnidadAcceso.getTiempo()==null) &&
    		   (pUnidadAcceso.getPuntoReferencia()==null || pUnidadAcceso.getPuntoReferencia().trim().isEmpty()) && 
    		   (pUnidadAcceso.getTipoTransporte()==null)) {
    		
    		return Eliminar(pUnidadAcceso.getUnidadAccesoId());
    	}		

		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
        	
        	UnidadAcceso oDetachedUnidadAcceso = (UnidadAcceso)oEM.find(UnidadAcceso.class, pUnidadAcceso.getUnidadAccesoId());
        	UnidadAcceso oUnidadAcceso=oEM.merge(oDetachedUnidadAcceso);
        	
        	oUnidadAcceso.setComoLlegar(pUnidadAcceso.getComoLlegar());
        	if (pUnidadAcceso.getTipoTransporte()==null) {
        		oUnidadAcceso.setTipoTransporte(null);
        	} else {
        		TipoTransporte oTipoTransporte = (TipoTransporte)oEM.find(TipoTransporte.class, pUnidadAcceso.getTipoTransporte().getCatalogoId());
        		oUnidadAcceso.setTipoTransporte(oTipoTransporte);
        	}
        			
        	if (pUnidadAcceso.getClaseAccesibilidad()==null) {
        		oUnidadAcceso.setClaseAccesibilidad(null);
        	} else {
        		ClaseAccesibilidad oClaseAccesibilidad = (ClaseAccesibilidad)oEM.find(ClaseAccesibilidad.class, pUnidadAcceso.getClaseAccesibilidad().getCatalogoId());
        		oUnidadAcceso.setClaseAccesibilidad(oClaseAccesibilidad);
        	}
           			
        	oUnidadAcceso.setDistancia(pUnidadAcceso.getDistancia());
        	oUnidadAcceso.setPuntoReferencia(pUnidadAcceso.getPuntoReferencia());
        	oUnidadAcceso.setTiempo(pUnidadAcceso.getTiempo());
        	
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
	public InfoResultado Agregar(UnidadAcceso pUnidadAcceso) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
		@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
            oEM.persist(pUnidadAcceso);
            oEM.getTransaction().commit();
            oResultado.setObjeto(pUnidadAcceso);
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
	public InfoResultado Eliminar(long pUnidadAccesoId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		UnidadAcceso oUnidadAcceso = (UnidadAcceso)oEM.find(UnidadAcceso.class, pUnidadAccesoId);
    		if (oUnidadAcceso!=null) {
    			oEM.remove(oUnidadAcceso);
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

	@Override
	public UnidadAcceso EncontrarPorUnidad(long pCodigo) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("unidadAcceso.encontrarPorUnidad");
            query.setParameter("pCodigo", pCodigo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return (UnidadAcceso)query.getSingleResult();
        }catch (NoResultException iExcepcion) {
        	return null;
        }
        finally{
            em.close();
        }			
	}
}
