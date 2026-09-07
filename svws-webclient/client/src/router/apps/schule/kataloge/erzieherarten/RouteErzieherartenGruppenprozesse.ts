import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { ErzieherartenGruppenprozesseProps } from "~/components/schule/kataloge/erzieherarten/gruppenprozesse/ErzieherartenGruppenprozesseProps";
import type { RouteErzieherarten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherarten";
import { routeErzieherarten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherarten";
import { RouteNode } from "~/router/RouteNode";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";

const ErzieherartenGruppenprozesse = () => import("~/components/schule/kataloge/erzieherarten/gruppenprozesse/ErzieherartenGruppenprozesse.vue");

export class RouteErzieherartenGruppenprozesse extends RouteNode<any, RouteErzieherarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.erzieherarten.gruppenprozesse", "gruppenprozesse", ErzieherartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): ErzieherartenGruppenprozesseProps {
		return {
			manager: () => routeErzieherarten.data.manager,
			delete: routeErzieherarten.data.delete,
			deleteCheck: routeErzieherarten.data.deleteCheck,
			gotoDefaultView: routeErzieherarten.data.gotoDefaultView,
		};
	}
}

export const routeErzieherartenGruppenprozesse = new RouteErzieherartenGruppenprozesse();

