// -----------------------------------------------
// ManzanaService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.Manzana;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en ManzanaDA.
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 22/05/2012
 * @since jdk1.6.0_21
 */
public interface ManzanaService {

	/**
	 * Retorna la lista de objetos {@link Manzana} que pertenecen
	 * a una comunidad   
	 *
	 * @param pComunidad    C�digo de la comunidad
	 * @param pSoloActivas  Valor booleano que indica si �nicamente se incluir�n
	 * 					    las manzanas activas (<code>true</code>) o todas las
	 * 						manzanas, sin importar su condici�n (<code>false</code>)
	 * @param pManzana		Incluye la manzana identificada por su c�digo, en la lista
	 * 					    de manzanas activas a retornar.  Si <code>null</code> incluir� solo
	 * 						las manzanas activas.
	 * @return Lista de objetos {@link Manzana}
	 */
	public List<Manzana> ManzanasPorComunidad(String pComunidad,boolean pSoloActivas,String pManzana);

	/**
	 * Lista paginada de las manzanas activas asociadas a una comunidad.
	 * 
	 * @param pComunidad        C�digo de la comunidad
	 * @param pPaginaActual 
	 * @param pTotalPorPagina
	 * @param pNumRegistros
	 * @return
	 */
	public List<Manzana> ListarPorComunidad(String pComunidad, int pPaginaActual, int pTotalPorPagina, int pNumRegistros);
	
	public int ContarPorComunidad(String pComunidad);
	
	/**
	 * Busca un objeto {@link Manzana} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pManzanaId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pManzanaId);
	
	/**
	 * Guarda un objeto {@link Manzana} existente en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operaci�n UPDATE en la base de datos
	 * @param pManzana Objeto {@link Manzana} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Guardar(Manzana pManzana);
	/**
	 * Agrega un objeto {@link Manzana} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operaci�n INSERT en la base de datos
	 *  
	 * @param pManzana Objeto {@link Manzana} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Agregar(Manzana pManzana);
	/**
	 * Elimina el objeto {@link Manzana} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operaci�n DELETE en la base de datos
	 * 
	 * @param pManzanaId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Eliminar(long pManzanaId);
	

}
