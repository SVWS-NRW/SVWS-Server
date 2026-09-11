import { JavaObject } from '../../../java/lang/JavaObject';
import { UvPlanungsabschnitt } from '../../../core/data/uv/UvPlanungsabschnitt';
import { UvAlgorithmusDynDatenRegel17 } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel17';
import { UvAlgorithmusDynDatenKlasse } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenKlasse';
import { UvAlgorithmusDynDaten, cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDaten } from '../../../core/utils/uvblockung/UvAlgorithmusDynDaten';
import { ArrayList } from '../../../java/util/ArrayList';
import { UvManager } from '../../../core/utils/uv/UvManager';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { Logger } from '../../../core/logger/Logger';
import { UvAlgorithmusDynDatenJahrgang } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenJahrgang';
import { UvAlgorithmusDynDatenFach } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenFach';
import { Random } from '../../../java/util/Random';
import { UvAlgorithmusDynDatenLehrkraft } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenLehrkraft';
import { UvLerngruppe } from '../../../core/data/uv/UvLerngruppe';
import type { List } from '../../../java/util/List';
import type { UvAlgorithmusDynDatenRegel } from '../../../core/utils/uvblockung/UvAlgorithmusDynDatenRegel';
import { Class } from '../../../java/lang/Class';

export class UvAlgorithmusDynDatenLerngruppe extends JavaObject {

	/**
	 * Ein Logger für Debug-Zwecke.
	 */
	readonly log: Logger;

	/**
	 * Ein Random-Objekt für Zufallsentscheidungen bei der Berechnung.
	 */
	readonly rnd: Random;

	/**
	 * Der Index im internen Array.
	 */
	readonly interneID: number;

	/**
	 * Die externe ID der DB/GUI.
	 */
	readonly uvID: number;

	/**
	 * Die vorgesehenen Wochenstunden (ohne Kürzungen).
	 */
	readonly wochenstundenVorgesehenUngekuerzt: number;

	/**
	 * Die vorgesehenen Wochenstunden (mit potentieller Kürzung).
	 */
	wochenstundenVorgesehenGekuerzt: number = 0;

	/**
	 * Wert für der angibt, ob es sich um ein Korrekturfach handelt (1 = ja, 0 = nein).
	 */
	readonly korrekturBelastung: number;

	/**
	 * Menge aller potentiellen Lehrkräfte.
	 */
	readonly lehrkraeftePotentiell: List<UvAlgorithmusDynDatenLehrkraft>;

	/**
	 * Menge aller zugeordneten Lehrkräfte, die nicht fixiert sind. In der Regel eine Person. Sehr selten mehr.
	 */
	readonly lehrkraefteZugeordnet: List<UvAlgorithmusDynDatenLehrkraft>;

	/**
	 * Menge aller zugeordneten Lehrkräfte, die fixiert sind. In der Regel eine Person. Sehr selten mehr.
	 */
	readonly lehrkraefteZugeordnetFixiert: List<UvAlgorithmusDynDatenLehrkraft>;

	/**
	 * Menge aller zugeordneten Lehrkräfte.
	 */
	readonly lehrkraefteZugeordnetAlle: List<UvAlgorithmusDynDatenLehrkraft>;

	/**
	 * Die Klassen dieser Lerngruppe (in der Regel 1).
	 */
	readonly klassenmenge: List<UvAlgorithmusDynDatenKlasse>;

	/**
	 * Die Jahrgänge dieser Lerngruppe (in der Regel 1).
	 */
	readonly jahrgangmenge: List<UvAlgorithmusDynDatenJahrgang>;

	/**
	 * Alle Zeitslots denen diese Lerngruppe zugeordnet ist (Regel 17).
	 */
	readonly zeitslots: List<UvAlgorithmusDynDatenRegel17>;

	/**
	 * Die Menge aller Regel-Objekte die sich auf diese Lerngruppe beziehen.
	 */
	readonly regeln: List<UvAlgorithmusDynDatenRegel>;

	/**
	 * Das Fach dieser Lerngruppe.
	 */
	readonly fach: UvAlgorithmusDynDatenFach;


	/**
	 * Der Konstruktor.
	 *
	 * @param index               Der Index, unter dem dieses Objekt im Array der aufrufenden Klasse gespeichert ist.
	 * @param parent              Das Elternobjekt.
	 * @param man                 Der {@link UvManager}.
	 * @param planungsabschnitt   Der {@link UvPlanungsabschnitt} auf den sich die Berechnung bezieht.
	 * @param klassen             Die {@link UvAlgorithmusDynDatenKlasse}-Menge dieser Lerngruppe.
	 * @param jahrgaenge          Die {@link UvAlgorithmusDynDatenJahrgang}-Menge dieser Lerngruppe.
	 */
	public constructor(index: number, parent: UvAlgorithmusDynDaten, man: UvManager, planungsabschnitt: UvPlanungsabschnitt, klassen: List<UvAlgorithmusDynDatenKlasse>, jahrgaenge: List<UvAlgorithmusDynDatenJahrgang>) {
		super();
		const lerngruppenmenge: List<UvLerngruppe> = man.lerngruppeGetMengeByPlanungsabschnitt(planungsabschnitt);
		const lerngruppe: UvLerngruppe = lerngruppenmenge.get(index);
		this.fach = parent.gibDynFachByLerngruppe(lerngruppe);
		this.log = parent.gibLogger();
		this.rnd = parent.gibRandom();
		this.interneID = index;
		this.uvID = lerngruppe.id;
		this.klassenmenge = new ArrayList(klassen);
		this.jahrgangmenge = new ArrayList(jahrgaenge);
		this.lehrkraeftePotentiell = new ArrayList();
		this.lehrkraefteZugeordnet = new ArrayList();
		this.lehrkraefteZugeordnetFixiert = new ArrayList();
		this.lehrkraefteZugeordnetAlle = new ArrayList();
		this.wochenstundenVorgesehenUngekuerzt = lerngruppe.wochenstunden;
		this.wochenstundenVorgesehenGekuerzt = lerngruppe.wochenstunden;
		this.korrekturBelastung = man.lerngruppeIstKorrekturfach(lerngruppe) ? 1 : 0;
		this.zeitslots = new ArrayList();
		this.regeln = new ArrayList();
	}

	public toString(): string {
		return JavaString.format(("UvAlgorithmusDynDatenLerngruppe { interneID=%d, uvID=%d }"), this.interneID, this.uvID);
	}

	/**
	 * Liefert true, falls die Lehrkraft der Lerngruppe zugeordnet ist.
	 * Hinweis: Der Vergleich der Objektreferenzen ist hier korrekt und effizienter als "equals".
	 *
	 * @param lehrkraft   Die Lehrkraft, die überprüft wird.
	 *
	 * @return true, falls die Lehrkraft der Lerngruppe zugeordnet ist.
	 */
	gibIstLehrkraftZugeordnet(lehrkraft: UvAlgorithmusDynDatenLehrkraft): boolean {
		return this.lehrkraefteZugeordnet.contains(lehrkraft) || this.lehrkraefteZugeordnetFixiert.contains(lehrkraft);
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft fixiert ist.
	 *
	 * @param lehrkraft   Die angefragte {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft fixiert ist.
	 */
	gibIstLehrkraftFixiert(lehrkraft: UvAlgorithmusDynDatenLehrkraft): boolean {
		return this.lehrkraefteZugeordnetFixiert.contains(lehrkraft);
	}

	/**
	 * Liefert TRUE, falls die Lehrkraft potentiell diese Lerngruppe unterrichten darf.
	 *
	 * @param lehrkraft   Die angefragte {@link UvAlgorithmusDynDatenLehrkraft}.
	 *
	 * @return TRUE, falls die Lehrkraft potentiell diese Lerngruppe unterrichten darf.
	 */
	gibIstLehrkraftPotentiell(lehrkraft: UvAlgorithmusDynDatenLehrkraft): boolean {
		return this.lehrkraeftePotentiell.contains(lehrkraft);
	}

	/**
	 * Liefert eine potentielle Lehrkraft, welche dieser Lerngruppe noch nicht zugeordnet ist.
	 * <br>Hinweis: Eine Optimierung lohnt sich hier nicht,
	 *              denn die zugeordneten Lehrkräfte sind fast immer 0 oder 1.
	 *
	 * @return eine potentielle Lehrkraft, welche dieser Lerngruppe noch nicht zugeordnet ist.
	 */
	gibLehrkraftPotentiellZufaelligOderNull(): UvAlgorithmusDynDatenLehrkraft | null {
		const size: number = this.lehrkraeftePotentiell.size();
		if (size === 0) {
			return null;
		}
		const lehrkraft: UvAlgorithmusDynDatenLehrkraft = this.lehrkraeftePotentiell.get(this.rnd.nextInt(size));
		if (this.lehrkraefteZugeordnet.contains(lehrkraft)) {
			return null;
		}
		if (this.lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
			return null;
		}
		return lehrkraft;
	}

	/**
	 * Liefert eine zufällige derzeit zugeordnete Lehrkraft die nicht fixiert ist, oder null.
	 *
	 * @return eine zufällige derzeit zugeordnete Lehrkraft die nicht fixiert ist, oder null.
	 */
	gibLehrkraftZugeordnetAberNichtFixiertZufaellig(): UvAlgorithmusDynDatenLehrkraft | null {
		const size: number = this.lehrkraefteZugeordnet.size();
		if (size === 0) {
			return null;
		}
		return this.lehrkraefteZugeordnet.get(this.rnd.nextInt(size));
	}

	/**
	 * Fügt die {@link UvAlgorithmusDynDatenLehrkraft} der potentiellen Menge dieser Lerngruppe hinzu.
	 *
	 * @param lehrkraft   Die Lehrkraft, welche hinzugefügt wird.
	 */
	fuegeLehrkraftHinzuf(lehrkraft: UvAlgorithmusDynDatenLehrkraft): void {
		if (this.lehrkraeftePotentiell.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Lehrkraft %s Duplikat bei der potentiellen Menge!", lehrkraft.toString()));
		}
		this.lehrkraeftePotentiell.add(lehrkraft);
	}

	/**
	 * Entfernt die Lehrkraft aus der Menge der potentiellen Lehrkräfte.
	 *
	 * @param lehrkraft   Die zu entfernende Lehrkraft.
	 */
	entferneLehrkraft(lehrkraft: UvAlgorithmusDynDatenLehrkraft): void {
		if (this.lehrkraefteZugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Lehrkraft %s soll in Lerngruppe verboten werden, ist aber bereits zugeordnet!", lehrkraft.toString()));
		}
		if (this.lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Lehrkraft %s soll in Lerngruppe verboten werden, ist aber bereits fixiert!", lehrkraft.toString()));
		}
		this.lehrkraeftePotentiell.remove(lehrkraft);
	}

	/**
	 * Entfernt die Lehrkraft aus der Menge der potentiellen Lehrkräfte, falls diese Lerngruppe die Klasse tangiert.
	 *
	 * @param lehrkraft   Die zu entfernende Lehrkraft.
	 * @param klasse      Die verbotene Klasse.
	 */
	entferneLehrkraftWennKlasseUebereinstimmt(lehrkraft: UvAlgorithmusDynDatenLehrkraft, klasse: UvAlgorithmusDynDatenKlasse): void {
		if (this.lehrkraefteZugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Lehrkraft %s soll verboten werden, ist aber bereits zugeordnet!", lehrkraft.toString()));
		}
		if (this.lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Lehrkraft %s soll verboten werden, ist aber bereits fixiert!", lehrkraft.toString()));
		}
		if (this.klassenmenge.contains(klasse)) {
			this.lehrkraeftePotentiell.remove(lehrkraft);
		}
	}

	/**
	 * Entfernt die Lehrkraft aus der Menge der potentiellen Lehrkräfte, falls diese Lerngruppe die Stufe tangiert.
	 *
	 * @param lehrkraft   Die zu entfernende Lehrkraft.
	 * @param jahrgang    Der verbotene Jahrgang.
	 */
	entferneLehrkraftWennStufeUebereinstimmt(lehrkraft: UvAlgorithmusDynDatenLehrkraft, jahrgang: UvAlgorithmusDynDatenJahrgang): void {
		if (this.lehrkraefteZugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Lehrkraft %s soll in Stufe verboten werden, ist aber bereits zugeordnet!", lehrkraft.toString()));
		}
		if (this.lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Lehrkraft %s soll in Stufe verboten werden, ist aber bereits fixiert!", lehrkraft.toString()));
		}
		if (this.jahrgangmenge.contains(jahrgang)) {
			this.lehrkraeftePotentiell.remove(lehrkraft);
		}
	}

	/**
	 * Entfernt die Lehrkraft aus der Menge der potentiellen Lehrkräfte, falls das Fach und die Lerngruppe übereinstimmen.
	 *
	 * @param lehrkraft    Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fach         Das {@link UvAlgorithmusDynDatenFach}.
	 * @param jahrgaenge   Die {@link UvAlgorithmusDynDatenJahrgang}-Menge.
	 */
	entferneLehrkraftWennJahrgangUndFachUebereinstimmt(lehrkraft: UvAlgorithmusDynDatenLehrkraft, fach: UvAlgorithmusDynDatenFach, jahrgaenge: List<UvAlgorithmusDynDatenJahrgang>): void {
		if (this.fach as unknown === fach as unknown) {
			for (const jahrgang of jahrgaenge) {
				if (this.jahrgangmenge.contains(jahrgang)) {
					if (this.lehrkraefteZugeordnet.contains(lehrkraft)) {
						throw new DeveloperNotificationException(JavaString.format("Regel 24: Lehrkraft %s soll in Stufe verboten werden, ist aber bereits zugeordnet!", lehrkraft.toString()));
					}
					if (this.lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
						throw new DeveloperNotificationException(JavaString.format("Regel 24: Lehrkraft %s soll in Stufe verboten werden, ist aber bereits fixiert!", lehrkraft.toString()));
					}
					this.lehrkraeftePotentiell.remove(lehrkraft);
					return;
				}
			}
		}
	}

	/**
	 * Entfernt die Lehrkraft aus der Menge der potentiellen Lehrkräfte, falls diese Lerngruppe das entsprechende Fach repräsentiert.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fach        Das {@link UvAlgorithmusDynDatenFach}.
	 */
	entferneLehrkraftWennFachUebereinstimmt(lehrkraft: UvAlgorithmusDynDatenLehrkraft, fach: UvAlgorithmusDynDatenFach): void {
		if (this.fach as unknown === fach as unknown) {
			if (this.lehrkraefteZugeordnet.contains(lehrkraft)) {
				throw new DeveloperNotificationException(JavaString.format("Regel 25: Lehrkraft %s soll in Stufe verboten werden, ist aber bereits zugeordnet!", lehrkraft.toString()));
			}
			if (this.lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
				throw new DeveloperNotificationException(JavaString.format("Regel 25: Lehrkraft %s soll in Stufe verboten werden, ist aber bereits fixiert!", lehrkraft.toString()));
			}
			this.lehrkraeftePotentiell.remove(lehrkraft);
		}
	}

	/**
	 * Fügt dieser Lerngruppe eine Lehrkraft hinzu.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 * @param fixiert     Falls true, wird die Lehrkraft der Liste aller fixierten Lehrkräfte zugeordnet, sonst der normalen Liste.
	 */
	stateLehrkraftZugeordnetAdd(lehrkraft: UvAlgorithmusDynDatenLehrkraft, fixiert: boolean): void {
		if (this.lehrkraefteZugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Die Lerngruppe %s hat bereits die zugeordnete Lehrkraft %s!", this.toString(), lehrkraft.toString()));
		}
		if (this.lehrkraefteZugeordnetFixiert.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Die Lerngruppe %s hat bereits die fixierte Lehrkraft %s!", this.toString(), lehrkraft.toString()));
		}
		if (fixiert) {
			this.lehrkraefteZugeordnetFixiert.add(lehrkraft);
		} else {
			this.lehrkraefteZugeordnet.add(lehrkraft);
		}
		this.lehrkraefteZugeordnetAlle.add(lehrkraft);
	}

	/**
	 * Entfernt eine zuvor hinzugefügte Lehrkraft aus dieser Lerngruppe.
	 *
	 * @param lehrkraft   Die {@link UvAlgorithmusDynDatenLehrkraft}.
	 */
	stateLehrkraftZugeordnetDel(lehrkraft: UvAlgorithmusDynDatenLehrkraft): void {
		if (!this.lehrkraefteZugeordnet.contains(lehrkraft)) {
			throw new DeveloperNotificationException(JavaString.format("Die Lerngruppe %s hat keine zugeordnete Lehrkraft %s!", this.toString(), lehrkraft.toString()));
		}
		this.lehrkraefteZugeordnet.remove(lehrkraft);
		this.lehrkraefteZugeordnetAlle.remove(lehrkraft);
	}

	/**
	 * Ändert den Stunden-SOLL dieser Lerngruppe.
	 *
	 * @param neuerSoll   Der neue Wert.
	 */
	public setzeStundenSollAuf(neuerSoll: number): void {
		this.wochenstundenVorgesehenGekuerzt = neuerSoll;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenLerngruppe';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenLerngruppe'].includes(name);
	}

	public static readonly class = new Class<UvAlgorithmusDynDatenLerngruppe>('de.svws_nrw.core.utils.uvblockung.UvAlgorithmusDynDatenLerngruppe');

}

export function cast_de_svws_nrw_core_utils_uvblockung_UvAlgorithmusDynDatenLerngruppe(obj: unknown): UvAlgorithmusDynDatenLerngruppe {
	return obj as UvAlgorithmusDynDatenLerngruppe;
}
