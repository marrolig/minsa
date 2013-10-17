/**
 * 
 */
package ni.gob.minsa.malaria.datos.encuestas;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosIntervencion;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosPesquisa;
import ni.gob.minsa.malaria.servicios.encuestas.IntervencionServices;

/**
 * @author dev
 *
 */
public class IntervencionDA implements IntervencionServices {

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.IntervencionServices#obtenerIntervencionPorId(long)
	 */
	@Override
	public InfoResultado obtenerIntervencionPorId(long pIntervencionId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.IntervencionServices#obtenerIntervencionesPorPesquisa(int, int, java.lang.String, java.lang.Boolean, ni.gob.minsa.malaria.modelo.encuesta.CriaderosPesquisa)
	 */
	@Override
	public InfoResultado obtenerIntervencionesPorPesquisa(int pPaginaActual,
			int pRegistroPorPagina, String pFieldSort, Boolean pSortOrder,
			CriaderosPesquisa pPesquisa) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.IntervencionServices#guardarIntervencion(ni.gob.minsa.malaria.modelo.encuesta.CriaderosIntervencion)
	 */
	@Override
	public InfoResultado guardarIntervencion(CriaderosIntervencion pIntervencion) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ni.gob.minsa.malaria.servicios.encuestas.IntervencionServices#eliminarIntervencion(long)
	 */
	@Override
	public InfoResultado eliminarIntervencion(long pIntervencionId) {
		// TODO Auto-generated method stub
		return null;
	}

}
