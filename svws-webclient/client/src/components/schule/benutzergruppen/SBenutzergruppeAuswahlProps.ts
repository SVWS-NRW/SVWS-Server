import type { BenutzergruppeListeEintrag} from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface BenutzergruppeAuswahlProps {
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	auswahl: () => BenutzergruppeListeEintrag |undefined;
	mapBenutzergruppe: Map<number, BenutzergruppeListeEintrag>;
	gotoBenutzergruppe: (benutzer: BenutzergruppeListeEintrag) => Promise<void>;
	createBenutzergruppe : (bezeichnung: string, istAdmin: boolean) => Promise<void>;
	deleteBenutzergruppen : (selectedItems: BenutzergruppeListeEintrag[]) => Promise<void>;
	gotoSchule: () => Promise<void>;
}