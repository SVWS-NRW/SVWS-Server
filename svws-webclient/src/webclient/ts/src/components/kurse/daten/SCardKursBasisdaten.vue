<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input
					v-model="inputKuerzel"
					type="text"
					placeholder="Kürzel"
				/>
				<svws-ui-text-input
					v-model="inputIdSchuljahresabschnitt"
					type="text"
					placeholder="Schuljahresabschnitt"
				/>
				<svws-ui-multi-select
					v-model="inputIdJahrgaenge"
					tags
					title="Jahrgaenge"
					:items="listJahrgaenge"
					:item-text="(item: Jahrgaenge) => item?.daten.kuerzel || ''"
				/>
				<svws-ui-text-input
					v-model="inputIdFach"
					type="number"
					placeholder="Id Fach"
				/>
				<svws-ui-multi-select
					v-model="inputLehrer"
					:items="listLehrer"
					:item-text="(item:LehrerListeEintrag) => item.kuerzel"
					title="Lehrer"
				/>
				<svws-ui-text-input
					v-model="inputSortierung"
					type="number"
					placeholder="Sortierung"
				/>
				<svws-ui-checkbox v-model="inputIstSichtbar"
					>Ist sichtbar</svws-ui-checkbox
				>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { Jahrgaenge, JahrgangsListeEintrag, LehrerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.kurse;
	const appLehrer = main.apps.lehrer;
	const schuljahresabschnitte = main.apps.jahrgaenge.auswahl.liste;

	const listJahrgaenge: ComputedRef<Jahrgaenge[]> = computed(() => Jahrgaenge.values());

	const id: ComputedRef<number | undefined> = computed(() => {
		return app.auswahl.ausgewaehlt?.id.valueOf();
	});

	const inputIdSchuljahresabschnitt: WritableComputedRef<number | undefined> = computed({
		get(): number | undefined {
			const schuljahrAbschnitt = schuljahresabschnitte.find((sa: JahrgangsListeEintrag) => {
				return sa.id === app.auswahl.ausgewaehlt?.idSchuljahresabschnitt;
			});
			return app.auswahl.ausgewaehlt?.idSchuljahresabschnitt;
		},
		set(val: number | undefined) {
			app.kursdaten.patch({idSchuljahresabschnitt: val});
		}
	});

	const inputKuerzel: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.auswahl.ausgewaehlt?.kuerzel?.toString();
		},
		set(val: string | undefined) {
			app.kursdaten.patch({ kuerzel: val });
		}
	});

const inputIdJahrgaenge: WritableComputedRef<Jahrgaenge[] | undefined> =
	computed({
		get(): Jahrgaenge[] | undefined {
			const selection: Jahrgaenge[] = [];
			app.auswahl.ausgewaehlt?.idJahrgaenge.toArray().forEach((id: unknown) => {
					const jahrgang = listJahrgaenge.value.find((j: Jahrgaenge) => {
						return Number(id) === j.daten.id;
					});
					if(jahrgang) selection.push(jahrgang);
			});
			return selection;
		},
		set(val: Jahrgaenge[] | undefined) {
			const ids: Vector<Number> = new Vector();
			// val?.forEach((jahrgang: Jahrgaenge) => {
			// 	ids.add(jahrgang.daten.id);
			// });
			app.kursdaten.patch({idJahrgaenge: new Vector(ids)});
		}
	});

	// const inputJahrgaenge: WritableComputedRef<Jahrgaenge[] | undefined> =
	// 	computed({
	// 		get(): Jahrgaeng[] | undefined {
	// 			const jahrgaenge: Jahrgaenge[] = [];
	// 			app.auswahl.ausgewaehlt?.idJahrgaenge.forEach((i: Number) => {
	// 			jahrgaenge.push(jahrgaengeList.value.find((e: Jahrgaenge) => {
	// 				return e.daten.id === i;
	// 			}))
	// 		})
	// 		},
	// 		set(val: Jahrgaenge[] | undefined) {
	// 			app.klassendaten.patch({idJahrgaenge: val?.daten.id});
	// 		}
	// 	});

const inputIdFach: WritableComputedRef<number | undefined> =
	computed({
		get(): number | undefined {
			return app.auswahl.ausgewaehlt?.idFach;
		},
		set(val: number | undefined) {
			app.kursdaten.patch({idFach: val});
		}
	});

const inputLehrer: WritableComputedRef<LehrerListeEintrag | undefined> =
	computed({
		get(): LehrerListeEintrag | undefined {
			const lehrer = appLehrer.auswahl.liste.find((l: LehrerListeEintrag) => {
				return l.id === app.auswahl.ausgewaehlt?.lehrer;
			})
			return lehrer;
		},
		set(val: LehrerListeEintrag | undefined) {
			app.kursdaten.patch({lehrer: val?.id});
		}
	});

	const inputIstSichtbar: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined {
			return app.auswahl.ausgewaehlt?.istSichtbar;
		},
		set(val: boolean | undefined) {
			app.kursdaten.patch({istSichtbar: val});
		}
	});

	const inputSortierung: WritableComputedRef<number | undefined> = computed({
		get(): number | undefined {
			return app.auswahl.ausgewaehlt?.sortierung;
		},
		set(val: number | undefined) {
			app.kursdaten.patch({sortierung: val});
		}
	});

	const listLehrer: ComputedRef<LehrerListeEintrag[]> = computed(() => {
		return appLehrer.auswahl.liste;
	});
</script>
