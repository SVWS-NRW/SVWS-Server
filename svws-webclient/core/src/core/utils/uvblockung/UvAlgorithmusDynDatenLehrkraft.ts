import { JavaObject } from '../../../java/lang/JavaObject';
import { UvPlanungsabschnitt } from '../../../core/data/uv/UvPlanungsabschnitt';
import { UvLehrer } from '../../../core/data/uv/UvLehrer';
import { UvAlgorithmusDynDatenKlasse } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenKlasse';
import { ArrayList } from '../../../java/util/ArrayList';
import { UvAlgorithmusDynDatenLerngruppe } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLerngruppe';
import { UvManager } from '../../../core/utils/uv/UvManager';
import { JahrgangsDaten } from '../../../core/data/jahrgang/JahrgangsDaten';
import { JavaString } from '../../../java/lang/JavaString';
import { Logger } from '../../../core/logger/Logger';
import { LogLevel } from '../../../core/logger/LogLevel';
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
	 * Der Pflichtstunden-Soll.
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
	 * Die aktuelle Anzahl an Stunden in denen die Lehrkraft in der jeweiligen Klasse unterrichtet.
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
	 * Die aktuelle Anzahl an verschiedenen Jahrgängen in denen diese Lehrkraft unterrichtet.
	 */
	istJahrgangAnzahl: number = 0;

	/**
	 * Die aktuelle Anzahl an Lerngruppen-Zuordnungen im Zeitslot [Wochentag][Stunde][Wochentyp] (Regel 17).
	 */
	readonly istWochentagStundeWochentypCounter: Array<Array<Array<number>>>;

	/**
	 * Die Menge aller Regel-Objekte die sich auf diese Lehrkraft beziehen.
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
		const maxWochentag: number = man.zeitrasterGetMaxWochentag();
		const maxStunde: number = man.zeitrasterGetMaxStunde();
		const maxWochentyp: number = man.zeitrasterGetWochentypmodell();
		this.istWochentagStundeWochentypCounter = [...Array(maxWochentag + 1)].map(e => [...Array(maxStunde + 1)].map(e => Array(maxWochentyp + 1).fill(0)));
		for (let iWochentag: number = 0; iWochentag <= maxWochentag; iWochentag++) {
			for (let iStunde: number = 0; iStunde <= maxStunde; iStunde++) {
				for (let iWochentyp: number = 0; iWochentyp <= maxWochentyp; iWochentyp++) {
					this.istWochentagStundeWochentypCounter[iWochentag][iStunde][iWochentyp] = 0;
				}
			}
		}
		this.regeln = new ArrayList();
	}

	public toString(): string {
		return JavaString.format(("UvAlgorithmusDynDatenLehrkraft { interneID=%d, uvID=%d }"), this.interneID, this.uvID);
	}

	/**
	 * Erhöht die Leitung-1-Anzahl der Lehrkraft und aktualisiert den Leitung-1-Malus.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public stateLeitung1Inc(klasse: UvAlgorithmusDynDatenKlasse): void {
		if (this.istLeitung1InKlasse[klasse.interneID]) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s ist bereits Leitung1 in Klasse %s!", this.toString(), klasse.toString()));
			return;
		}
		this.istAnzahlLeitung1++;
		this.istLeitung1InKlasse[klasse.interneID] = true;
	}

	/**
	 * Verringert die Leitung-1-Anzahl der Lehrkraft und aktualisiert den Leitung-1-Malus.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public stateLeitung1Dec(klasse: UvAlgorithmusDynDatenKlasse): void {
		if (!this.istLeitung1InKlasse[klasse.interneID]) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s ist gar nicht Leitung1 in Klasse %s!", this.toString(), klasse.toString()));
			return;
		}
		if (this.istAnzahlLeitung1 <= 0) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s hätte nach dem Entfernen von Leitung1 einen negativen Wert!", this.toString()));
			return;
		}
		this.istAnzahlLeitung1--;
		this.istLeitung1InKlasse[klasse.interneID] = false;
	}

	/**
	 * Erhöht die Leitung-2-Anzahl der Lehrkraft und aktualisiert den Leitung-2-Malus.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public stateLeitung2Inc(klasse: UvAlgorithmusDynDatenKlasse): void {
		if (this.istLeitung2InKlasse[klasse.interneID]) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s ist bereits Leitung2 in Klasse %s!", this.toString(), klasse.toString()));
			return;
		}
		this.istAnzahlLeitung2++;
		this.istLeitung2InKlasse[klasse.interneID] = true;
	}

	/**
	 * Verringert die Leitung-2-Anzahl der Lehrkraft und aktualisiert den Leitung-2-Malus.
	 *
	 * @param klasse   Die {@link UvAlgorithmusDynDatenKlasse}.
	 */
	public stateLeitung2Dec(klasse: UvAlgorithmusDynDatenKlasse): void {
		if (!this.istLeitung2InKlasse[klasse.interneID]) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s ist gar nicht Leitung2 in Klasse %s!", this.toString(), klasse.toString()));
			return;
		}
		if (this.istAnzahlLeitung2 <= 0) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s hätte nach dem Entfernen von Leitung2 einen negativen Wert!", this.toString()));
			return;
		}
		this.istAnzahlLeitung2--;
		this.istLeitung2InKlasse[klasse.interneID] = false;
	}

	/**
	 * Fügt der Lehrkraft eine zugeordnete Lerngruppe hinzu und aktualisiert den Stunden-Malus.
	 *
	 * @param lerngruppe   Die hinzuzufügende Lerngruppe.
	 */
	public stateLerngruppeZugeordnetAdd(lerngruppe: UvAlgorithmusDynDatenLerngruppe): void {
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
			this.istWochentagStundeWochentypCounter[zeitslot.wochentag][zeitslot.stunde][zeitslot.wochentyp]++;
		}
	}

	/**
	 * Entfernt bei der Lehrkraft eine zugeordnete Lerngruppe und aktualisiert den Stunden-Malus.
	 *
	 * @param lerngruppe  Die zu entfernende Lerngruppe.
	 */
	public stateLerngruppeZugeordnetDel(lerngruppe: UvAlgorithmusDynDatenLerngruppe): void {
		const stundenDanach: number = this.istStundensummeLerngruppen - lerngruppe.wochenstundenVorgesehenGekuerzt;
		if (stundenDanach < 0) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s hätte nach dem Entfernen der Lerngruppe %s eine negative Stundenzahl %f!", this.toString(), lerngruppe.toString(), stundenDanach));
			return;
		}
		const lerngruppenAnzahlDanach: number = this.istAnzahlLerngruppen - 1;
		if (lerngruppenAnzahlDanach < 0) {
			this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s hätte nach dem Entfernen der Lerngruppe %s eine negative Lerngruppen-Anzahl %d!", this.toString(), lerngruppe.toString(), lerngruppenAnzahlDanach));
			return;
		}
		for (const klasse of lerngruppe.klassenmenge) {
			if (this.istStundenProKlasse[klasse.interneID] - lerngruppe.wochenstundenVorgesehenGekuerzt < 0) {
				this.log.logLn(LogLevel.ERROR, JavaString.format("Die Lehrkraft %s hätte nach dem Entfernen der Lerngruppe %s in Klasse %s eine negative Stunden-Anzahl!", this.toString(), lerngruppe.toString(), klasse.toString()));
				return;
			}
		}
		this.istStundensummeLerngruppen = stundenDanach;
		this.istAnzahlLerngruppen = lerngruppenAnzahlDanach;
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
		this.istFachZuLerngruppenAnzahl[lerngruppe.fach.interneID]--;
		this.istAnzahlKorrekturen -= lerngruppe.korrekturBelastung;
		for (const zeitslot of lerngruppe.zeitslots) {
			this.istWochentagStundeWochentypCounter[zeitslot.wochentag][zeitslot.stunde][zeitslot.wochentyp]--;
		}
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
