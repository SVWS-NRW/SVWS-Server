import { BenutzerKompetenz, GostJahrgang, GostStatistikFachwahl, List, Schulform, Vector } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { GostFachwahlenProps } from "~/components/gost/fachwahlen/SGostFachwahlenProps";
import { api } from "~/router/Api";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { RouteNode } from "~/router/RouteNode";

export class RouteDataGostFachwahlen  {
	item: GostJahrgang | undefined = undefined;
	fachwahlen: List<GostStatistikFachwahl> = new Vector<GostStatistikFachwahl>();
}

const SGostFachwahlen = () => import("~/components/gost/fachwahlen/SGostFachwahlen.vue");

export class RouteGostFachwahlen extends RouteNode<RouteDataGostFachwahlen, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.fachwahlen", "fachwahlen", SGostFachwahlen, new RouteDataGostFachwahlen());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(routeGost.data.auswahl);
		}
	}

	public checkHidden(jahrgang: GostJahrgang | undefined) {
		return (jahrgang === undefined) || (jahrgang.abiturjahr === -1);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to_params.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to.name === this.name) {
			if (to_params.abiturjahr === undefined)
				return false;
			if (this.parent === undefined)
				throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			const id = parseInt(to_params.abiturjahr);
			const jahrgang = this.parent.data.mapAbiturjahrgaenge.get(id)
			if (this.checkHidden(jahrgang))
				return { name: this.parent.defaultChild!.name, params: { abiturjahr: this.parent.data.mapAbiturjahrgaenge.values().next().value.abiturjahr }};
		}
		return true;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		if (to_params.abiturjahr === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.abiturjahr);
			await this.onSelect(this.parent.data.mapAbiturjahrgaenge.get(id));
		}
	}

	protected async onSelect(item?: GostJahrgang) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.fachwahlen = new Vector<GostStatistikFachwahl>();
		} else {
			this.data.item = item;
			this.data.fachwahlen = await api.server.getGostAbiturjahrgangFachwahlstatistik(api.schema, this.data.item.abiturjahr || -1);
		}
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenProps {
		return {
			jahrgang: routeGost.data.jahrgangsdaten,
			fachwahlen: this.data.fachwahlen
		};
	}

}

export const routeGostFachwahlen = new RouteGostFachwahlen();
