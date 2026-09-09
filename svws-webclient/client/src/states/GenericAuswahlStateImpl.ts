import type { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ArrayList } from "@core/java/util/ArrayList";
import { HashMap } from "@core/java/util/HashMap";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";
import type { AuswahlManager } from "@ui/ui/manager/AuswahlManager";
import { ViewType } from "@ui/ui/nav/ViewType";
import { StateManager } from "@ui/ui/StateManager";

import { RoutingStatus } from "~/router/RoutingStatus";

import type { GenericAuswahlState } from "./GenericAuswahlState";
import type { GenericAuswahlStateRoutingAdapter } from "./GenericAuswahlStateRoutingAdapter";

export interface GenericAuswahlReactiveState<TAuswahlManager extends AuswahlManager<number, TAuswahl, TDaten>, TAuswahl = any, TDaten = any> {
	idSchuljahresabschnitt: number;
	manager: TAuswahlManager | undefined;
	activeViewType: ViewType;
}


/**
 * Die abstrakte Basisklasse für generische States für Auswahllisten
 */
export abstract class GenericAuswahlStateImpl<TAuswahlManager extends AuswahlManager<number, TAuswahl, TDaten>,
	R extends GenericAuswahlReactiveState<TAuswahlManager, TAuswahl, TDaten>, TAuswahl = any, TDaten = any>
	extends StateManager<R> implements GenericAuswahlState<TAuswahlManager> {

	/** Die Route für Gruppenprozesse */
	private readonly _routingAdapter: GenericAuswahlStateRoutingAdapter;


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
		this._routingAdapter = routingAdapter;
	}


	/**
	 * Gibt zurück, ob der State aktuell mit einer gültigen initialisiert und verfügbar ist.
	 */
	public get isAvailable(): boolean {
		return (this._state.value.manager !== undefined);
	}

	/**
	 * Gibt den Auswahl-Manager zurück.
	 *
	 * @throws DeveloperNotificationException wenn der State aktuell nicht initialisiert ist
	 */
	public get manager(): TAuswahlManager {
		if (this._state.value.manager === undefined) {
			throw new DeveloperNotificationException("Abfrage des Managers ohne vorige Initialisierung");
		}
		return this._state.value.manager;
	}


	/**
	 * Die Methode muss überschrieben werden und erzeugt dann einen neuen Auswahl-Manager
	 * für den angegebenen Schuljahresabschnitt.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	protected abstract createManager(idSchuljahresabschnitt: number): Promise<Partial<R>>;


	/**
	 * Initialisiert den State für den ausgewählten Schuljahresabschnitt. Es werden die Daten für diesen Abschnitt geladen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param force                    gibt an, ob das Laden der Daten auch erzwungen werden soll, wenn die ID des
	 *                                 Schuljahresabschnittes bereits gesetzt ist (z.B. beim Betreten einer Route)
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number, force: boolean): Promise<number | null> {
		// Wenn die ID des Schuljahresabschnittes bereits gesetzt ist, dann muss der Manager für den Schuljahresabschnitt nicht neu initialisiert werden
		if (!force && (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt)) {
			return null;
		}
		// Ansonsten lade den Schuljahresabschnitt...
		return this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
	}

	/**
	 * Diese Methode kann überschrieben werden, wenn weitere Aktualisierungen beim Ersetzen des
	 * Managers bei einem Wechsel des Schuljahresabschnittes durchgeführt werden sollen und eine
	 * vorige Auswahl bestand.
	 *
	 * @param manager      der neue Manager
	 * @param managerAlt   der alte Manager
	 * @param daten        die neuen Daten
	 */
	protected async updateManager(manager: TAuswahlManager, managerAlt: TAuswahlManager, daten: TDaten): Promise<void> {
		// nichts zu tun
	}


	/**
	 * Lädt die Daten zum ausgewählten Schuljahresabschnitt und setzt die Default-Werte für den Manager.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @returns ein Promise mit der ID der Auswahl oder null
	 */
	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt: number): Promise<number | null> {
		const newState = await this.createManager(idSchuljahresabschnitt);
		const manager = newState.manager;
		if (manager === undefined) {
			throw new DeveloperNotificationException('Die Methode createManager muss einen gültigen State mit einem initialisierten Manager liefern');
		}
		newState.idSchuljahresabschnitt = idSchuljahresabschnitt;

		// Lade und setze Daten falls eine Auswahl bestand und diese im neuen Manager vorhanden ist
		const vorherigeAuswahl = ((this._state.value.manager !== undefined) && this.manager.hasDaten()) ? this.manager.auswahl() : null;
		if (vorherigeAuswahl !== null) {
			const auswahl = manager.liste.get(this.manager.getIdByEintrag(vorherigeAuswahl));
			let daten = await this.ladeDaten(auswahl, newState);
			if ((daten === null) && (!manager.liste.list().isEmpty())) {
				daten = await this.ladeDaten(manager.liste.list().getFirst(), newState);
			}
			manager.setDaten(daten);
			if (daten !== null) {
				await this.updateManager(manager, this.manager, daten);
			}
		}

		// stellt die ursprünglich gefilterte Liste wieder her
		manager.filtered();

		if (manager.hasDaten()) {
			newState.activeViewType = this.activeViewType;
		} else {
			newState.activeViewType = ViewType.DEFAULT;
			this._routingAdapter.setDefaultView(ViewType.DEFAULT);
		}

		this.setPatchedDefaultState(newState);
		return this.manager.auswahlID();
	}


	/**
	 * Die Methode muss überschrieben werden und kümmert sich um das Nachladen von Daten, wenn in der
	 * Auswahl ein neuer Eintrag ausgewählt wird.
	 *
	 * @param auswahl   die neu Auswahl oder null
	 * @param state     der State, bei welchem die Daten angepasst werden
	 *
	 * @returns die geladenen Daten oder null
	 */
	public abstract ladeDaten(auswahl: TAuswahl | null, state: Partial<R>): Promise<TDaten | null>;


	/**
	 * Die Methode kann überschrieben werden und kümmert sich um das Nachladen von Daten, wenn in der
	 * Auswahl mehrere Eintrag gleichzeitig ausgewählt werden.
	 *
	 * @param auswahlList   die neue Auswahl
	 * @param state     der State, bei welchem die Daten angepasst werden
	 *
	 * @returns die geladenen Daten oder null
	 * @throws DeveloperNotificationException wenn Methode aufgerufen aber nicht überschrieben wurde
	 */
	public async ladeDatenMultiple(auswahlList: List<TAuswahl>, state: Partial<R>): Promise<List<TDaten> | null> {
		throw new DeveloperNotificationException("Die Methode ladeDatenMultiple() ist nicht implementiert.");
	}


	/**
	 * Diese Methode kann überschrieben werden. Sie aktualisiert die Daten im Manager.
	 *
	 * @param daten   die im Manager zu aktualisierenden Daten
	 */
	protected async updateDaten(daten: TDaten | null) {
		this.manager.setDaten(daten);
	}

	/**
	 * Setzt die Auswahl in der Liste und lädt die Daten zu der Auswahl in den Manager.
	 *
	 * @param auswahl   die Auswahl
	 */
	public async setDaten(auswahl: TAuswahl | null): Promise<void> {
		const hatteDaten = this.manager.hasDaten();
		if ((auswahl === null) && (!hatteDaten)) {
			return;
		}

		if ((auswahl === null)/* || (this.manager.liste.list().isEmpty())*/) {
			this.manager.setDaten(null);
			this.commit();
			return;
		}

		const id = this.manager.getIdByEintrag(auswahl);
		if (hatteDaten && (id === this.manager.getIdByEintrag(this.manager.auswahl()))) {
			return;
		}

		const eintrag = this.manager.getEintragOrDefault(id);
		const daten = await this.ladeDaten(eintrag, this._state.value);
		await this.updateDaten(daten);
		this.commit();
	}


	/**
	 * Setzt den Filter neu und lädt den ersten Eintrag aus der gefilterten Liste
	 */
	public async setFilter(): Promise<void> {
		if (!this.manager.hasDaten() && (this.activeViewType === ViewType.DEFAULT)) {
			const listFiltered = this.manager.filtered();
			if (!listFiltered.isEmpty()) {
				const id = this.manager.getIdByEintrag(listFiltered.getFirst());
				await this.gotoDefaultView(id);
				return;
			}
		}
		this.commit();
	};


	/**
	 * Diese Methode muss implementiert werden, um den eigentlichen Patch über die API auszuführen.
	 *
	 * @param data   die Daten für den Patch
	 * @param id     die ID der zu patchenden Daten
	 */
	protected abstract doPatch(data: Partial<TDaten>, id: number): Promise<boolean>;

	/**
	 * Führt einen Patch auf den aktuellen Eintrag mit den übergebenen Daten aus.
	 *
	 * @param data   die Daten für den Patch
	 */
	public async patch(data: Partial<TDaten>): Promise<boolean> {
		if (!this.manager.hasDaten()) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}
		const id = this.manager.getIdByEintrag(this.manager.auswahl());
		const daten = this.manager.daten();
		await this.doPatch(data, id);
		Object.assign(daten as object, data);
		this.manager.setDaten(daten);
		this.commit();
		return true;
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
		for (const entry of this.manager.liste.auswahlSorted()) {
			idsEntries.add(this.manager.getIdByEintrag(entry));
		}

		const filteredIds = this.filterOnDelete(idsEntries);

		const deleteResponses = await this.doDelete(filteredIds);

		let anyEntryRemoved = false;
		let allEntriesRemoved = true;
		const logMessages = new ArrayList<string>();
		for (const deleteResponse of deleteResponses) {
			if (deleteResponse.success && (deleteResponse.id !== null)) {
				const entry = this.manager.liste.get(deleteResponse.id);
				logMessages.add(this.deleteMessage(deleteResponse.id, entry));
				anyEntryRemoved = true;
			} else {
				allEntriesRemoved = false;
				logMessages.addAll(deleteResponse.log);
			}
		}

		if (anyEntryRemoved) {
			this.manager.liste.auswahlClear();
			await this.ladeSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt);
		}

		return [allEntriesRemoved, logMessages];
	};


	/**
	 * Gibt die aktuelle Art der aktuellen View zurück (Default, Hinzufügen oder Gruppenprozess, Schnelleingabe).
	 */
	public get activeViewType(): ViewType {
		return this._state.value.activeViewType;
	}

	/**
	 * Setzt die aktuelle Art der View zurück (Default, Hinzufügen oder Gruppenprozess).
	 *
	 * @param value   die Art der View
	 */
	protected set activeViewType(value: ViewType) {
		this._state.value.activeViewType = value;
	}


	/**
	 * Lädt die Default-Ansicht - also nicht die Ansicht für das Hinzufügen oder die Gruppenprozesse.
	 *
	 * @param id   die zu setzende ID oder null
	 */
	public async gotoDefaultView(id?: number | null): Promise<void> {
		if ((id !== null) && (id !== undefined) && this.manager.liste.has(id)) {
			// Führe das Routing aus
			const useDefaultRoute = this.activeViewType !== ViewType.DEFAULT;
			const result = await this._routingAdapter.goto(id, useDefaultRoute);

			// Wenn das Routing unterbrochen wurde, dann wird der Eintrag beim Manager gesetzt
			if (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE) {
				const eintrag = this.manager.getEintragOrDefault(id);
				await this.setDaten(eintrag);
			}

			// Entferne ggf. eine Mehrfachauswahl, wenn von den Gruppenprozessen zur Einzelauswahl gewechselt wurde
			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE)) {
				this.manager.liste.auswahlClear();
				this.activeViewType = ViewType.DEFAULT;
			}

			this.commit();
			return;
		}

		// Wenn keine ID übergeben wurde, so wird ein Default-Eintrag ausgewählt, sofern der aktuelle Filter ein nicht leeres Ergebnis liefert
		const filtered = this.manager.filtered();
		if (!filtered.isEmpty()) {
			const eintrag = filtered.getFirst();

			// Führe das Routing aus.
			const idDefault = this.manager.getIdByEintrag(eintrag);
			const result = await this._routingAdapter.goto(idDefault, true);

			// Entferne ggf. eine Mehrfachauswahl, wenn von den Gruppenprozessen zur Einzelauswahl gewechselt wurde
			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE)) {
				this.manager.liste.auswahlClear();
				this.activeViewType = ViewType.DEFAULT;
				this.commit();
			}

			// Wähle den neuen Eintrag aus
			await this.setDaten(eintrag);
		}
	};

	/**
	 * Lädt die Gruppenprozess-Ansicht
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die View im State gesetzt werden soll
	 */
	public async gotoGruppenprozessView(navigate: boolean = true) {
		// Prüfe, ob die Auswahl überhaupt Gruppenprozesse unterstützt
		if (!this._routingAdapter.hatGruppenprozesse) {
			throw new DeveloperNotificationException("Es wird keine Ansicht für Gruppenprozesse unterstützt. Daher darf diese Methode nicht aufgerufen werden.");
		}

		// Bilde die Differenz (deltaSelection) zwischen der aktuellen Auswahl (currentSelection) und der neuen Auswahl (new Selection)
		const currentSelection: JavaMap<number, TDaten> = this.manager.getListeDaten();
		const newSelection: JavaMap<number, TDaten> = new HashMap();
		const deltaSelection: List<TAuswahl> = new ArrayList();
		for (const eintrag of this.manager.liste.auswahl()) {
			const id = this.manager.getIdByEintrag(eintrag);
			const daten = currentSelection.get(id);
			if (daten === null) {
				deltaSelection.add(eintrag);
			} else {
				newSelection.put(id, daten);
			}
		}

		// Wenn es ein delta gibt, so müssen die Datensätze nachgeladen werden
		if (deltaSelection.size() > 0) {
			const deltaDaten = await this.ladeDatenMultiple(deltaSelection, this._state.value);
			if (deltaDaten === null) {
				throw new DeveloperNotificationException("Fehler beim Laden der Daten. Es konnten keine Daten zu den ausgewählten Einträgen geladen werden.");
			}

			for (const datenObj of deltaDaten) {
				newSelection.put(this.manager.getIdByDaten(datenObj), datenObj);
			}
		}

		// Aktualisiere dann die aktuelle Auswahl
		currentSelection.clear();
		currentSelection.putAll(newSelection);

		// Prüfe, ob die Ansicht gewechselt werden muss
		if (this.activeViewType !== ViewType.GRUPPENPROZESSE) {
			this.activeViewType = ViewType.GRUPPENPROZESSE;
			if (navigate) {
				await this._routingAdapter.gotoGruppenprozesse(true);
			}
		}
		this.commit();
	};

	/**
	 * Lädt die Ansicht für das Hinzufügen von Daten
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die View im State gesetzt werden soll
	 */
	public async gotoHinzufuegenView(navigate: boolean) {
		// Prüfe, ob die Auswahl überhaupt das Hinzufügen unterstützt
		if (!this._routingAdapter.hatHinzufuegen) {
			throw new DeveloperNotificationException("Es wird keine Ansicht für das Hinzufügen von Daten unterstützt. Daher darf diese Methode nicht aufgerufen werden.");
		}

		// Führe ggf. einen Wechsel der Ansicht durch
		if (this.activeViewType !== ViewType.HINZUFUEGEN) {
			this.activeViewType = ViewType.HINZUFUEGEN;
			if (navigate) {
				const result = await this._routingAdapter.gotoHinzufuegen();
				if (result === RoutingStatus.SUCCESS) {
					this.manager.liste.auswahlClear();
				}
			} else {
				this._routingAdapter.setViewHinzufuegen();
			}
			this.commit();
		}
	};

	/**
	 * Lädt die Ansicht für die Schnelleingabe von Daten
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die View im State gesetzt werden soll
	 * @param id         die ID des Schülers, zu dessen Schnelleingabe navigiert werden soll
	 */
	public async gotoSchnelleingabeView(navigate: boolean, id?: number | null) {
		// Prüfe, ob die Auswahl überhaupt eine Schnelleingabe unterstützt
		if (!this._routingAdapter.hatSchnelleingabe) {
			throw new DeveloperNotificationException("Es wird keine Ansicht für eine Schnelleingabe von Daten unterstützt. Daher darf diese Methode nicht aufgerufen werden.");
		}

		// Führe ggf. einen Wechsel der Ansicht durch
		if (this.activeViewType !== ViewType.NEU) {
			this.activeViewType = ViewType.NEU;
			if (navigate && (id !== undefined) && (id !== null) && this.manager.liste.has(id)) {
				await this._routingAdapter.gotoSchnelleingabe(id);
			}
			this.commit();
		}
	};

}
