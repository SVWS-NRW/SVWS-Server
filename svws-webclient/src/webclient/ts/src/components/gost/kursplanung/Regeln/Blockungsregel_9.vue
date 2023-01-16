<template>
	<div>
		<div class="flex justify-between items-center" :class="{'mb-2' : lehrer_regel}">
			<h5 class="text-sm font-bold leading-loose pr-4 py-1">{{ regel_typ.bezeichnung }}</h5>
			<svws-ui-checkbox v-model="regel" :disabled="!allow_regeln" />
		</div>
		<div v-if="lehrer_regel">
			<svws-ui-radio-group>
				<svws-ui-radio-option v-model="extern" :value="false" name="interne" label="externe Lehrkräfte nicht beachten" />
				<svws-ui-radio-option v-model="extern" :value="true" name="externe" label="alle Lehrkräfte beachten" />
			</svws-ui-radio-group>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref, watch, WritableComputedRef } from "vue";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";

	const props = defineProps<{
		blockung: DataGostKursblockung;
	}>();

	const allow_regeln: ComputedRef<boolean> = computed(() => props.blockung.daten?.ergebnisse.size() === 1)

	const regel_typ = GostKursblockungRegelTyp.LEHRKRAFT_BEACHTEN
	//public static readonly LEHRKRAFT_BEACHTEN : GostKursblockungRegelTyp =
	//new GostKursblockungRegelTyp("LEHRKRAFT_BEACHTEN", 9, 9, "Lehrkräfte beachten (auch Externe?)",
	//Arrays.asList(GostKursblockungRegelParameterTyp.BOOLEAN));

	const extern: Ref<boolean> = ref(true);

	watch(()=> extern.value, value => {
		const r = lehrer_regel.value;
		if (!r)
			return;
		r.parameter.set(0, value ? 1 : 0);
		props.blockung.patch_blockung_regel(r)
	})

	const lehrer_regel: ComputedRef<GostBlockungRegel | undefined> = computed(()=> {
		const regeln = props.blockung.datenmanager?.getMengeOfRegeln()
		if (!regeln)
			return undefined;
		for (const r of regeln)
			if (r.typ === regel_typ.typ)
				return r;
		return undefined;
	})

	const regel: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return lehrer_regel.value ? true : false;
		},
		set(val: boolean) {
			if (val === true) {
				const r = new GostBlockungRegel();
				r.typ = regel_typ.typ;
				r.parameter.add(extern.value ? 1 : 0);
				props.blockung.add_blockung_regel(r);
			} else {
				const r = lehrer_regel.value;
				if (r)
					props.blockung.del_blockung_regel(r.id);
			}
		}
	})

</script>

