<template>
	<td> <svws-ui-multi-select v-model="fach1" title="Fach" :items="faecher" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? ''" headless /> </td>
	<td> <svws-ui-multi-select v-model="kursart1" title="Kursart" :items="kursarten" :item-text="(i: GostKursart) => i.kuerzel" headless /> </td>
	<td> <svws-ui-multi-select v-model="fach2" title="Fach" :items="faecher" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? ''" headless /> </td>
	<td> <svws-ui-multi-select v-model="kursart2" title="Kursart" :items="kursarten" :item-text="(i: GostKursart) => i.kuerzel" headless /> </td>
	<td> <svws-ui-checkbox v-model="gueltigEF1" headless /> </td>
	<td> <svws-ui-checkbox v-model="gueltigEF2" headless /> </td>
	<td> <svws-ui-checkbox v-model="gueltigQ11" headless /> </td>
	<td> <svws-ui-checkbox v-model="gueltigQ12" headless /> </td>
	<td> <svws-ui-checkbox v-model="gueltigQ21" headless /> </td>
	<td> <svws-ui-checkbox v-model="gueltigQ22" headless /> </td>
	<td> <svws-ui-icon class="cursor-pointer text-red-400" @click="del_fachkombi"><i-ri-delete-bin-2-line /></svws-ui-icon> </td>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { GostJahrgangFachkombination, GostFach, GostFaecherManager, GostKursart, GostHalbjahr, LinkedCollection } from "@svws-nrw/svws-core-ts";

	const props = defineProps<{
		getFaecherManager: () => GostFaecherManager;
		patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<boolean>;
		removeFachkombination: (id: number) => Promise<GostJahrgangFachkombination | undefined>;
		kombination: GostJahrgangFachkombination;
	}>();

	const faecher: ComputedRef<LinkedCollection<GostFach> | undefined> = computed(() => props.getFaecherManager().faecher());
	const kursarten: ComputedRef<GostKursart[]> = computed(() => GostKursart.values());

	const fach1: WritableComputedRef<GostFach | undefined> = computed({
		get: () => props.getFaecherManager().get(props.kombination.fachID1) || undefined,
		set: (value) => void props.patchFachkombination({ fachID1: value?.id }, props.kombination.id)
	});

	const kursart1: WritableComputedRef<GostKursart | undefined> = computed({
		get: () => GostKursart.fromKuerzel(props.kombination.kursart1) || undefined,
		set: (value) => void props.patchFachkombination({ kursart1: value?.kuerzel || null }, props.kombination.id)
	});

	const fach2: WritableComputedRef<GostFach | undefined> = computed({
		get: () => props.getFaecherManager().get(props.kombination.fachID2) || undefined,
		set: (value) => void props.patchFachkombination({ fachID2: value?.id }, props.kombination.id)
	});

	const kursart2: WritableComputedRef<GostKursart | undefined> = computed({
		get: () => GostKursart.fromKuerzel(props.kombination.kursart2) || undefined,
		set: (value) => void props.patchFachkombination({ kursart2: value?.kuerzel || null }, props.kombination.id)
	});

	const gueltigEF1: WritableComputedRef<boolean> = computed({
		get: () => props.kombination.gueltigInHalbjahr[GostHalbjahr.EF1.id],
		set: (value) => {
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.EF1.id] = value;
			void props.patchFachkombination({ gueltigInHalbjahr: result }, props.kombination.id);
		}
	});

	const gueltigEF2: WritableComputedRef<boolean> = computed({
		get: () => props.kombination.gueltigInHalbjahr[GostHalbjahr.EF2.id],
		set: (value) => {
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.EF2.id] = value;
			void props.patchFachkombination({ gueltigInHalbjahr: result }, props.kombination.id);
		}
	});

	const gueltigQ11: WritableComputedRef<boolean> = computed({
		get: () => props.kombination.gueltigInHalbjahr[GostHalbjahr.Q11.id],
		set: (value) => {
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q11.id] = value;
			void props.patchFachkombination({ gueltigInHalbjahr: result }, props.kombination.id);
		}
	});

	const gueltigQ12: WritableComputedRef<boolean> = computed({
		get: () => props.kombination.gueltigInHalbjahr[GostHalbjahr.Q12.id],
		set: (value) => {
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q12.id] = value;
			void props.patchFachkombination({ gueltigInHalbjahr: result }, props.kombination.id);
		}
	});

	const gueltigQ21: WritableComputedRef<boolean> = computed({
		get: () => props.kombination.gueltigInHalbjahr[GostHalbjahr.Q21.id],
		set: (value) => {
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q21.id] = value;
			void props.patchFachkombination({ gueltigInHalbjahr: result }, props.kombination.id);
		}
	});

	const gueltigQ22: WritableComputedRef<boolean> = computed({
		get: () => props.kombination.gueltigInHalbjahr[GostHalbjahr.Q22.id],
		set: (value) => {
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q22.id] = value;
			void props.patchFachkombination({ gueltigInHalbjahr: result }, props.kombination.id);
		}
	});

	const del_fachkombi = () => {
		void props.removeFachkombination(props.kombination.id);
	}

</script>
