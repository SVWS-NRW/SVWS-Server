<template>
	<div class="svws-ui-td" role="cell">
		<svws-ui-select v-model="fach1" title="Fach" :items="faecher" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? ''" headless />
	</div>
	<div class="svws-ui-td svws-divider" role="cell">
		<svws-ui-select v-model="kursart1" title="Kursart" :items="kursarten" :item-text="(i: GostKursart) => i.kuerzel" headless />
	</div>
	<div class="svws-ui-td" role="cell">
		&nbsp;
	</div>
	<div class="svws-ui-td" role="cell">
		<svws-ui-select v-model="fach2" title="Fach" :items="faecher" :item-text="(i: GostFach) => i.kuerzelAnzeige ?? ''" headless />
	</div>
	<div class="svws-ui-td svws-divider" role="cell">
		<svws-ui-select v-model="kursart2" title="Kursart" :items="kursarten" :item-text="(i: GostKursart) => i.kuerzel" headless />
	</div>
	<div class="svws-ui-td svws-align-center" role="cell">
		<svws-ui-checkbox v-model="gueltigEF1" headless />
	</div>
	<div class="svws-ui-td svws-align-center svws-divider" role="cell">
		<svws-ui-checkbox v-model="gueltigEF2" headless />
	</div>
	<div class="svws-ui-td svws-align-center" role="cell">
		<svws-ui-checkbox v-model="gueltigQ11" headless />
	</div>
	<div class="svws-ui-td svws-align-center svws-divider" role="cell">
		<svws-ui-checkbox v-model="gueltigQ12" headless />
	</div>
	<div class="svws-ui-td svws-align-center" role="cell">
		<svws-ui-checkbox v-model="gueltigQ21" headless />
	</div>
	<div class="svws-ui-td svws-align-center svws-divider" role="cell">
		<svws-ui-checkbox v-model="gueltigQ22" headless />
	</div>
	<div class="svws-ui-td svws-align-center" role="cell">
		<s-row-gost-fachkombination-modal v-slot="{openModal}" :hinweistext="hinweistext" :kombination="kombination" :patch-fachkombination="patchFachkombination">
			<svws-ui-tooltip position="top">
				<button role="button" class="button button--icon"><span class="icon i-ri-edit-2-line" @click="openModal" /></button>
				<template #content>"{{ kombination.hinweistext || hinweistext }}"<br><span class="opacity-50">Klicken, um den Text zu bearbeiten</span> </template>
			</svws-ui-tooltip>
		</s-row-gost-fachkombination-modal>
	</div>
	<div class="svws-ui-td svws-align-center" role="cell">
		<svws-ui-button type="trash" @click="del_fachkombi" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostJahrgangFachkombination, GostFach, GostFaecherManager, List} from "@core";
	import { GostLaufbahnplanungFachkombinationTyp, GostKursart, GostHalbjahr } from "@core";

	const props = defineProps<{
		faecherManager: () => GostFaecherManager;
		patchFachkombination: (data: Partial<GostJahrgangFachkombination>, id : number) => Promise<void>;
		removeFachkombination: (id: number) => Promise<GostJahrgangFachkombination | undefined>;
		typ: GostLaufbahnplanungFachkombinationTyp;
		kombination: GostJahrgangFachkombination;
	}>();

	const faecher = computed<List<GostFach>>(() => props.faecherManager().faecher());
	const kursarten = computed<GostKursart[]>(() => GostKursart.values());

	const fach1 = computed<GostFach | null>({
		get: () => props.faecherManager().get(props.kombination.fachID1),
		set: (value) => void props.patchFachkombination({ fachID1: value?.id }, props.kombination.id)
	});

	const kursart1 = computed<GostKursart | null>({
		get: () => GostKursart.fromKuerzel(props.kombination.kursart1),
		set: (value) => void props.patchFachkombination({ kursart1: value?.kuerzel || null }, props.kombination.id)
	});

	const fach2 = computed<GostFach | null>({
		get: () => props.faecherManager().get(props.kombination.fachID2),
		set: (value) => void props.patchFachkombination({ fachID2: value?.id }, props.kombination.id)
	});

	const kursart2 = computed<GostKursart | null>({
		get: () => GostKursart.fromKuerzel(props.kombination.kursart2),
		set: (value) => void props.patchFachkombination({ kursart2: value?.kuerzel || null }, props.kombination.id)
	});

	const gueltigEF1 = computed<boolean>({
		get: () => props.kombination.gueltigInHalbjahr[GostHalbjahr.EF1.id],
		set: (value) => {
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.EF1.id] = value;
			void props.patchFachkombination({ gueltigInHalbjahr: result }, props.kombination.id);
		}
	});

	const gueltigEF2 = computed<boolean>({
		get: () => props.kombination.gueltigInHalbjahr[GostHalbjahr.EF2.id],
		set: (value) => {
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.EF2.id] = value;
			void props.patchFachkombination({ gueltigInHalbjahr: result }, props.kombination.id);
		}
	});

	const gueltigQ11 = computed<boolean>({
		get: () => props.kombination.gueltigInHalbjahr[GostHalbjahr.Q11.id],
		set: (value) => {
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q11.id] = value;
			void props.patchFachkombination({ gueltigInHalbjahr: result }, props.kombination.id);
		}
	});

	const gueltigQ12 = computed<boolean>({
		get: () => props.kombination.gueltigInHalbjahr[GostHalbjahr.Q12.id],
		set: (value) => {
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q12.id] = value;
			void props.patchFachkombination({ gueltigInHalbjahr: result }, props.kombination.id);
		}
	});

	const gueltigQ21 = computed<boolean>({
		get: () => props.kombination.gueltigInHalbjahr[GostHalbjahr.Q21.id],
		set: (value) => {
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q21.id] = value;
			void props.patchFachkombination({ gueltigInHalbjahr: result }, props.kombination.id);
		}
	});

	const gueltigQ22 = computed<boolean>({
		get: () => props.kombination.gueltigInHalbjahr[GostHalbjahr.Q22.id],
		set: (value) => {
			const result : boolean[] = [...props.kombination.gueltigInHalbjahr];
			result[GostHalbjahr.Q22.id] = value;
			void props.patchFachkombination({ gueltigInHalbjahr: result }, props.kombination.id);
		}
	});

	const hinweistext = computed<string>(() => {
		const typ = (GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH.getValue() === props.kombination.typ)
			? 'erfordert' : 'erlaubt kein'
		return `${fach1.value?.kuerzel || ''} ${kursart1.value || ''} ${typ} ${fach2.value?.kuerzel || ''} ${kursart2.value || ''}`;
	})

	const del_fachkombi = () => {
		void props.removeFachkombination(props.kombination.id);
	}

</script>
