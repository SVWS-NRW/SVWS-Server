import type { AbschnittAuswahlDaten } from "@comp";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

export interface StundenplanKatalogeAuswahlProps {
	returnToStundenplan: () => Promise<void>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	setChild: (value: AuswahlChildData) => Promise<void>;
	child: AuswahlChildData;
	children: AuswahlChildData[];
	childrenHidden: boolean[];
}