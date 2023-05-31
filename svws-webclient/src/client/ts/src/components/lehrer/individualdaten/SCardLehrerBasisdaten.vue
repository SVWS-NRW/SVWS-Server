<template>
	<svws-ui-content-card title="Basisdaten">
		<template #actions>
			<svws-ui-checkbox v-model="istSichtbar"> Ist sichtbar </svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" v-model="inputKuerzel" type="text" required />
			<svws-ui-multi-select title="Personal-Typ" v-model="inputPersonalTyp" :items="PersonalTyp.values()" :item-text="(i: PersonalTyp) => i.bezeichnung" required />
			<svws-ui-text-input placeholder="Nachname" v-model="inputNachname" type="text" required />
			<svws-ui-text-input placeholder="Vorname" v-model="inputVorname" type="text" required />
			<svws-ui-multi-select title="Geschlecht" v-model="inputGeschlecht" :items="Geschlecht.values()" :item-text="i=>i.text" required />
			<svws-ui-text-input placeholder="Geburtsdatum" v-model="inputGeburtsdatum" type="date" required />
			<svws-ui-multi-select title="Staatsangehörigkeit" v-model="inputStaatsangehoerigkeit" :items="Nationalitaeten.values()"
				:item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
				:item-filter="staatsangehoerigkeitKatalogEintragFilter" required autocomplete />
			<svws-ui-text-input placeholder="Akad.Grad" v-model="inputTitel" type="text" />
			<svws-ui-text-input placeholder="Amtsbezeichnung" v-model="inputAmtsbezeichnung" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import type { LehrerStammdaten} from "@svws-nrw/svws-core";
	import { computed } from "vue";
	import { DeveloperNotificationException, Geschlecht, Nationalitaeten, PersonalTyp } from "@svws-nrw/svws-core";
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

	const istSichtbar: WritableComputedRef<boolean> = computed({
		get: () => true,
		set: (value) => {throw new DeveloperNotificationException("Sichtbarkeit ist noch nicht implementiert")}//doPatch({ istSichtbar: value })
	});


</script>
