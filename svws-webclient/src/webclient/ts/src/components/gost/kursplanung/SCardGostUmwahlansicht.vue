<template>
	<svws-ui-content-card title="Schüler und Kurszuordnungen">
	<div class="flex flex-row gap-4">
		<div class="flex-none">
			<div class="sticky">
				<div class="rounded-lg shadow">
					<div class="flex justify-between w-72">
						<svws-ui-checkbox v-model="kurs_filter_toggle" class="" > Kursfilter </svws-ui-checkbox>
						<svws-ui-multi-select v-if="kurs_filter_toggle" v-model="kurs_filter" :items="kurse" headless :item-text="(kurs: GostBlockungKurs) => manager?.getOfKursName(kurs.id)" class="w-52"/>
					</div>
					<div class="flex justify-between w-72">
						<svws-ui-checkbox v-model="fach_filter_toggle" class="" > Fachfilter </svws-ui-checkbox>
						<svws-ui-multi-select v-if="fach_filter_toggle" v-model="fach_filter" :items="app.dataFaecher.daten" headless :item-text="(fach: GostFach) => fach.bezeichnung" class="w-36"/>
						<svws-ui-multi-select v-if="fach_filter_toggle" v-model="kursart_filter" :items="GostKursart.values()" headless :item-text="(kursart: GostKursart) => kursart.kuerzel" class="w-16"/>
					</div>
					<div class="pl-4">
						<svws-ui-radio-group>
							<svws-ui-radio-option v-model="radio_filter" value="alle" name="Alle" label="Alle" />
							<svws-ui-radio-option v-model="radio_filter" value="kollisionen" name="Kollisionen" label="Kollisionen" />
							<svws-ui-radio-option v-model="radio_filter" value="nichtwahlen" name="Nichtwahlen" label="Nichtwahlen" />
							<svws-ui-radio-option v-model="radio_filter" value="kollisionen_nichtwahlen" name="Kollisionen_Nichtwahlen" label="Kollisionen und Nichtwahlen" />
						</svws-ui-radio-group>
					</div>
					<svws-ui-text-input v-model="filter_name" type="search" placeholder="Suche nach Namen">
						<i-ri-search-line />
					</svws-ui-text-input>
					<div class="max-h-screen overflow-auto">
						<table class="w-full border-collapse text-sm">
							<s-kurs-schueler-schueler
								v-for="s in schueler"
								:key="s.id"
								:schueler="s"
								:selected="selected === s"
								@click="selected = s"
								/>
						</table>
					</div>
				</div>
			</div>
		</div>
		<svws-ui-drop-data
			v-if="selected"
			v-slot="{ active }"
			class="w-40 flex-none"
			@drop="drop_entferne_kurszuordnung"
			>
			<div :class="{ 'border-2 border-dashed border-red-700': active }">
				<div class="overflow-hidden rounded-lg shadow">
					<table class="w-full border-collapse text-sm">
						<s-kurs-schueler-fachbelegung
							v-for="fach in fachbelegungen"
							:key="fach.fachID"
							:fach="fach"
							:kurse="blockungsergebnisse"
							:schueler-id="selected.id"
							/>
					</table>
					<template v-if="!blockung_aktiv">
						<div class="flex items-center justify-center bg-slate-100" :class="{'bg-red-400 text-white': active}">
							<i-ri-delete-bin-2-line class="m-2 text-4xl" :class="{ 'text-red-700': is_dragging }" />
						</div>
						<div class="flex items-center justify-center bg-slate-100 py-2">
							<svws-ui-button size="small" class="m-2" @click="auto_verteilen" :disabled="pending">Automatisch verteilen</svws-ui-button>
						</div>
					</template>
				</div>
			</div>
		</svws-ui-drop-data>
		<div v-if="selected" class="flex-none">
			<div class="">
				<div class="overflow-hidden rounded-lg shadow">
					<table class="border-collapse text-sm">
						<s-kurs-schueler-schiene
							v-for="schiene in schienen"
							:key="schiene.id"
							:schiene="schiene"
							:selected="selected"
							/>
					</table>
				</div>
			</div>
		</div>
	</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import {
		GostBlockungKurs,
		GostBlockungsergebnisKurs,
		GostBlockungsergebnisManager,
		GostBlockungsergebnisSchiene,
		GostFach,
		GostFachwahl,
		GostKursart,
		List,
		SchuelerListeEintrag,
		Vector
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, onErrorCaptured, Ref, ref, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost;
	const schueler_filter = app.listAbiturjahrgangSchueler.filter;

	onErrorCaptured((e)=>{
		alert("Es ist ein Fehler aufgetreten: " + e.message);
		// return false;
	})

	const is_dragging: Ref<boolean> = ref(false)

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
		computed(() => {
			// löse ein erneutes Filtern aus, wenn der Manager sich ändert (z.B. bei Blockungs- oder -Ergebniswechsel)
			app.listAbiturjahrgangSchueler.filter = app.listAbiturjahrgangSchueler.filter;
			return app.dataKursblockung.ergebnismanager
		});

	const kurse: ComputedRef<List<GostBlockungKurs>> =
		computed(() => app.dataKursblockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer()
			|| new Vector<GostBlockungKurs>())

	const schienen: ComputedRef<List<GostBlockungsergebnisSchiene>> =
		computed(() => manager.value?.getMengeAllerSchienen() || new Vector<GostBlockungsergebnisSchiene>())

	const schueler: ComputedRef<Array<SchuelerListeEintrag> | undefined> =
		computed(() => app.listAbiturjahrgangSchueler.gefiltert);

	const selected: WritableComputedRef<SchuelerListeEintrag | undefined> =
		computed({
			get(): SchuelerListeEintrag | undefined {
				return app.listAbiturjahrgangSchueler.ausgewaehlt;
			},
			set(value: SchuelerListeEintrag | undefined) {
				if (app) {
					app.listAbiturjahrgangSchueler.ausgewaehlt = value;
				}
			}
		});

	const blockung_aktiv: ComputedRef<boolean> =
		computed(()=> app.blockungsauswahl.ausgewaehlt?.istAktiv || false);

	const aktiver_kursname: ComputedRef<String | undefined> =
		computed(() => schueler_filter.kurs === undefined ? undefined : manager.value?.getOfKursName(schueler_filter.kurs.id));

	const schueler_kollisionen: ComputedRef<number> =
		computed(() => {
			if (manager.value === undefined)
				return 0;
			if (schueler_filter.kurs?.id !== undefined)
				return manager.value.getOfKursAnzahlKollisionen(schueler_filter.kurs.id);
			return manager.value.getMengeDerSchuelerMitKollisionen().size();
		});

	const blockungsergebnisse: ComputedRef<Map<GostBlockungKurs, GostBlockungsergebnisKurs[]>> =
		computed(() => {
		const map = new Map();
		if (!schienen.value?.size()) return map;
		for (const k of kurse.value)
			for (const s of schienen.value) {
				const arr = []
				for (const kk of s.kurse)
					kk.id === k.id ? arr.push(kk) : arr.push(undefined)
				map.set(k, arr)
			}
		return map;
	});

	const fachbelegungen: ComputedRef<List<GostFachwahl>> =
		computed(() => {
		if (!selected.value?.id || !app.dataKursblockung.datenmanager) return new Vector<GostFachwahl>()
		return app.dataKursblockung.datenmanager.getOfSchuelerFacharten(selected.value.id)
	});

	const fach_filter_toggle: WritableComputedRef<boolean> =
		computed({
			get(): boolean {
				if (fach_filter.value) return true;
				return false;
			},
			set(value: boolean) {
				if (value && app.dataFaecher.daten) {
					kurs_filter_toggle.value = false;
					fach_filter.value = app.dataFaecher.daten.get(0);
					kursart_filter.value = GostKursart.GK;
				} else {
					fach_filter.value = undefined;
					kursart_filter.value = undefined;
				}
			}
		})
	const fach_filter: WritableComputedRef<GostFach | undefined> =
		computed({
			get(): GostFach | undefined {
				return schueler_filter.fach;
			},
			set(value: GostFach | undefined) {
				schueler_filter.fach = value;
				app.listAbiturjahrgangSchueler.filter = schueler_filter;
			}
		})

	const kurs_filter_toggle: WritableComputedRef<boolean> =
		computed({
			get(): boolean {
				if (kurs_filter.value) return true;
				return false;
			},
			set(value: boolean) {
				if (value && app.dataFaecher.daten) {
					kurs_filter.value = kurse.value.get(0);
					fach_filter_toggle.value = false;
				}
				else
					kurs_filter.value = undefined;
			}
		})
	const kurs_filter: WritableComputedRef<GostBlockungKurs | undefined> =
		computed({
			get(): GostBlockungKurs | undefined {
				return schueler_filter.kurs;
			},
			set(value: GostBlockungKurs | undefined) {
				schueler_filter.kurs = value;
				app.listAbiturjahrgangSchueler.filter = schueler_filter;
			}
		})

	const kursart_filter: WritableComputedRef<GostKursart | undefined> =
		computed({
			get(): GostKursart | undefined {
				return schueler_filter.kursart;
			},
			set(value: GostKursart | undefined) {
				schueler_filter.kursart = value;
				app.listAbiturjahrgangSchueler.filter = schueler_filter;
			}
		})

	const radio_filter: WritableComputedRef<string> =
		computed({
		get(): string {
			if (schueler_filter.kollisionen && schueler_filter.nichtwahlen)
				return 'kollisionen_nichtwahlen';
			if (schueler_filter.kollisionen)
				return 'kollisionen';
			if (schueler_filter.nichtwahlen)
				return 'nichtwahlen';
			else return 'alle';
		},
		set(value: string) {
			switch (value) {
				case 'alle':
					schueler_filter.kollisionen = false;
					schueler_filter.nichtwahlen = false;
					break;
				case 'kollisionen':
					schueler_filter.kollisionen = true;
					schueler_filter.nichtwahlen = false;
					break;
				case 'nichtwahlen':
					schueler_filter.kollisionen = false;
					schueler_filter.nichtwahlen = true;
					break;
				case 'kollisionen_nichtwahlen':
					schueler_filter.kollisionen = true;
					schueler_filter.nichtwahlen = true;
					break;
			}
			app.listAbiturjahrgangSchueler.filter = schueler_filter;
		}
	});

	const filter_name: WritableComputedRef<string> =
		computed({
		get(): string {
			return app.listAbiturjahrgangSchueler?.filter?.name;
		},
		set(value: string) {
			const filter = schueler_filter;
			filter.name = value;
			app.listAbiturjahrgangSchueler.filter = filter;
		}
	});

	const pending = computed(()=>app.dataKursblockungsergebnis.pending);

	// Macht Probleme beim Neuverteilen der Kurse. Schüler spring tzurück auf 1
	//watch(()=>schueler.value, (new_val)=> selected.value = new_val ? new_val[0] : undefined)

	async function drop_entferne_kurszuordnung(kurs: any) {
		const schuelerid = selected.value?.id;
		if (!schuelerid || !kurs?.id) return;
		let ok = false
		ok = await app.dataKursblockungsergebnis.removeSchuelerKurs(schuelerid, kurs.id);
		if (ok) main.config.drag_and_drop_data = undefined;
	}

	async function auto_verteilen() {
		if (selected.value === undefined)
			return;
		await app.dataKursblockungsergebnis.multiAssignSchuelerKurs(selected.value.id)
	}
</script>
