import { JavaObject } from '../../../java/lang/JavaObject';
import { GostFach } from '../../../core/data/gost/GostFach';
import { Abi30BelegpruefungProjektkurse } from '../../../core/abschluss/gost/belegpruefung/abi2030/Abi30BelegpruefungProjektkurse';
import { GostAbiturFach } from '../../../core/types/gost/GostAbiturFach';
import { AbiturFachbelegung } from '../../../core/data/gost/AbiturFachbelegung';
import { GostFachUtils } from '../../../core/utils/gost/GostFachUtils';
import { ArrayList } from '../../../java/util/ArrayList';
import { GostBesondereLernleistung } from '../../../core/types/gost/GostBesondereLernleistung';
import { AbiturFachbelegungHalbjahr } from '../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { JavaString } from '../../../java/lang/JavaString';
import { AbiturdatenManager } from '../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegpruefung } from '../../../core/abschluss/gost/GostBelegpruefung';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { GostKursart } from '../../../core/types/gost/GostKursart';
import { SprachendatenUtils } from '../../../core/utils/schueler/SprachendatenUtils';
import { Abi30BelegpruefungAbiFaecher } from '../../../core/abschluss/gost/belegpruefung/abi2030/Abi30BelegpruefungAbiFaecher';
import { GostAbiturMarkierungsalgorithmusBelegung } from '../../../core/abschluss/gost/GostAbiturMarkierungsalgorithmusBelegung';
import type { Comparator } from '../../../java/util/Comparator';
import { GostFachbereich } from '../../../core/types/gost/GostFachbereich';
import { GostAbiturMarkierungspruefungErgebnis } from '../../../core/abschluss/gost/GostAbiturMarkierungspruefungErgebnis';
import { GostHalbjahr } from '../../../core/types/gost/GostHalbjahr';
import { GostSchriftlichkeit } from '../../../core/types/gost/GostSchriftlichkeit';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class Abi30GostAbiturMarkierungspruefung extends JavaObject {

	/**
	 * Das Ergebnis der Prüfung
	 */
	private readonly ergebnis: GostAbiturMarkierungspruefungErgebnis = new GostAbiturMarkierungspruefungErgebnis();

	/**
	 * Die aktuelle Einrückung für das Logging
	 */
	private logIndent: string = "";

	/**
	 * Der Abiturdaten-Manager
	 */
	private readonly manager: AbiturdatenManager;

	/**
	 * Die zuvor durchgeführten Belegprüfung zu dem Projektkurs
	 */
	private readonly belegpruefungProjektkurse: Abi30BelegpruefungProjektkurse;

	/**
	 * Die zuvor durchgeführten Belegprüfung zu den Abiturfächern
	 */
	private readonly belegpruefungAbiturfaecher: Abi30BelegpruefungAbiFaecher;

	/**
	 * Die Belegungen der vier Abiturfächer
	 */
	readonly abi: Array<AbiturFachbelegung | null> = Array(5).fill(null);

	/**
	 * Die Belegung einer (ersten) vollständig markierten Fremdsprache (keine Bili-Sachfach!)
	 */
	fremdsprache: AbiturFachbelegung | null = null;

	/**
	 * Gibt an, ob eine weitere Fremdsprache neben der ersten gefunden wurde (Bili-Sachfach ist möglich)
	 */
	hatWeitereFremdsprache: boolean = false;

	/**
	 * Die Belegung einer vollständig markierten klassischen Naturwissenschaft
	 */
	naturwissenschaft: AbiturFachbelegung | null = null;

	/**
	 * Gibt an, ob eine weitere Naturwissenschaft neben der ersten gefunden wurde
	 */
	hatWeitereNaturwissenschaft: boolean = false;


	/**
	 * Erstellt eine neue Instanz des Markierungsalgorithmus unter Verwendung des übergebenen Abiturdaten-Manager und den zuvor
	 * durchgeführten Belegprüfungen.
	 *
	 * @param manager            der Abiturdaten-Manager
	 * @param belegpruefungen    die durchgeführten Belegprüfungen
	 */
	private constructor(manager: AbiturdatenManager, belegpruefungen: List<GostBelegpruefung>) {
		super();
		this.manager = manager;
		this.logIndent = "";
		let tmpBelegpruefungProjektkurse: Abi30BelegpruefungProjektkurse | null = null;
		let tmpBelegpruefungAbiturfaecher: Abi30BelegpruefungAbiFaecher | null = null;
		for (const pruefung of belegpruefungen) {
			if (((pruefung instanceof JavaObject) && (pruefung.isTranspiledInstanceOf('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungProjektkurse'))))
				tmpBelegpruefungProjektkurse = (pruefung as unknown as Abi30BelegpruefungProjektkurse);
			if (((pruefung instanceof JavaObject) && (pruefung.isTranspiledInstanceOf('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungAbiFaecher'))))
				tmpBelegpruefungAbiturfaecher = (pruefung as unknown as Abi30BelegpruefungAbiFaecher);
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
		this.abi[4] = this.belegpruefungAbiturfaecher.getAbiturfach(GostAbiturFach.AB5);
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
	public static pruefe(manager: AbiturdatenManager, belegpruefungen: List<GostBelegpruefung>): GostAbiturMarkierungspruefungErgebnis {
		const pruefung: Abi30GostAbiturMarkierungspruefung = new Abi30GostAbiturMarkierungspruefung(manager, belegpruefungen);
		pruefung.ergebnis.erfolgreich = pruefung.pruefung();
		return pruefung.ergebnis;
	}

	/**
	 * Führt die Prüfung der aktuellen Markierung der Abiturdaten des Abiturdaten-Manager durch.
	 *
	 * @return true, falls die Prüfung erfolgreich war, und ansonsten false
	 */
	private pruefung(): boolean {
		this.fremdsprache = null;
		let success: boolean = this.pruefeLeistungskursDefizite();
		success = success && this.pruefeAbiturfachNullPunkte();
		success = success && this.pruefeAbiturfachMarkierung();
		success = success && this.pruefeDeutschMarkierung();
		success = success && this.pruefeFremdsprachenMarkierung();
		if (!success)
			return success;
		this.pruefeAufWeitereFremdsprache();
		success = this.pruefeNeuEinsetzendeFremdsprache();
		success = success && this.pruefeKunstMusikOderLiteraturMarkierung();
		success = success && this.pruefeLiteraturAnzahlMarkierung();
		success = success && this.pruefeMusikMarkierung();
		success = success && this.pruefeAnzahlMarkierungen(GostFachbereich.GESCHICHTE, 2, "Es müssen mindestens zwei Kurse in Geschichte markiert werden.");
		success = success && this.pruefeAnzahlMarkierungen(GostFachbereich.SOZIALWISSENSCHAFTEN, 2, "Es müssen mindestens zwei Kurse in Sozialwissenschaften markiert werden.");
		success = success && this.pruefeGesellschaftswissenschaftMarkierung();
		success = success && this.pruefeReligionsOderErsatzMarkierungen();
		success = success && this.pruefeAnzahlMarkierungen(GostFachbereich.MATHEMATIK, 4, "Mathematik muss durchgehend markiert sein.");
		success = success && this.pruefeNaturwissenschaftMarkierung();
		if (!success)
			return success;
		this.pruefeAufWeitereNaturwissenschaft();
		success = this.pruefeSchwerpunkt();
		success = success && this.pruefeProjektkurs();
		success = success && this.pruefeAnzahlUndDefizite();
		if (!success)
			return success;
		return this.pruefeOptimierung();
	}

	private pruefeLeistungskursDefizite(): boolean {
		if ((this.abi[0] === null) || (this.abi[1] === null)) {
			this.ergebnis.log.add("Es müssen zwei Leistungskurse als Abiturfächer gewählt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		let defiziteLK: number = 0;
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			if ((this.abi[0].belegungen[halbjahr.id] === null) || (this.abi[1].belegungen[halbjahr.id] === null)) {
				this.ergebnis.log.add(JavaString.format("Beide Leistungskurse müssen im Halbjahr %s belegt sein, damit eine Abiturzulassung möglich ist.", halbjahr.kuerzel));
				return false;
			}
			const np1: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[0].belegungen[halbjahr.id]);
			const np2: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[1].belegungen[halbjahr.id]);
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

	private pruefeAbiturfachNullPunkte(): boolean {
		if ((this.abi[0] === null) || (this.abi[1] === null) || (this.abi[2] === null) || (this.abi[3] === null) || (this.abi[4] === null)) {
			this.ergebnis.log.add("Es müssen fünf Abiturfächer gewählt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		const istAbiPJK: boolean = JavaObject.equalsTranspiler(GostKursart.PJK.kuerzel, (this.abi[4].letzteKursart));
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			if ((this.abi[0].belegungen[halbjahr.id] === null) || (this.abi[1].belegungen[halbjahr.id] === null) || (this.abi[2].belegungen[halbjahr.id] === null) || (this.abi[3].belegungen[halbjahr.id] === null) || (!istAbiPJK && (this.abi[4].belegungen[halbjahr.id] === null)) || (istAbiPJK && ((halbjahr as unknown === GostHalbjahr.Q21 as unknown) || (halbjahr as unknown === GostHalbjahr.Q22 as unknown)) && (this.abi[4].belegungen[halbjahr.id] === null))) {
				this.ergebnis.log.add(JavaString.format("Alle Abiturfächer müssen im Halbjahr %s belegt sein, damit eine Abiturzulassung möglich ist.", halbjahr.kuerzel));
				return false;
			}
			const np1: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[0].belegungen[halbjahr.id]);
			const np2: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[1].belegungen[halbjahr.id]);
			const np3: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[2].belegungen[halbjahr.id]);
			const np4: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[3].belegungen[halbjahr.id]);
			const np5: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(this.abi[4].belegungen[halbjahr.id]);
			if (istAbiPJK && halbjahr.istIn(GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12))
				continue;
			if ((np1 === null) || (np2 === null) || (np3 === null) || (np4 === null) || (np5 === null)) {
				this.ergebnis.log.add(JavaString.format("Alle Abiturfächer müssen im Halbjahr %s bewertet sein, damit eine Abiturzulassung möglich ist.", halbjahr.kuerzel));
				return false;
			}
			if ((np1 === 0) || (np2 === 0) || (np3 === 0) || (np4 === 0) || (np5 === 0)) {
				this.ergebnis.log.add("Abiturfächer mit 0 Notenpunkten gelten als nicht belegt. Keine Zulassung zum Abitur.");
				return false;
			}
		}
		return true;
	}

	private pruefeAbiturfachMarkierung(): boolean {
		if ((this.abi[0] === null) || (this.abi[1] === null) || (this.abi[2] === null) || (this.abi[3] === null) || (this.abi[4] === null)) {
			this.ergebnis.log.add("Es müssen vier Abiturfächer gewählt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (let i: number = 1; i < 6; i++) {
			if ((i === 5) && JavaObject.equalsTranspiler(GostKursart.PJK.kuerzel, (this.abi[4].letzteKursart))) {
				if (this.manager.hatMarkierungHalbjahr(this.abi[i - 1], GostHalbjahr.Q21) && this.manager.hatMarkierungHalbjahr(this.abi[i - 1], GostHalbjahr.Q22))
					continue;
				this.ergebnis.log.add("Ein Projektkursfach als 5. Abiturfach muss in der Q2 markiert sein.");
				return false;
			}
			if (!this.manager.hatMarkierungQualifikationsphase(this.abi[i - 1])) {
				this.ergebnis.log.add(JavaString.format("Es müssen alle Abiturfächer durchgehend markiert sein. Dies ist mindestens bei dem %d. Abiturfach nicht der Fall.", i));
				return false;
			}
		}
		return true;
	}

	private pruefeDeutschMarkierung(): boolean {
		const belegung: AbiturFachbelegung | null = this.manager.getFachbelegung(GostFachbereich.DEUTSCH);
		if (belegung === null) {
			this.ergebnis.log.add("Deutsch muss belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			if (belegung.belegungen[halbjahr.id] === null) {
				this.ergebnis.log.add(JavaString.format("Deutsch mussen im Halbjahr %s belegt sein, damit eine Abiturzulassung möglich ist.", halbjahr.kuerzel));
				return false;
			}
			const np: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
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

	private pruefeFremdsprachenMarkierung(): boolean {
		const belegungen: List<AbiturFachbelegung> = this.manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE);
		if (belegungen.isEmpty()) {
			this.ergebnis.log.add("Es muss mindestens eine Fremdsprache belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (const belegung of belegungen) {
			let found: boolean = true;
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				if (belegung.belegungen[halbjahr.id] === null) {
					found = false;
					break;
				}
				const np: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
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

	private pruefeAufWeitereFremdsprache(): void {
		this.hatWeitereFremdsprache = false;
		let belegungen: List<AbiturFachbelegung> = this.manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE);
		for (const belegung of belegungen) {
			if (belegung as unknown === this.fremdsprache as unknown)
				continue;
			let found: boolean = true;
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				if (belegung.belegungen[halbjahr.id] === null) {
					found = false;
					break;
				}
				const np: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np === null) || (np === 0)) {
					if (((halbjahr as unknown === GostHalbjahr.Q21 as unknown) || (halbjahr as unknown === GostHalbjahr.Q22 as unknown)) || this.manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
						found = false;
						break;
					}
					continue;
				}
				if (((halbjahr as unknown === GostHalbjahr.Q21 as unknown) || (halbjahr as unknown === GostHalbjahr.Q22 as unknown)) && !this.manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
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
			const tmpFach: GostFach | null = this.manager.getFach(this.fremdsprache);
			const fs: string | null = (tmpFach === null) ? "" : GostFachUtils.getFremdsprache(tmpFach);
			for (const belegung of belegungen) {
				const sachfach: GostFach | null = this.manager.getFach(belegung);
				if ((sachfach === null) || (sachfach.biliSprache === null) || (JavaString.isBlank(sachfach.biliSprache)) || (JavaObject.equalsTranspiler(sachfach.biliSprache, (fs))))
					continue;
				let found: boolean = true;
				for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
					if (belegung.belegungen[halbjahr.id] === null) {
						found = false;
						break;
					}
					const np: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
					if ((np === null) || (np === 0)) {
						if (((halbjahr as unknown === GostHalbjahr.Q21 as unknown) || (halbjahr as unknown === GostHalbjahr.Q22 as unknown)) || this.manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
							found = false;
							break;
						}
						continue;
					}
					if (((halbjahr as unknown === GostHalbjahr.Q21 as unknown) || (halbjahr as unknown === GostHalbjahr.Q22 as unknown)) && !this.manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
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

	private pruefeNeuEinsetzendeFremdsprache(): boolean {
		const fs2: string | null = SprachendatenUtils.getZweiteSpracheInSekI(this.manager.getSprachendaten());
		if (fs2 !== null)
			return true;
		const belegungen: List<AbiturFachbelegung> = this.manager.filterFremdspracheNeuEinsetzend(this.manager.getFachbelegungen(GostFachbereich.FREMDSPRACHE));
		for (const belegung of belegungen) {
			let found: boolean = true;
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				if (belegung.belegungen[halbjahr.id] === null) {
					found = false;
					break;
				}
				const np: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
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

	private pruefeKunstMusikOderLiteraturMarkierung(): boolean {
		const belegungen: List<AbiturFachbelegung> = this.manager.getFachbelegungen(GostFachbereich.KUNST_MUSIK_LITERATUR);
		if (belegungen.isEmpty()) {
			this.ergebnis.log.add("Es muss mindestens Kunst, Musik oder Literatur belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		for (const belegung of belegungen) {
			let kurseMarkiert: number = 0;
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				if (belegung.belegungen[halbjahr.id] === null)
					continue;
				const np: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np === null) || (np === 0))
					continue;
				if (this.manager.hatMarkierungHalbjahr(belegung, halbjahr))
					kurseMarkiert++;
			}
			if (kurseMarkiert >= 2)
				return true;
		}
		this.ergebnis.log.add("Es müssen mindestens 2 Kurse in Kunst, Musik oder Literatur markiert sein, damit eine Abiturzulassung möglich ist.");
		return false;
	}

	private pruefeLiteraturAnzahlMarkierung(): boolean {
		const belegung: AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel("LI");
		if (belegung === null)
			return true;
		let kurseMarkiert: number = 0;
		for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
			if (belegung.belegungen[halbjahr.id] === null)
				continue;
			const np: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
			if ((np === null) || (np === 0))
				continue;
			if (this.manager.hatMarkierungHalbjahr(belegung, halbjahr))
				kurseMarkiert++;
		}
		if (kurseMarkiert > 2) {
			this.ergebnis.log.add("Es dürfen maximal 2 Kurse in Literatur markiert sein.");
			return false;
		}
		return true;
	}

	private pruefeMusikMarkierung(): boolean {
		const belVP: AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel("VP");
		const belIN: AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel("IN");
		if ((this.manager.zaehleMarkierungenQualifikationsphase(belVP) > 0) || (this.manager.zaehleMarkierungenQualifikationsphase(belIN) > 0)) {
			this.ergebnis.log.add("Vokal- und Instrumentalpraktischer Grundkurse können nicht für die Abiturzulassung markiert werden.");
			return false;
		}
		return true;
	}

	private pruefeAnzahlMarkierungen(fb: GostFachbereich, min: number, fehler: string): boolean {
		const belegungen: List<AbiturFachbelegung> = this.manager.getFachbelegungen(fb);
		let anzahl: number = 0;
		for (const belegung of belegungen)
			anzahl += this.manager.zaehleMarkierungenQualifikationsphase(belegung);
		if (anzahl < min) {
			this.ergebnis.log.add(fehler);
			return false;
		}
		return true;
	}

	private pruefeGesellschaftswissenschaftMarkierung(): boolean {
		const belegungen: List<AbiturFachbelegung> = this.manager.getFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH);
		if (belegungen.isEmpty()) {
			this.ergebnis.log.add("Es muss mindestens eine Gesellschaftswissenschaft belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		if (this.manager.pruefeMarkierungExistiertDurchgaengig(belegungen) !== null)
			return true;
		this.ergebnis.log.add("Es muss mindestens eine Gesellschaftswissenschaft durchgängig markiert sein, damit eine Abiturzulassung möglich ist.");
		return false;
	}

	private pruefeReligionsOderErsatzMarkierungen(): boolean {
		const hatAbiRE: boolean = this.manager.hatFachbereichInAbiturfaechern(GostFachbereich.RELIGION);
		const hatAbiPL: boolean = this.manager.hatFachbereichInAbiturfaechern(GostFachbereich.PHILOSOPHIE);
		const belRE: List<AbiturFachbelegung> = this.manager.getFachbelegungen(GostFachbereich.RELIGION);
		const countRE: number = this.manager.zaehleAlleMarkierungenQualifikationsphase(belRE);
		const countPL: number = this.manager.zaehleAlleMarkierungenQualifikationsphase(this.manager.getFachbelegungen(GostFachbereich.PHILOSOPHIE));
		if (!hatAbiRE && !hatAbiPL && ((countRE + countPL) < 2)) {
			this.ergebnis.log.add("Es müssen mindestens zwei Kurse aus der Fächergruppe Religionslehre und Philosophie markiert werden.");
			return false;
		}
		const countGW: number = this.manager.zaehleAlleMarkierungenQualifikationsphase(this.manager.getFachbelegungen(GostFachbereich.GESCHICHTE, GostFachbereich.SOZIALWISSENSCHAFTEN, GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE));
		const hatAbiGW: boolean = this.manager.hatFachbereichInAbiturfaechern(GostFachbereich.GESCHICHTE, GostFachbereich.SOZIALWISSENSCHAFTEN, GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE);
		if (!(!hatAbiRE && hatAbiPL && !hatAbiGW))
			return true;
		if (countRE >= 2)
			return true;
		if (this.manager.pruefeBelegungExistiert(belRE, GostHalbjahr.Q11, GostHalbjahr.Q12)) {
			this.ergebnis.log.add("Es müssen mindestens zwei Kurse aus der Fächergruppe Religionslehre markiert werden.");
			return false;
		}
		if ((countRE === 1) && (countGW + countPL >= 9))
			return true;
		if (this.manager.pruefeBelegungExistiert(belRE, GostHalbjahr.Q11) || this.manager.pruefeBelegungExistiert(belRE, GostHalbjahr.Q11)) {
			this.ergebnis.log.add("Es müssen zwei Kurse Religionslehre oder ein Kurs Religionslehre und ein Kurs des Ersatzfaches markiert werden.");
			return false;
		}
		if (countGW + countPL >= 10)
			return true;
		this.ergebnis.log.add("Es müssen zwei Kurse Religionslehre oder ein Kurs Religionslehre und ein Kurs des Ersatzfaches oder zwei Kurse des Ersatzfaches markiert werden.");
		return false;
	}

	private pruefeNaturwissenschaftMarkierung(): boolean {
		const belegungen: List<AbiturFachbelegung> = this.manager.getFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH);
		if (belegungen.isEmpty()) {
			this.ergebnis.log.add("Es muss mindestens eine klassische Naturwissenschaft belegt sein, damit eine Abiturzulassung möglich ist.");
			return false;
		}
		this.naturwissenschaft = this.manager.pruefeMarkierungExistiertDurchgaengig(belegungen);
		if (this.naturwissenschaft !== null)
			return true;
		this.ergebnis.log.add("Es muss mindestens eine klassische Naturwissenschaft durchgängig markiert sein, damit eine Abiturzulassung möglich ist.");
		return false;
	}

	private pruefeAufWeitereNaturwissenschaft(): void {
		if (this.naturwissenschaft === null)
			return;
		this.hatWeitereNaturwissenschaft = false;
		const belegungen: List<AbiturFachbelegung> = this.manager.getFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH);
		for (const belegung of belegungen) {
			if (belegung.fachID === this.naturwissenschaft.fachID)
				continue;
			let found: boolean = true;
			for (const halbjahr of GostHalbjahr.getQualifikationsphase()) {
				if (belegung.belegungen[halbjahr.id] === null) {
					found = false;
					break;
				}
				const np: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belegung.belegungen[halbjahr.id]);
				if ((np === null) || (np === 0)) {
					if (((halbjahr as unknown === GostHalbjahr.Q21 as unknown) || (halbjahr as unknown === GostHalbjahr.Q22 as unknown)) || this.manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
						found = false;
						break;
					}
					continue;
				}
				if (((halbjahr as unknown === GostHalbjahr.Q21 as unknown) || (halbjahr as unknown === GostHalbjahr.Q22 as unknown)) && !this.manager.hatMarkierungHalbjahr(belegung, halbjahr)) {
					found = false;
					break;
				}
			}
			if (found) {
				this.hatWeitereNaturwissenschaft = true;
				return;
			}
		}
	}

	private pruefeSchwerpunkt(): boolean {
		if (this.hatWeitereFremdsprache || this.hatWeitereNaturwissenschaft)
			return true;
		this.ergebnis.log.add("Es müssen zwei Kurse einer Naturwissenschaft oder einer schriftlich belegten weiteren Fremdsprache in Q2.1 und Q2.2 markiert werden.");
		return false;
	}

	private pruefeProjektkurs(): boolean {
		const projektkurs: AbiturFachbelegung | null = this.belegpruefungProjektkurse.getProjektkurs();
		if ((projektkurs === null) || (!this.manager.pruefeBelegungMitSchriftlichkeit(projektkurs, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q21, GostHalbjahr.Q22))) {
			this.ergebnis.log.add("Es muss ein Projektkurs schriftlich in der Q2 belegt werden.");
			return false;
		}
		if (this.manager.zaehleHalbjahresbelegungen(projektkurs, ...GostHalbjahr.getQualifikationsphase()) !== 2) {
			this.ergebnis.log.add("Der Projektkurs wurde in zu vielen Halbjahren belegt.");
			return false;
		}
		if (this.manager.zaehleMarkierungenQualifikationsphase(projektkurs) !== 2) {
			this.ergebnis.log.add("Beide Halbjahre des Projektkurse müssen markiert werden.");
			return false;
		}
		const fach: GostFach | null = this.manager.faecher().get(projektkurs.fachID);
		if (fach === null) {
			this.ergebnis.log.add("Es konnte kein Fach für die Projektkurs-Fachbelegung bestimmt werden.");
			return false;
		}
		const referenzfachBelegungen: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();
		if (fach.projektKursLeitfach1ID !== null) {
			const fb1: AbiturFachbelegung | null = this.manager.getFachbelegungByID(fach.projektKursLeitfach1ID);
			if (fb1 !== null)
				referenzfachBelegungen.add(fb1);
		}
		if (fach.projektKursLeitfach2ID !== null) {
			const fb2: AbiturFachbelegung | null = this.manager.getFachbelegungByID(fach.projektKursLeitfach2ID);
			if (fb2 !== null)
				referenzfachBelegungen.add(fb2);
		}
		for (const referenzfachBelegung of referenzfachBelegungen) {
			const referenzfach: GostFach | null = this.manager.faecher().get(referenzfachBelegung.fachID);
			if (referenzfach === null) {
				this.ergebnis.log.add("Es konnte kein Fach für die Belegung des Referenzfaches bestimmt werden.");
				continue;
			}
			if ((projektkurs.abiturFach !== null) && (referenzfachBelegung.abiturFach !== null))
				continue;
			if (!this.manager.pruefeBelegung(referenzfachBelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12))
				continue;
			if (!this.manager.pruefeBelegungMitSchriftlichkeit(referenzfachBelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12))
				continue;
			if (this.manager.hatMarkierungHalbjahr(referenzfachBelegung, GostHalbjahr.Q11) && this.manager.hatMarkierungHalbjahr(referenzfachBelegung, GostHalbjahr.Q12))
				return true;
		}
		this.ergebnis.log.add("Für den Projektkurs muss ein Referenzfach in der Q1 markiert werden. Ist das Projektkursfach fünftes Abiturfach, so darf das Referenzfach kein Abiturfach sein.");
		return false;
	}

	private pruefeAnzahlUndDefizite(): boolean {
		if (this.manager.zaehleMarkierungenOhneWertungOderMitNullPunkten(this.manager.daten().fachbelegungen) > 0) {
			this.ergebnis.log.add("Es wurden Kurse markiert, welche mit 0 Punkten bewertet wurden. Diese gelten aber als nicht belegt und dürfen nicht markiert werden.");
			return false;
		}
		const count: number = this.manager.zaehleAlleMarkierungenQualifikationsphase(this.manager.daten().fachbelegungen);
		if ((count !== 36)) {
			this.ergebnis.log.add("Es müssen genau 36 Kurse markiert werden.");
			return false;
		}
		const countDefizite: number = this.manager.zaehleMarkierungenMitDefiziten(this.manager.daten().fachbelegungen);
		if (countDefizite > 7) {
			this.ergebnis.log.add("Keine Zulassung zum Abitur. Es wurden zu viele Kurse mit Defizit markiert.");
			return false;
		}
		return true;
	}

	private pruefeOptimierung(): boolean {
		const count: number = this.manager.zaehleAlleMarkierungenQualifikationsphase(this.manager.daten().fachbelegungen);
		if (count >= 36)
			return true;
		const durchschnitt: number = this.manager.berechneMarkierungenDurchschnittspunkte();
		const hatPjkBLL: boolean = JavaObject.equalsTranspiler(GostBesondereLernleistung.PROJEKTKURS.kuerzel, (this.manager.daten().besondereLernleistung));
		const auswahlliste: List<GostAbiturMarkierungsalgorithmusBelegung> = new ArrayList<GostAbiturMarkierungsalgorithmusBelegung>();
		for (const belegung of this.manager.daten().fachbelegungen) {
			const fach: GostFach | null = this.manager.getFach(belegung);
			if ((fach === null) || (hatPjkBLL && JavaObject.equalsTranspiler("PX", (fach.kuerzel))))
				continue;
			for (const hj of GostHalbjahr.getQualifikationsphase()) {
				const belHj: AbiturFachbelegungHalbjahr | null = belegung.belegungen[hj.id];
				if ((belHj === null) || ((belHj.block1gewertet !== null) && belHj.block1gewertet))
					continue;
				const np: number | null = this.manager.getNotenpunkteOfFachbelegungHalbjahr(belHj);
				if ((np === null) || (np === 0))
					continue;
				auswahlliste.add(new GostAbiturMarkierungsalgorithmusBelegung(belegung, belHj, np));
			}
		}
		if (auswahlliste.isEmpty())
			return true;
		const belMU: AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel("MU");
		const belLI: AbiturFachbelegung | null = this.manager.getFachbelegungByKuerzel("LI");
		const countMU: number = this.manager.zaehleMarkierungenQualifikationsphase(belMU);
		const countLI: number = this.manager.zaehleMarkierungenQualifikationsphase(belLI);
		const countMoeglichLiteratur: number = 2 - countLI;
		const countMoeglichMusik: number = 4 - countMU;
		this.sort(auswahlliste);
		while (!auswahlliste.isEmpty()) {
			const bel: GostAbiturMarkierungsalgorithmusBelegung = auswahlliste.getFirst();
			auswahlliste.removeFirst();
			if (bel.notenpunkte <= durchschnitt)
				break;
			const fach: GostFach | null = this.manager.getFach(bel.belegung);
			if ((fach === null) || (JavaObject.equalsTranspiler("LI", (fach.kuerzel)) && (countMoeglichLiteratur <= 0)) || (JavaObject.equalsTranspiler("MU", (fach.kuerzel)) && (countMoeglichMusik <= 0)))
				continue;
			this.ergebnis.log.add("Es existieren nicht markierte Kurse, die durch Markierung den Abiturdurchschnitt verbessern können.");
			return false;
		}
		return true;
	}

	private sort(auswahlliste: List<GostAbiturMarkierungsalgorithmusBelegung>): void {
		const comparatorBelegungen: Comparator<GostAbiturMarkierungsalgorithmusBelegung> = { compare: (a: GostAbiturMarkierungsalgorithmusBelegung, b: GostAbiturMarkierungsalgorithmusBelegung) => {
			let tmp: number = b.notenpunkte - a.notenpunkte;
			if (tmp !== 0)
				return tmp;
			const aFach: GostFach | null = this.manager.getFach(a.belegung);
			const bFach: GostFach | null = this.manager.getFach(b.belegung);
			if ((aFach === null) || (bFach === null))
				return -1;
			tmp = GostFachbereich.compareGostFach(aFach, bFach);
			if (tmp !== 0)
				return tmp;
			const hjA: GostHalbjahr | null = GostHalbjahr.fromKuerzel(a.belegungHalbjahr.halbjahrKuerzel);
			const hjB: GostHalbjahr | null = GostHalbjahr.fromKuerzel(b.belegungHalbjahr.halbjahrKuerzel);
			if ((hjA === null) || (hjB === null))
				return -1;
			return hjB.id - hjA.id;
		} };
		auswahlliste.sort(comparatorBelegungen);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.Abi30GostAbiturMarkierungspruefung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.Abi30GostAbiturMarkierungspruefung'].includes(name);
	}

	public static readonly class = new Class<Abi30GostAbiturMarkierungspruefung>('de.svws_nrw.core.abschluss.gost.Abi30GostAbiturMarkierungspruefung');

}

export function cast_de_svws_nrw_core_abschluss_gost_Abi30GostAbiturMarkierungspruefung(obj: unknown): Abi30GostAbiturMarkierungspruefung {
	return obj as Abi30GostAbiturMarkierungspruefung;
}
