import type { BenutzerListeEintrag} from "@svws-nrw/svws-core";
import { List } from "@svws-nrw/svws-core";
export interface BenutzerAuswahlProps {
    auswahl: () => BenutzerListeEintrag | undefined;
	mapBenutzer: Map<number, BenutzerListeEintrag>;
	gotoBenutzer: (benutzer: BenutzerListeEintrag) => Promise<void>;
	createBenutzerAllgemein : (anmeldename: string, benutzername: string, passwort: string) => Promise<void>;
	deleteBenutzerAllgemein : (selectedItems: BenutzerListeEintrag[]) => Promise<void>;
}