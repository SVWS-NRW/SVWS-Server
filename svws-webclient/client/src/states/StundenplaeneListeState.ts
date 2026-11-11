import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { useBenutzerState } from "@ui/states/BenutzerState";
import { StateManager } from "@ui/ui/StateManager";

import { api } from "~/router/Api";

interface StundenplaeneListeReactiveState {
	mapStundenplaene: Map<number, StundenplanListeEintrag>;
}

/**
 * Der Teil-State für die Stundenpläne eines Schuljahresabschnittes, welcher von Auswahl-States genutzt wird.
 */
export class StundenplaeneListeState extends StateManager<StundenplaeneListeReactiveState> {

	public constructor() {
		super({ mapStundenplaene: new Map() });
	}

	/**
	 * Gibt eine Map mit den Stundenplänen des Schuljahresabschnittes zugeordnet zu deren ID zurück.
	 *
	 * @returns die Map mit den Stundenplänen
	 */
	public get mapStundenplaene(): Map<number, StundenplanListeEintrag> {
		return this._state.value.mapStundenplaene;
	}

	/**
	 * Aktualisiert die Map für die Stundenpläne über eine Abfrage bei der API. Besitzt der Benutzer nicht
	 * die Kompetenz zum Ansehen der Stundenpläne, so bleibt die Map leer.
	 *
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnittes
	 */
	public async update(idSchuljahresabschnitt: number): Promise<void> {
		const mapStundenplaene = new Map<number, StundenplanListeEintrag>();
		if (useBenutzerState().benutzerHatKompetenz(BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN)) {
			const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, idSchuljahresabschnitt);
			for (const l of listStundenplaene) {
				mapStundenplaene.set(l.id, l);
			}
		}
		this.setPatchedState({ mapStundenplaene });
	}

}
