import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulLeistungen, type RouteNotenmodulLeistungen } from "./RouteNotenmodulLeistungen";
import type { RouteLocationNormalized } from "vue-router";
import type { NotenmodulLeistungenProps } from "~/components/notenmodul/NotenmodulLeistungenProps";
import { notenmodulStateImpl } from "~/states/NotenmodulStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

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
		return {
			enmManager: () => notenmodulStateImpl.manager,
			auswahl: () => notenmodulStateImpl.auswahlLerngruppen,
			patchLeistung: notenmodulStateImpl.patchLeistung,
			columnsVisible: () => routeNotenmodulLeistungen.data.columnsVisible,
			setColumnsVisible: routeNotenmodulLeistungen.data.setColumnsVisible,
		};
	}

}

export const routeNotenmodulLeistungenData = new RouteNotenmodulLeistungenData();
