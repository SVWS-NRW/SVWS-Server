import { RouteLocationNormalized, Router } from "vue-router";

import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/RouteApp";
import { routeLogin } from "~/router/RouteLogin";

import { mainApp } from "~/apps/Main";


export class RouteManager {

    protected router: Router;

    public constructor (router: Router) {
        this.router = router;
        this.router.beforeEach(this.beforeEach);
        // Füge die Haupt-Routen hinzu
        this.router.addRoute(routeLogin.record);
        this.router.addRoute(routeApp.record);
    }

    protected async beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized) {
        // Ist der Benutzer nicht authentifiziert, so wird er zur Login-Seite weitergeleitet
        if (!mainApp.authenticated && to.name !== "login")
            return { name: "login", query: { redirect: to.fullPath } }; // TODO 
        // Bestimme die Knoten, für die Quelle und das Ziel der Route
        const to_node : RouteNode<unknown> | undefined = RouteNode.getNodeByName(to.name?.toString());
        const from_node : RouteNode<unknown> | undefined = RouteNode.getNodeByName(from.name?.toString());
        if (to_node === undefined)
            return false;
        if ((from_node === undefined) && (from.fullPath !== "/"))
            return false;
        // Prüfe mithilfe der hidden-Methode, ob die Route sichtbar ist
        if (to_node.hidden)
            return false;
        // Rufe die beforeEach-Methode bei der Ziel-Route auf und prüfe, ob die Route abgelehnt oder umgeleite wird...
        const result = await to_node.beforeEach(to_node, to.params, from_node, from.params);
        if (result !== true)
            return result;
        // Ereignisbehandlung: Sende die entsprechenden Nachrichten enter, update, leave zur Aktualisierung an die Knoten
        if (from.fullPath === "/") {
            // Die Analyse der Quell-Route ist nicht erheblich - die Ereignisse für die Ziel-Route sind aber wichtig
            const to_predecessors: RouteNode<unknown>[] = to_node.getPredecessors();
            to_predecessors.forEach(
                async node => await node.enter(to_node, to.params)
            );
            await to_node.enter(to_node, to.params);
            to_predecessors.forEach(
                async node => await node.update(to_node, to.params)
            );
            await to_node.update(to_node, to.params);
        } else {
            // Bestimme den Knoten, der Route, die zuvor ausgewählt war - diese muss ja auch gültig sein...
            if (from_node === undefined)
                return false;
            // Prüfe, ob die Knoten Nachfolger bzw. Vorgänger voneinander sind
            const equals = (to_node.name === from_node.name);
            const to_is_successor = to_node.checkSuccessorOf(from_node);
            const from_is_successor = from_node.checkSuccessorOf(to_node);
            if (to_is_successor) {
                console.log("to_is_successor");
                for (let node of to_is_successor)
                    if (node.name !== from_node.name)
                        await node.enter(to_node, to.params);
                for (let node of to_is_successor)
                    await node.update(to_node, to.params);
            } else if (from_is_successor) {
                from_is_successor.slice(1).reverse().forEach(
                    async node => await node.leave(from_node, from.params)
                );
                await to_node.update(to_node, to.params);
            } else if (equals) {
                await to_node.update(to_node, to.params);
            } else {
                let from_predecessors: RouteNode<unknown>[] = from_node.getPredecessors();
                let to_predecessors: RouteNode<unknown>[] = to_node.getPredecessors();
                // Entferne gemeinsame Teilroute am Anfang der beiden Routen - diese Routen-Teile bleiben erhalten
                while ((from_predecessors.length > 0) && (to_predecessors.length > 0) && (from_predecessors[0].name === to_predecessors[0].name)) {
                    from_predecessors = from_predecessors.slice(1);
                    to_predecessors = to_predecessors.slice(1);
                }
                await from_node.leave(from_node, from.params);
                [...from_predecessors].reverse().forEach(
                    async node => await node.leave(from_node, from.params)
                );
                to_predecessors.forEach(
                    async node => await node.enter(to_node, to.params)
                );
                await to_node.enter(to_node, to.params);
                to_predecessors.forEach(
                    async node => await node.update(to_node, to.params)
                );
                await to_node.update(to_node, to.params);
            }
        }
        // Akzeptiere die Route...
        return true;
    }

}
