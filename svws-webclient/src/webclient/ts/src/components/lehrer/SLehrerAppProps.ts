import { LehrerStammdaten } from "@svws-nrw/svws-core-ts";
import { AuswahlChildData } from "../AuswahlChildData";

export interface LehrerAppProps {
	stammdaten: LehrerStammdaten | undefined;
	setChild: (value: AuswahlChildData) => Promise<void>;
	child: AuswahlChildData;
	children: AuswahlChildData[];
	childrenHidden: boolean[];
}