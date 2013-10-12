// -----------------------------------------------
// MarcadorDA.java
// -----------------------------------------------
package ni.gob.minsa.malaria.datos.general;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.general.Marcador;
import ni.gob.minsa.malaria.servicios.general.MarcadorService;
import ni.gob.minsa.malaria.soporte.Mensajes;


/**
 * Acceso de Datos para la entidad {@link Marcador}.
 * Por ser tabla centralizada, no se implementa la eliminación
 * actualización y creación de marcadores.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 18/06/2012
 * @since jdk1.6.0_21
 */
public class MarcadorDA implements MarcadorService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public MarcadorDA() {  // clase no instanciable
    }
    
    @Override
    public InfoResultado Encontrar(long pMarcadorId) {
        
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		Marcador oMarcador = (Marcador)oEM.find(Marcador.class, pMarcadorId);
    		if (oMarcador!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oMarcador);
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
	public List<Marcador> listarMarcadores() {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("marcador.listar");
            //retorna el resultado del query
            return(query.getResultList());
        }finally{
            em.close();
        }		
	}

	@Override
	public InfoResultado Encontrar(String pCodigo) {
        EntityManager oEM = jpaResourceBean.getEMF().createEntityManager();
        InfoResultado oResultado=new InfoResultado();
        try{
            Query query = oEM.createNamedQuery("marcador.encontrarPorCodigo");
            query.setParameter("pCodigo", pCodigo);
            query.setMaxResults(1);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            oResultado.setObjeto((Marcador)query.getSingleResult());
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

}
