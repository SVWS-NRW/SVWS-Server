package de.nrw.schule.svws.db.dto.rev8.views.statkue;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbank-View Statkue_Organisationsform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOStatkueOrganisationsformPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die Schulform für welche die Organisationsform zur Verfügung steht */
	public String SF;

	/** Das Kürzel der Organisationsform */
	public String OrgForm;

	/** Eine Einschränkung auf einen Förderschwerpunkt (hier nur Kompatibilität angegeben) */
	public String FSP;

	/** Flag - zur Kompatibilität (hier 1) */
	public String Flag;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueOrganisationsformPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOStatkueOrganisationsformPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueOrganisationsformPK.
	 * @param SF   der Wert für das Attribut SF
	 * @param OrgForm   der Wert für das Attribut OrgForm
	 * @param FSP   der Wert für das Attribut FSP
	 * @param Flag   der Wert für das Attribut Flag
	 */
	public Rev8DTOStatkueOrganisationsformPK(final String SF, final String OrgForm, final String FSP, final String Flag) {
		if (SF == null) { 
			throw new NullPointerException("SF must not be null");
		}
		this.SF = SF;
		if (OrgForm == null) { 
			throw new NullPointerException("OrgForm must not be null");
		}
		this.OrgForm = OrgForm;
		if (FSP == null) { 
			throw new NullPointerException("FSP must not be null");
		}
		this.FSP = FSP;
		if (Flag == null) { 
			throw new NullPointerException("Flag must not be null");
		}
		this.Flag = Flag;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStatkueOrganisationsformPK other = (Rev8DTOStatkueOrganisationsformPK) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;

		if (OrgForm == null) {
			if (other.OrgForm != null)
				return false;
		} else if (!OrgForm.equals(other.OrgForm))
			return false;

		if (FSP == null) {
			if (other.FSP != null)
				return false;
		} else if (!FSP.equals(other.FSP))
			return false;

		if (Flag == null) {
			if (other.Flag != null)
				return false;
		} else if (!Flag.equals(other.Flag))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SF == null) ? 0 : SF.hashCode());

		result = prime * result + ((OrgForm == null) ? 0 : OrgForm.hashCode());

		result = prime * result + ((FSP == null) ? 0 : FSP.hashCode());

		result = prime * result + ((Flag == null) ? 0 : Flag.hashCode());
		return result;
	}
}