import type { RouteLocationRaw, RouteParamsRawGeneric } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import { schulformenGymOb } from "~/router/RouteHelper";
import { RouteDataSchuleDatenaustauschKurs42 } from "./RouteDataSchuleDatenaustauschKurs42";
import { routeSchuleDatenaustauschKurs42Blockung } from "./RouteSchuleDatenaustauschKurs42Blockung";
import { routeSchuleDatenaustauschKurs42Raeume } from "./RouteSchuleDatenaustauschKurs42Raeume";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteTabNode } from "~/router/RouteTabNode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const SSchuleDatenaustauschKurs42 = () => import("~/components/schule/datenaustausch/kurs42/SSchuleDatenaustauschKurs42.vue");

export class RouteSchuleDatenaustauschKurs42 extends RouteTabNode<RouteDataSchuleDatenaustauschKurs42, RouteApp> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "schule.datenaustausch.kurs42", "kurs42", SSchuleDatenaustauschKurs42, new RouteDataSchuleDatenaustauschKurs42());
		super.mode = ServerMode.STABLE;
		super.text = "Kurs42";
		super.menugroup = RouteSchuleMenuGroup.DATENAUSTAUSCH;
		super.children = [
			routeSchuleDatenaustauschKurs42Blockung,
			routeSchuleDatenaustauschKurs42Raeume,
		];
		this.defaultChild = routeSchuleDatenaustauschKurs42Blockung;
	}

	public getRouteDefaultChild(params?: RouteParamsRawGeneric): RouteLocationRaw {
		if (routeSchuleDatenaustauschKurs42Blockung.hidden() === false) {
			return routeSchuleDatenaustauschKurs42Blockung.getRoute();
		} else {
			return routeSchuleDatenaustauschKurs42Raeume.getRoute();
		}
	}

}

export const routeSchuleDatenaustauschKurs42 = new RouteSchuleDatenaustauschKurs42();

