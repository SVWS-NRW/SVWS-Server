import type { AbschnittAuswahlDaten } from "@comp";

export interface ZeitrasterAuswahlProps {
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	returnToKataloge: () => Promise<void>;
	returnToStundenplan: () => Promise<void>;
}