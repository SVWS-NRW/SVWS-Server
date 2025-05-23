import { expect, type Page } from "@playwright/test";
import { api } from "~/Api";
import { dataSchuelerAuswahl } from "../DataSchuelerAuswahl";
import { dataSchueler } from "../DataSchueler";
import type { Class, SchuelerStammdaten, TranspiledObject } from "@svws-nrw/svws-core";
import { schueler_fahrschueler_art, staatsangehörigkeit, schueler_status, geschlecht, konfession, schueler_haltestellen, sprachen, land } from "./kataloge";

const test_schueler : SchuelerStammdaten=  {
	id: 9228,
	foto: '',
	nachname: 'Isak',
	vorname: 'Büyük',
	alleVornamen: 'Isak Büyük',
	geschlecht: 4,
	geburtsdatum: '2002-11-11',
	geburtsort: 'Ispir',
	geburtsname: 'Yildiz',
	strassenname: 'Landsberger Straße',
	hausnummer: '180',
	hausnummerZusatz: '',
	wohnortID: 419, //Schermbeck
	ortsteilID: null,
	telefon: '01234-671182',
	telefonMobil: '0189-978344',
	emailPrivat: 'J.Aderhold@smail.de',
	emailSchule: null,
	staatsangehoerigkeitID: 'USA',
	staatsangehoerigkeit2ID: null,
	religionID: 13,
	druckeKonfessionAufZeugnisse: true,
	religionabmeldung: '2022-11-11',
	religionanmeldung: '2012-11-11',
	hatMigrationshintergrund: true,
	zuzugsjahr: null,
	geburtsland: 'XXC',
	verkehrspracheFamilie: 'din',
	geburtslandVater: 'XXC',
	geburtslandMutter: 'XXC',
	status: 8,
	istDuplikat: false,
	externeSchulNr: null,
	fahrschuelerArtID: 1,
	haltestelleID: 1,
	anmeldedatum: '2022-10-19',
	aufnahmedatum: '2011-08-01',
	istVolljaehrig: true,
	istSchulpflichtErfuellt: false,
	istBerufsschulpflichtErfuellt: false,
	hatMasernimpfnachweis: true,
	keineAuskunftAnDritte: true,
	erhaeltSchuelerBAFOEG: true,
	erhaeltMeisterBAFOEG: false,
	bemerkungen: 'Kapitel I \n Selam Millet',
	// bemerkungen: 'Kap. I.\r\n' +
	// 	'§ 1.\r\n' +
	// 	'Ich wusste wohl, mein Brutus, dass, als ich das, was die geistreichsten und gelehrtesten Philosophen in griechischer Sprache behandelt hatten, in lateinischer wiedergab, meine Arbeit mancherlei Tadel finden würde. Denn manchen und nicht gerade ungelehrten Männern gefällt das Philosophiren überhaupt nicht; andere wollen eine mässige Thätigkeit hier wohl gestatten, aber meinen, dass man nicht so grossen Fleiss und so viele Mühe darauf verwenden dürfe. Auch giebt es Männer, die, mit den Schriften der Griechen vertraut, die lateinischen verachten und sagen, dass sie ihre Mühe lieber auf jene verwenden mögen. Endlich werden auch Einige mich vermuthlich an andere Wissenschaften verweisen, weil diese Art von Schriftstellerei, trotz des Scharfsinns, doch nach ihrer Meinung meiner Person und Würde nicht gezieme.\r\n' +
	// 	'\r\n' +
	// 	'§ 2.\r\n' +
	// 	'Gegen alle Diese möchte ich hier Einiges sagen. Den Tadlern der Philosophie habe ich zwar schon hinlänglich in jener Schrift geantwortet, worin ich die von Hortensius angeklagte und getadelte Philosophie vertheidigt und gelobt habe, und da diese Schrift sowohl von Dir wie von Allen, denen ich ein Urtheil zutraue, gebilligt worden ist, so bin ich in diesen Arbeiten fortgefahren, damit es nicht scheine, als könnte ich das Interesse für diese Wissenschaft wohl erwecken, aber nicht dauernd erhalten. Wenn dagegen Manche, die dem wohl beistimmen, doch nur eine mässigere Thätigkeit hier gestatten wollen, so fordern sie eine Mässigung bei einem Gegenstande, wo sie schwer einzuhalten ist, und der, einmal aufgenommen, sich nicht in Schranken halten oder wieder bei Seite legen lässt. Vielmehr möchte ich dann eher Jenen beitreten, welche die Philosophie überhaupt nicht zulassen wollen, als Diesen, die eine Schranke für einen Gegenstand ziehen, der unerschöpflich ist und um so besser wird, je grösser er wird. ',
	isTranspiledInstanceOf: function (name: string): boolean {
		throw new Error("Function not implemented.");
	},
	getClass: function <T extends TranspiledObject>(): Class<T> {
		throw new Error("Function not implemented.");
	},
	hashCode: function (): number {
		throw new Error("Function not implemented.");
	},
	equals: function (obj: any): boolean {
		throw new Error("Function not implemented.");
	},
	clone: function (): unknown {
		throw new Error("Function not implemented.");
	},
	transpilerCanonicalName: function (): string {
		throw new Error("Function not implemented.");
	}
}

export class PageSchuelerIndividualdaten {

	public schueler_before_test:SchuelerStammdaten|undefined ;

	constructor(public page: Page) {}

	// Anfang API-Methoden
	public async testeAPI_SpeicherungSchueler()
	{
		await this.page.waitForTimeout(1000);
		const api_schueler = await api.server.getSchuelerStammdaten(api.schema,dataSchueler[0].id);
		//Allgemein
		expect(test_schueler.nachname).toBe(api_schueler.nachname);
		expect(test_schueler.vorname).toBe(api_schueler.vorname);
		expect(test_schueler.alleVornamen).toBe(api_schueler.alleVornamen);
		expect(test_schueler.geschlecht).toBe(api_schueler.geschlecht);
		expect(test_schueler.geburtsdatum).toBe(api_schueler.geburtsdatum);
		expect(test_schueler.geburtsort).toBe(api_schueler.geburtsort);
		expect(test_schueler.geburtsname).toBe(api_schueler.geburtsname);

		//Wohnort und Kontaktdaten
		expect(test_schueler.strassenname).toBe(api_schueler.strassenname);
		expect(test_schueler.wohnortID).toBe(api_schueler.wohnortID);
		//expect(test_schueler.ortsteilID).toBe(api_schueler.ortsteilID);  // TODO
		expect(test_schueler.telefon).toBe(api_schueler.telefon);
		expect(test_schueler.telefonMobil).toBe(api_schueler.telefonMobil);
		expect(test_schueler.emailPrivat).toBe(api_schueler.emailPrivat);
		expect(test_schueler.emailSchule).toBe(api_schueler.emailSchule);

		//Staatsangehörigkeit und Konfession
		expect(test_schueler.druckeKonfessionAufZeugnisse).toBe(api_schueler.druckeKonfessionAufZeugnisse);
		expect(test_schueler.staatsangehoerigkeitID).toBe(api_schueler.staatsangehoerigkeitID);
		expect(test_schueler.staatsangehoerigkeitID).toBe(api_schueler.staatsangehoerigkeitID);
		expect(test_schueler.religionID).toBe(api_schueler.religionID);
		expect(test_schueler.religionabmeldung).toBe(api_schueler.religionabmeldung);
		expect(test_schueler.religionanmeldung).toBe(api_schueler.religionanmeldung);

		//Migrationshintergrund
		expect(test_schueler.hatMigrationshintergrund).toBe(api_schueler.hatMigrationshintergrund);
		expect(test_schueler.geburtsland).toBe(api_schueler.geburtsland);
		expect(test_schueler.verkehrspracheFamilie).toBe(api_schueler.verkehrspracheFamilie);
		expect(test_schueler.geburtslandMutter).toBe(api_schueler.geburtslandMutter);
		expect(test_schueler.geburtslandVater).toBe(api_schueler.geburtslandVater);

		//Statusdaten
		// expect(test_schueler.istDuplikat).toBe(api_schueler.istDuplikat);
		expect(test_schueler.status).toBe(api_schueler.status);
		expect(test_schueler.fahrschuelerArtID).toBe(api_schueler.fahrschuelerArtID)
		expect(test_schueler.haltestelleID).toBe(api_schueler.haltestelleID);
		expect(test_schueler.istVolljaehrig).toBe(api_schueler.istVolljaehrig);
		// TODO Patch wird nicht angestoßen.
		//expect(test_schueler.istSchulpflichtErfuellt).toBe(api_schueler.istSchulpflichtErfuellt);
		expect(test_schueler.hatMasernimpfnachweis).toBe(api_schueler.hatMasernimpfnachweis);
		expect(test_schueler.keineAuskunftAnDritte).toBe(api_schueler.keineAuskunftAnDritte);
		expect(test_schueler.istBerufsschulpflichtErfuellt).toBe(api_schueler.istBerufsschulpflichtErfuellt);

		expect(test_schueler.erhaeltSchuelerBAFOEG).toBe(api_schueler.erhaeltSchuelerBAFOEG);
		expect(test_schueler.bemerkungen).toBe(api_schueler.bemerkungen);


	}
	// Ende API-Methoden

	//Anfang Test-Methoden
	async testeIndividualDaten_Allgemein(){

		// Speicher den Schüler vor der Bearbeitung
		this.schueler_before_test = await api.server.getSchuelerStammdaten(api.schema,dataSchueler[0].id);

		console.log(this.schueler_before_test);
		// Bearbeite den Schüler
		await this.fillContentCardAllgemein(test_schueler);
		await this.fillContentCardWohnortundKontaktdaten(test_schueler);
		await this.fillContentCardStaatsangehoerigkeitundNationalitaet(test_schueler);
		await this.fillContentCardMigrationshintergrund(test_schueler);
		await this.fillContentCardStatusdaten(test_schueler);

		await this.page.getByRole('heading', { name: 'Schüler' }).locator('span').click();

		//Überprüfung der Speicherung der Daten in DB
		await this.testeAPI_SpeicherungSchueler();

		//await this.page.waitForTimeout(1000);

		// Mache die Änderungen rückgängig.
		await this.page.getByRole('row', { name: dataSchueler[1].name2click }).click();
		await this.page.getByRole('row', { name: "09b Isak Büyük" }).click();
		await this.fillContentCardAllgemein(this.schueler_before_test);
		await this.fillContentCardWohnortundKontaktdaten(this.schueler_before_test);
		await this.fillContentCardStaatsangehoerigkeitundNationalitaet(this.schueler_before_test);
		await this.fillContentCardMigrationshintergrund(this.schueler_before_test);
		await this.fillContentCardStatusdaten(this.schueler_before_test);

	}

	//Ende Testmethoden

	//Anfagn ContentCard-ALLGEMEIN
	async fillContentCardAllgemein(s:SchuelerStammdaten|undefined) {
		if(s ){
			await this.fillTextfeld("Nachname",s.nachname);
			await this.fillTextfeld("Rufname",s.vorname);
			await this.fillTextfeld("Alle Vornamen",s.alleVornamen);
			await this.fillGeschlecht(s.geschlecht);
			await this.fillTextfeld("Geburtsdatum", s.geburtsdatum === null ? '' : s.geburtsdatum);
			await this.fillTextfeld("Geburtsort", s.geburtsort === null ? '' : s.geburtsort);
			await this.fillTextfeld("Geburtsname", s.geburtsname === null ? '' : s.geburtsname);
		}
		else{
			expect("Undefined darf").toBe("nicht sein");
		}

	}

	async fillGeschlecht(value: number) {
		await this.page.getByLabel('Geschlecht').click();
		await this.page.getByRole('option', { name: 'weiblich' }).click();
		await this.page.getByLabel('Geschlecht').click();
		await this.page.getByRole('option', { name: 'männlich' }).click();
		await this.page.getByLabel('Geschlecht').click();
		await this.page.getByRole('option', { name: 'divers' }).click();
		await this.page.getByLabel('Geschlecht').click();
		await this.page.getByRole('option', { name: 'ohne Angabe' }).click();
		await this.page.getByLabel('Geschlecht').click();
		await this.page.getByRole('option', { name: geschlecht.get(value) }).click();
	}

	//Ende Contentcard-Allgemein


	//Anfang Contentcard-WohnortundKontaktdaten
	async fillContentCardWohnortundKontaktdaten(s:SchuelerStammdaten|undefined) {
		if(s ){
			await this.page.getByRole('heading', { name: 'Wohnort und Kontaktdaten' }).click();
			await this.fillTextfeld("Straße", s.strassenname === null ? '' : s.strassenname);
			await this.fillWohnort(s.wohnortID);
			await this.fillOrtsteil(s.ortsteilID);
			await this.fillTextfeld("Telefon", s.telefon === null ? '' : s.telefon);
			await this.fillTextfeld("Mobil oder Fax", s.telefonMobil === null ? '' : s.telefonMobil);
			await this.fillTextfeld("Private E-Mail-Adresse", s.emailPrivat === null ? '' : s.emailPrivat);
			await this.fillTextfeld("Schulische E-Mail-Adresse", s.emailSchule === null ? '' : s.emailSchule);
		}
		else{
			expect("Undefined darf").toBe("nicht sein");
		}
	}

	/* Test nur mit den Orten : Kürten(630) und Langerwehe*/
	async fillWohnort(wohnortID: number | null) {
		if(wohnortID === 849){
			await this.page.getByLabel('Wohnort').click();
			await this.page.getByText('Rüthen').click();
		}
		else if(wohnortID === 419){
			await this.page.getByLabel('Wohnort').click();
			await this.page.getByText('Schermbeck').click();
		}
		else
			expect("Unerwatete WohnortId").toBe("Erwartet werden 849(rüthen) oder 419(Schermbeck)")
	}

	async fillOrtsteil(ortsteilID: number | null) {
		await this.page.getByLabel('Ortsteil').click();
		// TODO Implementierung des Clicks
	}
	//Ende Contentcard-WohnortundKontaktdaten



	//Anfang Contentcard-NationalitätundReligion
	async fillContentCardStaatsangehoerigkeitundNationalitaet(s:SchuelerStammdaten|undefined) {
		if(s ){
			await this.page.getByRole('heading', { name: 'Staatsangehörigkeit und' }).click();
			await this.clickCheckBox("Konfession aufs Zeugnis", s.druckeKonfessionAufZeugnisse);
			await this.click_1_staatsbuergerschaft(s.staatsangehoerigkeitID === null ? 'XXC' : s.staatsangehoerigkeitID);
			await this.click_2_staatsbuergerschaft(s.staatsangehoerigkeit2ID === null ? 'XXC' : s.staatsangehoerigkeit2ID);
			await this.click_Konfession(s.religionID);
			//await this.fillAbmeldungsDatumReligionsUnterricht(s.religionabmeldung);

			await this.fillTextfeld("Abmeldung vom", s.religionabmeldung === null ? '' : s.religionabmeldung);
			await this.fillTextfeld("Wiederanmeldung", s.religionanmeldung === null ? '' : s.religionanmeldung);
		}
		else{
			expect("Undefined darf").toBe("nicht sein");
		}
	}

	async clickKonfessionaufsZeugnis(value : boolean) {
		await expect(this.page.getByText('Konfession aufs Zeugnis')).toBeVisible();
		value ? await this.page.getByLabel('Konfession aufs Zeugnis').check()
			: await this.page.getByLabel('Konfession aufs Zeugnis').uncheck();
	}

	async click_1_staatsbuergerschaft(value : string){
		await expect(this.page.getByLabel('1. Staatsangehörigkeit')).toBeVisible();
		await this.page.getByLabel('1. Staatsangehörigkeit').click();
		await this.page.getByRole('option', { name: staatsangehörigkeit.get(value) }).click();
	}

	async click_2_staatsbuergerschaft(value : string){
		await expect(this.page.getByLabel('2. Staatsangehörigkeit')).toBeVisible();
		await this.page.getByLabel('2. Staatsangehörigkeit').click();
		await this.page.getByRole('option', { name: staatsangehörigkeit.get(value) }).click();
	}

	async click_Konfession(i : number | null){
		if(i){
			await this.page.getByLabel('Konfession', { exact: true }).click();
  			await this.page.getByRole('option', { name: konfession.get(i) }).click();
		}
		else{
			expect("Konfession_ID").toBe("darf nicht null sein");
		}
	}
	//Ende Contentcard-NationalitätundReligion

	//Anfang Contentcard-Migrationshintergrund
	async fillContentCardMigrationshintergrund(s:SchuelerStammdaten|undefined) {
		await this.page.getByRole('heading', { name: 'Migrationshintergrund' }).click();
		if(s ){
			await this.clickMigrationshintergrundVorhanden(s.hatMigrationshintergrund);
			if(s.hatMigrationshintergrund){
				await this.fillGeburtsland(s.geburtsland === null ? 'XXC' : s.geburtsland);
				await this.fillGeburtslandMutter(s.geburtslandMutter === null ? 'XXC' : s.geburtslandMutter);
				await this.fillGeburtslandVater(s.geburtslandVater === null ? 'XXC' : s.geburtslandVater);
				await this.fillVerkehrssprache(s.verkehrspracheFamilie === null ? 'deu' : s.verkehrspracheFamilie)
			}
		}
		else{
			expect("Undefined darf").toBe("nicht sein");
		}

	}

	async clickMigrationshintergrundVorhanden(value : boolean) {
		await expect(this.page.getByLabel('Migrationshintergrund')).toBeVisible();
		await expect(this.page.getByText('Migrationshintergrund vorhanden')).toBeVisible();
		value ? await this.page.getByLabel('Migrationshintergrund').check()
			: await this.page.getByLabel('Migrationshintergrund').uncheck();
	}

	async fillVerkehrssprache(value: string) {
		await expect(this.page.getByLabel('Verkehrssprache', { exact: true })).toBeVisible();
		await this.page.getByLabel('Verkehrssprache').click();
		await this.page.getByRole('option', { name: sprachen.get(value) }).click();
		await this.page.waitForLoadState('networkidle');
	}
	async fillGeburtsland(value: string) {
		await expect(this.page.getByLabel('Geburtsland', { exact: true })).toBeVisible();
		await this.page.getByLabel('Geburtsland', { exact: true }).click();
		await this.page.getByRole('option', { name: land.get(value) }).click();
	}

	async fillGeburtslandMutter(value: string) {
		await expect(this.page.getByLabel('Geburtsland Mutter', { exact: true })).toBeVisible();
		await this.page.getByLabel('Geburtsland Mutter', { exact: true }).click();
		await this.page.getByRole('option', { name: land.get(value) }).click();
	}

	async fillGeburtslandVater(value: string) {
		await expect(this.page.getByLabel('Geburtsland Vater', { exact: true })).toBeVisible();
		await this.page.getByLabel('Geburtsland Vater', { exact: true }).click();
		await this.page.getByRole('option', { name: land.get(value) }).click();
	}
	//Ende Contentcard-Migrationshintergrund

	//Anfang Contentcard-Statusdaten
	async fillContentCardStatusdaten(s:SchuelerStammdaten|undefined) {
		if(s ){
			await this.page.getByRole('heading', { name: 'Statusdaten' }).click();
			await this.selectStatus(s.status);
			await this.selectFahrschuelerArtID(s.fahrschuelerArtID);
			await this.selectHaltestelle(s.haltestelleID);
			await this.fillTextfeld("Anmeldedatum", s.anmeldedatum === null ? '' : s.anmeldedatum);
			await this.fillTextfeld("Aufnahmedatum", s.aufnahmedatum === null ? '' : s.aufnahmedatum);
			await this.fillTextfeld("Bemerkungen", s.bemerkungen === null ? '' : s.bemerkungen)
			await this.clickCheckBox("Volljährig",s.istVolljaehrig);
			await this.clickCheckBox("Schulpflicht erfüllt",s.istSchulpflichtErfuellt);
			await this.clickCheckBox("Masern Impfnachweis",s.hatMasernimpfnachweis);
			await this.clickCheckBox("Keine Auskunft an Dritte",s.keineAuskunftAnDritte);
			await this.clickCheckBox("Schulpflicht SII erfüllt",s.istBerufsschulpflichtErfuellt);
			await this.clickCheckBox("BAFöG",s.erhaeltSchuelerBAFOEG);
		}
		else{
			expect("Undefined darf").toBe("nicht sein");
		}
	}

	async selectStatus(value :  number){
		for(const value of schueler_status.values()){
			await this.page.getByRole('main').getByLabel('Status').click();
			await this.page.getByRole('option', { name: value }).click();
		}
		await this.page.getByRole('main').getByLabel('Status').click();
		await this.page.getByRole('option', { name: schueler_status.get(value) }).click();
	}

	async selectFahrschuelerArtID(value : number | null){
		await expect(this.page.getByLabel('Fahrschüler')).toBeVisible();
		if(value){
			for(const val of schueler_fahrschueler_art.values()){
				await this.page.getByLabel('Fahrschüler').click();
				await this.page.getByRole('option', { name: val }).click();
			}
			await this.page.getByLabel('Fahrschüler').click();
			await this.page.getByRole('option', { name: schueler_fahrschueler_art.get(value) }).click();
		}

		else{  // TODO Warte auf Erledigung des Bugs #1412
			await this.page.getByLabel('Fahrschüler').click();
			await this.page.getByRole('option', { name: schueler_fahrschueler_art.get(1) }).click();
		}

	}

	async selectHaltestelle(value : number | null){
		await expect(this.page.getByLabel('Haltestelle')).toBeVisible();
		if(value){
			for(const val of schueler_haltestellen.values()){
				await this.page.getByLabel('Haltestelle').click();
				await this.page.getByRole('option', { name: val }).click();
			}
			await this.page.getByLabel('Haltestelle').click();
			await this.page.getByRole('option', { name: schueler_haltestellen.get(value) }).click();
		}

		else{  // TODO Warte auf Erledigung des Bugs #1412
			await this.page.getByLabel('Haltestelle').click();
			await this.page.getByRole('option', { name: schueler_haltestellen.get(1) }).click();
		}

	}
	//Ende Contentcard-Statusdaten

	//Hilfsmethoden
	async fillTextfeld(name:string, value: string){
		await this.page.getByLabel(name+"").click();
  		await this.page.getByLabel(name+"").fill(value);
	}

	async clickCheckBox(name: string, value: boolean){
		await expect(this.page.getByText(name+"")).toBeVisible();
		value ? await this.page.getByLabel(name+"").check()
			: await this.page.getByLabel(name+"").uncheck();
	}
}
