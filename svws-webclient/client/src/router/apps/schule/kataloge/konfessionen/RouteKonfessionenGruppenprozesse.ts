import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeKonfessionen } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionen";
import type { RouteKonfessionen } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionen";
import type { KonfessionenGruppenprozesseProps } from "~/components/schule/kataloge/konfessionen/gruppenprozesse/KonfessionenGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const KonfessionenGruppenprozesse = () => import("~/components/schule/kataloge/konfessionen/gruppenprozesse/KonfessionenGruppenprozesse.vue");

export class RouteKonfessionenGruppenprozesse extends RouteNode<any, RouteKonfessionen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN],
			"schule.konfessionen.gruppenprozesse", "gruppenprozesse", KonfessionenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): KonfessionenGruppenprozesseProps {
		return {
			delete: routeKonfessionen.data.delete,
			deleteCheck: routeKonfessionen.data.deleteCheck,
			manager: () => routeKonfessionen.data.manager,
			gotoDefaultView: routeKonfessionen.data.gotoDefaultView,
		};
	}

}

export const routeKonfessionenGruppenprozesse = new RouteKonfessionenGruppenprozesse();

