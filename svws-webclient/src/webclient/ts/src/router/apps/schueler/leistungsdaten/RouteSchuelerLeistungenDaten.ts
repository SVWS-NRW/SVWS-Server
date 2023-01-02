import { FaecherListeEintrag, LehrerListeEintrag, SchuelerLernabschnittListeEintrag } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "@vue/reactivity";
import { RouteLocationNormalized, useRoute, useRouter } from "vue-router";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeSchueler } from "~/router/apps/RouteSchueler";
import { computed } from "vue";
import { DataSchuelerAbschnittsdaten } from "~/apps/schueler/DataSchuelerAbschnittsdaten";
import { ListAbschnitte } from "~/apps/schueler/ListAbschnitte";
import { ListFaecher } from "~/apps/faecher/ListFaecher";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";

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

export class RouteSchuelerLeistungenDaten extends RouteNodeListView<SchuelerLernabschnittListeEintrag, RouteDataSchuelerLeistungenDaten> {

	public constructor() {
		super("schueler_leistungen_daten", ":idLernabschnitt(\\d+)?", SSchuelerLeistungenAuswahl, SSchuelerLeistungenDaten, new RouteDataSchuelerLeistungenDaten());
		super.propHandler = (route) => this.getProps(route, true);
		super.text = "Leistungsdaten";
        super.setView("lernabschnittauswahl", SSchuelerLeistungenAuswahl, (route) => this.getProps(route, false));
		super.children = [
		];
	}

    /**
     * TODO see RouterManager - global hook
     * 
     * @param to    die Ziel-Route
     * @param from   die Quell-Route
     */
    public async beforeEach(to: RouteLocationNormalized, from: RouteLocationNormalized): Promise<any> {
		if (to.params.id === undefined)
			return false;
		const id = parseInt(to.params.id as string);
		const to_name = to.name?.toString();
		const from_name = from.name?.toString();
		if (to_name !== from_name) {
			await this.data.listFaecher.update_list();
			this.data.mapFaecher.clear();
			this.data.listFaecher.liste.forEach(f => this.data.mapFaecher.set(f.id, f));
			await this.data.listLehrer.update_list();
			this.data.mapLehrer.clear();
			this.data.listLehrer.liste.forEach(l => this.data.mapLehrer.set(l.id, l));
		}
		if ((to_name !== from_name) || (from.params.id === undefined) || (parseInt(from.params.id as string) != id))
			await this.data.auswahl.update_list(id);
		if ((to_name === this.name) && (to.params.idLernabschnitt === undefined))
			return { name: this.name, params: { id: to.params.id, idLernabschnitt: this.data.auswahl.liste.at(0)?.id }};
        return true;
    }

	protected getAuswahlComputedProperty(): WritableComputedRef<SchuelerLernabschnittListeEintrag | undefined> {
		const router = useRouter();
		const route = useRoute();
		const self = this;

		const selected = computed({
			get(): SchuelerLernabschnittListeEintrag | undefined {
				if (route.params.id === undefined)
					return undefined;
				let tmp = self.data.auswahl.ausgewaehlt;
				if ((tmp === undefined) || (tmp.id.toString() !== route.params.idLernabschnitt))
					tmp = self.data.auswahl.liste.find(s => s.id.toString() === route.params.idLernabschnitt);
				return tmp;
			},
			set(value: SchuelerLernabschnittListeEintrag | undefined) {
				self.data.auswahl.ausgewaehlt = value;
				const from_name = route.name?.toString() || "";
				if ((from_name !== self.name) && from_name?.startsWith(self.name)) {  // TODO Ergänze Methode bei RouteNode isNested und nutze diese 
					const params = {...route.params};
					params.idLernabschnitt = "" + value?.id;
					router.push({ name: from_name, params: params });
				} else {
					router.push({ name: self.name, params: { id: route.params.id, idLernabschnitt: value?.id } });
				}
			}
		});
		return selected;
	}

	protected onSelect(item?: SchuelerLernabschnittListeEintrag) {
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

	public getProps(to: RouteLocationNormalized, doSelect: boolean): Record<string, any> {
		let prop: Record<string, any> = routeSchueler.getProps(to);
		let idLernabschnitt: number | undefined = undefined;
		let itemLernabschnitt: SchuelerLernabschnittListeEintrag | undefined = undefined;
		if (to.params.idLernabschnitt !== undefined) {
			idLernabschnitt = parseInt(to.params.idLernabschnitt as string);
			itemLernabschnitt = this.data.auswahl.liste.find(s => s.id === idLernabschnitt);
		}
		if (doSelect)
			this.onSelect(itemLernabschnitt);
		prop.lernabschnitt = this.data.item;
		prop.data = this.data.daten;
		prop.mapFaecher = this.data.mapFaecher;
		prop.mapLehrer = this.data.mapLehrer;
		return prop;
	}

}

export const routeSchuelerLeistungenDaten = new RouteSchuelerLeistungenDaten();

