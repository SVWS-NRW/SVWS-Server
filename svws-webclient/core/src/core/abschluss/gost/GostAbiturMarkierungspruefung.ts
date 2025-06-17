import { JavaObject } from '../../../java/lang/JavaObject';
import { GostAbiturFach } from '../../../core/types/gost/GostAbiturFach';
import { AbiturFachbelegung } from '../../../core/data/gost/AbiturFachbelegung';
import { JavaString } from '../../../java/lang/JavaString';
import { AbiturdatenManager } from '../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegpruefung } from '../../../core/abschluss/gost/GostBelegpruefung';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { Projektkurse, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Projektkurse } from '../../../core/abschluss/gost/belegpruefung/Projektkurse';
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
		let success : boolean = true;
		success = this.pruefeLeistungskursDefizite();
		if (!success)
			return success;
		success = this.pruefeAbiturfachNullPunkte();
		if (!success)
			return success;
		success = this.pruefeAbiturfachMarkierung();
		if (!success)
			return success;
		success = this.pruefeDeutschMarkierung();
		if (!success)
			return success;
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
			if (!this.manager.hatMarkierungQualifikationsphase(belegung)) {
				this.ergebnis.log.add("Deutsch muss durchgehend markiert sein.");
				return false;
			}
		}
		return true;
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
