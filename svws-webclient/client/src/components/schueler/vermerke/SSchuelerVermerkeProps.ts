import {List, Schueler, SchuelerVermerke} from "@core";

export interface SchuelerVermerkeProps {
	data: List<SchuelerVermerke>;
	patch: (data : Partial<SchuelerVermerke>) => Promise<void>;
}
