package ni.gob.minsa.malaria.reglas;

import java.util.Date;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.supervision.VisitaPuesto;

public class VisitaPuestoValidacion {
	
	public static InfoResultado validarVisitaPuesto(VisitaPuesto pVisitaPuesto){
	
		InfoResultado oResultado = new InfoResultado();
		oResultado.setOk(true);
		if (pVisitaPuesto==null) {
			return oResultado;
		}
		
		if(pVisitaPuesto.getClave()==null || pVisitaPuesto.getClave().isEmpty()){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe definir el número de clave bajo la cual funciona el puesto de notificación");
			return oResultado;
		}
		
		if(pVisitaPuesto.getPuestoNotificacion()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe definir el puesto de notificación al cual se realiza la supervisión");
			return oResultado;
		}
		
		if(pVisitaPuesto.getFechaEntrada()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar la fecha en la cual se inicia la inspección");
			return oResultado;
		}
		
		if(pVisitaPuesto.getFechaEntrada().after(new Date())){
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha en la cual se inicia la inspección no puede ser posterior a la fecha actual");
			return oResultado;
		}
		
		if(pVisitaPuesto.getFechaSalida()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar la fecha en la cual finaliza la inspección");
			return oResultado;
		}
		
		if(pVisitaPuesto.getFechaSalida().after(new Date())){
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha en la cual se finaliza la inspección no puede ser posterior a la fecha actual");
			return oResultado;
		}
		
		if(pVisitaPuesto.getFechaEntrada().after(pVisitaPuesto.getFechaSalida())){
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha en la cual se inicia la inspección no puede ser posterior a la fecha en que finaliza la misma");
			return oResultado;
		}
		
		if(pVisitaPuesto.getPuestoNotificacion().getFechaApertura()!=null){
			if (pVisitaPuesto.getFechaEntrada().before(pVisitaPuesto.getPuestoNotificacion().getFechaApertura())) {
				oResultado.setOk(false);
				oResultado.setMensaje("La fecha en la cual se inicia la inspección no puede ser anterior a la fecha de apertura");
				return oResultado;
			}
		}
		
		if(pVisitaPuesto.getPuestoNotificacion().getFechaCierre()!=null){
			if(pVisitaPuesto.getFechaEntrada().after(pVisitaPuesto.getPuestoNotificacion().getFechaCierre())){
				oResultado.setOk(false);
				oResultado.setMensaje("La fecha en la cual se inicia la inspección no puede posterior la fecha de cierre del puesto.");
				return oResultado;
			}
			
			if(pVisitaPuesto.getFechaSalida().after(pVisitaPuesto.getPuestoNotificacion().getFechaCierre())){
				oResultado.setOk(false);
				oResultado.setMensaje("La fecha en la cual finaliza la inspección no puede posterior la fecha de cierre del puesto.");
				return oResultado;
			}
		}
		
		if(pVisitaPuesto.getHorarioInicio()==null || pVisitaPuesto.getHorarioFin()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar el hora en que inicia y finaliza regularmente la atención");
			return oResultado;
		}
		
		if(pVisitaPuesto.getHorarioInicio().after(pVisitaPuesto.getHorarioInicio())){
			oResultado.setOk(false);
			oResultado.setMensaje("La hora en que inicia la atención no puede ser posterior a la fecha en que finaliza");
			return oResultado;
		}
		
		if(pVisitaPuesto.getVisibleCarnet()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si el puesto tiene visible su carnet de identidad");
			return oResultado;
		}
		
		if(pVisitaPuesto.getAtencionPacientes()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si los pacientes son prontamente atendidos");
			return oResultado;
		}
		
		if(pVisitaPuesto.getTomaMuestras()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si las muestras en el puesto fueron tomadas satisfactoriamente");
			return oResultado;
		}
		
		if(pVisitaPuesto.getStock()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si el puesto tiene material suficiente o stock mínimo");
			return oResultado;
		}
		
		if(pVisitaPuesto.getReconocido()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si la poblaciónaledaña conoce de la existencia del puesto:");
			return oResultado;
		}
		
		if(pVisitaPuesto.getDivulgacion()==null){
			oResultado.setOk(false);
			oResultado.setMensaje("Debe indicar si el puesto tiene divulgación");
			return oResultado;
		}
	
		
		if(pVisitaPuesto.getProximaVisita()!=null){
			if(pVisitaPuesto.getPuestoNotificacion().getFechaApertura()!=null){
				if (pVisitaPuesto.getProximaVisita().before(pVisitaPuesto.getPuestoNotificacion().getFechaApertura())) {
					oResultado.setOk(false);
					oResultado.setMensaje("La fecha estimada de próxima visita es posterior a la fecha de cierre del puesto. Por favor indicar una fecha igual o anterior.");
					return oResultado;
				}
			}
			
			if(pVisitaPuesto.getPuestoNotificacion().getFechaCierre()!=null){
				if(pVisitaPuesto.getProximaVisita().after(pVisitaPuesto.getPuestoNotificacion().getFechaCierre())){
					oResultado.setOk(false);
					oResultado.setMensaje("La fecha estimada de próxima visita es posterior a la fecha de cierre del puesto. Por favor indicar una fecha igual o anterior.");
					return oResultado;
				}
			}

			if(pVisitaPuesto.getProximaVisita().equals(pVisitaPuesto.getFechaEntrada())){
				oResultado.setOk(false);
				oResultado.setMensaje("La fecha estimada de próxima visita no puede ser igual a la fecha de cierre. Por favor indicar una fecha posterior");
				return oResultado;
			}
		}
		
	
		if(pVisitaPuesto.getProximaVisita().before(pVisitaPuesto.getFechaEntrada())){
			oResultado.setOk(false);
			oResultado.setMensaje("La fecha estimada de próxima visita no puede ser anterior a la fecha de entrada");
			return oResultado;
		}
		

		oResultado.setOk(true);
		return oResultado;
	}
}
