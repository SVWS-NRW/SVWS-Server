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
								:items="inputBetriebAnsprechpartner" :item-text="(i: BetriebAnsprechpartner) => i.name" />
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
					<svws-ui-multi-select title="betreuende Lehrkraft" v-model="inputBetreuungslehrer" :items="inputLehrerListe" :item-text="(i:LehrerListeEintrag) => i.nachname" />
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
				<svws-ui-text-input placeholder="Anrede"  v-model="ap_anrede" type="text" />
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
				<svws-ui-text-input placeholder="E-Mail Adresse"  v-model="ap_neu.email" type="email" verify-email />
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

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
	const modalEdit = ref();
	const modalAdd = ref();

	/** Kataloge */

	const inputBetriebAnsprechpartner: ComputedRef<BetriebAnsprechpartner[]> = computed(() => {
		if ((app.listSchuelerbetriebe === undefined) || (app.listSchuelerbetriebe.ausgewaehlt === undefined))
			return []
		const id = app.listSchuelerbetriebe.ausgewaehlt.betrieb_id;
		return app.listSchuelerbetriebe.betriebansprechpartner.liste.filter(l => l.betrieb_id === id);
	})

	const inputKatalogOrte: ComputedRef<List<OrtKatalogEintrag>> = computed(() => {
		return main.kataloge.orte;
	});

	const inputLehrerListe: ComputedRef<LehrerListeEintrag[]> = computed(() => {
		return app.listSchuelerbetriebe?.lehrer.liste || [];
	});


	/** Basisdaten */

	const name : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.betriebsStammdaten.daten?.name1?.valueOf();
		},
		set(val: string | undefined){
			app.betriebsStammdaten.patch({ name1 : val });
		}
	})

	const namezusatz : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.betriebsStammdaten.daten?.name2?.valueOf();
		},
		set(val: string | undefined){
			app.betriebsStammdaten.patch({ name2 : val });
		}
	})

	const telefon1 : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.betriebsStammdaten.daten?.telefon1?.valueOf();
		},
		set(val: string | undefined){
			app.betriebsStammdaten.patch({ telefon1 : val });
		}
	})

	const telefon2 : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.betriebsStammdaten.daten?.telefon2?.valueOf();
		},
		set(val: string | undefined){
			app.betriebsStammdaten.patch({ telefon2 : val });
		}
	})

	const fax : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.betriebsStammdaten.daten?.fax?.valueOf();
		},
		set(val: string | undefined){
			app.betriebsStammdaten.patch({ fax : val });
		}
	})

	const email : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.betriebsStammdaten.daten?.email?.valueOf();
		},
		set(val: string | undefined){
			app.betriebsStammdaten.patch({ email : val });
		}
	})

	const branche : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.betriebsStammdaten.daten?.branche?.valueOf();
		},
		set(val: string | undefined){
			app.betriebsStammdaten.patch({ branche : val });
		}
	})

	const ansprechpartner: WritableComputedRef<BetriebAnsprechpartner | undefined> = computed({
		get(): BetriebAnsprechpartner | undefined {
			if (app.listSchuelerbetriebe?.ausgewaehlt) {
				return inputBetriebAnsprechpartner.value.find(l => l.id === app.listSchuelerbetriebe?.ausgewaehlt?.ansprechpartner_id);
			}
			return undefined;
		},
		set(val: BetriebAnsprechpartner | undefined) {
			if (app.listSchuelerbetriebe?.ausgewaehlt) {
				const data = app.listSchuelerbetriebe?.ausgewaehlt as SchuelerBetriebsdaten;
				data.ansprechpartner_id = Number(val?.id);
				if ((!data) || (!data.id))
					return;
				App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id.valueOf());
			}
			return;
		}
	});

	const inputBetreuungslehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get(): LehrerListeEintrag | undefined {
			if(app.listSchuelerbetriebe?.ausgewaehlt){
				if (!inputLehrerListe.value)
					return undefined;
				return inputLehrerListe.value.find(l => l.id === app.listSchuelerbetriebe?.ausgewaehlt?.betreuungslehrer_id);
			}
			return undefined;
		},
		set(val: LehrerListeEintrag | undefined) {
			const data: SchuelerBetriebsdaten | undefined = app.listSchuelerbetriebe?.ausgewaehlt;
			if ((!data) || (!data.id) || (!val))
				return;
			data.betreuungslehrer_id = val?.id;
			App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id.valueOf()).then();
		}
	});


	/** Adresse */

	const strassenname : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.betriebsStammdaten.daten?.strassenname?.valueOf();
		},
		set(val: string | undefined){
			app.betriebsStammdaten.patch({ strassenname : val });
		}
	})

	const hausnummerzusatz : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.betriebsStammdaten.daten?.hausnrzusatz?.valueOf();
		},
		set(val: string | undefined){
			app.betriebsStammdaten.patch({ hausnrzusatz : val });
		}
	})

	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> =computed({
			get(): OrtKatalogEintrag | undefined {
				// TODO Die UI-Komponente zeigt nach Auswahl eines neuen Eintrags den Eintrag doppelt. Erst nach 10 Sekunden ist es wieder normal.
				if (app.betriebsStammdaten.daten?.ort_id) {
					const id = app.betriebsStammdaten.daten?.ort_id;
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
			set(val: OrtKatalogEintrag | undefined) {
				app.betriebsStammdaten.patch({ ort_id: val?.id });
			}
		});


	const anschreiben: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined {
			if (app.listSchuelerbetriebe?.ausgewaehlt) {
				const data = app.listSchuelerbetriebe?.ausgewaehlt as SchuelerBetriebsdaten;
				return data.allgadranschreiben?.valueOf();
			}
			return undefined;
		},
		set(val: boolean | undefined) {
			if (app.listSchuelerbetriebe?.ausgewaehlt) {
				const data = app.listSchuelerbetriebe?.ausgewaehlt as SchuelerBetriebsdaten;
				data.allgadranschreiben = Boolean(val);
				if ((!data) || (!data.id))
					return;
				App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id.valueOf());
			}
			return;
		}
	});

	const bemerkungen : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			// TODO Der Wert wird von API richtig geliefert, jedoch wird er von UI-Komponente nicht dargestellt.
			return app.betriebsStammdaten.daten?.bemerkungen?.valueOf();
		},
		set(val: string | undefined){
			app.betriebsStammdaten.patch({ bemerkungen : val });
		}
	})

	/**
	 * Modal-Fenster-Edit-Anpsprechpartner
	 */

	const ap_name : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return ansprechpartner.value?.name?.valueOf();
		},
		set(val: string | undefined){
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.name = String(val);
			if ((!data) || (!data.id))
				return;
			App.api.patchBetriebanpsrechpartnerdaten(data, App.schema,data.id.valueOf());
		}
	})
	const ap_vorname : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return ansprechpartner.value?.vorname?.valueOf();
		},
		set(val: string | undefined){
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.vorname = String(val);
			if ((!data) || (!data.id))
				return;
			App.api.patchBetriebanpsrechpartnerdaten(data, App.schema,data.id.valueOf());
		}
	})

	const ap_anrede : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return ansprechpartner.value?.anrede?.valueOf();
		},
		set(val: string | undefined){
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.anrede = String(val);
			if ((!data) || (!data.id))
				return;
			App.api.patchBetriebanpsrechpartnerdaten(data, App.schema,data.id.valueOf());
		}
	})

	const ap_telefonnr : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return ansprechpartner.value?.telefon?.valueOf();
		},
		set(val: string | undefined){
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.telefon = String(val);
			if ((!data) || (!data.id))
				return;
			App.api.patchBetriebanpsrechpartnerdaten(data, App.schema,data.id.valueOf());
		}
	})

	const ap_email : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return ansprechpartner.value?.email?.valueOf();
		},
		set(val: string | undefined){
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.email = String(val);
			if ((!data) || (!data.id))
				return;
			App.api.patchBetriebanpsrechpartnerdaten(data, App.schema,data.id.valueOf());
		}
	})

	const ap_abteilung : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return ansprechpartner.value?.abteilung?.valueOf();
		},
		set(val: string | undefined){
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.abteilung = String(val);
			if ((!data) || (!data.id))
				return;
			App.api.patchBetriebanpsrechpartnerdaten(data, App.schema,data.id.valueOf());
		}
	})

	const ap_titel : WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return ansprechpartner.value?.titel?.valueOf();
		},
		set(val: string | undefined){
			const data = ansprechpartner.value as BetriebAnsprechpartner;
			data.titel = String(val);
			if ((!data) || (!data.id))
				return;
			App.api.patchBetriebanpsrechpartnerdaten(data, App.schema,data.id.valueOf());
		}
	})


	/** Modalfenster-Neu-Anpsrechpartner */

	const ap_neu : BetriebAnsprechpartner = reactive(new BetriebAnsprechpartner())

	async function saveEntries(){
		const id = app.betriebsStammdaten.daten?.id?.valueOf();
		if (id === undefined) 
			return;
		ap_neu.betrieb_id = app.betriebsStammdaten.daten?.id || null;
        await App.api.createBetriebansprechpartner(ap_neu,App.schema, id);
		app.listSchuelerbetriebe?.betriebansprechpartner.update_list();
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
