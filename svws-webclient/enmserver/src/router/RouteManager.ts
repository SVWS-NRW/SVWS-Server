import { reactive } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, Router, NavigationFailure, RouteParams } from "vue-router";
import { createRouter, createWebHashHistory } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";
import { routeLogin } from "~/router/RouteLogin";
import { routeError } from "~/router/error/RouteError";
import { RoutingStatus } from "~/router/RoutingStatus";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";

interface RouteStateError {
	code: number | undefined;
	error: Error | undefined;
}

export class RouteManager {

	/** Die Instanz des Route-Managers */
	protected static _instance?: RouteManager;

	/** Das Router-Objekt */
	protected router: Router;

	/** Gibt an, ob aktuell ein Routing stattfindet und eine Ereignisbehandlung durchgeführt wird. */
	protected active = false;

	/** Die Routing-Node, welche zuletzt erfolgreich ausgewählt wurde (siehe auch Methode afterEach) */
	protected _node : RouteNode<any, any> | undefined = undefined;

	/** Die Route-Location, welche zuletzt erfolgreich ausgewählt wurde (siehe auch Methode afterEach) */
	protected _routeLocation: RouteLocationNormalized | undefined = undefined;

	/** Der aktuelle Fehlerstatus der Route */
	private _errorstate = reactive<RouteStateError>({
		code: undefined,
		error: undefined,
	});

	/**
	 * Erstellt die Instanz des Managers für die übergebene Route
	 *
	 * @param router   der Router, welcher dem Manager zugeordnet wird
	 */
	private constructor(router: Router) {
		this.router = router;
		this.active = false;
		this.router.beforeEach((to: RouteLocationNormalized, from: RouteLocationNormalized) => RouteManager._instance?.beforeEach(to, from));
		this.router.afterEach((to: RouteLocationNormalized, from: RouteLocationNormalized, failure?: NavigationFailure | void) => RouteManager._instance?.afterEach(to, from, failure));
		// Füge die Haupt-Routen hinzu
		this.router.addRoute(routeLogin.record);
		this.router.addRoute(routeError.record);
		this.router.addRoute(routeApp.record);
	}

	public get errorcode(): number | undefined {
		return this._errorstate.code;
	}

	public set errorcode(value : number | undefined) {
		this._errorstate.code = value;
	}

	public get error(): Error | undefined {
		return this._errorstate.error;
	}

	public set error(value : Error | undefined) {
		this._errorstate.error = value;
	}

	public resetErrorState() {
		this._errorstate.code = undefined;
		this._errorstate.error = undefined;
	}

	/**
	 * Setzt die Route auf das angegebene Ziel
	 *
	 * @param to   die Ziel-Route
	 */
	public static async doRoute(to: RouteLocationRaw): Promise<RoutingStatus> {
		const manager = this.getInstanceOrException();
		// Ignoriere Aufruf, wenn der manager bereits in einem Routing-Vorgang ist. Dies kann sonst das aktuelle Routing canceln
		// und zu ungültigen Zuständen führen
		if (manager.active)
			return RoutingStatus.STOPPED_ROUTING_IS_ACTIVE;

		// Prüfen, ob ein Checkpoint betreten wurde, an dem gestoppt werden muss, um die doCheckpoint() Hook auszuführen
		const currentNode = manager._node;
		if (currentNode?.isCheckpointActive() === true) {
			await currentNode.doCheckpoint(to);
			return RoutingStatus.STOPPED_CHECKPOINT_ACTIVATED;
		}

		// Führe das Routing durch...
		const routingResult = await manager.router.push(to);
		return (routingResult === undefined) ? RoutingStatus.SUCCESS : RoutingStatus.ERROR;
	}

	/**
	 * Das ursprüngliche Routing, dass durch den aktiven Checkpoint gestoppt wurde, wird fortgeführt.
	 * Hinweis: Der Aufruf funktioniert nur erstmalig, nach dem ein Checkpoint im aktiven Zustand betreten wurde.
	 */
	public static async continueRoutingAfterCheckpoint(): Promise<RoutingStatus> {
		const currentRoute = this.getInstanceOrException()._node;
		if (currentRoute === undefined)
			throw new DeveloperNotificationException("Es kann kein Routing nach einem Checkpoint fortgeführt werden, da die aktuelle RouteNode undefined ist.");

		const currentCheckpoint = currentRoute.checkpoint;
		const destinationRoute = currentCheckpoint.originallyDestinationRoute;
		if (destinationRoute === undefined)
			return RoutingStatus.STOPPED_CHECKPOINT_DESTINATION_ROUTE_MISSING;

		// clean/remove checkpoint destination route for next checkpoint activation to prevent problems
		currentCheckpoint.originallyDestinationRoute = undefined;
		return RouteManager.doRoute(destinationRoute);
	}

	public static getInstanceOrException() : RouteManager {
		const manager = RouteManager._instance;
		if (manager === undefined)
			throw new DeveloperNotificationException("Unzulässiger Zugriff auf den RouteManager, bevor eine Instanz erzeugt wurde.");

		return manager;
	}

	/**
	 * Erstellt die Instanz des Managers für den übergebenen Route
	 *
	 * @param router   der Router, welcher dem Manager zugeordnet wird
	 *
	 * @returns die Instanz des Managers
	 */
	public static create(router: Router): RouteManager {
		if (RouteManager._instance !== undefined)
			throw new DeveloperNotificationException("Es sind keine zwei Instanzen des Route-Managers erlaubt.");
		RouteManager._instance = new RouteManager(router);
		return RouteManager._instance;
	}

	/**
	 * Gibt zurück, ob das Routing derzeit aktiv ist.
	 *
	 * @returns true, falls das Routing aktiv ist und ansonsten false
	 */
	public static isActive() : boolean {
		const manager = RouteManager._instance;
		if (manager === undefined)
			return false;
		return manager.active;
	}

	/**
	 * Der beforeEach-Handler ist ein Navigation-Guard, welcher prüft, ob eine Route angesteuert werden darf oder nicht.
	 * Dabei wird das Routing so lange gesperrt, bis der afterEach-Handler ausgeführt wurde.
	 * Schlägt der Navigation-Guard fehl, dann gibt es zwei mögliche Antworten:
	 *   1. false, d.h. die Route ist ungültig
	 *   2. ein neues Routing-Ziel, so dass es zu einem Redirect kommt, wodurch das Routing noch nicht abgeschlossen ist und
	 * 	    der beforeEach-Handler über die neue Route erneut aufgerufen wird.
	 * Der beforeEach-Handler erezugt zunächst das Ereignis beforeEach nur bei der Zielroute (vollständiger Pfad).
	 * Dann erzeugt er die Ereignisse enter, update und leaveBefore. Diese werden entsprechend auf alle
	 * Teile des Pfades getriggert und es werden auch Ereignisse bei den entsrpechenden Teilrouten/Parents getriggert.
	 *
	 * @param to die gewünschte/angesteuerte Zielroute
	 * @param from die Route, von der ausgegangen wird
	 *
	 * @returns false im Fehlerfall, void/true bei Erfolg oder ein Redirect
	 */
	protected async beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized) : Promise<boolean | void | Error | RouteLocationRaw> {
		// Prüfe, ob bereits ein Routing-Vorgang durchgeführt wird. Ist dies der Fall, so wird der neue Vorgang ignoriert
		if ((this.active) && (to.redirectedFrom === undefined))
			return false;
		this.active = true; // Setze, dass ein Routing-Vorgang bearbeitet wird
		api.status.start();
		// Ist der Benutzer nicht authentifiziert, so wird er zur Login-Seite weitergeleitet
		if (!api.authenticated && (to.name !== "login")) {
			routeLogin.routepath = to.fullPath;
			return { name: "login", query: { redirect: to.fullPath } };
		}
		// Aktualisiere ggf. den goto-Parameter
		if (!api.authenticated && (to.name === "login")) {
			let redirect = to.query.redirect;
			if ((redirect === undefined) || (redirect === null) || ((!Array.isArray(redirect)) && (redirect.startsWith("/error"))))
				redirect = "/";
			if (redirect.toString() !== routeLogin.routepath) {
				routeLogin.routepath = redirect.toString();
				return { name: "login", query: { redirect: redirect } };
			}
		}
		// Bestimme die Knoten, für die Quelle und das Ziel der Route
		const to_node : RouteNode<any, any> | undefined = RouteNode.getNodeByName(to.name?.toString());
		const from_node : RouteNode<any, any> | undefined = RouteNode.getNodeByName(from.name?.toString());
		const nodeRedirected : RouteNode<any, any> | undefined = RouteNode.getNodeByName(to.redirectedFrom?.name?.toString());
		if (to_node === undefined)
			return false;
		if ((from_node === undefined) && (from.fullPath !== "/"))
			return false;
		// Prüfe zunächst, ob die Ziel-Route für den angemeldeten Benutzer und die Schulform der Schule erlaubt ist oder nicht
		if (api.authenticated && (!to_node.hatSchulform() || !to_node.hatEineKompetenz()))
			return false;
		if (api.mode !== ServerMode.STABLE)
			console.log("Routing '" + from.fullPath + "' --> '" + to.fullPath + "'"); // + "': " + from_node?.name + " " + JSON.stringify(from.params) +  " --> " + to_node.name + " " + JSON.stringify(to.params)
		if (!to_node.mode.checkServerMode(api.mode))
			return routeError.getErrorRoute(new DeveloperNotificationException("Die Route ist nicht verfügbar, da die Client-Funktionen sich derzeit in der Entwicklung befinden (Stand: " + to_node.mode.name() + ")."), 503);
		// Rufe die beforeEach-Methode bei der Ziel-Route auf und prüfe, ob die Route abgelehnt oder umgeleite wird...
		let result: any = await to_node.doBeforeEach(to_node, to.params, from_node, from.params);
		if (result !== true)
			return result;
		// Ereignisbehandlung: Sende die entsprechenden Nachrichten enter, update, leave zur Aktualisierung an die Knoten
		if (from.fullPath === "/") {
			// Die Analyse der Quell-Route ist nicht erheblich - die Ereignisse für die Ziel-Route sind aber wichtig
			const to_predecessors: RouteNode<any, any>[] = to_node.getPredecessors();
			for (const node of to_predecessors) {
				result = await node.doUpdate(to_node, to.params, from_node, from.params, true, nodeRedirected);
				if (result !== undefined)
					return result;
			}
			result = await to_node.doUpdate(to_node, to.params, from_node, from.params, true, nodeRedirected);
			if (result !== undefined)
				return result;
		} else {
			// Bestimme den Knoten, der Route, die zuvor ausgewählt war - diese muss ja auch gültig sein...
			if (from_node === undefined)
				return false;
			// Prüfe, ob die Knoten Nachfolger bzw. Vorgänger voneinander sind
			const equals = (to_node.name === from_node.name);
			const to_is_successor = to_node.checkSuccessorOf(from_node);
			const from_is_successor = from_node.checkSuccessorOf(to_node);
			const to_predecessors_all: RouteNode<any, any>[] = to_node.getPredecessors();
			if (to_is_successor !== false) {
				for (const node of to_predecessors_all) {
					result = await node.doUpdate(to_node, to.params, from_node, from.params, (to_is_successor.includes(node) && (node.name !== from_node.name)), nodeRedirected);
					if (result !== undefined)
						return result;
				}
				result = await to_node.doUpdate(to_node, to.params, from_node, from.params, true, nodeRedirected);
				if (result !== undefined)
					return result;
			} else if (from_is_successor !== false) {
				for (const node of from_is_successor.slice(1).reverse()) {
					result = await node.doLeaveBefore(from_node, from.params);
					if (result !== undefined)
						return result;
				}
				for (const node of to_predecessors_all) {
					result = await node.doUpdate(to_node, to.params, from_node, from.params, false, nodeRedirected);
					if (result !== undefined)
						return result;
				}
				result = await to_node.doUpdate(to_node, to.params, from_node, from.params, false, nodeRedirected);
				if (result !== undefined)
					return result;
			} else if (equals) {
				for (const node of to_predecessors_all) {
					result = await node.doUpdate(to_node, to.params, from_node, from.params, false, nodeRedirected);
					if (result !== undefined)
						return result;
				}
				result = await to_node.doUpdate(to_node, to.params, from_node, from.params, false, nodeRedirected);
				if (result !== undefined)
					return result;
			} else {
				let from_predecessors: RouteNode<any, any>[] = from_node.getPredecessors();
				let to_predecessors: RouteNode<any, any>[] = [...to_predecessors_all];
				// Entferne gemeinsame Teilroute am Anfang der beiden Routen - diese Routen-Teile bleiben erhalten
				while ((from_predecessors.length > 0) && (to_predecessors.length > 0) && (from_predecessors[0].name === to_predecessors[0].name)) {
					from_predecessors = from_predecessors.slice(1);
					to_predecessors = to_predecessors.slice(1);
				}
				result = await from_node.doLeaveBefore(from_node, from.params);
				if (result !== undefined)
					return result;
				for (const node of [...from_predecessors].reverse()) {
					result = await node.doLeaveBefore(from_node, from.params);
					if (result !== undefined)
						return result;
				}
				for (const node of to_predecessors_all) {
					result = await node.doUpdate(to_node, to.params, from_node, from.params, to_predecessors.includes(node), nodeRedirected);
					if (result !== undefined)
						return result;
				}
				result = await to_node.doUpdate(to_node, to.params, from_node, from.params, true, nodeRedirected);
				if (result !== undefined)
					return result;
			}
		}
		// Akzeptiere die Route...
		return true;
	}


	/**
	 * Der afterEach-Handler wird aufgerufen, wenn das Routing für eine Route abgeschlossen ist und kann
	 * zum Aufräumen nach dem Routing genutzt werden - oder zur Fehlerbehandlung, wenn ein Fehler beim Routing aufgetreten
	 * ist und die gwünschte Route nicht angesteuert wurde, wodurch das Routing in einem inkonsistenten Zustand sein könnte.
	 *
	 * @param to die gwünschte/angesteuerte Route
	 * @param from die Route, welche zuvor gewählt wure
	 * @param failure ggf. der Grund für einen Fehler
	 */
	protected async afterEach(to: RouteLocationNormalized, from: RouteLocationNormalized, failure?: NavigationFailure | void): Promise<any> {
		try {
			const to_node : RouteNode<any, any> | undefined = RouteNode.getNodeByName(to.name?.toString());
			this._node = to_node;
			this._routeLocation = to;
			const from_node : RouteNode<any, any> | undefined = RouteNode.getNodeByName(from.name?.toString());
			if (failure === undefined) {
				if (api.mode !== ServerMode.STABLE)
					console.log("Completed routing '" + from.fullPath + "' --> '" + to.fullPath + "'"); // + "': " + from_node?.name + " " + JSON.stringify(from.params) +  " --> " + to_node?.name + " " + JSON.stringify(to.params)
				if ((to_node !== undefined) && (from_node !== undefined) && (from.fullPath !== "/") && api.authenticated) {
					// Prüfe, ob die Knoten Nachfolger bzw. Vorgänger voneinander sind
					const equals = (to_node.name === from_node.name);
					const to_is_successor = to_node.checkSuccessorOf(from_node);
					if (to_is_successor === false) {
						const from_is_successor = from_node.checkSuccessorOf(to_node);
						const to_predecessors_all: RouteNode<any, any>[] = to_node.getPredecessors();
						if (from_is_successor !== false) {
							for (const node of from_is_successor.slice(1).reverse())
								await node.leave(from_node, from.params);
						} else if (!equals) {
							let from_predecessors: RouteNode<any, any>[] = from_node.getPredecessors();
							let to_predecessors: RouteNode<any, any>[] = [...to_predecessors_all];
							// Entferne gemeinsame Teilroute am Anfang der beiden Routen - diese Routen-Teile bleiben erhalten
							while ((from_predecessors.length > 0) && (to_predecessors.length > 0) && (from_predecessors[0].name === to_predecessors[0].name)) {
								from_predecessors = from_predecessors.slice(1);
								to_predecessors = to_predecessors.slice(1);
							}
							await from_node.leave(from_node, from.params);
							for (const node of [...from_predecessors].reverse())
								await node.leave(from_node, from.params);
						}
					}
				}
			} else {
				if (api.mode !== ServerMode.STABLE)
					console.log("Failed Routing '" + from.fullPath + "' --> '" + to.fullPath + "'"); //  + "': " + from_node?.name + " " + JSON.stringify(from.params) +  " --> " + to_node?.name + " " + JSON.stringify(to.params)
			}
		} catch(e) {
			console.log("Unexpected routing error:", e);
		} finally {
			api.status.stop();
			this.active = false; // Setze, dass die Handhabung des Routing-Vorgangs abgeschlossen wurde
		}
	}

	/**
	 * Gibt die RouteNode zur aktuellen Route zurück. Diese wird immer neu gesetzt,
	 * wenn ein Routing abgeschlossen wird und afterEach ausgeführt wird.
	 *
	 * @returns die RouteNode zur aktuellen Route
	 */
	public getRouteNode() : RouteNode<any, any> | undefined {
		return this._node;
	}

	/**
	 * Gibt die Parameter zur aktuellen Route zurück. Diese werden immer neu gesetzt,
	 * wenn ein Routing abgeschlossen wird.
	 *
	 * @returns die Parameter zur aktuellen Route
	 */
	public getRouteParams() : RouteParams | undefined {
		if (this._routeLocation === undefined)
			return undefined;
		return this._routeLocation.params;
	}

	/**
	 * Führt einen Reset auf den Daten bei allen Routing-Knoten
	 * aus, außer beim Login-Knoten. Dies dient dazu den internen Client-State
	 * bei einem Logout aufzuräumen.
	 */
	public static resetRouteState() : void {
		routeError.resetDataRecursive();
		routeApp.resetDataRecursive();
	}

}

// Initialisiere den Router
export const router = createRouter({
	history: createWebHashHistory(import.meta.env.BASE_URL),
	routes: [ ],
});

export const routerManager = RouteManager.create(router);
