import type { RouteLocationNormalized, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerAbitur, type RouteSchuelerAbitur } from "~/router/apps/schueler/abitur/RouteSchuelerAbitur";
import type { SchuelerAbiturPruefungsuebersichtProps } from "~/components/schueler/abitur/SchuelerAbiturPruefungsuebersichtProps";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeSchueler } from "../RouteSchueler";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SchuelerAbiturPruefungsuebersicht = () => import("~/components/schueler/abitur/SchuelerAbiturPruefungsuebersicht.vue");

export class RouteSchuelerAbiturPruefungsuebersicht extends RouteNode<any, RouteSchuelerAbitur> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
		], "schueler.abitur.pruefungsuebersicht", "pruefungsuebersicht", SchuelerAbiturPruefungsuebersicht);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Prüfung";
		this.isHidden = (params?: RouteParams) => this.parent?.hidden(params) ?? false; // TODO automatically perform parent check in hidden method in RouteNode
		super.children = [
		];
	}

	public getProps(to: RouteLocationNormalized): SchuelerAbiturPruefungsuebersichtProps {
		return {
			schueler: routeSchueler.data.manager.auswahl(),
			manager: () => routeSchuelerAbitur.data.managerAbitur,
			updateAbiturpruefungsdaten: routeSchuelerAbitur.data.updateAbiturpruefungsdaten,
		};
	}

}

export const routeSchuelerAbiturPruefungsuebersicht = new RouteSchuelerAbiturPruefungsuebersicht();

