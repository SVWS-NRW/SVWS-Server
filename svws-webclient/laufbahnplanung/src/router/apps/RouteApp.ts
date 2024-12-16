import type { RouteLocationRaw, RouteParams } from "vue-router";
import type { AppProps } from "~/components/AppProps";

import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";

import { RouteData } from "~/router/apps/RouteData";

import { routeLadeDaten } from "~/router/apps/RouteLadeDaten";

import AppLaufbahnplanung from "~/components/AppLaufbahnplanung.vue";
import { routeLaufbahnplanung } from "./RouteLaufbahnplanung";
import type { TabData } from "../../../../ui/src/ui/nav/TabData";


export class RouteApp extends RouteNode<RouteData, any> {

	public constructor() {
		super("app", "/", AppLaufbahnplanung, new RouteData());
		super.propHandler = (route) => this.getProps();
		super.text = "SVWS - Laufbahnplanung Oberstufe";
		super.children = [
			routeLadeDaten,
			routeLaufbahnplanung,
		];
		super.menu = [
			routeLadeDaten,
			routeLaufbahnplanung,
		];
		super.defaultChild = routeLadeDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return this.getRoute();
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
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
			setApp: this.setApp,
			app: this.getApp(),
			apps: this.getApps(),
			appsHidden: this.children_hidden().value,
		};
	}

	private getApp(): TabData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getApps(): TabData[] {
		const result: TabData[] = [];
		for (const c of super.menu)
			result.push({ name: c.name, text: c.text });
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
