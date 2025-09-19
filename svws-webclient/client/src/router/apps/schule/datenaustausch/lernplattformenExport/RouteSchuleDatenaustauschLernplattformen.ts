import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteDataSchuleDatenaustauschLernplattformen } from "./RouteDataSchuleDatenaustauschLernplattformen";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuleDatenaustauschLernplattformenProps } from "~/components/schule/datenaustausch/lernplattformenExport/SSchuleDatenaustauschLernplattformenProps";
import { api } from "~/router/Api";

const SSchuleDatenaustauschLernplattformen = () => import("~/components/schule/datenaustausch/lernplattformenExport/SSchuleDatenaustauschLernplattformen.vue");

export class RouteSchuleDatenaustauschLernplattformen extends RouteNode<RouteDataSchuleDatenaustauschLernplattformen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.IMPORT_EXPORT_LERNPLATTFORM], "schule.datenaustausch.lernplattformen", "lernplattformen", SSchuleDatenaustauschLernplattformen, new RouteDataSchuleDatenaustauschLernplattformen());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lernplattformen Export";
		super.menugroup = RouteSchuleMenuGroup.DATENAUSTAUSCH;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.init();
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschLernplattformenProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			lernplattformen: this.data.lernplattformen,
			export: this.data.export,
		};
	}
}

export const routeSchuleDatenaustauschLernplattformen = new RouteSchuleDatenaustauschLernplattformen();
