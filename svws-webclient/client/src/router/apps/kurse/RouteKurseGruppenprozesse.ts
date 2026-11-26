import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { KurseGruppenprozesseProps } from "~/components/kurse/gruppenprozesse/KurseGruppenprozesseProps";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { useKurseAuswahlState } from "~/states/kurse/KurseAuswahlState";
import { KurseDatenBereich } from "~/states/kurse/KurseDatenBereich";

import { type RouteKurse, routeKurse } from "./RouteKurse";

const KurseGruppenprozesse = () => import("~/components/kurse/gruppenprozesse/KurseGruppenprozesse.vue");

export class RouteKurseGruppenprozesse extends RouteNode<any, RouteKurse> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "kurse.gruppenprozesse", "gruppenprozesse", KurseGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	protected async update(): Promise<void | Error | RouteLocationRaw> {
		await useKurseAuswahlState().gruppenprozesse.register(KurseDatenBereich.SCHLUESSEL, () => new KurseDatenBereich());
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps(to: RouteLocationNormalized): KurseGruppenprozesseProps {
		return {
			apiStatus: api.status,
		};
	}

}

export const routeKurseGruppenprozesse = new RouteKurseGruppenprozesse();

