import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";
import type { SchuelerLernabschnittAllgemeinProps } from "~/components/schueler/lernabschnitte/allgemein/SchuelerLernabschnittAllgemeinProps";
import { routeSchueler } from "../RouteSchueler";

const SchuelerLernabschnittAllgmein = () => import("~/components/schueler/lernabschnitte/allgemein/SchuelerLernabschnittAllgemein.vue");

export class RouteSchuelerLernabschnittAllgemein extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN], "schueler.lernabschnitt.allgemein", "allgemein", SchuelerLernabschnittAllgmein);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Allgemein";
		super.children = [
		];
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittAllgemeinProps {
		return {
			schuelerListeManager: () => routeSchueler.data.manager,
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt,
		};
	}

}

export const routeSchuelerLernabschnittAllgemein = new RouteSchuelerLernabschnittAllgemein();
