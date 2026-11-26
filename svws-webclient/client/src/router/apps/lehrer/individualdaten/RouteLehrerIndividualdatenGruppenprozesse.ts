import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import type { RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { RouteNode } from "~/router/RouteNode";
import { useLehrerAuswahlState } from "~/states/lehrer/LehrerAuswahlState";
import { LehrerStammdatenBereich } from "~/states/lehrer/LehrerStammdatenBereich";

const LehrerIndividualdatenGruppenprozesse = () => import("~/components/lehrer/individualdaten/LehrerIndividualdatenGruppenprozesse.vue");

export class RouteLehrerIndividualdatenGruppenprozesse extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "lehrer.gruppenprozesse.daten", "gruppenprozesse/daten", LehrerIndividualdatenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV;
		super.text = "Individualdaten";
	}

	protected async update(): Promise<void | Error | RouteLocationRaw> {
		// Der Bereich der Stammdaten wird beim Betreten des Tabs bei den Gruppenprozessen angemeldet
		await useLehrerAuswahlState().gruppenprozesse.register(LehrerStammdatenBereich.SCHLUESSEL, () => new LehrerStammdatenBereich());
	}

}

export const routeLehrerIndividualdatenGruppenprozesse = new RouteLehrerIndividualdatenGruppenprozesse();

