<template>
	<td>
		<svws-ui-multi-select title="Betrieb" v-model="inputBetrieb" :items="inputBetriebListe" :item-text="(i: BetriebListeEintrag) => i.name1?.toString() || ''" />
	</td>
	<td>
		<svws-ui-text-input placeholder="Ausbilder" v-model="ausbilder" type="text" />
	</td>
	<td>
		<svws-ui-multi-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="inputBeschaeftigungsarten" :item-text="(i: KatalogEintrag) => i.text?.toString() || ''" />
	</td>
	<td>
		<svws-ui-text-input placeholder="Vertragsbeginn" v-model="vertragsbeginn" type="date" />
	</td>
	<td>
		<svws-ui-text-input placeholder="Vertragsende" v-model="vertragsende" type="date" />
	</td>
	<td>
		<svws-ui-checkbox v-model="praktikum" />
	</td>
	<!--
	<td>
		<svws-ui-multi-select title="Betreuungslehrer" v-model="inputBetreuungslehrer" :items="inputLehrerListe" :item-text="(i: LehrerListeEintrag) => i.nachname" />
	</td>
	<td>
		<svws-ui-multi-select v-if="inputBetriebAnsprechpartner.length > 0" title="Ansprechpartner" v-model="ansprechpartner"
			:items="inputBetriebAnsprechpartner" :item-text="(i: BetriebAnsprechpartner) => i.name" />
		<p v-else> Kein BetriebAnsprechpartner </p>
	</td>
	<td>
		<svws-ui-checkbox v-model="anschreiben" />
	</td>
	-->
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { BetriebAnsprechpartner, BetriebListeEintrag, KatalogEintrag, LehrerListeEintrag, List, SchuelerBetriebsdaten } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { App } from "~/apps/BaseApp";
	import { ListSchuelerBetriebsdaten } from "~/apps/schueler/ListSchuelerBetriebsdaten";

	const props = defineProps<{
		betrieb: SchuelerBetriebsdaten;
		listSchuelerbetriebe : ListSchuelerBetriebsdaten;
	}>();

	const main: Main = injectMainApp();

	const inputBeschaeftigungsarten: ComputedRef<List<KatalogEintrag>> = computed(() => main.kataloge.beschaeftigungsarten);

	const inputLehrerListe: ComputedRef<LehrerListeEintrag[]> = computed(() => {
		return props.listSchuelerbetriebe.lehrer.liste || [];
	});

	const inputBetriebListe: ComputedRef<BetriebListeEintrag[]> = computed(() => {
		return props.listSchuelerbetriebe.betriebe.liste || [];
	})

	const inputBetriebAnsprechpartner: ComputedRef<BetriebAnsprechpartner[]> = computed(() => {
		return props.listSchuelerbetriebe.betriebansprechpartner.liste.filter(l => l.betrieb_id === props.betrieb.betrieb_id) || [];
	})

	const inputBetreuungslehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get(): LehrerListeEintrag | undefined {
			if (!inputLehrerListe.value)
				return undefined;
			return inputLehrerListe.value.find(l => l.id === props.betrieb.betreuungslehrer_id);
		},
		set(val: LehrerListeEintrag | undefined) {
			const data = props.listSchuelerbetriebe.ausgewaehlt;
			if (data?.id == null || val === undefined)
				return;
			data.betreuungslehrer_id = val.id;
			void App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id);
		}
	});

	const ausbilder: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.betrieb.ausbilder?.toString();
		},
		set(val: string | undefined) {
			const data = props.listSchuelerbetriebe.ausgewaehlt as SchuelerBetriebsdaten;
			data.ausbilder = String(val);
			if (data?.id == null)
				return;
			void App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id);
		}
	})

	const inputBetrieb: WritableComputedRef<BetriebListeEintrag | undefined> = computed({
		get(): BetriebListeEintrag | undefined {
			// TODO ISAK BÜYÜK  : Nach Betriebsauswahl sollte als Defaultanpsrechpartner der erste gezeigt werden.
			if (!inputBetriebListe.value)
				return undefined;
			return inputBetriebListe.value.find(l => { return l.id === props.betrieb.betrieb_id });
		},
		set(val: BetriebListeEintrag | undefined) {
			// Nach Auswahl des Betriebs werden die Ansprechpartner vom Server neugeladen.
			void props.listSchuelerbetriebe.betriebansprechpartner.update_list();
			const data: SchuelerBetriebsdaten | undefined = props.listSchuelerbetriebe.ausgewaehlt;
			if (data?.id == null || val === undefined)
				return;
			data.betrieb_id = val.id;
			void App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id);
		}
	});

	const beschaeftigungsart: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get(): KatalogEintrag | undefined {
			const id = props.betrieb.beschaeftigungsart_id;
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
			const data: SchuelerBetriebsdaten | undefined = props.listSchuelerbetriebe.ausgewaehlt;
			if (data?.id == null || val === undefined)
				return;
			data.beschaeftigungsart_id = val.id;
			void App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id);
		}
	})

	const praktikum: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined {
			return props.betrieb.praktikum ?? undefined;
		},
		set(val: boolean | undefined) {
			const data = props.listSchuelerbetriebe.ausgewaehlt as SchuelerBetriebsdaten;
			data.praktikum = Boolean(val);
			if (data?.id == null)
				return;
			void App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id);
		}
	});

	const vertragsbeginn: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.betrieb.vertragsbeginn ?? undefined;
		},
		set(val: string | undefined) {
			const data = props.listSchuelerbetriebe?.ausgewaehlt as SchuelerBetriebsdaten;
			data.vertragsbeginn = String(val);
			if (data?.id == null)
				return;
			void App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id);
		}
	})

	const vertragsende: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.betrieb.vertragsende ?? undefined;
		},
		set(val: string | undefined) {
			const data = props.listSchuelerbetriebe?.ausgewaehlt as SchuelerBetriebsdaten;
			if (val)
				data.vertragsende = val;
			if (data?.id == null)
				return;
			void App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id);
		}
	})

	const anschreiben: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined {
			return props.betrieb.allgadranschreiben ?? undefined;
		},
		set(val: boolean | undefined) {
			const data = props.listSchuelerbetriebe?.ausgewaehlt as SchuelerBetriebsdaten;
			data.allgadranschreiben = Boolean(val);
			if (data?.id == null)
				return;
			void App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id);
		}
	});

	const ansprechpartner: WritableComputedRef<BetriebAnsprechpartner | undefined> = computed({
		get(): BetriebAnsprechpartner | undefined {
			if (!inputBetriebAnsprechpartner.value)
				return;
			return inputBetriebAnsprechpartner.value.find(l => (l.id === props.betrieb.ansprechpartner_id));
		},
		set(val: BetriebAnsprechpartner | undefined) {
			const data: SchuelerBetriebsdaten | undefined = props.listSchuelerbetriebe?.ausgewaehlt;
			if (data?.id == null || val === undefined)
				return;
			data.ansprechpartner_id = val.id;
			void App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id);
		}
	});
</script>

<style scoped>

	td {
		@apply whitespace-nowrap px-1 py-2 text-sm font-medium text-gray-600
	}

</style>
