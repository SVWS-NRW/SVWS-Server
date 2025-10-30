import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import { type RouteApp } from "~/router/apps/RouteApp";

import { RouteDataNotenmodul } from "~/router/apps/notenmodul/RouteDataNotenmodul";
import { AppMenuGroup } from "@ui";


export class RouteNotenmodul extends RouteNode<RouteDataNotenmodul, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION,
		], "notenmodul", "notenmodul", undefined, new RouteDataNotenmodul());
		super.text = "Noten";
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getNoProps(route);
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-music-2-fill";
	}

}

export const routeNotenmodul = new RouteNotenmodul();
