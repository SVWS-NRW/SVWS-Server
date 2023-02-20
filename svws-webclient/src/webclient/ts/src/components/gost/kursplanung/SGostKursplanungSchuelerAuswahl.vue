<template>
	<svws-ui-content-card>
		<div class="flex justify-between items-center">
			<svws-ui-checkbox v-model="kurs_filter_toggle" class="">Kursfilter<span v-if="kurs_filter_toggle">:</span></svws-ui-checkbox>
			<svws-ui-multi-select v-if="kurs_filter_toggle" v-model="schuelerFilter.kurs.value" :items="schuelerFilter.getKurse()" headless
				:item-text="(kurs: GostBlockungKurs) => getErgebnismanager().getOfKursName(kurs.id) ?? ''" class="w-48" />
		</div>
		<div class="flex justify-between items-center mb-3">
			<svws-ui-checkbox v-model="fach_filter_toggle" class=""> Fachfilter<span v-if="fach_filter_toggle">:</span></svws-ui-checkbox>
			<svws-ui-multi-select v-if="fach_filter_toggle" v-model="schuelerFilter.fach.value" :items="faecherManager.toVector()" headless :item-text="(fach: GostFach) => fach.bezeichnung ?? ''" class="w-32" />
			<svws-ui-multi-select v-if="fach_filter_toggle" v-model="schuelerFilter.kursart.value" :items="GostKursart.values()" headless :item-text="(kursart: GostKursart) => kursart.kuerzel" class="w-16" />
		</div>
		<div class="mb-3">
			<svws-ui-radio-group class="radio--row">
				<svws-ui-radio-option v-model="radio_filter" value="alle" name="Alle" label="Alle" :icon="false" />
				<svws-ui-radio-option v-model="radio_filter" value="kollisionen" name="Kollisionen" label="Kollisionen">
					<i-ri-alert-line />
				</svws-ui-radio-option>
				<svws-ui-radio-option v-model="radio_filter" value="nichtwahlen" name="Nichtwahlen" label="Nichtwahlen">
					<i-ri-forbid-2-line />
				</svws-ui-radio-option>
				<svws-ui-radio-option v-model="radio_filter" value="kollisionen_nichtwahlen" name="Kollisionen_Nichtwahlen" label="K/N">
					<i-ri-alert-fill />
				</svws-ui-radio-option>
			</svws-ui-radio-group>
		</div>
		<div class="mb-2 relative z-20">
			<svws-ui-text-input v-model="schuelerFilter.name.value" type="search" placeholder="Suche nach Namen">
				<i-ri-search-line />
			</svws-ui-text-input>
		</div>
		<div class="sticky h-8 -mt-8 -top-8 bg-white z-10" />
		<div class="v-table--container v-table--rows-white">
			<table class="v-table--complex">
				<thead>
					<tr>
						<th class="table--th-padding">
							Schülerauswahl
						</th>
					</tr>
				</thead>
				<tbody>
					<s-kurs-schueler-schueler v-for="s in filtered.values()" :key="s.id" :schueler="s" :selected="selected === s" @click="selected = s"
						:get-ergebnismanager="getErgebnismanager" :schueler-filter="schuelerFilter" />
					<tr v-if="!filtered.size">
						<td class="opacity-50 text-sm">Keine Schüler zu diesem Filter gefunden.</td>
					</tr>
				</tbody>
			</table>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungsergebnisManager, GostFach, GostFaecherManager, GostKursart, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";
	import { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";

	const props = defineProps<{
		setSchueler: (schueler: SchuelerListeEintrag) => Promise<void>;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		schueler: SchuelerListeEintrag | undefined;
		schuelerFilter: GostKursplanungSchuelerFilter;
		faecherManager: GostFaecherManager;
	}>();

	const kurs_filter_toggle = props.schuelerFilter.kurs_filter_toggle();
	const fach_filter_toggle = props.schuelerFilter.fach_filter_toggle();
	const radio_filter = props.schuelerFilter.radio_filter();

	const filtered = props.schuelerFilter.filtered();

	const selected: WritableComputedRef<SchuelerListeEintrag | undefined> = computed({
		get: () => props.schueler,
		set: (value) => { if (value !== undefined) void props.setSchueler(value); }
	});

</script>
