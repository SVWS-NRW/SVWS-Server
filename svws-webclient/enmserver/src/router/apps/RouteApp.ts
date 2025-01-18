import type { RouteLocationRaw, RouteParams } from "vue-router";
import type { AppProps } from "~/components/SAppProps";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { RoutingStatus } from "~/router/RoutingStatus";
import { RouteDataApp } from "~/router/apps/RouteDataApp";
import { routeError } from "~/router/error/RouteError";
import SApp from "~/components/SApp.vue";
import { routeLogin } from "~/router/RouteLogin";
import { routeLeistungen } from "~/router/apps/RouteLeistungen";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { TabData } from "@ui/ui/nav/TabData";
import { routeTeilleistungen } from "./RouteTeilleistungen";
import { routeKlassenleitung } from "./RouteKlassenleitung";


export class RouteApp extends RouteNode<RouteDataApp, any> {

	/** Die Knoten, welche im Haupt-Menu zur Verfügung gestellt werden */
	private _menuMain: RouteNode<any, any>[];

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "app", "/", SApp, new RouteDataApp());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "ENM-Client";
		this._menuMain = [
			routeLeistungen,
			routeTeilleistungen,
			routeKlassenleitung,
		];
		super.children = [
			...this._menuMain,
		];
		super.menu = this._menuMain;
		super.defaultChild = routeLeistungen;
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			// Prüfe, ob dies die Ziel-Route ist. Wenn ja, dann leite zum Default-Child um
			if (to.name === this.name)
				return this.defaultChild!.getRoute();
			// Prüfe, ob die View aktualisiert werden muss
			let cur: RouteNode<any, any> = to;
			while (cur.parent !== this)
				cur = cur.parent;
			if (cur !== this.data.view)
				this.data.setView(cur, this.children);
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(): AppProps {
		return {
			logout: routeLogin.logout,
			username: api.username,
			// Props für die Navigation
			setApp: this.setApp,
			app: this.getApp(),
			selectedChild: this.getSelectedChild(),
			apps: this.getApps(),
			appsHidden: this.children_hidden().value,
			apiStatus: api.status,
		};
	}

	private getSelectedChild(): TabData {
		const child = this.selectedChild!;
		return { name: child.name, text: child.text, hide: false };
	}

	private getApp(): TabData {
		return { name: this.data.view.name, text: this.data.view.text, hide: !this.data.view.hasView('liste') };
	}

	private getApps(): TabData[] {
		const result: TabData[] = [];
		for (const c of super.menu)
			if (c.hatEineKompetenz() && c.hatSchulform() && (c.hidden() === false))
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setApp = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		const result = await RouteManager.doRoute(node.getRoute());
		if (result === RoutingStatus.SUCCESS)
			this.data.setView(node, this.children);
	}

}

export const routeApp = new RouteApp();
