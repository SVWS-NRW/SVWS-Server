import type { RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { SchulinterneFunktionenListeManager } from "@ui/ui/manager/kataloge/SchulinterneFunktionenListeManager";

import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteNode } from "~/router/RouteNode";

import { RouteDataSchulinterneFunktionen } from "./RouteDataSchulinterneFunktionen";
import { routeSchulinterneFunktionenDaten } from "./RouteSchulinterneFunktionenDaten";
import { routeSchulinterneFunktionenGruppenprozesse } from "./RouteSchulinterneFunktionenGruppenprozesse";
import { routeSchulinterneFunktionenNeu } from "./RouteSchulinterneFunktionenNeu";

const SchulinterneFunktionenApp = () => import("~/components/schule/kataloge/schulinternefunktionen/SchulinterneFunktionenApp.vue");
const SchulinterneFunktionenAuswahl = () => import("~/components/schule/kataloge/schulinternefunktionen/SchulinterneFunktionenAuswahl.vue");

export class RouteSchulinterneFunktionen extends RouteAuswahlNode<SchulinterneFunktionenListeManager, RouteDataSchulinterneFunktionen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.schulinternefunktionen",
			String.raw`schule/schulinternefunktionen/:id(\d+)?`, SchulinterneFunktionenApp, SchulinterneFunktionenAuswahl, new RouteDataSchulinterneFunktionen());
		super.text = "Schulinterne Funktionen";
		super.mode = ServerMode.STABLE;
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeSchulinterneFunktionenDaten,
			routeSchulinterneFunktionenNeu,
			routeSchulinterneFunktionenGruppenprozesse,
		];
		super.defaultChild = routeSchulinterneFunktionenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeSchulinterneFunktionen = new RouteSchulinterneFunktionen();
