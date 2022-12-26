<script setup lang="ts">
	import { injectMainApp, Main } from "~/apps/Main";
	import { GostBlockungRegel, GostBlockungSchiene, GostKursart, GostKursblockungRegelTyp, List, Vector } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef, shallowRef, WritableComputedRef } from "vue";

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const regel_typ = GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS
	// public static readonly KURSART_ALLEIN_IN_SCHIENEN_VON_BIS : GostKursblockungRegelTyp =
	//new GostKursblockungRegelTyp("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS", 2, 6, "Kursart: Allein in Schienen (von/bis)",
	//Arrays.asList(GostKursblockungRegelParameterTyp.KURSART, GostKursblockungRegelParameterTyp.SCHIENEN_NR, GostKursblockungRegelParameterTyp.SCHIENEN_NR));
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
		await app.dataKursblockung.add_blockung_regel(regel.value)
		regel.value = undefined
	}

	const regel_hinzufuegen = () => {
	const r = new GostBlockungRegel();
	r.typ = regel_typ.typ;
	r.parameter.add(1);
	r.parameter.add(1);
	r.parameter.add(1);
	regel.value = r;
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
		<div class="flex justify-between items-center" :class="{'mb-2' : regeln.length}">
			<h5 class="text-sm font-bold leading-loose pr-4 py-1">{{ regel_typ.bezeichnung }}</h5>
			<svws-ui-button v-if="!regel && allow_regeln" size="small" type="primary" @click="regel_hinzufuegen">Regel hinzufügen</svws-ui-button>
		</div>
		<div v-for="r in regeln" :key="r.id" class="flex justify-between">
			<div class="cursor-pointer" @click="select_regel(r)" :class="{'bg-dark-20 font-bold px-1 rounded -ml-1':r===regel}">
				{{GostKursart.fromID(r.parameter.get(0).valueOf()).beschreibung}} alleine in Schiene {{r.parameter.get(1)}} bis {{r.parameter.get(2)}}
			</div>
			<svws-ui-icon v-if="allow_regeln" type="danger" class="cursor-pointer" @click="regel_entfernen(r)">
				<i-ri-delete-bin-2-line /> </svws-ui-icon>
		</div>
		<div v-if="regel && allow_regeln" class="mt-3">
			<div class="inline-flex items-center gap-2 w-full">
				Nur
				<parameter-kursart v-model="kursart" />
				von
				<parameter-schiene v-model="start" />
				bis
				<parameter-schiene v-model="ende" />
				<svws-ui-button type="icon" class="hover--danger ml-auto" @click="regel=undefined">
					<svws-ui-icon> <i-ri-delete-bin-2-line /> </svws-ui-icon> </svws-ui-button>
				<svws-ui-button type="primary" @click="speichern">
					<svws-ui-icon> <i-ri-check-line /> </svws-ui-icon> </svws-ui-button>
			</div>
		</div>
	</div>
</template>
