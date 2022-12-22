<template>
	<svws-ui-content-card class="lg:col-span-2 4xl:col-span-3 mt-auto pt-8">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="Bemerkungen" v-model="inputBemerkungen" resizeable="vertical" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const inputBemerkungen: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.stammdaten.daten?.bemerkungen?.toString() || undefined;
		},
		set(val) {
			app.stammdaten.patch({ bemerkungen: val });
		}
	});

</script>
