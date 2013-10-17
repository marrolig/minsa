package ni.gob.minsa.malaria.datos.vigilancia;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ejbPersona.dto.Persona;
import ni.gob.minsa.ejbPersona.servicios.PersonaUTMService;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.datos.sis.SisPersonaDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Manzana;
import ni.gob.minsa.malaria.modelo.poblacion.Vivienda;
import ni.gob.minsa.malaria.modelo.sis.Etnia;
import ni.gob.minsa.malaria.modelo.sis.Sexo;
import ni.gob.minsa.malaria.modelo.sis.SisPersona;
import ni.gob.minsa.malaria.modelo.vigilancia.DensidadCruces;
import ni.gob.minsa.malaria.modelo.vigilancia.EstadioPFalciparum;
import ni.gob.minsa.malaria.modelo.vigilancia.MotivoFaltaDiagnostico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoDiagnostico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoHematico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoPruebaRapida;
import ni.gob.minsa.malaria.modelo.vigilancia.ResponsablePruebaRapida;
import ni.gob.minsa.malaria.modelo.vigilancia.ResultadoPruebaRapida;
import ni.gob.minsa.malaria.reglas.PersonaValidacion;
import ni.gob.minsa.malaria.reglas.VigilanciaValidacion;
import ni.gob.minsa.malaria.servicios.sis.SisPersonaService;
import ni.gob.minsa.malaria.servicios.vigilancia.MuestreoHematicoService;
import ni.gob.minsa.malaria.soporte.Mensajes;

public class MuestreoHematicoDA implements MuestreoHematicoService {

	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();
    private static SisPersonaService sisPersonaService = new SisPersonaDA();

	@Override
	public InfoResultado Encontrar(long pMuestreoHematicoId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		MuestreoHematico oMuestreoHematico = (MuestreoHematico)oEM.find(MuestreoHematico.class, pMuestreoHematicoId);
    		if (oMuestreoHematico!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oMuestreoHematico);
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
	public InfoResultado Guardar(MuestreoHematico pMuestreoHematico, 
								 Persona pPersona) {

		InfoResultado oResultado=new InfoResultado();
		oResultado=VigilanciaValidacion.validarMuestreoHematico(pMuestreoHematico);
		if (!oResultado.isOk()) return oResultado;
		
		oResultado=PersonaValidacion.validar(pPersona);
		if (!oResultado.isOk()) return oResultado;
		
		oResultado=new InfoResultado();
		
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	
    	InitialContext ctx;
    	PersonaUTMService personaUTMService = null;

        try{
        	MuestreoHematico oDetachedMuestreo = (MuestreoHematico)oEM.find(MuestreoHematico.class, pMuestreoHematico.getMuestreoHematicoId());
        	MuestreoHematico oMuestreoHematico=oEM.merge(oDetachedMuestreo);
        	
        	// los siguientes datos no son modificables y por tanto no forman
        	// parte del proceso de actualización
        	
        	// entidad de notificación
        	// unidad de notificación
        	// municipio de notificación
        	// puesto de notificación (clave y colvol, si procede)
        	// número de lámina (si la fecha de recepción aún no ha sido declarada)
        	// persona, aunque sus datos pueden ser modificados

        	// si no se había declarado diagnóstico (resultados de la prueba de gota gruesa)
        	// se pueden modificar el número de lámina, fecha de toma y año/semana epidemiológica

        	if (oMuestreoHematico.getDiagnostico()==null) {
        		oMuestreoHematico.setNumeroLamina(pMuestreoHematico.getNumeroLamina());
            	oMuestreoHematico.setFechaToma(pMuestreoHematico.getFechaToma());
            	oMuestreoHematico.setAñoEpidemiologico(pMuestreoHematico.getAñoEpidemiologico());
            	oMuestreoHematico.setSemanaEpidemiologica(pMuestreoHematico.getSemanaEpidemiologica());
        	}
        	
        	// Datos de la persona que pueden ser modificados.  Estos datos únicamente serán modificados
        	// si son modificados directamente en el registro del muestreo hemático.  Si desde otro aplicativo
        	// estos datos son modificados, no representa una actualización en los datos asociado al registro de muestreo hemático
        	
        	if (pMuestreoHematico.getEtnia()!=null) {
        		Etnia oEtnia = (Etnia)oEM.find(Etnia.class, pMuestreoHematico.getEtnia().getCatalogoId());
        		oMuestreoHematico.setEtnia(oEtnia);
        	} else {
        		oMuestreoHematico.setEtnia(null);
        	}
        	
        	// la comunidad se considera requerida
        	Comunidad oComunidadResidencia = (Comunidad)oEM.find(Comunidad.class, pMuestreoHematico.getComunidadResidencia().getComunidadId());
        	oMuestreoHematico.setComunidadResidencia(oComunidadResidencia);
        	
        	oMuestreoHematico.setDireccionResidencia(pMuestreoHematico.getDireccionResidencia());
        	oMuestreoHematico.setFechaNacimiento(pMuestreoHematico.getFechaNacimiento());
        	
        	DivisionPolitica oMunicipioResidencia = (DivisionPolitica)oEM.find(DivisionPolitica.class, pMuestreoHematico.getMunicipioResidencia().getDivisionPoliticaId());
        	oMuestreoHematico.setMunicipioResidencia(oMunicipioResidencia);
        	
        	// el sexo es requerido
        	Sexo oSexo = (Sexo)oEM.find(Sexo.class, pMuestreoHematico.getSexo().getCatalogoId());
        	oMuestreoHematico.setSexo(oSexo);

        	// datos de la ficha
        	
        	oMuestreoHematico.setTipoBusqueda(pMuestreoHematico.getTipoBusqueda());
        	oMuestreoHematico.setEmbarazada(pMuestreoHematico.getEmbarazada());
        	oMuestreoHematico.setPersonaReferente(pMuestreoHematico.getPersonaReferente()!=null && !pMuestreoHematico.getPersonaReferente().isEmpty()?pMuestreoHematico.getPersonaReferente():null);
        	oMuestreoHematico.setTelefonoReferente(pMuestreoHematico.getTelefonoReferente()!=null && !pMuestreoHematico.getTelefonoReferente().isEmpty()?pMuestreoHematico.getTelefonoReferente():null);
    
        	// si se declaró la manzana, la busca en el contexto de la persistencia
        	if (pMuestreoHematico.getManzana()!=null) {
        		Manzana oManzana = (Manzana)oEM.find(Manzana.class, pMuestreoHematico.getManzana().getManzanaId());
        		oMuestreoHematico.setManzana(oManzana);
        	} else {
        		oMuestreoHematico.setManzana(null);
        	}
        	
        	// si se declaró la vivienda, la busca en el contexto de la persistencia
        	if (pMuestreoHematico.getVivienda()!=null) {
        		Vivienda oVivienda = (Vivienda)oEM.find(Vivienda.class, pMuestreoHematico.getVivienda().getViviendaId());
        		oMuestreoHematico.setVivienda(oVivienda);
        	} else {
        		oMuestreoHematico.setVivienda(null);
        	}
        	
        	oMuestreoHematico.setInicioSintomas(pMuestreoHematico.getInicioSintomas());
        	oMuestreoHematico.setManejoClinico(pMuestreoHematico.getManejoClinico());
        	oMuestreoHematico.setInicioTratamiento(pMuestreoHematico.getInicioTratamiento());
        	oMuestreoHematico.setFinTratamiento(pMuestreoHematico.getFinTratamiento());
        	oMuestreoHematico.setTratamientoEnBoca(pMuestreoHematico.getTratamientoEnBoca());
        	oMuestreoHematico.setTratamientoRemanente(pMuestreoHematico.getTratamientoRemanente());
        	oMuestreoHematico.setCloroquina(pMuestreoHematico.getCloroquina());
        	oMuestreoHematico.setPrimaquina5mg(pMuestreoHematico.getPrimaquina5mg());
        	oMuestreoHematico.setPrimaquina15mg(pMuestreoHematico.getPrimaquina15mg());
        	
        	// datos de la prueba rápida
        	
        	if (pMuestreoHematico.getPruebaRapida()==null) {
        		oMuestreoHematico.setPruebaRapida(null);
        	} else {
        		// para la prueba rápida de malaria, todos los datos
        		// son requeridos, por tanto si luego de la validación
        		// el objeto no es nulo, podría ser ya existía y
        		// los datos hayan sido eliminados.  Por tanto, si la fecha
        		// es nula, todo el objeto sera null
        		if ((pMuestreoHematico.getPruebaRapida().getFecha()==null)) {
        			oMuestreoHematico.setPruebaRapida(null);
        		} else {
        			if (oMuestreoHematico.getPruebaRapida()==null) {
        				// como no existían datos de la prueba rápida en la base de datos
        				// se coloca directamente el objeto de la prueba rápida
        				oMuestreoHematico.setPruebaRapida(pMuestreoHematico.getPruebaRapida());
        			} else {
        				// si existen datos de la prueba rápida, se obtiene el objeto del contexto
        				// para luego modificarlo y ser persistido
        				MuestreoPruebaRapida oMuestreoPruebaRapida = (MuestreoPruebaRapida)oEM.find(MuestreoPruebaRapida.class, oMuestreoHematico.getPruebaRapida().getMuestreoPruebaRapidaId());
        				oMuestreoPruebaRapida.setFecha(pMuestreoHematico.getPruebaRapida().getFecha());
        				
        				if (pMuestreoHematico.getPruebaRapida().getResultado()==null) {
        					oMuestreoPruebaRapida.setResultado(null);
        				} else {
        					ResultadoPruebaRapida oResultadoPruebaRapida = (ResultadoPruebaRapida)oEM.find(ResultadoPruebaRapida.class, pMuestreoHematico.getPruebaRapida().getResultado().getCatalogoId());
        					oMuestreoPruebaRapida.setResultado(oResultadoPruebaRapida);
        				}
        				
        				if (pMuestreoHematico.getPruebaRapida().getRealizado()==null) {
        					oMuestreoPruebaRapida.setRealizado(null);
        				} else {
        					ResponsablePruebaRapida oResponsablePruebaRapida = (ResponsablePruebaRapida)oEM.find(ResponsablePruebaRapida.class, pMuestreoHematico.getPruebaRapida().getRealizado().getCatalogoId());
        					oMuestreoPruebaRapida.setRealizado(oResponsablePruebaRapida);
        				}
        				
        				oMuestreoHematico.setPruebaRapida(oMuestreoPruebaRapida);
        			}
        		}
        	}

        	// datos del diagnóstico por la técnica de la gota gruesa
        	
        	if (pMuestreoHematico.getDiagnostico()==null) {
        		oMuestreoHematico.setDiagnostico(null);
        	} else {
        		
        		// tanto para el diagnóstico como para el motivo de la falta de diagnóstico, la fecha de recepción es requerida, 
        		// por consiguiente si dicha fecha no existe, significaría que todos los datos asociados al diagnóstico fueron eliminados
        		
        		if ((pMuestreoHematico.getDiagnostico().getFechaRecepcion()==null)) {
        			oMuestreoHematico.setDiagnostico(null);
        		} else {
        			
        			// si el identificador del objeto del diagnóstico es diferente de cero, implica que ya existía un
        			// diagnóstico declarado en la base de datos, por tanto, obtiene el objeto para persistir los cambios,
        			// caso contrario, únicamente actualiza los datos
        			
        			if (pMuestreoHematico.getDiagnostico().getMuestreoDiagnosticoId()!=0) {
        				
        				MuestreoDiagnostico oMuestreoDiagnostico = (MuestreoDiagnostico)oEM.find(MuestreoDiagnostico.class, oMuestreoHematico.getDiagnostico().getMuestreoDiagnosticoId());
        				oMuestreoDiagnostico.setFechaRecepcion(pMuestreoHematico.getDiagnostico().getFechaRecepcion());
        				oMuestreoDiagnostico.setFechaDiagnostico(pMuestreoHematico.getDiagnostico().getFechaDiagnostico());
        				oMuestreoDiagnostico.setResultado(pMuestreoHematico.getDiagnostico().getResultado());
						oMuestreoDiagnostico.setPositivoPVivax(pMuestreoHematico.getDiagnostico().getPositivoPVivax());
						oMuestreoDiagnostico.setPositivoPFalciparum(pMuestreoHematico.getDiagnostico().getPositivoPFalciparum());
						
						if (pMuestreoHematico.getDiagnostico().getDensidadPVivax()!=null) {
							DensidadCruces oDensidadPVivax = (DensidadCruces)oEM.find(DensidadCruces.class, pMuestreoHematico.getDiagnostico().getDensidadPVivax().getCatalogoId());
							oMuestreoDiagnostico.setDensidadPVivax(oDensidadPVivax);
						} else {
							oMuestreoDiagnostico.setDensidadPVivax(null);
						}
						
						if (pMuestreoHematico.getDiagnostico().getDensidadPFalciparum()!=null) {
							DensidadCruces oDensidadPFalciparum = (DensidadCruces)oEM.find(DensidadCruces.class, pMuestreoHematico.getDiagnostico().getDensidadPFalciparum().getCatalogoId());
							oMuestreoDiagnostico.setDensidadPFalciparum(oDensidadPFalciparum);
						} else {
							oMuestreoDiagnostico.setDensidadPFalciparum(null);
						}

						if (pMuestreoHematico.getDiagnostico().getEstadioPFalciparum()!=null) {
							EstadioPFalciparum oEstadioPFalciparum = (EstadioPFalciparum)oEM.find(EstadioPFalciparum.class, pMuestreoHematico.getDiagnostico().getEstadioPFalciparum().getCatalogoId());
							oMuestreoDiagnostico.setEstadioPFalciparum(oEstadioPFalciparum);
						} else {
							oMuestreoDiagnostico.setEstadioPFalciparum(null);
						}

    					oMuestreoDiagnostico.setEstadiosAsexuales(pMuestreoHematico.getDiagnostico().getEstadiosAsexuales());
    					oMuestreoDiagnostico.setGametocitos(pMuestreoHematico.getDiagnostico().getGametocitos());
    					oMuestreoDiagnostico.setLeucocitos(pMuestreoHematico.getDiagnostico().getLeucocitos());
        				
        				if (pMuestreoHematico.getDiagnostico().getMotivoFaltaDiagnostico()!=null) {
            				MotivoFaltaDiagnostico oMotivoFaltaDiagnostico = (MotivoFaltaDiagnostico)oEM.find(MotivoFaltaDiagnostico.class, pMuestreoHematico.getDiagnostico().getMotivoFaltaDiagnostico().getCatalogoId());
            				oMuestreoDiagnostico.setMotivoFaltaDiagnostico(oMotivoFaltaDiagnostico);
            			} else {
            				oMuestreoDiagnostico.setMotivoFaltaDiagnostico(null);
            			}

        				if (pMuestreoHematico.getDiagnostico().getEntidadAdtvaLaboratorio()==null) {
        					oMuestreoDiagnostico.setEntidadAdtvaLaboratorio(null);
        				} else {
        					EntidadAdtva oEntidadLab = (EntidadAdtva)oEM.find(EntidadAdtva.class, pMuestreoHematico.getDiagnostico().getEntidadAdtvaLaboratorio().getEntidadAdtvaId());
        					oMuestreoDiagnostico.setEntidadAdtvaLaboratorio(oEntidadLab);
        				}
            			
        				if (pMuestreoHematico.getDiagnostico().getMunicipioLaboratorio()==null) {
        					oMuestreoDiagnostico.setMunicipioLaboratorio(null);
        				} else {
        					DivisionPolitica oMunicipioLab = (DivisionPolitica)oEM.find(DivisionPolitica.class, pMuestreoHematico.getDiagnostico().getMunicipioLaboratorio().getDivisionPoliticaId());
        					oMuestreoDiagnostico.setMunicipioLaboratorio(oMunicipioLab);
        				}
            			
        				if (pMuestreoHematico.getDiagnostico().getUnidadLaboratorio()==null) {
        					oMuestreoDiagnostico.setUnidadLaboratorio(null);
        				} else {
        					Unidad oUnidadLab = (Unidad)oEM.find(Unidad.class, pMuestreoHematico.getDiagnostico().getUnidadLaboratorio().getUnidadId());
        					oMuestreoDiagnostico.setUnidadLaboratorio(oUnidadLab);
        				}
            			
        				oMuestreoDiagnostico.setLaboratorista(pMuestreoHematico.getDiagnostico().getLaboratorista()!=null && !pMuestreoHematico.getDiagnostico().getLaboratorista().isEmpty()?pMuestreoHematico.getDiagnostico().getLaboratorista():null);
        				oMuestreoHematico.setDiagnostico(oMuestreoDiagnostico);
        				
        			}
        		}
        	}
        	
        	// obtiene el objeto sisPersona vinculado al contexto, dicho objeto
        	// es la versión anterior a cualquier cambio, los cuales, de existir, se encuentran
        	// en el objeto pPersona

        	SisPersona sisPersona = oEM.find(SisPersona.class,pPersona.getPersonaId());
        	if (sisPersona==null) {
        		oResultado.setFilasAfectadas(0);
        		oResultado.setExcepcion(false);
        		oResultado.setFuenteError("MuestreoHematicoDA.Guardar.GuardarPersona");
        		oResultado.setMensaje("La persona a la cual se encuentra vinculado el muestreo hemático no existe.");
        		oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
        		oResultado.setOk(false);
        		return oResultado;
        	}

        	oMuestreoHematico.setSisPersona(sisPersona);
        	
        	// para evitar una actualización innecesaria del registro de persona
        	// se compara la versión anterior de sisPersona y el objeto pPersona
        	
        	boolean iPersonaSinCambio=PersonaValidacion.comparar(pPersona, sisPersona);

    		ctx = new InitialContext();
    		personaUTMService = (PersonaUTMService)ctx.lookup("ejb/PersonaUTM");
    		personaUTMService.iniciarTransaccion();

        	if (!iPersonaSinCambio || (pPersona.getVerificados()!=sisPersona.isConfirmado())) {

        		if (!iPersonaSinCambio) {
        			InfoResultado oResultadoPersona=personaUTMService.guardarPersona(pPersona, pMuestreoHematico.getUsuarioRegistro());
        			if (!oResultadoPersona.isOk()) {
        				oResultado.setFilasAfectadas(0);
        				oResultado.setExcepcion(false);
        				oResultado.setFuenteError("MuestreoHematicoDA.Guardar.GuardarPersona");
        				oResultado.setMensaje(oResultadoPersona.getMensaje());
        				oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
        				oResultado.setOk(false);
        				personaUTMService.rollbackTransaccion();
        				return oResultado;
        			}
        		}

        		if (!sisPersona.isConfirmado() && pPersona.getVerificados()) {
        			InfoResultado oResVerificar=personaUTMService.confirmarDatosPersonales(pPersona);
        			if (!oResVerificar.isOk()) {
        				oResultado.setFilasAfectadas(0);
        				oResultado.setExcepcion(false);
        				oResultado.setFuenteError("MuestreoHematicoDA.Guardar.GuardarPersona");
        				oResultado.setMensaje(oResVerificar.getMensaje());
        				oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
        				oResultado.setOk(false);
        				personaUTMService.rollbackTransaccion();
        				return oResultado;
        			}
            	}
        	}
        	
            oEM.getTransaction().commit();
            personaUTMService.commitTransaccion();
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
    		try {
				personaUTMService.rollbackTransaccion();
			} catch (Exception e) {
				e.printStackTrace();
			}
    		return oResultado;
    		
    	} catch (ConstraintViolationException iExcepcion) {
    		oResultado.setExcepcion(true);
    		ConstraintViolation<?> oConstraintViolation = iExcepcion.getConstraintViolations().iterator().next();
    		oResultado.setMensaje(oConstraintViolation.getMessage());
    		oResultado.setFuenteError(oConstraintViolation.getPropertyPath().toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setFilasAfectadas(0);
    		try {
				personaUTMService.rollbackTransaccion();
			} catch (Exception e) {
				e.printStackTrace();
			}
    		return oResultado;
    	} 
    	catch (PersistenceException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Guardar");
    		oResultado.setMensaje(Mensajes.REGISTRO_NO_GUARDADO);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setOk(false);
    		try {
				personaUTMService.rollbackTransaccion();
			} catch (Exception e) {
				e.printStackTrace();
			}
    		return oResultado;
    		
    	} catch (Exception iExcepcion){
    		oResultado.setExcepcion(true);
    		oResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO + iExcepcion.getMessage());
    		oResultado.setFuenteError(iExcepcion.toString().split(":",1).toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
    		oResultado.setFilasAfectadas(0);
    		iExcepcion.printStackTrace();
    		try {
				personaUTMService.rollbackTransaccion();
			} catch (Exception e) {
				e.printStackTrace();
			}
    		return oResultado;
    		
    	} finally{
    		oEM.close();
    	}
	}

	@Override
	public InfoResultado Agregar(MuestreoHematico pMuestreoHematico, Persona pPersona) {

		InfoResultado oResultado=new InfoResultado();

		oResultado=PersonaValidacion.validar(pPersona);
		if (!oResultado.isOk()) return oResultado;

		oResultado=VigilanciaValidacion.validarMuestreoHematico(pMuestreoHematico);
		if (!oResultado.isOk()) return oResultado;
		
		oResultado=new InfoResultado();

    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
		@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);

		InitialContext ctx;
		
        try{

			ctx = new InitialContext();
			PersonaUTMService personaUTMService = (PersonaUTMService)ctx.lookup("ejb/PersonaUTM");
        	personaUTMService.iniciarTransaccion();

        	InfoResultado oResultadoPersona=personaUTMService.guardarPersona(pPersona, pMuestreoHematico.getUsuarioRegistro());
        	if (!oResultadoPersona.isOk()) {
        		oResultado.setFilasAfectadas(0);
        		oResultado.setExcepcion(false);
        		oResultado.setFuenteError("Agregar Muestreo Hemático");
        		oResultado.setMensaje(oResultadoPersona.getMensaje());
        		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
        		oResultado.setOk(false);
        		personaUTMService.rollbackTransaccion();
        		return oResultado;
        	}

            personaUTMService.commitTransaccion();

        	if (pPersona.getPersonaId()==0) {
        		pPersona.setPersonaId(((Persona)oResultadoPersona.getObjeto()).getPersonaId());
        	}
        	
			InfoResultado oResSisPersona=sisPersonaService.Encontrar(pPersona.getPersonaId());
			
			if (!oResSisPersona.isOk()) {
				personaUTMService.rollbackTransaccion();
				oResultado.setFilasAfectadas(0);
        		oResultado.setExcepcion(false);
        		oResultado.setFuenteError("Agregar Muestreo Hemático");
        		oResultado.setMensaje(oResSisPersona.getMensaje());
        		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
        		oResultado.setOk(false);
				return oResultado;
			}

			pMuestreoHematico.setSisPersona((SisPersona)oResSisPersona.getObjeto());
        	oEM.persist(pMuestreoHematico);

            oEM.getTransaction().commit();
            oResultado.setObjeto(pMuestreoHematico);
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);
    		
    		return oResultado;
   			
		} catch (NamingException e) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Agregar Muestreo Hemático");
    		oResultado.setMensaje(Mensajes.SERVICIO_NO_ENCONTRADO + " " + Mensajes.NOTIFICACION_ADMINISTRADOR);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setOk(false);
    		return oResultado;
    		
    	} catch (EntityExistsException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Agregar Muestreo Hemático");
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
    		oResultado.setFuenteError("Agregar Muestreo Hemático");
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
	public InfoResultado Eliminar(long pMuestreoHematicoId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		MuestreoHematico oMuestreoHematico = (MuestreoHematico)oEM.find(MuestreoHematico.class, pMuestreoHematicoId);
    		if (oMuestreoHematico!=null) {
    			oEM.remove(oMuestreoHematico);
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
	public MuestreoHematico EncontrarPorLamina(String pClave,BigDecimal pNumeroLamina) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("MuestreoHematico.encontrarPorLamina");
            query.setParameter("pClave", pClave);
            query.setParameter("pNumeroLamina",pNumeroLamina);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return (MuestreoHematico)query.getSingleResult();
        }catch (NoResultException iExcepcion) {
        	return null;
        }
        finally{
            em.close();
        }			
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MuestreoHematico> ListarPorPersona(long pPersonaId,Date pFechaToma) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("MuestreoHematico.listarPorPersona");
            query.setParameter("pPersonaId", pPersonaId);
            query.setParameter("pFechaToma",pFechaToma);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        } finally{
            em.close();
        }			
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MuestreoHematico> ListarPorClave(String pClave,Date pFechaToma) {
		
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("MuestreoHematico.listarPorClave");
            query.setParameter("pClave", pClave);
            query.setParameter("pFechaToma",pFechaToma);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return(query.getResultList());
        } finally{
            em.close();
        }			
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MuestreoHematico> ListarPostivosPorUnidad(long pUnidadId) {
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
		try {
			Query query = null;
			query = em.createNamedQuery("MuestreoHematico.listarPositivosPorUnidad");
			query.setParameter("pUnidadId", pUnidadId);
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			return (query.getResultList());
		} finally {
			em.close();
		}
	}

}
