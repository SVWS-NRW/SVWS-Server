<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Identnummer" :model-value="personaldaten.identNrTeil1" @change="identNrTeil1 => patch({identNrTeil1})" type="text" span="full" />
			<svws-ui-text-input placeholder="Seriennummer" :model-value="personaldaten.identNrTeil2SerNr" @change="identNrTeil2SerNr => patch({identNrTeil2SerNr})" type="text" />
			<svws-ui-text-input placeholder="Vergütungsschlüssel" :model-value="personaldaten.lbvVerguetungsschluessel" @change="lbvVerguetungsschluessel => patch({lbvVerguetungsschluessel})" type="text" />
			<svws-ui-text-input placeholder="PA-Nummer" :model-value="personaldaten.personalaktennummer" @change="personalaktennummer => patch({personalaktennummer})" type="text" />
			<svws-ui-text-input placeholder="LBV-Pers.Nummer" :model-value="personaldaten.lbvPersonalnummer" @change="lbvPersonalnummer => patch({lbvPersonalnummer})" type="text" />
			<svws-ui-spacing />
			<svws-ui-select title="Lehrbefähigung" v-model="lehrbefaehigung" :items="LehrerLehrbefaehigung.values()" :item-text="(i: LehrerLehrbefaehigung) => i.daten.text" required class="col-span-full" />
			<svws-ui-select title="Fachrichtung" v-model="fachrichtung" :items="LehrerFachrichtung.values()" :item-text="(i: LehrerFachrichtung) => i.daten.text" required class="col-span-full" />
			<svws-ui-text-input placeholder="Zugangsdatum" :model-value="personaldaten.zugangsdatum" @change="zugangsdatum => patch({zugangsdatum})" type="date" />
			<svws-ui-text-input placeholder="Abgangsdatum" :model-value="personaldaten.abgangsdatum" @change="abgangsdatum => patch({abgangsdatum})" type="date" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { type LehrerPersonaldaten, LehrerFachrichtung, LehrerLehrbefaehigung } from "@core";

	const props = defineProps<{
		personaldaten: LehrerPersonaldaten;
		patch: (data : Partial<LehrerPersonaldaten>) => Promise<void>;
	}>();

	const lehrbefaehigung = computed<LehrerLehrbefaehigung | undefined>({
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

	const fachrichtung = computed<LehrerFachrichtung | undefined>({
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
