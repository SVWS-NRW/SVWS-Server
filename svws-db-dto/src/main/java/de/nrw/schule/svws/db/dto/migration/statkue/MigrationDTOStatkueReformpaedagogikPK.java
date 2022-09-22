package de.nrw.schule.svws.db.dto.migration.statkue;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Statkue_Reformpaedagogik.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOStatkueReformpaedagogikPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Statkue Tabelle IT.NRW: Statstikkürzel Reformpädagogik */
	public String RPG;

	/** Statkue Tabelle IT.NRW: zulässige Schulform für Reformpädagogik */
	public String SF;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStatkueReformpaedagogikPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOStatkueReformpaedagogikPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStatkueReformpaedagogikPK.
	 * @param RPG   der Wert für das Attribut RPG
	 * @param SF   der Wert für das Attribut SF
	 */
	public MigrationDTOStatkueReformpaedagogikPK(final String RPG, final String SF) {
		if (RPG == null) { 
			throw new NullPointerException("RPG must not be null");
		}
		this.RPG = RPG;
		if (SF == null) { 
			throw new NullPointerException("SF must not be null");
		}
		this.SF = SF;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOStatkueReformpaedagogikPK other = (MigrationDTOStatkueReformpaedagogikPK) obj;
		if (RPG == null) {
			if (other.RPG != null)
				return false;
		} else if (!RPG.equals(other.RPG))
			return false;

		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((RPG == null) ? 0 : RPG.hashCode());

		result = prime * result + ((SF == null) ? 0 : SF.hashCode());
		return result;
	}
}