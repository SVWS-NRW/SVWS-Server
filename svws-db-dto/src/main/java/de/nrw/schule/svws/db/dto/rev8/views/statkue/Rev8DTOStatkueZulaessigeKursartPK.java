package de.nrw.schule.svws.db.dto.rev8.views.statkue;

import java.io.Serializable;

/**
 * Diese Klasse dient als DTO für den Primärschlüssel der Datenbank-View Statkue_ZulKuArt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
public class Rev8DTOStatkueZulaessigeKursartPK implements Serializable {

	/** Die UID für diese Klasse */
	private static final long serialVersionUID = 1L;

	/** Die Schulform bei der die Kursart zulässig ist. */
	public String SF;

	/** Eine Einschränkung der Zulässigkeit der Kusart auf einen Förderschwerpunkt (hier nur Kompatibilität angegeben) */
	public String FSP;

	/** Eine Einschränkung der Zulässigkeit der Kursart auf einen Bildungsgang */
	public String BG;

	/** Der numerische Schlüssel für die amtliche Schulstatistik */
	public String Kursart;

	/** Das Kürzel der Kursart */
	public String Kursart2;

	/** Die Bezeichnung der Kursart */
	public String Bezeichnung;

	/** Zur Kompatibilität vorhanden */
	public Integer SGLBereich;

	/** Flag - zur Kompatibilität (hier 1) */
	public String Flag;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueZulaessigeKursartPK ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOStatkueZulaessigeKursartPK() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueZulaessigeKursartPK.
	 * @param SF   der Wert für das Attribut SF
	 * @param FSP   der Wert für das Attribut FSP
	 * @param BG   der Wert für das Attribut BG
	 * @param Kursart   der Wert für das Attribut Kursart
	 * @param Kursart2   der Wert für das Attribut Kursart2
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param SGLBereich   der Wert für das Attribut SGLBereich
	 * @param Flag   der Wert für das Attribut Flag
	 */
	public Rev8DTOStatkueZulaessigeKursartPK(final String SF, final String FSP, final String BG, final String Kursart, final String Kursart2, final String Bezeichnung, final Integer SGLBereich, final String Flag) {
		if (SF == null) { 
			throw new NullPointerException("SF must not be null");
		}
		this.SF = SF;
		if (FSP == null) { 
			throw new NullPointerException("FSP must not be null");
		}
		this.FSP = FSP;
		if (BG == null) { 
			throw new NullPointerException("BG must not be null");
		}
		this.BG = BG;
		if (Kursart == null) { 
			throw new NullPointerException("Kursart must not be null");
		}
		this.Kursart = Kursart;
		if (Kursart2 == null) { 
			throw new NullPointerException("Kursart2 must not be null");
		}
		this.Kursart2 = Kursart2;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (SGLBereich == null) { 
			throw new NullPointerException("SGLBereich must not be null");
		}
		this.SGLBereich = SGLBereich;
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
		Rev8DTOStatkueZulaessigeKursartPK other = (Rev8DTOStatkueZulaessigeKursartPK) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;

		if (FSP == null) {
			if (other.FSP != null)
				return false;
		} else if (!FSP.equals(other.FSP))
			return false;

		if (BG == null) {
			if (other.BG != null)
				return false;
		} else if (!BG.equals(other.BG))
			return false;

		if (Kursart == null) {
			if (other.Kursart != null)
				return false;
		} else if (!Kursart.equals(other.Kursart))
			return false;

		if (Kursart2 == null) {
			if (other.Kursart2 != null)
				return false;
		} else if (!Kursart2.equals(other.Kursart2))
			return false;

		if (Bezeichnung == null) {
			if (other.Bezeichnung != null)
				return false;
		} else if (!Bezeichnung.equals(other.Bezeichnung))
			return false;

		if (SGLBereich == null) {
			if (other.SGLBereich != null)
				return false;
		} else if (!SGLBereich.equals(other.SGLBereich))
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

		result = prime * result + ((FSP == null) ? 0 : FSP.hashCode());

		result = prime * result + ((BG == null) ? 0 : BG.hashCode());

		result = prime * result + ((Kursart == null) ? 0 : Kursart.hashCode());

		result = prime * result + ((Kursart2 == null) ? 0 : Kursart2.hashCode());

		result = prime * result + ((Bezeichnung == null) ? 0 : Bezeichnung.hashCode());

		result = prime * result + ((SGLBereich == null) ? 0 : SGLBereich.hashCode());

		result = prime * result + ((Flag == null) ? 0 : Flag.hashCode());
		return result;
	}
}