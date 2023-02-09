<template>
	<svws-ui-content-card>
		<div class="flex justify-between items-center">
			<svws-ui-checkbox v-model="kurs_filter_toggle" class="">Kursfilter<span v-if="kurs_filter_toggle">:</span></svws-ui-checkbox>
			<svws-ui-multi-select v-if="kurs_filter_toggle" v-model="schuelerFilter.kurs.value" :items="schuelerFilter.getKurse()" headless :item-text="(kurs: GostBlockungKurs) => manager?.getOfKursName(kurs.id) ?? ''" class="w-48" />
		</div>
		<div class="flex justify-between items-center mb-3">
			<svws-ui-checkbox v-model="fach_filter_toggle" class=""> Fachfilter<span v-if="fach_filter_toggle">:</span></svws-ui-checkbox>
			<svws-ui-multi-select v-if="fach_filter_toggle" v-model="schuelerFilter.fach.value" :items="dataFaecher.daten" headless :item-text="(fach: GostFach) => fach.bezeichnung ?? ''" class="w-32" />
			<svws-ui-multi-select v-if="fach_filter_toggle" v-model="schuelerFilter.kursart.value" :items="GostKursart.values()" headless :item-text="(kursart: GostKursart) => kursart.kuerzel" class="w-16" />
		</div>
		<div class="mb-3">
			<svws-ui-radio-group>
				<svws-ui-radio-option v-model="radio_filter" value="alle" name="Alle" label="Alle" />
				<svws-ui-radio-option v-model="radio_filter" value="kollisionen" name="Kollisionen" label="Kollisionen" />
				<svws-ui-radio-option v-model="radio_filter" value="nichtwahlen" name="Nichtwahlen" label="Nichtwahlen" />
				<svws-ui-radio-option v-model="radio_filter" value="kollisionen_nichtwahlen" name="Kollisionen_Nichtwahlen" label="Kollisionen und Nichtwahlen" />
			</svws-ui-radio-group>
		</div>
		<div class="mb-2">
			<svws-ui-text-input v-model="schuelerFilter.name.value" type="search" placeholder="Suche nach Namen">
				<i-ri-search-line />
			</svws-ui-text-input>
		</div>
		<div class="h-full overflow-hidden mb-[-1px]">
			<!---ml-4 -mr-4, table--container: -ml-px-->
			<div class="v-table--container v-table--rows-white">
				<table class="v-table--complex">
					<thead>
						<tr>
							<th class="table--th-padding">Schülerauswahl</th>
						</tr>
					</thead>
					<tbody>
						<s-kurs-schueler-schueler v-for="s in filtered.values()" :key="s.id" :schueler="s" :selected="selected === s" @click="selected = s"
							:blockung="blockung" :schueler-filter="schuelerFilter" />
					</tbody>
				</table>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostBlockungsergebnisManager, GostFach, GostHalbjahr, GostKursart, GostStatistikFachwahl, LehrerListeEintrag,
		List, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef, WritableComputedRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { DataGostKursblockungsergebnis } from "~/apps/gost/DataGostKursblockungsergebnis";
	import { GostKursplanungSchuelerFilter } from "./GostKursplanungSchuelerFilter";

	const props = defineProps<{
		setSchueler: (schueler: SchuelerListeEintrag) => Promise<void>;
		schueler: SchuelerListeEintrag | undefined;
		schuelerFilter: GostKursplanungSchuelerFilter;
		dataFaecher: DataGostFaecher;
		halbjahr: ShallowRef<GostHalbjahr>;
		blockung: DataGostKursblockung;
		ergebnis: DataGostKursblockungsergebnis;
		mapLehrer: Map<number, LehrerListeEintrag>;
		fachwahlen: List<GostStatistikFachwahl>;
		mapSchueler: Map<number, SchuelerListeEintrag>;
	}>();

	const kurs_filter_toggle = props.schuelerFilter.kurs_filter_toggle();
	const fach_filter_toggle = props.schuelerFilter.fach_filter_toggle();
	const radio_filter = props.schuelerFilter.radio_filter();

	const filtered = props.schuelerFilter.filtered();

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => {
		// löse ein erneutes Filtern aus, wenn der Manager sich ändert (z.B. bei Blockungs- oder -Ergebniswechsel)
		return props.blockung.ergebnismanager
	});

	const selected: WritableComputedRef<SchuelerListeEintrag | undefined> = computed({
		get: () => props.schueler,
		set: (value) => { if (value !== undefined) void props.setSchueler(value); }
	});

</script>
