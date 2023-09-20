import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { RouteNode } from "~/router/RouteNode";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";

import type { LadeDatenProps } from "~/components/LadeDatenProps";

const LadeDaten = () => import("~/components/LadeDaten.vue")


export class RouteLadeDaten extends RouteNode<unknown, RouteApp> {

	public constructor() {
		super("load", "/load", LadeDaten, null);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Laden";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { }};
	}

	public getProps(to: RouteLocationNormalized): LadeDatenProps {
		return {
			importLaufbahnplanung: routeApp.data.importLaufbahnplanung,
		};
	}

}

export const routeLadeDaten = new RouteLadeDaten();
