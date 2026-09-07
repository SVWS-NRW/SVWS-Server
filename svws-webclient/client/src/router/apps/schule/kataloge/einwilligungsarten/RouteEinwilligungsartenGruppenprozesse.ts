import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { EinwilligungsartenGruppenprozesseProps } from "~/components/schule/kataloge/einwilligungsarten/gruppenprozesse/EinwilligungsartenGruppenprozesseProps";
import type { RouteEinwilligungsarten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsarten";
import { routeEinwilligungsarten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsarten";
import { RouteNode } from "~/router/RouteNode";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";

const EinwilligungsartenGruppenprozesse = () => import("~/components/schule/kataloge/einwilligungsarten/gruppenprozesse/EinwilligungsartenGruppenprozesse.vue");

export class RouteEinwilligungsartenGruppenprozesse extends RouteNode<any, RouteEinwilligungsarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.einwilligungsarten.gruppenprozesse", "gruppenprozesse", EinwilligungsartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): EinwilligungsartenGruppenprozesseProps {
		return {
			delete: routeEinwilligungsarten.data.delete,
			deleteCheck: routeEinwilligungsarten.data.deleteCheck,
			manager: () => routeEinwilligungsarten.data.manager,
			gotoDefaultView: routeEinwilligungsarten.data.gotoDefaultView,
		};
	}

}

export const routeEinwilligungsartenGruppenprozesse = new RouteEinwilligungsartenGruppenprozesse();

