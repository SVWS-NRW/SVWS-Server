import { RouteNode } from "~/router/RouteNode";
import { routeBetriebsarten, type RouteBetriebsarten } from "./RouteBetriebsarten";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { BetriebsartenGruppenprozesseProps } from "~/components/schule/kataloge/betriebsarten/gruppenprozesse/BetriebsartenGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const BetriebsartenGruppenprozesse = () => import(
	"~/components/schule/kataloge/betriebsarten/gruppenprozesse/BetriebsartenGruppenprozesse.vue");

class RouteBetriebsartenGruppenprozesse extends RouteNode<any, RouteBetriebsarten> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.betriebsarten.gruppenprozesse", "gruppenprozesse", BetriebsartenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): BetriebsartenGruppenprozesseProps {
		return {
			manager: () => routeBetriebsarten.data.manager,
			delete: routeBetriebsarten.data.delete,
			deleteCheck: routeBetriebsarten.data.deleteCheck,
			gotoDefaultView: routeBetriebsarten.data.gotoDefaultView,
		};
	}
}

export const routeBetriebsartenGruppenprozesse = new RouteBetriebsartenGruppenprozesse();

