<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Identnummer" :model-value="personaldaten.identNrTeil1" @change="identNrTeil1=>doPatch({identNrTeil1})" type="text" span="full" />
			<svws-ui-text-input placeholder="Seriennummer" :model-value="personaldaten.identNrTeil2SerNr" @change="identNrTeil2SerNr=>doPatch({identNrTeil2SerNr})" type="text" />
			<svws-ui-text-input placeholder="Vergütungsschlüssel" :model-value="personaldaten.lbvVerguetungsschluessel" @change="lbvVerguetungsschluessel=>doPatch({lbvVerguetungsschluessel})" type="text" />
			<svws-ui-text-input placeholder="PA-Nummer" :model-value="personaldaten.personalaktennummer" @change="personalaktennummer=>doPatch({personalaktennummer})" type="text" />
			<svws-ui-text-input placeholder="LBV-Pers.Nummer" :model-value="personaldaten.lbvPersonalnummer" @change="lbvPersonalnummer=>doPatch({lbvPersonalnummer})" type="text" />
			<svws-ui-spacing />
			<svws-ui-multi-select title="Lehrbefähigung" v-model="lehrbefaehigung" :items="LehrerLehrbefaehigung.values()" :item-text="(i: LehrerLehrbefaehigung) => i.daten.text" required span="full" />
			<svws-ui-multi-select title="Fachrichtung" v-model="fachrichtung" :items="LehrerFachrichtung.values()" :item-text="(i: LehrerFachrichtung) =>i.daten.text" required span="full" />
			<svws-ui-text-input placeholder="Zugangsdatum" :model-value="personaldaten.zugangsdatum" @change="zugangsdatum=>doPatch({zugangsdatum})" type="date" />
			<svws-ui-text-input placeholder="Abgangsdatum" :model-value="personaldaten.abgangsdatum" @change="abgangsdatum=>doPatch({abgangsdatum})" type="date" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { LehrerPersonaldaten} from "@core";
	import type { WritableComputedRef } from "vue";
	import { LehrerFachrichtung, LehrerLehrbefaehigung } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		personaldaten: LehrerPersonaldaten
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<LehrerPersonaldaten>): void;
	}>()

	function doPatch(data: Partial<LehrerPersonaldaten>) {
		emit('patch', data);
	}

	const lehrbefaehigung: WritableComputedRef<LehrerLehrbefaehigung | undefined> = computed({
		get(): LehrerLehrbefaehigung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerLehrbefaehigung.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerLehrbefaehigung | undefined) {
			void val;
			// TODO props.personaldaten.patch({ lehrbefaehigung: val?.kuerzel });
		}
	});

	const fachrichtung: WritableComputedRef<LehrerFachrichtung | undefined> = computed({
		get(): LehrerFachrichtung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return LehrerFachrichtung.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerFachrichtung | undefined) {
			void val;
			// TODO props.personaldaten.patch({ fachrichtung: val?.kuerzel });
		}
	});


</script>
