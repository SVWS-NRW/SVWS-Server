package de.svws_nrw.service.enm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.NoteKatalogEintrag;
import de.svws_nrw.asd.data.schule.FoerderschwerpunktKatalogEintrag;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.enm.v2.ENMv2Abteilung;
import de.svws_nrw.core.data.enm.v2.ENMv2Ankreuzkompetenz;
import de.svws_nrw.core.data.enm.v2.ENMv2Daten;
import de.svws_nrw.core.data.enm.v2.ENMv2Fach;
import de.svws_nrw.core.data.enm.v2.ENMv2Foerderschwerpunkt;
import de.svws_nrw.core.data.enm.v2.ENMv2Jahrgang;
import de.svws_nrw.core.data.enm.v2.ENMv2Klasse;
import de.svws_nrw.core.data.enm.v2.ENMv2Lehrer;
import de.svws_nrw.core.data.enm.v2.ENMv2Leistung;
import de.svws_nrw.core.data.enm.v2.ENMv2Lerngruppe;
import de.svws_nrw.core.data.enm.v2.ENMv2Note;
import de.svws_nrw.core.data.enm.v2.ENMv2Schueler;
import de.svws_nrw.core.data.enm.v2.ENMv2SchuelerAnkreuzkompetenz;
import de.svws_nrw.core.data.enm.v2.ENMv2Teilleistung;
import de.svws_nrw.core.data.enm.v2.ENMv2Teilleistungsart;
import de.svws_nrw.core.data.enm.v2.ENMv2ZP10;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerZP10;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLeistungsdaten;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse dien dem Verwalten von ENM-Daten (siehe auch {@link ENMv2Daten}).
 */
public class EnmV2DatenManager {

	/** Die ENM-Daten, die von diesem Daten-Manager verwaltet werden. */
	public final @NotNull ENMv2Daten daten;


	/** Temporäre Map für das Befüllen der ENMLehrer-Vektors.*/
	private final @NotNull Map<Long, ENMv2Lehrer> mapLehrer = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMSchueler-Vektors.*/
	private final @NotNull Map<Long, ENMv2Schueler> mapSchueler = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMFach-Vektors.*/
	private final @NotNull Map<Long, ENMv2Fach> mapFaecher = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMFach-Vektors.*/
	private final @NotNull Map<String, ENMv2Fach> mapFaecherByKuerzel = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMJahrgang-Vektors.*/
	private final @NotNull Map<Long, ENMv2Jahrgang> mapJahrgaenge = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMKlasse-Vektors.*/
	private final @NotNull Map<Long, ENMv2Klasse> mapKlassen = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMAbteilungen-Vektors.*/
	private final @NotNull Map<Long, ENMv2Abteilung> mapAbteilungen = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMTeilleistungsarten-Vektors.*/
	private final @NotNull Map<Long, ENMv2Teilleistungsart> mapTeilleistungsarten = new HashMap<>();

	/** Temporäre Map für das Befüllen des ENMAnkreuzkompetenz-Vektors.*/
	private final @NotNull Map<Long, ENMv2Ankreuzkompetenz> mapAnkreuzkompetenzen = new HashMap<>();

	/** Zählt die Id der Lerngruppe hoch. */
	private long lerngruppenIDZaehler = 1;

	/** Temporäre Map für die Lerngruppen. */
	private final @NotNull Map<String, ENMv2Lerngruppe> mapLerngruppen = new HashMap<>();


	/**
	 * Erzeugt einen neuen ENM-Daten-Manager mit leeren ENM-Daten.
	 *
	 * @param lehrerID   die ID des Lehrers für welchen die ENM-Daten erzeugt werden oder null für alle Lehrer
	 */
	public EnmV2DatenManager(final Long lehrerID) {
		this.daten = new ENMv2Daten();
		this.daten.lehrerID = lehrerID;
	}


	/**
	 * Setzt die grundlegenden Daten zur Schule und zu dem Schuljahresabschnitts für welchen
	 * die ENM-Daten generiert wurden.
	 *
	 * @param schulnummer                 die Schulnummer
	 * @param schuljahr                   das Schuljahr
	 * @param anzahlAbschnitte            die Anzahl der Abschnitte an der Schule (2: Halbjahrsmodus, 4: Quartalsmodus)
	 * @param abschnitt                   die Nummer des Abschnittes im Schuljahr
	 * @param schulform                   das Kürzel der Schulform der Schule
	 * @param idSchulleitung              die Lehrer-ID der Schulleitung
	 * @param idSchulleitungStv           die Lehrer-ID der stellvertretenden Schulleitung
	 */
	public void setSchuldaten(final int schulnummer, final int schuljahr, final int anzahlAbschnitte, final int abschnitt,
			final @NotNull String schulform, final Long idSchulleitung, final Long idSchulleitungStv) {
		daten.schulnummer = schulnummer;
		daten.schuljahr = schuljahr;
		daten.anzahlAbschnitte = anzahlAbschnitte;
		daten.aktuellerAbschnitt = abschnitt;
		daten.schulform = schulform;
		daten.idSchulleitung = idSchulleitung;
		daten.idSchulleitungStv = idSchulleitungStv;
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
		if (!daten.noten.isEmpty()) {
			return;
		}
		final @NotNull List<Note> noten = Note.data().getWerteBySchuljahr(schuljahr);
		for (final @NotNull Note note : noten) {
			final NoteKatalogEintrag nke = note.daten(schuljahr);
			if ((nke == null) || (nke.id < 0)) {
				continue;
			}
			final @NotNull ENMv2Note enmNote = new ENMv2Note();
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
		if (!daten.foerderschwerpunkte.isEmpty()) {
			return;
		}
		final @NotNull List<Foerderschwerpunkt> foerderschwerpunkte = Foerderschwerpunkt.getBySchuljahrAndSchulform(schuljahr, schulform);
		for (final Foerderschwerpunkt foerderschwerpunkt : foerderschwerpunkte) {
			final FoerderschwerpunktKatalogEintrag fske = foerderschwerpunkt.daten(schuljahr);
			if (fske == null) {
				continue;
			}
			final ENMv2Foerderschwerpunkt enmFoerderschwerpunkt = new ENMv2Foerderschwerpunkt();
			enmFoerderschwerpunkt.id = fske.id;
			enmFoerderschwerpunkt.kuerzel = fske.kuerzel;
			enmFoerderschwerpunkt.beschreibung = fske.text;
			daten.foerderschwerpunkte.add(enmFoerderschwerpunkt);
		}
	}


	/**
	 * Fügt einen Lehrer hinzu und überprüft dabei, ob der Lehrer schon in der Liste vorhanden ist.
	 *
	 * @param lehrer            die Lehrer-Daten aus der Datenbank
	 * @param passwordHash      der Password-Hash des Lehrer-Kennwortes für das Notenmodul
	 * @param tsPasswordHash    der Zeitstempel, wann der Password-Hash zuletzt geändert wurde
	 *
	 * @return true, falls der Lehrer hinzugefügt wurde, ansonsten false
	 */
	public boolean addLehrer(final DTOLehrer lehrer, final @NotNull String passwordHash, final String tsPasswordHash) {
		if (mapLehrer.get(lehrer.ID) != null) {
			return false;
		}
		final @NotNull ENMv2Lehrer enmLehrer = new ENMv2Lehrer();
		enmLehrer.id = lehrer.ID;
		enmLehrer.kuerzel = lehrer.Kuerzel;
		enmLehrer.nachname = lehrer.Nachname;
		enmLehrer.vorname = lehrer.Vorname;
		enmLehrer.geschlecht = lehrer.Geschlecht.kuerzel;
		enmLehrer.eMailDienstlich = lehrer.eMailDienstlich;
		enmLehrer.passwordHash = passwordHash;
		enmLehrer.tsPasswordHash = tsPasswordHash;
		enmLehrer.totpSecret = null;          // TODO aus DB, sobald dort implementiert
		enmLehrer.istErstanmeldung = false;   // TODO aus DB, sobald dort implementiert
		enmLehrer.tsIstErstanmeldung = null;  // TODO aus DB, sobald dort implementiert
		daten.lehrer.add(enmLehrer);
		mapLehrer.put(lehrer.ID, enmLehrer);
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
		if (mapSchueler.get(id) != null) {
			return false;
		}
		final @NotNull ENMv2Schueler enmSchueler = new ENMv2Schueler();
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
	 * @param fach   die Fach
	 */
	public void addFach(final DTOFach fach) {
		if ((fach == null) || (getFach(fach.ID) != null)) {
			return;
		}
		final @NotNull ENMv2Fach enmFach = new ENMv2Fach();
		enmFach.id = fach.ID;
		enmFach.kuerzel = fach.StatistikKuerzel;
		enmFach.kuerzelAnzeige = fach.Kuerzel;
		enmFach.bezeichnung = fach.Bezeichnung;
		enmFach.sortierung = fach.SortierungAllg;
		enmFach.istFremdsprache = fach.IstFremdsprache;
		daten.faecher.add(enmFach);
		mapFaecher.put(fach.ID, enmFach);
		mapFaecherByKuerzel.put(fach.Kuerzel, enmFach);
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
		if (mapJahrgaenge.get(id) != null) {
			return false;
		}
		final @NotNull ENMv2Jahrgang enmJahrgang = new ENMv2Jahrgang();
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
		if (mapKlassen.get(id) != null) {
			return false;
		}
		final @NotNull ENMv2Klasse enmKlasse = new ENMv2Klasse();
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
	 * Fügt eine neue Abteilung zu den ENM-Daten hinzu
	 *
	 * @param abteilung   die hinzuzufügende Abteilungg
	 */
	public void addAbteilung(final DTOAbteilungen abteilung) {
		if (mapAbteilungen.get(abteilung.ID) != null) {
			return;
		}
		final @NotNull ENMv2Abteilung enmAbteilung = new ENMv2Abteilung();
		enmAbteilung.id = abteilung.ID;
		enmAbteilung.idAbteilungsleiter = abteilung.AbteilungsLeiter_ID;
		enmAbteilung.bezeichnung = (abteilung.Bezeichnung == null) ? "" : abteilung.Bezeichnung;
		enmAbteilung.sortierung = (abteilung.Sortierung == null) ? 32000 : abteilung.Sortierung;
		daten.abteilungen.add(enmAbteilung);
		mapAbteilungen.put(abteilung.ID, enmAbteilung);
	}


	/**
	 * Fügt eine neue Klassenzuordnung zu einer Abteilung hinzu
	 *
	 * @param idAbteilung   die Abteilung
	 * @param idKlasse      die Klasse
	 */
	public void addAbteilungKlasse(final long idAbteilung, final long idKlasse) {
		final ENMv2Abteilung enmAbteilung = mapAbteilungen.get(idAbteilung);
		if (enmAbteilung == null) {
			return;
		}
		if (!enmAbteilung.klassenzuordnungen.contains(idKlasse)) {
			enmAbteilung.klassenzuordnungen.add(idKlasse);
		}
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
		if (mapTeilleistungsarten.get(id) != null) {
			return false;
		}
		final @NotNull ENMv2Teilleistungsart enmArt = new ENMv2Teilleistungsart();
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
	 * @param jahrgaenge         die Jahrgänge, denen die Ankreuzkompetenz zugeordnet ist
	 * @param text               der Text der Ankreuzkompetenz
	 * @param sortierung         die Reihenfolge der Ankreuzkompetenzen
	 *
	 * @return true, falls die Ankreuzkompetenz hinzugefügt wurde, ansonsten false
	 */
	public boolean addAnkreuzkompetenz(final long id, final boolean istFachkompetenz, final Long fachID, final @NotNull List<Long> jahrgaenge,
			final @NotNull String text, final int sortierung) {
		if (mapAnkreuzkompetenzen.get(id) != null) {
			return false;
		}
		final @NotNull ENMv2Ankreuzkompetenz kompetenz = new ENMv2Ankreuzkompetenz();
		kompetenz.id = id;
		kompetenz.istFachkompetenz = istFachkompetenz;
		kompetenz.fachID = fachID;
		kompetenz.jahrgaenge.addAll(jahrgaenge);
		kompetenz.text = text;
		kompetenz.sortierung = sortierung;
		daten.ankreuzkompetenzen.kompetenzen.add(kompetenz);
		mapAnkreuzkompetenzen.put(id, kompetenz);
		return true;
	}


	/**
	 * Liefert das ENM-Lehrer-Objekt für die angegebene Lehrer-ID zurück.
	 *
	 * @param id   die ID des Lehrers
	 *
	 * @return das ENM-Lehrer-Objekt
	 */
	public ENMv2Lehrer getLehrer(final long id) {
		return mapLehrer.get(id);
	}


	/**
	 * Liefert das ENM-Schüler-Objekt für die angegebene Schüler-ID zurück.
	 *
	 * @param id   die ID des Schülers
	 *
	 * @return das ENM-Schüler-Objekt
	 */
	public ENMv2Schueler getSchueler(final long id) {
		return mapSchueler.get(id);
	}


	/**
	 * Liefert das ENM-Fächer-Objekt für die angegebene Fächer-ID zurück.
	 *
	 * @param id   die ID des Faches
	 *
	 * @return das ENM-Fächer-Objekt
	 */
	public ENMv2Fach getFach(final long id) {
		return mapFaecher.get(id);
	}


	/**
	 * Liefert das ENM-Fächer-Objekt für das angegebene Fächer-Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel des Faches
	 *
	 * @return das ENM-Fächer-Objekt
	 */
	public ENMv2Fach getFachByKuerzel(final @NotNull String kuerzel) {
		return mapFaecherByKuerzel.get(kuerzel);
	}


	/**
	 * Liefert das ENM-Jahrgänge-Objekt für die angegebene Jahrgangs-ID zurück.
	 *
	 * @param id   die ID des Jahrgangs
	 *
	 * @return das ENM-Jahrgänge-Objekt
	 */
	public ENMv2Jahrgang getJahrgang(final long id) {
		return mapJahrgaenge.get(id);
	}


	/**
	 * Liefert das ENM-Klassen-Objekt für die angegebene Klassen-ID zurück.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return das ENM-Klassen-Objekt
	 */
	public ENMv2Klasse getKlasse(final long id) {
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
	public ENMv2Teilleistungsart getTeilleistungsart(final long id) {
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
	public ENMv2Ankreuzkompetenz getAnkreuzkompetenz(final long id) {
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
		if (mapLerngruppen.get(strID) != null) {
			return;
		}
		final @NotNull ENMv2Lerngruppe lerngruppe = new ENMv2Lerngruppe();
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
	public ENMv2Lerngruppe getLerngruppe(final @NotNull String strID) {
		return mapLerngruppen.get(strID);
	}




	/**
	 * Fügt die Klassenlehrer zu der List der Klassenlehrer bei einem Schüler hinzu
	 *
	 * @param schueler           der Schüler
	 * @param klassenlehrerIDs   die IDs der Klassenlehrer
	 */
	public void addSchuelerKlassenlehrer(final @NotNull ENMv2Schueler schueler, final long... klassenlehrerIDs) {
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
	public void addSchuelerSprachenfolge(final @NotNull ENMv2Schueler schueler, final String sprache, final long fachID, final String fachKuerzel,
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
	public @NotNull ENMv2SchuelerAnkreuzkompetenz addSchuelerAnkreuzkompetenz(final @NotNull ENMv2Schueler schueler, final long id,
			final Long kompetenzID, final @NotNull boolean[] stufen, final String tsStufe) {
		final @NotNull ENMv2SchuelerAnkreuzkompetenz kompetenz = new ENMv2SchuelerAnkreuzkompetenz();
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
	 * @param schueler                          der Schüler
	 * @param lerngruppenID                     die eindeutige ID der Lerngruppe, der der Schüler zugeordnet ist.
	 *                                          (Klasse oder Kurs wird erst in der Lerngruppe unterschieden!)
	 * @param leistung                          die Leistungsdaten des Schülers in der SVWS-DB (z.B. 307956)
	 * @param tsLeistung                        die Zeitstempel zu den Leistungsdaten des Schülers
	 * @param istSchriftlich                    gibt an, ob das Fach schriftlich belegt wurde oder nicht
	 * @param abiturfach                        gibt an, ob es sich um ein Abitufach handelt (1,2,3 oder 4) oder nicht (null)
	 *                                          diese fachbezogen ermittel werden
	 * @param istDifferenzierungkursErweitert   gibt an, ob es sich um einen Erweiterungskurs handelt oder nicht
	 * @param istGemahnt                        gibt an, ob ein Fach gemahnt wurde oder nicht
	 * @param mahndatum                         das Mahndatum bei erfolgter Mahnung
	 *
	 * @return die neue ENM-Leistung
	 */
	public @NotNull ENMv2Leistung addSchuelerLeistungsdaten(final @NotNull ENMv2Schueler schueler, final long lerngruppenID,
			final DTOSchuelerLeistungsdaten leistung, final DTOTimestampsSchuelerLeistungsdaten tsLeistung, final boolean istSchriftlich,
			final Integer abiturfach, final boolean istDifferenzierungkursErweitert, final boolean istGemahnt, final String mahndatum) {
		final @NotNull ENMv2Leistung enmLeistung = new ENMv2Leistung();
		enmLeistung.id = leistung.ID;
		enmLeistung.lerngruppenID = lerngruppenID;
		enmLeistung.note = leistung.NotenKrz;
		enmLeistung.tsNote = tsLeistung.tsNotenKrz;
		enmLeistung.noteQuartal = leistung.NotenKrzQuartal;
		enmLeistung.tsNoteQuartal = tsLeistung.tsNotenKrzQuartal;
		enmLeistung.istSchriftlich = istSchriftlich;
		enmLeistung.abiturfach = abiturfach;
		enmLeistung.fehlstundenFach = leistung.FehlStd;
		enmLeistung.tsFehlstundenFach = tsLeistung.tsFehlStd;
		enmLeistung.fehlstundenUnentschuldigtFach = leistung.uFehlStd;
		enmLeistung.tsFehlstundenUnentschuldigtFach = tsLeistung.tsuFehlStd;
		enmLeistung.fachbezogeneBemerkungen = leistung.Lernentw;
		enmLeistung.tsFachbezogeneBemerkungen = tsLeistung.tsLernentw;
		enmLeistung.istDifferenzierungkursErweitert = istDifferenzierungkursErweitert;
		enmLeistung.neueZuweisungKursart = null;
		enmLeistung.tsNeueZuweisungKursart = null;
		enmLeistung.istGemahnt = istGemahnt;
		enmLeistung.tsIstGemahnt = tsLeistung.tsWarnung;
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
	public void addSchuelerTeilleistung(final @NotNull ENMv2Leistung leistung, final long id, final long artID, final String tsArtID,
			final String datum, final String tsDatum, final String bemerkung, final String tsBemerkung, final String note, final String tsNote) {
		final @NotNull ENMv2Teilleistung enmTeilleistung = new ENMv2Teilleistung();
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


	/**
	 * Fügt die ZP10-Daten zu einem Schüler hinzu.
	 *
	 * @param schueler   die Daten des Schülers
	 * @param zp10       die ZP10-Daten des Schülers
	 */
	public void addSchuelerZP10(final @NotNull ENMv2Schueler schueler, final @NotNull DTOSchuelerZP10 zp10) {
		final ENMv2ZP10 enmZP10 = new ENMv2ZP10();
		enmZP10.id = zp10.ID;
		enmZP10.idFach = zp10.Fach_ID;
		enmZP10.idLehrer = zp10.Fachlehrer_ID;
		enmZP10.vornote = zp10.Vornote;
		enmZP10.tsVornote = null;                                           // TODO Zeitstempel wird hier benötigt
		enmZP10.noteSchriftlichePruefung = zp10.NoteSchriftlich;
		enmZP10.tsNoteSchriftlichePruefung = null;                          // TODO Zeitstempel wird hier benötigt
		enmZP10.muendlichePruefung = (zp10.MdlPruefung == null) ? false : zp10.MdlPruefung;
		enmZP10.tsMuendlichePruefung = null;                                // TODO Zeitstempel wird hier benötigt
		enmZP10.muendlichePruefungFreiwillig = (zp10.MdlPruefungFW == null) ? false : zp10.MdlPruefungFW;
		enmZP10.tsMuendlichePruefungFreiwillig = null;                      // TODO Zeitstempel wird hier benötigt
		enmZP10.noteMuendlichePruefung = zp10.NoteMuendlich;
		enmZP10.tsNoteMuendlichePruefung = null;                            // TODO Zeitstempel wird hier benötigt
		enmZP10.abschlussnote = zp10.NoteAbschluss;
		enmZP10.tsAbschlussnote = null;                                     // TODO Zeitstempel wird hier benötigt
		schueler.zp10.add(enmZP10);
	}

}
