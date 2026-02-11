import { AuswahlManager } from '../../ui/AuswahlManager';
import type { List } from '../../../../core/src/java/util/List';
import type { Schuljahresabschnitt } from '../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import type { Schulform } from '../../../../core/src/asd/types/schule/Schulform';
import type { Comparator } from '../../../../core/src/java/util/Comparator';
import { Arrays } from '../../../../core/src/java/util/Arrays';
import type { JavaFunction } from '../../../../core/src/java/util/function/JavaFunction';
import { JavaLong } from '../../../../core/src/java/lang/JavaLong';
import { ENMServerConnection } from '../../../../core/src/core/data/enm/ENMServerConnection';
import { SimpleOperationResponse } from '../../../../core/src/core/data/SimpleOperationResponse';

/**
 * Ein Manager für die Auswahl-Liste der Klassenleitungen im Externen Notenmodul (ENM)
 */
export class WenomAuswahlListeManager extends AuswahlManager<number, ENMServerConnection, ENMServerConnection> {


	private static readonly _eintragToId: JavaFunction<ENMServerConnection, number> = { apply: (l: ENMServerConnection) => l.id };
	private readonly _mapAvailability = new Map<number, SimpleOperationResponse | null>();
	private readonly _mapSetupResponse = new Map<number, boolean | null>();

	/** Ein Default-Comparator für den Vergleich von Servereinträgen. */
	public static readonly comparator: Comparator<ENMServerConnection> = { compare: (a: ENMServerConnection, b: ENMServerConnection) => JavaLong.compare(a.id, b.id) };

	private readonly _konfigurationLokal: ENMServerConnection;

	public constructor(schuljahresabschnitt: number, schuljahresabschnittSchule: number, schuljahresabschnitte: List<Schuljahresabschnitt>, schulform: Schulform | null, connections: List<ENMServerConnection>) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, connections, WenomAuswahlListeManager.comparator,
			WenomAuswahlListeManager._eintragToId, WenomAuswahlListeManager._eintragToId, Arrays.asList());
		this._konfigurationLokal = new ENMServerConnection();
		this._konfigurationLokal.id = -1;
		this._konfigurationLokal.bezeichnung = "Lokales Notenmodul";
		for (const server of connections) {
			this._mapAvailability.set(server.id, null);
		}
	}

	public getConnectionResponse(id: number): SimpleOperationResponse {
		const res = this._mapAvailability.get(id);
		if ((res === undefined) || (res === null)) {
			return new SimpleOperationResponse();
		} else {
			return res;
		}
	}

	public getAuswahlConnectionResponse(): SimpleOperationResponse {
		const id = this.auswahl().id;
		return this.getConnectionResponse(id);
	}

	public setConnectionResponse(id: number, res: SimpleOperationResponse) {
		this._mapAvailability.set(id, res);
	}

	public setAuswahlConnectionResponse(res: SimpleOperationResponse) {
		const id = this.auswahl().id;
		if (id >= 0) {
			this.setConnectionResponse(id, res);
		}
	}

	public getAuswahlSetupResponse(): boolean | null {
		const id = this.auswahlID();
		if (id === null) {
			return null;
		}
		return this._mapSetupResponse.get(id) ?? null;
	}

	public setAuswahlSetupResponse(res: boolean | null) {
		const id = this.auswahlID();
		if ((id !== null) && (id >= 0)) {
			this._mapSetupResponse.set(id, res);
		}
	}

	/**
	 * Setzt die Daten. Dabei wird ggf. die Auswahl angepasst. Die vorherige Auswahl wird gespeichert.
	 *
	 * @param daten   die neuen Daten
	 *
	 * @throws DeveloperNotificationException   falls die Daten nicht in der Auswahlliste vorhanden ist
	 */
	public setDaten(daten: ENMServerConnection | null): void {
		if (daten !== null && daten.id === -1) {
			this._vorherigeAuswahl = this._daten;
			this._daten = daten;
			this._filtered = null;
		} else {
			super.setDaten(daten);
		}
	}

	public filtered(): List<ENMServerConnection> {
		const hasCache = this._filtered !== null;
		const filtered = super.filtered();
		if (hasCache) {
			return filtered;
		}
		filtered.addFirst(this._konfigurationLokal);
		return filtered;
	}

	public auswahl(): ENMServerConnection {
		if (this._daten?.id !== -1) {
			return super.auswahl();
		}
		return this._konfigurationLokal;
	}

	/**
	 * Gibt den Vorlagen-Stundenplan zurück.
	 *
	 * @return den Vorlagen-Stundenplan.
	 * @throws DeveloperNotificationException wenn kein Vorlagen-Stundenplan gesetzt ist.
	 */
	public getKonfigurationLokal(): ENMServerConnection {
		return this._konfigurationLokal;
	}

	/**
	 * Überprüft, ob es sich beim ausgewählten Stundenplan um die Vorlage handelt.
	 *
	 * @return <code>true</code> wenn es sich um die Vorlage handelt, ansonsten <code>false</code>
	 */
	public auswahlIsKonfigurationLokal(): boolean {
		return this.hasDaten() && (this.auswahl() === this._konfigurationLokal);
	}

	/* Vergleicht zwei Einträge anhand der spezifizierten Ordnung.
	 *
	 * @param a   der erste Eintrag
	 * @param b   der zweite Eintrag
	 *
	 * @return das Ergebnis des Vergleichs (-1 kleine, 0 gleich und 1 größer)
	 */
	protected compareAuswahl(a: ENMServerConnection, b: ENMServerConnection): number {
		return WenomAuswahlListeManager.comparator.compare(a, b);
	}

	protected checkFilter(eintrag: any): boolean {
		return true;
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.AuswahlManager'].includes(name);
	}

}
