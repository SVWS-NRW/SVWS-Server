import type { BenutzerKompetenz, BetriebListeEintrag, BetriebStammdaten, KatalogEintrag, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
import type { AbschnittAuswahlDaten } from "@ui";

export interface BetriebeAuswahlProps{
	auswahl: BetriebListeEintrag | undefined;
	mapKatalogeintraege: Map<number, BetriebListeEintrag>;
	mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
	mapOrte: Map<number, OrtKatalogEintrag>;
	mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	addEintrag: (betrieb: BetriebStammdaten) => Promise<void>;
	deleteEintraege: (betrieb: BetriebListeEintrag[]) => Promise<void>;
	gotoEintrag:(eintrag: BetriebListeEintrag) => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	benutzerKompetenzen: Set<BenutzerKompetenz>;
}
