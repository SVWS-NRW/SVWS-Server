import { GostJahrgang, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";
import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
import { DataSchuelerLaufbahnplanung } from "~/apps/schueler/DataSchuelerLaufbahnplanung";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler, routeSchueler } from "~/router/apps/RouteSchueler";
import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";

export class RouteDataSchuelerLaufbahnplanung {
	item: SchuelerListeEintrag | undefined = undefined;
	dataLaufbahn: DataSchuelerLaufbahnplanung = new DataSchuelerLaufbahnplanung();
	gostJahrgang: GostJahrgang = new GostJahrgang();
	dataJahrgang: DataGostJahrgang = new DataGostJahrgang();
	dataFaecher: DataGostFaecher = new DataGostFaecher();
	dataFachkombinationen: DataGostFachkombinationen = new DataGostFachkombinationen();
}

const SSchuelerLaufbahnplanung = () => import("~/components/schueler/laufbahnplanung/SSchuelerLaufbahnplanung.vue");

export class RouteSchuelerLaufbahnplanung extends RouteNode<RouteDataSchuelerLaufbahnplanung, RouteSchueler> {

	public constructor() {
		super("schueler_laufbahnplanung", "laufbahnplanung", SSchuelerLaufbahnplanung, new RouteDataSchuelerLaufbahnplanung());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Laufbahnplanung";
		super.isHidden = (params?: RouteParams) => {
			if (routeSchueler.item === undefined)
				return false;
			const abiturjahr = routeSchueler.item?.abiturjahrgang;
			const jahrgang = routeSchueler.data.listeAbiturjahrgaenge.liste.find(j => (j.abiturjahr === abiturjahr));
			return (jahrgang === undefined);
		}
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any>  {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const tmp = parseInt(to_params.id as string);
			const success = await this.onSelect(this.parent!.liste.liste.find(s => s.id === tmp));
			if (!success)
				return routeSchueler.getRoute(tmp);
		}
	}

	protected async onSelect(item?: SchuelerListeEintrag) : Promise<boolean> {
		if (item === this.data.item)
			return true;
		if (item === undefined) {
			this.data.item = undefined;
			await this.data.dataLaufbahn.unselect();
			this.data.dataLaufbahn.dataGostFaecher = undefined;
			this.data.dataLaufbahn.dataGostJahrgang = undefined;
			this.data.dataLaufbahn.dataSchule = undefined;
			await this.data.dataJahrgang.unselect();
			await this.data.dataFaecher.unselect();
			await this.data.dataFachkombinationen.unselect();
		} else {
			this.data.item = item;
			await this.data.dataLaufbahn.select(this.data.item);
			if (this.data.dataLaufbahn.daten === undefined)
				return false;
			if (this.data.item.abiturjahrgang !== null) {
				this.data.gostJahrgang.abiturjahr = this.data.item.abiturjahrgang.valueOf();
				this.data.gostJahrgang.jahrgang = this.data.item.jahrgang;
				await this.data.dataJahrgang.select(this.data.gostJahrgang);
				if (this.data.dataJahrgang.daten === undefined) {
					await this.data.dataLaufbahn.unselect();
					return false;
				}
				await this.data.dataFaecher.select(this.data.gostJahrgang);
				this.data.dataLaufbahn.dataGostFaecher = this.data.dataFaecher;
				this.data.dataLaufbahn.dataGostJahrgang = this.data.dataJahrgang;
				this.data.dataLaufbahn.dataSchule = routeSchueler.data.schule;
				await this.data.dataFachkombinationen.select(this.data.gostJahrgang);
			}
		}
		return true;
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeSchueler.getProps(to),
			dataLaufbahn: this.data.dataLaufbahn,
			dataFaecher: this.data.dataFaecher,
			dataFachkombinationen: this.data.dataFachkombinationen
		};
	}

}

export const routeSchuelerLaufbahnplanung = new RouteSchuelerLaufbahnplanung();

