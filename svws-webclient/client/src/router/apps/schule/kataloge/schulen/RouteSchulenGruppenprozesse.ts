import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { routeSchulen, type RouteSchulen } from "~/router/apps/schule/kataloge/schulen/RouteSchulen";
import type { SchulenGruppenprozesseProps } from "~/components/schule/kataloge/schulen/gruppenprozesse/SchulenGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";

const SchulenGruppenprozesse = () => import("~/components/schule/kataloge/schulen/gruppenprozesse/SchulenGruppenprozesse.vue");

export class RouteSchulenGruppenprozesse extends RouteNode<any, RouteSchulen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.schulen.gruppenprozesse", "gruppenprozesse", SchulenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): SchulenGruppenprozesseProps {
		return {
			manager: () => routeSchulen.data.manager,
			delete: routeSchulen.data.delete,
			deleteCheck: routeSchulen.data.deleteCheck,
		};
	}

}

export const routeSchulenGruppenprozesse = new RouteSchulenGruppenprozesse();

