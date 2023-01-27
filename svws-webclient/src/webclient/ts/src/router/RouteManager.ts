import { RouteLocationNormalized, Router } from "vue-router";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/RouteApp";
import { routeLogin } from "~/router/RouteLogin";

import { mainApp } from "~/apps/Main";

export class RouteManager {

	/** Die Instanz des Route-Managers */
	protected static _instance?: RouteManager;

	/** Das Router-Objekt */
	protected router: Router;

	/** Gibt an, ob aktuell ein Routing stattfindet und eine Ereignisbehandlung durchgeführt wird. */
	protected active = false;

	/**
     * Erstellt die Instanz des Managers für die übergebene Route
     *
     * @param router   der Router, welcher dem Manager zugeordnet wird
     */
	private constructor(router: Router) {
		this.router = router;
		this.active = false;
		this.router.beforeEach((to: RouteLocationNormalized, from: RouteLocationNormalized) => RouteManager._instance?.beforeEach(to, from));
		// Füge die Haupt-Routen hinzu
		this.router.addRoute(routeLogin.record);
		this.router.addRoute(routeApp.record);
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
			throw new Error("Es sind keine zwei Insanzen des Route-Managers erlaubt.");
		RouteManager._instance = new RouteManager(router);
		return RouteManager._instance;
	}

	public static isActive() : boolean {
		const manager = RouteManager._instance;
		if (manager === undefined)
			return false;
		return manager.active;
	}

	protected async beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized) {
		// Prüfe, ob bereits ein Routing-Vorgang durchgeführt wird. Ist dies der Fall, so wird der neue Vorgang ignoriert
		if (this.active)
			return false;
		this.active = true; // Setze, dass ein Routing-Vorgang bearbeitet wird
		const result = await this.beforeEachHandler(to, from); // Prüfe, ob die Route in Ordnung ist und führe die berforeEach, enter, update und leave-Ereignisse auf der Route aus
		this.active = false; // Setze, dass die Handhabung des Routing-Vorgangs abgeschlossen wurde
		return result;
	}

	protected async beforeEachHandler(to: RouteLocationNormalized, from: RouteLocationNormalized) {
		// Ist der Benutzer nicht authentifiziert, so wird er zur Login-Seite weitergeleitet
		if (!mainApp.authenticated && to.name !== "login")
			return { name: "login", query: { redirect: to.fullPath } }; // TODO
		// Bestimme die Knoten, für die Quelle und das Ziel der Route
		const to_node : RouteNode<unknown, any> | undefined = RouteNode.getNodeByName(to.name?.toString());
		const from_node : RouteNode<unknown, any> | undefined = RouteNode.getNodeByName(from.name?.toString());
		if (to_node === undefined)
			return false;
		if ((from_node === undefined) && (from.fullPath !== "/"))
			return false;
		// Prüfe mithilfe der hidden-Methode, ob die Route sichtbar ist
		if (to_node.hidden(to.params))
			return false;
		console.log("Routing:");
		console.log("  from: " + from_node?.name + " params=" + JSON.stringify(from.params));
		console.log("  to: " + to_node.name + " params=" + JSON.stringify(to.params));
		console.log("  to-path: " + to.fullPath);
		// Rufe die beforeEach-Methode bei der Ziel-Route auf und prüfe, ob die Route abgelehnt oder umgeleite wird...
		let result: any = await to_node.beforeEach(to_node, to.params, from_node, from.params);
		if (result !== true)
			return result;
		// Ereignisbehandlung: Sende die entsprechenden Nachrichten enter, update, leave zur Aktualisierung an die Knoten
		if (from.fullPath === "/") {
			// Die Analyse der Quell-Route ist nicht erheblich - die Ereignisse für die Ziel-Route sind aber wichtig
			const to_predecessors: RouteNode<unknown, any>[] = to_node.getPredecessors();
			for (const node of to_predecessors) {
				result = await node.enter(to_node, to.params);
				if (result !== undefined)
					return result;
			}
			result = await to_node.enter(to_node, to.params);
			if (result !== undefined)
				return result;
			for (const node of to_predecessors) {
				result = await node.doUpdate(to_node, to.params);
				if (result !== undefined)
					return result;
			}
			result = await to_node.doUpdate(to_node, to.params);
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
			const to_predecessors_all: RouteNode<unknown, any>[] = to_node.getPredecessors();
			if (to_is_successor) {
				for (const node of to_is_successor) {
					if (node.name !== from_node.name) {
						result = await node.enter(to_node, to.params);
						if (result !== undefined)
							return result;
					}
				}
				for (const node of to_predecessors_all) {
					result = await node.doUpdate(to_node, to.params);
					if (result !== undefined)
						return result;
				}
				result = await to_node.doUpdate(to_node, to.params);
				if (result !== undefined)
					return result;
			} else if (from_is_successor) {
				for (const node of from_is_successor.slice(1).reverse()) {
					result = await node.leave(from_node, from.params);
					if (result !== undefined)
						return result;
				}
				for (const node of to_predecessors_all) {
					result = await node.doUpdate(to_node, to.params);
					if (result !== undefined)
						return result;
				}
				result = await to_node.doUpdate(to_node, to.params);
				if (result !== undefined)
					return result;
			} else if (equals) {
				for (const node of to_predecessors_all) {
					result = await node.doUpdate(to_node, to.params);
					if (result !== undefined)
						return result;
				}
				result = await to_node.doUpdate(to_node, to.params);
				if (result !== undefined)
					return result;
			} else {
				let from_predecessors: RouteNode<unknown, any>[] = from_node.getPredecessors();
				let to_predecessors: RouteNode<unknown, any>[] = [...to_predecessors_all];
				// Entferne gemeinsame Teilroute am Anfang der beiden Routen - diese Routen-Teile bleiben erhalten
				while ((from_predecessors.length > 0) && (to_predecessors.length > 0) && (from_predecessors[0].name === to_predecessors[0].name)) {
					from_predecessors = from_predecessors.slice(1);
					to_predecessors = to_predecessors.slice(1);
				}
				result = await from_node.leave(from_node, from.params);
				if (result !== undefined)
					return result;
				for (const node of [...from_predecessors].reverse()) {
					result = await node.leave(from_node, from.params);
					if (result !== undefined)
						return result;
				}
				for (const node of to_predecessors) {
					result = await node.enter(to_node, to.params);
					if (result !== undefined)
						return result;
				}
				result = await to_node.enter(to_node, to.params);
				if (result !== undefined)
					return result;
				for (const node of to_predecessors_all) {
					result = await node.doUpdate(to_node, to.params);
					if (result !== undefined)
						return result;
				}
				result = await to_node.doUpdate(to_node, to.params);
				if (result !== undefined)
					return result;
			}
		}
		// Akzeptiere die Route...
		return true;
	}

}
