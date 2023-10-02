import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";

import type { SchuelerLernabschnittLeistungenProps } from "~/components/schueler/lernabschnitte/SSchuelerLernabschnittLeistungenProps";

const SSchuelerLernabschnittLeistungen = () => import("~/components/schueler/lernabschnitte/SSchuelerLernabschnittLeistungen.vue");

export class RouteSchuelerLernabschnittLeistungen extends RouteNode<unknown, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.lernabschnitt.leistungen", "leistungen", SSchuelerLernabschnittLeistungen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leistungsdaten";
		super.children = [
		];
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number, abschnitt: number, wechselNr: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittLeistungenProps {
		return {
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patchLeistung: routeSchuelerLernabschnitte.data.patchLeistung
		};
	}

}

export const routeSchuelerLernabschnittLeistungen = new RouteSchuelerLernabschnittLeistungen();

