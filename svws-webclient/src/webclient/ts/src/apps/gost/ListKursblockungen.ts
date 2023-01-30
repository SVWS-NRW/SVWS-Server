import { GostBlockungListeneintrag, GostHalbjahr } from "@svws-nrw/svws-core-ts";
import { reactive } from "vue";
import { App } from "../BaseApp";
import { BaseList } from "../BaseList";
import { ApiStatus, FeedbackValues } from "./userfeedback";

export class ListKursblockungen extends BaseList<GostBlockungListeneintrag> {

	protected _filter = undefined;

	protected _abiturjahr: number | undefined = undefined;
	protected _halbjahr: GostHalbjahr | undefined = undefined;

	public get abiturjahr() {
		return this._abiturjahr;
	}

	public get halbjahr() {
		return this._halbjahr;
	}

	public async update_list(abiturjahr: number, halbjahr: GostHalbjahr): Promise<void> {
		this._abiturjahr = abiturjahr;
		this._halbjahr = halbjahr;
		await super._update_list(async () => App.api.getGostAbiturjahrgangBlockungsliste(App.schema, abiturjahr, halbjahr.id));
	}

	/**
	 * Enthält eine Liste der Halbjahre für die entweder ein Api-Call läuft oder für
	 * die im letzten Api-Call ein Fehler aufgetreten ist.
	 */
	public apiStatus: ApiStatus = reactive({});

	/**
	 * Fügt einen Eintrag zur Apistatusliste hinzu.
	 * @param id Der Hashcode, unter dem ein neuer Eintrag hinzugefügt werden soll.
	 */
	public addIdToApiStatus(id: number) {
		this.apiStatus[id] = { error: false, idle: false };
	}

	/**
	 * Gibt den Eintrag für das Halbjahr mit der entsprechenden Id zurück.
	 * @param id Der Hashcode des gesuchten Halbjahres
	 * @returns FeedbackValues des ApiCalls
	 */
	public getApiStatus(id: number): FeedbackValues {
		return this.apiStatus[id];
	}

	/**
	 * Setzt die FeedbakValues des Halbjahres id für einen laufenden Api-Call.
	 * @param id Hashcode des Halbjahres, für das die FeedbackValues geändert werden sollen.
	 */
	public setApiStatusIdle(id: number) {
		Object.assign(this.apiStatus[id], { error: false, idle: true });
	}

	/**
	 * Setzt die FeedbakValues des Halbjahres id für einen fehlgeschlagenen Api-Call
	 * @param id
	 */
	public setApiStatusError(id: number) {
		Object.assign(this.apiStatus[id], { idle: false, error: true });
	}

	/**
	 * Entfernt den Eintrag des Halbjahres mit dem zur Id gehörigen Hashcode aus dem apiStatus.
	 * @param id Der Hashcode des Halbjahres
	 */
	public removeApiStatusId(id: number) {
		delete this.apiStatus[id];
	}

}
