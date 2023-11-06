import type { BenutzerDaten} from "@core";

export interface BenutzerprofilDatenProps {
	benutzer: BenutzerDaten;
	patch: (data : Partial<BenutzerDaten>) => Promise<void>;
}