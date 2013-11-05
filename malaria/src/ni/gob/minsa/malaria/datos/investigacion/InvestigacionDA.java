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
	@SuppressWarnings("unchecked")
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
		
		InfoResultado oResultado=new InfoResultado();
		oResultado=InvestigacionValidacion.validarInvestigacionMalaria(
				pInvestigacionMalaria,
				pInvestigacionSintoma,
				pSintomaLugarInicio,
				pInvestigacionMedicamento,
				pInvestigacionLugar,
				pInvestigacionTransfusion,
				pInvestigacionHospitalario);
		if (!oResultado.isOk()) return oResultado;
		
		Query query=null;
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
		
    	try{
    		InvestigacionMalaria oDetachedInvestigacion = (InvestigacionMalaria) oEM.find(InvestigacionMalaria.class, pInvestigacionMalaria.getInvestigacionMalariaId());
    		InvestigacionMalaria oInvestigacion=oEM.merge(oDetachedInvestigacion);
    		
    		oInvestigacion.setNumeroCaso(pInvestigacionMalaria.getNumeroCaso());
    		oInvestigacion.setLatitudVivienda(pInvestigacionMalaria.getLatitudVivienda());
    		oInvestigacion.setLongitudVivienda(pInvestigacionMalaria.getLatitudVivienda());
    		oInvestigacion.setConfirmacionEntidad(pInvestigacionMalaria.getConfirmacionEntidad());
    		oInvestigacion.setConfirmacionCndr(pInvestigacionMalaria.getConfirmacionCndr());
    		oInvestigacion.setSintomatico(pInvestigacionMalaria.getSintomatico());
    		oInvestigacion.setViajesZonaRiesgo(pInvestigacionMalaria.getViajesZonaRiesgo());
    		if(oInvestigacion.getViajesZonaRiesgo().intValue()==0 || oInvestigacion.getViajesZonaRiesgo().intValue()==-1){
    			oInvestigacion.setUsoMosquitero(null);
    		}else{
    			oInvestigacion.setUsoMosquitero(pInvestigacionMalaria.getUsoMosquitero());
    		}
    		oInvestigacion.setManejoClinico(pInvestigacionMalaria.getManejoClinico());
    		oInvestigacion.setInicioTratamiento(pInvestigacionMalaria.getInicioTratamiento());
    		oInvestigacion.setFinTratamiento(pInvestigacionMalaria.getFinTratamiento());
    		oInvestigacion.setConvivientesTratados(pInvestigacionMalaria.getConvivientesTratados());
    		oInvestigacion.setColateralesTratados(pInvestigacionMalaria.getColateralesTratados());
    		oInvestigacion.setTratamientoSupervisado(pInvestigacionMalaria.getTratamientoSupervisado());
    		oInvestigacion.setTratamientoCompleto(pInvestigacionMalaria.getTratamientoCompleto());
    		oInvestigacion.setControlParasitario(pInvestigacionMalaria.getControlParasitario());
    		oInvestigacion.setCondicionFinalVivo(pInvestigacionMalaria.getControlParasitario());
    		if(oInvestigacion.getControlParasitario().intValue()==1){
    			oInvestigacion.setDiasPosterioresControl(pInvestigacionMalaria.getDiasPosterioresControl());
    			oInvestigacion.setResultadoControlPositivo(pInvestigacionMalaria.getResultadoControlPositivo());
    		}else{
    			oInvestigacion.setDiasPosterioresControl(null);
    			oInvestigacion.setResultadoControlPositivo(null);
    		}
    		oInvestigacion.setCondicionFinalVivo(pInvestigacionMalaria.getCondicionFinalVivo());
    		if(oInvestigacion.getCondicionFinalVivo().intValue()==0){
    			oInvestigacion.setFechaDefuncion(pInvestigacionMalaria.getFechaDefuncion());
    		}else{
    			oInvestigacion.setFechaDefuncion(null);
    		}
    		oInvestigacion.setAutomedicacion(pInvestigacionMalaria.getAutomedicacion());
    		if(oInvestigacion.getAutomedicacion().intValue()==1){
    			oInvestigacion.setMedicamentosAutomedicacion(pInvestigacionMalaria.getMedicamentosAutomedicacion());
    		}else{
    			oInvestigacion.setMedicamentosAutomedicacion(null);
    		}
    		oInvestigacion.setFechaInfeccion(pInvestigacionMalaria.getFechaInfeccion());
    		oInvestigacion.setInfeccionResidencia(pInvestigacionMalaria.getInfeccionResidencia());
    		oInvestigacion.setClasificacionClinica(pInvestigacionMalaria.getClasificacionClinica());
    		oInvestigacion.setTipoRecurrencia(pInvestigacionMalaria.getTipoRecurrencia());
    		oInvestigacion.setTipoComplicacion(pInvestigacionMalaria.getTipoComplicacion());
    		oInvestigacion.setClasificacionCaso(pInvestigacionMalaria.getClasificacionCaso());
    		
    		oInvestigacion.setObservaciones(pInvestigacionMalaria.getObservaciones());
    		oInvestigacion.setNivelAutorizacion(pInvestigacionMalaria.getNivelAutorizacion());
    		oInvestigacion.setResponsable(pInvestigacionMalaria.getResponsable());
    		oInvestigacion.setEpidemiologo(pInvestigacionMalaria.getEpidemiologo());
    		
    		oInvestigacion.setCasoCerrado(pInvestigacionMalaria.getCasoCerrado());
    		if(oInvestigacion.getCasoCerrado().intValue()==1){
    			oInvestigacion.setFechaCierreCaso(pInvestigacionMalaria.getFechaCierreCaso());
    		}else{
    			oInvestigacion.setFechaCierreCaso(null);
    		}
    		
    		//Se guardan o eliminan los valores asociados a investigación de sintomas
    		InvestigacionSintoma oInvestigacionSintoma=null;
    		query = oEM.createNamedQuery("InvestigacionSintoma.encontrarPorInvestigacionMalaria");
            query.setParameter("pInvestigacionMalariaId", oInvestigacion.getInvestigacionMalariaId());
            query.setMaxResults(1);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<InvestigacionSintoma> oDetachedInvestigacionSintoma = (List<InvestigacionSintoma>)query.getResultList();
            if(!(oDetachedInvestigacionSintoma==null || oDetachedInvestigacionSintoma.size()==0)){
            	oInvestigacionSintoma = oEM.merge(oDetachedInvestigacionSintoma.get(0));
            }
            
            if(oInvestigacion.getSintomatico().intValue()==1){
            	if(oInvestigacionSintoma!=null){
            		oInvestigacionSintoma.setFechaInicioSintomas(pInvestigacionSintoma.getFechaInicioSintomas());
        			oInvestigacionSintoma.setSintomatico(pInvestigacionSintoma.getSintomatico());
        			oInvestigacionSintoma.setEstadoFebril(pInvestigacionSintoma.getEstadoFebril());
        			oInvestigacionSintoma.setPersonasSintomas(pInvestigacionSintoma.getPersonasSintomas());
        			oInvestigacionSintoma.setInicioResidencia(pInvestigacionSintoma.getInicioResidencia());
        			
        			SintomaLugarInicio oSintomaLugarInicio=null;
        			query = oEM.createNamedQuery("SintomaLugarInicio.encontrarPorInvestigacionSintoma");
                    query.setParameter("pInvestigacionSintomaId", oInvestigacionSintoma.getInvestigacionSintomaId());
                    query.setMaxResults(1);
                    query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                    List<SintomaLugarInicio> oDetachedSintomaLugarInicio = (List<SintomaLugarInicio>)query.getResultList();
                    if(!(oDetachedSintomaLugarInicio==null || oDetachedSintomaLugarInicio.size()==0)){
                    	oSintomaLugarInicio = oEM.merge(oDetachedSintomaLugarInicio.get(0));
                    }
                    
                    if(oInvestigacionSintoma.getInicioResidencia().intValue()==0){
                    	if(oSintomaLugarInicio!=null){
                    		oSintomaLugarInicio.setInicioResidencia(pSintomaLugarInicio.getInicioResidencia());
            				oSintomaLugarInicio.setPais(pSintomaLugarInicio.getPais());
            				oSintomaLugarInicio.setMunicipio(pSintomaLugarInicio.getMunicipio());
            				oSintomaLugarInicio.setComunidad(pSintomaLugarInicio.getComunidad());
            				oSintomaLugarInicio.setEstadia(pSintomaLugarInicio.getEstadia());
                    	}else{
                    		pSintomaLugarInicio.setInvestigacionSintoma(oInvestigacionSintoma);
                    		oEM.persist(oInvestigacionSintoma);
                    	}
                    }else if(oSintomaLugarInicio!=null){
                    	oEM.remove(oSintomaLugarInicio);
                    }	
            	}else{
            		pInvestigacionSintoma.setInvestigacionMalaria(oInvestigacion);
            		oEM.persist(pInvestigacionSintoma);
            		if(pInvestigacionSintoma.getInicioResidencia().intValue()==0){
            			pSintomaLugarInicio.setInvestigacionSintoma(pInvestigacionSintoma);
            			oEM.persist(pSintomaLugarInicio);
            		}
            	}
            }else if(oInvestigacionSintoma!=null){
            	oEM.remove(oInvestigacionSintoma);
            }
            
            if(oInvestigacion.getSintomatico().intValue()==0 && (pSintomaLugarOtro!=null && pSintomaLugarOtro.size() >0)){
            	for(SintomaLugarOtro oLugarOtro:pSintomaLugarOtro){
            		oEM.remove(oLugarOtro);
            	}
            }
                   
           //Se guardan o eliminan los antecedentes transfusionales, se guardarán los valores asociados.
            InvestigacionTransfusion oInvTransfusion=null;
            query = oEM.createNamedQuery("InvestigacionTransfusion.encontrarPorInvestigacionMalaria");
            query.setParameter("pInvestigacionMalariaId", oInvestigacion.getInvestigacionMalariaId());
            query.setMaxResults(1);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<InvestigacionTransfusion> oInvTransfusionDetached = (List<InvestigacionTransfusion>)query.getResultList();
            if(!(oInvTransfusionDetached==null || oInvTransfusionDetached.size()==0)){
            	oInvTransfusion = oEM.merge(oInvTransfusionDetached.get(0));
            }
            
            if(oInvestigacion.getTransfusion().intValue()==1){
            	if(oInvTransfusion!=null){
            		oInvTransfusion.setTransfusion(pInvestigacionTransfusion.getTransfusion());
                	oInvTransfusion.setPais(pInvestigacionTransfusion.getPais());
                	oInvTransfusion.setUnidad(pInvestigacionTransfusion.getUnidad());
            	}else{
            		pInvestigacionTransfusion.setInvestigacionMalaria(oInvestigacion);
            		oEM.persist(pInvestigacionTransfusion);
            	}
            }else if(oInvTransfusion!=null){
            	oEM.remove(oInvTransfusion);
            }
            
        	//Se guardan o eliminan los datos asociados al manejo clínico.
            InvestigacionHospitalario oInvHospitalario=null;
            query = oEM.createNamedQuery("InvestigacionHospitalario.encontrarPorInvestigacionMalaria");
            query.setParameter("pInvestigacionMalariaId", oInvestigacion.getInvestigacionMalariaId());
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setMaxResults(1);
            List<InvestigacionHospitalario> oInvHospitalarioDetached = (List<InvestigacionHospitalario>)query.getResultList();
            if(!(oInvHospitalarioDetached==null || oInvHospitalarioDetached.size()==0)){
            	oInvHospitalario = oEM.merge(oInvHospitalarioDetached.get(0));
            }
            if(oInvestigacion.getManejoClinico().intValue()==1){
            	if(oInvHospitalario!=null){
            		oInvHospitalario = new InvestigacionHospitalario();
                	oInvHospitalario.setManejoClinico(pInvestigacionHospitalario.getManejoClinico());
                	oInvHospitalario.setUnidad(pInvestigacionHospitalario.getUnidad());
                	oInvHospitalario.setExpediente(pInvestigacionHospitalario.getExpediente());
                	oInvHospitalario.setFechaIngreso(pInvestigacionHospitalario.getFechaIngreso());
                	oInvHospitalario.setDiasEstancia(pInvestigacionHospitalario.getDiasEstancia());
        			if(oInvHospitalario.getUnidad()!=null){
        				oInvHospitalario.setMunicipio(pInvestigacionHospitalario.getMunicipio());
        			}else{
        				pInvestigacionHospitalario.setInvestigacionMalaria(oInvestigacion);
        				oEM.persist(pInvestigacionHospitalario);
        			}
            	}
            }else if(oInvHospitalario!=null){
            	oEM.remove(oInvHospitalario);
            }
            
            //Se guardar on eliminan los datos asociados a investigación de lugares
            InvestigacionLugar oInvLugar=null;
            query = oEM.createNamedQuery("InvestigacionLugar.encontrarPorInvestigacionMalaria");
            query.setParameter("pInvestigacionMalariaId", oInvestigacion.getInvestigacionMalariaId());
            query.setMaxResults(1);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<InvestigacionLugar> oInvLugarDetached = (List<InvestigacionLugar>)query.getResultList();
            if(!(oInvLugarDetached==null || oInvLugarDetached.size()==0)){
            	oInvLugar = oEM.merge(oInvLugarDetached.get(0));
            }
            if(oInvestigacion.getInfeccionResidencia().intValue()==0){
            	if(oInvLugar!=null){
            		oInvLugar.setInfeccionResidencia(pInvestigacionLugar.getInfeccionResidencia());
                	oInvLugar.setPais(pInvestigacionLugar.getPais());
                	oInvLugar.setMunicipio(pInvestigacionLugar.getMunicipio());
                	oInvLugar.setComunidad(pInvestigacionLugar.getComunidad());
            	}else{
            		pInvestigacionLugar.setInvestigacionMalaria(pInvestigacionMalaria);
            		oEM.persist(pInvestigacionLugar);
            	}
            }else if(oInvLugarDetached!=null){
            	oEM.remove(oInvLugarDetached);
            }
            
            // Se guardar on eliminan los objetos relacionados a Investigación medicamentos.
            query = oEM.createNamedQuery("InvestigacionMedicamento.listar");
	    	query.setParameter("pInvestigacionMalaria",oInvestigacion.getInvestigacionMalariaId());
	    	List<InvestigacionMedicamento> oInvMedicamentos = (List<InvestigacionMedicamento>) query.getResultList();
	        
	    	if(oInvMedicamentos!=null){
				for (InvestigacionMedicamento oMedicamento : oInvMedicamentos) {
					oEM.remove(oMedicamento);
				}
	    	}
	    	
			if (pInvestigacionMedicamento != null) {
				for (InvestigacionMedicamento oMedicamento : pInvestigacionMedicamento) {
					oMedicamento.setInvestigacionMedicamentoId(0);
					oMedicamento.setInvestigacionesMalaria(pInvestigacionMalaria);
					oEM.persist(oMedicamento);
				}
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
				pSintomaLugarInicio,
				pInvestigacionMedicamento,
				pInvestigacionLugar,
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
            	if(pInvestigacionSintoma.getSintomatico().intValue() == 1){
            		pInvestigacionSintoma.setInvestigacionMalaria(pInvestigacionMalaria);
            		oEM.persist(pInvestigacionSintoma);
            		
            		if(pSintomaLugarInicio!=null){
            			pSintomaLugarInicio.setInvestigacionSintoma(pInvestigacionSintoma);
            			oEM.persist(pSintomaLugarInicio);
            		}
            		
            		if(pSintomaLugarAnte!=null){
            			for(SintomaLugarAnte oLugarAnte:pSintomaLugarAnte){
                			if(oLugarAnte.getFechaUltima()!=null){
                				oLugarAnte.setSintomaLugarAntesId(0);
                				oLugarAnte.setInvestigacionesSintoma(pInvestigacionSintoma);
                    			oEM.persist(oLugarAnte);
                			}
                		}
            		}
            		
            		if(pSintomaLugarOtro!=null){
            			for(SintomaLugarOtro oLugarOtro:pSintomaLugarOtro){
            				oLugarOtro.setSintomaLugarOtroId(0);
            				oLugarOtro.setInvestigacionesSintoma(pInvestigacionSintoma);
                			oEM.persist(oLugarOtro);
                		}
            		}
            	}
            }
            
			// Agregando los objetos relacionados a Investigación medicamentos.
			if (pInvestigacionMedicamento != null) {
				for (InvestigacionMedicamento oMedicamento : pInvestigacionMedicamento) {
					oMedicamento.setInvestigacionMedicamentoId(0);
					oMedicamento.setInvestigacionesMalaria(pInvestigacionMalaria);
					oEM.persist(oMedicamento);
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
