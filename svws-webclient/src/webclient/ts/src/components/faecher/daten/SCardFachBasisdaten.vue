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
				<svws-ui-text-input
					v-model="inputFachStatistik"
					placeholder="Bezeichnung in Statistik"
					type="text"
				/>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.faecher;

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

	const inputFachStatistik: WritableComputedRef<string | undefined> =
		computed({
			get(): string | undefined {
					return app.dataFach.daten?.kuerzelStatistik?.toString();
			},
			set(val: string | undefined) {
				app.dataFach.patch({
					kuerzelStatistik: val
				});
			}
		});
</script>
