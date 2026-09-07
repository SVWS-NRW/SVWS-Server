import LaufbahnplanungOberstufe from "@lupo/components/LaufbahnplanungOberstufe.vue";
import type { LaufbahnplanungOberstufeProps } from "@lupo/components/LaufbahnplanungOberstufeProps";
import { type RouteApp, routeApp } from "@lupo/router/apps/RouteApp";
import { routeError } from "@lupo/router/error/RouteError";
import { RouteNode } from "@lupo/router/RouteNode";
import { gostLaufbahnplanungStateImpl } from "@lupo/states/GostLaufbahnplanungStateImpl";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { routeLadeDaten } from "./RouteLadeDaten";


export class RouteLaufbahnplanung extends RouteNode<unknown, RouteApp> {

	public constructor() {
		super("laufbahnplanung", "laufbahnplanung", LaufbahnplanungOberstufe, null);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Laufbahnplanung";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		if (this.parent === undefined) {
			return routeError.getRoute(new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert"));
		}
		if (!gostLaufbahnplanungStateImpl.hatAuswahl) {
			return routeLadeDaten.getRoute();
		}
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await gostLaufbahnplanungStateImpl.clear();
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { } };
	}

	public getProps(to: RouteLocationNormalized): LaufbahnplanungOberstufeProps {
		return {
			config: () => gostLaufbahnplanungStateImpl.config,
			exitLaufbahnplanung: routeApp.data.exitLaufbahnplanung,
			dirty: () => gostLaufbahnplanungStateImpl.modified,
		};
	}

}

export const routeLaufbahnplanung = new RouteLaufbahnplanung();

