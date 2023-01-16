<template>
	<td> <svws-ui-multi-select v-model="fach1" title="Fach" :items="faecher" :item-text="(i: GostFach) => i.kuerzelAnzeige?.toString() || ''" headless /> </td>
	<td> <svws-ui-multi-select v-model="kursart1" title="Kursart" :items="kursarten" :item-text="(i: GostKursart) => i.kuerzel.toString()" headless /> </td>
	<td> <svws-ui-multi-select v-model="fach2" title="Fach" :items="faecher" :item-text="(i: GostFach) => i.kuerzelAnzeige?.toString() || ''" headless /> </td>
	<td> <svws-ui-multi-select v-model="kursart2" title="Kursart" :items="kursarten" :item-text="(i: GostKursart) => i.kuerzel.toString()" headless /> </td>
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
	import { List, GostJahrgangFachkombination, GostFach, GostFaecherManager, GostKursart, GostHalbjahr } from "@svws-nrw/svws-core-ts";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostFachkombinationen } from "~/apps/gost/DataGostFachkombinationen";
	import { routeGost } from "~/router/apps/RouteGost";

	const props = defineProps<{
		kombination: GostJahrgangFachkombination;
		dataFaecher: DataGostFaecher;
		dataFachkombinationen: DataGostFachkombinationen;
	}>();

	const abiturjahr = routeGost.liste.ausgewaehlt?.abiturjahr?.valueOf() || undefined;

	const fachManager: ComputedRef<GostFaecherManager | undefined> = computed(() => props.dataFaecher.manager);

	const faecher: ComputedRef<List<GostFach> | undefined> = computed(() => props.dataFaecher.daten);

	const kursarten: ComputedRef<GostKursart[]> = computed(() => GostKursart.values());

	const fach1: WritableComputedRef<GostFach | undefined> = computed({
		get(): GostFach | undefined {
			return fachManager.value?.get(props.kombination.fachID1) || undefined;
		},
		set(val: GostFach | undefined) {
			props.dataFachkombinationen.patch({ fachID1: val?.id }, props.kombination, abiturjahr);
		}
	});

	const kursart1: WritableComputedRef<GostKursart | undefined> = computed({
		get(): GostKursart | undefined {
			return GostKursart.fromKuerzel(props.kombination.kursart1) || undefined;
		},
		set(val: GostKursart | undefined) {
			props.dataFachkombinationen.patch({ kursart1: val?.kuerzel || null }, props.kombination, abiturjahr);
		}
	});

	const fach2: WritableComputedRef<GostFach | undefined> = computed({
		get(): GostFach | undefined {
			return fachManager.value?.get(props.kombination.fachID2) || undefined;
		},
		set(val: GostFach | undefined) {
			props.dataFachkombinationen.patch({ fachID2: val?.id }, props.kombination, abiturjahr);
		}
	});

	const kursart2: WritableComputedRef<GostKursart | undefined> = computed({
		get(): GostKursart | undefined {
			return GostKursart.fromKuerzel(props.kombination.kursart2) || undefined;
		},
		set(val: GostKursart | undefined) {
			props.dataFachkombinationen.patch({ kursart2: val?.kuerzel || null }, props.kombination, abiturjahr);
		}
	});

	const gueltigEF1: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kombination.gueltigInHalbjahr[GostHalbjahr.EF1.id];
		},
		set(value: boolean) {
			if (!routeGost.liste.ausgewaehlt?.abiturjahr)
				return;
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.EF1.id] = value;
			props.dataFachkombinationen.patch( { gueltigInHalbjahr: result }, props.kombination, abiturjahr);
		}
	});

	const gueltigEF2: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kombination.gueltigInHalbjahr[GostHalbjahr.EF2.id];
		},
		set(value: boolean) {
			if (!routeGost.liste.ausgewaehlt?.abiturjahr)
				return;
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.EF2.id] = value;
			props.dataFachkombinationen.patch( { gueltigInHalbjahr: result }, props.kombination,
				routeGost.liste.ausgewaehlt.abiturjahr.valueOf()
			);
		}
	});

	const gueltigQ11: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kombination.gueltigInHalbjahr[GostHalbjahr.Q11.id];
		},
		set(value: boolean) {
			if (!routeGost.liste.ausgewaehlt?.abiturjahr)
				return;
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q11.id] = value;
			props.dataFachkombinationen.patch( { gueltigInHalbjahr: result }, props.kombination,
				routeGost.liste.ausgewaehlt.abiturjahr.valueOf()
			);
		}
	});

	const gueltigQ12: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kombination.gueltigInHalbjahr[GostHalbjahr.Q12.id];
		},
		set(value: boolean) {
			if (!routeGost.liste.ausgewaehlt?.abiturjahr)
				return;
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q12.id] = value;
			props.dataFachkombinationen.patch( { gueltigInHalbjahr: result }, props.kombination,
				routeGost.liste.ausgewaehlt.abiturjahr.valueOf()
			);
		}
	});

	const gueltigQ21: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kombination.gueltigInHalbjahr[GostHalbjahr.Q21.id];
		},
		set(value: boolean) {
			if (!routeGost.liste.ausgewaehlt?.abiturjahr)
				return;
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q21.id] = value;
			props.dataFachkombinationen.patch( { gueltigInHalbjahr: result }, props.kombination,
				routeGost.liste.ausgewaehlt.abiturjahr.valueOf()
			);
		}
	});

	const gueltigQ22: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kombination.gueltigInHalbjahr[GostHalbjahr.Q22.id];
		},
		set(value: boolean) {
			if (!routeGost.liste.ausgewaehlt?.abiturjahr)
				return;
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q22.id] = value;
			props.dataFachkombinationen.patch( { gueltigInHalbjahr: result }, props.kombination,
				routeGost.liste.ausgewaehlt.abiturjahr.valueOf()
			);
		}
	});

	const del_fachkombi = () => {
		props.dataFachkombinationen.delete(props.kombination.id);
	}

</script>
