package ni.gob.minsa.malaria.servicios.investigacion;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarInicio;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en SintomaLugarInicioDA.
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author F�lix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 18/10/2013
 * @since jdk1.6.0_21
 */
public interface SintomaLugarInicioService {
	/**
	 * Busca un objeto {@link SintomaLugarInicio} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pSintomaLugarInicioId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pSintomaLugarInicioId);
	
	/** 
	 * Busca un objeto {@link SintomaLugarInicio} asociado a la investigaci�n de sintomas
     * identificado por <b>pInvestigacionSintomaId</b> y retorna el resultado de la operaci�n 
     * en un objeto {@link InfoResultado}
     * 
     * @param pInvestigacionSintomaId Identificador de la investigaci�n de sintomas
     * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
     */
	public InfoResultado EncontrarPorInvestigacionSintoma(long pInvestigacionSintomaId);
	/**
	 * Guarda un objeto {@link SintomaLugarInicio} existente en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operaci�n UPDATE en la base de datos
	 * @param pSintomaLugarInicio Objeto {@link SintomaLugarInicio} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Guardar(SintomaLugarInicio pSintomaLugarInicio);
	/**
	 * Agrega un objeto {@link SintomaLugarInicio} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operaci�n INSERT en la base de datos
	 *  
	 * @param pSintomaLugarInicio Objeto {@link SintomaLugarInicio} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Agregar(SintomaLugarInicio pSintomaLugarInicio);
	/**
	 * Elimina el objeto {@link SintomaLugarInicio} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operaci�n DELETE en la base de datos
	 * 
	 * @param pSintomaLugarInicioId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Eliminar(long pSintomaLugarInicioId);
}
