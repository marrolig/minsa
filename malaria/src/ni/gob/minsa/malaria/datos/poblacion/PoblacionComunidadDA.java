// -----------------------------------------------
// PoblacionComunidadDA.java
// -----------------------------------------------
package ni.gob.minsa.malaria.datos.poblacion;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.poblacion.PoblacionComunidad;
import ni.gob.minsa.malaria.modelo.poblacion.Sector;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.PoblacionAnualComunidad;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.PoblacionAnualSector;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.ResumenPoblacion;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.UnidadSituacion;
import ni.gob.minsa.malaria.servicios.poblacion.PoblacionComunidadService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.TipoArea;

/**
 * Acceso de Datos para la entidad {@link PoblacionComunidad}.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 08/05/2012
 * @since jdk1.6.0_21
 */
public class PoblacionComunidadDA implements PoblacionComunidadService {

	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public PoblacionComunidadDA() {  // clase no instanciable
    }

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.poblacion.PoblacionComunidadService#PoblacionComunidadesPorSector(java.lang.String, java.lang.Integer, boolean)
     */
    @SuppressWarnings("unchecked")
	@Override
	public List<PoblacionAnualComunidad> PoblacionComunidadesPorSector(String pSector, Integer pAño, boolean pPlan) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
        	Query query = null;
            
        	if (pPlan) {
        		query = em.createNativeQuery("WITH params AS (SELECT ? AS pAÑO, ? AS pSECTOR FROM dual) " +
        									 "SELECT p.pAÑO, t3.POBLACION_COMUNIDAD_ID, t0.COMUNIDAD_ID, t0.CODIGO, t0.NOMBRE, t0.TIPO_AREA, t3.HOGARES, t3.MANZANAS, t3.POBLACION, t3.VIVIENDAS " + 
        									 "		FROM GENERAL.COMUNIDADES t0 " +  
        									 "		INNER JOIN params p ON 1=1 " + 
        									 "		LEFT OUTER JOIN GENERAL.POBLACION_COMUNIDADES t3 ON (t3.COMUNIDAD = t0.CODIGO AND t3.AÑO=p.pAÑO) " +
        									 "WHERE ((t0.SECTOR = p.pSECTOR) AND (t0.PASIVO = '0')) "+
        									 "ORDER BY p.pAÑO, t0.CODIGO ASC");
        		query.setParameter(1, pAño);
        		query.setParameter(2, pSector);
        	} else {
        		query = em.createNamedQuery("poblacionComunidadesPorSector");
                query.setParameter("pAño", pAño);
                query.setParameter("pSector", pSector);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        	}

        	if (pPlan) {
            	List<Object[]> oFilasResultados=query.getResultList();
            	Object[] oFilaResultado;

            	List<PoblacionAnualComunidad> oPoblacionAnualComunidades = new ArrayList<PoblacionAnualComunidad>();
            	
            	for(int i=0;i<oFilasResultados.size();i++){
            		PoblacionAnualComunidad oPoblacionAnualComunidad = new PoblacionAnualComunidad();
            		oFilaResultado=oFilasResultados.get(i);
            		oPoblacionAnualComunidad.setAño((BigDecimal)oFilaResultado[0]);
            		if (oFilaResultado[1]!=null) oPoblacionAnualComunidad.setPoblacionComunidadId(((BigDecimal)oFilaResultado[1]).longValue());
            		oPoblacionAnualComunidad.setComunidadId(((BigDecimal)oFilaResultado[2]).longValue());
            		oPoblacionAnualComunidad.setCodigo((String)oFilaResultado[3]);
            		oPoblacionAnualComunidad.setNombre((String)oFilaResultado[4]);
            		String iTipoArea=null;
            		if (oFilaResultado[5]==null) iTipoArea="N/D";
            		if (oFilaResultado[5]!=null && ((String)oFilaResultado[5]).equals("U")) iTipoArea=TipoArea.URBANO.getNombre();
            		if (oFilaResultado[5]!=null && ((String)oFilaResultado[5]).equals("R")) iTipoArea=TipoArea.RURAL.getNombre();
            		oPoblacionAnualComunidad.setTipoArea(iTipoArea);
            		oPoblacionAnualComunidad.setHogares((BigDecimal)oFilaResultado[6]);
            		oPoblacionAnualComunidad.setManzanas((BigDecimal)oFilaResultado[7]);
            		oPoblacionAnualComunidad.setPoblacion((BigDecimal)oFilaResultado[8]);
            		oPoblacionAnualComunidad.setViviendas((BigDecimal)oFilaResultado[9]);
            		oPoblacionAnualComunidades.add(oPoblacionAnualComunidad);
            	}
            	return oPoblacionAnualComunidades;
             } else {
            	return(query.getResultList());
             }

            		             
        }finally{
            em.close();
        }
	}

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.poblacion.PoblacionComunidadService#PoblacionSectoresPorUnidad(long, java.lang.Integer, boolean)
     */
    @SuppressWarnings("unchecked")
	@Override
	public List<PoblacionAnualSector> PoblacionSectoresPorUnidad(long pUnidad, Integer pAño, boolean pPlan) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
        	Query query = null;
            
        	if (pPlan) {
        		query = em.createNativeQuery("WITH params AS (SELECT ? AS pAÑO, ? AS pUNIDAD FROM dual) " +
        									 "SELECT p.pAÑO, t0.SECTOR_ID, t0.CODIGO, t0.NOMBRE, COUNT(t4.COMUNIDAD_ID), SUM(t3.HOGARES), SUM(t3.MANZANAS), SUM(t3.POBLACION), SUM(t3.VIVIENDAS) " + 
        									 "   FROM GENERAL.SECTORES t0 " + 
        									 "        INNER JOIN params p ON 1=1 "+
        									 "        LEFT OUTER JOIN GENERAL.COMUNIDADES t4 ON (t4.SECTOR = t0.CODIGO) "+ 
        									 "        LEFT OUTER JOIN GENERAL.POBLACION_COMUNIDADES t3 ON (t3.COMUNIDAD = t4.CODIGO AND t3.AÑO=p.pAÑO) " +
        									 "   WHERE ((t0.UNIDAD = p.pUNIDAD) AND (t4.PASIVO = '0')) " +
        									 "   GROUP BY p.pAÑO, t0.SECTOR_ID, t0.CODIGO, t0.NOMBRE " +
                  							 "   ORDER BY t0.CODIGO ASC");
                query.setParameter(1, pAño);
        		query.setParameter(2, pUnidad);
        	} else {
        		query = em.createNamedQuery("poblacionAnualPorSector");
                query.setParameter("pAño", pAño);
        		query.setParameter("pUnidad", pUnidad);
        		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        	}

        	if (pPlan) {
            	List<Object[]> oFilasResultados=query.getResultList();
            	Object[] oFilaResultado;

            	List<PoblacionAnualSector> oPoblacionAnualSectores = new ArrayList<PoblacionAnualSector>();
            	
            	for(int i=0;i<oFilasResultados.size();i++){
            		PoblacionAnualSector oPoblacionAnualSector = new PoblacionAnualSector();
            		oFilaResultado=oFilasResultados.get(i);
            		oPoblacionAnualSector.setAño((BigDecimal)oFilaResultado[0]);
            		oPoblacionAnualSector.setSectorId(((BigDecimal)oFilaResultado[1]).longValue());
            		oPoblacionAnualSector.setCodigo((String)oFilaResultado[2]);
            		oPoblacionAnualSector.setNombre((String)oFilaResultado[3]);
            		oPoblacionAnualSector.setComunidades(((BigDecimal)oFilaResultado[4]).intValueExact());
            		oPoblacionAnualSector.setHogares((BigDecimal)oFilaResultado[5]);
            		oPoblacionAnualSector.setManzanas((BigDecimal)oFilaResultado[6]);
            		oPoblacionAnualSector.setPoblacion((BigDecimal)oFilaResultado[7]);
            		oPoblacionAnualSector.setViviendas((BigDecimal)oFilaResultado[8]);
            		oPoblacionAnualSectores.add(oPoblacionAnualSector);
            	}
            	return oPoblacionAnualSectores;
             } else {
            	return(query.getResultList());
             }
            		             
        }finally{
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
	@Override
	public List<ResumenPoblacion> ResumenPoblacionSectoresPorUnidad(long pUnidad, Integer pAño, boolean pPlan) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
        	Query query = null;
        	List<ResumenPoblacion> oResumenPoblacionSectores = new ArrayList<ResumenPoblacion>();
            
        	if (pPlan) {
        		query = em.createNativeQuery("WITH params AS (SELECT ? AS pAÑO, ? AS pUNIDAD FROM dual)" +
        								     "SELECT p.pAÑO, t4.TIPO_AREA, COUNT(t4.COMUNIDAD_ID), SUM(t3.HOGARES), SUM(t3.MANZANAS), SUM(t3.POBLACION), SUM(t3.VIVIENDAS) " + 
        								     "FROM GENERAL.SECTORES t0 " + 
        								     "		INNER JOIN params p ON 1=1 " + 
        								     "		LEFT OUTER JOIN GENERAL.COMUNIDADES t4 ON (t4.SECTOR = t0.CODIGO) " + 
        								     "		LEFT OUTER JOIN GENERAL.POBLACION_COMUNIDADES t3 ON (t3.COMUNIDAD = t4.CODIGO AND t3.AÑO=p.pAÑO)" + 
        								     "WHERE ((t0.UNIDAD = p.pUNIDAD) AND (t4.PASIVO = '0')) " + 
        								     "GROUP BY p.pAÑO, t4.TIPO_AREA " +
        								     "ORDER BY t4.TIPO_AREA ASC");
                query.setParameter(1, pAño);
        		query.setParameter(2, pUnidad);

        	} else {
        		query = em.createNamedQuery("resumenPoblacionSector");
                query.setParameter("pAño", pAño);
                query.setParameter("pUnidad", pUnidad);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        	}

        	if (pPlan) {
            	List<Object[]> oFilasResultados=query.getResultList();
            	Object[] oFilaResultado;
            	            	
            	for(int i=0;i<oFilasResultados.size();i++){
            		ResumenPoblacion oResumenPoblacion = new ResumenPoblacion();
            		oFilaResultado=oFilasResultados.get(i);
            		oResumenPoblacion.setAño((BigDecimal)oFilaResultado[0]);
            		String iTipoArea=null;
            		if (oFilaResultado[1]==null) iTipoArea="N/D";
            		if (oFilaResultado[1]!=null && ((String)oFilaResultado[1]).equals("U")) iTipoArea="Urbano";
            		if (oFilaResultado[1]!=null && ((String)oFilaResultado[1]).equals("R")) iTipoArea="Rural";
            		oResumenPoblacion.setTipoArea(iTipoArea); 
            		oResumenPoblacion.setComunidades(Long.valueOf(((BigDecimal)oFilaResultado[2]).longValue()));
            		oResumenPoblacion.setHogares((BigDecimal)oFilaResultado[3]);
            		oResumenPoblacion.setManzanas((BigDecimal)oFilaResultado[4]);
            		oResumenPoblacion.setPoblacion((BigDecimal)oFilaResultado[5]);
            		oResumenPoblacion.setViviendas((BigDecimal)oFilaResultado[6]);
            		oResumenPoblacionSectores.add(oResumenPoblacion);
            	}
             } else {
            	 oResumenPoblacionSectores=query.getResultList();
            	 for (ResumenPoblacion oResumenPoblacion:oResumenPoblacionSectores) {
            		 if (oResumenPoblacion.getTipoArea()!=null) {
            			 if (oResumenPoblacion.getTipoArea().equals("U")) oResumenPoblacion.setTipoArea(TipoArea.URBANO.getNombre());
            			 if (oResumenPoblacion.getTipoArea().equals("R")) oResumenPoblacion.setTipoArea(TipoArea.RURAL.getNombre());
            		 } else {
            			 oResumenPoblacion.setTipoArea("N/D");
            		 }
            	 }
            	 
             }
        	
        	 // totaliza y aplica porcentajes

        	boolean oUrbano=false;
        	boolean oRural=false;

        	 ResumenPoblacion oTotalPoblacionSector=new ResumenPoblacion();
        	 for (int i=0;i<oResumenPoblacionSectores.size();i++) {

        		 ResumenPoblacion oResumenPoblacion = oResumenPoblacionSectores.get(i);
        		 if (oResumenPoblacion.getTipoArea()!=null) {
        			 if (oResumenPoblacion.getTipoArea().equals("Urbano")) oUrbano=true;
        			 if (oResumenPoblacion.getTipoArea().equals("Rural")) oRural=true;
        		 } 
        		 
        		 if (oResumenPoblacion.getComunidades()!=null) {
        			 oTotalPoblacionSector.setComunidades(oTotalPoblacionSector.getComunidades()==null?oResumenPoblacion.getComunidades():oTotalPoblacionSector.getComunidades()+oResumenPoblacion.getComunidades());
        		 }
        		 if (oResumenPoblacion.getHogares()!=null) {
        			 oTotalPoblacionSector.setHogares(oTotalPoblacionSector.getHogares()==null?oResumenPoblacion.getHogares():oTotalPoblacionSector.getHogares().add(oResumenPoblacion.getHogares()));
        		 }
        		 if (oResumenPoblacion.getManzanas()!=null) {
        			 oTotalPoblacionSector.setManzanas(oTotalPoblacionSector.getManzanas()==null?oResumenPoblacion.getManzanas():oTotalPoblacionSector.getManzanas().add(oResumenPoblacion.getManzanas()));
        		 }
        		 if (oResumenPoblacion.getViviendas()!=null) {
        			 oTotalPoblacionSector.setViviendas(oTotalPoblacionSector.getViviendas()==null?oResumenPoblacion.getViviendas():oTotalPoblacionSector.getViviendas().add(oResumenPoblacion.getViviendas()));
        		 }
        		 if (oResumenPoblacion.getPoblacion()!=null) {
        			 oTotalPoblacionSector.setPoblacion(oTotalPoblacionSector.getPoblacion()==null?oResumenPoblacion.getPoblacion():oTotalPoblacionSector.getPoblacion().add(oResumenPoblacion.getPoblacion()));
        		 }
        	 }
        	 
        	 for (int i=0;i<oResumenPoblacionSectores.size();i++) {
        		 ResumenPoblacion oResumenPoblacion = oResumenPoblacionSectores.get(i);
        		 
        		 if (oTotalPoblacionSector.getComunidades()!=0) {
        			 BigDecimal oPorcentaje=null;
        			 oPorcentaje=new BigDecimal(100.0 * oResumenPoblacion.getComunidades()/oTotalPoblacionSector.getComunidades());
        			 oResumenPoblacion.setPorcentajeComunidades(oPorcentaje.setScale(1, RoundingMode.HALF_UP));
        		 }
        		 
        		 if (oResumenPoblacion.getHogares()!=null) oResumenPoblacion.setPorcentajeHogares(oResumenPoblacion.getHogares().multiply(new BigDecimal(100)).divide(oTotalPoblacionSector.getHogares(),1,RoundingMode.HALF_UP));
        		 if (oResumenPoblacion.getManzanas()!=null) oResumenPoblacion.setPorcentajeManzanas(oResumenPoblacion.getManzanas().multiply(new BigDecimal(100)).divide(oTotalPoblacionSector.getManzanas(),1,RoundingMode.HALF_UP));
        		 if (oResumenPoblacion.getViviendas()!=null) oResumenPoblacion.setPorcentajeViviendas(oResumenPoblacion.getViviendas().multiply(new BigDecimal(100)).divide(oTotalPoblacionSector.getViviendas(),1,RoundingMode.HALF_UP));
        		 if (oResumenPoblacion.getPoblacion()!=null) oResumenPoblacion.setPorcentajePoblacion(oResumenPoblacion.getPoblacion().multiply(new BigDecimal(100)).divide(oTotalPoblacionSector.getPoblacion(),1,RoundingMode.HALF_UP));
        	 }
        	 
        	 if (!oUrbano) {
        		 ResumenPoblacion oResumenPoblacion = new ResumenPoblacion();
        		 oResumenPoblacion.setAño(new BigDecimal(pAño.intValue()));
        		 oResumenPoblacion.setTipoArea("Urbano");
        		 oResumenPoblacionSectores.add(oResumenPoblacion);
        	 }

        	 if (!oRural) {
        		 ResumenPoblacion oResumenPoblacion = new ResumenPoblacion();
        		 oResumenPoblacion.setAño(new BigDecimal(pAño.intValue()));
        		 oResumenPoblacion.setTipoArea("Rural");
        		 oResumenPoblacionSectores.add(oResumenPoblacion);
        	 }

        	 return oResumenPoblacionSectores;

        }finally{
            em.close();
        }
    }

    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.poblacion.PoblacionComunidadService#Encontrar(long)
     */
	@Override
	public InfoResultado Encontrar(long pPoblacionComunidadId) {

		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		PoblacionComunidad oPoblacionComunidad = (PoblacionComunidad)oEM.find(ni.gob.minsa.malaria.modelo.poblacion.PoblacionComunidad.class, pPoblacionComunidadId);
    		if (oPoblacionComunidad!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oPoblacionComunidad);
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
	 * @see ni.gob.minsa.malaria.servicios.poblacion.PoblacionComunidadService#EncontrarPorCodigo(java.lang.String, java.lang.Integer)
	 */
	@Override
	public InfoResultado EncontrarPorCodigo(String pComunidad, Integer pAño) {
        EntityManager oEM = jpaResourceBean.getEMF().createEntityManager();
        InfoResultado oResultado=new InfoResultado();
        try{
            Query query = oEM.createNamedQuery("xxxxxx");   //TODO
            query.setParameter("pComunidad", pComunidad);
            query.setParameter("pAño", pAño);
            query.setMaxResults(1);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            //retorna el resultado del query
            oResultado.setObjeto((Sector)query.getSingleResult());
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

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.PoblacionComunidadService#Guardar(ni.gob.minsa.malaria.modelo.poblacion.PoblacionComunidad)
	 */
	@Override
	public InfoResultado Guardar(PoblacionComunidad pPoblacionComunidad) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
        	PoblacionComunidad oDetachedPoblacionComunidad = (PoblacionComunidad)oEM.find(ni.gob.minsa.malaria.modelo.poblacion.PoblacionComunidad.class, pPoblacionComunidad.getPoblacionComunidadId());
        	PoblacionComunidad oPoblacionComunidad=oEM.merge(oDetachedPoblacionComunidad);
        	oPoblacionComunidad.setPoblacion(pPoblacionComunidad.getPoblacion());

			if (pPoblacionComunidad.getHogares()!=null && pPoblacionComunidad.getHogares().compareTo(new BigDecimal(0))==0) 	pPoblacionComunidad.setHogares(null);
			if (pPoblacionComunidad.getViviendas()!=null && pPoblacionComunidad.getViviendas().compareTo(new BigDecimal(0))==0) pPoblacionComunidad.setViviendas(null);
			if (pPoblacionComunidad.getManzanas()!=null && pPoblacionComunidad.getManzanas().compareTo(new BigDecimal(0))==0) pPoblacionComunidad.setManzanas(null);

			oPoblacionComunidad.setViviendas(pPoblacionComunidad.getViviendas());
			oPoblacionComunidad.setHogares(pPoblacionComunidad.getHogares());
			oPoblacionComunidad.setManzanas(pPoblacionComunidad.getManzanas());
        	
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
    		
    	} catch (PersistenceException iExPersistencia) {
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
	
	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.PoblacionComunidadService#Agregar(ni.gob.minsa.malaria.modelo.poblacion.PoblacionComunidad)
	 */
	@Override
	public InfoResultado Agregar(PoblacionComunidad pPoblacionComunidad) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
		@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
            oEM.persist(pPoblacionComunidad);
            oEM.getTransaction().commit();
            oResultado.setObjeto(pPoblacionComunidad);
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
    		
    	} catch (PersistenceException iExPersistencia) {
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
	
	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.PoblacionComunidadService#Eliminar(long)
	 */
	@Override
	public InfoResultado Eliminar(long pPoblacionComunidadId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		PoblacionComunidad oPoblacionComunidad = (PoblacionComunidad)oEM.find(ni.gob.minsa.malaria.modelo.poblacion.PoblacionComunidad.class, pPoblacionComunidadId);
    		if (oPoblacionComunidad!=null) {
    			oEM.remove(oPoblacionComunidad);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<ResumenPoblacion> ResumenPoblacionComunidadesPorSector(
			String pSector, Integer pAño, boolean pPlan) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
        	Query query = null;
        	List<ResumenPoblacion> oResumenPoblacionComunidades = new ArrayList<ResumenPoblacion>();
            
        	if (pPlan) {
        		query = em.createNativeQuery("WITH params AS (SELECT ? AS pAÑO, ? AS pSECTOR FROM dual) " +
					     "SELECT p.pAÑO, t4.TIPO_AREA, COUNT(t4.COMUNIDAD_ID), SUM(t3.HOGARES), SUM(t3.MANZANAS), SUM(t3.POBLACION), SUM(t3.VIVIENDAS) " + 
					     "FROM GENERAL.COMUNIDADES t4 " + 
					     "		INNER JOIN params p ON 1=1 " + 
					     "		LEFT OUTER JOIN GENERAL.POBLACION_COMUNIDADES t3 ON (t3.COMUNIDAD = t4.CODIGO AND t3.AÑO=p.pAÑO) " + 
					     "WHERE ((t4.SECTOR = p.pSECTOR) AND (t4.PASIVO = '0')) " + 
					     "GROUP BY p.pAÑO, t4.TIPO_AREA " +
					     "ORDER BY t4.TIPO_AREA ASC");
        				 query.setParameter(1, pAño);
        				 query.setParameter(2, pSector);

        	} else {
        		query = em.createNamedQuery("resumenPoblacionComunidad");
                query.setParameter("pAño", pAño);
                query.setParameter("pSector", pSector);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        	}
        	
        	if (pPlan) {
            	List<Object[]> oFilasResultados=query.getResultList();
            	Object[] oFilaResultado;
            	            	
            	for(int i=0;i<oFilasResultados.size();i++){
            		ResumenPoblacion oResumenPoblacion = new ResumenPoblacion();
            		oFilaResultado=oFilasResultados.get(i);
            		oResumenPoblacion.setAño((BigDecimal)oFilaResultado[0]);
            		String iTipoArea=null;
            		if (oFilaResultado[1]==null) iTipoArea="N/D";
            		if (oFilaResultado[1]!=null && ((String)oFilaResultado[1]).equals("U")) iTipoArea="Urbano";
            		if (oFilaResultado[1]!=null && ((String)oFilaResultado[1]).equals("R")) iTipoArea="Rural";
            		oResumenPoblacion.setTipoArea(iTipoArea);
            		oResumenPoblacion.setComunidades(Long.valueOf(((BigDecimal)oFilaResultado[2]).longValue()));
            		oResumenPoblacion.setHogares((BigDecimal)oFilaResultado[3]);
            		oResumenPoblacion.setManzanas((BigDecimal)oFilaResultado[4]);
            		oResumenPoblacion.setPoblacion((BigDecimal)oFilaResultado[5]);
            		oResumenPoblacion.setViviendas((BigDecimal)oFilaResultado[6]);
            		oResumenPoblacionComunidades.add(oResumenPoblacion);
            	}
             } else {
            	 oResumenPoblacionComunidades=query.getResultList();
            	 for (ResumenPoblacion oResumenPoblacion:oResumenPoblacionComunidades) {
            		 if (oResumenPoblacion.getTipoArea()!=null) {
            			 if (oResumenPoblacion.getTipoArea().equals("U")) oResumenPoblacion.setTipoArea(TipoArea.URBANO.getNombre());
            			 if (oResumenPoblacion.getTipoArea().equals("R")) oResumenPoblacion.setTipoArea(TipoArea.RURAL.getNombre());
            		 } else {
            			 oResumenPoblacion.setTipoArea("N/D");
            		 }
            	 }
             }
        	
        	 // totaliza y aplica porcentajes
        	 
        	 boolean oUrbano=false;
        	 boolean oRural=false;
        	 
        	 ResumenPoblacion oTotalPoblacionComunidad=new ResumenPoblacion();
        	 for (int i=0;i<oResumenPoblacionComunidades.size();i++) {
        		 ResumenPoblacion oResumenPoblacion = oResumenPoblacionComunidades.get(i);
        		 if (oResumenPoblacion.getTipoArea().equals("Urbano")) oUrbano=true;
        		 if (oResumenPoblacion.getTipoArea().equals("Rural")) oRural=true;
        		 if (oResumenPoblacion.getComunidades()!=null) {
        			 oTotalPoblacionComunidad.setComunidades(oTotalPoblacionComunidad.getComunidades()==null?oResumenPoblacion.getComunidades():oTotalPoblacionComunidad.getComunidades()+oResumenPoblacion.getComunidades());
        		 }
        		 if (oResumenPoblacion.getHogares()!=null) {
        			 oTotalPoblacionComunidad.setHogares(oTotalPoblacionComunidad.getHogares()==null?oResumenPoblacion.getHogares():oTotalPoblacionComunidad.getHogares().add(oResumenPoblacion.getHogares()));
        		 }
        		 if (oResumenPoblacion.getManzanas()!=null) {
        			 oTotalPoblacionComunidad.setManzanas(oTotalPoblacionComunidad.getManzanas()==null?oResumenPoblacion.getManzanas():oTotalPoblacionComunidad.getManzanas().add(oResumenPoblacion.getManzanas()));
        		 }
        		 if (oResumenPoblacion.getViviendas()!=null) {
        			 oTotalPoblacionComunidad.setViviendas(oTotalPoblacionComunidad.getViviendas()==null?oResumenPoblacion.getViviendas():oTotalPoblacionComunidad.getViviendas().add(oResumenPoblacion.getViviendas()));
        		 }
        		 if (oResumenPoblacion.getPoblacion()!=null) {
        			 oTotalPoblacionComunidad.setPoblacion(oTotalPoblacionComunidad.getPoblacion()==null?oResumenPoblacion.getPoblacion():oTotalPoblacionComunidad.getPoblacion().add(oResumenPoblacion.getPoblacion()));
        		 }
        	 }
        	 
        	 for (int i=0;i<oResumenPoblacionComunidades.size();i++) {
        		 ResumenPoblacion oResumenPoblacion = oResumenPoblacionComunidades.get(i);
        		 
        		 if (oTotalPoblacionComunidad.getComunidades()!=null && oTotalPoblacionComunidad.getComunidades()!=0) {
        			 BigDecimal oPorcentaje=null;
        			 oPorcentaje=new BigDecimal(100.0 * oResumenPoblacion.getComunidades()/oTotalPoblacionComunidad.getComunidades());
        			 oResumenPoblacion.setPorcentajeComunidades(oPorcentaje.setScale(1, RoundingMode.HALF_UP));
        		 }
        		 
        		 if (oResumenPoblacion.getHogares()!=null) oResumenPoblacion.setPorcentajeHogares(oResumenPoblacion.getHogares().multiply(new BigDecimal(100)).divide(oTotalPoblacionComunidad.getHogares(),1,RoundingMode.HALF_UP));
        		 if (oResumenPoblacion.getManzanas()!=null) oResumenPoblacion.setPorcentajeManzanas(oResumenPoblacion.getManzanas().multiply(new BigDecimal(100)).divide(oTotalPoblacionComunidad.getManzanas(),1,RoundingMode.HALF_UP));
        		 if (oResumenPoblacion.getViviendas()!=null) oResumenPoblacion.setPorcentajeViviendas(oResumenPoblacion.getViviendas().multiply(new BigDecimal(100)).divide(oTotalPoblacionComunidad.getViviendas(),1,RoundingMode.HALF_UP));
        		 if (oResumenPoblacion.getPoblacion()!=null) oResumenPoblacion.setPorcentajePoblacion(oResumenPoblacion.getPoblacion().multiply(new BigDecimal(100)).divide(oTotalPoblacionComunidad.getPoblacion(),1,RoundingMode.HALF_UP));
        	 }
        	 
        	 if (!oUrbano) {
        		 ResumenPoblacion oResumenPoblacion = new ResumenPoblacion();
        		 oResumenPoblacion.setAño(new BigDecimal(pAño.intValue()));
        		 oResumenPoblacion.setTipoArea(TipoArea.URBANO.getNombre());
        		 oResumenPoblacionComunidades.add(oResumenPoblacion);
        	 }

        	 if (!oRural) {
        		 ResumenPoblacion oResumenPoblacion = new ResumenPoblacion();
        		 oResumenPoblacion.setAño(new BigDecimal(pAño.intValue()));
        		 oResumenPoblacion.setTipoArea(TipoArea.RURAL.getNombre());
        		 oResumenPoblacionComunidades.add(oResumenPoblacion);
        	 }

        	 return oResumenPoblacionComunidades;
            		             
        }finally{
            em.close();
        }
     }

	@Override
	public InfoResultado EliminarPorSector(String pSector,Integer pAño) {

        InfoResultado oResultado=new InfoResultado();
        
		EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
            Query query = oEM.createQuery("delete from PoblacionComunidad pc where pc.comunidad.sector.codigo=:pSector and pc.año=:pAño");
            query.setParameter("pSector", pSector);
            query.setParameter("pAño", pAño);
            int iEliminado=query.executeUpdate();
            oEM.getTransaction().commit();
			oResultado.setFilasAfectadas(iEliminado);
			oResultado.setOk(true);
            return oResultado;
        } 
        catch (PersistenceException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("EliminarPorSector");
    		oResultado.setMensaje(Mensajes.ELIMINAR_RESTRICCION);
    		oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
    		oResultado.setOk(false);
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
	}

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.poblacion.PoblacionComunidadService#SituacionUnidadesPorEntidad(long, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UnidadSituacion> SituacionUnidadesPorEntidad(
			long pEntidadAdtva, Integer pAño) {

        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
        	Query query = null;
        	List<UnidadSituacion> oSituacionUnidades = new ArrayList<UnidadSituacion>();
            
        	query = em.createNativeQuery("WITH params AS (SELECT ? AS pAÑO, ? AS pENTIDAD FROM dual) " +
					     "SELECT DP.NOMBRE MUNICIPIO, U.NOMBRE NOMBRE, TU.NOMBRE TIPO, " +
					     "		COUNT(C.CODIGO) EXISTENTES,COUNT(PC.COMUNIDAD) CON_REGISTRO, " + 
					     "		SUM(PC.POBLACION) POBLACION, " + 
        				 "		CASE WHEN CUP.UNIDAD IS NOT NULL THEN 1 ELSE 0 END CONFIRMADO " +
        				 "	FROM GENERAL.UNIDADES U " + 
        				 "       INNER JOIN GENERAL.SECTORES S ON S.UNIDAD=U.CODIGO " +
        				 " 		 INNER JOIN GENERAL.TIPOS_UNIDADES TU ON TU.CODIGO=U.TIPO_UNIDAD " +
        				 "		 INNER JOIN GENERAL.DIVISIONPOLITICA DP ON U.MUNICIPIO=DP.CODIGO_NACIONAL " +
        				 "		 INNER JOIN params p ON 1=1 " +
        				 "		 LEFT OUTER JOIN GENERAL.COMUNIDADES C ON C.SECTOR=S.CODIGO " +
        				 "		 LEFT OUTER JOIN GENERAL.POBLACION_COMUNIDADES PC ON PC.COMUNIDAD=C.CODIGO AND PC.AÑO=p.pAño " +
        				 "		 LEFT OUTER JOIN GENERAL.CONTROL_UNIDAD_POBLACION CUP ON CUP.UNIDAD=U.CODIGO AND CUP.AÑO=p.pAño " +
        				 "	WHERE U.ENTIDAD_ADTVA=p.pEntidad " +
        				 "	GROUP BY DP.NOMBRE, TU.NOMBRE, U.NOMBRE, CASE WHEN CUP.UNIDAD IS NOT NULL THEN 1 ELSE 0 END " +
        				 "	ORDER BY DP.NOMBRE, TU.NOMBRE, U.NOMBRE");
        				 query.setParameter(1, pAño);
        				 query.setParameter(2, pEntidadAdtva);

        	List<Object[]> oFilasResultados=query.getResultList();
        	Object[] oFilaResultado;
        	            	            	
        	for(int i=0;i<oFilasResultados.size();i++){
        		UnidadSituacion oUnidadSituacion = new UnidadSituacion();
        	    oFilaResultado=oFilasResultados.get(i);
        	    oUnidadSituacion.setMunicipio((String)oFilaResultado[0]);
        	    oUnidadSituacion.setUnidad((String)oFilaResultado[1]);
        	    oUnidadSituacion.setTipoUnidad((String)oFilaResultado[2]);
        	    oUnidadSituacion.setComunidadesExistentes((BigDecimal)oFilaResultado[3]);
        	    oUnidadSituacion.setComunidadesConRegistro((BigDecimal)oFilaResultado[4]);
        	    oUnidadSituacion.setPoblacion((BigDecimal)oFilaResultado[5]);
        	    oUnidadSituacion.setSituacion((BigDecimal)oFilaResultado[6]);
        	    
        	    // situación = 1 : confirmada
        	    // situación = 2 : sin confirmar
        	    // situación = 3 : sin población
        	    
        	    if (oUnidadSituacion.getSituacion()==null || oUnidadSituacion.getSituacion().equals(new BigDecimal(0))) {
        	    	if ((oUnidadSituacion.getPoblacion()!=null) && (oUnidadSituacion.getPoblacion().compareTo(new BigDecimal(0)))==1) {
        	    		oUnidadSituacion.setSituacion(new BigDecimal(2));
        	    	} else {
        	    		oUnidadSituacion.setSituacion(new BigDecimal(3)) ;
        	    	}
        	    }
        	            	    
        	    oSituacionUnidades.add(oUnidadSituacion);
        	}
        	return oSituacionUnidades;
        } finally {
            em.close();
        }
	}

}
