<template>
	<svws-ui-content-card title="Allgemein">
		<div class="input-wrapper">
			<div class="input-wrapper-3-cols">
				<svws-ui-text-input placeholder="Identnummer" v-model="inputIdentNrTeil1" type="text" />
				<svws-ui-text-input placeholder="Seriennummer" v-model="inputIdentNrTeil2SerNr" type="text" />
				<svws-ui-text-input placeholder="Vergütungsschlüssel" v-model="inputLbvVerguetungsschluessel" type="text" />
			</div>
			<svws-ui-text-input placeholder="PA-Nummer" v-model="inputPersonalaktennummer" type="text" />
			<svws-ui-text-input placeholder="LBV-Pers.Nummer" v-model="inputLbvPersonalnummer" type="text" />
			<svws-ui-multi-select title="Lehrbefähigung" v-model="lehrbefaehigung" :items="LehrerLehrbefaehigung.values()" 
				:item-text="(i: LehrerLehrbefaehigung) => i.daten.text.toString()" required />
			<svws-ui-multi-select title="Fachrichtung" v-model="fachrichtung" :items="LehrerFachrichtung.values()"
				:item-text="(i: LehrerFachrichtung) =>i.daten.text.toString()" required />
			<svws-ui-text-input placeholder="Zugangsdatum" v-model="inputZugangsdatum" type="date" />
			<svws-ui-text-input placeholder="Abgangsdatum" v-model="inputAbgangsdatum" type="date" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { LehrerAbgangsgrund, LehrerFachrichtung, LehrerLehrbefaehigung, LehrerPersonaldaten, LehrerZugangsgrund } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { DataLehrerPersonaldaten } from "~/apps/lehrer/DataLehrerPersonaldaten";

	const props = defineProps<{ personaldaten: DataLehrerPersonaldaten }>();

	const daten: ComputedRef<LehrerPersonaldaten> = computed(() => props.personaldaten.daten || new LehrerPersonaldaten());

	const zugangsgrund: WritableComputedRef<LehrerZugangsgrund | undefined> =
		computed({
			get(): LehrerZugangsgrund | undefined {
				return LehrerZugangsgrund.values().find(e => e.daten.kuerzel === daten.value.zugangsgrund);
			},
			set(val: LehrerZugangsgrund | undefined) {
				props.personaldaten.patch({ zugangsgrund: val?.daten.kuerzel });
			}
		});

	const abgangsgrund: WritableComputedRef<LehrerAbgangsgrund | undefined> =
		computed({
			get(): LehrerAbgangsgrund | undefined {
				return LehrerAbgangsgrund.values().find(e => e.daten.kuerzel === daten.value.abgangsgrund );
			},
			set(val: LehrerAbgangsgrund | undefined) {
				props.personaldaten.patch({ abgangsgrund: val?.daten.kuerzel });
			}
		});

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
			return LehrerFachrichtung.values().find(e => String(e.daten.kuerzel) === kuerzel);
		},
		set(val: LehrerFachrichtung | undefined) {
			void val;
			// TODO props.personaldaten.patch({ fachrichtung: val?.kuerzel });
		}
	});

	const inputZugangsdatum: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.zugangsdatum?.toString();
		},
		set(val) {
			props.personaldaten.patch({ zugangsdatum: val });
		}
	});

	const inputAbgangsdatum: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.abgangsdatum?.toString();
		},
		set(val: string | undefined) {
			props.personaldaten.patch({ abgangsdatum: val });
		}
	});

	const inputIdentNrTeil1: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.identNrTeil1?.toString();
		},
		set(val: string | undefined) {
			props.personaldaten.patch({ identNrTeil1: val });
		}
	});

	const inputIdentNrTeil2SerNr: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.identNrTeil2SerNr?.toString();
		},
		set(val: string | undefined) {
			props.personaldaten.patch({ identNrTeil2SerNr: val });
		}
	});

	const inputLbvVerguetungsschluessel: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.lbvVerguetungsschluessel?.toString();
		},
		set(val: string | undefined) {
			props.personaldaten.patch({ lbvVerguetungsschluessel: val });
		}
	});

	const inputPersonalaktennummer: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.personalaktennummer?.toString();
		},
		set(val) {
			props.personaldaten.patch({ personalaktennummer: val });
		}
	});

	const inputLbvPersonalnummer: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.lbvPersonalnummer?.toString();
		},
		set(val) {
			props.personaldaten.patch({ lbvPersonalnummer: val });
		}
	});

</script>
