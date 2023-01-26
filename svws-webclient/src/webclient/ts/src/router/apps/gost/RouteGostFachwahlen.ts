import { RouteNode } from "~/router/RouteNode";
import { RouteGost, routeGost } from "~/router/apps/RouteGost";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { GostJahrgang, GostStatistikFachwahl, List, Vector } from "@svws-nrw/svws-core-ts";
import { App } from "~/apps/BaseApp";

export class RouteDataGostKursplanung  {
	item: GostJahrgang | undefined = undefined;
	fachwahlen: List<GostStatistikFachwahl> = new Vector<GostStatistikFachwahl>();
}

const SGostFachwahlen = () => import("~/components/gost/fachwahlen/SGostFachwahlen.vue");

export class RouteGostFachwahlen extends RouteNode<RouteDataGostKursplanung, RouteGost> {

	public constructor() {
		super("gost_fachwahlen", "fachwahlen", SGostFachwahlen, new RouteDataGostKursplanung());
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
			this.data.fachwahlen = await App.api.getGostAbiturjahrgangFachwahlstatistik(App.schema, this.data.item.abiturjahr?.valueOf() || -1);
		}
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeGost.getProps(to),
			fachwahlen: this.data.fachwahlen
		};
	}

}

export const routeGostFachwahlen = new RouteGostFachwahlen();
