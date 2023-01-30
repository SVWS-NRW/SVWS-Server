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

	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { Geschlecht, LehrerStammdaten, Nationalitaeten, PersonalTyp } from "@svws-nrw/svws-core-ts";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort } from "~/helfer";
	import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";

	const props = defineProps<{ stammdaten: DataLehrerStammdaten }>();

	const daten: ComputedRef<LehrerStammdaten> = computed(() => props.stammdaten.daten || new LehrerStammdaten());

	const inputKuerzel: WritableComputedRef<string> = computed({
		get(): string {
			return daten.value.kuerzel;
		},
		set(val) {
			void props.stammdaten.patch({ kuerzel: val });
		}
	});

	const inputNachname: WritableComputedRef<string> = computed({
		get(): string {
			return daten.value.nachname;
		},
		set(val: string) {
			void props.stammdaten.patch({ nachname: val });
		}
	});

	const inputVorname: WritableComputedRef<string> = computed({
		get(): string {
			return daten.value.vorname;
		},
		set(val) {
			void props.stammdaten.patch({ vorname: val });
		}
	});

	const inputGeschlecht: WritableComputedRef<Geschlecht> = computed({
		get(): Geschlecht {
			return Geschlecht.fromValue(daten.value.geschlecht) || Geschlecht.X;
		},
		set(val: Geschlecht) {
			void props.stammdaten.patch({ geschlecht: val.id });
		}
	});

	const inputGeburtsdatum: WritableComputedRef<string> = computed({
		get(): string {
			return daten.value?.geburtsdatum ? daten.value.geburtsdatum : '';
		},
		set(val) {
			void props.stammdaten.patch({ geburtsdatum: val });
		}
	});

	const inputPersonalTyp: WritableComputedRef<PersonalTyp> = computed({
		get(): PersonalTyp {
			return PersonalTyp.values().find(i => i.kuerzel === daten.value.personalTyp) || PersonalTyp.SONSTIGE;
		},
		set(val: PersonalTyp) {
			void props.stammdaten.patch({ personalTyp: val.kuerzel });
		}
	});

	const inputStaatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get(): Nationalitaeten {
			return Nationalitaeten.getByISO3(daten.value.staatsangehoerigkeitID) || Nationalitaeten.DEU;
		},
		set(val: Nationalitaeten) {
			void props.stammdaten.patch({ staatsangehoerigkeitID: val.daten.iso3 });
		}
	});

	const inputTitel: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return (daten.value.titel) === null ? "" : daten.value.titel;
		},
		set(val) {
			void props.stammdaten.patch({ titel: val });
		}
	});

	const inputAmtsbezeichnung: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return (daten.value.amtsbezeichnung === null) ? "" : daten.value.amtsbezeichnung;
		},
		set(val) {
			void props.stammdaten.patch({ amtsbezeichnung: val });
		}
	});

</script>
