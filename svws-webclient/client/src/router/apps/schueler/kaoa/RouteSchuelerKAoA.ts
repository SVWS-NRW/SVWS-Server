import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuelerKAoAProps } from "~/components/schueler/kaoa/SchuelerKaoaProps";
import { RouteDataSchuelerKAoA } from "~/router/apps/schueler/kaoa/RouteDataSchuelerKAoA";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeSchueler, type RouteSchueler } from "~/router/apps/schueler/RouteSchueler";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";

const SchuelerKaoa = () => import("~/components/schueler/kaoa/SchuelerKaoa.vue");

export class RouteSchuelerKAoA extends RouteNode<RouteDataSchuelerKAoA, RouteSchueler> {

	public constructor() {
		super(Schulform.values().filter(f => ![Schulform.G, Schulform.FW, Schulform.HI, Schulform.KS].includes(f)),
			[BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_KAOA_DATEN_AENDERN],
			"schueler.kaoa", "kaoa", SchuelerKaoa, new RouteDataSchuelerKAoA());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "KAoA";
		this.isHidden = (params?: RouteParams) => this.checkHidden(params);
	}

	protected checkHidden(params: RouteParams = {}) {
		try {
			const { id } = RouteNode.getIntParams(params, ["id"]);
			const auswahl = routeSchueler.data.manager.auswahl();
			const schuljahr = schuleStateImpl.schuljahr;
			if (!routeSchueler.data.manager.hasDaten()
					|| (auswahl.status === SchuelerStatus.EXTERN.daten(schuljahr)?.id)
					|| (auswahl.status === SchuelerStatus.EHEMALIGE.daten(schuljahr)?.id)
					|| !this.isJahrgangEligible(auswahl.jahrgang)) {
				return routeSchueler.getRouteDefaultChild({ id });
			}
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (id !== undefined) {
				await this.data.ladeDaten(routeSchueler.data.manager.liste.get(id));
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	private isJahrgangEligible(jahrgang: string): boolean {
		const jahrgangNumber = Number.parseInt(jahrgang, 10);
		if (!Number.isNaN(jahrgangNumber)) {
			return jahrgangNumber >= 8;
		}

		return true;
	}

	public getProps(_: RouteLocationNormalized): SchuelerKAoAProps {
		return {
			manager: () => this.data.manager,
			auswahl: () => this.data.auswahl,
			add: this.data.add,
			patch: this.data.patch,
			delete: this.data.delete,
		};
	}

}

export const routeSchuelerKAoA = new RouteSchuelerKAoA();

