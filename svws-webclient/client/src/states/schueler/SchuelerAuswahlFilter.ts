import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import type { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { KlassenUtils } from "@core/core/utils/klassen/KlassenUtils";
import { JavaInteger } from "@core/java/lang/JavaInteger";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import type { SchuleState } from "@ui/states/SchuleState";

import type { AuswahlKatalog } from "../AuswahlKatalog";
import { getSchulgliederungen } from "../AuswahlKatalogUtils";
import { GenericAuswahlFilter } from "../GenericAuswahlFilter";

/** Die Filterwerte der Auswahlliste der Schüler */
export interface SchuelerAuswahlFilterValues {
	/** Die Schlüssel (Statuskürzel als Zahl) der gewählten Schüler-Status */
	schuelerstatus: JavaSet<number>;
	/** Die IDs der gewählten Klassen */
	klassen: JavaSet<number>;
	/** Die IDs der gewählten Jahrgänge */
	jahrgaenge: JavaSet<number>;
	/** Die IDs der gewählten Kurse */
	kurse: JavaSet<number>;
	/** Die IDs der Katalog-Einträge der gewählten Schulgliederungen */
	schulgliederungen: JavaSet<number>;
	/** Gibt an, ob bei einem nicht aktuellen Schuljahresabschnitt nur Schüler mit einem Lernabschnitt angezeigt werden */
	nurMitLernabschnitt: boolean;
}

/** Die Optionen der Filterfelder der Auswahlliste der Schüler */
export interface SchuelerAuswahlFilterOptions {
	schuelerstatus: Array<SchuelerStatus>;
	klassen: List<KlassenDaten>;
	jahrgaenge: List<JahrgangsDaten>;
	kurse: List<KursDaten>;
	schulgliederungen: List<Schulgliederung>;
	/** Das Schuljahr, für welches die Schlüssel der Status und Schulgliederungen bestimmt werden */
	schuljahr: number;
	/** Die ID des Schuljahresabschnittes der Auswahl */
	idSchuljahresabschnitt: number;
	/** Gibt an, ob der Schuljahresabschnitt der Auswahl der aktuelle Schuljahresabschnitt der Schule ist */
	istAbschnittAktuell: boolean;
	/** Die Funktion, welche zu einer Klassen-ID die Klasse (auch aus anderen Schuljahresabschnitten) oder null liefert */
	klasse: (idKlasse: number) => KlassenDaten | null;
}

/** Die Quellen, aus denen der Filter der Schüler seine Optionen erzeugt */
export interface SchuelerAuswahlFilterSources {
	/** Der State der Schule mit Schulform, Schuljahr und aktuellem Abschnitt */
	schule: SchuleState;
	/** Der Katalog der Klassen des Schuljahresabschnittes */
	klassen: AuswahlKatalog<KlassenDaten>;
	/** Der Katalog aller Klassen der Schülerliste, auch der Klassen anderer Schuljahresabschnitte */
	klassenAlle: AuswahlKatalog<KlassenDaten>;
	/** Der Katalog der Jahrgänge */
	jahrgaenge: AuswahlKatalog<JahrgangsDaten>;
	/** Der Katalog der Kurse */
	kurse: AuswahlKatalog<KursDaten>;
	/** Die Funktion, welche die ID des Schuljahresabschnittes der Auswahl liefert */
	idSchuljahresabschnitt: () => number;
}

/**
 * Gibt den Schlüssel eines Schüler-Status im Filter zurück.
 *
 * @param status     der Schüler-Status
 * @param schuljahr   das Schuljahr
 *
 * @returns der Schlüssel (das Statuskürzel als Zahl) oder null, falls der Status in dem Schuljahr nicht gültig ist
 */
export function schuelerFilterStatusKey(status: SchuelerStatus, schuljahr: number): number | null {
	const eintrag = status.daten(schuljahr);
	return (eintrag === null) ? null : JavaInteger.parseInt(eintrag.kuerzel);
}

/**
 * Gibt den Schlüssel einer Schulgliederung im Filter zurück.
 *
 * @param schulgliederung   die Schulgliederung
 * @param schuljahr         das Schuljahr
 *
 * @returns die ID des Katalog-Eintrags oder null, falls die Schulgliederung in dem Schuljahr nicht gültig ist
 */
export function schuelerFilterSchulgliederungKey(schulgliederung: Schulgliederung, schuljahr: number): number | null {
	return schulgliederung.daten(schuljahr)?.id ?? null;
}

/**
 * Übernimmt aus den bisherigen Werten nur die Werte, für die es unter den Optionen noch einen Eintrag gibt.
 *
 * @param values    die bisherigen Werte
 * @param options   die neuen Optionen
 * @param toKey     die Funktion, welche den Wert zu einer Option liefert (oder null, wenn es keinen gibt)
 *
 * @returns die bereinigten Werte
 */
function retain<O>(values: JavaSet<number>, options: Iterable<O>, toKey: (option: O) => number | null): JavaSet<number> {
	const result = new HashSet<number>();
	for (const option of options) {
		const key = toKey(option);
		if ((key !== null) && values.contains(key)) {
			result.add(key);
		}
	}
	return result;
}

/**
 * Der Filter für die Auswahlliste der Schüler
 */
export class SchuelerAuswahlFilter extends GenericAuswahlFilter<SchuelerListeEintrag, SchuelerAuswahlFilterValues, SchuelerAuswahlFilterOptions> {

	/** Das Model für das Auswahlfeld nurMitLernabschnitt */
	private readonly _nurMitLernabschnittModel = this.valueModel("nurMitLernabschnitt");

	/** Das Model für das Auswahlfeld schuelerstatus */
	private readonly _schuelerstatusModel = this.selectionModel("schuelerstatus", () => this.options.schuelerstatus, s => schuelerFilterStatusKey(s, this.options.schuljahr));

	/** Das Model für das Auswahlfeld schulgliederungen */
	private readonly _schulgliederungenModel = this.selectionModel("schulgliederungen", () => this.options.schulgliederungen, sg => schuelerFilterSchulgliederungKey(sg, this.options.schuljahr));

	/** Das Model für das Auswahlfeld jahrgaenge */
	private readonly _jahrgaengeModel = this.selectionModel("jahrgaenge", () => this.options.jahrgaenge, j => j.id);

	/** Das Model für das Auswahlfeld klassen */
	private readonly _klassenModel = this.selectionModel("klassen", () => this.options.klassen, k => k.id);

	/** Das Model für das Auswahlfeld kurse */
	private readonly _kurseModel = this.selectionModel("kurse", () => this.options.kurse, k => k.id);

	/** Die Quellen für die Optionen der Filterfelder oder null */
	private readonly _sources: SchuelerAuswahlFilterSources | null;

	/**
	 * Erzeugt einen neuen Filter für die Schüler.
	 *
	 * @param entries      die Funktion, welche die Einträge der Schülerliste liefert
	 * @param idSelected   die Funktion, welche die ID des aktuell gewählten Schülers oder null liefert
	 * @param sources      die Quellen für die Optionen der Filterfelder (ohne Quellen sind die Optionen leer)
	 */
	public constructor(entries: () => Iterable<SchuelerListeEintrag>, idSelected: () => number | null, sources: SchuelerAuswahlFilterSources | null = null) {
		super({
			schuelerstatus: new HashSet<number>(), klassen: new HashSet<number>(), jahrgaenge: new HashSet<number>(),
			kurse: new HashSet<number>(), schulgliederungen: new HashSet<number>(), nurMitLernabschnitt: true,
		}, entries, idSelected, [{ field: "klassen", ascending: true }, { field: "nachname", ascending: true }]);
		this._sources = sources;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: nurMitLernabschnitt */
	public get nurMitLernabschnittModel(): boolean {
		return this._nurMitLernabschnittModel.value;
	}

	public set nurMitLernabschnittModel(value: boolean) {
		this._nurMitLernabschnittModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: schuelerstatus */
	public get schuelerstatusModel(): SchuelerStatus[] {
		return this._schuelerstatusModel.value;
	}

	public set schuelerstatusModel(value: SchuelerStatus[]) {
		this._schuelerstatusModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: schulgliederungen */
	public get schulgliederungenModel(): Schulgliederung[] {
		return this._schulgliederungenModel.value;
	}

	public set schulgliederungenModel(value: Schulgliederung[]) {
		this._schulgliederungenModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: jahrgaenge */
	public get jahrgaengeModel(): JahrgangsDaten[] {
		return this._jahrgaengeModel.value;
	}

	public set jahrgaengeModel(value: JahrgangsDaten[]) {
		this._jahrgaengeModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: klassen */
	public get klassenModel(): KlassenDaten[] {
		return this._klassenModel.value;
	}

	public set klassenModel(value: KlassenDaten[]) {
		this._klassenModel.value = value;
	}

	/** Das Model für das Auswahlfeld der Oberfläche: kurse */
	public get kurseModel(): KursDaten[] {
		return this._kurseModel.value;
	}

	public set kurseModel(value: KursDaten[]) {
		this._kurseModel.value = value;
	}

	/**
	 * Setzt die Schüler-Status des Filters, ohne das Ereignis {@link onChanged} auszulösen. Dies dient der Initialisierung
	 * des Filters, z.B. vor dem Wechsel in die Schnelleingabe.
	 *
	 * @param status   die Schüler-Status, die gefiltert werden sollen
	 */
	public setSchuelerstatusSilent(status: SchuelerStatus[]): void {
		const keys = new HashSet<number>();
		for (const s of status) {
			const key = schuelerFilterStatusKey(s, this.options.schuljahr);
			if (key !== null) {
				keys.add(key);
			}
		}
		this.setValueSilent("schuelerstatus", keys);
	}

	protected createOptions(): SchuelerAuswahlFilterOptions {
		if (this._sources === null) {
			return {
				schuelerstatus: [], klassen: new ArrayList<KlassenDaten>(), jahrgaenge: new ArrayList<JahrgangsDaten>(),
				kurse: new ArrayList<KursDaten>(), schulgliederungen: new ArrayList<Schulgliederung>(),
				schuljahr: 0, idSchuljahresabschnitt: -1, istAbschnittAktuell: true, klasse: () => null,
			};
		}
		const schuleState = this._sources.schule;
		const klassenAlle = this._sources.klassenAlle;
		const idSchuljahresabschnitt = this._sources.idSchuljahresabschnitt();
		return {
			schuelerstatus: SchuelerStatus.values(),
			klassen: this._sources.klassen.list(),
			jahrgaenge: this._sources.jahrgaenge.list(),
			kurse: this._sources.kurse.list(),
			schulgliederungen: getSchulgliederungen(schuleState.schulform, schuleState.schuljahr),
			schuljahr: schuleState.schuljahr,
			idSchuljahresabschnitt,
			istAbschnittAktuell: idSchuljahresabschnitt === schuleState.abschnitt.id,
			klasse: idKlasse => klassenAlle.get(idKlasse),
		};
	}

	/**
	 * Gibt die Schlüssel der Schüler-Status zurück, die standardmäßig gefiltert werden (aktiv und extern)
	 * Sie hängen vom Schuljahr der Optionen ab.
	 *
	 * @returns die Schlüssel der Default-Status
	 */
	public get defaultStatus(): JavaSet<number> {
		const result = new HashSet<number>();
		for (const status of [SchuelerStatus.AKTIV, SchuelerStatus.EXTERN]) {
			const key = schuelerFilterStatusKey(status, this.options.schuljahr);
			if (key !== null) {
				result.add(key);
			}
		}
		return result;
	}

	protected override get defaultValues(): SchuelerAuswahlFilterValues {
		return { ...super.defaultValues, schuelerstatus: this.defaultStatus };
	}

	protected checkPassFilter(entry: SchuelerListeEintrag, values: SchuelerAuswahlFilterValues, options: SchuelerAuswahlFilterOptions): boolean {
		if (values.nurMitLernabschnitt && !options.istAbschnittAktuell && (entry.idSchuljahresabschnitt !== options.idSchuljahresabschnitt)) {
			return false;
		}
		if (!values.jahrgaenge.isEmpty() && ((entry.idJahrgang < 0) || !values.jahrgaenge.contains(entry.idJahrgang))) {
			return false;
		}
		if (!values.klassen.isEmpty() && ((entry.idKlasse < 0) || (entry.idSchuljahresabschnitt !== options.idSchuljahresabschnitt) || !values.klassen.contains(entry.idKlasse))) {
			return false;
		}
		if (!values.kurse.isEmpty()) {
			let hatEinenKurs = false;
			for (const idKurs of entry.kurse) {
				if (values.kurse.contains(idKurs)) {
					hatEinenKurs = true;
				}
			}
			if (!hatEinenKurs) {
				return false;
			}
		}
		if (!values.schulgliederungen.isEmpty() && ((entry.idSchulgliederung < 0) || !values.schulgliederungen.contains(entry.idSchulgliederung))) {
			return false;
		}
		if (!values.schuelerstatus.isEmpty() && options.istAbschnittAktuell && !values.schuelerstatus.contains(entry.status)) {
			return false;
		}
		return true;
	}

	protected checkPassSearch(entry: SchuelerListeEintrag, search: string): boolean {
		const searchValueIsNumber = /^\d+$/.test(search.trim());
		const searchLowerCase = search.toLocaleLowerCase();
		return (searchValueIsNumber && entry.id.toString().includes(search))
			|| entry.nachname.toLocaleLowerCase().includes(searchLowerCase)
			|| entry.vorname.toLocaleLowerCase().includes(searchLowerCase);
	}

	protected compare(a: SchuelerListeEintrag, b: SchuelerListeEintrag): number {
		for (const { field, ascending } of this._state.value.order) {
			let cmp: number;
			if (field === "klassen") {
				const aKlasse = this.options.klasse(a.idKlasse);
				const bKlasse = this.options.klasse(b.idKlasse);
				if ((aKlasse === null) && (bKlasse === null)) {
					cmp = 0;
				} else if (aKlasse === null) {
					cmp = -1;
				} else if (bKlasse === null) {
					cmp = 1;
				} else {
					cmp = KlassenUtils.comparator.compare(aKlasse, bKlasse);
				}
			} else if (field === "nachname") {
				cmp = JavaString.compareTo(a.nachname, b.nachname);
			} else if (field === "vorname") {
				cmp = JavaString.compareTo(a.vorname, b.vorname);
			} else {
				throw new DeveloperNotificationException("Fehler bei der Sortierung. Das Sortierkriterium wird vom Filter nicht unterstützt.");
			}
			if (cmp !== 0) {
				return ascending ? cmp : -cmp;
			}
		}
		return JavaLong.compare(a.id, b.id);
	}

	protected hasFilter(values: SchuelerAuswahlFilterValues): boolean {
		if (!values.klassen.isEmpty() || !values.jahrgaenge.isEmpty() || !values.kurse.isEmpty() || !values.schulgliederungen.isEmpty()) {
			return true;
		}
		const defaultStatus = this.defaultStatus;
		return (values.schuelerstatus.size() !== defaultStatus.size()) || !values.schuelerstatus.containsAll(defaultStatus);
	}

	protected cleanValues(values: SchuelerAuswahlFilterValues, options: SchuelerAuswahlFilterOptions): SchuelerAuswahlFilterValues {
		return {
			schuelerstatus: retain(values.schuelerstatus, options.schuelerstatus, s => schuelerFilterStatusKey(s, options.schuljahr)),
			klassen: retain(values.klassen, options.klassen, k => k.id),
			jahrgaenge: retain(values.jahrgaenge, options.jahrgaenge, j => j.id),
			kurse: retain(values.kurse, options.kurse, k => k.id),
			schulgliederungen: retain(values.schulgliederungen, options.schulgliederungen, sg => schuelerFilterSchulgliederungKey(sg, options.schuljahr)),
			nurMitLernabschnitt: values.nurMitLernabschnitt,
		};
	}

}
