
export interface SchuleDatenaustauschUntisExporteProps {
	exportUntisKlassenGPU003: () => Promise<string>;
	exportUntisLehrerGPU004: () => Promise<string>;
	exportUntisFaecherGPU006: () => Promise<string>;
	exportUntisSchuelerGPU010: (sidvariante: number) => Promise<string>;
	exportUntisKlausurenGPU017: (sidvariante: number, gpu002: string) => Promise<string>;
}