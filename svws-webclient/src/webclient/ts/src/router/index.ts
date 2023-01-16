import { createRouter, createWebHashHistory } from "vue-router";

import { RouteManager } from "~/router/RouteManager";


// Initialisiere den Router
export const router = createRouter({
	history: createWebHashHistory(import.meta.env.BASE_URL ?? "/"),
	routes: [ ]
});

export const routerManager = RouteManager.create(router);
