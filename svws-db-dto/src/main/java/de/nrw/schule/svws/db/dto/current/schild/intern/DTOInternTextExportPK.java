package de.nrw.schule.svws.db.dto.current.schild.intern;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbanktabelle Schildintern_TextExport.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class DTOInternTextExportPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Schildintern Tabelle: Excel_CSV_Export */
	public String DatenartKrz;

	/** Schildintern Tabelle: Excel_CSV_Export */
	public String Feldname;

	/** Schildintern Tabelle: Excel_CSV_Export */
	public String AnzeigeText;

	/** Schildintern Tabelle: Excel_CSV_Export */
	public String DBFormat;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternTextExportPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOInternTextExportPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternTextExportPK.
	 * @param DatenartKrz   der Wert für das Attribut DatenartKrz
	 * @param Feldname   der Wert für das Attribut Feldname
	 * @param AnzeigeText   der Wert für das Attribut AnzeigeText
	 * @param DBFormat   der Wert für das Attribut DBFormat
	 */
	public DTOInternTextExportPK(final String DatenartKrz, final String Feldname, final String AnzeigeText, final String DBFormat) {
		if (DatenartKrz == null) { 
			throw new NullPointerException("DatenartKrz must not be null");
		}
		this.DatenartKrz = DatenartKrz;
		if (Feldname == null) { 
			throw new NullPointerException("Feldname must not be null");
		}
		this.Feldname = Feldname;
		if (AnzeigeText == null) { 
			throw new NullPointerException("AnzeigeText must not be null");
		}
		this.AnzeigeText = AnzeigeText;
		if (DBFormat == null) { 
			throw new NullPointerException("DBFormat must not be null");
		}
		this.DBFormat = DBFormat;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOInternTextExportPK other = (DTOInternTextExportPK) obj;
		if (DatenartKrz == null) {
			if (other.DatenartKrz != null)
				return false;
		} else if (!DatenartKrz.equals(other.DatenartKrz))
			return false;

		if (Feldname == null) {
			if (other.Feldname != null)
				return false;
		} else if (!Feldname.equals(other.Feldname))
			return false;

		if (AnzeigeText == null) {
			if (other.AnzeigeText != null)
				return false;
		} else if (!AnzeigeText.equals(other.AnzeigeText))
			return false;

		if (DBFormat == null) {
			if (other.DBFormat != null)
				return false;
		} else if (!DBFormat.equals(other.DBFormat))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DatenartKrz == null) ? 0 : DatenartKrz.hashCode());

		result = prime * result + ((Feldname == null) ? 0 : Feldname.hashCode());

		result = prime * result + ((AnzeigeText == null) ? 0 : AnzeigeText.hashCode());

		result = prime * result + ((DBFormat == null) ? 0 : DBFormat.hashCode());
		return result;
	}
}