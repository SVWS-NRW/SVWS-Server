import { AppMenuGroup } from "@ui";
import { BenutzerKompetenz, type BenutzerKompetenzGruppe, type List, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { routeBenutzerprofilNutzereinstellungen } from "~/router/apps/benutzerprofil/nutzereinstellungen/RouteBenutzerprofilNutzereinstellungen";

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
		const schuljahr = routeApp.data.aktAbschnitt.value.schuljahr;
		const schulformEintrag = api.schulform.daten(schuljahr);
		const schulform = Schulform.data().getWertByID(schulformEintrag?.id ?? -1);
		return BenutzerKompetenz.getKompetenzenMitSchulform(schuljahr, gruppe, schulform);
	};
}

export const routeBenutzerprofil = new RouteBenutzerprofil();
