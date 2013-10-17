/**
 * 
 */
package ni.gob.minsa.malaria.datos.encuestas;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.encuesta.Criadero;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosEspecie;
import ni.gob.minsa.malaria.servicios.encuestas.CriaderosEspecieServices;

/**
 * @author dev
 *
 */
public class CriaderoEspecieDA implements CriaderosEspecieServices {

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.CriaderosEspecieServices#obtenerEspeciePorId(long)
	 */
	@Override
	public InfoResultado obtenerEspeciePorId(long pEspecieId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.CriaderosEspecieServices#obtenerEspeciesPorCriadero(int, int, java.lang.String, java.lang.Boolean, ni.gob.minsa.malaria.modelo.encuesta.Criadero)
	 */
	@Override
	public InfoResultado obtenerEspeciesPorCriadero(int pPaginaActual,
			int pRegistroPorPagina, String pFieldSort, Boolean pSortOrder,
			Criadero pCriadero) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.CriaderosEspecieServices#obtenerEspeciesPorCriadero(ni.gob.minsa.malaria.modelo.encuesta.Criadero)
	 */
	@Override
	public InfoResultado obtenerEspeciesPorCriadero(Criadero pCriadero) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.CriaderosEspecieServices#guardarEspecie(ni.gob.minsa.malaria.modelo.encuesta.CriaderosEspecie)
	 */
	@Override
	public InfoResultado guardarEspecie(CriaderosEspecie pEspecie) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.CriaderosEspecieServices#eliminarEspecie(long)
	 */
	@Override
	public InfoResultado eliminarEspecie(long pEspecieId) {
		// TODO Auto-generated method stub
		return null;
	}

}
