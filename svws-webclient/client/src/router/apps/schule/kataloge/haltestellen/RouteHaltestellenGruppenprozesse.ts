import type { HaltestellenGruppenprozesseProps } from "~/components/schule/kataloge/haltestellen/gruppenprozesse/HaltestellenGruppenprozesseProps";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteHaltestellen } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellen";
import { RouteNode } from "~/router/RouteNode";
import { routeHaltestellen } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellen";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const HaltestellenGruppenprozesse = () => import("~/components/schule/kataloge/haltestellen/gruppenprozesse/HaltestellenGruppenprozesse.vue");

export class RouteHaltestellenGruppenprozesse extends RouteNode<any, RouteHaltestellen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.haltestellen.gruppenprozesse", "gruppenprozesse", HaltestellenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): HaltestellenGruppenprozesseProps {
		return {
			delete: routeHaltestellen.data.delete,
			deleteCheck: routeHaltestellen.data.deleteCheck,
			manager: () => routeHaltestellen.data.manager,
			gotoDefaultView: routeHaltestellen.data.gotoDefaultView,
		};
	}
}

export const routeHaltestellenGruppenprozesse = new RouteHaltestellenGruppenprozesse();
