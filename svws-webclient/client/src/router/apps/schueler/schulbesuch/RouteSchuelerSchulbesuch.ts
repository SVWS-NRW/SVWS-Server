import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteDataSchuelerSchulbesuch } from "~/router/apps/schueler/schulbesuch/RouteDataSchuelerSchulbesuch";
import type { SchuelerSchulbesuchProps } from "~/components/schueler/schulbesuch/SchuelerSchulbesuchProps";
import { RouteManager } from "~/router/RouteManager";
import { routeSchulen } from "~/router/apps/schule/kataloge/schulen/RouteSchulen";
import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

const SchuelerSchulbesuch = () => import("~/components/schueler/schulbesuch/SchuelerSchulbesuch.vue");

export class RouteSchuelerSchulbesuch extends RouteNode<RouteDataSchuelerSchulbesuch, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN], "schueler.schulbesuch", "schulbesuch", SchuelerSchulbesuch, new RouteDataSchuelerSchulbesuch());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulbesuch";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id !== undefined) {
				await this.data.ladeDaten();
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	goToSchule = async (idSchule: number) => {
		await RouteManager.doRoute(routeSchulen.getRoute({ id: idSchule }));
	};

	public getProps(to: RouteLocationNormalized): SchuelerSchulbesuchProps {
		return {
			manager: () => this.data.manager,
			autofocus: routeSchueler.data.autofocus,
			patch: routeSchuelerSchulbesuch.data.patch,
			goToSchule: this.goToSchule,
			addBisherigeSchule: this.data.addBisherigeSchule,
			patchBisherigeSchule: this.data.patchBisherigeSchule,
			deleteBisherigeSchulen: this.data.deleteBisherigeSchulen,
			addMerkmal: this.data.addMerkmal,
			patchMerkmal: this.data.patchMerkmal,
			deleteMerkmale: this.data.deleteMerkmale,
		};
	}
}

export const routeSchuelerSchulbesuch = new RouteSchuelerSchulbesuch();
