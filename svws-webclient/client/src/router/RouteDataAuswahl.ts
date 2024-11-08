import type { List, SimpleOperationResponse} from "@core";
import { ArrayList, DeveloperNotificationException, type AuswahlManager } from "@core";
import { RouteData, type RouteStateInterface } from "./RouteData";
import type { RouteParamsRawGeneric } from "vue-router";
import { RouteManager } from "./RouteManager";
import { RoutingStatus } from "./RoutingStatus";
import { ViewType } from "@ui";
import type { RouteNode } from "./RouteNode";

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
	private routeGruppenprozesse: RouteNode<any,any>;

	/** Die Route für das Hinzufügen */
	private routeHinzufuegen: RouteNode<any,any>;


	/**
	 * Erzeugt ein neues Route-Daten-Objekt mit dem übergebenen Default-State.
	 * Optional können noch gültige Sichten/Child Routes übergeben werden, sofern diese
	 * hier genutzt werden.
	 *
	 * @param defaultState   der Default-State
	 */
	protected constructor(defaultState : RouteState, routeGruppenprozesse: RouteNode<any,any>, routeHinzufuegen: RouteNode<any,any>) {
		super(defaultState);
		this.routeGruppenprozesse = routeGruppenprozesse;
		this.routeHinzufuegen = routeHinzufuegen;
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
		if (this._state.value.manager === undefined)
			throw new DeveloperNotificationException("Abfrage des Managers ohne vorige Initialisierung");
		return this._state.value.manager;
	}


	/**
	 * Die Methode muss überschrieben werden und erstellt einen neuen Auswahl-Manager
	 * für den angegebenen Schuljahresabschnitt. Daber werden benötigte Daten über die API geladen.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	protected abstract createManager(idSchuljahresabschnitt : number) : Promise<TAuswahlManager>;


	/**
	 * Setzt die Daten zum ausgewählten Schuljahresabschnitt und triggert damit das Laden der Defaults für diesen Abschnitt
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	public async setSchuljahresabschnitt(idSchuljahresabschnitt : number, isEntering: boolean) : Promise<number | null> {
		// Wenn die ID des Schuljahresabschnittes bereits gesetzt ist, dann muss der Manager für den Schuljahresabschnitt nicht neu initialisiert werden
		if (!isEntering && (idSchuljahresabschnitt === this._state.value.idSchuljahresabschnitt))
			return null;
		// Ansonsten lade den Schuljahresabschnitt...
		return this.ladeSchuljahresabschnitt(idSchuljahresabschnitt);
	}


	/**
	 * Lädt die Daten zum ausgewählten Schuljahresabschnitt und setzt die Default-Werte für den Manager.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 */
	private async ladeSchuljahresabschnitt(idSchuljahresabschnitt : number) : Promise<number | null> {
		const newState = <Partial<RouteState>>{ idSchuljahresabschnitt };
		newState.manager = await this.createManager(idSchuljahresabschnitt);

		// Lade und setze Schüler Stammdaten falls ein Schüler ausgewählt ist und dieser im neuen Manager vorhanden ist
		// const vorherigeAuswahl = ((this._state.value.schuelerListeManager !== undefined) && this.schuelerListeManager.hasDaten()) ? this.schuelerListeManager.auswahl() : null;
		// if (vorherigeAuswahl !== null) {
		// 	const auswahl = this.schuelerListeManager.liste.get(vorherigeAuswahl.id);
		// 	let schuelerStammdaten = await this.ladeDaten(auswahl);
		// 	if (schuelerStammdaten === null)
		// 		schuelerStammdaten = await this.ladeDaten(manager.liste.list().get(0));
		//
		// 	this.schuelerListeManager.setDaten(schuelerStammdaten);
		// }

		// stellt die ursprünglich gefilterte Liste wieder her
		newState.manager.filtered();

		newState.view = (newState.manager.hasDaten()) ? this._state.value.view : this.defaultView;
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
	public abstract addID(param: RouteParamsRawGeneric, id: number) : void;


	/**
	 * Gibt den Eintrag des Managers passend zu dem übergebenen Schlüssel zurück. Ist dieser nicht vorhanden,
	 * so wird als Default der erste Wert der gefilterten Liste zurückgegeben. Ist diese leer, so wird null
	 * zurückgegeben.
	 *
	 * @param id   die ID des Eintrags
	 *
	 * @returns der Eintrag
	 */
	public getEintragOrDefault(id: number) : TAuswahl | null {
		if (this.manager.liste.has(id))
			return this.manager.liste.get(id);
		return this.manager.filtered().isEmpty() ? null : this.manager.filtered().get(0);
	}


	/**
	 * Die Methode muss überschrieben werden und kümmert sich um das Nachladen von Daten, wenn in der
	 * Auswahl ein neuer Eintrag ausgewählt wird.
	 *
	 * @param auswahl   die neu Auswahl oder null
	 *
	 * @returns die geladenen Daten oder null
	 */
	public abstract ladeDaten(auswahl: TAuswahl | null) : Promise<TDaten | null>;

	/**
	 * Setzt die Auswahl in der Liste und lädt die Daten zu der Auswahl in den Manager.
	 *
	 * @param auswahl   die Auswahl
	 */
	public async setDaten(auswahl: TAuswahl | null): Promise<void> {
		const hatteDaten = this.manager.hasDaten();
		if ((auswahl === null) && (!hatteDaten))
			return;

		if ((auswahl === null) || (this.manager.liste.list().isEmpty())) {
			this.manager.setDaten(null);
			this.commit();
			return;
		}

		const id = this.manager.getIdByEintrag(auswahl);
		if (hatteDaten && (id === this.manager.getIdByEintrag(this.manager.auswahl())))
			return;

		const eintrag = this.getEintragOrDefault(id);
		const daten = await this.ladeDaten(eintrag);
		this.manager.setDaten(daten);
		this.commit();
	}


	/**
	 * Setzt den Filter neu und lädt den ersten Eintrag aus der gefilterten Liste
	 */
	setFilter = async () : Promise<void> => {
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
	protected abstract doPatch(data : Partial<TDaten>, id: number) : Promise<void>;

	/**
	 * Führt einen Patch auf den aktuellen Eintrag mit den übergebenen Daten aus.
	 *
	 * @param data   die Daten für den Patch
	 */
	patch = async (data : Partial<TDaten>) => {
		if (!this.manager.hasDaten())
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const id = this.manager.getIdByEintrag(this.manager.auswahl());
		const stammdaten = this.manager.daten();
		await this.doPatch(data, id);
		Object.assign(stammdaten as object, data);
		this.manager.setDaten(stammdaten);
		this.commit();
	}


	protected abstract doDelete(ids: List<number>): Promise<List<SimpleOperationResponse>>;

	protected abstract deleteMessage(id: number, eintrag: TAuswahl | null) : string;

	public deleteSchueler = async (): Promise<[boolean, List<string | null>]> => {
		const ids = new ArrayList<number>();
		for (const schueler of this.manager.liste.auswahlSorted())
			ids.add(this.manager.getIdByEintrag(schueler));

		const operationResponses = await this.doDelete(ids);

		const schuelerToRemove = new ArrayList<TAuswahl>();
		const logMessages = new ArrayList<string | null>();
		let status = true;
		for (const response of operationResponses) {
			if (response.success && (response.id !== null)) {
				const schueler = this.manager.liste.get(response.id);
				logMessages.add(this.deleteMessage(response.id, schueler));
				schuelerToRemove.add(schueler);
			} else {
				status = false;
				logMessages.addAll(response.log);
			}
		}
		if (!schuelerToRemove.isEmpty()) {
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
		this.activeViewType = ViewType.DEFAULT
		this._state.value.view = (this._state.value.view?.name === this.view.name) ? this.view : this.defaultView;
		this.commit();
	}


	/**
	 * Lädt die Default-Ansicht - also nicht die Ansicht für das Hinzufügen oder die Gruppenprozesse.
	 *
	 * @param id   die zu setzende ID oder null
	 */
	gotoDefaultView = async (id?: number | null) : Promise<void> => {
		const params = {};
		if ((id !== null) && (id !== undefined) && this.manager.liste.has(id)) {
			this.addID(params, id);
			const route = ((this.view !== this.routeHinzufuegen) && (this.view !== this.routeGruppenprozesse))
				? this.view.getRoute(params) : this.defaultView.getRoute(params);
			const result = await RouteManager.doRoute(route);
			if (result === RoutingStatus.STOPPED_ROUTING_IS_ACTIVE){
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
		if ((this.activeViewType === ViewType.GRUPPENPROZESSE) || (this._state.value.view === this.routeGruppenprozesse)) {
			this.commit();
			return;
		}

		this.activeViewType = ViewType.GRUPPENPROZESSE;

		if (navigate)
			await RouteManager.doRoute(this.routeGruppenprozesse.getRoute());

		this._state.value.view = this.routeGruppenprozesse;
		this.manager.setDaten(null);
		this.commit();
	}

	/**
	 * Lädt die Ansicht für das Hinzufügen von Daten
	 *
	 * @param navigate   gibt an, ob ein Routing durchgeführt werden soll oder nur die View im State gesetzt werden soll
	 */
	gotoHinzufuegenView = async (navigate: boolean) => {
		if ((this.activeViewType === ViewType.HINZUFUEGEN) || (this._state.value.view === this.routeHinzufuegen)) {
			this.commit();
			return;
		}

		this.activeViewType = ViewType.HINZUFUEGEN;

		if (navigate) {
			const result = await RouteManager.doRoute(this.routeHinzufuegen.getRoute());
			if (result === RoutingStatus.SUCCESS)
				this.manager.liste.auswahlClear();
		}

		this._state.value.view = this.routeHinzufuegen;
		this.manager.setDaten(null);
		this.commit();
	}

}
