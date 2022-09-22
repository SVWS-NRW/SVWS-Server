package de.nrw.schule.svws.db.dto.rev8.schulver;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Schulver_WeitereSF.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOSchulverWeitereSFPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Schulver Tabelle IT.NRW: Schulnummer der Schule */
	public String SNR;

	/** Schulver Tabelle IT.NRW: Gliederung Statistikkürzel */
	public String SGL;

	/** Schulver Tabelle IT.NRW: Förderschwerpunkt ASD-Kürzel */
	public String FSP;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchulverWeitereSFPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOSchulverWeitereSFPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchulverWeitereSFPK.
	 * @param SNR   der Wert für das Attribut SNR
	 * @param SGL   der Wert für das Attribut SGL
	 * @param FSP   der Wert für das Attribut FSP
	 */
	public Rev8DTOSchulverWeitereSFPK(final String SNR, final String SGL, final String FSP) {
		if (SNR == null) { 
			throw new NullPointerException("SNR must not be null");
		}
		this.SNR = SNR;
		if (SGL == null) { 
			throw new NullPointerException("SGL must not be null");
		}
		this.SGL = SGL;
		if (FSP == null) { 
			throw new NullPointerException("FSP must not be null");
		}
		this.FSP = FSP;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOSchulverWeitereSFPK other = (Rev8DTOSchulverWeitereSFPK) obj;
		if (SNR == null) {
			if (other.SNR != null)
				return false;
		} else if (!SNR.equals(other.SNR))
			return false;

		if (SGL == null) {
			if (other.SGL != null)
				return false;
		} else if (!SGL.equals(other.SGL))
			return false;

		if (FSP == null) {
			if (other.FSP != null)
				return false;
		} else if (!FSP.equals(other.FSP))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SNR == null) ? 0 : SNR.hashCode());

		result = prime * result + ((SGL == null) ? 0 : SGL.hashCode());

		result = prime * result + ((FSP == null) ? 0 : FSP.hashCode());
		return result;
	}
}