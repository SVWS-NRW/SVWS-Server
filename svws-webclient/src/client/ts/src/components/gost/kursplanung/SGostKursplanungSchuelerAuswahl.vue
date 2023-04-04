<template>
	<svws-ui-content-card>
		<svws-ui-data-table v-model="schuelerFilter.filtered"
							v-model:clicked="selected"
							clickable
							:filter="true"
							:filter-open="true">
			<template #search>
				<svws-ui-text-input type="search" v-model="schuelerFilter.name.value" placeholder="Suche" />
			</template>
			<template #filter>
				<div class="flex justify-between items-center">
					<svws-ui-checkbox v-model="kurs_filter_toggle" class="">Kursfilter<span v-if="kurs_filter_toggle">:</span></svws-ui-checkbox>
					<svws-ui-multi-select v-if="kurs_filter_toggle" v-model="schuelerFilter.kurs.value" :items="schuelerFilter.getKurse()" headless
										  :item-text="(kurs: GostBlockungKurs) => getErgebnismanager().getOfKursName(kurs.id) ?? ''" class="w-48" />
				</div>
				<div class="flex justify-between items-center mb-3">
					<svws-ui-checkbox v-model="fach_filter_toggle" class=""> Fachfilter<span v-if="fach_filter_toggle">:</span></svws-ui-checkbox>
					<svws-ui-multi-select v-if="fach_filter_toggle" v-model="fach" :items="faecherManager.toVector()" headless :item-text="(fach: GostFach) => fach.bezeichnung ?? ''" class="w-32" />
					<svws-ui-multi-select v-if="fach_filter_toggle" v-model="schuelerFilter.kursart.value" :items="GostKursart.values()" headless :item-text="(kursart: GostKursart) => kursart.kuerzel" class="w-16" />
				</div>
				<div class="mb-3">
					<svws-ui-radio-group class="radio--row">
						<svws-ui-radio-option v-model="radio_filter" value="alle" name="Alle" label="Alle" :icon="false" />
						<svws-ui-radio-option v-model="radio_filter" value="kollisionen" name="Kollisionen" label="Kollisionen">
							<i-ri-alert-line />
						</svws-ui-radio-option>
						<svws-ui-radio-option v-model="radio_filter" value="nichtwahlen" name="Nichtwahlen" label="Nichtverteilt">
							<i-ri-forbid-2-line />
						</svws-ui-radio-option>
						<svws-ui-radio-option v-model="radio_filter" value="kollisionen_nichtwahlen" name="Kollisionen_Nichtwahlen" label="K/N">
							<i-ri-alert-fill />
						</svws-ui-radio-option>
					</svws-ui-radio-group>
				</div>
			</template>
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr">
					<div role="columnheader"
						 class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<span class="data-table__th-title">
								Schülerauswahl
							</span>
						</div>
					</div>
				</div>
			</template>
			<template #body>
				<div role="row"
					 class="data-table__tr data-table__tbody__tr"
					 v-for="(s, index) in schuelerFilter.filtered.value.values()"
					 :key="index">
					<s-kurs-schueler-schueler :schueler="s" :selected="selected === s" @click="selected = s"
											  :get-ergebnismanager="getErgebnismanager" :schueler-filter="schuelerFilter" />
				</div>
				<tr v-if="!schuelerFilter.filtered.value.size">
					<td class="opacity-50 text-sm">Keine Schüler zu diesem Filter gefunden.</td>
				</tr>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostFach, GostKursart, SchuelerListeEintrag } from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef } from "vue";
	import { KursplanungSchuelerAuswahlProps } from "./SGostKursplanungSchuelerAuswahlProps";

	const props = defineProps<KursplanungSchuelerAuswahlProps>();

	const kurs_filter_toggle = props.schuelerFilter.kurs_filter_toggle();
	const fach_filter_toggle = props.schuelerFilter.fach_filter_toggle();
	const radio_filter = props.schuelerFilter.radio_filter();

	const fach: WritableComputedRef<GostFach|undefined> = computed({
		get: () => {
			for (const fach of props.faecherManager.toVector())
				if (fach.id === props.schuelerFilter.fach.value)
					return fach;
			return undefined
		},
		set: (value) => props.schuelerFilter.fach.value = value?.id
	})

	// const filtered = props.schuelerFilter.filtered;

	const selected: WritableComputedRef<SchuelerListeEintrag | undefined> = computed({
		get: () => props.schueler,
		set: (value) => { if (value !== undefined) void props.setSchueler(value); }
	});

</script>
