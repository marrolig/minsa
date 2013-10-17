/**
 * 
 */
package ni.gob.minsa.malaria.datos.encuestas;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.encuesta.Criadero;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosPesquisa;
import ni.gob.minsa.malaria.servicios.encuestas.PesquisaServices;

/**
 * @author dev
 *
 */
public class PesquisaDA implements PesquisaServices {

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.PesquisaServices#obtenerPesquisaPorId(long)
	 */
	@Override
	public InfoResultado obtenerPesquisaPorId(long pPesquisaId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.PesquisaServices#obtenerPesquisasPorCriadero(int, int, java.lang.String, java.lang.Boolean, ni.gob.minsa.malaria.modelo.encuesta.Criadero)
	 */
	@Override
	public InfoResultado obtenerPesquisasPorCriadero(int pPaginaActual,
			int pRegistroPorPagina, String pFieldSort, Boolean pSortOrder,
			Criadero pCriadero) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.PesquisaServices#guardarPesquisa(ni.gob.minsa.malaria.modelo.encuesta.CriaderosPesquisa)
	 */
	@Override
	public InfoResultado guardarPesquisa(CriaderosPesquisa pPesquisa) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.PesquisaServices#eliminarPesquisa(long)
	 */
	@Override
	public InfoResultado eliminarPesquisa(long pPesquisaId) {
		// TODO Auto-generated method stub
		return null;
	}

}
