import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { NotenmodulVerbindungGruppenprozesseProps } from "~/components/notenmodul/NotenmodulVerbindungGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const NotenmodulVerbindungGruppenprozesse = () => import("~/components/notenmodul/NotenmodulVerbindungGruppenprozesse.vue");

export class RouteNotenmodulVerbindungGruppenprozesse extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.gruppenprozesse", "gruppenprozesse", NotenmodulVerbindungGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): NotenmodulVerbindungGruppenprozesseProps {
		return {
			manager: () => routeNotenmodulAdministration.data.manager,
			deleteVerbindung: routeNotenmodulAdministration.data.delete,
		};
	}

}

export const routeNotenmodulVerbindungGruppenprozesse = new RouteNotenmodulVerbindungGruppenprozesse();

