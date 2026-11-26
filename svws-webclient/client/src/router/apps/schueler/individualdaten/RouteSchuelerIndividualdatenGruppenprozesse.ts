import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

import { routeApp } from "~/router/apps/RouteApp";
import { type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeError } from "~/router/error/RouteError";
import { RouteNode } from "~/router/RouteNode";
import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";
import { SchuelerStammdatenBereich } from "~/states/schueler/SchuelerStammdatenBereich";
import { serverStateImpl } from "~/states/ServerStateImpl";

const SchuelerIndividualdatenGruppenprozesse = () => import("~/components/schueler/individualdaten/SchuelerIndividualdatenGruppenprozesse.vue");

export class RouteSchuelerIndividualdatenGruppenprozesse extends RouteNode<any, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "schueler.gruppenprozesse.daten", "gruppenprozesse/daten", SchuelerIndividualdatenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.DEV; // checkHidden() kann wieder entfernt werden, sobald der ServerMode für diese Route auf Stable gesetzt ist
		super.text = "Individualdaten";
		this.isHidden = (params?: RouteParams) => this.checkHidden(params);
	}

	protected checkHidden(params?: RouteParams) {
		try {
			if (serverStateImpl.hasDev) {
				return false;
			}
			return this.getRoute(params);
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(): Promise<void | Error | RouteLocationRaw> {
		// Der Bereich der Stammdaten wird beim Betreten des Tabs bei den Gruppenprozessen angemeldet
		await useSchuelerAuswahlState().gruppenprozesse.register(SchuelerStammdatenBereich.SCHLUESSEL, () => new SchuelerStammdatenBereich());
	}

}

export const routeSchuelerIndividualdatenGruppenprozesse = new RouteSchuelerIndividualdatenGruppenprozesse();
