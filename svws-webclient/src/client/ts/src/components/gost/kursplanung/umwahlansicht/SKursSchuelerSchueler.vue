<template>
	<div role="cell" class="data-table__td">
		<div class="flex items-center justify-between gap-1 w-full">
			<span>
				{{ `${schueler.nachname}, ${schueler.vorname}` }}
			</span>
			<div class="flex items-center gap-1 cursor-pointer">
				<svws-ui-tooltip v-if="schueler.status !== 2">
					<span class="badge badge--light badge--lg badge--short">{{ SchuelerStatus.fromID(schueler.status)?.bezeichnung }}</span>
					<template #content>{{ SchuelerStatus.fromID(schueler.status)?.bezeichnung }}</template>
				</svws-ui-tooltip>
				<svws-ui-tooltip>
					<span class="badge badge--light badge--lg">{{ schueler.geschlecht }}</span>
					<template #content>{{ schueler.geschlecht }}</template>
				</svws-ui-tooltip>
				<div class="leading-none overflow-hidden w-5" style="margin-bottom: -0.1em;" :class="{ 'text-error': kollision, 'text-black': !kollision, }">
					<svws-ui-tooltip v-if="kollision && !nichtwahl">
						<i-ri-alert-line /> <template #content> Kollision </template>
					</svws-ui-tooltip>
					<svws-ui-tooltip v-else-if="!kollision && nichtwahl">
						<i-ri-forbid-2-line /> <template #content> Nichtverteilt </template>
					</svws-ui-tooltip>
					<svws-ui-tooltip v-else-if="kollision && nichtwahl" color="danger">
						<i-ri-alert-fill /> <template #content> Kollision und Nichtverteilt </template>
					</svws-ui-tooltip>
					<div v-else class="icon opacity-25"> <i-ri-check-fill /> </div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type {GostBlockungsergebnisManager, SchuelerListeEintrag} from "@core";
	import { SchuelerStatus} from "@core";
	import type {ComputedRef} from "vue";
	import { computed} from "vue";
	import type {GostKursplanungSchuelerFilter} from "../GostKursplanungSchuelerFilter";

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		schueler: SchuelerListeEintrag;
		schuelerFilter: GostKursplanungSchuelerFilter;
	}>();

	const kollision: ComputedRef<boolean> = computed(() => {
		const kursid = props.schuelerFilter.kurs.value?.id;
		if (kursid === undefined)
			return props.getErgebnismanager().getOfSchuelerHatKollision(props.schueler.id);
		return props.getErgebnismanager().getOfKursSchuelermengeMitKollisionen(kursid).contains(props.schueler.id);
	});

	const nichtwahl: ComputedRef<boolean> = computed(() => props.getErgebnismanager().getOfSchuelerHatNichtwahl(props.schueler.id));

</script>
