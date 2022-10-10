<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";
import { GostBlockungKurs, GostBlockungRegel, GostBlockungSchiene, GostKursart, GostKursblockungRegelTyp, Vector } from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, Ref, ref } from "vue";

const main: Main = injectMainApp();
const app = main.apps.gost;
const allow_regeln: ComputedRef<boolean> = computed(()=> app.blockungsergebnisauswahl.liste.length === 1)
const manager = app.dataKursblockung.manager
const faechermanager = app.dataFaecher.manager

const regel_typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE
// public static readonly KURS_FIXIERE_IN_SCHIENE : GostKursblockungRegelTyp = 
// new GostKursblockungRegelTyp("KURS_FIXIERE_IN_SCHIENE", 2, 2, "Kurs: Fixiere in Schiene", 
//Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.SCHIENEN_NR));
const kurse = app.dataKursblockung.daten?.kurse || new Vector<GostBlockungKurs>()
const schienen = app.dataKursblockung.daten?.schienen || new Vector<GostBlockungSchiene>()

const kurs: Ref<GostBlockungKurs> = ref(kurse.get(0))
const schiene: Ref<GostBlockungSchiene> = ref(schienen.get(0))
const regeln: ComputedRef<GostBlockungRegel[]> = computed(() => {
	const arr = []
	if (!app.dataKursblockung.daten?.regeln) return []
	for (const r of app.dataKursblockung.daten.regeln) if (r.typ === regel_typ.typ) arr.push(r)
	return arr
})

const regel: Ref<GostBlockungRegel | undefined> = ref(undefined)
const speichern = async () => {
	if (!regel.value) return
	regel.value.parameter.set(0, kurs.value.id)
	regel.value.parameter.set(1, schiene.value.nummer)
	await app.dataKursblockung.patch_blockung_regel(regel.value)
	regel.value = undefined
}

const regel_hinzufuegen = async () => {
	regel.value = await app.dataKursblockung.add_blockung_regel(regel_typ.typ)
	if (!regel.value) return
	app.dataKursblockung.manager?.addRegel(regel.value)
}

const regel_entfernen = async (r: GostBlockungRegel) => {
	await app.dataKursblockung.del_blockung_regel(r.id)
	if (r === regel.value) regel.value = undefined
}

const kursbezeichnung = (regel: GostBlockungRegel): string => {
	const kurs = manager?.getKurs(regel.parameter.get(0).valueOf())
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
				{{kursbezeichnung(r)}} auf Schiene {{r.parameter.get(1)}} fixiert
			</div>
			<svws-ui-icon v-if="allow_regeln" type="danger" class="cursor-pointer" @click="regel_entfernen(r)">
				<i-ri-delete-bin-2-line />
			</svws-ui-icon>
		</div>
		<div v-if="regel && allow_regeln">
			<div class="inline-flex items-baseline">
				Fixiere
				<parameter-kurs v-model="kurs" />
				in
				<parameter-schiene v-model="schiene" />
				<svws-ui-button type="primary" @click="speichern">
					<svws-ui-icon>
						<i-ri-check-line />
					</svws-ui-icon>
				</svws-ui-button>
			</div>
		</div>
	</div>
</template>