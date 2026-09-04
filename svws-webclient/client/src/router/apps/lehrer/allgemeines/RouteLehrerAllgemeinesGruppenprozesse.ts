import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { api } from "~/router/Api";
import type { RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import type { LehrerAllgemeinesGruppenprozesseProps } from "~/components/lehrer/allgemeines/LehrerAllgemeinesGruppenprozesseProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const LehrerAllgemeinesGruppenprozesse = () => import("~/components/lehrer/allgemeines/LehrerAllgemeinesGruppenprozesse.vue");


export class RouteLehrerAllgemeinesGruppenprozesse extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "lehrer.gruppenprozesse.allgemeines", "gruppenprozesse/allgemeines", LehrerAllgemeinesGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Allgemeines";
	}

	protected async update(): Promise<void | Error | RouteLocationRaw> {
		await routeLehrer.data.updateMapStundenplaene();
	}

	public getProps(to: RouteLocationNormalized): LehrerAllgemeinesGruppenprozesseProps {
		return {
			apiStatus: api.status,
			mapStundenplaene: routeLehrer.data.mapStundenplaene,
			lehrerListeManager: () => routeLehrer.data.manager,
			deleteLehrer: routeLehrer.data.delete,
			deleteCheck: routeLehrer.data.deleteCheck,
		};
	}

}

export const routeLehrerAllgemeinesGruppenprozesse = new RouteLehrerAllgemeinesGruppenprozesse();

