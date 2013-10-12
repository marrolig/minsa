// -----------------------------------------------
// ParametroEpidemiologicoService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.vigilancia;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.vigilancia.ParametroEpidemiologico;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en ParametroEpidemiologicoDA.
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 24/05/2012
 * @since jdk1.6.0_21
 */
public interface ParametroEpidemiologicoService {

	/**
	 * Busca un objeto {@link ParametroEpidemiologico} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pParametroEpidemiologicoId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pParametroEpidemiologicoId);
	
	/**
	 * Guarda un objeto {@link ParametroEpidemiologico} existente en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operaci�n UPDATE en la base de datos
	 * @param pParametroEpidemiologico Objeto {@link ParametroEpidemiologico} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Guardar(ParametroEpidemiologico pParametroEpidemiologico);
	/**
	 * Agrega un objeto {@link ParametroEpidemiologico} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operaci�n INSERT en la base de datos
	 *  
	 * @param pParametroEpidemiologico Objeto {@link ParametroEpidemiologico} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Agregar(ParametroEpidemiologico pParametroEpidemiologico);
	/**
	 * Elimina el objeto {@link ParametroEpidemiologico} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operaci�n DELETE en la base de datos
	 * 
	 * @param pParametroEpidemiologicoId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Eliminar(long pParametroEpidemiologicoId);
	
	/**
	 * Retorna una lista de objetos {@link ParametroEpidemiologico}.
	 * 
	 * @param pPasivo        <code>true</code> incluye �nicamente los pasivos;
	 * 						 <code>false</code> incluye �nicamente los activos;
	 * 						 <code>null</code> lista todos
	 * @return Lista de objetos {@link ParametroEpidemiologico}
	 */
	public List<ParametroEpidemiologico> Listar(Boolean pPasivo); 

	/**
	 * Retorna una lista de objetos {@link ParametroEpidemiologico} activos, donde se
	 * incluye el Parametro Epidemiologico con el c�digo <code>pParametroEpidemiologico</code>,
	 * el cual puede o no estar activo.
	 * 
	 * @param pParametroEpidemiologico  C�digo del Parametro Epidemiologico que ser� incluido en la lista
	 * @return Lista de objetos {@link ParametroEpidemiologico}
	 */
	public List<ParametroEpidemiologico> Listar(String pParametroEpidemiologico);

	/**
	 * Busca un objeto {@link ParametroEpidemiologico} en la base de datos mediante
	 * el c�digo asignado a dicho Par�metro Epidemiol�gico.  Retorna <code>null</code>
	 * si no encuentra ning�n par�metro epidemiol�gico
	 * 
	 * @param pCodigo C�digo asignado al par�metro epidemiol�gico
	 * @return Objeto {@link ParametroEpidemiologico} o <code>null</code> si no encuentra
	 */
	public ParametroEpidemiologico EncontrarPorCodigo(String pCodigo);

}
