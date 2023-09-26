import type { BenutzerListeEintrag} from "@core";

export interface BenutzerAuswahlProps {
  auswahl: () => BenutzerListeEintrag | undefined;
	mapBenutzer: Map<number, BenutzerListeEintrag>;
	gotoBenutzer: (benutzer: BenutzerListeEintrag) => Promise<void>;
	createBenutzerAllgemein : (anmeldename: string, benutzername: string, passwort: string) => Promise<void>;
	deleteBenutzerAllgemein : (selectedItems: BenutzerListeEintrag[]) => Promise<void>;
	gotoSchule: () => Promise<void>;
}