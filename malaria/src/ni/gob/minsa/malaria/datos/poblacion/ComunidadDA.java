// -----------------------------------------------
// ComunidadDA.java
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
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.Sector;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * Acceso de Datos para la entidad {@link ni.gob.minsa.modelo.poblacion.Comunidad}.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 07/09/2011
 * @since jdk1.6.0_21
 */
public class ComunidadDA implements ComunidadService {

	
	
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public ComunidadDA() {  // clase no instanciable
    }

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.ComunidadService#ComunidadesPorMunicipioNombre(java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<Comunidad> ComunidadesPorMunicipioNombre(String pCodMunicipio,
			String pNombre, Integer pRegistros) {
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
		Query query = null;
		 
		try{
		    query = em.createQuery("select tc from Comunidad tc " +
					" where tc.sector.municipio.codigoNacional=:pCodMunicipio " +
					" and UPPER(tc.nombre) LIKE :pNombre " +
					" order by tc.nombre");
			query.setParameter("pCodMunicipio", pCodMunicipio);
			query.setParameter("pNombre", "%" + pNombre.toUpperCase() + "%");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			query.setMaxResults(pRegistros);
			return(query.getResultList());
		}finally{
		    em.close();
		}	
	}

    
    /* (non-Javadoc)
	 * @see ni.gob.minsa.servicios.poblacion.ComunidadService#ComunidadesPorSector(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Comunidad> ComunidadesPorSector(long pSectorId, int pPaginaActual, int pTotalPorPagina, int pNumRegistros) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("comunidadesPorSector");
            query.setParameter("pSectorId", pSectorId);
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

	/* (non-Javadoc)
	 * @see ni.gob.minsa.servicios.poblacion.ComunidadService#ComunidadesPorSectorActivos(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Comunidad> ComunidadesPorSectorActivos(long pSectorId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("comunidadesPorSectorActivos");
            query.setParameter("pSectorId", pSectorId);
            //retorna el resultado del query
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.servicios.poblacion.ComunidadService#ComunidadesPorMunicipio(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Comunidad> ComunidadesPorMunicipio(long pMunicipioId, int pPaginaActual, int pTotalPorPagina, int pNumRegistros) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        
        try{
            Query query = em.createNamedQuery("comunidadesPorMunicipio");	
            query.setParameter("pMunicipioId", pMunicipioId);
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

	/* (non-Javadoc)
	 * @see ni.gob.minsa.servicios.poblacion.ComunidadService#ComunidadesPorMunicipioActivos(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Comunidad> ComunidadesPorMunicipioActivos(long pMunicipioId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("comunidadesPorMunicipioActivos");
            query.setParameter("pMunicipioId", pMunicipioId);
            //retorna el resultado del query
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.servicios.poblacion.ComunidadService#Encontrar(long)
	 */
	@Override
	public InfoResultado Encontrar(long pComunidadId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		Comunidad oComunidad = (Comunidad)oEM.find(ni.gob.minsa.malaria.modelo.poblacion.Comunidad.class, pComunidadId);
    		if (oComunidad!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oComunidad);
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
	 * @see ni.gob.minsa.servicios.poblacion.ComunidadService#Guardar(ni.gob.minsa.modelo.poblacion.Comunidad)
	 */
	@Override
	public InfoResultado Guardar(Comunidad pComunidad) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
        	Comunidad oDetachedComunidad = (Comunidad)oEM.find(ni.gob.minsa.malaria.modelo.poblacion.Comunidad.class, pComunidad.getComunidadId());
        	Comunidad oComunidad=oEM.merge(oDetachedComunidad);
        	oComunidad.setNombre(pComunidad.getNombre());
        	oComunidad.setReferencias(pComunidad.getReferencias());
        	oComunidad.setCaracteristicas(pComunidad.getCaracteristicas());
        	oComunidad.setTipoArea(pComunidad.getTipoArea());
        	oComunidad.setPasivo(pComunidad.getPasivo());
        	oComunidad.setLatitud(pComunidad.getLatitud());
        	oComunidad.setLongitud(pComunidad.getLongitud());

        	Sector oSector=(Sector)oEM.find(ni.gob.minsa.malaria.modelo.poblacion.Sector.class, pComunidad.getSector().getSectorId());
        	oComunidad.setSector(oSector);
        	
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
    		
    	} catch (PersistenceException iExPersistencia) {
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

	/* (non-Javadoc)
	 * @see ni.gob.minsa.servicios.poblacion.ComunidadService#Agregar(ni.gob.minsa.modelo.poblacion.Comunidad)
	 */
	@Override
	public InfoResultado Agregar(Comunidad pComunidad) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
		@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
            oEM.persist(pComunidad);
            oEM.getTransaction().commit();
            oResultado.setObjeto(pComunidad);
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
	 * @see ni.gob.minsa.servicios.poblacion.ComunidadService#Eliminar(long)
	 */
	@Override
	public InfoResultado Eliminar(long pComunidadId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		Comunidad oComunidad = (Comunidad)oEM.find(ni.gob.minsa.malaria.modelo.poblacion.Comunidad.class, pComunidadId);
    		if (oComunidad!=null) {
    			oEM.remove(oComunidad);
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
	public List<Comunidad> ComunidadesPorNombre(long pMunicipioId,
			long pSectorId, String pNombre, int pPaginaActual, 
			int pTotalPorPagina, int pNumRegistros) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("comunidadesPorNombre");
            query.setParameter("pMunicipioId", pMunicipioId);
            query.setParameter("pSectorId", pSectorId);
            query.setParameter("pNombre", "%" + pNombre.toUpperCase() + "%");
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
	public int ContarComunidades(long pMunicipioId, long pSectorId, String pNombre) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        int totalComunidades=0;
        
        if ((pNombre!=null) && (pNombre.trim().length()>0)) {
        	Query query = em.createQuery("select count(tc) from Comunidad tc " +
											"where tc.sector.municipio.divisionPoliticaId=:pMunicipioId and " +
											"      (:pSectorId=0 or tc.sector.sectorId=:pSectorId) and " +
											"      UPPER(tc.nombre) LIKE :pNombre " +
											"order by tc.nombre");
            query.setParameter("pMunicipioId", pMunicipioId);
            query.setParameter("pSectorId", pSectorId);
            query.setParameter("pNombre", "%" + pNombre.toUpperCase() + "%");
        	totalComunidades = ((Long)query.getSingleResult()).intValue();
        } else {
        
        	if (pSectorId==0) {
        		Query query = em.createQuery("select count(tc) from Comunidad tc where tc.sector.municipio.divisionPoliticaId=:pMunicipioId");
        		query.setParameter("pMunicipioId", pMunicipioId);
        		totalComunidades = ((Long)query.getSingleResult()).intValue();
        	} else {
        		Query query = em.createQuery("select count(tc) from Comunidad tc where tc.sector.municipio.divisionPoliticaId=:pMunicipioId and tc.sector.sectorId=:pSectorId");
        		query.setParameter("pMunicipioId", pMunicipioId);
        		query.setParameter("pSectorId", pSectorId);
        		totalComunidades = ((Long)query.getSingleResult()).intValue();
        	}
        }
        
        return totalComunidades;	
    }

	@Override
	public int ContarComunidadesPorUnidad(long pUnidadId, String pNombre, String pTipoArea) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        int totalComunidades=0;
        
        if ((pNombre!=null) && (pNombre.trim().length()>0)) {
        	Query query = em.createQuery("select count(tc) from Comunidad tc " +
											"where tc.sector.unidad.unidadId=:pUnidadId and " +
											"      (:pTipoArea is null or tc.tipoArea=:pTipoArea) and " +
											"      UPPER(tc.nombre) LIKE :pNombre");
            query.setParameter("pUnidadId", pUnidadId);
            query.setParameter("pTipoArea", pTipoArea);
            query.setParameter("pNombre", "%" + pNombre.toUpperCase() + "%");
        	totalComunidades = ((Long)query.getSingleResult()).intValue();
        } else {
        	Query query = em.createQuery("select count(tc) from Comunidad tc " + 
        								    "where tc.sector.unidad.unidadId=:pUnidadId and " +
        								    "      (:pTipoArea is null or tc.tipoArea=:pTipoArea)");
        	query.setParameter("pUnidadId", pUnidadId);
        	query.setParameter("pTipoArea", pTipoArea);
        	totalComunidades = ((Long)query.getSingleResult()).intValue();
        }
        
        return totalComunidades;	
    }

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.servicios.poblacion.ComunidadService#Encontrar(java.lang.String)
	 */
	@Override
	public InfoResultado Encontrar(String pCodigo) {
        EntityManager oEM = jpaResourceBean.getEMF().createEntityManager();
        InfoResultado oResultado=new InfoResultado();
        try{
            Query query = oEM.createNamedQuery("comunidadPorCodigo");
            query.setParameter("pCodigo", pCodigo);
            query.setMaxResults(1);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            oResultado.setObjeto((Comunidad)query.getSingleResult());
			oResultado.setFilasAfectadas(1);
			oResultado.setOk(true);
            return oResultado;
        }catch (NoResultException iExcepcion) {
        	return null;
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
	}

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.ComunidadService#ComunidadesPorUnidadYNombre(long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Comunidad> ComunidadesPorUnidadYNombre(long pUnidadId,
			String pNombre) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("comunidadesActivasPorUnidadYNombre");
            query.setParameter("pUnidadId", pUnidadId);
            query.setParameter("pTipoArea", null);
            query.setParameter("pNombre", "%" + pNombre.toUpperCase() + "%");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Comunidad> ComunidadesPorUnidad(long pUnidadId, String pNombre, String pTipoArea, 
			int pPaginaActual, int pTotalPorPagina, int pNumRegistros) {

		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
        	Query query = null;
        	if (pNombre==null) {
        		query = em.createNamedQuery("comunidadesActivasPorUnidad");
        		query.setParameter("pUnidadId", pUnidadId);
        		query.setParameter("pTipoArea", pTipoArea);
        	} else {
        		query= em.createNamedQuery("comunidadesActivasPorUnidadYNombre");
                query.setParameter("pUnidadId", pUnidadId);
                query.setParameter("pTipoArea", pTipoArea);
                query.setParameter("pNombre", "%" + pNombre.toUpperCase() + "%");
        	}
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

}
