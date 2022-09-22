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
					v-model="inputBezeichnung"
					type="text"
					placeholder="Bezeichnung"
				/>
				<svws-ui-multi-select
					v-model="inputFachStatistik"
					title="Statistikkürzel"
					:items="inputKatalogFaecherStatistik"
					:item-text="(i: ZulaessigesFach) => i.daten.kuerzelASD"
					required
				/>
				<svws-ui-multi-select
					v-model="inputFachStatistik"
					title="Statistikfach"
					:items="inputKatalogFaecherStatistik"
					:item-text="(i: ZulaessigesFach) => i.daten.bezeichnung"
					required
				/>
				<svws-ui-checkbox v-model="inputIstFremdsprache"
					>Fremdsprache</svws-ui-checkbox
				>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.faecher;
	const inputKatalogFaecherStatistik: ComputedRef<
		ZulaessigesFach[] | undefined
	> = computed(() => {
		// TODO Filter auf die Fächer für die Schulform der Schule
		return ZulaessigesFach.values();
	});

	const id: ComputedRef<number | undefined> = computed(() => {
		return app.dataFach.daten?.id.valueOf();
	});

	const inputKuerzel: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.dataFach.daten?.kuerzel?.toString();
		},
		set(val: string | undefined) {
			app.dataFach.patch({ kuerzel: val });
		}
	});

	const inputBezeichnung: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.dataFach.daten?.bezeichnung?.toString();
		},
		set(val: string | undefined) {
			app.dataFach.patch({ bezeichnung: val });
		}
	});

	const inputFachStatistik: WritableComputedRef<ZulaessigesFach | undefined> =
		computed({
			get(): ZulaessigesFach | undefined {
				return ZulaessigesFach.getByKuerzelASD(
					app.dataFach.daten?.kuerzelStatistik || null
				);
			},
			set(val: ZulaessigesFach | undefined) {
				app.dataFach.patch({
					kuerzelStatistik: val?.daten.kuerzelASD
				});
			}
		});

	const inputIstFremdsprache: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return true;
			// return app.dataFach.daten.istFremdsprache;
		},
		set(val: boolean) {
			val;
			// app.dataFach.update("istFremdsprache", val);
		}
	});
</script>
