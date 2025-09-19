import type { RouteParams } from "vue-router";

import type { SchuelerAuswahlProps } from "~/components/schueler/SSchuelerAuswahlProps";
import type { SchuelerListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import type { RouteNode } from "~/router/RouteNode";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteDataSchueler } from "~/router/apps/schueler/RouteDataSchueler";
import { routeSchuelerNeu } from "~/router/apps/schueler/RouteSchuelerNeu";
import { routeSchuelerAusbildungsbetriebe } from "~/router/apps/schueler/ausbildungsbetriebe/RouteSchuelerAusbildungsbetriebe";
import { routeSchuelerErziehungsberechtigte } from "~/router/apps/schueler/erziehungsberechtigte/RouteSchuelerErziehungsberechtigte";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";
import { routeSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";
import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/schulbesuch/RouteSchuelerSchulbesuch";
import { routeSchuelerStundenplan } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplan";
import { routeSchuelerKAoA } from "~/router/apps/schueler/kaoa/RouteSchuelerKAoA";
import { routeSchuelerSprachen } from "./sprachen/RouteSchuelerSprachen";
import { AppMenuGroup } from "@ui";
import { api } from "~/router/Api";
import type { SchuelerAppProps } from "~/components/schueler/SSchuelerAppProps";
import { routeSchuelerSonstiges } from "./sonstiges/RouteSchuelerSonstiges";
import { routeSchuelerAllgemeinesGruppenprozesse } from "~/router/apps/schueler/allgemeines/RouteSchuelerAllgemeinesGruppenprozesse";
import { routeSchuelerIndividualdatenGruppenprozesse } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdatenGruppenprozesse";
import { routeSchuelerAbitur } from "./abitur/RouteSchuelerAbitur";
import { routeSchuelerNeuSchnelleingabe } from "~/router/apps/schueler/RouteSchuelerNeuSchnelleingabe";

const SSchuelerAuswahl = () => import("~/components/schueler/SSchuelerAuswahl.vue")
const SSchuelerApp = () => import("~/components/schueler/SSchuelerApp.vue")


export class RouteSchueler extends RouteAuswahlNode<SchuelerListeManager, RouteDataSchueler, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler", "schueler/:id(\\d+)?", SSchuelerApp, SSchuelerAuswahl, new RouteDataSchueler());
		super.mode = ServerMode.STABLE;
		super.text = "Schüler";
		super.getAuswahlListProps = (props) => (<SchuelerAuswahlProps>{
			...props,
			schulform: api.schulform,
		});
		super.getAuswahlProps = props => (<SchuelerAppProps>{
			...props,
			schulform: api.schulform,
			gotoDefaultView: this.data.gotoDefaultView,
		});
		super.children = [
			routeSchuelerIndividualdaten,
			routeSchuelerSonstiges,
			routeSchuelerErziehungsberechtigte,
			routeSchuelerAusbildungsbetriebe,
			routeSchuelerKAoA,
			routeSchuelerSchulbesuch,
			routeSchuelerLernabschnitte,
			routeSchuelerSprachen,
			routeSchuelerLaufbahnplanung,
			routeSchuelerAbitur,
			routeSchuelerStundenplan,
			routeSchuelerNeuSchnelleingabe,
			routeSchuelerAllgemeinesGruppenprozesse,
			routeSchuelerIndividualdatenGruppenprozesse,
			routeSchuelerNeu,
		];
		super.defaultChild = routeSchuelerIndividualdaten;
		super.updateIfTarget = this.doUpdateIfTarget;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-group-line";
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		if ((from !== undefined) && (/(\.|^)stundenplan/).test(from.name))
			return this.getRouteView(routeSchuelerStundenplan);
		return this.getRouteSelectedChild();
	};

}

export const routeSchueler = new RouteSchueler();
