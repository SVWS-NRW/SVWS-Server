<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";
import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core-ts";
import { computed, ComputedRef, WritableComputedRef } from "vue";

const main: Main = injectMainApp();
const app = main.apps.gost;
const allow_regeln: ComputedRef<boolean> = computed(()=> app.blockungsergebnisauswahl.liste.length === 1)

const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN
//public static readonly LEHRKRAFT_BEACHTEN : GostKursblockungRegelTyp =
//new GostKursblockungRegelTyp("LEHRKRAFT_BEACHTEN", 9, 9, "Lehrkräfte beachten (auch Externe?)",
//Arrays.asList(GostKursblockungRegelParameterTyp.BOOLEAN));

const lehrer_regel =
	computed(()=> {
		const regeln = app.dataKursblockung.datenmanager?.getMengeOfRegeln()
		if (!regeln)
			return undefined;
		for (const r of regeln)
			if (r.typ === regel_typ.typ)
				return r;
	})

	const regel: WritableComputedRef<boolean> =
	computed({
		get(): boolean {
			if (lehrer_regel.value !== undefined)
				return !!lehrer_regel.value.parameter.get(0);
			return false;
		},
		set(val: boolean) {
			if (lehrer_regel.value === undefined) {
				const r = new GostBlockungRegel();
				r.typ = regel_typ.typ;
				r.parameter.add(val);
				app.dataKursblockung.add_blockung_regel(r);
			} else {
				const r = lehrer_regel.value;
				r.parameter.set(0, val);
			  app.dataKursblockung.patch_blockung_regel(r)
			}
		}
	})
</script>

<template>
	<div>
		<div class="flex justify-between my-4">
			<h5 class="headline-5">{{ regel_typ.bezeichnung }}</h5>
				<svws-ui-checkbox v-model="regel" :disabled="!allow_regeln"/>
		</div>
	</div>
</template>