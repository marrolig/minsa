package ni.gob.minsa.malaria.datos.estructura;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.soporte.Mensajes;


/**
 * Acceso de Datos para la entidad {@link ni.gob.minsa.malaria.modelo.estructura.modelo.general.EntidadAdtva}.
 * Por ser tabla centralizada, no se implementa la eliminación
 * actualización y creación de entidades administrativas.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 28/10/2010
 * @since jdk1.6.0_21
 */
public class EntidadAdtvaDA implements ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public EntidadAdtvaDA() {  // clase no instanciable
    }

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.general.EntidadAdtvaService#Encontrar(long)
     */
    @Override
    public InfoResultado Encontrar(long pEntidadAdtvaId) {
        
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		EntidadAdtva oEntidadAdtva = (EntidadAdtva)oEM.find(ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva.class, pEntidadAdtvaId);
    		if (oEntidadAdtva!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oEntidadAdtva);
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
     * @see ni.gob.minsa.malaria.servicios.general.EntidadAdtvaService#EntidadesAdtvasActivas()
     */
    @SuppressWarnings("unchecked")
	@Override
	public List<EntidadAdtva> EntidadesAdtvasActivas() {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("entidadesAdtvasActivas");
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}
    
}
