// -----------------------------------------------
// ColVolService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.vigilancia;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ejbPersona.dto.Persona;
import ni.gob.minsa.malaria.modelo.vigilancia.ColVol;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoNotificacion;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en ColVolDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 22/06/2012
 * @since jdk1.6.0_21
 */
public interface ColVolService {

	/**
	 * Busca un objeto {@link ColVol} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pColVolId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pColVolId);
	
	/**
	 * Guarda un objeto {@link ColVol} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.  Adicionalmente
	 * guarda, crea o elimina el objeto {@link PuestoNotificacion} vinculado al Colaborador
	 * Voluntario así como las comunidades asociades a dicho puesto.  Si se crea el puesto
	 * de notificación, automáticamente se agregará la asociación entre dicho puesto y la
	 * comunidad de residencia del colaborador voluntario. 
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pColVol Objeto {@link ColVol} a ser almacenado en la base de datos
	 * @param pPersona Objeto {@link Persona} que será actualizado y que está asociado al ColVol
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(ColVol pColVol, Persona pPersona, PuestoNotificacion pPuestoNotificacion);
	/**
	 * Agrega un objeto {@link ColVol} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pColVol Objeto {@link ColVol} a ser agregado en la base de datos
	 * @param pPersona Objeto {@link Persona} que será agregado o actualizado y que será asociado o está asociado al ColVol
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(ColVol pColVol,Persona pPersona,PuestoNotificacion pPuestoNotificacion);
	/**
	 * Elimina el objeto {@link ColVol} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pColVolId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pColVolId);
	
	/**
	 * Retorna una lista de objetos {@link ColVol} según su situación, activo o pasivo
	 * coordinados por una unidad de salud.<br>
	 * Se considera activo a todo ColVol cuya fecha final no ha sido declarada o si la
	 * fecha final es mayor que la fecha actual.
	 * 
	 * @param pUnidadId      Identificador de la unidad de salud
	 * @param pNombre        Literal de búsqueda por el nombre del colVol
	 * @param pSoloActivos   <code>true</code> incluye únicamente los activos;
	 * 						 <code>false</code> retorna todos los colvoles;
	 * @return Lista de objetos {@link ColVol}
	 */
	public List<ColVol> ListarPorUnidad(long pUnidadId, String pNombre, 
			boolean pSoloActivos, int pPaginaActual, 
			int pTotalPorPagina, int pNumRegistros); 

	/**
	 * Retorna una lista de todos los objetos {@link ColVol} activos coordinados 
	 * por una unidad de salud, sin paginación.<br>
	 * 
	 * @param pUnidadId      Identificador de la unidad de salud
	 * @return Lista de objetos {@link ColVol}
	 */
	public List<ColVol> ListarPorUnidad(long pUnidadId); 

	public int ContarPorUnidad(long pUnidadId, String pNombre, boolean pSoloActivos);
	
	/**
	 * Busca un objeto {@link ColVol} en la base de datos mediante
	 * el identificador de la persona.  Retorna <code>null</code>
	 * si no encuentra ningún colvol asociado.
	 * 
	 * @param pPersonaId Identificador de la persona
	 * @return Objeto {@link ColVol} o <code>null</code> si no encuentra
	 */
	public ColVol EncontrarPorPersona(long pPersonaId);

}
