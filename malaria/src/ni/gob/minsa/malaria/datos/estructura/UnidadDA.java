// -----------------------------------------------
// UnidadDA.java
// -----------------------------------------------
package ni.gob.minsa.malaria.datos.estructura;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.soporte.Mensajes;

import org.eclipse.persistence.config.QueryHints;



/**
 * Acceso de Datos para la entidad {@link ni.gob.minsa.malaria.modelo.estructura.Unidad}.
 * Por ser tabla centralizada, no se implementa la eliminación
 * actualización y creación de unidades.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 18/09/2010
 * @since jdk1.6.0_21
 */
public class UnidadDA implements ni.gob.minsa.malaria.servicios.estructura.UnidadService{
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public UnidadDA() {  // clase no instanciable
    }
    
    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.general.UnidadService#Encontrar(long)
     */
    @Override
    public InfoResultado Encontrar(long pUnidadId) {
        
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		Unidad oUnidad = (Unidad)oEM.find(ni.gob.minsa.malaria.modelo.estructura.Unidad.class, pUnidadId);
    		oEM.refresh(oUnidad);
    		if (oUnidad!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oUnidad);
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
     * @see ni.gob.minsa.malaria.servicios.general.UnidadService#UnidadesActivas()
     */
    @SuppressWarnings("unchecked")
	@Override
	public List<Unidad> UnidadesActivas() {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("unidadesActivas");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.general.UnidadService#UnidadesActivasPorEntidadAdtva(long)
     */
    @SuppressWarnings("unchecked")
    @Override
	public List<Unidad> UnidadesActivasPorEntidadAdtva(long pEntidadId){
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("unidadesActivasPorEntidadAdtva");
            query.setParameter("pEntidadId", pEntidadId);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setHint(QueryHints.FETCH, "tu.categoriaUnidad");
            query.setHint(QueryHints.FETCH, "tu.entidadAdtva");
            query.setHint(QueryHints.FETCH, "tu.municipio");
            query.setHint(QueryHints.FETCH, "tu.regimen");
            query.setHint(QueryHints.FETCH, "tu.tipoUnidad");
            return(query.getResultList());
        }finally{
            em.close();
        }
    }

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.general.UnidadService#UnidadesActivasPorEntidadYTipo(long, long)
     */
    @SuppressWarnings("unchecked")
    @Override
	public List<Unidad> UnidadesActivasPorEntidadYTipo(long pEntidadId,long pTipoUnidadId){
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
        	
        	Query query=null;
        	
        	if (pTipoUnidadId!=0) {
        		query = em.createNamedQuery("unidadesActivasPorEntidadYTipo");
        	} else {
        		query = em.createNamedQuery("unidadesActivasPorEntidadAdtva");
        	}
        	query.setParameter("pEntidadId", pEntidadId);
        	if (pTipoUnidadId!=0) {
        		query.setParameter("pTipoUnidadId", pTipoUnidadId);
        	}
        	query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        	query.setHint(QueryHints.FETCH, "tu.categoriaUnidad");
        	query.setHint(QueryHints.FETCH, "tu.entidadAdtva");
        	query.setHint(QueryHints.FETCH, "tu.municipio");
        	query.setHint(QueryHints.FETCH, "tu.regimen");
        	query.setHint(QueryHints.FETCH, "tu.tipoUnidad");
        	//retorna el resultado del query
        	return(query.getResultList());
        }finally{
            em.close();
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public List<Unidad> UnidadesActivasPorEntidadYCategoria(long pEntidadId,BigDecimal pCategoriaUnidad){
    	 EntityManager em = jpaResourceBean.getEMF().createEntityManager();
         try{
         	
         	Query query = em.createNamedQuery("unidadesActivasPorEntidadYCategoria");
         	query.setParameter("pEntidadId", pEntidadId);
         	query.setParameter("pCategoriaUnidad", pCategoriaUnidad);
         	
         	query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         	query.setHint(QueryHints.FETCH, "tu.categoriaUnidad");
         	query.setHint(QueryHints.FETCH, "tu.entidadAdtva");
         	query.setHint(QueryHints.FETCH, "tu.municipio");
         	query.setHint(QueryHints.FETCH, "tu.regimen");
         	query.setHint(QueryHints.FETCH, "tu.tipoUnidad");
         	//retorna el resultado del query
         	return(query.getResultList());
         }finally{
             em.close();
         }
	}
    
    @SuppressWarnings("unchecked")
	@Override
    public List<Unidad>UnidadesActivasPorPropiedad(String pPropiedad){
    	if(pPropiedad==null || pPropiedad.trim().equals("")) return null;
    	EntityManager em = jpaResourceBean.getEMF().createEntityManager();
         try{
         	
         	Query	query = em.createNamedQuery("UnidadesActivasPorPropiedad");
         	query.setParameter("pPropiedad", pPropiedad);
   
         	query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         	query.setHint(QueryHints.FETCH, "tu.categoriaUnidad");
         	query.setHint(QueryHints.FETCH, "tu.entidadAdtva");
         	query.setHint(QueryHints.FETCH, "tu.municipio");
         	query.setHint(QueryHints.FETCH, "tu.regimen");
         	query.setHint(QueryHints.FETCH, "tu.tipoUnidad");
         	//retorna el resultado del query
         	return(query.getResultList());
         }finally{
             em.close();
         }
    }
    
	@Override
	public int ContarUnidadesPorMunicipio(long pMunicipioId, long pTipoUnidadId, String pNombre) {
        
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        
        int totalUnidades=0;
        
        if ((pNombre!=null) && (pNombre.trim().length()>0)) {
        	Query query = em.createQuery("select count(tu) from Unidad tu " +
											"where tu.municipio.divisionPoliticaId=:pMunicipioId and " +
        									"      tu.pasivo='0' AND " +
											"      (:pTipoUnidadId=0 or tu.tipoUnidad.tipoUnidadId=:pTipoUnidadId) and " +
											"      UPPER(tu.nombre) LIKE :pNombre");
            query.setParameter("pMunicipioId", pMunicipioId);
            query.setParameter("pTipoUnidadId", pTipoUnidadId);
            query.setParameter("pNombre", "%" + pNombre.toUpperCase() + "%");
        	totalUnidades = ((Long)query.getSingleResult()).intValue();
        } else {
        	Query query = em.createQuery("select count(tu) from Unidad tu " + 
        								    "where tu.municipio.divisionPoliticaId=:pMunicipioId and " +
        								    "      tu.pasivo='0' AND " +
        								    "      (:pTipoUnidadId is null or tu.tipoUnidadId=:pTipoUnidadId)");
        	query.setParameter("pMunicipioId", pMunicipioId);
        	query.setParameter("pTipoUnidadId", pTipoUnidadId);
        	totalUnidades = ((Long)query.getSingleResult()).intValue();
        }
        
        return totalUnidades;
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<Unidad> UnidadesPorMunicipio(long pMunicipioId,
			long pTipoUnidadId, String pNombre, int pPaginaActual,
			int pTotalPorPagina, int pNumRegistros) {

		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
        	Query query = null;
        	if (pNombre==null) {
        		query = em.createNamedQuery("unidadesActivasPorMunicipio");
        		query.setParameter("pMunicipioId", pMunicipioId);
        		query.setParameter("pTipoUnidadId", pTipoUnidadId);
        	} else {
        		query= em.createNamedQuery("unidadesActivasPorMunicipioYNombre");
                query.setParameter("pMunicipioId", pMunicipioId);
                query.setParameter("pTipoUnidadId", pTipoUnidadId);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Unidad> UnidadesPorNombre(String pNombre, long pEntidadAdtvaId,
			long pMunicipioId, String pPasivo) {

		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("unidadesPorNombre");
            query.setParameter("pEntidadAdtvaId", pEntidadAdtvaId);
            query.setParameter("pMunicipioId", pMunicipioId);
            query.setParameter("pPasivo", pPasivo);
            query.setParameter("pNombre", "%" + pNombre.toUpperCase()+ "%");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}


}
