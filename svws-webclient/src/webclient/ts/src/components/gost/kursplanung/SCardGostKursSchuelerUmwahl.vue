<template>
	<svws-ui-content-card title="Schüler und Kurszuordnungen" v-if="visible">
	<div class="flex flex-row gap-4">
		<div class="flex-none">
			<div class="sticky">
				<div class="rounded-lg shadow">
					<svws-ui-checkbox v-model="filter_kollision" class="px-4">
						Nur Kollisionen ({{Object.values(schueler_kollisionen).length}}/{{schueler?.length || 0}})
					</svws-ui-checkbox>
					<div>
						<svws-ui-checkbox
							v-if="app.dataKursblockungsergebnis.active_kurs.value"
							v-model="filter_negiert" class="px-4">
							Nicht in diesem Kurs</svws-ui-checkbox>
					</div>
					<div v-if="app.dataKursblockungsergebnis.active_kurs.value" class="px-4">
						<b>Kursansicht {{app.dataKursblockungsergebnis.active_kurs.value.fachID}}</b>
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
								:kollision="!!schueler_kollisionen[s.id]"
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
			@drag-over="drag_over($event)"
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
import { computed, ComputedRef, watch, WritableComputedRef } from "vue";

import { injectMainApp, Main } from "~/apps/Main";

const main: Main = injectMainApp();
const app = main.apps.gost;

const visible: ComputedRef<boolean> =
	computed(()=> !!manager.value && !manager.value.getOfSchuelerAlleFachwahlenNichtZugeordnet())

const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
	computed(() => app.dataKursblockungsergebnis.manager);

const is_dragging: ComputedRef<boolean> =
	computed(() => !!main.config.drag_and_drop_data);

const kurse: ComputedRef<List<GostBlockungKurs>> =
	computed(() => app.dataKursblockung.manager?.getKursmengeSortiertNachKursartFachNummer()
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

const schueler_kollisionen: ComputedRef<{ [index: number]: boolean }> =
	computed(() => {
		if (!schienen.value) return {};
		const kolls: { [index: number]: boolean } = {};
		for (const s of schienen.value)
			for (const k of s.kurse)
				for (const ss of k.schueler)
					if (manager.value?.getOfSchuelerHatKollision(ss.valueOf())) kolls[ss.valueOf()] = true;
		return kolls;
	});

const schueler_negiert: ComputedRef<{ [index: number]: boolean }> =
	computed(() => {
	const kurs = app.dataKursblockungsergebnis.active_kurs?.value
	if (!kurs) return {};
	const kurse = manager.value?.getOfFachKursmenge(kurs.fachID)
	if (!kurse) return {}
	const negiert: { [index: number]: boolean } = {};
	for (const k of kurse)
		if (kurs !== k && kurs.kursart === k.kursart)
			for (const ss of k.schueler)
				negiert[ss.valueOf()] = true;
	return negiert;
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
	if (!selected.value?.id || !app.dataKursblockung.manager) return new Vector<GostFachwahl>()
	return app.dataKursblockung.manager.getOfSchuelerFacharten(selected.value.id)
});

const filter_kollision: WritableComputedRef<boolean> =
	computed({
	get(): boolean {
		return !!app.listAbiturjahrgangSchueler.filter.kollision;
	},
	set(value: boolean) {
		const filterValue = app.listAbiturjahrgangSchueler.filter;
		filterValue.kollision = value
			? schueler_kollisionen.value
			: undefined;
		app.listAbiturjahrgangSchueler.filter = filterValue;
	}
});

const filter_negiert: WritableComputedRef<boolean> =
	computed({
	get(): boolean {
		return !!app.listAbiturjahrgangSchueler.filter.negiert;
	},
	set(value: boolean) {
		const filterValue = app.listAbiturjahrgangSchueler.filter;
		filterValue.negiert = value
			? schueler_negiert.value
			: undefined;
		app.listAbiturjahrgangSchueler.filter = filterValue;
	}
});

const filter_name: WritableComputedRef<string> =
	computed({
	get(): string {
		return app.listAbiturjahrgangSchueler?.filter?.name;
	},
	set(value: string) {
		const filter = app.listAbiturjahrgangSchueler.filter;
		filter.name = value;
		app.listAbiturjahrgangSchueler.filter = filter;
	}
});

watch(()=>schueler.value, (new_val)=> selected.value = new_val ? new_val[0] : undefined)

function drop_entferne_kurszuordnung(kurs: any) {
	const schuelerid = selected.value?.id;
	if (!schuelerid || !kurs?.id) return;
	app.dataKursblockungsergebnis.assignSchuelerKurs(
		schuelerid,
		kurs.id,
		true
	);
	manager.value?.setSchuelerKurs(schuelerid, kurs.id, true);
}

// Zweites Argument (kurs: GostBlockungsergebnisKurs) entfernt, da in Template nicht verwendet.
function drag_over(event: DragEvent) {
	event.preventDefault();
}
</script>
