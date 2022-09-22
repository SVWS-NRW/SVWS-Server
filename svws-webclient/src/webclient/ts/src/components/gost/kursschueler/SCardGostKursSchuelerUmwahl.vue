<template>
	<div class="flex flex-row gap-4">
		<div class="test flex-none">
			<div class="sticky">
				<b>Schüler</b>
				<div class="rounded-lg shadow">
					<svws-ui-checkbox v-model="filter_kollision" class="p-5">
						Nur Kollisionen ({{Object.values(schueler_kollisionen).length}}/{{schueler?.length || 0}})
					</svws-ui-checkbox>
					<svws-ui-text-input v-model="filter_name" type="search" placeholder="Suche nach Namen">
						<i-ri-search-line />
					</svws-ui-text-input>
					<div class="max-h-screen overflow-auto">
						<table class="w-full border-collapse text-sm">
							<s-kurs-schueler-schueler
								v-for="(s, i) in schueler"
								:key="i"
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
			type="kurs"
			class="w-40 flex-none"
			@drop="drop_entferne_kurszuordnung"
			@drag-over="drag_over($event)"
			>
			<div :class="{ 'border-2 border-dashed border-red-700': active }">
				<b>Kurswahlen</b>
				<div class="overflow-hidden rounded-lg shadow">
					<table class="w-full border-collapse text-sm">
						<s-kurs-schueler-fachbelegung
							v-for="(f, i) in fachbelegungen"
							:key="i"
							:fach="f"
							:kurse="blockungsergebnisse"
							:schueler-id="selected.id"
							/>
					</table>
					<div class="flex w-full items-center justify-center bg-slate-100">
						<i-ri-delete-bin-2-line class="m-2 text-4xl" :class="{ 'text-red-700': is_dragging }" />
					</div>
				</div>
			</div>
		</drop-data>
		<div v-if="selected" class="w-full flex-none">
			<div class="">
				<b>Zuordnungen </b>
				<div class="overflow-hidden rounded-lg shadow">
					<table class="w-full border-collapse text-sm">
						<s-kurs-schueler-schiene
							v-for="(s, i) in schienen"
							:key="i"
							:schiene="s"
							:selected="selected"
							/>
					</table>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import {
	AbiturFachbelegung,
	Comparator,
	GostBlockungKurs,
	GostBlockungsergebnisKurs,
	GostBlockungsergebnisManager,
	GostBlockungsergebnisSchiene,
	SchuelerListeEintrag,
	Vector
} from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, WritableComputedRef } from "vue";

import { injectMainApp, Main } from "~/apps/Main";

const main: Main = injectMainApp();
const app = main.apps.gost;

const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
	computed(() => {
		return app.dataKursblockungsergebnis.manager;
	});

const bezeichnung: ComputedRef<string | undefined> = computed(() => {
	return app.auswahl.ausgewaehlt?.bezeichnung?.toString();
});

const is_dragging: ComputedRef<boolean> = computed(() => {
	return !!main.config.drag_and_drop_data;
});

const kurse: ComputedRef<Vector<GostBlockungKurs>> = computed(() => {
	return (
		app.dataKursblockung.daten?.kurse || new Vector<GostBlockungKurs>()
	);
});

const schienen: ComputedRef<
	Vector<GostBlockungsergebnisSchiene> | undefined
> = computed(() => app.dataKursblockungsergebnis.daten?.schienen);

const schueler: ComputedRef<Array<SchuelerListeEintrag> | undefined> =
	computed(() => {
		return app.listAbiturjahrgangSchueler.gefiltert;
	});

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
					if (ss.hatKollisionen) kolls[ss.id] = true;
		return kolls;
	});

const blockungsergebnisse: ComputedRef<
	Map<GostBlockungKurs, GostBlockungsergebnisKurs[]>
> = computed(() => {
	const v = app.dataKursblockungsergebnis.daten?.schienen
	if (!v) return new Map();
	const schienen = Array.from(v)
	const map = new Map();
	for (const k of kurse.value)
		map.set(k, schienen.map(s => Array.from(s.kurse).find(kk => k.id === kk.id)));
	return map;
});

const fachbelegungen: ComputedRef<Vector<AbiturFachbelegung> | undefined> =
	computed(() => {
		const belegungen: Vector<AbiturFachbelegung> | undefined =
			app.dataSchuelerLaufbahndaten.daten?.fachbelegungen
		if (!belegungen?.size) return undefined;
		const comp: Comparator<AbiturFachbelegung> = class {
			static compare(a: AbiturFachbelegung, b: AbiturFachbelegung): number {
				const sortA = manager.value?.getFach(a.fachID).sortierung || -1;
				const sortB = manager.value?.getFach(b.fachID).sortierung || -1;
				return sortA - sortB;
			}
		}
		belegungen.sort(comp);
		return belegungen;
	});

const filter_kollision: WritableComputedRef<boolean> = computed({
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

const filter_name: WritableComputedRef<string> = computed({
	get(): string {
		return app.listAbiturjahrgangSchueler?.filter?.name;
	},
	set(value: string) {
		const filter = app.listAbiturjahrgangSchueler.filter;
		filter.name = value;
		app.listAbiturjahrgangSchueler.filter = filter;
	}
});

function drop_entferne_kurszuordnung(kurs: any) {
	const schuelerid = selected.value?.id;
	if (!schuelerid) return;
	app.dataKursblockungsergebnis.assignSchuelerKurs(
		schuelerid,
		kurs.id,
		true
	);
}

// Zweites Argument (kurs: GostBlockungsergebnisKurs) entfernt, da in Template nicht verwendet.
function drag_over(event: DragEvent) {
	event.preventDefault();
}
</script>
