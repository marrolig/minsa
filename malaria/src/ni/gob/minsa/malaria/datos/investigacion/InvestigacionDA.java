package ni.gob.minsa.malaria.datos.investigacion;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionHospitalario;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionLugar;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMalaria;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionSintoma;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionTransfusion;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarAnte;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarInicio;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarOtro;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionService;
import ni.gob.minsa.malaria.soporte.Mensajes;

/**
 * Acceso de Datos para la entidad InvestigacionMalaria.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual será
 * inyectada con un JSF managed bean.
 * <p>
 * @author Felix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 17/10/2013
 * @since jdk1.6.0_21
 */
public class InvestigacionDA implements InvestigacionService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();
    
    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.investigacion.InvestigacionService#Encontrar(long)
     */
	@Override
	public InfoResultado Encontrar(long pInvestigacionId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
			InvestigacionMalaria oInvestigacion = (InvestigacionMalaria) oEM.find(InvestigacionMalaria.class, pInvestigacionId);
			if (oInvestigacion!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oInvestigacion);
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
    /*
     * (non-Javadoc)
     * @see ni.gob.minsa.malaria.servicios.investigacion.InvestigacionService#EncontrarPorMuestreoHematico(long)
     */
    @Override
	public InfoResultado EncontrarPorMuestreoHematico(long pMuestreoHematicoId) {
    	InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		Query query = oEM.createNamedQuery("InvestigacionMalaria.encontrarPorMuestreoHematico");
            query.setParameter("pMuestreoHematicoId", pMuestreoHematicoId);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            InvestigacionMalaria oInvestigacionMalaria = (InvestigacionMalaria)query.getSingleResult();
            
    		if (oInvestigacionMalaria==null ) {
    			oResultado.setMensaje(Mensajes.ENCONTRAR_REGISTRO_NO_EXISTE);
    			oResultado.setOk(false);
    			oResultado.setFilasAfectadas(0);
    			return oResultado;
    		}
    		else {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oInvestigacionMalaria);
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
	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.investigacion.InvestigacionService#Eliminar(long)
	 */
	@Override
	public InfoResultado Eliminar(long pInvestigacionId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		InvestigacionMalaria oInvestigacion = (InvestigacionMalaria)oEM.find(InvestigacionMalaria.class, pInvestigacionId);
    		if (oInvestigacion!=null) {
    			oEM.remove(oInvestigacion);
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
	 * @see ni.gob.minsa.malaria.servicios.investigacion.InvestigacionService#Guardar(ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMalaria, ni.gob.minsa.malaria.modelo.investigacion.InvestigacionSintoma, ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarInicio, ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarAnte, ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarOtro, ni.gob.minsa.malaria.modelo.investigacion.InvestigacionLugar, ni.gob.minsa.malaria.modelo.investigacion.InvestigacionTransfusion, ni.gob.minsa.malaria.modelo.investigacion.InvestigacionHospitalario)
	 */
	@Override
	public InfoResultado Guardar(InvestigacionMalaria pInvestigacionMalaria,
			InvestigacionSintoma pInvestigacionSintoma,
			SintomaLugarInicio pSintomaLugarInicio,
			SintomaLugarAnte pSintomaLugarAnte,
			SintomaLugarOtro pSintomaLugarOtro,
			InvestigacionLugar pInvestigacionLugar,
			InvestigacionTransfusion pInvestigacionTransfusion,
			InvestigacionHospitalario pInvestigacionHospitalario) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.investigacion.InvestigacionService#Agregar(ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMalaria, ni.gob.minsa.malaria.modelo.investigacion.InvestigacionSintoma, ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarInicio, ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarAnte, ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarOtro, ni.gob.minsa.malaria.modelo.investigacion.InvestigacionLugar, ni.gob.minsa.malaria.modelo.investigacion.InvestigacionTransfusion, ni.gob.minsa.malaria.modelo.investigacion.InvestigacionHospitalario)
	 */
	@Override
	public InfoResultado Agregar(InvestigacionMalaria pInvestigacionMalaria,
			InvestigacionSintoma pInvestigacionSintoma,
			SintomaLugarInicio pSintomaLugarInicio,
			SintomaLugarAnte pSintomaLugarAnte,
			SintomaLugarOtro pSintomaLugarOtro,
			InvestigacionLugar pInvestigacionLugar,
			InvestigacionTransfusion pInvestigacionTransfusion,
			InvestigacionHospitalario pInvestigacionHospitalario) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
