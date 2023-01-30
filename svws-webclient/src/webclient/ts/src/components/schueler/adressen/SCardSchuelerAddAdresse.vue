<template>
	<svws-ui-modal ref="modalAddBetrieb" size="medium">
		<template #modalTitle>Ansprechpartner Hinzufügen</template>
		<template #modalContent>
			<div class="input-wrapper">
				<svws-ui-multi-select title="Betrieb" v-model="betrieb" :items="inputBetriebListe" :item-text="(i: BetriebListeEintrag) => i.name1 ?? ''" />
				<svws-ui-text-input placeholder="Ausbilder" v-model="s_betrieb.ausbilder" type="text" />
				<svws-ui-multi-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="inputBeschaeftigungsarten" :item-text="(i: KatalogEintrag) => i.text ?? ''" />
				<svws-ui-text-input placeholder="Vertragsbeginn" v-model="s_betrieb.vertragsbeginn" type="date" />
				<svws-ui-text-input placeholder="Vertragsende" v-model="s_betrieb.vertragsende" type="date" />
				<svws-ui-checkbox v-model="s_betrieb.praktikum"> Praktikum </svws-ui-checkbox>
				<svws-ui-multi-select title="Betreuungslehrer" v-model="betreuungslehrer" :items="inputLehrerListe" :item-text="(i: LehrerListeEintrag) => i.nachname" />
				<svws-ui-multi-select title="Ansprechpartner" v-model="ansprechpartner" :items="inputBetriebAnsprechpartner" :item-text="(i: BetriebAnsprechpartner) => i.name ?? ''" />
				<svws-ui-checkbox v-model="s_betrieb.allgadranschreiben"> Anschreiben </svws-ui-checkbox>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modalAddBetrieb.closeModal()"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="save"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-button @click="modalAddBetrieb.openModal()">Adresse hinzufügen</svws-ui-button>
</template>

<script setup lang="ts">

	import { BetriebAnsprechpartner, BetriebListeEintrag, KatalogEintrag, LehrerListeEintrag, List, SchuelerBetriebsdaten, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, reactive, ref, WritableComputedRef } from "vue";
	import { App } from "~/apps/BaseApp";
	import { injectMainApp, Main } from "~/apps/Main";
	import { ListSchuelerBetriebsdaten } from "~/apps/schueler/ListSchuelerBetriebsdaten";

	const props = defineProps<{
		item?: SchuelerListeEintrag;
		listSchuelerbetriebe : ListSchuelerBetriebsdaten;
	}>();
	const main: Main = injectMainApp();

	const modalAddBetrieb = ref();
	const s_betrieb : SchuelerBetriebsdaten = reactive(new SchuelerBetriebsdaten());
	const inputBeschaeftigungsarten: ComputedRef<List<KatalogEintrag>> = computed(() => main.kataloge.beschaeftigungsarten);

	const inputLehrerListe: ComputedRef<LehrerListeEintrag[]> = computed(() => {
		return props.listSchuelerbetriebe.lehrer.liste || [];
	});

	const inputBetriebListe: ComputedRef<BetriebListeEintrag[]> = computed(() => {
		return props.listSchuelerbetriebe.betriebe.liste || [];
	})

	const inputBetriebAnsprechpartner: ComputedRef<BetriebAnsprechpartner[]> = computed(() => {
		if (s_betrieb.betrieb_id === null)
			return [];
		return props.listSchuelerbetriebe.betriebansprechpartner.liste.filter(l => l.betrieb_id === s_betrieb.betrieb_id);
	})

	const betrieb: WritableComputedRef<BetriebListeEintrag | undefined> = computed({
		get(): BetriebListeEintrag | undefined {
			return s_betrieb.betrieb_id === null ? undefined : inputBetriebListe.value.find(l => l.id === s_betrieb.betrieb_id);
		},
		set(val: BetriebListeEintrag | undefined) {
			s_betrieb.betrieb_id = val?.id ?? null;
			const ap = inputBetriebAnsprechpartner.value.filter(l => l.betrieb_id === s_betrieb.betrieb_id);
			if (ap.length === 1)
				s_betrieb.ansprechpartner_id = ap[0].id;
		}
	});

	const ansprechpartner: WritableComputedRef<BetriebAnsprechpartner | undefined> = computed({
		get(): BetriebAnsprechpartner | undefined {
			return inputBetriebAnsprechpartner.value.find(l => l.id === s_betrieb.ansprechpartner_id) || undefined;
		},
		set(val: BetriebAnsprechpartner | undefined) {
			s_betrieb.ansprechpartner_id = val?.id ?? null;
		}
	});

	const beschaeftigungsart: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get(): KatalogEintrag | undefined {
			const id = s_betrieb.beschaeftigungsart_id;
			let o;
			for (const r of inputBeschaeftigungsarten.value) {
				if (r.id === id) {
					o = r;
					break;
				}
			}
			return o;
		},
		set(val: KatalogEintrag | undefined) {
			s_betrieb.beschaeftigungsart_id = val?.id ?? null;
		}
	})

	const betreuungslehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get(): LehrerListeEintrag | undefined {
			return inputLehrerListe.value.find(l => l.id === s_betrieb.betreuungslehrer_id) || undefined;
		},
		set(val: LehrerListeEintrag | undefined) {
			s_betrieb.betreuungslehrer_id = val?.id ?? null;
		}
	});

	async function save() {
		s_betrieb.schueler_id = props.item === undefined ? null : props.item?.id;
		if (!s_betrieb.betrieb_id || !s_betrieb.schueler_id){
			alert("Betrieb-ID bzw. Schuler_ID darf nicht null sein.");
		} else {
			await App.api.createSchuelerbetrieb(s_betrieb,App.schema,s_betrieb.schueler_id, s_betrieb.betrieb_id);
			// TODO Zeitverzögerung muss her, weil das Laden der Daten schneller geht, als sie in die Datenbank geschrieben werden.
			if (props.item !== undefined)
				await props.listSchuelerbetriebe.update_list(props.item.id);
			modalAddBetrieb.value.closeModal();
		}
		// TODO Nach der Erstellung eines neuen Schülerbetriebs wird die Schülerbetriebliste nicht aktualisiert.
		loeschen();
	}

	function loeschen() {
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
