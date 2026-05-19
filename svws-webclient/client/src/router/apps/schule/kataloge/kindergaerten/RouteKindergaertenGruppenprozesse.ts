import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { KindergaertenGruppenprozesseProps } from "~/components/schule/kataloge/kindergaerten/gruppenprozesse/KindergaertenGruppenprozesseProps";
import type { RouteKindergaerten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaerten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeKindergaerten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaerten";
import { abschnittState } from "~/states/AbschnittStateImpl";

const KindergaertenGruppenprozesse = () => import("~/components/schule/kataloge/kindergaerten/gruppenprozesse/KindergaertenGruppenprozesse.vue");

export class RouteKindergaertenGruppenprozesse extends RouteNode<any, RouteKindergaerten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.kindergaerten.gruppenprozesse", "gruppenprozesse", KindergaertenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittState.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): KindergaertenGruppenprozesseProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			delete: routeKindergaerten.data.delete,
			deleteCheck: routeKindergaerten.data.deleteCheck,
			manager: () => routeKindergaerten.data.manager,
			gotoDefaultView: routeKindergaerten.data.gotoDefaultView,
		};
	}
}

export const routeKindergaertenGruppenprozesse = new RouteKindergaertenGruppenprozesse();
