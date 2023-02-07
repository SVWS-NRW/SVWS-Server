<template>
	<svws-ui-content-card title="Zeugnis / Abschluss">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Konferenz-Datum" :model-value="data.datumKonferenz || undefined"
				@update:model-value="doPatch({ datumKonferenz: String($event) })" type="date" />
			<svws-ui-text-input placeholder="Zeugnis-Datum" :model-value="data.datumZeugnis || undefined"
				@update:model-value="doPatch({ datumZeugnis: String($event) })" type="date" />

			<div class="input-wrapper-3-cols">
				<div> TODO: istAbschlussPrognose </div>
				<div> TODO: versetzungsvermerk </div>
				<div> TODO: abschlussart </div>
				<svws-ui-multi-select title="Folgeklasse" v-model="folgeklasse" :items="mapKlassen.values()" :item-text="i => `${i.kuerzel}`" autocomplete />
			</div>
			<div> TODO: abschluss </div>
			<div> TODO: abschlussBerufsbildend </div>
			<div> TODO: zeugnisart </div>

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

	import { KlassenListeEintrag, SchuelerLernabschnittBemerkungen, SchuelerLernabschnittsdaten } from '@svws-nrw/svws-core-ts';
	import { computed, WritableComputedRef } from 'vue';

	const props = defineProps<{
		data: SchuelerLernabschnittsdaten;
		mapKlassen: Map<number, KlassenListeEintrag>;
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

	const folgeklasse: WritableComputedRef<KlassenListeEintrag | undefined> = computed({
		get: () => props.data.folgeklassenID === null ? undefined : props.mapKlassen.get(props.data.folgeklassenID),
		set: (value) => emit('patch', { folgeklassenID: (value === undefined) ? null : value.id })
	});

</script>
