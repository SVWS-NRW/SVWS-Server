<template>
	<td> <svws-ui-multi-select v-model="fach1" title="Fach" :items="faecher" :item-text="(i: GostFach) => i.kuerzelAnzeige" headless/> </td>
	<td> <svws-ui-multi-select v-model="kursart1" title="Kursart" :items="kursarten" :item-text="(i: GostKursart) => i.kuerzel" headless/> </td>
	<td> <svws-ui-multi-select v-model="fach2" title="Fach" :items="faecher" :item-text="(i: GostFach) => i.kuerzelAnzeige" headless/> </td>
	<td> <svws-ui-multi-select v-model="kursart2" title="Kursart" :items="kursarten" :item-text="(i: GostKursart) => i.kuerzel" headless /> </td>
	<td> <svws-ui-checkbox v-model="gueltigEF1" headless></svws-ui-checkbox> </td>
	<td> <svws-ui-checkbox v-model="gueltigEF2" headless></svws-ui-checkbox> </td>
	<td> <svws-ui-checkbox v-model="gueltigQ11" headless></svws-ui-checkbox> </td>
	<td> <svws-ui-checkbox v-model="gueltigQ12" headless></svws-ui-checkbox> </td>
	<td> <svws-ui-checkbox v-model="gueltigQ21" headless></svws-ui-checkbox> </td>
	<td> <svws-ui-checkbox v-model="gueltigQ22" headless></svws-ui-checkbox> </td>
	<td> <svws-ui-icon class="cursor-pointer text-red-400" @click="del_fachkombi"><i-ri-delete-bin-2-line /></svws-ui-icon> </td>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { List, GostJahrgangFachkombinationen, GostFach, GostFaecherManager, GostKursart, GostHalbjahr } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps({
		kombination: {
			type: Object as () => GostJahrgangFachkombinationen,
			required: true
		}
	});

	const main: Main = injectMainApp();
	const app = main.apps.gost;
	const abiturjahr = app.auswahl.ausgewaehlt?.abiturjahr?.valueOf() || undefined;

	const fachManager: ComputedRef<GostFaecherManager | undefined> = computed(
		() => { return app.dataFaecher.manager; }
	);

	const faecher: ComputedRef<List<GostFach> | undefined> = computed(
		() => { return app.dataFaecher.daten; }
	);

	const kursarten: ComputedRef<GostKursart[]> = computed(() => 
		GostKursart.values()
	);

	const fach1: WritableComputedRef<GostFach | undefined> = computed({
		get(): GostFach | undefined {
			return fachManager.value?.get(props.kombination.fachID1) || undefined;
		},
		set(val: GostFach | undefined) {
			app.dataFachkombinationen.patch({ fachID1: val?.id }, props.kombination, abiturjahr);
		}
	});

	const kursart1: WritableComputedRef<GostKursart | undefined> = computed({
		get(): GostKursart | undefined {
			return GostKursart.fromKuerzel(props.kombination.kursart1) || undefined;
		},
		set(val: GostKursart | undefined) {
			app.dataFachkombinationen.patch({ kursart1: val?.kuerzel || null }, props.kombination, abiturjahr);
		}
	});

	const fach2: WritableComputedRef<GostFach | undefined> = computed({
		get(): GostFach | undefined {
			return fachManager.value?.get(props.kombination.fachID2) || undefined;
		},
		set(val: GostFach | undefined) {
			app.dataFachkombinationen.patch({ fachID2: val?.id }, props.kombination, abiturjahr);
		}
	});

	const kursart2: WritableComputedRef<GostKursart | undefined> = computed({
		get(): GostKursart | undefined {
			return GostKursart.fromKuerzel(props.kombination.kursart2) || undefined;
		},
		set(val: GostKursart | undefined) {
			app.dataFachkombinationen.patch({ kursart2: val?.kuerzel || null }, props.kombination, abiturjahr);
		}
	});

	const gueltigEF1: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kombination.gueltigInHalbjahr[GostHalbjahr.EF1.id];
		},
		set(value: boolean) {
			if (!app.auswahl.ausgewaehlt?.abiturjahr) 
				return;
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.EF1.id] = value;
			app.dataFachkombinationen.patch( { gueltigInHalbjahr: result }, props.kombination, abiturjahr);
		}
	});

	const gueltigEF2: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kombination.gueltigInHalbjahr[GostHalbjahr.EF2.id];
		},
		set(value: boolean) {
			if (!app.auswahl.ausgewaehlt?.abiturjahr) 
				return;
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.EF2.id] = value;
			app.dataFachkombinationen.patch( { gueltigInHalbjahr: result }, props.kombination, 
				app.auswahl.ausgewaehlt.abiturjahr.valueOf() 
			);
		}
	});

	const gueltigQ11: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kombination.gueltigInHalbjahr[GostHalbjahr.Q11.id];
		},
		set(value: boolean) {
			if (!app.auswahl.ausgewaehlt?.abiturjahr) 
				return;
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q11.id] = value;
			app.dataFachkombinationen.patch( { gueltigInHalbjahr: result }, props.kombination, 
				app.auswahl.ausgewaehlt.abiturjahr.valueOf() 
			);
		}
	});

	const gueltigQ12: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kombination.gueltigInHalbjahr[GostHalbjahr.Q12.id];
		},
		set(value: boolean) {
			if (!app.auswahl.ausgewaehlt?.abiturjahr) 
				return;
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q12.id] = value;
			app.dataFachkombinationen.patch( { gueltigInHalbjahr: result }, props.kombination, 
				app.auswahl.ausgewaehlt.abiturjahr.valueOf() 
			);
		}
	});

	const gueltigQ21: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kombination.gueltigInHalbjahr[GostHalbjahr.Q21.id];
		},
		set(value: boolean) {
			if (!app.auswahl.ausgewaehlt?.abiturjahr) 
				return;
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q21.id] = value;
			app.dataFachkombinationen.patch( { gueltigInHalbjahr: result }, props.kombination, 
				app.auswahl.ausgewaehlt.abiturjahr.valueOf() 
			);
		}
	});

	const gueltigQ22: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return props.kombination.gueltigInHalbjahr[GostHalbjahr.Q22.id];
		},
		set(value: boolean) {
			if (!app.auswahl.ausgewaehlt?.abiturjahr) 
				return;
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q22.id] = value;
			app.dataFachkombinationen.patch( { gueltigInHalbjahr: result }, props.kombination, 
				app.auswahl.ausgewaehlt.abiturjahr.valueOf() 
			);
		}
	});

	const del_fachkombi = () => {
		app.dataFachkombinationen.delete(props.kombination.id);
	}

</script>
