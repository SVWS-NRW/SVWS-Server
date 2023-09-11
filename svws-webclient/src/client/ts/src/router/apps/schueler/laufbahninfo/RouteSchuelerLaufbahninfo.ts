import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerLaufbahninfo } from "~/router/apps/schueler/laufbahninfo/RouteDataSchuelerLaufbahninfo";
import { type SchuelerLaufbahninfoProps } from "~/components/schueler/laufbahninfo/SchuelerLaufbahninfoProps";

const SSchuelerLaufbahninfo = () => import("~/components/schueler/laufbahninfo/SSchuelerLaufbahninfo.vue");

export class RouteSchuelerLaufbahninfo extends RouteNode<RouteDataSchuelerLaufbahninfo, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.laufbahninfo", "laufbahninfo", SSchuelerLaufbahninfo, new RouteDataSchuelerLaufbahninfo());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Laufbahn";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			return routeError.getRoute(new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein"));
		if (this.parent === undefined)
			return routeError.getRoute(new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert"));
		if (to_params.id === undefined) {
			await this.data.auswahlSchueler();
			return;
		}
		const id = parseInt(to_params.id);
		try {
			await this.data.auswahlSchueler(this.parent.data.mapSchueler.get(id));
		} catch(error) {
			return routeSchueler.getRoute(id);
		}
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await this.data.clear();
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLaufbahninfoProps {
		return {
			sprachbelegungen: () => this.data.sprachbelegungen,
			sprachpruefungen: () => this.data.sprachpruefungen,
		};
	}

}

export const routeSchuelerLaufbahninfo = new RouteSchuelerLaufbahninfo();

