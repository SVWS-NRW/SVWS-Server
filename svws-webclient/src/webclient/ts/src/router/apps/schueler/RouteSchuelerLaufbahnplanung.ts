import { GostJahrgang, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized } from "vue-router";
import { App } from "~/apps/BaseApp";
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
		super.isHidden = () => {
			const abiturjahr = routeSchueler.data.item.value?.abiturjahrgang;
			const jahrgang = App.apps.gost.auswahl.liste.find(j => (j.abiturjahr === abiturjahr));   // TODO Bestimme Jahrgang aus der Liste der Abiturjahgänge in RouteSchueler
			return (jahrgang === undefined);
		}
	}

	protected onSelect(item?: SchuelerListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.dataLaufbahn.unselect();
			this.data.dataLaufbahn.dataGostFaecher = undefined;
			this.data.dataLaufbahn.dataGostJahrgang = undefined;
			this.data.dataLaufbahn.dataSchule = undefined;
			this.data.dataJahrgang.unselect();
			this.data.dataFaecher.unselect();
			this.data.dataFachkombinationen.unselect();
		} else {
			this.data.item = item;
			this.data.dataLaufbahn.select(this.data.item);
			if (this.data.item.abiturjahrgang !== null) {
				this.data.gostJahrgang.abiturjahr = this.data.item.abiturjahrgang.valueOf();
				this.data.gostJahrgang.jahrgang = this.data.item.jahrgang;
				this.data.dataJahrgang.select(this.data.gostJahrgang);
				this.data.dataFaecher.select(this.data.gostJahrgang);
				this.data.dataLaufbahn.dataGostFaecher = this.data.dataFaecher;
				this.data.dataLaufbahn.dataGostJahrgang = this.data.dataJahrgang;
				this.data.dataLaufbahn.dataSchule = routeSchueler.data.schule;
				this.data.dataFachkombinationen.select(this.data.gostJahrgang);
			}
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		let prop: Record<string, any> = routeSchueler.getProps(to);
		this.onSelect(prop.item.value);
		prop.dataLaufbahn = this.data.dataLaufbahn;
		prop.dataFaecher = this.data.dataFaecher;
		prop.dataFachkombinationen = this.data.dataFachkombinationen;
		return prop;
	}

}

export const routeSchuelerLaufbahnplanung = new RouteSchuelerLaufbahnplanung();

