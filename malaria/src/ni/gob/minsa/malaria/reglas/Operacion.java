// -----------------------------------------------
// Operacion.java
// -----------------------------------------------
package ni.gob.minsa.malaria.reglas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.ejbPersona.dto.Persona;
import ni.gob.minsa.ejbPersona.servicios.PersonaBTMService;
import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.datos.estructura.UnidadDA;
import ni.gob.minsa.malaria.datos.seguridad.PropiedadUnidadDA;
import ni.gob.minsa.malaria.datos.seguridad.UsuarioEntidadDA;
import ni.gob.minsa.malaria.datos.seguridad.UsuarioUnidadDA;
import ni.gob.minsa.malaria.datos.vigilancia.PuestoNotificacionDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.seguridad.UsuarioEntidad;
import ni.gob.minsa.malaria.modelo.seguridad.UsuarioUnidad;
import ni.gob.minsa.malaria.modelo.sis.SisPersona;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;
import ni.gob.minsa.malaria.servicios.poblacion.UsuarioEntidadService;
import ni.gob.minsa.malaria.servicios.poblacion.UsuarioUnidadService;
import ni.gob.minsa.malaria.servicios.seguridad.PropiedadUnidadService;
import ni.gob.minsa.malaria.servicios.vigilancia.PuestoNotificacionService;


/**
 * Reglas de Operación
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 08/05/2012
 * @since jdk1.6.0_21
 */
public class Operacion {

	/**
	 * Retorna la lista de objetos {@link EntidadAdtva} a las cuales tiene
	 * acceso el usuario; la lista dependerá del valor del parámetro <code>pExplicito</code> y
	 * de las siguientes condiciones:
	 * <p>
	 * <li>Si <code>pExplicito</code> es <code>true</code>, únicamente se retornarán
	 * aquellas entidades administrativas con autorización explícita.  Debe entenderse
	 * que el usuario que tiene privilegio del nivel central, no se le otorga autorización
	 * explicita a ninguna entidad adminitrativa.</li>
	 * <p>
	 * <li>Si <code>pExplicito</code> es <code>false</code>, únicamente se retornarán
	 * aquellas entidades administrativas que están asociada a las unidades de salud a
	 * las cuales se les ha concedido autorización explícita. </li>
	 * <p> 
	 * <li>Si <code>pExplicito</code> es <code>null</code>, se retornarán aquellas
	 * entidades administrativas a las cuales se les ha concedido autorización explícita más
	 * aquellas entidades administrativas asociadas a las unidades de salud a las cuales
	 * se les ha concedido autorización explícita.  En el caso de que el usuario posea
	 * privilegio de nivel central, retornará todas las entidades adminstrativas.</li>
	 * <p>
	 *  
	 * @param pUsuarioId          Identificador el usuario
	 * @param pExplicito          Valor Booleano (true, false o null).  Ver condiciones.
	 * @return Lista de objetos {@link EntidadAdtva}
	 */
	public static List<EntidadAdtva> entidadesAutorizadas(long pUsuarioId, Boolean pExplicito) {

		InfoSesion infoSesion=Utilidades.obtenerInfoSesion();
		
		EntidadAdtvaService entidadAdtvaService = new EntidadAdtvaDA();
		UsuarioEntidadService usuarioEntidadService = new UsuarioEntidadDA();
		UsuarioUnidadService usuarioUnidadService = new UsuarioUnidadDA();
		
		List<EntidadAdtva> oEntidades=new ArrayList<EntidadAdtva>();

		if (infoSesion.isNivelCentral() && pExplicito==null) {
			oEntidades=entidadAdtvaService.EntidadesAdtvasActivas();
			return oEntidades;
		}
		
		// se verifica si tiene una o más entidades autorizadas

		if (pExplicito==null || pExplicito) {
			List<UsuarioEntidad> oUsuariosEntidades = usuarioEntidadService.EntidadesPorUsuario(pUsuarioId);
			if ((oUsuariosEntidades!=null) && (oUsuariosEntidades.size()>0)) {
				for(UsuarioEntidad oUsuarioEntidad:oUsuariosEntidades){
					oEntidades.add(oUsuarioEntidad.getEntidadAdtva());
				}
			}
			
			if (pExplicito!=null) return oEntidades;
		}
		
		// se verifica si tiene unidades asociadas y a través de ellas 
		// obtener la lista de entidades administrativas y agregarlas en
		// caso de que existan entidades autorizadas directamente
		List<UsuarioUnidad> oUsuariosUnidades = usuarioUnidadService.UnidadesPorUsuario(pUsuarioId);
		if ((oUsuariosUnidades!=null) && (oUsuariosUnidades.size()>0)) {
			boolean iExisteEntidad=false;
			for(UsuarioUnidad oUsuarioUnidad:oUsuariosUnidades) {
				iExisteEntidad=false;
				for (EntidadAdtva oEntidad:oEntidades) {
					if (oUsuarioUnidad.getUnidad().getEntidadAdtva().getEntidadAdtvaId()==oEntidad.getEntidadAdtvaId()) {
						iExisteEntidad=true;
						break;
					} 
				} 
				if (!iExisteEntidad) {
					oEntidades.add(oUsuarioUnidad.getUnidad().getEntidadAdtva());
				} 
			} // fin for
		} // fin if
		
		Collections.sort(oEntidades,EntidadAdtva.ORDEN_NOMBRE);

		return oEntidades;

	}
		
	public static boolean esEntidadAutorizada(long pUsuarioId, long pEntidadId) {
		
		UsuarioEntidadService usuarioEntidadService = new UsuarioEntidadDA();
		List<UsuarioEntidad> oUsuariosEntidades = new ArrayList<UsuarioEntidad>();
		oUsuariosEntidades=usuarioEntidadService.EntidadesPorUsuario(pUsuarioId);
		boolean esAutorizado=false;
		
		for(UsuarioEntidad oUsuarioEntidad:oUsuariosEntidades) {
			if (oUsuarioEntidad.getEntidadAdtva().getEntidadAdtvaId()==pEntidadId) {
				esAutorizado=true;
				break;
			}
		}
		return esAutorizado;
		
	}
	
	/**
	 * Retorna una lista de objetos {@link Unidad} que dependen a una entidad administrativa
	 * y cuya tipología se corresponda con el indicado en el parámetro <code>pTipoUnidadId</code>.
	 * Si <code>pTipoUnidadId</code> es cero, se retornarán todas las unidades de salud.</p>
	 * Si <code>pDirecta</code> es <code>true</code>, únicamente se retornarán aquellas unidades de 
	 * salud con autorización directa.  Si <code>pDirecta</code> es <code>false</code> se
	 * retornarán todas las unidades de salud <p>
	 * a) que pertenecen a una entidad administrativa autorizada de forma explícita o <p>
	 * b) que pertenecen a una entidad administrativa cuyo acceso ha sido concedido por el
	 * privilegio de Nivel Central.
	 * c) que pertenecen a una entidad administrativa con acceso implicito, i.e. aquellas
	 * unidades autorización explícita.
	 * <p>
	 * <code>pDirecta</code> con el valor <code>true</code> es de utilidad para modo de acceso ABM <i>(altas,
	 * bajas, modificaciones)</i>.  En este caso, la entidad administrativa debe de ser ímplicita.<p>
	 * <code>pDirecta</code> con el valor <code>false</code> es de utilidad para modo de acceso de lectura
	 * o consulta de datos.  En este caso, la entidad administrativa puede ser explícita o ímplicita
	 * 
	 * @param pUsuarioId			Identificador del usuario que realiza la petición
	 * @param pEntidadId			Identificador de la entidad administrativa
	 * @param pTipoUnidadId         Identificador del tipo de unidad.
	 * @param pDirecta				Valor booleano.  Ver explicación.
	 * @param pPropiedad            Filtro de unidades por una propiedad específica.  Pendiente de implementación.     
	 * @return
	 */
	public static List<Unidad> unidadesAutorizadasPorEntidad(long pUsuarioId,long pEntidadId,
			long pTipoUnidadId,boolean pDirecta, String pPropiedad) {
		
		InfoSesion infoSesion=Utilidades.obtenerInfoSesion();
		
		UnidadService unidadService = new UnidadDA();
		UsuarioUnidadService usuarioUnidadService = new UsuarioUnidadDA();
		
		List<Unidad> oUnidades=new ArrayList<Unidad>();
				
		// si es del nivel central tiene acceso para visualizar todas las unidades
		if (!pDirecta && infoSesion.isNivelCentral()) {
			oUnidades=unidadService.UnidadesActivasPorEntidadYTipo(pEntidadId, pTipoUnidadId);
			if (pPropiedad==null) return oUnidades;
			return filtrarUnidades(oUnidades,pPropiedad);
		}
		
		// si se solicitan únicamente las unidades de salud con autorización directa
		// se debe verificar si la entidad para la cual se solicitan las unidades es una
		// entidad administrativa de acceso ímplicito
			
		if (!pDirecta && esEntidadAutorizada(pUsuarioId,pEntidadId)) {
			oUnidades=unidadService.UnidadesActivasPorEntidadYTipo(pEntidadId, pTipoUnidadId);
			if (pPropiedad==null) return oUnidades;
			return filtrarUnidades(oUnidades,pPropiedad);
		}
		
		// se verifica si tiene unidades autorizadas de forma expresa
		// y únicamente a estas tendrá acceso
		List<UsuarioUnidad> oUsuariosUnidades = usuarioUnidadService.UnidadesPorUsuario(pUsuarioId, pEntidadId, pTipoUnidadId);
		for(UsuarioUnidad oUsuarioUnidad:oUsuariosUnidades){
			oUnidades.add(oUsuarioUnidad.getUnidad());
		}

		if (pPropiedad==null) return oUnidades;
		return filtrarUnidades(oUnidades, pPropiedad);
	}
	
	private static List<Unidad> filtrarUnidades(List<Unidad> pUnidades, String pPropiedad) {

		if (pPropiedad==null) return pUnidades;
		PuestoNotificacionService puestoNotificacionService = new PuestoNotificacionDA();
		PropiedadUnidadService propiedadUnidadService = new PropiedadUnidadDA();
		
		List<Unidad> oUnidades=new ArrayList<Unidad>();

		for(Unidad oUnidad: pUnidades) {
			if (pPropiedad.contains(Utilidades.ES_PUESTO_NOTIFICACION)) {
				if (puestoNotificacionService.EncontrarPorUnidad(oUnidad.getUnidadId(),1)!=null) {
					oUnidades.add(oUnidad);
					break;
				}
			}
		
			if (pPropiedad.contains(Utilidades.DECLARA_MUESTREO_HEMATICO)) {
				if (propiedadUnidadService.TienePropiedad(oUnidad.getUnidadId(), Utilidades.DECLARA_MUESTREO_HEMATICO)) {
					oUnidades.add(oUnidad);
				}
			}
			if (pPropiedad.contains(Utilidades.ES_UNIDAD_TRANSFUSIONAL)) {
				if (propiedadUnidadService.TienePropiedad(oUnidad.getUnidadId(), Utilidades.ES_UNIDAD_TRANSFUSIONAL)) {
					oUnidades.add(oUnidad);
				}
			}
		}
		
		return oUnidades;
	}
	
	/**
	 * Retorna el objeto {@link Persona} a partir del objeto {@link SisPersona}.
	 * No se efectúa ninguna validación en este proceso.  La validación debe ser
	 * efectuada con el método correspondiente sobre el objeto {@link SisPersona}
	 * 
	 * @param pSisPersona Objeto {@link SisPersona}
	 * @return
	 */
	public static InfoResultado ensamblarPersona(SisPersona pSisPersona) {
		
		InitialContext ctx;
		InfoResultado oResultado = new InfoResultado();
		
        try{

			ctx = new InitialContext();
			PersonaBTMService personaBTMService = (PersonaBTMService)ctx.lookup("ejb/PersonaBTM");

        	oResultado=personaBTMService.obtenerPorId(pSisPersona.getPersonaId());
        	if (!oResultado.isOk()) {
        		oResultado.setFilasAfectadas(0);
        		oResultado.setExcepcion(false);
        		oResultado.setFuenteError("EnsamblarPersona");
        		oResultado.setMensaje(oResultado.getMensaje());
        		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
        		oResultado.setOk(false);
        	}

        	return oResultado;
        
		} catch (NamingException e) {

			oResultado.setFilasAfectadas(0);
    		oResultado.setExcepcion(false);
    		oResultado.setFuenteError("Ensamblar Persona");
    		oResultado.setMensaje(Mensajes.SERVICIO_NO_ENCONTRADO + " " + Mensajes.NOTIFICACION_ADMINISTRADOR);
    		oResultado.setGravedad(InfoResultado.SEVERITY_ERROR);
    		oResultado.setOk(false);
    		return oResultado;
    		
		} 
        	
	}

}
