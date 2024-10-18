import type { BenutzerDaten, SchuleStammdaten } from "@core";

export interface EinstellungenProps {
	schule: () => SchuleStammdaten;
	benutzerdaten: () => BenutzerDaten;
}