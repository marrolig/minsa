package ni.gob.minsa.malaria.datos.supervision;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.supervision.VisitaPuesto;
import ni.gob.minsa.malaria.reglas.VisitaPuestoValidacion;
import ni.gob.minsa.malaria.servicios.supervision.VisitaPuestoService;
import ni.gob.minsa.malaria.soporte.Mensajes;

public class VisitaPuestoDA implements VisitaPuestoService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();
    
    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.supervision.VisitaPuestoService#Encontrar(long)
     */
	@Override
	public InfoResultado Encontrar(long pVisitaPuestoId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
			VisitaPuesto oVista = (VisitaPuesto) oEM.find(VisitaPuesto.class, pVisitaPuestoId);
			if (oVista!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oVista);
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
	
	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.supervision.VisitaPuestoService#EncontrarPorEntidadAñoEpiYMunicipio(long, int, java.lang.Long, int, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VisitaPuesto>  ListarPorEntidadAñoEpiYMunicipio(
			long pEntidadAdtvaId, int pAñoEpi, Long pMunicipioId,
			int pPaginaActual, int pTotalPorPagina, int pNumRegistros) {
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
		try {
			Query query = em.createNamedQuery("VisitaPuesto.listarPorEntidadAñoEpiYMunicipio");
			query.setParameter("pEntidadAdtvaId", pEntidadAdtvaId);
			query.setParameter("pAñoEpi",pAñoEpi);
	    	query.setParameter("pMunicipioId", pMunicipioId!=null ? pMunicipioId : 0);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            
            if (pNumRegistros<=pPaginaActual) pPaginaActual-=pNumRegistros;
            pPaginaActual=pNumRegistros<=pPaginaActual ? 0: pPaginaActual;
            query.setFirstResult(pPaginaActual);
            query.setMaxResults(pTotalPorPagina);
			return (query.getResultList());
		} finally {
			em.close();
		}
	}
	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.supervision.VisitaPuestoService#ContarPositivosPorUnidad(long, int, java.lang.Long)
	 */
	@Override
	public int ContarPositivosPorUnidad(long pEntidadAdtvaId, int pAñoEpi,
			Long pMunicipioId) {
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        int totalVisitas=0; 
        try{
        	Query query = em.createQuery("select count(tvp) from VisitaPuesto tvp " 
						+ "where tvp.entidadAdtva.entidadAdtvaId=:pEntidadAdtvaId and " 
						+ "tvp.añoEpidemiologico=:pAñoEpi and (tvp.municipio.divisionPoliticaId=:pMunicipioId or 0=:pMunicipioId) ");
	        query.setParameter("pEntidadAdtvaId", pEntidadAdtvaId);
			query.setParameter("pAñoEpi",pAñoEpi);
	    	query.setParameter("pMunicipioId", pMunicipioId!=null ? pMunicipioId : 0);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        	totalVisitas = ((Long)query.getSingleResult()).intValue();
        }finally{
        	em.close();
        }
        
        return totalVisitas;       
	}
	
	@Override
	public InfoResultado Guardar(VisitaPuesto pVisitaPuesto) {
		InfoResultado oResultado=new InfoResultado();
		oResultado=VisitaPuestoValidacion.validarVisitaPuesto(pVisitaPuesto);
		if (!oResultado.isOk()) return oResultado;
		
		EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
		
		try{
			VisitaPuesto oDetachedVisitaPuesto= (VisitaPuesto) oEM.find(VisitaPuesto.class, pVisitaPuesto.getVisitaPuestoId());
			VisitaPuesto oVisitaPuesto = oEM.merge(oDetachedVisitaPuesto);
			oVisitaPuesto.setDivulgacion(pVisitaPuesto.getDivulgacion());
			oVisitaPuesto.setVisibleCarnet(pVisitaPuesto.getVisibleCarnet());
			oVisitaPuesto.setAtencionPacientes(pVisitaPuesto.getAtencionPacientes());
			oVisitaPuesto.setReconocido(pVisitaPuesto.getReconocido());
			oVisitaPuesto.setHorarioInicio(pVisitaPuesto.getHorarioInicio());
			oVisitaPuesto.setHorarioFin(pVisitaPuesto.getHorarioFin());
			oVisitaPuesto.setTomaMuestras(pVisitaPuesto.getTomaMuestras());
			oVisitaPuesto.setStock(pVisitaPuesto.getStock());
			oVisitaPuesto.setProximaVisita(pVisitaPuesto.getProximaVisita());
			
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
	public InfoResultado Agregar(VisitaPuesto pVisitaPuesto) {
		InfoResultado oResultado=new InfoResultado();
		oResultado=VisitaPuestoValidacion.validarVisitaPuesto(pVisitaPuesto);
		if (!oResultado.isOk()) return oResultado;
		
		EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
            oEM.persist(pVisitaPuesto);
            oEM.getTransaction().commit();
            oResultado.setObjeto(pVisitaPuesto);
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
	
	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.supervision.VisitaPuestoService#Eliminar(long)
	 */
	@Override
	public InfoResultado Eliminar(long pVisitaPuestoId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		VisitaPuesto oVisita = (VisitaPuesto)oEM.find(VisitaPuesto.class, pVisitaPuestoId);
    		if (oVisita!=null) {
    			oEM.remove(oVisita);
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

	

}
