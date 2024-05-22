import {List, Schueler, SchuelerVermerke, VermerkartEintrag} from "@core";

export interface SchuelerVermerkeProps {
	data: List<SchuelerVermerke>;
	mapVermerkArten: Map<number, VermerkartEintrag>;
	patch: (data : Partial<SchuelerVermerke>, id : number) => Promise<void>;
}
