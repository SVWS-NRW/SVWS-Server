import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteNode } from "~/router/RouteNode";


export class RouteNotenmodul extends RouteNode<never, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
			BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION,
		], "notenmodul", "notenmodul");
		super.text = "Noten";
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-music-2-fill";
	}

}

export const routeNotenmodul = new RouteNotenmodul();
