package de.svws_nrw.module.reporting.types.schueler.erzieher;

import java.util.List;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Erzieher-Art-Gruppe.
 * Sie gruppiert verschiedene Erzieher gleicher Art zusammen, um sie als gemeinsamen Erzieher in Reports ansprechen zu können.
 */
public class ReportingErzieherArtGruppe extends ReportingBaseType {

	/** Bezeichnung der gruppierten Erzieher-Art. */
	protected String bezeichnung;

	/** Die Erzieher in dieser Gruppe. */
	protected List<ReportingErzieher> erzieher;

	/** id der gruppierten Erzieher-Art. */
	protected long id;

	/** Der Schüler, dem diese Erziehergruppe zugeordnet ist. */
	protected ReportingSchueler schueler;

	/** Der Wert der Sortierung der gruppierten Erzieher-Art. */
	protected Integer sortierung;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param bezeichnung	Bezeichnung der gruppierten Erzieher-Art.
	 * @param erzieher 		Die Erzieher in dieser Gruppe.
	 * @param id			Die id der gruppierten Erzieher-Art.
	 * @param schueler		Der Schüler, dem diese Erziehergruppe zugeordnet ist.
	 * @param sortierung 	Der Wert der Sortierung der gruppierten Erzieher-Art.
	 */
	public ReportingErzieherArtGruppe(final String bezeichnung, final List<ReportingErzieher> erzieher, final long id, final ReportingSchueler schueler,
			final Integer sortierung) {
		this.bezeichnung = bezeichnung;
		this.erzieher = erzieher;
		this.id = id;
		this.schueler = schueler;
		this.sortierung = sortierung;
	}

	// ##### Berechnete Felder #####

	/**
	 * Erzeugt die mehrzeilige Briefanschrift im html-Format.
	 *
	 * @return Briefanschrift (html)
	 */
	public String anschrift() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		final StringBuilder result = new StringBuilder();
		for (final ReportingErzieher e : erzieher()) {
			switch (e.anrede()) {
				case "Frau" -> result.append("Frau ").append(e.vornameNachname()).append("<br/>");
				case "Herr" -> result.append("Herrn ").append(e.vornameNachname()).append("<br/>");
				case "Familie" -> result.append("Familie").append(e.nachname()).append("<br/>");
				case null, default -> result.append(e.vornameNachname()).append("<br/>");
			}
		}
		result.append(!erzieher().getFirst().wohnortsteilname().isEmpty() ? ("OT " + erzieher().getFirst().wohnortsteilname() + "<br/>") : "");
		result.append(erzieher().getFirst().strassennameHausnummer()).append("<br/>");
		result.append(erzieher().getFirst().plzOrt());

		return result.toString();
	}

	/**
	 * Erzeugt die formale Anrede ("Sehr geehrte") des Erziehers.
	 *
	 * @return Formale Anrede
	 */
	public String briefanredeFormal() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		String result = erzieher().getFirst().briefanredeFormal();

		// Maximal ein zweiter Erzieher kann noch in der Gruppe sein.
		if (erzieher().size() > 1)
			result += "," + erzieher().getLast().briefanredeFormal().substring(0, 1).toLowerCase() + erzieher().getLast().briefanredeFormal().substring(1);

		return result;
	}

	/**
	 * Erzeugt die persönliche Anrede ("Liebe") des Erziehers.
	 *
	 * @return Persönliche Anrede
	 */
	public String briefanredePersoenlich() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		String result = erzieher().getFirst().briefanredePersoenlich();

		// Maximal ein zweiter Erzieher kann noch in der Gruppe sein.
		if (erzieher().size() > 1)
			result += "," + erzieher().getLast().briefanredePersoenlich().substring(0, 1).toLowerCase()
					+ erzieher().getLast().briefanredePersoenlich().substring(1);

		return result;
	}

	/**
	 * Gibt an, ob ein Erzieher in der Gruppe gemäß Beschreibung der volljährige Schüler selbst ist.
	 *
	 * @return true, wenn einer der Erzieher ein Schüler ist, sonst false
	 */
	public boolean istVolljaehrigerSchueler() {
		return ((this.erzieher.size() == 1) && (this.erzieher().getFirst().art.bezeichnung().toLowerCase().contains("schüler"))
				&& (this.erzieher().getFirst().art.bezeichnung().toLowerCase().contains("ist volljährig")));
	}

	/**
	 * Erzeugt den vollständigen Namen, Nachname zuerst.
	 *
	 * @return Vollständiger Name.
	 */
	public String nachnameVorname() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		String result = "";
		// Maximal zwei Erzieher können in einer Gruppe sein.
		if (erzieher().size() == 1)
			result = erzieher().getFirst().nachnameVorname();
		else {
			final ReportingErzieher e1 = erzieher().getFirst();
			final ReportingErzieher e2 = erzieher().getLast();
			if (e1.nachname().equals(e2.nachname()))
				result = e1.nachnameVorname() + " und " + e2.vorname();
			else {
				result = e1.nachnameVorname() + " und " + e2.nachnameVorname();
			}
		}

		return result;
	}

	/**
	 * Erzeugt den vollständigen Namen mit Titel, Nachname zuerst
	 *
	 * @return Vollständiger Name.
	 */
	public String nachnameVornameMitTitel() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		String result = "";
		// Maximal zwei Erzieher können in einer Gruppe sein.
		if (erzieher().size() == 1)
			result = erzieher().getFirst().nachnameVornameMitTitel();
		else {
			final ReportingErzieher e1 = erzieher().getFirst();
			final ReportingErzieher e2 = erzieher().getLast();
			if (e1.nachname().equals(e2.nachname()))
				result = e1.nachnameVornameMitTitel() + " und " + e2.vorname() + (!e2.titel().isEmpty() ? ", " + e2.titel() : "");
			else {
				result = e1.nachnameVornameMitTitel() + " und " + e2.nachnameVornameMitTitel();
			}
		}

		return result;
	}

	/**
	 * Erzeugt die Angabe der Postleitzahl.
	 *
	 * @return Postleitzahl
	 */
	public String plz() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		// Eine Erziehergruppe hat immer nur eine Adresse.
		return erzieher().getFirst().plz();
	}

	/**
	 * Erzeugt die Angabe von Postleitzahl und Wohnort.
	 *
	 * @return Postleitzahl und Wohnort
	 */
	public String plzOrt() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		// Eine Erziehergruppe hat immer nur eine Adresse.
		return erzieher().getFirst().plzOrt();
	}

	/**
	 * Erzeugt den geschlechtsspezifischen Ausdruck "Ihr Sohn/Ihre Tochter" im Nominativ.
	 *
	 * @return Geschlechtsspezifischer Ausdruck für "Ihr Sohn/Ihre Tochter" im Nominativ
	 */
	public String sohnTochterNominativ() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		// Eine Erziehergruppe hat immer nur eine Adresse.
		return erzieher().getFirst().sohnTochterNominativ();
	}

	/**
	 * Erzeugt den geschlechtsspezifischen Ausdruck "Ihr Sohn/Ihre Tochter" im Genitiv.
	 *
	 * @return Geschlechtsspezifischer Ausdruck für "Ihr Sohn/Ihre Tochter" im Genitiv
	 */
	public String sohnTochterGenitiv() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		// Eine Erziehergruppe hat immer nur eine Adresse.
		return erzieher().getFirst().sohnTochterGenitiv();
	}

	/**
	 * Erzeugt den geschlechtsspezifischen Ausdruck "Ihr Sohn/Ihre Tochter" im Dativ.
	 *
	 * @return Geschlechtsspezifischer Ausdruck für "Ihr Sohn/Ihre Tochter" im Dativ
	 */
	public String sohnTochterDativ() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		// Eine Erziehergruppe hat immer nur eine Adresse.
		return erzieher().getFirst().sohnTochterDativ();
	}

	/**
	 * Erzeugt den geschlechtsspezifischen Ausdruck "Ihr Sohn/Ihre Tochter" im Akkusativ.
	 *
	 * @return Geschlechtsspezifischer Ausdruck für "Ihr Sohn/Ihre Tochter" im Akkusativ
	 */
	public String sohnTochterAkkusativ() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		// Eine Erziehergruppe hat immer nur eine Adresse.
		return erzieher().getFirst().sohnTochterAkkusativ();
	}

	/**
	 * Erzeugt die Angabe von Straße und Hausnummer.
	 *
	 * @return Straße und Hausnummer
	 */
	public String strassennameHausnummer() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		// Eine Erziehergruppe hat immer nur eine Adresse.
		final ReportingErzieher e1 = erzieher().getFirst();

		if (e1.strassenname().isEmpty())
			return "";

		String result = e1.strassenname();
		result += !e1.hausnummer().isEmpty() ? (" " + e1.hausnummer()) : "";
		result += (!e1.hausnummer().isEmpty() && !e1.hausnummerZusatz().isEmpty()) ? (" " + e1.hausnummerZusatz()) : "";

		return result;
	}

	/**
	 * Erzeugt den vollständigen Namen, Vorname zuerst,
	 *
	 * @return Vollständiger Name.
	 */
	public String vornameNachname() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		String result = "";
		// Maximal zwei Erzieher können in einer Gruppe sein.
		if (erzieher().size() == 1)
			result = erzieher().getFirst().vornameNachname();
		else {
			final ReportingErzieher e1 = erzieher().getFirst();
			final ReportingErzieher e2 = erzieher().getLast();
			if (e1.nachname().equals(e2.nachname()))
				result = e1.vorname() + " und " + e2.vornameNachname();
			else {
				result = e1.vornameNachname() + " und " + e2.vornameNachname();
			}
		}

		return result;
	}

	/**
	 * Erzeugt den vollständigen Namen mit Titel, Vorname zuerst.
	 *
	 * @return Vollständiger Name.
	 */
	public String vornameNachnameMitTitel() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		String result = "";
		// Maximal zwei Erzieher können in einer Gruppe sein.
		if (erzieher().size() == 1)
			result = erzieher().getFirst().vornameNachnameMitTitel();
		else {
			final ReportingErzieher e1 = erzieher().getFirst();
			final ReportingErzieher e2 = erzieher().getLast();
			if (e1.nachname().equals(e2.nachname()))
				result = (e1.titel().isEmpty() ? e1.titel() + " " : "") + e1.vorname() + " und " + e2.vornameNachnameMitTitel();
			else {
				result = e1.vornameNachnameMitTitel() + " und " + e2.vornameNachnameMitTitel();
			}
		}

		return result;
	}

	/**
	 * Erzeugt den Namen des Wohnorts.
	 *
	 * @return Wohnortname
	 */
	public String wohnortname() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		// Eine Erziehergruppe hat immer nur eine Adresse.
		return erzieher().getFirst().wohnortname();
	}

	/**
	 * Erzeugt den Namen des Wohnortsteils.
	 *
	 * @return Wohnortsteilname
	 */
	public String wohnortsteilname() {
		if ((erzieher() == null) || erzieher().isEmpty())
			return "";

		// Eine Erziehergruppe hat immer nur eine Adresse.
		return erzieher().getFirst().wohnortsteilname();
	}


	// ##### Getter #####
	/**
	 * Bezeichnung der gruppierten Erzieher-Art.
	 *
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Die Erzieher in dieser Gruppe.
	 *
	 * @return Inhalt des Feldes erzieher
	 */
	public List<ReportingErzieher> erzieher() {
		return erzieher;
	}

	/**
	 * ID der gruppierten Erzieher-Art.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Der Schüler, dem diese Erziehergruppe zugeordnet ist.
	 *
	 * @return Inhalt des Feldes schueler
	 */
	public ReportingSchueler schueler() {
		return schueler;
	}

	/**
	 * Der Wert der Sortierung der gruppierten Erzieher-Art.
	 *
	 * @return Inhalt des Feldes sortierung
	 */
	public Integer sortierung() {
		return sortierung;
	}
}
