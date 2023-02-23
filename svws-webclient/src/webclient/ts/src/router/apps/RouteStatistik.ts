import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteApp } from "../RouteApp";
import { RouteNode } from "../RouteNode";

const SStatistikAuswahl = () => import("~/components/statistik/SStatistikAuswahl.vue")
const SStatistikApp = () => import("~/components/statistik/SStatistikApp.vue")

export class RouteStatistik extends RouteNode<unknown, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "statistik", "/statistik", SStatistikApp);
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
