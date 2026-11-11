import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashMap } from "@core/java/util/HashMap";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";
import { StateManager } from "@ui/ui/StateManager";
import type { PendingStateManager } from "@ui/ui/wrapper/PendingStateManager";

import type { GenericAuswahlMultiHost } from "./GenericAuswahlState";
import type { GenericGruppenprozessState } from "./GenericGruppenprozessState";

/** Der reaktive State der Stammdaten der Mehrfachauswahl */
export interface GenericGruppenprozessReactiveState<TDaten> {
	listeDaten: JavaMap<number, TDaten>;
}

/**
 * Die abstrakte Basisklasse für die generischen States der Stammdaten der Mehrfachauswahl (Gruppenprozesse) zu einer
 * Auswahlliste. Die Mehrfachauswahl liegt im Auswahl-State, an dem sich dieser State per onMultiSelect anhängt, so wie
 * sich der Stammdaten-State per onSelect an die Einzelauswahl anhängt.
 *
 * @typeParam TEintrag   der Typ der Einträge der Auswahlliste
 * @typeParam TDaten     der Typ der Stammdaten
 */
export abstract class GenericGruppenprozessStateImpl<TEintrag extends { id: number }, TDaten extends object>
	extends StateManager<GenericGruppenprozessReactiveState<TDaten>> implements GenericGruppenprozessState<TDaten> {

	/** Die Funktion, welche die ID zu Stammdaten liefert */
	private readonly _getIdDaten: (daten: TDaten) => number;

	/** Der Auswahl-State, an den sich der State anhängt */
	private readonly _auswahl: GenericAuswahlMultiHost<TEintrag>;


	/**
	 * Erzeugt einen neuen State für die Stammdaten der Mehrfachauswahl.
	 *
	 * @param getIdDaten   die Funktion, welche die ID zu Stammdaten liefert
	 * @param auswahl      der Auswahl-State, an den sich der State anhängt
	 */
	protected constructor(getIdDaten: (daten: TDaten) => number, auswahl: GenericAuswahlMultiHost<TEintrag>) {
		super({ listeDaten: new HashMap<number, TDaten>() });
		this._getIdDaten = getIdDaten;
		this._auswahl = auswahl;
		auswahl.onMultiSelect(async idsAuswahl => await this.loadForMultiSelection(idsAuswahl));
	}


	/**
	 * Diese Methode muss überschrieben werden und lädt die Stammdaten zu mehreren Einträgen der Auswahlliste.
	 *
	 * @param ids   die IDs der Einträge, zu denen die Stammdaten geladen werden sollen
	 *
	 * @returns die geladenen Stammdaten oder null
	 */
	protected abstract ladeDatenMultiple(ids: List<number>): Promise<Iterable<TDaten> | null>;

	/**
	 * Gibt den Eintrag der Auswahlliste zu der ID zurück.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @returns der Eintrag
	 *
	 * @throws DeveloperNotificationException   wenn der Eintrag nicht in der Auswahlliste enthalten ist
	 */
	protected getEintragOrException(id: number): TEintrag {
		const eintrag = this._auswahl.getEntry(id);
		if (eintrag === null) {
			throw new DeveloperNotificationException("Der Eintrag mit der ID " + id.toString() + " ist nicht in der Auswahlliste enthalten.");
		}
		return eintrag;
	}


	public get listeDaten(): JavaMap<number, TDaten> {
		return this._state.value.listeDaten;
	}

	public async loadForMultiSelection(idsAuswahl: List<number>): Promise<() => void> {
		const currentSelection = this._state.value.listeDaten;
		const newSelection = new HashMap<number, TDaten>();
		const delta = new ArrayList<number>();
		for (const id of idsAuswahl) {
			const daten = currentSelection.get(id);
			if (daten === null) {
				delta.add(id);
			} else {
				newSelection.put(id, daten);
			}
		}

		let deltaDaten: Iterable<TDaten> = [];
		if (!delta.isEmpty()) {
			const geladen = await this.ladeDatenMultiple(delta);
			if (geladen === null) {
				throw new DeveloperNotificationException("Fehler beim Laden der Daten. Es konnten keine Daten zu den ausgewählten Einträgen geladen werden.");
			}
			deltaDaten = geladen;
		}

		return () => {
			for (const daten of deltaDaten) {
				newSelection.put(this._getIdDaten(daten), daten);
			}
			this.setPatchedState({ listeDaten: newSelection });
		};
	}

	public async patchMultiple(pendingStateManager: PendingStateManager<any>): Promise<void> {
		throw new DeveloperNotificationException("Das Patchen mehrerer Datensätze wird nicht unterstützt.");
	}

	public reset(): void {
		this.setPatchedDefaultState({ listeDaten: new HashMap<number, TDaten>() });
	}

}
