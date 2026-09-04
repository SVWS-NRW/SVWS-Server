import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeApp } from "~/router/apps/RouteApp";
import { type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerErziehungsberechtigte } from "~/router/apps/schueler/erziehungsberechtigte/RouteDataSchuelerErziehungsberechtigte";
import type { SchuelerErziehungsberechtigteProps } from "~/components/schueler/erziehungsberechtigte/SchuelerErziehungsberechtigteProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SchuelerErziehungsberechtigte = () => import("~/components/schueler/erziehungsberechtigte/SchuelerErziehungsberechtigte.vue");

export class RouteSchuelerErziehungsberechtigte extends RouteNode<RouteDataSchuelerErziehungsberechtigte, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN], "schueler.erziehungsberechtigte", "erziehungsberechtigte", SchuelerErziehungsberechtigte, new RouteDataSchuelerErziehungsberechtigte());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Erziehungsberechtigte";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		try {
			if (isEntering) {
				await this.data.ladeListe();
			}
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			await this.data.setEintrag(id);
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): SchuelerErziehungsberechtigteProps {
		return {
			patchErzieher: this.data.patchErzieher,
			patchErzieherAnPosition: this.data.patchErzieherAnPosition,
			addErzieher: this.data.addErzieher,
			deleteErzieher: this.data.deleteErzieher,
			data: () => this.data.daten,
			erzieherartenById: routeApp.cache.kataloge.erzieherartenById,
		};
	}

}

export const routeSchuelerErziehungsberechtigte = new RouteSchuelerErziehungsberechtigte();

