import { BenutzerListeEintrag, List } from "@svws-nrw/svws-core-ts";
export interface BenutzerAuswahlProps {
    auswahl: BenutzerListeEintrag | undefined;
	auswahlGruppe: BenutzerListeEintrag[];
	listBenutzer: List<BenutzerListeEintrag>;
	setBenutzer: (benutzer: BenutzerListeEintrag) => Promise<void>;
	createBenutzerAllgemein : (anmeldename: string, benutzername: string, passwort: string) => Promise<void>;
	deleteBenutzerAllgemein : () => Promise<void>;
	setAuswahlGruppe: (value: BenutzerListeEintrag[]) => void;
}