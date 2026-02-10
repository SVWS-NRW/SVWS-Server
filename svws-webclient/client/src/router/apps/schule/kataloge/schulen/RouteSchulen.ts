import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { SchulenAuswahlProps } from "~/components/schule/kataloge/schulen/SchulenAuswahlProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { SchulenListeManager } from "@ui";
import { routeSchulenDaten } from "~/router/apps/schule/kataloge/schulen/RouteSchulenDaten";
import { RouteDataSchulen } from "./RouteDataSchulen";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeSchulenNeu } from "~/router/apps/schule/kataloge/schulen/RouteSchulenNeu";
import { routeSchulenGruppenprozesse } from "~/router/apps/schule/kataloge/schulen/RouteSchulenGruppenprozesse";
import { routeApp } from "~/router/apps/RouteApp";

const SchulenAuswahl = () => import("~/components/schule/kataloge/schulen/SchulenAuswahl.vue");
const SchulenApp = () => import("~/components/schule/kataloge/schulen/SchulenApp.vue");

export class RouteSchulen extends RouteAuswahlNode<SchulenListeManager, RouteDataSchulen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.schulen", String.raw`schule/schulen/:id(\d+)?`, SchulenApp, SchulenAuswahl, new RouteDataSchulen());
		super.mode = ServerMode.DEV;
		super.text = "Schulen";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeSchulenDaten,
			routeSchulenNeu,
			routeSchulenGruppenprozesse,
		];
		super.defaultChild = routeSchulenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
		super.getAuswahlListProps = (props) => (<SchulenAuswahlProps> {
			...props,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
		});
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};

}

export const routeSchulen = new RouteSchulen();
