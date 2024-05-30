import type { AbschnittAuswahlDaten } from "@comp";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface SchuleDatenaustauschAuswahlProps {
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	gotoSchule: () => Promise<void>;
	setChild: (value: AuswahlChildData) => Promise<void>;
	child: AuswahlChildData;
	children: AuswahlChildData[];
	childrenHidden: boolean[];
}