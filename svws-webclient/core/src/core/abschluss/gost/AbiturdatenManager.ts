import { JavaObject } from '../../../java/lang/JavaObject';
import { Naturwissenschaften } from '../../../core/abschluss/gost/belegpruefung/Naturwissenschaften';
import { Schwerpunkt } from '../../../core/abschluss/gost/belegpruefung/Schwerpunkt';
import { Abi29BelegpruefungKurszahlenUndWochenstunden } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungKurszahlenUndWochenstunden';
import { GostFaecherManager } from '../../../core/utils/gost/GostFaecherManager';
import { HashMap } from '../../../java/util/HashMap';
import { Abi29BelegpruefungSport } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungSport';
import { GostAbiturMarkierungsalgorithmus } from '../../../core/abschluss/gost/GostAbiturMarkierungsalgorithmus';
import { GostFachUtils } from '../../../core/utils/gost/GostFachUtils';
import { KurszahlenUndWochenstunden } from '../../../core/abschluss/gost/belegpruefung/KurszahlenUndWochenstunden';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostBelegpruefungErgebnis } from '../../../core/abschluss/gost/GostBelegpruefungErgebnis';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaMath } from '../../../java/lang/JavaMath';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import { Latinum } from '../../../core/abschluss/gost/belegpruefung/Latinum';
import { Abi29BelegpruefungSchwerpunkt } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungSchwerpunkt';
import { Sprachendaten } from '../../../asd/data/schueler/Sprachendaten';
import { GostFachbereich } from '../../../core/types/gost/GostFachbereich';
import { Abi29BelegpruefungNaturwissenschaften } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungNaturwissenschaften';
import { Allgemeines } from '../../../core/abschluss/gost/belegpruefung/Allgemeines';
import { Sport } from '../../../core/abschluss/gost/belegpruefung/Sport';
import type { List } from '../../../java/util/List';
import { Abi29BelegpruefungFachkombinationen } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungFachkombinationen';
import { Collections } from '../../../java/util/Collections';
import { AbiFaecher } from '../../../core/abschluss/gost/belegpruefung/AbiFaecher';
import { HashSet } from '../../../java/util/HashSet';
import { Abi29BelegpruefungAllgemeines } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungAllgemeines';
import { Fach } from '../../../asd/types/fach/Fach';
import { LiterarischKuenstlerisch } from '../../../core/abschluss/gost/belegpruefung/LiterarischKuenstlerisch';
import { Abi29BelegpruefungProjektkurse } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungProjektkurse';
import { GostAbiturFach } from '../../../core/types/gost/GostAbiturFach';
import { ArrayMap } from '../../../core/adt/map/ArrayMap';
import { Deutsch } from '../../../core/abschluss/gost/belegpruefung/Deutsch';
import { Fachkombinationen } from '../../../core/abschluss/gost/belegpruefung/Fachkombinationen';
import { Abi29BelegpruefungMathematik } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungMathematik';
import { GostJahrgangFachkombination } from '../../../core/data/gost/GostJahrgangFachkombination';
import { AbiturKursMarkierung } from '../../../core/data/gost/AbiturKursMarkierung';
import { Abi29BelegpruefungLatinum } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungLatinum';
import { Note } from '../../../asd/types/Note';
import { Class } from '../../../java/lang/Class';
import type { JavaMap } from '../../../java/util/JavaMap';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import { FachWaehlbar } from '../../../core/abschluss/gost/belegpruefung/FachWaehlbar';
import type { JavaSet } from '../../../java/util/JavaSet';
import { Abi29BelegpruefungDeutsch } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungDeutsch';
import { Abi29BelegpruefungFachWaehlbar } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungFachWaehlbar';
import { GostBelegpruefungsArt } from '../../../core/abschluss/gost/GostBelegpruefungsArt';
import { AbiturFachbelegungHalbjahr } from '../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostBesondereLernleistung } from '../../../core/types/gost/GostBesondereLernleistung';
import { GostSchuelerFachwahl } from '../../../core/data/gost/GostSchuelerFachwahl';
import { Abi29BelegpruefungAbiFaecher } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungAbiFaecher';
import { GostJahrgangsdaten } from '../../../core/data/gost/GostJahrgangsdaten';
import { NullPointerException } from '../../../java/lang/NullPointerException';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { GostSchriftlichkeit } from '../../../core/types/gost/GostSchriftlichkeit';
import { Abi29BelegpruefungGesellschaftswissenschaftenUndReligion } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungGesellschaftswissenschaftenUndReligion';
import { GesellschaftswissenschaftenUndReligion } from '../../../core/abschluss/gost/belegpruefung/GesellschaftswissenschaftenUndReligion';
import { ServerMode } from '../../../core/types/ServerMode';
import { GostBelegungsfehler } from '../../../core/abschluss/gost/GostBelegungsfehler';
import { GostFach } from '../../../core/data/gost/GostFach';
import { Abi29BelegpruefungFremdsprachen } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungFremdsprachen';
import { AbiturFachbelegung } from '../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefung } from '../../../core/abschluss/gost/GostBelegpruefung';
import { Abi29GostAbiturMarkierungsalgorithmus } from '../../../core/abschluss/gost/Abi29GostAbiturMarkierungsalgorithmus';
import { Abiturdaten } from '../../../core/data/gost/Abiturdaten';
import { Projektkurse } from '../../../core/abschluss/gost/belegpruefung/Projektkurse';
import { SprachendatenUtils } from '../../../core/utils/schueler/SprachendatenUtils';
import { AbiturBelegungsart } from '../../../core/types/gost/AbiturBelegungsart';
import { Abi29BelegpruefungLiterarischKuenstlerisch } from '../../../core/abschluss/gost/belegpruefung/abi2029/Abi29BelegpruefungLiterarischKuenstlerisch';
import { Fremdsprachen } from '../../../core/abschluss/gost/belegpruefung/Fremdsprachen';
import { NoteKatalogEintrag } from '../../../asd/data/NoteKatalogEintrag';
import { GostAbiturMarkierungsalgorithmusErgebnis } from '../../../core/abschluss/gost/GostAbiturMarkierungsalgorithmusErgebnis';
import { GostBelegpruefungErgebnisFehler } from '../../../core/abschluss/gost/GostBelegpruefungErgebnisFehler';
import { Mathematik } from '../../../core/abschluss/gost/belegpruefung/Mathematik';

export class AbiturdatenManager extends JavaObject {

	/**
	 * der Modus, in welchem der Server betrieben wird, um einen experimentellen Modus zu unterstützen.
	 */
	private readonly servermode : ServerMode;

	/**
	 * Das Abiturdaten-Objekt, welches mithilfe dieses Managers bearbeitet wird
	 */
	private readonly abidaten : Abiturdaten;

	/**
	 * Die Informationen zu den Jahrgangsdaten, sofern welche vorhanden sind - ansonsten null
	 */
	private readonly _jahrgangsdaten : GostJahrgangsdaten | null;

	/**
	 * Der Manager für die Fächer und Fachkombinationen der gymnasialen Oberstufe für diesen Abiturjahrgang
	 */
	private readonly faecherManager : GostFaecherManager;

	/**
	 * Die Art der durchzuführenden Belegprüfung
	 */
	private readonly pruefungsArt : GostBelegpruefungsArt;

	/**
	 * Eine HashMap, welche den schnellen Zugriff auf eine Fachbelegung anhand der Fach-ID ermöglicht
	 */
	private readonly mapFachbelegungByFachID : JavaMap<number, AbiturFachbelegung> = new HashMap<number, AbiturFachbelegung>();

	/**
	 * Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen für ein Fach-Kürzel ermöglicht (können mehrere sein - Beispiel billingual mit Wechsel
	 */
	private readonly mapFachbelegungenByFachKuerzel : JavaMap<string, List<AbiturFachbelegung>> = new HashMap<string, List<AbiturFachbelegung>>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Fachbelegungen, welche in dem Halbjahr eine gültige Belegung haben
	 */
	private readonly mapFachbelegungenByGostHalbjahr : JavaMap<GostHalbjahr, ArrayList<AbiturFachbelegung>> = new ArrayMap<GostHalbjahr, ArrayList<AbiturFachbelegung>>(GostHalbjahr.values());

	/**
	 * Eine HashMap2D für den schnellen Zugriff auf die Halbjahresbelegungen anhand der Fach-UD und der ID des GostHalbjahres
	 */
	private readonly mapFachbelegungHalbjahrByFachIDAndHalbjahrID : HashMap2D<number, number, AbiturFachbelegungHalbjahr> = new HashMap2D<number, number, AbiturFachbelegungHalbjahr>();

	/**
	 * Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen über den Fachbereich ermöglicht
	 */
	private readonly mapFachbereiche : JavaMap<GostFachbereich, ArrayList<AbiturFachbelegung>> = new ArrayMap<GostFachbereich, ArrayList<AbiturFachbelegung>>(GostFachbereich.values());

	/**
	 * Eine HashMap, welche den schnellen Zugriff auf die Prüfungsordnungs-relevanten Fachbelegungen über den Fachbereich ermöglicht
	 */
	private readonly mapFachbereicheRelevant : JavaMap<GostFachbereich, ArrayList<AbiturFachbelegung>> = new ArrayMap<GostFachbereich, ArrayList<AbiturFachbelegung>>(GostFachbereich.values());

	/**
	 * Die Prüfungsergebnisse der einzelnen Teilprüfungen der Belegprüfung
	 */
	private belegpruefungen : List<GostBelegpruefung> = new ArrayList<GostBelegpruefung>();

	/**
	 * Die zuletzt durchgeführte Belegprüfung bezüglich der Kurszahlen und der Wochenstunden
	 */
	private belegpruefungKurszahlenUndWochenstunden : KurszahlenUndWochenstunden | null = null;

	/**
	 * Die zuletzt durchgeführte Belegprüfung bezüglich der Kurszahlen und der Wochenstunden - experimenteller Code
	 */
	private abi29BelegpruefungKurszahlenUndWochenstunden : Abi29BelegpruefungKurszahlenUndWochenstunden | null = null;

	/**
	 * Die Menge der Belegprüfungsfehler, die bei den durchgeführten Belegprüfungen aufgetreten sind.
	 */
	private belegpruefungsfehler : List<GostBelegungsfehler> = new ArrayList<GostBelegungsfehler>();

	/**
	 * Gibt an, ob die Belegprüfung insgesamt erfolgreich war oder nicht.
	 */
	private belegpruefungErfolgreich : boolean = false;

	/**
	 * Das Ergebnis des Markierungsalgorithmus
	 */
	private markierungsErgebnis : GostAbiturMarkierungsalgorithmusErgebnis = new GostAbiturMarkierungsalgorithmusErgebnis();


	/**
	 * Erstellt ein neues Manager-Objekt, welches mit den übergebenen Abiturdaten verknüpft wird.
	 *
	 * @param servermode     der Modus, in welchem der Server betrieben wird, um einen experimentellen Modus zu unterstützen
	 * @param abidaten       die Abiturdaten
	 * @param gostJahrgang   die Informationen zu dem Abiturjahrgang
	 * @param faecherManager der Manager für die Fächer und Fachkombinationen der Gymnasialen Oberstufe
	 * @param pruefungsArt   die Art der Belegpruefung (z.B. EF1 oder GESAMT)
	 */
	public constructor(servermode : ServerMode, abidaten : Abiturdaten, gostJahrgang : GostJahrgangsdaten | null, faecherManager : GostFaecherManager, pruefungsArt : GostBelegpruefungsArt) {
		super();
		this.servermode = servermode;
		this.abidaten = abidaten;
		this._jahrgangsdaten = gostJahrgang;
		this.faecherManager = faecherManager;
		this.pruefungsArt = pruefungsArt;
		this.init();
	}

	/**
	 * Initialisiert die Belegprüfung der Art pruefungs_art für einen Schüler durch, dessen Abiturdaten mit dem angegebenen
	 * Manager verwaltet werden.
	 *
	 * @param pruefungsArt    die Art der Prüfung, die durchgeführt wird
	 *
	 * @return eine Liste mit den durchgefuehrten Belegpruefungen
	 */
	private getPruefungenDefault(pruefungsArt : GostBelegpruefungsArt) : List<GostBelegpruefung> {
		const pruefungen : ArrayList<GostBelegpruefung> = new ArrayList<GostBelegpruefung>();
		pruefungen.add(new Deutsch(this, pruefungsArt));
		const pruefungFremdsprachen : Fremdsprachen = new Fremdsprachen(this, pruefungsArt);
		pruefungen.add(pruefungFremdsprachen);
		pruefungen.add(new Latinum(this, pruefungsArt));
		pruefungen.add(new LiterarischKuenstlerisch(this, pruefungsArt));
		pruefungen.add(new GesellschaftswissenschaftenUndReligion(this, pruefungsArt));
		pruefungen.add(new Mathematik(this, pruefungsArt));
		const pruefungNaturwissenschaften : Naturwissenschaften = new Naturwissenschaften(this, pruefungsArt);
		pruefungen.add(pruefungNaturwissenschaften);
		pruefungen.add(new Sport(this, pruefungsArt));
		const pruefungProjektkurse : Projektkurse = new Projektkurse(this, pruefungsArt);
		pruefungen.add(pruefungProjektkurse);
		pruefungen.add(new Schwerpunkt(this, pruefungsArt, pruefungFremdsprachen, pruefungNaturwissenschaften));
		pruefungen.add(new AbiFaecher(this, pruefungsArt));
		this.belegpruefungKurszahlenUndWochenstunden = new KurszahlenUndWochenstunden(this, pruefungsArt, pruefungProjektkurse);
		pruefungen.add(this.belegpruefungKurszahlenUndWochenstunden);
		pruefungen.add(new Allgemeines(this, pruefungsArt));
		pruefungen.add(new Fachkombinationen(this, pruefungsArt));
		pruefungen.add(new FachWaehlbar(this, pruefungsArt));
		return pruefungen;
	}

	/**
	 * Einbinden von experimentellem Code:
	 * Initialisiert die Belegprüfung der Art pruefungs_art für einen Schüler durch, dessen Abiturdaten mit dem angegebenen
	 * Manager verwaltet werden.
	 *
	 * @param pruefungsArt    die Art der Prüfung, die durchgeführt wird
	 *
	 * @return eine Liste mit den durchgefuehrten Belegpruefungen
	 */
	private getPruefungenAbi2029(pruefungsArt : GostBelegpruefungsArt) : List<GostBelegpruefung> {
		const pruefungen : ArrayList<GostBelegpruefung> = new ArrayList<GostBelegpruefung>();
		pruefungen.add(new Abi29BelegpruefungDeutsch(this, pruefungsArt));
		const pruefungFremdsprachen : Abi29BelegpruefungFremdsprachen = new Abi29BelegpruefungFremdsprachen(this, pruefungsArt);
		pruefungen.add(pruefungFremdsprachen);
		pruefungen.add(new Abi29BelegpruefungLatinum(this, pruefungsArt));
		pruefungen.add(new Abi29BelegpruefungLiterarischKuenstlerisch(this, pruefungsArt));
		pruefungen.add(new Abi29BelegpruefungGesellschaftswissenschaftenUndReligion(this, pruefungsArt));
		pruefungen.add(new Abi29BelegpruefungMathematik(this, pruefungsArt));
		const pruefungNaturwissenschaften : Abi29BelegpruefungNaturwissenschaften = new Abi29BelegpruefungNaturwissenschaften(this, pruefungsArt);
		pruefungen.add(pruefungNaturwissenschaften);
		pruefungen.add(new Abi29BelegpruefungSport(this, pruefungsArt));
		const pruefungProjektkurse : Abi29BelegpruefungProjektkurse = new Abi29BelegpruefungProjektkurse(this, pruefungsArt);
		pruefungen.add(pruefungProjektkurse);
		pruefungen.add(new Abi29BelegpruefungSchwerpunkt(this, pruefungsArt, pruefungFremdsprachen, pruefungNaturwissenschaften));
		pruefungen.add(new Abi29BelegpruefungAbiFaecher(this, pruefungsArt, pruefungProjektkurse));
		this.abi29BelegpruefungKurszahlenUndWochenstunden = new Abi29BelegpruefungKurszahlenUndWochenstunden(this, pruefungsArt, pruefungProjektkurse);
		pruefungen.add(this.abi29BelegpruefungKurszahlenUndWochenstunden);
		pruefungen.add(new Abi29BelegpruefungAllgemeines(this, pruefungsArt));
		pruefungen.add(new Abi29BelegpruefungFachkombinationen(this, pruefungsArt));
		pruefungen.add(new Abi29BelegpruefungFachWaehlbar(this, pruefungsArt));
		return pruefungen;
	}

	/**
	 * Führt die Belegprüfung der Art pruefungs_art für einen Schüler durch, dessen Abiturdaten mit dem angegebenen
	 * Manager verwaltet werden.
	 *
	 * @param pruefungsArt    die Art der Prüfung, die durchgeführt wird
	 *
	 * @return eine Liste mit den durchgefuehrten Belegpruefungen
	 */
	public getPruefungen(pruefungsArt : GostBelegpruefungsArt) : List<GostBelegpruefung> {
		if ((this.abidaten.abiturjahr >= 2029) && (this.servermode as unknown === ServerMode.DEV as unknown))
			return this.getPruefungenAbi2029(pruefungsArt);
		return this.getPruefungenDefault(pruefungsArt);
	}

	/**
	 * Initialisiert bzw. reinitialisert die Datenstrukturen, die für den schnellen Zugriff auf die Daten
	 * eingerichtet werden.
	 */
	public init() : void {
		if (this.abidaten === null)
			return;
		this.initMaps();
		this.belegpruefungen = this.getPruefungen(this.pruefungsArt);
		for (const belegpruefung of this.belegpruefungen)
			belegpruefung.pruefe();
		this.belegpruefungsfehler = GostBelegpruefung.getBelegungsfehlerAlle(this.belegpruefungen);
		this.belegpruefungErfolgreich = GostBelegpruefung.istErfolgreich(this.belegpruefungsfehler);
		if (this.istBewertetQualifikationsPhase()) {
			if ((this.abidaten.abiturjahr >= 2029) && (this.servermode as unknown === ServerMode.DEV as unknown)) {
				this.markierungsErgebnis = Abi29GostAbiturMarkierungsalgorithmus.berechne(this, this.belegpruefungen);
			} else {
				this.markierungsErgebnis = GostAbiturMarkierungsalgorithmus.berechne(this, this.belegpruefungen);
			}
		}
	}

	/**
	 * Initialisiert bzw. reinitialisiert die Map für den schnellen Zugriff auf Fachbelegungen
	 * anhand des Fachbereichs, der Fach-ID oder des Halbjahres.
	 */
	private initMaps() : void {
		this.mapFachbelegungByFachID.clear();
		this.mapFachbelegungenByFachKuerzel.clear();
		this.mapFachbelegungHalbjahrByFachIDAndHalbjahrID.clear();
		this.mapFachbereiche.clear();
		this.mapFachbereicheRelevant.clear();
		for (const fachbereich of GostFachbereich.values()) {
			this.mapFachbereiche.put(fachbereich, new ArrayList<AbiturFachbelegung>());
			this.mapFachbereicheRelevant.put(fachbereich, new ArrayList<AbiturFachbelegung>());
		}
		this.mapFachbelegungenByGostHalbjahr.clear();
		for (const halbjahr of GostHalbjahr.values())
			this.mapFachbelegungenByGostHalbjahr.put(halbjahr, new ArrayList<AbiturFachbelegung>());
		const fachbelegungen : List<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fachbelegung of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fachbelegung);
			if (fach === null)
				continue;
			this.mapFachbelegungByFachID.put(fachbelegung.fachID, fachbelegung);
			const tmpListFachbelegungenByFachKuerzel : List<AbiturFachbelegung> | null = this.mapFachbelegungenByFachKuerzel.computeIfAbsent(fach.kuerzel, { apply : (k: string | null) => new ArrayList() });
			if (tmpListFachbelegungenByFachKuerzel !== null)
				tmpListFachbelegungenByFachKuerzel.add(fachbelegung);
			if (this.zaehleBelegung(fachbelegung) > 0) {
				const fachbereiche : List<GostFachbereich> = GostFachbereich.getBereiche(fach);
				for (const fachbereich of fachbereiche) {
					let listFachbelegungen : List<AbiturFachbelegung> | null = this.mapFachbereiche.get(fachbereich);
					if (listFachbelegungen !== null)
						listFachbelegungen.add(fachbelegung);
					if (fach.istPruefungsordnungsRelevant) {
						listFachbelegungen = this.mapFachbereicheRelevant.get(fachbereich);
						if (listFachbelegungen !== null)
							listFachbelegungen.add(fachbelegung);
					}
				}
			}
			for (const halbjahr of GostHalbjahr.values()) {
				const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr === null) || (belegungHalbjahr.kursartKuerzel === null))
					continue;
				this.mapFachbelegungHalbjahrByFachIDAndHalbjahrID.put(fachbelegung.fachID, halbjahr.id, belegungHalbjahr);
				if (halbjahr.istQualifikationsphase() && (Note.fromKuerzel(belegungHalbjahr.notenkuerzel) as unknown === Note.UNGENUEGEND as unknown))
					continue;
				const tmp : List<AbiturFachbelegung> | null = this.mapFachbelegungenByGostHalbjahr.get(halbjahr);
				if (tmp === null)
					continue;
				tmp.add(fachbelegung);
			}
		}
	}

	/**
	 * Fasst die bilingualen und nicht biligualen Fachbelegungen unter dem Fach der
	 * letzten Halbjahresbelegung zusammen und initialisiert den Manager danach neu.
	 *
	 * @return true, wenn das Kombinieren erfolgreich durchgeführt wurde, und ansonsten false
	 */
	public kombiniereFachbelegungenEinesFaches() : boolean {
		for (const belegungen of this.mapFachbelegungenByFachKuerzel.values()) {
			if (belegungen.size() < 2)
				continue;
			let ziel : AbiturFachbelegung | null = null;
			let letztes : GostHalbjahr = GostHalbjahr.EF2;
			for (const belegung of this.abidaten.fachbelegungen) {
				let halbjahr : GostHalbjahr | null = letztes.next();
				while (halbjahr !== null) {
					const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = belegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr !== null) && (belegungHalbjahr.kursartKuerzel !== null) && (belegungHalbjahr.notenkuerzel !== null) && (!JavaObject.equalsTranspiler("", (belegungHalbjahr.notenkuerzel)))) {
						ziel = belegung;
						letztes = halbjahr;
					}
					halbjahr = halbjahr.next();
				}
			}
			if (ziel === null)
				continue;
			for (const belegung of this.abidaten.fachbelegungen) {
				if (belegung as unknown === ziel as unknown)
					continue;
				for (const halbjahr of GostHalbjahr.values()) {
					const zielHalbjahr : AbiturFachbelegungHalbjahr | null = ziel.belegungen[halbjahr.id];
					if ((zielHalbjahr !== null) && (zielHalbjahr.kursartKuerzel !== null) && (zielHalbjahr.notenkuerzel !== null) && (!JavaObject.equalsTranspiler("", (zielHalbjahr.notenkuerzel))))
						continue;
					const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = belegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr !== null) && (belegungHalbjahr.kursartKuerzel !== null) && (belegungHalbjahr.notenkuerzel !== null)) {
						ziel.belegungen[halbjahr.id] = belegungHalbjahr;
					}
				}
				this.abidaten.fachbelegungen.remove(belegung);
			}
		}
		this.init();
		return true;
	}

	/**
	 * Gibt die von diesem Manager verwalteten Abiturdaten zurück.
	 *
	 * @return die Abiturdaten des Managers
	 */
	public daten() : Abiturdaten {
		return this.abidaten;
	}

	/**
	 * Gibt den zugehörigen Fächer-Manager zurück.
	 *
	 * @return der Fächer-Manager
	 */
	public faecher() : GostFaecherManager {
		return this.faecherManager;
	}

	/**
	 * Gibt die Jahrgangsdaten für den Abiturjahrgang zurück.
	 *
	 * @return die Jahrgangsdaten
	 *
	 * @throws DeveloperNotificationException falls keine Jahrgangsdaten vorliegen
	 */
	public jahrgangsdaten() : GostJahrgangsdaten {
		if (this._jahrgangsdaten === null)
			throw new DeveloperNotificationException("Es liegen keine Jahrgangsdaten vor.")
		return this._jahrgangsdaten;
	}

	/**
	 * gibt die gewählte Prüfungsart zurück
	 *
	 * @return entweder Gesamtprüfung oder EF1-Prüfung
	 */
	public getPruefungsArt() : GostBelegpruefungsArt {
		return this.pruefungsArt;
	}

	/**
	 * Gibt zurück, ob das angegebene Halbjahr bereits bewertet ist oder nicht.
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return true, falls es bereits bewertet ist
	 */
	public istBewertet(halbjahr : GostHalbjahr) : boolean {
		return this.abidaten.bewertetesHalbjahr[halbjahr.id];
	}

	/**
	 * Gibt zurück, ob alle Halbjahr der Qualifikationsphase bewertet sind oder nicht.
	 *
	 * @return true, falls alle Halbjahre bewertet sind, und ansonsten false
	 */
	public istBewertetQualifikationsPhase() : boolean {
		for (const hj of GostHalbjahr.getQualifikationsphase())
			if (!this.istBewertet(hj))
				return false;
		return true;
	}

	/**
	 * Liefert die in den Abiturdaten enthaltenen Sprachendaten.
	 *
	 * @return Die Sprachendaten (siehe {@link Sprachendaten})
	 */
	public getSprachendaten() : Sprachendaten {
		return this.abidaten.sprachendaten;
	}

	/**
	 * Liefert das Abiturjahr
	 *
	 * @return das Abiturjahr
	 */
	public getAbiturjahr() : number {
		return this.abidaten.abiturjahr;
	}

	/**
	 * Liefert das Schuljahr, in dem das Abitur gemacht wird
	 *
	 * @return das Schuljahr
	 */
	public getSchuljahr() : number {
		return this.abidaten.schuljahrAbitur;
	}

	/**
	 * Gibt zurück, ob es sich bei der Halbjahresbelegung um eine Belegung handelt, welche mit
	 * null Punkten abgeschlossen wurde und welche daher als nicht belegter Kurs zu werten ist.
	 *
	 * @param halbjahresbelegung   die Halbjahresbelegung eines Kurses
	 *
	 * @return true, fall es sich um einen Null-Punkte-Kurs in der Qualifikationsphase handelt.
	 */
	public static istNullPunkteBelegungInQPhase(halbjahresbelegung : AbiturFachbelegungHalbjahr) : boolean {
		const hj : GostHalbjahr | null = GostHalbjahr.fromKuerzel(halbjahresbelegung.halbjahrKuerzel);
		if ((hj === null) || (hj.istEinfuehrungsphase()))
			return false;
		return Note.fromKuerzel(halbjahresbelegung.notenkuerzel) as unknown === Note.UNGENUEGEND as unknown;
	}

	private static getSchuelerFachwahlFromBelegung(belegung : AbiturFachbelegung, halbjahr : GostHalbjahr) : string | null {
		let halbjahresbelegung : AbiturFachbelegungHalbjahr | null = belegung.belegungen[halbjahr.id];
		if (halbjahresbelegung === null) {
			halbjahresbelegung = new AbiturFachbelegungHalbjahr();
			halbjahresbelegung.halbjahrKuerzel = halbjahr.kuerzel;
			belegung.belegungen[halbjahr.id] = halbjahresbelegung;
		}
		const kursart : GostKursart | null = GostKursart.fromKuerzel(halbjahresbelegung.kursartKuerzel);
		if (kursart === null)
			return (JavaObject.equalsTranspiler("", (halbjahresbelegung.kursartKuerzel)) ? null : halbjahresbelegung.kursartKuerzel);
		if (AbiturdatenManager.istNullPunkteBelegungInQPhase(halbjahresbelegung))
			return null;
		if ((kursart as unknown === GostKursart.ZK as unknown) || (kursart as unknown === GostKursart.LK as unknown))
			return kursart.kuerzel;
		return halbjahresbelegung.schriftlich ? "S" : "M";
	}

	/**
	 * Bestimmt die Schüler-Fachwahl für das Fach mit der übergebenen ID
	 *
	 * @param fachID   die ID des Faches
	 *
	 * @return die Schüler-Fachwahl
	 */
	public getSchuelerFachwahl(fachID : number) : GostSchuelerFachwahl {
		const belegung : AbiturFachbelegung | null = this.getFachbelegungByID(fachID);
		if (belegung === null)
			return new GostSchuelerFachwahl();
		const wahl : GostSchuelerFachwahl = new GostSchuelerFachwahl();
		wahl.halbjahre[0] = AbiturdatenManager.getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.EF1);
		wahl.halbjahre[1] = AbiturdatenManager.getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.EF2);
		wahl.halbjahre[2] = AbiturdatenManager.getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q11);
		wahl.halbjahre[3] = AbiturdatenManager.getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q12);
		wahl.halbjahre[4] = AbiturdatenManager.getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q21);
		wahl.halbjahre[5] = AbiturdatenManager.getSchuelerFachwahlFromBelegung(belegung, GostHalbjahr.Q22);
		wahl.abiturFach = belegung.abiturFach;
		return wahl;
	}

	/**
	 * Bestimmt die Schüler-Fachwahlen aller belegten Fächer.
	 *
	 * @return die Map mit den Schüler-Fachwahlen
	 */
	public getSchuelerFachwahlen() : JavaMap<number, GostSchuelerFachwahl> {
		const fachwahlen : HashMap<number, GostSchuelerFachwahl> = new HashMap<number, GostSchuelerFachwahl>();
		const fachbelegungen : List<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fb of fachbelegungen)
			fachwahlen.put(fb.fachID, this.getSchuelerFachwahl(fb.fachID));
		return fachwahlen;
	}

	/**
	 * Liefert das Fach der gymnasialen Oberstufe für die angegeben Abiturfachbelegung.
	 *
	 * @param belegung   die Fachbelegung (siehe {@link AbiturFachbelegung})
	 *
	 * @return das Fach der gymnasialen Oberstufe (siehe {@link GostFach})
	 */
	public getFach(belegung : AbiturFachbelegung | null) : GostFach | null {
		if (belegung === null)
			return null;
		return this.faecherManager.get(belegung.fachID);
	}

	/**
	 * Prüft, ob in den Abiturdaten mindestens eine Fachbelegung existiert oder nicht.
	 * Wichtig: Bei dieser Methode wird auch eine 0-Punkte-Belegung als Fachbelegung gezählt!
	 *
	 * @return true, wenn mindestens eine Fachbelegung existiert, und ansonsten false
	 */
	public existsFachbelegung() : boolean {
		const fachbelegungen : List<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if (fach === null)
				continue;
			for (let i : number = 0; i < GostHalbjahr.maxHalbjahre; i++)
				if (fb.belegungen[i] !== null)
					return true;
		}
		return false;
	}

	/**
	 * Prüft, ob das Fach in allen angegebenen Halbjahren belegt wurde.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachbelegung
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls das Fach in den Halbjahren belegt wurde, sonst false
	 */
	public pruefeBelegung(fachbelegung : AbiturFachbelegung | null, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if (halbjahre.length === 0)
			return true;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr === null) || (belegungHalbjahr.kursartKuerzel === null))
				return false;
			if (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr))
				return false;
		}
		return true;
	}

	/**
	 * Bestimmt die Anzahl der Fachbelegungen, die dem Fach zugeordnet sind.
	 * Wird keine gültige Fachbelegung übergeben, so wird 0 zurückgegeben.
	 *
	 * @param fachbelegung   die Fachbelegung
	 *
	 * @return die Anzahl der Belegungen des Faches
	 */
	public zaehleBelegung(fachbelegung : AbiturFachbelegung | null) : number {
		if (fachbelegung === null)
			return 0;
		let anzahl : number = 0;
		for (let i : number = 0; i < GostHalbjahr.maxHalbjahre; i++) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[i];
			if ((belegungHalbjahr !== null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				anzahl++;
		}
		return anzahl;
	}

	/**
	 * Zählt die Anzahl der Halbjahresbelegungen für die angegebene Fachbelegung in den angegeben Halbjahren.
	 * Ist die Fachbelegung null, so wird 0 zurückgegeben. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so wird ebenfalls 0 zurückgegeben.
	 *
	 * @param fachbelegung   die Fachbelegung
	 * @param halbjahre      die Halbjahre
	 *
	 * @return die Anzahl der Belegungen in den Halbjahren
	 */
	public zaehleHalbjahresbelegungen(fachbelegung : AbiturFachbelegung | null, ...halbjahre : Array<GostHalbjahr>) : number {
		if (fachbelegung === null)
			return 0;
		if (halbjahre.length === 0)
			return 0;
		let anzahl : number = 0;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr !== null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				anzahl++;
		}
		return anzahl;
	}

	/**
	 * Zählt die Anzahl der Belegungen für die angegebenen Fachbelegungen in den angegeben Halbjahren.
	 * Ist die Fachbelegung null, so wird 0 zurückgegeben. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so wird ebenfalls 0 zurückgegeben.
	 *
	 * @param fachbelegungen      die Fachbelegungen
	 * @param halbjahre           die Halbjahre
	 *
	 * @return die Anzahl der Belegungen in den Halbjahren und den Fächern
	 */
	public zaehleBelegungInHalbjahren(fachbelegungen : List<AbiturFachbelegung> | null, ...halbjahre : Array<GostHalbjahr>) : number {
		if (fachbelegungen === null)
			return 0;
		if (halbjahre.length === 0)
			return 0;
		let anzahl : number = 0;
		for (const fachbelegung of fachbelegungen) {
			for (const halbjahr of halbjahre) {
				const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr !== null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
					anzahl++;
			}
		}
		return anzahl;
	}

	/**
	 * Zählt die Anzahl der 0-Punkte-Belegungen aller Fachbelegungen in der Qualifikationsphase.
	 *
	 * @return die Anzahl der 0-Punkte-Belegungen aller Fachbelegungen in der Qualifikationsphase
	 */
	public zaehleNullPunkteBelegungenInQPhase() : number {
		let anzahl : number = 0;
		for (const fachbelegung of this.abidaten.fachbelegungen) {
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr !== null) && (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
					anzahl++;
			}
		}
		return anzahl;
	}

	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Kursart entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungMitKursart(fachbelegung : AbiturFachbelegung | null, kursart : GostKursart, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if (halbjahre.length === 0)
			return true;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr === null) || (kursart as unknown !== GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel) as unknown) || (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				return false;
		}
		return true;
	}

	/**
	 * Prüft, ob eine Fachbelegung existiert, welche in den angegebenen Halbjahren der angegebenen Kursart
	 * entspricht.
	 * Ist keine Fachbelegung angegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen
	 * Fachbelegung kein Halbjahr angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungExistiertMitKursart(fachbelegungen : List<AbiturFachbelegung> | null, kursart : GostKursart, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if ((fachbelegungen === null) || (fachbelegungen.isEmpty()))
			return false;
		if (halbjahre.length === 0)
			return true;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungMitKursart(fachbelegung, kursart, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren mindestens einmal die angegebenen Kursart hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Kursart nicht
	 * einmal existiert.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Kursart mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungHatMindestensEinmalKursart(fachbelegung : AbiturFachbelegung | null, kursart : GostKursart, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if (halbjahre.length === 0)
			return false;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if (belegungHalbjahr === null)
				continue;
			if ((kursart as unknown === GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel) as unknown) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung des Faches in dem angegebenen Halbjahr der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahr          das zu prüfende Halbjahr
	 *
	 * @return true, falls die Schriftlichkeit in dem Halbjahr gegeben ist, sonst false
	 */
	public pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung : AbiturFachbelegung | null, schriftlichkeit : GostSchriftlichkeit, halbjahr : GostHalbjahr) : boolean {
		if (fachbelegung === null)
			return false;
		const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
		if ((belegungHalbjahr === null) || (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
			return false;
		switch (schriftlichkeit) {
			case GostSchriftlichkeit.BELIEBIG: {
				return true;
			}
			case GostSchriftlichkeit.SCHRIFTLICH: {
				return belegungHalbjahr.schriftlich;
			}
			case GostSchriftlichkeit.MUENDLICH: {
				return !belegungHalbjahr.schriftlich;
			}
			default: {
				return false;
			}
		}
	}

	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungMitSchriftlichkeit(fachbelegung : AbiturFachbelegung | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if (halbjahre.length === 0)
			return true;
		for (const halbjahr of halbjahre)
			if (!this.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, schriftlichkeit, halbjahr))
				return false;
		return true;
	}

	/**
	 * Prüft, ob eine Belegung des Faches in den angegebenen Halbjahren nicht der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren nicht gegeben ist, sonst false
	 */
	public pruefeBelegungErfuelltNicht(fachbelegung : AbiturFachbelegung | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if (halbjahre.length === 0)
			return true;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr === null) || (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)) || ((schriftlichkeit as unknown !== GostSchriftlichkeit.BELIEBIG as unknown) && (((schriftlichkeit as unknown === GostSchriftlichkeit.SCHRIFTLICH as unknown) && (!belegungHalbjahr.schriftlich)) || ((schriftlichkeit as unknown === GostSchriftlichkeit.MUENDLICH as unknown) && (belegungHalbjahr.schriftlich)))))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob eine Belegung des Faches in den angegebenen Halbjahren nicht der angegebenen Schriftlichkeit entspricht,
	 * sofern es in dem Halbjahr überhaupt belegt wurde..
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren nicht gegeben ist, sonst false
	 */
	public pruefeBelegungErfuelltNichtFallsBelegt(fachbelegung : AbiturFachbelegung | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if (halbjahre.length === 0)
			return true;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr === null) || (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				continue;
			if (((schriftlichkeit as unknown !== GostSchriftlichkeit.BELIEBIG as unknown) && (((schriftlichkeit as unknown === GostSchriftlichkeit.SCHRIFTLICH as unknown) && (!belegungHalbjahr.schriftlich)) || ((schriftlichkeit as unknown === GostSchriftlichkeit.MUENDLICH as unknown) && (belegungHalbjahr.schriftlich)))))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren mindestens einmal die angegebene Schritflichkeit hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Schriftlichkeit nicht
	 * einmal existiert.
	 *
	 * @param fachbelegung      die zu prüfende Fachbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die angegebene Schriftlichkeit mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungHatMindestensEinmalSchriftlichkeit(fachbelegung : AbiturFachbelegung | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if (halbjahre.length === 0)
			return false;
		for (const halbjahr of halbjahre) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr === null) || (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				continue;
			if ((schriftlichkeit.istSchriftlich === null) || (JavaObject.equalsTranspiler(schriftlichkeit.istSchriftlich, (belegungHalbjahr.schriftlich))))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob eine Fachbelegung existiert, welche in den angegebenen Halbjahren mindestens einmal die angegebene
	 * Schritflichkeit hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Schriftlichkeit nicht
	 * einmal existiert.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die angegebene Schriftlichkeit bei einer Fachbelegung mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungExistiertHatMindestensEinmalSchriftlichkeit(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if ((fachbelegungen === null) || (fachbelegungen.isEmpty()))
			return false;
		if (halbjahre.length === 0)
			return false;
		for (const fachbelegung of fachbelegungen)
			if (this.pruefeBelegungHatMindestensEinmalSchriftlichkeit(fachbelegung, schriftlichkeit, ...halbjahre))
				return true;
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit den angegebenen Halbjahren existiert.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist z.B. bei bilingualen
	 * Fächern nötig oder bei der Unterscheidung von Sport-Profilen.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls eine Fachbelegung mit den Halbjahren existiert, sonst false
	 */
	public pruefeBelegungExistiert(fachbelegungen : List<AbiturFachbelegung> | null, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		if ((halbjahre === null) || (halbjahre.length === 0))
			return true;
		for (const fachbelegung of fachbelegungen) {
			const fach : GostFach | null = this.faecherManager.get(fachbelegung.fachID);
			if (fach === null)
				continue;
			const alleBelegungen : List<AbiturFachbelegung> | null = this.getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen === null) || (alleBelegungen.isEmpty()))
				continue;
			let hatBelegung : boolean = true;
			for (const halbjahr of halbjahre) {
				let hatHalbjahresBelegung : boolean = false;
				for (const aktFachbelegung of alleBelegungen) {
					const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = aktFachbelegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr !== null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr))) {
						hatHalbjahresBelegung = true;
						break;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit dem angegebenen Halbjahr existiert.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist z.B. bei bilingualen
	 * Fächern nötig oder bei der Unterscheidung von Sport-Profilen.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param halbjahr          das zu prüfende Halbjahr
	 *
	 * @return true, falls eine Fachbelegung mit dem Halbjahr existiert, sonst false
	 */
	public pruefeBelegungExistiertEinzeln(fachbelegungen : List<AbiturFachbelegung> | null, halbjahr : GostHalbjahr) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			const fach : GostFach | null = this.faecherManager.get(fachbelegung.fachID);
			if (fach === null)
				continue;
			const alleBelegungen : List<AbiturFachbelegung> | null = this.getFachbelegungByFachkuerzel(fach.kuerzel);
			if (alleBelegungen.isEmpty())
				continue;
			for (const aktFachbelegung of alleBelegungen) {
				const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = aktFachbelegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr !== null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
					return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit einer durchgängigen Belegung existiert,
	 * die zumindest in der Q1 und der Q2.1 schriftlich ist.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist bei bilingualen
	 * Fächern nötig.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 *
	 * @return true, falls eine durchgehend schriftliche Fachbelegung existiert, sonst false
	 */
	public pruefeBelegungExistiertDurchgehendSchriftlich(fachbelegungen : List<AbiturFachbelegung> | null) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			const fach : GostFach | null = this.faecherManager.get(fachbelegung.fachID);
			if (fach === null)
				continue;
			const alleBelegungen : List<AbiturFachbelegung> | null = this.getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen === null) || (alleBelegungen.isEmpty()))
				continue;
			let hatBelegung : boolean = true;
			for (const halbjahr of GostHalbjahr.values()) {
				let hatHalbjahresBelegung : boolean = false;
				for (const aktFachbelegung of alleBelegungen) {
					const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = aktFachbelegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr !== null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)) && (((halbjahr as unknown !== GostHalbjahr.Q11 as unknown) && (halbjahr as unknown !== GostHalbjahr.Q12 as unknown) && (halbjahr as unknown !== GostHalbjahr.Q21 as unknown)) || (belegungHalbjahr.schriftlich))) {
						hatHalbjahresBelegung = true;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in dem angegebenen Halbjahr der angegebenen Schriftlichkeit entspricht.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahr          das zu prüfende Halbjahr
	 *
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in dem Halbjahr gegeben ist, sonst false
	 */
	public pruefeBelegungExistiertMitSchriftlichkeitEinzeln(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, halbjahr : GostHalbjahr) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, schriftlichkeit, halbjahr))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren mindestens einmal die angegebene Kursart
	 * hat. Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param kursart           die zu prüfende Kursart
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Kursart bei einer Fachbelegung mindestens einmal in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungExistiertHatMindestensEinmalKursart(fachbelegungen : List<AbiturFachbelegung> | null, kursart : GostKursart, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungHatMindestensEinmalKursart(fachbelegung, kursart, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren existiert,
	 * bei welchem in mind. einem der Halbjahren die angebene Schriftlichkeit nicht gegeben ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true oder false (siehe oben)
	 */
	public pruefeBelegungExistiertErfuelltNicht(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungErfuelltNicht(fachbelegung, schriftlichkeit, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren existiert,
	 * bei welchem in mind. einem der Halbjahren die angebene Schriftlichkeit nicht gegeben ist, sofern
	 * das Fach überhaupt belegt wurde.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true oder false (siehe oben)
	 */
	public pruefeBelegungExistiertErfuelltNichtFallsBelegt(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungErfuelltNichtFallsBelegt(fachbelegung, schriftlichkeit, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgehend belegbar ist.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungDurchgehendBelegbar(fachbelegung : AbiturFachbelegung | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegung === null)
			return false;
		if (!GostFachUtils.istDurchgehendBelegbarBisQ22(this.getFach(fachbelegung)))
			return false;
		return this.pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, ...halbjahre);
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgängig belegbar ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungDurchgehendBelegbarExistiert(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegungDurchgehendBelegbar(fachbelegung, schriftlichkeit, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgängig belegt ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich sofern das Fach durchgängig belegt wurde, da kein Halbjahr auf die
	 * Schriftlichkeit geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu prüfenden Fachbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit
	 * @param halbjahre         die zu prüfenden Halbjahre
	 *
	 * @return true, falls bei eine Fachbelegung durchgängig belegt wurde und die Schriftlichkeit in den Halbjahren gegeben ist, sonst false
	 */
	public pruefeBelegungDurchgehendBelegtExistiert(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : boolean {
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeBelegung(fachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22) && this.pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, ...halbjahre))
				return true;
		}
		return false;
	}

	/**
	 * Prüft, ob die Fachbelegung in mindestens einem der Halbjahre die angegebene Kursart aufweist.
	 * Existiert die Fachbelegung nicht (null), so kommt die Kursart auch nicht vor.
	 *
	 * @param fachbelegung   die Fachbelegung
	 * @param kursart        die Kursart
	 *
	 * @return true, falls mindestens einmal die Kursart belegt wurde, sonst false
	 */
	public pruefeAufKursart(fachbelegung : AbiturFachbelegung | null, kursart : GostKursart) : boolean {
		if (fachbelegung === null)
			return false;
		for (const belegunghalbjahr of fachbelegung.belegungen) {
			if ((belegunghalbjahr !== null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegunghalbjahr)) && (GostKursart.fromKuerzel(belegunghalbjahr.kursartKuerzel) as unknown === kursart as unknown))
				return true;
		}
		return false;
	}

	/**
	 * Filtert die Fachbelegungen und gibt nur die Fachbelegungen zurück, bei denen die
	 * Kursart existiert.
	 * Wird keine Fachbelegung übergeben (null oder leere Liste), so kommt auch keine
	 * Belegung mit der Kursart vor.
	 *
	 * @param fachbelegungen   die Fachbelegungen
	 * @param kursart          die Kursart
	 *
	 * @return eine Liste mit den Fachbelegungen, welche die kursart haben
	 */
	public filterBelegungKursartExistiert(fachbelegungen : List<AbiturFachbelegung> | null, kursart : GostKursart) : List<AbiturFachbelegung> {
		const result : ArrayList<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		if ((fachbelegungen === null) || (fachbelegungen.isEmpty()))
			return result;
		for (const fachbelegung of fachbelegungen) {
			if (this.pruefeAufKursart(fachbelegung, kursart))
				result.add(fachbelegung);
		}
		return result;
	}

	/**
	 * Prüft, ob die Fachbelegung eine durchgängige Belegung hat. Zusatzkurse können nicht für eine
	 * durchgängige Belegung zählen.
	 *
	 * @param fachbelegung   die zu prüfende Fachbelegung
	 *
	 * @return true, wenn die Belegung durchgängig ist.
	 */
	public pruefeDurchgaengigkeit(fachbelegung : AbiturFachbelegung | null) : boolean {
		if ((fachbelegung === null) || (this.pruefeAufKursart(fachbelegung, GostKursart.ZK)))
			return false;
		return this.pruefeBelegung(fachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22);
	}

	/**
	 * Zählt die Fachbelegungen, welche eine durchgängige Belegung aufweisen. Zusatzkurse zählen
	 * nicht für eine durchgängige Belegung.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist bei bilingualen
	 * Fächern nötig.
	 *
	 * @param fachbelegungen   die zu überprüfenden Fachbelegungen
	 *
	 * @return die Anzahl der durchgängigen Belegungen
	 */
	public zaehleDurchgaengigeBelegungen(fachbelegungen : List<AbiturFachbelegung> | null) : number {
		if (fachbelegungen === null)
			return 0;
		let anzahl : number = 0;
		for (const fachbelegung of fachbelegungen) {
			const fach : GostFach | null = this.faecherManager.get(fachbelegung.fachID);
			if (fach === null)
				continue;
			if (fachbelegung.belegungen[GostHalbjahr.EF1.id] === null)
				continue;
			const alleBelegungen : List<AbiturFachbelegung> | null = this.getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen === null) || (alleBelegungen.isEmpty()))
				continue;
			let hatBelegung : boolean = true;
			const halbjahre : Array<GostHalbjahr> = [GostHalbjahr.EF1, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22];
			for (const halbjahr of halbjahre) {
				let hatHalbjahresBelegung : boolean = false;
				for (const aktFachbelegung of alleBelegungen) {
					const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = aktFachbelegung.belegungen[halbjahr.id];
					if ((belegungHalbjahr !== null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr))) {
						hatHalbjahresBelegung = true;
						break;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				anzahl++;
		}
		return anzahl;
	}

	/**
	 * Prüft, ob die Fachbelegung eine durchgängige Belegung hat und prüft die Schriftlichkeit
	 * in der Qualifikationsphase. Ein Fach in der Qualifikationsphase gilt als Schriftlich belegt,
	 * sofern die ersten 3 Halbjahre der Qualifikationsphase schriftlich belegt wurden.
	 * - Zusatzkurse können nicht für eine durchgängige Belegung zählen.
	 *
	 * @param fachbelegung   die zu prüfende die zu überprüfenden Fachbelegung
	 *
	 * @return true, wenn die Belegung durchgängig ist und die Schriftlichkeit den Anforderungen genügt.
	 */
	public pruefeDurchgaengigkeitSchriftlich(fachbelegung : AbiturFachbelegung | null) : boolean {
		if (!this.pruefeDurchgaengigkeit(fachbelegung))
			return false;
		return this.pruefeBelegungMitSchriftlichkeit(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);
	}

	/**
	 * Prüft, ob unter den angegebenen Fachbelegungen ein Fach als Abiturfach von einem der angegebenen Arten
	 * gewählt wurde. Wird keine Art angebeben, so wird jede Fachbelegung akzeptiert und true zurückgegeben.
	 *
	 * @param fachbelegungen   die Fachbelegungen
	 * @param arten            die Arten der Abiturfächer
	 *
	 * @return true, falls unter den Fachbelegungen mindestens ein Fach als Abiturfach von einem der
	 *         angegebenen Arten gewählt wurde und false sonst
	 */
	public pruefeExistiertAbiFach(fachbelegungen : List<AbiturFachbelegung> | null, ...arten : Array<GostAbiturFach>) : boolean {
		if ((arten === null) || (arten.length === 0))
			return true;
		if (fachbelegungen === null)
			return false;
		for (const fachbelegung of fachbelegungen)
			for (const art of arten) {
				const abiturFach : GostAbiturFach | null = GostAbiturFach.fromID(fachbelegung.abiturFach);
				if (abiturFach as unknown === art as unknown)
					return true;
			}
		return false;
	}

	/**
	 * Prüft, ob ein Abiturfach der übergebenen Art (1-4) existiert oder nicht.
	 *
	 * @param art   die Art des Abiturfaches (siehe {@link GostAbiturFach}
	 *
	 * @return true, falls die Art des Abiturfaches belegt wurde und ansonsten false
	 */
	public hatAbiFach(art : GostAbiturFach) : boolean {
		for (const fachbelegung of this.abidaten.fachbelegungen) {
			const abiturFach : GostAbiturFach | null = GostAbiturFach.fromID(fachbelegung.abiturFach);
			if (abiturFach as unknown === art as unknown)
				return true;
		}
		return false;
	}

	/**
	 * Prüft anhand des Statistik-Kürzels, ob in dem angegebenen Halbjahr eine doppelte Fachbelegung
	 * vorliegt oder nicht. Bei den Fremdsprachen werden nur unterschiedliche Fremdsprachen in einem Halbjahr
	 * akzeptiert und es dürfen mehrere Vertiefungsfächer (VX) in einem Halbjahr vorkommen. Fächer, die
	 * als nicht Prüfungsordnungs-relevant markiert sind werden ignoriert
	 *
	 * @param halbjahr   das zu prüfende Halbjahr
	 *
	 * @return true, falls eine doppelte Belegung vorliegt, sonst false
	 */
	public hatDoppelteFachbelegungInHalbjahr(halbjahr : GostHalbjahr) : boolean {
		const set : HashSet<string> = new HashSet<string>();
		const fachbelegungen : List<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if ((fach === null) || (!fach.istPruefungsordnungsRelevant))
				continue;
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = this.getBelegungHalbjahr(fb, halbjahr, GostSchriftlichkeit.BELIEBIG);
			if ((belegungHalbjahr === null) || (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				continue;
			let kuerzel : string | null = GostFachUtils.getFremdsprache(fach);
			if (kuerzel === null)
				kuerzel = (fach.kuerzel === null) ? "" : fach.kuerzel;
			if (!set.add(kuerzel) && (!JavaObject.equalsTranspiler("VX", (kuerzel))))
				return true;
		}
		return false;
	}

	/**
	 * Prüft anhand des Statistik-Kürzels, ob in einem der angegebenen Halbjahre eine doppelte Fachbelegung
	 * vorliegt oder nicht. Bei den Fremdsprachen werden nur unterschiedliche Fremdsprachen in einem Halbjahr
	 * akzeptiert und es dürfen mehrere Vertiefungsfächer (VX) in einem Halbjahr vorkommen.
	 *
	 * @param halbjahre   die zu prüfenden Halbjahre
	 *
	 * @return true, falls eine doppelte Belegung vorliegt, sonst false
	 */
	public hatDoppelteFachbelegung(...halbjahre : Array<GostHalbjahr>) : boolean {
		if (halbjahre.length === 0)
			return false;
		for (const halbjahr of halbjahre)
			if (this.hatDoppelteFachbelegungInHalbjahr(halbjahr))
				return true;
		return false;
	}

	/**
	 * Gibt zurück, ob der Projektkurs als besondere Lernleistung verwendet wird.
	 *
	 * @return true, falls der Projektkurs als besondere Lernleistung verwendet wird
	 */
	public istProjektKursBesondereLernleistung() : boolean {
		return (GostBesondereLernleistung.PROJEKTKURS.is(this.abidaten.besondereLernleistung));
	}

	/**
	 * Bestimmt die Fachbelegung des Faches mit der angegebenen ID
	 *
	 * @param fachID   die ID des Faches
	 *
	 * @return die Fachbelegung oder null, falls keine vorhanden ist
	 */
	public getFachbelegungByID(fachID : number) : AbiturFachbelegung | null {
		return this.mapFachbelegungByFachID.get(fachID);
	}

	/**
	 * Bestimmt die erste Fachbelegung mit dem angegebenen Statistik-Kürzel
	 *
	 * @param kuerzel          das Kürzel des Faches, kann null sein (dann wird auch null zurückgegeben)
	 *
	 * @return die Fachbelegung oder null, falls keine vorhanden ist
	 */
	public getFachbelegungByKuerzel(kuerzel : string | null) : AbiturFachbelegung | null {
		if ((kuerzel === null) || (JavaObject.equalsTranspiler("", (kuerzel))))
			return null;
		const fachbelegungen : List<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if ((fach !== null) && (JavaObject.equalsTranspiler(kuerzel, (fach.kuerzel))))
				return fb;
		}
		return null;
	}

	/**
	 * Liefert alle Fachbelegungen der Abiturdaten, welche den angegebenen Fachbereichen zuzuordnen sind.
	 * Wird kein Fachbereich angegeben, so werden alle Fachbelegungen der Abiturdaten zurückgegeben.
	 *
	 * @param fachbereiche   die Fachbereiche
	 *
	 * @return eine Liste der Fachbelegungen aus den Fachbereichen
	 */
	public getFachbelegungen(...fachbereiche : Array<GostFachbereich>) : List<AbiturFachbelegung> {
		if ((fachbereiche === null) || (fachbereiche.length === 0))
			return this.abidaten.fachbelegungen;
		const result : ArrayList<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		for (const fachbereich of fachbereiche) {
			const fachbelegungen : List<AbiturFachbelegung> | null = this.mapFachbereiche.get(fachbereich);
			if (fachbelegungen === null)
				continue;
			result.addAll(fachbelegungen);
		}
		return result;
	}

	/**
	 * Liefert alle Prüfungsordnungs-relevanten Fachbelegungen der Abiturdaten, welche den angegebenen Fachbereichen
	 * zuzuordnen sind.
	 * Wird kein Fachbereich angegeben, so werden alle Fachbelegungen der Abiturdaten zurückgegeben.
	 *
	 * @param fachbereiche   die Fachbereiche
	 *
	 * @return eine Liste der Fachbelegungen aus den Fachbereichen
	 */
	public getRelevanteFachbelegungen(...fachbereiche : Array<GostFachbereich>) : List<AbiturFachbelegung> {
		if ((fachbereiche === null) || (fachbereiche.length === 0))
			return this.abidaten.fachbelegungen;
		const result : ArrayList<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		for (const fachbereich of fachbereiche) {
			const fachbelegungen : List<AbiturFachbelegung> | null = this.mapFachbereicheRelevant.get(fachbereich);
			if (fachbelegungen === null)
				continue;
			result.addAll(fachbelegungen);
		}
		return result;
	}

	/**
	 * Liefert alle Fachbelegungen, die bilingual unterrichtet wurden.
	 *
	 * @return eine Liste der Fachbelegungen
	 */
	public getFachbelegungenBilingual() : List<AbiturFachbelegung> {
		const result : List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		const fachbelegungen : List<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fb of fachbelegungen) {
			if (this.zaehleBelegung(fb) <= 0)
				continue;
			const fach : GostFach | null = this.getFach(fb);
			if ((fach !== null) && (!GostFachbereich.FREMDSPRACHE.hat(fach)) && (!GostFachbereich.DEUTSCH.hat(fach)) && (fach.biliSprache !== null) && (!JavaObject.equalsTranspiler("D", (fach.biliSprache))))
				result.add(fb);
		}
		return result;
	}

	/**
	 * Filtert die Fachbelegungen auf neu einsetzende Fremdsprachen.
	 *
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public filterFremdspracheNeuEinsetzend(fachbelegungen : List<AbiturFachbelegung> | null) : List<AbiturFachbelegung> {
		if (fachbelegungen === null)
			return Collections.emptyList();
		const result : ArrayList<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if ((fach !== null) && fach.istFremdsprache && fach.istFremdSpracheNeuEinsetzend)
				result.add(fb);
		}
		return result;
	}

	/**
	 * Filtert die Fachbelegungen auf fortgeführte Fremdsprachen.
	 *
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public filterFremdspracheFortgefuehrt(fachbelegungen : List<AbiturFachbelegung> | null) : List<AbiturFachbelegung> {
		if (fachbelegungen === null)
			return Collections.emptyList();
		const result : ArrayList<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if ((fach !== null) && fach.istFremdsprache && !fach.istFremdSpracheNeuEinsetzend)
				result.add(fb);
		}
		return result;
	}

	/**
	 * Filtert die Fachbelegungen danach, ob sie durchgehend belegbar sind
	 *
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public filterDurchgehendBelegbar(fachbelegungen : List<AbiturFachbelegung> | null) : List<AbiturFachbelegung> {
		const result : ArrayList<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		if (fachbelegungen === null)
			return result;
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if (GostFachUtils.istDurchgehendBelegbarBisQ22(fach))
				result.add(fb);
		}
		return result;
	}

	/**
	 * Filtert die Fachbelegungen. Es werden nur Fachbelegungen behalten, die in den angegebenen Halbjahren eine Belegung aufweisen.
	 * Wird kein Halbjahr angegeben, so wird nichts gefiltert, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu filternden Fachbelegungen
	 * @param halbjahre         die Halbjahre, die belegt sein müssen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public filterBelegungen(fachbelegungen : List<AbiturFachbelegung> | null, ...halbjahre : Array<GostHalbjahr>) : List<AbiturFachbelegung> {
		if (fachbelegungen === null)
			return Collections.emptyList();
		const result : ArrayList<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		for (const fb of fachbelegungen) {
			if (this.pruefeBelegung(fb, ...halbjahre))
				result.add(fb);
		}
		return result;
	}

	/**
	 * Bestimmt die Menge der unterschiedlichen Statistik-Fächer in der angegebenen Fachbelegungen.
	 *
	 * @param fachbelegungen   die Fachbelegungen
	 *
	 * @return die Menge der Statistik-Fächer
	 */
	private getMengeStatistikFaecher(fachbelegungen : List<AbiturFachbelegung>) : JavaSet<Fach> {
		const faecher : HashSet<Fach> = new HashSet<Fach>();
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.faecherManager.get(fb.fachID);
			if (fach === null)
				continue;
			faecher.add(Fach.getBySchluesselOrDefault(fach.kuerzel));
		}
		return faecher;
	}

	/**
	 * Diese Methode zählt die Anzahl der angegebenen Fachbelegungen, welche in allen
	 * Halbjahren belegt sind. Dabei werden Fachbelegungen, welche dem gleichem Statistik-Fach
	 * zuzuordnen sind zusammengefasst. Dies ist bei der Abwahl von bilingualen Sachfächern
	 * relevant.
	 *
	 * @param fachbelegungen   die zu zählenden Fachbelegungen
	 *
	 * @return die Anzahl der Fachbelegungen
	 */
	public zaehleBelegungenDurchgaengig(fachbelegungen : List<AbiturFachbelegung> | null) : number {
		if (fachbelegungen === null)
			return 0;
		const faecher : JavaSet<Fach> = this.getMengeStatistikFaecher(fachbelegungen);
		let count : number = 0;
		for (const zulFach of faecher) {
			let vorhanden : boolean = true;
			for (const halbjahr of GostHalbjahr.values()) {
				let belegung_vorhanden : boolean = false;
				for (const fb of fachbelegungen) {
					const fbFach : GostFach | null = this.faecherManager.get(fb.fachID);
					if (fbFach === null)
						continue;
					const fbZulFach : Fach = Fach.getBySchluesselOrDefault(fbFach.kuerzel);
					const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fb.belegungen[halbjahr.id];
					if ((zulFach as unknown === fbZulFach as unknown) && (belegungHalbjahr !== null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr))) {
						belegung_vorhanden = true;
						break;
					}
				}
				if (!belegung_vorhanden) {
					vorhanden = false;
					break;
				}
			}
			if (vorhanden)
				count++;
		}
		return count;
	}

	private istBelegungDurchgaengigSchriftlichInQPhase(zulFach : Fach, halbjahr : GostHalbjahr, fb : AbiturFachbelegung) : boolean {
		const fbFach : GostFach | null = this.faecherManager.get(fb.fachID);
		if (fbFach === null)
			return false;
		const fbZulFach : Fach = Fach.getBySchluesselOrDefault(fbFach.kuerzel);
		if (zulFach as unknown === fbZulFach as unknown) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fb.belegungen[halbjahr.id];
			if ((belegungHalbjahr !== null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr))) {
				let istSchriftlichkeitOK : boolean = true;
				if (((halbjahr as unknown === GostHalbjahr.Q11 as unknown) || (halbjahr as unknown === GostHalbjahr.Q12 as unknown) || (halbjahr as unknown === GostHalbjahr.Q21 as unknown)) && (!belegungHalbjahr.schriftlich))
					istSchriftlichkeitOK = false;
				if (istSchriftlichkeitOK)
					return true;
			}
		}
		return false;
	}

	/**
	 * Diese Methode zählt die Anzahl der angegebenen Fachbelegungen, welche in allen
	 * Halbjahren belegt sind. Dabei werden Fachbelegungen, welche dem gleichem Statistik-Fach
	 * zuzuordnen sind zusammengefasst. Dies ist bei der Abwahl von bilingualen Sachfächern
	 * relevant.
	 *
	 * @param fachbelegungen   die zu zählenden Fachbelegungen
	 *
	 * @return die Anzahl der Fachbelegungen
	 */
	public zaehleBelegungenDurchgaengigSchriftlichInQPhase(fachbelegungen : List<AbiturFachbelegung> | null) : number {
		if (fachbelegungen === null)
			return 0;
		const faecher : JavaSet<Fach> = this.getMengeStatistikFaecher(fachbelegungen);
		let count : number = 0;
		for (const zulFach of faecher) {
			let vorhanden : boolean = true;
			for (const halbjahr of GostHalbjahr.values()) {
				let belegung_vorhanden : boolean = false;
				for (const fb of fachbelegungen) {
					if (this.istBelegungDurchgaengigSchriftlichInQPhase(zulFach, halbjahr, fb)) {
						belegung_vorhanden = true;
						break;
					}
				}
				if (!belegung_vorhanden) {
					vorhanden = false;
					break;
				}
			}
			if (vorhanden)
				count++;
		}
		return count;
	}

	/**
	 * Filtert die Fachbelegungen. Es werden nur Belegungen behalten, die in den angegebenen Halbjahren die geforderte Schriftlichkeit aufweisen.
	 * Wird kein Halbjahr angegeben, so wird nichts gefiltert, da kein Halbjahr geprüft werden muss.
	 *
	 * @param fachbelegungen    die zu filternden Fachbelegungen
	 * @param schriftlichkeit   die geforderte Schriftlichkeit
	 * @param halbjahre         die Halbjahre, die belegt sein müssen
	 *
	 * @return die gefilterten Fachbelegungen
	 */
	public filterBelegungenMitSchriftlichkeit(fachbelegungen : List<AbiturFachbelegung> | null, schriftlichkeit : GostSchriftlichkeit, ...halbjahre : Array<GostHalbjahr>) : List<AbiturFachbelegung> {
		if (fachbelegungen === null)
			return Collections.emptyList();
		const result : ArrayList<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		for (const fb of fachbelegungen) {
			if (this.pruefeBelegungMitSchriftlichkeit(fb, schriftlichkeit, ...halbjahre))
				result.add(fb);
		}
		return result;
	}

	/**
	 * Liefert die erste Fachbelegung für den Fachbereich - sofern eine existiert
	 *
	 * @param fachbereich   der Fachbereich
	 *
	 * @return die Fachbelegung oder null
	 */
	public getFachbelegung(fachbereich : GostFachbereich) : AbiturFachbelegung | null {
		const faecher : ArrayList<AbiturFachbelegung> | null = this.mapFachbereiche.get(fachbereich);
		if ((faecher === null) || (faecher.isEmpty()))
			return null;
		return faecher.get(0);
	}

	/**
	 * Liefert die erste Prüfungsordnungs-relevante Fachbelegung für den Fachbereich - sofern eine existiert
	 *
	 * @param fachbereich   der Fachbereich
	 *
	 * @return die Fachbelegung oder null
	 */
	public getRelevanteFachbelegung(fachbereich : GostFachbereich) : AbiturFachbelegung | null {
		const faecher : ArrayList<AbiturFachbelegung> | null = this.mapFachbereicheRelevant.get(fachbereich);
		if ((faecher === null) || (faecher.isEmpty()))
			return null;
		return faecher.get(0);
	}

	/**
	 * Liefert alle Fachbelegungen mit dem angegebenen Statistk-Kürzel des Faches
	 *
	 * @param kuerzel   das Kürzel des Faches
	 *
	 * @return eine Liste mit den Fachbelegungen
	 */
	public getFachbelegungByFachkuerzel(kuerzel : string | null) : List<AbiturFachbelegung> {
		const fachbelegungen : List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		if (kuerzel === null)
			return fachbelegungen;
		const tmpFachbelegungen : List<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fachbelegung of tmpFachbelegungen) {
			const fach : GostFach | null = this.faecherManager.get(fachbelegung.fachID);
			if ((fach === null) || (!JavaObject.equalsTranspiler(kuerzel, (fach.kuerzel))))
				continue;
			fachbelegungen.add(fachbelegung);
		}
		return fachbelegungen;
	}

	/**
	 * Prüft, ob der Kurs in dem angegebenen Halbjahr mit der angegebenen Schriftlichkeit belegt ist
	 * und gibt ggf. die Belegung zurück.
	 *
	 * @param fachbelegung   die Abiturfachbelegung aus welcher die Belegungsinformationen für das Halbjahr entnommen wird
	 * @param halbjahr       das Halbjahr, in welchem die Belegung gesucht wird.
	 * @param schriftlich    gibt an, ob das Fach schriftlich oder mündlich belegt sein muss
	 *
	 * @return die Belegungsinformationen zu dem Fach
	 */
	public getBelegungHalbjahr(fachbelegung : AbiturFachbelegung, halbjahr : GostHalbjahr, schriftlich : GostSchriftlichkeit) : AbiturFachbelegungHalbjahr | null {
		const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
		return ((belegungHalbjahr !== null) && (!AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)) && ((schriftlich as unknown === GostSchriftlichkeit.BELIEBIG as unknown) || ((schriftlich as unknown === GostSchriftlichkeit.SCHRIFTLICH as unknown) && (belegungHalbjahr.schriftlich)) || ((schriftlich as unknown === GostSchriftlichkeit.MUENDLICH as unknown) && (!belegungHalbjahr.schriftlich)))) ? belegungHalbjahr : null;
	}

	/**
	 * Liefert die erste Sprachbelegung - sofern eine existiert
	 *
	 * @param sprache   das einstellige Kürzel der Sprache
	 *
	 * @return die Fachbelegung für die Sprache
	 */
	public getSprachbelegung(sprache : string | null) : AbiturFachbelegung | null {
		if (sprache === null)
			return null;
		const fachbelegungen : List<AbiturFachbelegung> = this.abidaten.fachbelegungen;
		for (const fb of fachbelegungen) {
			const fach : GostFach | null = this.getFach(fb);
			if ((fach === null) || (!GostFachUtils.istFremdsprachenfach(fach, sprache)))
				continue;
			if (JavaObject.equalsTranspiler(sprache, (GostFachUtils.getFremdsprache(fach))))
				return fb;
		}
		return null;
	}

	/**
	 * Prüft, ob das Fach mit der übergebenen ID als Abiturfach möglich ist und mit welcher Kursart
	 * dieses Fach aufgrund der Belegungen im Abitur gewählt werden kann.
	 *
	 * @param id   die ID des Fachs
	 *
	 * @return die mögliche Kursart des Faches im Abitur oder null, falls das Fach nicht als Abiturfach
	 *         ausgewählt werden kann.
	 */
	public getMoeglicheKursartAlsAbiturfach(id : number) : GostKursart | null {
		if ((this.abidaten.abiturjahr >= 2029) && (this.servermode as unknown === ServerMode.DEV as unknown))
			return this._getAbi29MoeglicheKursartAlsAbiturfach(id);
		const fach : GostFach | null = this.faecherManager.get(id);
		if ((fach === null) || (!fach.istPruefungsordnungsRelevant))
			return null;
		const belegung : AbiturFachbelegung | null = this.getFachbelegungByID(id);
		if ((belegung === null) || (belegung.letzteKursart === null))
			return null;
		const kursart : GostKursart | null = GostKursart.fromKuerzel(belegung.letzteKursart);
		if ((kursart === null) || ((kursart as unknown === GostKursart.LK as unknown) && (!fach.istMoeglichAbiLK)) || ((kursart as unknown === GostKursart.GK as unknown) && (!fach.istMoeglichAbiGK)) || ((kursart as unknown !== GostKursart.GK as unknown) && (kursart as unknown !== GostKursart.LK as unknown)))
			return null;
		if (kursart as unknown === GostKursart.LK as unknown)
			return this.pruefeBelegungMitKursart(belegung, kursart, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22) ? kursart : null;
		if (belegung.belegungen[GostHalbjahr.Q22.id] === null)
			return null;
		const fachbelegungen : List<AbiturFachbelegung> = GostFachbereich.RELIGION.hat(fach) ? this.getFachbelegungen(GostFachbereich.RELIGION) : this.getFachbelegungByFachkuerzel(fach.kuerzel);
		return (this.pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11) && this.pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q12) && this.pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q21)) ? kursart : null;
	}

	/**
	 * Experimenteller Code:
	 *
	 * Prüft, ob das Fach mit der übergebenen ID als Abiturfach möglich ist und mit welcher Kursart
	 * dieses Fach aufgrund der Belegungen im Abitur gewählt werden kann.
	 *
	 * @param id   die ID des Fachs
	 *
	 * @return die mögliche Kursart des Faches im Abitur oder null, falls das Fach nicht als Abiturfach
	 *         ausgewählt werden kann.
	 */
	private _getAbi29MoeglicheKursartAlsAbiturfach(id : number) : GostKursart | null {
		const fach : GostFach | null = this.faecherManager.get(id);
		if ((fach === null) || (!fach.istPruefungsordnungsRelevant))
			return null;
		const belegung : AbiturFachbelegung | null = this.getFachbelegungByID(id);
		if ((belegung === null) || (belegung.letzteKursart === null))
			return null;
		const kursart : GostKursart | null = GostKursart.fromKuerzel(belegung.letzteKursart);
		if ((kursart === null) || (kursart as unknown !== GostKursart.GK as unknown) && (kursart as unknown !== GostKursart.LK as unknown) && (kursart as unknown !== GostKursart.PJK as unknown))
			return null;
		if (kursart as unknown === GostKursart.LK as unknown) {
			if (!fach.istMoeglichAbiLK)
				return null;
			return this.pruefeBelegungMitKursart(belegung, kursart, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22) ? kursart : null;
		}
		if (kursart as unknown === GostKursart.PJK as unknown) {
			if (!fach.istMoeglichAbiGK)
				return null;
			if (!this.pruefeBelegungMitKursart(belegung, kursart, GostHalbjahr.Q21, GostHalbjahr.Q22))
				return null;
			const leitfach : AbiturFachbelegung | null = this.getFachbelegungByKuerzel(fach.projektKursLeitfach1Kuerzel);
			if (!this.pruefeBelegungMitKursart(leitfach, GostKursart.GK, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12))
				return null;
			if (!this.pruefeBelegungMitSchriftlichkeit(leitfach, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12))
				return null;
			return kursart;
		}
		if (!fach.istMoeglichAbiGK)
			return null;
		if (belegung.belegungen[GostHalbjahr.Q22.id] === null)
			return null;
		const fachbelegungen : List<AbiturFachbelegung> = GostFachbereich.RELIGION.hat(fach) ? this.getFachbelegungen(GostFachbereich.RELIGION) : this.getFachbelegungByFachkuerzel(fach.kuerzel);
		return (this.pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11) && this.pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q12) && this.pruefeBelegungExistiertMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q21)) ? kursart : null;
	}

	/**
	 * Liefert für die übergebene Fachbelegung die Halbjahre, in denen das Fach mit einer der angebenen
	 * Kursarten belegt wurde. Ist keine Kursart angegeben, so werden die Halbjahre aller Belegungen
	 * zurückgegeben. Ist keine Fachbelegung angegeben, so wird eine leere Liste zurückgegeben.
	 *
	 * @param fachbelegung   die Fachbelegung
	 * @param kursarten      die Kursarten
	 *
	 * @return eine Liste der Halbjahre in den das Fach mit einer der Kursarten belegt wurde
	 */
	public getHalbjahreKursart(fachbelegung : AbiturFachbelegung | null, ...kursarten : Array<GostKursart>) : List<GostHalbjahr> {
		const halbjahre : ArrayList<GostHalbjahr> = new ArrayList<GostHalbjahr>();
		if (fachbelegung !== null) {
			for (const belegungHalbjahr of fachbelegung.belegungen) {
				if ((belegungHalbjahr === null) || (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
					continue;
				const halbjahr : GostHalbjahr | null = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if (halbjahr === null)
					continue;
				if ((kursarten === null) || (kursarten.length === 0)) {
					halbjahre.add(halbjahr);
					continue;
				}
				for (const kursart of kursarten) {
					if (JavaObject.equalsTranspiler(kursart, (GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel)))) {
						halbjahre.add(halbjahr);
						break;
					}
				}
			}
		}
		return halbjahre;
	}

	/**
	 * Gibt die Sprache des bilingualen Bildungsgang zurück oder null, falls keiner gewählt wurde.
	 *
	 * @return die Sprache des bilingualen Bildungsgang oder null
	 */
	public getBiligualenBildungsgang() : string | null {
		return this.abidaten.bilingualeSprache;
	}

	/**
	 * Prüft bei der Sprachenfolge, ob eine laut Sprachenfolge fortgeführte
	 * Fremdsprache fehlerhafterweise als neu einsetzende Fremdsprache belegt wurde.
	 * Übergebene Fachbelegungen, die keine Fremdsprachen sind werden ignoriert.
	 *
	 * @param fremdsprachen   die zu prüfenden Fachbelegungen
	 *
	 * @return true, falls eine fortgeführte Fremdsprache bei den übergebenen
	 *         Fachbelegungen existiert, ansonsten false
	 */
	public hatFortgefuehrteFremdspracheInSprachendaten(fremdsprachen : List<AbiturFachbelegung> | null) : boolean {
		if (fremdsprachen === null)
			return false;
		if (this.abidaten.sprachendaten === null)
			return false;
		for (const fs of fremdsprachen) {
			const fach : GostFach | null = this.getFach(fs);
			if ((fach === null) || (!fach.istFremdsprache))
				continue;
			if (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(this.abidaten.sprachendaten, GostFachUtils.getFremdsprache(fach))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Prüft bei der Sprachenfolge, ob für eine laut Sprachenfolge neu einsetzende
	 * Fremdsprache fehlerhafterweise ein Kurs in einer fortgeführten Fremdsprache belegt wurde.
	 * Übergebene Fachbelegungen, die keine Fremdsprachen sind werden ignoriert.
	 *
	 * @param fremdsprachen   die zu prüfenden Fachbelegungen
	 *
	 * @return true, falls eine neu einsetzende Fremdsprache bei den übergebenen
	 *         Fachbelegungen existiert, ansonsten false
	 */
	public hatNeuEinsetzendeFremdspracheInSprachendaten(fremdsprachen : List<AbiturFachbelegung> | null) : boolean {
		if (fremdsprachen === null)
			return false;
		if (this.abidaten.sprachendaten === null)
			return false;
		for (const fs of fremdsprachen) {
			const fach : GostFach | null = this.getFach(fs);
			if ((fach === null) || (!fach.istFremdsprache))
				continue;
			if (SprachendatenUtils.istNeueinsetzbareSpracheInGOSt(this.abidaten.sprachendaten, GostFachUtils.getFremdsprache(fach))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Prüft, ob die Belegung seit der EF1 vorhanden ist. Hierbei werden
	 * Zusatz-, Vertiefungs- und Projektkurse auch als später einsetzend akzeptiert.
	 * Dies gilt auch für Literatur, instrumental- und vokal-praktische Kurse sowie
	 * für Religion und Philosophie.
	 *
	 * @param fachbelegung   die Abiturfachbelegungen, die geprüft werden
	 *
	 * @return true, falls das Fach seit EF1 durchgängig belegt wurde oder eine der Ausnahmen zutrifft, sonst false
	 */
	public istBelegtSeitEF(fachbelegung : AbiturFachbelegung) : boolean {
		const fach : GostFach | null = this.getFach(fachbelegung);
		if (fach === null)
			return false;
		if (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))
			return true;
		if (GostFachbereich.RELIGION.hat(fach))
			return true;
		if (JavaObject.equalsTranspiler("PL", (fach.kuerzel)))
			return true;
		for (const belegungHalbjahr of fachbelegung.belegungen) {
			if ((belegungHalbjahr === null) || (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahr)))
				continue;
			const halbjahr : GostHalbjahr | null = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
			const kursart : GostKursart | null = GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel);
			if ((halbjahr === null) || (kursart === null))
				continue;
			if ((kursart as unknown === GostKursart.ZK as unknown) || (kursart as unknown === GostKursart.PJK as unknown) || (kursart as unknown === GostKursart.VTF as unknown))
				continue;
			const prevHalbjahr : GostHalbjahr | null = halbjahr.previous();
			if (prevHalbjahr === null)
				continue;
			const belegungHalbjahrVorher : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[prevHalbjahr.id];
			if ((belegungHalbjahrVorher === null) || (AbiturdatenManager.istNullPunkteBelegungInQPhase(belegungHalbjahrVorher))) {
				const alleBelegungen : List<AbiturFachbelegung> | null = this.getFachbelegungByFachkuerzel(fach.kuerzel);
				if ((alleBelegungen === null) || (alleBelegungen.size() <= 1))
					return false;
				if (!this.pruefeBelegungExistiert(alleBelegungen, prevHalbjahr))
					return false;
			}
		}
		return true;
	}

	/**
	 * Gibt zurück, ob ein Zusatzkurs in Geschichte in diesem Jahrgang angeboten
	 * wird oder nicht.
	 *
	 * @return true, wenn einer angeboten wird und ansonsten false
	 */
	public istErlaubtZusatzkursGE() : boolean {
		if (this._jahrgangsdaten === null)
			return true;
		return this._jahrgangsdaten.hatZusatzkursGE;
	}

	/**
	 * Gibt das erste Halbjahr für Zusatzkurse in Geschichte
	 * in diesem Jahrgang zurück.
	 *
	 * @return das erste Halbjahr für Zusatzkurse in Geschichte
	 */
	public getBeginnZusatzkursGE() : GostHalbjahr {
		if (this._jahrgangsdaten === null)
			return GostHalbjahr.Q21;
		const hj : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this._jahrgangsdaten.beginnZusatzkursGE);
		if (hj === null)
			return GostHalbjahr.Q21;
		return hj;
	}

	/**
	 * Gibt zurück, ob ein Zusatzkurs in Sozialwissenschaften in diesem Jahrgang
	 * angeboten wird oder nicht.
	 *
	 * @return true, wenn einer angeboten wird und ansonsten false
	 */
	public istErlaubtZusatzkursSW() : boolean {
		if (this._jahrgangsdaten === null)
			return true;
		return this._jahrgangsdaten.hatZusatzkursSW;
	}

	/**
	 * Gibt das erste Halbjahr für Zusatzkurse in Sozialwissenschaften
	 * in diesem Jahrgang zurück.
	 *
	 * @return das erste Halbjahr für Zusatzkurse in Sozialwissenschaften
	 */
	public getBeginnZusatzkursSW() : GostHalbjahr {
		if (this._jahrgangsdaten === null)
			return GostHalbjahr.Q21;
		const hj : GostHalbjahr | null = GostHalbjahr.fromKuerzel(this._jahrgangsdaten.beginnZusatzkursSW);
		if (hj === null)
			return GostHalbjahr.Q21;
		return hj;
	}

	/**
	 * Gibt alle Fachkombination zurück, welche in der EF.1 gültig sind.
	 *
	 * @return die Liste mit den Fachkombinationen
	 */
	public getFachkombinationenEF1() : List<GostJahrgangFachkombination> {
		const kombis : List<GostJahrgangFachkombination> = new ArrayList<GostJahrgangFachkombination>();
		for (const kombi of this.faecherManager.getFachkombinationen())
			if (kombi.gueltigInHalbjahr[GostHalbjahr.EF1.id])
				kombis.add(kombi);
		return kombis;
	}

	/**
	 * Gibt alle Fachkombination zurück.
	 *
	 * @return die Liste mit den Fachkombinationen
	 */
	public getFachkombinationenGesamt() : List<GostJahrgangFachkombination> {
		return this.faecherManager.getFachkombinationen();
	}

	/**
	 * Gibt das Ergebnis der Belegprüfung zurück. Dieses enthält eine Liste der Fehler, die bei der Belegprüfung
	 * festgestellt wurden und ob diese erfolgreich gewesen ist oder nicht.
	 *
	 * @return das Ergebnis der Belegprüfung
	 */
	public getBelegpruefungErgebnis() : GostBelegpruefungErgebnis {
		const ergebnis : GostBelegpruefungErgebnis = new GostBelegpruefungErgebnis();
		ergebnis.erfolgreich = this.belegpruefungErfolgreich;
		for (let i : number = 0; i < this.belegpruefungsfehler.size(); i++) {
			const fehler : GostBelegungsfehler = this.belegpruefungsfehler.get(i);
			ergebnis.fehlercodes.add(new GostBelegpruefungErgebnisFehler(fehler, this.pruefungsArt));
		}
		return ergebnis;
	}

	/**
	 * Gibt das Belegprüfungs-Objekt für die KurszahlenUndWochenstunden zurück, welches für
	 * die Belegprüfung genutzt wurde und die Statistiken dazu erstellt hat.
	 *
	 * @return das Belegprüfungs-Objekt für die KurszahlenUndWochenstunden
	 */
	private getKurszahlenUndWochenstunden() : KurszahlenUndWochenstunden {
		if (this.belegpruefungKurszahlenUndWochenstunden === null)
			throw new NullPointerException("Die Belegprüfung zu Kurszahlen und Wochenstunden wurde noch nicht erstellt und durchgeführt.")
		return this.belegpruefungKurszahlenUndWochenstunden;
	}

	/**
	 * Experimenteller Code:
	 * Gibt das Belegprüfungs-Objekt für die KurszahlenUndWochenstunden zurück, welches für
	 * die Belegprüfung genutzt wurde und die Statistiken dazu erstellt hat.
	 *
	 * @return das Belegprüfungs-Objekt für die KurszahlenUndWochenstunden
	 */
	private getAbi29KurszahlenUndWochenstunden() : Abi29BelegpruefungKurszahlenUndWochenstunden {
		if (this.abi29BelegpruefungKurszahlenUndWochenstunden === null)
			throw new NullPointerException("Die Belegprüfung zu Kurszahlen und Wochenstunden wurde noch nicht erstellt und durchgeführt.")
		return this.abi29BelegpruefungKurszahlenUndWochenstunden;
	}

	/**
	 * Berechnet die Wochenstunden, welche von dem Schüler in den einzelnen
	 * Halbjahren der gymnasialen Oberstufe für das Abitur relevant belegt wurden.
	 *
	 * @return ein Array mit den Wochenstunden für die sechs Halbjahre
	 */
	public getWochenstunden() : Array<number> {
		if ((this.abidaten.abiturjahr >= 2029) && (this.servermode as unknown === ServerMode.DEV as unknown)) {
			const kuw : Abi29BelegpruefungKurszahlenUndWochenstunden = this.getAbi29KurszahlenUndWochenstunden();
			const stunden : Array<number> = [0, 0, 0, 0, 0, 0];
			for (const hj of GostHalbjahr.values())
				stunden[hj.id] = kuw.getWochenstunden(hj);
			return stunden;
		}
		const kuw : KurszahlenUndWochenstunden = this.getKurszahlenUndWochenstunden();
		const stunden : Array<number> = [0, 0, 0, 0, 0, 0];
		for (const hj of GostHalbjahr.values())
			stunden[hj.id] = kuw.getWochenstunden(hj);
		return stunden;
	}

	/**
	 * Gibt die Anzahl der Wochenstunden für die Einführungsphase zurück.
	 *
	 * @return die Anzahl der Wochenstunden
	 */
	public getWochenstundenEinfuehrungsphase() : number {
		if ((this.abidaten.abiturjahr >= 2029) && (this.servermode as unknown === ServerMode.DEV as unknown)) {
			const kuw : Abi29BelegpruefungKurszahlenUndWochenstunden = this.getAbi29KurszahlenUndWochenstunden();
			return kuw.getWochenstundenEinfuehrungsphase();
		}
		const kuw : KurszahlenUndWochenstunden = this.getKurszahlenUndWochenstunden();
		return kuw.getWochenstundenEinfuehrungsphase();
	}

	/**
	 * Gibt die Anzahl der Wochenstunden für die Qualifikationsphase zurück.
	 *
	 * @return die Anzahl der Wochenstunden
	 */
	public getWochenstundenQualifikationsphase() : number {
		if ((this.abidaten.abiturjahr >= 2029) && (this.servermode as unknown === ServerMode.DEV as unknown)) {
			const kuw : Abi29BelegpruefungKurszahlenUndWochenstunden = this.getAbi29KurszahlenUndWochenstunden();
			return kuw.getWochenstundenQualifikationsphase();
		}
		const kuw : KurszahlenUndWochenstunden = this.getKurszahlenUndWochenstunden();
		return kuw.getWochenstundenQualifikationsphase();
	}

	/**
	 * Berechnet die Anzahl der anrechenbaren Kurse, welche von dem Schüler in den einzelnen
	 * Halbjahren der gymnasialen Oberstufe für das Abitur belegt wurden.
	 *
	 * @return ein Array mit den anrechenbaren Kursen für die sechs Halbjahre
	 */
	public getAnrechenbareKurse() : Array<number> {
		if ((this.abidaten.abiturjahr >= 2029) && (this.servermode as unknown === ServerMode.DEV as unknown)) {
			const kuw : Abi29BelegpruefungKurszahlenUndWochenstunden = this.getAbi29KurszahlenUndWochenstunden();
			const anzahl : Array<number> = [0, 0, 0, 0, 0, 0];
			for (const hj of GostHalbjahr.values())
				anzahl[hj.id] = kuw.getKurszahlenAnrechenbar(hj);
			return anzahl;
		}
		const kuw : KurszahlenUndWochenstunden = this.getKurszahlenUndWochenstunden();
		const anzahl : Array<number> = [0, 0, 0, 0, 0, 0];
		for (const hj of GostHalbjahr.values())
			anzahl[hj.id] = kuw.getKurszahlenAnrechenbar(hj);
		return anzahl;
	}

	/**
	 * Gibt die Anzahl der anrechenbaren Kurse für den Block I des Abiturs
	 * zurück.
	 *
	 * @return die anrechenbaren Kursen für Block I des Abiturs
	 */
	public getAnrechenbareKurseBlockI() : number {
		if ((this.abidaten.abiturjahr >= 2029) && (this.servermode as unknown === ServerMode.DEV as unknown)) {
			const kuw : Abi29BelegpruefungKurszahlenUndWochenstunden = this.getAbi29KurszahlenUndWochenstunden();
			return kuw.getBlockIAnzahlAnrechenbar();
		}
		const kuw : KurszahlenUndWochenstunden = this.getKurszahlenUndWochenstunden();
		return kuw.getBlockIAnzahlAnrechenbar();
	}

	/**
	 * Gibt für die Notenpunkte für die Halbjahresbelegung zurück
	 *
	 * @param belegungHalbjahr   die Halbjahresbelegung
	 *
	 * @return die Notenpunkte oder null, falls das Fach in dem Halbjahr nicht gewählt wurde
	 */
	public getNotenpunkteOfFachbelegungHalbjahr(belegungHalbjahr : AbiturFachbelegungHalbjahr | null) : number | null {
		if (belegungHalbjahr === null)
			return null;
		const nke : NoteKatalogEintrag | null = Note.fromKuerzel(belegungHalbjahr.notenkuerzel).daten(this.getSchuljahr());
		return (nke === null) ? null : nke.notenpunkte;
	}

	/**
	 * Gibt für die übergebene Fach-ID und das übergebene Halbjahr die Notenpunkte zurück, sofern eine
	 * Belegung vorliegt, welche eine Note hat, und ansonsten null.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das Halbjahr
	 *
	 * @return die Notenpunkte oder null
	 */
	public getNotenpunkteByFachIDAndHalbjahr(idFach : number, halbjahr : GostHalbjahr) : number | null {
		return this.getNotenpunkteOfFachbelegungHalbjahr(this.mapFachbelegungHalbjahrByFachIDAndHalbjahrID.getOrNull(idFach, halbjahr.id));
	}

	/**
	 * Ermittelt die Informationen zu markierten Kursen in dem angegeben Halbjahr und gibt diese in einem Array
	 * zurück. Diese sind:
	 * - Notenpunktsumme mit doppelter Wertung der LKs (Index 0)
	 * - Anzahl der Kurse (Index 1)
	 * - Anzahl der Kurse mit doppelter Zählung der LKs (Index 2)
	 * - Anzahl der markierten Defizite im LK-Bereich (Index 3)
	 * - Anzahl der markierten Defizite im GK-Bereich (Index 4)
	 * - Anzahl der markierten Defizite (Index 5)
	 *
	 * @param halbjahr   das Halbjahr
	 *
	 * @return die Informationen zu den markierten Kursen
	 */
	public getKursinformationenOfMarkierteKurseByHalbjahr(halbjahr : GostHalbjahr) : Array<number> {
		const belegungen : List<AbiturFachbelegung> | null = this.mapFachbelegungenByGostHalbjahr.get(halbjahr);
		const result : Array<number> | null = Array(6).fill(0);
		result[0] = 0;
		result[1] = 0;
		result[2] = 0;
		result[3] = 0;
		result[4] = 0;
		result[5] = 0;
		if (belegungen === null)
			return result;
		for (const belegung of belegungen) {
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = belegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr === null) || (!belegungHalbjahr.block1gewertet))
				continue;
			const istLKBelegung : boolean = JavaObject.equalsTranspiler(GostKursart.LK.kuerzel, (belegungHalbjahr.kursartKuerzel));
			const np : number | null = this.getNotenpunkteOfFachbelegungHalbjahr(belegungHalbjahr);
			if ((np !== null) && (np !== 0)) {
				if (istLKBelegung) {
					result[0] += np * 2;
					result[1]++;
					result[2] += 2;
					if (np < 5)
						result[3]++;
				} else {
					result[0] += np;
					result[1]++;
					result[2]++;
					if (np < 5)
						result[4]++;
				}
			}
		}
		result[5] = result[3] + result[4];
		return result;
	}

	/**
	 * Gibt das Ergebnis des Markierungsalgorithmus zurück. Dieses enthält, ob der Algorithmus erfolgreich gewesen ist
	 * und im Erfolgsfall ein Liste für die einzelnen Belegungen der Qualifikationsphase, ob diese markiert wurden.
	 *
	 * @return das Ergebnis der Belegprüfung
	 */
	public getErgebnisMarkierungsalgorithmus() : GostAbiturMarkierungsalgorithmusErgebnis {
		return this.markierungsErgebnis;
	}

	/**
	 * Wendet das Ergebnis des Markierungsergebnis auf diese Belegung an.
	 *
	 * @return true, wenn es erfolgreich angewendet wurde, und ansonsten false
	 */
	public applyErgebnisMarkierungsalgorithmus() : boolean {
		if (!this.markierungsErgebnis.erfolgreich) {
			this.abidaten.block1Zulassung = false;
			return false;
		}
		for (const markierung of this.markierungsErgebnis.markierungen) {
			const belegung : AbiturFachbelegung | null = this.getFachbelegungByID(markierung.idFach);
			if (belegung === null)
				return false;
			const halbjahr : GostHalbjahr | null = GostHalbjahr.fromID(markierung.idHalbjahr);
			if (halbjahr === null)
				return false;
			const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = belegung.belegungen[markierung.idHalbjahr];
			if (belegungHalbjahr === null)
				return false;
			belegungHalbjahr.block1gewertet = markierung.markiert;
		}
		this.abidaten.block1AnzahlKurse = 0;
		this.abidaten.block1DefiziteGesamt = 0;
		this.abidaten.block1DefiziteLK = 0;
		this.abidaten.block1PunktSummeGK = 0;
		this.abidaten.block1PunktSummeLK = 0;
		for (const fachbelegung of this.abidaten.fachbelegungen) {
			let summeKurseFach : number = 0.0;
			let punktSummeEinfach : number = 0;
			fachbelegung.block1PunktSumme = 0;
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				const belegungHalbjahr : AbiturFachbelegungHalbjahr | null = fachbelegung.belegungen[halbjahr.id];
				if ((belegungHalbjahr === null) || (belegungHalbjahr.block1gewertet === null) || (!belegungHalbjahr.block1gewertet))
					continue;
				const istLK : boolean = JavaObject.equalsTranspiler(GostKursart.LK.kuerzel, (belegungHalbjahr.kursartKuerzel));
				const note : Note | null = Note.fromKuerzel(belegungHalbjahr.notenkuerzel);
				const nke : NoteKatalogEintrag | null = note.getKatalogEintrag(this.getSchuljahr());
				if ((nke === null) || (nke.notenpunkte === null))
					continue;
				punktSummeEinfach += nke.notenpunkte;
				const notenpunkte : number = nke.notenpunkte * (istLK ? 2 : 1);
				fachbelegung.block1PunktSumme += notenpunkte;
				summeKurseFach++;
				this.abidaten.block1AnzahlKurse++;
				if (istLK) {
					this.abidaten.block1PunktSummeLK += notenpunkte;
					if (notenpunkte < 5)
						this.abidaten.block1DefiziteLK++;
				} else {
					this.abidaten.block1PunktSummeGK += notenpunkte;
				}
				if (notenpunkte < 5)
					this.abidaten.block1DefiziteGesamt++;
			}
			fachbelegung.block1NotenpunkteDurchschnitt = (summeKurseFach === 0.0) ? null : (punktSummeEinfach / summeKurseFach);
		}
		const summeNotenpunkte : number = this.abidaten.block1PunktSummeLK + this.abidaten.block1PunktSummeGK;
		const anzahlKurse : number = (this.abidaten.block1AnzahlKurse + 8.0);
		this.abidaten.block1PunktSummeNormiert = Math.round((40.0 * summeNotenpunkte) / anzahlKurse) as number;
		this.abidaten.block1NotenpunkteDurchschnitt = Math.round((summeNotenpunkte / anzahlKurse) * 100.0) / 100.0;
		this.abidaten.block1Zulassung = (this.abidaten.block1PunktSummeNormiert >= 200) && (((this.abidaten.block1AnzahlKurse >= 35) && (this.abidaten.block1AnzahlKurse <= 37) && (this.abidaten.block1DefiziteGesamt <= 7)) || ((this.abidaten.block1AnzahlKurse >= 38) && (this.abidaten.block1DefiziteGesamt <= 8)));
		return true;
	}

	/**
	 * Gibt die Belegungsart für die Halbjahresbelegung zurück: Nicht belegt, mündlich oder schriftlich
	 *
	 * @param belegungHalbjahr   die Halbjahresbelegung
	 *
	 * @return die Belegungsart
	 */
	public static getBelegungsartFromHalbjahresbelegung(belegungHalbjahr : AbiturFachbelegungHalbjahr | null) : AbiturBelegungsart {
		if (belegungHalbjahr === null)
			return AbiturBelegungsart.NICHT_BELEGT;
		return belegungHalbjahr.schriftlich ? AbiturBelegungsart.SCHRIFTLICH : AbiturBelegungsart.MUENDLICH;
	}

	/**
	 * Gibt die Markierungsinformation für die Halbjahresbelegung zurück
	 *
	 * @param belegungHalbjahr   die Halbjahresbelegung
	 *
	 * @return die Markierungsinformation
	 */
	public static getKursmarkierungFromHalbjahresbelegung(belegungHalbjahr : AbiturFachbelegungHalbjahr | null) : AbiturKursMarkierung | null {
		if (belegungHalbjahr === null)
			return null;
		const istGewertet : boolean = (belegungHalbjahr.block1gewertet !== null) && (belegungHalbjahr.block1gewertet);
		const istKursAufZeugnis : boolean = istGewertet || ((belegungHalbjahr.block1kursAufZeugnis !== null) && (belegungHalbjahr.block1kursAufZeugnis));
		return new AbiturKursMarkierung(istGewertet, istKursAufZeugnis);
	}

	/**
	 * Wandelt das Noten-Kürzel der übergebenen Halbjahresbelegung in den zugehörigen Notenpunkte-String um.
	 *
	 * @param belegungHalbjahr   die Halbjahresbelegung
	 *
	 * @return die Notenpunkte als zweistelliger String, "AT" oder null
	 */
	public getNotenpunkteStringHalbjahresbelegung(belegungHalbjahr : AbiturFachbelegungHalbjahr | null) : string | null {
		if (belegungHalbjahr === null)
			return null;
		const tmpNote : Note | null = Note.fromKuerzel(belegungHalbjahr.notenkuerzel);
		if (tmpNote as unknown === Note.ATTEST as unknown)
			return belegungHalbjahr.notenkuerzel;
		if (!tmpNote.istNote(this.getSchuljahr()))
			return null;
		const nke : NoteKatalogEintrag | null = tmpNote.daten(this.getSchuljahr());
		if ((nke === null) || (nke.notenpunkte === null))
			return null;
		return (nke.notenpunkte < 10) ? ("0" + nke.notenpunkte) : ("" + nke.notenpunkte);
	}

	/**
	 * Wandelt das übergebene Noten-Kürzel in die zugehörigen Notepunkte um.
	 *
	 * @param kuerzel   das Notenkürzel
	 *
	 * @return die Notenpunkte oder null
	 */
	public getNotenpunkteFromKuerzel(kuerzel : string | null) : number | null {
		const tmpNote : Note | null = Note.fromKuerzel(kuerzel);
		if (!tmpNote.istNote(this.getSchuljahr()))
			return null;
		const nke : NoteKatalogEintrag | null = tmpNote.daten(this.getSchuljahr());
		return (nke === null) ? null : nke.notenpunkte;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.AbiturdatenManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.AbiturdatenManager'].includes(name);
	}

	public static class = new Class<AbiturdatenManager>('de.svws_nrw.core.abschluss.gost.AbiturdatenManager');

}

export function cast_de_svws_nrw_core_abschluss_gost_AbiturdatenManager(obj : unknown) : AbiturdatenManager {
	return obj as AbiturdatenManager;
}
