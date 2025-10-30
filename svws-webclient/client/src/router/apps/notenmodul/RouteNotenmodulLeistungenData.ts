import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { routeNotenmodul } from "./RouteNotenmodul";
import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulLeistungen, type RouteNotenmodulLeistungen } from "./RouteNotenmodulLeistungen";
import type { RouteLocationNormalized } from "vue-router";
import type { NotenmodulLeistungenProps } from "~/components/notenmodul/NotenmodulLeistungenProps";

const NotenmodulLeistungen = () => import("~/components/notenmodul/NotenmodulLeistungen.vue");

export class RouteNotenmodulLeistungenData extends RouteNode<any, RouteNotenmodulLeistungen> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION,
		], "notenmodul.leistungen.daten", "daten", NotenmodulLeistungen);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leistungsdaten";
	}

	public getProps(to: RouteLocationNormalized): NotenmodulLeistungenProps {
		return {
			enmManager: () => routeNotenmodul.data.manager,
			auswahl: () => routeNotenmodul.data.auswahlLerngruppen,
			patchLeistung: routeNotenmodul.data.patchLeistung,
			columnsVisible: () => routeNotenmodulLeistungen.data.columnsVisible,
			setColumnsVisible: routeNotenmodulLeistungen.data.setColumnsVisible,
			floskelEditorVisible: routeNotenmodulLeistungen.data.floskelEditorVisible,
			setFloskelEditorVisible: routeNotenmodulLeistungen.data.setFloskelEditorVisible,
		};
	}

}

export const routeNotenmodulLeistungenData = new RouteNotenmodulLeistungenData();
