import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { routeNotenmodul } from "./RouteNotenmodul";
import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized } from "vue-router";
import type { NotenmodulAnkreuzkompetenzenProps } from "~/components/notenmodul/NotenmodulAnkreuzkompetenzenProps";
import type { RouteNotenmodulAnkreuzkompetenzen } from "./RouteNotenmodulAnkreuzkompetenzen";

const NotenmodulAnkreuzkompetenzen = () => import("~/components/notenmodul/NotenmodulAnkreuzkompetenzen.vue");

export class RouteNotenmodulAnkreuzkompetenzenData extends RouteNode<any, RouteNotenmodulAnkreuzkompetenzen> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION,
		], "notenmodul.ankreuzkompetenzen.daten", "daten", NotenmodulAnkreuzkompetenzen);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ankreuzkompetenzen";
	}

	public getProps(to: RouteLocationNormalized): NotenmodulAnkreuzkompetenzenProps {
		return {
			enmManager: () => routeNotenmodul.data.manager,
			auswahl: () => routeNotenmodul.data.auswahlKlassen,
			patchLeistung: routeNotenmodul.data.patchLeistung,
			patchAnkreuzkompetenz: routeNotenmodul.data.patchAnkreuzkompetenz,
		};
	}

}

export const routeNotenmodulAnkreuzkompetenzenData = new RouteNotenmodulAnkreuzkompetenzenData();
