import { Schuljahresabschnitt } from "@svws-nrw/svws-core-ts";

export interface SchuleDatenaustauschAuswahlProps {
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToSchule: () => Promise<void>;
}