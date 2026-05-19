import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import type { RouteTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import { routeTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import type { TelefonartenGruppenprozesseProps } from "~/components/schule/kataloge/telefonarten/gruppenprozesse/TelefonartenGruppenprozesseProps";
import { abschnittState } from "~/states/AbschnittStateImpl";

const TelefonartenGruppenprozesse = () => import("~/components/schule/kataloge/telefonarten/gruppenprozesse/TelefonartenGruppenprozesse.vue");

export class RouteTelefonartenGruppenprozesse extends RouteNode<any, RouteTelefonarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.telefonarten.gruppenprozesse", "gruppenprozesse", TelefonartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittState.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): TelefonartenGruppenprozesseProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeTelefonarten.data.manager,
			delete: routeTelefonarten.data.delete,
			deleteCheck: routeTelefonarten.data.deleteCheck,
			gotoDefaultView: routeTelefonarten.data.gotoDefaultView,
		};
	}
}

export const routeTelefonartenGruppenprozesse = new RouteTelefonartenGruppenprozesse();

