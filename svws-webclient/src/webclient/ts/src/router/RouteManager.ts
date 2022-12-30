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


    protected beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized) {
        // Ist der Benutzer nicht authentifiziert, so wird er zur Login-Seite weitergeleitet
        if (!mainApp.authenticated && to.name !== "login")
            return { name: "login", query: { redirect: to.fullPath } }; // TODO 
        // Bestimme den Knoten, für das Ziel der Route
        const node : RouteNode<unknown> | undefined = RouteNode.getNodeByName(to.name?.toString());
        if (node === undefined)
            return true; // TODO später sollte dies false sein, wenn alle Routen vollständig auf den RouteManager umgestellt wurden
        // Prüfe mithilfe der hidden-Methode, ob die Route sichtbar ist
        if (node.hidden())
            return false;
        // Rufe die beforeEach-Methode bei der Ziel-Route auf...
        return node.beforeEach(to, from);
    }

}
