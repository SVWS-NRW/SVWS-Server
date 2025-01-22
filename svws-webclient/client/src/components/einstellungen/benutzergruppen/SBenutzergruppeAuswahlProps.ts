import type { BenutzergruppeListeEintrag} from "@core";
import type { AbschnittAuswahlDaten } from "@ui";

export interface BenutzergruppeAuswahlProps {
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	auswahl: () => BenutzergruppeListeEintrag |undefined;
	mapBenutzergruppe: Map<number, BenutzergruppeListeEintrag>;
	gotoBenutzergruppe: (benutzer: BenutzergruppeListeEintrag) => Promise<void>;
	createBenutzergruppe : (bezeichnung: string, istAdmin: boolean) => Promise<void>;
	deleteBenutzergruppen : (selectedItems: BenutzergruppeListeEintrag[]) => Promise<void>;
}