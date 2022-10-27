<template>
	<svws-ui-content-card title="Benutzer">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input v-model="anzeigename" type="text" placeholder="Login-Name" />
				<svws-ui-text-input v-model="name" type="text" placeholder="Name" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, WritableComputedRef } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;

	const anzeigename: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.dataBenutzer.daten?.anzeigename.toString();
		},
		set(val: string | undefined) {
			console.log(val);
		}
	});

	const name: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return app.dataBenutzer.daten?.name.toString();
		},
		set(val: string | undefined) {
			console.log(val);
		}
	});

</script>
