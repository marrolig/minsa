package ni.gob.minsa.malaria.modelo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ni.gob.minsa.malaria.modelo.general.ClaseAccesibilidad;
import ni.gob.minsa.malaria.modelo.general.TipoTransporte;

@MappedSuperclass  
public class BaseEntidadAcceso extends BaseEntidadCreacion  
{  
	@Size(min=1,max=500,message="Solo se admiten 500 caracteres para describir como llegar")
    @NotNull(message="La descripción de como llegar es requerida.")
	@Column(name="COMO_LLEGAR",nullable=false,length=500)
	private String comoLlegar;
	
	@ManyToOne
	@JoinColumn(name="TIPO_TRANSPORTE", nullable=true, referencedColumnName="CODIGO")
	private TipoTransporte tipoTransporte;

    @ManyToOne
	@JoinColumn(name="ACCESIBILIDAD", nullable=true, referencedColumnName="CODIGO")
	private ClaseAccesibilidad claseAccesibilidad;

    @DecimalMax(value="100.0",message="La distancia desde el punto de referencia no puede ser superior a 100 km")
    @DecimalMin(value="0.1",message="La distancia debe de ser superior a 0.1 km (100 mts)")
	@Digits(integer=4,fraction=1)
	@Column(nullable=true,precision=4,scale=1)
    private BigDecimal distancia;

	@DecimalMax(value="1440",message="El tiempo no puede ser superior a 1440 minutos (24 horas)")
	@DecimalMin(value="1",message="El tiempo debe ser superior a 1 minuto")
	@Digits(integer=4,fraction=0)
	@Column(nullable=true,precision=4,scale=0)
    private BigDecimal tiempo;

	@Size(min=1,max=100,message="El número de caracteres permitidos es de 1 a 100.")
	@Column(name="PUNTO_REFERENCIA",nullable=true,length=100)
    private String puntoReferencia;

	public String getComoLlegar() {
		return comoLlegar;
	}

	public void setComoLlegar(String comoLlegar) {
		this.comoLlegar = comoLlegar;
	}

	public TipoTransporte getTipoTransporte() {
		return tipoTransporte;
	}

	public void setTipoTransporte(TipoTransporte tipoTransporte) {
		this.tipoTransporte = tipoTransporte;
	}

	public ClaseAccesibilidad getClaseAccesibilidad() {
		return claseAccesibilidad;
	}

	public void setClaseAccesibilidad(ClaseAccesibilidad claseAccesibilidad) {
		this.claseAccesibilidad = claseAccesibilidad;
	}

	public BigDecimal getDistancia() {
		return distancia;
	}

	public void setDistancia(BigDecimal distancia) {
		this.distancia = distancia;
	}

	public BigDecimal getTiempo() {
		return tiempo;
	}

	public void setTiempo(BigDecimal tiempo) {
		this.tiempo = tiempo;
	}

	public String getPuntoReferencia() {
		return puntoReferencia;
	}

	public void setPuntoReferencia(String puntoReferencia) {
		this.puntoReferencia = puntoReferencia;
	}

	
}  
