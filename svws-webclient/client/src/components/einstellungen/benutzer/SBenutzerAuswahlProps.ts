import type { BenutzerListeEintrag } from "@core/core/data/benutzer/BenutzerListeEintrag";
import type { LehrerCredentialsOhneBenutzerEintrag } from "@core/core/data/benutzer/LehrerCredentialsOhneBenutzerEintrag";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { List } from "@core/java/util/List";

export interface BenutzerAuswahlProps {
	auswahl: () => BenutzerListeEintrag | undefined;
	mapBenutzer: Map<number, BenutzerListeEintrag>;
	gotoBenutzer: (benutzer: BenutzerListeEintrag) => Promise<void>;
	createBenutzerAllgemein: (anmeldename: string, benutzername: string, passwort: string) => Promise<void>;
	createBenutzerLehrer: (idLehrer: number, benutzername: string, passwort: string) => Promise<void>;
	listLehrer: List<LehrerListeEintrag>;
	mapLehrerCredentialsOhneBenutzer: Map<number, LehrerCredentialsOhneBenutzerEintrag>;
	deleteBenutzerMenge: (selectedItems: BenutzerListeEintrag[]) => Promise<void>;
}