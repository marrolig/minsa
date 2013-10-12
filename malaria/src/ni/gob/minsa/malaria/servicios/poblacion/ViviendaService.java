// -----------------------------------------------
// ViviendaService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.Vivienda;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.ViviendaUltimaEncuesta;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en ViviendaDA.
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 25/05/2012
 * @since jdk1.6.0_21
 */
public interface ViviendaService {

	/**
	 * Retorna la lista de objetos {@link ViviendaUltimaEncuesta} que pertenecen
	 * a una comunidad   
	 *
	 * @param pComunidad    C�digo de la comunidad
	 * @param pPasivo       Valor booleano que indica si �nicamente se incluir�n
	 * 					    las viviendas pasivas (<code>true</code>) o solo las
	 * 					    activas (<code>false</code>).  Si es (<code>nulo</code>) 
	 * 						se retornar�n todas las viviendas sin importar su 
	 * 						situaci�n
	 * @return Lista de objetos {@link ViviendaUltimaEncuesta}
	 */
	public List<ViviendaUltimaEncuesta> ViviendasPorComunidad(String pComunidad,Boolean pPasivo);

	/**
	 * Busca un objeto {@link Vivienda} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pViviendaId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pViviendaId);
	/**
	 * Busca un objeto {@link Vivienda} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pVivienda Cadena de caracteres con el c�digo de la vivienda
	 * @return Objeto InfoResultado con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(String pVivienda);
	
	/**
	 * Guarda un objeto {@link Vivienda} existente en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operaci�n UPDATE en la base de datos
	 * @param pVivienda Objeto {@link Vivienda} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Guardar(Vivienda pVivienda);
	/**
	 * Agrega un objeto {@link Vivienda} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operaci�n INSERT en la base de datos
	 *  
	 * @param pVivienda Objeto {@link Vivienda} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Agregar(Vivienda pVivienda);
	/**
	 * Elimina el objeto {@link Vivienda} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operaci�n DELETE en la base de datos
	 * 
	 * @param pViviendaId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Eliminar(long pViviendaId);

	/**
	 * Realiza el conteo de las viviendas existentes en una comunidad.  
	 * Si pNombre es nulo, no se realizar� la b�squeda de viviendas
	 * por el nombre del jefe de familia
	 * 
	 * @param pComunidad   C�digo de la comunidad
	 * @param pNombre      Literal para la b�squeda de viviendas
	 * @param pPasivo      <code>true</code> solo pasivas,
	 * 					   <code>false</code> solo activas,
	 * 					   <code>null</code> todas
	 * @return
	 */
	public int ContarViviendasPorComunidad(String pComunidad, String pNombre, Boolean pPasivo);

	/**
	 * Retorna la lista de objetos {@link ViviendaUltimaEncuesta}, asociados a una
	 * comunidad.  La literal establecida en <code>pNombre</code> es opcional.
	 * 
	 * @param pComunidad 	  C�digo de la Comunidad
	 * @param pNombre         Literal del nombre del jefe de familia
	 * @param pPasivo         <>true</>, solo pasivas; <>false</>, solo activas; <>null</>, todas
	 * @param pPaginaActual   N�mero de p�gina actual
	 * @param pTotalPorPagina N�mero de registros por p�gina
	 * @param pNumRegistros   Total de registros
	 * @return Lista de objetos ViviendaUltimaEncuesta
	 */
	public List<ViviendaUltimaEncuesta> ViviendasPorComunidad(String pComunidad, String pNombre, Boolean pPasivo, int pPaginaActual, int pTotalPorPagina, int pNumRegistros);

}
