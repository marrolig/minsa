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
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.investigacion.ClasificacionCaso;
import ni.gob.minsa.malaria.modelo.investigacion.ClasificacionClinica;
import ni.gob.minsa.malaria.modelo.investigacion.ConfirmacionDiagnostico;
import ni.gob.minsa.malaria.modelo.investigacion.EstadoFebril;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionHospitalario;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionLugar;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMalaria;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMedicamento;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionSintoma;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionTransfusion;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarAnte;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarInicio;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarOtro;
import ni.gob.minsa.malaria.modelo.investigacion.TipoComplicacion;
import ni.gob.minsa.malaria.modelo.investigacion.TipoRecurrencia;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Pais;
import ni.gob.minsa.malaria.reglas.InvestigacionValidacion;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionHospitalarioService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionLugarService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionMedicamentoService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionSintomaService;
import ni.gob.minsa.malaria.servicios.investigacion.InvestigacionTransfusionService;
import ni.gob.minsa.malaria.servicios.investigacion.SintomaLugarAnteService;
import ni.gob.minsa.malaria.servicios.investigacion.SintomaLugarInicioService;
import ni.gob.minsa.malaria.servicios.investigacion.SintomaLugarOtroService;
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
		
		InvestigacionTransfusionService oInvTransfusionService = new InvestigacionTransfusionDA();
		InvestigacionHospitalarioService oInvHospitalarioService = new InvestigacionHospitalarioDA();
		InvestigacionSintomaService oInvSintomaService = new InvestigacionSintomaDA();
		InvestigacionLugarService oInvLugarService = new InvestigacionLugarDA();
		SintomaLugarAnteService oSintomaLugAnteService=new SintomaLugarAnteDA();
		SintomaLugarOtroService oSintomaLugOtroService= new SintomaLugarOtroDA();
		SintomaLugarInicioService oSintomaLugInicioService = new SintomaLugarInicioDA();
		InvestigacionMedicamentoService oInvMedicamentoService = new InvestigacionMedicamentoDA();
	
		InvestigacionSintoma oDetachedInvSintoma=null;
		SintomaLugarInicio oLugarInicioSintomaDetached=null;
		InvestigacionHospitalario oInvHospitalarioDetached=null;
		InvestigacionLugar oInvLugarDetached=null;
		InvestigacionTransfusion oInvTransfusionDetached=null;
		List<SintomaLugarAnte> oSintomaLugAnteDetached=null;
		List<SintomaLugarOtro> oSintomaLugOtroDetached=null;
		List<InvestigacionMedicamento> oMedicamentoDetached=null;
		
		oResultado = oInvSintomaService.EncontrarPorInvestigacionMalaria(pInvestigacionMalaria.getInvestigacionMalariaId());
		if(oResultado!=null){
			oDetachedInvSintoma = (InvestigacionSintoma)oResultado.getObjeto();
		}
		if(oDetachedInvSintoma!=null){
			oResultado = oSintomaLugInicioService.EncontrarPorInvestigacionSintoma(oDetachedInvSintoma.getInvestigacionSintomaId());
			if(oResultado!=null){
				oLugarInicioSintomaDetached = (SintomaLugarInicio)oResultado.getObjeto();
			}
			oSintomaLugAnteDetached=oSintomaLugAnteService.SintomasLugarAntePorInvestigacionSintomas(oDetachedInvSintoma.getInvestigacionSintomaId());
			oSintomaLugOtroDetached=oSintomaLugOtroService.SintomasLugarOtroPorInvestigacionSintomas(oDetachedInvSintoma.getInvestigacionSintomaId());
		}
		oResultado = oInvHospitalarioService.EncontrarPorInvestigacionMalaria(pInvestigacionMalaria.getInvestigacionMalariaId());
		if(oResultado!=null){
			oInvHospitalarioDetached = (InvestigacionHospitalario)oResultado.getObjeto();
		}
		oResultado = oInvTransfusionService.EncontrarPorInvestigacionMalaria(pInvestigacionMalaria.getInvestigacionMalariaId());
		if(oResultado!=null){
			 oInvTransfusionDetached = (InvestigacionTransfusion)oResultado.getObjeto();
		}
		oResultado = oInvLugarService.EncontrarPorInvestigacionMalaria(pInvestigacionMalaria.getInvestigacionMalariaId());
		if(oResultado!=null){
			oInvLugarDetached = (InvestigacionLugar)oResultado.getObjeto();
		}
		oMedicamentoDetached = oInvMedicamentoService.EncontrarPorInvestigacionMalaria(pInvestigacionMalaria.getInvestigacionMalariaId());
		
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
		
    	try{
    		InvestigacionMalaria oDetachedInvestigacion = (InvestigacionMalaria) oEM.find(InvestigacionMalaria.class, pInvestigacionMalaria.getInvestigacionMalariaId());
    		InvestigacionMalaria oInvestigacion=oEM.merge(oDetachedInvestigacion);
    		InvestigacionSintoma oInvestigacionSintoma=null;
    		if(oDetachedInvSintoma!=null){
            	oInvestigacionSintoma = oEM.merge((InvestigacionSintoma)oEM.find(InvestigacionSintoma.class, oDetachedInvSintoma.getInvestigacionSintomaId()));
            }
    		
    		//Se guardan o eliminan los valores asociados a investigación de sintomas
            if(pInvestigacionMalaria.getSintomatico().intValue()==1){
            	oInvestigacion.setSintomatico(pInvestigacionMalaria.getSintomatico());
            	oEM.flush();
            	if(oDetachedInvSintoma!=null){
            		oInvestigacionSintoma.setFechaInicioSintomas(pInvestigacionSintoma.getFechaInicioSintomas());
        			oInvestigacionSintoma.setSintomatico(pInvestigacionMalaria.getSintomatico());
        			oInvestigacionSintoma.setEstadoFebril(pInvestigacionSintoma.getEstadoFebril()!=null ?(EstadoFebril) oEM.find(EstadoFebril.class,pInvestigacionSintoma.getEstadoFebril().getCatalogoId()):null);
        			oInvestigacionSintoma.setPersonasSintomas(pInvestigacionSintoma.getPersonasSintomas());
        			oInvestigacionSintoma.setInicioResidencia(pInvestigacionSintoma.getInicioResidencia());
        			oEM.flush();
            	}else{
            		pInvestigacionSintoma.setInvestigacionMalaria(oInvestigacion);
            		oEM.persist(pInvestigacionSintoma);
            	}
            }else if(oDetachedInvSintoma!=null){
            	oEM.remove((InvestigacionSintoma) oEM.find(InvestigacionSintoma.class, oDetachedInvSintoma.getInvestigacionSintomaId()));
            	oEM.flush();
            }
            
           //Se guardan o eliminan los valores asociados a investigación de sintomas Lugar Inicio, Otros y Antes
			if (pInvestigacionSintoma!=null && pInvestigacionMalaria.getSintomatico().intValue() == 1) {
                if(pInvestigacionSintoma.getInicioResidencia().intValue()==0){
                	if(oLugarInicioSintomaDetached!=null){
                		SintomaLugarInicio oSintomaLugarInicio = oEM.merge((SintomaLugarInicio) oEM.find(SintomaLugarInicio.class,oLugarInicioSintomaDetached.getSintomaLugarInicioId()));
                		oSintomaLugarInicio.setInicioResidencia(pInvestigacionSintoma.getInicioResidencia());
        				oSintomaLugarInicio.setPais(pSintomaLugarInicio.getPais()!=null ? (Pais) oEM.find(Pais.class, pSintomaLugarInicio.getPais().getPaisId()):null);
        				oSintomaLugarInicio.setMunicipio(pSintomaLugarInicio.getMunicipio()!=null ? (DivisionPolitica) oEM.find(DivisionPolitica.class,pSintomaLugarInicio.getMunicipio().getDivisionPoliticaId()):null);
        				oSintomaLugarInicio.setComunidad(pSintomaLugarInicio.getComunidad()!=null ? (Comunidad) oEM.find(Comunidad.class, pSintomaLugarInicio.getComunidad().getComunidadId()):null);
        				oSintomaLugarInicio.setEstadia(pSintomaLugarInicio.getEstadia());
                	}else{
                		pSintomaLugarInicio.setInvestigacionSintoma(oInvestigacionSintoma!=null ? oInvestigacionSintoma : pInvestigacionSintoma);
                		oEM.persist(pSintomaLugarInicio);
                	}
                }else if(oLugarInicioSintomaDetached!=null){
                	oEM.remove((SintomaLugarInicio) oEM.find(SintomaLugarInicio.class, oLugarInicioSintomaDetached.getSintomaLugarInicioId()));
                	oEM.flush();
                }	
                if(!(oSintomaLugAnteDetached==null || oSintomaLugAnteDetached.size()==0)){
                	for(SintomaLugarAnte oSintoma:oSintomaLugAnteDetached){
                		oEM.remove((SintomaLugarAnte) oEM.find(SintomaLugarAnte.class, oSintoma.getSintomaLugarAntesId()));
                		oEM.flush();
                	}
                }
				if (pSintomaLugarAnte != null) {
					for (SintomaLugarAnte oLugarAnte : pSintomaLugarAnte) {
						oLugarAnte.setSintomaLugarAntesId(0);
						oLugarAnte.setInvestigacionesSintoma(oInvestigacionSintoma!=null ? oInvestigacionSintoma : pInvestigacionSintoma);
						oEM.persist(oLugarAnte);
					}
				}
				
				if (!(oSintomaLugOtroDetached == null || oSintomaLugOtroDetached.size() == 0)) {
					for (SintomaLugarOtro oSintoma : oSintomaLugOtroDetached) {
						oEM.remove((SintomaLugarOtro) oEM.find(SintomaLugarOtro.class, oSintoma.getSintomaLugarOtroId()));
						oEM.flush();
					}
				}
				if (pSintomaLugarOtro != null) {
					for (SintomaLugarOtro oLugarOtro : pSintomaLugarOtro) {
						oLugarOtro.setSintomaLugarOtroId(0);
						oLugarOtro.setInvestigacionesSintoma(oInvestigacionSintoma!=null ? oInvestigacionSintoma : pInvestigacionSintoma);
						oEM.persist(oLugarOtro);
						oEM.flush();
					}
				}

			}

           //Se guardan o eliminan los antecedentes transfusionales, se guardarán los valores asociados.
           
            if(pInvestigacionMalaria.getTransfusion().intValue()==1){
            	oInvestigacion.setTransfusion(pInvestigacionMalaria.getTransfusion());
            	oEM.flush();
            	if(oInvTransfusionDetached!=null){
            		InvestigacionTransfusion oInvTransfusion= oEM.merge((InvestigacionTransfusion)oEM.find(InvestigacionTransfusion.class, oInvTransfusionDetached.getInvestigacionTransfusionId()));
            		oInvTransfusion.setTransfusion(pInvestigacionMalaria.getTransfusion());
            		oInvTransfusion.setFechaTransfusion(pInvestigacionTransfusion.getFechaTransfusion());
                	oInvTransfusion.setPais(pInvestigacionTransfusion.getPais()!=null ? (Pais) oEM.find(Pais.class, pInvestigacionTransfusion.getPais().getPaisId()):null);
                	oInvTransfusion.setUnidad(pInvestigacionTransfusion.getUnidad()!=null ? (Unidad) oEM.find(Unidad.class, pInvestigacionTransfusion.getUnidad().getUnidadId()):null);
            	}else{
            		pInvestigacionTransfusion.setInvestigacionMalaria(oInvestigacion);
            		oEM.persist(pInvestigacionTransfusion);
            	}
            }else if(oInvTransfusionDetached!=null){
            	oEM.remove((InvestigacionTransfusion)oEM.find(InvestigacionTransfusion.class, oInvTransfusionDetached.getInvestigacionTransfusionId()));
            	oEM.flush();
            }
            
        	//Se guardan o eliminan los datos asociados al manejo clínico.
            if(pInvestigacionMalaria.getManejoClinico().intValue()==1){
            	oInvestigacion.setManejoClinico(pInvestigacionMalaria.getManejoClinico());
            	oEM.flush();
            	if(oInvHospitalarioDetached!=null){
            	    InvestigacionHospitalario oInvHospitalario = oEM.merge((InvestigacionHospitalario)oEM.find(InvestigacionHospitalario.class, oInvHospitalarioDetached.getInvestigacionHospitalarioId()));
                	oInvHospitalario.setManejoClinico(pInvestigacionMalaria.getManejoClinico());
                	oInvHospitalario.setUnidad(pInvestigacionHospitalario.getUnidad()!=null ? (Unidad) oEM.find(Unidad.class,pInvestigacionHospitalario.getUnidad().getUnidadId()):null);
                	oInvHospitalario.setExpediente(pInvestigacionHospitalario.getExpediente());
                	oInvHospitalario.setFechaIngreso(pInvestigacionHospitalario.getFechaIngreso());
                	oInvHospitalario.setDiasEstancia(pInvestigacionHospitalario.getDiasEstancia());
        			if(oInvHospitalario.getUnidad()!=null){
        				oInvHospitalario.setMunicipio(pInvestigacionHospitalario.getMunicipio()!=null ? (DivisionPolitica) oEM.find(DivisionPolitica.class,pInvestigacionHospitalario.getMunicipio().getDivisionPoliticaId()):null);
        			}
            	}else{
    				pInvestigacionHospitalario.setInvestigacionMalaria(oInvestigacion);
    				oEM.persist(pInvestigacionHospitalario);
    			}
            }else if(oInvHospitalarioDetached!=null){
            	oEM.remove((InvestigacionHospitalario)oEM.find(InvestigacionHospitalario.class, oInvHospitalarioDetached.getInvestigacionHospitalarioId()));
            	oEM.flush();
            }
            
            //Se guardar on eliminan los datos asociados a investigación de lugares
            if(pInvestigacionMalaria.getInfeccionResidencia().intValue()==0){
            	oInvestigacion.setInfeccionResidencia(pInvestigacionMalaria.getInfeccionResidencia());
            	oEM.flush();
            	if(oInvLugarDetached!=null){
            		InvestigacionLugar oInvLugar = oEM.merge((InvestigacionLugar)oEM.find(InvestigacionLugar.class, oInvLugarDetached.getInvsmalariaLugarId()));
            		oInvLugar.setInfeccionResidencia(pInvestigacionLugar.getInfeccionResidencia());
                	oInvLugar.setPais(pInvestigacionLugar.getPais()!=null ? oEM.find(Pais.class, pInvestigacionLugar.getPais().getPaisId()):null);
                	oInvLugar.setMunicipio(pInvestigacionLugar.getMunicipio()!=null ? oEM.find(DivisionPolitica.class, pInvestigacionLugar.getMunicipio().getDivisionPoliticaId()):null);
                	oInvLugar.setComunidad(pInvestigacionLugar.getComunidad()!=null ? oEM.find(Comunidad.class, pInvestigacionLugar.getComunidad().getComunidadId()):null);
            	}else{
            		pInvestigacionLugar.setInvestigacionMalaria(pInvestigacionMalaria);
            		oEM.persist(pInvestigacionLugar);
            	}
            }else if(oInvLugarDetached!=null){
            	oEM.remove((InvestigacionLugar)oEM.find(InvestigacionLugar.class, oInvLugarDetached.getInvsmalariaLugarId()));
            	oEM.flush();
            }
            
           //Se guardan o eliminan los valores asociados a investigación de medicamentos
			if (!(pInvestigacionMedicamento== null||pInvestigacionMedicamento.size()==0)) {
				if((oMedicamentoDetached==null || oMedicamentoDetached.size()==0) && !(pInvestigacionMedicamento==null || pInvestigacionMedicamento.size()==0) ){
					for (InvestigacionMedicamento oMedicamento : pInvestigacionMedicamento) {
						oMedicamento.setInvestigacionMedicamentoId(0);
						oMedicamento.setInvestigacionesMalaria(oInvestigacion);
						oEM.persist(oMedicamento);
					}
				}else{
					for (InvestigacionMedicamento oInvMedicamento : pInvestigacionMedicamento) {
						oInvMedicamento.setInvestigacionMedicamentoId(0);
						oInvMedicamento.setInvestigacionesMalaria((InvestigacionMalaria) oEM.find(InvestigacionMalaria.class, pInvestigacionMalaria.getInvestigacionMalariaId()));
						if(!oMedicamentoDetached.contains(oInvMedicamento)){
							oEM.persist(oInvMedicamento);
						}
					}	
					for (InvestigacionMedicamento oInvMedicamento : oMedicamentoDetached) {
						if(!pInvestigacionMedicamento.contains(oInvMedicamento)){
							oEM.remove((InvestigacionMedicamento) oEM.find(InvestigacionMedicamento.class,oInvMedicamento.getInvestigacionMedicamentoId()));
						}
					}
				}
				
			}else if (!(oMedicamentoDetached == null || oMedicamentoDetached.size() == 0)){
				for (InvestigacionMedicamento oInvMedicamento : oMedicamentoDetached) {
					oEM.remove((InvestigacionMedicamento) oEM.find(
							InvestigacionMedicamento.class,
							oInvMedicamento.getInvestigacionMedicamentoId()));
				}
			}
			
			//Se guardan los valores de Investigación de Malaria
    		oInvestigacion.setNumeroCaso(pInvestigacionMalaria.getNumeroCaso());
    		oInvestigacion.setLatitudVivienda(pInvestigacionMalaria.getLatitudVivienda());
    		oInvestigacion.setLongitudVivienda(pInvestigacionMalaria.getLongitudVivienda());
    		oInvestigacion.setConfirmacionEntidad(pInvestigacionMalaria.getConfirmacionEntidad()!=null ?(ConfirmacionDiagnostico) oEM.find(ConfirmacionDiagnostico.class, pInvestigacionMalaria.getConfirmacionEntidad().getCatalogoId()):null);
    		oInvestigacion.setConfirmacionCndr(pInvestigacionMalaria.getConfirmacionCndr()!=null ?(ConfirmacionDiagnostico) oEM.find(ConfirmacionDiagnostico.class, pInvestigacionMalaria.getConfirmacionCndr().getCatalogoId()):null);
    		oInvestigacion.setSintomatico(pInvestigacionMalaria.getSintomatico());
    		oInvestigacion.setViajesZonaRiesgo(pInvestigacionMalaria.getViajesZonaRiesgo());
    		if(oInvestigacion.getViajesZonaRiesgo().intValue()==0 || oInvestigacion.getViajesZonaRiesgo().intValue()==-1){
    			oInvestigacion.setUsoMosquitero(null);
    		}else{
    			oInvestigacion.setUsoMosquitero(pInvestigacionMalaria.getUsoMosquitero());
    		}
    		oInvestigacion.setTransfusion(pInvestigacionMalaria.getTransfusion());
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
    		oInvestigacion.setClasificacionClinica(pInvestigacionMalaria.getClasificacionClinica()!=null? (ClasificacionClinica) oEM.find(ClasificacionClinica.class, pInvestigacionMalaria.getClasificacionClinica().getCatalogoId()):null);
    		oInvestigacion.setTipoRecurrencia(pInvestigacionMalaria.getTipoRecurrencia()!=null ? (TipoRecurrencia) oEM.find(TipoRecurrencia.class, pInvestigacionMalaria.getTipoRecurrencia().getCatalogoId()):null);
    		oInvestigacion.setTipoComplicacion(pInvestigacionMalaria.getTipoComplicacion()!=null?(TipoComplicacion) oEM.find(TipoComplicacion.class, pInvestigacionMalaria.getTipoComplicacion().getCatalogoId()):null);
    		oInvestigacion.setClasificacionCaso(pInvestigacionMalaria.getClasificacionCaso()!=null ? (ClasificacionCaso) oEM.find(ClasificacionCaso.class, pInvestigacionMalaria.getClasificacionCaso().getCatalogoId()):null);
    		
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
