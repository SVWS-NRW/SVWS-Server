<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Identnummer" :model-value="data.identNrTeil1" @blur="identNrTeil1=>doPatch({identNrTeil1})" type="text" span="full" />
			<svws-ui-text-input placeholder="Seriennummer" :model-value="data.identNrTeil2SerNr" @blur="identNrTeil2SerNr=>doPatch({identNrTeil2SerNr})" type="text" />
			<svws-ui-text-input placeholder="Vergütungsschlüssel" :model-value="data.lbvVerguetungsschluessel" @blur="lbvVerguetungsschluessel=>doPatch({lbvVerguetungsschluessel})" type="text" />
			<svws-ui-text-input placeholder="PA-Nummer" :model-value="data.personalaktennummer" @blur="personalaktennummer=>doPatch({personalaktennummer})" type="text" />
			<svws-ui-text-input placeholder="LBV-Pers.Nummer" :model-value="data.lbvPersonalnummer" @blur="lbvPersonalnummer=>doPatch({lbvPersonalnummer})" type="text" />
			<svws-ui-spacing />
			<svws-ui-multi-select title="Lehrbefähigung" v-model="lehrbefaehigung" :items="LehrerLehrbefaehigung.values()" :item-text="(i: LehrerLehrbefaehigung) => i.daten.text" required span="full" />
			<svws-ui-multi-select title="Fachrichtung" v-model="fachrichtung" :items="LehrerFachrichtung.values()" :item-text="(i: LehrerFachrichtung) =>i.daten.text" required span="full" />
			<svws-ui-text-input placeholder="Zugangsdatum" :model-value="data.zugangsdatum" @blur="zugangsdatum=>doPatch({zugangsdatum})" type="date" />
			<svws-ui-text-input placeholder="Abgangsdatum" :model-value="data.abgangsdatum" @blur="abgangsdatum=>doPatch({abgangsdatum})" type="date" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { LehrerPersonaldaten} from "@core";
	import type { WritableComputedRef } from "vue";
	import { LehrerFachrichtung, LehrerLehrbefaehigung } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		data: LehrerPersonaldaten
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
