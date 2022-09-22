<template>
	<svws-ui-content-card title="Beschäftigungsdaten">
		<div class="input-wrapper">
			<svws-ui-multi-select
				v-model="rechtsverhaeltnis"
				title="Rechtsverhältnis"
				:items="rechtsverhaeltnisse_liste"
				:item-text="(i: LehrerRechtsverhaeltnis) =>i.daten.text"
			/>
			<svws-ui-multi-select
				v-model="beschaeftigungsart"
				title="Beschäftigungsart"
				:items="beschaeftigungsarten_liste"
				:item-text="(i: LehrerBeschaeftigungsart) =>i.daten.text"
			/>
			<svws-ui-text-input
				v-model="pflichtstundensoll"
				type="text"
				placeholder="Pflichtstundensoll"
			/>
			<svws-ui-multi-select
				v-model="einsatzstatus"
				title="Einsatzstatus"
				:items="einsatzstatus_liste"
				:item-text="(i: LehrerEinsatzstatus) =>i.daten.text"
			/>
			<svws-ui-text-input
				v-model="stammschulnummer"
				type="text"
				placeholder="Stammschule"
			/>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import {
		LehrerBeschaeftigungsart,
		LehrerEinsatzstatus,
		LehrerRechtsverhaeltnis
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.lehrer;

	const rechtsverhaeltnisse_liste: ComputedRef<LehrerRechtsverhaeltnis[]> =
		computed(() => {
			return LehrerRechtsverhaeltnis.values();
		});

	const beschaeftigungsarten_liste: ComputedRef<LehrerBeschaeftigungsart[]> =
		computed(() => {
			return LehrerBeschaeftigungsart.values();
		});

	const einsatzstatus_liste: ComputedRef<LehrerEinsatzstatus[]> = computed(
		() => {
			return LehrerEinsatzstatus.values();
		}
	);

	const rechtsverhaeltnis: WritableComputedRef<
		LehrerRechtsverhaeltnis | undefined
	> = computed({
		get(): LehrerRechtsverhaeltnis | undefined {
			const kuerzel = app.personaldaten?.daten?.rechtsverhaeltnis;
			return rechtsverhaeltnisse_liste.value.find(
				r => r.daten.kuerzel === kuerzel
			);
		},
		set(val: LehrerRechtsverhaeltnis | undefined) {
			app.personaldaten.patch({
				rechtsverhaeltnis: val?.daten.kuerzel
			});
		}
	});

	const beschaeftigungsart: WritableComputedRef<
		LehrerBeschaeftigungsart | undefined
	> = computed({
		get(): LehrerBeschaeftigungsart | undefined {
			const kuerzel = app.personaldaten?.daten?.beschaeftigungsart;
			return beschaeftigungsarten_liste.value.find(
				r => r.daten.kuerzel === kuerzel
			);
		},
		set(val: LehrerBeschaeftigungsart | undefined) {
			app.personaldaten.patch({
				beschaeftigungsart: val?.daten.kuerzel
			});
		}
	});

	const einsatzstatus: WritableComputedRef<LehrerEinsatzstatus | undefined> =
		computed({
			get(): LehrerEinsatzstatus | undefined {
				const kuerzel = app.personaldaten?.daten?.einsatzstatus;
				return einsatzstatus_liste.value.find(
					r => r.daten.kuerzel === kuerzel
				);
			},
			set(val: LehrerEinsatzstatus | undefined) {
				app.personaldaten.patch({
					einsatzstatus: val?.daten.kuerzel
				});
			}
		});

	const pflichtstundensoll: WritableComputedRef<number | undefined> =
		computed({
			get(): number | undefined {
				return app.personaldaten?.daten?.pflichtstundensoll?.valueOf();
			},
			set(val: number | undefined) {
				app.personaldaten.patch({ pflichtstundensoll: val });
			}
		});

	const stammschulnummer: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.personaldaten?.daten?.stammschulnummer?.toString();
		},
		set(val: string | undefined) {
			app.personaldaten.patch({ stammschulnummer: val });
		}
	});
</script>
