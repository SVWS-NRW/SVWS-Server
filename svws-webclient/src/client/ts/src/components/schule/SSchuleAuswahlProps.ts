import type { AuswahlChildData } from "../AuswahlChildData";
import type { SchuleStammdaten } from "@svws-nrw/svws-core";

export interface SchuleAuswahlProps {
	setChild: (value: AuswahlChildData) => Promise<void>;
	child: AuswahlChildData;
	children: AuswahlChildData[];
	childrenHidden: boolean[];
	schule: SchuleStammdaten;
}
