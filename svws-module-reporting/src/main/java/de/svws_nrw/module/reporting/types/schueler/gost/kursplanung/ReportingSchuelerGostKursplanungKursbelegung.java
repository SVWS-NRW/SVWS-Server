package de.svws_nrw.module.reporting.types.schueler.gost.kursplanung;

import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.gost.kursplanung.ReportingGostKursplanungKurs;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungKurs.
 */
public class ReportingSchuelerGostKursplanungKursbelegung extends ReportingBaseType {

	/** Nummer des Abiturfaches, sofern das Fach des Kurses ein Abiturfach des Schülers ist. */
	protected String abiturfach;

	/** Gibt an, ob der Schüler in diesem Kurs einen gleichwertigen komplexen Leistungsnachweis (GKL) gewählt hat. */
	protected boolean hatGkl;

	/** Gibt an, ob die Kursbelegung eine gültige Fachwahl im Abiturjahrgang hat. */
	protected boolean hatGueltigeFachwahl;

	/** Gibt an, ob der Kurs schriftlich belegt ist. */
	protected boolean istSchriftlich;

	/** Der Kurs, der vom Schüler belegt wird. */
	protected ReportingGostKursplanungKurs kurs;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param abiturfach			Abiturfach, falls das Fach des Kurses Abiturfach ist.
	 * @param hatGkl				Angabe, ob der Schüler in diesem Kurs einen GKL gewählt hat.
	 * @param hatGueltigeFachwahl 	Angabe, ob die Kursbelegung eine gültige Fachwahl im Abiturjahrgang hat.
	 * @param istSchriftlich		Angabe, ob der Kurs schriftlich belegt ist.
	 * @param kurs 					Der Kurs, der vom Schüler belegt wird.
	 */
	public ReportingSchuelerGostKursplanungKursbelegung(final String abiturfach, final boolean hatGkl, final boolean hatGueltigeFachwahl,
			final boolean istSchriftlich, final ReportingGostKursplanungKurs kurs) {
		this.abiturfach = ersetzeNullBlankTrim(abiturfach);
		this.hatGkl = hatGkl;
		this.hatGueltigeFachwahl = hatGueltigeFachwahl;
		this.istSchriftlich = istSchriftlich;
		this.kurs = kurs;
	}



	// ##### Getter #####

	/**
	 * Nummer des Abiturfaches, sofern das Fach des Kurses ein Abiturfach des Schülers ist.
	 *
	 * @return Inhalt des Feldes abiturfach; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String abiturfach() {
		return abiturfach;
	}

	/**
	 * Gibt an, ob der Schüler in diesem Kurs einen gleichwertigen komplexen Leistungsnachweis (GKL) gewählt hat.
	 *
	 * @return Inhalt des Feldes hatGkl
	 */
	public boolean hatGkl() {
		return hatGkl;
	}

	/**
	 * Gibt an, ob die Kursbelegung eine gültige Fachwahl im Abiturjahrgang hat.
	 *
	 * @return Inhalt des Feldes hatGueltigeFachwahl
	 */
	public boolean hatGueltigeFachwahl() {
		return hatGueltigeFachwahl;
	}

	/**
	 * Gibt an, ob der Kurs schriftlich belegt ist.
	 *
	 * @return Inhalt des Feldes istSchriftlich
	 */
	public boolean istSchriftlich() {
		return istSchriftlich;
	}

	/**
	 * Das Kürzel der Schriftlichkeit für die Ausgabe. Alle Vorlagen nutzen es, damit sie die Belegung gleich darstellen. Eine eingetragene GKL hängt ein
	 * „+“ an, auch an einen mündlichen Kurs.
	 *
	 * @return "S" oder "M", mit angehängtem "+", wenn eine GKL eingetragen ist
	 */
	public String kuerzelSchriftlichkeit() {
		return (istSchriftlich ? "S" : "M") + (hatGkl ? "+" : "");
	}

	/**
	 * Gibt an, ob eine GKL eingetragen ist, die fachlich unzulässig ist. Eine GKL ist nur in einem schriftlichen Grundkurs zulässig. Die Vorlagen heben
	 * eine unzulässige GKL hervor.
	 *
	 * @return true, wenn eine GKL außerhalb eines schriftlichen Grundkurses eingetragen ist
	 */
	public boolean hatUnzulaessigeGkl() {
		return hatGkl && !(istSchriftlich && (kurs != null) && (kurs.gostKursart() == GostKursart.GK));
	}

	/**
	 * Der Kurs, der vom Schüler belegt wird.
	 *
	 * @return Inhalt des Feldes kurs; kann {@code null} sein, wenn der Belegung kein Kurs zugeordnet ist.
	 */
	public ReportingGostKursplanungKurs kurs() {
		return kurs;
	}

}
