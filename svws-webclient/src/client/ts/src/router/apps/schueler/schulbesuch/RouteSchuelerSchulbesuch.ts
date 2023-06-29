import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerSchulbesuch } from "~/router/apps/schueler/schulbesuch/RouteDataSchuelerSchulbesuch";

import type { SchuelerSchulbesuchProps } from "~/components/schueler/schulbesuch/SSchuelerSchulbesuchProps";

const SSchuelerSchulbesuch = () => import("~/components/schueler/schulbesuch/SSchuelerSchulbesuch.vue");

export class RouteSchuelerSchulbesuch extends RouteNode<RouteDataSchuelerSchulbesuch, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.schulbesuch", "schulbesuch", SSchuelerSchulbesuch, new RouteDataSchuelerSchulbesuch());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulbesuch";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		if (to_params.id !== undefined) {
			const id = parseInt(to_params.id);
			await this.data.setEintrag(id);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerSchulbesuchProps {
		return {
			data: this.data.daten,
			patch: this.data.patch
		};
	}

}

export const routeSchuelerSchulbesuch = new RouteSchuelerSchulbesuch();

