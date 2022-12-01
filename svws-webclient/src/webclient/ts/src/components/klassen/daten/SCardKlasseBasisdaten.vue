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
					v-model="inputParallelitaet"
					type="text"
					placeholder="Parallelität"
				/>
				<svws-ui-text-input
					v-model="inputSortierung"
					type="text"
					placeholder="Sirtierung"
				/>
				<svws-ui-multi-select
					v-model="inputJahrgang"
					title="Jahrgang"
					:items="jahrgaengeList"
					:item-text="(item: Jahrgaenge) => item.daten.kuerzel.toString() ||''"
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

	import { Jahrgaenge, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.klassen;
	const appLehrer = main.apps.lehrer;
	// const inputKatalogFaecherStatistik: ComputedRef<
	// 	ZulaessigesFach[] | undefined
	// > = computed(() => {
	// 	// TODO Filter auf die Fächer für die Schulform der Schule
	// 	return ZulaessigesFach.values();
	// });

	const id: ComputedRef<number | undefined> = computed(() => {
		return app.auswahl.ausgewaehlt?.id.valueOf();
	});

	const jahrgaengeList: ComputedRef<Jahrgaenge[]> = computed(() => Jahrgaenge.values());

	const inputKuerzel: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.auswahl.ausgewaehlt?.kuerzel?.toString();
		},
		set(val: string | undefined) {
			app.klassendaten.patch({ kuerzel: val });
		}
	});

	const inputParallelitaet: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.auswahl.ausgewaehlt?.parallelitaet?.toString();
		},
		set(val: string | undefined) {
			app.klassendaten.patch({ parallelitaet: val });
		}
	});

	const inputJahrgang: WritableComputedRef<Jahrgaenge | undefined> =
		computed({
			get(): Jahrgaenge | undefined {
				return jahrgaengeList.value.find((e: Jahrgaenge) => {
					return e.daten.id === app.auswahl.ausgewaehlt?.idJahrgang;
				});
			},
			set(val: Jahrgaenge | undefined) {
				app.klassendaten.patch({idJahrgang: val?.daten.id});
			}
		});

	const inputIstSichtbar: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined {
			return app.auswahl.ausgewaehlt?.istSichtbar;
		},
		set(val: boolean | undefined) {
			app.klassendaten.patch({istSichtbar: val});
		}
	});

	const inputSortierung: WritableComputedRef<number | undefined> = computed({
		get(): number | undefined {
			return app.auswahl.ausgewaehlt?.sortierung;
		},
		set(val: number | undefined) {
			app.klassendaten.patch({sortierung: val});
		}
	});

	const listLehrer: ComputedRef<LehrerListeEintrag[]> = computed(() => {
		return appLehrer.auswahl.liste;
	});

	// const inputKlassenleitungen: WritableComputedRef<LehrerListeEintrag[] | undefined> = computed({
	// 	get(): LehrerListeEintrag[] | undefined {
	// 		let results: LehrerListeEintrag[] = [];
	// 		app.auswahl.ausgewaehlt?.klassenLehrer?.forEach((i: Number | null) => {
	// 			const lehrer: LehrerListeEintrag | undefined = listLehrer.value.find((l: LehrerListeEintrag) => {return l.id === i});
	// 			if(lehrer) results.push(lehrer);
	// 		});
	// 		return results;
	// 	},
	// 	set(val: LehrerListeEintrag[] | undefined) {

	// 	}
	// });
</script>
