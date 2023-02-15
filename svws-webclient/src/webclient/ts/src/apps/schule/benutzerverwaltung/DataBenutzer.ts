import { BaseData } from "~/apps/BaseData";
import { router } from "~/router/RouteManager";
import { BenutzerDaten, BenutzergruppeDaten, BenutzergruppeListeEintrag, BenutzerKompetenz, BenutzerKompetenzGruppe,
	BenutzerListeEintrag, BenutzerManager, Credentials, Vector } from "@svws-nrw/svws-core-ts";
import { routeLogin } from "~/router/RouteLogin";
import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
import { routeSchuleBenutzerDaten } from "~/router/apps/benutzer/RouteSchuleBenutzerDaten";

export class DataBenutzer extends BaseData<BenutzerDaten, BenutzerListeEintrag, BenutzerManager> {

	protected on_update(daten: Partial<BenutzerDaten>): void {
		return void daten;
	}

	/**
	 * Wird bei einer Änderung des ausgewählten Listeneintrags aufgerufen und holt
	 * die Daten vom SVWS-Server.
	 *
	 * @returns {Promise<BenutzerDaten>} Die Daten als Promise
	 */
	public async on_select(): Promise<BenutzerDaten | undefined> {
		if (!this.selected_list_item)  {
			this.manager = undefined;
			return super.unselect();
		}
		const benutzerdaten = await super._select((eintrag: BenutzerListeEintrag) =>
			routeLogin.data.api.getBenutzerDaten(routeLogin.data.schema, eintrag.id)
		);
		if (benutzerdaten)
			this.manager = new BenutzerManager(benutzerdaten);
		return benutzerdaten;
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<BenutzerDaten>} data Die Daten, die aktualisiert werden sollen
	 *
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	 public async patch(data: Partial<BenutzerDaten>): Promise<boolean> {
		 return false;
	}


	// /**
	//  * Setzt, ob die Benutzergruppe eine administrative Gruppe ist oder nicht
	//  *
	//  * @param {boolean} istAdmin
	//  *
	//  * @returns {Promise<void>}
	//  */
	//  public async setIstAdmin(istAdmin: boolean): Promise<void> {
	// 	if (!this.manager)
	// 		return;
	// 	if(istAdmin)
	// 		await routeLogin.data.api.addBenutzerAdmin(routeLogin.data.schema, this.manager.getID());
	// 	else
	// 		await routeLogin.data.api.removeBenutzerAdmin(routeLogin.data.schema, this.manager.getID());
	// 	this.manager.setAdmin(istAdmin);
	// }

	// /**
	//  * Fügt den Benutzer in eine Benutzergruppe ein
	//  *
	//  * @param {number} bg_id
	//  *
	//  * @returns {Promise<void>}
	//  */
	//  public async addBenutzergruppeBenutzer(bg_id: number): Promise<void> {
	// 	if (!this.manager)
	// 		return;
	// 	const bg_ids = new Vector<number>();
	// 	bg_ids.add(this.manager.getID());
	// 	const result = await routeLogin.data.api.addBenutzergruppeBenutzer(bg_ids, routeLogin.data.schema,bg_id) as BenutzergruppeDaten;
	// 	this.manager.addToGruppe(result);
	// }

	// /**
	//  * Fügt den Benutzer in alle Benutzergruppe ein
	//  *
	//  * @param {} bg_id
	//  *
	//  * @returns {Promise<void>}
	//  */
	//  public async addBenutzergruppenBenutzer(bgle: BenutzergruppeListeEintrag[] | undefined): Promise<void> {
	// 	const benutzer_id = new Vector<number>();
	// 	benutzer_id.add(this.manager?.getID() ?? null);
	// 	bgle?.forEach(eintrag =>  {
	// 		if (!this.manager?.IstInGruppe(eintrag.id)) {
	// 			routeLogin.data.api.addBenutzergruppeBenutzer(benutzer_id, routeLogin.data.schema,eintrag.id)
	// 				.then(result => this.manager?.addToGruppe(result))
	// 				.catch(e => {throw e});
	// 		}
	// 	});
	// }

	// /**
	//  * Entfernt den Benutzer aus einer Gruppe
	//  *
	//  * @param {number} bg_id
	//  *
	//  * @returns {Promise<void>}
	//  */
	//  public async removeBenutzergruppeBenutzer(bg_id: number): Promise<void> {
	// 	if (!this.manager)
	// 		return;
	// 	const bg_ids = new Vector<number>();
	// 	bg_ids.add(this.manager.getID());
	// 	const result = await routeLogin.data.api.removeBenutzergruppeBenutzer(bg_ids, routeLogin.data.schema,bg_id) as BenutzergruppeDaten;
	// 	this.manager.removeFromGruppe(result);
	// }

	// /**
	//  * Entfernt den Benutzer aus allen Benutzergruppen.
	//  *
	//  * @param {bgle} Benutzergruppen
	//  *
	//  * @returns {Promise<void>}
	//  */
	//  public async removeBenutzergruppenBenutzer(bgle: BenutzergruppeListeEintrag[] | undefined): Promise<void> {
	// 	const benutzer_id = new Vector<number>();
	// 	benutzer_id.add(this.manager?.getID() ?? null);
	// 	bgle?.forEach(eintrag =>  {
	// 		if (this.manager?.IstInGruppe(eintrag.id)) {
	// 			routeLogin.data.api.removeBenutzergruppeBenutzer(benutzer_id, routeLogin.data.schema, eintrag.id)
	// 				.then(result => this.manager?.removeFromGruppe(result))
	// 				.catch(e => {throw e});
	// 		}
	// 	});

	// }

	// /**
	//  * Fügt die übergebene Kompetenz zu dem Benutzer hinzu
	//  *
	//  * @param kompetenz   die hinzuzufügende Kompetenz
	//  */
	//  public async addKompetenz(kompetenz : BenutzerKompetenz) : Promise<boolean> {
	// 	const kid = new Vector<number>();
	// 	kid.add(kompetenz.daten.id);
	// 	if (!this.manager)
	// 		return false;
	// 	if (this.manager.hatKompetenz(kompetenz))
	// 		return false;
	// 	await routeLogin.data.api.addBenutzerKompetenzen(kid, routeLogin.data.schema, this.manager.getID());
	// 	this.manager.addKompetenz(kompetenz);
	// 	return true;
	// }

	// /**
	//  * Entfernt die übergebene Kompetenz von diesem Benutzer
	//  *
	//  * @param kompetenz   die zu entfernende Kompetenz
	//  */
	//  public async removeKompetenz(kompetenz : BenutzerKompetenz) : Promise<boolean> {
	// 	const kid = new Vector<number>();
	// 	kid.add(kompetenz.daten.id);
	// 	if (!this.manager)
	// 		return false;
	// 	if (!this.manager.hatKompetenz(kompetenz))
	// 		return false;
	// 	await routeLogin.data.api.removeBenutzerKompetenzen(kid, routeLogin.data.schema, this.manager.getID());
	// 	this.manager.removeKompetenz(kompetenz);
	// 	return true;
	// }



	// /**
	//  * Fügt die übergebene Kompetenzen einer Benutzerkompetenzgruppe zu diesem Benutzer hinzu
	//  *
	//  * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen hinzugefügt werden.
	//  */
	//  public async addBenutzerKompetenzGruppe(kompetenzgruppe : BenutzerKompetenzGruppe) : Promise<boolean> {
	// 	const kids = new Vector<number>();
	// 	if (!this.manager)
	// 		return false;
	// 	if (!this.manager.istAdmin()) {
	// 		//Es werden nur die IDs der Kompetenzen in kids gespreichert, welche dem Benutzer direkt zugordnet sind.
	// 		// Sie sind also nicht über Benutzergruppen vererbt.
	// 		for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
	// 			if(this.manager.getGruppen(komp).size() === 0)
	// 				kids.add(komp.daten.id);
	// 		}
	// 		await routeLogin.data.api.addBenutzerKompetenzen(kids,routeLogin.data.schema,this.manager.getID());
	// 		//Den obigen Schritten entsprechende Anpassung des Client-Objekts mithilfe des Managers
	// 		for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
	// 			if(this.manager.getGruppen(komp).size() === 0){
	// 				if (!this.manager?.hatKompetenz(komp))
	// 					this.manager?.addKompetenz(komp);
	// 			}
	// 		}
	// 	}
	// 	return true;
	// }

	// /**
	//  * Entfernt die übergebene Kompetenzen einer Benutzerkompetenzgruppe von diesem Benutzer
	//  *
	//  * @param kompetenzgruppe   die Kompetenzgruppe, deren Kompetenzen entfernt werden.
	//  */
	//  public async removeBenutzerKompetenzGruppe(kompetenzgruppe : BenutzerKompetenzGruppe) : Promise<boolean> {
	// 	const kids = new Vector<number>();
	// 	if (!this.manager)
	// 		return false;
	// 	if (!this.manager.istAdmin()) {
	// 		for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe))
	// 			if(this.manager.getGruppen(komp).size() === 0)
	// 				kids.add(komp.daten.id);
	// 		await routeLogin.data.api.removeBenutzerKompetenzen(kids,routeLogin.data.schema,this.manager.getID());
	// 		for (const komp of BenutzerKompetenz.getKompetenzen(kompetenzgruppe)) {
	// 			if(this.manager.getGruppen(komp).size() === 0){
	// 				if (this.manager?.hatKompetenz(komp))
	// 					this.manager?.removeKompetenz(komp);
	// 			}
	// 		}
	// 	}
	// 	return true;
	// }
	// /**
	//  * Erstellt eine neue Benutzergruppe
	//  * @param bezeichnung die Bezichnung der neuen Benutzergruppe
	//  * @param istAdmin    True, wenn die neue Benutzrgruppe administrativ ist.
	//  */
	// public async createBenutzerAllgemein( anmeldename : string, benutzername : string, passwort : string){
	// 	const credential = new Credentials();
	// 	credential.benutzername = benutzername;
	// 	credential.password = passwort;
	// 	const result = await routeLogin.data.api.createBenutzerAllgemein(credential,routeLogin.data.schema,anmeldename);
	// 	const ble = new BenutzerListeEintrag();
	// 	ble.id = result.id;
	// 	ble.anzeigename = result.anzeigename;
	// 	ble.name = result.name;
	// 	ble.istAdmin= result.istAdmin;
	// 	ble.idCredentials = result.idCredentials;
	// 	routeSchuleBenutzer.liste.liste.push(ble);
	// 	routeSchuleBenutzer.liste.ausgewaehlt = result;
	// 	await router.push({ name: routeSchuleBenutzer.name, params: { id : ble.id } });
	// }

	// /**
	//  * Entfernt die ausgewählten Benutzer
	//  */
	// public async deleteBenutzerAllgemein(){
	// 	const benutzer = routeSchuleBenutzer.liste.ausgewaehlt_gruppe;
	// 	const bids = new Vector<number>();
	// 	for ( const b of benutzer){
	// 		bids.add(b.id)
	// 	}
	// 	await routeLogin.data.api.removeBenutzerAllgemein(bids,routeLogin.data.schema);
	// 	routeSchuleBenutzer.liste.ausgewaehlt_gruppe = [];
	// 	for(const b of benutzer) {
	// 		routeSchuleBenutzer.liste.liste = routeSchuleBenutzer.liste.liste.filter(item => item.id !== b.id);
	// 	}
	// 	alert("Benutzer gelöscht!!!");
	// 	await router.push({ name: routeSchuleBenutzer.name});
	// }

	// // /**
	// //  * Setzt das neue Passwort
	// //  *
	// //  * @passwort das neue Passwort
	// //  */

	// // public async setPassword( passwort : string ){
	// // 	if (!this.manager)
	// // 		return false;
	// // 	await routeLogin.data.api.setBenutzerPasswort(passwort,routeLogin.data.schema,this.manager.getID());
	// // 	setTimeout( function ( ) { alert( "Das Kennwort wurde erfolgreich geändert!!" ); }, 300 );
	// // }

	// /**
	//  * Liefert zur einer Kompetenz die Gruppenzugehörigkeiten
	//  *
	//  * @kompetenz die Kompetenz
	//  */
	// public getGruppen4Kompetenz( kompetenz : BenutzerKompetenz ) : string{
	// 	let text="";
	// 	let i = 0;
	// 	if(this.manager?.getGruppen(kompetenz)){
	// 		for(const bg of this.manager.getGruppen(kompetenz)){
	// 			if( i === 0)
	// 				text+=bg.bezeichnung;
	// 			else
	// 				text+=", "+bg.bezeichnung;
	// 			i = -2;
	// 		}
	// 	}
	// 	return text;
	// }
}