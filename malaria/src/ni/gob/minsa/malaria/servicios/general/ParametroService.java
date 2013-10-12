/**
 * ParametroService.java
 */
package ni.gob.minsa.malaria.servicios.general;

import ni.gob.minsa.ciportal.dto.InfoResultado;

/**
 * Esta clase define el interface para ParametroService.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.  Este interface también proporciona las operaciones de
 * consulta para los datos de la tabla PARAMETROS.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 18/06/2012
 * @since jdk1.6.0_21
 */
public interface ParametroService {
	/**
	 * Obtiene el objeto {@link Parametro} que posee el código indicado.  Si el resultado es
	 * nulo, implica que no existe dicho código en la tabla, caso contrario se retorna
	 * el objeto InfoResultado.
	 * 
	 * @param pCodigo Cadena que representa el código asignado al parámetro
	 * @return Objeto {@link InfoResultado} con el resultado de la solicitud.
	 */
	public InfoResultado Encontrar(String pCodigo);

    /**
     * Busca una instancia del objeto {@link Parametro} en el contexto de la persistencia
     * utilizando su identificador.  Si no encuentra el objeto Parametro, se retornará el objeto
     * InfoResultado pero sin objeto encapsaludo.
     * 
     * @param pParametroId Identificador del objeto parámetro
     * @return InfoResultado Objeto con el resultado de la operación
     */
	public InfoResultado Encontrar(long pParametroId);

}
