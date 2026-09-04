import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLernabschnitte, type RouteSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";
import type { SchuelerLernabschnittLeistungenProps } from "~/components/schueler/lernabschnitte/leistungen/SchuelerLernabschnittLeistungenProps";
import { routeSchueler } from "../RouteSchueler";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SchuelerLernabschnittLeistungen = () => import("~/components/schueler/lernabschnitte/leistungen/SchuelerLernabschnittLeistungen.vue");

export class RouteSchuelerLernabschnittLeistungen extends RouteNode<any, RouteSchuelerLernabschnitte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN], "schueler.lernabschnitt.leistungen", "leistungen", SchuelerLernabschnittLeistungen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leistungsdaten";
		super.children = [
		];
	}

	public getProps(to: RouteLocationNormalized): SchuelerLernabschnittLeistungenProps {
		return {
			schuelerListeManager: () => routeSchueler.data.manager,
			manager: () => routeSchuelerLernabschnitte.data.manager,
			patch: routeSchuelerLernabschnitte.data.patchLernabschnitt,
			patchLeistung: routeSchuelerLernabschnitte.data.patchLeistung,
			addLeistung: routeSchuelerLernabschnitte.data.addLeistung,
			deleteLeistungen: routeSchuelerLernabschnitte.data.deleteLeistungen,
		};
	}

}

export const routeSchuelerLernabschnittLeistungen = new RouteSchuelerLernabschnittLeistungen();
