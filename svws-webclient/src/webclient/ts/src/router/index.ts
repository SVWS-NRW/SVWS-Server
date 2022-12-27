import { createRouter, createWebHashHistory } from "vue-router";

import { RouteLogin } from "~/router/RouteLogin";
import { RouteApp } from "~/router/RouteApp";
import { RouteManager } from "~/router/RouteManager";


// Initialisiere den Router
export const router = createRouter({
	history: createWebHashHistory(import.meta.env.BASE_URL ?? "/"),
	routes: [ ]
});

export const routerManager = new RouteManager(router);
