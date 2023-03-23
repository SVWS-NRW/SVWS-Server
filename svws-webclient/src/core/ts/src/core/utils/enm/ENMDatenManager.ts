import { JavaObject } from '../../../java/lang/JavaObject';
import { ENMKlasse } from '../../../core/data/enm/ENMKlasse';
import { ENMLeistung } from '../../../core/data/enm/ENMLeistung';
import { ENMFach } from '../../../core/data/enm/ENMFach';
import { ENMJahrgang } from '../../../core/data/enm/ENMJahrgang';
import { HashMap } from '../../../java/util/HashMap';
import { Schulform } from '../../../core/types/schule/Schulform';
import { ENMLerngruppe } from '../../../core/data/enm/ENMLerngruppe';
import { ENMLehrer } from '../../../core/data/enm/ENMLehrer';
import { ENMSchueler } from '../../../core/data/enm/ENMSchueler';
import { ENMFoerderschwerpunkt } from '../../../core/data/enm/ENMFoerderschwerpunkt';
import { ENMDaten, cast_de_nrw_schule_svws_core_data_enm_ENMDaten } from '../../../core/data/enm/ENMDaten';
import { Note } from '../../../core/types/Note';
import { List } from '../../../java/util/List';
import { Geschlecht } from '../../../core/types/Geschlecht';
import { ENMNote } from '../../../core/data/enm/ENMNote';
import { Foerderschwerpunkt } from '../../../core/types/schueler/Foerderschwerpunkt';

export class ENMDatenManager extends JavaObject {

	/**
	 * Die ENM-Daten, die von diesem Daten-Manager verwaltet werden.
	 */
	public readonly daten : ENMDaten;

	/**
	 * Temporäre Map für das Befüllen der ENMLehrer-Vektors.
	 */
	private mapLehrer : HashMap<number, ENMLehrer> = new HashMap();

	/**
	 * Temporäre Map für das Befüllen des ENMSchueler-Vektors.
	 */
	private mapSchueler : HashMap<number, ENMSchueler> = new HashMap();

	/**
	 * Temporäre Map für das Befüllen des ENMFach-Vektors.
	 */
	private mapFaecher : HashMap<number, ENMFach> = new HashMap();

	/**
	 * Temporäre Map für das Befüllen des ENMFach-Vektors.
	 */
	private mapFaecherByKuerzel : HashMap<string, ENMFach> = new HashMap();

	/**
	 * Temporäre Map für das Befüllen des ENMJahrgang-Vektors.
	 */
	private mapJahrgaenge : HashMap<number, ENMJahrgang> = new HashMap();

	/**
	 * Temporäre Map für das Befüllen des ENMKlasse-Vektors.
	 */
	private mapKlassen : HashMap<number, ENMKlasse> = new HashMap();

	/**
	 * Zählt die Id der Lerngruppe hoch.
	 */
	private lerngruppenIDZaehler : number = 1;

	/**
	 * Temporäre Map für die Lerngruppen.
	 */
	private mapLerngruppen : HashMap<string, ENMLerngruppe> = new HashMap();


	/**
	 * Erzeugt einen neuen ENM-Daten-Manager mit leeren ENM-Daten.
	 *
	 * @param lehrerID   die ID des Lehrers für welchen die ENM-Daten erzeugt werden oder null für alle Lehrer
	 */
	public constructor(lehrerID : number | null);

	/**
	 * Erzeugt einen neuen ENM-Daten-Manager für die übergebenen Daten.
	 *
	 * @param daten   die ENM-Daten
	 */
	public constructor(daten : ENMDaten);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : ENMDaten | null | number) {
		super();
		if (((typeof __param0 !== "undefined") && (typeof __param0 === "number") || (__param0 === null))) {
			const lehrerID : number | null = __param0;
			this.daten = new ENMDaten();
			this.daten.lehrerID = lehrerID;
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.data.enm.ENMDaten'))))) {
			const daten : ENMDaten = cast_de_nrw_schule_svws_core_data_enm_ENMDaten(__param0);
			this.daten = daten;
		} else throw new Error('invalid method overload');
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
	public setSchuldaten(schulnummer : number, schuljahr : number, anzahlAbschnitte : number, abschnitt : number, publicKey : string | null, fehlstundenEingabe : boolean, fehlstundenSIFachbezogen : boolean, fehlstundenSIIFachbezogen : boolean, schulform : string, mailadresse : string | null) : void {
		this.daten.schulnummer = schulnummer;
		this.daten.schuljahr = schuljahr;
		this.daten.anzahlAbschnitte = anzahlAbschnitte;
		this.daten.aktuellerAbschnitt = abschnitt;
		this.daten.publicKey = publicKey;
		this.daten.fehlstundenEingabe = fehlstundenEingabe;
		this.daten.fehlstundenSIFachbezogen = fehlstundenSIFachbezogen;
		this.daten.fehlstundenSIIFachbezogen = fehlstundenSIIFachbezogen;
		this.daten.schulform = schulform;
		this.daten.mailadresse = mailadresse;
	}

	/**
	 * Fügt alle Noten des Core-Type {@link Note} zu dem Noten-Katalog der ENM-Datei hinzu.
	 */
	public addNoten() : void {
		if (this.daten.noten.size() > 0)
			return;
		const noten : Array<Note> = Note.values();
		for (let i : number = 0; i < noten.length; i++) {
			const note : Note = noten[i];
			const enmNote : ENMNote = new ENMNote();
			enmNote.id = note.id;
			enmNote.kuerzel = note.kuerzel;
			enmNote.notenpunkte = note.notenpunkte;
			enmNote.text = note.text;
			this.daten.noten.add(enmNote);
		}
	}

	/**
	 * Fügt alle Förderschwerpunkte des Core-Type {@link Foerderschwerpunkt} zu dem
	 * Förderschwerpunkt-Katalog der ENM-Datei hinzu.
	 *
	 * @param schulform   die Schulform, für welche die zulässigen Förderschwerpunkte
	 *                    zurückgegeben werden
	 */
	public addFoerderschwerpunkte(schulform : Schulform) : void {
		if (this.daten.foerderschwerpunkte.size() > 0)
			return;
		const foerderschwerpunkte : List<Foerderschwerpunkt> = Foerderschwerpunkt.get(schulform);
		for (let i : number = 0; i < foerderschwerpunkte.size(); i++) {
			const foerderschwerpunkt : Foerderschwerpunkt | null = foerderschwerpunkte.get(i);
			const enmFoerderschwerpunkt : ENMFoerderschwerpunkt | null = new ENMFoerderschwerpunkt();
			enmFoerderschwerpunkt.id = foerderschwerpunkt.daten.id;
			enmFoerderschwerpunkt.kuerzel = foerderschwerpunkt.daten.kuerzel;
			enmFoerderschwerpunkt.beschreibung = foerderschwerpunkt.daten.beschreibung;
			this.daten.foerderschwerpunkte.add(enmFoerderschwerpunkt);
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
	public addLehrer(id : number, kuerzel : string | null, nachname : string | null, vorname : string | null, geschlecht : Geschlecht, eMailDienstlich : string | null) : boolean {
		if (this.mapLehrer.get(id) !== null)
			return false;
		const enmLehrer : ENMLehrer = new ENMLehrer();
		enmLehrer.id = id;
		enmLehrer.kuerzel = kuerzel;
		enmLehrer.nachname = nachname;
		enmLehrer.vorname = vorname;
		enmLehrer.geschlecht = geschlecht.kuerzel;
		enmLehrer.eMailDienstlich = eMailDienstlich;
		this.daten.lehrer.add(enmLehrer);
		this.mapLehrer.put(id, enmLehrer);
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
	public addSchueler(id : number, jahrgangID : number, klasseID : number, nachname : string | null, vorname : string | null, geschlecht : Geschlecht, bilingualeSprache : string | null, istZieldifferent : boolean, istDaZFoerderung : boolean) : boolean {
		if (this.mapSchueler.get(id) !== null)
			return false;
		const enmSchueler : ENMSchueler = new ENMSchueler();
		enmSchueler.id = id;
		enmSchueler.jahrgangID = jahrgangID;
		enmSchueler.klasseID = klasseID;
		enmSchueler.nachname = nachname;
		enmSchueler.vorname = vorname;
		enmSchueler.geschlecht = geschlecht.kuerzel;
		enmSchueler.bilingualeSprache = bilingualeSprache;
		enmSchueler.istZieldifferent = istZieldifferent;
		enmSchueler.istDaZFoerderung = istDaZFoerderung;
		this.daten.schueler.add(enmSchueler);
		this.mapSchueler.put(id, enmSchueler);
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
	public addFach(id : number, kuerzel : string, kuerzelAnzeige : string, sortierung : number, istFremdsprache : boolean) : boolean {
		if (this.mapFaecher.get(id) !== null)
			return false;
		const enmFach : ENMFach = new ENMFach();
		enmFach.id = id;
		enmFach.kuerzel = kuerzel;
		enmFach.kuerzelAnzeige = kuerzelAnzeige;
		enmFach.sortierung = sortierung;
		enmFach.istFremdsprache = istFremdsprache;
		this.daten.faecher.add(enmFach);
		this.mapFaecher.put(id, enmFach);
		this.mapFaecherByKuerzel.put(kuerzelAnzeige, enmFach);
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
	public addJahrgang(id : number, kuerzel : string | null, kuerzelAnzeige : string | null, beschreibung : string | null, stufe : string | null, sortierung : number) : boolean {
		if (this.mapJahrgaenge.get(id) !== null)
			return false;
		const enmJahrgang : ENMJahrgang = new ENMJahrgang();
		enmJahrgang.id = id;
		enmJahrgang.kuerzel = kuerzel;
		enmJahrgang.kuerzelAnzeige = kuerzelAnzeige;
		enmJahrgang.beschreibung = beschreibung;
		enmJahrgang.stufe = stufe;
		enmJahrgang.sortierung = sortierung;
		this.daten.jahrgaenge.add(enmJahrgang);
		this.mapJahrgaenge.put(id, enmJahrgang);
		return true;
	}

	/**
	 * Fügt eine Klasse hinzu und überprüft dabei, ob die Klasse schon in der Liste vorhanden ist.
	 *
	 * @param id                die eindeutige ID der Klasse
	 * @param kuerzel           das Kürzel der Klasse, wie es im Rahmen der amtlichen Schulstatistik verwendet wird. (z.B. EF)
	 * @param kuerzelAnzeige    das Kürzel der Klasse, wie es im Rahmen der Schule benannt wird und angezeigt werden soll. (z.B. EF)
	 * @param sortierung        die Reihenfolge der Klasse bei der Sortierung der Klassen. (z.B. 8)
	 *
	 * @return true, falls die Klasse hinzugefügt wurde, ansonsten false
	 */
	public addKlasse(id : number, kuerzel : string | null, kuerzelAnzeige : string | null, sortierung : number) : boolean {
		if (this.mapKlassen.get(id) !== null)
			return false;
		const enmKlasse : ENMKlasse = new ENMKlasse();
		enmKlasse.id = id;
		enmKlasse.kuerzel = kuerzel;
		enmKlasse.kuerzelAnzeige = kuerzelAnzeige;
		enmKlasse.sortierung = sortierung;
		this.daten.klassen.add(enmKlasse);
		this.mapKlassen.put(id, enmKlasse);
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
	public getLehrer(id : number) : ENMLehrer | null {
		return this.mapLehrer.get(id);
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
	public getSchueler(id : number) : ENMSchueler | null {
		return this.mapSchueler.get(id);
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
	public getFach(id : number) : ENMFach | null {
		return this.mapFaecher.get(id);
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
	public getFachByKuerzel(kuerzel : string) : ENMFach | null {
		return this.mapFaecherByKuerzel.get(kuerzel);
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
	public getJahrgang(id : number) : ENMJahrgang | null {
		return this.mapJahrgaenge.get(id);
	}

	/**
	 * Liefert das ENM-Klassen-Objekt für die angegebene Klassen-ID zurück,
	 * sofern die Klassen über die Methode {@link ENMDatenManager#addKlasse(long, String, String, int)}
	 * hinzugefügt wurden.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return das ENM-Klassen-Objekt
	 */
	public getKlasse(id : number) : ENMKlasse | null {
		return this.mapKlassen.get(id);
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
	public addLerngruppe(strID : string, kID : number, fachID : number, kursartID : number | null, bezeichnung : string | null, kursartKuerzel : string | null, bilingualeSprache : string | null, wochenstunden : number) : void {
		if (this.mapLerngruppen.get(strID) !== null)
			return;
		const lerngruppe : ENMLerngruppe = new ENMLerngruppe();
		lerngruppe.id = this.lerngruppenIDZaehler++;
		lerngruppe.kID = kID;
		lerngruppe.fachID = fachID;
		lerngruppe.kursartID = kursartID;
		lerngruppe.bezeichnung = bezeichnung;
		lerngruppe.kursartKuerzel = kursartKuerzel;
		lerngruppe.bilingualeSprache = bilingualeSprache;
		lerngruppe.wochenstunden = wochenstunden;
		this.mapLerngruppen.put(strID, lerngruppe);
		this.daten.lerngruppen.add(lerngruppe);
	}

	/**
	 * Liefert die Lerngruppe mit der übergebenen (temporären) ID zurück.
	 *
	 * @param strID   die temporäre ID der Lerngruppe, um festzustellen, ob es diese Lerngruppe bereits gibt.
	 *
	 * @return die Lerngruppe
	 */
	public getLerngruppe(strID : string) : ENMLerngruppe | null {
		return this.mapLerngruppen.get(strID);
	}

	/**
	 * Fügt die Klassenlehrer zu der List der Klassenlehrer bei einem Schüler hinzu
	 *
	 * @param schueler           der Schüler
	 * @param klassenlehrerIDs   die IDs der Klassenlehrer
	 */
	public addSchuelerKlassenlehrer(schueler : ENMSchueler, ...klassenlehrerIDs : Array<number>) : void {
		// empty block
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
	public addSchuelerSprachenfolge(schueler : ENMSchueler, sprache : string | null, fachID : number, fachKuerzel : string | null, reihenfolge : number, belegungVonJahrgang : number, belegungVonAbschnitt : number, belegungBisJahrgang : number | null, belegungBisAbschnitt : number | null, referenzniveau : string | null, belegungSekI : number | null) : void {
		// empty block
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
	 */
	public addSchuelerLeistungsdaten(schueler : ENMSchueler, leistungID : number, lerngruppenID : number, note : string | null, tsNote : string | null, istSchriftlich : boolean, abiturfach : number | null, fehlstundenFach : number | null, tsFehlstundenFach : string | null, fehlstundenUnentschuldigtFach : number | null, tsFehlstundenUnentschuldigtFach : string | null, fachbezogeneBemerkungen : string | null, tsFachbezogeneBemerkungen : string | null, neueZuweisungKursart : string | null, istGemahnt : boolean, tsIstGemahnt : string | null, mahndatum : string | null) : void {
		const enmLeistung : ENMLeistung = new ENMLeistung();
		enmLeistung.id = leistungID;
		enmLeistung.lerngruppenID = lerngruppenID;
		enmLeistung.note = note;
		enmLeistung.tsNote = tsNote;
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
	}

	/**
	 * Fügt die Teilleistung mit den übergebenen Angaben zu übergebenen Leistungsdaten
	 * eines Schülers hinzu.
	 *
	 * @param leistung       die Leistungsdaten eines Schülers
	 * @param id             die ID der Teilleistung
	 * @param artID          die ID der Art von Teileistungen
	 * @param datum          das Datum, welches dem Erbringen der Teilleistung zuzuordnen ist (z.B. Klausurdatum)
	 * @param bemerkung      ggf. eine Bemerkung zu der Teilleistung
	 * @param notenKuerzel   das Notenkürzel, welches der Teilleistung zuzuordnen ist.
	 */
	public addSchuelerTeilleistung(leistung : ENMLeistung, id : number, artID : number, datum : string | null, bemerkung : string | null, notenKuerzel : string | null) : void {
		// empty block
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.enm.ENMDatenManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_enm_ENMDatenManager(obj : unknown) : ENMDatenManager {
	return obj as ENMDatenManager;
}
