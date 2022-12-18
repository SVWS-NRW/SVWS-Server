<template>
	<svws-ui-content-card title="Allgemein">
		<div class="input-wrapper">
			<div class="input-wrapper-3-cols">
				<svws-ui-text-input
					v-model="inputIdentNrTeil1"
					type="text"
					placeholder="Identnummer"
				/>
				<svws-ui-text-input
					v-model="inputIdentNrTeil2SerNr"
					type="text"
					placeholder="Seriennummer"
				/>
				<svws-ui-text-input
					v-model="inputLbvVerguetungsschluessel"
					type="text"
					placeholder="Vergütungsschlüssel"
				/>
			</div>
			<svws-ui-text-input
				v-model="inputPersonalaktennummer"
				type="text"
				placeholder="PA-Nummer"
			/>
			<svws-ui-text-input
				v-model="inputLbvPersonalnummer"
				type="text"
				placeholder="LBV-Pers.Nummer"
			/>
			<svws-ui-multi-select
				v-model="lehrbefaehigung"
				title="Lehrbefähigung"
				:items="lehrbefaehigungen_liste"
				:item-text="(i: LehrerLehrbefaehigung) =>i.daten.text"
				required
			/>
			<svws-ui-multi-select
				v-model="fachrichtung"
				title="Fachrichtung"
				:items="fachrichtungen_liste"
				:item-text="(i: LehrerFachrichtung) =>i.daten.text"
				required
			/>
			<svws-ui-text-input
				v-model="inputZugangsdatum"
				type="date"
				placeholder="Zugangsdatum"
			/>
			<svws-ui-text-input
				v-model="inputAbgangsdatum"
				type="date"
				placeholder="Abgangsdatum"
			/>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import {
		LehrerAbgangsgrund,
		LehrerFachrichtung,
		LehrerLehrbefaehigung,
		LehrerZugangsgrund
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.lehrer;

	const zugaenge_liste: ComputedRef<LehrerZugangsgrund[]> = computed(() =>
		LehrerZugangsgrund.values()
	);

	const abgaenge_liste: ComputedRef<LehrerAbgangsgrund[]> = computed(() =>
		LehrerAbgangsgrund.values()
	);

	const lehrbefaehigungen_liste: ComputedRef<LehrerLehrbefaehigung[]> =
		computed(() => LehrerLehrbefaehigung.values());

	const fachrichtungen_liste: ComputedRef<LehrerFachrichtung[]> = computed(
		() => LehrerFachrichtung.values()
	);

	const zugangsgrund: WritableComputedRef<LehrerZugangsgrund | undefined> =
		computed({
			get(): LehrerZugangsgrund | undefined {
				const kuerzel = app.personaldaten?.daten?.zugangsgrund;
				return zugaenge_liste.value.find((e: LehrerZugangsgrund) => {
					return e.daten.kuerzel === kuerzel;
				});
			},
			set(val: LehrerZugangsgrund | undefined) {
				app.personaldaten.patch({
					zugangsgrund: val?.daten.kuerzel
				});
			}
		});

	const abgangsgrund: WritableComputedRef<LehrerAbgangsgrund | undefined> =
		computed({
			get(): LehrerAbgangsgrund | undefined {
				const kuerzel = app.personaldaten?.daten?.abgangsgrund;
				return abgaenge_liste.value.find(
					e => e.daten.kuerzel === kuerzel
				);
			},
			set(val: LehrerAbgangsgrund | undefined) {
				app.personaldaten.patch({
					abgangsgrund: val?.daten.kuerzel
				});
			}
		});

	const lehrbefaehigung: WritableComputedRef<
		LehrerLehrbefaehigung | undefined
	> = computed({
		get(): LehrerLehrbefaehigung | undefined {
			// TODO aus Personaldaten bestimmten
			const kuerzel = undefined;
			return lehrbefaehigungen_liste.value.find(
				e => e.daten.kuerzel === kuerzel
			);
		},
		set(val: LehrerLehrbefaehigung | undefined) {
			void val;
			// TODO
			// this.app.personaldaten.patch({
			//	lehrbefaehigung: val?.kuerzel
			// });
		}
	});

	const fachrichtung: WritableComputedRef<LehrerFachrichtung | undefined> =
		computed({
			get(): LehrerFachrichtung | undefined {
				// TODO aus Personaldaten bestimmten
				const kuerzel = undefined;
				return fachrichtungen_liste.value.find(
					e => String(e.daten.kuerzel) === kuerzel
				);
			},
			set(val: LehrerFachrichtung | undefined) {
				void val;
				// TODO
				// this.app.personaldaten.patch({
				//	fachrichtung: val?.kuerzel
				// });
			}
		});

	const inputZugangsdatum: WritableComputedRef<string | undefined> = computed(
		{
			get(): string | undefined {
				return app.personaldaten.daten?.zugangsdatum?.toString();
			},
			set(val) {
				app.personaldaten.patch({ zugangsdatum: val });
			}
		}
	);

	const inputAbgangsdatum: WritableComputedRef<string | undefined> = computed(
		{
			get(): string | undefined {
				return app.personaldaten.daten?.abgangsdatum?.toString();
			},
			set(val: string | undefined) {
				app.personaldaten.patch({ abgangsdatum: val });
			}
		}
	);

	const inputIdentNrTeil1: WritableComputedRef<string | undefined> = computed(
		{
			get(): string | undefined {
				return app.personaldaten.daten?.identNrTeil1?.toString();
			},
			set(val: string | undefined) {
				app.personaldaten.patch({ identNrTeil1: val });
			}
		}
	);

	const inputIdentNrTeil2SerNr: WritableComputedRef<string | undefined> =
		computed({
			get(): string | undefined {
				return app.personaldaten.daten?.identNrTeil2SerNr?.toString();
			},
			set(val: string | undefined) {
				app.personaldaten.patch({ identNrTeil2SerNr: val });
			}
		});

	const inputLbvVerguetungsschluessel: WritableComputedRef<
		string | undefined
	> = computed({
		get(): string | undefined {
			return app.personaldaten.daten?.lbvVerguetungsschluessel?.toString();
		},
		set(val: string | undefined) {
			app.personaldaten.patch({
				lbvVerguetungsschluessel: val
			});
		}
	});

	const inputPersonalaktennummer: WritableComputedRef<string | undefined> =
		computed({
			get(): string | undefined {
				return app.personaldaten.daten?.personalaktennummer?.toString();
			},
			set(val) {
				app.personaldaten.patch({ personalaktennummer: val });
			}
		});

	const inputLbvPersonalnummer: WritableComputedRef<string | undefined> =
		computed({
			get(): string | undefined {
				return app.personaldaten.daten?.lbvPersonalnummer?.toString();
			},
			set(val) {
				app.personaldaten.patch({ lbvPersonalnummer: val });
			}
		});
</script>
