<template>
	<div role="cell" class="data-table__td data-table__td__no-padding">
		<svws-ui-multi-select v-model="fach1" title="Fach" :items="faecher" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? ''" headless />
	</div>
	<div role="cell" class="data-table__td data-table__td__no-padding data-table__td__separate">
		<svws-ui-multi-select v-model="kursart1" title="Kursart" :items="kursarten" :item-text="(i: GostKursart) => i.kuerzel" headless />
	</div>
	<div role="cell" class="data-table__td data-table__td__no-padding">
		<svws-ui-multi-select v-model="fach2" title="Fach" :items="faecher" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? ''" headless />
	</div>
	<div role="cell" class="data-table__td data-table__td__no-padding data-table__td__separate">
		<svws-ui-multi-select v-model="kursart2" title="Kursart" :items="kursarten" :item-text="(i: GostKursart) => i.kuerzel" headless />
	</div>
	<div role="cell" class="data-table__td  data-table__td__align-center data-table__td__separate">
		<s-row-gost-fachkombination-modal v-slot="{openModal}" :hinweistext="hinweistext" :kombination="kombination" :patch-fachkombination="patchFachkombination">
			<svws-ui-tooltip position="top">
				<i-ri-message-2-line @click="openModal" />
				<template #content> {{ kombination.hinweistext || hinweistext }} </template>
			</svws-ui-tooltip>
		</s-row-gost-fachkombination-modal>
	</div>
	<div role="cell" class="data-table__td data-table__td__align-center">
		<svws-ui-checkbox circle v-model="gueltigEF1" headless />
	</div>
	<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate">
		<svws-ui-checkbox circle v-model="gueltigEF2" headless />
	</div>
	<div role="cell" class="data-table__td data-table__td__align-center">
		<svws-ui-checkbox circle v-model="gueltigQ11" headless />
	</div>
	<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate">
		<svws-ui-checkbox circle v-model="gueltigQ12" headless />
	</div>
	<div role="cell" class="data-table__td data-table__td__align-center">
		<svws-ui-checkbox circle v-model="gueltigQ21" headless />
	</div>
	<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate">
		<svws-ui-checkbox circle v-model="gueltigQ22" headless />
	</div>
	<div role="cell" class="data-table__td data-table__td__align-center">
		<svws-ui-button type="trash" @click="del_fachkombi" />
	</div>
</template>

<script setup lang="ts">

	import type { GostJahrgangFachkombination, GostFach, GostFaecherManager, LinkedCollection} from "@svws-nrw/svws-core";
	import type { ComputedRef, WritableComputedRef } from "vue";
	import { computed } from "vue";
	import { GostLaufbahnplanungFachkombinationTyp } from "@svws-nrw/svws-core";
	import { GostKursart, GostHalbjahr } from "@svws-nrw/svws-core";

	const props = defineProps<{
		faecherManager: () => GostFaecherManager;
		patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<boolean>;
		removeFachkombination: (id: number) => Promise<GostJahrgangFachkombination | undefined>;
		kombination: GostJahrgangFachkombination;
	}>();

	const faecher: ComputedRef<LinkedCollection<GostFach>> = computed(() => props.faecherManager().faecher());
	const kursarten: ComputedRef<GostKursart[]> = computed(() => GostKursart.values());

	const fach1: WritableComputedRef<GostFach | undefined> = computed({
		get: () => props.faecherManager().get(props.kombination.fachID1) || undefined,
		set: (value) => void props.patchFachkombination({ fachID1: value?.id }, props.kombination.id)
	});

	const kursart1: WritableComputedRef<GostKursart | undefined> = computed({
		get: () => GostKursart.fromKuerzel(props.kombination.kursart1) || undefined,
		set: (value) => void props.patchFachkombination({ kursart1: value?.kuerzel || null }, props.kombination.id)
	});

	const fach2: WritableComputedRef<GostFach | undefined> = computed({
		get: () => props.faecherManager().get(props.kombination.fachID2) || undefined,
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

	const hinweistext: ComputedRef<string> = computed(() => {
		const typ = (GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() === props.kombination.typ)
			? 'erfordert' : 'erlaubt kein'
		return `${fach1.value?.kuerzel || ''} ${kursart1.value || ''} ${typ} ${fach2.value?.kuerzel || ''} ${kursart2.value || ''}`;
	})

	const del_fachkombi = () => {
		void props.removeFachkombination(props.kombination.id);
	}

</script>
