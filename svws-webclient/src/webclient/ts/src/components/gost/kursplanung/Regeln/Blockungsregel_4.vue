<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";
import { GostBlockungKurs, GostBlockungRegel, GostKursart, GostKursblockungRegelTyp, List, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, Ref, ref, WritableComputedRef } from "vue";

const main: Main = injectMainApp();
const app = main.apps.gost;
const manager = app.dataKursblockung.datenmanager
const faechermanager = app.dataFaecher.manager

const regel_typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS
// public static readonly SCHUELER_FIXIEREN_IN_KURS : GostKursblockungRegelTyp =
// new GostKursblockungRegelTyp("SCHUELER_FIXIEREN_IN_KURS", 4, 4, "Schüler: Fixiere in Kurs",
// Arrays.asList(GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.KURS_ID));
const schuelerliste = app.listAbiturjahrgangSchueler.liste || []
const kurse: ComputedRef<List<GostBlockungKurs>> =
	computed(()=> app.dataKursblockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || new Vector())

const schueler: WritableComputedRef<SchuelerListeEintrag> =
	computed({
		get(): SchuelerListeEintrag {
			for (const s of schuelerliste)
				if (s.id === regel.value?.parameter.get(0))
					return s;
			return new SchuelerListeEintrag()
		},
		set(val: SchuelerListeEintrag) {
			if (regel.value)
				regel.value.parameter.set(0, val.id)	
		}
	})

const kurs: WritableComputedRef<GostBlockungKurs> = 
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
	
const regel: Ref<GostBlockungRegel | undefined> = ref(undefined)

const regeln: ComputedRef<GostBlockungRegel[]> =
	computed(()=> {
	const arr = []
	const regeln = app.dataKursblockung.datenmanager?.getMengeOfRegeln()
	if (!regeln) return []
	for (const r of regeln)
		if (r.typ === regel_typ.typ)
			arr.push(r)
	return arr })

const allow_regeln: ComputedRef<boolean> =
	computed(()=> app.blockungsergebnisauswahl.liste.length === 1)

const speichern = async () => {
	if (!regel.value) return
	await app.dataKursblockung.patch_blockung_regel(regel.value)
	regel.value = undefined
}

const regel_hinzufuegen = async () => {
	regel.value = await app.dataKursblockung.add_blockung_regel(regel_typ.typ)
}

const regel_entfernen = async (r: GostBlockungRegel|undefined) => {
	if (r === undefined)
		return;
	await app.dataKursblockung.del_blockung_regel(r.id)
	if (r === regel.value) regel.value = undefined
}

const name = (id: number) => {
	const schueler = schuelerliste.find(s => s.id === id)
	return schueler ? `${schueler.nachname}, ${schueler.vorname}` : ""
}

const kursbezeichnung = (regel: GostBlockungRegel): string => {
	const kurs = manager?.getKurs(regel.parameter.get(1).valueOf())
	if (!kurs) return ""
	const fach = faechermanager?.get(kurs.fach_id)
	if (!fach) return ""
	return `${fach.kuerzel}-${GostKursart.fromID(kurs.kursart).kuerzel}${kurs.nummer}${kurs.suffix ? '-'+kurs.suffix : ''}`
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
				{{name(r.parameter.get(0).valueOf())}} in
				{{kursbezeichnung(r)}} gesperrt
			</div>
			<svws-ui-icon v-if="allow_regeln" type="danger" class="cursor-pointer" @click="regel_entfernen(r)">
				<i-ri-delete-bin-2-line />
			</svws-ui-icon>
		</div>
		<div v-if="regel && allow_regeln">
			<div class="inline-flex items-baseline">
				Fixiere
				<parameter-schueler v-model="schueler" />
				in
				<parameter-kurs v-model="kurs" />
				<svws-ui-button type="danger" @click="regel_entfernen(regel)" class="mr-2">
					<svws-ui-icon> <i-ri-delete-bin-2-line /> </svws-ui-icon> </svws-ui-button>
				<svws-ui-button type="secondary" @click="speichern">
					<svws-ui-icon> <i-ri-check-line /> </svws-ui-icon> </svws-ui-button>
			</div>
		</div>
	</div>
</template>