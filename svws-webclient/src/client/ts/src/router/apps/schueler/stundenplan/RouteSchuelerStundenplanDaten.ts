import { BenutzerKompetenz, List, SchuelerStundenplanManager, Schulform, StundenplanListeEintrag, Vector } from "@svws-nrw/svws-core";
import { ref, Ref } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { SchuelerStundenplanAuswahlProps } from "~/components/schueler/stundenplan/SSchuelerStundenplanAuswahlProps";
import { SchuelerStundenplanDatenProps } from "~/components/schueler/stundenplan/SSchuelerStundenplanDatenProps";
import { api } from "~/router/Api";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { RouteSchuelerStundenplan } from "~/router/apps/schueler/RouteSchuelerStundenplan";
import { routeApp } from "~/router/RouteApp";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

export class RouteDataSchuelerStundenplan {

	auswahl: Ref<StundenplanListeEintrag | undefined> = ref(undefined);
	listAuswahl: Ref<List<StundenplanListeEintrag>> = ref(new Vector());
	manager: Ref<SchuelerStundenplanManager | undefined> = ref(undefined);

	public async onSelect(idSchueler: number, idStundenplan?: number) {
		if (((idStundenplan === undefined) && (this.manager.value === undefined)) ||
			((this.manager.value !== undefined) && (this.manager.value.getSchuelerID() === idSchueler) && (this.manager.value.getStundenplanID() === idStundenplan)))
			return;
		const item = this.getEintrag(idStundenplan);
		this.auswahl.value = item;
		if (item === undefined) {
			this.manager.value = undefined;
		} else {
			const daten = await api.server.getSchuelerStundenplan(api.schema, item.id, idSchueler);
			this.manager.value = new SchuelerStundenplanManager(daten);
		}
	}

	public getEintrag(id?: number) : StundenplanListeEintrag | undefined {
		if ((id === undefined) || (this.listAuswahl.value === undefined) || (this.listAuswahl.value?.size() === 0))
			return undefined;
		for (const e of this.listAuswahl.value)
			if (e.id === id)
				return e;
		return undefined;
	}

	setStundenplan = async (value: StundenplanListeEintrag | undefined) => {
		await RouteManager.doRoute({ name: routeSchuelerStundenplanDaten.name, params: { id: routeSchueler.data.stammdaten.id, idStundenplan: value?.id } });
	}

}

const SSchuelerStundenplanDaten = () => import("~/components/schueler/stundenplan/SSchuelerStundenplanDaten.vue");
const SSchuelerStundenplanAuswahl = () => import("~/components/schueler/stundenplan/SSchuelerStundenplanAuswahl.vue")

export class RouteSchuelerStundenplanDaten extends RouteNode<RouteDataSchuelerStundenplan, RouteSchuelerStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.stundenplan.daten", ":idStundenplan(\\d+)?", SSchuelerStundenplanDaten, new RouteDataSchuelerStundenplan());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.setView("stundenplanauswahl", SSchuelerStundenplanAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return (to_params.id !== undefined);
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.id === undefined)
			return routeSchueler.getRoute(undefined);
		const abschnitt = api.abschnitt.id;
		if (abschnitt === undefined)
			return false;
		this.data.listAuswahl.value = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, abschnitt);
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.id === undefined)
			return routeSchueler.getRoute(undefined);
		const idSchueler = parseInt(to_params.id as string);
		if (to_params.idStundenplan === undefined) {
			if ((this.data.listAuswahl.value === undefined) || (this.data.listAuswahl.value.size() === 0))
				return false;
			return { name: this.name, params: { id: to_params.id, idStundenplan: this.data.listAuswahl.value.get(0)?.id }};
		}
		const idStundenplan = parseInt(to_params.idStundenplan as string);
		await this.data.onSelect(idSchueler, idStundenplan);
	}

	public getRoute(id: number, idStundenplan: number) : RouteLocationRaw {
		return { name: this.name, params: { id, idStundenplan }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuelerStundenplanAuswahlProps {
		return {
			stundenplan: this.data.auswahl.value,
			stundenplaene: this.data.listAuswahl.value,
			setStundenplan: this.data.setStundenplan
		};
	}

	public getProps(to: RouteLocationNormalized): SchuelerStundenplanDatenProps {
		return {
			manager: this.data.manager.value
		};
	}

}

export const routeSchuelerStundenplanDaten = new RouteSchuelerStundenplanDaten();

