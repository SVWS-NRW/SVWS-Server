import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostHalbjahr, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";

import type { SchuelerLernabschnittGostKlausurenProps } from "~/components/schueler/lernabschnitte/gostKlausuren/SSchuelerLernabschnittGostKlausurenProps";
import { api } from "~/router/Api";
import { routeSchueler } from "../RouteSchueler";

const SSchuelerLernabschnittGostKlausuren = () => import("~/components/schueler/lernabschnitte/gostKlausuren/SSchuelerLernabschnittGostKlausuren.vue");

export class RouteSchuelerLernabschnittGostKlausuren extends RouteNode<unknown, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.lernabschnitt.gostKlausuren", "gostKlausuren", SSchuelerLernabschnittGostKlausuren);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gost-Klausuren";
		super.children = [
		];
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number, abschnitt: number, wechselNr: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittGostKlausurenProps {
		return {
			schule: api.schuleStammdaten,
			manager: () => routeSchuelerLernabschnitte.data.manager,
			klausurManager: () => routeSchuelerLernabschnitte.data.klausurManager,
			hatKlausurManager: () => routeSchuelerLernabschnitte.data.hatKlausurManager,
			createSchuelerklausurTermin: routeSchuelerLernabschnitte.data.createSchuelerklausurTermin,
			deleteLastSchuelerklausurTermin: routeSchuelerLernabschnitte.data.deleteLastSchuelerklausurTermin,
			patchSchuelerklausurTermin: routeSchuelerLernabschnitte.data.patchSchuelerklausurTermin
		};
	}

}

export const routeSchuelerLernabschnittGostKlausuren = new RouteSchuelerLernabschnittGostKlausuren();

