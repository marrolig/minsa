package ni.gob.minsa.malaria.datos.investigacion;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityExistsException;
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
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMedicamento;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionSintoma;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionTransfusion;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarAnte;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarInicio;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarOtro;
import ni.gob.minsa.malaria.reglas.InvestigacionValidacion;
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
 @SuppressWarnings("unchecked")
    @Override
	public InfoResultado EncontrarPorMuestreoHematico(long pMuestreoHematicoId) {
    	InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		Query query = oEM.createNamedQuery("InvestigacionMalaria.encontrarPorMuestreoHematico");
            query.setParameter("pMuestreoHematicoId", pMuestreoHematicoId);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
           
			List<InvestigacionMalaria> oInvestigacionMalaria = (List<InvestigacionMalaria>)query.getResultList();
            
    		if (oInvestigacionMalaria==null || oInvestigacionMalaria.size() < 1) {
    			oResultado.setMensaje(Mensajes.ENCONTRAR_REGISTRO_NO_EXISTE);
    			oResultado.setOk(false);
    			oResultado.setFilasAfectadas(0);
    			return oResultado;
    		}
    		else {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oInvestigacionMalaria.get(0));
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
			List<SintomaLugarAnte> pSintomaLugarAnte,
			List<SintomaLugarOtro> pSintomaLugarOtro,
			List<InvestigacionMedicamento> pInvestigacionMedicamento,
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
			List<SintomaLugarAnte> pSintomaLugarAnte,
			List<SintomaLugarOtro> pSintomaLugarOtro,
			List<InvestigacionMedicamento> pInvestigacionMedicamento,
			InvestigacionLugar pInvestigacionLugar,
			InvestigacionTransfusion pInvestigacionTransfusion,
			InvestigacionHospitalario pInvestigacionHospitalario) {
		
		InfoResultado oResultado=new InfoResultado();
		
		oResultado=InvestigacionValidacion.validarInvestigacionMalaria(
				pInvestigacionMalaria,
				pInvestigacionSintoma,
				pInvestigacionMedicamento,
				pInvestigacionTransfusion,
				pInvestigacionHospitalario);
		if (!oResultado.isOk()) return oResultado;
		
		
		
		EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
		@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

        try{
        	
        	oEM.persist(pInvestigacionMalaria);
           
        	//Agregando los objetos relacionados a Investigación de sintomas.
            if(pInvestigacionSintoma!=null){
            	if(pInvestigacionSintoma.getSintomatico() == BigDecimal.valueOf(1)){
            		pInvestigacionSintoma.setInvestigacionMalaria(pInvestigacionMalaria);
            		oEM.persist(pInvestigacionSintoma);
            		
            		if(pSintomaLugarAnte!=null){
            			for(SintomaLugarAnte oLugarAnte:pSintomaLugarAnte){
                			if(oLugarAnte.getInvestigacionesSintoma()!=null && oLugarAnte.getFechaUltima()!=null){
                				oLugarAnte.setInvestigacionesSintoma(pInvestigacionSintoma);
                    			oEM.persist(oLugarAnte);
                			}
                		}
            		}
            		
            		if(pSintomaLugarOtro!=null){
            			for(SintomaLugarOtro oLugarOtro:pSintomaLugarOtro){
                			if(oLugarOtro.getInvestigacionesSintoma()!=null && oLugarOtro.getEstadia()!=null 
                					&& oLugarOtro.getMesInicio()!=null && oLugarOtro.getAñoInicio()!=null){
                				oLugarOtro.setInvestigacionesSintoma(pInvestigacionSintoma);
                    			oEM.persist(oLugarOtro);
                			}
                		}
            		}
            	}
            }
            
            //Agregando los objetos relacionados a Investigación medicamentos.
            if(pInvestigacionMedicamento!=null){
            	for(InvestigacionMedicamento oMedicamento:pInvestigacionMedicamento){
        			if(oMedicamento.getInvestigacionesMalaria()!=null && oMedicamento.getMedicamento()!=null){
        				oMedicamento.setInvestigacionesMalaria(pInvestigacionMalaria);
            			oEM.persist(oMedicamento);
        			}
        		}
            }
            
           //Agregando los objetos relacionados a Investigación Lugar.
            if(pInvestigacionLugar!=null){
            	pInvestigacionLugar.setInvestigacionMalaria(pInvestigacionMalaria);
            	oEM.persist(pInvestigacionLugar);
            }
           //Agregando los objetos relacionados a Investigación Transfusión.
            if(pInvestigacionTransfusion!=null){
            	pInvestigacionTransfusion.setInvestigacionMalaria(pInvestigacionMalaria);
            	oEM.persist(pInvestigacionTransfusion);
            }
          //Agregando los objetos relacionados a atención hospitalaria.
            if(pInvestigacionHospitalario!=null){
            	pInvestigacionHospitalario.setInvestigacionMalaria(pInvestigacionMalaria);
            	oEM.persist(pInvestigacionHospitalario);
            }
          
            oEM.getTransaction().commit();
            oResultado.setObjeto(pInvestigacionMalaria);
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

}
