import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import { ArrayList } from "@core/java/util/ArrayList";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import type { SchuleState } from "@ui/states/SchuleState";

import type { AuswahlKatalog } from "../AuswahlKatalog";
import { getSchulgliederungen } from "../AuswahlKatalogUtils";
import { GenericAuswahlFilter } from "../GenericAuswahlFilter";


/** Die Filterwerte der Auswahlliste der Klassen */
export interface KlassenAuswahlFilterValues {
	/** Die IDs der gewählten Jahrgänge */
	jahrgaenge: JavaSet<number>;
	/** Die IDs der gewählten Lehrer (Klassenleitungen) */
	lehrer: JavaSet<number>;
	/** Die Kürzel der gewählten Schulgliederungen */
	schulgliederungen: JavaSet<string>;
}

/** Die Optionen der Filterfelder der Auswahlliste der Klassen */
export interface KlassenAuswahlFilterOptions {
	jahrgaenge: List<JahrgangsDaten>;
	lehrer: List<LehrerListeEintrag>;
	schulgliederungen: List<Schulgliederung>;
	/** Das Schuljahr, für welches die Kürzel der Schulgliederungen bestimmt werden */
	schuljahr: number;
}

/** Die Quellen, aus denen der Filter der Klassen seine Optionen erzeugt */
export interface KlassenAuswahlFilterSources {
	/** Der State der Schule mit Schulform, Schuljahr und aktuellem Abschnitt */
	schule: SchuleState;
	/** Der Katalog der Jahrgänge */
	jahrgaenge: AuswahlKatalog<JahrgangsDaten>;
	/** Der Katalog der Lehrer */
	lehrer: AuswahlKatalog<LehrerListeEintrag>;
}

/**
 * Gibt den Schlüssel einer Schulgliederung im Filter zurück.
 *
 * @param schulgliederung   die Schulgliederung
 * @param schuljahr         das Schuljahr
 *
 * @returns der Schlüssel (das Kürzel der Schulgliederung im Schuljahr)
 */
export function klassenFilterSchulgliederungKey(schulgliederung: Schulgliederung, schuljahr: number): string {
	return schulgliederung.daten(schuljahr)?.kuerzel ?? '';
}

/**
 * Prüft, ob ein Eintrag der Klassenliste den Filter passiert.
 *
 * @param entry      der Eintrag der Klassenliste
 * @param values     die Filterwerte
 * @param jahrgang   die Funktion, welche zu einer Jahrgangs-ID die Jahrgangsdaten liefert oder null
 *
 * @returns true, wenn der Eintrag den Filter passiert
 */
export function klassenCheckPassFilter(entry: KlassenListeEintrag, values: KlassenAuswahlFilterValues, jahrgang: (id: number) => JahrgangsDaten | null): boolean {
	if ((values.jahrgaenge.size() > 0) && ((entry.idJahrgang === null) || (!values.jahrgaenge.contains(entry.idJahrgang)))) {
		return false;
	}
	if (values.lehrer.size() > 0) {
		let hatEinenLehrer = false;
		for (const idLehrer of entry.idsKlassenleitungen) {
			if (values.lehrer.contains(idLehrer)) {
				hatEinenLehrer = true;
			}
		}
		if (!hatEinenLehrer) {
			return false;
		}
	}
	if (values.schulgliederungen.size() > 0) {
		if (entry.idJahrgang === null) {
			return false;
		}
		const j = jahrgang(entry.idJahrgang);
		if (j === null) {
			throw new DeveloperNotificationException("Kein gültiger Schlüsselwert.");
		}
		if (j.idSchulgliederung === null) {
			return false;
		}
		if (!values.schulgliederungen.contains(Schulgliederung.data().getEintragByID(j.idSchulgliederung)?.kuerzel ?? '')) {
			return false;
		}
	}
	return true;
}

/**
 * Der Comparator für den Vergleich von Einträgen der Klassenliste: zuerst nach Kürzel, dann nach ID.
 */
export const comparatorKlassenListeEintrag: Comparator<KlassenListeEintrag> = {
	compare: (a: KlassenListeEintrag, b: KlassenListeEintrag) => {
		if (a.kuerzel !== null && b.kuerzel !== null) {
			const cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
			if (cmp !== 0) {
				return cmp;
			}
		}
		return JavaLong.compare(a.id, b.id);
	},
};

/**
 * Der Filter für die Auswahlliste der Klassen
 */
export class KlassenAuswahlFilter extends GenericAuswahlFilter<KlassenListeEintrag, KlassenAuswahlFilterValues, KlassenAuswahlFilterOptions> {

	/** Das Model für das Auswahlfeld jahrgaenge */
	private readonly _jahrgaengeModel = this.selectionModel("jahrgaenge", () => this.options.jahrgaenge, j => j.id);

	/** Das Model für das Auswahlfeld lehrer */
	private readonly _lehrerModel = this.selectionModel("lehrer", () => this.options.lehrer, l => l.id);

	/** Das Model für das Auswahlfeld schulgliederungen */
	private readonly _schulgliederungenModel = this.selectionModel("schulgliederungen", () => this.options.schulgliederungen, sg => klassenFilterSchulgliederungKey(sg, this.options.schuljahr));

	/** Die Quellen für die Optionen der Filterfelder oder null */
	private readonly _sources: KlassenAuswahlFilterSources | null;

	/**
	 * Erzeugt einen neuen Filter für die Klassen.
	 *
	 * @param entries      die Funktion, welche die Einträge der Klassenliste liefert
	 * @param idSelected   die Funktion, welche die ID der aktuell gewählten Klasse oder null liefert
	 * @param sources      die Quellen für die Optionen der Filterfelder (ohne Quellen sind die Optionen leer)
	 */
	public constructor(entries: () => Iterable<KlassenListeEintrag>, idSelected: () => number | null, sources: KlassenAuswahlFilterSources | null = null) {
		super({ jahrgaenge: new HashSet<number>(), lehrer: new HashSet<number>(), schulgliederungen: new HashSet<string>() }, entries, idSelected);
		this._sources = sources;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: jahrgaenge */
	public get jahrgaengeModel(): JahrgangsDaten[] {
		return this._jahrgaengeModel.value;
	}

	public set jahrgaengeModel(value: JahrgangsDaten[]) {
		this._jahrgaengeModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: lehrer */
	public get lehrerModel(): LehrerListeEintrag[] {
		return this._lehrerModel.value;
	}

	public set lehrerModel(value: LehrerListeEintrag[]) {
		this._lehrerModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: schulgliederungen */
	public get schulgliederungenModel(): Schulgliederung[] {
		return this._schulgliederungenModel.value;
	}

	public set schulgliederungenModel(value: Schulgliederung[]) {
		this._schulgliederungenModel.value = value;
	}

	protected createOptions(): KlassenAuswahlFilterOptions {
		if (this._sources === null) {
			return { jahrgaenge: new ArrayList<JahrgangsDaten>(), lehrer: new ArrayList<LehrerListeEintrag>(), schulgliederungen: new ArrayList<Schulgliederung>(), schuljahr: 0 };
		}
		const schuleState = this._sources.schule;
		return {
			jahrgaenge: this._sources.jahrgaenge.list(),
			lehrer: this._sources.lehrer.list(),
			schulgliederungen: getSchulgliederungen(schuleState.schulform, schuleState.schuljahr),
			schuljahr: schuleState.schuljahr,
		};
	}

	protected checkPassFilter(entry: KlassenListeEintrag, values: KlassenAuswahlFilterValues, options: KlassenAuswahlFilterOptions): boolean {
		return klassenCheckPassFilter(entry, values, id => {
			for (const j of options.jahrgaenge) {
				if (j.id === id) {
					return j;
				}
			}
			return null;
		});
	}

	protected checkPassSearch(entry: KlassenListeEintrag, search: string): boolean {
		return (entry.kuerzel !== null) && entry.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase());
	}

	protected compare(a: KlassenListeEintrag, b: KlassenListeEintrag): number {
		return comparatorKlassenListeEintrag.compare(a, b);
	}

	protected sortList(list: List<KlassenListeEintrag>): void {
		list.sort({ compare: (a: KlassenListeEintrag, b: KlassenListeEintrag) => a.sortierung - b.sortierung });
	}

	protected hasFilter(values: KlassenAuswahlFilterValues): boolean {
		return !values.jahrgaenge.isEmpty() || !values.lehrer.isEmpty() || !values.schulgliederungen.isEmpty();
	}

	protected cleanValues(values: KlassenAuswahlFilterValues, options: KlassenAuswahlFilterOptions): KlassenAuswahlFilterValues {
		const jahrgaenge = new HashSet<number>();
		for (const j of options.jahrgaenge) {
			if (values.jahrgaenge.contains(j.id)) {
				jahrgaenge.add(j.id);
			}
		}
		const lehrer = new HashSet<number>();
		for (const l of options.lehrer) {
			if (values.lehrer.contains(l.id)) {
				lehrer.add(l.id);
			}
		}
		const schulgliederungen = new HashSet<string>();
		for (const sg of options.schulgliederungen) {
			const key = klassenFilterSchulgliederungKey(sg, options.schuljahr);
			if (values.schulgliederungen.contains(key)) {
				schulgliederungen.add(key);
			}
		}
		return { jahrgaenge, lehrer, schulgliederungen };
	}

}
