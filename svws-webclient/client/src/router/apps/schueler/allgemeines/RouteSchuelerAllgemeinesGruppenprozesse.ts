import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { SSchuelerAllgemeinesGruppenprozesseProps } from "~/components/schueler/allgemeines/SSchuelerAllgemeinesGruppenprozesseProps";
import { api } from "~/router/Api";
import { type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteNode } from "~/router/RouteNode";
import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";

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
		const schuelerAuswahlState = useSchuelerAuswahlState();
		await schuelerAuswahlState.updateMapStundenplaene();
	}

	public getProps(to: RouteLocationNormalized): SSchuelerAllgemeinesGruppenprozesseProps {
		return {
			apiStatus: api.status,
		};
	}

}

export const routeSchuelerAllgemeinesGruppenprozesse = new RouteSchuelerAllgemeinesGruppenprozesse();

