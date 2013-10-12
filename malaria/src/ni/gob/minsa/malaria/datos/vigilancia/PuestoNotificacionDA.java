package ni.gob.minsa.malaria.datos.vigilancia;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ejbPersona.servicios.PersonaBTMService;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.estructura.UnidadAcceso;
import ni.gob.minsa.malaria.modelo.general.ClaseAccesibilidad;
import ni.gob.minsa.malaria.modelo.general.TipoTransporte;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoNotificacion;
import ni.gob.minsa.malaria.modelo.vigilancia.noEntidad.ColVolPuesto;
import ni.gob.minsa.malaria.reglas.Validacion;
import ni.gob.minsa.malaria.reglas.VigilanciaValidacion;
import ni.gob.minsa.malaria.servicios.vigilancia.PuestoNotificacionService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

public class PuestoNotificacionDA implements PuestoNotificacionService {

	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

	@Override
	public InfoResultado Encontrar(long pPuestoNotificacionId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		PuestoNotificacion oPuestoNotificacion = (PuestoNotificacion)oEM.find(PuestoNotificacion.class, pPuestoNotificacionId);
    		if (oPuestoNotificacion!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oPuestoNotificacion);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<PuestoNotificacion> ListarUnidadesPorEntidad(long pEntidadAdtvaId, boolean pSoloActivos) {
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
	    try{
	    	Query query = null;
	    	query=em.createNamedQuery("PuestoNotificacion.listarUnidadesPorEntidad");
	    	query.setParameter("pEntidadAdtvaId",pEntidadAdtvaId);
	    	query.setParameter("pTodos", pSoloActivos?0:1);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
	        return(query.getResultList());
	    }finally{
	    	em.close();
	    }		
	}

	@SuppressWarnings("unchecked")
	public List<PuestoNotificacion> ListarPuestosPorUnidad(long pUnidadId, boolean pSoloActivos) {
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
	    try{
	    	Query query = null;
	    	query=em.createNamedQuery("PuestoNotificacion.listarPuestosPorUnidad");
	    	query.setParameter("pUnidadId",pUnidadId);
	    	query.setParameter("pTodos", pSoloActivos?0:1);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
	        return(query.getResultList());
	    }finally{
	    	em.close();
	    }		
	}
	
	@SuppressWarnings("unchecked")
	public List<PuestoNotificacion> ListarPuestosPorUnidad(long pUnidadId, String pNombre, 
			boolean pSoloActivos, int pPaginaActual, 
			int pTotalPorPagina, int pNumRegistros) {

		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
	    try{
	    	Query query = null;
	    	if (pNombre==null || pNombre.trim().isEmpty()) {
	    		query=em.createNamedQuery("PuestoNotificacion.listarPuestosPorUnidad");
	    	} else {
	    		query=em.createNamedQuery("PuestoNotificacion.listarPuestosPorNombre");
	    		query.setParameter("pCodigoNombre",pNombre.toUpperCase());
	    	}
	    	query.setParameter("pUnidadId",pUnidadId);
	    	query.setParameter("pTodos", pSoloActivos?0:1);
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
	public PuestoNotificacion EncontrarPorUnidad(long pUnidadId,int pActiva) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("PuestoNotificacion.encontrarPorUnidad");
            query.setParameter("pUnidadId", pUnidadId);
            query.setParameter("pActiva", pActiva);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return (PuestoNotificacion)query.getSingleResult();
        }catch (NoResultException iExcepcion) {
        	return null;
        }
        finally{
            em.close();
        }			
	}

	public PuestoNotificacion EncontrarPorColVol(long pColVolId) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("PuestoNotificacion.encontrarPorColVol");
            query.setParameter("pColVolId", pColVolId);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return (PuestoNotificacion)query.getSingleResult();
        }catch (NoResultException iExcepcion) {
        	return null;
        }
        finally{
            em.close();
        }			
	}

	@Override
	public PuestoNotificacion EncontrarPorClave(String pClave) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("PuestoNotificacion.encontrarPorClave");
            query.setParameter("pClave", pClave);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return (PuestoNotificacion)query.getSingleResult();
        }catch (NoResultException iExcepcion) {
        	return null;
        }
        finally{
            em.close();
        }
	}

	@Override
	public InfoResultado Agregar(PuestoNotificacion pPuestoNotificacion,
			Unidad pUnidad, UnidadAcceso pUnidadAcceso) {

		InfoResultado oResultado=new InfoResultado();

		oResultado=Validacion.coordenadas(pUnidad.getLatitud(),pUnidad.getLongitud());
		if (!oResultado.isOk()) return oResultado;
		
		oResultado=VigilanciaValidacion.validarUnidadAcceso(pUnidadAcceso);
		if (!oResultado.isOk()) return oResultado;
		
		oResultado=VigilanciaValidacion.validarPuestoNotificacion(pPuestoNotificacion, pUnidad);
		if (!oResultado.isOk()) return oResultado;
		
		oResultado=new InfoResultado();

    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
		@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{

        	// actualiza los datos de la unidad de salud
        	
        	Unidad oDetachedUnidad = (Unidad)oEM.find(Unidad.class, pUnidad.getUnidadId());
        	Unidad oUnidad=oEM.merge(oDetachedUnidad);
        	oUnidad.setLatitud(pUnidad.getLatitud());
        	oUnidad.setLongitud(pUnidad.getLongitud());
        	oUnidad.setDireccion((pUnidad.getDireccion()!=null && !pUnidad.getDireccion().trim().isEmpty())?pUnidad.getDireccion():null);
        	oUnidad.setTelefono((pUnidad.getTelefono()!=null && !pUnidad.getTelefono().trim().isEmpty())?pUnidad.getTelefono():null);
        	oUnidad.setFax((pUnidad.getFax()!=null && !pUnidad.getFax().trim().isEmpty())?pUnidad.getFax():null);
        	oUnidad.setEmail((pUnidad.getEmail()!=null && !pUnidad.getEmail().trim().isEmpty())?pUnidad.getEmail():null);

        	// agrega, modifica o elimina los datos de acceso de la unidad de salud
        	
        	if (pUnidadAcceso.getUnidadAccesoId()!=0) {
        		UnidadAcceso oDetachedUnidadAcceso = (UnidadAcceso)oEM.find(UnidadAcceso.class, pUnidadAcceso.getUnidadAccesoId());
        			
        		if ((pUnidadAcceso.getClaseAccesibilidad()==null) &&
        				(pUnidadAcceso.getComoLlegar()==null || pUnidadAcceso.getComoLlegar().trim().isEmpty()) &&
        				(pUnidadAcceso.getDistancia()==null && pUnidadAcceso.getTiempo()==null) &&
        				(pUnidadAcceso.getPuntoReferencia()==null || pUnidadAcceso.getPuntoReferencia().trim().isEmpty()) && 
        				(pUnidadAcceso.getTipoTransporte()==null)) {
        					oEM.remove(oDetachedUnidadAcceso);
        		} else {
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
        		}
        	} else {

        		// la declaración de como llegar es requerida en caso de que uno de los otros datos
        		// se haya declarado, o bien, de forma independiente, por tanto la existencia de
        		// dicho dato indica que hay un nuevo objeto UnidadAcceso que requiere ser persistido
        		if (pUnidadAcceso.getComoLlegar()!=null && !pUnidadAcceso.getComoLlegar().trim().isEmpty()) { 
        			oEM.persist(pUnidadAcceso);
        		}
        	}

        	oEM.persist(pPuestoNotificacion);

            oEM.getTransaction().commit();
            oResultado.setObjeto(pPuestoNotificacion);
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);
    		
    		return oResultado;
   			
    	} catch (EntityExistsException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Agregar Puesto de Notificación");
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
    		oResultado.setFuenteError("Agregar Puesto de Notificación");
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
	public InfoResultado Eliminar(long pPuestoNotificacionId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		PuestoNotificacion oPuestoNotificacion = (PuestoNotificacion)oEM.find(PuestoNotificacion.class, pPuestoNotificacionId);
    		if (oPuestoNotificacion!=null) {
    			oEM.remove(oPuestoNotificacion);
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
	public InfoResultado Guardar(PuestoNotificacion pPuestoNotificacion,
			Unidad pUnidad, UnidadAcceso pUnidadAcceso) {
		
		InfoResultado oResultado=new InfoResultado();

		oResultado=Validacion.coordenadas(pUnidad.getLatitud(),pUnidad.getLongitud());
		if (!oResultado.isOk()) return oResultado;
		
		oResultado=VigilanciaValidacion.validarUnidadAcceso(pUnidadAcceso);
		if (!oResultado.isOk()) return oResultado;
		
		oResultado=VigilanciaValidacion.validarPuestoNotificacion(pPuestoNotificacion, pUnidad);
		if (!oResultado.isOk()) return oResultado;

		oResultado=new InfoResultado();
		
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	
        try{
        	
        	// actualiza los datos de la unidad de salud
        	
        	Unidad oDetachedUnidad = (Unidad)oEM.find(Unidad.class, pUnidad.getUnidadId());
        	Unidad oUnidad=oEM.merge(oDetachedUnidad);
        	oUnidad.setLatitud(pUnidad.getLatitud());
        	oUnidad.setLongitud(pUnidad.getLongitud());
        	oUnidad.setDireccion((pUnidad.getDireccion()!=null && !pUnidad.getDireccion().trim().isEmpty())?pUnidad.getDireccion():null);
        	oUnidad.setTelefono((pUnidad.getTelefono()!=null && !pUnidad.getTelefono().trim().isEmpty())?pUnidad.getTelefono():null);
        	oUnidad.setFax((pUnidad.getFax()!=null && !pUnidad.getFax().trim().isEmpty())?pUnidad.getFax():null);
        	oUnidad.setEmail((pUnidad.getEmail()!=null && !pUnidad.getEmail().trim().isEmpty())?pUnidad.getEmail():null);

        	// agrega, modifica o elimina los datos de acceso de la unidad de salud
        	
        	if (pUnidadAcceso.getUnidadAccesoId()!=0) {
        		UnidadAcceso oDetachedUnidadAcceso = (UnidadAcceso)oEM.find(UnidadAcceso.class, pUnidadAcceso.getUnidadAccesoId());
        			
        		if ((pUnidadAcceso.getClaseAccesibilidad()==null) &&
        				(pUnidadAcceso.getComoLlegar()==null || pUnidadAcceso.getComoLlegar().trim().isEmpty()) &&
        				(pUnidadAcceso.getDistancia()==null && pUnidadAcceso.getTiempo()==null) &&
        				(pUnidadAcceso.getPuntoReferencia()==null || pUnidadAcceso.getPuntoReferencia().trim().isEmpty()) && 
        				(pUnidadAcceso.getTipoTransporte()==null)) {
        					oEM.remove(oDetachedUnidadAcceso);
        		} else {
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
        		}
        	} else {
        		
        		// la declaración de como llegar es requerida en caso de que uno de los otros datos
        		// se haya declarado, o bien, de forma independiente, por tanto la existencia de
        		// dicho dato indica que hay un nuevo objeto UnidadAcceso que requiere ser persistido
        		if (pUnidadAcceso.getComoLlegar()!=null && !pUnidadAcceso.getComoLlegar().trim().isEmpty()) { 
        			oEM.persist(pUnidadAcceso);
        		}

        	}
        	
        	PuestoNotificacion oDetachedPuesto = (PuestoNotificacion)oEM.find(PuestoNotificacion.class, pPuestoNotificacion.getPuestoNotificacionId());
        	PuestoNotificacion oPuestoNotificacion=oEM.merge(oDetachedPuesto);
        	oPuestoNotificacion.setFechaApertura(pPuestoNotificacion.getFechaApertura());
        	oPuestoNotificacion.setFechaCierre(pPuestoNotificacion.getFechaCierre());
        	oPuestoNotificacion.setClave(pPuestoNotificacion.getClave());
        	oPuestoNotificacion.setObservaciones(pPuestoNotificacion.getObservaciones()!=null && !pPuestoNotificacion.getObservaciones().trim().isEmpty()?pPuestoNotificacion.getObservaciones().trim():null);

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
    		iExcepcion.printStackTrace();
    		return oResultado;
    		
    	} finally{
    		oEM.close();
    	}
    }

	@Override
	public int ContarPorEntidad(long pEntidadAdtvaId, String pNombre,
			String pTipoPuesto, boolean pSoloActivos) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        int totalUnidades=0;
        int totalColVoles=0;
        String pCodigoNombre="";
        
        if ((pNombre==null) || (pNombre.trim().isEmpty())) {
        	Query query = null;
        	if ((pTipoPuesto == null) || (pTipoPuesto.equals(Utilidades.PUESTO_NOTIFICACION_UNIDAD))) {
        		query = em.createQuery("select count(pn) from PuestoNotificacion pn " +
        									"where (pn.unidad IS NOT NULL AND pn.unidad.entidadAdtva.entidadAdtvaId=:pEntidadAdtvaId) and " +
        								 	"   (:pTodos=1 or (:pTodos=0 and (pn.fechaCierre is null or pn.fechaCierre>CURRENT_DATE))) ");
        		query.setParameter("pEntidadAdtvaId", pEntidadAdtvaId);
        		query.setParameter("pTodos", pSoloActivos?0:1);
        		totalUnidades = ((Long)query.getSingleResult()).intValue();
        	} 
        	if ((pTipoPuesto == null) || (pTipoPuesto.equals(Utilidades.PUESTO_NOTIFICACION_COLVOL))) {
        		query = em.createQuery("select count(pn) from PuestoNotificacion pn " +
											"where (pn.colVol IS NOT NULL AND pn.colVol.unidad.entidadAdtva.entidadAdtvaId=:pEntidadAdtvaId) and " +
					 						"   (:pTodos=1 or (:pTodos=0 and (pn.fechaCierre is null or pn.fechaCierre>CURRENT_DATE))) ");
        		query.setParameter("pEntidadAdtvaId", pEntidadAdtvaId);
        		query.setParameter("pTodos", pSoloActivos?0:1);
        		totalColVoles = ((Long)query.getSingleResult()).intValue();
        	}
        } else {
        	Query query = null;
        	if ((pTipoPuesto == null) || (pTipoPuesto.equals(Utilidades.PUESTO_NOTIFICACION_UNIDAD))) {
        		query = em.createQuery("select count(pn) from PuestoNotificacion pn " +
										 "where (pn.unidad IS NOT NULL AND pn.unidad.entidadAdtva.entidadAdtvaId=:pEntidadAdtvaId) and " +
										 "  (:pTodos=1 or (:pTodos=0 and (pn.fechaCierre is null or pn.fechaCierre>CURRENT_DATE))) and " +
										 "  (UPPER(pn.unidad.nombre) LIKE :pNombre)");
        		query.setParameter("pEntidadAdtvaId", pEntidadAdtvaId);
        		query.setParameter("pTodos", pSoloActivos?0:1);
        		query.setParameter("pNombre", "%" + pNombre.toUpperCase() + "%");
        		totalUnidades = ((Long)query.getSingleResult()).intValue();
        	}
        	if ((pTipoPuesto == null) || (pTipoPuesto.equals(Utilidades.PUESTO_NOTIFICACION_COLVOL))) {
        		
            	InitialContext ctx;
            	PersonaBTMService personaBTMService = null;

                try{
            		ctx = new InitialContext();
            		personaBTMService = (PersonaBTMService)ctx.lookup("ejb/PersonaBTM");
            		pCodigoNombre=personaBTMService.obtenerCodigoSND(pNombre);
        		
            		query = em.createQuery("select count(pn) from PuestoNotificacion pn " +
										 		"where (pn.colVol IS NOT NULL AND pn.colVol.unidad.entidadAdtva.entidadAdtvaId=:pEntidadAdtvaId) and " +
										 		"  (:pTodos=1 or (:pTodos=0 and (pn.fechaCierre is null or pn.fechaCierre>CURRENT_DATE))) and " +
										 		"  FUNC('CATSEARCH',pn.colVol.persona.sndNombre, :pCodigoNombre,null)>0");
            		query.setParameter("pEntidadAdtvaId", pEntidadAdtvaId);
            		query.setParameter("pTodos", pSoloActivos?0:1);
            		query.setParameter("pCodigoNombre", pCodigoNombre);
            		totalColVoles = ((Long)query.getSingleResult()).intValue();
                } catch (NamingException iExcepcion){
            		iExcepcion.printStackTrace();
            		return totalUnidades;
                } finally {
                	em.close();
                }
        	}
        	
        }
        return (totalUnidades+totalColVoles);
	}
	
	public int ContarPorUnidad(long pUnidadId, String pNombre, boolean pSoloActivos) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        int totalPuestos=0;
        
        InitialContext ctx;
        String oCodigoSND=null;
        
        if ((pNombre!=null) && (!pNombre.trim().isEmpty())) {
        	try {
        		ctx = new InitialContext();
        		PersonaBTMService personaBTMService = (PersonaBTMService)ctx.lookup("ejb/PersonaBTM");
        		oCodigoSND=personaBTMService.obtenerCodigoSND(pNombre).toString();
        		System.out.println("-------------------------------------------------------");
        		System.out.println("Código: " + oCodigoSND);
        	} catch (NamingException e) {
        		FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_FATAL, "El servicio para el enlace a personas no se encuentra activo",Mensajes.NOTIFICACION_ADMINISTRADOR);
        		if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
        		return 0;
        	}
        }
        
        if ((pNombre==null) || (pNombre.trim().isEmpty())) {
        	Query query = em.createQuery("select count(pn) from PuestoNotificacion pn " +
        								 "where pn.colVol IS NOT NULL AND " +
        								 "		pn.colVol.unidad.unidadId=:pUnidadId AND " +
        								 "      (:pTodos=1 OR (:pTodos=0 AND (pn.fechaCierre IS NULL OR pn.fechaCierre>CURRENT_DATE))) ");
            query.setParameter("pUnidadId", pUnidadId);
            query.setParameter("pTodos", pSoloActivos?0:1);
        	totalPuestos = ((Long)query.getSingleResult()).intValue();
        } else {
        	Query query = em.createQuery("select count(pn) from PuestoNotificacion pn " +
										 "where pn.colVol.unidad.unidadId=:pUnidadId AND " +
										 "  (:pTodos=1 OR (:pTodos=0 AND (pn.fechaCierre IS NULL OR pn.fechaCierre>CURRENT_DATE))) AND " +
										 "  FUNC('CATSEARCH',pn.colVol.sisPersona.sndNombre, :pCodigoNombre,null)>0");
        	query.setParameter("pUnidadId", pUnidadId);
        	query.setParameter("pTodos", pSoloActivos?0:1);
        	query.setParameter("pCodigoNombre", oCodigoSND);
        	totalPuestos = ((Long)query.getSingleResult()).intValue();
        }
        return totalPuestos;
	}
	
	@SuppressWarnings("unchecked")
	public List<ColVolPuesto> ListarColVolPorUnidad(long pUnidadId, String pNombre, 
										boolean pSoloActivos, int pPaginaActual, 
										int pTotalPorPagina, int pNumRegistros) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();

        InitialContext ctx;
        String oCodigoSND=null;
    	BigDecimal iTodos = pSoloActivos?(new BigDecimal(0)):(new BigDecimal(1));
        
        if ((pNombre!=null) && (!pNombre.trim().isEmpty())) {
        	try {
        		ctx = new InitialContext();
        		PersonaBTMService personaBTMService = (PersonaBTMService)ctx.lookup("ejb/PersonaBTM");
        		oCodigoSND=personaBTMService.obtenerCodigoSND(pNombre).toString();
        	} catch (NamingException e) {
        		FacesMessage msg = Mensajes.enviarMensaje(FacesMessage.SEVERITY_FATAL, "El servicio para el enlace a personas no se encuentra activo",Mensajes.NOTIFICACION_ADMINISTRADOR);
        		if (msg!=null) FacesContext.getCurrentInstance().addMessage(null, msg);
        		return (new ArrayList<ColVolPuesto>());
        	}
        }

	    try {

	    	Query query = null;

	    	if ((pNombre==null) || (pNombre.trim().isEmpty())) {
	    		query = em.createNativeQuery("WITH params AS (SELECT ? AS pUnidadId, ? AS pTodos, ? AS pCodigoSND FROM DUAL) " +
					     "SELECT t2.PUESTO_NOTIFICACION_ID AS a1, " +
					     "	     t2.CLAVE AS a2, t0.PRIMER_NOMBRE AS a3, t0.SEGUNDO_NOMBRE AS a4, t0.PRIMER_APELLIDO AS a5, t0.SEGUNDO_APELLIDO AS a6 " +
					     "	FROM GENERAL.UNIDADES t3, " +
					     "		 SIVE.PUESTOS_NOTIFICACION t2, " +
					     "		 SIVE.COLVOLS t1, " +
					     "		 SIS.SIS_PERSONAS t0 " +
					     "       INNER JOIN params p ON 1=1 " +
					     "	WHERE ((((t3.UNIDAD_ID = p.pUnidadId AND " +
					     "		  NOT (t2.COLVOL IS NULL)) AND " +
					     "		  ((p.pTodos = 1) OR ((p.pTodos = 0) AND " + 
					     "        ((t2.FECHA_CIERRE IS NULL) OR (t2.FECHA_CIERRE > TO_DATE(CURRENT_DATE)))))) AND " +
					     "		  (((t1.COLVOL_ID = t2.COLVOL) AND " +
					     "		  (t3.CODIGO = t1.UNIDAD)) AND " +
					     "		  (t0.PERSONA_ID = t1.PERSONA))) " +
					     "	ORDER BY t0.PRIMER_APELLIDO ASC, " +
					     "			 t0.SEGUNDO_APELLIDO ASC, " +
					     "			 t0.PRIMER_NOMBRE ASC, " +
					     "			 t0.SEGUNDO_NOMBRE ASC)");

	    		query.setParameter(1, pUnidadId);
            	query.setParameter(2, iTodos);
	    		
	    	} else {
	    		query = em.createNativeQuery("WITH params AS (SELECT ? AS pUnidadId, ? AS pTodos, ? AS pCodigoSND FROM DUAL) " +
	    								     "SELECT t2.PUESTO_NOTIFICACION_ID AS a1, " +
	    								     "	     t2.CLAVE AS a2, t0.PRIMER_NOMBRE AS a3, t0.SEGUNDO_NOMBRE AS a4, t0.PRIMER_APELLIDO AS a5, t0.SEGUNDO_APELLIDO AS a6 " +
	    								     "	FROM GENERAL.UNIDADES t3, " +
	    								     "		 SIVE.PUESTOS_NOTIFICACION t2, " +
	    								     "		 SIVE.COLVOLS t1, " +
	    								     "		 SIS.SIS_PERSONAS t0 " +
	    								     "       INNER JOIN params p ON 1=1 " +
	    								     "	WHERE ((((t3.UNIDAD_ID = p.pUnidadId AND " +
	    								     "		  NOT (t2.COLVOL IS NULL)) AND " +
	    								     "		  ((p.pTodos = 1) OR ((p.pTodos = 0) AND " + 
	    								     "        ((t2.FECHA_CIERRE IS NULL) OR (t2.FECHA_CIERRE > TO_DATE(CURRENT_DATE)))))) AND " +
	    								     "        (CATSEARCH(t0.SND_NOMBRE, p.pCodigoSND, null) > 0)) AND " +
	    								     "		  (((t1.COLVOL_ID = t2.COLVOL) AND " +
	    								     "		  (t3.CODIGO = t1.UNIDAD)) AND " +
	    								     "		  (t0.PERSONA_ID = t1.PERSONA))) " +
	    								     "	ORDER BY t0.PRIMER_APELLIDO ASC, " +
	    								     "			 t0.SEGUNDO_APELLIDO ASC, " +
	    								     "			 t0.PRIMER_NOMBRE ASC, " +
	    								     "			 t0.SEGUNDO_NOMBRE ASC");
	            	
	    			query.setParameter(1, pUnidadId);
	            	query.setParameter(2, iTodos);
	            	query.setParameter(3, oCodigoSND);
	    	}
	    	
	        if (pNumRegistros<=pPaginaActual) pPaginaActual-=pNumRegistros;
	        pPaginaActual=pNumRegistros<=pPaginaActual ? 0: pPaginaActual;

	        query.setFirstResult(pPaginaActual);
	        query.setMaxResults(pTotalPorPagina);
	            	
	        List<Object[]> oFilasResultados=query.getResultList();
	        Object[] oFilaResultado;

	        List<ColVolPuesto> oColVolPuestos = new ArrayList<ColVolPuesto>();
	            	
	        for(int i=0;i<oFilasResultados.size();i++){
	        	ColVolPuesto oColVolPuesto = new ColVolPuesto();
	            oFilaResultado=oFilasResultados.get(i);
	            oColVolPuesto.setPuestoNotificacionId(((BigDecimal)oFilaResultado[0]).longValue());
	            oColVolPuesto.setClave((String)oFilaResultado[1]);

	            String iApellido2=(oFilaResultado[5]!=null && !((String)oFilaResultado[5]).isEmpty())?" "+(String)oFilaResultado[5]:"";
	            String iNombre2=(oFilaResultado[3]!=null && !((String)oFilaResultado[3]).isEmpty())?" "+(String)oFilaResultado[3]:"";

	            oColVolPuesto.setNombreColVol((String)oFilaResultado[2]+iNombre2+", "+(String)oFilaResultado[4]+iApellido2);
	            oColVolPuestos.add(oColVolPuesto);
	        }
	            	
	        return oColVolPuestos;
	        
	    } finally{
	    	em.close();
	    }		

	}
	
	@SuppressWarnings("unchecked")
	public List<ColVolPuesto> ListarColVolPorUnidad(long pUnidadId, boolean pSoloActivos) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();

    	BigDecimal iTodos = pSoloActivos?(new BigDecimal(0)):(new BigDecimal(1));
        
	    try {

	    	Query query = null;
    		query = em.createNativeQuery("WITH params AS (SELECT ? AS pUnidadId, ? AS pTodos, ? AS pCodigoSND FROM DUAL) " +
					     "SELECT t2.PUESTO_NOTIFICACION_ID AS a1, " +
					     "	     t2.CLAVE AS a2, t0.PRIMER_NOMBRE AS a3, t0.SEGUNDO_NOMBRE AS a4, t0.PRIMER_APELLIDO AS a5, t0.SEGUNDO_APELLIDO AS a6 " +
					     "	FROM GENERAL.UNIDADES t3, " +
					     "		 SIVE.PUESTOS_NOTIFICACION t2, " +
					     "		 SIVE.COLVOLS t1, " +
					     "		 SIS.SIS_PERSONAS t0 " +
					     "       INNER JOIN params p ON 1=1 " +
					     "	WHERE ((((t3.UNIDAD_ID = p.pUnidadId AND " +
					     "		  NOT (t2.COLVOL IS NULL)) AND " +
					     "		  ((p.pTodos = 1) OR ((p.pTodos = 0) AND " + 
					     "        ((t2.FECHA_CIERRE IS NULL) OR (t2.FECHA_CIERRE > TO_DATE(CURRENT_DATE)))))) AND " +
					     "		  (((t1.COLVOL_ID = t2.COLVOL) AND " +
					     "		  (t3.CODIGO = t1.UNIDAD)) AND " +
					     "		  (t0.PERSONA_ID = t1.PERSONA))) " +
					     "	ORDER BY t0.PRIMER_APELLIDO ASC, " +
					     "			 t0.SEGUNDO_APELLIDO ASC, " +
					     "			 t0.PRIMER_NOMBRE ASC, " +
					     "			 t0.SEGUNDO_NOMBRE ASC)");

	    	query.setParameter(1, pUnidadId);
            query.setParameter(2, iTodos);
	    	
	        List<Object[]> oFilasResultados=query.getResultList();
	        Object[] oFilaResultado;

	        List<ColVolPuesto> oColVolPuestos = new ArrayList<ColVolPuesto>();
	            	
	        for(int i=0;i<oFilasResultados.size();i++){
	        	ColVolPuesto oColVolPuesto = new ColVolPuesto();
	            oFilaResultado=oFilasResultados.get(i);
	            oColVolPuesto.setPuestoNotificacionId(((BigDecimal)oFilaResultado[0]).longValue());
	            oColVolPuesto.setClave((String)oFilaResultado[1]);

	            String iApellido2=(oFilaResultado[5]!=null && !((String)oFilaResultado[5]).isEmpty())?" "+(String)oFilaResultado[5]:"";
	            String iNombre2=(oFilaResultado[3]!=null && !((String)oFilaResultado[3]).isEmpty())?" "+(String)oFilaResultado[3]:"";

	            oColVolPuesto.setNombreColVol((String)oFilaResultado[2]+iNombre2+", "+(String)oFilaResultado[4]+iApellido2);
	            oColVolPuestos.add(oColVolPuesto);
	        }
	            	
	        return oColVolPuestos;
	        
	    } finally{
	    	em.close();
	    }		

	}

}
