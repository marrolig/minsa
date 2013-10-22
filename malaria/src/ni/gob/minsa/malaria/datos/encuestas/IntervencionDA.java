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
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosIntervencion;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosPesquisa;
import ni.gob.minsa.malaria.servicios.encuestas.IntervencionServices;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * @author dev
 *
 */
public class IntervencionDA implements IntervencionServices {

	private static JPAResourceBean jpaResourceBean = new JPAResourceBean();	
	
	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.IntervencionServices#obtenerIntervencionPorId(long)
	 */
	@Override
	public InfoResultado obtenerIntervencionPorId(long pIntervencionId) {
		InfoResultado oResultado = new InfoResultado();
		
		EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		CriaderosIntervencion resultado = (CriaderosIntervencion)oEM.find(CriaderosIntervencion.class, pIntervencionId);
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
	 * @see ni.gob.minsa.malaria.servicios.encuestas.IntervencionServices#obtenerIntervencionesPorPesquisa(int, int, java.lang.String, java.lang.Boolean, ni.gob.minsa.malaria.modelo.encuesta.CriaderosPesquisa)
	 */
	@Override
	public InfoResultado obtenerIntervencionesPorPesquisa(int pPaginaActual,
			int pRegistroPorPagina, String pFieldSort, Boolean pSortOrder,
			CriaderosPesquisa pPesquisa) {
		InfoResultado oResultado = new InfoResultado();
		List<CriaderosIntervencion> resultado = null;
		
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
		Query query = null;
		
		if( pPesquisa == null ){
			oResultado.setOk(false);
			oResultado.setMensaje(Mensajes.RESTRICCION_BUSQUEDA);
			oResultado.setMensajeDetalle("Pesquisa no identificada");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			oResultado.setFilasAfectadas(0);
			return oResultado;
		}
		
		String strJPQL = "select int from CriaderosIntervencion int " +
				" where int.criaderosPesquisa.criaderoPesquisaId = :pPesquisaId order by criaderoIntervencionId";
		
		try{
			
			query = em.createQuery(strJPQL);
			query.setParameter("pPesquisaId", pPesquisa.getCriaderoPesquisaId());
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			
			resultado = (List<CriaderosIntervencion>) query.getResultList();
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
			
            resultado = (List<CriaderosIntervencion>) query.getResultList();
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
	 * @see ni.gob.minsa.malaria.servicios.encuestas.IntervencionServices#guardarIntervencion(ni.gob.minsa.malaria.modelo.encuesta.CriaderosIntervencion)
	 */
	@Override
	public InfoResultado guardarIntervencion(CriaderosIntervencion pIntervencion) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);		
    	String strUpdate = "";
		
    	
    	try{
    		if( pIntervencion.getCriaderoIntervencionId() > 0 ){
    			CriaderosIntervencion oDetachedIntervencion = (CriaderosIntervencion)oEM.find(CriaderosIntervencion.class, pIntervencion.getCriaderoIntervencionId());
    			CriaderosIntervencion oIntervencion=oEM.merge(oDetachedIntervencion);
            	strUpdate = "Guardar";
    		}else{
    			oEM.persist(pIntervencion);
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
	 * @see ni.gob.minsa.malaria.servicios.encuestas.IntervencionServices#eliminarIntervencion(long)
	 */
	@Override
	public InfoResultado eliminarIntervencion(long pIntervencionId) {
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

	@Override
	public InfoResultado eliminarIntervencionPorPesquisa(
			CriaderosPesquisa pPesquisa) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	Query query = null;
    	int n = 0;
    	
		try{
			
			query = oEM.createNamedQuery("DELETE FROM CRIADEROS_INTERVENCIONES " +
					" WHERE PESQUISA = " + pPesquisa.getCriaderoPesquisaId());
			n = query.executeUpdate();
			
			if( n == 0 ){
				oResultado.setOk(false);
				oResultado.setMensaje("No se encontraron coincidencias de registro para eliminar");
				oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
				return oResultado;
			}
			
			oResultado.setOk(true);
			oResultado.setMensaje("Registros Eliminados");
			
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
		
		
		return oResultado;

	}

}
