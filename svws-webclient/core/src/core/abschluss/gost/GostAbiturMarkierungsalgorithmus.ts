import { JavaObject } from '../../../java/lang/JavaObject';
import { HashMap2D } from '../../../core/adt/map/HashMap2D';
import type { JavaSet } from '../../../java/util/JavaSet';
import { HashMap } from '../../../java/util/HashMap';
import { GostFachUtils } from '../../../core/utils/gost/GostFachUtils';
import { AbiturFachbelegungHalbjahr } from '../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { AbiturdatenManager, cast_de_svws_nrw_core_abschluss_gost_AbiturdatenManager } from '../../../core/abschluss/gost/AbiturdatenManager';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { JavaMath } from '../../../java/lang/JavaMath';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import type { Comparator } from '../../../java/util/Comparator';
import { GostFachbereich } from '../../../core/types/gost/GostFachbereich';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { GostSchriftlichkeit } from '../../../core/types/gost/GostSchriftlichkeit';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { AbiFaecher, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_AbiFaecher } from '../../../core/abschluss/gost/belegpruefung/AbiFaecher';
import { HashSet } from '../../../java/util/HashSet';
import { GostAbiturMarkierungsalgorithmusMarkierung } from '../../../core/abschluss/gost/GostAbiturMarkierungsalgorithmusMarkierung';
import { GostFach } from '../../../core/data/gost/GostFach';
import { Fach } from '../../../asd/types/fach/Fach';
import { GostAbiturFach } from '../../../core/types/gost/GostAbiturFach';
import { AbiturFachbelegung } from '../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefung } from '../../../core/abschluss/gost/GostBelegpruefung';
import { Projektkurse, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Projektkurse } from '../../../core/abschluss/gost/belegpruefung/Projektkurse';
import { SprachendatenUtils } from '../../../core/utils/schueler/SprachendatenUtils';
import { GostAbiturMarkierungsalgorithmusBelegung } from '../../../core/abschluss/gost/GostAbiturMarkierungsalgorithmusBelegung';
import { NoteKatalogEintrag } from '../../../asd/data/NoteKatalogEintrag';
import { GostAbiturMarkierungsalgorithmusErgebnis } from '../../../core/abschluss/gost/GostAbiturMarkierungsalgorithmusErgebnis';
import { Note } from '../../../asd/types/Note';
import { Class } from '../../../java/lang/Class';
import type { JavaMap } from '../../../java/util/JavaMap';

export class GostAbiturMarkierungsalgorithmus extends JavaObject {

	/**
	 * Das Ergebnis des Algorithmus
	 */
	private readonly ergebnis : GostAbiturMarkierungsalgorithmusErgebnis = new GostAbiturMarkierungsalgorithmusErgebnis();

	/**
	 * Die aktuelle Einrückung für das Logging
	 */
	private logIndent : string = "";

	/**
	 * Der Abiturdaten-Manager
	 */
	private readonly manager : AbiturdatenManager;

	/**
	 * Die zuvor durchgeführten Belegprüfung zu dem Projektkurs
	 */
	private readonly belegpruefungProjektkurse : Projektkurse;

	/**
	 * Die zuvor durchgeführten Belegprüfung zu den Abiturfächern
	 */
	private readonly belegpruefungAbiturfaecher : AbiFaecher;

	/**
	 * Die Belegungen der vier Abiturfächer
	 */
	readonly abi : Array<AbiturFachbelegung | null> = Array(4).fill(null);

	/**
	 * Gibt an, ob die Zulassung mit diesem Ergebniss erreicht wurde oder nicht.
	 */
	private hatZulassung : boolean = false;

	/**
	 * Die Summe der Notenpunkte, normiert nach der Formel aus der APO-Gost
	 */
	private summeNormiert : number = -1;

	/**
	 * Die Summe der Notenpunkte aller Markierungen, LKs sind doppelt gezählt
	 */
	private summeNotenpunkte : number = 0;

	/**
	 * Die Summe der markierten Kurse, LKs sind doppelt gezählt
	 */
	private summeKurse : number = 0;

	/**
	 * Die Summe der markierten Defizite im LK-Bereich
	 */
	private defiziteLK : number = 0;

	/**
	 * Die Summe der markierten Defizite im GK-Bereich
	 */
	private defiziteGK : number = 0;

	/**
	 * Gibt an, wieviele Fremdsprachen im Abiturbereich markiert wurden.
	 */
	private anzahlAbiFremdsprachen : number = 0;

	/**
	 * Gibt an, ob im Abitur eine neu einsetzende Fremdsprache markiert wurde
	 */
	private hatAbiFremspracheNeueinsetzend : boolean = false;

	/**
	 * Gibt an, ob im Abitur eine Gesellschaftswissenschaft - außer Religion - markiert wurde
	 */
	private anzahlAbiGesellschaftswissenschaft : number = 0;

	/**
	 * Gibt an, ob im Abitur Kunst oder Musik gewählt wurde
	 */
	private hatAbiKunstOderMusik : boolean = false;

	/**
	 * Die Anzahl der noch erlaubten Musik-Kurse
	 */
	private restErlaubtMusik : number = 5;

	/**
	 * Die Anzahl der noch erlaubten Vokal- oder Instrumentalpraktischen Kurse
	 */
	private restErlaubtVPIP : number = 2;

	/**
	 * Die Anzahl der noch erlaubten Literatur Kurse
	 */
	private restErlaubtKUMUErsatz : number = 2;

	/**
	 * Eine Map für die Anzahl von Belegungen in einem Fachbereich
	 */
	private readonly anzahlBelegungen : JavaMap<GostFachbereich, number> = new HashMap<GostFachbereich, number>();

	/**
	 * Die Menge der markierten Halbjahresbelegungen
	 */
	private readonly markiert : HashMap2D<number, number, GostAbiturMarkierungsalgorithmusMarkierung> = new HashMap2D<number, number, GostAbiturMarkierungsalgorithmusMarkierung>();

	/**
	 * Vergleicht zwei bewertete Belegungen anhand der Note und als zweiten Kriterium anhand des Faches und als drittes Kriterium anhand des Halbjahres
	 */
	private readonly comparatorBelegungen : Comparator<GostAbiturMarkierungsalgorithmusBelegung> = { compare : (a: GostAbiturMarkierungsalgorithmusBelegung, b: GostAbiturMarkierungsalgorithmusBelegung) => {
		let tmp : number = b.notenpunkte - a.notenpunkte;
		if (tmp !== 0)
			return tmp;
		const aFach : GostFach | null = this.getFach(a.belegung);
		const bFach : GostFach | null = this.getFach(b.belegung);
		if ((aFach === null) || (bFach === null))
			return -1;
		tmp = GostFachbereich.compareGostFach(aFach, bFach);
		if (tmp !== 0)
			return tmp;
		const hjA : GostHalbjahr | null = GostHalbjahr.fromKuerzel(a.belegungHalbjahr.halbjahrKuerzel);
		const hjB : GostHalbjahr | null = GostHalbjahr.fromKuerzel(b.belegungHalbjahr.halbjahrKuerzel);
		if ((hjA === null) || (hjB === null))
			return -1;
		return hjB.id - hjA.id;
	} };

	/**
	 * Vergleicht zwei Ergebnisse miteinander und sortiert diese
	 */
	private static readonly comparatorStates : Comparator<GostAbiturMarkierungsalgorithmus> = { compare : (a: GostAbiturMarkierungsalgorithmus, b: GostAbiturMarkierungsalgorithmus) => {
		if (a.ergebnis.erfolgreich === !b.ergebnis.erfolgreich)
			return a.ergebnis.erfolgreich ? -1 : 1;
		if (a.hatZulassung === !b.hatZulassung)
			return a.hatZulassung ? -1 : 2;
		let tmp : number = b.summeNormiert - a.summeNormiert;
		if (tmp !== 0)
			return tmp;
		const aDefizite : number = a.defiziteLK + a.defiziteGK;
		const bDefizite : number = b.defiziteLK + b.defiziteGK;
		tmp = aDefizite - bDefizite;
		if (tmp !== 0)
			return tmp;
		tmp = a.defiziteLK - b.defiziteLK;
		if (tmp !== 0)
			return tmp;
		tmp = a.summeKurse - b.summeKurse;
		if (tmp !== 0)
			return tmp;
		const aPjk : number | null = a.anzahlBelegungen.get(GostFachbereich.PROJEKTKURSE);
		const bPjk : number | null = b.anzahlBelegungen.get(GostFachbereich.PROJEKTKURSE);
		return ((aPjk === null) ? 0 : aPjk) - ((bPjk === null) ? 0 : bPjk);
	} };


	/**
	 * Erstellt eine neue Instanz des Markierungsalgorithmus unter Verwendung des übergebenen Abiturdaten-Manager und den zuvor
	 * durchgeführten Belegprüfungen.
	 *
	 * @param manager            der Abiturdaten-Manager
	 * @param belegpruefungen    die durchgeführten Belegprüfungen
	 */
	private constructor(manager : AbiturdatenManager, belegpruefungen : List<GostBelegpruefung>);

	/**
	 * Erstellt einen neue Instanz des Algorithmus basierend auf der übergebenen Instanz.
	 *
	 * @param original   die ursprüngliche Instanz, welche kopiert wird
	 */
	private constructor(original : GostAbiturMarkierungsalgorithmus);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	private constructor(__param0 : AbiturdatenManager | GostAbiturMarkierungsalgorithmus, __param1? : List<GostBelegpruefung>) {
		super();
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.abschluss.gost.AbiturdatenManager')))) && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('java.util.List'))) || (__param1 === null))) {
			const manager : AbiturdatenManager = cast_de_svws_nrw_core_abschluss_gost_AbiturdatenManager(__param0);
			const belegpruefungen : List<GostBelegpruefung> = cast_java_util_List(__param1);
			this.manager = manager;
			let belegpruefungProjektkurse : Projektkurse | null = null;
			let belegpruefungAbiturfaecher : AbiFaecher | null = null;
			for (const pruefung of belegpruefungen) {
				if (((pruefung instanceof JavaObject) && (pruefung.isTranspiledInstanceOf('de.svws_nrw.core.abschluss.gost.belegpruefung.Projektkurse'))))
					belegpruefungProjektkurse = cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Projektkurse(pruefung);
				if (((pruefung instanceof JavaObject) && (pruefung.isTranspiledInstanceOf('de.svws_nrw.core.abschluss.gost.belegpruefung.AbiFaecher'))))
					belegpruefungAbiturfaecher = cast_de_svws_nrw_core_abschluss_gost_belegpruefung_AbiFaecher(pruefung);
			}
			if (belegpruefungProjektkurse === null)
				throw new DeveloperNotificationException("Die Projektkursprüfung muss als Belegprüfung vorhanden sein.")
			this.belegpruefungProjektkurse = belegpruefungProjektkurse;
			if (belegpruefungAbiturfaecher === null)
				throw new DeveloperNotificationException("Die Abiturfächerprüfung muss als Belegprüfung vorhanden sein.")
			this.belegpruefungAbiturfaecher = belegpruefungAbiturfaecher;
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmus')))) && (__param1 === undefined)) {
			const original : GostAbiturMarkierungsalgorithmus = cast_de_svws_nrw_core_abschluss_gost_GostAbiturMarkierungsalgorithmus(__param0);
			this.ergebnis.markierungen.addAll(original.ergebnis.markierungen);
			this.ergebnis.log = original.ergebnis.log;
			this.logIndent = original.logIndent + "    ";
			this.ergebnis.erfolgreich = original.ergebnis.erfolgreich;
			this.manager = original.manager;
			this.belegpruefungProjektkurse = original.belegpruefungProjektkurse;
			this.belegpruefungAbiturfaecher = original.belegpruefungAbiturfaecher;
			for (let i : number = 0; i < this.abi.length; i++)
				this.abi[i] = original.abi[i];
			this.summeNormiert = original.summeNormiert;
			this.hatZulassung = original.hatZulassung;
			this.summeNotenpunkte = original.summeNotenpunkte;
			this.summeKurse = original.summeKurse;
			this.defiziteLK = original.defiziteLK;
			this.defiziteGK = original.defiziteGK;
			this.anzahlAbiFremdsprachen = original.anzahlAbiFremdsprachen;
			this.anzahlAbiGesellschaftswissenschaft = original.anzahlAbiGesellschaftswissenschaft;
			this.hatAbiFremspracheNeueinsetzend = original.hatAbiFremspracheNeueinsetzend;
			this.hatAbiKunstOderMusik = original.hatAbiKunstOderMusik;
			this.restErlaubtMusik = original.restErlaubtMusik;
			this.restErlaubtVPIP = original.restErlaubtVPIP;
			this.restErlaubtKUMUErsatz = original.restErlaubtKUMUErsatz;
			for (const e of original.anzahlBelegungen.entrySet())
				this.anzahlBelegungen.put(e.getKey(), e.getValue());
			for (const e1 of original.markiert.getEntrySet())
				for (const e2 of e1.getValue().entrySet())
					this.markiert.put(e1.getKey(), e2.getKey(), e2.getValue());
		} else throw new Error('invalid method overload');
	}

	/**
	 * Führt eine automatische Markierung von Halbjahresbelegungen zur Verwendung in Block II
	 * von anrechenbaren Kursen für die Abiturberechnung durch. Vorraussetzung hierfür ist, dass
	 * alle anrechenbare Kurse ein gültige Note zugeordnet haben.
	 *
	 * @param manager           der Manager zur Verwaltung der Abiturdaten.
	 * @param belegpruefungen   die zuvor durchgeführten Belegprüfung der Laufbahnplanung
	 *
	 * @return das Ergebnis der Berechnung
	 */
	public static berechne(manager : AbiturdatenManager, belegpruefungen : List<GostBelegpruefung>) : GostAbiturMarkierungsalgorithmusErgebnis {
		const initialState : GostAbiturMarkierungsalgorithmus = new GostAbiturMarkierungsalgorithmus(manager, belegpruefungen);
		if (!initialState.init())
			return initialState.ergebnis;
		const states : List<GostAbiturMarkierungsalgorithmus> = initialState.markiereErsteFremsprache();
		if (states.isEmpty())
			return initialState.ergebnis;
		states.sort(GostAbiturMarkierungsalgorithmus.comparatorStates);
		const state : GostAbiturMarkierungsalgorithmus = states.getFirst();
		state.schreibeErgebnis();
		const ergebnis : GostAbiturMarkierungsalgorithmusErgebnis = state.ergebnis;
		return ergebnis;
	}

	/**
	 * Initialisiert den Zustand des Markierungsalgorithmus mit Markierungen von Kursen, welche auf jeden Fall
	 * markiert werden müssen. Diese bilden die Grundlage für späteren Markierungsschritte.
	 *
	 * @return true, falls nach der Initialisierung noch eine Markierung möglich ist, und ansonsten false
	 */
	private init() : boolean {
		const anzahlFortfuehrbareFremdsprachen : number = SprachendatenUtils.getFortfuehrbareSprachenInGOSt(this.manager.getSprachendaten()).size();
		this.ergebnis.log.add(this.logIndent + "Anzahl der Fortführbaren Fremdsprachen: " + anzahlFortfuehrbareFremdsprachen);
		const hatSchwerpunktFremdsprachen : boolean = 2 <= this.manager.zaehleBelegungInHalbjahren(this.manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE), GostHalbjahr.Q22);
		const hatSchwerpunktNaturwissenschaften : boolean = 2 <= this.manager.zaehleBelegungInHalbjahren(this.manager.getFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH), GostHalbjahr.Q22);
		this.ergebnis.log.add(this.logIndent + "Schwerpunkt: " + (hatSchwerpunktFremdsprachen ? "Fremdsprachen" : "") + " " + (hatSchwerpunktNaturwissenschaften ? "Naturwissenschaften" : ""));
		if (!this.markiereAbiturfaecher())
			return false;
		if (!this.markiereDeutsch())
			return false;
		if (!this.markiereMathematik())
			return false;
		return true;
	}

	/**
	 * Schreibt die gesetzten Markierung in das Ergebnis und trägt zusätzlich für die nicht markierten Kursbelegungen ein, dass diese
	 * nicht markiert sind.
	 */
	private schreibeErgebnis() : void {
		for (const belegung of this.manager.daten().fachbelegungen) {
			const fach : GostFach | null = this.getFach(belegung);
			if (fach === null)
				return;
			if (GostFachbereich.getBereiche(fach).isEmpty())
				continue;
			for (const hj of GostHalbjahr.getQualifikationsphase()) {
				const hjBelegung : AbiturFachbelegungHalbjahr | null = belegung.belegungen[hj.id];
				if (hjBelegung === null)
					continue;
				let markierung : GostAbiturMarkierungsalgorithmusMarkierung | null = this.markiert.getOrNull(fach.id, hj.id);
				if (markierung === null) {
					markierung = new GostAbiturMarkierungsalgorithmusMarkierung();
					markierung.idFach = fach.id;
					markierung.idHalbjahr = hj.id;
					markierung.markiert = false;
				}
				this.ergebnis.markierungen.add(markierung);
			}
		}
	}

	/**
	 * Ermittel die Menge aller Sprachbelegungen, die für den Sprachenschwerpunkt in Frage kommen.
	 *
	 * @return die Menge aller Sprachbelegungen, die für den Sprachenschwerpunkt in Frage kommen
	 */
	private getSprachbelegungenFuerSchwerpunkt() : JavaSet<AbiturFachbelegung> {
		const belegungen : JavaSet<AbiturFachbelegung> = new HashSet<AbiturFachbelegung>();
		const belegteFremdsprachen : JavaSet<string> = new HashSet<string>();
		for (const belegung of this.manager.getRelevanteFachbelegungen(GostFachbereich.FREMDSPRACHE)) {
			if ((this.manager.pruefeBelegungMitSchriftlichkeit(belegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21)) && (this.manager.pruefeBelegung(belegung, GostHalbjahr.Q22))) {
				const fach : GostFach | null = this.manager.getFach(belegung);
				if (fach === null)
					continue;
				const fs : string | null = GostFachUtils.getFremdsprache(fach);
				if (fs === null)
					continue;
				belegteFremdsprachen.add(fs);
				belegungen.add(belegung);
			}
		}
		for (const belegung of this.manager.getFachbelegungenBilingual()) {
			if ((this.manager.pruefeBelegungMitSchriftlichkeit(belegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21)) && (this.manager.pruefeBelegung(belegung, GostHalbjahr.Q22))) {
				const fach : GostFach | null = this.manager.getFach(belegung);
				if (fach === null)
					continue;
				if (belegteFremdsprachen.contains(fach.biliSprache))
					continue;
				belegungen.add(belegung);
			}
		}
		return belegungen;
	}

	/**
	 * Bestimmt das Fach für die übergeben Abiturfachbelegung.
	 *
	 * @param belegung   die Fachbelegung
	 *
	 * @return das Fach
	 */
	private getFach(belegung : AbiturFachbelegung) : GostFach | null {
		const fach : GostFach | null = this.manager.faecher().get(belegung.fachID);
		if (fach === null) {
			this.ergebnis.log.add(this.logIndent + "Fehler im Algorithmus: Kann das Fach mit der ID " + belegung.fachID + " nicht bestimmen. Überprüfen Sie die Fächerdefinition für dieses Fach.");
			this.ergebnis.erfolgreich = false;
		}
		return fach;
	}

	/**
	 * Bestimme die zwei besten Halbjahresbelegungen und gib die Halbjahre zurück.
	 *
	 * @param belegung   die Fachbelegung
	 *
	 * @return die beiden besten Halbjahre
	 */
	private getZweiBesteBelegungen(belegung : AbiturFachbelegung) : Array<GostHalbjahr | null> {
		const result : Array<GostHalbjahr | null> = Array(2).fill(null);
		let np1 : number = -1;
		let np2 : number = -1;
		for (const hj of GostHalbjahr.getQualifikationsphase()) {
			const np : number = this.getNotenpunkte(belegung, hj);
			if (np <= 0)
				continue;
			if ((result[0] === null) || (np > np1)) {
				result[1] = result[0];
				np2 = np1;
				result[0] = hj;
				np1 = np;
			} else
				if ((result[1] === null) || (np > np2)) {
					result[1] = hj;
					np2 = np;
				}
		}
		return result;
	}

	/**
	 * Erhöht die Anzahl der Belegungen in dem angegebenen Fachbereich, sofern das Fach zu dem Fachbereich
	 * gehört.
	 *
	 * @param fb     der Fachbereich
	 * @param fach   das Fach, welches belegt wurde
	 */
	private increaseBelegungInFachbereich(fb : GostFachbereich, fach : GostFach) : void {
		if (fb.hat(fach)) {
			let anzahl : number | null = this.anzahlBelegungen.computeIfAbsent(fb, { apply : (k: GostFachbereich | null) => 0 });
			if (anzahl === null)
				anzahl = 0;
			this.anzahlBelegungen.put(fb, anzahl + 1);
		}
	}

	/**
	 * Bestimmt die Summe der Notenpunkte einer Belegung in den angegebenen Halbjahren. Sollte die Belegung
	 * nicht vorhanden oder fehlerhaft sein, so wird -1 zurückgegeben.
	 *
	 * @param belegung    die Fachbelegung
	 * @param halbjahre   die Halbjahre
	 *
	 * @return die Summe der Notenpunkte
	 */
	private getNotenpunkte(belegung : AbiturFachbelegung, ...halbjahre : Array<GostHalbjahr>) : number {
		let summe : number = 0;
		for (const hj of halbjahre) {
			const hjBelegung : AbiturFachbelegungHalbjahr | null = belegung.belegungen[hj.id];
			if (hjBelegung === null)
				return -1;
			if ((hjBelegung.notenkuerzel === null) || (JavaString.isBlank(hjBelegung.notenkuerzel)))
				return -1;
			const note : Note = Note.fromKuerzel(hjBelegung.notenkuerzel);
			if (!note.istNote(this.manager.getSchuljahr()))
				return -1;
			const nke : NoteKatalogEintrag | null = note.daten(this.manager.getSchuljahr());
			if (nke === null)
				return -1;
			if (nke.notenpunkte === null)
				return -1;
			summe += nke.notenpunkte;
		}
		return summe;
	}

	private markiereHalbjahresbelegung(belegung : AbiturFachbelegung, hj : GostHalbjahr) : boolean {
		const istAbiturbereich : boolean = (belegung.abiturFach !== null);
		const fach : GostFach | null = this.getFach(belegung);
		if (fach === null)
			return false;
		const hjBelegung : AbiturFachbelegungHalbjahr | null = belegung.belegungen[hj.id];
		if (hjBelegung === null) {
			if (istAbiturbereich)
				this.ergebnis.log.add(this.logIndent + "  Im Halbjahr " + hj.kuerzel + " fehlt eine Belegung des Abiturfaches");
			return false;
		}
		if ((hjBelegung.notenkuerzel === null) || (JavaString.isBlank(hjBelegung.notenkuerzel))) {
			this.ergebnis.log.add(this.logIndent + "  Im Halbjahr " + hj.kuerzel + " wurde für das Fach keine gültige Note erteilt. Der Kurs kann daher nicht anrechenbar sein");
			return false;
		}
		const note : Note = Note.fromKuerzel(hjBelegung.notenkuerzel);
		if (!note.istNote(this.manager.getSchuljahr())) {
			this.ergebnis.log.add(this.logIndent + "  Im Halbjahr " + hj.kuerzel + " wurde für das Fach eine Pseudonote erteilt. Der Kurs kann daher nicht anrechenbar sein.");
			return false;
		}
		if (note as unknown === Note.UNGENUEGEND as unknown) {
			if (istAbiturbereich)
				this.ergebnis.log.add(this.logIndent + "  Im Halbjahr " + hj.kuerzel + " wurde die Note ungenügend für das Abiturfach erteilt. Somit ist keine Zulassung mehr möglich, da der Kurs somit als nicht belegt gilt.");
			return false;
		}
		const istLKBelegung : boolean = JavaObject.equalsTranspiler(GostKursart.LK.kuerzel, (hjBelegung.kursartKuerzel));
		if ((note as unknown === Note.AUSREICHEND_MINUS as unknown) || (note as unknown === Note.MANGELHAFT_PLUS as unknown) || (note as unknown === Note.MANGELHAFT as unknown) || (note as unknown === Note.MANGELHAFT_MINUS as unknown)) {
			if (istLKBelegung) {
				this.ergebnis.log.add(this.logIndent + "  Im Halbjahr " + hj.kuerzel + " liegt ein Defizit im LK-Bereich vor.");
				this.defiziteLK++;
			} else {
				this.ergebnis.log.add(this.logIndent + "  Im Halbjahr " + hj.kuerzel + " liegt ein einbringungspflichtiges Defizit im GK-Bereich vor.");
				this.defiziteGK++;
			}
		}
		if (this.defiziteLK > 3) {
			this.ergebnis.log.add(this.logIndent + "  Es ist keine Zulassung mehr möglich, da die maximale Anzahl der Defizite im Leistungskursbereich überschritten wurde.");
			return false;
		}
		const nke : NoteKatalogEintrag | null = note.daten(this.manager.getSchuljahr());
		if ((nke === null) || (nke.notenpunkte === null)) {
			this.ergebnis.log.add(this.logIndent + "  Interner Fehler: Der Note sind keine Notenpunkte zugeordnet.");
			return false;
		}
		this.summeNotenpunkte += (istLKBelegung ? 2 : 1) * nke.notenpunkte;
		this.summeKurse++;
		this.increaseBelegungInFachbereich(GostFachbereich.DEUTSCH, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.FREMDSPRACHE, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.KUNST_MUSIK, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.GESCHICHTE, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.SOZIALWISSENSCHAFTEN, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.PHILOSOPHIE, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.RELIGION, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.MATHEMATIK, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.NATURWISSENSCHAFTLICH_NEU_EINSETZEND, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.NATURWISSENSCHAFTLICH, fach);
		this.increaseBelegungInFachbereich(GostFachbereich.PROJEKTKURSE, fach);
		const markierung : GostAbiturMarkierungsalgorithmusMarkierung = new GostAbiturMarkierungsalgorithmusMarkierung();
		markierung.idFach = belegung.fachID;
		markierung.idHalbjahr = hj.id;
		markierung.markiert = true;
		this.markiert.put(markierung.idFach, markierung.idHalbjahr, markierung);
		this.ergebnis.log.add(this.logIndent + "    Markiere " + hj.kuerzel + " im Fach " + fach.kuerzelAnzeige + " (" + nke.notenpunkte + ").");
		return true;
	}

	private markiereBelegungDurchgaengig(belegung : AbiturFachbelegung | null) : boolean {
		if (belegung === null) {
			this.ergebnis.log.add(this.logIndent + "  Es liegt keine Fachbelegung vor.");
			return false;
		}
		const fach : GostFach | null = this.getFach(belegung);
		if (fach === null)
			return false;
		const fachbelegungen : List<AbiturFachbelegung> = this.manager.getFachbelegungByFachkuerzel(fach.kuerzel);
		for (const hj of GostHalbjahr.getQualifikationsphase()) {
			let current : AbiturFachbelegung | null = null;
			for (const fb of fachbelegungen) {
				if (fb.belegungen[hj.id] !== null) {
					if (current !== null) {
						this.ergebnis.log.add(this.logIndent + "  Die gleichzeitige Belegung eines Sachfaches bilingual und nicht-biligual ist nicht zulässig.");
						return false;
					}
					current = fb;
				}
			}
			if (current === null) {
				this.ergebnis.log.add(this.logIndent + "  Im Halbjahr " + hj.kuerzel + " wurde für das Fach keine gültige Belegung gefunden.");
				return false;
			}
			const hjBelegung : AbiturFachbelegungHalbjahr | null = current.belegungen[hj.id];
			if ((hjBelegung === null) || (hjBelegung.notenkuerzel === null) || (JavaString.isBlank(hjBelegung.notenkuerzel))) {
				this.ergebnis.log.add(this.logIndent + "  Im Halbjahr " + hj.kuerzel + " wurde für das Fach keine gültige Note erteilt.");
				return false;
			}
			const note : Note = Note.fromKuerzel(hjBelegung.notenkuerzel);
			if (!note.istNote(this.manager.getSchuljahr())) {
				this.ergebnis.log.add(this.logIndent + "  Im Halbjahr " + hj.kuerzel + " wurde für das Fach eine Pseudonote erteilt. Das ist nicht zulässig.");
				return false;
			}
			if (note as unknown === Note.UNGENUEGEND as unknown) {
				this.ergebnis.log.add(this.logIndent + "  Im Halbjahr " + hj.kuerzel + " wurde die Note ungenügend für das Fach erteilt. Somit ist keine Zulassung mehr möglich, da das Fach somit als nicht belegt gilt.");
				return false;
			}
			if ((this.markiert.getOrNull(fach.id, hj.id) === null) && (!this.markiereHalbjahresbelegung(current, hj)))
				return false;
		}
		return true;
	}

	/**
	 * Markiert die beiden besten Halbjahresbelegungen der Qualifikationsphase aus der
	 * übergebenen Menge der Fachbelegungen.
	 *
	 * @param belegungen   die Belegungen
	 *
	 * @return true, wenn die Markierungen erfolgreich waren, und ansonsten false
	 */
	private markiereZweiBeste(belegungen : List<AbiturFachbelegung>) : boolean {
		if (this.manager.zaehleBelegungInHalbjahren(belegungen, ...GostHalbjahr.getQualifikationsphase()) < 2) {
			this.ergebnis.log.add(this.logIndent + "  Keine Fachbelegung in zwei Halbjahren vorhanden.");
			return false;
		}
		const resBelegung : Array<AbiturFachbelegung | null> = Array(2).fill(null);
		const resFach : Array<GostFach | null> = Array(2).fill(null);
		const resHalbjahre : Array<GostHalbjahr | null> = Array(2).fill(null);
		const resNotenpunkte : Array<number> = Array(2).fill(0);
		for (const belegung of belegungen) {
			const fach : GostFach | null = this.getFach(belegung);
			if (fach === null)
				continue;
			const halbjahre : Array<GostHalbjahr | null> = this.getZweiBesteBelegungen(belegung);
			for (const hj of halbjahre) {
				if (hj === null)
					continue;
				const np : number = this.getNotenpunkte(belegung, hj);
				if ((resBelegung[0] === null) || (np > resNotenpunkte[0])) {
					resBelegung[1] = resBelegung[0];
					resFach[1] = resFach[0];
					resHalbjahre[1] = resHalbjahre[0];
					resNotenpunkte[1] = resNotenpunkte[0];
					resBelegung[0] = belegung;
					resFach[0] = fach;
					resHalbjahre[0] = hj;
					resNotenpunkte[0] = np;
				} else
					if ((resBelegung[1] === null) || (np > resNotenpunkte[1])) {
						resBelegung[1] = belegung;
						resFach[1] = fach;
						resHalbjahre[1] = hj;
						resNotenpunkte[1] = np;
					}
			}
		}
		if ((resBelegung[0] === null) || (resBelegung[1] === null) || (resHalbjahre[0] === null) || (resHalbjahre[1] === null) || (resFach[0] === null) || (resFach[1] === null)) {
			this.ergebnis.log.add(this.logIndent + "  Fehler: Konnte keine Bewertung für zwei Halbjahre bestimmen.");
			return false;
		}
		this.ergebnis.log.add(this.logIndent + "  Markiere die beiden Kurse in " + resHalbjahre[0].kuerzel + " (" + resNotenpunkte[0] + " Punkte) für " + resFach[0].kuerzelAnzeige + " und " + resHalbjahre[1].kuerzel + " (" + resNotenpunkte[1] + " Punkte) für " + resFach[1].kuerzelAnzeige + "...");
		return this.markiereHalbjahresbelegung(resBelegung[0], resHalbjahre[0]) && this.markiereHalbjahresbelegung(resBelegung[1], resHalbjahre[1]);
	}

	private markiereAbiturfaecher() : boolean {
		this.abi[0] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.LK1);
		this.abi[1] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.LK2);
		this.abi[2] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.AB3);
		this.abi[3] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.AB4);
		for (let i : number = 1; i <= 4; i++) {
			const belegung : AbiturFachbelegung | null = this.abi[i - 1];
			if (belegung === null) {
				this.ergebnis.log.add(this.logIndent + "Es wurde keine Belegung für das  " + i + ". Abiturfaches gefunden.");
				return false;
			}
			const fach : GostFach | null = this.getFach(belegung);
			if (fach === null)
				return false;
			this.ergebnis.log.add(this.logIndent + "Markiere die vier Kurse des " + i + ". Abiturfaches (" + fach.kuerzelAnzeige + ")...");
			if (fach.istFremdsprache)
				this.anzahlAbiFremdsprachen++;
			if (fach.istFremdsprache && fach.istFremdSpracheNeuEinsetzend)
				this.hatAbiFremspracheNeueinsetzend = true;
			if (GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH.hat(fach))
				this.anzahlAbiGesellschaftswissenschaft++;
			const istKunst : boolean = this.manager.faecher().fachIstKunst(fach.id);
			const istMusik : boolean = this.manager.faecher().fachIstMusik(fach.id);
			if (istKunst || istMusik)
				this.hatAbiKunstOderMusik = true;
			if (istMusik && ((i === 1) || (i === 2))) {
				this.restErlaubtMusik = 0;
				this.restErlaubtVPIP = 0;
			} else
				if (istMusik) {
					this.restErlaubtMusik = 2;
				}
			if (!this.markiereBelegungDurchgaengig(belegung))
				return false;
		}
		return true;
	}

	/**
	 * Markiere alle Kurse des Faches Deutsch, sofern dieses noch nicht belegt wurde
	 *
	 * @return true, wenn die Deutsch-Fachbelegung sichergestellt ist
	 */
	private markiereDeutsch() : boolean {
		if (this.anzahlBelegungen.computeIfAbsent(GostFachbereich.DEUTSCH, { apply : (k: GostFachbereich | null) => 0 }) === 0) {
			this.ergebnis.log.add(this.logIndent + "Markiere die vier Kurse für das Fach Deutsch...");
			const deutsch : AbiturFachbelegung | null = this.manager.getRelevanteFachbelegung(GostFachbereich.DEUTSCH);
			if (!this.markiereBelegungDurchgaengig(deutsch))
				return false;
		}
		return true;
	}

	/**
	 * Markiere alle Kurse des Faches Mathematik, sofern dieses noch nicht belegt wurde
	 *
	 * @return true, wenn die Mathematik-Fachbelegung sichergestellt ist
	 */
	private markiereMathematik() : boolean {
		if (this.anzahlBelegungen.computeIfAbsent(GostFachbereich.MATHEMATIK, { apply : (k: GostFachbereich | null) => 0 }) === 0) {
			this.ergebnis.log.add(this.logIndent + "Markiere die vier Kurse für das Fach Mathematik...");
			const mathematik : AbiturFachbelegung | null = this.manager.getRelevanteFachbelegung(GostFachbereich.MATHEMATIK);
			if (!this.markiereBelegungDurchgaengig(mathematik))
				return false;
		}
		return true;
	}

	/**
	 * Markierungsbaum - Ebene 1:
	 * Markiere alle Kurse einer Fremdsprache, die durchgängig belegt wurde, sofern nicht schon
	 * eine als Abiturfach belegt wurde.
	 * Führe für diese Knoten dabei jeweils die Berechnung aus der 2. Ebene aus.
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private markiereErsteFremsprache() : List<GostAbiturMarkierungsalgorithmus> {
		const newStates : List<GostAbiturMarkierungsalgorithmus> = new ArrayList<GostAbiturMarkierungsalgorithmus>();
		this.ergebnis.log.add(this.logIndent + "Markierung der ersten Fremdsprache:");
		if (this.anzahlAbiFremdsprachen >= 1) {
			this.ergebnis.log.add(this.logIndent + "  Eine erste Fremdsprache wurde bereits im Abiturbereich markiert.");
			newStates.addAll((new GostAbiturMarkierungsalgorithmus(this)).markiereErsteKlassischeNaturwissenschaft());
			return newStates;
		}
		const fremdsprachen : List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		for (const tmpFremdsprache of this.manager.getRelevanteFachbelegungen(GostFachbereich.FREMDSPRACHE))
			if (this.manager.pruefeBelegung(tmpFremdsprache, ...GostHalbjahr.getQualifikationsphase()))
				fremdsprachen.add(tmpFremdsprache);
		if (fremdsprachen.isEmpty()) {
			this.ergebnis.log.add(this.logIndent + "  Konnte keine durchgängig belegte Fremdsprache zur Markierung ermitteln.");
			return newStates;
		}
		for (const fremdsprache of fremdsprachen) {
			const fach : GostFach | null = this.getFach(fremdsprache);
			if (fach === null)
				continue;
			const newState : GostAbiturMarkierungsalgorithmus = new GostAbiturMarkierungsalgorithmus(this);
			this.ergebnis.log.add(this.logIndent + "  Fallunterscheidung: Markiere die vier Kurse für die Fremdsprache " + fach.kuerzelAnzeige + "...");
			if (!newState.markiereBelegungDurchgaengig(fremdsprache))
				continue;
			newStates.addAll(newState.markiereErsteKlassischeNaturwissenschaft());
		}
		return newStates;
	}

	/**
	 * Markierungsbaum - Ebene 2:
	 * Markiere alle Kurse klassischen Naturwissenschaft, die durchgängig belegt wurde, sofern nicht schon
	 * eine als Abiturfach belegt wurde.
	 * Führe für diese Knoten dabei jeweils die Berechnung aus der 3. Ebene aus.
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private markiereErsteKlassischeNaturwissenschaft() : List<GostAbiturMarkierungsalgorithmus> {
		this.ergebnis.log.add(this.logIndent + "Markierung der ersten klassischen Naturwissenschaft:");
		const newStates : List<GostAbiturMarkierungsalgorithmus> = new ArrayList<GostAbiturMarkierungsalgorithmus>();
		const anzahlNW : number | null = this.anzahlBelegungen.computeIfAbsent(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH, { apply : (k: GostFachbereich | null) => 0 });
		if ((anzahlNW !== null) && (anzahlNW > 0)) {
			this.ergebnis.log.add(this.logIndent + "  Eine klassische Naturwissenschaft wurde bereits im Abiturbereich markiert.");
			newStates.addAll((new GostAbiturMarkierungsalgorithmus(this)).markiereZweitesSchwerpunktfachQ2());
			return newStates;
		}
		const belegungen : List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		const bereitsGeprueft : JavaSet<string> = new HashSet<string>();
		for (const belegung of this.manager.getRelevanteFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH)) {
			const fach : GostFach | null = this.getFach(belegung);
			if (fach === null)
				continue;
			if (bereitsGeprueft.contains(fach.kuerzel))
				continue;
			const tmpBelegung : List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
			tmpBelegung.add(belegung);
			if (this.manager.pruefeBelegungExistiert(tmpBelegung, ...GostHalbjahr.getQualifikationsphase()))
				belegungen.add(belegung);
			bereitsGeprueft.add(fach.kuerzel);
		}
		if (belegungen.isEmpty()) {
			this.ergebnis.log.add(this.logIndent + "  Konnte keine durchgängig belegte klassische Naturwissenschaft zur Markierung ermitteln.");
			return newStates;
		}
		for (const belegung of belegungen) {
			const fach : GostFach | null = this.getFach(belegung);
			if (fach === null)
				continue;
			const newState : GostAbiturMarkierungsalgorithmus = new GostAbiturMarkierungsalgorithmus(this);
			this.ergebnis.log.add(this.logIndent + "  Fallunterscheidung: Markiere die vier Kurse für die klassische Naturwissenschaft " + fach.kuerzelAnzeige + "...");
			if (!newState.markiereBelegungDurchgaengig(belegung))
				continue;
			newStates.addAll(newState.markiereZweitesSchwerpunktfachQ2());
		}
		return newStates;
	}

	/**
	 * Markierungsbaum - Ebene 3:
	 * Markiere je nach Schwerpunkt die beiden Kurse aus der Q2 des weiteren Schwerpunktfaches. Im Falle einer
	 * Fremdsprache muss die Belegung schriftlich sein.
	 * Führe für diese Knoten dabei jeweils die Berechnung aus der 4. Ebene aus.
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private markiereZweitesSchwerpunktfachQ2() : List<GostAbiturMarkierungsalgorithmus> {
		this.ergebnis.log.add(this.logIndent + "Markierung der zweiten Schwerpunktfaches (Fremdsprache oder Naturwissenschaft in der Q2):");
		const newStates : List<GostAbiturMarkierungsalgorithmus> = new ArrayList<GostAbiturMarkierungsalgorithmus>();
		if (this.anzahlAbiFremdsprachen >= 2) {
			this.ergebnis.log.add(this.logIndent + "  Eine zweite Fremdsprache wurde bereits im Abiturbereich markiert.");
			newStates.addAll((new GostAbiturMarkierungsalgorithmus(this)).markiereKunstMusikOderErsatz());
			return newStates;
		}
		const anzahlNW : number | null = this.anzahlBelegungen.computeIfAbsent(GostFachbereich.NATURWISSENSCHAFTLICH, { apply : (k: GostFachbereich | null) => 0 });
		if ((anzahlNW !== null) && (anzahlNW >= 8)) {
			this.ergebnis.log.add(this.logIndent + "  Es wurden bereits zwei Naturwissenschaften markiert.");
			newStates.addAll((new GostAbiturMarkierungsalgorithmus(this)).markiereKunstMusikOderErsatz());
			return newStates;
		}
		const setBelegungenSprachenschwerpunkt : JavaSet<AbiturFachbelegung> = this.getSprachbelegungenFuerSchwerpunkt();
		const hatSchwerpunktFremdsprachen : boolean = 2 <= setBelegungenSprachenschwerpunkt.size();
		const hatSchwerpunktNaturwissenschaften : boolean = 2 <= this.manager.zaehleBelegungInHalbjahren(this.manager.getFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH), GostHalbjahr.Q22);
		const belegungen : JavaSet<AbiturFachbelegung> = new HashSet<AbiturFachbelegung>();
		if (hatSchwerpunktFremdsprachen) {
			for (const belegung of this.manager.getRelevanteFachbelegungen(GostFachbereich.FREMDSPRACHE))
				if (setBelegungenSprachenschwerpunkt.contains(belegung) && (!this.markiert.contains(belegung.fachID, GostHalbjahr.Q21.id) && !this.markiert.contains(belegung.fachID, GostHalbjahr.Q22.id)))
					belegungen.add(belegung);
			for (const belegung of this.manager.getFachbelegungenBilingual())
				if (setBelegungenSprachenschwerpunkt.contains(belegung))
					belegungen.add(belegung);
		}
		if (hatSchwerpunktNaturwissenschaften)
			for (const belegung of this.manager.getRelevanteFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH))
				if (this.manager.pruefeBelegung(belegung, ...GostHalbjahr.getQualifikationsphase()) && (!this.markiert.contains(belegung.fachID, GostHalbjahr.Q21.id) && !this.markiert.contains(belegung.fachID, GostHalbjahr.Q22.id)) && !setBelegungenSprachenschwerpunkt.contains(belegung))
					belegungen.add(belegung);
		if (belegungen.isEmpty()) {
			this.ergebnis.log.add(this.logIndent + "  Konnte kein durchgängig belegtes Schwerpunktfach zur Markierung ermitteln.");
			return newStates;
		}
		for (const belegung of belegungen) {
			const fach : GostFach | null = this.getFach(belegung);
			if (fach === null)
				continue;
			const newState : GostAbiturMarkierungsalgorithmus = new GostAbiturMarkierungsalgorithmus(this);
			this.ergebnis.log.add(this.logIndent + "  Fallunterscheidung: Markiere die beiden Kurse in der Q2 für das Schwerpunktfach " + fach.kuerzelAnzeige + "...");
			if (!this.markiert.contains(belegung.fachID, GostHalbjahr.Q21.id) && !newState.markiereHalbjahresbelegung(belegung, GostHalbjahr.Q21))
				continue;
			if (!this.markiert.contains(belegung.fachID, GostHalbjahr.Q22.id) && !newState.markiereHalbjahresbelegung(belegung, GostHalbjahr.Q22))
				continue;
			newStates.addAll(newState.markiereKunstMusikOderErsatz());
		}
		return newStates;
	}

	/**
	 * Markierungsbaum - Ebene 4:
	 * Markiere Kunst, Musik oder ein Ersatzfach, wenn nicht bereits Kunst oder Musik im Abiturbereich
	 * markiert wurde.
	 * Führe für diese Knoten dabei jeweils die Berechnung aus der 5. Ebene aus.
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private markiereKunstMusikOderErsatz() : List<GostAbiturMarkierungsalgorithmus> {
		this.ergebnis.log.add(this.logIndent + "Markierung von Kunst, Musik oder einem Ersatzfach:");
		const newStates : List<GostAbiturMarkierungsalgorithmus> = new ArrayList<GostAbiturMarkierungsalgorithmus>();
		if (this.hatAbiKunstOderMusik) {
			this.ergebnis.log.add(this.logIndent + "  Kunst oder Musik wurde bereits im Abiturbereich markiert.");
			const newState : GostAbiturMarkierungsalgorithmus = new GostAbiturMarkierungsalgorithmus(this);
			if (!newState.markiereGeschichteBzwSozialwissenschaften(GostFachbereich.GESCHICHTE))
				return newStates;
			if (!newState.markiereGeschichteBzwSozialwissenschaften(GostFachbereich.SOZIALWISSENSCHAFTEN))
				return newStates;
			newStates.addAll(newState.markiereEinsprachlerNeuEinsetzendeFremdsprache());
			return newStates;
		}
		const belegungen : List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		for (const belegung of this.manager.getRelevanteFachbelegungen(GostFachbereich.LITERARISCH_KUENSTLERISCH))
			if (this.manager.zaehleHalbjahresbelegungen(belegung, ...GostHalbjahr.getQualifikationsphase()) >= 2)
				belegungen.add(belegung);
		if (belegungen.isEmpty()) {
			this.ergebnis.log.add(this.logIndent + "  Keine Fachbelegung von Kunst, Musik oder einem Ersatzfach in zwei Halbjahren vorhanden.");
			return newStates;
		}
		for (const belegung of belegungen) {
			const fach : GostFach | null = this.getFach(belegung);
			if (fach === null)
				continue;
			const hj : Array<GostHalbjahr | null> = this.getZweiBesteBelegungen(belegung);
			if ((hj[0] === null) || (hj[1] === null)) {
				this.ergebnis.log.add(this.logIndent + "  Fehler: Konnte keine Bewertung für zwei Halbjahre bestimmen.");
				continue;
			}
			const np1 : number = this.getNotenpunkte(belegung, hj[0]);
			const np2 : number = this.getNotenpunkte(belegung, hj[1]);
			const newState : GostAbiturMarkierungsalgorithmus = new GostAbiturMarkierungsalgorithmus(this);
			this.ergebnis.log.add(this.logIndent + "  Fallunterscheidung: Markiere die beiden Kurse in " + hj[0].kuerzel + " (" + np1 + " Punkte) und " + hj[1].kuerzel + " (" + np2 + " Punkte) für das " + fach.kuerzelAnzeige + "...");
			if (!newState.markiereHalbjahresbelegung(belegung, hj[0]))
				continue;
			if (!newState.markiereHalbjahresbelegung(belegung, hj[1]))
				continue;
			if (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)) {
				newState.restErlaubtVPIP = 0;
				newState.restErlaubtKUMUErsatz = 0;
			}
			const f : Fach = Fach.getBySchluesselOrDefault(fach.kuerzel);
			if ((f as unknown === Fach.MU as unknown) || (f as unknown === Fach.IN as unknown) || (f as unknown === Fach.VO as unknown))
				newState.restErlaubtMusik = 3;
			if (!newState.markiereGeschichteBzwSozialwissenschaften(GostFachbereich.GESCHICHTE))
				continue;
			if (!newState.markiereGeschichteBzwSozialwissenschaften(GostFachbereich.SOZIALWISSENSCHAFTEN))
				continue;
			newStates.addAll(newState.markiereEinsprachlerNeuEinsetzendeFremdsprache());
		}
		return newStates;
	}

	/**
	 * Markierungsbaum - Ebene 5:
	 * Markiere die beiden besten Geschichts- bzw. Sozialwissenschaftskurse,
	 * wenn dieses nicht als bereits im Abiturbereich markiert wurde.
	 *
	 * @param fb   der Fachbereich Geschichte oder Sozialwissenschaften
	 *
	 * @return true, wenn der Markierungsvorgang erfolgreich war
	 */
	private markiereGeschichteBzwSozialwissenschaften(fb : GostFachbereich) : boolean {
		const strFB : string = (fb as unknown === GostFachbereich.GESCHICHTE as unknown) ? "Geschichte" : "Sozialwissenschaften";
		this.ergebnis.log.add(this.logIndent + "Markierung zwei Kursen in " + strFB + ":");
		const anzahlGE : number | null = this.anzahlBelegungen.get(fb);
		if ((anzahlGE !== null) && (anzahlGE > 0)) {
			this.ergebnis.log.add(this.logIndent + "  " + strFB + " wurde bereits im Abiturbereich markiert.");
			return true;
		}
		return this.markiereZweiBeste(this.manager.getRelevanteFachbelegungen(fb));
	}

	/**
	 * Markierungsbaum - Ebene 5:
	 * Markiere bei Einsprachlern die beiden Halbjahre in der Q2 einer neu einsetzenden Fremdsprache, falls
	 * diese Bedingungn nicht bereits zuvor erfüllt wurde.
	 * Führe für diese Knoten dabei jeweils die Berechnung aus der 6. Ebene aus.
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private markiereEinsprachlerNeuEinsetzendeFremdsprache() : List<GostAbiturMarkierungsalgorithmus> {
		this.ergebnis.log.add(this.logIndent + "Markierung einer neu einsetzenden Fremdsprache in der Q2 bei Einsprachlern...");
		const newStates : List<GostAbiturMarkierungsalgorithmus> = new ArrayList<GostAbiturMarkierungsalgorithmus>();
		const istEinsprachler : boolean = (SprachendatenUtils.getZweiteSpracheInSekI(this.manager.getSprachendaten()) === null);
		if (!istEinsprachler)
			this.ergebnis.log.add(this.logIndent + "  Es wurde in der Sek I mehr als eine Fremdsprache belegt.");
		const belegungen : List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		let hatEinsprachlerBereitsNeueinsetzendeFremdsprache : boolean = false;
		if (istEinsprachler) {
			for (const belegung of this.manager.getRelevanteFachbelegungen(GostFachbereich.FREMDSPRACHE)) {
				if (belegung.istFSNeu && this.manager.pruefeBelegung(belegung, ...GostHalbjahr.getQualifikationsphase())) {
					belegungen.add(belegung);
					if ((this.markiert.getOrNull(belegung.fachID, GostHalbjahr.Q21.id) !== null) && (this.markiert.getOrNull(belegung.fachID, GostHalbjahr.Q22.id) !== null))
						hatEinsprachlerBereitsNeueinsetzendeFremdsprache = true;
				}
			}
		}
		if (!istEinsprachler && hatEinsprachlerBereitsNeueinsetzendeFremdsprache)
			this.ergebnis.log.add(this.logIndent + "  Es ist bereits eine neu einsetzende Fremdsprache in der Q2 markiert.");
		if (!istEinsprachler || hatEinsprachlerBereitsNeueinsetzendeFremdsprache) {
			const newState : GostAbiturMarkierungsalgorithmus = new GostAbiturMarkierungsalgorithmus(this);
			newStates.addAll(newState.markiereReligionOderPhilosophieUndEineGesellschaftswissenschaft());
			return newStates;
		}
		if (belegungen.isEmpty()) {
			this.ergebnis.log.add(this.logIndent + "  Keine Fachbelegungen einer durchgängig belegten neu einsetzenden Fremdsprache in den zwei Halbjahren der Q2 vorhanden.");
			return newStates;
		}
		for (const belegung of belegungen) {
			const fach : GostFach | null = this.getFach(belegung);
			if (fach === null)
				continue;
			const newState : GostAbiturMarkierungsalgorithmus = new GostAbiturMarkierungsalgorithmus(this);
			this.ergebnis.log.add(this.logIndent + "  Fallunterscheidung: Markiere die beiden Kurse in der Q2 für das " + fach.kuerzelAnzeige + "...");
			if (!newState.markiereHalbjahresbelegung(belegung, GostHalbjahr.Q21))
				continue;
			if (!newState.markiereHalbjahresbelegung(belegung, GostHalbjahr.Q22))
				continue;
			newStates.addAll(newState.markiereReligionOderPhilosophieUndEineGesellschaftswissenschaft());
		}
		return newStates;
	}

	/**
	 * Markierungsbaum - Ebene 6:
	 * Markiere die beiden besten Religions- oder Philosophiekurse, sofern
	 * diese nicht bereits im Abiturbereich markiert wurden.
	 * Markiere außerdem eine Gesellschaftswissenschaft, sofern diese nicht bereits im Abitur markiert wurde
	 * TODO: Führe die Markierungen aus Ebene 7 aus
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private markiereReligionOderPhilosophieUndEineGesellschaftswissenschaft() : List<GostAbiturMarkierungsalgorithmus> {
		this.ergebnis.log.add(this.logIndent + "Markierung zwei Religions- oder Philosophiekurse (oder ggf. einer Gesellschaftswissenschaft als Ersatz):");
		const newStates : List<GostAbiturMarkierungsalgorithmus> = new ArrayList<GostAbiturMarkierungsalgorithmus>();
		const anzahlPL : number | null = this.anzahlBelegungen.get(GostFachbereich.PHILOSOPHIE);
		const hatAbiPL : boolean = (anzahlPL !== null) && (anzahlPL > 0);
		const anzahlRE : number | null = this.anzahlBelegungen.get(GostFachbereich.RELIGION);
		const hatAbiRE : boolean = (anzahlRE !== null) && (anzahlRE > 0);
		let hatReBelegungErfuellt : boolean = false;
		if (hatAbiRE) {
			this.ergebnis.log.add(this.logIndent + "  Es wurde bereits im Abiturbereich bereits ein Religionsfach markiert.");
			hatReBelegungErfuellt = true;
		} else {
			if (hatAbiPL)
				this.ergebnis.log.add(this.logIndent + "  Philosophie wurde im Abiturbereich gewählt und kann daher nicht als Ersatz für ein Religionsfach genutzt werden.");
			if (!hatAbiPL) {
				if (!this.markiereZweiBeste(this.manager.getRelevanteFachbelegungen(GostFachbereich.RELIGION, GostFachbereich.PHILOSOPHIE)))
					return newStates;
				hatReBelegungErfuellt = true;
			}
			if (hatAbiPL) {
				const belegungen : List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen(GostFachbereich.RELIGION);
				const hatReligionsbelegungen : boolean = this.manager.pruefeBelegungExistiert(belegungen, GostHalbjahr.Q11, GostHalbjahr.Q12);
				if (hatReligionsbelegungen) {
					if (!this.markiereZweiBeste(belegungen))
						return newStates;
					hatReBelegungErfuellt = true;
				}
			}
		}
		if (this.anzahlAbiGesellschaftswissenschaft > 0) {
			const newState : GostAbiturMarkierungsalgorithmus = new GostAbiturMarkierungsalgorithmus(this);
			if ((!hatReBelegungErfuellt) && (!hatAbiPL || (this.anzahlAbiGesellschaftswissenschaft === 1)))
				newState.markiereReligionOderErsatzAusGesellschaftswissenschaften();
			newStates.addAll(newState.markiereProjektkurs());
			return newStates;
		}
		const belegungen : List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		const bereitsGeprueft : JavaSet<string> = new HashSet<string>();
		for (const belegung of this.manager.getRelevanteFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH)) {
			const fach : GostFach | null = this.getFach(belegung);
			if (fach === null)
				continue;
			if (bereitsGeprueft.contains(fach.kuerzel))
				continue;
			const tmpBelegung : List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
			tmpBelegung.add(belegung);
			if (this.manager.pruefeBelegungExistiert(tmpBelegung, ...GostHalbjahr.getQualifikationsphase()))
				belegungen.add(belegung);
			bereitsGeprueft.add(fach.kuerzel);
		}
		if (belegungen.isEmpty()) {
			this.ergebnis.log.add(this.logIndent + "  Konnte keine durchgängig belegte Gesellschaftswissenschaft zur Markierung ermitteln.");
			return newStates;
		}
		for (const belegung of belegungen) {
			const fach : GostFach | null = this.getFach(belegung);
			if (fach === null)
				continue;
			const newState : GostAbiturMarkierungsalgorithmus = new GostAbiturMarkierungsalgorithmus(this);
			this.ergebnis.log.add(this.logIndent + "  Fallunterscheidung: Markiere die vier Kurse für die Gesellschaftswissenschaft " + fach.kuerzelAnzeige + "...");
			if (!newState.markiereBelegungDurchgaengig(belegung))
				continue;
			if (!hatReBelegungErfuellt)
				newState.markiereReligionOderErsatzAusGesellschaftswissenschaften();
			newStates.addAll(newState.markiereProjektkurs());
		}
		return newStates;
	}

	private markiereReligionOderErsatzAusGesellschaftswissenschaften() : boolean {
		this.ergebnis.log.add(this.logIndent + "    Ergänze Markierungen im Bereich Gesellschaftswissenschaften und Religion (Philosophie im Abitur und Relgion nicht in Q1).");
		const belegungen : List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION);
		if (belegungen.isEmpty()) {
			this.ergebnis.log.add(this.logIndent + "    Keine Fachbelegungen gefunden.");
			return false;
		}
		const resBelegung : Array<AbiturFachbelegung | null> | null = Array(2).fill(null);
		const resFach : Array<GostFach | null> | null = Array(2).fill(null);
		const resHalbjahre : Array<GostHalbjahr | null> | null = Array(2).fill(null);
		const resNotenpunkte : Array<number> | null = Array(2).fill(0);
		for (const belegung of belegungen) {
			const fach : GostFach | null = this.getFach(belegung);
			if (fach === null)
				continue;
			for (const hj of GostHalbjahr.getQualifikationsphase()) {
				if (this.markiert.getOrNull(belegung.fachID, hj.id) !== null)
					continue;
				if (!this.manager.pruefeBelegung(belegung, hj))
					continue;
				const np : number = this.getNotenpunkte(belegung, hj);
				if ((resBelegung[0] === null) || (np > resNotenpunkte[0])) {
					resBelegung[1] = resBelegung[0];
					resFach[1] = resFach[0];
					resHalbjahre[1] = resHalbjahre[0];
					resNotenpunkte[1] = resNotenpunkte[0];
					resBelegung[0] = belegung;
					resFach[0] = fach;
					resHalbjahre[0] = hj;
					resNotenpunkte[0] = np;
				} else
					if ((resBelegung[1] === null) || (np > resNotenpunkte[1])) {
						resBelegung[1] = belegung;
						resFach[1] = fach;
						resHalbjahre[1] = hj;
						resNotenpunkte[1] = np;
					}
			}
		}
		if ((resBelegung[0] === null) || (resBelegung[1] === null) || (resHalbjahre[0] === null) || (resHalbjahre[1] === null) || (resFach[0] === null) || (resFach[1] === null)) {
			this.ergebnis.log.add(this.logIndent + "  Fehler: Konnte keine Bewertung für zwei Halbjahre bestimmen.");
			return false;
		}
		this.ergebnis.log.add(this.logIndent + "  Markiere die beiden Kurse in " + resHalbjahre[0].kuerzel + " (" + resNotenpunkte[0] + " Punkte) für " + resFach[0].kuerzelAnzeige + " und " + resHalbjahre[1].kuerzel + " (" + resNotenpunkte[1] + " Punkte) für " + resFach[1].kuerzelAnzeige + "...");
		return this.markiereHalbjahresbelegung(resBelegung[0], resHalbjahre[0]) && this.markiereHalbjahresbelegung(resBelegung[1], resHalbjahre[1]);
	}

	/**
	 * Markierungsbaum - Ebene 7:
	 * Berücksichtige die beiden Fälle, dass ein Projektkurs markiert werden kann oder nicht,
	 * sofern einer belegt wurde.
	 * TODO: Führe die Markierungen aus Ebene 8 aus
	 *
	 * @return die resultierenden möglichen Zustände
	 */
	private markiereProjektkurs() : List<GostAbiturMarkierungsalgorithmus> {
		this.ergebnis.log.add(this.logIndent + "Markierung eines Projektkurses, sofern einer belegt wurde:");
		const newStates : List<GostAbiturMarkierungsalgorithmus> = new ArrayList<GostAbiturMarkierungsalgorithmus>();
		const projektkurs : AbiturFachbelegung | null = this.belegpruefungProjektkurse.getProjektkurs();
		const fach : GostFach | null = (projektkurs === null) ? null : this.getFach(projektkurs);
		if (fach !== null)
			this.ergebnis.log.add(this.logIndent + "  Es wurde ein Projektkurs belegt: " + fach.kuerzelAnzeige);
		const pjkHalbjahre : List<GostHalbjahr> = this.belegpruefungProjektkurse.getAnrechenbareHalbjahre();
		const pjkAnrechenbar : boolean = (projektkurs !== null) && (!this.manager.istProjektKursBesondereLernleistung()) && (pjkHalbjahre.size() === 2);
		if (pjkAnrechenbar)
			this.ergebnis.log.add(this.logIndent + "  und dieser ist anrechenbar.");
		const pjkIstBll : boolean = (projektkurs !== null) && (this.manager.istProjektKursBesondereLernleistung());
		if (pjkIstBll)
			this.ergebnis.log.add(this.logIndent + "  und dieser wird für eine besondere Lernleistung verwendet.");
		if (pjkAnrechenbar && (projektkurs !== null)) {
			this.ergebnis.log.add(this.logIndent + "  Fallunterscheidung: Markiere die beiden Projektkursbelegung...");
			const newState : GostAbiturMarkierungsalgorithmus = new GostAbiturMarkierungsalgorithmus(this);
			const hj1 : GostHalbjahr = pjkHalbjahre.get(0);
			const hj2 : GostHalbjahr = pjkHalbjahre.get(1);
			const bel1 : AbiturFachbelegungHalbjahr | null = projektkurs.belegungen[hj1.id];
			if (bel1 === null) {
				this.ergebnis.log.add(this.logIndent + "  Die Belegung im Halbjahr " + hj1.kuerzel + " ist fehlerhaft.");
				return newStates;
			}
			const bel2 : AbiturFachbelegungHalbjahr | null = projektkurs.belegungen[hj2.id];
			if (bel2 === null) {
				this.ergebnis.log.add(this.logIndent + "  Die Belegung im Halbjahr " + hj2.kuerzel + " ist fehlerhaft.");
				return newStates;
			}
			const n2 : Note | null = (bel2.notenkuerzel === null) ? null : Note.fromKuerzel(bel2.notenkuerzel);
			const hatNote2 : boolean = (n2 !== null) && n2.istNote(this.manager.getSchuljahr());
			if (!hatNote2) {
				this.ergebnis.log.add(this.logIndent + "  Die Belegungen in dem Halbjahr " + hj2.kuerzel + " hat keine Note gesetzt. Diese muss als Jahresabschluss die Jahresnote des Projektkurses beinhalten.");
				return newStates;
			}
			bel1.notenkuerzel = bel2.notenkuerzel;
			if (!newState.markiereHalbjahresbelegung(projektkurs, hj1) || !newState.markiereHalbjahresbelegung(projektkurs, hj2))
				return newStates;
			if (newState.markiereWeitereKurse())
				newStates.add(newState);
		}
		this.ergebnis.log.add(this.logIndent + "  " + (pjkAnrechenbar ? "Fallunterscheidung:" : "") + "Markiere keine Projektkursbelegung");
		const newState : GostAbiturMarkierungsalgorithmus = new GostAbiturMarkierungsalgorithmus(this);
		if (newState.markiereWeitereKurse())
			newStates.add(newState);
		return newStates;
	}

	/**
	 * Markierungsbaum - Ebene 7:
	 * Dieser Knoten stellt das Blatt im Baum dar.
	 * Markiert weitere Kurse, die noch nicht markiert sind und anrechenbar sind.
	 * - Dabei werden keine Projektkursbelegungen berücksichtigt, da sie auf einer höheren
	 * Ebene bereits beachtet wurden.
	 * - Bei Musik oder KU-MU-Ersatzfächern werden nur Kurse bis zur erlaubten Zahl berücksichtigt.
	 * - Bis 35 Kurse wird aufgefüllt
	 * - Danach nur, wenn es aufgrund der Defizitanzahl nötig ist oder den Durchschnitt verbessert
	 *
	 * @return true, wenn genügend Kurse markiert wurden und ansonsten false.
	 */
	private markiereWeitereKurse() : boolean {
		this.ergebnis.log.add(this.logIndent + "Markierung weiterer Kurse:");
		const auswahlliste : List<GostAbiturMarkierungsalgorithmusBelegung> = new ArrayList<GostAbiturMarkierungsalgorithmusBelegung>();
		for (const belegung of this.manager.daten().fachbelegungen) {
			const fach : GostFach | null = this.getFach(belegung);
			if ((fach === null) || (JavaObject.equalsTranspiler("PX", (fach.kuerzel))))
				continue;
			for (const hj of GostHalbjahr.getQualifikationsphase()) {
				if (this.markiert.contains(belegung.fachID, hj.id))
					continue;
				if (!this.manager.pruefeBelegung(belegung, hj))
					continue;
				const belHj : AbiturFachbelegungHalbjahr | null = belegung.belegungen[hj.id];
				if (belHj === null)
					continue;
				const note : Note | null = (belHj.notenkuerzel === null) ? null : Note.fromKuerzel(belHj.notenkuerzel);
				if ((note === null) || !note.istNote(this.manager.getSchuljahr()))
					continue;
				const nke : NoteKatalogEintrag | null = note.daten(this.manager.getSchuljahr());
				if ((nke === null) || (nke.notenpunkte === null))
					continue;
				auswahlliste.add(new GostAbiturMarkierungsalgorithmusBelegung(belegung, belHj, nke.notenpunkte));
			}
		}
		auswahlliste.sort(this.comparatorBelegungen);
		while ((this.summeKurse < 40) && !auswahlliste.isEmpty()) {
			const eintrag : GostAbiturMarkierungsalgorithmusBelegung = auswahlliste.removeFirst();
			const fach : GostFach | null = this.getFach(eintrag.belegung);
			if (fach === null)
				continue;
			const f : Fach = Fach.getBySchluesselOrDefault(fach.kuerzel);
			if ((((f as unknown === Fach.IN as unknown) || (f as unknown === Fach.VO as unknown)) && ((this.restErlaubtVPIP === 0) || (this.restErlaubtMusik === 0) || (this.restErlaubtKUMUErsatz === 0))) || ((f as unknown === Fach.LI as unknown) && (this.restErlaubtKUMUErsatz === 0)) || ((f as unknown === Fach.MU as unknown) && (this.restErlaubtMusik === 0)))
				continue;
			const durchschnittNotepunkte : number = this.summeNotenpunkte / (this.summeKurse + 8.0);
			const defizite : number = this.defiziteLK + this.defiziteGK;
			if ((((this.summeKurse >= 35) && (this.summeKurse <= 37) && (defizite <= 7)) || ((this.summeKurse >= 38) && (defizite <= 8))) && (eintrag.notenpunkte <= durchschnittNotepunkte))
				break;
			if (!this.markiereHalbjahresbelegung(eintrag.belegung, eintrag.halbjahr)) {
				this.ergebnis.log.add(this.logIndent + "  Markierung unerwartet fehlgeschlagen, breche Algorithmus ab.");
				return false;
			}
			if ((f as unknown === Fach.IN as unknown) || (f as unknown === Fach.VO as unknown)) {
				this.restErlaubtVPIP--;
				this.restErlaubtMusik--;
				this.restErlaubtKUMUErsatz--;
			} else
				if (f as unknown === Fach.LI as unknown) {
					this.restErlaubtKUMUErsatz--;
				} else
					if (f as unknown === Fach.MU as unknown) {
						this.restErlaubtMusik--;
					}
		}
		if (this.summeKurse < 35) {
			this.ergebnis.log.add(this.logIndent + "  Es wurden zu wenig Kurse markiert.");
			return false;
		}
		const defizite : number = this.defiziteLK + this.defiziteGK;
		if (defizite > 8) {
			this.ergebnis.log.add(this.logIndent + "  Es liegen mehr als 8 Defizite vor.");
			return false;
		}
		if ((defizite > 7) && (this.summeKurse < 38)) {
			this.ergebnis.log.add(this.logIndent + "  Es liegen mehr als 7 Defizite bei wenige als 38 Kursen vor.");
			return false;
		}
		this.summeNormiert = Math.round((40.0 * this.summeNotenpunkte) / (this.summeKurse + 8.0)) as number;
		this.hatZulassung = (this.summeNormiert >= 200) && (((this.summeKurse >= 35) && (this.summeKurse <= 37) && (defizite <= 7)) || ((this.summeKurse >= 38) && (defizite <= 8)));
		this.ergebnis.erfolgreich = true;
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmus';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmus'].includes(name);
	}

	public static class = new Class<GostAbiturMarkierungsalgorithmus>('de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungsalgorithmus');

}

export function cast_de_svws_nrw_core_abschluss_gost_GostAbiturMarkierungsalgorithmus(obj : unknown) : GostAbiturMarkierungsalgorithmus {
	return obj as GostAbiturMarkierungsalgorithmus;
}
