import type { Schuljahresabschnitt } from "@svws-nrw/svws-core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface SchuleDatenaustauschAuswahlProps {
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	returnToSchule: () => Promise<void>;
	setChild: (value: AuswahlChildData) => Promise<void>;
	child: AuswahlChildData;
	children: AuswahlChildData[];
	childrenHidden: boolean[];
}