import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";
import { AppMenuGroup, type TabData } from "@ui";
import { RouteDataStatistik } from "./RouteDataStatistik";
import type { RouteParams, RouteLocationRaw, RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "../RouteApp";
import { routeStatistikUebersicht } from "./RouteStatistikUebersicht";
import { routeStatistikSchueler } from "./RouteStatistikSchueler";
import { routeStatistikLehrer } from "./RouteStatistikLehrer";
import { RouteManager } from "~/router/RouteManager";
import type { StatistikAppProps } from "~/components/statistik/StatistikAppProps";
import { orteStateImpl } from "~/states/kataloge/OrteStateImpl";

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
		];
		super.defaultChild = routeStatistikUebersicht;
		super.icon = "i-ri-bar-chart-2-line";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await Promise.all([orteStateImpl.init(), this.data.ladeDaten()]);
		}
		if (to === this) {
			return this.getRouteDefaultChild();
		}
	}

	public getProps(to: RouteLocationNormalized): StatistikAppProps {
		return {
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
			schuleStammdaten: this.data.schuleStammdaten,
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
