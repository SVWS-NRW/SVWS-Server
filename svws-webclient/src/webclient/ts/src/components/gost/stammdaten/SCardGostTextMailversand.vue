<template>
	<svws-ui-content-card title="Laufbahnplanung: Text für Mailversand">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-textarea-input
					v-model="inputTextMailversand"
					placeholder="Text für Mailversand"
					resizeable="vertical"
				/>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const autosize: ComputedRef<boolean> = computed(() => {
		return true;
	});

	const inputTextMailversand: WritableComputedRef<string | undefined> =
		computed({
			get(): string | undefined {
				return app.dataJahrgang.daten?.textMailversand?.toString();
			},
			set(val) {
				app.dataJahrgang.patch({ textMailversand: val });
			}
		});
</script>
