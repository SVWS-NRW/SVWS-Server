package de.nrw.schule.svws.db.dto.migration.schild.intern;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Schildintern_Zusatzinfos.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class MigrationDTOInternZusatzinfosPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	public String SGL_BKAbschl;

	/** Schildintern Tabelle: für den WinSchildImport ??? DEPRECATED */
	public String JG_BKAbschl;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternZusatzinfosPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternZusatzinfosPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternZusatzinfosPK.
	 * @param SGL_BKAbschl   der Wert für das Attribut SGL_BKAbschl
	 * @param JG_BKAbschl   der Wert für das Attribut JG_BKAbschl
	 */
	public MigrationDTOInternZusatzinfosPK(final String SGL_BKAbschl, final String JG_BKAbschl) {
		if (SGL_BKAbschl == null) { 
			throw new NullPointerException("SGL_BKAbschl must not be null");
		}
		this.SGL_BKAbschl = SGL_BKAbschl;
		if (JG_BKAbschl == null) { 
			throw new NullPointerException("JG_BKAbschl must not be null");
		}
		this.JG_BKAbschl = JG_BKAbschl;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOInternZusatzinfosPK other = (MigrationDTOInternZusatzinfosPK) obj;
		if (SGL_BKAbschl == null) {
			if (other.SGL_BKAbschl != null)
				return false;
		} else if (!SGL_BKAbschl.equals(other.SGL_BKAbschl))
			return false;

		if (JG_BKAbschl == null) {
			if (other.JG_BKAbschl != null)
				return false;
		} else if (!JG_BKAbschl.equals(other.JG_BKAbschl))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SGL_BKAbschl == null) ? 0 : SGL_BKAbschl.hashCode());

		result = prime * result + ((JG_BKAbschl == null) ? 0 : JG_BKAbschl.hashCode());
		return result;
	}
}