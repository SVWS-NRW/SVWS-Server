import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { routeNotenmodul } from "./RouteNotenmodul";
import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulTeilleistungen, type RouteNotenmodulTeilleistungen } from "./RouteNotenmodulTeilleistungen";
import type { RouteLocationNormalized } from "vue-router";
import type { NotenmodulTeilleistungenProps } from "~/components/notenmodul/NotenmodulTeilleistungenProps";

const NotenmodulTeilleistungen = () => import("~/components/notenmodul/NotenmodulTeilleistungen.vue");

export class RouteNotenmodulTeilleistungenData extends RouteNode<any, RouteNotenmodulTeilleistungen> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION,
		], "notenmodul.teilleistungen.daten", "daten", NotenmodulTeilleistungen);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Teilleistungen";
	}

	public getProps(to: RouteLocationNormalized): NotenmodulTeilleistungenProps {
		return {
			enmManager: () => routeNotenmodul.data.manager,
			auswahl: () => routeNotenmodul.data.auswahlLerngruppen,
			patchLeistung: routeNotenmodul.data.patchLeistung,
			patchTeilleistung: routeNotenmodul.data.patchTeilleistung,
			columnsVisible: () => routeNotenmodulTeilleistungen.data.columnsVisible,
			setColumnsVisible: routeNotenmodulTeilleistungen.data.setColumnsVisible,
		};
	}

}

export const routeNotenmodulTeilleistungenData = new RouteNotenmodulTeilleistungenData();
