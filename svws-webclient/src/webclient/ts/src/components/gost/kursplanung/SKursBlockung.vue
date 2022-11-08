<template>
	<tr
		class="px-2 text-left"
		:style="{ 'background-color': bgColor }"
		>
		<td class="border border-[#7f7f7f]/20 px-2 whitespace-nowrap" :class="{'border-t-2': kursdifferenz}">
			<div class="flex gap-1">
				<template v-if=" kurs === edit_name ">
					{{ fachKuerzel }}-{{ art.kuerzel }}{{ kurs.nummer }}-
					<svws-ui-text-input
						v-model="suffix"
						focus headless
						style="width: 2rem"
						@blur="edit_name=undefined"
						@keyup.enter="edit_name=undefined"
					/>
				</template>
				<template v-else>
					<span class="underline decoration-dashed underline-offset-2 cursor-text" @click="edit_name = kurs">
						{{ fachKuerzel }}-{{ art.kuerzel }}{{ kurs.nummer }}{{ kurs.suffix ? "-"+kurs.suffix : "" }}
					</span>
				</template>
			</div>
		</td>
		<td class="border border-[#7f7f7f]/20 text-center" :class="{'border-t-2': kursdifferenz}">
			<svws-ui-checkbox v-model="koop"></svws-ui-checkbox>
		</td>
		<template v-if="setze_kursdifferenz && kurs_blockungsergebnis">
			<td 
			  class="border border-[#7f7f7f]/20 text-center border-t-2"
				:rowspan="kursdifferenz[0]"
			>{{kursdifferenz[1]}}</td>
		</template>
		<template v-if="!kurs_blockungsergebnis">
			<td></td>
		</template>
		<drop-data
			v-if="allow_regeln"
			v-for="(schiene) in schienen"
			:key="schiene.id"
			class="border border-[#7f7f7f]/20 text-center"
			:class="{'border-t-2': kursdifferenz, 'bg-yellow-200': is_drop_zone(schiene), 'bg-slate-500': schiene_gesperrt(schiene)}"
			tag="td"
			@drop="drop_aendere_kursschiene($event, schiene.id)"
			@drag-over="drag_over($event)"
			>
			<drag-data
				v-if="manager?.getOfKursOfSchieneIstZugeordnet(kurs.id, schiene.id)"
				:key="kurs.id"
				tag="div"
				:data="{kurs, schiene}"
				class="select-none"
				:draggable="true" 
				:class="{'bg-slate-500': schiene_gesperrt(schiene) }"
				:style="{ 'background-color': schiene_gesperrt(schiene)? '':bgColor}"
			>
				<svws-ui-badge size="tiny" class="cursor-grab" :variant="selected_kurs ? 'primary' : fixier_regeln.length ? 'error' : 'highlight'" @click="toggle_active_kurs">
					{{ kurs_blockungsergebnis?.schueler.size() }}
					<svws-ui-icon class="cursor-pointer" @click="fixieren_regel_toggle" >
						<i-ri-pushpin-fill v-if="fixier_regeln.length" class="inline-block"/>
						<i-ri-pushpin-line v-else class="inline-block"/>
					</svws-ui-icon>
				</svws-ui-badge>
			</drag-data>
			<template v-else>
				<svws-ui-icon class="cursor-pointer px-4 py-2" @click="sperren_regel_toggle(schiene.nummer)">
					<i-ri-forbid-fill v-if="sperr_regeln.find(r=>r.parameter.get(1) === schiene.nummer)" class="inline-block text-red-500" />
					<i-ri-forbid-line v-else class="inline-block opacity-0 hover:opacity-25" />
				</svws-ui-icon>
			</template>
		</drop-data>
		<!-- Es dürfen keine Regeln erstellt werden -->
		<template v-else v-for="schiene in schienen" :key="schiene.nummer">
			<td 
				class="border border-[#7f7f7f]/20 text-center"
				:class="{ 'border-t-2': kursdifferenz }"
			>
				<svws-ui-badge
					v-if="manager?.getOfKursOfSchieneIstZugeordnet(kurs.id, schiene.id)"
					size="tiny" :variant="selected_kurs?'primary':'highlight'" class="cursor-pointer"
					@click="toggle_active_kurs">
					{{ kurs_blockungsergebnis?.schueler.size() }}
				</svws-ui-badge>
			</td>
		</template>
		<template v-if="setze_kursdifferenz && kurs_blockungsergebnis && allow_regeln">
			<td
				class="border border-[#7f7f7f]/20 text-center border-t-2 whitespace-nowrap w-2"
				:rowspan="kursdifferenz[0]" :colspan="kurszahl_anzeige?2:1"
				@click="toggle_kurszahl_anzeige">
				<div
					v-if="kurszahl_anzeige"
					class="cursor-pointer rounded-lg bg-slate-100 px-2">
					<span @click="del_kurs">-</span>
					<span class="px-2">{{kursdifferenz[2]}}</span>
					<span @click="add_kurs">+</span>
				</div>
				<div v-else class="cursor-pointer underline decoration-dashed underline-offset-2">{{kursdifferenz[2]}}</div>
			</td>
		</template>
		<template v-if="!kurs_blockungsergebnis && allow_regeln">
			<td class="bg-white"></td>
		</template>
		<!-- <td class="border-none bg-white"></td> -->
	</tr>
</template>

<script setup lang="ts">
import {
	GostBlockungKurs,
	GostBlockungRegel,
	GostBlockungSchiene,
	GostBlockungsergebnisKurs,
	GostBlockungsergebnisManager,
	GostFach,
	GostKursart,
	GostKursblockungRegelTyp,
	List,
	Vector,
	ZulaessigesFach
} from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";

import { injectMainApp, Main } from "~/apps/Main";

const props = defineProps({
	kurs: {
		type: Object as () => GostBlockungKurs,
		required: true
	}
});

const art = GostKursart.fromID(props.kurs.kursart)

const main: Main = injectMainApp();
const app = main.apps.gost;

const edit_name: Ref<GostBlockungKurs | undefined> = ref(undefined)
const kurszahl_anzeige: Ref<boolean> = ref(false)

const gostFach: ComputedRef<GostFach | null> =
	computed(() => {
	let fach: GostFach | null = null
	if (!app.dataFaecher.manager) return null
	for (const f of app.dataFaecher.manager.values())
		if (f.id === props.kurs.fach_id) {
			fach = f
			break
		}
	return fach});

const fachKuerzel: ComputedRef<string> =
	computed(() => gostFach.value?.kuerzelAnzeige?.toString() || "?");

const fach: ComputedRef<ZulaessigesFach> =
	computed(() => ZulaessigesFach.getByKuerzelASD(gostFach.value?.kuerzel || null));

const bgColor: ComputedRef<string> =
	computed(() => fach.value ? fach.value.getHMTLFarbeRGB().valueOf() : "#ffffff");


const koop: WritableComputedRef<boolean> =
	computed({
	get(): boolean {
		return props.kurs.istKoopKurs.valueOf();
	},
	set(value: boolean) {
		const kurs = app.dataKursblockung.manager?.getKurs(props.kurs.id)
		if (!kurs) return
		kurs.istKoopKurs = Boolean(value);
		app.dataKursblockung.patch_kurs(kurs);
	}});
const suffix: WritableComputedRef<string> =
	computed({
	get(): string {
		return props.kurs.suffix.toString();
	},
	set(value: string) {
		const kurs = app.dataKursblockung.manager?.getKurs(props.kurs.id)
		if (!kurs) return
		kurs.suffix = String(value);
		app.dataKursblockung.patch_kurs(kurs);
	}});
const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
	computed(()=>app.dataKursblockungsergebnis.manager)

const schienen: ComputedRef<List<GostBlockungSchiene>> =
	computed(()=> app.dataKursblockung.manager?.getMengeOfSchienen() || new Vector<GostBlockungSchiene>())

const kurs_blockungsergebnis: ComputedRef<GostBlockungsergebnisKurs|undefined> =
	computed(()=>{
	try {
		return manager.value?.getKursE(props.kurs.id)
	} catch (e) { return undefined }})

const selected_kurs: ComputedRef<boolean> =
	computed(()=> kurs_blockungsergebnis.value === app.dataKursblockungsergebnis.active_kurs?.value)

const filtered_by_kursart: ComputedRef<GostBlockungsergebnisKurs[]> =
	computed(()=>{
	const kurse = manager.value?.getOfFachKursmenge(props.kurs.fach_id)
	if (!kurse) return []
	const arr = kurse.toArray(new Array<GostBlockungsergebnisKurs>())
	return arr.filter(k => k.kursart === art.id).sort((a,b)=>{
		const a_name = manager.value?.getOfKursName(a.id)
		const b_name = manager.value?.getOfKursName(b.id)
		return a_name?.localeCompare(String(b_name), "de-DE") || 0
	})})

const setze_kursdifferenz: ComputedRef<boolean> =
	computed(()=>filtered_by_kursart.value[0]===kurs_blockungsergebnis.value)

const kursdifferenz: ComputedRef<[number, number, number]> =
	computed(() => {
	if (!filtered_by_kursart.value.length) return [-1,-1, -1]
	const wahlen = filtered_by_kursart.value.reduce((previousValue, currentValue) => previousValue + currentValue.schueler.size(), 0)
	if (filtered_by_kursart.value.length === 2) return [2, Math.abs(filtered_by_kursart.value[0].schueler.size() - filtered_by_kursart.value[1].schueler.size()), wahlen]
	const sorted = [...filtered_by_kursart.value].sort((a, b) => b.schueler.size() - a.schueler.size())
	return [filtered_by_kursart.value.length, sorted[0].schueler.size() - sorted[sorted.length - 1].schueler.size(), wahlen]})

const regeln: ComputedRef<List<GostBlockungRegel>> =
	computed(()=> app.dataKursblockung.manager?.getMengeOfRegeln() || new Vector<GostBlockungRegel>())

const sperr_regeln: ComputedRef<GostBlockungRegel[]> =
	computed(() => {
	const arr = []
	for (const regel of regeln.value)
		if (regel.typ === GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ
				&& regel.parameter.get(0) === props.kurs.id)
			arr.push(regel)
	return arr})

const fixier_regeln: ComputedRef<GostBlockungRegel[]> =
	computed(() => {
	const arr = []
	for (const regel of regeln.value)
		if (regel.typ === GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ
				&& regel.parameter.get(0) === props.kurs.id)
			arr.push(regel)
	return arr})

const allow_regeln: ComputedRef<boolean> =
	computed(()=> app.blockungsergebnisauswahl.liste.length === 1)

const toggle_kurszahl_anzeige = () => kurszahl_anzeige.value = !kurszahl_anzeige.value

const is_drop_zone = (schiene: GostBlockungSchiene) => {
	const kurs = main.config.drag_and_drop_data?.kurs;
	const drag_schiene = main.config.drag_and_drop_data?.schiene;
	return kurs?.id === props.kurs.id && schiene.id !== drag_schiene.id
};

const schiene_gesperrt = (schiene: GostBlockungSchiene): boolean => {
	for (const regel of regeln.value) {
		if (regel.typ === GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ
			&& regel.parameter.get(0) !== props.kurs.kursart 
			&& (schiene.nummer >= regel.parameter.get(1) && schiene.nummer <= regel.parameter.get(2)))
			return true
		else if (regel.typ === GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ
			&& regel.parameter.get(0) === props.kurs.kursart
			&& (schiene.nummer >= regel.parameter.get(1) && schiene.nummer <= regel.parameter.get(2)))
			return true
	}
	return false
}

const regel_speichern = async (regel: GostBlockungRegel) => {
	regel.parameter.set(0, props.kurs.id)
	await app.dataKursblockung.patch_blockung_regel(regel)
}

const fixieren_regel_toggle = () => fixier_regeln.value.length ? fixieren_regel_entfernen() : fixieren_regel_hinzufuegen()
const fixieren_regel_hinzufuegen = async () => {
	const regel = await app.dataKursblockung.add_blockung_regel(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ)
	if (!regel) return
	const kurs = kurs_blockungsergebnis.value
	if (!kurs) return
	const schienen = manager.value?.getOfKursSchienenmenge(kurs.id)
	if (!schienen) return
	const schiene = manager.value?.getSchieneG([...schienen][0].id)
	if (!schiene) return
	regel.parameter.set(1, schiene.nummer)
	await regel_speichern(regel)
	app.dataKursblockung.manager?.addRegel(regel)
}
const fixieren_regel_entfernen = async () => {
	if (!fixier_regeln.value) return
	for (const regel of fixier_regeln.value)
		app.dataKursblockung.del_blockung_regel(regel.id)
}

const sperren_regel_toggle = (nummer: number) =>
	 sperr_regeln.value.find(r=>r.parameter.get(1) === nummer)
	 	? sperren_regel_entfernen(nummer)
		: sperren_regel_hinzufuegen(nummer)

const sperren_regel_hinzufuegen = async (nummer: number) => {
	const regel = await app.dataKursblockung.add_blockung_regel(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ)
	if (!regel) return
	regel.parameter.set(1, nummer)
	await regel_speichern(regel)
	app.dataKursblockung.manager?.addRegel(regel)
}
const sperren_regel_entfernen = async (nummer: number) => {
	if (!sperr_regeln.value.length) return
	const regel = sperr_regeln.value.find(r=>r.parameter.get(1) === nummer)
	if (!regel) return
	await app.dataKursblockung.del_blockung_regel(regel.id)
}

function drop_aendere_kursschiene(drag_data: {kurs: GostBlockungsergebnisKurs; schiene: GostBlockungSchiene}, schiene_id: number) {
	if (drag_data.kurs.id === kurs_blockungsergebnis.value?.id && schiene_id !== drag_data.schiene.id) {
		if (fixier_regeln.value) fixieren_regel_entfernen()
		app.dataKursblockungsergebnis.assignKursSchiene(drag_data.kurs.id, drag_data.schiene.id, schiene_id)
		manager.value?.setKursSchiene(drag_data.kurs.id, drag_data.schiene.id, false);
		manager.value?.setKursSchiene(drag_data.kurs.id, schiene_id, true);
		// fixieren_regel_hinzufuegen()
	}
}
function drag_over(event: DragEvent) {
	const transfer = event.dataTransfer;
	if (!transfer) return;
	const data = main.config.drag_and_drop_data;
	if (!data) return;
	event.preventDefault();
}

async function add_kurs() {
	await app.dataKursblockung.add_blockung_kurse(props.kurs.fach_id, props.kurs.kursart)
}
async function del_kurs() {
	await app.dataKursblockung.del_blockung_kurse(props.kurs.fach_id, props.kurs.kursart)
}

function toggle_active_kurs() {
	const filterValue = app.listAbiturjahrgangSchueler.filter;
	if (app.dataKursblockungsergebnis.active_kurs.value === kurs_blockungsergebnis.value) {
		app.dataKursblockungsergebnis.active_kurs.value = undefined
		filterValue.kurs = undefined
	} else {
		app.dataKursblockungsergebnis.active_kurs.value = kurs_blockungsergebnis.value
		const schueler: { [index: number]: boolean } = {};
		if (kurs_blockungsergebnis.value)
			for (const id of kurs_blockungsergebnis.value.schueler)
				schueler[id.valueOf()] = true;
		filterValue.kurs = schueler
	}
	app.listAbiturjahrgangSchueler.filter = filterValue;
}
</script>
