import { createRouter, createWebHashHistory } from "vue-router";

import { RouteLogin } from "~/router/RouteLogin";
import { RouteApp } from "~/router/RouteApp";

import { mainApp } from "../apps/Main";


// Initialisiere den Router
export const router = createRouter({
	history: createWebHashHistory(import.meta.env.BASE_URL ?? "/"),
	routes: [ RouteLogin, RouteApp ]
});


// Prüfe bei jeder Routenwahl die Authentifizierung
router.beforeEach(to => {
	// Prüfe, ob der Benutzer authentifiziert ist und leite ggf. auf die Login-Seite weiter. Vermeide ein unendliches weiterleiten von login zu login❗️
	if (!mainApp.authenticated && to.name !== "login") {
		// redirect the user to the login page
		return { name: "login", query: { redirect: to.fullPath } };
	}
});
