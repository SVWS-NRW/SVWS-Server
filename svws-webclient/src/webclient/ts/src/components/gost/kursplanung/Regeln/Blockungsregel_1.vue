<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";
import { GostBlockungRegel, GostBlockungSchiene, GostKursart, GostKursblockungRegelTyp, Vector } from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, Ref, ref } from "vue";

const main: Main = injectMainApp();
const app = main.apps.gost;

const regel_typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS
//public static readonly KURSART_SPERRE_SCHIENEN_VON_BIS : GostKursblockungRegelTyp = 
//new GostKursblockungRegelTyp("KURSART_SPERRE_SCHIENEN_VON_BIS", 1, 1, 
//"Kursart: Sperre Schienen (von/bis)", Arrays.asList(GostKursblockungRegelParameterTyp.KURSART, GostKursblockungRegelParameterTyp.SCHIENEN_NR, GostKursblockungRegelParameterTyp.SCHIENEN_NR));
const schienen = app.dataKursblockung.daten?.schienen || new Vector<GostBlockungSchiene>()
const kursart: Ref<GostKursart> = ref(GostKursart.GK)
const start: Ref<GostBlockungSchiene> = ref(schienen.get(0))
const ende: Ref<GostBlockungSchiene> = ref(schienen.get(0))
const regeln: ComputedRef<GostBlockungRegel[]> = computed(()=> {
	const arr = []
	if (!app.dataKursblockung.daten?.regeln) return []
	for (const r of app.dataKursblockung.daten.regeln) if (r.typ === regel_typ.typ) arr.push(r)
	return arr
})

const regel: Ref<GostBlockungRegel | undefined> = ref(undefined)
const allow_regeln: ComputedRef<boolean> = computed(()=> app.blockungsergebnisauswahl.liste.length === 1)
const speichern = async () => {
	if (!regel.value) return
	regel.value.parameter.set(0, kursart.value.id)
	regel.value.parameter.set(1, start.value.nummer)
	regel.value.parameter.set(2, ende.value.nummer)
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
			<svws-ui-badge v-if="!regel && allow_regeln" size="tiny" variant="primary" @click="regel_hinzufuegen" class="cursor-pointer">Regel hinzufügen</svws-ui-badge>
		</div>
		<div v-for="r in regeln" :key="r.id" class="flex justify-between">
			<div class="cursor-pointer" @click="regel = (regel !== r) ? r:undefined" :class="{'bg-slate-200':r===regel}">
				{{GostKursart.fromID(r.parameter.get(0).valueOf()).beschreibung}}, von Schiene {{r.parameter.get(1)}} bis {{r.parameter.get(2)}} gesperrt
			</div>
			<svws-ui-icon v-if="allow_regeln" type="danger" class="cursor-pointer" @click="regel_entfernen(r)">
					<i-ri-delete-bin-2-line />
				</svws-ui-icon>
		</div>
		<div v-if="regel && allow_regeln">
			<div class="inline-flex items-baseline">
				Sperre
				<parameter-kursart v-model="kursart" class="mx-1" />
				von
				<parameter-schiene v-model="start" class="mx-1" />bis
				<parameter-schiene v-model="ende" class="mx-1" />
				<svws-ui-button type="primary" @click="speichern">
					<svws-ui-icon>
						<i-ri-check-line />
					</svws-ui-icon>
				</svws-ui-button>
			</div>
		</div>
	</div>
</template>