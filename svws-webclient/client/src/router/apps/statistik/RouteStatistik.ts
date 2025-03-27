import type { WritableComputedRef } from "vue";
import { computed } from "vue";
import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import { type RouteApp } from "~/router/apps/RouteApp";

import type { StatistikAppProps } from "~/components/statistik/SStatistikAppProps";
import { api } from "~/router/Api";
import { AppMenuGroup } from "@ui";

const SStatistikAuswahl = () => import("~/components/statistik/SStatistikAuswahl.vue")
const SStatistikApp = () => import("~/components/statistik/SStatistikApp.vue")

export class RouteStatistik extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "statistik", "statistik", SStatistikApp);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Statistik";
		super.setView("liste", SStatistikAuswahl, (route) => this.getNoProps(route));
		super.children = [
		];
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-bar-chart-2-line";
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<undefined> {
		// TODO
		return computed({ get(): undefined { return undefined; }, set(value: undefined) { }});
	}

	public getProps(to: RouteLocationNormalized): StatistikAppProps {
		return {
			schule: api.schuleStammdaten,
		};
	}

}

export const routeStatistik = new RouteStatistik();
