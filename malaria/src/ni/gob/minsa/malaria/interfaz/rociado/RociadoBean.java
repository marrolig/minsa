package ni.gob.minsa.malaria.interfaz.rociado;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.encuestas.CriaderoDA;
import ni.gob.minsa.malaria.datos.estructura.EntidadAdtvaDA;
import ni.gob.minsa.malaria.datos.general.CatalogoElementoDA;
import ni.gob.minsa.malaria.datos.poblacion.ComunidadDA;
import ni.gob.minsa.malaria.datos.poblacion.DivisionPoliticaDA;
import ni.gob.minsa.malaria.datos.poblacion.SectorDA;
import ni.gob.minsa.malaria.datos.rociado.RociadoChkListDA;
import ni.gob.minsa.malaria.datos.rociado.RociadoDA;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.poblacion.Sector;
import ni.gob.minsa.malaria.modelo.rociado.ChecklistMalaria;
import ni.gob.minsa.malaria.modelo.rociado.EquiposMalaria;
import ni.gob.minsa.malaria.modelo.rociado.InsecticidaML;
import ni.gob.minsa.malaria.modelo.rociado.ItemsCheckListMalaria;
import ni.gob.minsa.malaria.modelo.rociado.RociadosMalaria;
import ni.gob.minsa.malaria.servicios.encuestas.CriaderoServices;
import ni.gob.minsa.malaria.servicios.estructura.EntidadAdtvaService;
import ni.gob.minsa.malaria.servicios.general.CatalogoElementoService;
import ni.gob.minsa.malaria.servicios.poblacion.ComunidadService;
import ni.gob.minsa.malaria.servicios.poblacion.DivisionPoliticaService;
import ni.gob.minsa.malaria.servicios.poblacion.SectorService;
import ni.gob.minsa.malaria.servicios.rociado.RociadoChkListServices;
import ni.gob.minsa.malaria.servicios.rociado.RociadoServices;
import ni.gob.minsa.malaria.soporte.Mensajes;
import ni.gob.minsa.malaria.soporte.Utilidades;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean
@ViewScoped
public class RociadoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4250206745795866734L;

	
	private static RociadoServices srvRociado = new RociadoDA();
	private static CriaderoServices srvCriadero = new CriaderoDA();
	private static RociadoChkListServices srvChkList = new RociadoChkListDA();
	private static EntidadAdtvaService srvSilais = new EntidadAdtvaDA();
	private static DivisionPoliticaService srvMunicipio = new DivisionPoliticaDA();
	private static SectorService srvSector = new SectorDA();
	private static ComunidadService srvComunidad = new ComunidadDA();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<ItemsCheckListMalaria,Integer> srvItemsListaCat = new CatalogoElementoDA(ItemsCheckListMalaria.class,"ItemsCheckListMalaria");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<EquiposMalaria,Integer> srvEquiposCat = new CatalogoElementoDA(EquiposMalaria.class,"EquiposMalaria");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static CatalogoElementoService<InsecticidaML,Integer> srvInsecticidaCat = new CatalogoElementoDA(InsecticidaML.class,"InsecticidaML");
	
	private Integer frmSom_SilaisBusqueda = 0;
	private String frmSom_MunicipioBusqueda = "0";
	private Comunidad frmInp_ComunidadBusqueda;
	private List<EntidadAdtva> itemsSilais;
	private List<DivisionPolitica> itemsMunicipioBusqueda;
	
	
	private RociadosMalaria rociadoActual;
	
	private Integer frmSom_SilaisUbicacion = 0;
	private String frmSom_MunicipioUbicacion = "0";
	private Comunidad frmInp_ComunidadUbicacion;
	private List<DivisionPolitica> itemsMunicipio;
	
	private Short frmInp_Control;
	
	private Long frmSom_Equipo;
	private Float frmInp_Carga;
	private Long[] frmSom_CheckList;
	private String frmInp_Boquilla;
	private Long frmSom_Insecticida;
	private Float frmInp_Formula;
	private Short frmInp_Ciclo;
	private Date frmDate_Fecha;

	
	private Short frmInp_viProgramadas;
	private Short frmInp_VidesAdecuado;
	private Short frmInp_VidesNoAdecuado;
	private Short frmInp_ViRociadas;
	private Short frmInp_HabitantesProtegidos;
	
	private Short frmInp_ViNoRxRenuente;
	private Short frmInp_ViNoRxCerradas;
	private Short frmInp_ViNoRxConstruccion;
	private Short frmInp_ViNoRxEnfermos;
	private Short frmInp_ViNoRxOtros;
	
	private Short frmInp_HabRociadas;
	private Short frmInp_HabNoRociadas;
	
	
	private Float frmInp_TotalCargas;
	private Float frmInp_TotalUtilizadas;
	
	private String frmInp_Rociador;
	
	
	
	private List<ItemsCheckListMalaria> itemsCheckList;
	private List<EquiposMalaria> itemsEquipos;
	private List<InsecticidaML> itemsInsecticidas;
	
	
	private short panelBusqueda = 0;
	private short panelRociado = 0;
	private short cmbNuevo = 0;
	private short cmbGuardar = 0;
	private short cmbRegresar = 0;	
	
	private LazyDataModel<RociadosMalaria> lazyRociados = null;

	public RociadoBean() {
		
		this.itemsCheckList = srvItemsListaCat.ListarActivos();
		this.itemsEquipos = srvEquiposCat.ListarActivos();
		this.itemsInsecticidas = srvInsecticidaCat.ListarActivos();
		this.itemsSilais = ni.gob.minsa.malaria.reglas.Operacion.entidadesAutorizadas(Utilidades.obtenerInfoSesion().getUsuarioId(),false);
		
		lazyRociados = new LazyDataModel<RociadosMalaria>() {
			
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public List<RociadosMalaria> load(int pPage, int pRows, String pSortField,
					SortOrder pSortOrder, Map<String, String> maps) {
				InfoResultado oResultado = new InfoResultado();
				
				List<RociadosMalaria> resultado = null;
				
				if( frmInp_ComunidadBusqueda != null )
					oResultado = srvRociado.obtenerListaRociados(pPage, pRows, pSortField, pSortOrder, frmInp_ComunidadBusqueda);
				else if( !frmSom_MunicipioBusqueda.equals("0") )
					oResultado = srvRociado.obtenerListaRociados(pPage, pRows, pSortField, pSortOrder, frmSom_MunicipioBusqueda);
				else if( frmSom_SilaisBusqueda > 0 )
					oResultado = srvRociado.obtenerListaRociados(pPage, pRows, pSortField, pSortOrder, frmSom_SilaisBusqueda);
				else oResultado.setOk(false);
				
				if( oResultado.isOk() && oResultado.getObjeto() != null ){
					this.setRowCount(oResultado.getFilasAfectadas());
					resultado = (List<RociadosMalaria>) oResultado.getObjeto();
				}else{
					this.setRowCount(0);
				}

				return resultado;
			}
		};
		
	}

	public void actualizarVisibilidadPaneles(ActionEvent evt){

		if( evt.getComponent().getClientId().equals("frmRociado:cmbNuevo")
				|| evt.getComponent().getId().equals("cmbSeleccionarRociado") ){
			panelBusqueda = 1;
			panelRociado = 1;
			cmbRegresar = 1;
			cmbGuardar = 1;
			cmbNuevo = 1;
			limpiarFormularioRociado();
		}else if( evt.getComponent().getClientId().equals("frmRociado:cmbRegresar")){;
			panelBusqueda = 0;
			panelRociado = 0;
			cmbNuevo = 0;
			cmbGuardar = 0;
			cmbRegresar = 0;
			limpiarFormularioRociado();
		}

	}	
	
	@SuppressWarnings("unchecked")
	public void actualizarMunicipiosBusqueda(){
		InfoResultado oResultado = new InfoResultado();
		try{
			
			oResultado = srvCriadero.obtenerMunicipiosPorSilais(frmSom_SilaisBusqueda);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				itemsMunicipioBusqueda = (List<DivisionPolitica>) oResultado.getObjeto();
			}else{
				itemsMunicipioBusqueda = null;
			}
			
		}catch(Exception e){
			System.out.println("Error Obteniendo Listado Municipios Por Silais");
			e.printStackTrace();
		}
		
	}
		
	@SuppressWarnings("unchecked")
	public void actualizarMunicipiosUbicacion(){
		InfoResultado oResultado = new InfoResultado();
		try{
			
			oResultado = srvCriadero.obtenerMunicipiosPorSilais(frmSom_SilaisUbicacion);
			if( oResultado.isOk() && oResultado.getObjeto() != null ){
				itemsMunicipio = (List<DivisionPolitica>) oResultado.getObjeto();
			}else{
				itemsMunicipio = null;
			}
			
		}catch(Exception e){
			System.out.println("Error Obteniendo Listado Municipios Por Silais");
			e.printStackTrace();
		}
		
	}

	public List<Comunidad> completarComunidadBusqueda(String query){
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades = srvComunidad.ComunidadesPorMunicipioNombre(frmSom_MunicipioBusqueda, query, 10);
		return oComunidades;		
	}	
	
	public List<Comunidad> completarComunidad(String query){
		List<Comunidad> oComunidades = new ArrayList<Comunidad>();
		oComunidades = srvComunidad.ComunidadesPorMunicipioNombre(frmSom_MunicipioUbicacion, query, 10);
		return oComunidades;		
	}
	
	public void limpiarFormulario(ActionEvent evt){
		limpiarFormulario();
		limpiarFormularioRociado();
	}
	
	public void limpiarFormulario(){
		panelBusqueda = 0;
		panelRociado = 0;
		cmbNuevo = 0;
		cmbGuardar = 0;
		cmbRegresar = 0;		
	}
	
	public void limpiarFormularioRociado(){
		
		frmSom_SilaisUbicacion = null;
		frmSom_MunicipioUbicacion = null;
		frmInp_ComunidadUbicacion = null;
		itemsMunicipio = null;
		
		rociadoActual = null;
		
		frmInp_Control = null;
		
		frmSom_Equipo = null;
		frmInp_Carga = null;
		frmInp_Boquilla = null;
		frmSom_Insecticida = null;
		frmInp_Formula = null;
		frmInp_Ciclo = null;
		frmDate_Fecha = null;
		
		frmInp_viProgramadas = null;
		frmInp_VidesAdecuado = null;
		frmInp_VidesNoAdecuado = null;
		frmInp_ViRociadas = null;
		
		frmInp_ViNoRxRenuente = null;
		frmInp_ViNoRxCerradas = null;
		frmInp_ViNoRxConstruccion = null;
		frmInp_ViNoRxEnfermos = null;
		frmInp_ViNoRxOtros = null;
		
		frmInp_HabRociadas = null;
		frmInp_HabNoRociadas = null;
		
		frmInp_TotalCargas = null;
		frmInp_TotalUtilizadas = null;
		frmInp_HabitantesProtegidos = null;
		
		frmInp_Rociador = null;
		
		frmSom_CheckList = new Long[6];
		
	}
	
	public void calcularViviendas(){
		frmInp_ViRociadas = (short) ( (frmInp_VidesAdecuado == null ? 0 : frmInp_VidesAdecuado) 
											+ (frmInp_VidesNoAdecuado == null ? 0 : frmInp_VidesNoAdecuado)) ;
	}
	
	public void cacularTotalInsecticidaUtilizado(){
		frmInp_TotalUtilizadas = (frmInp_TotalCargas == null ? 0 : frmInp_TotalCargas) * ( frmInp_Formula == null ? 0 : frmInp_Formula );
	}
	
	private void actualizarDatosRociado(RociadosMalaria pRociado){
		InfoResultado oResultado = null;
		FacesMessage msg = null;
		
		if( pRociado == null ){
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Rociado no valido","Seleccionar un rociado valido para modificar");
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
				
		frmSom_SilaisUbicacion = pRociado.getSilais() != null ? (int) pRociado.getSilais().getEntidadAdtvaId() : 0;
		frmSom_MunicipioUbicacion = pRociado.getMunicipio() != null ? pRociado.getMunicipio().getCodigoNacional() : "0";
		frmInp_ComunidadUbicacion = pRociado.getComunidad() != null ? pRociado.getComunidad() : null;
		
		oResultado = null;
		oResultado = srvCriadero.obtenerMunicipiosPorSilais(frmSom_SilaisUbicacion);
		if( oResultado.isOk() && oResultado.getObjeto() != null ){
			itemsMunicipio = (List<DivisionPolitica>) oResultado.getObjeto();
		}
		
		frmInp_Control = pRociado.getControl();
		
		frmSom_Equipo = pRociado.getEquipo() != null ? pRociado.getEquipo().getCatalogoId() : 0;
		frmInp_Carga = pRociado.getCarga();
		
		frmInp_Boquilla = pRociado.getBoquilla();
				
//		frmSom_Insecticida = pRociado.getEquipo() != null ? pRociado.getInsecticida().getCatalogoId() : 0;
		
		oResultado = null;
		oResultado = srvRociado.obtenerCatalogoPorCodigo(pRociado.getInsecticida());
		if( oResultado.isOk() && oResultado.getObjeto() != null){
			frmSom_Insecticida = ((InsecticidaML) oResultado.getObjeto()).getCatalogoId();
		}
		
		frmInp_Formula = pRociado.getFormulacion();
		frmInp_Ciclo = pRociado.getCiclo();
		frmDate_Fecha = pRociado.getFecha();
		
		frmInp_viProgramadas = pRociado.getViviendasProgramadas();
		frmInp_VidesAdecuado = pRociado.getDesalojoAdecuado();
		frmInp_VidesNoAdecuado = pRociado.getDesalojoInadecuado();
		frmInp_ViRociadas = pRociado.getViviendasRociadas();
		
		frmInp_ViNoRxRenuente = pRociado.getRenuentes();
		frmInp_ViNoRxCerradas = pRociado.getCerradas();
		frmInp_ViNoRxConstruccion = pRociado.getConstruccion();
		frmInp_ViNoRxEnfermos = pRociado.getEnfermos();
		frmInp_ViNoRxOtros = pRociado.getOtros();
		
		frmInp_HabRociadas = pRociado.getHabRociadas();
		frmInp_HabNoRociadas = pRociado.getHabNoRociadas();
		
		frmInp_TotalCargas = pRociado.getTotalCargas();
		frmInp_TotalUtilizadas = pRociado.getTotalUtilizadas();
		frmInp_HabitantesProtegidos = pRociado.getHabitantesProtegidos();
		
		frmInp_Rociador = pRociado.getRociador();
		
		
		List<ChecklistMalaria> listaChk = new ArrayList<ChecklistMalaria>();
		oResultado = null;
		oResultado = srvChkList.obtenerChkListPorRociado(pRociado);
		if( oResultado.isExcepcion()  ){
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,oResultado.getMensaje(),oResultado.getMensajeDetalle());
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		frmSom_CheckList = new Long[6];
		int i=0;
		listaChk = (List<ChecklistMalaria>) oResultado.getObjeto();
		if( listaChk != null){
			for( ChecklistMalaria oChk: listaChk ){
				frmSom_CheckList[i++] = oChk.getElementoLista().getCatalogoId();
			}
		}
		
	}
	
	private InfoResultado validarRociado(){
		InfoResultado oResultado = new InfoResultado();
		
		
		if( frmSom_SilaisUbicacion == 0 ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Silais");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;					
		}
		
		if( frmSom_MunicipioUbicacion.equals("0") ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Municipio");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;					
		}		
		
		if( frmInp_ComunidadUbicacion == null){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Comunidad");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;
		}
		
		if( frmInp_Control == null){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Control Registro");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;
		}
			

		
		if( frmSom_Equipo == 0){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Equipo");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmInp_Carga == null){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Carga");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;
		}
		
		if( frmSom_CheckList == null){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Lista de Comprobación");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmInp_Boquilla == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Boquilla");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;
		}
		
		if( frmSom_Insecticida == 0 ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Boquilla");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmInp_Formula == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Formularción");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}

		if( frmInp_Ciclo == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Ciclo");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		

		if( frmDate_Fecha == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar Fecha de Rociado");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}				
		
		if( rociadoActual == null ){
			boolean esPermitidoRegistro = srvRociado.validarNumeroControlRociado(frmInp_Control,0,"0",frmInp_ComunidadUbicacion.getCodigo(),"0", frmDate_Fecha);
			if( !esPermitidoRegistro ){
				oResultado.setOk(false);
				oResultado.setMensaje("El numero de control ya existe registrado, validar los datos");
				oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
				return oResultado;
			}
		}
		
		if( frmInp_viProgramadas == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar viviendas programadas");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmInp_VidesAdecuado == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar viviendas con desalojo adecuado");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmInp_VidesNoAdecuado == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar viviendas con desalojo no adecuado");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmInp_ViRociadas == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar viviendas rociadas");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}
		
		if( frmInp_ViNoRxRenuente == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar viviendas no rociadas por motivo renuente");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmInp_ViNoRxCerradas == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar viviendas no rociadas por motivo cerradas");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		
		
		if( frmInp_ViNoRxConstruccion == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar viviendas no rociadas por motivo en construcción");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}						
		
		if( frmInp_ViNoRxEnfermos == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar viviendas no rociadas por motivo enfermos");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}						
		
		if( frmInp_ViNoRxOtros == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar viviendas no rociadas por motivo otros");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}				

		if( frmInp_HabRociadas == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar habitaciones rociadas");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}				

		if( frmInp_HabNoRociadas == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar habitaciones no rociadas");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}		

		if( frmInp_TotalCargas == null ){
			oResultado.setOk(false);
			oResultado.setMensaje("Completar total cargas");
			oResultado.setGravedad(InfoResultado.SEVERITY_WARN);
			return oResultado;			
		}	
		
		oResultado.setOk(true);
		return oResultado;
	}
	
	private InfoResultado guardarRociado(){
		InfoResultado oResultado = new InfoResultado();
		long auxIdRociado = 0;
		
		if( rociadoActual == null ){
			rociadoActual = new RociadosMalaria();
			rociadoActual.setUsuarioRegistro(Utilidades.obtenerInfoSesion().getUsername());
			rociadoActual.setFechaRegistro(new Date());
		}
		auxIdRociado = rociadoActual.getRociadoId();
		
		try{
	
			rociadoActual.setBoquilla(frmInp_Boquilla);
			rociadoActual.setCarga(frmInp_Carga);
			rociadoActual.setCerradas(frmInp_ViNoRxCerradas);
			rociadoActual.setCiclo(frmInp_Ciclo);;
			rociadoActual.setComunidad(frmInp_ComunidadUbicacion);
			rociadoActual.setConstruccion(frmInp_ViNoRxConstruccion);
			rociadoActual.setControl(frmInp_Control);
			rociadoActual.setDesalojoAdecuado(frmInp_VidesAdecuado);
			rociadoActual.setDesalojoInadecuado(frmInp_VidesNoAdecuado);
			rociadoActual.setEnfermos(frmInp_ViNoRxEnfermos);
			rociadoActual.setFecha(frmDate_Fecha);
			rociadoActual.setFormulacion(frmInp_Formula);
			rociadoActual.setHabitantesProtegidos(frmInp_HabitantesProtegidos);
			rociadoActual.setHabNoRociadas(frmInp_HabNoRociadas);
			rociadoActual.setHabRociadas(frmInp_HabRociadas);
			rociadoActual.setOtros(frmInp_ViNoRxOtros);
			rociadoActual.setRenuentes(frmInp_ViNoRxRenuente);
			rociadoActual.setRociador(frmInp_Rociador);
			rociadoActual.setTotalCargas(frmInp_TotalCargas);
			rociadoActual.setTotalUtilizadas(frmInp_TotalUtilizadas);
			rociadoActual.setViviendasProgramadas(frmInp_viProgramadas);
			rociadoActual.setViviendasRociadas( ((Integer) (frmInp_VidesAdecuado + frmInp_VidesNoAdecuado)).shortValue());
			
			oResultado = null;
			oResultado = srvEquiposCat.Encontrar(frmSom_Equipo);
			if( !oResultado.isOk() || oResultado.isExcepcion()){
				if( auxIdRociado == 0) rociadoActual.setRociadoId(0);
				return oResultado;
			}
			EquiposMalaria oEquipo = (EquiposMalaria) oResultado.getObjeto();
			rociadoActual.setEquipo(oEquipo);
			
			oResultado = null;
			oResultado = srvInsecticidaCat.Encontrar(frmSom_Insecticida);
			if( !oResultado.isOk() || oResultado.isExcepcion()){
				if( auxIdRociado == 0) rociadoActual.setRociadoId(0);
				return oResultado;
			}
			InsecticidaML oInsecticida = (InsecticidaML) oResultado.getObjeto();
			rociadoActual.setInsecticida(oInsecticida.getCodigo());
			
			oResultado = null;
			oResultado = srvMunicipio.EncontrarPorCodigoNacional(frmSom_MunicipioUbicacion);
			if( !oResultado.isOk() || oResultado.isExcepcion()){
				if( auxIdRociado == 0) rociadoActual.setRociadoId(0);
				return oResultado;
			}
			DivisionPolitica oMunicipio = (DivisionPolitica) oResultado.getObjeto();
			rociadoActual.setMunicipio(oMunicipio);
			
			oResultado = null;
			oResultado = srvSector.Encontrar(frmInp_ComunidadUbicacion.getSector().getSectorId());
			if( !oResultado.isOk() || oResultado.isExcepcion()){
				if( auxIdRociado == 0) rociadoActual.setRociadoId(0);
				return oResultado;
			}
			Sector oSector = (Sector) oResultado.getObjeto();
			rociadoActual.setSector(oSector);
			
			oResultado = null;
			oResultado = srvSilais.Encontrar(frmSom_SilaisUbicacion);
			if( !oResultado.isOk() || oResultado.isExcepcion()){
				if( auxIdRociado == 0) rociadoActual.setRociadoId(0);
				return oResultado;
			}
			EntidadAdtva oSilais = (EntidadAdtva) oResultado.getObjeto();
			rociadoActual.setSilais(oSilais);			
			
			oResultado = null;
			oResultado = srvRociado.guardarRociado(rociadoActual);
			if( !oResultado.isOk() || oResultado.isExcepcion()){
				if( auxIdRociado == 0) rociadoActual.setRociadoId(0);
				return oResultado;
			}
			rociadoActual = (RociadosMalaria) oResultado.getObjeto();
			
			boolean existe = false;
			long oLong;
			int i;
			List<ChecklistMalaria> listChk = new ArrayList<ChecklistMalaria>();
			List<ChecklistMalaria> listChkEliminar = new ArrayList<ChecklistMalaria>();
			if( frmSom_CheckList != null ){
				
				
				
				oResultado = null;
				oResultado = srvChkList.obtenerChkListPorRociado(rociadoActual);
				if( oResultado.isExcepcion() ){
					return oResultado;
				}
				listChk = (List<ChecklistMalaria>) oResultado.getObjeto();
				
				if( listChk == null ) listChk = new ArrayList<ChecklistMalaria>();
				for(i=0; i < frmSom_CheckList.length; i++){
					oResultado = null;
					oLong = frmSom_CheckList[i];
					existe = false;
					ChecklistMalaria oCheckList = new ChecklistMalaria();
					for( ChecklistMalaria oChk : listChk ){
						if( oChk.getElementoLista().getCatalogoId() == oLong ){
							existe = true;
							break;
						}
					}
					if( !existe ){
						oResultado = srvItemsListaCat.Encontrar(oLong);
						if( oResultado.isOk() && oResultado.getObjeto() != null ){
							oCheckList.setElementoLista( (ItemsCheckListMalaria) oResultado.getObjeto() );
							oCheckList.setRociadosMalaria(rociadoActual);
							oCheckList.setFechaRegistro(new Date());
							oCheckList.setUsuarioRegistro(Utilidades.obtenerInfoSesion().getUsername());
							listChk.add(oCheckList);
						}
					}
				}
				existe = false;
				for( ChecklistMalaria oChk1 : listChk){
					for(i=0; i < frmSom_CheckList.length ;i++){
						oLong = frmSom_CheckList[i];
						existe = false;
						if( oChk1.getElementoLista().getCatalogoId() == oLong ){
							existe = true;
							break;
						}
					}
					if( !existe ){
						listChkEliminar.add(oChk1);
					}					
				}
				
				oResultado = null;
				for( ChecklistMalaria oChkg : listChk ){
					oResultado = srvChkList.guardarChkList(oChkg);
					if( !oResultado.isOk() || oResultado.isExcepcion()){
						return oResultado;
					}
				}
				
				oResultado = null;
				for( ChecklistMalaria oChkg1 : listChkEliminar ){
					oResultado = srvChkList.eliminarChkList(oChkg1.getChecklistMalariaId());
					if( !oResultado.isOk() || oResultado.isExcepcion()){
						return oResultado;
					}
				}
				
			}
			
			oResultado = new InfoResultado();
			oResultado.setOk(true);
			if( auxIdRociado > 0 ) oResultado.setMensaje(Mensajes.REGISTRO_GUARDADO);
			else oResultado.setMensaje(Mensajes.REGISTRO_AGREGADO);
			
		}catch(Exception e){
			if( auxIdRociado == 0) rociadoActual.setRociadoId(0);
			oResultado = new InfoResultado();
			oResultado.setOk(false);
			oResultado.setExcepcion(true);
			oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
			oResultado.setMensaje("Error Metodo Guardar");
			oResultado.setMensajeDetalle(e.getMessage());			
		}
		
		
		return oResultado;
	}
	
	public void guardar(ActionEvent evt){
		InfoResultado oResultado = null;
		FacesMessage msg = null;
		
		oResultado = validarRociado();
		if( !oResultado.isOk() ){
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,oResultado.getMensaje(), oResultado.getMensajeDetalle());
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		oResultado = guardarRociado();
		if( !oResultado.isOk() || oResultado.isExcepcion() ){
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,oResultado.getMensaje(), oResultado.getMensajeDetalle());
			if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		
		msg = Mensajes.enviarMensaje(oResultado);
		if( msg != null ) FacesContext.getCurrentInstance().addMessage(null, msg);
		
	}

	public Integer getFrmSom_SilaisBusqueda() {
		return frmSom_SilaisBusqueda;
	}

	public void setFrmSom_SilaisBusqueda(Integer frmSom_SilaisBusqueda) {
		this.frmSom_SilaisBusqueda = frmSom_SilaisBusqueda;
	}

	public String getFrmSom_MunicipioBusqueda() {
		return frmSom_MunicipioBusqueda;
	}

	public void setFrmSom_MunicipioBusqueda(String frmSom_MunicipioBusqueda) {
		this.frmSom_MunicipioBusqueda = frmSom_MunicipioBusqueda;
	}

	public Comunidad getFrmInp_ComunidadBusqueda() {
		return frmInp_ComunidadBusqueda;
	}

	public void setFrmInp_ComunidadBusqueda(Comunidad frmInp_ComunidadBusqueda) {
		this.frmInp_ComunidadBusqueda = frmInp_ComunidadBusqueda;
	}

	public List<DivisionPolitica> getItemsMunicipioBusqueda() {
		return itemsMunicipioBusqueda;
	}

	public void setItemsMunicipioBusqueda(
			List<DivisionPolitica> itemsMunicipioBusqueda) {
		this.itemsMunicipioBusqueda = itemsMunicipioBusqueda;
	}

	public RociadosMalaria getRociadoActual() {
		return rociadoActual;
	}

	public void setRociadoActual(RociadosMalaria rociadoActual) {
		this.rociadoActual = rociadoActual;
		actualizarDatosRociado(rociadoActual);
	}

	public Integer getFrmSom_SilaisUbicacion() {
		return frmSom_SilaisUbicacion;
	}

	public void setFrmSom_SilaisUbicacion(Integer frmSom_SilaisUbicacion) {
		this.frmSom_SilaisUbicacion = frmSom_SilaisUbicacion;
	}

	public String getFrmSom_MunicipioUbicacion() {
		return frmSom_MunicipioUbicacion;
	}

	public void setFrmSom_MunicipioUbicacion(String frmSom_MunicipioUbicacion) {
		this.frmSom_MunicipioUbicacion = frmSom_MunicipioUbicacion;
	}

	public Comunidad getFrmInp_ComunidadUbicacion() {
		return frmInp_ComunidadUbicacion;
	}

	public void setFrmInp_ComunidadUbicacion(Comunidad frmInp_ComunidadUbicacion) {
		this.frmInp_ComunidadUbicacion = frmInp_ComunidadUbicacion;
	}

	public List<DivisionPolitica> getItemsMunicipio() {
		return itemsMunicipio;
	}

	public void setItemsMunicipio(List<DivisionPolitica> itemsMunicipio) {
		this.itemsMunicipio = itemsMunicipio;
	}

	public Short getFrmInp_Control() {
		return frmInp_Control;
	}

	public void setFrmInp_Control(Short frmInp_Control) {
		this.frmInp_Control = frmInp_Control;
	}

	public Long getFrmSom_Equipo() {
		return frmSom_Equipo;
	}

	public void setFrmSom_Equipo(Long frmSom_Equipo) {
		this.frmSom_Equipo = frmSom_Equipo;
	}

	public Float getFrmInp_Carga() {
		return frmInp_Carga;
	}

	public void setFrmInp_Carga(Float frmInp_Carga) {
		this.frmInp_Carga = frmInp_Carga;
	}

	public String getFrmInp_Boquilla() {
		return frmInp_Boquilla;
	}

	public void setFrmInp_Boquilla(String frmInp_Boquilla) {
		this.frmInp_Boquilla = frmInp_Boquilla;
	}

	public Long getFrmSom_Insecticida() {
		return frmSom_Insecticida;
	}

	public void setFrmSom_Insecticida(Long frmSom_Insecticida) {
		this.frmSom_Insecticida = frmSom_Insecticida;
	}

	public Float getFrmInp_Formula() {
		return frmInp_Formula;
	}

	public void setFrmInp_Formula(Float frmInp_Formula) {
		this.frmInp_Formula = frmInp_Formula;
	}

	public Short getFrmInp_Ciclo() {
		return frmInp_Ciclo;
	}

	public void setFrmInp_Ciclo(Short frmInp_Ciclo) {
		this.frmInp_Ciclo = frmInp_Ciclo;
	}

	public Date getFrmDate_Fecha() {
		return frmDate_Fecha;
	}

	public void setFrmDate_Fecha(Date frmDate_Fecha) {
		this.frmDate_Fecha = frmDate_Fecha;
	}

	public Short getFrmInp_viProgramadas() {
		return frmInp_viProgramadas;
	}

	public void setFrmInp_viProgramadas(Short frmInp_viProgramadas) {
		this.frmInp_viProgramadas = frmInp_viProgramadas;
	}

	public Short getFrmInp_VidesAdecuado() {
		return frmInp_VidesAdecuado;
	}

	public void setFrmInp_VidesAdecuado(Short frmInp_VidesAdecuado) {
		this.frmInp_VidesAdecuado = frmInp_VidesAdecuado;
	}

	public Short getFrmInp_VidesNoAdecuado() {
		return frmInp_VidesNoAdecuado;
	}

	public void setFrmInp_VidesNoAdecuado(Short frmInp_VidesNoAdecuado) {
		this.frmInp_VidesNoAdecuado = frmInp_VidesNoAdecuado;
	}

	public Short getFrmInp_ViRociadas() {
		return frmInp_ViRociadas;
	}

	public void setFrmInp_ViRociadas(Short frmInp_ViRociadas) {
		this.frmInp_ViRociadas = frmInp_ViRociadas;
	}

	public Short getFrmInp_ViNoRxRenuente() {
		return frmInp_ViNoRxRenuente;
	}

	public void setFrmInp_ViNoRxRenuente(Short frmInp_ViNoRxRenuente) {
		this.frmInp_ViNoRxRenuente = frmInp_ViNoRxRenuente;
	}

	public Short getFrmInp_ViNoRxCerradas() {
		return frmInp_ViNoRxCerradas;
	}

	public void setFrmInp_ViNoRxCerradas(Short frmInp_ViNoRxCerradas) {
		this.frmInp_ViNoRxCerradas = frmInp_ViNoRxCerradas;
	}

	public Short getFrmInp_ViNoRxConstruccion() {
		return frmInp_ViNoRxConstruccion;
	}

	public void setFrmInp_ViNoRxConstruccion(Short frmInp_ViNoRxConstruccion) {
		this.frmInp_ViNoRxConstruccion = frmInp_ViNoRxConstruccion;
	}

	public Short getFrmInp_ViNoRxEnfermos() {
		return frmInp_ViNoRxEnfermos;
	}

	public void setFrmInp_ViNoRxEnfermos(Short frmInp_ViNoRxEnfermos) {
		this.frmInp_ViNoRxEnfermos = frmInp_ViNoRxEnfermos;
	}

	public Short getFrmInp_ViNoRxOtros() {
		return frmInp_ViNoRxOtros;
	}

	public void setFrmInp_ViNoRxOtros(Short frmInp_ViNoRxOtros) {
		this.frmInp_ViNoRxOtros = frmInp_ViNoRxOtros;
	}

	public Short getFrmInp_HabRociadas() {
		return frmInp_HabRociadas;
	}

	public void setFrmInp_HabRociadas(Short frmInp_HabRociadas) {
		this.frmInp_HabRociadas = frmInp_HabRociadas;
	}

	public Short getFrmInp_HabNoRociadas() {
		return frmInp_HabNoRociadas;
	}

	public void setFrmInp_HabNoRociadas(Short frmInp_HabNoRociadas) {
		this.frmInp_HabNoRociadas = frmInp_HabNoRociadas;
	}

	public Short getFrmInp_HabitantesProtegidos() {
		return frmInp_HabitantesProtegidos;
	}

	public void setFrmInp_HabitantesProtegidos(Short frmInp_HabitantesProtegidos) {
		this.frmInp_HabitantesProtegidos = frmInp_HabitantesProtegidos;
	}

	public Float getFrmInp_TotalCargas() {
		return frmInp_TotalCargas;
	}

	public void setFrmInp_TotalCargas(Float frmInp_TotalCargas) {
		this.frmInp_TotalCargas = frmInp_TotalCargas;
	}

	public Float getFrmInp_TotalUtilizadas() {
		return frmInp_TotalUtilizadas;
	}

	public void setFrmInp_TotalUtilizadas(Float frmInp_TotalUtilizadas) {
		this.frmInp_TotalUtilizadas = frmInp_TotalUtilizadas;
	}

	public String getFrmInp_Rociador() {
		return frmInp_Rociador;
	}

	public void setFrmInp_Rociador(String frmInp_Rociador) {
		this.frmInp_Rociador = frmInp_Rociador;
	}

	public Long[] getFrmSom_CheckList() {
		return frmSom_CheckList;
	}

	public void setFrmSom_CheckList(Long[] frmSom_CheckList) {
		this.frmSom_CheckList = frmSom_CheckList;
	}

	public List<ItemsCheckListMalaria> getItemsCheckList() {
		return itemsCheckList;
	}

	public void setItemsCheckList(List<ItemsCheckListMalaria> itemsCheckList) {
		this.itemsCheckList = itemsCheckList;
	}

	public List<EquiposMalaria> getItemsEquipos() {
		return itemsEquipos;
	}

	public void setItemsEquipos(List<EquiposMalaria> itemsEquipos) {
		this.itemsEquipos = itemsEquipos;
	}

	public List<EntidadAdtva> getItemsSilais() {
		return itemsSilais;
	}

	public void setItemsSilais(List<EntidadAdtva> itemsSilais) {
		this.itemsSilais = itemsSilais;
	}

	public short getPanelBusqueda() {
		return panelBusqueda;
	}

	public void setPanelBusqueda(short panelBusqueda) {
		this.panelBusqueda = panelBusqueda;
	}

	public short getPanelRociado() {
		return panelRociado;
	}

	public void setPanelRociado(short panelRociado) {
		this.panelRociado = panelRociado;
	}

	public short getCmbNuevo() {
		return cmbNuevo;
	}

	public void setCmbNuevo(short cmbNuevo) {
		this.cmbNuevo = cmbNuevo;
	}

	public short getCmbGuardar() {
		return cmbGuardar;
	}

	public void setCmbGuardar(short cmbGuardar) {
		this.cmbGuardar = cmbGuardar;
	}

	public short getCmbRegresar() {
		return cmbRegresar;
	}

	public void setCmbRegresar(short cmbRegresar) {
		this.cmbRegresar = cmbRegresar;
	}

	public LazyDataModel<RociadosMalaria> getLazyRociados() {
		return lazyRociados;
	}

	public List<InsecticidaML> getItemsInsecticidas() {
		return itemsInsecticidas;
	}

	
	
	
	
	
	
	
	
}