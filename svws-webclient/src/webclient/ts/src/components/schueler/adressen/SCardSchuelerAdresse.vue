<template>
	<svws-ui-content-card>
		<template #actions>
			<svws-ui-button type="error">
				<svws-ui-icon> <i-ri-delete-bin-2-line /> </svws-ui-icon>
				<span class="ml-2">Löschen</span>
			</svws-ui-button>
		</template>
		<div class="input-wrapper">
			<div class="entry-wrapper">
				<h2 class="svws-ui-text-black col-span-2">Basisdaten</h2>
				<div class="entry-content">
					<svws-ui-text-input placeholder="Name" v-model="name" type="text" />
					<svws-ui-text-input placeholder="Namensergänzung" v-model="namezusatz" type="text" />
					<svws-ui-text-input placeholder="1. Telefon-Nr." v-model="telefon1" type="text" />
					<svws-ui-text-input placeholder="2. Telefon-Nr." v-model="telefon2" type="text" />
					<svws-ui-text-input placeholder="Fax-Nr." v-model="fax" type="text" />
					<svws-ui-text-input placeholder="E-Mail Adresse" v-model="email" type="email" verify-email />
					<svws-ui-text-input placeholder="Branche" v-model="branche" title="Branche" type="text" />
					<div class="flex w-full flex-row items-center space-x-4">
						<div class="flex-grow">
							<svws-ui-multi-select v-if="inputBetriebAnsprechpartner.length > 0" title="Ansprechpartner" v-model="ansprechpartner"
								:items="inputBetriebAnsprechpartner" :item-text="(i: BetriebAnsprechpartner) => i.name?.toString() || ''" />
							<p v-else>Kein Ansprechpartner</p>
						</div>
						<div class="flex flex-row space-x-4">
							<svws-ui-button v-if="inputBetriebAnsprechpartner.length > 0" type="secondary" @click="modalEdit.openModal()">
								<svws-ui-icon> <i-ri-draft-line /> </svws-ui-icon>
							</svws-ui-button>
							<svws-ui-button type="secondary" @click="modalAdd.openModal()">
								<svws-ui-icon> <i-ri-user-add-line /> </svws-ui-icon>
							</svws-ui-button>
						</div>
					</div>
					<svws-ui-multi-select title="betreuende Lehrkraft" v-model="inputBetreuungslehrer" :items="inputLehrerListe" :item-text="(i:LehrerListeEintrag) => i.nachname.toString()" />
				</div>
			</div>
			<div class="entry-wrapper">
				<h2 class="svws-ui-text-black col-span-2">Adresse</h2>
				<div class="entry-content">
					<div class="col-span-2">
						<svws-ui-text-input placeholder="Straße / Hausnummer" v-model="strassenname" type="text" />
					</div>
					<div class="col-span-2">
						<svws-ui-text-input placeholder="Zusatz" v-model="hausnummerzusatz" type="text" />
					</div>
					<svws-ui-multi-select title="Wohnort" v-model="inputWohnortID" :items="inputKatalogOrte" :item-filter="orte_filter"
						:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
					<!-- TODO In der Datenbank gibt es für die Adresse nur Ortsteil_id
					<svws-ui-multi-select title="Ortsteil" v-model="inputOrtsteilID" :items="inputKatalogOrtsteil" :item-filter="ortsteilFilter"
						:item-sort="ortsteilSort" :item-text="i => i.ortsteil" />
					-->
					<svws-ui-checkbox v-model="anschreiben"> erhält Anschreiben </svws-ui-checkbox>
				</div>
			</div>
			<div class="entry-wrapper">
				<h2 class="svws-ui-text-black col-span-2"> Bemerkungen </h2>
				<div class="entry-content">
					<div class="col-span-2">
						<svws-ui-textarea-input v-model="bemerkungen" placeholder="Bemerkungen" />
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>

	<!--   MODALFENSTER-ANSPRECHPARTNER-EDITIEREN -->
	<svws-ui-modal ref="modalEdit" size="medium">
		<template #modalTitle>Ansprechpartner Editieren</template>
		<template #modalContent>
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Name" v-model="ap_name" type="text" />
				<svws-ui-text-input placeholder="Vorname" v-model="ap_vorname" type="text" />
				<svws-ui-text-input placeholder="Anrede" v-model="ap_anrede" type="text" />
				<svws-ui-text-input placeholder="Titel" v-model="ap_titel" type="text" />
				<svws-ui-text-input placeholder="Abteilung" v-model="ap_abteilung" type="text" />
				<svws-ui-text-input placeholder="Telefon-Nr." v-model="ap_telefonnr" type="tel" />
				<svws-ui-text-input placeholder="E-Mail Adresse" v-model="ap_email" type="email" verify-email />
			</div>
		</template>
		<template #modalActions>
			<!-- <svws-ui-button type="secondary" @click="$refs.modalEdit.closeModal"> Abbrechen </svws-ui-button> -->
			<svws-ui-button type="primary" @click="modalEdit.closeModal"> Schließen </svws-ui-button>
		</template>
	</svws-ui-modal>

	<svws-ui-modal ref="modalAdd" size="medium">
		<template #modalTitle>Ansprechpartner Hinzufügen</template>
		<template #modalContent>
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Name" v-model="ap_neu.name" type="text" />
				<svws-ui-text-input placeholder="Vorname" v-model="ap_neu.vorname" type="text" />
				<svws-ui-text-input placeholder="Anrede" v-model="ap_neu.anrede" type="text" />
				<svws-ui-text-input placeholder="Titel" v-model="ap_neu.titel" type="text" />
				<svws-ui-text-input placeholder="Abteilung" v-model="ap_neu.abteilung" type="text" />
				<svws-ui-text-input placeholder="Telefon-Nr." v-model="ap_neu.telefon" type="tel" />
				<svws-ui-text-input placeholder="E-Mail Adresse" v-model="ap_neu.email" type="email" verify-email />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modalAdd.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="saveEntries()"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { injectMainApp, Main } from "~/apps/Main";
	import { computed, ComputedRef, reactive, ref, WritableComputedRef } from "vue";
	import { BetriebAnsprechpartner, LehrerListeEintrag, List, OrtKatalogEintrag, SchuelerBetriebsdaten } from "@svws-nrw/svws-core-ts";
	import { orte_filter, orte_sort, } from "~/helfer";
	import { App } from "~/apps/BaseApp";
	import { DataBetriebsstammdaten } from "~/apps/schueler/DataBetriebsstammdaten";
	import { ListSchuelerBetriebsdaten } from "~/apps/schueler/ListSchuelerBetriebsdaten";

	const props = defineProps<{
		listSchuelerbetriebe : ListSchuelerBetriebsdaten;
		betriebsStammdaten: DataBetriebsstammdaten;
	}>();

	const main: Main = injectMainApp();
	const modalEdit = ref();
	const modalAdd = ref();

	/** Kataloge */

	const inputBetriebAnsprechpartner: ComputedRef<BetriebAnsprechpartner[]> = computed(() => {
		if (props.listSchuelerbetriebe.ausgewaehlt === undefined)
			return []
		const id = props.listSchuelerbetriebe.ausgewaehlt.betrieb_id;
		return props.listSchuelerbetriebe.betriebansprechpartner.liste.filter(l => l.betrieb_id === id);
	})

	const inputKatalogOrte: ComputedRef<List<OrtKatalogEintrag>> = computed(() => {
		return main.kataloge.orte;
	});

	const inputLehrerListe: ComputedRef<LehrerListeEintrag[]> = computed(() => {
		return props.listSchuelerbetriebe.lehrer.liste;
	});


	/** Basisdaten */

	const name : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.daten?.name1 ?? undefined,
		set: (value) => void props.betriebsStammdaten.patch({ name1 : value })
	})

	const namezusatz : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.daten?.name2 ?? undefined,
		set: (value) => void props.betriebsStammdaten.patch({ name2 : value })
	})

	const telefon1 : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.daten?.telefon1 ?? undefined,
		set: (value) => void props.betriebsStammdaten.patch({ telefon1 : value })
	})

	const telefon2 : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.daten?.telefon2 ?? undefined,
		set: (value) => void props.betriebsStammdaten.patch({ telefon2 : value })
	})

	const fax : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.daten?.fax ?? undefined,
		set: (value) => void props.betriebsStammdaten.patch({ fax : value })
	})

	const email : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.daten?.email ?? undefined,
		set: (value) => void props.betriebsStammdaten.patch({ email : value })
	})

	const branche : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.daten?.branche ?? undefined,
		set: (value) => void props.betriebsStammdaten.patch({ branche : value })
	})

	const ansprechpartner: WritableComputedRef<BetriebAnsprechpartner | undefined> = computed({
		get: () => props.listSchuelerbetriebe.ausgewaehlt === undefined ? undefined
			: inputBetriebAnsprechpartner.value.find(l => l.id === props.listSchuelerbetriebe.ausgewaehlt?.ansprechpartner_id),
		set: (value) => {
			if (props.listSchuelerbetriebe.ausgewaehlt === undefined)
				return;
			const data = props.listSchuelerbetriebe.ausgewaehlt as SchuelerBetriebsdaten;
			data.ansprechpartner_id = Number(value?.id);
			if (data.id === null)
				return;
			void App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id);
		}
	});

	const inputBetreuungslehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => props.listSchuelerbetriebe.ausgewaehlt === undefined ? undefined
			: inputLehrerListe.value.find(l => l.id === props.listSchuelerbetriebe.ausgewaehlt?.betreuungslehrer_id),
		set: (value) => {
			const data: SchuelerBetriebsdaten | undefined = props.listSchuelerbetriebe.ausgewaehlt;
			if ((!data) || (!data.id) || (!value))
				return;
			data.betreuungslehrer_id = Number(value?.id);
			void App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id);
		}
	});


	/** Adresse */

	const strassenname : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.daten?.strassenname ?? undefined,
		set: (value) => void props.betriebsStammdaten.patch({ strassenname : value })
	})

	const hausnummerzusatz : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.daten?.hausnrzusatz ?? undefined,
		set: (value) => void props.betriebsStammdaten.patch({ hausnrzusatz : value })
	})

	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get: () => {
			// TODO Die UI-Komponente zeigt nach Auswahl eines neuen Eintrags den Eintrag doppelt. Erst nach 10 Sekunden ist es wieder normal.
			if (props.betriebsStammdaten.daten?.ort_id) {
				const id = props.betriebsStammdaten.daten?.ort_id;
				let o;
				for (const r of inputKatalogOrte.value) {
					if (r.id === id) {
						o = r;
						break;
					}
				}
				return o;
			}
			return undefined;
		},
		set: (value) => void props.betriebsStammdaten.patch({ ort_id: value?.id })
	});


	const anschreiben: WritableComputedRef<boolean | undefined> = computed({
		get: () => {
			if (props.listSchuelerbetriebe?.ausgewaehlt) {
				const data = props.listSchuelerbetriebe.ausgewaehlt;
				return data.allgadranschreiben ?? undefined;
			}
			return undefined;
		},
		set: (value) => {
			if (props.listSchuelerbetriebe?.ausgewaehlt) {
				const data = props.listSchuelerbetriebe.ausgewaehlt as SchuelerBetriebsdaten;
				data.allgadranschreiben = Boolean(value);
				if ((!data) || (!data.id))
					return;
				void App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id);
			}
			return;
		}
	});

	const bemerkungen : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.daten?.bemerkungen ?? undefined,
		set: (value) => void props.betriebsStammdaten.patch({ bemerkungen : value })
	})

	/**
	 * Modal-Fenster-Edit-Anpsprechpartner
	 */

	const ap_name : WritableComputedRef<string | undefined> = computed({
		get: () => ansprechpartner.value?.name ?? undefined,
		set: (value) => {
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.name = String(value);
			if ((!data) || (!data.id))
				return;
			void App.api.patchBetriebanpsrechpartnerdaten(data, App.schema, data.id);
		}
	})

	const ap_vorname : WritableComputedRef<string | undefined> = computed({
		get: () => ansprechpartner.value?.vorname ?? undefined,
		set: (value) => {
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.vorname = String(value);
			if ((!data) || (!data.id))
				return;
			void App.api.patchBetriebanpsrechpartnerdaten(data, App.schema, data.id);
		}
	})

	const ap_anrede : WritableComputedRef<string | undefined> = computed({
		get: () => ansprechpartner.value?.anrede ?? undefined,
		set: (value) => {
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.anrede = String(value);
			if ((!data) || (!data.id))
				return;
			void App.api.patchBetriebanpsrechpartnerdaten(data, App.schema, data.id);
		}
	})

	const ap_telefonnr : WritableComputedRef<string | undefined> = computed({
		get: () => ansprechpartner.value?.telefon ?? undefined,
		set: (value) => {
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.telefon = String(value);
			if ((!data) || (!data.id))
				return;
			void App.api.patchBetriebanpsrechpartnerdaten(data, App.schema, data.id);
		}
	})

	const ap_email : WritableComputedRef<string | undefined> = computed({
		get: () => ansprechpartner.value?.email ?? undefined,
		set: (value) => {
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.email = String(value);
			if ((!data) || (!data.id))
				return;
			void App.api.patchBetriebanpsrechpartnerdaten(data, App.schema, data.id);
		}
	})

	const ap_abteilung : WritableComputedRef<string | undefined> = computed({
		get: () => ansprechpartner.value?.abteilung ?? undefined,
		set: (value) => {
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.abteilung = String(value);
			if ((!data) || (!data.id))
				return;
			void App.api.patchBetriebanpsrechpartnerdaten(data, App.schema, data.id);
		}
	})

	const ap_titel : WritableComputedRef<string | undefined> = computed({
		get: () => ansprechpartner.value?.titel ?? undefined,
		set: (value) => {
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.titel = String(value);
			if ((!data) || (!data.id))
				return;
			void App.api.patchBetriebanpsrechpartnerdaten(data, App.schema, data.id);
		}
	})


	/** Modalfenster-Neu-Anpsrechpartner */

	const ap_neu : BetriebAnsprechpartner = reactive(new BetriebAnsprechpartner())

	async function saveEntries(){
		const id = props.betriebsStammdaten.daten?.id ?? undefined;
		if (id === undefined)
			return;
		ap_neu.betrieb_id = props.betriebsStammdaten.daten?.id || null;
		await App.api.createBetriebansprechpartner(ap_neu, App.schema, id);
		void props.listSchuelerbetriebe.betriebansprechpartner.update_list();
		modalAdd.value.closeModal();
	}

</script>

<style scoped>

	.input-wrapper {
		@apply grid flex-grow grid-cols-1 gap-4 xl:grid-cols-2;
	}

	.entry-content {
		@apply grid flex-grow grid-cols-1 gap-4 xl:grid-cols-2;
	}

	h2 {
		@apply mb-2 text-sm font-bold;
	}

</style>
