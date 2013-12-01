package ni.gob.minsa.malaria.datos.seguridad;

import java.util.List;

import javax.faces.bean.NoneScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.eclipse.persistence.config.QueryHints;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.seguridad.UsuarioUnidad;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * Acceso de Datos para la entidad {@link UsuarioUnidad}.
 * La persistencia es accedida mediante {@link JPAResourceBean}
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.1, &nbsp; 19/03/2012
 * @since jdk1.6.0_21
 */
@NoneScoped
public class UsuarioUnidadDA implements ni.gob.minsa.malaria.servicios.poblacion.UsuarioUnidadService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();
    
    public UsuarioUnidadDA() {  // clase no instanciable
    }

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.servicios.seguridad.UsuarioUnidadService#UnidadesPorUsuario(long, long)
     */
    @SuppressWarnings("unchecked")
    @Override
	public List<UsuarioUnidad> UnidadesPorUsuario(long pUsuarioId){
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("unidadesPorUsuario");
            query.setParameter("pUsuarioId", pUsuarioId);
            query.setHint(QueryHints.FETCH, "tuu.usuario");
            query.setHint(QueryHints.FETCH, "tuu.unidad");
            query.setHint(QueryHints.FETCH, "tuu.unidad.categoriaUnidad");
            query.setHint(QueryHints.FETCH, "tuu.unidad.entidadAdtva");
            query.setHint(QueryHints.FETCH, "tuu.unidad.municipio");
            query.setHint(QueryHints.FETCH, "tuu.unidad.regimen");
            query.setHint(QueryHints.FETCH, "tuu.unidad.tipoUnidad");
            return query.getResultList();
        }finally{
            em.close();
        }
    }

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.servicios.seguridad.UsuarioUnidadService#Encontrar(long)
     */
    @Override
    public InfoResultado Encontrar(long pUsuarioUnidadId) {
        
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		UsuarioUnidad oUsuarioUnidad = (UsuarioUnidad)oEM.find(ni.gob.minsa.malaria.modelo.seguridad.UsuarioUnidad.class, pUsuarioUnidadId);
    		if (oUsuarioUnidad!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oUsuarioUnidad);
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
    
    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.servicios.seguridad.UsuarioUnidadService#UnidadesPorUsuario(long, long, long, long)
     */
    @SuppressWarnings("unchecked")
	@Override
	public List<UsuarioUnidad> UnidadesPorUsuario(long pUsuarioId,
			long pEntidadId, long pTipoUnidadId) {

		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("unidadesPorUsuarioEntidadYTipo");
            query.setParameter("pUsuarioId", pUsuarioId);
            query.setParameter("pEntidadId", pEntidadId);
            query.setParameter("pTipoUnidadId", pTipoUnidadId);
            query.setHint(QueryHints.FETCH, "tuu.usuario");
            query.setHint(QueryHints.FETCH, "tuu.unidad");
            query.setHint(QueryHints.FETCH, "tuu.unidad.categoriaUnidad");
            query.setHint(QueryHints.FETCH, "tuu.unidad.entidadAdtva");
            query.setHint(QueryHints.FETCH, "tuu.unidad.entidadAdtva.municipio");
            query.setHint(QueryHints.FETCH, "tuu.unidad.municipio");
            query.setHint(QueryHints.FETCH, "tuu.unidad.municipio.departamento");
            query.setHint(QueryHints.FETCH, "tuu.unidad.regimen");
            query.setHint(QueryHints.FETCH, "tuu.unidad.tipoUnidad");
            return query.getResultList();
        }finally{
            em.close();
        }
	}

	@Override
	public UsuarioUnidad Encontrar(long pUsuarioId, long pUnidadId) {

        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createQuery("select uu from UsuarioUnidad uu " +
                    		" where uu.unidad.unidadId=:pUnidadId and " +
                             "uu.usuario.usuarioId = :pUsuarioId ");

            query.setParameter("pUnidadId", pUnidadId);
            query.setParameter("pUsuarioId",pUsuarioId);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return (UsuarioUnidad)query.getSingleResult();
        } catch (NoResultException iExcepcion) {
			return null;
        }
        finally{
            em.close();
        }			
	}

}
