package ni.gob.minsa.malaria.servicios.supervision;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMalaria;
import ni.gob.minsa.malaria.modelo.supervision.VisitaPuesto;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en VisitaPuestoDA.
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author F�lix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 09/11/2013
 * @since jdk1.6.0_21
 */
public interface VisitaPuestoService {
	
	/**
	 * Busca un objeto {@link InvestigacionMalaria} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pVisitaPuestoId Entero largo con el identificador del objeto {@link VisitaPuesto}
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pVisitaPuestoId);
	
	/**
	 * Obtiene todas los objetos {@link VisitaPuestoService}  de acuerdo al filtro seleccionado
     * 
     * * <li>Si <code>pMunicipioId</code> es <code>null</code>, se retornar�n aquellas visitas de las unidades asociadas a la Entidad Administrativa
     * seleccionada, en el a�o epidemiol�gico indicado.
     * 
	 * @param pEntidadAdtvaId Identificador de la Entidad administrativa con registro de visita a alguno de sus Puestos de Notificaci�n
	 * @param pA�oEpi A�o epidemiol�gico en el que se efectu� una visita.
	 * @param pMunicipioId Identificador del Municipio en el que se encuentran las Unidades de notificaci�n visitadas. Este �ltimo es
	 * opcional.
	 * @return Una lista de objetos {@link VisitaPuestoService}
	 */
	public List<VisitaPuesto>  ListarPorEntidadA�oEpiYMunicipio(long pEntidadAdtvaId, int pA�oEpi, Long pMunicipioId,
			int pPaginaActual, int pTotalPorPagina, int pNumRegistros);
	
	/**
	 * Retorna un entero con el n�mero de Vistas de acuerdo al filtro seleccionado
	 * 
	 * @param pEntidadAdtvaId Identificador de la Entidad administrativa con registro de visita a alguno de sus Puestos de Notificaci�n
	 * @param pA�oEpi A�o epidemiol�gico en el que se efectu� una visita.
	 * @param pMunicipioId Identificador del Municipio en el que se encuentran las Unidades de notificaci�n visitadas. Este �ltimo es
	 * opcional.
	 * @return Entero con el n�mero de objetos {@link VisitaPuestoService}.
	 */
	public int ContarPositivosPorUnidad(
			long pEntidadAdtvaId,
			int pA�oEpi,
			Long pMunicipioId);
	
	
	/**
	 * 
	 *  Guarda un objeto {@link VisitaPuesto} existente en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}. 
	 * <p>
	 * Realiza una operaci�n UPDATE en la base de datos
	 * 
	 * @param pVisitaPuesto objeto {@link VisitaPuesto} a ser almacenado en la base de datos.
	 * @return  Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Guardar(VisitaPuesto pVisitaPuesto);
	
	/**
	 * Agrega un objeto {@link VisitaPuesto} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operaci�n INSERT en la base de datos
	 *  
	 * @param pVisitaPuesto Objeto {@link VisitaPuesto} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Agregar(VisitaPuesto pVisitaPuesto);
	
	/**
	 * Elimina el objeto {@link VisitaPuestoService} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operaci�n DELETE en la base de datos
	 * 
	 * @param pVisitaPuestoId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Eliminar(long pVisitaPuestoId);

}
