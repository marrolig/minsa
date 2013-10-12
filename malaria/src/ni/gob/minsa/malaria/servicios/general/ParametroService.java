/**
 * ParametroService.java
 */
package ni.gob.minsa.malaria.servicios.general;

import ni.gob.minsa.ciportal.dto.InfoResultado;

/**
 * Esta clase define el interface para ParametroService.
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.  Este interface tambi�n proporciona las operaciones de
 * consulta para los datos de la tabla PARAMETROS.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 18/06/2012
 * @since jdk1.6.0_21
 */
public interface ParametroService {
	/**
	 * Obtiene el objeto {@link Parametro} que posee el c�digo indicado.  Si el resultado es
	 * nulo, implica que no existe dicho c�digo en la tabla, caso contrario se retorna
	 * el objeto InfoResultado.
	 * 
	 * @param pCodigo Cadena que representa el c�digo asignado al par�metro
	 * @return Objeto {@link InfoResultado} con el resultado de la solicitud.
	 */
	public InfoResultado Encontrar(String pCodigo);

    /**
     * Busca una instancia del objeto {@link Parametro} en el contexto de la persistencia
     * utilizando su identificador.  Si no encuentra el objeto Parametro, se retornar� el objeto
     * InfoResultado pero sin objeto encapsaludo.
     * 
     * @param pParametroId Identificador del objeto par�metro
     * @return InfoResultado Objeto con el resultado de la operaci�n
     */
	public InfoResultado Encontrar(long pParametroId);

}
