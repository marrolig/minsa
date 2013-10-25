package ni.gob.minsa.malaria.modelo.encuesta;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import ni.gob.minsa.malaria.modelo.general.Catalogo;


/**
 * The persistent class for the CRIADEROS_ESPECIES database table.
 * 
 */
@Entity
@Table(name="ML_CRIADEROSESPECIES")
public class CriaderosEspecie implements Serializable {
	private static final long serialVersionUID = 1L;
	private long criaderoEspecieId;
	private Criadero criadero;
	private EspeciesAnopheles especieAnophele;
	private Date fechaRegistro;
	private String usuarioRegistro;

    public CriaderosEspecie() {
    }


	@Id
	@SequenceGenerator(name="CRIADEROS_ESPECIES_CRIADEROESPECIEID_GENERATOR",sequenceName="SIVE.SEQ_CRIADEROESPECIAID",allocationSize=1 )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CRIADEROS_ESPECIES_CRIADEROESPECIEID_GENERATOR")
	@Column(name="CRIADERO_ESPECIE_ID")
	public long getCriaderoEspecieId() {
		return this.criaderoEspecieId;
	}

	public void setCriaderoEspecieId(long criaderoEspecieId) {
		this.criaderoEspecieId = criaderoEspecieId;
	}


	@NotNull(message="Criadero es requerido")
	@ManyToOne(targetEntity=Criadero.class,fetch=FetchType.LAZY)
	@JoinColumn(name="CRIADERO",referencedColumnName="CODIGO",nullable=false,updatable=true)
	public Criadero getCriadero() {
		return this.criadero;
	}

	public void setCriadero(Criadero criadero) {
		this.criadero = criadero;
	}


	@NotNull(message="Identificacion del tipo de especie anophele requerido")
	@ManyToOne(targetEntity=Catalogo.class,fetch=FetchType.LAZY)
	@JoinColumn(name="ESPECIE_ANOPHELE",referencedColumnName="CODIGO",nullable=false,updatable=true)
	public EspeciesAnopheles getEspecieAnophele() {
		return this.especieAnophele;
	}

	public void setEspecieAnophele(EspeciesAnopheles especieAnophele) {
		this.especieAnophele = especieAnophele;
	}


	@NotNull(message="Fecha registro de datos requerido")
	@Temporal(TemporalType.DATE)
	@Column(name="FECHA_REGISTRO",nullable=false)
	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}


	@NotNull(message="Usuario registro requerido")
	@Column(name="USUARIO_REGISTRO",nullable=false)
	public String getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

}