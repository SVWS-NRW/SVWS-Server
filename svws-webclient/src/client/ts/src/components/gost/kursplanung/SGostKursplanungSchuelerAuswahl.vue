<template>
	<svws-ui-content-card overflow-scroll class="-mt-0.5">
		<svws-ui-data-table :model-value="filtered" v-model:clicked="selected" clickable :items="undefined"
			:filter="true" :filter-reverse="false"
			:no-data="schuelerFilter.filtered.value.size <= 0" no-data-html="Keine Schüler zu diesem Filter gefunden.">
			<template #search>
				<div class="mb-1 3xl:mb-0.5">
					<svws-ui-text-input type="search" v-model="schuelerFilter.name.value" placeholder="Suche" />
				</div>
			</template>
			<template #filter>
				<svws-ui-radio-group class="radio--row col-span-full">
					<svws-ui-radio-option v-model="kurs_filter_toggle" :value="!kurs_filter_toggle" name="Filter" label="Kursfilter" :force-checked="kurs_filter_toggle ?? false">
						<i-ri-filter-line />
					</svws-ui-radio-option>
					<svws-ui-radio-option v-model="fach_filter_toggle" :value="!fach_filter_toggle" name="FilterFa" label="Fachfilter" :force-checked="fach_filter_toggle ?? false">
						<i-ri-filter-line />
					</svws-ui-radio-option>
				</svws-ui-radio-group>
				<svws-ui-input-wrapper class="col-span-full" v-if="kurs_filter_toggle">
					<svws-ui-multi-select v-model="schuelerFilter.kurs.value" :items="schuelerFilter.getKurse()"
						:item-text="(kurs: GostBlockungKurs) => getErgebnismanager().getOfKursName(kurs.id) ?? ''" />
				</svws-ui-input-wrapper>
				<svws-ui-input-wrapper class="col-span-full" v-if="fach_filter_toggle">
					<svws-ui-multi-select v-model="fach" :items="faecherManager.toList()" :item-text="(fach: GostFach) => fach.bezeichnung ?? ''" />
					<svws-ui-multi-select v-model="schuelerFilter.kursart.value" :items="GostKursart.values()" :item-text="(kursart: GostKursart) => kursart.kuerzel" />
				</svws-ui-input-wrapper>
				<svws-ui-spacing />
				<svws-ui-radio-group class="radio--row col-span-full">
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
				<div role="row" class="data-table__tr data-table__tbody__tr" :class="{'data-table__tr--clicked': selected === s}"
					v-for="(s, index) in schuelerFilter.filtered.value.values()" @click="selected = s" :key="index">
					<s-kurs-schueler-schueler :schueler="s" :selected="selected === s" :get-ergebnismanager="getErgebnismanager" :schueler-filter="schuelerFilter" />
				</div>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { GostBlockungKurs, GostFach, SchuelerListeEintrag } from "@svws-nrw/svws-core";
	import { GostKursart } from "@svws-nrw/svws-core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import type { KursplanungSchuelerAuswahlProps } from "./SGostKursplanungSchuelerAuswahlProps";

	const props = defineProps<KursplanungSchuelerAuswahlProps>();

	const kurs_filter_toggle = props.schuelerFilter.kurs_filter_toggle();
	const fach_filter_toggle = props.schuelerFilter.fach_filter_toggle();
	const radio_filter = props.schuelerFilter.radio_filter();

	const fach: WritableComputedRef<GostFach|undefined> = computed({
		get: () => {
			for (const fach of props.faecherManager.toList())
				if (fach.id === props.schuelerFilter.fach.value)
					return fach;
			return undefined
		},
		set: (value) => props.schuelerFilter.fach.value = value?.id
	})

	const filtered = computed(()=> [...props.schuelerFilter.filtered.value.values()]);

	const selected: WritableComputedRef<SchuelerListeEintrag | undefined> = computed({
		get: () => props.schueler,
		set: (value) => { if (value !== undefined) void props.setSchueler(value); }
	});

</script>
