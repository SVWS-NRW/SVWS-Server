import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { routeNotenmodul } from "./RouteNotenmodul";
import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulKlassenleitung, type RouteNotenmodulKlassenleitung } from "./RouteNotenmodulKlassenleitung";
import type { RouteLocationNormalized } from "vue-router";
import type { NotenmodulKlassenleitungProps } from "~/components/notenmodul/NotenmodulKlassenleitungProps";

const NotenmodulKlassenleitung = () => import("~/components/notenmodul/NotenmodulKlassenleitung.vue");

export class RouteNotenmodulKlassenleitungData extends RouteNode<any, RouteNotenmodulKlassenleitung> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION,
		], "notenmodul.klassenleitung.daten", "daten", NotenmodulKlassenleitung);
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassenleitung";
	}

	public getProps(to: RouteLocationNormalized): NotenmodulKlassenleitungProps {
		return {
			enmManager: () => routeNotenmodul.data.manager,
			auswahl: () => routeNotenmodul.data.auswahlKlassen,
			patchBemerkungen: routeNotenmodul.data.patchBemerkungen,
			patchLernabschnitt: routeNotenmodul.data.patchLernabschnitt,
			columnsVisible: () => routeNotenmodulKlassenleitung.data.columnsVisible,
			setColumnsVisible: routeNotenmodulKlassenleitung.data.setColumnsVisible,
			floskelEditorVisible: routeNotenmodulKlassenleitung.data.floskelEditorVisible,
			setFloskelEditorVisible: routeNotenmodulKlassenleitung.data.setFloskelEditorVisible,
		};
	}

}

export const routeNotenmodulKlassenleitungData = new RouteNotenmodulKlassenleitungData();
