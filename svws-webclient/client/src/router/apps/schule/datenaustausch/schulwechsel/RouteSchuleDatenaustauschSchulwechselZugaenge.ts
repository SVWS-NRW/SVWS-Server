import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteSchuleDatenaustauschSchulwechsel } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchuleDatenaustauschSchulwechsel";
import type { SSchuleDatenaustauschSchulwechselZugaengeProps } from "~/components/schule/datenaustausch/schulwechsel/SSchuleDatenaustauschSchulwechselZugaengeProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const SSchuleDatenaustauschSchulwechselZugaenge = () => import("~/components/schule/datenaustausch/schulwechsel/SSchuleDatenaustauschSchulwechselZugaenge.vue");

export class RouteSchuleDatenaustauschSchulwechselZugaenge extends RouteNode<any, RouteSchuleDatenaustauschSchulwechsel> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE,
		], "schule.datenaustausch.schulwechsel.zugaenge", "zugaenge", SSchuleDatenaustauschSchulwechselZugaenge);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zugänge";
	}

	public getProps(to: RouteLocationNormalized): SSchuleDatenaustauschSchulwechselZugaengeProps {
		return {
			serverMode: ServerMode.DEV,
		};
	}
}

export const routeSchuleDatenaustauschSchulwechselZugaenge = new RouteSchuleDatenaustauschSchulwechselZugaenge();
