// -----------------------------------------------
// GestorAuditoria.java
// -----------------------------------------------
package ni.gob.minsa.malaria.servicios.auditoria;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoSesion;
import ni.gob.minsa.malaria.modelo.auditoria.AuditSsn;
import ni.gob.minsa.malaria.modelo.auditoria.AuditTrn;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.eclipse.persistence.descriptors.DescriptorEvent;
import org.eclipse.persistence.descriptors.DescriptorEventAdapter;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.queries.InsertObjectQuery;
import org.eclipse.persistence.queries.WriteObjectQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;

/**
 * Implementación de los eventos post actualizar, insertar y eliminar para
 * ser utilizados en un escuchador (listener) de las entitades a las 
 * cuales se desea establecer una auditoría de cambios.<p>
 * En esta versión no se ha implementado el almacenamiento del valor
 * anterior en el detalle de las columnas modificadas.
 * <p>
 * @author Marlon Arróliga 
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 13/03/2012
 * @since jdk1.6.0_21
 */
public class GestorAuditoria extends DescriptorEventAdapter {

	@Override
	public void postDelete(DescriptorEvent event) {
		
		AuditSsn oAuditSsn = new AuditSsn();
		oAuditSsn.setOperacion(AuditSsn.DELETE_OPERATION);
		oAuditSsn.setFecha(new Date());
		oAuditSsn.setSentenciaSQL(event.getQuery().getTranslatedSQLString(event.getSession(), event.getQuery().getTranslationRow()));
		oAuditSsn.setTabla(event.getClassDescriptor().getTableName());
		
		InfoSesion oInfoSesion = Utilidades.obtenerInfoSesion();
		
		oAuditSsn.setSistema(oInfoSesion.getSistemaSesion());
		oAuditSsn.setUsuario(oInfoSesion.getUsername());
		
		InsertObjectQuery insertQuery = new InsertObjectQuery(oAuditSsn);
		event.getSession().executeQuery(insertQuery);
	}


	@Override
	public void postInsert(DescriptorEvent event) {
		processWriteEvent(event);
	}

	@Override
	public void postUpdate(DescriptorEvent event) {
		processWriteEvent(event);
	}

	/**
	 * Método común para gestionar los eventos Update (actualizar) e Insert (insertar).
	 * <p>
	 * En esta versión no se ha implementado el guardado del valor anterior en el detalle
	 * de la auditoría del registro modificado.
	 * @param event Descriptor del evento a ser procesado
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void processWriteEvent(DescriptorEvent event) {

		AuditSsn oAuditSsn = new AuditSsn();
		oAuditSsn.setOperacion(event.getEventCode() == 7 ? AuditSsn.UPDATE_OPERATION : 
														   AuditSsn.INSERT_OPERATION);
		oAuditSsn.setFecha(new Date());
		oAuditSsn.setSentenciaSQL(event.getQuery().getTranslatedSQLString(event.getSession(), event.getQuery().getTranslationRow()));
		oAuditSsn.setTabla(event.getClassDescriptor().getTableName());
		
		InfoSesion oInfoSesion = Utilidades.obtenerInfoSesion();
		
		oAuditSsn.setSistema(oInfoSesion.getSistemaSesion());
		oAuditSsn.setUsuario(oInfoSesion.getUsername());

		List<AuditTrn> columnas = new LinkedList();
		
		WriteObjectQuery queryWriteObject = (WriteObjectQuery) event.getQuery();
		DatabaseRecord registrosActualizados = (DatabaseRecord) queryWriteObject.getModifyRow();

		for (Object columna : registrosActualizados.keySet()) {
			DatabaseField dbcol = (DatabaseField) columna;

			AuditTrn oAuditTrn = new AuditTrn();
			Object iValorColumna=registrosActualizados.getValues(dbcol);
			if (iValorColumna!=null) {

				oAuditTrn.setAuditSsn(oAuditSsn);
				oAuditTrn.setColumna(dbcol.getName());
				oAuditTrn.setValorNuevo(iValorColumna.toString());
				columnas.add(oAuditTrn);
			}

		}
			
		oAuditSsn.setColumnas(new HashSet(columnas));

		InsertObjectQuery insertQuery = new InsertObjectQuery(oAuditSsn);
		event.getSession().executeQuery(insertQuery);

		for (AuditTrn oAuditTrn : columnas)
		{
			insertQuery = new InsertObjectQuery(oAuditTrn);
			event.getSession().executeQuery(insertQuery);
		}
	}

}
