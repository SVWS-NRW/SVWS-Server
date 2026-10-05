import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { RouteApp } from "../../../RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteDataSchulwechsel } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteDataSchulwechsel";
import { routeSchulwechselAbgaenge } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchulwechselAbgaenge";
import { routeSchulwechselZugaenge } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchulwechselZugaenge";
import { RouteTabNode } from "~/router/RouteTabNode";

const schulwechselApp = () => import("~/components/schule/datenaustausch/schulwechsel/SchulwechselApp.vue");

export class RouteSchulwechsel extends RouteTabNode<RouteDataSchulwechsel, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE], "schule.datenaustausch.schulwechsel",
			"schulwechsel", schulwechselApp, new RouteDataSchulwechsel());
		super.mode = ServerMode.DEV;
		super.text = "Schulwechsel";
		super.menugroup = RouteSchuleMenuGroup.DATENAUSTAUSCH;
		super.children = [
			routeSchulwechselAbgaenge,
			routeSchulwechselZugaenge,
		];
		super.defaultChild = routeSchulwechselAbgaenge;
	}
}

export const routeSchulwechsel = new RouteSchulwechsel();
