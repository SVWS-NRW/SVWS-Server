export const schueler_fahrschueler_art = new Map<number, string>([
	[1	,	"Rheingold"],
	[2	,	"Meinhold"],
	[3	,	"WSW"],
])

export const schueler_haltestellen = new Map<number, string>([
	[1	,	"628 Meckelstraße"],
	[2	,	"628 Fingscheid"],
	[3	,	"RE7 Hauptbahnhof"],
])

export const geschlecht = new Map<number, string>([
	[3	,	"männlich"],
	[4	,	"weiblich"],
	[5	,	"diverse"],
	[6	,	"ohne Angebe"]
])

export const schueler_status = new Map<number, string>([
	[0	,	"Neuaufnahme"],
	[1	,	"Warteliste"],
	[2	,	"Aktiv"],
	[3	,	"Beurlaubt"],
	[4	,	"Extern"],
	[5	,	"Abschluss"],
	[6	,	"Abgang"],
	[7	,	"Ehemalige"]
])

export const land = new Map<string, string>([
	["DEU"	,	"Deutschland (DEU)"],
	["DMA"	,	"Dominica (DMA)"],
	["ECU"  , 	"Ecuador (ECU)"],
	["SLV"  , 	"El Salvador (SLV)"],
	["XXC"	,	"(ohne Angabe) (XXC)"]
])

export const sprachen = new Map<string, string>([
	["deu"	,	"Deutsch (deu)"],
	["din"	,	"Dinka (din)"]
])


export const staatsangehörigkeit = new Map<string, string>([
	["DEU"	,	"deutsch"],
	["USA"	,	"amerikanisch"],
	["XXC"	,	"ohne Angabe"]
])

export const konfession = new Map<number, string>([
	[11, "evangelisch"],
	[12, "katholisch"],
	[13, "islamisch"],
	[14, "ohne Bekenntnis"],
	[15, "sonstige orthodoxe"],
	[16, "jüdisch"],
	[17, "andere Religionen"]
]);

