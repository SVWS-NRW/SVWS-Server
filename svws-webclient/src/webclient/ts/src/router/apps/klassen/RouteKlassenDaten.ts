import { JahrgangsListeEintrag, KlassenListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized } from "vue-router";
import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
import { DataKlasse } from "~/apps/klassen/DataKlasse";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { RouteNode } from "~/router/RouteNode";
import { routeKlassen } from "../RouteKlassen";

export class RouteDataKlassenDaten {
	item: KlassenListeEintrag | undefined = undefined;
	daten: DataKlasse = new DataKlasse();
	listLehrer: ListLehrer = new ListLehrer();
	mapLehrer: Map<Number, LehrerListeEintrag> = new Map();
	listJahrgaenge: ListJahrgaenge = new ListJahrgaenge();
	mapJahrgaenge: Map<Number, JahrgangsListeEintrag> = new Map();
}

const SKlassenDaten = () => import("~/components/klassen/daten/SKlassenDaten.vue");

export class RouteKlassenDaten extends RouteNode<RouteDataKlassenDaten> {

	public constructor() {
		super("klassen_daten", "daten", SKlassenDaten, new RouteDataKlassenDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

    public async beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized): Promise<any> {
		if (from.name?.toString() !== this.name) {
			await this.data.listLehrer.update_list();
			this.data.mapLehrer.clear();
			this.data.listLehrer.liste.forEach(l => this.data.mapLehrer.set(l.id, l));
			await this.data.listJahrgaenge.update_list();
			this.data.mapJahrgaenge.clear();
			this.data.listJahrgaenge.liste.forEach(j => this.data.mapJahrgaenge.set(j.id, j));
		}
        return true;
    }

	protected onSelect(item?: KlassenListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten.unselect();
			
		} else {
			this.data.item = item;
			this.data.daten.select(this.data.item);
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		let prop: Record<string, any> = routeKlassen.getProps(to);
		this.onSelect(prop.item);
		prop.data = this.data.daten;
		prop.listLehrer = this.data.listLehrer;
		prop.mapLehrer = this.data.mapLehrer;
		prop.listJahrgaenge = this.data.listJahrgaenge;
		prop.mapJahrgaenge = this.data.mapJahrgaenge;
		return prop;
	}

}

export const routeKlassenDaten = new RouteKlassenDaten();

