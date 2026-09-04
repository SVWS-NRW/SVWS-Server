import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulKlassenleitung, type RouteNotenmodulKlassenleitung } from "./RouteNotenmodulKlassenleitung";
import type { RouteLocationNormalized } from "vue-router";
import type { NotenmodulKlassenleitungProps } from "~/components/notenmodul/NotenmodulKlassenleitungProps";
import { notenmodulStateImpl } from "~/states/NotenmodulStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

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
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassenleitung";
	}

	public getProps(to: RouteLocationNormalized): NotenmodulKlassenleitungProps {
		return {
			enmManager: () => notenmodulStateImpl.manager,
			auswahl: () => notenmodulStateImpl.auswahlKlassen,
			patchBemerkungen: notenmodulStateImpl.patchBemerkungen,
			patchLernabschnitt: notenmodulStateImpl.patchLernabschnitt,
			columnsVisible: () => routeNotenmodulKlassenleitung.data.columnsVisible,
			setColumnsVisible: routeNotenmodulKlassenleitung.data.setColumnsVisible,
		};
	}

}

export const routeNotenmodulKlassenleitungData = new RouteNotenmodulKlassenleitungData();
