/**
 * 
 */
package ni.gob.minsa.malaria.datos.encuestas;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.encuesta.Criadero;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosPesquisa;
import ni.gob.minsa.malaria.servicios.encuestas.PesquisaServices;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * @author dev
 *
 */
public class PesquisaDA implements PesquisaServices {
	
	private static JPAResourceBean jpaResourceBean = new JPAResourceBean();	

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.PesquisaServices#obtenerPesquisaPorId(long)
	 */
	@Override
	public InfoResultado obtenerPesquisaPorId(long pPesquisaId) {
		InfoResultado oResultado = new InfoResultado();
		
		EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		CriaderosPesquisa resultado = (CriaderosPesquisa)oEM.find(CriaderosPesquisa.class, pPesquisaId);
    		if (resultado!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)resultado);
    			return oResultado;
    		}
    		else {
    			oResultado.setMensaje(Mensajes.ENCONTRAR_REGISTRO_NO_EXISTE);
    			oResultado.setOk(false);
    			oResultado.setFilasAfectadas(0);
    			return oResultado;
    		}
    	}
    	catch (ConstraintViolationException iExcepcion) {
    		oResultado.setExcepcion(true);
    		ConstraintViolation<?> oConstraintViolation = iExcepcion.getConstraintViolations().iterator().next();
    		oResultado.setMensaje(oConstraintViolation.getMessage());
    		oResultado.setFuenteError(oConstraintViolation.getPropertyPath().toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setFilasAfectadas(0);
    		return oResultado;
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

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.PesquisaServices#obtenerPesquisasPorCriadero(int, int, java.lang.String, java.lang.Boolean, ni.gob.minsa.malaria.modelo.encuesta.Criadero)
	 */
	@Override
	public InfoResultado obtenerPesquisasPorCriadero(int pPaginaActual,
			int pRegistroPorPagina, String pFieldSort, Boolean pSortOrder,
			Criadero pCriadero) {
		InfoResultado oResultado = new InfoResultado();
		List<CriaderosPesquisa> resultado = null;
		
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
		Query query = null;
		
		if( pCriadero == null ){
			oResultado.setOk(false);
			oResultado.setMensaje(Mensajes.RESTRICCION_BUSQUEDA);
			oResultado.setMensajeDetalle("Criadero no identificado");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			oResultado.setFilasAfectadas(0);
			return oResultado;
		}
		
		String strJPQL = "select psq from CriaderosPesquisa psq where psq.criadero.codigo = :pCodCriadero order by criaderoPesquisaId";
		
		try{
			
			query = em.createQuery(strJPQL);
			query.setParameter("pCodCriadero", pCriadero.getCodigo());
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			
			resultado = (List<CriaderosPesquisa>) query.getResultList();
			if( resultado.isEmpty()){
				oResultado.setOk(false);
				oResultado.setMensaje("Registros no encontrados");
				return oResultado;
			}
			
			oResultado.setFilasAfectadas(resultado.size());
            if (oResultado.getFilasAfectadas()<=pPaginaActual) pPaginaActual-=oResultado.getFilasAfectadas();
            pPaginaActual=oResultado.getFilasAfectadas()<=pPaginaActual ? 0: pPaginaActual;
            query.setFirstResult(pPaginaActual);
            query.setMaxResults(pRegistroPorPagina);
			
            resultado = (List<CriaderosPesquisa>) query.getResultList();
            oResultado.setObjeto(resultado);
            oResultado.setOk(true);
            
		}catch(Exception iExcepcion){
    		oResultado.setExcepcion(true);
    		oResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO + iExcepcion.getMessage());
    		oResultado.setFuenteError(iExcepcion.toString().split(":",1).toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
    		oResultado.setFilasAfectadas(0);	
		}
		
		return oResultado;

	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.PesquisaServices#guardarPesquisa(ni.gob.minsa.malaria.modelo.encuesta.CriaderosPesquisa)
	 */
	@Override
	public InfoResultado guardarPesquisa(CriaderosPesquisa pPesquisa) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);		
    	String strUpdate = "";
		
    	
    	try{
    		if( pPesquisa.getCriaderoPesquisaId() > 0 ){
    			CriaderosPesquisa oDetachedPesquisa = (CriaderosPesquisa)oEM.find(CriaderosPesquisa.class, pPesquisa.getCriaderoPesquisaId());
    			CriaderosPesquisa oPesquisa=oEM.merge(oDetachedPesquisa);
            	strUpdate = "Guardar";
    		}else{
    			oEM.persist(pPesquisa);
    			strUpdate = "Agregar";
    		}
    		        	
            oEM.getTransaction().commit();
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);
   			
   			
    	} catch (EntityExistsException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError(strUpdate);
    		oResultado.setMensaje(Mensajes.EXCEPCION_REGISTRO_EXISTE);
    		oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
    		oResultado.setOk(false);
    		return oResultado;
    	} catch (ConstraintViolationException iExcepcion) {
    		oResultado.setExcepcion(true);
    		ConstraintViolation<?> oConstraintViolation = iExcepcion.getConstraintViolations().iterator().next();
    		oResultado.setMensaje(oConstraintViolation.getMessage());
    		oResultado.setFuenteError(oConstraintViolation.getPropertyPath().toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setFilasAfectadas(0);
    		return oResultado;
    	} 
    	catch (PersistenceException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError(strUpdate);
    		oResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setOk(false);
    		return oResultado;
    		
    	} catch (Exception iExcepcion){
    		oResultado.setExcepcion(true);
    		oResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO + iExcepcion.getMessage());
    		oResultado.setFuenteError(iExcepcion.toString().split(":",1).toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
    		oResultado.setFilasAfectadas(0);
    		return oResultado;
    		
    	} finally{
    		oEM.close();
    	}    	
    	
    	
    	
		return oResultado;

	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.PesquisaServices#eliminarPesquisa(long)
	 */
	@Override
	public InfoResultado eliminarPesquisa(long pPesquisaId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		CriaderosPesquisa oPesquisa = (CriaderosPesquisa)oEM.find(CriaderosPesquisa.class, pPesquisaId);
    		if (oPesquisa!=null) {
    			oEM.remove(oPesquisa);
    			oEM.getTransaction().commit();
    			
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			
    			return oResultado;
    		}
    		else {
    			
    			oResultado.setMensaje(Mensajes.ELIMINAR_REGISTRO_NO_EXISTE);
    			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
    			oResultado.setOk(false);
    			
    			return oResultado;
    		}
    	} catch (PersistenceException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Eliminar");
    		oResultado.setMensaje(Mensajes.ELIMINAR_RESTRICCION);
    		oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
    		oResultado.setOk(false);
    		return oResultado;
    		
    	} catch (Exception iExcepcion) {
    		
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
