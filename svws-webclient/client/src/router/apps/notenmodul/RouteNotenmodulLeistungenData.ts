import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { useNotenmodulState } from "@ui/states/NotenmodulState";

import type { NotenmodulLeistungenProps } from "~/components/notenmodul/NotenmodulLeistungenProps";
import { RouteNode } from "~/router/RouteNode";

import { type RouteNotenmodulLeistungen, routeNotenmodulLeistungen } from "./RouteNotenmodulLeistungen";

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
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leistungsdaten";
	}

	public getProps(to: RouteLocationNormalized): NotenmodulLeistungenProps {
		const notenmodulState = useNotenmodulState();
		return {
			enmManager: () => notenmodulState.manager,
			auswahl: () => notenmodulState.auswahlLerngruppen,
			patchLeistung: notenmodulState.patchLeistung,
			columnsVisible: () => routeNotenmodulLeistungen.data.columnsVisible,
			setColumnsVisible: routeNotenmodulLeistungen.data.setColumnsVisible,
		};
	}

}

export const routeNotenmodulLeistungenData = new RouteNotenmodulLeistungenData();
