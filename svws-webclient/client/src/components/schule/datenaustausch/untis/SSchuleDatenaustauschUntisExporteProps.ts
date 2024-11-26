
export interface SchuleDatenaustauschUntisExporteProps {
	exportUntisKlassenGPU003: () => Promise<string>;
	exportUntisLehrerGPU004: () => Promise<string>;
	exportUntisFaecherGPU006: () => Promise<string>;
	exportUntisSchuelerGPU010: () => Promise<string>;
	exportUntisKlausurenGPU017: (idschema: "id" | "kurz" |"lang", gpu002: string) => Promise<string>;
}
