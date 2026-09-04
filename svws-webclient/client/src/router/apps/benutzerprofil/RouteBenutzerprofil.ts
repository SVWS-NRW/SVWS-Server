import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { routeBenutzerprofilNutzereinstellungen } from "~/router/apps/benutzerprofil/nutzereinstellungen/RouteBenutzerprofilNutzereinstellungen";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { BenutzerKompetenzGruppe } from "@core/core/types/benutzer/BenutzerKompetenzGruppe";
import { ServerMode } from "@core/core/types/ServerMode";
import type { List } from "@core/java/util/List";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";

export class RouteBenutzerprofil extends RouteNode<any, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "benutzerprofil", "benutzerprofil");
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "Benutzerprofil";
		super.menugroup = AppMenuGroup.BENUTZERPROFIL;
	}


	protected async update(to: RouteNode<any, any>) {
		if (to.name === this.name) {
			// redirect to routeBenutzerprofilNutzereinstellungen
			return routeBenutzerprofilNutzereinstellungen.getRoute();
		}
	}

	public benutzerKompetenzen = (gruppe: BenutzerKompetenzGruppe): List<BenutzerKompetenz> => {
		const schuljahr = abschnittStateImpl.auswahl.schuljahr;
		const schulformEintrag = schuleStateImpl.schulform.daten(schuljahr);
		const schulform = Schulform.data().getWertByID(schulformEintrag?.id ?? -1);
		return BenutzerKompetenz.getKompetenzenMitSchulform(schuljahr, gruppe, schulform);
	};
}

export const routeBenutzerprofil = new RouteBenutzerprofil();
