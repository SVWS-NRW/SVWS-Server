import type { RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

import type { SchulwechselAbgaengeProps } from "~/components/schule/datenaustausch/schulwechsel/SchulwechselAbgaengeProps";
import { RouteDataSchulwechselAbgaenge } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteDataSchulwechselAbgaenge";
import type { RouteSchulwechsel } from "~/router/apps/schule/datenaustausch/schulwechsel/RouteSchulwechsel";
import { RouteNode } from "~/router/RouteNode";

const schulwechselAbgaenge = () => import("~/components/schule/datenaustausch/schulwechsel/SchulwechselAbgaenge.vue");

export class RouteSchulwechselAbgaenge extends RouteNode<RouteDataSchulwechselAbgaenge, RouteSchulwechsel> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE], "schule.datenaustausch.schulwechsel.abgaenge", "abgaenge", schulwechselAbgaenge, new RouteDataSchulwechselAbgaenge());
		super.mode = ServerMode.DEV;
		super.propHandler = () => this.getProps();
		super.text = "Abgänge";
	}

	public getProps(): SchulwechselAbgaengeProps {
		return {
			abgaenge: () => this.data.abgaenge,
			abgaengeAuswahl: () => this.data.abgaengeAuswahl,
		};
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await this.data.ladeDaten();
		}
	}
}

export const routeSchulwechselAbgaenge = new RouteSchulwechselAbgaenge();
