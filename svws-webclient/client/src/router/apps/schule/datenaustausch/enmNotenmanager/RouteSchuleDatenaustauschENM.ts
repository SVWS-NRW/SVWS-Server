import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import type { SchuleDatenaustauschENMProps } from "~/components/schule/datenaustausch/enmNotenmanager/SSchuleDatenaustauschENMProps";
import { RouteDataSchuleDatenaustauschENM } from "./RouteDataSchuleDatenaustauschENM";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";

const SSchuleDatenaustauschENM = () => import("~/components/schule/datenaustausch/enmNotenmanager/SSchuleDatenaustauschENM.vue");

export class RouteSchuleDatenaustauschENM extends RouteNode<RouteDataSchuleDatenaustauschENM, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.NOTENMODUL_ADMINISTRATION], "schule.datenaustausch.enm", "enm", SSchuleDatenaustauschENM, new RouteDataSchuleDatenaustauschENM());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "ENM Notenmanager";
		super.menugroup = RouteSchuleMenuGroup.DATENAUSTAUSCH;
	}
	protected async update(): Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeDaten();
	}

	public getProps(to: RouteLocationNormalized): SchuleDatenaustauschENMProps {
		return {
			listLehrer: this.data.listLehrer,
			exportLehrerENM: this.data.exportLehrerENM,
			exportGzipENM: this.data.exportGzipENM,
			importGzipENM: this.data.importGzipENM,
			importENM: this.data.importENM,
		};
	}
}

export const routeSchuleDatenaustauschENM = new RouteSchuleDatenaustauschENM();
