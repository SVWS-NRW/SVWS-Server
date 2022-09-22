<template>
	<td>
		<svws-ui-multi-select 
			v-model="inputBetrieb" 
			title="Betrieb" 
			:items="inputBetriebListe"
			:item-text="(i: BetriebListeEintrag) => i.name1" 
		/>
	</td>
	<td>
		<svws-ui-text-input 
			v-model="ausbilder" 
			type="text" 
			placeholder="Ausbilder" 
		/>
	</td>
	<td>
		<svws-ui-multi-select 
			v-model="beschaeftigungsart" 
			title="Beschäftigungsart" 
			:items="inputBeschaeftigungsarten"
			:item-text="(i: KatalogEintrag) => i.text" 
		/>
	</td>
	<td>
		<svws-ui-text-input 
			v-model="vertragsbeginn" 
			type="date" 
			placeholder="Vertragsbeginn" 
		/>
	</td>
	<td>
		<svws-ui-text-input 
			v-model="vertragsende" 
			type="date" 
			placeholder="Vertragsende" 
		/>
	</td>
	<td>
		<svws-ui-checkbox 
			v-model="praktikum" 
		/>
	</td>
	
	<!--
	<td>
		<svws-ui-multi-select 
			v-model="inputBetreuungslehrer" 
			title="Betreuungslehrer" 
			:items="inputLehrerListe"
			:item-text="(i: LehrerListeEintrag) => i.nachname" 
		/>
	</td>
	<td>
		<svws-ui-multi-select
			v-if="inputBetriebAnsprechpartner.length > 0"
			v-model="ansprechpartner" 
			title="Ansprechpartner" 
			:items="inputBetriebAnsprechpartner"
			:item-text="(i: BetriebAnsprechpartner) => i.name" 
		/>
		<p v-else>Kein BetriebAnsprechpartner</p>
	</td>
	
	<td>
		<svws-ui-checkbox 
			v-model="anschreiben"
		/>
	</td>
	-->
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { BetriebAnsprechpartner, BetriebListeEintrag, KatalogEintrag, LehrerListeEintrag, SchuelerBetriebsdaten } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { App } from "~/apps/BaseApp";

	const props = defineProps({
		betrieb: {
			type: SchuelerBetriebsdaten,
			default: null
		}
	})

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const inputBeschaeftigungsarten: ComputedRef<KatalogEintrag[]> = computed(() => main.kataloge.beschaeftigungsarten);

	const inputLehrerListe: ComputedRef<LehrerListeEintrag[]> = computed(() => {
		return app.listSchuelerbetriebe?.lehrer.liste || [];
	});

	const inputBetriebListe: ComputedRef<BetriebListeEintrag[]> = computed(() => {
		return app.listSchuelerbetriebe?.betriebe.liste || [];
	})

	const inputBetriebAnsprechpartner: ComputedRef<BetriebAnsprechpartner[]> = computed(() => {
		return app.listSchuelerbetriebe?.betriebansprechpartner.liste.filter(l => {return l.betrieb_id === props.betrieb.betrieb_id}) || [];
	})

	const inputBetreuungslehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get(): LehrerListeEintrag | undefined {
			if (!inputLehrerListe.value)
				return undefined;
			return inputLehrerListe.value.find(l => { return l.id === props.betrieb.betreuungslehrer_id });
		},
		set(val: LehrerListeEintrag | undefined) {
			const data: SchuelerBetriebsdaten | undefined = app.listSchuelerbetriebe?.ausgewaehlt;
			if ((!data) || (!data.id) || (!val))
				return;
			data.betreuungslehrer_id = val?.id;
			App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id.valueOf()).then();
		}
	});

	const ausbilder: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.betrieb.ausbilder?.toString();
		},
		set(val: string | undefined) {
			const data = app.listSchuelerbetriebe?.ausgewaehlt as SchuelerBetriebsdaten;
			data.ausbilder = String(val);
			if ((!data) || (!data.id))
				return;
			App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id.valueOf());
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
			//Nach Auswahl des Betriebs werden die Ansprechpartner vom Server neugeladen.
			app.listSchuelerbetriebe?.betriebansprechpartner.update_list();
			const data: SchuelerBetriebsdaten | undefined = app.listSchuelerbetriebe?.ausgewaehlt;
			if ((!data) || (!data.id) || (!val))
				return;
			data.betrieb_id = val?.id;
			App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id.valueOf()).then();
		}
	});

	const beschaeftigungsart: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get(): KatalogEintrag | undefined {
			return inputBeschaeftigungsarten.value.find(l => { return l.id === props.betrieb.beschaeftigungsart_id });
		},
		set(val: KatalogEintrag | undefined) {
			const data: SchuelerBetriebsdaten | undefined = app.listSchuelerbetriebe?.ausgewaehlt;
			if ((!data) || (!data.id) || (!val))
				return;
			data.beschaeftigungsart_id = val?.id;
			App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id.valueOf()).then();
		}
	})

	const praktikum: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined {
			return props.betrieb.praktikum?.valueOf();
		},
		set(val: boolean | undefined) {
			const data = app.listSchuelerbetriebe?.ausgewaehlt as SchuelerBetriebsdaten;
			data.praktikum = Boolean(val);
			if ((!data) || (!data.id))
				return;
			App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id.valueOf());
		}
	});

	const vertragsbeginn: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.betrieb.vertragsbeginn?.toString();
		},
		set(val: string | undefined) {
			const data = app.listSchuelerbetriebe?.ausgewaehlt as SchuelerBetriebsdaten;
			data.vertragsbeginn = String(val);
			if ((!data) || (!data.id))
				return;
			App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id.valueOf());
		}
	})

	const vertragsende: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.betrieb.vertragsende?.toString();
		},
		set(val: string | undefined) {
			const data = app.listSchuelerbetriebe?.ausgewaehlt as SchuelerBetriebsdaten;
			if (val)
				data.vertragsende = val;
			if ((!data) || (!data.id))
				return;
			App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id.valueOf());
		}
	})

	const anschreiben: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined {
			return props.betrieb.allgadranschreiben?.valueOf();
		},
		set(val: boolean | undefined) {
			const data = app.listSchuelerbetriebe?.ausgewaehlt as SchuelerBetriebsdaten;
			data.allgadranschreiben = Boolean(val);
			if ((!data) || (!data.id))
				return;
			App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id.valueOf());
		}
	});

	const ansprechpartner: WritableComputedRef<BetriebAnsprechpartner | undefined> = computed({
		get(): BetriebAnsprechpartner | undefined {
			if (!inputBetriebAnsprechpartner.value)
				return undefined;
			return inputBetriebAnsprechpartner.value.find(l => { return (l.id === props.betrieb.ansprechpartner_id) });
		},
		set(val: BetriebAnsprechpartner | undefined) {
			const data: SchuelerBetriebsdaten | undefined = app.listSchuelerbetriebe?.ausgewaehlt;
			if ((!data) || (!data.id) || (!val))
				return;
			data.ansprechpartner_id = val?.id;
			App.api.patchSchuelerBetriebsdaten(data, App.schema, data.id.valueOf()).then();
		}
	});
</script>

<style scoped>
	td {
		@apply whitespace-nowrap px-1 py-2 text-sm font-medium text-gray-600

	}
	


</style>