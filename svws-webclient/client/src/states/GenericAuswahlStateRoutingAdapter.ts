import type { RouteParamsRawGeneric } from "vue-router";

import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { RouteData } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";
import type { RoutingStatus } from "~/router/RoutingStatus";

/**
 * Die Konfiguration für einen Routing-Adapter
 */
interface GenericAuswahlRoutingAdapterConfig {

	/** Der Status der Route für die Auswahl */
	route: RouteData<any>,

	/** Die Default-Route für die Ansicht für Gruppenprozess-Operationen */
	gruppenprozesse?: RouteNode<any, any>,

	/** Die Default-Route für die Ansicht für das Hinzufügen von Daten */
	hinzufuegen?: RouteNode<any, any>,

	/** Die Default-Route für die Ansicht für eine Schnelleingabe-Möglichkeit von Daten */
	schnelleingabe?: RouteNode<any, any>,

	/** Die Methode, um die ID als Routen-Parameter zu ergänzen */
	addID: (param: RouteParamsRawGeneric, id: number) => void,
}


/**
 * Diese Klasse dient als Adapter von Auswahl-States zum Routing, so dass je nach Auswahl eine geeignete Route
 * angewählt wird.
 */
export class GenericAuswahlStateRoutingAdapter {

	/** Die Konfiguration des Adapters */
	private readonly _config: GenericAuswahlRoutingAdapterConfig;

	/**
	 * Erstellt einen neuen Adapter mit der übergebenen Konfiguration
	 *
	 * @param config   die Konfiguration des Adapters mit den zu verwendenen Routen
	 */
	public constructor(config: GenericAuswahlRoutingAdapterConfig) {
		this._config = config;
	}

	/**
	 * Setzt die Default-Ansicht für den übergebenen View-Type
	 *
	 * @param type   der ViewType
	 */
	public setDefaultView(type: ViewType): void {
		let view = this._config.route.defaultView;
		if ((type === ViewType.GRUPPENPROZESSE) && (this._config.gruppenprozesse !== undefined)) {
			view = this._config.gruppenprozesse;
		} else if ((type === ViewType.HINZUFUEGEN) && (this._config.hinzufuegen !== undefined)) {
			view = this._config.hinzufuegen;
		} else if ((type === ViewType.NEU) && (this._config.schnelleingabe !== undefined)) {
			view = this._config.schnelleingabe;
		}
		this._config.route.setView(view, [view]);
	}


	/**
	 * Führt ein Routing zu der übergebenen ID der Auswahl in der normalen Ansicht aus.
	 *
	 * @param id               die ID
	 * @param useDefaultView   gibt an, ob die Default-View für die normale Ansicht gesetzt werden soll oder nicht.
	 *
	 * @returns das Ergebnis des Routing-Aufrufs
	 */
	public async goto(id: number, useDefaultView: boolean = false): Promise<RoutingStatus> {
		const params = {};
		this._config.addID(params, id);
		const view = useDefaultView ? this._config.route.defaultView : this._config.route.view;
		const route = view.getRoute(params);
		const result = await RouteManager.doRoute(route);
		if (useDefaultView) {
			this._config.route.setView(view, [view]);
		}
		return result;
	}


	/**
	 * Gibt zurück, ob die Auswahl Ansichten für Gruppenprozesse unterstützt oder nicht.
	 *
	 * @returns true, wenn Ansichten für Gruppenprozesse unterstützt werden, und ansonsten false
	 */
	public get hatGruppenprozesse(): boolean {
		return this._config.gruppenprozesse !== undefined;
	}


	/**
	 * Führt ein Routing zu der Ansicht für Gruppenprozesse aus.
	 *
	 * @param useDefaultView   gibt an, ob die Default-View für die Gruppenprozess-Ansicht gesetzt werden soll oder nicht.
	 *
	 * @returns das Ergebnis des Routing-Aufrufs
	 */
	public async gotoGruppenprozesse(useDefaultView: boolean = false): Promise<RoutingStatus> {
		if (this._config.gruppenprozesse === undefined) {
			throw new DeveloperNotificationException("Keine Route für Gruppenprozesse definiert");
		}
		const view = useDefaultView ? this._config.gruppenprozesse : this._config.route.view;
		const route = view.getRoute();
		const result = await RouteManager.doRoute(route);
		if (useDefaultView) {
			this._config.route.setView(view, [view]);
		}
		return result;
	}


	/**
	 * Gibt zurück, ob die Auswahl Ansichten für das Hinzufügen von Einträgen unterstützt oder nicht.
	 *
	 * @returns true, wenn Ansichten für das Hinzufügen von Einträgen unterstützt werden, und ansonsten false
	 */
	public get hatHinzufuegen(): boolean {
		return this._config.hinzufuegen !== undefined;
	}


	/**
	 * Wechselt in die Ansicht für das Hinzufügen, ohne ein Routing konkret durchzuführen.
	 */
	public setViewHinzufuegen(): void {
		const view = this._config.hinzufuegen;
		if (view === undefined) {
			throw new DeveloperNotificationException("Die Ansicht Hinzufügen wird von diesem Adapter nicht unterstützt.");
		}
		this._config.route.setView(view, [view]);
	}


	/**
	 * Führt ein Routing zu der Ansicht für das Hinzufügen aus.
	 *
	 * @returns eine Promise mit dem RoutingStatus
	 */
	public async gotoHinzufuegen(): Promise<RoutingStatus> {
		const view = this._config.hinzufuegen;
		if (view === undefined) {
			throw new DeveloperNotificationException("Die Ansicht Hinzufügen wird von diesem Adapter nicht unterstützt.");
		}
		const route = view.getRoute();
		const result = await RouteManager.doRoute(route);
		this._config.route.setView(view, [view]);
		return result;
	}


	/**
	 * Gibt zurück, ob die Auswahl Ansichten für eine Schnelleingabe für Einträge unterstützt oder nicht.
	 *
	 * @returns true, wenn Ansichten für eine Schnelleingabe für Einträge unterstützt werden, und ansonsten false
	 */
	public get hatSchnelleingabe(): boolean {
		return this._config.schnelleingabe !== undefined;
	}


	/**
	 * Wechselt in die Ansicht für die Schnelleingabe, ohne ein Routing konkret durchzuführen.
	 */
	public setViewSchnelleingabe(): void {
		const view = this._config.schnelleingabe;
		if (view === undefined) {
			throw new DeveloperNotificationException("Die Ansicht für eine Schnelleingabe wird von diesem Adapter nicht unterstützt.");
		}
		this._config.route.setView(view, [view]);
	}


	/**
	 * Führt ein Routing zu der Schnelleingabe-Ansicht aus.
	 *
	 * @returns eine Promise mit dem RoutingStatus
	 */
	public async gotoSchnelleingabe(id: number): Promise<RoutingStatus> {
		const view = this._config.schnelleingabe;
		if (view === undefined) {
			throw new DeveloperNotificationException("Die Ansicht für eine Schnelleingabe wird von diesem Adapter nicht unterstützt.");
		}
		const params = {};
		this._config.addID(params, id);
		const route = view.getRoute(params);
		const result = await RouteManager.doRoute(route);
		this._config.route.setView(view, [view]);
		return result;
	}

}
