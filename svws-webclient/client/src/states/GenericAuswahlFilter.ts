import { computed, type ComputedRef, type ShallowRef, shallowRef, type WritableComputedRef } from "vue";

import { ArrayList } from "@core/java/util/ArrayList";
import type { Comparator } from "@core/java/util/Comparator";
import { HashSet } from "@core/java/util/HashSet";
import type { JavaSet } from "@core/java/util/JavaSet";
import type { List } from "@core/java/util/List";
import type { AuswahlManagerSortierOrdnung } from "@ui/ui/manager/AuswahlManagerSortierOrdnung";
import { StateManager } from "@ui/ui/StateManager";

interface GenericAuswahlFilterReactiveState<TValues> {
	values: TValues;
	search: string;
	order: AuswahlManagerSortierOrdnung[];
}

/**
 * Die abstrakte Basisklasse für die Filter von Auswahllisten. Der Filter hält die Filterwerte (als IDs bzw. Schlüssel),
 * die Optionen für die Filterfelder und die Suche. Aus den Einträgen der Auswahl werden daraus die gefilterte und die
 * angezeigte Liste berechnet. Der aktuell gewählte Eintrag bleibt dabei in der Liste sichtbar.
 *
 * @typeParam TEntry     der Typ der Einträge der Auswahlliste
 * @typeParam TValues    der Typ der Filterwerte
 * @typeParam TOptions   der Typ der Optionen für die Filterfelder
 */
export abstract class GenericAuswahlFilter<TEntry extends { id: number }, TValues extends Record<string, any>, TOptions> extends StateManager<GenericAuswahlFilterReactiveState<TValues>> {

	/** Die Funktion, welche die Einträge der Auswahlliste liefert */
	private readonly _entries: () => Iterable<TEntry>;

	/** Die Funktion, welche die ID des aktuell gewählten Eintrags oder null liefert */
	private readonly _idSelected: () => number | null;

	/** Feste Optionen, welche anstelle der aus den Quellen des Filters erzeugten Optionen genutzt werden (z.B. für Tests) */
	private readonly _optionsOverride: ShallowRef<TOptions | null> = shallowRef(null);

	/** Die Optionen für die Filterfelder */
	private readonly _options: ComputedRef<TOptions>;

	/** Der Comparator für die Reihenfolge der gefilterten Liste */
	private readonly _comparator: Comparator<TEntry> = { compare: (a: TEntry, b: TEntry) => this.compare(a, b) };

	/** Die gefilterte Liste (ohne Suche) */
	private readonly _filtered: ComputedRef<List<TEntry>>;

	/** Die angezeigte Liste (gefiltert und durchsucht) */
	private readonly _visible: ComputedRef<List<TEntry>>;

	/**
	 * Wird aufgerufen, nachdem sich die Filterwerte geändert haben (nicht bei einer Änderung der Suche).
	 * Der Auswahl-State nutzt dies, um bei Bedarf einen Eintrag aus der gefilterten Liste auszuwählen.
	 */
	public onChanged: (() => void) | null = null;


	/**
	 * Erzeugt einen neuen Filter.
	 *
	 * @param defaultValues    die Filterwerte, wenn kein Filter gesetzt ist
	 * @param entries          die Funktion, welche die Einträge der Auswahlliste liefert
	 * @param idSelected       die Funktion, welche die ID des aktuell gewählten Eintrags oder null liefert
	 * @param defaultOrder     die Sortier-Ordnung, wenn keine andere gesetzt wurde (optional, standardmäßig leer)
	 */
	protected constructor(defaultValues: TValues, entries: () => Iterable<TEntry>, idSelected: () => number | null,
		defaultOrder: AuswahlManagerSortierOrdnung[] = []) {
		super({ values: defaultValues, search: "", order: defaultOrder });
		this._entries = entries;
		this._idSelected = idSelected;
		this._options = computed(() => this._optionsOverride.value ?? this.createOptions());
		this._filtered = computed(() => this.doFilter());
		this._visible = computed(() => this.doSearch());
	}


	/**
	 * Erzeugt die Optionen für die Filterfelder aus den Quellen des Filters (z.B. den Katalogen des Auswahl-States).
	 * Die Optionen werden zwischengespeichert und nur bei einer Änderung der Quellen neu erzeugt.
	 *
	 * @returns die Optionen
	 */
	protected abstract createOptions(): TOptions;

	/**
	 * Prüft, ob der Eintrag den Filter passiert.
	 *
	 * @param entry     der zu prüfende Eintrag
	 * @param values    die aktuellen Filterwerte
	 * @param options   die aktuellen Optionen
	 *
	 * @returns true, wenn der Eintrag den Filter passiert
	 */
	protected abstract checkPassFilter(entry: TEntry, values: TValues, options: TOptions): boolean;

	/**
	 * Prüft, ob der Eintrag zur Suche passt.
	 *
	 * @param entry    der zu prüfende Eintrag
	 * @param search   der Suchtext
	 *
	 * @returns true, wenn der Eintrag zur Suche passt
	 */
	protected abstract checkPassSearch(entry: TEntry, search: string): boolean;

	/**
	 * Vergleicht zwei Einträge für die Reihenfolge der gefilterten Liste.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @returns ein negativer Wert, 0 oder ein positiver Wert
	 */
	protected abstract compare(a: TEntry, b: TEntry): number;

	/**
	 * Gibt zurück, ob bei den übergebenen Filterwerten ein Filter gesetzt ist.
	 *
	 * @param values   die Filterwerte
	 *
	 * @returns true, wenn ein Filter gesetzt ist
	 */
	protected abstract hasFilter(values: TValues): boolean;

	/**
	 * Diese Methode kann überschrieben werden, um die Reihenfolge der angezeigten Liste abweichend von der
	 * Reihenfolge der gefilterten Liste festzulegen.
	 *
	 * @param list   die angezeigte Liste, welche sortiert werden kann
	 */
	protected sortList(list: List<TEntry>): void {
		// nichts zu tun
	}

	/**
	 * Erzeugt ein Model für ein Auswahlfeld der Oberfläche (z.B. ein Multi-Select). Das Model liefert die Objekte
	 * der Optionen, deren Schlüssel in den Filterwerten enthalten sind. Beim Setzen werden die Schlüssel der
	 * übergebenen Objekte als Filterwerte gesetzt (siehe {@link setValue}).
	 *
	 * @param key       der Name des Filterwertes, welcher eine Menge von Schlüsseln enthält
	 * @param options   die Funktion, welche die Optionen des Auswahlfeldes liefert
	 * @param toKey     die Funktion, welche den Schlüssel einer Option liefert (oder null, wenn es keinen gibt)
	 *
	 * @returns das Model
	 */
	protected selectionModel<O>(key: keyof TValues, options: () => Iterable<O>, toKey: (option: O) => number | string | null): WritableComputedRef<O[]> {
		return computed<O[]>({
			get: () => {
				const selected = this._state.value.values[key] as JavaSet<number | string>;
				return [...options()].filter(option => {
					const optionKey = toKey(option);
					return (optionKey !== null) && selected.contains(optionKey);
				});
			},
			set: (value: O[]) => {
				const result = new HashSet<number | string>();
				for (const option of value) {
					const optionKey = toKey(option);
					if (optionKey !== null) {
						result.add(optionKey);
					}
				}
				this.setValue(key, result as TValues[keyof TValues]);
			},
		});
	}

	/**
	 * Erzeugt ein Model für ein Feld der Oberfläche mit einem einfachen Filterwert (z.B. einen Schalter).
	 *
	 * @param key   der Name des Filterwertes
	 *
	 * @returns das Model
	 */
	protected valueModel<K extends keyof TValues>(key: K): WritableComputedRef<TValues[K]> {
		return computed<TValues[K]>({
			get: () => this._state.value.values[key],
			set: (value: TValues[K]) => this.setValue(key, value),
		});
	}

	/**
	 * Gibt die Filterwerte zurück, die gelten, wenn der Filter zurückgesetzt wird.
	 *
	 * @returns die Default-Werte
	 */
	protected get defaultValues(): TValues {
		return this._defaultState.values;
	}


	private doFilter(): List<TEntry> {
		const { values } = this._state.value;
		const idSelected = this._idSelected();
		const list = new ArrayList<TEntry>();
		let options: TOptions | undefined = undefined;
		for (const entry of this._entries()) {
			if (((idSelected !== null) && (entry.id === idSelected)) || this.checkPassFilter(entry, values, options ??= this._options.value)) {
				list.add(entry);
			}
		}
		list.sort(this._comparator);
		return list;
	}

	private doSearch(): List<TEntry> {
		const search = this._state.value.search;
		const list = new ArrayList<TEntry>();
		for (const entry of this._filtered.value) {
			if (this.checkPassSearch(entry, search)) {
				list.add(entry);
			}
		}
		this.sortList(list);
		return list;
	}


	/**
	 * Gibt die aktuellen Filterwerte zurück.
	 *
	 * @returns die Filterwerte
	 */
	public get values(): Readonly<TValues> {
		return this._state.value.values;
	}

	/**
	 * Gibt die Optionen für die Filterfelder zurück.
	 *
	 * @returns die Optionen
	 */
	public get options(): Readonly<TOptions> {
		return this._options.value;
	}

	/**
	 * Gibt zurück, ob ein Filter gesetzt ist, d.h. die Filterwerte von den Default-Werten abweichen.
	 *
	 * @returns true, wenn ein Filter gesetzt ist
	 */
	public get isActive(): boolean {
		return this.hasFilter(this._state.value.values);
	}

	/**
	 * Gibt den aktuellen Suchtext zurück.
	 *
	 * @returns der Suchtext
	 */
	public get search(): string {
		return this._state.value.search;
	}

	/**
	 * Setzt den Suchtext. Dies löst nicht das Ereignis {@link onChanged} aus.
	 *
	 * @param value   der neue Suchtext
	 */
	public set search(value: string) {
		this.setPatchedState({ search: value });
	}

	/**
	 * Gibt die gefilterte Liste (ohne Suche) zurück. Der aktuell gewählte Eintrag ist stets enthalten.
	 *
	 * @returns die gefilterte Liste
	 */
	public get filtered(): List<TEntry> {
		return this._filtered.value;
	}

	/**
	 * Gibt die angezeigte Liste zurück, d.h. die gefilterte Liste, die zusätzlich zum Suchtext passt.
	 *
	 * @returns die angezeigte Liste
	 */
	public get visible(): List<TEntry> {
		return this._visible.value;
	}


	/**
	 * Setzt einen Filterwert und löst danach das Ereignis {@link onChanged} aus.
	 *
	 * @param key     der Name des Filterwertes
	 * @param value   der neue Wert
	 */
	public setValue<K extends keyof TValues>(key: K, value: TValues[K]): void {
		this.setPatchedState({ values: { ...this._state.value.values, [key]: value } });
		this.onChanged?.();
	}

	/**
	 * Setzt einen Filterwert, ohne das Ereignis {@link onChanged} auszulösen. Dies dient der Initialisierung
	 * des Filters, z.B. aus einer gespeicherten Konfiguration.
	 *
	 * @param key     der Name des Filterwertes
	 * @param value   der neue Wert
	 */
	public setValueSilent<K extends keyof TValues>(key: K, value: TValues[K]): void {
		this.setPatchedState({ values: { ...this._state.value.values, [key]: value } });
	}

	/**
	 * Gibt die aktuelle Sortier-Ordnung zurück. Jeder Eintrag beschreibt ein Sortierfeld und die Richtung.
	 *
	 * @returns eine Kopie der Sortier-Ordnung, das zuletzt gesetzte Sortierfeld steht an erster Stelle
	 */
	public orderGet(): AuswahlManagerSortierOrdnung[] {
		return [...this._state.value.order];
	}

	/**
	 * Aktualisiert die Sortier-Ordnung für das angegebene Feld und löst danach das Ereignis {@link onChanged} aus.
	 * Das Feld wird an die erste Stelle der Ordnung gesetzt. Bei null als Richtung wird das Feld entfernt.
	 * Ist das Feld bereits mit dieser Richtung gesetzt, so geschieht nichts.
	 *
	 * @param field       das Sortierfeld
	 * @param ascending   true für aufsteigend, false für absteigend und null zum Entfernen des Feldes
	 */
	public orderUpdate(field: string, ascending: boolean | null): void {
		const current = this._state.value.order.find(o => o.field === field);
		if ((current !== undefined) && (current.ascending === ascending)) {
			return;
		}
		const order = this._state.value.order.filter(o => o.field !== field);
		if (ascending !== null) {
			order.unshift({ field, ascending });
		}
		this.setPatchedState({ order });
		this.onChanged?.();
	}

	/**
	 * Setzt alle Filterwerte auf die Default-Werte zurück und löst danach das Ereignis {@link onChanged} aus.
	 * Der Suchtext bleibt unverändert. Die Funktion ist an die Instanz gebunden und kann daher direkt
	 * als Callback weitergegeben werden (z.B. als Zurücksetzen-Aktion der Tabelle).
	 */
	public readonly setDefault = (): void => {
		this.setPatchedState({ values: this.defaultValues });
		this.onChanged?.();
	};

	/**
	 * Bereinigt die Filterwerte um alle Werte, die bei den aktuellen Optionen nicht existieren. Dies ist nötig, wenn
	 * sich die Optionen geändert haben, z.B. nach dem Wechsel des Schuljahresabschnittes. Die übrigen Filterwerte bleiben erhalten.
	 */
	public pruneValues(): void {
		this.setPatchedState({ values: this.cleanValues(this._state.value.values, this._options.value) });
	}

	/**
	 * Setzt feste Optionen für die Filterfelder anstelle der aus den Quellen des Filters erzeugten Optionen (z.B. für Tests), und
	 * bereinigt die Filterwerte um Werte, die bei den neuen Optionen nicht existieren.
	 *
	 * @param options   die neuen Optionen
	 */
	public setOptions(options: TOptions): void {
		this._optionsOverride.value = options;
		this.pruneValues();
	}

	/**
	 * Entfernt aus den Filterwerten alle Werte, die bei den übergebenen Optionen nicht existieren.
	 *
	 * @param values    die aktuellen Filterwerte
	 * @param options   die neuen Optionen
	 *
	 * @returns die bereinigten Filterwerte
	 */
	protected abstract cleanValues(values: TValues, options: TOptions): TValues;

	/**
	 * Berechnet die gefilterte und die angezeigte Liste neu. Dies ist nötig, wenn sich die Einträge der
	 * Auswahlliste oder die Auswahl geändert haben, ohne dass sich der Filter selbst geändert hat.
	 */
	public refresh(): void {
		this.commit();
	}

}
