import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";

import type { SchuelerLernabschnittAllgemeinProps } from "~/components/schueler/lernabschnitte/allgemein/SSchuelerLernabschnittAllgemeinProps";
import { api } from "~/router/Api";

const SSchuelerLernabschnittAllgmein = () => import("~/components/schueler/lernabschnitte/allgemein/SSchuelerLernabschnittAllgemein.vue");

export class RouteSchuelerLernabschnittAllgemein extends RouteNode<unknown, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.lernabschnitt.allgemein", "allgemein", SSchuelerLernabschnittAllgmein);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Allgemein";
		super.children = [
		];
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number, abschnitt: number, wechselNr: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittAllgemeinProps {
		return {
			schule: api.schuleStammdaten,
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt
		};
	}

}

export const routeSchuelerLernabschnittAllgemein = new RouteSchuelerLernabschnittAllgemein();

