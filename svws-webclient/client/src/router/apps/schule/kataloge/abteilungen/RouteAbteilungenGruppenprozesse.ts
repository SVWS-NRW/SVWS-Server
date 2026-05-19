import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { AbteilungenGruppenprozesseProps } from "~/components/schule/kataloge/abteilungen/gruppenprozesse/AbteilungenGruppenprozesseProps";
import type { RouteAbteilungen } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { routeAbteilungen } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungen";
import { api } from "~/router/Api";
import { abschnittState } from "~/states/AbschnittStateImpl";

const AbteilungenGruppenprozesse = () => import("~/components/schule/kataloge/abteilungen/gruppenprozesse/AbteilungenGruppenprozesse.vue");

export class RouteAbteilungenGruppenprozesse extends RouteNode<any, RouteAbteilungen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.abteilungen.gruppenprozesse", "gruppenprozesse", AbteilungenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittState.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): AbteilungenGruppenprozesseProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeAbteilungen.data.manager,
			delete: routeAbteilungen.data.delete,
		};
	};
}

export const routeAbteilungenGruppenprozesse = new RouteAbteilungenGruppenprozesse();
