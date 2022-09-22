<template>
	<svws-ui-content-card title="Text für Beratungsbögen">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-textarea-input
					v-model="inputTextBeratungsbogen"
					placeholder="Text für Beratungsbögen"
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

	const inputTextBeratungsbogen: WritableComputedRef<string | undefined> =
		computed({
			get(): string | undefined {
				return app.dataJahrgang?.daten?.textBeratungsbogen?.toString();
			},
			set(val) {
				app.dataJahrgang.patch({ textBeratungsbogen: val });
			}
		});
</script>
