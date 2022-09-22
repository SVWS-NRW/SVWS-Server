<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";
import { GostBlockungKurs, GostBlockungRegel, GostBlockungSchiene, GostKursblockungRegelTyp, Vector } from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, Ref, ref } from "vue";

const main: Main = injectMainApp();
const app = main.apps.gost;
const manager = app.dataKursblockung.manager
const faechermanager = app.dataFaecher.manager

const regel_typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE
// public static readonly KURS_SPERRE_IN_SCHIENE : GostKursblockungRegelTyp =
// new GostKursblockungRegelTyp("KURS_SPERRE_IN_SCHIENE", 3, 3, "Kurs: Sperre in Schiene",
// Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.SCHIENEN_NR));
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
</script>
	
<template>
	<div>
		<div class="flex justify-between my-4">
			<h5 class="headline-5">{{ regel_typ.bezeichnung }}</h5>
			<svws-ui-badge v-if="!regel" size="tiny" variant="primary" @click="regel_hinzufuegen" class="cursor-pointer">Regel
				hinzufügen</svws-ui-badge>
		</div>
		<div v-for="r in regeln" :key="r.id" class="flex justify-between">
			<div class="cursor-pointer" @click="regel = (regel !== r) ? r:undefined" :class="{'bg-slate-200':r===regel}">
				{{`${faechermanager?.get(manager?.getKurs(r.parameter.get(0).valueOf())?.fach_id.valueOf()||-1)?.kuerzel}
				${kurs.kursart}${kurs.nummer}${kurs.suffix ? "-"+kurs.suffix:""}`}} auf Schiene {{r.parameter.get(1)}} gesperrt
			</div>
			<svws-ui-icon type="danger" class="cursor-pointer" @click="regel_entfernen(r)">
				<i-ri-delete-bin-2-line />
			</svws-ui-icon>
		</div>
		<div v-if="regel">
			<div class="inline-flex items-baseline">
				Sperre
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