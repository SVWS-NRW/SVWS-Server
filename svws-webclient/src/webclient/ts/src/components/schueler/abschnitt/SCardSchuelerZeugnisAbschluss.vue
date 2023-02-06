<template>
	<svws-ui-content-card title="Zeugnis / Abschluss">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Konferenz-Datum" :model-value="data.datumKonferenz || undefined"
				@update:model-value="doPatch({ datumKonferenz: String($event) })" type="date" />
			<svws-ui-text-input placeholder="Zeugnis-Datum" :model-value="data.datumZeugnis || undefined"
				@update:model-value="doPatch({ datumZeugnis: String($event) })" type="date" />

			<div class="input-wrapper-3-cols">
				<svws-ui-multi-select title="Vers.-Vermerk" v-model="inputVersetzungsVermerk" :items="inputVersetzungsVermerke" :item-filter="versetzungsVermerk_filter"
					:item-sort="versetzungsVermerk_sort" :item-text="i => `${i.versetzungsVermerk}`" autocomplete />
				<svws-ui-multi-select title="Abschluss-Art" v-model="inputAbschlussArt" :items="inputAbschlussArt" :item-filter="abschlussArt_filter"
					:item-sort="abschlussArt_sort" :item-text="i => `${i.abschlussArt}`" autocomplete />
				<svws-ui-multi-select title="Folgeklasse" v-model="inputFolgeklasse" :items="inputFolgeklassen" :item-filter="folgeklasse_filter"
					:item-sort="folgeklasse_sort" :item-text="i => `${i.folgeklasse}`" autocomplete />
			</div>

			<svws-ui-multi-select title="Abschluss" v-model="inputAbschluss" :items="inputAbschluss" :item-filter="abschluss_filter"
				:item-sort="abschluss_sort" :item-text="i => `${i.abschluss}`" autocomplete />
			<svws-ui-multi-select title="Zeugnis-Art" v-model="inputZeugnisArt" :items="inputZeugnisArte" :item-filter="zeugnisArt_filter"
				:item-sort="zeugnisArt_sort" :item-text="i => `${i.zeugnisArt}`" autocomplete />

			<div class="col-span-2 flex gap-4">
				<svws-ui-button type="primary"> Versetzungs-/Abschluss-Berechnung </svws-ui-button>
				<svws-ui-button type="secondary"> Nachprüfungen </svws-ui-button>
			</div>

			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="Zeugnisbemerkungen" :model-value="data.bemerkungen.zeugnisAllgemein || undefined"
					@update:model-value="doPatchBemerkungen({ zeugnisAllgemein : String($event) })" resizeable="vertical" />
			</div>
			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="Arbeits- und Sozialverhalten" :model-value="data.bemerkungen.zeugnisASV || undefined"
					@update:model-value="doPatchBemerkungen({ zeugnisASV : String($event) })" resizeable="vertical" />
			</div>
			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="Außerunterrichtliches Engagement" :model-value="data.bemerkungen.zeugnisAUE || undefined"
					@update:model-value="doPatchBemerkungen({ zeugnisAUE : String($event) })" resizeable="vertical" />
			</div>
			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="Bemerkung Versetzung" :model-value="data.bemerkungen.versetzungsentscheidung || undefined"
					@update:model-value="doPatchBemerkungen({ versetzungsentscheidung : String($event) })" resizeable="vertical" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { SchuelerLernabschnittBemerkungen, SchuelerLernabschnittsdaten } from '@svws-nrw/svws-core-ts';

	const props = defineProps<{
		data: SchuelerLernabschnittsdaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerLernabschnittsdaten>) : void;
		(e: 'patchBemerkungen', data: Partial<SchuelerLernabschnittBemerkungen>) : void;
	}>()

	function doPatch(data: Partial<SchuelerLernabschnittsdaten>) {
		emit('patch', data);
	}

	function doPatchBemerkungen(data: Partial<SchuelerLernabschnittBemerkungen>) {
		emit('patchBemerkungen', data);
	}

</script>
