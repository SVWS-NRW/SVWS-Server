import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteTabNode } from "~/router/RouteTabNode";

import { RouteDataSchuleDatenaustauschUntis } from "./RouteDataSchuleDatenaustauschUntis";
import { routeSchuleDatenaustauschUntisExporte } from "./RouteSchuleDatenaustauschUntisExporte";
import { routeSchuleDatenaustauschUntisImporte } from "./RouteSchuleDatenaustauschUntisImporte";

const SSchuleDatenaustauschUntis = () => import("~/components/schule/datenaustausch/untis/SSchuleDatenaustauschUntis.vue");

export class RouteSchuleDatenaustauschUntis extends RouteTabNode<RouteDataSchuleDatenaustauschUntis, RouteApp> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
			BenutzerKompetenz.STUNDENPLAN_FUNKTIONSBEZOGEN_ANSEHEN,
		], "schule.datenaustausch.untis", "untis", SSchuleDatenaustauschUntis, new RouteDataSchuleDatenaustauschUntis());
		super.mode = ServerMode.STABLE;
		super.text = "Untis";
		super.menugroup = RouteSchuleMenuGroup.DATENAUSTAUSCH;
		super.children = [
			routeSchuleDatenaustauschUntisImporte,
			routeSchuleDatenaustauschUntisExporte,
		];
		super.defaultChild = routeSchuleDatenaustauschUntisImporte;
	}

}

export const routeSchuleDatenaustauschUntis = new RouteSchuleDatenaustauschUntis();
