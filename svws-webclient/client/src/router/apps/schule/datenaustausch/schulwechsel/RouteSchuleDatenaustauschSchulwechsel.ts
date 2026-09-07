import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteDataSchuleDatenaustauschSchulwechsel } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteDataSchuleDatenaustauschSchulwechsel";
import { routeSchuleDatenaustauschSchulwechselAbgaenge } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchuleDatenaustauschSchulwechselAbgaenge";
import { routeSchuleDatenaustauschSchulwechselKonfiguration } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchuleDatenaustauschSchulwechselKonfiguration";
import { routeSchuleDatenaustauschSchulwechselZugaenge } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchuleDatenaustauschSchulwechselZugaenge";
import { RouteTabNode } from "~/router/RouteTabNode";

const SSchuleDatenaustauschSchulbewerbung = () => import("~/components/schule/datenaustausch/schulwechsel/SSchuleDatenaustauschSchulwechsel.vue");

export class RouteSchuleDatenaustauschSchulwechsel extends RouteTabNode<RouteDataSchuleDatenaustauschSchulwechsel, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE], "schule.datenaustausch.schulbewerbung",
			"schulbewerbung", SSchuleDatenaustauschSchulbewerbung, new RouteDataSchuleDatenaustauschSchulwechsel());
		super.mode = ServerMode.DEV;
		super.text = "Schulwechsel";
		super.menugroup = RouteSchuleMenuGroup.DATENAUSTAUSCH;
		super.children = [
			routeSchuleDatenaustauschSchulwechselAbgaenge,
			routeSchuleDatenaustauschSchulwechselZugaenge,
			routeSchuleDatenaustauschSchulwechselKonfiguration,
		];
		super.defaultChild = routeSchuleDatenaustauschSchulwechselAbgaenge;
	}
}

export const routeSchuleDatenaustauschSchulwechsel = new RouteSchuleDatenaustauschSchulwechsel();
