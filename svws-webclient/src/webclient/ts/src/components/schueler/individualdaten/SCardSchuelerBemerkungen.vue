<template>
	<svws-ui-content-card title="Bemerkungen">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-textarea-input
					v-model="inputBemerkungen"
					placeholder="Bemerkungen"
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
	const app = main.apps.schueler;

	const autosize: ComputedRef<boolean> = computed(() => {
		return true;
	});

	const inputBemerkungen: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.stammdaten.daten?.bemerkungen?.toString() || undefined;
		},
		set(val) {
			app.stammdaten.patch({ bemerkungen: val });
		}
	});
</script>
