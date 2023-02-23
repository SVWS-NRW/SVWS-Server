import { GostJahrgang, GostStatistikFachwahl, List, Schulform, Vector } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
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
		super("gost.fachwahlen", "fachwahlen", SGostFachwahlen, new RouteDataGostFachwahlen());
		super.setSchulformenErlaubt(Schulform.getMitGymOb());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(routeGost.data.item.value);
		}
	}

	public checkHidden(jahrgang: GostJahrgang | undefined) {
		return (jahrgang === undefined) || (jahrgang.abiturjahr === -1);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to.name === this.name) {
			if (to_params.abiturjahr === undefined)
				return false;
			const jahrgang = this.parent!.liste.liste.find(elem => elem.abiturjahr.toString() === to_params.abiturjahr);
			if (this.checkHidden(jahrgang))
				return { name: this.parent!.defaultChild!.name, params: { abiturjahr: this.parent!.liste.liste.at(0)?.abiturjahr }};
		}
		return true;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.abiturjahr === undefined) {
			await this.onSelect(undefined);
		} else {
			const tmp = parseInt(to_params.abiturjahr as string);
			await this.onSelect(this.parent!.liste.liste.find(s => s.abiturjahr === tmp));
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

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			jahrgang: routeGost.data.item.value,
			fachwahlen: this.data.fachwahlen
		};
	}

}

export const routeGostFachwahlen = new RouteGostFachwahlen();
