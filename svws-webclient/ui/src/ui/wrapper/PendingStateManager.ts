import { computed } from 'vue';

import { ArrayList } from "@core/java/util/ArrayList";
import { Arrays } from "@core/java/util/Arrays";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";

import type { ListeMitAuswahl } from "../manager/ListeMitAuswahl";
import { StateManager } from "../StateManager";

/**
 * Schnittstelle, die den Zustand der ausstehenden Patches darstellt.
 * @typeParam T - Der Typ der zu patchenden Werte.
 */
interface PendingState<T> {
	pendingValues: Partial<T>;
	pendingIDs: List<number>;
}

/**
 * Klasse zur Verwaltung des Zustands ausstehender Patches.
 * @typeParam T - Der Typ der zu patchenden Werte.
 * @extends StateManager<PendingStateManager<T>>
 */
export class PendingStateManager<T> extends StateManager<PendingState<T>> {

	/** Name des Attributes, welches als ID des Objektes agiert, standardmäßig wird das Attribut 'id' gesetzt. */
	private readonly _idFieldName: string;

	/** Funktion, die die zugehörige Auswahlliste mit der Mehrfachauswahl zurückgibt. */
	protected readonly _liste: () => ListeMitAuswahl<number, any>;

	/** Funktion, die die geladenen Daten der Mehrfachauswahl zurückgibt, zugeordnet zu deren ID. */
	protected readonly _listeDaten: () => JavaMap<number, T>;

	/** Eine Map mit Mappings für Attribute, die eine individuelle Aufbereitung in der UI benötigen. */
	protected _attributeDisplayMappers: Map<keyof T, (value: any) => any> = new Map();

	/** Ein Mapper, um einen <code>boolean</code> Wert für die Anzeige in der UI aufzubereiten. */
	protected readonly _defaultBooleanDisplayMapper = (value: boolean) => value ? 'Ja' : 'Nein';

	/** Ein Mapper, um einen <code>Datum String</code> Wert für die Anzeige in der UI zu formatieren und aufzubereiten. */
	protected readonly _defaultDateDisplayMapper = (value: string) => new Date(value).toLocaleDateString("de-DE", {
		day: "2-digit",
		month: "2-digit",
		year: "numeric",
	});

	/**
	 * Konstruktor, der einen neue Instanz im Standardzustand erstellt.
	 */
	public constructor(idFieldName: any, liste: () => ListeMitAuswahl<number, any>, listeDaten: () => JavaMap<number, T>) {
		super({
			pendingValues: {},
			pendingIDs: new ArrayList<number>(),
		});
		this._idFieldName = idFieldName;
		this._liste = liste;
		this._listeDaten = listeDaten;
	}

	/**
	 * Erzeugt ein computed-Attribut für ein bestimmtes Attribut des Objekts.
	 *
	 * @param attributeName - Der Name des Attributs, für das das computed-Attribut erstellt werden soll.
	 * @param defaultValue - Der Standardwert, der zurückgegeben wird, wenn kein ausstehender Wert vorhanden ist.
	 * @param getMapper - Eine optionale Funktion, die den ausstehenden Wert in den gewünschten Typ umwandelt.
	 * @param setMapper - Eine optionale Funktion, die den Wert in den ausstehenden Wert umwandelt, wenn er gesetzt wird.
	 */
	public genComputed<Type>(attributeName: keyof T, defaultValue: Type, getMapper: ((value: any) => Type) | null, setMapper: ((value: Type) => any) | null) {
		return computed<Type>({
			get: () => {
				const value = this.pendingValues[attributeName];
				if (value === undefined) {
					return defaultValue;
				}
				return getMapper ? getMapper(value) : value as Type;
			},
			set: (value: Type) => {
				this.setPendingState(attributeName, setMapper ? setMapper(value) : value, this._liste().auswahlKeyList());
			},
		}
		);
	}

	/**
	 * Gibt eine Map mit verfügbaren Mappings für Attribute zurück, die eine individuelle Aufbereitung in der UI benötigen.
	 *
	 * @return Map mit Attribut Mappings für die Aufbereitung in der UI
	 */
	get attributeDisplayMappers() {
		return this._attributeDisplayMappers;
	}

	/**
	 * Gibt die ausstehenden Werte zurück.
	 *
	 * @returns Die ausstehenden Werte.
	 */
	get pendingValues() {
		return this._state.value.pendingValues;
	}

	/**
	 * Gibt den Namen des ID-Attributes zurück.
	 *
	 * @returns Namen des ID-Attributes zurück.
	 */
	get idFieldName(): string {
		return this._idFieldName;
	}

	/**
	 * Gibt die Liste der Teilobjekte zurück, die die ausstehenden Patches darstellen.
	 *
	 * @returns Die Liste der Teilobjekte.
	 */
	get partials(): List<Partial<T>> {
		return Arrays.asList(this._state.value.pendingIDs.toArray().map((id: any) => {
			return { [this._idFieldName]: id, ...this._state.value.pendingValues } as Partial<T>;
		}));
	}

	/**
	 * Gibt die zugehörige Auswahlliste mit der Mehrfachauswahl zurück.
	 *
	 * @Return die Auswahlliste
	 */
	get liste() {
		return this._liste();
	}

	/**
	 * Gibt die geladenen Daten der Mehrfachauswahl zurück.
	 *
	 * @Return die Daten der Mehrfachauswahl, zugeordnet zu deren ID
	 */
	get listeDaten() {
		return this._listeDaten();
	}

	/**
	 * Überprüft, ob es mindestens eine ausstehende Änderung gibt.
	 * @returns <code>true</code>, wenn es ausstehende Patches gibt, andernfalls <code>false</code>.
	 */
	public pendingStateExists(): boolean {
		return (Object.keys(this._state.value.pendingValues).length > 0);
	}

	/**
	 * Überprüft, ob eine ausstehende Änderung für das übergebene Attribut vorliegt.
	 * @returns <code>true</code>, wenn es eine ausstehende Änderung gibt, andernfalls <code>false</code>.
	 */
	public isAttributePending(attributeName: keyof T): boolean {
		return attributeName in this._state.value.pendingValues;
	}

	/**
	 * Setzt alle ausstehenden Patches zurück.
	 */
	public resetPendingState(): void {
		this._defaultState.pendingValues = {};
		this._defaultState.pendingIDs = new ArrayList<number>();
		this.reset();
		this.commit();
	}

	/**
	 * Setzt einen ausstehenden Zustand für ein bestimmtes Feld und IDs.
	 *
	 * @param fieldName - Der Name des zu patchenden Feldes.
	 * @param newValue - Der neue Wert für das Feld.
	 * @param changedIds - Die Liste der geänderten IDs.
	 */
	public setPendingState(fieldName: keyof T, newValue: any, changedIds: List<number>): void {
		const patch = <T>{ [fieldName]: newValue };
		this._state.value.pendingValues = Object.assign({ ...this.pendingValues }, patch);
		this._state.value.pendingIDs = changedIds;
		this.commit();
	}

	/**
	 * Entfernt einen ausstehenden Zustand für ein bestimmtes Feld.
	 *
	 * @param fieldName - Der Name des Feldes, das aus dem ausstehenden Zustand entfernt werden soll.
	 */
	public removePendingState(fieldName: keyof T): void {
		const { [fieldName]: _, ...patch } = this.pendingValues as Record<string, any>;
		this._state.value.pendingValues = patch as unknown as Partial<T>;
		this.commit();
	}

}
