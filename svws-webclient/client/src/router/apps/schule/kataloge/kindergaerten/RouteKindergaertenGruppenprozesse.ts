import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { KindergaertenGruppenprozesseProps } from "~/components/schule/kataloge/kindergaerten/gruppenprozesse/KindergaertenGruppenprozesseProps";
import type { RouteKindergaerten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaerten";
import { RouteNode } from "~/router/RouteNode";
import { routeKindergaerten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaerten";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

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
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): KindergaertenGruppenprozesseProps {
		return {
			delete: routeKindergaerten.data.delete,
			deleteCheck: routeKindergaerten.data.deleteCheck,
			manager: () => routeKindergaerten.data.manager,
			gotoDefaultView: routeKindergaerten.data.gotoDefaultView,
		};
	}
}

export const routeKindergaertenGruppenprozesse = new RouteKindergaertenGruppenprozesse();
