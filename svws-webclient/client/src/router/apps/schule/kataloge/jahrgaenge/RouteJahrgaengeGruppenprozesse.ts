import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { type RouteJahrgaenge, routeJahrgaenge } from "~/router/apps/schule/kataloge/jahrgaenge/RouteJahrgaenge";
import type { JahrgaengeGruppenprozesseProps } from "~/components/schule/kataloge/jahrgaenge/gruppenprozesse/JahrgaengeGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const JahrgaengeGruppenprozesse = () => import("~/components/schule/kataloge/jahrgaenge/gruppenprozesse/JahrgaengeGruppenprozesse.vue");

export class RouteJahrgaengeGruppenprozesse extends RouteNode<any, RouteJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.jahrgaenge.gruppenprozesse", "gruppenprozesse", JahrgaengeGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): JahrgaengeGruppenprozesseProps {
		return {
			manager: () => routeJahrgaenge.data.manager,
			delete: routeJahrgaenge.data.delete,
			deleteCheck: routeJahrgaenge.data.deleteCheck,
		};
	}

}

export const routeJahrgaengeGruppenprozesse = new RouteJahrgaengeGruppenprozesse();

