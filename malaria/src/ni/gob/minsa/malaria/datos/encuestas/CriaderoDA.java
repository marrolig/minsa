/**
 * 
 */
package ni.gob.minsa.malaria.datos.encuestas;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.primefaces.model.SortOrder;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.encuesta.AddCatalogo;
import ni.gob.minsa.malaria.modelo.encuesta.AddSistemaCatalogo;
import ni.gob.minsa.malaria.modelo.encuesta.Criadero;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.general.Catalogo;
import ni.gob.minsa.malaria.modelo.general.ClaseEvento;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.vigilancia.ColVol;
import ni.gob.minsa.malaria.modelo.vigilancia.EventoSalud;
import ni.gob.minsa.malaria.servicios.encuestas.CriaderoServices;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

/**
 * @author dev
 *
 */
public class CriaderoDA implements CriaderoServices {
	
	private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.CriaderoServices#obtenerCriaderoPorId(long)
	 */
	@Override
	public InfoResultado obtenerCriaderoPorId(long pCriaderoId) {
		InfoResultado oResultado = new InfoResultado();
		
		EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		Criadero resultado = (Criadero)oEM.find(Criadero.class, pCriaderoId);
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
	 * @see ni.gob.minsa.malaria.servicios.encuestas.CriaderoServices#obtenerCriaderos(int, int, java.lang.String, java.lang.Boolean, ni.gob.minsa.malaria.modelo.poblacion.Comunidad)
	 */
	@Override
	public InfoResultado obtenerCriaderos(int pPaginaActual,
			int pRegistroPorPagina, String pFieldSort, SortOrder pSortOrder,
			Comunidad pComunidad) {
		InfoResultado oResultado = new InfoResultado();
		List<Criadero> resultado = null;
		
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
		Query query = null;
		
		if( pComunidad == null ){
			oResultado.setOk(false);
			oResultado.setMensaje(Mensajes.RESTRICCION_BUSQUEDA);
			oResultado.setMensajeDetalle("Comunidad no identificada");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			oResultado.setFilasAfectadas(0);
			return oResultado;
		}
		
		String strJPQL = "select cr from Criadero cr where cr.comunidad.codigo = :pCodComunidad order by criaderoId";
		
		try{
			
			query = em.createQuery(strJPQL);
			query.setParameter("pCodComunidad", pComunidad.getCodigo());
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			
			resultado = (List<Criadero>) query.getResultList();
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
			
            resultado = (List<Criadero>) query.getResultList();
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
	 * @see ni.gob.minsa.malaria.servicios.encuestas.CriaderoServices#obtenerCriaderos(int, int, java.lang.String, java.lang.Boolean, ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva)
	 */
	@Override
	public InfoResultado obtenerCriaderos(int pPaginaActual,
			int pRegistroPorPagina, String pFieldSort, SortOrder pSortOrder,
			long pCodSilais) {
		InfoResultado oResultado = new InfoResultado();
		List<Criadero> resultado = null;
		
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
		Query query = null;
		
		if( pCodSilais == 0 ){
			oResultado.setOk(false);
			oResultado.setMensaje(Mensajes.RESTRICCION_BUSQUEDA);
			oResultado.setMensajeDetalle("Silais no identificado");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			oResultado.setFilasAfectadas(0);
			return oResultado;
		}
		
		String strJPQL = 
				" select cr from Criadero cr " +
				" where cr.comunidad.codigo in" +
				" ( select com.codigo from Comunidad com " +
				"	where com.sector.codigo in" +
				"	( select sec.codigo from Sector sec " +
				"	  where sec.unidad.codigo in" +
				"		( select und.codigo from Unidad und " +
				"		  where und.entidadAdtva.codigo = :pCodEntidadAdtva )))  " +
				" order by criaderoId ";
		
		
		try{
			
			query = em.createQuery(strJPQL);
			query.setParameter("pCodEntidadAdtva", pCodSilais);
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			
			resultado = (List<Criadero>) query.getResultList();
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
			
            resultado = (List<Criadero>) query.getResultList();
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
	 * @see ni.gob.minsa.malaria.servicios.encuestas.CriaderoServices#obtenerCriaderos(int, int, java.lang.String, java.lang.Boolean, ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica)
	 */
	@Override
	public InfoResultado obtenerCriaderos(int pPaginaActual,
			int pRegistroPorPagina, String pFieldSort, SortOrder pSortOrder,
			String pCodMunicipio) {
		InfoResultado oResultado = new InfoResultado();
		List<Criadero> resultado = null;
		
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
		Query query = null;
		
		if( pCodMunicipio == null || pCodMunicipio.isEmpty() ){
			oResultado.setOk(false);
			oResultado.setMensaje(Mensajes.RESTRICCION_BUSQUEDA);
			oResultado.setMensajeDetalle("Municipio no identificado");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			oResultado.setFilasAfectadas(0);
			return oResultado;
		}
		
		String strJPQL = 
				" select cr from Criadero cr " +
				" where cr.comunidad.codigo in" +
				" ( select com.codigo from Comunidad com " +
				"	where com.sector.codigo in" +
				"	( select sec.codigo from Sector sec " +
				"	  where sec.municipio.codigoNacional = :pCodMunicipio))  " +
				" order by criaderoId ";
		
		try{
			
			query = em.createQuery(strJPQL);
			query.setParameter("pCodMunicipio", pCodMunicipio);
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			
			resultado = (List<Criadero>) query.getResultList();
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
			
            resultado = (List<Criadero>) query.getResultList();
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
	 * @see ni.gob.minsa.malaria.servicios.encuestas.CriaderoServices#guardarCriadero(ni.gob.minsa.malaria.modelo.encuesta.Criadero)
	 */
	@Override
	public InfoResultado guardarCriadero(Criadero pCriadero) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);		
    	String strUpdate = "";
		
    	
    	try{
    		if( pCriadero.getCriaderoId() > 0 ){
        		Criadero oDetachedCriadero = (Criadero)oEM.find(Criadero.class, pCriadero.getCriaderoId());
            	Criadero oCriadero=oEM.merge(oDetachedCriadero);
            	strUpdate = "Guardar";
    		}else{
    			oEM.persist(pCriadero);
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
	 * @see ni.gob.minsa.malaria.servicios.encuestas.CriaderoServices#eliminarCriadero(long)
	 */
	@Override
	public InfoResultado eliminarCriadero(long pCriaderoId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		Criadero oCriadero = (Criadero)oEM.find(Criadero.class, pCriaderoId);
    		if (oCriadero!=null) {
    			oEM.remove(oCriadero);
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
	public InfoResultado obtenerMunicipiosPorSilais(long pCodSilais) {
		InfoResultado oResultado = new InfoResultado();
		List<DivisionPolitica> resultado = null;
		
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
		Query query = null;
		
		if( pCodSilais == 0 ){
			oResultado.setOk(false);
			oResultado.setMensaje(Mensajes.RESTRICCION_BUSQUEDA);
			oResultado.setMensajeDetalle("Silais no identificado");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			oResultado.setFilasAfectadas(0);
			return oResultado;
		}
		
		String strJPQL = 
				" select mun from DivisionPolitica mun " +
				" where mun.codigoNacional in " +
				" ( select u.municipio.codigoNacional from Unidad u " +
				"	where u.entidadAdtva.codigo = :pCodSilais ) " +
				" order by mun.nombre ";
		
		try{
			
			query = em.createQuery(strJPQL);
			query.setParameter("pCodSilais", pCodSilais);
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			
			resultado = (List<DivisionPolitica>) query.getResultList();
			if( resultado.isEmpty()){
				oResultado.setOk(false);
				oResultado.setMensaje("Registros no encontrados");
				return oResultado;
			}
			
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
	 * @see ni.gob.minsa.malaria.servicios.encuestas.CriaderoServices#agregarCatOtros(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public InfoResultado agregarCatOtros(String pValor, String pCodigo,
			String pDependencia) {
		InfoResultado oResultado = new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);				
		
    	
    	if( pValor == null || pValor.isEmpty() ){
    		oResultado.setOk(false);
    		oResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO);
    		oResultado.setMensajeDetalle("Nombre del catalogo no identificado");
    		return oResultado;
    	}
    	
    	if( pCodigo == null || pCodigo.isEmpty() ){
    		oResultado.setOk(false);
    		oResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO);
    		oResultado.setMensajeDetalle("Codigo del catalogo no identificado");
    		return oResultado;    		
    	}
    	
    	if( pDependencia == null || pDependencia.isEmpty() ){
    		oResultado.setOk(false);
    		oResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO);
    		oResultado.setMensajeDetalle("Codigo Nodo Padre no Identificado");
    		return oResultado;    		    		
    	}
    	
    	AddCatalogo oCatalogo = new AddCatalogo();
    	oCatalogo.setValor(pValor);
		oCatalogo.setCodigo(pDependencia+ "|" + pCodigo);
		oCatalogo.setDependencia(pDependencia);
		oCatalogo.setUsuarioRegistro(Utilidades.obtenerInfoSesion().getUsername());
		oCatalogo.setFechaRegistro(new Date());
		
		AddSistemaCatalogo oSisCat = new AddSistemaCatalogo();
		oSisCat.setCatalogo(pCodigo);
		oSisCat.setSistema(Utilidades.obtenerInfoSesion().getSistemaSesion());
		oSisCat.setUsuarioRegistro(Utilidades.obtenerInfoSesion().getUsername());
		oSisCat.setFechaRegistro(new Date());
		
		try{
			
			oEM.persist(oCatalogo);
			oEM.persist(oSisCat);
			
            oEM.getTransaction().commit();
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);		
			
    	} catch (EntityExistsException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Agregar Cat");
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
    		oResultado.setFuenteError("Agregar Cat");
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

}
