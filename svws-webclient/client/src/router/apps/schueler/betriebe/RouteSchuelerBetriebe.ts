import type { RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerBetriebe } from "~/router/apps/schueler/betriebe/RouteDataSchuelerBetriebe";

const SchuelerBetriebe = () => import("~/components/schueler/betriebe/SchuelerBetriebe.vue");

export class RouteSchuelerBetriebe extends RouteNode<RouteDataSchuelerBetriebe, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN], "schueler.ausbildungsbetriebe", "ausbildungsbetriebe", SchuelerBetriebe, new RouteDataSchuelerBetriebe());
		super.mode = ServerMode.ALPHA;
		super.text = "Betriebe";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {

	}
}

export const routeSchuelerAusbildungsbetriebe = new RouteSchuelerBetriebe();

