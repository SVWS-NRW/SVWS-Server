import type { WritableComputedRef } from "vue";
import { computed } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";

const SStatistikAuswahl = () => import("~/components/statistik/SStatistikAuswahl.vue")
const SStatistikApp = () => import("~/components/statistik/SStatistikApp.vue")

export class RouteStatistik extends RouteNode<unknown, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "statistik", "/statistik", SStatistikApp);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Statistik";
		super.setView("liste", SStatistikAuswahl, (route) => this.getNoProps(route));
		super.children = [
		];
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<undefined> {
		// TODO
		return computed({ get(): undefined { return undefined; }, set(value: undefined) { }});
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name };
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		// TODO
		return { };
	}

}

export const routeStatistik = new RouteStatistik();
