import { RouteTabNode } from "~/router/RouteTabNode";
import { RouteDataSchuleDatenaustauschSchulwechsel } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteDataSchuleDatenaustauschSchulwechsel";
import { routeSchuleDatenaustauschSchulwechselAbgaenge } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchuleDatenaustauschSchulwechselAbgaenge";
import { routeSchuleDatenaustauschSchulwechselZugaenge } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchuleDatenaustauschSchulwechselZugaenge";
import { routeSchuleDatenaustauschSchulwechselKonfiguration } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchuleDatenaustauschSchulwechselKonfiguration";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";

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
