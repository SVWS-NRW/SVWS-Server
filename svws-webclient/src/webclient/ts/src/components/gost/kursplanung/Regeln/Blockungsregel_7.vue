<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";
import { GostBlockungKurs, GostBlockungRegel, GostBlockungsdatenManager, GostKursblockungRegelTyp, List, Vector } from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, ShallowRef, shallowRef, WritableComputedRef } from "vue";

const main: Main = injectMainApp();
const app = main.apps.gost;
const allow_regeln: ComputedRef<boolean> = computed(()=> app.blockungsergebnisauswahl.liste.length === 1)

const regel_typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS
//public static readonly KURS_VERBIETEN_MIT_KURS : GostKursblockungRegelTyp =
//new GostKursblockungRegelTyp("KURS_VERBIETEN_MIT_KURS", 7, 7, "Kurs verbiete mit Kurs",
//Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.KURS_ID));
const manager: ComputedRef<GostBlockungsdatenManager | undefined> =
	computed(()=> app.dataKursblockung.datenmanager)

const kurse: ComputedRef<List<GostBlockungKurs>> =
	computed(()=> app.dataKursblockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || new Vector())

const kurs1: WritableComputedRef<GostBlockungKurs> = 
	computed({
		get(): GostBlockungKurs {
			for (const k of kurse.value)
				if (k.id === regel.value?.parameter.get(0))
					return k;
			return new GostBlockungKurs()
		},
		set(val: GostBlockungKurs) {
			if (regel.value)
				regel.value.parameter.set(0, val.id)	
		}
	})

const kurs2: WritableComputedRef<GostBlockungKurs> = 
	computed({
		get(): GostBlockungKurs {
			for (const k of kurse.value)
				if (k.id === regel.value?.parameter.get(1))
					return k;
			return new GostBlockungKurs()
		},
		set(val: GostBlockungKurs) {
			if (regel.value)
				regel.value.parameter.set(1, val.id)	
		}
	})
	
	
const regel: ShallowRef<GostBlockungRegel | undefined> = shallowRef(undefined)

const regeln: ComputedRef<GostBlockungRegel[]> =
	computed(()=> {
		const arr = []
		const regeln = app.dataKursblockung.datenmanager?.getMengeOfRegeln()
		if (!regeln)
			return []
		for (const r of regeln)
			if (r.typ === regel_typ.typ)
				arr.push(r)
		return arr
	})

const speichern = async () => {
	if (!regel.value)
		return
	await app.dataKursblockung.add_blockung_regel(regel.value)
	regel.value = undefined
}

const regel_hinzufuegen = () => {
	const r = new GostBlockungRegel();
	r.typ = regel_typ.typ;
	r.parameter.add(kurse.value.get(0).id)
	r.parameter.add(kurse.value.get(0).id);
	regel.value = r;
}

const regel_entfernen = async (r: GostBlockungRegel|undefined) => {
	if (r === undefined)
		return;
	await app.dataKursblockung.del_blockung_regel(r.id)
	if (r === regel.value) regel.value = undefined
}

const kursbezeichnung1 = (regel: GostBlockungRegel): String => {
	if (manager.value === undefined)
		throw new Error("Der Kursblockungsmanager ist nicht verfügbar")
	const kurs = manager.value.getKurs(regel.parameter.get(0).valueOf())
	return manager.value.getNameOfKurs(kurs.id)
}
const kursbezeichnung2 = (regel: GostBlockungRegel): String => {
	if (manager.value === undefined)
		throw new Error("Der Kursblockungsmanager ist nicht verfügbar")
	const kurs = manager.value.getKurs(regel.parameter.get(1).valueOf())
	return manager.value.getNameOfKurs(kurs.id)
}
</script>

<template>
	<div>
		<div class="flex justify-between my-4">
			<h5 class="headline-5">{{ regel_typ.bezeichnung }}</h5>
			<svws-ui-badge v-if="!regel && allow_regeln" size="tiny" variant="primary" @click="regel_hinzufuegen" class="cursor-pointer">Regel
				hinzufügen</svws-ui-badge>
		</div>
		<div v-for="r in regeln" :key="r.id" class="flex justify-between">
			<div class="cursor-pointer" @click="regel = (regel !== r) ? r:undefined" :class="{'bg-slate-200':r===regel}">
				{{kursbezeichnung1(r)}} nie zusammen mit {{kursbezeichnung2(r)}}
			</div>
			<svws-ui-icon v-if="allow_regeln" type="danger" class="cursor-pointer" @click="regel_entfernen(r)">
				<i-ri-delete-bin-2-line />
			</svws-ui-icon>
		</div>
		<div v-if="regel && allow_regeln">
			<div class="inline-flex items-baseline gap-1">
				<parameter-kurs v-model="kurs1" />
				nie zusammen mit
				<parameter-kurs v-model="kurs2" />
				<svws-ui-button type="danger" @click="regel=undefined">
					<svws-ui-icon> <i-ri-delete-bin-2-line /> </svws-ui-icon> </svws-ui-button>
				<svws-ui-button type="secondary" @click="speichern">
					<svws-ui-icon> <i-ri-check-line /> </svws-ui-icon> </svws-ui-button>
			</div>
		</div>
	</div>
</template>