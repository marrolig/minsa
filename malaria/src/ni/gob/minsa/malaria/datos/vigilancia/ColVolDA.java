package ni.gob.minsa.malaria.datos.vigilancia;

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
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.general.ClaseAccesibilidad;
import ni.gob.minsa.malaria.modelo.general.TipoTransporte;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.sis.SisPersona;
import ni.gob.minsa.malaria.modelo.vigilancia.ColVol;
import ni.gob.minsa.malaria.modelo.vigilancia.ColVolAcceso;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoComunidad;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoNotificacion;
import ni.gob.minsa.malaria.reglas.PersonaValidacion;
import ni.gob.minsa.malaria.reglas.VigilanciaValidacion;
import ni.gob.minsa.malaria.servicios.sis.SisPersonaService;
import ni.gob.minsa.malaria.servicios.vigilancia.ColVolService;
import ni.gob.minsa.malaria.soporte.Mensajes;

public class ColVolDA implements ColVolService {

	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();
    private static SisPersonaService sisPersonaService = new SisPersonaDA();

	@Override
	public InfoResultado Encontrar(long pColVolId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		ColVol oColVol = (ColVol)oEM.find(ColVol.class, pColVolId);
    		if (oColVol!=null) {
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oColVol);
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
	public InfoResultado Guardar(ColVol pColVol, 
								 Persona pPersona, 
								 PuestoNotificacion pPuestoNotificacion) {

		InfoResultado oResultado=new InfoResultado();
		oResultado=VigilanciaValidacion.validarColVol(pColVol);
		if (!oResultado.isOk()) return oResultado;
		
		oResultado=VigilanciaValidacion.validarPuestoNotificacion(pPuestoNotificacion, pColVol);
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
        	ColVol oDetachedColVol = (ColVol)oEM.find(ColVol.class, pColVol.getColVolId());
        	ColVol oColVol=oEM.merge(oDetachedColVol);
        	oColVol.setFechaInicio(pColVol.getFechaInicio());
        	oColVol.setFechaFin(pColVol.getFechaFin());
        	oColVol.setLatitud(pColVol.getLatitud());
        	oColVol.setLongitud(pColVol.getLongitud());
        	oColVol.setObservaciones(pColVol.getObservaciones());
        	
        	if (pColVol.getColVolAcceso()==null) {
        		oColVol.setColVolAcceso(null);
        	} else {
        		// El objeto que contiene los datos de acceso no es nulo, pero
        		// puede ser que no tenga datos asociados y solo contenga el identificador
        		if ((pColVol.getColVolAcceso().getClaseAccesibilidad()==null) &&
        		   (pColVol.getColVolAcceso().getComoLlegar()==null || pColVol.getColVolAcceso().getComoLlegar().trim().isEmpty()) &&
        		   (pColVol.getColVolAcceso().getDistancia()==null && pColVol.getColVolAcceso().getTiempo()==null) &&
        		   (pColVol.getColVolAcceso().getPuntoReferencia()==null || pColVol.getColVolAcceso().getPuntoReferencia().trim().isEmpty()) && 
        		   (pColVol.getColVolAcceso().getTipoTransporte()==null)) 
        			if (oColVol.getColVolAcceso()!=null) {
            		ColVolAcceso oColVolAcceso = (ColVolAcceso)oEM.find(ColVolAcceso.class, oColVol.getColVolAcceso().getColVolAccesoId());
            		if (oColVolAcceso!=null) {
            			oEM.remove(oColVolAcceso);
            		}
        			oColVol.setColVolAcceso(null);
        		} else {
        			if (oColVol.getColVolAcceso()==null) {
        				oColVol.setColVolAcceso(pColVol.getColVolAcceso());
        			} else {
        				ColVolAcceso oColVolAcceso=(ColVolAcceso)oEM.find(ColVolAcceso.class,oColVol.getColVolAcceso().getColVolAccesoId());
        				oColVolAcceso.setComoLlegar(pColVol.getColVolAcceso().getComoLlegar());

        				if (pColVol.getColVolAcceso().getTipoTransporte()==null) {
        					oColVolAcceso.setTipoTransporte(null);
        				} else {
        					TipoTransporte oTipoTransporte = (TipoTransporte)oEM.find(TipoTransporte.class, pColVol.getColVolAcceso().getTipoTransporte().getCatalogoId());
        					oColVolAcceso.setTipoTransporte(oTipoTransporte);
        				}
        			
        				if (pColVol.getColVolAcceso().getClaseAccesibilidad()==null) {
        					oColVolAcceso.setClaseAccesibilidad(null);
        				} else {
        					ClaseAccesibilidad oClaseAccesibilidad = (ClaseAccesibilidad)oEM.find(ClaseAccesibilidad.class, pColVol.getColVolAcceso().getClaseAccesibilidad().getCatalogoId());
        					oColVolAcceso.setClaseAccesibilidad(oClaseAccesibilidad);
        				}
           			
        				oColVolAcceso.setDistancia(pColVol.getColVolAcceso().getDistancia());
        				oColVolAcceso.setPuntoReferencia(pColVol.getColVolAcceso().getPuntoReferencia());
        				oColVolAcceso.setTiempo(pColVol.getColVolAcceso().getTiempo());
        				oColVol.setColVolAcceso(oColVolAcceso);
        			}
        		}
        	}

        	Unidad oUnidad=(Unidad)oEM.find(Unidad.class,pColVol.getUnidad().getUnidadId());
        	oColVol.setUnidad(oUnidad);

        	// obtiene el objeto sisPersona vinculado al contexto, dicho objeto
        	// es la versión anterior a cualquier cambio, los cuales, de existir, se encuentran
        	// en el objeto pPersona
        	SisPersona sisPersona = oEM.find(SisPersona.class,pPersona.getPersonaId());
        	if (sisPersona==null) {
        		oResultado.setFilasAfectadas(0);
        		oResultado.setExcepcion(false);
        		oResultado.setFuenteError("ColVolDA.Guardar.GuardarPersona");
        		oResultado.setMensaje("La persona a la cual se encuentra vinculado el colaborador voluntario no existe.");
        		oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
        		oResultado.setOk(false);
        		return oResultado;
        	}

        	oColVol.setSisPersona(sisPersona);
        	
        	// para evitar una actualización innecesaria del registro de persona
        	// se compara la versión anterior de sisPersona y el objeto pPersona
        	
        	boolean iPersonaSinCambio=PersonaValidacion.comparar(pPersona, sisPersona);

    		ctx = new InitialContext();
    		personaUTMService = (PersonaUTMService)ctx.lookup("ejb/PersonaUTM");
    		personaUTMService.iniciarTransaccion();

        	if (!iPersonaSinCambio || (pPersona.getVerificados()!=sisPersona.isConfirmado())) {

        		if (!iPersonaSinCambio) {
        			InfoResultado oResultadoPersona=personaUTMService.guardarPersona(pPersona, pColVol.getUsuarioRegistro());
        			if (!oResultadoPersona.isOk()) {
        				oResultado.setFilasAfectadas(0);
        				oResultado.setExcepcion(false);
        				oResultado.setFuenteError("ColVolDA.Guardar.GuardarPersona");
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
        				oResultado.setFuenteError("ColVolDA.Guardar.GuardarPersona");
        				oResultado.setMensaje(oResVerificar.getMensaje());
        				oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
        				oResultado.setOk(false);
        				personaUTMService.rollbackTransaccion();
        				return oResultado;
        			}
            	}
        	}

        	// guarda los datos vinculados al puesto de notificación.
        	// a) si el colVol ya ha sido declarado como puesto de notificación, y los datos
        	//    asociados al puesto han sido eliminados, el registro del puesto de notificación
        	//    será eliminado, siempre y cuando no existan registros vinculados a éste
        	// b) si el colVol ya ha sido declarado como puesto de notificación, los datos
        	//    serán actualizados
        	// c) si el colVol no ha sido declarado como puesto de notificación, se creará 
        	//    el registro correspondiente

        	if (pPuestoNotificacion.getPuestoNotificacionId()!=0) {
        		
        		if (pPuestoNotificacion.getClave()==null || pPuestoNotificacion.getClave().trim().isEmpty()) {
            		PuestoNotificacion oPuestoNotificacion = (PuestoNotificacion)oEM.find(PuestoNotificacion.class, pPuestoNotificacion.getPuestoNotificacionId());
            		if (oPuestoNotificacion!=null) {

            			oEM.remove(oPuestoNotificacion);
            		}
        		} else {
        			
                	PuestoNotificacion oDetachedPuesto = (PuestoNotificacion)oEM.find(PuestoNotificacion.class, pPuestoNotificacion.getPuestoNotificacionId());
                	PuestoNotificacion oPuestoNotificacion=oEM.merge(oDetachedPuesto);
                	oPuestoNotificacion.setClave(pPuestoNotificacion.getClave());
                	oPuestoNotificacion.setFechaApertura(pPuestoNotificacion.getFechaApertura());
                	oPuestoNotificacion.setFechaCierre(pPuestoNotificacion.getFechaCierre());
        		}
        	} else {
        		
        		// si la clave fue definida y es un objeto válido, implica que el puesto de notificación será nulo
        		if (pPuestoNotificacion.getClave()!=null && !pPuestoNotificacion.getClave().trim().isEmpty()) {
        			
        			pPuestoNotificacion.setColVol(oColVol);
        			oEM.persist(pPuestoNotificacion);
        		
        			// agrega la comunidad de residencia del colVol como la comunidad
        			// por omisión que se vinculará al puesto de notificación
        		
        			PuestoComunidad oPuestoComunidad = new PuestoComunidad();
        			Comunidad oComunidad = oEM.find(Comunidad.class, pPersona.getComuResiId());
        			oPuestoComunidad.setComunidad(oComunidad);
        			oPuestoComunidad.setFechaRegistro(pPuestoNotificacion.getFechaRegistro());
        			oPuestoComunidad.setUsuarioRegistro(pPuestoNotificacion.getUsuarioRegistro());
        			oPuestoComunidad.setPuestoNotificacion(pPuestoNotificacion);
        			oEM.persist(oPuestoComunidad);
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

	/*
	 * (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.vigilancia.ColVolService#Agregar(ni.gob.minsa.malaria.modelo.vigilancia.ColVol, ni.gob.minsa.ejbPersona.dto.Persona)
	 */
	@Override
	public InfoResultado Agregar(ColVol pColVol, Persona pPersona, PuestoNotificacion pPuestoNotificacion) {

		InfoResultado oResultado=new InfoResultado();

		oResultado=PersonaValidacion.validar(pPersona);
		if (!oResultado.isOk()) return oResultado;

		oResultado=VigilanciaValidacion.validarColVol(pColVol);
		if (!oResultado.isOk()) return oResultado;
		
		oResultado=VigilanciaValidacion.validarPuestoNotificacion(pPuestoNotificacion, pColVol);
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

        	InfoResultado oResultadoPersona=personaUTMService.guardarPersona(pPersona, pColVol.getUsuarioRegistro());
        	if (!oResultadoPersona.isOk()) {
        		oResultado.setFilasAfectadas(0);
        		oResultado.setExcepcion(false);
        		oResultado.setFuenteError("Agregar ColVol");
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
        		oResultado.setFuenteError("Agregar ColVol");
        		oResultado.setMensaje(oResSisPersona.getMensaje());
        		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
        		oResultado.setOk(false);
				return oResultado;
			}

			pColVol.setSisPersona((SisPersona)oResSisPersona.getObjeto());
        	oEM.persist(pColVol);

    		// si la clave fue definida y es un objeto válido, implica que el puesto de notificación será nulo
    		if (pPuestoNotificacion.getClave()!=null && !pPuestoNotificacion.getClave().trim().isEmpty()) {
    			
    			pPuestoNotificacion.setColVol(pColVol);
    			oEM.persist(pPuestoNotificacion);
    		
    			// agrega la comunidad de residencia del colVol como la comunidad
    			// por omisión que se vinculará al puesto de notificación
    		
    			PuestoComunidad oPuestoComunidad = new PuestoComunidad();
    			Comunidad oComunidad = oEM.find(Comunidad.class, pColVol.getSisPersona().getComunidadResidencia().getComunidadId());
    			oPuestoComunidad.setComunidad(oComunidad);
    			oPuestoComunidad.setFechaRegistro(pPuestoNotificacion.getFechaRegistro());
    			oPuestoComunidad.setUsuarioRegistro(pPuestoNotificacion.getUsuarioRegistro());
    			oPuestoComunidad.setPuestoNotificacion(pPuestoNotificacion);
    			oEM.persist(oPuestoComunidad);
    		}

            oEM.getTransaction().commit();
            oResultado.setObjeto(pColVol);
    		oResultado.setFilasAfectadas(1);
    		oResultado.setOk(true);
    		
    		return oResultado;
   			
		} catch (NamingException e) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Agregar ColVol");
    		oResultado.setMensaje(Mensajes.SERVICIO_NO_ENCONTRADO + " " + Mensajes.NOTIFICACION_ADMINISTRADOR);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setOk(false);
    		return oResultado;
    		
    	} catch (EntityExistsException iExPersistencia) {
    		oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Agregar ColVol");
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
    		oResultado.setFuenteError("Agregar ColVol");
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
	public InfoResultado Eliminar(long pColVolId) {
		InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	oEM.getTransaction().begin();
    	@SuppressWarnings("unused")
		java.sql.Connection connection = oEM.unwrap(java.sql.Connection.class);
    	try{
    		ColVol oColVol = (ColVol)oEM.find(ColVol.class, pColVolId);
    		if (oColVol!=null) {
    			oEM.remove(oColVol);
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
	public List<ColVol> ListarPorUnidad(long pUnidadId, String pNombre, boolean pSoloActivos, int pPaginaActual, int pTotalPorPagina, int pNumRegistros) {
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
	    try{
	    	Query query = null;
	    	if (pNombre==null || pNombre.trim().isEmpty()) {
	    		query=em.createNamedQuery("ColVol.listarPorUnidad");
	    	} else {
	    		query=em.createNamedQuery("ColVol.listarPorNombre");
	    		query.setParameter("pNombre",pNombre.toUpperCase());
	    	}
	    	query.setParameter("pUnidadId",pUnidadId);
	    	query.setParameter("pTodos", pSoloActivos?0:1);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            
            if (pNumRegistros<=pPaginaActual) pPaginaActual-=pNumRegistros;
            pPaginaActual=pNumRegistros<=pPaginaActual ? 0: pPaginaActual;
            query.setFirstResult(pPaginaActual);
            query.setMaxResults(pTotalPorPagina);

	        return(query.getResultList());
	    }finally{
	    	em.close();
	    }		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ColVol> ListarPorUnidad(long pUnidadId) {
		EntityManager em = jpaResourceBean.getEMF().createEntityManager();
	    try{
	    	Query query = null;
	    	query=em.createNamedQuery("ColVol.listarPorUnidad");
	    	query.setParameter("pUnidadId",pUnidadId);
	    	query.setParameter("pTodos", 0);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
	        return(query.getResultList());
	    }finally{
	    	em.close();
	    }		
	}

	@Override
	public ColVol EncontrarPorPersona(long pPersonaId) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        try{
            Query query = em.createNamedQuery("ColVol.encontrarPorPersona");
            query.setParameter("pPersonaId", pPersonaId);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return (ColVol)query.getSingleResult();
        }catch (NoResultException iExcepcion) {
        	return null;
        }
        finally{
            em.close();
        }			
	}

	@Override
	public int ContarPorUnidad(long pUnidadId, String pNombre, boolean pSoloActivos) {
        EntityManager em = jpaResourceBean.getEMF().createEntityManager();
        int totalColVoles=0;
        
        if ((pNombre==null) || (pNombre.trim().isEmpty())) {
        	Query query = em.createQuery("select count(tc) from ColVol tc " +
        								 "where tc.unidad.unidadId=:pUnidadId and " +
        								 "   (:pTodos=1 or (:pTodos=0 and (tc.fechaFin is null or tc.fechaFin>CURRENT_DATE))) ");
            query.setParameter("pUnidadId", pUnidadId);
            query.setParameter("pTodos", pSoloActivos?0:1);
        	totalColVoles = ((Long)query.getSingleResult()).intValue();
        } else {
        	Query query = em.createQuery("select count(tc) from ColVol tc " +
										 "where tc.unidad.unidadId=:pUnidadId and " +
										 "  (:pTodos=1 or (:pTodos=0 and (tc.fechaFin is null or tc.fechaFin>CURRENT_DATE))) and " +
										 "  FUNC('CATSEARCH',tc.persona.sndNombre, :pCodigoNombre,null)>0");
        	query.setParameter("pUnidadId", pUnidadId);
        	query.setParameter("pTodos", pSoloActivos?0:1);
        	// TODO Falta obtener el código fonético del nombre y enviarlo como parámetro
        	query.setParameter("pCodigoNombre", pNombre);
        	totalColVoles = ((Long)query.getSingleResult()).intValue();
        }
        return totalColVoles;
	}
}
