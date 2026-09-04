import { RouteNode } from "~/router/RouteNode";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { api } from "~/router/Api";
import type { SSchuelerAllgemeinesGruppenprozesseProps } from "~/components/schueler/allgemeines/SSchuelerAllgemeinesGruppenprozesseProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const SSchuelerAllgemeinesGruppenprozesse = () => import("~/components/schueler/allgemeines/SSchuelerAllgemeinesGruppenprozesse.vue");


export class RouteSchuelerAllgemeinesGruppenprozesse extends RouteNode<any, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "schueler.gruppenprozesse.allgemeines", "gruppenprozesse/allgemeines", SSchuelerAllgemeinesGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Allgemeines";
	}

	protected async update(): Promise<void | Error | RouteLocationRaw> {
		await routeSchueler.data.updateMapStundenplaene();
	}

	public getProps(to: RouteLocationNormalized): SSchuelerAllgemeinesGruppenprozesseProps {
		return {
			apiStatus: api.status,
			mapStundenplaene: routeSchueler.data.mapStundenplaene,
			schuelerListeManager: () => routeSchueler.data.manager,
			deleteSchueler: routeSchueler.data.delete,
			deleteSchuelerCheck: routeSchueler.data.deleteSchuelerCheck,
		};
	}

}

export const routeSchuelerAllgemeinesGruppenprozesse = new RouteSchuelerAllgemeinesGruppenprozesse();

