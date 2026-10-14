import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { useNotenmodulState } from "@ui/states/NotenmodulState";

import type { NotenmodulTeilleistungenProps } from "~/components/notenmodul/NotenmodulTeilleistungenProps";
import { RouteNode } from "~/router/RouteNode";

import { type RouteNotenmodulTeilleistungen, routeNotenmodulTeilleistungen } from "./RouteNotenmodulTeilleistungen";

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
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Teilleistungen";
	}

	public getProps(to: RouteLocationNormalized): NotenmodulTeilleistungenProps {
		const notenmodulState = useNotenmodulState();
		return {
			enmManager: () => notenmodulState.manager,
			auswahl: () => notenmodulState.auswahlLerngruppen,
			patchLeistung: notenmodulState.patchLeistung,
			patchTeilleistung: notenmodulState.patchTeilleistung,
			columnsVisible: () => routeNotenmodulTeilleistungen.data.columnsVisible,
			setColumnsVisible: routeNotenmodulTeilleistungen.data.setColumnsVisible,
		};
	}

}

export const routeNotenmodulTeilleistungenData = new RouteNotenmodulTeilleistungenData();
