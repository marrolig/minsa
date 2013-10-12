package ni.gob.minsa.malaria.soporte;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

import ni.gob.minsa.ciportal.dto.InfoResultado;

public class Mensajes {
	
	/**
	 * Mensaje de error cuando se produce un error no controlado y se produce una
	 * excepción
	 */
	public static final String ERROR_NO_CONTROLADO="Se ha producido un error no controlado: ";
	/**
	 * Mensaje que según la gravedad del error, se solicita la notificación al
	 * administrador del sistema
	 */
	public static final String NOTIFICACION_ADMINISTRADOR="Anote el detalle del mensaje y notifique al administrador del sistema. ";
	/**
	 * Mensaje que se presenta cuando se guarda un registro
	 * de forma exitosa.
	 */
	public static final String REGISTRO_GUARDADO="El registro ha sido almacenado con éxito. ";
	public static final String REGISTRO_ELIMINADO="El registro ha sido eliminado con éxito. ";
	public static final String EXCEPCION_REGISTRO_EXISTE="El registro no puede ser guardado. El registro ya existe. ";
	public static final String REGISTRO_NO_GUARDADO="Se ha producido un error al guardar el registro. ";
	public static final String REGISTRO_DUPLICADO="El registro ya existe y no puede estar duplicado.";
	public static final String ELIMINAR_REGISTRO_NO_EXISTE="El registro ya no existe y por tanto, no puede ser eliminado";
	public static final String ENCONTRAR_REGISTRO_NO_EXISTE="El registro ya no existe, ha sido eliminado por otro usuario";
	public static final String ELIMINAR_RESTRICCION="El registro no puede ser eliminado ya que otros registros dependen de él";
	public static final String REGISTRO_AGREGADO="Registro Agregado con éxito";
	
	public static final String RESTRICCION_BUSQUEDA="La búsqueda solo es permitida para un número superior a 3 caracteres";
	public static final String SERVICIO_NO_ENCONTRADO="El servicio requerido para efectuar esta operación no se encuentra activo";
	
	public Mensajes() {  // clase no instanciable
	}
	
	public static FacesMessage enviarMensaje(Severity pGravedad,String pMensajeGeneral,String pMensajeDetalle) {
		
		FacesMessage msg = null;
		msg = new FacesMessage(pGravedad,pMensajeGeneral,pMensajeDetalle);
		return msg;

	}
	
	public static FacesMessage enviarMensaje(InfoResultado pResultado) {

		if ((pResultado.getMensaje()==null) || (pResultado.getMensaje().isEmpty())) {
			return null;
		}
		
		FacesMessage msg = null;
		FacesMessage.Severity oGravedad=FacesMessage.SEVERITY_INFO;;
		
		switch(pResultado.getGravedad()) {
			case 2:
				oGravedad=FacesMessage.SEVERITY_WARN;
				break;
			case 3:
				oGravedad=FacesMessage.SEVERITY_ERROR;
				break;
			case 4:
				oGravedad=FacesMessage.SEVERITY_FATAL;
		}
		
		String oDetalle="";
		if ((pResultado.getMensajeDetalle()!=null) && (!pResultado.getMensajeDetalle().isEmpty())) {
			oDetalle=pResultado.getFuenteError();
		}
		
		if (pResultado.isExcepcion()) {
			if ((pResultado.getFuenteError()!=null) && (!pResultado.getFuenteError().isEmpty())) {
				oDetalle=oDetalle.isEmpty() ? pResultado.getFuenteError(): oDetalle+": "+pResultado.getFuenteError();
			}
		}
		
		msg = new FacesMessage(oGravedad,pResultado.getMensaje(),oDetalle);
		
		return msg;
	}
}
