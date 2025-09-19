import type { JavaMap, List, SimpleOperationResponse } from "@core";
import { ArrayList, DeveloperNotificationException, HashMap } from "@core";
import { RouteData, type RouteStateInterface } from "./RouteData";
import type { RouteParamsRawGeneric } from "vue-router";
import { RouteManager } from "./RouteManager";
import { RoutingStatus } from "./RoutingStatus";
import { ViewType } from "@ui";
import type { AuswahlManager } from "@ui";
import type { RouteNode } from "./RouteNode";
import { PendingStateManagerRegistry } from "~/router/PendingStateManagerRegistry";

/**
 * Die Definition von gemeinsamen Attributen des States von Routen.
 */
export interface RouteStateAuswahlInterface<TAuswahlManager extends AuswahlManager<number, TAuswahl, TDaten>, TAuswahl = any, TDaten = any> extends RouteStateInterface {
	idSchuljahresabschnitt: number;
	manager: TAuswahlManager | undefined;
}


/**
 * Eine abstrakte Klasse für allgemeine Methoden beim Zugriff auf die Daten, welche einer Route
 * zugeordnet sind. Hier auch mit Methoden für die Handhabung von Auswahl-Managern einer Auswahlliste.
 * Erweitert die allgemeine Klasse RouteData.
 */
export abstract class RouteDataAuswahl<TAuswahlManager extends AuswahlManager<number, TAuswahl, TDaten>, RouteState extends RouteStateAuswahlInterface<TAuswahlManager>, TAuswahl = any, TDaten = any>
	extends RouteData<RouteState> {


	/** Die Route für Gruppenprozesse */
	private readonly _routeGruppenprozesse: RouteNode<any, any> | undefined;

	/** Die Route für das Hinzufügen */
	private readonly _routeHinzufuegen: RouteNode<any, any> | undefined;

	/** Die Route für die Schnelleingabe */
	private readonly _routeSchnelleingabe: RouteNode<any, any> | undefined;

	private _pendingStateManagerRegistry: PendingStateManagerRegistry;

	/**
	 * Erzeugt ein neues Route-Daten-Objekt mit dem übergebenen Default-State.
	 * Optional können noch gültige Sichten/Child Routes übergeben werden, sofern diese
	 * hier genutzt werden.
	 *
	 * @param defaultState   der Default-State
	 * @param routeGruppenprozesse Route für Gruppenprozesse
	 * @param routeHinzufuegen Route für Hinzufügen
	 * @param _routeSchnelleingabe Route für Schnelleingabe
	 */
	protected constructor(defaultState: RouteState, routes: { gruppenprozesse?: RouteNode<any, any>, hinzufuegen?: RouteNode<any, any>, schnelleingabe?: RouteNode<any, any> }) {
		super(defaultState);
		this._routeGruppenprozesse = routes.gruppenprozesse;
		this._routeHinzufuegen = routes.hinzufuegen;
		this._routeSchnelleingabe = routes.schnelleingabe;
		this._pendingStateManagerRegistry = new PendingStateManagerRegistry();
	}

	/**
	 * Gibt den Pending State Manager Registry zurück.
	 * Falls dieser noch nicht initialisiert wurde, wird eine DeveloperNotificationException geworfen.
	 *
	 * @throws DeveloperNotificationException falls der Zugriff vor der Initialisierung erfolgt.
	 * @returns der Pending State Manager Registry
	 */
	get pendingStateManagerRegistry(): PendingStateManagerRegistry {
		return this._pendingStateManagerRegistry;
	}

	/**
	 * Die Methode, um einen Dummy-Manager zurückzugeben, falls bisher kein Manager initialisiert wurde.
	 *
	 * @returns der Dummy-Manager
	 */
	protected getDummyManager(): TAuswahlManager | undefined {
		return undefined;
	}

	/**
	 * Gibt zurück, ob ein initialisierter Manager existiert.
	 */
	get hasManager(): boolean {
		return (this._state.value.manager !== undefined);
	}

	/**
	 * Gibt den Auswahl-Manager zurück.
	 */
	get manager(): TAuswahlManager {
		if (this._state.value.manager === undefined) {
			const dummy = this.getDummyManager();
			if (dummy !== undefined)
				return dummy;
			throw new DeveloperNotificationException("Abfrage des Managers ohne vorige Initialisierung");
		}
		return this._state.value.manager;
	}


	/**
	 * Die Methode muss überschrieben werden und erstellt einen neuen Auswahl-Manager
	 * für den angegebenen Schuljahresabschnitt. Daher werden benötigte Daten über die API geladen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	protected abstract createManager(idSchuljahresabschnitt: number): Promise<Partial<RouteState>>;


	/**
	 * Setzt die Daten zum ausgewählten Schuljahresabschnitt und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param isEntering Gibt an, ob die zugehörige Route initial betreten wird
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt: number, isEntering: boolean): Promise<number | null> {
		// Wenn die ID des Schuljahresabschnittes bereits gesetzt ist, dann muss der Manager für den Schuljahresabschnitt nicht neu initialisiert werden
		if (!isEntering && (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt))
			return null;
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
	 */
	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt: number): Promise<number | null> {
		const newState = await this.createManager(idSchuljahresabschnitt);
		const manager = newState.manager;
		if (manager === undefined)
			throw new DeveloperNotificationException('Die Methode createManager muss einen gültigen State mit einem initialisierten Manager liefern');
		newState.idSchuljahresabschnitt = idSchuljahresabschnitt;

		// Lade und setze Daten falls eine Auswahl bestand und diese im neuen Manager vorhanden ist
		const vorherigeAuswahl = ((this._state.value.manager !== undefined) && this.manager.hasDaten()) ? this.manager.auswahl() : null;
		if (vorherigeAuswahl !== null) {
			const auswahl = manager.liste.get(this.manager.getIdByEintrag(vorherigeAuswahl));
			let daten = await this.ladeDaten(auswahl, newState);
			if ((daten === null) && (!manager.liste.list().isEmpty()))
				daten = await this.ladeDaten(manager.liste.list().getFirst(), newState);
			manager.setDaten(daten);
			if (daten !== null)
				await this.updateManager(manager, this.manager, daten);
		}

		// stellt die ursprünglich gefilterte Liste wieder her
		manager.filtered();

		newState.view = (manager.hasDaten()) ? this._state.value.view : this.defaultView;
		newState.activeViewType = this.activeViewType;

		this.setPatchedDefaultState(newState);
		return this.manager.auswahlID();
	}


	/**
	 * Fügt die übergebene ID zu den übergebenen Route-Parametern hinzu
	 *
	 * @param param   die Parameter
	 * @param id      die ID
	 */
	public abstract addID(param: RouteParamsRawGeneric, id: number): void;


	/**
	 * Gibt den Eintrag des Managers passend zu dem übergebenen Schlüssel zurück. Ist dieser nicht vorhanden,
	 * so wird als Default der erste Wert der gefilterten Liste zurückgegeben. Ist diese leer, so wird null
	 * zurückgegeben.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @returns der Eintrag
	 */
	public getEintragOrDefault(id: number): TAuswahl | null {
		if (this.manager.liste.has(id))
			return this.manager.liste.get(id);
		return this.manager.filtered().isEmpty() ? null : this.manager.filtered().get(0);
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
	public abstract ladeDaten(auswahl: TAuswahl | null, state: Partial<RouteState>): Promise<TDaten | null>;


	/**
	 * Die Methode kann überschrieben werden und kümmert sich um das Nachladen von Daten, wenn in der
	 * Auswahl mehrere Eintrag gleichzeitig ausgewählt werden.
	 *
	 * @param auswahlList   die neu Auswahl oder null
	 * @param state     der State, bei welchem die Daten angepasst werden
	 *
	 * @returns die geladenen Daten oder null
	 * @throws DeveloperNotificationException wenn Methode aufgerufen aber nicht überschrieben wurde
	 */
	public async ladeDatenMultiple(auswahlList: List<TAuswahl> | null, state: Partial<RouteState>): Promise<List<TDaten> | null> {
		throw new DeveloperNotificationException("Die Methode ladeDatenMultiple() ist nicht implementiert.")
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
		if ((auswahl === null) && (!hatteDaten))
			return;

		if ((auswahl === null)/* || (this.manager.liste.list().isEmpty())*/) {
			this.manager.setDaten(null);
			this.commit();
			return;
		}

		const id = this.manager.getIdByEintrag(auswahl);
		if (hatteDaten && (id === this.manager.getIdByEintrag(this.manager.auswahl())))
			return;

		const eintrag = this.getEintragOrDefault(id);
		const daten = await this.ladeDaten(eintrag, this._state.value);
		await this.updateDaten(daten);
		this.commit();
	}


	/**
	 * Setzt den Filter neu und lädt den ersten Eintrag aus der gefilterten Liste
	 */
	setFilter = async (): Promise<void> => {
		if (!this.manager.hasDaten() && (this.activeViewType === ViewType.DEFAULT)) {
			const listFiltered = this.manager.filtered();
			if (!listFiltered.isEmpty()) {
				const id = this.manager.getIdByEintrag(listFiltered.getFirst());
				await this.gotoDefaultView(id);
				return;
			}
		}
		this.commit();
	}


	/**
	 * Diese Methode muss implementiert werden, um den eigentlichen Patch über die API auszuführen.
	 *
	 * @param data   die Daten für den Patch
	 * @param id     die ID der zu patchenden Daten
	 */
	protected abstract doPatch(data: Partial<TDaten>, id: number): Promise<void>;

	/**
	 * Führt einen Patch auf den aktuellen Eintrag mit den übergebenen Daten aus.
	 *
	 * @param data   die Daten für den Patch
	 */
	patch = async (data: Partial<TDaten>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const id = this.manager.getIdByEintrag(this.manager.auswahl());
		const daten = this.manager.daten();
		await this.doPatch(data, id);
		Object.assign(daten as object, data);
		this.manager.setDaten(daten);
		this.commit();
	}


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

	public delete = async (): Promise<[boolean, List<string | null>]> => {
		let ids: List<number> = new ArrayList();
		for (const eintrag of this.manager.liste.auswahlSorted())
			ids.add(this.manager.getIdByEintrag(eintrag));
		ids = this.filterOnDelete(ids);

		const operationResponses = await this.doDelete(ids);

		const eintraegeToRemove = new ArrayList<TAuswahl>();
		const logMessages = new ArrayList<string | null>();
		let status = true;
		for (const response of operationResponses) {
			if (response.success && (response.id !== null)) {
				const eintrag = this.manager.liste.get(response.id);
				logMessages.add(this.deleteMessage(response.id, eintrag));
				eintraegeToRemove.add(eintrag);
			} else {
				status = false;
				logMessages.addAll(response.log);
			}
		}
		if (!eintraegeToRemove.isEmpty()) {
			this.manager.liste.auswahlClear();
			this.manager.setDaten(null);
			await this.ladeSchuljahresabschnitt(this._state.value.idSchuljahresabschnitt);
		}

		return [status, logMessages];
	}


	/**
	 * Interne Hilfsmethode: Setzt den View-Type auf DEFAULT und entweder diesem Knoten oder die Default-View
	 */
	private setDefaults() {
		this.manager.liste.auswahlClear();
		this.activeViewType = ViewType.DEFAULT
		this._state.value.view = (this._state.value.view?.name === this.view.name) ? this.view : this.defaultView;
		this.commit();
	}


	/**
	 * Lädt die Default-Ansicht - also nicht die Ansicht für das Hinzufügen oder die Gruppenprozesse.
	 *
	 * @param id   die zu setzende ID oder null
	 */
	gotoDefaultView = async (id?: number | null): Promise<void> => {
		const params = {};
		if ((id !== null) && (id !== undefined) && this.manager.liste.has(id)) {
			this.addID(params, id);
			const route = (this.activeViewType !== ViewType.HINZUFUEGEN) && (this.activeViewType !== ViewType.GRUPPENPROZESSE) && (this.activeViewType !== ViewType.NEU)
				? this.view.getRoute(params) : this.defaultView.getRoute(params);
			const result = await RouteManager.doRoute(route);
			if (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE) {
				const eintrag = this.getEintragOrDefault(id);
				await this.setDaten(eintrag);
			}

			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE))
				this.setDefaults();

			this.commit();
			return;
		}

		// Default Eintrag selektieren, wenn keine ID übergeben wurde
		const filtered = this.manager.filtered();
		if (!filtered.isEmpty()) {
			const eintrag = filtered.getFirst();
			this.addID(params, this.manager.getIdByEintrag(eintrag));
			const route = this.defaultView.getRoute(params);
			const result = await RouteManager.doRoute(route);
			if ((result === RoutingStatus.SUCCESS) || (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE))
				this.setDefaults();

			await this.setDaten(eintrag);
		}
	}

	/**
	 * Lädt die Gruppenprozess-Ansicht
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die View im State gesetzt werden soll
	 */
	gotoGruppenprozessView = async (navigate: boolean) => {
		// Hier werden nur Daten geladen, wenn eine "neue" Gruppenprozess Route betreten wird
		// TODO: Sobald alle Routen auf die "neue" Gruppenprozesslogik umgestellt wurden kann das if weg
		if (this._defaultState.gruppenprozesseView !== undefined) {
			const currentSelection: JavaMap<number, TDaten> = this.manager.getListeDaten();
			const newSelection: JavaMap<number, TDaten> = new HashMap();
			const deltaSelection: List<TAuswahl> = new ArrayList();
			for (const eintrag of this.manager.liste.auswahl()) {
				const id = this.manager.getIdByEintrag(eintrag);
				const daten = currentSelection.get(id);
				if (daten === null)
					deltaSelection.add(eintrag);
				else
					newSelection.put(id, daten);
			}

			if (deltaSelection.size() > 0) {
				const deltaDaten = await this.ladeDatenMultiple(deltaSelection, this._state.value);
				if (deltaDaten === null)
					throw new DeveloperNotificationException("Fehler beim Laden der Daten. Es konnten keine Daten zu den ausgewählten Einträgen geladen werden.");

				for (const datenObj of deltaDaten) {
					newSelection.put(this.manager.getIdByDaten(datenObj), datenObj);
				}
			}
			currentSelection.clear();
			currentSelection.putAll(newSelection);
		}

		if (this.activeViewType === ViewType.GRUPPENPROZESSE) {
			this.commit();
			return;
		}

		this.activeViewType = ViewType.GRUPPENPROZESSE;

		if (navigate) {
			// TODO: Sobald alle Routen auf die "neue" Gruppenprozesslogik umgestellt wurden kann das if weg
			if (this._defaultState.gruppenprozesseView !== undefined) {
				await RouteManager.doRoute(this.defaultGruppenprozesseView.getRoute());
				this._state.value.view = (this._state.value.view?.name === this.view.name) ? this.view : this.defaultGruppenprozesseView;
			} else if (this._routeGruppenprozesse !== undefined) {
				await RouteManager.doRoute(this._routeGruppenprozesse.getRoute());
				this._state.value.view = this._routeGruppenprozesse
			} else
				throw new DeveloperNotificationException('Es wurde keine Standard Route für Gruppenprozesse festgelegt!');
		}
		this.commit();
	}

	/**
	 * Lädt die Ansicht für das Hinzufügen von Daten
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die View im State gesetzt werden soll
	 */
	gotoHinzufuegenView = async (navigate: boolean) => {
		if (this._routeHinzufuegen === undefined)
			throw new DeveloperNotificationException("Es wurde keine Route definiert, um Daten in der Auswahlliste zu ergänzen.");

		if ((this.activeViewType === ViewType.HINZUFUEGEN) || (this._state.value.view === this._routeHinzufuegen)) {
			this.commit();
			return;
		}

		this.activeViewType = ViewType.HINZUFUEGEN;
		if (navigate) {
			const result = await RouteManager.doRoute(this._routeHinzufuegen.getRoute());
			if (result === RoutingStatus.SUCCESS)
				this.manager.liste.auswahlClear();
		}

		this._state.value.view = this._routeHinzufuegen;
		this.commit();
	}

	/**
	 * Lädt die Ansicht für die Schnelleingabe von Daten
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die View im State gesetzt werden soll
	 * @param id         die ID des Schülers, zu dessen Schnelleingabe navigiert werden soll
	 */
	gotoSchnelleingabeView = async (navigate: boolean, id?: number | null) => {
		if (this._routeSchnelleingabe === undefined)
			throw new DeveloperNotificationException("Es wurde keine Route definiert, um Daten in der Auswahlliste zu ergänzen.");

		if ((this.activeViewType === ViewType.NEU) || (this._state.value.view === this._routeSchnelleingabe)) {
			return;
		}

		this.activeViewType = ViewType.NEU;

		if (navigate) {
			const params: RouteParamsRawGeneric = {};
			if ((id !== undefined) && (id !== null) && this.manager.liste.has(id))
				this.addID(params, id);

			await RouteManager.doRoute(this._routeSchnelleingabe.getRoute(params));
		}

		this._state.value.view = this._routeSchnelleingabe;
	}
}
