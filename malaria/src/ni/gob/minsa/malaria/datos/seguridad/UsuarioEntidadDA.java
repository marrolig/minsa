// -----------------------------------------------
// UsuarioEntidadDA.java
// -----------------------------------------------
package ni.gob.minsa.malaria.datos.seguridad;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.seguridad.UsuarioEntidad;
import ni.gob.minsa.malaria.soporte.Mensajes;


/**
 * Acceso de Datos para la entidad {@link ni.gob.minsa.modelo.seguridad.UsuarioEntidad}.
 * La persistencia es accedida mediante {@link ni.gob.minsa.datos.JPAResourceBean}.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.1, &nbsp; 19/03/2012
 * @since jdk1.6.0_21
 */
public class UsuarioEntidadDA implements ni.gob.minsa.malaria.servicios.poblacion.UsuarioEntidadService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public UsuarioEntidadDA() {  // clase no instanciable
    	
    }
   
    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.servicios.seguridad.UsuarioEntidadService#EntidadesPorUsuario(long)
     */
    @SuppressWarnings("unchecked")
    @Override
	public List<UsuarioEntidad> EntidadesPorUsuario(long pUsuarioId){
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("entidadesPorUsuario");
            query.setParameter("pUsuarioId", pUsuarioId);
            return query.getResultList();
        }finally{
            em.close();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.servicios.seguridad.UsuarioEntidadService#Encontrar(long)
     */
    @Override
    public InfoResultado Encontrar(long pUsuarioEntidadId) {
        
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		UsuarioEntidad oUsuarioEntidad = (UsuarioEntidad)oEM.find(ni.gob.minsa.malaria.modelo.seguridad.UsuarioEntidad.class, pUsuarioEntidadId);
    		if (oUsuarioEntidad!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oUsuarioEntidad);
    			return oResultado;
    		}
    		else {
    			oResultado.setMensaje(Mensajes.ENCONTRAR_REGISTRO_NO_EXISTE);
    			oResultado.setOk(false);
    			oResultado.setFilasAfectadas(0);
    			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
    			return oResultado;    		}
    	}
    	catch (Exception iExcepcion) {
    		oResultado.setExcepcion(true);
    		oResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO + iExcepcion.getMessage());
    		oResultado.setFuenteError(iExcepcion.toString().split(":",1).toString());
    		oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
    		oResultado.setOk(false);
    		oResultado.setFilasAfectadas(0);
    		return oResultado;
    	}
    	finally{
    		oEM.close();
    	}
    }
    
}
