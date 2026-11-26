import type { ComputedRef, ShallowRef, WritableComputedRef } from "vue";
import { computed, shallowRef } from "vue";

import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";


/**
 * Die Konfiguration für einen Multi-Model-Proxy für Daten des Typs T.
 */
export interface MultiModelProxyConfiguration<T extends object> {

	/** Der Lambda-Ausdruck für den Zugriff auf die aktuellen Daten der Mehrfachauswahl, welche i.A. vom SVWS-Server stammen */
	data: () => Iterable<T>;

	/** Bestimmt die ID eines Datensatzes */
	getId: (daten: T) => number;

	/** Führt den Patch für die übergebenen Teilobjekte (je Datensatz eines) aus und wirft eine Exception, wenn dies fehlschlägt */
	patchMultiple: (partials: List<Partial<T>>) => Promise<void>;

	/** Der Name des ID-Attributes, welches in den Teilobjekten gesetzt wird (Default: "id") */
	idField?: keyof T;

	/** Mapper für Attribute, die für die Anzeige in der UI aufbereitet werden müssen */
	displayMappers?: Map<keyof T, (value: any) => any>;

}


/**
 * Die Konfiguration eines Attributes für die Bindung in der UI, siehe {@link MultiModelProxy.attribute}.
 *
 * @typeParam T      der Typ der Datensätze
 * @typeParam K      das Attribut der Datensätze
 * @typeParam Type   der Typ des Wertes in der UI
 */
export interface MultiModelProxyAttributeConfiguration<T extends object, K extends keyof T, Type> {

	/** Das Attribut der Datensätze */
	attr: K;

	/** Der Wert in der UI, solange keine ausstehende Änderung für das Attribut vorliegt */
	defaultValue: NoInfer<Type>;

	/** Bereitet den ausstehenden Wert für die UI auf (ohne Angabe wird er unverändert verwendet) */
	fromPending?: (value: T[K]) => Type;

	/** Wandelt den Wert der UI in den ausstehenden Wert um (ohne Angabe wird er unverändert verwendet) */
	toPending?: (value: NoInfer<Type>) => T[K] | null | undefined;

}


/**
 * Ein Proxy, der den Pending-State für mehrere Datensätze (Mehrfachauswahl) verwaltet: Für jedes Attribut gibt es
 * höchstens einen ausstehenden Wert, der für alle Datensätze der Mehrfachauswahl gilt. Er hält die Häufigkeit der
 * aktuellen Werte für die Anzeige und erzeugt die Teilobjekte für das Patchen. Ändert sich die Auswahl, so muss der
 * Aufrufer das Pending mit {@link clearPending} verwerfen. Er ist das Pendant zum {@link ModelProxy} für eine Liste von
 * Datensätzen.
 *
 * @typeParam T   der Typ der Datensätze
 */
export class MultiModelProxy<T extends object> {

	/** Bereitet einen Wahrheitswert für die Anzeige in der UI auf */
	public static readonly booleanDisplayMapper = (value: boolean): string => value ? "Ja" : "Nein";

	/** Formatiert ein Datum (ISO-String) als String für die Anzeige in der UI */
	public static readonly dateDisplayMapper = (value: string): string => new Date(value).toLocaleDateString("de-DE", {
		day: "2-digit",
		month: "2-digit",
		year: "numeric",
	});

	/** Die Konfiguration des Proxies */
	private readonly _config: MultiModelProxyConfiguration<T>;

	/** Der Name des ID-Attributes in den Teilobjekten */
	private readonly _idField: keyof T;

	/** Die ausstehenden Werte; das Objekt wird bei Änderungen ersetzt, damit die Reaktivität erhalten bleibt */
	private readonly _pending: ShallowRef<Partial<T>> = shallowRef({});

	/** Die IDs der aktuellen Daten */
	private readonly _ids: ComputedRef<Set<number>>;


	/**
	 * Erzeugt einen neuen Proxy ohne ausstehende Änderungen.
	 *
	 * @param config   die Konfiguration
	 */
	public constructor(config: MultiModelProxyConfiguration<T>) {
		this._config = config;
		this._idField = config.idField ?? ("id" as keyof T);
		this._ids = computed(() => {
			const ids = new Set<number>();
			for (const daten of this._config.data()) {
				ids.add(this._config.getId(daten));
			}
			return ids;
		});
	}

	/**
	 * Gibt die ausstehenden Werte zurück.
	 *
	 * @returns die ausstehenden Werte
	 */
	public get pending(): Partial<T> {
		return this._pending.value;
	}

	/**
	 * Gibt zurück, ob mindestens eine ausstehende Änderung vorliegt.
	 *
	 * @returns true, wenn es ausstehende Änderungen gibt
	 */
	public get hasPending(): boolean {
		return Object.keys(this._pending.value).length > 0;
	}

	/**
	 * Gibt die Anzahl der Datensätze der Mehrfachauswahl zurück.
	 *
	 * @returns die Anzahl der Datensätze
	 */
	public get count(): number {
		return this._ids.value.size;
	}

	/**
	 * Gibt zurück, ob für das Attribut eine ausstehende Änderung vorliegt (auch mit dem Wert null).
	 *
	 * @param attr   das Attribut
	 *
	 * @returns true, wenn eine Änderung vorliegt
	 */
	public isPending(attr: keyof T): boolean {
		return attr in this._pending.value;
	}

	/**
	 * Setzt den ausstehenden Wert für das Attribut. Er gilt für alle Datensätze der Mehrfachauswahl.
	 *
	 * @param attr    das Attribut
	 * @param value   der neue Wert
	 */
	public setPending<K extends keyof T>(attr: K, value: T[K] | null): void {
		this._pending.value = { ...this._pending.value, [attr]: value };
	}

	/**
	 * Entfernt die ausstehende Änderung für das Attribut.
	 *
	 * @param attr   das Attribut
	 */
	public removePending(attr: keyof T): void {
		if (!this.isPending(attr)) {
			return;
		}
		const { [attr]: _, ...rest } = this._pending.value;
		this._pending.value = rest as Partial<T>;
	}

	/**
	 * Verwirft alle ausstehenden Änderungen.
	 */
	public clearPending(): void {
		if (this.hasPending) {
			this._pending.value = {};
		}
	}

	/**
	 * Bestimmt, wie viele Datensätze der Mehrfachauswahl welchen aktuellen Wert für das Attribut haben. Fehlende
	 * Werte werden unter null gezählt. Die Reihenfolge ist die des ersten Auftretens.
	 *
	 * @param attr   das Attribut
	 *
	 * @returns die Anzahl der Datensätze je aktuellem Wert
	 */
	public valueCounts<K extends keyof T>(attr: K): Map<T[K] | null, number> {
		const result = new Map<T[K] | null, number>();
		for (const daten of this._config.data()) {
			const value = daten[attr] ?? null;
			result.set(value, (result.get(value) ?? 0) + 1);
		}
		return result;
	}

	/**
	 * Erzeugt für jeden Datensatz der Mehrfachauswahl ein Teilobjekt mit der ID und den ausstehenden Werten.
	 *
	 * @returns die Liste der Teilobjekte, leer wenn es keine ausstehenden Änderungen gibt
	 */
	public partials(): List<Partial<T>> {
		const result = new ArrayList<Partial<T>>();
		if (!this.hasPending) {
			return result;
		}
		for (const id of this._ids.value) {
			result.add({ [this._idField]: id, ...this._pending.value } as Partial<T>);
		}
		return result;
	}

	/**
	 * Führt den Patch mit den ausstehenden Änderungen aus. Bei Erfolg werden diese verworfen, bei einem Fehler bleiben
	 * sie erhalten und der Fehler wird weitergegeben.
	 *
	 * @throws Error, wenn das Patchen fehlschlägt
	 */
	public async patchMultiple(): Promise<void> {
		if (!this.hasPending) {
			return;
		}
		await this._config.patchMultiple(this.partials());
		this.clearPending();
	}

	/**
	 * Bereitet einen Wert des Attributes für die Anzeige auf. Ohne Mapper wird der Wert unverändert zurückgegeben.
	 *
	 * @param attr    das Attribut
	 * @param value   der Wert
	 *
	 * @returns der aufbereitete Wert
	 */
	public displayText(attr: keyof T, value: any): any {
		const mapper = this._config.displayMappers?.get(attr);
		return (mapper === undefined) ? value : mapper(value);
	}

	/**
	 * Erzeugt ein computed für ein Attribut für die Bindung in der UI. Lesen liefert den Default ohne Pending, sonst
	 * den (aufbereiteten) ausstehenden Wert. Schreiben setzt den (umgewandelten) Wert als ausstehende Änderung. Der Typ
	 * des Wertes in der UI ist der Typ des Attributes, solange kein Mapper angegeben ist.
	 *
	 * @param config   die Konfiguration des Attributes
	 *
	 * @returns das computed
	 */
	public attribute<K extends keyof T, Type = T[K]>(config: MultiModelProxyAttributeConfiguration<T, K, Type>): WritableComputedRef<Type> {
		return computed<Type>({
			get: () => {
				const value = this._pending.value[config.attr];
				if (value === undefined) {
					return config.defaultValue;
				}
				return (config.fromPending === undefined) ? value as unknown as Type : config.fromPending(value as T[K]);
			},
			set: (value: Type) => this.setPending(config.attr, ((config.toPending === undefined) ? value : config.toPending(value)) as any),
		});
	}

}
