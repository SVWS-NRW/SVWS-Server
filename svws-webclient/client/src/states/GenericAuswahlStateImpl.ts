import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { ListeMitAuswahl } from "@ui/ui/manager/ListeMitAuswahl";
import { ViewType } from "@ui/ui/nav/ViewType";
import { StateManager } from "@ui/ui/StateManager";

import { GenericAuswahlNavigator } from "./GenericAuswahlNavigator";
import type { GenericAuswahlMultiSelectCallback, GenericAuswahlSelectCallback, GenericAuswahlState } from "./GenericAuswahlState";
import type { GenericAuswahlStateRoutingAdapter } from "./GenericAuswahlStateRoutingAdapter";
import type { GenericDatenState } from "./GenericDatenState";
import type { GenericGruppenprozessState } from "./GenericGruppenprozessState";

export interface GenericAuswahlReactiveState<TAuswahl> {
	idSchuljahresabschnitt: number;
	liste: ListeMitAuswahl<number, TAuswahl> | undefined;
	/** Die ID des Eintrags der Einzelauswahl oder null, falls keiner gewählt ist */
	idSelected: number | null;
}


/**
 * Die abstrakte Basisklasse für generische States für Auswahllisten
 */
export abstract class GenericAuswahlStateImpl<TAuswahl extends { id: number }, TDaten, R extends GenericAuswahlReactiveState<TAuswahl>>
	extends StateManager<R> implements GenericAuswahlState<TAuswahl, TDaten> {

	/** Der Navigator mit der aktiven Ansicht und dem Routing zu den Ansichten */
	private readonly _navigator: GenericAuswahlNavigator;

	/** Die Callbacks, die bei einer neuen Auswahl aufgerufen werden */
	private readonly _selectCallbacks = new Set<GenericAuswahlSelectCallback>();

	/** Die Callbacks, die bei einer Änderung der Mehrfachauswahl aufgerufen werden */
	private readonly _multiSelectCallbacks = new Set<GenericAuswahlMultiSelectCallback>();


	/**
	 * Gibt den State für die Stammdaten der Auswahl zurück. Dieser lädt und patcht die Stammdaten der Einzel- und
	 * Mehrfachauswahl und hält sie.
	 *
	 * @returns der State für die Stammdaten
	 */
	public abstract get stammdaten(): GenericDatenState<TDaten>;

	/**
	 * Gibt den State für die Stammdaten der Mehrfachauswahl zurück. Dieser hält die Stammdaten der Einträge der
	 * Mehrfachauswahl, die selbst in diesem Auswahl-State liegt.
	 *
	 * @returns der State für die Stammdaten der Mehrfachauswahl
	 */
	public abstract get gruppenprozesse(): GenericGruppenprozessState<TDaten>;

	/**
	 * Erzeugt einen neuen Auswahl-State mit dem übergebenen reaktiven State.
	 * Für das Routing zu Ansichten wie Gruppenprozesse, Hinzufügen und Schnelleingabe wird
	 * ein Routing-Adapter übergeben, um ein automatischen Umschalten zu den jeweiligen Ansichten zu ermöglichen.
	 *
	 * @param reactiveState    der reaktive State
	 * @param routingAdapter   der Adapter für das Routing zu den unterstützten Ansichten (Default, Gruppenprozesse, Neu, ...)
	 */
	public constructor(reactiveState: R, routingAdapter: GenericAuswahlStateRoutingAdapter) {
		super(reactiveState);
		this._navigator = new GenericAuswahlNavigator(routingAdapter, {
			hasEntry: id => this.liste.has(id),
			getFirstFilteredId: () => {
				const filtered = this.getListFiltered();
				return filtered.isEmpty() ? null : filtered.getFirst().id;
			},
			setEntry: async id => await this.setDaten(this.getEintragOrDefault(id)),
			clearSelection: () => this.liste.auswahlClear(),
			loadMultiple: async () => await this.ladeMultiAuswahl(),
			commit: () => this.commit(),
		});
	}

	/**
	 * Gibt den Navigator zurück. Dieser hält die aktive Ansicht und wechselt die Ansicht und die Auswahl.
	 *
	 * @returns der Navigator
	 */
	public get navigator(): GenericAuswahlNavigator {
		return this._navigator;
	}

	public onSelect(callback: GenericAuswahlSelectCallback): () => void {
		this._selectCallbacks.add(callback);
		return () => this.removeOnSelect(callback);
	}

	public removeOnSelect(callback: GenericAuswahlSelectCallback): void {
		this._selectCallbacks.delete(callback);
	}

	/**
	 * Ruft die hinterlegten Callbacks parallel auf und liefert eine Funktion, welche die geladenen Daten übernimmt.
	 *
	 * @param idAuswahl   die ID des gewählten Eintrags oder null
	 *
	 * @returns eine Promise mit der Funktion, welche die geladenen Daten aller Callbacks übernimmt
	 */
	private async ladeCallbacks(idAuswahl: number | null): Promise<() => void> {
		const applies = await Promise.all(this._selectCallbacks.values().map(callback => callback(idAuswahl)));
		return () => applies.forEach(apply => apply());
	}

	public onMultiSelect(callback: GenericAuswahlMultiSelectCallback): () => void {
		this._multiSelectCallbacks.add(callback);
		return () => this.removeOnMultiSelect(callback);
	}

	public removeOnMultiSelect(callback: GenericAuswahlMultiSelectCallback): void {
		this._multiSelectCallbacks.delete(callback);
	}

	public async setMultiSelection(items: Iterable<TAuswahl>): Promise<void> {
		this.liste.auswahlClear();
		for (const item of items) {
			if (this.liste.hasValue(item)) {
				this.liste.auswahlAdd(item);
			}
		}
		await this.ladeMultiAuswahl();
		this.commit();
	}

	/**
	 * Ruft die hinterlegten Callbacks für die Mehrfachauswahl parallel auf und liefert eine Funktion, welche die
	 * geladenen Daten übernimmt.
	 *
	 * @param idsAuswahl   die IDs der Einträge der Mehrfachauswahl
	 *
	 * @returns eine Promise mit der Funktion, welche die geladenen Daten aller Callbacks übernimmt
	 */
	private async ladeMultiCallbacks(idsAuswahl: List<number>): Promise<() => void> {
		const applies = await Promise.all(this._multiSelectCallbacks.values().map(callback => callback(idsAuswahl)));
		return () => applies.forEach(apply => apply());
	}

	/**
	 * Lädt die Daten zu der aktuellen Mehrfachauswahl über die hinterlegten Callbacks und übernimmt sie.
	 */
	private async ladeMultiAuswahl(): Promise<void> {
		const ids = new ArrayList<number>();
		for (const eintrag of this.liste.auswahl()) {
			ids.add(eintrag.id);
		}
		(await this.ladeMultiCallbacks(ids))();
	}

	public getEntry(id: number): TAuswahl | null {
		return this.liste.get(id);
	}

	public updateEntry(id: number, update: (entry: TAuswahl) => void): void {
		update(this.liste.getOrException(id));
		this.commit();
	}

	/**
	 * Setzt den State und die aktive Ansicht in den Default-State zurück.
	 */
	public reset(): void {
		super.reset();
		this._navigator.reset();
	}


	/**
	 * Erlaubt das manuelle Triggern der Reaktität auch von außen.
	 */
	public commit(): void {
		super.commit();
	}

	/**
	 * Gibt zurück, ob der State aktuell mit einer gültigen initialisiert und verfügbar ist.
	 */
	public get isAvailable(): boolean {
		return (this._state.value.liste !== undefined);
	}

	/**
	 * Gibt die ID des aktuell gewählten Schuljahresabschnittes zurück
	 *
	 * @returns die ID des aktuell gewählten Schuljahresabschnittes
	 */
	public get idSchuljahresabschnitt(): number {
		return this._state.value.idSchuljahresabschnitt;
	}

	/**
	 * Initialisiert den State für den ausgewählten Schuljahresabschnitt. Es werden die Daten für diesen Abschnitt geladen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param force                    gibt an, ob das Laden der Daten auch erzwungen werden soll, wenn die ID des
	 *                                 Schuljahresabschnittes bereits gesetzt ist (z.B. beim Betreten einer Route)
	 *
	 * @returns ein Promise mit der ID der Auswahl oder null
	 */
	public async init(idSchuljahresabschnitt: number, force: boolean): Promise<number | null> {
		return await this.setSchuljahresabschnitt(idSchuljahresabschnitt, force);
	}

	public get idSelected(): number | null {
		return this._state.value.idSelected;
	}

	public get selected(): TAuswahl {
		const id = this.idSelected;
		if (id === null) {
			throw new DeveloperNotificationException("Es ist kein Eintrag der Auswahlliste gewählt");
		}
		return this.liste.getOrException(id);
	}

	public get liste(): ListeMitAuswahl<number, TAuswahl> {
		if (this._state.value.liste === undefined) {
			throw new DeveloperNotificationException("Abfrage der Auswahlliste ohne vorige Initialisierung");
		}
		return this._state.value.liste;
	}


	/**
	 * Die Methode muss überschrieben werden und erzeugt dann die Auswahlliste mit den Einträgen
	 * für den angegebenen Schuljahresabschnitt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	protected abstract createListe(idSchuljahresabschnitt: number): Promise<Partial<R>>;


	/**
	 * Initialisiert den State für den ausgewählten Schuljahresabschnitt. Es werden die Daten für diesen Abschnitt geladen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param force                    gibt an, ob das Laden der Daten auch erzwungen werden soll, wenn die ID des
	 *                                 Schuljahresabschnittes bereits gesetzt ist (z.B. beim Betreten einer Route)
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number, force: boolean): Promise<number | null> {
		// Wenn die ID des Schuljahresabschnittes bereits gesetzt ist, dann muss die Auswahlliste für den Schuljahresabschnitt nicht neu erstellt werden
		if (!force && (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)) {
			return null;
		}
		// Ansonsten lade den Schuljahresabschnitt...
		return this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
	}

	/**
	 * Lädt die Daten zum ausgewählten Schuljahresabschnitt und setzt die Default-Werte für die Auswahlliste.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @returns ein Promise mit der ID der Auswahl oder null
	 */
	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt: number): Promise<number | null> {
		const newState = await this.createListe(idSchuljahresabschnitt);
		const liste = newState.liste;
		if (liste === undefined) {
			throw new DeveloperNotificationException('Die Methode createListe muss einen gültigen State mit einer initialisierten Auswahlliste liefern');
		}
		newState.idSchuljahresabschnitt = idSchuljahresabschnitt;

		const stammdaten = this.stammdaten;

		// Bestimme den Eintrag der vorherigen Auswahl in der neuen Auswahlliste, ansonsten wird der erste Eintrag gewählt
		const idVorherige = ((this._state.value.liste !== undefined) && (this.idSelected !== null)) ? this.idSelected : null;
		let idNeu: number | null = null;
		if (idVorherige !== null) {
			const eintrag = liste.get(idVorherige) ?? (liste.list().isEmpty() ? null : liste.list().getFirst());
			idNeu = (eintrag === null) ? null : eintrag.id;
		}

		// Lade die Daten zu der Auswahl über die hinterlegten Callbacks, ohne Auswahl werden die Daten verworfen
		const applyDaten = await this.ladeCallbacks(idNeu);

		// Die Mehrfachauswahl wird mit der neuen Auswahlliste verworfen
		const applyMulti = await this.ladeMultiCallbacks(new ArrayList<number>());

		// Ohne vorherige Auswahl wird die Default-Ansicht angezeigt
		if (idNeu === null) {
			this._navigator.resetView();
		}

		// Die Callbacks setzen die Daten erst, wenn die neue Auswahlliste aktiv ist
		newState.idSelected = idNeu;
		this.setPatchedDefaultState(newState);
		stammdaten.reset();
		applyDaten();
		applyMulti();
		return this.idSelected;
	}


	/**
	 * Setzt die Einzelauswahl direkt mit den übergebenen Stammdaten, ohne diese oder die Daten der hinterlegten
	 * Callbacks zu laden (z.B. nach dem Anlegen eines Eintrags, dessen Daten bereits vorliegen).
	 *
	 * @param daten   die Stammdaten des gewählten Eintrags
	 */
	public setDatenDirekt(daten: TDaten): void {
		this.stammdaten.setDaten(daten);
		this.setPatchedState({ idSelected: this.stammdaten.id } as Partial<R>);
	}

	/**
	 * Setzt die Auswahl in der Liste und lädt die Daten zu der Auswahl.
	 *
	 * @param auswahl   die Auswahl
	 */
	public async setDaten(auswahl: TAuswahl | null): Promise<void> {
		const hatteAuswahl = this.idSelected !== null;
		if ((auswahl === null) && (!hatteAuswahl)) {
			return;
		}

		if (auswahl !== null) {
			const id = auswahl.id;
			if (hatteAuswahl && (id === this.idSelected)) {
				return;
			}
		}

		const eintrag = (auswahl === null) ? null : this.getEintragOrDefault(auswahl.id);
		const idNeu = (eintrag === null) ? null : eintrag.id;
		const applyDaten = await this.ladeCallbacks(idNeu);
		this.setPatchedState({ idSelected: idNeu } as Partial<R>);
		applyDaten();
		this.commit();
	}


	/**
	 * Gibt den Eintrag zu der ID zurück. Ist dieser nicht vorhanden, so wird als Default der erste Eintrag
	 * der gefilterten Liste zurückgegeben. Ist diese leer, so wird null zurückgegeben.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @returns der Eintrag oder null
	 */
	private getEintragOrDefault(id: number): TAuswahl | null {
		if (this.liste.has(id)) {
			return this.liste.get(id);
		}
		const filtered = this.getListFiltered();
		return filtered.isEmpty() ? null : filtered.getFirst();
	}

	/**
	 * Gibt die gefilterte Liste der Auswahl zurück. Die Methode muss überschrieben werden und liefert die Liste
	 * des Filters der Auswahl.
	 *
	 * @returns die gefilterte Liste
	 */
	protected abstract getListFiltered(): List<TAuswahl>;

	/**
	 * Setzt den Filter neu und lädt den ersten Eintrag aus der gefilterten Liste
	 */
	public async setFilter(): Promise<void> {
		if ((this.idSelected === null) && (this._navigator.activeViewType === ViewType.DEFAULT)) {
			const listFiltered = this.getListFiltered();
			if (!listFiltered.isEmpty()) {
				const id = listFiltered.getFirst().id;
				await this._navigator.select(id);
				return;
			}
		}
		this.commit();
	};


	protected abstract doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>>;

	protected abstract deleteMessage(id: number, eintrag: TAuswahl | null): string;

	/**
	 * Diese Methode kann von einer abgeleiteten Klasse überschrieben werden, um die
	 * zu löschenden IDs nachträglich zu filtern.
	 *
	 * @param ids   die Liste der zu löschenden IDs, vor dem Filtern
	 *
	 * @returns die Liste der zu löschenden IDs, nach dem Filtern
	 */
	protected filterOnDelete(ids: List<number>): List<number> {
		return ids;
	}

	public async delete(): Promise<[boolean, List<string | null>]> {
		const idsEntries = new ArrayList<number>();
		for (const entry of this.liste.auswahlSorted()) {
			idsEntries.add(entry.id);
		}

		const filteredIds = this.filterOnDelete(idsEntries);

		const deleteResponses = await this.doDelete(filteredIds);

		let anyEntryRemoved = false;
		let allEntriesRemoved = true;
		const logMessages = new ArrayList<string>();
		for (const deleteResponse of deleteResponses) {
			if (deleteResponse.success && (deleteResponse.id !== null)) {
				const entry = this.liste.get(deleteResponse.id);
				logMessages.add(this.deleteMessage(deleteResponse.id, entry));
				anyEntryRemoved = true;
			} else {
				allEntriesRemoved = false;
				logMessages.addAll(deleteResponse.log);
			}
		}

		if (anyEntryRemoved) {
			this.liste.auswahlClear();
			await this.ladeSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt);
		}

		return [allEntriesRemoved, logMessages];
	};

}
