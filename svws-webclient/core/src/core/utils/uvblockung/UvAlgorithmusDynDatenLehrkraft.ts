import { JavaObject } from '../../../java/lang/JavaObject';
import { UvPlanungsabschnitt } from '../../../core/data/uv/UvPlanungsabschnitt';
import { UvLehrer } from '../../../core/data/uv/UvLehrer';
import { UvAlgorithmusDynDatenKlasse } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenKlasse';
import { UvAlgorithmusDynDatenLerngruppe } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLerngruppe';
import { ArrayList } from '../../../java/util/ArrayList';
import { UvManager } from '../../../core/utils/uv/UvManager';
import { JahrgangsDaten } from '../../../core/data/jahrgang/JahrgangsDaten';
import { JavaString } from '../../../java/lang/JavaString';
import { Logger } from '../../../core/logger/Logger';
import { LogLevel } from '../../../core/logger/LogLevel';
import { UvAlgorithmusDynDatenFach } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenFach';
import { UvKlasse } from '../../../core/data/uv/UvKlasse';
import type { List } from '../../../java/util/List';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenLehrkraft extends JavaObject {

	/**
	 * Ein Logger für Debug-Zwecke.
	 */
	readonly log: Logger;

	/**
	 * Der Index im internen Array.
	 */
	readonly interneID: number;

	/**
	 * Die externe ID der DB/GUI.
	 */
	readonly uvID: number;

	/**
	 * Das Pflichtstunden-Soll.
	 */
	readonly stundenSoll: number;

	/**
	 * Die Summe aller Entlastungen.
	 */
	readonly stundenAnrechnung: number;

	/**
	 * Die Summe aller Stunden der zugeordneten Lerngruppen.
	 */
	istStundensummeLerngruppen: number = 0;

	/**
	 * Die aktuelle Anzahl an Stunden, in denen die Lehrkraft in der jeweiligen Klasse unterrichtet.
	 */
	readonly istStundenProKlasse: Array<number>;

	/**
	 * Die aktuelle Definition, ob die Lehrkraft in der Klasse Klassenlehrer ist.
	 */
	readonly istLeitung1InKlasse: Array<boolean>;

	/**
	 * Die aktuelle Definition, ob die Lehrkraft in der Klasse stellv. Klassenlehrer ist.
	 */
	readonly istLeitung2InKlasse: Array<boolean>;

	/**
	 * Die aktuelle Anzahl an Klassenlehrer-Einsätzen (Leitung 1).
	 */
	istAnzahlLeitung1: number = 0;

	/**
	 * Die aktuelle Anzahl an stellv. Klassenlehrer-Einsätzen (Leitung 2).
	 */
	istAnzahlLeitung2: number = 0;

	/**
	 * Die aktuelle Anzahl an Lerngruppen, denen diese Lehrkraft zugeordnet ist.
	 */
	istAnzahlLerngruppen: number = 0;

	/**
	 * Die aktuelle Anzahl an Korrekturen dieser Lehrkraft.
	 */
	istAnzahlKorrekturen: number = 0;

	/**
	 * Die aktuelle Anzahl an zugeordneten Lerngruppen pro Fach.
	 */
	readonly istFachZuLerngruppenAnzahl: Array<number>;

	/**
	 * Die aktuelle Anzahl an Stunden, die diese Lehrkraft im jeweiligen Jahrgang unterrichtet.
	 */
	readonly istStundenProJahrgang: Array<number>;

	/**
	 * Die aktuelle Anzahl an verschiedenen Jahrgängen, in denen diese Lehrkraft unterrichtet.
	 */
	istJahrgangAnzahl: number = 0;

	/**
	 * Der Stundenplan dieser Lehrkraft: [Wochentag][Stunde][Wochentyp] --> Anzahl der belegenden Lerngruppen (Regel 17).
	 */
	readonly stundenplanAnzahl: Array<Array<Array<number>>>;

	/**
	 * Alle Lerngruppen, denen diese Lehrkraft aktuell zugeordnet ist.
	 */
	readonly istLerngruppenZugeordnet: List<UvAlgorithmusDynDatenLerngruppe>;

	/**
	 * Die Menge aller Regel-Objekte, die sich auf diese Lehrkraft beziehen.
	 */
	readonly regeln: List<UvAlgorithmusDynDatenRegel>;


	/**
	 * Der Konstruktor.
	 *
	 * @param index               Der Index, unter dem dieses Objekt im Array der aufrufenden Klasse gespeichert ist.
	 * @param log                 Ein Logger für Debug-Zwecke.
	 * @param man                 Der {@link UvManager}.
	 * @param planungsabschnitt   Der {@link UvPlanungsabschnitt}.
	 * @param uvLehrer            Das {@link UvLehrer}-Objekt der DB/GUI.
	 */
	public constructor(index: number, log: Logger, man: UvManager, planungsabschnitt: UvPlanungsabschnitt, uvLehrer: UvLehrer) {
		super();
		this.log = log;
		this.interneID = index;
		this.uvID = uvLehrer.id;
		const dStundenSoll: number | null = man.lehrerPflichtstundensollGetDoubleByLehrerAndPlanungsabschnitt(uvLehrer, planungsabschnitt);
		this.stundenSoll = (dStundenSoll === null) ? 0.0 : dStundenSoll;
		this.stundenAnrechnung = man.lehrerAnrechnungsstundenGetDoubleByLehrerAndPlanungsabschnitt(uvLehrer, planungsabschnitt);
		this.istStundensummeLerngruppen = 0.0;
		const uvKlassenMenge: List<UvKlasse> = man.klasseGetMengeByPlanungsabschnitt(planungsabschnitt);
		this.istStundenProKlasse = Array(uvKlassenMenge.size()).fill(0);
		this.istLeitung1InKlasse = Array(uvKlassenMenge.size()).fill(false);
		this.istLeitung2InKlasse = Array(uvKlassenMenge.size()).fill(false);
		this.istAnzahlLeitung1 = 0;
		this.istAnzahlLeitung2 = 0;
		this.istAnzahlLerngruppen = 0;
		this.istAnzahlKorrekturen = 0;
		const uvJahrgangMenge: List<JahrgangsDaten> = man.jahrgangsdatenGetMenge();
		this.istStundenProJahrgang = Array(uvJahrgangMenge.size()).fill(0);
		this.istJahrgangAnzahl = 0;
		const nFaecher: number = man.fachGetMengeAsList().size();
		this.istFachZuLerngruppenAnzahl = Array(nFaecher).fill(0);
		for (let i: number = 0; i < nFaecher; i++) {
			this.istFachZuLerngruppenAnzahl[i] = 0;
		}
		this.stundenplanAnzahl = [...Array(man.zeitrasterGetMaxWochentag() + 1)].map(e => [...Array(man.zeitrasterGetMaxStunde() + 1)].map(e => Array(man.zeitrasterGetWochentypmodell() + 1).fill(0)));
		this.istLerngruppenZugeordnet = new ArrayList();
		this.regeln = new ArrayList();
	}

	public toString(): string {
		return JavaString.format(("UvAlgorithmusDynDatenLehrkraft { interneID=%d, uvID=%d }"), this.interneID, this.uvID);
	}

	/**
	 * Erhöht die Leitung-1-Anzahl der Lehrkraft. Den Leitung-1-Malus aktualisiert der Aufrufer.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public stateLeitung1Inc(klasse: UvAlgorithmusDynDatenKlasse): void {
		if (this.istLeitung1InKlasse[klasse.interneID]) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s ist bereits Leitung 1 in Klasse %s!", this.toString(), klasse.toString()));
			return;
		}
		this.istAnzahlLeitung1++;
		this.istLeitung1InKlasse[klasse.interneID] = true;
	}

	/**
	 * Verringert die Leitung-1-Anzahl der Lehrkraft. Den Leitung-1-Malus aktualisiert der Aufrufer.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public stateLeitung1Dec(klasse: UvAlgorithmusDynDatenKlasse): void {
		if (!this.istLeitung1InKlasse[klasse.interneID]) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s ist gar nicht Leitung 1 in Klasse %s!", this.toString(), klasse.toString()));
			return;
		}
		if (this.istAnzahlLeitung1 <= 0) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s hätte nach dem Entfernen von Leitung 1 einen negativen Wert!", this.toString()));
			return;
		}
		this.istAnzahlLeitung1--;
		this.istLeitung1InKlasse[klasse.interneID] = false;
	}

	/**
	 * Erhöht die Leitung-2-Anzahl der Lehrkraft. Den Leitung-2-Malus aktualisiert der Aufrufer.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public stateLeitung2Inc(klasse: UvAlgorithmusDynDatenKlasse): void {
		if (this.istLeitung2InKlasse[klasse.interneID]) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s ist bereits Leitung 2 in Klasse %s!", this.toString(), klasse.toString()));
			return;
		}
		this.istAnzahlLeitung2++;
		this.istLeitung2InKlasse[klasse.interneID] = true;
	}

	/**
	 * Verringert die Leitung-2-Anzahl der Lehrkraft. Den Leitung-2-Malus aktualisiert der Aufrufer.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public stateLeitung2Dec(klasse: UvAlgorithmusDynDatenKlasse): void {
		if (!this.istLeitung2InKlasse[klasse.interneID]) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s ist gar nicht Leitung 2 in Klasse %s!", this.toString(), klasse.toString()));
			return;
		}
		if (this.istAnzahlLeitung2 <= 0) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s hätte nach dem Entfernen von Leitung 2 einen negativen Wert!", this.toString()));
			return;
		}
		this.istAnzahlLeitung2--;
		this.istLeitung2InKlasse[klasse.interneID] = false;
	}

	/**
	 * Fügt der Lehrkraft eine zugeordnete Lerngruppe hinzu. Den Stunden-Malus aktualisiert der Aufrufer.
	 *
	 * @param lerngruppe   Die hinzuzufügende Lerngruppe.
	 */
	public stateLerngruppeZugeordnetAdd(lerngruppe: UvAlgorithmusDynDatenLerngruppe): void {
		if (lerngruppe.fach === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s kann die Lerngruppe %s nicht hinzufügen, da das Fach der Lerngruppe nicht gemappt ist!", this.toString(), lerngruppe.toString()));
			return;
		}
		this.istStundensummeLerngruppen += lerngruppe.wochenstundenVorgesehenGekuerzt;
		this.istAnzahlLerngruppen++;
		for (const klasse of lerngruppe.klassenmenge) {
			this.istStundenProKlasse[klasse.interneID] += lerngruppe.wochenstundenVorgesehenGekuerzt;
		}
		for (const jahrgang of lerngruppe.jahrgangmenge) {
			const vorher: number = this.istStundenProJahrgang[jahrgang.interneID];
			const nachher: number = this.istStundenProJahrgang[jahrgang.interneID] + lerngruppe.wochenstundenVorgesehenGekuerzt;
			this.istStundenProJahrgang[jahrgang.interneID] = nachher;
			if ((vorher === 0.0) && (nachher > 0.0)) {
				this.istJahrgangAnzahl++;
			}
		}
		this.istFachZuLerngruppenAnzahl[lerngruppe.fach.interneID]++;
		this.istAnzahlKorrekturen += lerngruppe.korrekturBelastung;
		for (const zeitslot of lerngruppe.zeitslots) {
			if (zeitslot.wochentyp === 0) {
				for (let wochentyp: number = 1; wochentyp < this.stundenplanAnzahl[zeitslot.wochentag][zeitslot.stunde].length; wochentyp++) {
					this.stundenplanAnzahl[zeitslot.wochentag][zeitslot.stunde][wochentyp]++;
				}
			} else {
				this.stundenplanAnzahl[zeitslot.wochentag][zeitslot.stunde][zeitslot.wochentyp]++;
			}
		}
		this.istLerngruppenZugeordnet.add(lerngruppe);
	}

	/**
	 * Entfernt bei der Lehrkraft eine zugeordnete Lerngruppe. Den Stunden-Malus aktualisiert der Aufrufer.
	 *
	 * @param lerngruppe  Die zu entfernende Lerngruppe.
	 */
	public stateLerngruppeZugeordnetDel(lerngruppe: UvAlgorithmusDynDatenLerngruppe): void {
		if (this.istEntfernenMoeglich(lerngruppe)) {
			this.entferneLerngruppeZustand(lerngruppe);
		}
	}

	/**
	 * Prüft, ob die Lerngruppe bei dieser Lehrkraft entfernt werden kann.
	 *
	 * @param lerngruppe   Die zu entfernende Lerngruppe.
	 *
	 * @return TRUE, falls das Entfernen möglich ist.
	 */
	private istEntfernenMoeglich(lerngruppe: UvAlgorithmusDynDatenLerngruppe): boolean {
		if (lerngruppe.fach === null) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s kann die Lerngruppe %s nicht entfernen, da das Fach der Lerngruppe nicht gemappt ist!", this.toString(), lerngruppe.toString()));
			return false;
		}
		const stundenDanach: number = this.istStundensummeLerngruppen - lerngruppe.wochenstundenVorgesehenGekuerzt;
		if (stundenDanach < 0) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s hätte nach dem Entfernen der Lerngruppe %s eine negative Stunden-Anzahl %f!", this.toString(), lerngruppe.toString(), stundenDanach));
			return false;
		}
		if ((this.istAnzahlLerngruppen - 1) < 0) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s hätte nach dem Entfernen der Lerngruppe %s eine negative Lerngruppen-Anzahl %d!", this.toString(), lerngruppe.toString(), this.istAnzahlLerngruppen - 1));
			return false;
		}
		for (const klasse of lerngruppe.klassenmenge) {
			if (this.istStundenProKlasse[klasse.interneID] - lerngruppe.wochenstundenVorgesehenGekuerzt < 0) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s hätte nach dem Entfernen der Lerngruppe %s in Klasse %s eine negative Stunden-Anzahl!", this.toString(), lerngruppe.toString(), klasse.toString()));
				return false;
			}
		}
		return true;
	}

	/**
	 * Entfernt die Lerngruppe aus dem Zustand der Lehrkraft.
	 *
	 * @param lerngruppe   Die zu entfernende Lerngruppe.
	 */
	private entferneLerngruppeZustand(lerngruppe: UvAlgorithmusDynDatenLerngruppe): void {
		this.istStundensummeLerngruppen -= lerngruppe.wochenstundenVorgesehenGekuerzt;
		this.istAnzahlLerngruppen--;
		for (const klasse of lerngruppe.klassenmenge) {
			this.istStundenProKlasse[klasse.interneID] -= lerngruppe.wochenstundenVorgesehenGekuerzt;
		}
		for (const jahrgang of lerngruppe.jahrgangmenge) {
			const vorher: number = this.istStundenProJahrgang[jahrgang.interneID];
			const nachher: number = this.istStundenProJahrgang[jahrgang.interneID] - lerngruppe.wochenstundenVorgesehenGekuerzt;
			this.istStundenProJahrgang[jahrgang.interneID] = nachher;
			if ((vorher > 0.0) && (nachher === 0.0)) {
				this.istJahrgangAnzahl--;
			}
		}
		const fach: UvAlgorithmusDynDatenFach | null = lerngruppe.fach;
		if (fach !== null) {
			this.istFachZuLerngruppenAnzahl[fach.interneID]--;
		}
		this.istAnzahlKorrekturen -= lerngruppe.korrekturBelastung;
		for (const zeitslot of lerngruppe.zeitslots) {
			if (zeitslot.wochentyp === 0) {
				for (let wt: number = 1; wt < this.stundenplanAnzahl[zeitslot.wochentag][zeitslot.stunde].length; wt++) {
					this.stundenplanAnzahl[zeitslot.wochentag][zeitslot.stunde][wt]--;
				}
			} else {
				this.stundenplanAnzahl[zeitslot.wochentag][zeitslot.stunde][zeitslot.wochentyp]--;
			}
		}
		this.istLerngruppenZugeordnet.remove(lerngruppe);
	}

	gibIstLeitung1IncMoeglich(klasse: UvAlgorithmusDynDatenKlasse): boolean {
		return !this.istLeitung1InKlasse[klasse.interneID];
	}

	gibIstLeitung1DecMoeglich(klasse: UvAlgorithmusDynDatenKlasse): boolean {
		return this.istLeitung1InKlasse[klasse.interneID] && (this.istAnzahlLeitung1 > 0);
	}

	gibIstLeitung2IncMoeglich(klasse: UvAlgorithmusDynDatenKlasse): boolean {
		return !this.istLeitung2InKlasse[klasse.interneID];
	}

	gibIstLeitung2DecMoeglich(klasse: UvAlgorithmusDynDatenKlasse): boolean {
		return this.istLeitung2InKlasse[klasse.interneID] && (this.istAnzahlLeitung2 > 0);
	}

	gibIstLerngruppeZugeordnetDelMoeglich(lerngruppe: UvAlgorithmusDynDatenLerngruppe): boolean {
		if (this.istAnzahlLerngruppen <= 0) {
			return false;
		}
		if (this.istStundensummeLerngruppen - lerngruppe.wochenstundenVorgesehenGekuerzt < 0) {
			return false;
		}
		for (const klasse of lerngruppe.klassenmenge) {
			if (this.istStundenProKlasse[klasse.interneID] - lerngruppe.wochenstundenVorgesehenGekuerzt < 0) {
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenLehrkraft';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenLehrkraft'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenLehrkraft>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenLehrkraft');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenLehrkraft(obj: unknown): UvAlgorithmusDynDatenLehrkraft {
	return obj as UvAlgorithmusDynDatenLehrkraft;
}
