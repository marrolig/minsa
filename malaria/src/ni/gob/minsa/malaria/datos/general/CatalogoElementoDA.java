package ni.gob.minsa.malaria.datos.general;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.soporte.Mensajes;

public class CatalogoElementoDA<C,K> implements CatalogoElementoService<C, K> {

    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    private Class<C> clase;
    private String nombreClase;

    public CatalogoElementoDA() {
    }

    public CatalogoElementoDA(Class<C> clase, String nombreClase) {
        this.clase = clase;
        this.nombreClase = nombreClase;
    }

    public CatalogoElementoDA(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public CatalogoElementoDA(Class clase) {
        this.clase = clase;
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<C> ListarActivos() {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createQuery("select tr from " + nombreClase + " tr JOIN tr.sistemas s " +
											"where tr.pasivo=0 " +
											"order by tr.orden");
            List<C> objetos = query.getResultList();
            return objetos;
        }finally{
            em.close();
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<C> ListarActivos(String pElemento) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createQuery("select tr from " + nombreClase + " tr " +
										 "where (tr.pasivo=0 and " +
										 "	exists (select s from tr.sistemas s)) or " +
										 "  tr.codigo=:pElemento " +
										 "order by tr.orden");
            query.setParameter("pElemento", pElemento);
            List<C> objetos = query.getResultList();
            return objetos;
        }finally{
            em.close();
        }
    }

	@Override
	public InfoResultado Encontrar(long pElementoId) {
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		C oElemento = (C)oEM.find(clase, pElementoId);
    		if (oElemento!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oElemento);
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
