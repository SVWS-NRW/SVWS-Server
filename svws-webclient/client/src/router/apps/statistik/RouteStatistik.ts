import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";
import type { TabData } from "@ui/ui/nav/TabData";

import type { RouteApp } from "../RouteApp";
import type { StatistikAppProps } from "~/components/statistik/StatistikAppProps";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { orteStateImpl } from "~/states/kataloge/OrteStateImpl";
import { statistikStateImpl } from "~/states/statistik/StatistikStateImpl";

import { RouteDataStatistik } from "./RouteDataStatistik";
import { routeStatistikKlassen } from "./RouteStatistikKlassen";
import { routeStatistikKurse } from "./RouteStatistikKurse";
import { routeStatistikLehrer } from "./RouteStatistikLehrer";
import { routeStatistikSchueler } from "./RouteStatistikSchueler";
import { routeStatistikUebersicht } from "./RouteStatistikUebersicht";

const StatistikApp = () => import("~/components/statistik/StatistikApp.vue");

export class RouteStatistik extends RouteNode<RouteDataStatistik, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "statistik", "statistik", StatistikApp, new RouteDataStatistik());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Statistik";
		super.menugroup = AppMenuGroup.MAIN;
		super.children = [
			routeStatistikUebersicht,
			routeStatistikSchueler,
			routeStatistikLehrer,
			routeStatistikKurse,
			routeStatistikKlassen,
		];
		super.defaultChild = routeStatistikUebersicht;
		super.icon = "i-ri-bar-chart-2-line";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await Promise.all([orteStateImpl.init(), statistikStateImpl.init()]);
		}
		if (to === this) {
			return this.getRouteDefaultChild();
		}
	}

	public getProps(to: RouteLocationNormalized): StatistikAppProps {
		return {
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	private readonly setTab = async (value: TabData) => {
		if (value.name === this.data.view.name) {
			return;
		}
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined) {
			throw new DeveloperNotificationException("Unbekannte Route");
		}
		await RouteManager.doRoute(this.getRouteView(node));
		this.data.setView(node, this.children);
	};
}

export const routeStatistik = new RouteStatistik();
