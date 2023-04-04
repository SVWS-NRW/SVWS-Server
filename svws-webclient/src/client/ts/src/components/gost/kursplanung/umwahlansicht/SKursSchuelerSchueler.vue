<template>
	<div role="cell" class="data-table__td">
		<div class="flex items-center gap-1">
			<span>
				{{ `${schueler.nachname}, ${schueler.vorname}` }}
			</span>
			<div class="flex items-center">
				<svws-ui-badge v-if="schueler.status !== 2" type="light" class="mr-1"> {{ SchuelerStatus.fromID(schueler.status) }} </svws-ui-badge>
				<div class="leading-none overflow-hidden w-5"
					 style="margin-bottom: -0.1em;"
					 :class="{
							'color--error': kollision,
							'color--dark': !kollision,
						}">
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
					<svws-ui-icon v-else class="opacity-25"> <i-ri-check-fill /> </svws-ui-icon>
				</div>
			</div>

		</div>
	</div>
	<tr class="cursor-pointer" :class="{ 'selected': selected }">
		<td>
			<div class="flex justify-between items-center"></div>
		</td>
	</tr>
</template>

<script setup lang="ts">

	import { GostBlockungsergebnisManager, SchuelerListeEintrag, SchuelerStatus } from "@svws-nrw/svws-core";
	import { ComputedRef, computed } from "vue";
	import { GostKursplanungSchuelerFilter } from "../GostKursplanungSchuelerFilter";

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		schueler: SchuelerListeEintrag;
		schuelerFilter: GostKursplanungSchuelerFilter;
		selected: boolean;
	}>();

	const kollision: ComputedRef<boolean> = computed(()=> {
		const kursid = props.schuelerFilter.kurs.value?.id;
		if (kursid === undefined)
			return props.getErgebnismanager().getOfSchuelerHatKollision(props.schueler.id);
		return props.getErgebnismanager().getOfKursSchuelermengeMitKollisionen(kursid).contains(props.schueler.id);
	});

	const nichtwahl: ComputedRef<boolean> = computed(() => props.getErgebnismanager().getOfSchuelerHatNichtwahl(props.schueler.id));

</script>
