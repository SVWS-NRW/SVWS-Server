import type { AuswahlChildData } from "../AuswahlChildData";
import type { SchuleStammdaten } from "@core";

export interface SchuleAuswahlProps {
	setChild: (value: AuswahlChildData) => Promise<void>;
	child: AuswahlChildData;
	children: AuswahlChildData[];
	childrenHidden: boolean[];
	schule: () => SchuleStammdaten;
}
