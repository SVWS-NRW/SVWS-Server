import { RouteNode } from "~/router/RouteNode";
import type { RouteSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteSchuleStammdaten";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuleLogoverwaltungProps } from "~/components/schule/stammdaten/logoverwaltung/SchuleLogoverwaltungProps";
import { RouteDataSchuleLogoverwaltung } from "~/router/apps/schule/stammdaten/logoverwaltung/RouteDataSchuleLogoverwaltung";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SchuleLogoverwaltung = () => import("~/components/schule/stammdaten/logoverwaltung/SchuleLogoverwaltung.vue");

export class RouteSchuleLogoverwaltung extends RouteNode<RouteDataSchuleLogoverwaltung, RouteSchuleStammdaten> {

	public constructor() {
		super(Schulform.values(),
			[BenutzerKompetenz.KEINE],
			"schule.stammdaten.logoverwaltung",
			"logoverwaltung",
			SchuleLogoverwaltung,
			new RouteDataSchuleLogoverwaltung()
		);
		super.mode = ServerMode.DEV;
		super.text = "Logoverwaltung";
		super.propHandler = (route) => this.getProps(route);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await this.data.ladeDaten();
		}
	}

	public getProps(to: RouteLocationNormalized): SchuleLogoverwaltungProps {
		return {
			logos: () => this.data.logos,
			addLogo: this.data.addLogo,
			patchLogo: this.data.patchLogo,
			deleteLogo: this.data.deleteLogo,
			zipLogos: this.data.zipLogos,
		};
	}

}

export const routeSchuleLogoverwaltung = new RouteSchuleLogoverwaltung();
