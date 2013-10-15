// -----------------------------------------------
// MuestreoPruebaRapida.java
// -----------------------------------------------
package ni.gob.minsa.malaria.modelo.vigilancia;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import ni.gob.minsa.malaria.modelo.BaseEntidadCreacion;

import org.eclipse.persistence.annotations.Cache;

/**
 * Clase de persistencia asociada a la tabla MUESTREOS_PRUEBAS_RAPIDAS 
 * en el esquema SIVE.
 * 
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 01/11/2012
 * @since jdk1.6.0_21
 */
@Entity
@Table(name="MUESTREOS_PRUEBAS_RAPIDAS",schema="SIVE",
			uniqueConstraints=@UniqueConstraint(columnNames={"MUESTREO_HEMATICO"}))
@Cache(alwaysRefresh=true,disableHits=true)
public class MuestreoPruebaRapida extends BaseEntidadCreacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MSTRS_PRMS_ID_GENERATOR", sequenceName="SIVE.MSTRS_PRMS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MSTRS_PRMS_ID_GENERATOR")
	@Column(name="MUESTREO_PRUEBA_RAPIDA_ID",updatable=false,nullable=false)
	private long muestreoPruebaRapidaId;

	@NotNull(message="La asociación con el muestreo hemático es requerida")
	@OneToOne(targetEntity=MuestreoHematico.class)
	@JoinColumn(name="MUESTREO_HEMATICO",nullable=false,updatable=false)
	private MuestreoHematico muestreoHematico;
	
	@NotNull(message="La fecha en la cual se realizó la prueba rápida de malaria es requerida")
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="FECHA",nullable=false)
	private Date fecha;

	@NotNull(message="La declaración del resultado de la prueba rápida de malaria es requerida")
	@ManyToOne
	@JoinColumn(name="RESULTADO",referencedColumnName="CODIGO")
	private ResultadoPruebaRapida resultado;
	
	@NotNull(message="La declaración del responsable de realizar la prueba rápida de malaria es requerida")
	@ManyToOne
	@JoinColumn(name="REALIZADO",referencedColumnName="CODIGO")
	private ResponsablePruebaRapida realizado;
	
    public MuestreoPruebaRapida() {
    }

	/**
	 * @return Identificador único del resultado de la prueba rápida de malaria asociada a un muestreo hemático
	 */
	public long getMuestreoPruebaRapidaId() {
		return muestreoPruebaRapidaId;
	}

	/**
	 * Establece el identificador del resultado de la prueba rápida de malaria
	 * 
	 * @param muestreoPruebaRapidaId Identificador del resultado de la prueba rápida de malaria
	 */
	public void setMuestreoPruebaRapidaId(long muestreoPruebaRapidaId) {
		this.muestreoPruebaRapidaId = muestreoPruebaRapidaId;
	}

	/**
	 * Obtiene el objeto {@link MuestreoHematico} al cual corresponde el resultado de la
	 * prueba rápida de malaria
	 * 
	 * @return {@link MuestreoHematico}
	 */
	public MuestreoHematico getMuestreoHematico() {
		return muestreoHematico;
	}

	/**
	 * Estable el objeto {@link MuestreoHematico} al cual corresponde el resultado de la
	 * prueba rápida de malaria
	 * 
	 * @param muestreoHematico {@link MuestreoHematico}
	 */
	public void setMuestreoHematico(MuestreoHematico muestreoHematico) {
		this.muestreoHematico = muestreoHematico;
	}

	/**
	 * Obtiene la fecha en la cual se realizó la prueba rápida de malaria
	 * 
	 * @return fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * Establece la fecha en la cual se realizó la prueba rápida de malaria
	 * 
	 * @param fecha Fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Obtiene el objeto {@link ResultadoPruebaRapida} que corresponde al resultado obtenido
	 * de la prueba rápida de malaria
	 * 
	 * @return {@link ResultadoPruebaRapida}
	 */
	public ResultadoPruebaRapida getResultado() {
		return resultado;
	}

	/**
	 * Establece el objeto {@link ResultadoPruebaRapida} que corresponde al resultado obtenido
	 * de la prueba rápida de malaria
	 * 
	 * @param resultado {@link ResultadoPruebaRapida}
	 */
	public void setResultado(ResultadoPruebaRapida resultado) {
		this.resultado = resultado;
	}

	/**
	 * @return the realizado
	 */
	public ResponsablePruebaRapida getRealizado() {
		return realizado;
	}

	/**
	 * @param realizado the realizado to set
	 */
	public void setRealizado(ResponsablePruebaRapida realizado) {
		this.realizado = realizado;
	}


}