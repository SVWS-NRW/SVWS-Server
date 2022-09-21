<template>

	<svws-ui-modal ref="modalAddBetrieb" size="medium">
		<template #modalTitle>Ansprechpartner Hinzufügen</template>

		<template #modalContent>
			<div class="input-wrapper">
				<svws-ui-multi-select
					v-model="betrieb"
					title="Betrieb"
					:items="inputBetriebListe"
					:item-text="(i: BetriebListeEintrag) => i.name1"
				/>
				<svws-ui-text-input
					v-model="s_betrieb.ausbilder"
					type="text"
					placeholder="Ausbilder"
				/>
				<svws-ui-multi-select
					v-model="beschaeftigungsart"
					title="Beschäftigungsart"
					:items="inputBeschaeftigungsarten"
					:item-text="(i: KatalogEintrag) => i.text"
				/>
				<svws-ui-text-input
					v-model="s_betrieb.vertragsbeginn"
					type="date"
					placeholder="Vertragsbeginn"
				/>
				<svws-ui-text-input
					v-model="s_betrieb.vertragsende"
					type="date"
					placeholder="Vertragsende"
				/>
				<svws-ui-checkbox
					v-model="s_betrieb.praktikum"
				>Praktikum</svws-ui-checkbox>
				<svws-ui-multi-select
					v-model="betreuungslehrer"
					title="Betreuungslehrer"
					:items="inputLehrerListe"
					:item-text="(i: LehrerListeEintrag) => i.nachname"
				/>
				<svws-ui-multi-select
					v-model="ansprechpartner"
					title="Ansprechpartner"
					:items="inputBetriebAnsprechpartner"
					:item-text="(i: BetriebAnsprechpartner) => i.name"
				/>

				<svws-ui-checkbox
					v-model="s_betrieb.allgadranschreiben"
				>Anschreiben</svws-ui-checkbox>
			</div>
		</template>

		<template #modalActions>
			<svws-ui-button type="secondary" @click="modalAddBetrieb.closeModal()">
				Abbrechen
			</svws-ui-button>
			<svws-ui-button type="primary" @click="save">
				Speichern
			</svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-button @click="modalAddBetrieb.openModal()">Adresse hinzufügen</svws-ui-button>
</template>

<script setup lang="ts">
	import { BetriebAnsprechpartner, BetriebListeEintrag, KatalogEintrag, LehrerListeEintrag, List, SchuelerBetriebsdaten } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, reactive, ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { App } from "~/apps/BaseApp";
	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const modalAddBetrieb = ref();
	const s_betrieb : SchuelerBetriebsdaten = reactive(new SchuelerBetriebsdaten());
	const inputBeschaeftigungsarten: ComputedRef<List<KatalogEintrag>> = computed(() => main.kataloge.beschaeftigungsarten);

	const inputLehrerListe: ComputedRef<LehrerListeEintrag[]> = computed(() => {
		return app.listSchuelerbetriebe?.lehrer.liste || [];
	});

	const inputBetriebListe: ComputedRef<BetriebListeEintrag[]> = computed(() => {
		return app.listSchuelerbetriebe?.betriebe.liste || [];
	})

	const inputBetriebAnsprechpartner: ComputedRef<BetriebAnsprechpartner[]> = computed(() => {
		if(s_betrieb.betrieb_id)
			return app.listSchuelerbetriebe?.betriebansprechpartner.liste.filter(l => {
				return l.betrieb_id === s_betrieb.betrieb_id
			}) || [];
		return [];
	})



	const betrieb: WritableComputedRef<BetriebListeEintrag | undefined> = computed({
		get(): BetriebListeEintrag | undefined {
			if(s_betrieb.betrieb_id)
				return inputBetriebListe.value.find(l => { return l.id === s_betrieb.betrieb_id;
				});
			return undefined;
		},
		set(val: BetriebListeEintrag | undefined) {
			s_betrieb.betrieb_id = Number(val?.id);
			const ap= inputBetriebAnsprechpartner.value.filter(l => { return l.betrieb_id === s_betrieb.betrieb_id});
			if(ap.length === 1 )
				s_betrieb.ansprechpartner_id = ap[0].id;
		}
	});

	const ansprechpartner: WritableComputedRef<BetriebAnsprechpartner | undefined> = computed({
		get(): BetriebAnsprechpartner | undefined {
			return inputBetriebAnsprechpartner.value.find(l => { return l.id === s_betrieb.ansprechpartner_id}) || undefined;
		},
		set(val: BetriebAnsprechpartner | undefined) {
			s_betrieb.ansprechpartner_id = Number(val?.id);
		}
	});

	const beschaeftigungsart: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get(): KatalogEintrag | undefined {
			const id = s_betrieb.beschaeftigungsart_id;
			let o;
			for (const r of inputBeschaeftigungsarten.value) { if (r.id === id) { o = r; break } }
			return o;
		},
		set(val: KatalogEintrag | undefined) {
			s_betrieb.beschaeftigungsart_id = Number(val?.id);
		}
	})

	const betreuungslehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get(): LehrerListeEintrag | undefined {
			return inputLehrerListe.value.find(l => { return l.id === s_betrieb.betreuungslehrer_id}) || undefined;
		},
		set(val: LehrerListeEintrag | undefined) {
			s_betrieb.betreuungslehrer_id = Number(val?.id);
		}
	});

	async function save(){
		s_betrieb.schueler_id = Number(app.auswahl.ausgewaehlt?.id);
		if(!s_betrieb.betrieb_id || !s_betrieb.schueler_id){
			alert("Betrieb-ID bzw. Schuler_ID darf nicht null sein.");
		}

		else{
			await App.api.createSchuelerbetrieb(s_betrieb,App.schema,s_betrieb.schueler_id.valueOf(),s_betrieb.betrieb_id.valueOf());
			// TODO Zeitverzögerung muss her, weil das Laden der Daten schneller geht, als sie in die Datenbank geschrieben werden.
			app.listSchuelerbetriebe?.update_list(app.auswahl.ausgewaehlt?.id);
			modalAddBetrieb.value.closeModal();
		}
		// TODO Nach der Erstellung eines neuen Schülerbetriebs wird die Schülerbetriebliste nicht aktualisiert.
		//app.listSchuelerbetriebe?.update_list(app.auswahl.ausgewaehlt.id.valueOf());
		loeschen();
	}

	function loeschen(){
		s_betrieb.id = null;
		s_betrieb.allgadranschreiben = null;
		s_betrieb.ansprechpartner_id = null;
		s_betrieb.ausbilder = null;
		s_betrieb.beschaeftigungsart_id = null;
		s_betrieb.betreuungslehrer_id = null;
		s_betrieb.betrieb_id = null;
		s_betrieb.praktikum = null;
		s_betrieb.schueler_id = null;
		s_betrieb.vertragsbeginn = null;
		s_betrieb.vertragsende = null;
	}
</script>
