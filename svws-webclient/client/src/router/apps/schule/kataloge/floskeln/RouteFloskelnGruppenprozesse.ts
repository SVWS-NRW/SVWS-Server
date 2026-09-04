import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { RouteFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteFloskeln";
import { routeFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteFloskeln";
import type { FloskelnGruppenprozesseProps } from "~/components/schule/kataloge/floskeln/gruppenprozesse/FloskelnGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const FloskelnGruppenprozesse = () => import(
	"~/components/schule/kataloge/floskeln/gruppenprozesse/FloskelnGruppenprozesse.vue");

export class RouteFloskelnGruppenprozesse extends RouteNode<any, RouteFloskeln> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.floskeln.gruppenprozesse", "gruppenprozesse", FloskelnGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): FloskelnGruppenprozesseProps {
		return {
			delete: routeFloskeln.data.delete,
			manager: () => routeFloskeln.data.manager,
		};
	}
}

export const routeFloskelnGruppenprozesse = new RouteFloskelnGruppenprozesse();
