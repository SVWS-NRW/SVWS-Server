package de.svws_nrw.core.utils.enm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.NoteKatalogEintrag;
import de.svws_nrw.asd.data.schule.FoerderschwerpunktKatalogEintrag;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.enm.ENMAnkreuzkompetenz;
import de.svws_nrw.core.data.enm.ENMDaten;
import de.svws_nrw.core.data.enm.ENMFach;
import de.svws_nrw.core.data.enm.ENMFoerderschwerpunkt;
import de.svws_nrw.core.data.enm.ENMJahrgang;
import de.svws_nrw.core.data.enm.ENMKlasse;
import de.svws_nrw.core.data.enm.ENMLehrer;
import de.svws_nrw.core.data.enm.ENMLeistung;
import de.svws_nrw.core.data.enm.ENMLerngruppe;
import de.svws_nrw.core.data.enm.ENMNote;
import de.svws_nrw.core.data.enm.ENMSchueler;
import de.svws_nrw.core.data.enm.ENMSchuelerAnkreuzkompetenz;
import de.svws_nrw.core.data.enm.ENMTeilleistung;
import de.svws_nrw.core.data.enm.ENMTeilleistungsart;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dien dem Verwalten von ENM-Daten (siehe auch {@link ENMDaten}).
 */
public class ENMDatenManager {

	/** Die ENM-Daten, die von diesem Daten-Manager verwaltet werden. */
	public final @NotNull ENMDaten daten;


	/** Temporäre Map für das Befüllen der ENMLehrer-Vektors.*/
	private final @NotNull Map<Long, ENMLehrer> mapLehrer = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMSchueler-Vektors.*/
	private final @NotNull Map<Long, ENMSchueler> mapSchueler = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMFach-Vektors.*/
	private final @NotNull Map<Long, ENMFach> mapFaecher = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMFach-Vektors.*/
	private final @NotNull Map<String, ENMFach> mapFaecherByKuerzel = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMJahrgang-Vektors.*/
	private final @NotNull Map<Long, ENMJahrgang> mapJahrgaenge = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMKlasse-Vektors.*/
	private final @NotNull Map<Long, ENMKlasse> mapKlassen = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMTeilleistungsarten-Vektors.*/
	private final @NotNull Map<Long, ENMTeilleistungsart> mapTeilleistungsarten = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMAnkreuzkompetenz-Vektors.*/
	private final @NotNull Map<Long, ENMAnkreuzkompetenz> mapAnkreuzkompetenzen = new HashMap<>();

	/** Zählt die Id der Lerngruppe hoch. */
	private long lerngruppenIDZaehler = 1;

	/** Temporäre Map für die Lerngruppen. */
	private final @NotNull Map<String, ENMLerngruppe> mapLerngruppen = new HashMap<>();


	/**
	 * Erzeugt einen neuen ENM-Daten-Manager mit leeren ENM-Daten.
	 *
	 * @param lehrerID   die ID des Lehrers für welchen die ENM-Daten erzeugt werden oder null für alle Lehrer
	 */
	public ENMDatenManager(final Long lehrerID) {
		this.daten = new ENMDaten();
		this.daten.lehrerID = lehrerID;
	}


	/**
	 * Erzeugt einen neuen ENM-Daten-Manager für die übergebenen Daten.
	 *
	 * @param daten   die ENM-Daten
	 */
	public ENMDatenManager(final @NotNull ENMDaten daten) {
		this.daten = daten;
	}


	/**
	 * Setzt die grundlegenden Daten zur Schule und zu dem Schuljahresabschnitts für welchen
	 * die ENM-Daten generiert wurden.
	 *
	 * @param schulnummer                 die Schulnummer
	 * @param schuljahr                   das Schuljahr
	 * @param anzahlAbschnitte            die Anzahl der Abschnitte an der Schule (2: Halbjahrsmodus, 4: Quartalsmodus)
	 * @param abschnitt                   die Nummer des Abschnittes im Schuljahr
	 * @param publicKey                   der öffentlichen Schlüssel, welcher für die Verschlüsselung und den
	 *                                    Rückversand der Datei genutzt werden soll
	 * @param fehlstundenEingabe          gibt an, ob die Fehlstunden-Eingabe durch das externe Notenmodul erlaubt ist
	 *                                    oder nicht
	 * @param fehlstundenSIFachbezogen    gibt an, ob die Fehlstunden für die Sekundarstufe I fachbezogen eingetragen
	 *                                    werden oder nicht
	 * @param fehlstundenSIIFachbezogen   gibt an, ob die Fehlstunden für die Sekundarstufe II fachbezogen eingetragen
	 *                                    werden oder nicht
	 * @param schulform                   das Kürzel der Schulform der Schule
	 * @param mailadresse                 gibt die Mailadresse an, an welche die verschlüsselte Datei zurückgesendet werden soll (z.B. mail@schule.nrw.de)
	 */
	public void setSchuldaten(final int schulnummer, final int schuljahr, final int anzahlAbschnitte, final int abschnitt, final String publicKey,
			final boolean fehlstundenEingabe, final boolean fehlstundenSIFachbezogen, final boolean fehlstundenSIIFachbezogen,
			final @NotNull String schulform, final String mailadresse) {
		daten.schulnummer = schulnummer;
		daten.schuljahr = schuljahr;
		daten.anzahlAbschnitte = anzahlAbschnitte;
		daten.aktuellerAbschnitt = abschnitt;
		daten.publicKey = publicKey;
		daten.fehlstundenEingabe = fehlstundenEingabe;
		daten.fehlstundenSIFachbezogen = fehlstundenSIFachbezogen;
		daten.fehlstundenSIIFachbezogen = fehlstundenSIIFachbezogen;
		daten.schulform = schulform;
		daten.mailadresse = mailadresse;
	}


	/**
	 * Setzt die Informationen zu den Texten der einzelnen Kompetenzstufen für Ankreuzkompetenzen.
	 *
	 * @param stufe1     der Text für die Stufe 1
	 * @param stufe2     der Text für die Stufe 2
	 * @param stufe3     der Text für die Stufe 3
	 * @param stufe4     der Text für die Stufe 4
	 * @param stufe5     der Text für die Stufe 5
	 * @param sonstige   der Text für die frei definierbare Zeugnisrubrik "Sonstiges"
	 */
	public void setAnkreuzkompetenzenStufen(final String stufe1, final String stufe2, final String stufe3, final String stufe4, final String stufe5,
			final String sonstige) {
		daten.ankreuzkompetenzen.textStufen[0] = stufe1;
		daten.ankreuzkompetenzen.textStufen[1] = stufe2;
		daten.ankreuzkompetenzen.textStufen[2] = stufe3;
		daten.ankreuzkompetenzen.textStufen[3] = stufe4;
		daten.ankreuzkompetenzen.textStufen[4] = stufe5;
		daten.ankreuzkompetenzen.textSonstiges = sonstige;
	}


	/**
	 * Fügt alle Noten des Core-Type {@link Note} zu dem Noten-Katalog der ENM-Datei hinzu.
	 *
	 * @param schuljahr   das Schuljahr, für welches die ENM-Datei erzeugt wird
	 */
	public void addNoten(final int schuljahr) {
		if (!daten.noten.isEmpty())
			return;
		final @NotNull List<Note> noten = Note.data().getWerteBySchuljahr(schuljahr);
		for (final @NotNull Note note : noten) {
			final NoteKatalogEintrag nke = note.daten(schuljahr);
			if (nke == null)
				continue;
			final @NotNull ENMNote enmNote = new ENMNote();
			enmNote.id = (int) nke.id;
			enmNote.kuerzel = nke.kuerzel;
			enmNote.notenpunkte = nke.notenpunkte;
			enmNote.text = nke.text;
			daten.noten.add(enmNote);
		}
	}


	/**
	 * Fügt alle Förderschwerpunkte des Core-Type {@link Foerderschwerpunkt} zu dem
	 * Förderschwerpunkt-Katalog der ENM-Datei hinzu.
	 *
	 * @param schuljahr   das Schuljahr, für welches die ENM-Datei erzeugt wird
	 * @param schulform   die Schulform, für welche die zulässigen Förderschwerpunkte
	 *                    zurückgegeben werden
	 */
	public void addFoerderschwerpunkte(final int schuljahr, final @NotNull Schulform schulform) {
		if (!daten.foerderschwerpunkte.isEmpty())
			return;
		final @NotNull List<Foerderschwerpunkt> foerderschwerpunkte = Foerderschwerpunkt.getBySchuljahrAndSchulform(schuljahr, schulform);
		for (final Foerderschwerpunkt foerderschwerpunkt : foerderschwerpunkte) {
			final FoerderschwerpunktKatalogEintrag fske = foerderschwerpunkt.daten(schuljahr);
			if (fske == null)
				continue;
			final ENMFoerderschwerpunkt enmFoerderschwerpunkt = new ENMFoerderschwerpunkt();
			enmFoerderschwerpunkt.id = fske.id;
			enmFoerderschwerpunkt.kuerzel = fske.kuerzel;
			enmFoerderschwerpunkt.beschreibung = fske.text;
			daten.foerderschwerpunkte.add(enmFoerderschwerpunkt);
		}
	}


	/**
	 * Fügt einen Lehrer hinzu und überprüft dabei, ob der Lehrer schon in der Liste vorhanden ist.
	 *
	 * @param id            die eindeutige ID des Lehrers
	 * @param kuerzel       das Kürzel des Lehrers
	 * @param nachname      der Nachname des Lehrers
	 * @param vorname       der Vorname des Lehrers
	 * @param geschlecht        das Geschlecht des Lehrers
	 * @param eMailDienstlich   die Dienst-Email-Adresse des Lehrers
	 *
	 * @return true, falls der Lehrer hinzugefügt wurde, ansonsten false
	 */
	public boolean addLehrer(final long id, final String kuerzel, final String nachname, final String vorname, final @NotNull Geschlecht geschlecht,
			final String eMailDienstlich) {
		if (mapLehrer.get(id) != null)
			return false;
		final @NotNull ENMLehrer enmLehrer = new ENMLehrer();
		enmLehrer.id = id;
		enmLehrer.kuerzel = kuerzel;
		enmLehrer.nachname = nachname;
		enmLehrer.vorname = vorname;
		enmLehrer.geschlecht = geschlecht.kuerzel;
		enmLehrer.eMailDienstlich = eMailDienstlich;
		daten.lehrer.add(enmLehrer);
		mapLehrer.put(id, enmLehrer);
		return true;
	}


	/**
	 * Fügt einen Schueler hinzu und überprüft dabei, ob der Schueler schon in der Liste vorhanden ist.
	 *
	 * @param id                  die ID des Schülers in der SVWS-DB
	 * @param jahrgangID          die ID des aktuellen Jahrgangs, in dem sich der Schüler befindet
	 * @param klasseID            die ID der aktuellen Klasse, in der sich der Schüler befindet
	 * @param nachname            der Nachname des Schülers (z.B. Mustermann)
	 * @param vorname             der Vorname des Schülers (z.B. Max)
	 * @param geschlecht          das Geschlecht des Schülers
	 * @param bilingualeSprache   gibt an, ob sich der Schüler aktuell im bilingualen Bildungsgang befindet
	 *                            (wenn ja, z.B. F) oder nicht (null)
	 * @param istZieldifferent    gibt an, ob der Schüler Ziel-different unterrichtet wird
	 * @param istDaZFoerderung    gibt an, ob der Schüler Deutsch-Förderung mit Deutsch als Zweitsprache (DaZ)
	 *                            bekommt (Seiteneinsteiger, z.B. Flüchtlingskinder)
	 *
	 * @return true, falls der Schueler hinzugefügt wurde, ansonsten false
	 */
	public boolean addSchueler(final long id, final long jahrgangID, final long klasseID, final String nachname, final String vorname,
			final @NotNull Geschlecht geschlecht,
			final String bilingualeSprache, final boolean istZieldifferent, final boolean istDaZFoerderung) {
		if (mapSchueler.get(id) != null)
			return false;
		final @NotNull ENMSchueler enmSchueler = new ENMSchueler();
		enmSchueler.id = id;
		enmSchueler.jahrgangID = jahrgangID;
		enmSchueler.klasseID = klasseID;
		enmSchueler.nachname = nachname;
		enmSchueler.vorname = vorname;
		enmSchueler.geschlecht = geschlecht.kuerzel;
		enmSchueler.bilingualeSprache = bilingualeSprache;
		enmSchueler.istZieldifferent = istZieldifferent;
		enmSchueler.istDaZFoerderung = istDaZFoerderung;
		daten.schueler.add(enmSchueler);
		mapSchueler.put(id, enmSchueler);
		return true;
	}


	/**
	 * Fügt ein Fach hinzu und überprüft dabei, ob das Fach schon in der Liste vorhanden ist.
	 *
	 * @param id                die eindeutige ID des Faches
	 * @param kuerzel           das Kürzel des Faches, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. D)
	 * @param kuerzelAnzeige    das Kürzel des Faches, wie es im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. D)
	 * @param sortierung        die Reihenfolge des Faches bei der Sortierung der Fächer. (z.B. 37)
	 * @param istFremdsprache   gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht
	 *
	 * @return true, falls das Fach hinzugefügt wurde, ansonsten false
	 */
	public boolean addFach(final long id, final @NotNull String kuerzel, final @NotNull String kuerzelAnzeige, final int sortierung,
			final boolean istFremdsprache) {
		if (mapFaecher.get(id) != null)
			return false;
		final @NotNull ENMFach enmFach = new ENMFach();
		enmFach.id = id;
		enmFach.kuerzel = kuerzel;
		enmFach.kuerzelAnzeige = kuerzelAnzeige;
		enmFach.sortierung = sortierung;
		enmFach.istFremdsprache = istFremdsprache;
		daten.faecher.add(enmFach);
		mapFaecher.put(id, enmFach);
		mapFaecherByKuerzel.put(kuerzelAnzeige, enmFach);
		return true;
	}


	/**
	 * Fügt einen Jahrgang hinzu und überprüft dabei, ob der Jahrgang schon in der Liste vorhanden ist.
	 *
	 * @param id                die eindeutige ID des Jahrganges
	 * @param kuerzel           das Kürzel des Jahrgangs, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. EF)
	 * @param kuerzelAnzeige    das Kürzel des Jahrgangs, wie es im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. EF)
	 * @param beschreibung      die textuelle Bezeichnung des Jahrgangs. (z.B. Einführungsphase)
	 * @param stufe             die Stufe des Jahrgangs. (z.B. PR, SI, nur Berufskolleg: SII, Berufskolleg Anlage D und GOSt: SII-1, SII-2, SII-3)
	 * @param sortierung        die Reihenfolge des Jahrgangs bei der Sortierung der Jahrgänge. (z.B. 8)
	 *
	 * @return true, falls der Jahrgang hinzugefügt wurde, ansonsten false
	 */
	public boolean addJahrgang(final long id, final String kuerzel, final String kuerzelAnzeige, final String beschreibung, final String stufe,
			final int sortierung) {
		if (mapJahrgaenge.get(id) != null)
			return false;
		final @NotNull ENMJahrgang enmJahrgang = new ENMJahrgang();
		enmJahrgang.id = id;
		enmJahrgang.kuerzel = kuerzel;
		enmJahrgang.kuerzelAnzeige = kuerzelAnzeige;
		enmJahrgang.beschreibung = beschreibung;
		enmJahrgang.stufe = stufe;
		enmJahrgang.sortierung = sortierung;
		daten.jahrgaenge.add(enmJahrgang);
		mapJahrgaenge.put(id, enmJahrgang);
		return true;
	}


	/**
	 * Fügt eine Klasse hinzu und überprüft dabei, ob die Klasse schon in der Liste vorhanden ist.
	 *
	 * @param id                die eindeutige ID der Klasse
	 * @param kuerzel           das Kürzel der Klasse, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. EF)
	 * @param kuerzelAnzeige    das Kürzel der Klasse, wie es im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. EF)
	 * @param idJahrgang        die ID des Jahrgangs oder null bei jahrgangsübergreifenden Klassen
	 * @param sortierung        die Reihenfolge der Klasse bei der Sortierung der Klassen. (z.B. 8)
	 *
	 * @return true, falls die Klasse hinzugefügt wurde, ansonsten false
	 */
	public boolean addKlasse(final long id, final String kuerzel, final String kuerzelAnzeige, final Long idJahrgang, final int sortierung) {
		if (mapKlassen.get(id) != null)
			return false;
		final @NotNull ENMKlasse enmKlasse = new ENMKlasse();
		enmKlasse.id = id;
		enmKlasse.kuerzel = kuerzel;
		enmKlasse.kuerzelAnzeige = kuerzelAnzeige;
		enmKlasse.idJahrgang = idJahrgang;
		enmKlasse.sortierung = sortierung;
		daten.klassen.add(enmKlasse);
		mapKlassen.put(id, enmKlasse);
		return true;
	}


	/**
	 * Fügt eine Teilleistungsart hinzu und überprüft dabei, ob die Art schon in der Liste vorhanden ist.
	 *
	 * @param id            die eindeutige ID der Teilleistungsart
	 * @param bezeichnung   die Bezeichnung der Teilleistungsart
	 * @param sortierung    die Reihenfolge der Art bei der Sortierung der Arten. (z.B. 8)
	 * @param gewichtung    die Gewichtung der Art
	 *
	 * @return true, falls der Jahrgang hinzugefügt wurde, ansonsten false
	 */
	public boolean addTeilleistungsart(final long id, final String bezeichnung, final int sortierung, final double gewichtung) {
		if (mapTeilleistungsarten.get(id) != null)
			return false;
		final @NotNull ENMTeilleistungsart enmArt = new ENMTeilleistungsart();
		enmArt.id = id;
		enmArt.bezeichnung = bezeichnung;
		enmArt.sortierung = sortierung;
		enmArt.gewichtung = gewichtung;
		daten.teilleistungsarten.add(enmArt);
		mapTeilleistungsarten.put(id, enmArt);
		return true;
	}


	/**
	 * Fügt eine Ankreuzkompetenz zum Katalog hinzu und überprüft dabei, ob sie schon in der Liste vorhanden ist.
	 *
	 * @param id                 die eindeutige ID der Ankreuzkompetenz
	 * @param istFachkompetenz   gibt an, on es sich um eine Fach-bezogene Ankreuzkompetenz handelt oder nicht
	 * @param fachID             die ID des Faches
	 * @param jahrgang           der ASD-Jahrgang, dem die Ankreuzkompetenz zugeordnet ist
	 * @param text               der Text der Ankreuzkompetenz
	 * @param sortierung         die Reihenfolge der Ankreuzkompetenzen
	 *
	 * @return true, falls die Ankreuzkompetenz hinzugefügt wurde, ansonsten false
	 */
	public boolean addAnkreuzkompetenz(final long id, final boolean istFachkompetenz, final Long fachID, final @NotNull String jahrgang,
			final @NotNull String text, final int sortierung) {
		if (mapAnkreuzkompetenzen.get(id) != null)
			return false;
		final @NotNull ENMAnkreuzkompetenz kompetenz = new ENMAnkreuzkompetenz();
		kompetenz.id = id;
		kompetenz.istFachkompetenz = istFachkompetenz;
		kompetenz.fachID = fachID;
		kompetenz.jahrgang = jahrgang;
		kompetenz.text = text;
		kompetenz.sortierung = sortierung;
		daten.ankreuzkompetenzen.kompetenzen.add(kompetenz);
		mapAnkreuzkompetenzen.put(id, kompetenz);
		return true;
	}


	/**
	 * Liefert das ENM-Lehrer-Objekt für die angegebene Lehrer-ID zurück,
	 * sofern die Lehrer über die Methode {@link ENMDatenManager#addLehrer(long, String, String, String, Geschlecht, String)}
	 * hinzugefügt wurden.
	 *
	 * @param id   die ID des Lehrers
	 *
	 * @return das ENM-Lehrer-Objekt
	 */
	public ENMLehrer getLehrer(final long id) {
		return mapLehrer.get(id);
	}


	/**
	 * Liefert das ENM-Schüler-Objekt für die angegebene Schüler-ID zurück,
	 * sofern die Schüler über die Methode {@link ENMDatenManager#addSchueler(long, long, long, String, String, Geschlecht, String, boolean, boolean)}
	 * hinzugefügt wurden.
	 *
	 * @param id   die ID des Schülers
	 *
	 * @return das ENM-Schüler-Objekt
	 */
	public ENMSchueler getSchueler(final long id) {
		return mapSchueler.get(id);
	}


	/**
	 * Liefert das ENM-Fächer-Objekt für die angegebene Fächer-ID zurück,
	 * sofern die Fächer über die Methode {@link ENMDatenManager#addFach(long, String, String, int, boolean)}
	 * hinzugefügt wurden.
	 *
	 * @param id   die ID des Faches
	 *
	 * @return das ENM-Fächer-Objekt
	 */
	public ENMFach getFach(final long id) {
		return mapFaecher.get(id);
	}


	/**
	 * Liefert das ENM-Fächer-Objekt für das angegebene Fächer-Kürzel zurück,
	 * sofern die Fächer über die Methode {@link ENMDatenManager#addFach(long, String, String, int, boolean)}
	 * hinzugefügt wurden.
	 *
	 * @param kuerzel   das Kürzel des Faches
	 *
	 * @return das ENM-Fächer-Objekt
	 */
	public ENMFach getFachByKuerzel(final @NotNull String kuerzel) {
		return mapFaecherByKuerzel.get(kuerzel);
	}


	/**
	 * Liefert das ENM-Jahrgänge-Objekt für die angegebene Jahrgangs-ID zurück,
	 * sofern die Jahrgänge über die Methode {@link ENMDatenManager#addJahrgang(long, String, String, String, String, int)}
	 * hinzugefügt wurden.
	 *
	 * @param id   die ID des Jahrgangs
	 *
	 * @return das ENM-Jahrgänge-Objekt
	 */
	public ENMJahrgang getJahrgang(final long id) {
		return mapJahrgaenge.get(id);
	}


	/**
	 * Liefert das ENM-Klassen-Objekt für die angegebene Klassen-ID zurück,
	 * sofern die Klassen über die Methode {@link ENMDatenManager#addKlasse(long, String, String, Long, int)}
	 * hinzugefügt wurden.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return das ENM-Klassen-Objekt
	 */
	public ENMKlasse getKlasse(final long id) {
		return mapKlassen.get(id);
	}


	/**
	 * Liefert das ENM-Teilleistungsart-Objekt für die angegebene Teilleistungsart-ID zurück,
	 * sofern die Teilleistungsart hinzugefügt wurde.
	 *
	 * @param id   die ID der Teilleistungsart
	 *
	 * @return das ENM-Teilleistungsart-Objekt
	 */
	public ENMTeilleistungsart getTeilleistungsart(final long id) {
		return mapTeilleistungsarten.get(id);
	}


	/**
	 * Liefert das ENMAnkreuzkompetenz-Objekt für die angegebene Ankreuzkompetenz-ID zurück,
	 * sofern die Ankreuzkompetenz hinzugefügt wurde.
	 *
	 * @param id   die ID der Ankreuzkompetenz
	 *
	 * @return das ENMAnkreuzkompetenz-Objekt
	 */
	public ENMAnkreuzkompetenz getAnkreuzkompetenz(final long id) {
		return mapAnkreuzkompetenzen.get(id);
	}


	/**
	 * Fügt eine neue Lerngruppe mit den angegebenen Parametern hinzu, falls sie noch nicht existiert. Die strID ist dabei
	 * eine temporäre ID, die nur bei der Erstellung von ENMLerngruppen auf Serverseite genutzt wird.
	 *
	 * @param strID               die temporäre ID der Lerngruppe, um festzustellen, ob es diese Lerngruppe bereits gibt.
	 * @param kID                 die ID der Lerngruppe (Klasse oder Kurs) in der SVWS-DB
	 * @param fachID              die ID des Faches der Lerngruppe.
	 * @param kursartID           gibt die ID der Kursart an. Ist dieser Wert null, so handelt es sich um Klassen-Unterricht
	 * @param bezeichnung         die Bezeichnung der Lerngruppe (z.B. D-GK4)
	 * @param kursartKuerzel      das Kürzel der (allgemeinen) Kursart (z.B. GK)
	 * @param bilingualeSprache   das einstellige Kürzel der bilingualen Sprache, sofern es sich um eine bilinguale
	 *                            Lerngruppe handelt. (z.B. F)
	 * @param wochenstunden       die Anzahl der Wochenstunden, falls es sich um einen Kurs handelt.
	 */
	public void addLerngruppe(final @NotNull String strID, final long kID, final long fachID, final Integer kursartID, final String bezeichnung,
			final String kursartKuerzel, final String bilingualeSprache, final int wochenstunden) {
		if (mapLerngruppen.get(strID) != null)
			return;
		final @NotNull ENMLerngruppe lerngruppe = new ENMLerngruppe();
		lerngruppe.id = lerngruppenIDZaehler++;
		lerngruppe.kID = kID;
		lerngruppe.fachID = fachID;
		lerngruppe.kursartID = kursartID;
		lerngruppe.bezeichnung = bezeichnung;
		lerngruppe.kursartKuerzel = kursartKuerzel;
		lerngruppe.bilingualeSprache = bilingualeSprache;
		lerngruppe.wochenstunden = wochenstunden;
		mapLerngruppen.put(strID, lerngruppe);
		daten.lerngruppen.add(lerngruppe);
	}


	/**
	 * Liefert die Lerngruppe mit der übergebenen (temporären) ID zurück.
	 *
	 * @param strID   die temporäre ID der Lerngruppe, um festzustellen, ob es diese Lerngruppe bereits gibt.
	 *
	 * @return die Lerngruppe
	 */
	public ENMLerngruppe getLerngruppe(final @NotNull String strID) {
		return mapLerngruppen.get(strID);
	}




	/**
	 * Fügt die Klassenlehrer zu der List der Klassenlehrer bei einem Schüler hinzu
	 *
	 * @param schueler           der Schüler
	 * @param klassenlehrerIDs   die IDs der Klassenlehrer
	 */
	public void addSchuelerKlassenlehrer(final @NotNull ENMSchueler schueler, final long... klassenlehrerIDs) {
		// TODO
	}

	/**
	 * Fügt eine Sprache mit den übergebenen Informationen zu der Sprachenfolge eines Schülers hinzu.
	 *
	 * @param schueler               der Schüler
	 * @param sprache                das Kürzel der Sprache, bereinigt von dem Jahrgang, in dem die Sprache eingesetzt hat
	 * @param fachID                 die ID des Faches
	 * @param fachKuerzel            das Kürzel des Faches
	 * @param reihenfolge            die Reihenfolge des Faches in der Sprachenfolge (Beispiel 1)
	 * @param belegungVonJahrgang    die Information, ab welchem Jahrgang die Sprache belegt wurde (Beispiel 5)
	 * @param belegungVonAbschnitt   die Information, ab welchem Abschnitt in dem Jahrgang die Sprache belegt wurde (Beispiel 1)
	 * @param belegungBisJahrgang    die Information, bis zu welchem Jahrgang die Sprache belegt wurde (Beispiel 12), sofern die Sprache bereits abgeschlossen ist
	 * @param belegungBisAbschnitt   die Information, bis zu welchem Abschnitt in dem Jahrgang die Sprache belegt wurde (Beispiel 2), sofern die Sprache bereits abgeschlossen ist
	 * @param referenzniveau         die Bezeichnung des Sprachreferenzniveaus, welches bisher erreicht wurde (z.B. B2/C1)
	 * @param belegungSekI           die Mindest-Dauer der Belegung in der Sekundarstufe I gemäß den Stufen im Core-Type SprachBelegungSekI (z.B. 0, 2, 4, 6)
	 */
	public void addSchuelerSprachenfolge(final @NotNull ENMSchueler schueler, final String sprache, final long fachID, final String fachKuerzel,
			final int reihenfolge,
			final int belegungVonJahrgang, final int belegungVonAbschnitt, final Integer belegungBisJahrgang, final Integer belegungBisAbschnitt,
			final String referenzniveau, final Integer belegungSekI) {
		// TODO
	}

	/**
	 * Fügt die Leistungsdaten mit den übergebenen Informationen zu den Leistungsdaten eines Schülers hinzu
	 *
	 * @param schueler      der Schüler
	 * @param id            die ID der Schüler-Ankreuzkompetenz in der SVWS-DB (z.B. 307956)
	 * @param kompetenzID   die Katalog-ID der Ankreuzkompetenz
	 * @param stufen        die Information der Zuweisung zu den einzelnen Kompetenzstufen (Ein boolean-Array mit genau 5 Elementen)
	 * @param tsStufe       der Zeitstempel der letzten Änderung an der Zuweisung der Kompetenzstufen
	 *
	 * @return die neue ENM-Leistung
	 */
	public @NotNull ENMSchuelerAnkreuzkompetenz addSchuelerAnkreuzkompetenz(final @NotNull ENMSchueler schueler, final long id,
			final Long kompetenzID, final @NotNull boolean[] stufen, final String tsStufe) {
		final @NotNull ENMSchuelerAnkreuzkompetenz kompetenz = new ENMSchuelerAnkreuzkompetenz();
		kompetenz.id = id;
		kompetenz.kompetenzID = kompetenzID;
		kompetenz.stufen = stufen;
		kompetenz.tsStufe = tsStufe;
		schueler.ankreuzkompetenzen.add(kompetenz);
		return kompetenz;
	}


	/**
	 * Fügt die Leistungsdaten mit den übergebenen Informationen zu den Leistungsdaten eines Schülers hinzu
	 *
	 * @param schueler                        der Schüler
	 * @param leistungID                      die ID der Leistungsdaten des Schülers in der SVWS-DB (z.B. 307956)
	 * @param lerngruppenID                   die eindeutige ID der Lerngruppe, der der Schüler zugeordnet ist.
	 *                                        (Klasse oder Kurs wird erst in der Lerngruppe unterschieden!)
	 * @param note                            das Kürzel der Note, die vergeben wurde
	 * @param tsNote                          der Zeitstempe der letzten Änderung an der Note
	 * @param noteQuartal                     das Kürzel der Quartal-Note, die vergeben wurde
	 * @param tsNoteQuartal                   der Zeitstempe der letzten Änderung an der Quartal-Note
	 * @param istSchriftlich                  gibt an, ob das Fach schriftlich belegt wurde oder nicht
	 * @param abiturfach                      gibt an, ob es sich um ein Abitufach handelt (1,2,3 oder 4) oder nicht (null)
	 * @param fehlstundenFach                 gibt die Anzahl der gesamten Fehlstunden an, sofern diese fachbezogen ermittel werden
	 * @param tsFehlstundenFach               der Zeitstempel der letzten Änderung an den gesamten Fehlstunden, sofern
	 *                                    	  diese fachbezogen ermittel werden
	 * @param fehlstundenUnentschuldigtFach   gibt die Anzahl der unentschuldigten Fehlstunden an, sofern diese fachbezogen ermittel werden
	 * @param tsFehlstundenUnentschuldigtFach der Zeitstempel der letzten Änderung an den unentschuldigten Fehlstunden,
	 *                                        sofern diese fachbezogen ermittel werden
	 * @param fachbezogeneBemerkungen         die fachbezogenen Bemerkungen bzw. das Thema bei Projektkursen
	 * @param tsFachbezogeneBemerkungen       der Zeitstempel der letzten Änderung an den fachbezogenen Bemerkungen
	 * @param neueZuweisungKursart            die Kurszuweisung, die auf dem Zeugnis erscheinen soll für den nächsten Kursabschnitt
	 *                                        (z.B. E oder G-Kurs, z.B. an der Gesamtschule)
	 * @param istGemahnt                      gibt an, ob ein Fach gemahnt wurde oder nicht
	 * @param tsIstGemahnt                    der Zeitstempel der letzten Änderung an der Angabe, ob ein Fach gemahnt wurde oder nicht
	 * @param mahndatum                       das Mahndatum bei erfolgter Mahnung
	 *
	 * @return die neue ENM-Leistung
	 */
	public @NotNull ENMLeistung addSchuelerLeistungsdaten(final @NotNull ENMSchueler schueler, final long leistungID, final long lerngruppenID,
			final String note,
			final String tsNoteQuartal, final String noteQuartal, final String tsNote, final boolean istSchriftlich, final Integer abiturfach,
			final Integer fehlstundenFach, final String tsFehlstundenFach, final Integer fehlstundenUnentschuldigtFach,
			final String tsFehlstundenUnentschuldigtFach, final String fachbezogeneBemerkungen, final String tsFachbezogeneBemerkungen,
			final String neueZuweisungKursart, final boolean istGemahnt, final String tsIstGemahnt, final String mahndatum) {
		final @NotNull ENMLeistung enmLeistung = new ENMLeistung();
		enmLeistung.id = leistungID;
		enmLeistung.lerngruppenID = lerngruppenID;
		enmLeistung.note = note;
		enmLeistung.tsNote = tsNote;
		enmLeistung.noteQuartal = noteQuartal;
		enmLeistung.tsNoteQuartal = tsNoteQuartal;
		enmLeistung.istSchriftlich = istSchriftlich;
		enmLeistung.abiturfach = abiturfach;
		enmLeistung.fehlstundenFach = fehlstundenFach;
		enmLeistung.tsFehlstundenFach = tsFehlstundenFach;
		enmLeistung.fehlstundenUnentschuldigtFach = fehlstundenUnentschuldigtFach;
		enmLeistung.tsFehlstundenUnentschuldigtFach = tsFehlstundenUnentschuldigtFach;
		enmLeistung.fachbezogeneBemerkungen = fachbezogeneBemerkungen;
		enmLeistung.tsFachbezogeneBemerkungen = tsFachbezogeneBemerkungen;
		enmLeistung.neueZuweisungKursart = neueZuweisungKursart;
		enmLeistung.istGemahnt = istGemahnt;
		enmLeistung.tsIstGemahnt = tsIstGemahnt;
		enmLeistung.mahndatum = mahndatum;
		schueler.leistungsdaten.add(enmLeistung);
		return enmLeistung;
	}



	/**
	 * Fügt die Teilleistung mit den übergebenen Angaben zu übergebenen Leistungsdaten
	 * eines Schülers hinzu.
	 *
	 * @param leistung       die Leistungsdaten eines Schülers
	 * @param id             die ID der Teilleistung
	 * @param artID          die ID der Art von Teileistungen
	 * @param tsArtID        der Zeitstempel der letzten Änderung an der Teilleistungsart
	 * @param datum          das Datum, welches dem Erbringen der Teilleistung zuzuordnen ist (z.B. Klausurdatum)
	 * @param tsDatum        der Zeitstempel der letzten Änderung an dem Datum
	 * @param bemerkung      ggf. eine Bemerkung zu der Teilleistung
	 * @param tsBemerkung    der Zeitstempel der letzten Änderung an der Bemerkung
	 * @param note           das Notenkürzel, welches der Teilleistung zuzuordnen ist.
	 * @param tsNote         der Zeitstempel der letzten Änderung an der Note
	 */
	public void addSchuelerTeilleistung(final @NotNull ENMLeistung leistung, final long id, final long artID, final String tsArtID,
			final String datum, final String tsDatum, final String bemerkung, final String tsBemerkung, final String note, final String tsNote) {
		final @NotNull ENMTeilleistung enmTeilleistung = new ENMTeilleistung();
		enmTeilleistung.id = id;
		enmTeilleistung.artID = artID;
		enmTeilleistung.tsArtID = tsArtID;
		enmTeilleistung.datum = datum;
		enmTeilleistung.tsDatum = tsDatum;
		enmTeilleistung.bemerkung = bemerkung;
		enmTeilleistung.tsBemerkung = tsBemerkung;
		enmTeilleistung.note = note;
		enmTeilleistung.tsNote = tsNote;
		leistung.teilleistungen.add(enmTeilleistung);
	}

}
