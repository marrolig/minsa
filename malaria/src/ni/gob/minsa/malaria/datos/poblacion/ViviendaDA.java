package ni.gob.minsa.malaria.datos.poblacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.general.TipoVivienda;
import ni.gob.minsa.malaria.modelo.poblacion.Manzana;
import ni.gob.minsa.malaria.modelo.poblacion.Vivienda;
import ni.gob.minsa.malaria.modelo.poblacion.ViviendaManzana;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.ViviendaUltimaEncuesta;
import ni.gob.minsa.malaria.servicios.poblacion.ViviendaService;
import ni.gob.minsa.malaria.soporte.Mensajes;

public class ViviendaDA implements ViviendaService {

	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.poblacion.ViviendaService#ViviendasPorComunidad(java.lang.String, java.lang.Boolean)
     */
    @SuppressWarnings("unchecked")
	@Override
	public List<ViviendaUltimaEncuesta> ViviendasPorComunidad(String pComunidad, Boolean pPasivo) {
       
    	BigDecimal iPasivo = null;
        if (pPasivo!=null) iPasivo=pPasivo.booleanValue()?(new BigDecimal(1)):(new BigDecimal(0));
    	
    	EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
        	Query query = null;
        	query = em.createNativeQuery("WITH params AS (SELECT ? AS pComunidad, ? AS pPasivo FROM DUAL) " +
        								 "SELECT * FROM ( " +
        								 "   SELECT V.VIVIENDA_ID, V.CODIGO, TV.VALOR AS TIPO_VIVIENDA, " +
        								 "		VE.JEFE_FAMILIA, CO.VALOR AS CONDICION_OCUPACION, " + 
        								 "		VE.HABITANTES, VE.FECHA_ENCUESTA, " + 
        								 "		CASE " + 
        								 "			WHEN (SELECT COUNT(VR.FACTOR_RIESGO) FROM SIVE.VIVIENDAS_RIESGOS VR WHERE VR.VIVIENDA=V.CODIGO)>0 THEN 1 " +
        								 "			ELSE 0 " +
        								 "		END AS EXISTE_RIESGO, " +
        								 "		V.PASIVO, V.LATITUD, V.LONGITUD FROM GENERAL.VIVIENDAS V " +  
        								 "	  INNER JOIN params p ON 1=1 " +
        								 "	  INNER JOIN GENERAL.CATALOGOS TV ON V.TIPO_VIVIENDA=TV.CODIGO " +
        								 "	  LEFT OUTER JOIN SIVE.VIVIENDAS_ENCUESTAS VE ON V.CODIGO=VE.VIVIENDA " +
        								 "	  LEFT OUTER JOIN GENERAL.CATALOGOS CO ON VE.CONDICION_OCUPACION=CO.CODIGO " +
        								 "	  WHERE V.COMUNIDAD=p.pComunidad AND " +
        								 "	        (p.pPasivo IS NULL or V.PASIVO=p.pPasivo)) RV " +
        								 " WHERE RV.FECHA_ENCUESTA IS NULL OR " +
        								 "		 RV.FECHA_ENCUESTA =(SELECT MAX(VE1.FECHA_ENCUESTA) " + 
        		                         " 								FROM SIVE.VIVIENDAS_ENCUESTAS VE1 " + 
        		                         "                            WHERE RV.CODIGO=VE1.VIVIENDA) " +
        		                         " ORDER BY RV.CODIGO ASC");

        	query.setParameter(1, pComunidad);
        	query.setParameter(2, iPasivo);
        	
        	List<Object[]> oFilasResultados=query.getResultList();
        	Object[] oFilaResultado;

        	List<ViviendaUltimaEncuesta> oViviendasUltimaEncuesta = new ArrayList<ViviendaUltimaEncuesta>();
        	
        	for(int i=0;i<oFilasResultados.size();i++){
        		ViviendaUltimaEncuesta oViviendaUltimaEncuesta = new ViviendaUltimaEncuesta();
        		oFilaResultado=oFilasResultados.get(i);
        		oViviendaUltimaEncuesta.setViviendaId(((BigDecimal)oFilaResultado[0]).longValue());
        		oViviendaUltimaEncuesta.setCodigo((String)oFilaResultado[1]);
        		oViviendaUltimaEncuesta.setTipoVivienda((String)oFilaResultado[2]);
        		oViviendaUltimaEncuesta.setJefeFamilia((String)oFilaResultado[3]);
        		oViviendaUltimaEncuesta.setCondicionOcupacion((String)oFilaResultado[4]);
        		oViviendaUltimaEncuesta.setHabitantes((BigDecimal)oFilaResultado[5]);
        		oViviendaUltimaEncuesta.setFechaUltimaEncuesta((Date)oFilaResultado[6]);
        		oViviendaUltimaEncuesta.setExistenFactoresRiesgo((BigDecimal)oFilaResultado[7]);
        		oViviendaUltimaEncuesta.setPasivo((BigDecimal)oFilaResultado[8]);
        		oViviendaUltimaEncuesta.setLatitud((BigDecimal)oFilaResultado[9]);
        		oViviendaUltimaEncuesta.setLongitud((BigDecimal)oFilaResultado[10]);
        		oViviendasUltimaEncuesta.add(oViviendaUltimaEncuesta);
        	}
        	
        	return oViviendasUltimaEncuesta;
            		             
        }finally{
            em.close();
        }
	}

	@Override
	public InfoResultado Encontrar(long pViviendaId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		Vivienda oVivienda = (Vivienda)oEM.find(Vivienda.class, pViviendaId);
    		if (oVivienda!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oVivienda);
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

	@Override
	public InfoResultado Guardar(Vivienda pVivienda) {

		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
        	Vivienda oDetachedVivienda = (Vivienda)oEM.find(Vivienda.class, pVivienda.getViviendaId());
        	Vivienda oVivienda=oEM.merge(oDetachedVivienda);
        	oVivienda.setPasivo(pVivienda.getPasivo());
        	oVivienda.setLongitud(pVivienda.getLongitud());
        	oVivienda.setLatitud(pVivienda.getLatitud());
        	oVivienda.setDireccion(pVivienda.getDireccion());
        	
        	TipoVivienda oTipoVivienda = (TipoVivienda)oEM.find(TipoVivienda.class, pVivienda.getTipoVivienda().getCatalogoId());

        	oVivienda.setTipoVivienda(oTipoVivienda);
        	oVivienda.setObservaciones(pVivienda.getObservaciones());

        	// verifica si los cambios implican una eliminación de la manzana o un cambio de manzana,
        	// en cualquiera de los casos, el objeto Manzana al cual se hace referencia previo a los cambios
        	// debe ser eliminado.
        	if ((pVivienda.getViviendaManzana()==null && oVivienda.getViviendaManzana()!=null) || 
        			(pVivienda.getViviendaManzana()!=null && 
        			 oVivienda.getViviendaManzana()!=null && 
        			 !pVivienda.getViviendaManzana().getManzana().getCodigo().equals(oVivienda.getViviendaManzana().getManzana().getCodigo()))) {
        		
        		ViviendaManzana oViviendaManzana = (ViviendaManzana)oEM.find(ViviendaManzana.class, oVivienda.getViviendaManzana().getViviendaManzanaId());
        		if (oViviendaManzana!=null) {
        			oEM.remove(oViviendaManzana);
        		}
        		if (pVivienda.getViviendaManzana()==null) {
        			oVivienda.setViviendaManzana(null);
        		}
        	}
        	
        	// si se ha seleccionado una manzana, significa que la relación entre la manzana y la vivienda es
        	// nueva, y por tanto debe ser persistida
        	if (pVivienda.getViviendaManzana()!=null &&
        			(oVivienda.getViviendaManzana()==null || 
        			!pVivienda.getViviendaManzana().getManzana().getCodigo().equals(oVivienda.getViviendaManzana().getManzana().getCodigo()))) {
        		
        		ViviendaManzana oViviendaManzana = new ViviendaManzana();
        		Manzana pManzana = (Manzana)oEM.find(Manzana.class, pVivienda.getViviendaManzana().getManzana().getManzanaId());
        		oViviendaManzana.setManzana(pManzana);
        		oViviendaManzana.setVivienda(oVivienda);
        		oVivienda.setViviendaManzana(oViviendaManzana);
        	}
        	
            oEM.getTransaction().commit();
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);
   			
    		return oResultado;
   			
    	} catch (EntityExistsException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Guardar");
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
    		oResultado.setFuenteError("Guardar");
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
	}

	@Override
	public InfoResultado Agregar(Vivienda pVivienda) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
		@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
            oEM.persist(pVivienda);
            oEM.getTransaction().commit();
            oResultado.setObjeto(pVivienda);
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);
   			
    		return oResultado;
   			
    	} catch (EntityExistsException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Agregar");
    		oResultado.setMensaje(Mensajes.REGISTRO_DUPLICADO);
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
    		oResultado.setFuenteError("Agregar");
    		oResultado.setMensaje("Error al agregar el registro. " + Mensajes.REGISTRO_DUPLICADO);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setOk(false);
    		return oResultado;
    		
    	} catch (Exception iExcepcion){
    		oResultado.setExcepcion(true);
    		oResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO + iExcepcion.getMessage());
    		oResultado.setFuenteError(iExcepcion.toString().split(":",1).toString());
    		oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
    		oResultado.setOk(false);
    		oResultado.setFilasAfectadas(0);
    		return oResultado;
    		
    	} finally{
    		oEM.close();
    	}
	}

	@Override
	public InfoResultado Eliminar(long pViviendaId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		Vivienda oVivienda = (Vivienda)oEM.find(Vivienda.class, pViviendaId);
    		if (oVivienda!=null) {
    			oEM.remove(oVivienda);
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

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.ViviendaService#ContarViviendasPorComunidad(long, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public int ContarViviendasPorComunidad(String pComunidad, String pNombre,
			Boolean pPasivo) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        int totalViviendas=0;

        String iNombre=null;
        if (pNombre!=null) iNombre=pNombre.trim().isEmpty()?null:("%" + pNombre.trim().toUpperCase() + "%");
        BigDecimal iPasivo=null;
        if (pPasivo!=null) iPasivo=pPasivo.booleanValue()?(new BigDecimal(1)):(new BigDecimal(0));
        		
        Query query = em.createNativeQuery("WITH params AS (SELECT ? AS pComunidad, ? AS pPasivo, ? AS pNombre FROM DUAL) " +
					 "SELECT COUNT(RV.VIVIENDA_ID) FROM ( " +
					 "   SELECT V.VIVIENDA_ID, V.CODIGO, TV.VALOR AS TIPO_VIVIENDA, " +
					 "		VE.JEFE_FAMILIA, CO.VALOR AS CONDICION_OCUPACION, " + 
					 "		VE.HABITANTES, VE.FECHA_ENCUESTA, " + 
					 "		CASE " + 
					 "			WHEN (SELECT COUNT(VR.FACTOR_RIESGO) FROM SIVE.VIVIENDAS_RIESGOS VR WHERE VR.VIVIENDA=V.CODIGO)>0 THEN 1 " +
					 "			ELSE 0 " +
					 "		END AS EXISTE_RIESGO, " +
					 "		V.PASIVO FROM GENERAL.VIVIENDAS V " +  
					 "	  INNER JOIN params p ON 1=1 " +
					 "	  INNER JOIN GENERAL.CATALOGOS TV ON V.TIPO_VIVIENDA=TV.CODIGO " +
					 "	  LEFT OUTER JOIN SIVE.VIVIENDAS_ENCUESTAS VE ON V.CODIGO=VE.VIVIENDA " +
					 "	  LEFT OUTER JOIN GENERAL.CATALOGOS CO ON VE.CONDICION_OCUPACION=CO.CODIGO " +
					 "	  WHERE V.COMUNIDAD=p.pComunidad AND " +
					 "	        (p.pPasivo IS NULL or V.PASIVO=p.pPasivo)) RV " +
					 "    INNER JOIN params p1 ON 1=1 " +
					 " WHERE (RV.FECHA_ENCUESTA IS NULL OR " +
					 "		 RV.FECHA_ENCUESTA =(SELECT MAX(VE1.FECHA_ENCUESTA) " + 
                     " 								FROM SIVE.VIVIENDAS_ENCUESTAS VE1 " + 
                     "                            WHERE RV.CODIGO=VE1.VIVIENDA)) AND " +
                     "        (p1.pNombre IS NULL OR UPPER(RV.JEFE_FAMILIA) LIKE p1.pNombre)");

        query.setParameter(1, pComunidad);
        query.setParameter(2, iPasivo);
        query.setParameter(3, iNombre);
        totalViviendas = ((BigDecimal)query.getSingleResult()).intValue();
        
        return totalViviendas;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ViviendaUltimaEncuesta> ViviendasPorComunidad(
			String pComunidad, String pNombre, Boolean pPasivo,
			int pPaginaActual, int pTotalPorPagina, int pNumRegistros) {

        EntityManager em = jpaResourceBean.getEMF().createEntityManager();

        String iNombre=null;
        if (pNombre!=null) iNombre=pNombre.trim().isEmpty()?null:("%" + pNombre.trim().toUpperCase() + "%");
        BigDecimal iPasivo=null;
        if (pPasivo!=null) iPasivo=pPasivo.booleanValue()?(new BigDecimal(1)):(new BigDecimal(0));
        		
        Query query = em.createNativeQuery("WITH params AS (SELECT ? AS pComunidad, ? AS pPasivo, ? AS pNombre FROM DUAL) " +
					 "SELECT * FROM ( " +
					 "   SELECT V.VIVIENDA_ID, V.CODIGO, TV.VALOR AS TIPO_VIVIENDA, " +
					 "		VE.JEFE_FAMILIA, CO.VALOR AS CONDICION_OCUPACION, " + 
					 "		VE.HABITANTES, VE.FECHA_ENCUESTA, " + 
					 "		CASE " + 
					 "			WHEN (SELECT COUNT(VR.FACTOR_RIESGO) FROM SIVE.VIVIENDAS_RIESGOS VR WHERE VR.VIVIENDA=V.CODIGO)>0 THEN 1 " +
					 "			ELSE 0 " +
					 "		END AS EXISTE_RIESGO, " +
					 "		V.PASIVO, V.LATITUD, V.LONGITUD FROM GENERAL.VIVIENDAS V " +  
					 "	  INNER JOIN params p ON 1=1 " +
					 "	  INNER JOIN GENERAL.CATALOGOS TV ON V.TIPO_VIVIENDA=TV.CODIGO " +
					 "	  LEFT OUTER JOIN SIVE.VIVIENDAS_ENCUESTAS VE ON V.CODIGO=VE.VIVIENDA " +
					 "	  LEFT OUTER JOIN GENERAL.CATALOGOS CO ON VE.CONDICION_OCUPACION=CO.CODIGO " +
					 "	  WHERE V.COMUNIDAD=p.pComunidad AND " +
					 "	        (p.pPasivo IS NULL or V.PASIVO=p.pPasivo)) RV " +
					 "    INNER JOIN params p1 ON 1=1 " +
					 " WHERE (RV.FECHA_ENCUESTA IS NULL OR " +
					 "		 RV.FECHA_ENCUESTA =(SELECT MAX(VE1.FECHA_ENCUESTA) " + 
                     " 								FROM SIVE.VIVIENDAS_ENCUESTAS VE1 " + 
                     "                            WHERE RV.CODIGO=VE1.VIVIENDA)) AND " +
                     "        (p1.pNombre IS NULL OR UPPER(RV.JEFE_FAMILIA) LIKE p1.pNombre) " +
                     " ORDER BY RV.CODIGO ASC");

        query.setParameter(1, pComunidad);
        query.setParameter(2, iPasivo);
        query.setParameter(3, iNombre);

    	List<Object[]> oFilasResultados=query.getResultList();
    	Object[] oFilaResultado;

    	List<ViviendaUltimaEncuesta> oViviendasUltimaEncuesta = new ArrayList<ViviendaUltimaEncuesta>();
    	
    	for(int i=0;i<oFilasResultados.size();i++){
    		ViviendaUltimaEncuesta oViviendaUltimaEncuesta = new ViviendaUltimaEncuesta();
    		oFilaResultado=oFilasResultados.get(i);
    		oViviendaUltimaEncuesta.setViviendaId(((BigDecimal)oFilaResultado[0]).longValue());
    		oViviendaUltimaEncuesta.setCodigo((String)oFilaResultado[1]);
    		oViviendaUltimaEncuesta.setTipoVivienda((String)oFilaResultado[2]);
    		oViviendaUltimaEncuesta.setJefeFamilia((String)oFilaResultado[3]);
    		oViviendaUltimaEncuesta.setCondicionOcupacion((String)oFilaResultado[4]);
    		oViviendaUltimaEncuesta.setHabitantes((BigDecimal)oFilaResultado[5]);
    		oViviendaUltimaEncuesta.setFechaUltimaEncuesta((Date)oFilaResultado[6]);
    		oViviendaUltimaEncuesta.setExistenFactoresRiesgo((BigDecimal)oFilaResultado[7]);
    		oViviendaUltimaEncuesta.setPasivo((BigDecimal)oFilaResultado[8]);
    		oViviendaUltimaEncuesta.setLatitud((BigDecimal)oFilaResultado[9]);
    		oViviendaUltimaEncuesta.setLongitud((BigDecimal)oFilaResultado[10]);
    		oViviendasUltimaEncuesta.add(oViviendaUltimaEncuesta);
    	}
    	
    	return oViviendasUltimaEncuesta;

	}

	@Override
	public InfoResultado Encontrar(String pVivienda) {
        EntityManager oEM = jpaResourceBean.getEMF().createEntityManager();
        InfoResultado oResultado=new InfoResultado();
        try{
            Query query = oEM.createNamedQuery("viviendaPorCodigo");
            query.setParameter("pVivienda", pVivienda);
            query.setMaxResults(1);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            oResultado.setObjeto((Vivienda)query.getSingleResult());
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
