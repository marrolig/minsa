/**
 * 
 */
package ni.gob.minsa.malaria.datos.rociado;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.rociado.ChecklistMalaria;
import ni.gob.minsa.malaria.modelo.rociado.ItemsCheckListMalaria;
import ni.gob.minsa.malaria.modelo.rociado.RociadosMalaria;
import ni.gob.minsa.malaria.servicios.rociado.RociadoChkListServices;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * @author dev
 *
 */
public class RociadoChkListDA implements RociadoChkListServices {
	
	private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.rociado.RociadoChkListServices#obtenerChkListPorId(long)
	 */
	@Override
	public InfoResultado obtenerChkListPorId(long pChkListId) {
		InfoResultado oResultado = new InfoResultado();
		
		EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		ChecklistMalaria resultado = (ChecklistMalaria)oEM.find(ChecklistMalaria.class, pChkListId);
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
	 * @see ni.gob.minsa.malaria.servicios.rociado.RociadoChkListServices#obtenerChkListPorRociado(ni.gob.minsa.malaria.modelo.rociado.RociadosMalaria)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public InfoResultado obtenerChkListPorRociado(RociadosMalaria pRociado) {
		InfoResultado oResultado = new InfoResultado();
		List<ChecklistMalaria> resultado = null;
		
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
		Query query = null;
		
		if( pRociado == null ){
			oResultado.setOk(false);
			oResultado.setMensaje(Mensajes.RESTRICCION_BUSQUEDA);
			oResultado.setMensajeDetalle("Criadero no identificado");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			oResultado.setFilasAfectadas(0);
			return oResultado;
		}
		
		String strJPQL = "select chkList from ChecklistMalaria chkList where chkList.rociadosMalaria.rociadoId = :pRociadoId";
		
		try{
			
			query = em.createQuery(strJPQL);
			query.setParameter("pRociadoId", pRociado.getRociadoId());
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			
			resultado = (List<ChecklistMalaria>) query.getResultList();
			if( resultado.isEmpty()){
				oResultado.setOk(false);
				oResultado.setMensaje("Registros no encontrados");
				return oResultado;
			}
			
			oResultado.setFilasAfectadas(resultado.size());
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
	 * @see ni.gob.minsa.malaria.servicios.rociado.RociadoChkListServices#guardarChkList(ni.gob.minsa.malaria.modelo.rociado.ChecklistMalaria)
	 */
	@Override
	public InfoResultado guardarChkList(ChecklistMalaria pChkList) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
//    	@SuppressWarnings("unused")
//		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);		
    	String strUpdate = "";
    	ChecklistMalaria oCheckList = null;
    	
    	try{
    		if(  pChkList.getChecklistMalariaId() > 0 ){
    			ChecklistMalaria oDetachedCheckList = (ChecklistMalaria)oEM.find(ChecklistMalaria.class, pChkList.getChecklistMalariaId());
    			oCheckList=oEM.merge(oDetachedCheckList);
    			
    			RociadosMalaria oRociado = (RociadosMalaria)oEM.find(RociadosMalaria.class, pChkList.getRociadosMalaria().getRociadoId());
    			ItemsCheckListMalaria oElementoLista = (ItemsCheckListMalaria)oEM.find(ItemsCheckListMalaria.class, pChkList.getElementoLista().getCatalogoId());
    			
    			oCheckList.setElementoLista(oElementoLista);
    			oCheckList.setFechaRegistro(pChkList.getFechaRegistro());
    			oCheckList.setRociadosMalaria(oRociado);
    			oCheckList.setUsuarioRegistro(pChkList.getUsuarioRegistro());
               	strUpdate = "Guardar";
               	
    		}else{
    			oEM.persist(pChkList);
    			strUpdate = "Agregar";
    		}
    		        	
            oEM.getTransaction().commit();
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);
   			
    		if( strUpdate.equals("Guardar")) oResultado.setObjeto(oCheckList);
    		else oResultado.setObjeto(pChkList);
   			
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
	 * @see ni.gob.minsa.malaria.servicios.rociado.RociadoChkListServices#eliminarChkList(long)
	 */
	@Override
	public InfoResultado eliminarChkList(long pChkListId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
//    	@SuppressWarnings("unused")
//		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		ChecklistMalaria oElementoLista = (ChecklistMalaria)oEM.find(ChecklistMalaria.class, pChkListId);
    		if (oElementoLista!=null) {
    			oEM.remove(oElementoLista);
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
