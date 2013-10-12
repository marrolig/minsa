/**
 * DivisionPoliticaDA.java
 */
package ni.gob.minsa.malaria.datos.poblacion;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * Acceso de Datos para la entidad {@link ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica}.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 13/03/2013
 * @since jdk1.6.0_21
 * 
 */
public class DivisionPoliticaDA implements DivisionPoliticaService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public DivisionPoliticaDA() {  // clase no instanciable
    }

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService#DepartamentosActivos()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DivisionPolitica> DepartamentosActivos() {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("departamentosActivos");
            //retorna el resultado del query
            query.setParameter("pDivisionPoliticaId", 0);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService#DepartamentosActivos(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DivisionPolitica> DepartamentosActivos(Long pDivisionPoliticaId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("departamentosActivos");
            //retorna el resultado del query
            query.setParameter("pDivisionPoliticaId", pDivisionPoliticaId);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService#MunicipiosActivos(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DivisionPolitica> MunicipiosActivos(Long pDepartamentoId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("municipiosActivos");
            query.setParameter("pDepartamentoId", pDepartamentoId);
            query.setParameter("pMunicipioId", 0);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService#MunicipiosActivos(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DivisionPolitica> MunicipiosActivos(Long pDepartamentoId,
			Long pMunicipioId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("municipiosActivos");
            //retorna el resultado del query
            query.setParameter("pDepartamentoId", pDepartamentoId);
            query.setParameter("pMunicipioId", pMunicipioId);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService#DepartamentosTodos()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DivisionPolitica> DepartamentosTodos() {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("departamentosTodos");
            //retorna el resultado del query
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService#MunicipiosTodos(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DivisionPolitica> MunicipiosTodos(Long pDepartamentoId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("MunicipiosTodos");
            //retorna el resultado del query
            query.setParameter("pDepartamentoId", pDepartamentoId);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService#Encontrar(long)
	 */
	@Override
	public InfoResultado Encontrar(long pDivisionPoliticaId) {
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		DivisionPolitica oDivisionPolitica = (DivisionPolitica)oEM.find(DivisionPolitica.class, pDivisionPoliticaId);
    		if (oDivisionPolitica!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oDivisionPolitica);
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
	
	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService#EncontrarPorCodigoNacional(long)
	 */
	@Override
	public InfoResultado EncontrarPorCodigoNacional(String pCodigoNacional) {
        EntityManager oEM = jpaResourceBean.getEMF().createEntityManager();
        InfoResultado oResultado=new InfoResultado();
        try{
            Query query = oEM.createNamedQuery("divisionPorCodigoNacional");
            query.setParameter("pCodigoNacional", pCodigoNacional);
            query.setMaxResults(1);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            //retorna el resultado del query
            oResultado.setObjeto((DivisionPolitica)query.getSingleResult());
			oResultado.setFilasAfectadas(1);
			oResultado.setOk(true);
            return oResultado;
        }catch (NoResultException iExcepcion) {
        	oResultado.setOk(true);
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
	 * @see ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService#EncontrarPorCodigoISO(java.lang.String)
	 */
	@Override
	public InfoResultado EncontrarPorCodigoISO(String pCodigoIso) {
        EntityManager oEM = jpaResourceBean.getEMF().createEntityManager();
        InfoResultado oResultado=new InfoResultado();
        try{
            Query query = oEM.createNamedQuery("divisionPorCodigoISO");
            query.setParameter("pCodigoIso", pCodigoIso);
            query.setMaxResults(1);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            //retorna el resultado del query
            oResultado.setObjeto((DivisionPolitica)query.getSingleResult());
			oResultado.setFilasAfectadas(1);
			oResultado.setOk(true);
            return oResultado;
        }catch (NoResultException iExcepcion) {
        	oResultado.setOk(true);
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
}
