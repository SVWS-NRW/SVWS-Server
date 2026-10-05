import type { RouteLocationNormalized } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { SchulwechselZugaengeProps } from "~/components/schule/datenaustausch/schulwechsel/SchulwechselZugaengeProps";
import type { RouteSchulwechsel } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchulwechsel";
import { RouteNode } from "~/router/RouteNode";

const schulwechselZugaenge = () => import("~/components/schule/datenaustausch/schulwechsel/SchulwechselZugaenge.vue");

export class RouteSchulwechselZugaenge extends RouteNode<any, RouteSchulwechsel> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE,
		], "schule.datenaustausch.schulwechsel.zugaenge", "zugaenge", schulwechselZugaenge);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zugänge";
	}

	public getProps(to: RouteLocationNormalized): SchulwechselZugaengeProps {
		return {
			serverMode: ServerMode.DEV,
		};
	}
}

export const routeSchulwechselZugaenge = new RouteSchulwechselZugaenge();
