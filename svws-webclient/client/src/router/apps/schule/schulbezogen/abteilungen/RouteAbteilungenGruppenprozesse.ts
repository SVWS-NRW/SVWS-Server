import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { AbteilungenGruppenprozesseProps } from "~/components/schule/schulbezogen/abteilungen/gruppenprozesse/SAbteilungenGruppenprozesseProps";
import type { RouteAbteilungen } from "~/router/apps/schule/schulbezogen/abteilungen/RouteAbteilungen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { routeAbteilungen } from "~/router/apps/schule/schulbezogen/abteilungen/RouteAbteilungen";
import { routeApp } from "../../../RouteApp";
import { api } from "~/router/Api";

const SAbteilungenGruppenprozesse = () =>
	import("~/components/schule/schulbezogen/abteilungen/gruppenprozesse/SAbteilungenGruppenprozesse.vue")

export class RouteAbteilungenGruppenprozesse extends RouteNode<any, RouteAbteilungen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN ],
			"schule.abteilungen.gruppenprozesse", "gruppenprozesse", SAbteilungenGruppenprozesse);
		super.types = new Set([ ViewType.GRUPPENPROZESSE ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" }}
	}

	public getProps(to: RouteLocationNormalized): AbteilungenGruppenprozesseProps {
		return {
			serverMode: api.mode,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
			manager: () => routeAbteilungen.data.manager,
			deleteAbteilungen: routeAbteilungen.data.delete,
		}
	};
}

export const routeAbteilungenGruppenprozesse = new RouteAbteilungenGruppenprozesse();
