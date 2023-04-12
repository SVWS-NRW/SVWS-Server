<template>
	<div role="cell" class="data-table__td">
		<div class="flex items-center justify-between gap-1 w-full">
			<span>
				{{ `${schueler.nachname}, ${schueler.vorname}` }}
			</span>
			<div class="flex items-center gap-1 cursor-pointer">
				<svws-ui-badge v-if="schueler.status !== 2" type="light" size="big" class="normal-case" :short="true">
					{{ SchuelerStatus.fromID(schueler.status)?.bezeichnung }}
				</svws-ui-badge>
				<div class="leading-none overflow-hidden w-5" style="margin-bottom: -0.1em;" :class="{ 'text-error': kollision, 'text-dark-80': !kollision, }">
					<svws-ui-tooltip v-if="kollision && !nichtwahl">
						<i-ri-alert-line />
						<template #content>
							<span>Kollision</span>
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip v-else-if="!kollision && nichtwahl">
						<i-ri-forbid-2-line />
						<template #content>
							<span>Nichtverteilt</span>
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip v-else-if="kollision && nichtwahl" color="danger">
						<i-ri-alert-fill />
						<template #content>
							<span>Kollision und Nichtverteilt</span>
						</template>
					</svws-ui-tooltip>
					<svws-ui-icon v-else class="opacity-25">
						<i-ri-check-fill />
					</svws-ui-icon>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type {GostBlockungsergebnisManager, SchuelerListeEintrag} from "@svws-nrw/svws-core";
	import { SchuelerStatus} from "@svws-nrw/svws-core";
	import type {ComputedRef} from "vue";
	import { computed} from "vue";
	import type {GostKursplanungSchuelerFilter} from "../GostKursplanungSchuelerFilter";

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		schueler: SchuelerListeEintrag;
		schuelerFilter: GostKursplanungSchuelerFilter;
		selected: boolean;
	}>();

	const kollision: ComputedRef<boolean> = computed(() => {
		const kursid = props.schuelerFilter.kurs.value?.id;
		if (kursid === undefined)
			return props.getErgebnismanager().getOfSchuelerHatKollision(props.schueler.id);
		return props.getErgebnismanager().getOfKursSchuelermengeMitKollisionen(kursid).contains(props.schueler.id);
	});

	const nichtwahl: ComputedRef<boolean> = computed(() => props.getErgebnismanager().getOfSchuelerHatNichtwahl(props.schueler.id));

</script>
