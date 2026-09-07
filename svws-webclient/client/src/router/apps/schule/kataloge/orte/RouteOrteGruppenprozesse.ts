import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { OrteGruppenprozesseProps } from "~/components/schule/kataloge/orte/gruppenprozesse/OrteGruppenprozesseProps";
import type { RouteOrte } from "~/router/apps/schule/kataloge/orte/RouteOrte";
import { routeOrte } from "~/router/apps/schule/kataloge/orte/RouteOrte";
import { RouteNode } from "~/router/RouteNode";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";

const OrteGruppenprozesse = () => import("~/components/schule/kataloge/orte/gruppenprozesse/OrteGruppenprozesse.vue");

export class RouteOrteGruppenprozesse extends RouteNode<any, RouteOrte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.orte.gruppenprozesse", "gruppenprozesse", OrteGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(_: RouteLocationNormalized): OrteGruppenprozesseProps {
		return {
			manager: () => routeOrte.data.manager,
			delete: routeOrte.data.delete,
			deleteCheck: routeOrte.data.deleteCheck,
		};
	}

}

export const routeOrteGruppenprozesse = new RouteOrteGruppenprozesse();

