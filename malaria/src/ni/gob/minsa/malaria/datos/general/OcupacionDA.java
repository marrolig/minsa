// -----------------------------------------------
// OcupacionDA.java
// -----------------------------------------------
package ni.gob.minsa.malaria.datos.general;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.general.Ocupacion;
import ni.gob.minsa.malaria.servicios.general.OcupacionService;
import ni.gob.minsa.malaria.soporte.Mensajes;


/**
 * Acceso de Datos para la entidad {@link Ocupacion}.
 * Por ser tabla centralizada, no se implementa la eliminación
 * actualización y creación de tipos de unidades.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 23/07/2012
 * @since jdk1.6.0_21
 */
public class OcupacionDA implements OcupacionService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public OcupacionDA() {  // clase no instanciable
    }
    
    @Override
    public InfoResultado Encontrar(long pOcupacionId) {
        
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		Ocupacion oOcupacion = (Ocupacion)oEM.find(Ocupacion.class, pOcupacionId);
    		if (oOcupacion!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oOcupacion);
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
    
	@SuppressWarnings("unchecked")
	@Override
	public List<Ocupacion> ListarActivos(long pOcupacion) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("ocupacion.listarActivos");
            query.setParameter("pOcupacion", pOcupacion);
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ocupacion> ListarActivos() {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("ocupacion.listarActivos");
            query.setParameter("pOcupacion", 0);
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}
	
	@SuppressWarnings("unchecked")
	public List<Ocupacion> ListarPorNombre(String pNombre) {

		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("ocupacion.listarPorNombre");
            query.setParameter("pNombre", "%" + pNombre.toUpperCase()+ "%");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

}
