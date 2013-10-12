// -----------------------------------------------
// DivisionPoliticaService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;

/**
 * Esta clase define el interface para DivisionPoliticaService.
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.  Este interface tambi�n proporciona las operaciones de
 * mantenimiento para los datos de la tabla DIVISIONPOLITICA.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 07/03/2012
 * @since jdk1.6.0_21
 */
public interface DivisionPoliticaService {
	/**
	 * Retorna una lista de objetos {@link DivisionPolitica} que corresponden a
	 * los departamentos activos
	 * 
	 * @return Lista de objetos {@link DivisionPolitica}
	 */
	public List<DivisionPolitica> DepartamentosActivos();
	/**
	 * Retorna una lista de objetos {@link DivisionPolitica} que corresponden a
	 * los departamentos activos y adem�n el departamento utilizado el cual puede
	 * estar pasivo
	 * 
	 * @param pDivisionPoliticaId Identificador del departamento que se desea incluir en la lista
	 * @return Lista de objetos {@link DivisionPolitica}
	 */
	public List<DivisionPolitica> DepartamentosActivos(Long pDivisionPoliticaId);
	/**
	 * Retorna una lista de objetos {@link DivisionPolitica} que corresponden a
	 * los municipios activos que dependen de un departamento seleccionado seg�n
	 * su localizaci�n geogr�fica.<br>
	 * Si el identificador del departamento es nulo (<code>pDepartamentoId</code>) se retornar�n todos
	 * los municipios activos del pa�s.
	 * 
	 * @param pDepartamentoId Identificador de la divisi�n pol�tica que corresponde al departamento
	 * @return Lista de objetos {@link DivisionPolitica}
	 */
	public List<DivisionPolitica> MunicipiosActivos(Long pDepartamentoId);
	/**
	 * Retorna una lista de objetos {@link DivisionPolitica} que corresponden a
	 * los municipios activos y adem�s el municipio utilizado el cual puede
	 * estar pasivo o activo.  <br>
	 * Los municipios dependen de un departamento seleccionado
	 * seg�n su localizaci�n geogr�fica, sin embargo el municipio utilizado no 
	 * necesariamente debe depender del departamento seleccionado.<br>
	 * Si el identificador del departamento es nulo (<code>pDepartamentoId</code>) se retornar�n todos
	 * los municipios activos del pa�s incluyendo al municipio utilizado <code>pMunicipioId</code>.
	 * 
	 * 
	 * @param pDepartamentoId Identificador de la divisi�n pol�tica que corresponde al departamento
	 * @param pMunicipioId Identificador de la divisi�n pol�tica que corresponde al municipio utilizado
	 * @return Lista de objetos {@link DivisionPolitica}
	 */
	public List<DivisionPolitica> MunicipiosActivos(Long pDepartamentoId,Long pMunicipioId);

	/**
	 * Retorna la lista total de objetos {@link DivisionPolitica}, incluyendo
	 * activos y pasivos, que corresponde a departamentos del pa�s.
	 * 
	 * @return Lista de objetos {@link DivisionPolitica}
	 */
	public List<DivisionPolitica> DepartamentosTodos();
	/**
	 * Retorna la lista total de objetos {@link DivisionPolitica}, incluyendo
	 * activos y pasivos, que corresponde a municipios del pa�s que dependen geogr�ficamente
	 * de un departamento.  
	 * 
	 * @param pDepartamentoId Identificador del Departamento geogr�fico
	 * @return Lista de objetos {@link DivisionPolitica}
	 */
	public List<DivisionPolitica> MunicipiosTodos(Long pDepartamentoId);
	
	/**
	 * Busca un objeto {@link DivisionPolitica} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoReulstado}
	 * 
	 * @param pDivisionPoliticaId Entero largo con el identificador de la DivisionPolitica
	 * @return Objeto InfoResultado con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pDivisionPoliticaId);
	/**
	 * Busca un elemento de la divisi�n pol�tica, departamento o municipio
	 * mediante el c�digo institucional y el resultado es retornado 
	 * mediante un objeto {@link InfoResultado}
	 *  
	 * @param pCodigoNacional C�digo nacional asignado al elemento de la divisi�n pol�tica
	 * @return Objeto {@link InfoResultado}
	 */
	public InfoResultado EncontrarPorCodigoNacional(String pCodigoNacional);
	/**
	 * Busca un elemento de la divisi�n pol�tica, departamento o municipio
	 * mediante el c�digo ISO y el resultado es retornado 
	 * mediante un objeto {@link InfoResultado}
	 *  
	 * @param pCodigoIso C�digo ISO asignado al elemento de la divisi�n pol�tica
	 * @return Objeto {@link InfoResultado}
	 */
	public InfoResultado EncontrarPorCodigoISO(String pCodigoIso);

}
