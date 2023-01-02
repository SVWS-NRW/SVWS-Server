<template>
	<svws-ui-content-card>
		<div class="flex flex-row gap-4">
			<div class="-my-2 flex-none sm:-mx-6 lg:-mx-8">
				<div class="inline-block py-2 align-middle sm:px-6 lg:px-8">
					<div class="overflow-hidden rounded-lg shadow">
						<table class="border-collapse text-sm">
							<thead :class="{'bg-slate-100': !manuell, 'bg-red-400': manuell}">
								<tr>
									<td class="border border-[#7f7f7f]/20 text-center" colspan="3"> Fach </td>
									<td class="border border-[#7f7f7f]/20 text-center" colspan="2"> Sprachen </td>
									<td class="border border-[#7f7f7f]/20 text-center" colspan="2"> EF </td>
									<td class="border border-[#7f7f7f]/20 text-center" colspan="4"> Qualifikationsphase </td>
									<td class="border border-[#7f7f7f]/20 px-2 text-center" rowspan="2" > Abitur<br />-fach </td>
								</tr>
								<tr>
									<td class="border border-[#7f7f7f]/20 px-2 text-center"> Kürzel </td>
									<td class="border border-[#7f7f7f]/20 text-center"> Bezeichnung </td>
									<td class="border border-[#7f7f7f]/20 text-center">WStd.</td>
									<td class="border border-[#7f7f7f]/20 text-center">Folge</td>
									<td class="border border-[#7f7f7f]/20 text-center">ab Jg</td>
									<td class="border border-[#7f7f7f]/20 text-center">EF.1</td>
									<td class="border border-[#7f7f7f]/20 text-center">EF.2</td>
									<td class="border border-[#7f7f7f]/20 text-center">Q1.1</td>
									<td class="border border-[#7f7f7f]/20 text-center">Q1.2</td>
									<td class="border border-[#7f7f7f]/20 text-center">Q2.1</td>
									<td class="border border-[#7f7f7f]/20 text-center">Q2.2</td>
								</tr>
							</thead>
							<tr v-for="row in rows" :key="row.id" class="select-none">
								<s-row-lupo :fach="row" :dataLaufbahn="dataLaufbahn" :dataFaecher="dataFaecher" :dataFachkombinationen="dataFachkombinationen" />
							</tr>
							<thead class="bg-slate-100">
								<tr>
									<td class="border border-[#7f7f7f]/20 text-center" colspan="5" ></td>
									<td class="border border-[#7f7f7f]/20 text-center">EF.1</td>
									<td class="border border-[#7f7f7f]/20 text-center">EF.2</td>
									<td class="border border-[#7f7f7f]/20 text-center">Q1.1</td>
									<td class="border border-[#7f7f7f]/20 text-center">Q1.2</td>
									<td class="border border-[#7f7f7f]/20 text-center">Q2.1</td>
									<td class="border border-[#7f7f7f]/20 text-center">Q2.2</td>
									<td class="border border-[#7f7f7f]/20 text-center"></td>
								</tr>
							</thead>
							<tr>
								<td class="border border-[#7f7f7f]/20 bg-slate-100 text-center" colspan="5" > Anzahl Kurse </td>
								<td v-for="(jahrgang, i) in kurszahlen" :key="i"
									class="border border-[#7f7f7f]/20 text-center"
									:class="{
										'bg-yellow-400': jahrgang < 10,
										'bg-green-300': jahrgang > 9,
										'bg-green-700': jahrgang > 11
									}"
								> {{ jahrgang }} </td>
								<td class="border border-[#7f7f7f]/20 bg-slate-100 text-center"
									:class="{
										'bg-red-400': kurse_summe < 30,
										'bg-yellow-400': kurse_summe >= 31 && kurse_summe <= 32,
										'bg-green-300': kurse_summe > 32 && kurse_summe < 37,
										'bg-green-700': kurse_summe > 36
									}"
								> {{ kurse_summe }} </td>
							</tr>
							<tr>
								<td class="border border-[#7f7f7f]/20 bg-slate-100 text-center" colspan="5" > Wochenstunden </td>
								<td v-for="(jahrgang, i) in wochenstunden" :key="i"
									class="border border-[#7f7f7f]/20 text-center"
									:class="{
										'bg-red-400': jahrgang < 30,
										'bg-yellow-400': jahrgang >= 31 && jahrgang <= 32,
										'bg-green-300': jahrgang > 32 && jahrgang < 37,
										'bg-green-700': jahrgang > 36
									}"
								> {{ jahrgang }} </td>
								<td class="border border-[#7f7f7f]/20 bg-slate-100 text-center"
									:class="{
										'bg-red-400': wst_summe < 100,
										'bg-yellow-400': wst_summe >= 100 && wst_summe < 102,
										'bg-green-300': wst_summe >= 102 && wst_summe <= 108,
										'bg-green-700': wst_summe > 108
									}"
								> {{ wst_summe }} </td>
							</tr>
							<tr>
								<td class="border border-[#7f7f7f]/20 bg-slate-100 text-center" colspan="5" > Durchschnitt </td>
								<td colspan="2"
									class="border border-[#7f7f7f]/20 text-center"
									:class="{
										'bg-red-400': wst_d_ef < 34,
										'bg-green-300': wst_d_ef >= 34 && wst_d_ef < 37,
										'bg-green-700': wst_d_ef >= 37
									}"
								> {{ wst_d_ef }} </td>
								<td colspan="4"
									class="border border-[#7f7f7f]/20 bg-slate-100 text-center"
									:class="{
										'bg-red-400': wst_d_q < 34,
										'bg-green-300': wst_d_q >= 34 && wst_d_q < 37,
										'bg-green-700': wst_d_q >= 37
									}"
								> {{ wst_d_q }} </td>
								<td class="border border-[#7f7f7f]/20 bg-slate-100"></td>
							</tr>
						</table>
					</div>
					<div class="flex justify-between gap-1">
						<svws-ui-button @click.prevent="download_file" >Wahlbogen herunterladen</svws-ui-button >
						<svws-ui-button @click="toggle_modal">Reset</svws-ui-button>
						<svws-ui-button :type="manuell ? 'danger':'primary'" @click="manu">Manuellen Modus {{manuell?"de":""}}aktivieren</svws-ui-button>
					</div>
				</div>
			</div>
			<div class="-my-2 print:hidden sm:-mx-6 lg:-mx-8">
				<div class="inline-block py-2 align-middle sm:px-6 lg:px-8 w-full">
					<div class="overflow-hidden rounded-lg shadow">
						<table class="border-collapse text-sm">
							<thead class="bg-slate-100">
								<tr> <td class="px-2">Belegprüfung</td> </tr>
							</thead>
							<tbody>
								<tr class="border border-[#7f7f7f]/20 text-left" >
									<td class="px-2">
										<div class="flex gap-4">
											<div class="form-check form-check-inline cursor-pointer" >
												<input type="radio" name="inlineRadioOptions" id="ef"
													v-model="belegpruefungsart"
													:value="ef1"
													class="bg-text-center float-left mt-[0.15rem] mr-2 h-4 w-4 cursor-pointer rounded-full border border-gray-300 bg-white bg-contain checked:border-slate-100 checked:bg-slate-100 focus:outline-none"
												/>
												<label class="form-check-label inline-block cursor-pointer" for="ef" >EF 1</label >
											</div>
											<div class="form-check form-check-inline cursor-pointer" >
												<input id="gesamt" type="radio" name="inlineRadioOptions"
													v-model="belegpruefungsart"
													:value="gesamt"
													class="bg-text-center float-left mt-[0.15rem] mr-2 h-4 w-4 cursor-pointer rounded-full border border-gray-300 bg-white bg-contain checked:border-slate-100 checked:bg-slate-100 focus:outline-none"
												/>
												<label class="form-check-label inline-block cursor-pointer" for="gesamt" >Gesamt</label >
											</div>
										</div>
									</td>
								</tr>
							</tbody>
							<thead class="bg-slate-100">
								<tr>
									<td class="px-2"> Laufbahnfehler
										<svws-ui-badge v-if="belegungsfehler.size()" variant="error" size="tiny" >
											{{ belegungsfehler.size() }} </svws-ui-badge>
									</td>
								</tr>
							</thead>
							<tbody>
								<tr v-for="fehler in belegungsfehler" :key="fehler.code.toString()" class="border border-[#7f7f7f]/20 text-left" >
									<td v-if="belegungsfehler.size()" class="px-2" > {{ fehler.beschreibung }} </td>
								</tr>
								<tr v-if="!belegungsfehler.size()">
									<td class="px-2">Keine</td> </tr>
							</tbody>
							<thead class="bg-slate-100">
								<tr>
									<td class="px-2"> Informationen zur Laufbahn </td>
								</tr>
							</thead>
							<tbody>
								<tr v-for="fehler in belegungsHinweise" :key="fehler.code.toString()" class="border border-[#7f7f7f]/20 text-left" >
									<td class="px-2"> {{ fehler.beschreibung }} </td>
								</tr>
								<tr v-if="!belegungsHinweise.size()">
									<td class="px-2">Keine</td>
								</tr>
							</tbody>
							<template v-if="(fachkombis.size())">
								<thead class="bg-slate-100">
									<tr>
										<td class="px-2"> Informationen zu Fachkombinationsregeln </td>
									</tr>
								</thead>
								<tbody>
									<tr v-for="regel in fachkombi_erforderlich()" :key="regel.id" class="border border-[#7f7f7f]/20 text-left" >
										<td class="px-2">
											<span v-if="regel_umgesetzt(regel)" class="px-2 rounded-full bg-green-400 mr-1"></span> {{ regel.hinweistext }} </td>
									</tr>
									<tr v-for="regel in fachkombi_verboten()" :key="regel.id" class="border border-[#7f7f7f]/20 text-left" >
										<td class="px-2">
											<span v-if="regel_umgesetzt(regel)" class="px-2 rounded-full bg-green-400 mr-1"></span> {{ regel.hinweistext }} </td>
									</tr>
								</tbody>
							</template>
						</table>
					</div>
				</div>
				<div class="am:px-6 py-2 lg:px-8">
					<svws-ui-text-input v-model="inputBeratungsdatum" type="date" placeholder="Beratungsdatum" />
				</div>
				<div class="am:px-6 py-2 lg:px-8">
					<svws-ui-textarea-input placeholder="Kommentar" resizeable="vertical"></svws-ui-textarea-input>
				</div>
				<div v-if="dataLaufbahn.daten?.sprachendaten.pruefungen.size()" class="inline-block py-2 align-middle sm:px-6 lg:px-8 w-full">
					<div class="overflow-hidden rounded-lg shadow">
						<table class="border-collapse text-sm w-full">
							<thead class="bg-slate-100">
								<tr>
									<td colspan="5" class="px-2">Sprachprüfungen</td>
								</tr>
								<tr>
									<td class="px-2">Sprache</td>
									<td class="px-2">Typ</td>
									<td class="px-2">Niveau</td>
									<td class="px-2">Ersetzt</td>
									<td class="px-2 text-center">Note</td>
								</tr>
							</thead>
							<tbody>
								<tr v-for="pruefung in dataLaufbahn.daten?.sprachendaten.pruefungen" :key="pruefung.sprache?.valueOf()" class="border bottom-1  border-[#7f7f7f]/20">
									<td class="px-2">{{pruefung.sprache}}</td>
									<td class="px-2">{{pruefung.istHSUPruefung ? "HSU":''}}{{pruefung.istFeststellungspruefung ? 'SFP':''}}</td>
									<td class="px-2">{{Sprachpruefungniveau.getByID(pruefung.anspruchsniveauId || null)?.daten.beschreibung}}</td>
									<td class="px-2">{{pruefung.istHSUPruefung?'–':''}}{{pruefung.kannErstePflichtfremdspracheErsetzen ? 'Erste Fremdsprache':''}}{{pruefung.kannZweitePflichtfremdspracheErsetzen ? 'Zweite Fremdsprache':''}}{{pruefung.kannWahlpflichtfremdspracheErsetzen ? 'Wahlpflichtfremdsprache':''}}</td>
									<td class="text-center">{{pruefung.note}}</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>

	<svws-ui-modal ref="modal" size="small">
		<template #modalTitle>Alle Kurswahlen löschen</template>
		<template #modalDescription>
			<div class="flex gap-1 mb-2">
				Sollen die Fachwahlen der noch in Planung befindlichen Halbjahre gelöscht werden?
			</div>
			<div class="flex gap-1">
				<svws-ui-button @click="toggle_modal">Abbrechen</svws-ui-button>
				<svws-ui-button @click="reset_fachwahlen">Ja</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";

	import { GostBelegpruefungErgebnisFehler, GostBelegpruefungsArt, GostBelegungsfehlerArt, GostFach, Sprachpruefungniveau,
		List, Vector, GostJahrgangFachkombination, GostLaufbahnplanungFachkombinationTyp, GostHalbjahr, GostKursart, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { App } from "~/apps/BaseApp";
	import { DataSchuelerLaufbahnplanung } from "~/apps/schueler/DataSchuelerLaufbahnplanung";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";

	const { item, stammdaten, dataLaufbahn, dataFaecher, dataFachkombinationen } = defineProps<{ 
		item?: SchuelerListeEintrag, 
		stammdaten: DataSchuelerStammdaten, 
		dataLaufbahn: DataSchuelerLaufbahnplanung,
		dataFaecher: DataGostFaecher, 
		dataFachkombinationen: DataGostFachkombinationen
	}>();

	const ef1: GostBelegpruefungsArt = GostBelegpruefungsArt.EF1;
	const gesamt: GostBelegpruefungsArt = GostBelegpruefungsArt.GESAMT;
	const manuell = ref(false)

	const modal: Ref<any> = ref(null);

	function toggle_modal() {
		modal.value.isOpen ? modal.value.closeModal() : modal.value.openModal();
	};

	function reset_fachwahlen() {
		modal.value.closeModal();
		dataLaufbahn.reset_fachwahlen();
	}

	const abiturmanager = computed(()=> dataLaufbahn.manager);
	const faechermanager = computed(()=> dataFaecher.manager);

	const rows: ComputedRef<List<GostFach>> = computed(() => dataFaecher.daten || new Vector());

	const kurszahlen: ComputedRef<number[]> = computed(() => dataLaufbahn.anrechenbare_kurszahlen);

	const kurse_summe: ComputedRef<number> = computed(() => dataLaufbahn.anrechenbare_kurszahlen.reduce((p, c) => p + c, 0));
		//TODO korrigieren

	const wochenstunden: ComputedRef<number[]> = computed(() => dataLaufbahn.wochenstunden);

	const wst_summe: ComputedRef<number> = computed(() => wochenstunden.value.reduce((p, c) => p + c, 0) / 2);

	const wst_d_ef: ComputedRef<number> = computed(() => (wochenstunden.value[0] + wochenstunden.value[1]) / 2);

	const wst_d_q: ComputedRef<number> = computed(() => {
		const [e, f, ...q] = wochenstunden.value;
		void e, f;
		return q.reduce((p, c) => p + c, 0) / 4;
	});

	const belegungsfehlerAlle: ComputedRef<List<GostBelegpruefungErgebnisFehler>> = computed(() => dataLaufbahn.gostBelegpruefungsErgebnis.fehlercodes);

	const belegungsfehler: ComputedRef<List<GostBelegpruefungErgebnisFehler>> = computed(() => {
		let res = new Vector<GostBelegpruefungErgebnisFehler>();
		for (const fehler of belegungsfehlerAlle.value)
			if (!!fehler &&
				(GostBelegungsfehlerArt.fromKuerzel(fehler.art) ===
				GostBelegungsfehlerArt.BELEGUNG ||
				GostBelegungsfehlerArt.fromKuerzel(fehler.art) ===
				GostBelegungsfehlerArt.SCHRIFTLICHKEIT))
					res.add(fehler);
		return res;
	});

	const belegungsHinweise: ComputedRef<List<GostBelegpruefungErgebnisFehler>> = computed(() => {
		let res = new Vector<GostBelegpruefungErgebnisFehler>();
		for (const fehler of belegungsfehlerAlle.value)
			if (!!fehler && GostBelegungsfehlerArt.fromKuerzel(fehler.art) === GostBelegungsfehlerArt.HINWEIS)
				res.add(fehler);
		return res;
	});

	const belegpruefungsart: WritableComputedRef<GostBelegpruefungsArt> = computed({
		get(): GostBelegpruefungsArt {
			return dataLaufbahn.gostAktuelleBelegpruefungsart;
		},
		set(value: GostBelegpruefungsArt) {
			dataLaufbahn.gostAktuelleBelegpruefungsart = value;
		}
	});
	
	const fachkombis: ComputedRef<List<GostJahrgangFachkombination>> = computed(()=>{
		let list = new Vector<GostJahrgangFachkombination>();
		if (dataFachkombinationen.daten === undefined)
			return list;
		for (const regel of	dataFachkombinationen.daten)
			if (regel.abiturjahr === item?.abiturjahrgang)
				list.add(regel)
		return list;
	})

	const fachkombi_erforderlich = (): List<GostJahrgangFachkombination> => {
		let result = new Vector<GostJahrgangFachkombination>()
		for (const kombi of fachkombis.value)
			if (GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() === kombi.typ) {
				if (kombi.hinweistext === "") {
					const fach1 = faechermanager.value?.get(kombi.fachID1);
					const fach2 = faechermanager.value?.get(kombi.fachID2);
					kombi.hinweistext = `${fach1?.kuerzel} als ${kombi.kursart1} erlaubt kein ${fach2} als ${kombi.kursart2}`;
				}
				result.add(kombi);
			}
		return result;
	}

	const fachkombi_verboten = (): List<GostJahrgangFachkombination> => {
		let result = new Vector<GostJahrgangFachkombination>()
		for (const kombi of fachkombis.value)
			if (GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue() === kombi.typ) {
				if (kombi.hinweistext === "") {
					const fach1 = faechermanager.value?.get(kombi.fachID1);
					const fach2 = faechermanager.value?.get(kombi.fachID2);
					kombi.hinweistext = `${fach1?.kuerzel} als ${kombi.kursart1} erfordert ${fach2} als ${kombi.kursart2}`;
				}
				result.add(kombi);
			}
		return result;
	}

	const inputBeratungsdatum: WritableComputedRef<string> = computed({
		get(): string {
			return ""; // props.stammdaten.daten.geburtsdatum;
		},
		set(val: string) {
			void val;
			// props.stammdaten.patch({ geburtsdatum: val });
		}
	});

	function manu() {
		manuell.value = manuell.value ? false:true; dataLaufbahn.manuelle_eingabe = manuell.value
	}

	function download_file() {
		const id = stammdaten.daten?.id;
		if (!id) 
			return;
		App.api.getGostSchuelerPDFWahlbogen(App.schema, id).then(blob => {
			const link = document.createElement("a");
			link.href = URL.createObjectURL(blob);
			link.download = "Wahlbogen.pdf";
			link.target = "_blank";
			link.click();
			URL.revokeObjectURL(link.href);
		}).catch(console.error);
	}

	function regel_umgesetzt(kombi: GostJahrgangFachkombination): boolean {
		if (!abiturmanager.value || !faechermanager.value)
			return true;
		const fach1 = faechermanager.value.get(kombi.fachID1);
		const f1 = abiturmanager.value.getFachbelegungByKuerzel(fach1?.kuerzel || null)
		const fach2 = faechermanager.value.get(kombi.fachID2);
		const f2 = abiturmanager.value.getFachbelegungByKuerzel(fach2?.kuerzel || null)
		for (const hj of GostHalbjahr.values()) {
			if (kombi.gueltigInHalbjahr[hj.id]) {
				const belegung_1 = abiturmanager.value.pruefeBelegungMitKursart(f1, GostKursart.fromKuerzel(kombi.kursart1)!, hj)
				const belegung_2 = abiturmanager.value.pruefeBelegungMitKursart(f2, GostKursart.fromKuerzel(kombi.kursart1)!, hj);
				if (belegung_1 && belegung_2 && kombi.typ === GostLaufbahnplanungFachkombinationTyp.VERBOTEN.getValue()) 
					return false;
				if (kombi.typ === GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() && belegung_1 != belegung_2)
					return false;
			}
		}
		return true;
	}

</script>
