import type { List, SchuelerVermerke, VermerkartEintrag } from "@core";

export interface SchuelerVermerkeProps {
	schuelerVermerke: () => List<SchuelerVermerke>;
	mapVermerkArten: Map<number, VermerkartEintrag>;
	patch: (data : Partial<SchuelerVermerke>, idVermerk : number) => Promise<void>;
	add: () => Promise<void>;
	remove: (idVermerk: number) => Promise<void>;
}
