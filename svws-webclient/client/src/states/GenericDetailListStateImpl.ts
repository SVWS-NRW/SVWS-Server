import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { StateManager } from "@ui/ui/StateManager";

import type { GenericDetailState } from "./GenericDetailState";

/** Der reaktive State der Detail-Liste */
export interface GenericDetailListReactiveState<TEintrag> {
	entries: List<TEintrag>;
}

/**
 * Die abstrakte Basisklasse für States mit einer Liste von Detail-Einträgen zu einem Eintrag einer Auswahlliste
 * (z.B. die Telefoneinträge eines Schülers). Die Liste wird bei Änderungen stets ersetzt, damit die Reaktivität
 * ausgelöst wird.
 *
 * @typeParam TEintrag   der Typ der Detail-Einträge
 */
export abstract class GenericDetailListStateImpl<TEintrag extends object> extends StateManager<GenericDetailListReactiveState<TEintrag>> implements GenericDetailState {

	/** Die Funktion, welche die ID eines Detail-Eintrags liefert */
	private readonly _getId: (entry: TEintrag) => number;

	/**
	 * Erzeugt einen neuen State für Detail-Einträge.
	 *
	 * @param getId   die Funktion, welche die ID eines Detail-Eintrags liefert
	 */
	protected constructor(getId: (entry: TEintrag) => number) {
		super({ entries: new ArrayList<TEintrag>() });
		this._getId = getId;
	}

	/**
	 * Diese Methode muss überschrieben werden und lädt die Detail-Einträge zu dem Eintrag der Auswahlliste.
	 *
	 * @param idAuswahl   die ID des ausgewählten Eintrags der Auswahlliste
	 *
	 * @returns die Detail-Einträge
	 */
	protected abstract loadEntries(idAuswahl: number): Promise<List<TEintrag>>;

	/**
	 * Gibt die Detail-Einträge zurück.
	 *
	 * @returns die Detail-Einträge
	 */
	public get entries(): List<TEintrag> {
		return this._state.value.entries;
	}

	public async loadForSelection(idAuswahl: number): Promise<() => void> {
		const entries = await this.loadEntries(idAuswahl);
		return () => this.setPatchedState({ entries });
	}

	/**
	 * Lädt die Detail-Einträge zu dem Eintrag der Auswahlliste und übernimmt sie sofort.
	 *
	 * @param idAuswahl   die ID des ausgewählten Eintrags der Auswahlliste
	 */
	public async load(idAuswahl: number): Promise<void> {
		(await this.loadForSelection(idAuswahl))();
	}

	/**
	 * Fügt den Eintrag der Liste hinzu.
	 *
	 * @param entry   der neue Eintrag
	 */
	protected applyAdd(entry: TEintrag): void {
		const entries = new ArrayList<TEintrag>(this._state.value.entries);
		entries.add(entry);
		this.setPatchedState({ entries });
	}

	/**
	 * Übernimmt die Änderung in den Eintrag mit der ID.
	 *
	 * @param id      die ID des Eintrags
	 * @param patch   die Änderung
	 */
	protected applyPatch(id: number, patch: Partial<TEintrag>): void {
		const entries = new ArrayList<TEintrag>(this._state.value.entries);
		for (const entry of entries) {
			if (this._getId(entry) === id) {
				Object.assign(entry, patch);
				break;
			}
		}
		this.setPatchedState({ entries });
	}

	/**
	 * Entfernt die Einträge mit den IDs aus der Liste.
	 *
	 * @param ids   die IDs der Einträge
	 */
	protected applyRemove(ids: Iterable<number>): void {
		const toRemove = new Set<number>(ids);
		const entries = new ArrayList<TEintrag>();
		for (const entry of this._state.value.entries) {
			if (!toRemove.has(this._getId(entry))) {
				entries.add(entry);
			}
		}
		this.setPatchedState({ entries });
	}

	public reset(): void {
		this.setDefaultState();
	}

}
