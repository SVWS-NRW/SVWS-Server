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
				:item-text="(i: LehrerLehrbefaehigung) => i.daten.text" required />
			<svws-ui-multi-select title="Fachrichtung" v-model="fachrichtung" :items="LehrerFachrichtung.values()"
				:item-text="(i: LehrerFachrichtung) =>i.daten.text" required />
			<svws-ui-text-input placeholder="Zugangsdatum" v-model="inputZugangsdatum" type="date" />
			<svws-ui-text-input placeholder="Abgangsdatum" v-model="inputAbgangsdatum" type="date" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { LehrerAbgangsgrund, LehrerFachrichtung, LehrerLehrbefaehigung, LehrerPersonaldaten, LehrerZugangsgrund } from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef } from "vue";

	const props = defineProps<{
		personaldaten: LehrerPersonaldaten
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<LehrerPersonaldaten>): void;
	}>()

	function doPatch(data: Partial<LehrerPersonaldaten>) {
		emit('patch', data);
	}

	const zugangsgrund: WritableComputedRef<LehrerZugangsgrund | undefined> =
		computed({
			get(): LehrerZugangsgrund | undefined {
				return LehrerZugangsgrund.values().find(e => e.daten.kuerzel === props.personaldaten.zugangsgrund);
			},
			set(val: LehrerZugangsgrund | undefined) {
				doPatch({ zugangsgrund: val?.daten.kuerzel });
			}
		});

	const abgangsgrund: WritableComputedRef<LehrerAbgangsgrund | undefined> =
		computed({
			get(): LehrerAbgangsgrund | undefined {
				return LehrerAbgangsgrund.values().find(e => e.daten.kuerzel === props.personaldaten.abgangsgrund );
			},
			set(val: LehrerAbgangsgrund | undefined) {
				doPatch({ abgangsgrund: val?.daten.kuerzel });
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
			return LehrerFachrichtung.values().find(e => e.daten.kuerzel === kuerzel);
		},
		set(val: LehrerFachrichtung | undefined) {
			void val;
			// TODO props.personaldaten.patch({ fachrichtung: val?.kuerzel });
		}
	});

	const inputZugangsdatum: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.personaldaten.zugangsdatum ?? undefined;
		},
		set(val) {
			doPatch({ zugangsdatum: val });
		}
	});

	const inputAbgangsdatum: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.personaldaten.abgangsdatum ?? undefined;
		},
		set(val: string | undefined) {
			doPatch({ abgangsdatum: val });
		}
	});

	const inputIdentNrTeil1: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.personaldaten.identNrTeil1 ?? undefined;
		},
		set(val: string | undefined) {
			doPatch({ identNrTeil1: val });
		}
	});

	const inputIdentNrTeil2SerNr: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.personaldaten.identNrTeil2SerNr ?? undefined;
		},
		set(val: string | undefined) {
			doPatch({ identNrTeil2SerNr: val });
		}
	});

	const inputLbvVerguetungsschluessel: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.personaldaten.lbvVerguetungsschluessel ?? undefined;
		},
		set(val: string | undefined) {
			doPatch({ lbvVerguetungsschluessel: val });
		}
	});

	const inputPersonalaktennummer: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.personaldaten.personalaktennummer ?? undefined;
		},
		set(val) {
			doPatch({ personalaktennummer: val });
		}
	});

	const inputLbvPersonalnummer: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.personaldaten.lbvPersonalnummer ?? undefined;
		},
		set(val) {
			doPatch({ lbvPersonalnummer: val });
		}
	});

</script>
