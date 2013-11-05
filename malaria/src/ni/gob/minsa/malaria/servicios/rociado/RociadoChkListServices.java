package ni.gob.minsa.malaria.servicios.rociado;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.rociado.ChecklistMalaria;
import ni.gob.minsa.malaria.modelo.rociado.RociadosMalaria;

public interface RociadoChkListServices {

	/**
	 * Metodo que permite obtener un Check List de Rociado Malaria por
	 * su identificador unico de tipo {@link Long}, regresando como
	 * resultado un objeto de tipo {@link InfoResultado} conteniendo
	 * el resultado de la operacion
	 * 
	 * @param pChkListId 		Entero largo identificador unico del Check List
	 * @return InfoResultado 	Objeto tipo {@link InfoResultado}
	 */
	public InfoResultado obtenerChkListPorId(long pChkListId);
	
	/**
	 * Metodo que permite obtener una coleccion CheckList de rociado malaria
	 * a partir de un objeto Rociado de tipo {@link RociadosMalaria}, regresando
	 * como resultado un objeto de tipo {@link InfoResultado} conteniendo
	 * el resultado de la operacion
	 * 
	 * @param pRociado			Objeto de tipo {@link RociadosMalaria}
	 * @return InfoResultado 	Objeto tipo {@link InfoResultado}
	 */
	public InfoResultado obtenerChkListPorRociado(RociadosMalaria pRociado);
	
	/**
	 * Metodo que permite acceder a la capa de persistencia y guardar un objeto de tipo
	 * {@link ChecklistMalaria} a la capa del modelo de datos del sistema
	 * 
	 * @param pChkList			Objeto de tipo {@link ChecklistMalaria}
	 * @return InfoResultado 	Objeto tipo {@link InfoResultado}
	 */
	public InfoResultado guardarChkList(ChecklistMalaria pChkList);
	
	/**
	 * Metodo que elimina un objeto de tipo {@link ChecklistMalaria} a partir
	 * de su identificador unico, accediendo a la capa de persistencia y eliminando
	 * el objeto del modelo de datos del sistema
	 * 
	 * @param pChkListId 		Entero largo identificador unico del Check List
	 * @return InfoResultado 	Objeto tipo {@link InfoResultado}
	 */
	public InfoResultado eliminarChkList(long pChkListId);
	
}
