<template>
	<tr v-if="setze_kursdifferenz" class="row--kursdifferenz"/>
	<tr :style="{ 'background-color': bgColor }">
		<td :class="{'cell--kursdifferenz': setze_kursdifferenz}">
			<div class="flex gap-1">
				<template v-if=" kurs === edit_name">
					{{ kursbezeichnung }}-
					<svws-ui-text-input v-model="suffix" focus headless style="width: 2rem" @blur="edit_name=undefined" @keyup.enter="edit_name=undefined" />
				</template>
				<template v-else>
					<span class="underline decoration-dashed underline-offset-2 cursor-text" @click="edit_name = kurs">
						{{ kursbezeichnung }}</span>
				</template>
			</div>
		</td>
		<td class="text-center" :class="{'cell--kursdifferenz': setze_kursdifferenz}">
			<template v-if="allow_regeln">
				<svws-ui-multi-select v-model="kurslehrer" class="w-20" autocomplete :item-filter="lehrer_filter" removable headless
					:items="main.apps.lehrer.auswahl.liste" :item-text="(l: LehrerListeEintrag)=> `${l.kuerzel}`"/>
			</template>
			<template v-else>
				{{ kurslehrer?.kuerzel }}
			</template>
		</td>
		<td class="text-center" :class="{'cell--kursdifferenz': setze_kursdifferenz}">
			<svws-ui-checkbox headless v-if="allow_regeln" v-model="koop"></svws-ui-checkbox>
			<template v-else>{{koop ? "&#x2713;" : "&#x2717;"}}</template>
		</td>
		<template v-if="setze_kursdifferenz && kurs_blockungsergebnis">
			<td class="text-center blockung--kursdifferenz" :rowspan="kursdifferenz[0] + (kursdetail_anzeige ? 1:0)" >
				{{kursdifferenz[2]}}</td>
			<td class="text-center blockung--kursdifferenz" :rowspan="kursdifferenz[0] + (kursdetail_anzeige ? 1:0)">{{kursdifferenz[1]}}</td>
		</template>
		<template v-if="!kurs_blockungsergebnis">
			<td></td> </template>
		<drop-data v-if="!blockung_aktiv"
			v-for="(schiene) in schienen"
			v-slot="{ active }"
			:key="schiene.id"
			class="text-center"
			:class="{'cell--kursdifferenz': setze_kursdifferenz, 'bg-yellow-200': drag_data.kurs?.id === kurs.id && drag_data.schiene?.id !== schiene.id, 'bg-slate-500': schiene_gesperrt(schiene)}"
			tag="td"
			@drop="drop_aendere_kursschiene($event, schiene)"
			>
			<drag-data v-if="kurs_schiene_zugeordnet(schiene)"
				:key="kurs.id"
				tag="div"
				:data="{kurs, schiene}"
				class="select-none leading-5"
				:draggable="true"
				:class="{'bg-slate-500': schiene_gesperrt(schiene)}"
				:style="{ 'background-color': schiene_gesperrt(schiene)? '':bgColor}"
				@drag-start="drag_started"
				@dragEnd="drag_ended"
			>
				<svws-ui-badge size="tiny" class="cursor-grab" :variant="selected_kurs ? 'primary' : fixier_regeln.length ? 'error' : active && drag_data?.kurs?.id !== kurs.id ? 'success' : 'highlight'" @click="toggle_active_kurs">
					{{ kurs_blockungsergebnis?.schueler.size() }}
					<svws-ui-icon class="cursor-pointer" @click="fixieren_regel_toggle" >
						<i-ri-pushpin-fill v-if="fixier_regeln.length" class="inline-block"/>
						<i-ri-pushpin-line v-if="!fixier_regeln.length && allow_regeln" class="inline-block"/> </svws-ui-icon>
				</svws-ui-badge>
			</drag-data>
			<template v-else>
				<div :class="{'bg-green-400': active && drag_data?.schiene?.id !== schiene.id && drag_data.kurs?.id === kurs.id && drag_data.schiene?.id !== schiene.id}">
					<svws-ui-icon class="cursor-pointer px-4 py-2" @click="sperren_regel_toggle(schiene)">
						<i-ri-forbid-fill v-if="sperr_regeln.find(r=>r.parameter.get(1) === ermittel_parent_schiene(schiene).nummer)" class="inline-block text-red-500" />
						<i-ri-forbid-line v-if="allow_regeln && !sperr_regeln.find(r=>r.parameter.get(1) === ermittel_parent_schiene(schiene).nummer)" class="inline-block opacity-0 hover:opacity-25" />
					</svws-ui-icon>
				</div>
			</template>
		</drop-data>
		<template v-else v-for="schiene in schienen" :key="schiene.nummer">
			<td class="text-center leading-5 select-none" :class="{ 'cell--kursdifferenz': setze_kursdifferenz }" >
				<svws-ui-badge
					v-if="kurs_schiene_zugeordnet(schiene)"
					size="tiny" :variant="selected_kurs?'primary':'highlight'" class="cursor-pointer"
					@click="toggle_active_kurs">
					{{ kurs_blockungsergebnis?.schueler.size() }}
					<svws-ui-icon v-if="fixier_regeln.length">
						<i-ri-pushpin-fill class="inline-block"/></svws-ui-icon>
					<svws-ui-icon class="px-4 py-2" v-if="sperr_regeln.find(r=>r.parameter.get(1) === ermittel_parent_schiene(schiene).nummer)">
					<i-ri-forbid-fill class="inline-block text-red-500" /> </svws-ui-icon>
				</svws-ui-badge>
			</td>
		</template>
		<template v-if="allow_regeln">
			<td class="text-center leading-5 w-2" :class="{'cell--kursdifferenz': setze_kursdifferenz}" @click="toggle_kursdetail_anzeige">
				<div v-if="kursdetail_anzeige" class="cursor-pointer">V</div>
				<div v-else class="cursor-pointer">A</div>
			</td>
		</template>
		<!-- <template v-else>
			<td class="bg-white" :class="{'cell--kursdifferenz': setze_kursdifferenz}"></td>
		</template> -->
		<!-- <td class="border-none bg-white"></td> -->
	</tr>
	<!--Wenn Kursdtails angewählt sind, erscheint die zusätzliche Zeile-->
	<tr v-if="kursdetail_anzeige">
		<td colspan="3">
			<svws-ui-button class="h-6" size="small" type="secondary" @click="toggle_zusatzkraefte_modal">Zusatzkräfte anlegen</svws-ui-button>
		</td>
		<td :colspan="1+schienen.size()">
			<div class="flex gap-1 p-2">
					Schienen
					<svws-ui-button class="h-6" size="small" type="secondary" @click="">+</svws-ui-button>
					{{ kurs.anzahlSchienen }}
					<svws-ui-button class="h-6" size="small" type="secondary" @click="">-</svws-ui-button>
					<span class="px-4">Kurse</span>
					<svws-ui-button class="h-6" size="small" type="secondary" @click="add_kurs">+</svws-ui-button>
					<svws-ui-button class="h-6" size="small" type="secondary" @click="del_kurs">-</svws-ui-button>
					<svws-ui-button class="h-6" type="secondary">Aufteilen …</svws-ui-button>
					<svws-ui-dropdown class="h-6" variant="icon" v-if="filtered_by_kursart.length>1">
						<template #dropdownButton>Zusammenlegen</template>
						<template #dropdownItems>
							<svws-ui-dropdown-item v-for="k in filtered_by_kursart.filter(k=>k.id!==kurs.id)" :key="k.id" class="px-2" @click=""> {{ get_kursbezeichnung(k.id) }} </svws-ui-dropdown-item>
						</template>
					</svws-ui-dropdown>
			</div>
		</td>
	</tr>
	<!--Modal-->
	<svws-ui-modal ref="kurs_und_kurs_modal" size="small">
		<template #modalTitle>Regel erstellen für Kurse</template>
		<template #modalDescription>
			<div class="">
				Sollen die Kurse {{get_kursbezeichnung(kurs1?.id)}} und {{get_kursbezeichnung(kurs.id)}} immer oder nie zusammen auf einer Schiene liegen?
			</div>
			<div class="flex gap-1">
				<svws-ui-button @click="toggle_kurs_und_kurs_modal">Abbrechen</svws-ui-button>
				<svws-ui-button @click="create_regel_immer">Immer</svws-ui-button>
				<svws-ui-button @click="create_regel_nie">Nie</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
	<svws-ui-modal ref="zusatzkraefte_modal" size="small">
		<template #modalTitle>Zusatzkräfte für Kurse</template>
		<template #modalDescription>
			<div class="">
				<s-kurslehrer-select :kurs="kurs"/>
			</div>
			<div class="flex gap-1">
				<svws-ui-button @click="toggle_zusatzkraefte_modal">Beende</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
import {
	GostBlockungKurs,
	GostBlockungRegel,
	GostBlockungSchiene,
	GostBlockungsergebnisKurs,
	GostBlockungsergebnisManager,
	GostBlockungsergebnisSchiene,
	GostFach,
	GostKursart,
	GostKursblockungRegelTyp,
	LehrerListeEintrag,
	List,
	Vector,
	ZulaessigesFach
} from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";
import { App } from "~/apps/BaseApp";

import { injectMainApp, Main } from "~/apps/Main";
import { lehrer_filter } from "~/helfer";

const props = defineProps({ kurs: { type: Object as () => GostBlockungKurs, required: true } });

const art = GostKursart.fromID(props.kurs.kursart)

const main: Main = injectMainApp();
const app = main.apps.gost;

const edit_name: Ref<GostBlockungKurs | undefined> = ref(undefined)
const kursdetail_anzeige: Ref<boolean> = ref(false)

const drag_data: Ref<{kurs: GostBlockungKurs|undefined; schiene: GostBlockungSchiene|undefined}> = ref({schiene: undefined, kurs: undefined})

const gostFach: ComputedRef<GostFach | null> =
	computed(() => {
		let fach: GostFach | null = null
		if (!app.dataFaecher.manager) return null
		for (const f of app.dataFaecher.manager.values())
			if (f.id === props.kurs.fach_id) {
				fach = f
				break
			}
		return fach;
	});

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
		const kurs = app.dataKursblockung.datenmanager?.getKurs(props.kurs.id)
		if (!kurs)
			return;
		app.dataKursblockung.patch_kurs(kurs.id, { istKoopKurs: Boolean(value) });
		kurs.istKoopKurs = value;
	}});

const suffix: WritableComputedRef<string> =
	computed({
	get(): string {
		return props.kurs.suffix.toString();
	},
	set(value: string) {
		const kurs = app.dataKursblockung.datenmanager?.getKurs(props.kurs.id)
		if (!kurs)
			return;
		app.dataKursblockung.patch_kurs(kurs.id, { suffix: String(value) });
	}});

const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
	computed(()=> app.dataKursblockung.ergebnismanager);

const kursbezeichnung: ComputedRef<String> =
	computed(()=> get_kursbezeichnung(props.kurs.id));

const kurslehrer: WritableComputedRef<LehrerListeEintrag|undefined> =
	computed({
		get(): LehrerListeEintrag | undefined {
			if (!app.dataKursblockung.datenmanager || !props.kurs)
				return;
			const liste = app.dataKursblockung.datenmanager.getOfKursLehrkraefteSortiert(props.kurs.id);
			if (liste.size()) {
				const lehrer = liste.get(0);
				return App.apps.lehrer.auswahl.liste.find(l=>l.id === lehrer.id);
			}
			else
				return;
		},
		set(value: LehrerListeEintrag | undefined) {
			if (!props.kurs)
				return;
			if (value !== undefined) {
				app.dataKursblockung.add_blockung_lehrer(props.kurs.id, value.id)
					.then(lehrer => {
						if (!lehrer || !app.dataKursblockung.datenmanager || !props.kurs)
							throw new Error("Fehler beim Anlegen des Kurslehrers");
						app.dataKursblockung.datenmanager.patchOfKursAddLehrkraft(props.kurs.id, lehrer);
						add_lehrer_regel();
					})
				}
			else remove_kurslehrer()
		}
	})

function remove_kurslehrer() {
	if (!app.dataKursblockung.datenmanager || !props.kurs || !kurslehrer.value)
		return;
	app.dataKursblockung.del_blockung_lehrer(props.kurs.id, kurslehrer.value.id);
	app.dataKursblockung.datenmanager.patchOfKursRemoveLehrkraft(props.kurs.id, kurslehrer.value.id);
}

const lehrer_regel: ComputedRef<GostBlockungRegel | undefined> =
	computed(()=> {
		const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN
		const regeln = app.dataKursblockung.datenmanager?.getMengeOfRegeln()
		if (!regeln)
			return undefined;
		for (const r of regeln)
			if (r.typ === regel_typ.typ)
				return r;
	})

function add_lehrer_regel() {
	if (lehrer_regel !== undefined)
		return;
	const r = new GostBlockungRegel();
	const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN
	r.typ = regel_typ.typ;
	r.parameter.add(1);
	app.dataKursblockung.add_blockung_regel(r);
}

function get_kursbezeichnung(kurs_id: number | undefined) {
	if (!kurs_id || !manager.value)
		return String();
	return manager.value.getOfKursName(kurs_id);
}

const schienen: ComputedRef<List<GostBlockungsergebnisSchiene>> =
	computed(()=> manager.value?.getMengeAllerSchienen() || new Vector<GostBlockungsergebnisSchiene>())

const kurs_blockungsergebnis: ComputedRef<GostBlockungsergebnisKurs|undefined> =
	computed(()=> manager.value?.getKursE(props.kurs.id))

const selected_kurs: ComputedRef<boolean> =
	computed(() => (kurs_blockungsergebnis.value !== undefined)
		&& (kurs_blockungsergebnis.value?.id === app.listAbiturjahrgangSchueler.filter.kurs?.id))

const filtered_by_kursart: ComputedRef<GostBlockungsergebnisKurs[]> =
	computed(()=>{
		let kurse: Vector<GostBlockungsergebnisKurs>|undefined
			kurse = manager.value?.getOfFachKursmenge(props.kurs.fach_id)
		if (!kurse) return []
		const arr = kurse.toArray(new Array<GostBlockungsergebnisKurs>())
		return arr.filter(k => k.kursart === art.id).sort((a,b)=>{
			const a_name = manager.value?.getOfKursName(a.id)
			const b_name = manager.value?.getOfKursName(b.id)
			return a_name?.localeCompare(String(b_name), "de-DE") || 0
		})
	})

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
	computed(()=> app.dataKursblockung.datenmanager?.getMengeOfRegeln() || new Vector<GostBlockungRegel>())

const sperr_regeln: ComputedRef<GostBlockungRegel[]> =
	computed(() => {
		const arr = []
		for (const regel of regeln.value)
			if (regel.typ === GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ
					&& regel.parameter.get(0) === props.kurs.id)
				arr.push(regel)
		return arr;
	})

const fixier_regeln: ComputedRef<GostBlockungRegel[]> =
	computed(() => {
		const arr = []
		for (const regel of regeln.value)
			if (regel.typ === GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ
					&& regel.parameter.get(0) === props.kurs.id)
				arr.push(regel)
		return arr;
	})

const allow_regeln: ComputedRef<boolean> =
	computed(()=> app.blockungsergebnisauswahl.liste.length === 1)

const blockung_aktiv: ComputedRef<boolean> =
	computed(()=> app.blockungsauswahl.ausgewaehlt?.istAktiv || false);

function drag_started(e: DragEvent) {
	const transfer = e.dataTransfer;
	const data = JSON.parse(transfer?.getData('text/plain') || "") as {kurs: GostBlockungKurs; schiene: GostBlockungSchiene} | undefined;
	if (!data) return;
	drag_data.value.kurs = data.kurs;
	drag_data.value.schiene = data.schiene;
}

function drag_ended() {
	drag_data.value.kurs = undefined;
	drag_data.value.schiene = undefined;
}

const schiene_gesperrt = (schiene: GostBlockungsergebnisSchiene): boolean => {
	for (const regel of regeln.value) {
		const { nummer } = ermittel_parent_schiene(schiene)
		if (regel.typ === GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ
			&& regel.parameter.get(0) !== props.kurs.kursart
			&& (nummer >= regel.parameter.get(1) && nummer <= regel.parameter.get(2)))
				return true;
		else if (regel.typ === GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ
			&& regel.parameter.get(0) === props.kurs.kursart
			&& (nummer >= regel.parameter.get(1) && nummer <= regel.parameter.get(2)))
				return true;
	}
	return false;
}

const ermittel_parent_schiene = (ergebnis_schiene: GostBlockungsergebnisSchiene): GostBlockungSchiene => {
	const schiene =	manager.value?.getSchieneG(ergebnis_schiene.id)
	if (!schiene) throw new Error("Schiene fehlt in der Definition")
	return schiene
}

const fixieren_regel_toggle = () => {
	if (app.dataKursblockung.pending || !allow_regeln.value)
		return;
	fixier_regeln.value.length ? fixieren_regel_entfernen() : fixieren_regel_hinzufuegen()
}
const fixieren_regel_hinzufuegen = async () => {
	if (app.dataKursblockung.pending || !manager.value)
		return;
	const regel = new GostBlockungRegel();
	regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
	const kurs = kurs_blockungsergebnis.value
	if (!kurs)
		return;
	const schienen = manager.value.getOfKursSchienenmenge(kurs.id)
	if (!schienen)
		return;
	const schiene = manager.value.getSchieneG([...schienen][0].id)
	if (!schiene)
		return;
	regel.parameter.add(props.kurs.id);
	regel.parameter.add(schiene.nummer);
	await app.dataKursblockung.add_blockung_regel(regel)
}

const fixieren_regel_entfernen = async () => {
	if (!fixier_regeln.value || app.dataKursblockung.pending)
		return;
	for (const regel of fixier_regeln.value)
		app.dataKursblockung.del_blockung_regel(regel.id);
}

const sperren_regel_toggle = (schiene: GostBlockungsergebnisSchiene) => {
	if (app.dataKursblockung.pending || !allow_regeln.value)
		return;
	const { nummer } = ermittel_parent_schiene(schiene);
	return sperr_regeln.value.find(r=>r.parameter.get(1) === nummer)
		? sperren_regel_entfernen(nummer)
		: sperren_regel_hinzufuegen(nummer)
}

const sperren_regel_hinzufuegen = async (nummer: number) => {
	const regel = new GostBlockungRegel();
	regel.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
	regel.parameter.add(props.kurs.id);
	regel.parameter.add(nummer);
	await app.dataKursblockung.add_blockung_regel(regel);
}

const sperren_regel_entfernen = async (nummer: number) => {
	if (!sperr_regeln.value.length)
		return
	const regel = sperr_regeln.value.find(r=>r.parameter.get(1) === nummer)
	if (!regel) return
	await app.dataKursblockung.del_blockung_regel(regel.id)
}

let kurs1: GostBlockungsergebnisKurs | undefined = undefined;
function drop_aendere_kursschiene(drag_data: {kurs: GostBlockungsergebnisKurs; schiene: GostBlockungSchiene}, schiene: GostBlockungsergebnisSchiene) {
	if (!drag_data.kurs || !drag_data.schiene || app.dataKursblockungsergebnis.pending || kurs_blockungsergebnis.value === undefined)
		return
	if (drag_data.kurs.id !== kurs_blockungsergebnis.value.id && kurs_schiene_zugeordnet(schiene)) {
		kurs1 = drag_data.kurs;
		toggle_kurs_und_kurs_modal();
		return;
	}
	if (drag_data.kurs.id === kurs_blockungsergebnis.value.id && schiene.id !== drag_data.schiene.id) {
		if (fixier_regeln.value) fixieren_regel_entfernen()
		app.dataKursblockungsergebnis.assignKursSchiene(drag_data.kurs.id, drag_data.schiene.id, schiene.id)
	}
}

async function add_kurs() {
	if (app.dataKursblockung.pending)
		return;
	await app.dataKursblockung.add_blockung_kurse(props.kurs.fach_id, props.kurs.kursart)
}

async function del_kurs() {
	if (app.dataKursblockung.pending)
		return;
	await app.dataKursblockung.del_blockung_kurse(props.kurs.fach_id, props.kurs.kursart);
}

function toggle_active_kurs() {
	const filterValue = app.listAbiturjahrgangSchueler.filter;
	if (filterValue.kurs !== props.kurs) {
		app.listAbiturjahrgangSchueler.reset_filter();
		filterValue.kurs = props.kurs;
	}
	else app.listAbiturjahrgangSchueler.reset_filter();
	app.listAbiturjahrgangSchueler.filter = filterValue;
}

function kurs_schiene_zugeordnet(schiene: GostBlockungsergebnisSchiene): boolean {
	return manager.value?.getOfKursOfSchieneIstZugeordnet(props.kurs.id, schiene.id) || false
}

const toggle_kursdetail_anzeige = () => kursdetail_anzeige.value = !kursdetail_anzeige.value

const zusatzkraefte_modal: Ref<any> = ref(null);
function toggle_zusatzkraefte_modal() {
	zusatzkraefte_modal.value.isOpen ? zusatzkraefte_modal.value.closeModal() : zusatzkraefte_modal.value.openModal();
};

const kurs_und_kurs_modal: Ref<any> = ref(null);
function toggle_kurs_und_kurs_modal() {
	kurs_und_kurs_modal.value.isOpen ? kurs_und_kurs_modal.value.closeModal() : kurs_und_kurs_modal.value.openModal();
};

async function create_regel_immer() {
	kurs_und_kurs_modal.value.closeModal();
	if (!kurs1)
		return;
	const regel = new GostBlockungRegel();
	regel.typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ;
	regel.parameter.add(kurs1.id)
	regel.parameter.add(props.kurs.id);
	await app.dataKursblockung.add_blockung_regel(regel)
}
async function create_regel_nie() {
	kurs_und_kurs_modal.value.closeModal();
	if (!kurs1)
		return;
	const regel = new GostBlockungRegel();
	regel.typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ;
	regel.parameter.add(kurs1.id)
	regel.parameter.add(props.kurs.id);
	await app.dataKursblockung.add_blockung_regel(regel)
}
</script>
