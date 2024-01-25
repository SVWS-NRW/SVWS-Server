import { dataSchuelerAuswahl } from "./DataSchuelerAuswahl";

export type Schueler = {
	id : number,
	name : string,
	name2click : string,
	import_lp_name : string,
	export_lp_name : string,
	abiturjahr : number,
	jahrgang : number
}

export const dataSchueler : Schueler[] = [
	{
		id : 9228,
		name : 'Bock Torsten',
		name2click: '09b Bock Torsten',
		import_lp_name: "Laufbahnplanung_2022_09_Bock_Torsten-9228.lp",
		export_lp_name: "Export_Laufbahnplanung_2022_09_Bock_Torsten-9228.lp",
		abiturjahr: 2022,
		jahrgang: 5,
	},
	{
		id : 9186,
		name : 'Steege Patrick',
		name2click: '09a Steege Patrick',
		import_lp_name: "Laufbahnplanung_2022_09_Bock_Torsten-9228.lp",
		export_lp_name: "Export_Laufbahnplanung_2022_09_Bock_Torsten-9228.lp",
		abiturjahr: 2022,
		jahrgang: 5,

	} ];

export const dataSchueler_JG9_IDs: number[] = [9115,9296]

export const dataSchueler_IDs_ab_EF: number[] = [1199,1173,1058]

export const dataSchueler_IDs_eine_pro_JGab9 : number[] =[9115,1244,1173,1058]

export const dataSchueler_3_von_JG9 : Schueler[] = [
	{
		id: 9115,
		name: 'Matthias Ankel',
		name2click: '09a Ankel Matthias',
		import_lp_name: 'Export_Laufbahnplanung9115_Matthias_Ankel.lp',
		export_lp_name: 'Export_Laufbahnplanung9115_Matthias_Ankel.lp',
		abiturjahr: 2022,
		jahrgang: 5
	  },
	  {
		id: 9298,
		name: 'Eric Berg',
		name2click: '09a Berg Eric',
		import_lp_name: 'Export_Laufbahnplanung9228_Eric_Berg.lp',
		export_lp_name: 'Export_Laufbahnplanung9228_Eric_Berg.lp',
		abiturjahr: 2022,
		jahrgang: 5
	  },
	  {
		id: 9187,
		name: 'Martina Halbe',
		name2click: '09b Halbe Martina',
		import_lp_name: 'Export_Laufbahnplanung9187_Martina_Halbe.lp',
		export_lp_name: 'Export_Laufbahnplanung9115_Martina_Halbe.lp',
		abiturjahr: 2022,
		jahrgang: 5
	  }
]

export const dataSchueler_3_von_JGabEF : Schueler[] = [
	{
	  id: 1244,
	  name: 'Annett Aits',
	  name2click: 'EF Aits Annett',
	  import_lp_name: 'Export_Laufbahnplanung1244_Annett_Aits.lp',
	  export_lp_name: 'Export_Laufbahnplanung1244_Annett_Aits.lp',
	  abiturjahr: 2022,
	  jahrgang: 10
	},
	{
	  id: 1173,
	  name: 'Ulrike Ahlenstiel',
	  name2click: 'Q1 Ahlenstiel Ulrike',
	  import_lp_name: 'Export_Laufbahnplanung1173_Ulrike_Ahlenstiel.lp',
	  export_lp_name: 'Export_Laufbahnplanung1173_Ulrike_Ahlenstiel.lp',
	  abiturjahr: 2022,
	  jahrgang: 11
	},
	{
	  id: 1058,
	  name: 'Jessika Aderhold',
	  name2click: 'Q2 Aderhold Jessika',
	  import_lp_name: 'Export_Laufbahnplanung1058_Jessika_Aderhold.lp',
	  export_lp_name: 'Export_Laufbahnplanung1058_Jessika_Aderhold.lp',
	  abiturjahr: 2022,
	  jahrgang: 12
	}
]



export const dataSchueler_1_pro_JGab9 : Schueler[] = [
	  {
		id: 1244,
		name: 'Annett Aits',
		name2click: 'EF Aits Annett',
		import_lp_name: 'Export_Laufbahnplanung1244_Annett_Aits.lp',
		export_lp_name: 'Export_Laufbahnplanung1244_Annett_Aits.lp',
		abiturjahr: 2022,
		jahrgang: 10
	  },
	{
	  id: 1173,
	  name: 'Ulrike Ahlenstiel',
	  name2click: 'Q1 Ahlenstiel Ulrike',
	  import_lp_name: 'Export_Laufbahnplanung1173_Ulrike_Ahlenstiel.lp',
	  export_lp_name: 'Export_Laufbahnplanung1173_Ulrike_Ahlenstiel.lp',
	  abiturjahr: 2022,
	  jahrgang: 11
	},
	{
	  id: 1058,
	  name: 'Jessika Aderhold',
	  name2click: 'Q2 Aderhold Jessika',
	  import_lp_name: 'Export_Laufbahnplanung1058_Jessika_Aderhold.lp',
	  export_lp_name: 'Export_Laufbahnplanung1058_Jessika_Aderhold.lp',
	  abiturjahr: 2022,
	  jahrgang: 12
	}
]




