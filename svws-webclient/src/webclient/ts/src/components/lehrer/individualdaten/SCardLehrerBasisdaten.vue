<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="flex-shrink-0">
				<svws-ui-avatar
					:src="'data:image/png;base64, ' + foto"
					:alt="'Foto ' + inputVorname + ' ' + inputNachname"
				/>
			</div>
			<div class="input-wrapper">
				<svws-ui-text-input
					v-model="inputKuerzel"
					type="text"
					placeholder="Kürzel"
					required
				/>
				<svws-ui-multi-select
					v-model="inputPersonalTyp"
					:items="inputPersonalTypen"
					:item-text="(i: PersonalTyp) => i.bezeichnung"
					title="Personal-Typ"
					required
				/>
				<svws-ui-text-input
					v-model="inputNachname"
					type="text"
					placeholder="Nachname"
					required
				/>
				<svws-ui-text-input
					v-model="inputVorname"
					type="text"
					placeholder="Vorname"
					required
				/>
				<svws-ui-multi-select
					v-model="inputGeschlecht"
					title="Geschlecht"
					:items="inputKatalogGeschlecht"
					required
				/>
				<svws-ui-text-input
					v-model="inputGeburtsdatum"
					type="date"
					placeholder="Geburtsdatum"
					required
				/>
				<svws-ui-multi-select
					v-model="inputStaatsangehoerigkeit"
					title="Staatsangehörigkeit"
					:items="inputKatalogStaatsangehoerigkeit"
					:item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort"
					:item-filter="staatsangehoerigkeitKatalogEintragFilter"
					required
				/>
				<svws-ui-text-input
					v-model="inputTitel"
					type="text"
					placeholder="Akad.Grad"
				/>
				<svws-ui-text-input
					v-model="inputAmtsbezeichnung"
					type="text"
					placeholder="Amtsbezeichnung"
				/>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import {
		Geschlecht,
		LehrerStammdaten,
		Nationalitaeten,
		PersonalTyp
	} from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import {
		staatsangehoerigkeitKatalogEintragFilter,
		staatsangehoerigkeitKatalogEintragSort
	} from "~/helfer";

	const main: Main = injectMainApp();
	const app = main.apps.lehrer;

	const inputKatalogStaatsangehoerigkeit: ComputedRef<Nationalitaeten[]> = computed(() =>
		Nationalitaeten.values()
	);
	const inputKatalogGeschlecht: ComputedRef<Geschlecht[]> = computed(() =>
		Geschlecht.values()
	);
	const inputPersonalTypen: ComputedRef<PersonalTyp[]> = computed(() =>
		PersonalTyp.values()
	);

	const daten: ComputedRef<LehrerStammdaten> = computed(
		() => app.stammdaten.daten || new LehrerStammdaten()
	);
	const id: ComputedRef<number | undefined> = computed(() => {
		return daten.value.id;
	});

	const foto: ComputedRef<string | undefined> = computed(() => {
		return String(daten.value.foto);
	});

	const inputKuerzel: WritableComputedRef<string> = computed({
		get(): string {
			return daten.value.kuerzel.toString();
		},
		set(val) {
			if (app.stammdaten) {
				app.stammdaten.patch({ kuerzel: val });
			}
		}
	});

	const inputNachname: WritableComputedRef<string> = computed({
		get(): string {
			return daten.value.nachname.toString();
		},
		set(val: string) {
			app.stammdaten.patch({ nachname: val });
		}
	});

	const inputVorname: WritableComputedRef<string> = computed({
		get(): string {
			return daten.value.vorname.toString();
		},
		set(val) {
			app.stammdaten.patch({ vorname: val });
		}
	});

	const inputGeschlecht: WritableComputedRef<Geschlecht> = computed({
		get(): Geschlecht {
			return (
				Geschlecht.fromValue(Number(daten.value.geschlecht)) ||
				Geschlecht.X
			);
		},
		set(val: Geschlecht) {
			app.stammdaten.patch({ geschlecht: val.id });
		}
	});

	const inputGeburtsdatum: WritableComputedRef<string> = computed({
		get(): string {
			return daten.value?.geburtsdatum ? String(daten.value.geburtsdatum) : '';
		},
		set(val) {
			app.stammdaten.patch({ geburtsdatum: val });
		}
	});

	const inputPersonalTyp: WritableComputedRef<PersonalTyp> = computed({
		get(): PersonalTyp {
			const kuerzel = daten.value.personalTyp;
			return (
				inputPersonalTypen.value.find(i => i.kuerzel === kuerzel) ||
				PersonalTyp.SONSTIGE
			);
		},
		set(val: PersonalTyp) {
			app.stammdaten.patch({ personalTyp: val.kuerzel.toString() });
		}
	});

	const inputStaatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get(): Nationalitaeten {
			return Nationalitaeten.getByISO3(daten.value.staatsangehoerigkeitID) || Nationalitaeten.DEU;
		},
		set(val: Nationalitaeten) {
			app.stammdaten.patch({ staatsangehoerigkeitID: val.daten.iso3 });
		}
	});

	const inputTitel: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value?.titel !== null ? String(daten.value.titel) : '';
		},
		set(val) {
			app.stammdaten.patch({ titel: val });
		}
	});

	const inputAmtsbezeichnung: WritableComputedRef<string | undefined> =
		computed({
			get(): string | undefined {
				return daten.value?.amtsbezeichnung ? String(daten.value.amtsbezeichnung) : '';
			},
			set(val) {
				app.stammdaten.patch({ amtsbezeichnung: val });
			}
		});
</script>
