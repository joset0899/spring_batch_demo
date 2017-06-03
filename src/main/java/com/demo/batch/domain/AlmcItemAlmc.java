package com.demo.batch.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the almc_item_almc database table.
 * 
 */
@Entity
@Table(name="almc_item_almc")
@NamedQuery(name="AlmcItemAlmc.findAll", query="SELECT a FROM AlmcItemAlmc a")
public class AlmcItemAlmc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="COD_ITEM_ALMC")
	private int codItemAlmc;

	@Column(name="COD_UNID_MEDI")
	private int codUnidMedi;

	@Column(name="NUM_PREC_UNIT_ACTU")
	private BigDecimal numPrecUnitActu;

	@Column(name="NUM_PREC_UNIT_ANTE")
	private BigDecimal numPrecUnitAnte;

	@Column(name="NUM_STCK_ACTU")
	private BigDecimal numStckActu;

	@Column(name="NUM_STCK_MINI")
	private BigDecimal numStckMini;

	@Column(name="TXT_NOMB_ITEM_ALMC")
	private String txtNombItemAlmc;

	

	public AlmcItemAlmc() {
	}

	public int getCodItemAlmc() {
		return this.codItemAlmc;
	}

	public void setCodItemAlmc(int codItemAlmc) {
		this.codItemAlmc = codItemAlmc;
	}

	public int getCodUnidMedi() {
		return this.codUnidMedi;
	}

	public void setCodUnidMedi(int codUnidMedi) {
		this.codUnidMedi = codUnidMedi;
	}

	public BigDecimal getNumPrecUnitActu() {
		return this.numPrecUnitActu;
	}

	public void setNumPrecUnitActu(BigDecimal numPrecUnitActu) {
		this.numPrecUnitActu = numPrecUnitActu;
	}

	public BigDecimal getNumPrecUnitAnte() {
		return this.numPrecUnitAnte;
	}

	public void setNumPrecUnitAnte(BigDecimal numPrecUnitAnte) {
		this.numPrecUnitAnte = numPrecUnitAnte;
	}

	public BigDecimal getNumStckActu() {
		return this.numStckActu;
	}

	public void setNumStckActu(BigDecimal numStckActu) {
		this.numStckActu = numStckActu;
	}

	public BigDecimal getNumStckMini() {
		return this.numStckMini;
	}

	public void setNumStckMini(BigDecimal numStckMini) {
		this.numStckMini = numStckMini;
	}

	public String getTxtNombItemAlmc() {
		return this.txtNombItemAlmc;
	}

	public void setTxtNombItemAlmc(String txtNombItemAlmc) {
		this.txtNombItemAlmc = txtNombItemAlmc;
	}



}