package ni.gob.minsa.malaria.reglas;

import java.math.BigDecimal;
import java.util.Calendar;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.general.ParametroDA;
import ni.gob.minsa.malaria.datos.vigilancia.PuestoNotificacionDA;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.modelo.estructura.UnidadAcceso;
import ni.gob.minsa.malaria.modelo.general.Parametro;
import ni.gob.minsa.malaria.modelo.vigilancia.ColVol;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoDiagnostico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoHematico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoPruebaRapida;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoNotificacion;
import ni.gob.minsa.malaria.servicios.general.ParametroService;
import ni.gob.minsa.malaria.servicios.vigilancia.PuestoNotificacionService;
import ni.gob.minsa.malaria.soporte.Utilidades;

public class VigilanciaValidacion {

	/**
	 * Valida el objeto {@link ColVol}. Las reglas aplicadas son:<br>
	 * (a) Si la fecha final es declarada, dicha fecha debe ser superior o igual a la
	 * fecha inicial.<br>
	 * (b) Si el punto de referencia no está definido (i.e. es <code>null</code>), el
	 * tipo de transporte, accesibilidad, distancia y tiempo, también no deben de estar
	 * declarados.<br>
	 * (c) Deben establecerse ambas coordenadas geográficas o ninguna.
	 * 
	 * @param pColVol
	 * @return
	 */
	public static InfoResultado validarColVol(ColVol pColVol) {

		InfoResultado oResultado=new InfoResultado();
		
		// se excluye la validación de existir una persona vinculada al colVol
		// ya que de no existir, implica que se trata de un registro nuevo
		
		oResultado=Validacion.coordenadas(pColVol.getLatitud(), pColVol.getLongitud());
		if (!oResultado.isOk()) return oResultado;
		
		if (pColVol.getFechaInicio()==null) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha de inicio es requerida");
			return oResultado;
		}
		
		if (pColVol.getFechaFin()!=null && pColVol.getFechaFin().before(pColVol.getFechaInicio())) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha final de actividad del colaborador voluntario debe ser superior o igual que la fecha de inicio");
			return oResultado;
		}
		
		if (pColVol.getColVolAcceso()!=null) {

			if (pColVol.getColVolAcceso().getComoLlegar()==null || pColVol.getColVolAcceso().getComoLlegar().trim().isEmpty()) {
				oResultado.setOk(false);
				oResultado.setMensaje("La descripción de como llegar es requerida si indica el punto de referencia, tipo de transporte, etc.");
				return oResultado;
			}
			
			String declarado="";
			if (pColVol.getColVolAcceso().getClaseAccesibilidad()!=null) declarado="1"; else declarado="0";
			if (pColVol.getColVolAcceso().getTipoTransporte()!=null) declarado=declarado+"1"; else declarado=declarado+"0";
			if (pColVol.getColVolAcceso().getTiempo()!=null && !pColVol.getColVolAcceso().getTiempo().equals(BigDecimal.ZERO)) declarado=declarado+"1"; else declarado=declarado+"0";
			if (pColVol.getColVolAcceso().getDistancia()!=null && !pColVol.getColVolAcceso().getDistancia().equals(BigDecimal.ZERO)) declarado=declarado+"1"; else declarado=declarado+"0";

			if (pColVol.getColVolAcceso().getPuntoReferencia()!=null && !pColVol.getColVolAcceso().getPuntoReferencia().isEmpty()) {
				if (!declarado.equals("1111")) {
					oResultado.setOk(false);
					oResultado.setMensaje("Si especifica un punto de referencia, el tipo de transporte, accesibilidad, distancia y tiempo, deben ser declarados");
					return oResultado;
				} 
			}

			if (pColVol.getColVolAcceso().getPuntoReferencia()==null || pColVol.getColVolAcceso().getPuntoReferencia().isEmpty()) {
				if (!declarado.equals("0000")) {
					oResultado.setOk(false);
					oResultado.setMensaje("Si no especifica un punto de referencia, el tipo de transporte, accesibilidad, distancia y tiempo, no deben ser declarados");
					return oResultado;
				} 
			}
		}
		
		oResultado.setOk(true);
		return oResultado;
	}

	/**
	 * Valida los datos del puesto de notificación que se encuentra vinculado
	 * a un colaborador voluntario.
	 * 
	 * @param pPuestoNotificacion {@link PuestoNotificacion}
	 * @param pColVol {@link ColVol}
	 * @return
	 */
	public static InfoResultado validarPuestoNotificacion(PuestoNotificacion pPuestoNotificacion, ColVol pColVol) {

		InfoResultado oResultado = new InfoResultado();
		
		String control="";

		control=(pPuestoNotificacion.getClave()!=null && !pPuestoNotificacion.getClave().trim().isEmpty())?"1":"0";
		control=control+((pPuestoNotificacion.getFechaApertura()!=null)?"1":"0");
		
		// si se cumple 01 o 10, implica que se ha declarado uno de los dos únicamente
		if (control.equals("01")) {
			oResultado.setOk(false);
			oResultado.setMensaje("Debe declarar una clave para el puesto de notificación si declara una fecha de apertura");
			return oResultado;
		}
		
		if (control.equals("10")) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha de apertura del puesto de notificación es requerida");
			return oResultado;
		}
		
		// en este punto, control es 00 o 11, si es 00, la fecha de cierre debe estar en nulo,
		// si es 11, la fecha de cierre debe ser superior o igual a la fecha de apertura.
		
		if (control.equals("00") && pPuestoNotificacion.getFechaCierre()!=null) {
			oResultado.setOk(false);
			oResultado.setMensaje("No debe declararse una fecha de cierre para el puesto de notificación");
			return oResultado;
		}
		
		if (control.equals("11") && pPuestoNotificacion.getFechaCierre()!=null && 
									pPuestoNotificacion.getFechaCierre().before(pPuestoNotificacion.getFechaApertura())) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha de cierre del puesto de notificación debe ser igual o superior a la fecha de apertura");
			return oResultado;
		}
		
		if (control.equals("11") && pPuestoNotificacion.getFechaApertura().before(pColVol.getFechaInicio())) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha de inicio del puesto de notificación debe ser igual o superior a la fecha de inicio del colaborador voluntario");
			return oResultado;
		}
		
		if (control.equals("11") && pColVol.getFechaFin()!=null &&
				pPuestoNotificacion.getFechaCierre()!=null &&
				pColVol.getFechaFin().before(pPuestoNotificacion.getFechaCierre())) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha final del colaborador voluntario debe ser igual o superior a la fecha de cierre del puesto de notificación");
			return oResultado;
		}

		if (control.equals("11") && pColVol.getFechaFin()!=null && pPuestoNotificacion.getFechaCierre()==null) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha de cierre del puesto de notificación es requerida si declara una fecha final de la actividad del colaborador voluntario");
			return oResultado;
		}
		
		// La clave declarada no debe corresponder a ningun otro
		// colaborador voluntario vinculado a un puesto de notificación con la misma
		// clave que aún se encuentra activa, i.e. sin fecha de cierre o fecha de cierre
		// posterior a la fecha actual.
		
		if (control.equals("11")) {
			PuestoNotificacionService puestoNotificacionService=new PuestoNotificacionDA();
			PuestoNotificacion oPuestoNotificacion=puestoNotificacionService.EncontrarPorClave(pPuestoNotificacion.getClave().trim());
			if (oPuestoNotificacion!=null && oPuestoNotificacion.getClave().equals(pPuestoNotificacion.getClave().trim()) && oPuestoNotificacion.getPuestoNotificacionId()!=pPuestoNotificacion.getPuestoNotificacionId()) {
				oResultado.setOk(false);
				oResultado.setMensaje("La clave del puesto de notificación aún se encuentra activa y no puede ser utilizada");
				if (oPuestoNotificacion.getColVol()!=null) {
					oResultado.setMensajeDetalle("La clave está asociada al colaborador voluntario " + oPuestoNotificacion.getColVol().getSisPersona().getNombreCompleto() + " que reside en la comunidad " + oPuestoNotificacion.getColVol().getSisPersona().getComunidadResidencia().getNombre() + " bajo la coordinación de la unidad " + oPuestoNotificacion.getColVol().getUnidad().getNombre());
				} else {
					oResultado.setMensajeDetalle("La clave está asociada a la unidad de salud " + oPuestoNotificacion.getUnidad().getNombre() + " ubicada en el municipio " + oPuestoNotificacion.getUnidad().getMunicipio().getNombre());
				}
				return oResultado;
			}
		}
		
		oResultado.setOk(true);
		
		return oResultado;
		
	}

	/**
	 * Valida los datos del puesto de notificación que se encuentra
	 * asociado a una unidad de salud.
	 * 
	 * @param pPuestoNotificacion {@link PuestoNotificacion}
	 * @param pUnidad {@link Unidad}
	 * @return {@link InfoResultado}
	 */
	public static InfoResultado validarPuestoNotificacion(PuestoNotificacion pPuestoNotificacion, Unidad pUnidad) {
		
		InfoResultado oResultado = new InfoResultado();
		
		if (pPuestoNotificacion.getClave()==null || pPuestoNotificacion.getClave().trim().isEmpty()) {
			oResultado.setOk(false);
			oResultado.setMensaje("Debe declarar una clave para el puesto de notificación");
			return oResultado;
		}
		
		if (pPuestoNotificacion.getFechaApertura()==null) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha de apertura del puesto de notificación es requerida");
			return oResultado;
		}
		
		if (pPuestoNotificacion.getFechaCierre()!=null && 
									pPuestoNotificacion.getFechaCierre().before(pPuestoNotificacion.getFechaApertura())) {
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha de cierre del puesto de notificación debe ser igual o superior a la fecha de apertura");
			return oResultado;
		}
		
		// La clave declarada no debe corresponder a ninguna otra unidad de salud
		// o colaborador voluntario que se vincule a un puesto de notificación 
		// con la misma clave que aún se encuentra activa, i.e. sin fecha de 
		// cierre o fecha de cierre posterior a la fecha actual.
		
		PuestoNotificacionService puestoNotificacionService=new PuestoNotificacionDA();
		PuestoNotificacion oPuestoNotificacion=puestoNotificacionService.EncontrarPorClave(pPuestoNotificacion.getClave().trim());
		if (oPuestoNotificacion!=null && oPuestoNotificacion.getClave().equals(pPuestoNotificacion.getClave().trim()) && oPuestoNotificacion.getPuestoNotificacionId()!=pPuestoNotificacion.getPuestoNotificacionId()) {
			oResultado.setOk(false);
			oResultado.setMensaje("La clave del puesto de notificación aún se encuentra activa y no puede ser utilizada");
			if (oPuestoNotificacion.getColVol()!=null) {
				oResultado.setMensajeDetalle("La clave está asociada al colaborador voluntario " + oPuestoNotificacion.getColVol().getSisPersona().getNombreCompleto() + " que reside en la comunidad " + oPuestoNotificacion.getColVol().getSisPersona().getComunidadResidencia().getNombre() + " bajo la coordinación de la unidad " + oPuestoNotificacion.getColVol().getUnidad().getNombre());
			} else {
				oResultado.setMensajeDetalle("La clave está asociada a la unidad de salud " + oPuestoNotificacion.getUnidad().getNombre() + " ubicada en el municipio " + oPuestoNotificacion.getUnidad().getMunicipio().getNombre());
			}
			return oResultado;
		}
		
		oResultado.setOk(true);
		
		return oResultado;
		
	}
	
	public static InfoResultado validarUnidadAcceso(UnidadAcceso pUnidadAcceso) {
		
		InfoResultado oResultado = new InfoResultado();
		
		if (pUnidadAcceso!=null) {

			String declarado="";
			if (pUnidadAcceso.getClaseAccesibilidad()!=null) declarado="1"; else declarado="0";
			if (pUnidadAcceso.getTipoTransporte()!=null) declarado=declarado+"1"; else declarado=declarado+"0";
			if (pUnidadAcceso.getTiempo()!=null && !pUnidadAcceso.getTiempo().equals(BigDecimal.ZERO)) declarado=declarado+"1"; else declarado=declarado+"0";
			if (pUnidadAcceso.getDistancia()!=null && !pUnidadAcceso.getDistancia().equals(BigDecimal.ZERO)) declarado=declarado+"1"; else declarado=declarado+"0";

			if (pUnidadAcceso.getPuntoReferencia()!=null && !pUnidadAcceso.getPuntoReferencia().isEmpty()) {
				if (!declarado.equals("1111")) {
					oResultado.setOk(false);
					oResultado.setMensaje("Si especifica un punto de referencia, el tipo de transporte, accesibilidad, distancia y tiempo, deben ser declarados");
					return oResultado;
				}
				
				if (pUnidadAcceso.getComoLlegar()==null || pUnidadAcceso.getComoLlegar().trim().isEmpty()) {
					oResultado.setOk(false);
					oResultado.setMensaje("La descripción de como llegar es requerida si indica el punto de referencia, tipo de transporte, etc.");
					return oResultado;
				}

			}
			
			if (pUnidadAcceso.getPuntoReferencia()==null || pUnidadAcceso.getPuntoReferencia().isEmpty()) {
				if (!declarado.equals("0000")) {
					oResultado.setOk(false);
					oResultado.setMensaje("Si no especifica un punto de referencia, el tipo de transporte, accesibilidad, distancia y tiempo, no deben ser declarados");
					return oResultado;
				} 
			}
		}
		
		return oResultado;
		
	}
	
	/**
	 * Validación del Muestreo Hematico y los datos vinculados a la misma, i.e.
	 * Diagnótico por Gota Gruesa, Prueba Rápida de Malaria y Datos de la Persona que
	 * persisten en el muestreo.
	 * 
	 * @param pMuestreoHematico Objeto {@link MuestreoHematico}
	 * @return {@link InfoResultado}
	 */
	public static InfoResultado validarMuestreoHematico(MuestreoHematico pMuestreoHematico) {
		
		InfoResultado oResultado = new InfoResultado();
		oResultado.setOk(false);
		
		Calendar cFechaActual = Calendar.getInstance();

		// El número de lámina debe de ser declarado y mayor que cero.
		if (pMuestreoHematico.getNumeroLamina()==null || (pMuestreoHematico.getNumeroLamina().compareTo(Utilidades.CERO)!=1)) {
			oResultado.setMensaje("El número de lámina es requerido y debe de ser superior a cero");
			return oResultado;
		}

		// La clave es requerida siempre y cuando se haya establecido un puesto de notificación
		if ((pMuestreoHematico.getClave()==null || pMuestreoHematico.getClave().isEmpty()) && pMuestreoHematico.getPuestoNotificacion()!=null) {
			oResultado.setMensaje("La clave asociada al puesto de notificación es requerida");
			return oResultado;
		}

		// El tipo de búsqueda debe ser Pasiva o Activa.
		if (pMuestreoHematico.getTipoBusqueda()==null || ((pMuestreoHematico.getTipoBusqueda().intValue()!=Utilidades.TIPO_BUSQUEDA_ACTIVA) && (pMuestreoHematico.getTipoBusqueda().intValue()!=Utilidades.TIPO_BUSQUEDA_PASIVA))) {
			oResultado.setMensaje("El tipo de búsqueda es requerido y debe de ser Activa o Pasiva");
			return oResultado;
		}
		
		// La fecha de toma de la muestra de la gota gruesa es requerida.
		if (pMuestreoHematico.getFechaToma()==null) {
			oResultado.setMensaje("La fecha de la toma de la muestra de la gota gruesa es requerida");
			return oResultado;
		}
		
		// La fecha de toma de la muestra de la gota gruesa no debe ser mayor que la fecha actual.
		if (pMuestreoHematico.getFechaToma().after(cFechaActual.getTime())) {
			oResultado.setMensaje("La fecha de la toma de la muestra de la gota gruesa no puede ser superior a la fecha actual");
			return oResultado;
		}

		// La semana y año epidemiológico son requeridos
		if (pMuestreoHematico.getSemanaEpidemiologica()==null) {
			oResultado.setMensaje("La semana epidemiológica es requerida.");
			return oResultado;
		}
		if (pMuestreoHematico.getAñoEpidemiologico()==null) {
			oResultado.setMensaje("El año epidemiológico es requerido.");
			return oResultado;
		}

		if (pMuestreoHematico.getSexo()==null) {
			oResultado.setMensaje("El sexo del paciente es requerido");
			return oResultado;
		}

		if (pMuestreoHematico.getSexo().getCodigo().equals(Utilidades.SEXO_MASCULINO) && pMuestreoHematico.getEmbarazada()!=null) {
			oResultado.setMensaje("No puede especificar la condición de embarazada porque el paciente es del sexo masculino");
			return oResultado;
		}
			
		if (pMuestreoHematico.getFechaNacimiento()==null) {
			oResultado.setMensaje("La fecha de nacimiento del paciente es requerida");
			return oResultado;
		}
		
		if (pMuestreoHematico.getMunicipioResidencia()==null) {
			oResultado.setMensaje("El municipio de residencia de la persona es requerido");
			return oResultado;
		}
		
		if (pMuestreoHematico.getComunidadResidencia()==null) {
			oResultado.setMensaje("La comunidad de residencia de la persona es requerida");
			return oResultado;
		}
		
		if (pMuestreoHematico.getDireccionResidencia()==null || pMuestreoHematico.getDireccionResidencia().isEmpty()) {
			oResultado.setMensaje("La dirección de residencia de la persona es requerida");
			return oResultado;
		}
		
		if (!pMuestreoHematico.getManejoClinico().equals(Utilidades.MANEJO_CLINICO_AMBULATORIO) && !pMuestreoHematico.getManejoClinico().equals(Utilidades.MANEJO_CLINICO_HOSPITALARIO)) {
			oResultado.setMensaje("El manejo clínico es requerido y debe ser Ambulatorio u Hospitalario");
			return oResultado;
		}
		
		if (pMuestreoHematico.getTelefonoReferente()!=null && !pMuestreoHematico.getTelefonoReferente().isEmpty() && pMuestreoHematico.getPersonaReferente()==null) {
			oResultado.setMensaje("Si declara el teléfono de la persona referente, debe indicar el nombre y apellidos de dicha persona");
			return oResultado;
		}

		if (pMuestreoHematico.getInicioTratamiento()==null &&
				(pMuestreoHematico.getFinTratamiento()!=null || 
				(pMuestreoHematico.getTratamientoEnBoca()!=null && !pMuestreoHematico.getTratamientoEnBoca().equals(BigDecimal.ZERO)) ||
				(pMuestreoHematico.getTratamientoRemanente()!=null && !pMuestreoHematico.getTratamientoRemanente().equals(BigDecimal.ZERO)) ||
				(pMuestreoHematico.getCloroquina()!=null && !pMuestreoHematico.getCloroquina().equals(BigDecimal.ZERO)) ||
				(pMuestreoHematico.getPrimaquina5mg()!=null && !pMuestreoHematico.getPrimaquina5mg().equals(BigDecimal.ZERO)) ||
				(pMuestreoHematico.getPrimaquina15mg()!=null && !pMuestreoHematico.getPrimaquina15mg().equals(BigDecimal.ZERO)))) {
			
			oResultado.setMensaje("Si indica algún dato sobre el tratamiento malárico, la fecha de inicio del tratamiento es requerida");
			return oResultado;
		}
		
		if (pMuestreoHematico.getInicioTratamiento()!=null && 
				pMuestreoHematico.getFinTratamiento()!=null && 
				pMuestreoHematico.getFinTratamiento().before(pMuestreoHematico.getInicioTratamiento())) {
			
			oResultado.setMensaje("La fecha de finalización del tratamiento debe ser mayor o igual que la fecha de inicio");
			return oResultado;
		}

		if (pMuestreoHematico.getSexo().equals(Utilidades.SEXO_FEMENINO)) {
			
			// valores por omisión para las edades mínimas y máximas del embarazo
			int edad_max_embarazo=70;
			int edad_min_embarazo=10;
		
			ParametroService parametroService = new ParametroDA();
			InfoResultado oResultadoParametro = parametroService.Encontrar("EDAD_MAX_EMBARAZO");
			if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
				Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
				if (Utilidades.esEntero(oParametro.getValor())) {
					edad_max_embarazo=Integer.parseInt(oParametro.getValor());
				}
			}
	
			oResultadoParametro = parametroService.Encontrar("EDAD_MIN_EMBARAZO");
			if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
				Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
				if (Utilidades.esEntero(oParametro.getValor())) {
					edad_min_embarazo=Integer.parseInt(oParametro.getValor());
				}
			}

			// edad de la persona
			int edad=Utilidades.calcularEdadEnAnios(pMuestreoHematico.getFechaNacimiento()).intValue();
		
			// verificar si existen los parámetros para la edad mínima y máxima del embarazo 
			if (pMuestreoHematico.getSexo().getCodigo().equals(Utilidades.SEXO_FEMENINO) && 
		    		(edad<=edad_min_embarazo || edad>=edad_max_embarazo)) {

				oResultado.setMensaje("La persona posee una edad fuera del límite establecido para el embarazo ("+ String.valueOf(edad_min_embarazo) +" a " + String.valueOf(edad_max_embarazo) +")");
				return oResultado;
			}
		}
		
		// validación de los datos de la prueba rápida de malaria
		oResultado=validarPRM(pMuestreoHematico.getPruebaRapida());
		if (!oResultado.isOk()) {
			return oResultado;
		}
		
		// validación de datos del diagnóstico
		
		oResultado=validarPGG(pMuestreoHematico.getDiagnostico());
		return oResultado;
	}
	
	public static InfoResultado validarPGG(MuestreoDiagnostico pMuestreoDiagnostico) {
		
		InfoResultado oResultado = new InfoResultado();
		oResultado.setOk(true);
		
		if (pMuestreoDiagnostico==null) {
			return oResultado;
		}
		
		// verifica que si existe una fecha de diagnóstico, debe existir un resultado
		// y también debe especificarse si es positivo o negativo para pVivax o pFalciparum
			
		if (pMuestreoDiagnostico.getFechaDiagnostico()!=null &&
			pMuestreoDiagnostico.getResultado()==null) {
				
			oResultado.setMensaje("Si se establece una fecha de diagnóstico debe indicar si el resultado es positivo o negativo");
			return oResultado;
		}
			
		if (pMuestreoDiagnostico.getFechaDiagnostico()==null &&
				pMuestreoDiagnostico.getResultado()!=null) {
				
			oResultado.setMensaje("Si se establece un resultado positivo o negativo, la fecha de diagnóstico es requerida");
			return oResultado;
		}
			
		if (pMuestreoDiagnostico.getResultado()==null &&
				(pMuestreoDiagnostico.getPositivoPVivax()!=null ||
				pMuestreoDiagnostico.getPositivoPFalciparum()!=null)) {
			
			oResultado.setMensaje("Debe indicar si el resultado es positivo o negativo para ambas especies");
			return oResultado;
				
		}
			
		//verificar que el resultado puede estar en nulo, por lo que podría existir un motivo de falta de diagnóstico
			
		if (pMuestreoDiagnostico.getResultado()!=null &&
				pMuestreoDiagnostico.getPositivoPVivax()==null &&
				pMuestreoDiagnostico.getPositivoPFalciparum()==null) {

			oResultado.setMensaje("Debe indicar si el resultado es positivo o negativo para ambas especies");
			return oResultado;
		}
			
		if (pMuestreoDiagnostico.getResultado()!=null) {

			if (pMuestreoDiagnostico.getMotivoFaltaDiagnostico()!=null) {
					
				oResultado.setMensaje("No puede declarar un motivo de falta de diagnóstico si ha declarado un resultado");
				return oResultado;
			}

			if (pMuestreoDiagnostico.getResultado().equals(Utilidades.NEGATIVO) &&
						(pMuestreoDiagnostico.getPositivoPVivax().equals(Utilidades.POSITIVO) || 
						pMuestreoDiagnostico.getPositivoPFalciparum().equals(Utilidades.POSITIVO))) {

				oResultado.setMensaje("Debe indicar que el resultado es negativo para ambas especies");
				return oResultado;
			}
				
			if (pMuestreoDiagnostico.getResultado().equals(Utilidades.POSITIVO) &&
						(pMuestreoDiagnostico.getPositivoPVivax().equals(Utilidades.NEGATIVO) && 
						pMuestreoDiagnostico.getPositivoPFalciparum().equals(Utilidades.NEGATIVO))) {

				oResultado.setMensaje("Debe indicar que el resultado es positivo para una de las especies");
				return oResultado;
			}
				
			if (pMuestreoDiagnostico.getPositivoPVivax().equals(Utilidades.POSITIVO) &&
						pMuestreoDiagnostico.getDensidadPVivax()==null) {

				oResultado.setMensaje("Debe establecer la densidad parásitaria en cruces para el P.vivax");
				return oResultado;
			}
				
			if (pMuestreoDiagnostico.getPositivoPFalciparum().equals(Utilidades.POSITIVO) &&
						pMuestreoDiagnostico.getDensidadPFalciparum()==null) {

				oResultado.setMensaje("Debe establecer la densidad parásitaria en cruces para el P.falciparum");
				return oResultado;
			}
				
			if (pMuestreoDiagnostico.getPositivoPFalciparum().equals(Utilidades.NEGATIVO) && 
						pMuestreoDiagnostico.getEstadioPFalciparum()!=null) {

				oResultado.setMensaje("Si el resultado es negativo para el P.falciparum, no puede declarar su estadío");
				return oResultado;
			}

			if (pMuestreoDiagnostico.getPositivoPFalciparum().equals(Utilidades.POSITIVO) && 
						pMuestreoDiagnostico.getEstadioPFalciparum()==null) {

				oResultado.setMensaje("Si el resultado es positivo para el P.falciparum, debe declarar su estadío");
				return oResultado;
			}
			
		}
			
		// si el resultado es nulo, no puede declararse la densidad medida por los estadíos asexuales, gametocitos y leucocitos,
		// caso contrario se deja libertad en declarar o no dichos valores.
			
		if (pMuestreoDiagnostico.getResultado()==null && 
					(pMuestreoDiagnostico.getEstadiosAsexuales()!=null ||
					 pMuestreoDiagnostico.getGametocitos()!=null ||
					 pMuestreoDiagnostico.getLeucocitos()!=null)) {
				
				oResultado.setMensaje("Si no ha especificado ningún resultado no puede declarar la densidad parasitaria");
				return oResultado;
		}
		
		return oResultado;
		
	}
	
	
	public static InfoResultado validarPRM(MuestreoPruebaRapida pMuestreoPruebaRapida) {
		
		InfoResultado oResultado = new InfoResultado();
		oResultado.setOk(true);
		
		if (pMuestreoPruebaRapida==null) {
			return oResultado;
		}
		
		String iControl="";
		iControl=pMuestreoPruebaRapida.getFecha()!=null?"1":"0";
		iControl=iControl+((pMuestreoPruebaRapida.getResultado()!=null && pMuestreoPruebaRapida.getResultado().getCatalogoId()!=0)?"1":"0");
		iControl=iControl+((pMuestreoPruebaRapida.getRealizado()!=null && pMuestreoPruebaRapida.getRealizado().getCatalogoId()!=0)?"1":"0");
		
		if (!iControl.equals("111") && !iControl.equals("000")) {
			oResultado.setOk(false);
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			oResultado.setMensaje("Debe completar todos los datos vinculados a la prueba rápida de malaria");
		}

		return oResultado;
		
	}
}
