import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { BenutzerKompetenzGruppe } from "@core/core/types/benutzer/BenutzerKompetenzGruppe";
import { ServerMode } from "@core/core/types/ServerMode";
import type { List } from "@core/java/util/List";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";

import { type RouteApp } from "~/router/apps/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

import { routeEinstellungenBenutzer } from "./benutzer/RouteEinstellungenBenutzer";

export class RouteEinstellungen extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "einstellungen", "einstellungen");
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "Einstellungen";
		super.menugroup = AppMenuGroup.EINSTELLUNGEN;
		super.icon = "i-ri-settings-3-line";
	}

	protected async update(to: RouteNode<any, any>) {
		if (to.name === this.name) {
			return routeEinstellungenBenutzer.getRoute();
		}
	}

	public benutzerKompetenzen = (gruppe: BenutzerKompetenzGruppe): List<BenutzerKompetenz> => {
		const schuljahr = abschnittStateImpl.auswahl.schuljahr;
		return BenutzerKompetenz.getKompetenzenMitSchulform(schuljahr, gruppe, schuleStateImpl.schulform);
	};
}

export const routeEinstellungen = new RouteEinstellungen();
