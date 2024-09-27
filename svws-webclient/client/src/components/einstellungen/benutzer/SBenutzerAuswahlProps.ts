import type { BenutzerListeEintrag} from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface BenutzerAuswahlProps {
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	auswahl: () => BenutzerListeEintrag | undefined;
	mapBenutzer: Map<number, BenutzerListeEintrag>;
	gotoBenutzer: (benutzer: BenutzerListeEintrag) => Promise<void>;
	createBenutzerAllgemein : (anmeldename: string, benutzername: string, passwort: string) => Promise<void>;
	deleteBenutzerMenge : (selectedItems: BenutzerListeEintrag[]) => Promise<void>;
	gotoEinstellungen: () => Promise<void>;
}