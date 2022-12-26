<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";
import { GostBlockungKurs, GostBlockungRegel, GostBlockungSchiene, GostBlockungsdatenManager, GostKursblockungRegelTyp, List, Vector } from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, ShallowRef, shallowRef, WritableComputedRef } from "vue";

const main: Main = injectMainApp();
const app = main.apps.gost;
const allow_regeln: ComputedRef<boolean> = computed(()=> app.blockungsergebnisauswahl.liste.length === 1)

const regel_typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE
// public static readonly KURS_FIXIERE_IN_SCHIENE : GostKursblockungRegelTyp =
// new GostKursblockungRegelTyp("KURS_FIXIERE_IN_SCHIENE", 2, 2, "Kurs: Fixiere in Schiene",
//Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.SCHIENEN_NR));
const manager: ComputedRef<GostBlockungsdatenManager | undefined> =
	computed(()=> app.dataKursblockung.datenmanager)

const kurse: ComputedRef<List<GostBlockungKurs>> =
	computed(()=> app.dataKursblockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || new Vector())

const schienen: ComputedRef<List<GostBlockungSchiene>> =
	computed(()=> app.dataKursblockung.datenmanager?.getMengeOfSchienen() || new Vector())

const kurs: WritableComputedRef<GostBlockungKurs> =
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

	const schiene: WritableComputedRef<GostBlockungSchiene> =
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
	r.parameter.add(1);
	regel.value = r;
}

const regel_entfernen = async (r: GostBlockungRegel|undefined) => {
	if (r === undefined)
		return;
	await app.dataKursblockung.del_blockung_regel(r.id)
	if (r === regel.value) regel.value = undefined
}

const kursbezeichnung = (regel: GostBlockungRegel): String => {
	if (manager.value === undefined)
		throw new Error("Der Kursblockungsmanager ist nicht verfügbar")
	const kurs = manager.value.getKurs(regel.parameter.get(0).valueOf())
	return manager.value.getNameOfKurs(kurs.id)
}
</script>

<template>
	<div>
		<div class="flex justify-between items-center" :class="{'mb-2' : regeln.length}">
			<h5 class="text-sm font-bold leading-loose pr-4 py-1">{{ regel_typ.bezeichnung }}</h5>
			<svws-ui-button v-if="!regel && allow_regeln" size="small" type="primary" @click="regel_hinzufuegen">Regel hinzufügen</svws-ui-button>
		</div>
		<div v-for="r in regeln" :key="r.id" class="flex justify-between">
			<div class="cursor-pointer" @click="regel = (regel !== r) ? r:undefined" :class="{'bg-dark-20 font-bold px-1 rounded -ml-1':r===regel}">
				{{kursbezeichnung(r)}} auf Schiene {{r.parameter.get(1)}} fixiert
			</div>
			<svws-ui-icon v-if="allow_regeln" type="danger" class="cursor-pointer" @click="regel_entfernen(r)">
				<i-ri-delete-bin-2-line />
			</svws-ui-icon>
		</div>
		<div v-if="regel && allow_regeln" class="mt-3">
			<div class="inline-flex items-center gap-2 w-full">
				Fixiere
				<parameter-kurs v-model="kurs" />
				in
				<parameter-schiene v-model="schiene" />
				<svws-ui-button type="icon" class="hover--danger ml-auto" @click="regel=undefined">
					<svws-ui-icon> <i-ri-delete-bin-2-line /> </svws-ui-icon> </svws-ui-button>
				<svws-ui-button type="primary" @click="speichern">
					<svws-ui-icon> <i-ri-check-line /> </svws-ui-icon> </svws-ui-button>
			</div>
		</div>
	</div>
</template>
