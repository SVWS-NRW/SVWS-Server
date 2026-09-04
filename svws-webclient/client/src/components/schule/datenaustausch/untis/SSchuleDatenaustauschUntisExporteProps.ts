import type { GostBlockungListeneintrag } from "@core/core/data/gost/GostBlockungListeneintrag";
import type { List } from "@core/java/util/List";

export interface SchuleDatenaustauschUntisExporteProps {
	exportUntisKlassenGPU003: () => Promise<string[]>;
	exportUntisLehrerGPU004: () => Promise<string[]>;
	exportUntisFaecherGPU006: () => Promise<string[]>;
	exportUntisSchuelerGPU010: (sidvariante: number) => Promise<string[]>;
	exportUntisFachwahlenGPU015: (sidvariante: number, gpu002: string) => Promise<string[]>;
	exportUntisKlausurenGPU017: (sidvariante: number, gpu002: string) => Promise<string[]>;
	exportUntisSchienenGPU019: (gpu002: string) => Promise<string[]>;
	ladeBlockungslisten: (abijahrgaenge: number[]) => Promise<Array<List<GostBlockungListeneintrag>>>;
	exportUntisBlockung: (sidvariante: number, gpu002: string, blockungsergebnisse: number[]) => Promise<string[]>;
}
