import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";
import type { SchuelerLernabschnittNachpruefungProps } from "~/components/schueler/lernabschnitte/nachpruefung/SchuelerLernabschnittNachpruefungProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

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
