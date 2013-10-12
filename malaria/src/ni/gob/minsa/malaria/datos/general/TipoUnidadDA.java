// -----------------------------------------------
// TipoUnidadDA.java
// -----------------------------------------------
package ni.gob.minsa.malaria.datos.general;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.general.TipoUnidad;
import ni.gob.minsa.malaria.soporte.Mensajes;


/**
 * Acceso de Datos para la entidad {@link TipoUnidad}.
 * Por ser tabla centralizada, no se implementa la eliminación
 * actualización y creación de tipos de unidades.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 01/10/2010
 * @since jdk1.6.0_21
 */
public class TipoUnidadDA implements ni.gob.minsa.malaria.servicios.general.TipoUnidadService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public TipoUnidadDA() {  // clase no instanciable
    }
    
    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.general.TipoUnidadService#Encontrar(long)
     */
    @Override
    public InfoResultado Encontrar(long pTipoUnidadId) {
        
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		TipoUnidad oTipoUnidad = (TipoUnidad)oEM.find(ni.gob.minsa.malaria.modelo.general.TipoUnidad.class, pTipoUnidadId);
    		if (oTipoUnidad!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oTipoUnidad);
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
     * @see ni.gob.minsa.malaria.servicios.general.TipoUnidadService#TiposUnidadesActivas()
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoUnidad> TiposUnidadesActivas() {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("tiposUnidadesActivas");
            //retorna el resultado del query
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.general.TipoUnidadService#TiposPorEntidadConActividad(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoUnidad> TiposPorEntidadConActividad(long pEntidadId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("tiposPorEntidadConActividad");
            query.setParameter("pEntidadId", pEntidadId);
            //retorna el resultado del query
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.general.TipoUnidadService#TiposPorEntidadConMeta(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoUnidad> TiposPorEntidadConMeta(long pEntidadId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("tiposPorEntidadConMeta");
            query.setParameter("pEntidadId", pEntidadId);
            //retorna el resultado del query
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.general.TipoUnidadService#TiposPorEntidadConInventario(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoUnidad> TiposPorEntidadConInventario(long pEntidadId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("tiposPorEntidadConInventario");
            query.setParameter("pEntidadId", pEntidadId);
            //retorna el resultado del query
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}
}
