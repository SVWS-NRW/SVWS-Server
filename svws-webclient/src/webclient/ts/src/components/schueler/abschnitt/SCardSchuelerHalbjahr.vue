<template>
	<svws-ui-content-card title="Allgemeine Angaben">
		<div class="input-wrapper">
			<svws-ui-multi-select title="Klasse" v-model="klasse" :items="props.mapKlassen.values()" :item-text="i => `${i.kuerzel}`" autocomplete />
			<svws-ui-multi-select title="Jahrgang" v-model="jahrgang" :items="props.mapJahrgaenge.values()" :item-text="i => `${i.kuerzel}`" autocomplete />

			<svws-ui-text-input placeholder="Datum von" :model-value="data.datumAnfang || undefined"
				@update:model-value="doPatch({ datumAnfang: String($event) })" type="date" />
			<svws-ui-text-input placeholder="Datum bis" :model-value="data.datumEnde || undefined"
				@update:model-value="doPatch({ datumEnde: String($event) })" type="date" />

			<div class="input-wrapper-3-cols">
				<svws-ui-multi-select title="Klassenlehrer / Tutor" v-model="inputKlassenlehrer" :items="props.mapLehrer.values()"
					:item-text="i => `${i.klassenlehrer}`" autocomplete />
				<svws-ui-multi-select title="Stellvertreter" v-model="inputStellvertreter" :items="props.mapLehrer.values()"
					:item-text="i => `${i.stellvertreter}`" autocomplete />
				<svws-ui-multi-select title="Sonderpädagoge" v-model="sonderpaedagoge" :items="props.mapLehrer.values()"
					:item-text="getLehrerText" autocomplete />
				<svws-ui-text-input placeholder="Maximale Fehlstunden" :model-value="data.fehlstundenGrenzwert || undefined"
					@update:model-value="doPatch({ fehlstundenGrenzwert: Number($event) })" type="number" />
				<svws-ui-text-input placeholder="Fehlstunden Gesamt" :model-value="data.fehlstundenGesamt || undefined"
					@update:model-value="doPatch({ fehlstundenGesamt: Number($event) })" type="number" />
				<svws-ui-text-input placeholder="Fehlstunden Unendschuldigt" :model-value="data.fehlstundenUnentschuldigt || undefined"
					@update:model-value="doPatch({ fehlstundenUnentschuldigt: Number($event) })" type="number" />
			</div>

			<svws-ui-multi-select title="Schulgliederung" v-model="inputSchulgliederung" :items="inputSchulgliederung" :item-filter="schulgliederung_filter"
				:item-sort="schulgliederung_sort" :item-text="i => `${i.schulgliederung}`" autocomplete />
			<svws-ui-multi-select title="Prüfungsordnung" v-model="inputPruefungsordnung" :items="inputPruefungsordnung" :item-filter="pruefungsordnung_filter"
				:item-sort="pruefungsordnung_sort" :item-text="i => `${i.pruefungsordnung}`" autocomplete />
			<svws-ui-multi-select title="Organisationsform" v-model="inputOrganisationsform" :items="inputOrganisationsform" :item-filter="organisationsform_filter"
				:item-sort="organisationsform_sort" :item-text="i => `${i.organisationsform}`" autocomplete />
			<svws-ui-multi-select title="Prüfungsordnung" v-model="inputKlassenart" :items="inputKlassenart" :item-filter="klassenart_filter"
				:item-sort="klassenart_sort" :item-text="i => `${i.klassenart}`" autocomplete />

			<svws-ui-multi-select title="Förderschwerpunkt" v-model="inputFoerderschwerpunkt" :items="inputFoerderschwerpunkt" :item-filter="foerderschwerpunkt_filter"
				:item-sort="foerderschwerpunkt_sort" :item-text="i => `${i.foerderschwerpunkt}`" autocomplete />
			<svws-ui-multi-select title="Weiterer Förderschwerpunkt" v-model="inputWeitererFoerderschwerpunkt" :items="inputWeitererFoerderschwerpunkte" :item-filter="weitererFoerderschwerpunkt_filter"
				:item-sort="weitererFoerderschwerpunkt_sort" :item-text="i => `${i.weitererFoerderschwerpunkt}`" autocomplete />

			<div class="col-span-2">
				<svws-ui-checkbox v-model="inputSchwerstbehinderung"> Schwerstbehinderung </svws-ui-checkbox>
			</div>

			<svws-ui-text-input placeholder="Lernbereichsnote Gesellschaftswissenschaft" v-model="inputLernbereichsnoteGesellschaftswissenschaft" type="text" />
			<svws-ui-text-input placeholder="Lernbereichsnote Naturwissenschaft" v-model="inputLernbereichsnoteNaturwissenschaft" type="text" />
			<div class="col-span-2">
				<svws-ui-text-input placeholder="mögliche Nachprüfungsfächer" v-model="inputNachpruefungsfaecher" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag, KlassenListeEintrag, LehrerListeEintrag, SchuelerLernabschnittsdaten } from '@svws-nrw/svws-core-ts';
	import { computed, WritableComputedRef } from 'vue';

	const props = defineProps<{
		data: SchuelerLernabschnittsdaten;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
		mapKlassen: Map<number, KlassenListeEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerLernabschnittsdaten>) : void;
	}>()

	function doPatch(data: Partial<SchuelerLernabschnittsdaten>) {
		emit('patch', data);
	}

	function getLehrerText(lehrer: LehrerListeEintrag) : string {
		return lehrer.nachname + ", " + lehrer.vorname + " (" + lehrer.kuerzel + ")";
	}

	const sonderpaedagoge: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => props.data.sonderpaedagogeID === null ? undefined : props.mapLehrer.get(props.data.sonderpaedagogeID),
		set: (value) => emit('patch', { sonderpaedagogeID: value === undefined ? null : value.id })
	});

	const jahrgang: WritableComputedRef<JahrgangsListeEintrag | undefined> = computed({
		get: () => props.mapJahrgaenge.get(props.data.jahrgangID),
		set: (value) => {
			if (value !== undefined)
				emit('patch', { jahrgangID: value.id });
		}
	});

	const klasse: WritableComputedRef<KlassenListeEintrag | undefined> = computed({
		get: () => props.mapKlassen.get(props.data.klassenID),
		set: (value) => {
			if (value !== undefined)
				emit('patch', { klassenID: value.id });
		}
	});

</script>
