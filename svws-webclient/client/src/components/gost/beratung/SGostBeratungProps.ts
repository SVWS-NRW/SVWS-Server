import type { BenutzerDaten, BenutzerKompetenz, GostJahrgangsdaten } from "@core";
import type { Config } from "@ui";

export interface GostBeratungProps {
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerdaten: BenutzerDaten;
	config: () => Config;
	patchJahrgangsdaten: (data: Partial<GostJahrgangsdaten>, abiturjahr: number) => Promise<boolean>;
	jahrgangsdaten: () => GostJahrgangsdaten;
}