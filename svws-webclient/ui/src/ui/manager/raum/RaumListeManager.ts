import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import type { Raum } from "@core/core/data/schule/Raum";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { RaumUtils } from "@core/core/utils/raum/RaumUtils";
import { Class } from "@core/java/lang/Class";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaObject } from "@core/java/lang/JavaObject";
import { JavaString } from "@core/java/lang/JavaString";
import { HashMap } from "@core/java/util/HashMap";
import type { List } from "@core/java/util/List";
import { AuswahlManager } from "../AuswahlManager";

export class RaumListeManager extends AuswahlManager<number, Raum, Raum> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static readonly _raumToId = (f: Raum) => f.id;

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste verwendet werden können
	 */
	private readonly _mapRaumByKuerzel: HashMap<string, Raum> = new HashMap<string, Raum>();


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schuljahresabschnitt    		der Schuljahresabschnitt, auf den sich die Auswahl bezieht
	 * @param schuljahresabschnitte			die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnittSchule	der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform						die Schulform der Schule
	 * @param raeume						die Liste der Raum
	 */
	public constructor(schuljahresabschnitt: number, schuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, raeume: List<Raum>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, raeume, RaumUtils.comparator, RaumListeManager._raumToId, RaumListeManager._raumToId, []);
		this.onMehrfachauswahlChanged();
	}

	protected onMehrfachauswahlChanged(): void {
		this._mapRaumByKuerzel.clear();
		for (const f of this.liste.list()) {
			this._mapRaumByKuerzel.put(f.kuerzel, f);
		}
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag   der Auswahl-Eintrag
	 * @param daten     das neue Daten-Objekt zu der Auswahl
	 */
	protected onSetDaten(eintrag: Raum, daten: Raum): boolean {
		let updateEintrag: boolean = false;
		if (!JavaObject.equalsTranspiler(daten.kuerzel, (eintrag.kuerzel))) {
			eintrag.kuerzel = daten.kuerzel;
			updateEintrag = true;
		}
		if (!JavaObject.equalsTranspiler(daten.beschreibung, (eintrag.beschreibung))) {
			eintrag.beschreibung = daten.beschreibung;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Vergleicht zwei Raumlisteneinträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: Raum, b: Raum): number {
		for (const { field, ascending } of this._order) {
			let cmp: number;

			if (field === "kuerzel") {
				cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
			} else if (field === "beschreibung") {
				cmp = JavaString.compareTo(a.beschreibung, b.beschreibung);
			} else if (field === "groesse") {
				cmp = JavaLong.compare(a.groesse, b.groesse);
			} else {
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Manager nicht unterstützt.");
			}

			if (cmp === 0) {
				continue;
			}

			return ascending ? cmp : -cmp;
		}

		return RaumUtils.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: Raum): boolean {
		return true;
	}

	/**
	 * Gibt die Raum anhand des übergebenen Kürzels zurück.
	 * Ist das Kürzel ungültig, so wird null zurückgegeben.
	 *
	 * @param kuerzel  das Kürzel
	 *
	 * @return die Raum oder null
	 */
	public getByKuerzelOrNull(kuerzel: string): Raum | null {
		return this._mapRaumByKuerzel.get(kuerzel);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.raum.RaumListeManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager', 'de.svws_nrw.core.utils.raum.RaumListeManager'].includes(name);
	}

	public static readonly class = new Class<RaumListeManager>('de.svws_nrw.core.utils.raum.RaumListeManager');

}

export function cast_de_svws_nrw_core_utils_raum_RaumListeManager(obj: unknown): RaumListeManager {
	return obj as RaumListeManager;
}
