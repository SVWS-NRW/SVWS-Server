import type { BenutzerKompetenzGruppe, List} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";

import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { routeEinstellungenBenutzer } from "./benutzer/RouteEinstellungenBenutzer";
import { api } from "~/router/Api";
import { AppMenuGroup } from "@ui";

export class RouteEinstellungen extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "einstellungen", "einstellungen");
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "Einstellungen";
		super.menugroup = AppMenuGroup.EINSTELLUNGEN;
		super.icon = "i-ri-settings-3-line";
	}

	protected async update(to: RouteNode<any, any>) {
		if (to.name === this.name)
			return routeEinstellungenBenutzer.getRoute();
	}

	public benutzerKompetenzen = (gruppe : BenutzerKompetenzGruppe) : List<BenutzerKompetenz> => {
		const schuljahr = routeApp.data.aktAbschnitt.value.schuljahr;
		const schulformEintrag = api.schulform.daten(schuljahr);
		const schulform = Schulform.data().getWertByID(schulformEintrag?.id ?? -1);
		return BenutzerKompetenz.getKompetenzenMitSchulform(schuljahr, gruppe, schulform);
	}
}

export const routeEinstellungen = new RouteEinstellungen();
