import { JahrgangsListeEintrag, KlassenListeEintrag, KursListeEintrag, SchuelerListeEintrag, Schulform, Vector } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
import { RouteNodeListView } from "../RouteNodeListView";
import { routeSchuelerAbschnitt } from "~/router/apps/schueler/RouteSchuelerAbschnitt";
import { routeSchuelerAdressen } from "~/router/apps/schueler/RouteSchuelerAdressen";
import { routeSchuelerErziehungsberechtigte } from "~/router/apps/schueler/RouteSchuelerErziehungsberechtigte";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/RouteSchuelerIndividualdaten";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/RouteSchuelerLaufbahnplanung";
import { routeSchuelerLeistungen } from "~/router/apps/schueler/RouteSchuelerLeistungen";
import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/RouteSchuelerSchulbesuch";
import { routeSchuelerStundenplan } from "~/router/apps/schueler/RouteSchuelerStundenplan";
import { ListSchueler } from "~/apps/schueler/ListSchueler";
import { RouteNode } from "~/router/RouteNode";
import { ListKlassen } from "~/apps/klassen/ListKlassen";
import { ListJahrgaenge } from "~/apps/kataloge/jahrgaenge/ListJahrgaenge";
import { ListKurse } from "~/apps/kurse/ListKurse";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { ListGost } from "~/apps/gost/ListGost";

export class RouteDataSchueler {
	stammdaten: DataSchuelerStammdaten = new DataSchuelerStammdaten();
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();
	listKlassen: ListKlassen = new ListKlassen();
	mapKlassen: Map<Number, KlassenListeEintrag> = new Map();
	listJahrgaenge: ListJahrgaenge = new ListJahrgaenge();
	mapJahrgaenge: Map<Number, JahrgangsListeEintrag> = new Map();
	listKurse: ListKurse = new ListKurse();
	mapKurs: Map<Number, KursListeEintrag> = new Map();
	listeAbiturjahrgaenge: ListGost = new ListGost();
}

const SSchuelerAuswahl = () => import("~/components/schueler/SSchuelerAuswahl.vue")
const SSchuelerApp = () => import("~/components/schueler/SSchuelerApp.vue")

export class RouteSchueler extends RouteNodeListView<ListSchueler, SchuelerListeEintrag, RouteDataSchueler, RouteApp> {

	public constructor() {
		super("schueler", "/schueler/:id(\\d+)?", SSchuelerAuswahl, SSchuelerApp, new ListSchueler(), 'id', new RouteDataSchueler());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schüler";
		super.setView("liste", SSchuelerAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchuelerIndividualdaten,
			routeSchuelerErziehungsberechtigte,
			routeSchuelerAdressen,
			routeSchuelerSchulbesuch,
			routeSchuelerAbschnitt,
			routeSchuelerLeistungen,
			routeSchuelerLaufbahnplanung,
			routeSchuelerStundenplan
		];
		super.defaultChild = routeSchuelerIndividualdaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.id === undefined) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			await this.liste.update_list();
			let id = this.liste.gefiltert[0]?.id;
			// TODO Handhabung bei neuer Schule -> Schülerliste leer
			if (id === undefined)
				id = -1;
			return this.getRoute(id);
		}
		await this.data.schule.select(true);  // undefined würde das laden verhindern, daher true
		// TODO Code für Schulform mit Fehlerhandling in die zukünftige API-Klasse auslagern
		const schulform : Schulform | undefined = this.data.schule.schulform.value;
		if (schulform === undefined)
			throw new Error("Schulform unbekannt.");
		if (schulform?.daten.hatGymOb)
			await this.data.listeAbiturjahrgaenge.update_list();
		await this.data.listKlassen.update_list();
		this.data.mapKlassen.clear();
		this.data.listKlassen.liste.forEach(k => this.data.mapKlassen.set(k.id, k));
		await this.data.listJahrgaenge.update_list();
		this.data.mapJahrgaenge.clear();
		this.data.listJahrgaenge.liste.forEach(j => this.data.mapJahrgaenge.set(j.id, j));
		await this.data.listKurse.update_list();
		this.data.mapKurs.clear();
		this.data.listKurse.liste.forEach(k => this.data.mapKurs.set(k.id, k));
		await this.liste.update_list();  // Die Auswahlliste wird als letztes geladen
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.liste.liste.find(s => s.id === id));
		}
	}

	protected async onSelect(item?: SchuelerListeEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
			await this.data.stammdaten.unselect();
		} else {
			this.item = item;
			await this.data.stammdaten.select(this.item);
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<SchuelerListeEintrag | undefined> {
		return this.getSelector();
	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...super.getProps(to),
			listKlassen: this.data.listKlassen,
			mapKlassen: this.data.mapKlassen,
			listJahrgaenge: this.data.listJahrgaenge,
			mapJahrgaenge: this.data.mapJahrgaenge,
			listKurse: this.data.listKurse,
			mapKurs: this.data.mapKurs,
			schulgliederungen: this.data.schule.schulgliederungen,
			abschnitte: this.data.schule.daten?.abschnitte || new Vector(),
			aktAbschnitt: routeApp.data.aktAbschnitt,
			setAbschnitt: routeApp.data.setAbschnitt
		};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...super.getProps(to),
			stammdaten: this.data.stammdaten,
			mapKlassen: this.data.mapKlassen,
		};
	}

}

export const routeSchueler = new RouteSchueler();
