import { JavaObject } from '../../../java/lang/JavaObject';
import { GostFach } from '../../../core/data/gost/GostFach';
import { GostAbiturFach } from '../../../core/types/gost/GostAbiturFach';
import { AbiturFachbelegung } from '../../../core/data/gost/AbiturFachbelegung';
import { GostFachUtils } from '../../../core/utils/gost/GostFachUtils';
import { JavaString } from '../../../java/lang/JavaString';
import { AbiturdatenManager } from '../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegpruefung } from '../../../core/abschluss/gost/GostBelegpruefung';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { Projektkurse, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Projektkurse } from '../../../core/abschluss/gost/belegpruefung/Projektkurse';
import { SprachendatenUtils } from '../../../core/utils/schueler/SprachendatenUtils';
import { GostFachbereich } from '../../../core/types/gost/GostFachbereich';
import { GostAbiturMarkierungspruefungErgebnis } from '../../../core/abschluss/gost/GostAbiturMarkierungspruefungErgebnis';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { AbiFaecher, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_AbiFaecher } from '../../../core/abschluss/gost/belegpruefung/AbiFaecher';

export class GostAbiturMarkierungspruefung extends JavaObject {

	/**
	 * Das Ergebnis der Prüfung
	 */
	private readonly ergebnis : GostAbiturMarkierungspruefungErgebnis = new GostAbiturMarkierungspruefungErgebnis();

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
	 * Die Belegung einer (ersten) vollständig markierten Fremdsprache (keine Bili-Sachfach!)
	 */
	fremdsprache : AbiturFachbelegung | null = null;

	/**
	 * Gibt an, ob eine weitere Fremdsprache neben der ersten gefunden wurde (Bili-Sachfach ist möglich)
	 */
	hatWeitereFremdsprache : boolean = false;


	/**
	 * Erstellt eine neue Instanz des Markierungsalgorithmus unter Verwendung des übergebenen Abiturdaten-Manager und den zuvor
	 * durchgeführten Belegprüfungen.
	 *
	 * @param manager            der Abiturdaten-Manager
	 * @param belegpruefungen    die durchgeführten Belegprüfungen
	 */
	private constructor(manager : AbiturdatenManager, belegpruefungen : List<GostBelegpruefung>) {
		super();
		this.manager = manager;
		this.logIndent = "";
		let tmpBelegpruefungProjektkurse : Projektkurse | null = null;
		let tmpBelegpruefungAbiturfaecher : AbiFaecher | null = null;
		for (const pruefung of belegpruefungen) {
			if (((pruefung instanceof JavaObject) && (pruefung.isTranspiledInstanceOf('de.svws_nrw.core.abschluss.gost.belegpruefung.Projektkurse'))))
				tmpBelegpruefungProjektkurse = cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Projektkurse(pruefung);
			if (((pruefung instanceof JavaObject) && (pruefung.isTranspiledInstanceOf('de.svws_nrw.core.abschluss.gost.belegpruefung.AbiFaecher'))))
				tmpBelegpruefungAbiturfaecher = cast_de_svws_nrw_core_abschluss_gost_belegpruefung_AbiFaecher(pruefung);
		}
		if (tmpBelegpruefungProjektkurse === null)
			throw new DeveloperNotificationException("Die Projektkursprüfung muss als Belegprüfung vorhanden sein.")
		this.belegpruefungProjektkurse = tmpBelegpruefungProjektkurse;
		if (tmpBelegpruefungAbiturfaecher === null)
			throw new DeveloperNotificationException("Die Abiturfächerprüfung muss als Belegprüfung vorhanden sein.")
		this.belegpruefungAbiturfaecher = tmpBelegpruefungAbiturfaecher;
		this.abi[0] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.LK1);
		this.abi[1] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.LK2);
		this.abi[2] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.AB3);
		this.abi[3] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.AB4);
	}

	/**
	 * Führt eine Prüfung der Markierung von Halbjahresbelegungen zur Verwendung in Block II
	 * von anrechenbaren Kursen für die Abiturberechnung durch. Vorraussetzung hierfür ist, dass
	 * alle anrechenbare Kurse ein gültige Note zugeordnet haben.
	 *
	 * @param manager           der Manager zur Verwaltung der Abiturdaten.
	 * @param belegpruefungen   die zuvor durchgeführten Belegprüfung der Laufbahnplanung
	 *
	 * @return das Ergebnis der Prüfung
	 */
	public static pruefe(manager : AbiturdatenManager, belegpruefungen : List<GostBelegpruefung>) : GostAbiturMarkierungspruefungErgebnis {
		const pruefung : GostAbiturMarkierungspruefung = new GostAbiturMarkierungspruefung(manager, belegpruefungen);
		pruefung.ergebnis.erfolgreich = pruefung.pruefung();
		return pruefung.ergebnis;
	}

	/**
	 * Führt die Prüfung der aktuellen Markierung der Abiturdaten des Abiturdaten-Manager durch.
	 *
	 * @return true, falls die Prüfung erfolgreich war, und ansonsten false
	 */
	private pruefung() : boolean {
		this.fremdsprache = null;
		let success : boolean = this.pruefeLeistungskursDefizite();
		success = success && this.pruefeAbiturfachNullPunkte();
		success = success && this.pruefeAbiturfachMarkierung();
		success = success && this.pruefeDeutschMarkierung();
		success = success && this.pruefeFremdsprachenMarkierung();
		if (!success)
			return success;
		this.pruefeAufWeitereFremdsprache();
		success = this.pruefeNeuEinsetzendeFremdsprache();
		success = success && this.pruefeKunstMusikOderErsatzMarkierung();
		success = success && this.pruefeKunstMusikErsatzAnzahlMarkierung();
		success = success && this.pruefeMusikAnzahlMarkierung();
		success = success && this.pruefeAnzahlMarkierungen(GostFachbereich.GESCHICHTE, 2, "Es müssen mindestens zwei Kurse in Geschichte markiert werden.");
		success = success && this.pruefeAnzahlMarkierungen(GostFachbereich.SOZIALWISSENSCHAFTEN, 2, "Es müssen mindestens zwei Kurse in Sozialwissenschaften markiert werden.");
		success = success && this.pruefeExistiertMitAnzahlMarkierungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH, 4, "Es muss ein gesellschaftswissenschaftliches Fach durchgängig markiert sein.");
		if (!success)
			return success;
		success = false;
		return success;
	}

	private pruefeLeistungskursDefizite() : boolean {
		if ((this.abi[0] === null) || (this.abi[1] === null)) {
			this.ergebnis.log.add("Es müssen zwei Leistungskurse als Abiturfächer gewählt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		let defiziteLK : number = 0;
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			if ((this.abi[0].belegungen[halbjahr.id] === null) || (this.abi[1].belegungen[halbjahr.id] === null)) {
				this.ergebnis.log.add(JavaString.format("Beide Leistungskurse müssen im Halbjahr %s belegt sein, damit eine Abiturzulassung möglich ist.", halbjahr.kuerzel));
				return false;
			}
			const np1 : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[0].belegungen[halbjahr.id]);
			const np2 : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[1].belegungen[halbjahr.id]);
			if ((np1 === null) || (np2 === null)) {
				this.ergebnis.log.add(JavaString.format("Beide Leistungskurse müssen im Halbjahr %s bewertet sein, damit eine Abiturzulassung möglich ist.", halbjahr.kuerzel));
				return false;
			}
			if (np1 < 5)
				defiziteLK++;
			if (np2 < 5)
				defiziteLK++;
		}
		if (defiziteLK > 3) {
			this.ergebnis.log.add("Es liegen mehr als drei Leistungskursdefizite vor. Keine Zulassung zum Abitur.");
			return false;
		}
		return true;
	}

	private pruefeAbiturfachNullPunkte() : boolean {
		if ((this.abi[0] === null) || (this.abi[1] === null) || (this.abi[2] === null) || (this.abi[3] === null)) {
			this.ergebnis.log.add("Es müssen vier Abiturfächer gewählt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			if ((this.abi[0].belegungen[halbjahr.id] === null) || (this.abi[1].belegungen[halbjahr.id] === null) || (this.abi[2].belegungen[halbjahr.id] === null) || (this.abi[3].belegungen[halbjahr.id] === null)) {
				this.ergebnis.log.add(JavaString.format("Alle Abiturfächer müssen im Halbjahr %s belegt sein, damit eine Abiturzulassung möglich ist.", halbjahr.kuerzel));
				return false;
			}
			const np1 : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[0].belegungen[halbjahr.id]);
			const np2 : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[1].belegungen[halbjahr.id]);
			const np3 : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[2].belegungen[halbjahr.id]);
			const np4 : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[3].belegungen[halbjahr.id]);
			if ((np1 === null) || (np2 === null) || (np3 === null) || (np4 === null)) {
				this.ergebnis.log.add(JavaString.format("Alle Abiturfächer müssen im Halbjahr %s bewertet sein, damit eine Abiturzulassung möglich ist.", halbjahr.kuerzel));
				return false;
			}
			if ((np1 === 0) || (np2 === 0) || (np3 === 0) || (np4 === 0)) {
				this.ergebnis.log.add("Abiturfächer mit 0 Notenpunkten gelten als nicht belegt. Keine Zulassung zum Abitur.");
				return false;
			}
		}
		return true;
	}

	private pruefeAbiturfachMarkierung() : boolean {
		if ((this.abi[0] === null) || (this.abi[1] === null) || (this.abi[2] === null) || (this.abi[3] === null)) {
			this.ergebnis.log.add("Es müssen vier Abiturfächer gewählt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (let i : number = 1; i < 5; i++) {
			if (!this.manager.hatMarkierungQualifikationsphase(this.abi[i - 1])) {
				this.ergebnis.log.add(JavaString.format("Es müssen alle Abiturfächer durchgehend markiert sein. Dies ist mindestens bei dem %d. Abiturfach nicht der Fall.", i));
				return false;
			}
		}
		return true;
	}

	private pruefeDeutschMarkierung() : boolean {
		const belegung : AbiturFachbelegung | null = this.manager.getFachbelegung(GostFachbereich.DEUTSCH);
		if (belegung === null) {
			this.ergebnis.log.add("Deutsch muss belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			if (belegung.belegungen[halbjahr.id] === null) {
				this.ergebnis.log.add(JavaString.format("Deutsch mussen im Halbjahr %s belegt sein, damit eine Abiturzulassung möglich ist.", halbjahr.kuerzel));
				return false;
			}
			const np : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
			if (np === null) {
				this.ergebnis.log.add(JavaString.format("Deutsch muss im Halbjahr %s bewertet sein, damit eine Abiturzulassung möglich ist.", halbjahr.kuerzel));
				return false;
			}
			if (np === 0) {
				this.ergebnis.log.add(JavaString.format("Deutsch wurde im Halbjahr %s mit 0 Notenpunkten bewertet und gilt damit als nicht belegt. Eine Abiturzulassung ist nicht möglich.", halbjahr.kuerzel));
				return false;
			}
			if (!this.manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
				this.ergebnis.log.add("Deutsch muss durchgehend markiert sein.");
				return false;
			}
		}
		return true;
	}

	private pruefeFremdsprachenMarkierung() : boolean {
		const belegungen : List<AbiturFachbelegung> = this.manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE);
		if (belegungen.isEmpty()) {
			this.ergebnis.log.add("Es muss mindestens eine Fremdsprache belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (const belegung of belegungen) {
			let found : boolean = true;
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				if (belegung.belegungen[halbjahr.id] === null) {
					found = false;
					break;
				}
				const np : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np === null) || (np === 0)) {
					found = false;
					break;
				}
				if (!this.manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
					found = false;
					break;
				}
			}
			if (found) {
				this.fremdsprache = belegung;
				return true;
			}
		}
		this.ergebnis.log.add("Es muss mindestens eine Fremdsprache durchgängig markiert sein, damit eine Abiturzulassung möglich ist.");
		return false;
	}

	private pruefeAufWeitereFremdsprache() : void {
		this.hatWeitereFremdsprache = false;
		let belegungen : List<AbiturFachbelegung> = this.manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE);
		for (const belegung of belegungen) {
			if (belegung as unknown === this.fremdsprache as unknown)
				continue;
			let found : boolean = true;
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				if (belegung.belegungen[halbjahr.id] === null) {
					found = false;
					break;
				}
				const np : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np === null) || (np === 0)) {
					found = false;
					break;
				}
				if (!this.manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
					found = false;
					break;
				}
			}
			if (found) {
				this.hatWeitereFremdsprache = true;
				return;
			}
		}
		belegungen = this.manager.getFachbelegungenBilingual();
		if (!belegungen.isEmpty()) {
			const tmpFach : GostFach | null = this.manager.getFach(this.fremdsprache);
			const fs : string | null = (tmpFach === null) ? "" : GostFachUtils.getFremdsprache(tmpFach);
			for (const belegung of belegungen) {
				const sachfach : GostFach | null = this.manager.getFach(this.fremdsprache);
				if ((sachfach === null) || (sachfach.biliSprache === null) || (JavaString.isBlank(sachfach.biliSprache)) || (JavaObject.equalsTranspiler(sachfach.biliSprache, (fs))))
					continue;
				let found : boolean = true;
				for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
					if (belegung.belegungen[halbjahr.id] === null) {
						found = false;
						break;
					}
					const np : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
					if ((np === null) || (np === 0)) {
						found = false;
						break;
					}
					if (!this.manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
						found = false;
						break;
					}
				}
				if (found) {
					this.hatWeitereFremdsprache = true;
					return;
				}
			}
		}
	}

	private pruefeNeuEinsetzendeFremdsprache() : boolean {
		const fs2 : string | null = SprachendatenUtils.getZweiteSpracheInSekI(this.manager.getSprachendaten());
		if (fs2 !== null)
			return true;
		const belegungen : List<AbiturFachbelegung> = this.manager.filterFremdspracheNeuEinsetzend(this.manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE));
		for (const belegung of belegungen) {
			if (belegung as unknown === this.fremdsprache as unknown)
				continue;
			let found : boolean = true;
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				if (belegung.belegungen[halbjahr.id] === null) {
					found = false;
					break;
				}
				const np : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np === null) || (np === 0)) {
					found = false;
					break;
				}
				if (((halbjahr as unknown === GostHalbjahr.Q21 as unknown) || (halbjahr as unknown === GostHalbjahr.Q22 as unknown)) && (!this.manager.hatMarkierungHalbjahr(belegung, halbjahr))) {
					found = false;
					break;
				}
			}
			if (found)
				return true;
		}
		this.ergebnis.log.add("Es muss eine neu einsetzende Fremdsprache in der Q2.1 und Q2.2 markiert sein, damit eine Abiturzulassung möglich ist.");
		return false;
	}

	private pruefeKunstMusikOderErsatzMarkierung() : boolean {
		const belegungen : List<AbiturFachbelegung> = this.manager.getFachbelegungen(GostFachbereich.LITERARISCH_KUENSTLERISCH);
		if (belegungen.isEmpty()) {
			this.ergebnis.log.add("Es muss mindestens Kunst, Musik oder ein Ersatzfach belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (const belegung of belegungen) {
			let kurseMarkiert : number = 0;
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				if (belegung.belegungen[halbjahr.id] === null)
					continue;
				const np : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np === null) || (np === 0))
					continue;
				if (this.manager.hatMarkierungHalbjahr(belegung, halbjahr))
					kurseMarkiert++;
			}
			if (kurseMarkiert >= 2)
				return true;
		}
		this.ergebnis.log.add("Es müssen mindestens 2 Kurse in Kunst, Musik oder einem Ersatzfach markiert sein, damit eine Abiturzulassung möglich ist.");
		return false;
	}

	private pruefeKunstMusikErsatzAnzahlMarkierung() : boolean {
		const belegungen : List<AbiturFachbelegung> = this.manager.getFachbelegungen(GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ);
		let faecherMarkiert : number = 0;
		for (const belegung of belegungen) {
			let kurseMarkiert : number = 0;
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				if (belegung.belegungen[halbjahr.id] === null)
					continue;
				const np : number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np === null) || (np === 0))
					continue;
				if (this.manager.hatMarkierungHalbjahr(belegung, halbjahr))
					kurseMarkiert++;
			}
			if (kurseMarkiert > 0)
				faecherMarkiert++;
			if (kurseMarkiert > 2) {
				this.ergebnis.log.add("Es dürfen maximal 2 Kurse in einem Ersatzfach markiert sein.");
				return false;
			}
		}
		if (faecherMarkiert > 2) {
			this.ergebnis.log.add("Es dürfen nur Kurse in einem Ersatzfach markiert sein.");
			return false;
		}
		return true;
	}

	private pruefeMusikAnzahlMarkierung() : boolean {
		const belMU : AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel("MU");
		const belVP : AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel("VP");
		const belIN : AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel("IN");
		const istAbiLK : boolean = (this.abi[0] as unknown === belMU as unknown) || (this.abi[1] as unknown === belMU as unknown);
		const istAbiGK : boolean = (this.abi[2] as unknown === belMU as unknown) || (this.abi[3] as unknown === belMU as unknown);
		const anzahl : number = this.manager.zaehleMarkierungenQualifikationsphase(belMU) + this.manager.zaehleMarkierungenQualifikationsphase(belVP) + this.manager.zaehleMarkierungenQualifikationsphase(belIN);
		if (istAbiLK) {
			if (anzahl > 4) {
				this.ergebnis.log.add("Wenn Musik als Leistungskurs belegt ist, dann dürfen keine weiteren Kurse in MU,VP und IN markiert werden.");
				return false;
			}
			return true;
		}
		if (istAbiGK) {
			if (anzahl > 6) {
				this.ergebnis.log.add("Wenn Musik als Grundkurs im Abitur belegt ist, dann dürfen maximal sechs Kurse in MU,VP und IN markiert werden.");
				return false;
			}
			return true;
		}
		if (anzahl > 6) {
			this.ergebnis.log.add("Es dürfen maximal fünf Kurse in MU,VP und IN markiert werden.");
			return false;
		}
		return true;
	}

	private pruefeAnzahlMarkierungen(fb : GostFachbereich, min : number, fehler : string) : boolean {
		const belegungen : List<AbiturFachbelegung> = this.manager.getFachbelegungen(fb);
		let anzahl : number = 0;
		for (const belegung of belegungen)
			anzahl += this.manager.zaehleMarkierungenQualifikationsphase(belegung);
		if (anzahl < min) {
			this.ergebnis.log.add(fehler);
			return false;
		}
		return true;
	}

	private pruefeExistiertMitAnzahlMarkierungen(fb : GostFachbereich, min : number, fehler : string) : boolean {
		const belegungen : List<AbiturFachbelegung> = this.manager.getFachbelegungen(fb);
		for (const belegung of belegungen)
			if (this.manager.zaehleMarkierungenQualifikationsphase(belegung) >= min)
				return true;
		this.ergebnis.log.add(fehler);
		return false;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungspruefung';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungspruefung'].includes(name);
	}

	public static class = new Class<GostAbiturMarkierungspruefung>('de.svws_nrw.core.abschluss.gost.GostAbiturMarkierungspruefung');

}

export function cast_de_svws_nrw_core_abschluss_gost_GostAbiturMarkierungspruefung(obj : unknown) : GostAbiturMarkierungspruefung {
	return obj as GostAbiturMarkierungspruefung;
}
