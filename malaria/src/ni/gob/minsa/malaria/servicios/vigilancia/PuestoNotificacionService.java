// -----------------------------------------------
// PuestoNotificacionService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.vigilancia;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.estructura.UnidadAcceso;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoNotificacion;
import ni.gob.minsa.malaria.modelo.vigilancia.noEntidad.ColVolPuesto;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en PuestoNotificacionDA.
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 04/09/2012
 * @since jdk1.6.0_21
 */
public interface PuestoNotificacionService {

	/**
	 * Busca un objeto {@link PuestoNotificacion} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pPuestoNotificacionId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pPuestoNotificacionId);
	
	/**
	 * Retorna una lista de objetos {@link PuestoNotificacion} seg�n su situaci�n, activo o pasivo
	 * coordinados por una entidad administrativa que corresponden a unidades de salud.<br>
	 * Se considera activo a todo Puesto de Notificaci�n cuya fecha de cierre no ha sido declarada o si la
	 * fecha cierre es mayor que la fecha actual.
	 * 
	 * @param pEntidadAdtvaId      Identificador de la entidad administrativa
	 * @param pSoloActivos   <code>true</code> incluye �nicamente los activos;
	 * 						 <code>false</code> retorna todos los puestos de notificacion;
	 * @return Lista de objetos {@link PuestoNotificacion}
	 */
	public List<PuestoNotificacion> ListarUnidadesPorEntidad(long pEntidadAdtvaId, boolean pSoloActivos); 

	/**
	 * Retorna una lista de objetos {@link PuestoNotificacion} correspondiente a colaboradores 
	 * voluntarios, seg�n su situaci�n, activo o pasivo, coordinados por una unidad de salud.<br>
	 * Se considera activo a todo Puesto de Notificaci�n cuya fecha de cierre no ha sido declarada o si la
	 * fecha cierre es mayor que la fecha actual.
	 * 
	 * @param pUnidadId     Identificador de la unidad de salud
	 * @param pSoloActivos  <code>true</code> incluye �nicamente los activos;
	 * 						 <code>false</code> retorna todos los puestos de notificacion;
	 * @return Lista de objetos {@link PuestoNotificacion}
	 */
	public List<PuestoNotificacion> ListarPuestosPorUnidad(long pUnidadId, boolean pSoloActivos);

	public List<PuestoNotificacion> ListarPuestosPorUnidad(long pUnidadId, String pNombre, boolean pSoloActivos, int pPaginaActual, int pTotalPorPagina, int pNumRegistros);
	
	/**
	 * Busca un objeto {@link PuestoNotificacion} en la base de datos mediante
	 * el identificador del colaborador voluntario.  Retorna <code>null</code>
	 * si no encuentra ning�n puesto de notificaci�n.
	 * 
	 * @param pColVolId Identificador del colaborador voluntario
	 * @return Objeto {@link PuestoNotificacion} o <code>null</code> si no encuentra
	 */
	public PuestoNotificacion EncontrarPorColVol(long pColVolId);
	
	/**
	 * Busca un objeto {@link PuestoNotificacion} vinculada a una unidad
	 * de salud espec�fica
	 * 
	 * @param pUnidadId Identificador de la unidad de salud
	 * @param pActiva 0=Ambas, activas y pasivas, 1=Solo activas
	 * @return Objeto {@link PuestoNotificacion} o <code>null</code> si no lo encuentra
	 */
	public PuestoNotificacion EncontrarPorUnidad(long pUnidadId,int pActiva);

	/**
	 * Busca un objeto {@link PuestoNotificacion} vinculado a una clave de un
	 * puesto de notificaci�n activo, i.e. con una fecha de cierre posterior a la
	 * fecha actual
	 * 
	 * @param pClave Clave que se asigna al puesto de notificaci�n
	 * @return Objeto {@link PuestoNotificacion} o <code>null</code> si no lo encuentra
	 */
	public PuestoNotificacion EncontrarPorClave(String pClave);
	
	/**
	 * Agrega un puesto de notificaci�n, actualiza los datos modificables de la unidad
	 * y agrega o modifica los datos de acceso de la unidad, todo dentro de una misma
	 * transacci�n.
	 * 
	 * @param pPuestoNotificacion {@link PuestoNotificacion}
	 * @param pUnidad {@link Unidad}
	 * @param pUnidadAcceso {@link UnidadAcceso}
	 * @return
	 */
	public InfoResultado Agregar(PuestoNotificacion pPuestoNotificacion, Unidad pUnidad, UnidadAcceso pUnidadAcceso);

	public InfoResultado Eliminar(long pPuestoNofificacionId);
	
	public InfoResultado Guardar(PuestoNotificacion pPuestoNotificacion, Unidad pUnidad, UnidadAcceso pUnidadAcceso);
	
	/**
	 * Realiza el conteo de puestos de notificaci�n por entidad administrativa, ya sea aquellos puestos vinculados a unidades
	 * de salud, los vinculados a colaboradores voluntarios, o ambos, tanto activos como activos y pasivos.  El conteo
	 * lo realiza por literal de b�squeda o total.
	 * 
	 * @param pEntidadAdtvaId  Identificador de la entidad administrativa para la cual se quiere realizar el conteo
	 * @param pNombre          Literal de b�squeda del puesto de notificaci�n
	 * @param pTipoPuesto      Ambito del conteo, U=Solo unidades, C=Solo colaboradores voluntarios, <code>null</code>=ambos
	 * @param pSoloActivos     <code>true</code>, solo activos. <code>false</code>, ambos (activos y pasivos)  
	 * @return
	 */
	public int ContarPorEntidad(long pEntidadAdtvaId, String pNombre, String pTipoPuesto, boolean pSoloActivos);

	/**
	 * Retorna el n�mero de puestos de notificaci�n que corresponden a colaboradores voluntarios
	 * vinculados a una unidad de salud, tanto activos como activos y pasivos.  El conteo se ejecuta seg�n literal
	 * de b�squeda o total (literal en nulo).
	 * 
	 * @param pUnidadId
	 * @param pNombre
	 * @param pSoloActivos
	 * @return
	 */
	public int ContarPorUnidad(long pUnidadId, String pNombre, boolean pSoloActivos);
	
	/**
	 * Retorna una lista de objetos ColVolPuesto que corresponden a colaboradores voluntarios
	 * vinculados a una unidad de salud, tanto activos como activos y pasivos.  La
	 * lista puede ser parcial seg�n b�squeda por literal indicada en pNombre, o total
	 * si pNombre es <code>null</code>
	 * 
	 * @param pUnidadId
	 * @param pNombre
	 * @param pSoloActivos
	 * @return
	 */
	public List<ColVolPuesto> ListarColVolPorUnidad(long pUnidadId, String pNombre, 
			boolean pSoloActivos, int pPaginaActual, 
			int pTotalPorPagina, int pNumRegistros);
	
	public List<ColVolPuesto> ListarColVolPorUnidad(long pUnidadId, boolean pSoloActivos);
}
