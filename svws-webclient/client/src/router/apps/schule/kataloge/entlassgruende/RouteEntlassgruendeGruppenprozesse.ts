import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { EntlassgruendeGruppenprozesseProps } from "~/components/schule/kataloge/entlassgruende/gruppenprozesse/EntlassgruendeGruppenprozesseProps";
import type { RouteEntlassgruende } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruende";
import { RouteNode } from "~/router/RouteNode";
import { routeEntlassgruende } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruende";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const EntlassgruendeGruppenprozesse = () => import("~/components/schule/kataloge/entlassgruende/gruppenprozesse/EntlassgruendeGruppenprozesse.vue");

export class RouteEntlassgruendeGruppenprozesse extends RouteNode<any, RouteEntlassgruende> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN,
			BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.entlassgruende.gruppenprozesse", "gruppenprozesse", EntlassgruendeGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): EntlassgruendeGruppenprozesseProps {
		return {
			delete: routeEntlassgruende.data.delete,
			deleteCheck: routeEntlassgruende.data.deleteCheck,
			manager: () => routeEntlassgruende.data.manager,
		};
	}

}

export const routeEntlassgruendeGruppenprozesse = new RouteEntlassgruendeGruppenprozesse();
