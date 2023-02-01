import { App } from "../BaseApp";

import { SchuleStammdaten, Schulform, Schulgliederung, Schuljahresabschnitt, UnsupportedOperationException } from "@svws-nrw/svws-core-ts";
import { BaseData } from "../BaseData";
import { computed, WritableComputedRef } from "vue";

export class DataSchuleStammdaten extends BaseData<SchuleStammdaten, unknown> {

	public schulform: WritableComputedRef<Schulform | undefined> = computed({
		get: () => this.daten === undefined ? undefined : (Schulform.getByKuerzel(this.daten.schulform) || undefined),
		set: (value) => { throw new UnsupportedOperationException("TODO implement DataSchuleStammdaten: set schulform"); }
	});

	public schulgliederungen: WritableComputedRef<Schulgliederung[] | undefined> = computed({
		get: () => {
			const sf = this.schulform.value;
			return sf === undefined ? [] : Schulgliederung.get(sf).toArray() as Schulgliederung[] || [];
		},
		set: (value) => {}
	});

	protected on_update(daten: Partial<SchuleStammdaten>) {
		return void daten;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<SchuleStammdaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<SchuleStammdaten | undefined> {
		const res = await super._select(() => App.api.getSchuleStammdaten(App.schema));
		if (App.akt_abschnitt === undefined) {
			const id = this.daten?.idSchuljahresabschnitt;
			const a = this.daten?.abschnitte
				.toArray(new Array<Schuljahresabschnitt>())
				.find(e => e.id === id);
			if (a) App.akt_abschnitt = a;
		}
		return res
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<SchuleStammdaten>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public async patch(data: Partial<SchuleStammdaten>): Promise<boolean> {
		return this._patch(data, () =>
			App.api.patchSchuleStammdaten(data, App.schema)
		);
	}
}
