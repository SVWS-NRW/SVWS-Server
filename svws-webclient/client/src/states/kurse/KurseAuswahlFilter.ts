import { computed, type ComputedRef } from "vue";

import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { KursUtils } from "@core/core/utils/kurse/KursUtils";
import { LehrerUtils } from "@core/core/utils/lehrer/LehrerUtils";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import type { SchuleState } from "@ui/states/SchuleState";
import { JahrgaengeListeManager } from "@ui/ui/manager/kataloge/JahrgaengeListeManager";

import type { AuswahlKatalog } from "../AuswahlKatalog";
import { getSchulgliederungen } from "../AuswahlKatalogUtils";
import { GenericAuswahlFilter } from "../GenericAuswahlFilter";

/** Die Filterwerte der Auswahlliste der Kurse */
export interface KurseAuswahlFilterValues {
	/** Die IDs der gewählten Jahrgänge */
	jahrgaenge: JavaSet<number>;
	/** Die IDs der gewählten Lehrer */
	lehrer: JavaSet<number>;
	/** Die IDs der gewählten Fächer */
	faecher: JavaSet<number>;
	/** Die IDs der gewählten Schüler */
	schueler: JavaSet<number>;
	/** Die Kürzel der gewählten Schulgliederungen */
	schulgliederungen: JavaSet<string>;
	/** Gibt an, ob nur sichtbare Kurse angezeigt werden */
	nurSichtbar: boolean;
}

/** Die Optionen der Filterfelder der Auswahlliste der Kurse */
export interface KurseAuswahlFilterOptions {
	jahrgaenge: List<JahrgangsDaten>;
	lehrer: List<LehrerListeEintrag>;
	faecher: List<FachDaten>;
	schueler: List<SchuelerListeEintrag>;
	schulgliederungen: List<Schulgliederung>;
	/** Das Schuljahr, für welches die Kürzel der Schulgliederungen bestimmt werden */
	schuljahr: number;
}

/** Die Quellen, aus denen der Filter der Kurse seine Optionen erzeugt */
export interface KurseAuswahlFilterSources {
	/** Der State der Schule mit Schulform, Schuljahr und aktuellem Abschnitt */
	schule: SchuleState;
	/** Der Katalog der Jahrgänge */
	jahrgaenge: AuswahlKatalog<JahrgangsDaten>;
	/** Der Katalog der Lehrer */
	lehrer: AuswahlKatalog<LehrerListeEintrag>;
	/** Der Katalog der Fächer */
	faecher: AuswahlKatalog<FachDaten>;
	/** Der Katalog der Schüler */
	schueler: AuswahlKatalog<SchuelerListeEintrag>;
}

/**
 * Gibt den Schlüssel einer Schulgliederung im Filter zurück.
 *
 * @param schulgliederung   die Schulgliederung
 * @param schuljahr         das Schuljahr
 *
 * @returns der Schlüssel (das Kürzel der Schulgliederung im Schuljahr)
 */
export function kurseFilterSchulgliederungKey(schulgliederung: Schulgliederung, schuljahr: number): string {
	return schulgliederung.daten(schuljahr)?.kuerzel ?? '';
}

/**
 * Übernimmt aus den bisherigen Werten nur die Werte, für die es unter den Optionen noch einen Eintrag gibt.
 *
 * @param values    die bisherigen Werte
 * @param options   die neuen Optionen
 * @param toKey     die Funktion, welche den Wert zu einer Option liefert
 *
 * @returns die bereinigten Werte
 */
function retain<K, O>(values: JavaSet<K>, options: Iterable<O>, toKey: (option: O) => K): JavaSet<K> {
	const result = new HashSet<K>();
	for (const option of options) {
		const key = toKey(option);
		if (values.contains(key)) {
			result.add(key);
		}
	}
	return result;
}

/**
 * Der Filter für die Auswahlliste der Kurse
 */
export class KurseAuswahlFilter extends GenericAuswahlFilter<KursDaten, KurseAuswahlFilterValues, KurseAuswahlFilterOptions> {

	/** Das Model für das Auswahlfeld nurSichtbar */
	private readonly _nurSichtbarModel = this.valueModel("nurSichtbar");

	/** Das Model für das Auswahlfeld jahrgaenge */
	private readonly _jahrgaengeModel = this.selectionModel("jahrgaenge", () => this.options.jahrgaenge, j => j.id);

	/** Das Model für das Auswahlfeld faecher */
	private readonly _faecherModel = this.selectionModel("faecher", () => this.options.faecher, f => f.id);

	/** Das Model für das Auswahlfeld lehrer */
	private readonly _lehrerModel = this.selectionModel("lehrer", () => this.options.lehrer, l => l.id);

	/** Das Model für das Auswahlfeld schueler */
	private readonly _schuelerModel = this.selectionModel("schueler", () => this.options.schueler, s => s.id);

	/** Das Model für das Auswahlfeld schulgliederungen */
	private readonly _schulgliederungenModel = this.selectionModel("schulgliederungen", () => this.options.schulgliederungen, sg => kurseFilterSchulgliederungKey(sg, this.options.schuljahr));

	/** Die Quellen für die Optionen der Filterfelder oder null */
	private readonly _sources: KurseAuswahlFilterSources | null;

	/** Die Lehrer der Optionen zugeordnet zu deren ID */
	private readonly _lehrerById: ComputedRef<Map<number, LehrerListeEintrag>> = computed(() => {
		const map = new Map<number, LehrerListeEintrag>();
		for (const l of this.options.lehrer) {
			map.set(l.id, l);
		}
		return map;
	});

	/** Die Jahrgänge der Optionen zugeordnet zu deren ID */
	private readonly _jahrgangById: ComputedRef<Map<number, JahrgangsDaten>> = computed(() => {
		const map = new Map<number, JahrgangsDaten>();
		for (const j of this.options.jahrgaenge) {
			map.set(j.id, j);
		}
		return map;
	});

	/**
	 * Erzeugt einen neuen Filter für die Kurse.
	 *
	 * @param entries      die Funktion, welche die Einträge der Kursliste liefert
	 * @param idSelected   die Funktion, welche die ID des aktuell gewählten Kurses oder null liefert
	 * @param sources      die Quellen für die Optionen der Filterfelder (ohne Quellen sind die Optionen leer)
	 */
	public constructor(entries: () => Iterable<KursDaten>, idSelected: () => number | null, sources: KurseAuswahlFilterSources | null = null) {
		super({
			jahrgaenge: new HashSet<number>(), lehrer: new HashSet<number>(), faecher: new HashSet<number>(),
			schueler: new HashSet<number>(), schulgliederungen: new HashSet<string>(), nurSichtbar: true,
		}, entries, idSelected, [{ field: "idJahrgaenge", ascending: true }, { field: "kuerzel", ascending: true }]);
		this._sources = sources;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: nurSichtbar */
	public get nurSichtbarModel(): boolean {
		return this._nurSichtbarModel.value;
	}

	public set nurSichtbarModel(value: boolean) {
		this._nurSichtbarModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: jahrgaenge */
	public get jahrgaengeModel(): JahrgangsDaten[] {
		return this._jahrgaengeModel.value;
	}

	public set jahrgaengeModel(value: JahrgangsDaten[]) {
		this._jahrgaengeModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: faecher */
	public get faecherModel(): FachDaten[] {
		return this._faecherModel.value;
	}

	public set faecherModel(value: FachDaten[]) {
		this._faecherModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: lehrer */
	public get lehrerModel(): LehrerListeEintrag[] {
		return this._lehrerModel.value;
	}

	public set lehrerModel(value: LehrerListeEintrag[]) {
		this._lehrerModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: schueler */
	public get schuelerModel(): SchuelerListeEintrag[] {
		return this._schuelerModel.value;
	}

	public set schuelerModel(value: SchuelerListeEintrag[]) {
		this._schuelerModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: schulgliederungen */
	public get schulgliederungenModel(): Schulgliederung[] {
		return this._schulgliederungenModel.value;
	}

	public set schulgliederungenModel(value: Schulgliederung[]) {
		this._schulgliederungenModel.value = value;
	}

	protected createOptions(): KurseAuswahlFilterOptions {
		if (this._sources === null) {
			return {
				jahrgaenge: new ArrayList<JahrgangsDaten>(), lehrer: new ArrayList<LehrerListeEintrag>(), faecher: new ArrayList<FachDaten>(),
				schueler: new ArrayList<SchuelerListeEintrag>(), schulgliederungen: new ArrayList<Schulgliederung>(), schuljahr: 0,
			};
		}
		const schuleState = this._sources.schule;
		return {
			jahrgaenge: this._sources.jahrgaenge.list(),
			lehrer: this._sources.lehrer.list(),
			faecher: this._sources.faecher.list(),
			schueler: this._sources.schueler.list(),
			schulgliederungen: getSchulgliederungen(schuleState.schulform, schuleState.schuljahr),
			schuljahr: schuleState.schuljahr,
		};
	}

	protected checkPassFilter(entry: KursDaten, values: KurseAuswahlFilterValues, options: KurseAuswahlFilterOptions): boolean {
		if (values.nurSichtbar && !entry.istSichtbar) {
			return false;
		}
		if (!values.faecher.isEmpty() && !values.faecher.contains(entry.idFach)) {
			return false;
		}
		if (!values.jahrgaenge.isEmpty()) {
			let hatEinenJahrgang = false;
			for (const idJahrgang of entry.idJahrgaenge) {
				if (values.jahrgaenge.contains(idJahrgang)) {
					hatEinenJahrgang = true;
				}
			}
			if (!hatEinenJahrgang) {
				return false;
			}
		}
		if (!values.lehrer.isEmpty() && ((entry.idLehrer === null) || !values.lehrer.contains(entry.idLehrer))) {
			return false;
		}
		if (!values.schueler.isEmpty()) {
			let hatEinenSchueler = false;
			for (const s of entry.schueler) {
				if (values.schueler.contains(s.id)) {
					hatEinenSchueler = true;
				}
			}
			if (!hatEinenSchueler) {
				return false;
			}
		}
		if (!values.schulgliederungen.isEmpty()) {
			let hatEineSchulgliederung = false;
			for (const idJahrgang of entry.idJahrgaenge) {
				const j = this._jahrgangById.value.get(idJahrgang);
				if ((j === undefined) || (j.idSchulgliederung === null)) {
					continue;
				}
				if (values.schulgliederungen.contains(Schulgliederung.data().getEintragByID(j.idSchulgliederung)?.kuerzel ?? '')) {
					hatEineSchulgliederung = true;
					break;
				}
			}
			if (!hatEineSchulgliederung) {
				return false;
			}
		}
		return true;
	}

	protected checkPassSearch(entry: KursDaten, search: string): boolean {
		return entry.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase());
	}

	protected compare(a: KursDaten, b: KursDaten): number {
		for (const { field, ascending } of this._state.value.order) {
			let cmp: number;
			if (field === "kuerzel") {
				cmp = KursUtils.comparator.compare(a, b);
			} else if (field === "idLehrer") {
				cmp = this.compareLehrer(a, b);
			} else if (field === "idJahrgaenge") {
				cmp = this.compareJahrgang(a, b);
			} else if (field === "schueler") {
				cmp = JavaInteger.compare(a.schueler.size(), b.schueler.size());
			} else {
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Filter nicht unterstützt.");
			}
			if (cmp !== 0) {
				return ascending ? cmp : -cmp;
			}
		}
		return JavaLong.compare(a.id, b.id);
	}

	private compareLehrer(a: KursDaten, b: KursDaten): number {
		if ((a.idLehrer === null) && (b.idLehrer === null)) {
			return 0;
		} else if (a.idLehrer === null) {
			return -1;
		} else if (b.idLehrer === null) {
			return 1;
		}
		const la = this._lehrerById.value.get(a.idLehrer);
		const lb = this._lehrerById.value.get(b.idLehrer);
		if ((la === undefined) && (lb === undefined)) {
			return 0;
		} else if (la === undefined) {
			return -1;
		} else if (lb === undefined) {
			return 1;
		}
		return LehrerUtils.comparator.compare(la, lb);
	}

	private compareJahrgang(a: KursDaten, b: KursDaten): number {
		if (a.idJahrgaenge.isEmpty() && b.idJahrgaenge.isEmpty()) {
			return 0;
		} else if (a.idJahrgaenge.isEmpty()) {
			return -1;
		} else if (b.idJahrgaenge.isEmpty()) {
			return 1;
		}
		const ja = this._jahrgangById.value.get(a.idJahrgaenge.get(0));
		const jb = this._jahrgangById.value.get(b.idJahrgaenge.get(0));
		if ((ja === undefined) && (jb === undefined)) {
			return 0;
		} else if (ja === undefined) {
			return -1;
		} else if (jb === undefined) {
			return 1;
		}
		return JahrgaengeListeManager.comparator.compare(ja, jb);
	}

	protected hasFilter(values: KurseAuswahlFilterValues): boolean {
		return !values.jahrgaenge.isEmpty() || !values.lehrer.isEmpty() || !values.faecher.isEmpty() || !values.schueler.isEmpty() || !values.schulgliederungen.isEmpty();
	}

	protected cleanValues(values: KurseAuswahlFilterValues, options: KurseAuswahlFilterOptions): KurseAuswahlFilterValues {
		return {
			jahrgaenge: retain(values.jahrgaenge, options.jahrgaenge, j => j.id),
			lehrer: retain(values.lehrer, options.lehrer, l => l.id),
			faecher: retain(values.faecher, options.faecher, f => f.id),
			schueler: retain(values.schueler, options.schueler, s => s.id),
			schulgliederungen: retain(values.schulgliederungen, options.schulgliederungen, sg => kurseFilterSchulgliederungKey(sg, options.schuljahr)),
			nurSichtbar: values.nurSichtbar,
		};
	}

}
