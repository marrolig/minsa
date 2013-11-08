/**
 * 
 */
package ni.gob.minsa.malaria.datos.rociado;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import ni.gob.minsa.malaria.modelo.encuesta.AbundanciaFauna;
import ni.gob.minsa.malaria.modelo.encuesta.AbundanciaVegetacion;
import ni.gob.minsa.malaria.modelo.encuesta.ClasesCriaderos;
import ni.gob.minsa.malaria.modelo.encuesta.Criadero;
import ni.gob.minsa.malaria.modelo.encuesta.ExposicionSol;
import ni.gob.minsa.malaria.modelo.encuesta.MovimientoAgua;
import ni.gob.minsa.malaria.modelo.encuesta.TiposCriaderos;
import ni.gob.minsa.malaria.modelo.encuesta.TurbidezAgua;
import ni.gob.minsa.malaria.modelo.encuesta.noEntidad.CriaderosUltimaNotificacion;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.general.Catalogo;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Sector;
import ni.gob.minsa.malaria.modelo.rociado.ChecklistMalaria;
import ni.gob.minsa.malaria.modelo.rociado.EquiposMalaria;
import ni.gob.minsa.malaria.modelo.rociado.InsecticidaML;
import ni.gob.minsa.malaria.modelo.rociado.RociadosMalaria;
import ni.gob.minsa.malaria.servicios.rociado.RociadoServices;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * @author dev
 *
 */
public class RociadoDA implements RociadoServices {
	
	private static JPAResourceBean jpaResourceBean = new JPAResourceBean();


	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.rociado.RociadoServices#obtenerCriaderoPorId(long)
	 */
	@Override
	public InfoResultado obtenerCriaderoPorId(long pRociadoId) {
		InfoResultado oResultado = new InfoResultado();
		
		EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		RociadosMalaria resultado = (RociadosMalaria)oEM.find(RociadosMalaria.class, pRociadoId);
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
	 * @see ni.gob.minsa.malaria.servicios.rociado.RociadoServices#obtenerListaRociados(int, int, java.lang.String, org.primefaces.model.SortOrder, ni.gob.minsa.malaria.modelo.poblacion.Comunidad)
	 */
	@Override
	public InfoResultado obtenerListaRociados(int pPaginaActual,
			int pRegistroPorPagina, String pFieldSort, SortOrder pSortOrder,
			Comunidad pComunidad) {
		InfoResultado oResultado = new InfoResultado();
		List<RociadosMalaria> resultado = null;
		
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
		
		String strJPQL = "select rx from RociadosMalaria rx where rx.comunidad.codigo = :pCodComunidad order by rx.fecha desc";
		
		try{
			
			query = em.createQuery(strJPQL);
			query.setParameter("pCodComunidad", pComunidad.getCodigo());
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			
			resultado = (List<RociadosMalaria>) query.getResultList();
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
			
            resultado = (List<RociadosMalaria>) query.getResultList();
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
	 * @see ni.gob.minsa.malaria.servicios.rociado.RociadoServices#obtenerListaRociados(int, int, java.lang.String, org.primefaces.model.SortOrder, ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica)
	 */
	@Override
	public InfoResultado obtenerListaRociados(int pPaginaActual,
			int pRegistroPorPagina, String pFieldSort, SortOrder pSortOrder,
			String pCodMunicipio) {
		InfoResultado oResultado = new InfoResultado();
		List<RociadosMalaria> resultado = null;
		
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
				" select rx from RociadosMalaria rx " +
				" where rx.comunidad.codigo in" +
				" ( select com.codigo from Comunidad com " +
				"	where com.sector.codigo in" +
				"	( select sec.codigo from Sector sec " +
				"	  where sec.municipio.codigoNacional = :pCodMunicipio))  " +
				" order by rx.fecha desc ";
		
		try{
			
			query = em.createQuery(strJPQL);
			query.setParameter("pCodMunicipio", pCodMunicipio);
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			
			resultado = (List<RociadosMalaria>) query.getResultList();
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
			
            resultado = (List<RociadosMalaria>) query.getResultList();
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
	 * @see ni.gob.minsa.malaria.servicios.rociado.RociadoServices#obtenerListaRociados(int, int, java.lang.String, org.primefaces.model.SortOrder, ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva)
	 */
	@Override
	public InfoResultado obtenerListaRociados(int pPaginaActual,
			int pRegistroPorPagina, String pFieldSort, SortOrder pSortOrder,
			long pCodSilais) {
		InfoResultado oResultado = new InfoResultado();
		List<RociadosMalaria> resultado = null;
		
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
				" select rx from RociadosMalaria rx " +
				" where rx.comunidad.codigo in" +
				" ( select com.codigo from Comunidad com " +
				"	where com.sector.codigo in" +
				"	( select sec.codigo from Sector sec " +
				"	  where sec.unidad.codigo in" +
				"		( select und.codigo from Unidad und " +
				"		  where und.entidadAdtva.codigo = :pCodEntidadAdtva )))  " +
				" order by rx.fecha desc ";
		
		
		try{
			
			query = em.createQuery(strJPQL);
			query.setParameter("pCodEntidadAdtva", pCodSilais);
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			
			resultado = (List<RociadosMalaria>) query.getResultList();
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
			
            resultado = (List<RociadosMalaria>) query.getResultList();
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
	 * @see ni.gob.minsa.malaria.servicios.rociado.RociadoServices#guardarRociado(ni.gob.minsa.malaria.modelo.rociado.RociadosMalaria)
	 */
	@Override
	public InfoResultado guardarRociado(RociadosMalaria pRociado) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		//java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	
    	String strUpdate = "";
		
    	RociadosMalaria oRociado = null;
    	try{
    		if( pRociado.getRociadoId() > 0 ){
    			RociadosMalaria oDetachedRociado = (RociadosMalaria)oEM.find(RociadosMalaria.class, pRociado.getRociadoId());
    			oRociado=oEM.merge(oDetachedRociado);

            	
            	Comunidad oComu = (Comunidad)oEM.find(Comunidad.class, pRociado.getComunidad().getComunidadId());            	
            	oRociado.setComunidad(oComu);
            	
            	EntidadAdtva oSilais = (EntidadAdtva)oEM.find(EntidadAdtva.class,pRociado.getSilais().getEntidadAdtvaId());
            	oRociado.setSilais(oSilais);
            	
            	DivisionPolitica oMuni = (DivisionPolitica)oEM.find(DivisionPolitica.class, pRociado.getMunicipio().getDivisionPoliticaId());
            	oRociado.setMunicipio(oMuni);
            	
            	Sector oSector = (Sector)oEM.find(Sector.class,pRociado.getSector().getSectorId());
            	oRociado.setSector(oSector);

//            	InsecticidaML oInsecticida = (InsecticidaML)oEM.find(InsecticidaML.class,pRociado.getInsecticida().getCatalogoId());
//            	oRociado.setInsecticida(oInsecticida);
            	oRociado.setInsecticida(pRociado.getInsecticida());
            	
            	EquiposMalaria oEquipo = (EquiposMalaria)oEM.find(EquiposMalaria.class,pRociado.getEquipo().getCatalogoId());
            	oRociado.setEquipo(oEquipo);
            	
            	oRociado.setBoquilla(pRociado.getBoquilla());
            	oRociado.setCarga(pRociado.getCarga());
            	oRociado.setCerradas(pRociado.getCerradas());
            	oRociado.setCiclo(pRociado.getCiclo());
            	oRociado.setConstruccion(pRociado.getConstruccion());
            	oRociado.setControl(pRociado.getControl());
            	oRociado.setDesalojoAdecuado(pRociado.getDesalojoAdecuado());
            	oRociado.setEnfermos(pRociado.getEnfermos());
            	oRociado.setFecha(pRociado.getFecha());
            	oRociado.setFormulacion(pRociado.getFormulacion());
            	oRociado.setHabNoRociadas(pRociado.getHabNoRociadas());
            	oRociado.setHabRociadas(pRociado.getHabRociadas());
            	oRociado.setInsecticida(pRociado.getInsecticida());
            	oRociado.setOtros(pRociado.getOtros());
            	oRociado.setRenuentes(pRociado.getRenuentes());
            	oRociado.setRociador(pRociado.getRociador());
            	oRociado.setTotalCargas(pRociado.getTotalCargas());
            	oRociado.setViviendasProgramadas(pRociado.getViviendasProgramadas());
            	oRociado.setViviendasRociadas(pRociado.getViviendasRociadas());
            	
            	strUpdate = "Guardar";
    		}else{
    			oEM.persist(pRociado);
    			strUpdate = "Agregar";
    		}
    		        	
            oEM.getTransaction().commit();
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);
    		
    		if( strUpdate.equals("Guardar")) oResultado.setObjeto(oRociado);
    		else oResultado.setObjeto(pRociado);
   			
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
	 * @see ni.gob.minsa.malaria.servicios.rociado.RociadoServices#validarNumeroControlRociado(int)
	 */
	@Override
	public boolean validarNumeroControlRociado(int pNumControl, long pCodSilais, String pCodMunicipio, 
			String pCodComunidad, String pCodSector, Date pFecha) {
		InfoResultado oResultado = new InfoResultado();
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
		Query query = null;
		String strSQL = "";
		
		strSQL = "WITH PARAMS as ( select  ? as pCodComunidad, ? as pFecha, ? as pControl from dual ) "
				+ " Select " 
				+ "   Rx.Control, " 
				+ "   rx.Fecha "
				+ " From "
				+ "   Rociados_Malaria rx "
				+ "   inner join params on 1=1 "
				+ " Where "
				+ "   Rx.Control = pControl " 
				+ "   And Rx.Comunidad = pCodComunidad"
				+ "   And To_Char(rx.Fecha,'YYYYMM') = To_Char(to_date(pFecha,'dd/MM/yyyy'),'YYYYMM') ";
		
		try{
			
			query = em.createNativeQuery(strSQL);
			query.setParameter(1,pCodComunidad);
			query.setParameter(2,pFecha);
			query.setParameter(3,pNumControl);
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			
			List<Object[]> oFilasResultados=query.getResultList();
			
			if( oFilasResultados.isEmpty()) return true;
			else return false;
            
		}catch(Exception iExcepcion){
    		oResultado.setExcepcion(true);
    		oResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO + iExcepcion.getMessage());
    		oResultado.setFuenteError(iExcepcion.toString().split(":",1).toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
    		oResultado.setFilasAfectadas(0);	
    		return false;
		}		

	}

	@Override
	public InfoResultado obtenerCatalogoPorCodigo(String pCodigo) {
		InfoResultado oResultado = new InfoResultado();
		
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
		Query query = null;
		
		if( pCodigo.isEmpty() ){
			oResultado.setOk(false);
			oResultado.setMensaje(Mensajes.RESTRICCION_BUSQUEDA);
			oResultado.setMensajeDetalle("Comunidad no identificada");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			oResultado.setFilasAfectadas(0);
			return oResultado;
		}
		
		String strJPQL = "select ct from InsecticidaML ct where ct.codigo = :pCodigo";
		
		InsecticidaML oInsecticida;
		
		try{
			
			query = em.createQuery(strJPQL);
			query.setParameter("pCodigo", pCodigo);
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			
			oInsecticida = (InsecticidaML) query.getSingleResult();
			if( oInsecticida == null){
				oResultado.setOk(false);
				oResultado.setMensaje("Registro no encontrado");
				return oResultado;
			}
			
			oResultado.setOk(true);
			oResultado.setObjeto(oInsecticida);
			
            
		}catch(Exception iExcepcion){
    		oResultado.setExcepcion(true);
    		oResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO + iExcepcion.getMessage());
    		oResultado.setFuenteError(iExcepcion.toString().split(":",1).toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
    		oResultado.setFilasAfectadas(0);	
		}
		
		return oResultado;	}

}
