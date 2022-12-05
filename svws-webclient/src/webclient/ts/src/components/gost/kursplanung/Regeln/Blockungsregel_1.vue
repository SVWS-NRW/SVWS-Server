<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";
import { GostBlockungRegel, GostBlockungSchiene, GostKursart, GostKursblockungRegelTyp, List, Vector } from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, ShallowRef, shallowRef, WritableComputedRef } from "vue";

const main: Main = injectMainApp();
const app = main.apps.gost;

const regel_typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS
//public static readonly KURSART_SPERRE_SCHIENEN_VON_BIS : GostKursblockungRegelTyp = 
//new GostKursblockungRegelTyp("KURSART_SPERRE_SCHIENEN_VON_BIS", 1, 1, 
//"Kursart: Sperre Schienen (von/bis)", Arrays.asList(GostKursblockungRegelParameterTyp.KURSART, GostKursblockungRegelParameterTyp.SCHIENEN_NR, GostKursblockungRegelParameterTyp.SCHIENEN_NR));
const schienen: ComputedRef<List<GostBlockungSchiene>> =
	computed(()=> app.dataKursblockung.datenmanager?.getMengeOfSchienen() || new Vector())

const kursart: WritableComputedRef<GostKursart> =
	computed({
		get(): GostKursart {
			const id = regel.value?.parameter.get(0)
			if (id)
				return GostKursart.fromID(id.valueOf())
			return GostKursart.LK
		},
		set(val: GostKursart) {
			console.log(regel.value?.parameter.get(0))
			if (regel.value)
				regel.value.parameter.set(0, val.id)	
		}
	})

const start: WritableComputedRef<GostBlockungSchiene> =
	computed({
		get(): GostBlockungSchiene {
			for (const schiene of schienen.value)
				if (schiene.nummer === regel.value?.parameter.get(1))
					return schiene;
			return new GostBlockungSchiene()
		},
		set(val: GostBlockungSchiene) {
			if (regel.value)
				regel.value.parameter.set(1, val.nummer)	
		}
	})

const ende: WritableComputedRef<GostBlockungSchiene> =
	computed({
		get(): GostBlockungSchiene {
			for (const schiene of schienen.value)
				if (schiene.nummer === regel.value?.parameter.get(2))
					return schiene;
			return new GostBlockungSchiene()
		},
		set(val: GostBlockungSchiene) {
			if (regel.value)
				regel.value.parameter.set(2, val.nummer)	
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
		return arr;
	})

const allow_regeln: ComputedRef<boolean> =
	computed(()=> app.blockungsergebnisauswahl.liste.length === 1)

const speichern = async () => {
	if (!regel.value)
		return
	await app.dataKursblockung.patch_blockung_regel(regel.value)
	regel.value = undefined
}

const regel_hinzufuegen = async () => {
	await app.dataKursblockung.add_blockung_regel(regel_typ.typ)
}

const regel_entfernen = async (r: GostBlockungRegel|undefined) => {
	if (r === undefined)
		return;
	await app.dataKursblockung.del_blockung_regel(r.id)
	if (r === regel.value) regel.value = undefined
}

function select_regel(r: GostBlockungRegel) {
	if (regel.value === r) regel.value = undefined;
	else regel.value = r;
}
</script>

<template>
	<div>
		<div class="flex justify-between my-4">
			<h5 class="headline-5">{{ regel_typ.bezeichnung }}</h5>
			<svws-ui-badge v-if="!regel && allow_regeln" size="tiny" variant="primary" @click="regel_hinzufuegen" class="cursor-pointer">Regel hinzufügen</svws-ui-badge>
		</div>
		<div v-for="r in regeln" :key="r.id" class="flex justify-between">
			<div class="cursor-pointer" @click="select_regel(r)" :class="{'bg-slate-200':r===regel}">
				{{GostKursart.fromID(r.parameter.get(0).valueOf()).beschreibung}}, von Schiene {{r.parameter.get(1)}} bis {{r.parameter.get(2)}} gesperrt
			</div>
			<svws-ui-icon v-if="allow_regeln" type="danger" class="cursor-pointer" @click="regel_entfernen(r)">
					<i-ri-delete-bin-2-line /></svws-ui-icon>
		</div>
		<div v-if="regel && allow_regeln">
			<div class="inline-flex items-baseline gap-1">
				Sperre
				<parameter-kursart v-model="kursart" />
				von
				<parameter-schiene v-model="start" />
				bis
				<parameter-schiene v-model="ende" />
				<svws-ui-button type="danger" @click="regel_entfernen(regel)">
					<svws-ui-icon> <i-ri-delete-bin-2-line /> </svws-ui-icon> </svws-ui-button>
				<svws-ui-button type="secondary" @click="speichern">
					<svws-ui-icon> <i-ri-check-line /> </svws-ui-icon> </svws-ui-button>
			</div>
		</div>
	</div>
</template>