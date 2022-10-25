import {
	JahrgangsListeEintrag,
	KlassenListeEintrag,
	KursListeEintrag,
	SchuelerListeEintrag,
	SchuelerStatus,
	Schulgliederung
} from "@svws-nrw/svws-core-ts";
import { reactive } from "vue";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";
import { ListAbschnitte } from "./ListAbschnitte";
import { ListStundenplaene } from "./ListStundenplaene";

/**
 * Das Interface für die Schülerfilterfunktion. Alle hier gesetzten Werte
 * stimmen sich aufeinander ab und zeigen nur noch kombinierte Ergebnisse
 */
interface Filter {
	jahrgang: JahrgangsListeEintrag | undefined;
	kurs: KursListeEintrag | undefined;
	klasse: KlassenListeEintrag | undefined;
	schulgliederung: Schulgliederung | undefined;
	status: Array<SchuelerStatus>;
}
export class ListSchueler extends BaseList<SchuelerListeEintrag, Filter> {
	protected _filter = reactive({
		jahrgang: undefined,
		kurs: undefined,
		klasse: undefined,
		schulgliederung: undefined,
		status: [SchuelerStatus.AKTIV]
	}) as Filter;

	protected listAbschnitte: ListAbschnitte;
	protected listStundenplaene: ListStundenplaene;

	public constructor(listAbschnitte: ListAbschnitte, listStundenplaene: ListStundenplaene) {
		super();
		this.listAbschnitte = listAbschnitte;
		this.listStundenplaene = listStundenplaene;
	}
	/**
	 * Aktualisiert die Liste für die Schülerauswahl
	 *
	 * @returns {Promise<void>}
	 */
	public async update_list(): Promise<void> {
		await super._update_list(() =>
			App.api.getSchuelerFuerAbschnitt(App.schema, App.akt_abschnitt.id)
		);
		if (!this.ausgewaehlt) this.ausgewaehlt = this.gefiltert[0]
	}

	/**
	 * Diese Methode filtert die Daten der Auswahlliste und wird von dem
	 * AppState-Objekt dieser Klasse beim Laden des Katalogs automatisch aufgerufen.
	 *
	 * @returns Void
	 */
	protected filter_liste(): void {
		this._state.gefiltert = this.liste
			.filter(
				s =>
					!this.filter.status.length ||
					this.filter.status
						.map(s => s.bezeichnung)
						.includes(s.status)
			)
			.filter(
				s =>
					!this.filter.jahrgang ||
					s.jahrgang === this.filter.jahrgang.kuerzel
			)
			.filter(
				s =>
					!this.filter.klasse ||
					s.jahrgang === this.filter.klasse.kuerzel
			)
			.filter(
				s =>
					!this.filter.kurs ||
					s.kurse
						?.toArray(new Array<number>())
						.includes(this.filter.kurs.id)
			)
			.filter(
				s =>
					!this.filter.schulgliederung ||
					s.schulgliederung ===
						this.filter.schulgliederung.daten.kuerzel
			);
			if (this.ausgewaehlt && !this.gefiltert.includes(this.ausgewaehlt)) this.ausgewaehlt = this.gefiltert[0]
	}

	/**
	 * Aktualisiert die Liste der Blockungsergebnisse
	 *
	 * @returns {Promise<void>}
	 */
	public async on_select(): Promise<void> {
		if (!this._state.ausgewaehlt?.id) return;
		await this.listAbschnitte.update_list(this._state.ausgewaehlt.id);
		await this.listStundenplaene.update_list();
		if (this.listStundenplaene.liste) this.listStundenplaene.ausgewaehlt = this.listStundenplaene.liste[0];
	}
}
