import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { LehrerAllgemeinesGruppenprozesseProps } from "~/components/lehrer/allgemeines/LehrerAllgemeinesGruppenprozesseProps";
import { api } from "~/router/Api";
import type { RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { RouteNode } from "~/router/RouteNode";
import { useLehrerAuswahlState } from "~/states/lehrer/LehrerAuswahlState";

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
		const lehrerAuswahlState = useLehrerAuswahlState();
		await lehrerAuswahlState.updateMapStundenplaene();
	}

	public getProps(to: RouteLocationNormalized): LehrerAllgemeinesGruppenprozesseProps {
		return {
			apiStatus: api.status,
		};
	}

}

export const routeLehrerAllgemeinesGruppenprozesse = new RouteLehrerAllgemeinesGruppenprozesse();

