import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { SchulenAuswahlProps } from "~/components/schule/kataloge/schulen/SchulenAuswahlProps";
import { routeSchulenDaten } from "~/router/apps/schule/kataloge/schulen/RouteSchulenDaten";
import { RouteDataSchulen } from "./RouteDataSchulen";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeSchulenNeu } from "~/router/apps/schule/kataloge/schulen/RouteSchulenNeu";
import { routeSchulenGruppenprozesse } from "~/router/apps/schule/kataloge/schulen/RouteSchulenGruppenprozesse";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { SchulenListeManager } from "@ui/ui/manager/kataloge/SchulenListeManager";

const SchulenAuswahl = () => import("~/components/schule/kataloge/schulen/SchulenAuswahl.vue");
const SchulenApp = () => import("~/components/schule/kataloge/schulen/SchulenApp.vue");

export class RouteSchulen extends RouteAuswahlNode<SchulenListeManager, RouteDataSchulen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.schulen", String.raw`schule/schulen/:id(\d+)?`, SchulenApp, SchulenAuswahl, new RouteDataSchulen());
		super.mode = ServerMode.STABLE;
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
