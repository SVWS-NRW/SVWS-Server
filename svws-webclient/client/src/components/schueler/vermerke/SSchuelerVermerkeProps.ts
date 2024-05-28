import {List, Schueler, SchuelerVermerke, VermerkartEintrag} from "@core";

export interface SchuelerVermerkeProps {
	data: List<SchuelerVermerke>;
	mapVermerkArten: Map<number, VermerkartEintrag>;
	patch: (data : Partial<SchuelerVermerke>, idVermerk : number) => Promise<void>;
	create: () => Promise<void>;
	deleteVermerk: (idVermerk: number) => Promise<void>;
}
