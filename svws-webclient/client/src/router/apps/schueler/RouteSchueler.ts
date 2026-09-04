import type { RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuelerAuswahlProps } from "~/components/schueler/SSchuelerAuswahlProps";
import type { SchuelerAppProps } from "~/components/schueler/SSchuelerAppProps";
import type { SchuelerListeManager } from "~/states/schueler/SchuelerListeManager";
import type { RouteNode } from "~/router/RouteNode";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { RouteDataSchueler } from "~/router/apps/schueler/RouteDataSchueler";
import { routeSchuelerBetriebe } from "~/router/apps/schueler/betriebe/RouteSchuelerBetriebe";
import { routeSchuelerErziehungsberechtigte } from "~/router/apps/schueler/erziehungsberechtigte/RouteSchuelerErziehungsberechtigte";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";
import { routeSchuelerLernabschnitte } from "~/router/apps/schueler/lernabschnitte/RouteSchuelerLernabschnitte";
import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/schulbesuch/RouteSchuelerSchulbesuch";
import { routeSchuelerStundenplan } from "~/router/apps/schueler/stundenplan/RouteSchuelerStundenplan";
import { routeSchuelerKAoA } from "~/router/apps/schueler/kaoa/RouteSchuelerKAoA";
import { routeSchuelerSprachen } from "./sprachen/RouteSchuelerSprachen";
import { routeSchuelerSonstiges } from "./sonstiges/RouteSchuelerSonstiges";
import { routeSchuelerAllgemeinesGruppenprozesse } from "~/router/apps/schueler/allgemeines/RouteSchuelerAllgemeinesGruppenprozesse";
import { routeSchuelerIndividualdatenGruppenprozesse } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdatenGruppenprozesse";
import { routeSchuelerAbitur } from "./abitur/RouteSchuelerAbitur";
import { routeSchuelerSchnelleingabe } from "~/router/apps/schueler/neu/RouteSchuelerSchnelleingabe";
import { routeSchuelerNeu } from "~/router/apps/schueler/neu/RouteSchuelerNeu";
import { Katalog } from "~/cache/Katalog";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { orteStateImpl } from "~/states/kataloge/OrteStateImpl";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SSchuelerAuswahl = () => import("~/components/schueler/SSchuelerAuswahl.vue");
const SSchuelerApp = () => import("~/components/schueler/SSchuelerApp.vue");


export class RouteSchueler extends RouteAuswahlNode<SchuelerListeManager, RouteDataSchueler, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "schueler", "schueler/:id(\\d+)?", SSchuelerApp, SSchuelerAuswahl, new RouteDataSchueler());
		super.mode = ServerMode.STABLE;
		super.text = "Schüler";
		super.getAuswahlListProps = (props) => (<SchuelerAuswahlProps>{
			...props,
			schulform: schuleStateImpl.schulform,
		});
		super.getAuswahlProps = props => (<SchuelerAppProps>{
			...props,
			schulform: schuleStateImpl.schulform,
			gotoDefaultView: this.data.gotoDefaultView,
		});
		super.children = [
			routeSchuelerIndividualdaten,
			routeSchuelerSonstiges,
			routeSchuelerErziehungsberechtigte,
			routeSchuelerBetriebe,
			routeSchuelerKAoA,
			routeSchuelerSchulbesuch,
			routeSchuelerLernabschnitte,
			routeSchuelerSprachen,
			routeSchuelerLaufbahnplanung,
			routeSchuelerAbitur,
			routeSchuelerStundenplan,
			routeSchuelerSchnelleingabe,
			routeSchuelerAllgemeinesGruppenprozesse,
			routeSchuelerIndividualdatenGruppenprozesse,
			routeSchuelerNeu,
		];
		super.defaultChild = routeSchuelerIndividualdaten;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-group-line";
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await Promise.all([orteStateImpl.init(), routeApp.cache.refreshKataloge(Katalog.BETRIEBE, Katalog.BESCHAEFTIGUNGSARTEN, Katalog.EINSCHULUNGSARTEN, Katalog.ENTLASSGRUENDE,
				Katalog.ERZIEHERARTEN, Katalog.FAHRSCHUELERARTEN, Katalog.FOERDERSCHWERPUNKTE, Katalog.HALTESTELLEN, Katalog.KINDERGAERTEN, Katalog.JAHRGAENGE,
				Katalog.MERKMALE, Katalog.RELIGIONEN, Katalog.SCHULEN, Katalog.TELEFONARTEN, Katalog.VERMERKARTEN)]);
		}
		return super.update(to, to_params, from, from_params, isEntering, redirected);
	}
}

export const routeSchueler = new RouteSchueler();
