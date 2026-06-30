import { shallowRef } from "vue";

import type { RouteNode } from "~/router/RouteNode";
import { routeApp } from "~/router/apps/RouteApp";
import { routeLadeDaten } from "~/router/apps/RouteLadeDaten";

import { RouteManager } from "../RouteManager";

export class RouteData {

	private readonly _view = shallowRef<RouteNode<any, any>>(routeLadeDaten);

	public async setView(view: RouteNode<any, any>) {
		if (routeApp.children.includes(view)) {
			this._view.value = view;
		} else {
			throw new Error("Diese gewählte Ansicht wird nicht unterstützt.");
		}
	}

	public get view(): RouteNode<any, any> {
		return this._view.value;
	}

	exitLaufbahnplanung = async () => {
		await RouteManager.doRoute(routeApp.getRoute());
	};

}
