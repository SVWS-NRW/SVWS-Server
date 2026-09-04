import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteSchuleDatenaustauschSchulwechsel } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchuleDatenaustauschSchulwechsel";
import type { SSchuleDatenaustauschSchulwechselKonfigurationProps } from "~/components/schule/datenaustausch/schulwechsel/SSchuleDatenaustauschSchulwechselKonfigurationProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const SSchuleDatenaustauschSchulwechselKonfiguration = () => import("~/components/schule/datenaustausch/schulwechsel/SSchuleDatenaustauschSchulwechselKonfiguration.vue");

export class RouteSchuleDatenaustauschSchulwechselKonfiguration extends RouteNode<any, RouteSchuleDatenaustauschSchulwechsel> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE], "schule.datenaustausch.schulwechsel.konfiguration", "konfiguration", SSchuleDatenaustauschSchulwechselKonfiguration);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konfiguration";
	}

	public getProps(to: RouteLocationNormalized): SSchuleDatenaustauschSchulwechselKonfigurationProps {
		return {
			serverMode: ServerMode.DEV,
		};
	}
}

export const routeSchuleDatenaustauschSchulwechselKonfiguration = new RouteSchuleDatenaustauschSchulwechselKonfiguration();
