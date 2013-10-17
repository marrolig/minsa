/**
 * 
 */
package ni.gob.minsa.malaria.datos.encuestas;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosIntervencion;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosPosInspeccion;
import ni.gob.minsa.malaria.servicios.encuestas.PosInspeccionServices;

/**
 * @author dev
 *
 */
public class PosInspeccionDA implements PosInspeccionServices {

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.PosInspeccionServices#obtenerPosInspeccionPorId(long)
	 */
	@Override
	public InfoResultado obtenerPosInspeccionPorId(long pPosInspeccionId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.PosInspeccionServices#obtenerPosInspeccionesPorPorIntervencion(int, int, java.lang.String, java.lang.Boolean, ni.gob.minsa.malaria.modelo.encuesta.CriaderosIntervencion)
	 */
	@Override
	public InfoResultado obtenerPosInspeccionesPorPorIntervencion(
			int pPaginaActual, int pRegistroPorPagina, String pFieldSort,
			Boolean pSortOrder, CriaderosIntervencion pIntervencion) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.PosInspeccionServices#guardarPosInspeccion(ni.gob.minsa.malaria.modelo.encuesta.CriaderosPosInspeccion)
	 */
	@Override
	public InfoResultado guardarPosInspeccion(
			CriaderosPosInspeccion pPosInspeccion) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.PosInspeccionServices#eliminarPosInspeccion(long)
	 */
	@Override
	public InfoResultado eliminarPosInspeccion(long pPosInspeccionId) {
		// TODO Auto-generated method stub
		return null;
	}

}
