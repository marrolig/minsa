// -----------------------------------------------
// SectorDA.java
// -----------------------------------------------
package ni.gob.minsa.malaria.datos.poblacion;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.poblacion.Sector;
import ni.gob.minsa.malaria.servicios.poblacion.SectorService;
import ni.gob.minsa.malaria.soporte.Mensajes;


/**
 * Acceso de Datos para la entidad {@link Sector}.
 * Por ser tabla centralizada, no se implementa la eliminación
 * actualización y creación de sectores.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 24/11/2010
 * @since jdk1.6.0_21
 */
public class SectorDA implements SectorService {

	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public SectorDA() {  // clase no instanciable
    }
    
    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.poblacion.SectorService#SectoresPorUnidad(long)
     */
    @SuppressWarnings("unchecked")
	@Override
	public List<Sector> SectoresPorUnidad(long pUnidadId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("sectoresPorUnidad");
            query.setParameter("pUnidadId", pUnidadId);
            //retorna el resultado del query
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }
	}

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.poblacion.SectorService#SectoresPorUnidadActivos(long)
     */
    @SuppressWarnings("unchecked")
	@Override
	public List<Sector> SectoresPorUnidadActivos(long pUnidadId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("sectoresPorUnidadActivos");
            query.setParameter("pUnidadId", pUnidadId);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }
    }

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.poblacion.SectorService#SectoresPorMunicipio(long)
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<Sector> SectoresPorMunicipio(long pMunicipioId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("sectoresPorMunicipio");
            query.setParameter("pMunicipioId", pMunicipioId);
            return(query.getResultList());
        }finally{
            em.close();
        }
	}

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.SectorService#Encontrar(long)
	 */
	@Override
	public InfoResultado Encontrar(long pSectorId) {

		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		Sector oSector = (Sector)oEM.find(ni.gob.minsa.malaria.modelo.poblacion.Sector.class, pSectorId);
    		if (oSector!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oSector);
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

}
