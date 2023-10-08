import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";

import type { SchuelerLernabschnittNachpruefungProps } from "~/components/schueler/lernabschnitte/nachpruefung/SSchuelerLernabschnittNachpruefungProps";
import { api } from "~/router/Api";

const SSchuelerLernabschnittAllgmein = () => import("~/components/schueler/lernabschnitte/nachpruefung/SSchuelerLernabschnittNachpruefung.vue");

export class RouteSchuelerLernabschnittNachpruefung extends RouteNode<unknown, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.lernabschnitt.nachpruefung", "nachpruefung", SSchuelerLernabschnittAllgmein);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Nachprüfung";
		super.children = [
		];
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number, abschnitt: number, wechselNr: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id, abschnitt: abschnitt, wechselNr: wechselNr }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittNachpruefungProps {
		return {
			schule: api.schuleStammdaten,
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt,
			patchBemerkungen: routeSchuelerLernabschnitte.data.patchBemerkungen
		};
	}

}

export const routeSchuelerLernabschnittNachpruefung = new RouteSchuelerLernabschnittNachpruefung();

