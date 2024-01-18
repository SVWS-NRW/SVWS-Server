import type { List} from "@core";
import {LehrerListeEintrag} from "@core";
import { api } from "~/Api";


process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

class DataLehrerAuswahl{

	listeLehrer? : List<LehrerListeEintrag>;

	constructor(){

	}

	async laden () {
		await api.connectTo((process.env.PLAYWRIGHT_svws_testing_api_host ?? 'https://localhost') + (process.env.PLAYWRIGHT_svws_testing_api_port != null ? (':' + process.env.PLAYWRIGHT_svws_testing_api_port) : ''));
		await api.login('gymabi', 'Admin', '');
		this.listeLehrer = await api.server.getLehrer(api.schema);

	}

	public async getZufallslehrer(): Promise<LehrerListeEintrag>{
		await this.laden();
		let lehrer : LehrerListeEintrag = new LehrerListeEintrag();
		if(dataLehrerAuswahl.listeLehrer?.size())
			lehrer = dataLehrerAuswahl.listeLehrer?.get(Math.floor(Math.random()* dataLehrerAuswahl.listeLehrer?.size()));
		return lehrer;
	}

	// /**
	//  * liefert alle Schüler  mit dem Status 2, also aktive Schüler
	//  */
	// public async getSchueler() : Promise<Schueler[]> {
	// 	await this.laden();
	// 	if (this.listeSchueler === undefined || this.listeKlassen === undefined)
	// 		return[];
	// 	const liste : Schueler[] = [];
	// 	for(const value of this.listeSchueler){
	// 		if( value.status === 2){
	// 			const klassen_kuerzel = this.listeKlassen.get(value.idKlasse)?.kuerzel;
	// 			const jahrgang = this.listeKlassen.get(value.idKlasse)?.idJahrgang;
	// 			let _jahrgang : number = -1;
	// 			if( jahrgang !== undefined && jahrgang !== null)
	// 				_jahrgang = jahrgang;
	// 			const role_name = klassen_kuerzel+' '+value.nachname+' '+value.vorname;
	// 			const dataSchueler : Schueler = {
	// 				id : value.id,
	// 				name : value.vorname+" "+value.nachname,
	// 				name2click: role_name,
	// 				import_lp_name: '',
	// 				export_lp_name: '',
	// 				abiturjahr: 2022,
	// 				jahrgang: _jahrgang,

	// 			}
	// 			liste.push(dataSchueler);
	// 		}
	// 	}
	// 	return liste;
	// }

	// /**
	//  * liefert die Schülerliste der übergebenen Jahrgänge mit der Anzahl n
	//  * @param jahrgaenge Jahrgänge der Schüler
	//  * @param n Anzahl der ausgewählten Schüler
	//  * @returns Schülerlister
	//  */
	// public async getSchuelermitJahrgang_N(jahrgaenge : number[], n : number) : Promise<Schueler[]> {
	// 	await this.laden();
	// 	if (this.listeSchueler === undefined || this.listeKlassen === undefined)
	// 		return [];
	// 	const liste : Schueler[] = [];
	// 	let anzahl : number = 0;

	// 	for (const value of this.listeSchueler) {

	// 		const idJahrgang = this.listeKlassen.get(value.idKlasse)?.idJahrgang;
	// 		if( typeof idJahrgang  === "number"){
	// 			if ( jahrgaenge.includes(idJahrgang) && (value.status === 2) && anzahl < n){
	// 				let _jahrgang : number = -1;
	// 				const jahrgang = this.listeKlassen.get(value.idKlasse)?.idJahrgang;
	// 				if( jahrgang !== undefined && jahrgang !== null)
	// 					_jahrgang = jahrgang;
	// 				const klassen_kuerzel = this.listeKlassen.get(value.idKlasse)?.kuerzel;
	// 				const role_name = klassen_kuerzel+' '+value.nachname+' '+value.vorname;
	// 				const dataSchueler : Schueler = {
	// 					id : value.id,
	// 					name : value.vorname+" "+value.nachname,
	// 					name2click: role_name,
	// 					import_lp_name: 'Export_Laufbahnplanung'+value.id+'_'+value.vorname+'_'+value.nachname+'.lp',
	// 					export_lp_name: 'Export_Laufbahnplanung'+value.id+'_'+value.vorname+'_'+value.nachname+'.lp',
	// 					abiturjahr: 2022,
	// 					jahrgang: _jahrgang,

	// 				}
	// 				liste.push(dataSchueler);
	// 				anzahl++;
	// 			}
	// 		}
	// 	}
	// 	return liste;
	// }

}

/** Die dataSchuelerAuswahl-Instanz zur Verwendung im Browser-Test */
export const dataLehrerAuswahl = new DataLehrerAuswahl();