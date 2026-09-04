import type { BenutzerListeEintrag } from "@core/core/data/benutzer/BenutzerListeEintrag";

export interface BenutzerAuswahlProps {
	auswahl: () => BenutzerListeEintrag | undefined;
	mapBenutzer: Map<number, BenutzerListeEintrag>;
	gotoBenutzer: (benutzer: BenutzerListeEintrag) => Promise<void>;
	createBenutzerAllgemein: (anmeldename: string, benutzername: string, passwort: string) => Promise<void>;
	deleteBenutzerMenge: (selectedItems: BenutzerListeEintrag[]) => Promise<void>;
}