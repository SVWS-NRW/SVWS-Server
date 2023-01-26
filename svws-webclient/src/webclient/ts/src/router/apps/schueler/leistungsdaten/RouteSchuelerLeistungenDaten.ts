import { FaecherListeEintrag, LehrerListeEintrag, SchuelerLernabschnittListeEintrag } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, useRoute, useRouter } from "vue-router";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { computed } from "vue";
import { DataSchuelerAbschnittsdaten } from "~/apps/schueler/DataSchuelerAbschnittsdaten";
import { ListAbschnitte } from "~/apps/schueler/ListAbschnitte";
import { ListFaecher } from "~/apps/kataloge/faecher/ListFaecher";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchuelerLeistungen } from "~/router/apps/schueler/RouteSchuelerLeistungen";

export class RouteDataSchuelerLeistungenDaten {
	auswahl: ListAbschnitte = new ListAbschnitte();
	item: SchuelerLernabschnittListeEintrag | undefined = undefined;
	daten: DataSchuelerAbschnittsdaten = new DataSchuelerAbschnittsdaten();
	listFaecher: ListFaecher = new ListFaecher();
	mapFaecher: Map<number, FaecherListeEintrag> = new Map();
	listLehrer: ListLehrer = new ListLehrer();
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();
}

const SSchuelerLeistungenDaten = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungenDaten.vue");
const SSchuelerLeistungenAuswahl = () => import("~/components/schueler/leistungsdaten/SSchuelerLeistungenAuswahl.vue")

export class RouteSchuelerLeistungenDaten extends RouteNodeListView<ListAbschnitte, SchuelerLernabschnittListeEintrag, RouteDataSchuelerLeistungenDaten, RouteSchuelerLeistungen> {

	public constructor() {
		super("schueler_leistungen_daten", ":idLernabschnitt(\\d+)?", SSchuelerLeistungenAuswahl, SSchuelerLeistungenDaten, new ListAbschnitte(), 'id', new RouteDataSchuelerLeistungenDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Leistungsdaten";
		super.setView("lernabschnittauswahl", SSchuelerLeistungenAuswahl, (route) => this.getProps(route));
		super.children = [
		];
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to_params.id === undefined)
			return false;
		const id = parseInt(to_params.id as string);
		if (to.name !== from?.name) {
			await this.data.listFaecher.update_list();
			this.data.mapFaecher.clear();
			this.data.listFaecher.liste.forEach(f => this.data.mapFaecher.set(f.id, f));
			await this.data.listLehrer.update_list();
			this.data.mapLehrer.clear();
			this.data.listLehrer.liste.forEach(l => this.data.mapLehrer.set(l.id, l));
		}
		if ((to.name !== from?.name) || (from_params.id === undefined) || (parseInt(from_params.id as string) != id))
			await this.data.auswahl.update_list(id);
		if ((to.name === this.name) && (to_params.idLernabschnitt === undefined))
			return { name: this.name, params: { id: to_params.id, idLernabschnitt: this.data.auswahl.liste.at(0)?.id }};
		return true;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.idLernabschnitt === undefined) {
			await this.onSelect(undefined);
		} else {
			const idLernabschnitt = parseInt(to_params.idLernabschnitt as string);
			await this.onSelect(this.data.auswahl.liste.find(s => s.id === idLernabschnitt));
		}
	}

	protected async onSelect(item?: SchuelerLernabschnittListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			await this.data.daten.unselect();
		} else {
			this.data.item = item;
			await this.data.daten.select(this.data.item);
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<SchuelerLernabschnittListeEintrag | undefined> {
		const router = useRouter();
		const route = useRoute();
		return computed({
			get: () => {
				if (route.params.id === undefined)
					return undefined;
				let tmp = this.data.auswahl.ausgewaehlt;
				if ((tmp === undefined) || (tmp.id === undefined) || (tmp.id.toString() !== route.params.idLernabschnitt))
					tmp = this.data.auswahl.liste.find(s => s.id.toString() === route.params.idLernabschnitt);
				return tmp;
			},
			set: (value) => {
				this.data.auswahl.ausgewaehlt = value;
				const from_name = route.name?.toString() || "";
				if ((from_name !== this.name) && from_name?.startsWith(this.name)) {  // TODO Ergänze Methode bei RouteNode isNested und nutze diese
					const params = {...route.params};
					params.idLernabschnitt = "" + value?.id;
					void router.push({ name: from_name, params: params });
				} else {
					void router.push({ name: this.name, params: { id: route.params.id, idLernabschnitt: value?.id } });
				}
			}
		});
	}

	public getRoute(id: number, idLernabschnitt: number) : RouteLocationRaw {
		return { name: this.name, params: { id, idLernabschnitt }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeSchueler.getProps(to),
			lernabschnitt: this.data.item,
			data: this.data.daten,
			mapFaecher: this.data.mapFaecher,
			mapLehrer: this.data.mapLehrer
		};
	}

}

export const routeSchuelerLeistungenDaten = new RouteSchuelerLeistungenDaten();

