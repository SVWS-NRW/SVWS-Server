import type { BenutzerDaten} from "@core";

export interface BenutzerprofilConfigProps {
	benutzer: BenutzerDaten;
	patch: (data : Partial<BenutzerDaten>) => Promise<void>;
}