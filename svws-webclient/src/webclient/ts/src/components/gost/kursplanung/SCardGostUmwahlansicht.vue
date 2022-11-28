<template>
	<svws-ui-content-card title="Schüler und Kurszuordnungen" v-if="visible">
	<div class="flex flex-row gap-4">
		<div class="flex-none">
			<div class="sticky">
				<div class="rounded-lg shadow">
					<svws-ui-checkbox v-model="filter_kollision" class="px-4">
						Nur Kollisionen ({{schueler_kollisionen}}/{{schueler?.length || 0}})
					</svws-ui-checkbox>
<!--					<svws-ui-checkbox v-model="kursfilter" class="px-4"> Kursfilter: {{aktiver_kursname}} </svws-ui-checkbox>
-->	
					<template v-if="kursfilter">
						<div>
							<svws-ui-checkbox
								v-if="kursfilter"
								v-model="filter_negiert" class="px-4"
							>
								Nicht in diesem Kurs
							</svws-ui-checkbox>
						</div>
						<div class="px-4 font-bold"> Kursansicht {{aktiver_kursname}} 
						</div>
					</template>
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
		<drop-data
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
					<div class="flex items-center justify-center bg-slate-100">
						<i-ri-delete-bin-2-line class="m-2 text-4xl" :class="{ 'text-red-700': is_dragging }" />
					</div>
					<div class="flex items-center justify-center bg-slate-100 py-2">
						<svws-ui-button size="small" @click="auto_verteilen">Automatisch verteilen</svws-ui-button>
					</div>
				</div>
			</div>
		</drop-data>
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
		GostFachwahl,
		List,
		SchuelerListeEintrag,
		Vector
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, watch, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost;
	const schueler_filter = app.listAbiturjahrgangSchueler.filter;

	const is_dragging: Ref<boolean> = ref(false)

	const visible: ComputedRef<boolean> =
		computed(()=> !!manager.value && !manager.value.getOfSchuelerAlleFachwahlenNichtZugeordnet())

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
		computed(() => app.dataKursblockung.ergebnismanager);

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

	const kursfilter: ComputedRef<boolean> = computed(() => schueler_filter.kursid !== undefined)

	const aktiver_kursname: ComputedRef<String | undefined> =
		computed(() => schueler_filter.kursid === undefined ? undefined : manager.value?.getOfKursName(schueler_filter.kursid));

	const schueler_kollisionen: ComputedRef<number> =
		computed(() => {
			if (manager.value === undefined)
				return 0;
			if (schueler_filter.kursid !== undefined)
				return manager.value.getOfKursAnzahlKollisionen(schueler_filter.kursid);
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

	const filter_kollision: WritableComputedRef<boolean> =
		computed({
		get(): boolean {
			return schueler_filter.kollisionsfilter;
		},
		set(value: boolean) {
			schueler_filter.kollisionsfilter = value;
			app.listAbiturjahrgangSchueler.filter = schueler_filter;
		}
	});

	const filter_negiert: WritableComputedRef<boolean> =
		computed({
		get(): boolean {
			return schueler_filter.kursfilter_negiert;
		},
		set(value: boolean) {
			schueler_filter.kursfilter_negiert = value;
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

	watch(()=>schueler.value, (new_val)=> selected.value = new_val ? new_val[0] : undefined)

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
