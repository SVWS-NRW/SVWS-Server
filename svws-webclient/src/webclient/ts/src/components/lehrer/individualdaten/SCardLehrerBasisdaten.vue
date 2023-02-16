<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Kürzel" v-model="inputKuerzel" type="text" required />
				<svws-ui-multi-select title="Personal-Typ" v-model="inputPersonalTyp" :items="PersonalTyp.values()" :item-text="(i: PersonalTyp) => i.bezeichnung" required />
				<svws-ui-text-input placeholder="Nachname" v-model="inputNachname" type="text" required />
				<svws-ui-text-input placeholder="Vorname" v-model="inputVorname" type="text" required />
				<svws-ui-multi-select title="Geschlecht" v-model="inputGeschlecht" :items="Geschlecht.values()" required />
				<svws-ui-text-input placeholder="Geburtsdatum" v-model="inputGeburtsdatum" type="date" required />
				<svws-ui-multi-select title="Staatsangehörigkeit" v-model="inputStaatsangehoerigkeit" :items="Nationalitaeten.values()"
					:item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
					:item-filter="staatsangehoerigkeitKatalogEintragFilter" required />
				<svws-ui-text-input placeholder="Akad.Grad" v-model="inputTitel" type="text" />
				<svws-ui-text-input placeholder="Amtsbezeichnung" v-model="inputAmtsbezeichnung" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, WritableComputedRef } from "vue";

	import { Geschlecht, LehrerStammdaten, Nationalitaeten, PersonalTyp } from "@svws-nrw/svws-core-ts";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort } from "~/helfer";

	const props = defineProps<{
		stammdaten: LehrerStammdaten
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<LehrerStammdaten>): void;
	}>()

	function doPatch(data: Partial<LehrerStammdaten>) {
		emit('patch', data);
	}

	const inputKuerzel: WritableComputedRef<string> = computed({
		get: () => props.stammdaten.kuerzel,
		set: (value) => doPatch({ kuerzel: value })
	});

	const inputNachname: WritableComputedRef<string> = computed({
		get: () => props.stammdaten.nachname,
		set: (value) => doPatch({ nachname: value })
	});

	const inputVorname: WritableComputedRef<string> = computed({
		get: () => props.stammdaten.vorname,
		set: (value) => doPatch({ vorname: value })
	});

	const inputGeschlecht: WritableComputedRef<Geschlecht> = computed({
		get: () => Geschlecht.fromValue(props.stammdaten.geschlecht) || Geschlecht.X,
		set: (value) => doPatch({ geschlecht: value.id })
	});

	const inputGeburtsdatum: WritableComputedRef<string> = computed({
		get: () => props.stammdaten?.geburtsdatum ? props.stammdaten.geburtsdatum : '',
		set: (value) => doPatch({ geburtsdatum: value })
	});

	const inputPersonalTyp: WritableComputedRef<PersonalTyp> = computed({
		get: () => PersonalTyp.values().find(i => i.kuerzel === props.stammdaten.personalTyp) || PersonalTyp.SONSTIGE,
		set: (value) => doPatch({ personalTyp: value.kuerzel })
	});

	const inputStaatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.stammdaten.staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => doPatch({ staatsangehoerigkeitID: value.daten.iso3 })
	});

	const inputTitel: WritableComputedRef<string | undefined> = computed({
		get: () => (props.stammdaten.titel) === null ? "" : props.stammdaten.titel,
		set: (value) => doPatch({ titel: value })
	});

	const inputAmtsbezeichnung: WritableComputedRef<string | undefined> = computed({
		get: () => (props.stammdaten.amtsbezeichnung === null) ? "" : props.stammdaten.amtsbezeichnung,
		set: (value) => doPatch({ amtsbezeichnung: value })
	});

</script>
