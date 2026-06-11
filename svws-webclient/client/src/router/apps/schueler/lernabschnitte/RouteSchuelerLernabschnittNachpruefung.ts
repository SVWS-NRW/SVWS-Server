import type { RouteLocationNormalized } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";

import type { SchuelerLernabschnittNachpruefungProps } from "~/components/schueler/lernabschnitte/nachpruefung/SchuelerLernabschnittNachpruefungProps";

const SchuelerLernabschnittAllgemein = () => import("~/components/schueler/lernabschnitte/nachpruefung/SchuelerLernabschnittNachpruefung.vue");

export class RouteSchuelerLernabschnittNachpruefung extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN], "schueler.lernabschnitt.nachpruefung", "nachpruefung", SchuelerLernabschnittAllgemein);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Nachprüfung";
		super.children = [
		];
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittNachpruefungProps {
		return {
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt,
		};
	}

}

export const routeSchuelerLernabschnittNachpruefung = new RouteSchuelerLernabschnittNachpruefung();
