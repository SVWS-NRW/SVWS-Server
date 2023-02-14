import { AbiturdatenManager, GostBelegpruefungsArt, GostFaecherManager, GostJahrgang, GostJahrgangFachkombination, GostSchuelerFachwahl, List, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";
import { DataSchuelerLaufbahnplanung } from "~/apps/schueler/DataSchuelerLaufbahnplanung";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler, routeSchueler } from "~/router/apps/RouteSchueler";
import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
import { routeLogin } from "~/router/RouteLogin";
import { shallowRef, ShallowRef } from "vue";

export class RouteDataSchuelerLaufbahnplanung {
	item: SchuelerListeEintrag | undefined = undefined;
	dataLaufbahn: DataSchuelerLaufbahnplanung = new DataSchuelerLaufbahnplanung();
	_faecherManager: ShallowRef<GostFaecherManager | undefined> = shallowRef(undefined);
	gostJahrgang: GostJahrgang = new GostJahrgang();
	dataJahrgang: DataGostJahrgang = new DataGostJahrgang();
	dataFachkombinationen: DataGostFachkombinationen = new DataGostFachkombinationen();

	get faechermanager(): GostFaecherManager {
		if (this._faecherManager.value === undefined)
			throw new Error("Unerwarteter Fehler: Fächer-Manager nicht initialisiert");
		return this._faecherManager.value;
	}

	set faecherManager(manager: GostFaecherManager | undefined) {
		this._faecherManager.value = manager;
	}

	get fachkombinationen(): List<GostJahrgangFachkombination> {
		const list = new Vector<GostJahrgangFachkombination>();
		if ((this.item === undefined) || (this.dataFachkombinationen.daten === undefined))
			return list;
		for (const regel of	this.dataFachkombinationen.daten)
			if (regel.abiturjahr === this.item.abiturjahrgang)
				list.add(regel)
		return list;
	}

	get abiturmanager(): AbiturdatenManager {
		if (this.dataLaufbahn.abimanager.value === undefined)
			throw new Error("Unerwarteter Fehler: Abiturdaten-Manager nicht initialisiert");
		return this.dataLaufbahn.abimanager.value;
	}

	setBelegpruefungsart = async (value: GostBelegpruefungsArt) => {
		this.dataLaufbahn.gostAktuelleBelegpruefungsart = value;
	}

	setWahl = async (fachID: number, wahl: GostSchuelerFachwahl) => {
		await this.dataLaufbahn.setWahl(fachID, wahl);
	}

	getPdfWahlbogen = async() => {
		if (this.item == undefined)
			throw Error("Keine Schülerauswahl zur Bestimmung des PDF-Wahlbogens vorhanden.");
		return await routeLogin.data.api.getGostSchuelerPDFWahlbogen(routeLogin.data.schema, this.item.id);
	}

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
			this.data.dataLaufbahn.gostFaecher = undefined;
			this.data.dataLaufbahn.dataGostJahrgang = undefined;
			this.data.dataLaufbahn.dataSchule = undefined;
			await this.data.dataJahrgang.unselect();
			this.data.faecherManager = undefined;
			await this.data.dataFachkombinationen.unselect();
		} else {
			this.data.item = item;
			await this.data.dataLaufbahn.select(this.data.item);
			if (this.data.dataLaufbahn.daten === undefined)
				return false;
			if (this.data.item.abiturjahrgang !== null) {
				this.data.gostJahrgang.abiturjahr = this.data.item.abiturjahrgang;
				this.data.gostJahrgang.jahrgang = this.data.item.jahrgang;
				await this.data.dataJahrgang.select(this.data.gostJahrgang);
				if (this.data.dataJahrgang.daten === undefined) {
					await this.data.dataLaufbahn.unselect();
					return false;
				}
				const listFaecher = await routeLogin.data.api.getGostAbiturjahrgangFaecher(routeLogin.data.schema, this.data.gostJahrgang.abiturjahr);
				this.data.faecherManager = new GostFaecherManager(listFaecher);
				this.data.dataLaufbahn.gostFaecher = listFaecher;
				this.data.dataLaufbahn.dataGostJahrgang = this.data.dataJahrgang;
				this.data.dataLaufbahn.dataSchule = routeSchueler.data.schule;
				await this.data.dataFachkombinationen.select(this.data.gostJahrgang);
				// TODO: Dies ist nur ein temporärer Workaround, um das Setzen des Abiturdaten-Managers und die Durchführung der Belegprüfung zu triggern...
				const tmp = this.data.dataLaufbahn.gostAktuelleBelegpruefungsart;
				this.data.dataLaufbahn.gostAktuelleBelegpruefungsart = tmp;
				// TODO Workaround Ende
			}
		}
		return true;
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			setWahl: this.data.setWahl,
			setBelegpruefungsart: this.data.setBelegpruefungsart,
			getPdfWahlbogen: this.data.getPdfWahlbogen,
			schueler: this.data.item,
			jahrgangsdaten: this.data.dataJahrgang.daten,
			belegpruefungsart: this.data.dataLaufbahn.gostAktuelleBelegpruefungsart,
			belegpruefungsergebnis: this.data.dataLaufbahn.gostBelegpruefungsErgebnis,
			abiturmanager: this.data.abiturmanager,
			faechermanager: this.data.faechermanager,
			fachkombinationen: this.data.fachkombinationen
		};
	}

}

export const routeSchuelerLaufbahnplanung = new RouteSchuelerLaufbahnplanung();

