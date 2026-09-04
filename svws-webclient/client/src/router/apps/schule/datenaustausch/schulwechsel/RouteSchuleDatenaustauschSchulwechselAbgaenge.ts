import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized } from "vue-router";
import type { RouteSchuleDatenaustauschSchulwechsel } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchuleDatenaustauschSchulwechsel";
import type { SSchuleDatenaustauschSchulwechselAbgaengeProps } from "~/components/schule/datenaustausch/schulwechsel/SSchuleDatenaustauschSchulwechselAbgaengeProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const SSchuleDatenaustauschSchulwechselAbgaenge = () => import("~/components/schule/datenaustausch/schulwechsel/SSchuleDatenaustauschSchulwechselAbgaenge.vue");

export class RouteSchuleDatenaustauschSchulwechselAbgaenge extends RouteNode<any, RouteSchuleDatenaustauschSchulwechsel> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE], "schule.datenaustausch.schulwechsel.abgaenge", "abgaenge", SSchuleDatenaustauschSchulwechselAbgaenge);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Abgänge";
	}

	public getProps(to: RouteLocationNormalized): SSchuleDatenaustauschSchulwechselAbgaengeProps {
		return {
		};
	}
}

export const routeSchuleDatenaustauschSchulwechselAbgaenge = new RouteSchuleDatenaustauschSchulwechselAbgaenge();
