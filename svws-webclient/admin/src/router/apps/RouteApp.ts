import type { RouteLocationRaw, RouteParams } from "vue-router";
import type { TabData } from "../../../../ui/src/components/App/TabData";
import type { AppProps } from "~/components/SAppProps";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";

import { RouteDataApp } from "~/router/apps/RouteDataApp";

import { routeSchema } from "~/router/apps/schema/RouteSchema";
import { routeLogin } from "~/router/login/RouteLogin";

import SApp from "~/components/SApp.vue";
import { routeSchemagruppe } from "./schemagruppe/RouteSchemagruppe";
import { routeSchemaNeu } from "./schemaneu/RouteSchemaNeu";
import { routeConfig } from "./config/RouteConfig";
import { ServerMode } from "../../../../core/src/core/types/ServerMode";


export class RouteApp extends RouteNode<RouteDataApp, any> {

	public constructor() {
		super("app", "/", SApp, new RouteDataApp());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "SVWS-Client";
		super.children = [
			routeSchema,
			routeSchemagruppe,
			routeSchemaNeu,
			routeConfig
		];
		super.menu = [
			routeSchema,
			routeConfig,
		];
		super.defaultChild = routeSchema;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return this.getRoute();
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		let cur: RouteNode<unknown, any> = to;
		while (cur.parent !== this)
			cur = cur.parent;
		if (cur !== this.data.view)
			await this.data.setView(cur);
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.defaultChild!.name };
	}

	public getProps(): AppProps {
		return {
			logout: routeLogin.logout,
			username: api.username,
			isServerAdmin: api.isServerAdmin,
			// Props für die Navigation
			setApp: this.setApp,
			app: this.getApp(),
			apps: this.getApps(),
			appsHidden: this.children_hidden().value,
			apiStatus: api.status,
		};
	}

	private getApp(): TabData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getApps(): TabData[] {
		const result: TabData[] = [];
		for (const { name, text } of super.menu)
			result.push({ name, text });
		return result;
	}

	private setApp = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { } });
		await this.data.setView(node);
	}

}

export const routeApp = new RouteApp();
